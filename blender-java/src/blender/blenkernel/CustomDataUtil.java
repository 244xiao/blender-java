/*
* $Id: customdata.c 20741 2009-06-08 20:08:19Z blendix $
*
* ***** BEGIN GPL LICENSE BLOCK *****
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software  Foundation,
* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*
* The Original Code is Copyright (C) 2006 Blender Foundation.
* All rights reserved.
*
* The Original Code is: all of this file.
*
* Contributor(s): Ben Batt <benbatt@gmail.com>
*
* ***** END GPL LICENSE BLOCK *****
*
* Implementation of CustomData.
*
* BKE_customdata.h contains the function prototypes for this file.
*
*/
package blender.blenkernel;

import java.lang.reflect.Array;

import blender.blenlib.StringUtil;
import blender.makesdna.CustomDataTypes;
import blender.makesdna.MeshDataTypes;
import blender.makesdna.sdna.CustomData;
import blender.makesdna.sdna.CustomDataLayer;
import blender.makesdna.sdna.MCol;
import blender.makesdna.sdna.MDeformVert;
import blender.makesdna.sdna.MEdge;
import blender.makesdna.sdna.MFace;
import blender.makesdna.sdna.MFloatProperty;
import blender.makesdna.sdna.MIntProperty;
import blender.makesdna.sdna.MLoopCol;
import blender.makesdna.sdna.MLoopUV;
import blender.makesdna.sdna.MSticky;
import blender.makesdna.sdna.MStringProperty;
import blender.makesdna.sdna.MTFace;
import blender.makesdna.sdna.MTexPoly;
import blender.makesdna.sdna.MVert;
import blender.makesdna.sdna.OrigSpaceFace;
import blender.makesdna.sdna.MDisps;

public class CustomDataUtil {

    /* for ORIGINDEX layer type, indicates no original index for this element */
    public static final int ORIGINDEX_NONE = -1;

    /* initialises a CustomData object with the same layer setup as source and
     * memory space for totelem elements. mask must be an array of length
     * CD_NUMTYPES elements, that indicate if a layer can be copied. */

    /* add/copy/merge allocation types */
    public static final int CD_ASSIGN = 0;  /* use the data pointer */
    public static final int CD_CALLOC = 1;  /* allocate blank memory */
    public static final int CD_DEFAULT = 2;  /* allocate and set to default */
    public static final int CD_REFERENCE = 3;  /* use data pointers, set layer flag NOFREE */
    public static final int CD_DUPLICATE = 4;  /* do a full copy of all layers, only allowed if source
    has same number of elements */
    
    /* number of layers to add when growing a CustomData object */
    public static final int CUSTOMDATA_GROW= 5;

    /********************* Layer type information **********************/

public static class LayerTypeInfo {

        Class type;
        int size;          /* the memory size of one element of this layer's data */
        byte[] structname;  /* name of the struct used, for file writing */
        int structnum;     /* number of structs per element, for file writing */
        byte[] defaultname; /* default layer name */

        public LayerTypeInfo(Class type, int size, String structname, int structnum, String defaultname,
                Copy copy, Free free, Interp interp, Swap swap, Default set_default) {
            this.type = type;
            this.size = size;
            this.structname = structname != null ? StringUtil.toCString(structname) : null;
            this.structnum = structnum;
            this.defaultname = defaultname != null ? StringUtil.toCString(defaultname) : null;
            this.copy = copy;
            this.free = free;
            this.interp = interp;
            this.swap = swap;
            this.set_default = set_default;
        }

	/* a function to copy count elements of this layer's data
	 * (deep copy if appropriate)
	 * if NULL, memcpy is used
	 */
        public static interface Copy {
            public void exec(Object source, int offset1, Object dest, int offset2, int count);
        };

        Copy copy;

	/* a function to free any dynamically allocated components of this
	 * layer's data (note the data pointer itself should not be freed)
	 * size should be the size of one element of this layer's data (e.g.
	 * LayerTypeInfo.size)
	 */
        public static interface Free {
            public void exec(Object data, int offset, int count, int size);
        };

        Free free;

	/* a function to interpolate between count source elements of this
	 * layer's data and store the result in dest
	 * if weights == NULL or sub_weights == NULL, they should default to 1
	 *
	 * weights gives the weight for each element in sources
	 * sub_weights gives the sub-element weights for each element in sources
	 *    (there should be (sub element count)^2 weights per element)
	 * count gives the number of elements in sources
	 */
        public static interface Interp {
            public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest);
        };

        Interp interp;

        /* a function to swap the data in corners of the element */
        public static interface Swap {
            public void exec(Object data, int offset, int[] corner_indices);
        };

        Swap swap;

        /* a function to set a layer's data to default values. if NULL, the
	   default is assumed to be all zeros */
        public static interface Default {
            public void exec(Object data, int offset, int count);
        };

        Default set_default;

    };

    public static LayerTypeInfo.Copy layerCopy_mdeformvert = new LayerTypeInfo.Copy() {
        public void exec(Object source, int offset1, Object dest, int offset2, int count) {
//	int i, size = sizeof(MDeformVert);
//
//	memcpy(dest, source, count * size);
//
//	for(i = 0; i < count; ++i) {
//		MDeformVert *dvert = (MDeformVert *)((char *)dest + i * size);
//
//		if(dvert.totweight) {
//			MDeformWeight *dw = MEM_callocN(dvert.totweight * sizeof(*dw),
//											"layerCopy_mdeformvert dw");
//
//			memcpy(dw, dvert.dw, dvert.totweight * sizeof(*dw));
//			dvert.dw = dw;
//		}
//		else
//			dvert.dw = NULL;
//	}
        }
    };

    public static LayerTypeInfo.Free layerFree_mdeformvert = new LayerTypeInfo.Free() {
        public void exec(Object data, int offset, int count, int size) {
//	int i;
//
//	for(i = 0; i < count; ++i) {
//		MDeformVert *dvert = (MDeformVert *)((char *)data + i * size);
//
//		if(dvert.dw) {
//			MEM_freeN(dvert.dw);
//			dvert.dw = NULL;
//			dvert.totweight = 0;
//		}
//	}
        }
    };

//static void linklist_free_simple(void *link)
//{
//	MEM_freeN(link);
//}

    public static LayerTypeInfo.Interp layerInterp_mdeformvert = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	MDeformVert *dvert = dest;
//	LinkNode *dest_dw = NULL; /* a list of lists of MDeformWeight pointers */
//	LinkNode *node;
//	int i, j, totweight;
//
//	if(count <= 0) return;
//
//	/* build a list of unique def_nrs for dest */
//	totweight = 0;
//	for(i = 0; i < count; ++i) {
//		MDeformVert *source = sources[i];
//		float interp_weight = weights ? weights[i] : 1.0f;
//
//		for(j = 0; j < source.totweight; ++j) {
//			MDeformWeight *dw = &source.dw[j];
//
//			for(node = dest_dw; node; node = node.next) {
//				MDeformWeight *tmp_dw = (MDeformWeight *)node.link;
//
//				if(tmp_dw.def_nr == dw.def_nr) {
//					tmp_dw.weight += dw.weight * interp_weight;
//					break;
//				}
//			}
//
//			/* if this def_nr is not in the list, add it */
//			if(!node) {
//				MDeformWeight *tmp_dw = MEM_callocN(sizeof(*tmp_dw),
//				                            "layerInterp_mdeformvert tmp_dw");
//				tmp_dw.def_nr = dw.def_nr;
//				tmp_dw.weight = dw.weight * interp_weight;
//				BLI_linklist_prepend(&dest_dw, tmp_dw);
//				totweight++;
//			}
//		}
//	}
//
//	/* now we know how many unique deform weights there are, so realloc */
//	if(dvert.dw) MEM_freeN(dvert.dw);
//
//	if(totweight) {
//		dvert.dw = MEM_callocN(sizeof(*dvert.dw) * totweight,
//		                        "layerInterp_mdeformvert dvert.dw");
//		dvert.totweight = totweight;
//
//		for(i = 0, node = dest_dw; node; node = node.next, ++i)
//			dvert.dw[i] = *((MDeformWeight *)node.link);
//	}
//	else
//		memset(dvert, 0, sizeof(*dvert));
//
//	BLI_linklist_free(dest_dw, linklist_free_simple);
        }
    };

    public static LayerTypeInfo.Interp layerInterp_msticky = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	float co[2], w;
//	MSticky *mst;
//	int i;
//
//	co[0] = co[1] = 0.0f;
//	for(i = 0; i < count; i++) {
//		w = weights ? weights[i] : 1.0f;
//		mst = (MSticky*)sources[i];
//
//		co[0] += w*mst.co[0];
//		co[1] += w*mst.co[1];
//	}
//
//	mst = (MSticky*)dest;
//	mst.co[0] = co[0];
//	mst.co[1] = co[1];
        }
    };

    public static LayerTypeInfo.Copy layerCopy_tface = new LayerTypeInfo.Copy() {
        public void exec(Object source, int offset1, Object dest, int offset2, int count) {
//	const MTFace *source_tf = (const MTFace*)source;
//	MTFace *dest_tf = (MTFace*)dest;
//	int i;
//
//	for(i = 0; i < count; ++i)
//		dest_tf[i] = source_tf[i];
        }
    };

    public static LayerTypeInfo.Interp layerInterp_tface = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	MTFace *tf = dest;
//	int i, j, k;
//	float uv[4][2];
//	float *sub_weight;
//
//	if(count <= 0) return;
//
//	memset(uv, 0, sizeof(uv));
//
//	sub_weight = sub_weights;
//	for(i = 0; i < count; ++i) {
//		float weight = weights ? weights[i] : 1;
//		MTFace *src = sources[i];
//
//		for(j = 0; j < 4; ++j) {
//			if(sub_weights) {
//				for(k = 0; k < 4; ++k, ++sub_weight) {
//					float w = (*sub_weight) * weight;
//					float *tmp_uv = src.uv[k];
//
//					uv[j][0] += tmp_uv[0] * w;
//					uv[j][1] += tmp_uv[1] * w;
//				}
//			} else {
//				uv[j][0] += src.uv[j][0] * weight;
//				uv[j][1] += src.uv[j][1] * weight;
//			}
//		}
//	}
//
//	*tf = *(MTFace *)sources[0];
//	for(j = 0; j < 4; ++j) {
//		tf.uv[j][0] = uv[j][0];
//		tf.uv[j][1] = uv[j][1];
//	}
        }
    };

    private static final short[] pin_flags = {MeshDataTypes.TF_PIN1, MeshDataTypes.TF_PIN2, MeshDataTypes.TF_PIN3, MeshDataTypes.TF_PIN4};
    private static final byte[] sel_flags = {MeshDataTypes.TF_SEL1, MeshDataTypes.TF_SEL2, MeshDataTypes.TF_SEL3, MeshDataTypes.TF_SEL4};

    public static LayerTypeInfo.Swap layerSwap_tface = new LayerTypeInfo.Swap() {
        public void exec(Object data, int offset, int[] corner_indices) {
            MTFace tf = (MTFace) data;
            float[][] uv = new float[4][2];
            short unwrap = (short) (tf.unwrap & ~(MeshDataTypes.TF_PIN1 | MeshDataTypes.TF_PIN2 | MeshDataTypes.TF_PIN3 | MeshDataTypes.TF_PIN4));
            byte flag = (byte) (tf.flag & ~(MeshDataTypes.TF_SEL1 | MeshDataTypes.TF_SEL2 | MeshDataTypes.TF_SEL3 | MeshDataTypes.TF_SEL4));
            int j;

            for (j = 0; j < 4; ++j) {
                int source_index = corner_indices[j];

                uv[j][0] = tf.uv[source_index][0];
                uv[j][1] = tf.uv[source_index][1];

                // swap pinning flags around
                if ((tf.unwrap & pin_flags[source_index]) != 0) {
                    unwrap |= pin_flags[j];
                }

                // swap selection flags around
                if ((tf.flag & sel_flags[source_index]) != 0) {
                    flag |= sel_flags[j];
                }
            }

            for (int i = 0; i < 4; i++) {
                System.arraycopy(uv[i], 0, tf.uv[i], 0, 2);
            }
            tf.unwrap = unwrap;
            tf.flag = flag;
        }
    };

    public static LayerTypeInfo.Default layerDefault_tface = new LayerTypeInfo.Default() {
        public void exec(Object data, int offset, int count) {
//	static MTFace default_tf = {{{0, 0}, {1, 0}, {1, 1}, {0, 1}}, NULL,
//	                           0, 0, TF_DYNAMIC, 0, 0};
//	MTFace *tf = (MTFace*)data;
//	int i;
//
//	for(i = 0; i < count; i++)
//		tf[i] = default_tf;
        }
    };

    public static LayerTypeInfo.Copy layerCopy_origspace_face = new LayerTypeInfo.Copy() {
        public void exec(Object source, int offset1, Object dest, int offset2, int count) {
//	const OrigSpaceFace *source_tf = (const OrigSpaceFace*)source;
//	OrigSpaceFace *dest_tf = (OrigSpaceFace*)dest;
//	int i;
//
//	for(i = 0; i < count; ++i)
//		dest_tf[i] = source_tf[i];
        }
    };

    public static LayerTypeInfo.Interp layerInterp_origspace_face = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	OrigSpaceFace *osf = dest;
//	int i, j, k;
//	float uv[4][2];
//	float *sub_weight;
//
//	if(count <= 0) return;
//
//	memset(uv, 0, sizeof(uv));
//
//	sub_weight = sub_weights;
//	for(i = 0; i < count; ++i) {
//		float weight = weights ? weights[i] : 1;
//		OrigSpaceFace *src = sources[i];
//
//		for(j = 0; j < 4; ++j) {
//			if(sub_weights) {
//				for(k = 0; k < 4; ++k, ++sub_weight) {
//					float w = (*sub_weight) * weight;
//					float *tmp_uv = src.uv[k];
//
//					uv[j][0] += tmp_uv[0] * w;
//					uv[j][1] += tmp_uv[1] * w;
//				}
//			} else {
//				uv[j][0] += src.uv[j][0] * weight;
//				uv[j][1] += src.uv[j][1] * weight;
//			}
//		}
//	}
//
//	*osf = *(OrigSpaceFace *)sources[0];
//	for(j = 0; j < 4; ++j) {
//		osf.uv[j][0] = uv[j][0];
//		osf.uv[j][1] = uv[j][1];
//	}
        }
    };

    public static LayerTypeInfo.Swap layerSwap_origspace_face = new LayerTypeInfo.Swap() {
        public void exec(Object data, int offset, int[] corner_indices) {
            OrigSpaceFace osf = (OrigSpaceFace) data;
            float[][] uv = new float[4][2];
            int j;

            for (j = 0; j < 4; ++j) {
                uv[j][0] = osf.uv[corner_indices[j]][0];
                uv[j][1] = osf.uv[corner_indices[j]][1];
            }
            for (int i = 0; i < 4; i++) {
                System.arraycopy(uv[i], 0, osf.uv[i], 0, 2);
            }
        }
    };

    public static LayerTypeInfo.Default layerDefault_origspace_face = new LayerTypeInfo.Default() {
        public void exec(Object data, int offset, int count) {
//	static OrigSpaceFace default_osf = {{{0, 0}, {1, 0}, {1, 1}, {0, 1}}};
//	OrigSpaceFace *osf = (OrigSpaceFace*)data;
//	int i;
//
//	for(i = 0; i < count; i++)
//		osf[i] = default_osf;
        }
    };

///* Adapted from sculptmode.c */
//static void mdisps_bilinear(float out[3], float (*disps)[3], int st, float u, float v)
//{
//	int x, y, x2, y2;
//	const int st_max = st - 1;
//	float urat, vrat, uopp;
//	float d[4][3], d2[2][3];
//
//	if(u < 0)
//		u = 0;
//	else if(u >= st)
//		u = st_max;
//	if(v < 0)
//		v = 0;
//	else if(v >= st)
//		v = st_max;
//
//	x = floor(u);
//	y = floor(v);
//	x2 = x + 1;
//	y2 = y + 1;
//
//	if(x2 >= st) x2 = st_max;
//	if(y2 >= st) y2 = st_max;
//
//	urat = u - x;
//	vrat = v - y;
//	uopp = 1 - urat;
//
//	VecCopyf(d[0], disps[y * st + x]);
//	VecCopyf(d[1], disps[y * st + x2]);
//	VecCopyf(d[2], disps[y2 * st + x]);
//	VecCopyf(d[3], disps[y2 * st + x2]);
//	VecMulf(d[0], uopp);
//	VecMulf(d[1], urat);
//	VecMulf(d[2], uopp);
//	VecMulf(d[3], urat);
//
//	VecAddf(d2[0], d[0], d[1]);
//	VecAddf(d2[1], d[2], d[3]);
//	VecMulf(d2[0], 1 - vrat);
//	VecMulf(d2[1], vrat);
//
//	VecAddf(out, d2[0], d2[1]);
//}

public static LayerTypeInfo.Swap layerSwap_mdisps = new LayerTypeInfo.Swap() {
public void exec(Object data, int offset, int[] ci)
{
//	MDisps *s = data;
//	float (*d)[3] = NULL;
//	int x, y, st;
//
//	if(!(ci[0] == 2 && ci[1] == 3 && ci[2] == 0 && ci[3] == 1)) return;
//
//	d = MEM_callocN(sizeof(float) * 3 * s->totdisp, "mdisps swap");
//	st = sqrt(s->totdisp);
//
//	for(y = 0; y < st; ++y) {
//		for(x = 0; x < st; ++x) {
//			VecCopyf(d[(st - y - 1) * st + (st - x - 1)], s->disps[y * st + x]);
//		}
//	}
//
//	if(s->disps)
//		MEM_freeN(s->disps);
//	s->disps = d;
}};

public static LayerTypeInfo.Interp layerInterp_mdisps = new LayerTypeInfo.Interp() {
public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest)
{
//	MDisps *d = dest;
//	MDisps *s = NULL;
//	int st, stl;
//	int i, x, y;
//	float crn[4][2];
//	float (*sw)[4] = NULL;
//
//	/* Initialize the destination */
//	for(i = 0; i < d->totdisp; ++i) {
//		float z[3] = {0,0,0};
//		VecCopyf(d->disps[i], z);
//	}
//
//	/* For now, some restrictions on the input */
//	if(count != 1 || !sub_weights) return;
//
//	st = sqrt(d->totdisp);
//	stl = st - 1;
//
//	sw = (void*)sub_weights;
//	for(i = 0; i < 4; ++i) {
//		crn[i][0] = 0 * sw[i][0] + stl * sw[i][1] + stl * sw[i][2] + 0 * sw[i][3];
//		crn[i][1] = 0 * sw[i][0] + 0 * sw[i][1] + stl * sw[i][2] + stl * sw[i][3];
//	}
//
//	s = sources[0];
//	for(y = 0; y < st; ++y) {
//		for(x = 0; x < st; ++x) {
//			/* One suspects this code could be cleaner. */
//			float xl = (float)x / (st - 1);
//			float yl = (float)y / (st - 1);
//			float mid1[2] = {crn[0][0] * (1 - xl) + crn[1][0] * xl,
//					 crn[0][1] * (1 - xl) + crn[1][1] * xl};
//			float mid2[2] = {crn[3][0] * (1 - xl) + crn[2][0] * xl,
//					 crn[3][1] * (1 - xl) + crn[2][1] * xl};
//			float mid3[2] = {mid1[0] * (1 - yl) + mid2[0] * yl,
//					 mid1[1] * (1 - yl) + mid2[1] * yl};
//
//			float srcdisp[3];
//
//			mdisps_bilinear(srcdisp, s->disps, st, mid3[0], mid3[1]);
//			VecCopyf(d->disps[y * st + x], srcdisp);
//		}
//	}
}};

public static LayerTypeInfo.Copy layerCopy_mdisps = new LayerTypeInfo.Copy() {
public void exec(Object source, int offset1, Object dest, int offset2, int count)
{
//	int i;
//	const MDisps *s = source;
//	MDisps *d = dest;
//
//	for(i = 0; i < count; ++i) {
//		if(s[i].disps) {
//			d[i].disps = MEM_dupallocN(s[i].disps);
//			d[i].totdisp = s[i].totdisp;
//		}
//		else {
//			d[i].disps = NULL;
//			d[i].totdisp = 0;
//		}
//
//	}
}};

public static LayerTypeInfo.Free layerFree_mdisps = new LayerTypeInfo.Free() {
public void exec(Object data, int offset, int count, int size)
{
//	int i;
//	MDisps *d = data;
//
//	for(i = 0; i < count; ++i) {
//		if(d[i].disps)
//			MEM_freeN(d[i].disps);
//		d[i].disps = NULL;
//		d[i].totdisp = 0;
//	}
}};

    /* --------- */

    public static LayerTypeInfo.Default layerDefault_mloopcol = new LayerTypeInfo.Default() {
        public void exec(Object data, int offset, int count) {
//	static MLoopCol default_mloopcol = {255,255,255,255};
//	MLoopCol *mlcol = (MLoopCol*)data;
//	int i;
//	for(i = 0; i < count; i++)
//		mlcol[i] = default_mloopcol;
//
        }
    };

    public static LayerTypeInfo.Interp layerInterp_mloopcol = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	MLoopCol *mc = dest;
//	int i;
//	float *sub_weight;
//	struct {
//		float a;
//		float r;
//		float g;
//		float b;
//	} col;
//	col.a = col.r = col.g = col.b = 0;
//
//	sub_weight = sub_weights;
//	for(i = 0; i < count; ++i){
//		float weight = weights ? weights[i] : 1;
//		MLoopCol *src = sources[i];
//		if(sub_weights){
//			col.a += src.a * (*sub_weight) * weight;
//			col.r += src.r * (*sub_weight) * weight;
//			col.g += src.g * (*sub_weight) * weight;
//			col.b += src.b * (*sub_weight) * weight;
//			sub_weight++;
//		} else {
//			col.a += src.a * weight;
//			col.r += src.r * weight;
//			col.g += src.g * weight;
//			col.b += src.b * weight;
//		}
//	}
//	/* Subdivide smooth or fractal can cause problems without clamping
//	 * although weights should also not cause this situation */
//	CLAMP(col.a, 0.0f, 255.0f);
//	CLAMP(col.r, 0.0f, 255.0f);
//	CLAMP(col.g, 0.0f, 255.0f);
//	CLAMP(col.b, 0.0f, 255.0f);
//	mc.a = (int)col.a;
//	mc.r = (int)col.r;
//	mc.g = (int)col.g;
//	mc.b = (int)col.b;
        }
    };

    public static LayerTypeInfo.Interp layerInterp_mloopuv = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	MLoopUV *mluv = dest;
//	int i;
//	float *sub_weight;
//	struct {
//		float u;
//		float v;
//	}uv;
//	uv.u = uv.v = 0.0;
//
//	sub_weight = sub_weights;
//	for(i = 0; i < count; ++i){
//		float weight = weights ? weights[i] : 1;
//		MLoopUV *src = sources[i];
//		if(sub_weights){
//			uv.u += src.uv[0] * (*sub_weight) * weight;
//			uv.v += src.uv[1] * (*sub_weight) * weight;
//			sub_weight++;
//		} else {
//			uv.u += src.uv[0] * weight;
//			uv.v += src.uv[1] * weight;
//		}
//	}
//	mluv.uv[0] = uv.u;
//	mluv.uv[1] = uv.v;
        }
    };

    public static LayerTypeInfo.Interp layerInterp_mcol = new LayerTypeInfo.Interp() {
        public void exec(Object[] sources, float[] weights, float[] sub_weights, int count, Object dest) {
//	MCol *mc = dest;
//	int i, j, k;
//	struct {
//		float a;
//		float r;
//		float g;
//		float b;
//	} col[4];
//	float *sub_weight;
//
//	if(count <= 0) return;
//
//	memset(col, 0, sizeof(col));
//
//	sub_weight = sub_weights;
//	for(i = 0; i < count; ++i) {
//		float weight = weights ? weights[i] : 1;
//
//		for(j = 0; j < 4; ++j) {
//			if(sub_weights) {
//				MCol *src = sources[i];
//				for(k = 0; k < 4; ++k, ++sub_weight, ++src) {
//					col[j].a += src.a * (*sub_weight) * weight;
//					col[j].r += src.r * (*sub_weight) * weight;
//					col[j].g += src.g * (*sub_weight) * weight;
//					col[j].b += src.b * (*sub_weight) * weight;
//				}
//			} else {
//				MCol *src = sources[i];
//				col[j].a += src[j].a * weight;
//				col[j].r += src[j].r * weight;
//				col[j].g += src[j].g * weight;
//				col[j].b += src[j].b * weight;
//			}
//		}
//	}
//
//	for(j = 0; j < 4; ++j) {
//		/* Subdivide smooth or fractal can cause problems without clamping
//		 * although weights should also not cause this situation */
//		CLAMP(col[j].a, 0.0f, 255.0f);
//		CLAMP(col[j].r, 0.0f, 255.0f);
//		CLAMP(col[j].g, 0.0f, 255.0f);
//		CLAMP(col[j].b, 0.0f, 255.0f);
//		mc[j].a = (int)col[j].a;
//		mc[j].r = (int)col[j].r;
//		mc[j].g = (int)col[j].g;
//		mc[j].b = (int)col[j].b;
//	}
        }
    };

    public static LayerTypeInfo.Swap layerSwap_mcol = new LayerTypeInfo.Swap() {
        public void exec(Object data, int offset, int[] corner_indices) {
            MCol mcol = (MCol) data;
            MCol[] col = new MCol[4];
            int j;

            for (j = 0; j < 4; ++j) {
                col[j] = mcol.myarray[corner_indices[j]];
            }

            for (int i = 0; i < 4; i++) {
                System.arraycopy(col, 0, mcol, 0, 2);
            }
        }
    };

    public static LayerTypeInfo.Default layerDefault_mcol = new LayerTypeInfo.Default() {
        public void exec(Object data, int offset, int count) {
//	static MCol default_mcol = {255, 255, 255, 255};
//	MCol *mcol = (MCol*)data;
//	int i;
//
//	for(i = 0; i < 4*count; i++)
//		mcol[i] = default_mcol;
        }
    };

    static final LayerTypeInfo[] LAYERTYPEINFO = {
        new LayerTypeInfo(MVert.class, 1, "MVert", 1, null, null, null, null, null, null),
        new LayerTypeInfo(MSticky.class, 1, "MSticky", 1, null, null, null, layerInterp_msticky, null, null),
        new LayerTypeInfo(MDeformVert.class, 1, "MDeformVert", 1, null, layerCopy_mdeformvert,
        layerFree_mdeformvert, layerInterp_mdeformvert, null, null),
        new LayerTypeInfo(MEdge.class, 1, "MEdge", 1, null, null, null, null, null, null),
        new LayerTypeInfo(MFace.class, 1, "MFace", 1, null, null, null, null, null, null),
        new LayerTypeInfo(MTFace.class, 1, "MTFace", 1, "UVTex", layerCopy_tface, null,
        layerInterp_tface, layerSwap_tface, layerDefault_tface),
        /* 4 MCol structs per face */
        new LayerTypeInfo(MCol.class, 4, "MCol", 4, "Col", null, null, layerInterp_mcol,
        layerSwap_mcol, layerDefault_mcol),
        new LayerTypeInfo(Integer.class, 1, "", 0, null, null, null, null, null, null),
        /* 3 floats per normal vector */
        new LayerTypeInfo(Float.class, 3, "", 0, null, null, null, null, null, null),
        new LayerTypeInfo(Integer.class, 1, "", 0, null, null, null, null, null, null),
        new LayerTypeInfo(MFloatProperty.class, 1, "MFloatProperty", 1, "Float", null, null, null, null, null),
        new LayerTypeInfo(MIntProperty.class, 1, "MIntProperty", 1, "Int", null, null, null, null, null),
        new LayerTypeInfo(MStringProperty.class, 1, "MStringProperty", 1, "String", null, null, null, null, null),
        new LayerTypeInfo(OrigSpaceFace.class, 1, "OrigSpaceFace", 1, "UVTex", layerCopy_origspace_face, null,
        layerInterp_origspace_face, layerSwap_origspace_face, layerDefault_origspace_face),
        new LayerTypeInfo(Float.class, 3, "", 0, null, null, null, null, null, null),
        new LayerTypeInfo(MTexPoly.class, 1, "MTexPoly", 1, "Face Texture", null, null, null, null, null),
        new LayerTypeInfo(MLoopUV.class, 1, "MLoopUV", 1, "UV coord", null, null, layerInterp_mloopuv, null, null),
        new LayerTypeInfo(MLoopCol.class, 1, "MLoopCol", 1, "Col", null, null, layerInterp_mloopcol, null, layerDefault_mloopcol),
        new LayerTypeInfo(Float.class, 3 * 4, "", 0, null, null, null, null, null, null),
//        new LayerTypeInfo(MDisps, 1, "MDisps", 1, null, layerCopy_mdisps,
//       	 layerFree_mdisps, layerInterp_mdisps, layerSwap_mdisps, null, layerRead_mdisps, layerWrite_mdisps,
//       	 layerFilesize_mdisps, layerValidate_mdisps),
       	new LayerTypeInfo(MDisps.class, 1, "MDisps", 1, null, layerCopy_mdisps,
              	 layerFree_mdisps, layerInterp_mdisps, layerSwap_mdisps, null),
       	new LayerTypeInfo(MCol.class, 4, "MCol", 4, "WeightCol", null, null, layerInterp_mcol,
       	 layerSwap_mcol, layerDefault_mcol),
       	new LayerTypeInfo(MCol.class, 4, "MCol", 4, "IDCol", null, null, layerInterp_mcol,
       	 layerSwap_mcol, layerDefault_mcol),
       	new LayerTypeInfo(MCol.class, 4, "MCol", 4, "TexturedCol", null, null, layerInterp_mcol,
       	 layerSwap_mcol, layerDefault_mcol),
       	new LayerTypeInfo(Float.class, 3, "", 0, null, null, null, null, null, null)
    };
//
//const char *LAYERTYPENAMES[CD_NUMTYPES] = {
//	"CDMVert", "CDMSticky", "CDMDeformVert", "CDMEdge", "CDMFace", "CDMTFace",
//	"CDMCol", "CDOrigIndex", "CDNormal", "CDFlags","CDMFloatProperty",
//	"CDMIntProperty","CDMStringProperty", "CDOrigSpace", "CDOrco", "CDMTexPoly", "CDMLoopUV",
//	"CDMloopCol", "CDTangent", "CDMDisps", "CDWeightMCol"};

    public static final int CD_MASK_BAREMESH =
            CustomDataTypes.CD_MASK_MVERT | CustomDataTypes.CD_MASK_MEDGE | CustomDataTypes.CD_MASK_MFACE;
    public static final int CD_MASK_MESH =
            CustomDataTypes.CD_MASK_MVERT | CustomDataTypes.CD_MASK_MEDGE | CustomDataTypes.CD_MASK_MFACE |
            CustomDataTypes.CD_MASK_MSTICKY | CustomDataTypes.CD_MASK_MDEFORMVERT | CustomDataTypes.CD_MASK_MTFACE | CustomDataTypes.CD_MASK_MCOL |
            CustomDataTypes.CD_MASK_PROP_FLT | CustomDataTypes.CD_MASK_PROP_INT | CustomDataTypes.CD_MASK_PROP_STR | CustomDataTypes.CD_MASK_MDISPS;
    public static final int CD_MASK_EDITMESH =
            CustomDataTypes.CD_MASK_MSTICKY | CustomDataTypes.CD_MASK_MDEFORMVERT | CustomDataTypes.CD_MASK_MTFACE |
            CustomDataTypes.CD_MASK_MCOL | CustomDataTypes.CD_MASK_PROP_FLT | CustomDataTypes.CD_MASK_PROP_INT | CustomDataTypes.CD_MASK_PROP_STR | CustomDataTypes.CD_MASK_MDISPS;
//const CustomDataMask CD_MASK_DERIVEDMESH =
//	CD_MASK_MSTICKY | CD_MASK_MDEFORMVERT | CD_MASK_MTFACE |
//	CD_MASK_MCOL | CD_MASK_ORIGINDEX | CD_MASK_PROP_FLT | CD_MASK_PROP_INT |
//	CD_MASK_PROP_STR | CD_MASK_ORIGSPACE | CD_MASK_ORCO | CD_MASK_TANGENT | CD_MASK_WEIGHT_MCOL;
//const CustomDataMask CD_MASK_BMESH =
//	CD_MASK_MSTICKY | CD_MASK_MDEFORMVERT | CD_MASK_PROP_FLT | CD_MASK_PROP_INT | CD_MASK_PROP_STR;
//const CustomDataMask CD_MASK_FACECORNERS =
//	CD_MASK_MTFACE | CD_MASK_MCOL | CD_MASK_MTEXPOLY | CD_MASK_MLOOPUV |
//	CD_MASK_MLOOPCOL;
//

    public static LayerTypeInfo layerType_getInfo(int type) {
        if (type < 0 || type >= CustomDataTypes.CD_NUMTYPES) {
            return null;
        }
        return LAYERTYPEINFO[type];
    }

//static const char *layerType_getName(int type)
//{
//	if(type < 0 || type >= CD_NUMTYPES) return NULL;
//
//	return LAYERTYPENAMES[type];
//}

    /********************* CustomData functions *********************/

    public static void CustomData_merge(CustomData source, CustomData dest, int mask, int alloctype, int totelem) {
        LayerTypeInfo typeInfo;
        CustomDataLayer layer, newlayer;
        int i, type, number = 0, lasttype = -1, lastactive = 0, lastrender = 0;

        for (i = 0; i < source.totlayer; ++i) {
            layer = source.layers.myarray[i];
            typeInfo = layerType_getInfo(layer.type);

            type = layer.type;

            if (type != lasttype) {
                number = 0;
                lastactive = layer.active;
                lastrender = layer.active_rnd;
//                lastclone = layer.active_clone;
//                lastmask = layer.active_mask;
                lasttype = type;
            } else {
                number++;
            }

            if ((layer.flag & CustomDataTypes.CD_FLAG_NOCOPY) != 0) {
                continue;
            } else if ((mask & (1 << type)) == 0) {
                continue;
            } else if (number < CustomData_number_of_layers(dest, type)) {
                continue;
            }

            if ((alloctype == CD_ASSIGN) && (layer.flag & CustomDataTypes.CD_FLAG_NOFREE) != 0) {
                newlayer = customData_add_layer__internal(dest, type, CD_REFERENCE,
                        layer.data, totelem, layer.name);
            } else {
                newlayer = customData_add_layer__internal(dest, type, alloctype,
                        layer.data, totelem, layer.name);
            }

            if (newlayer != null) {
                newlayer.active = lastactive;
                newlayer.active_rnd = lastrender;
//                newlayer.active_clone = lastclone;
//                newlayer.active_mask = lastmask;
            }
        }
    }

    public static void CustomData_copy(CustomData source, CustomData dest, int mask, int alloctype, int totelem) {
        dest.myarray = null;
        dest.layers = null;
        dest.totlayer = 0;
        dest.maxlayer = 0;
        dest.totsize = 0;
        //dest.pad = 0;
        dest.pool = null;
        CustomData_merge(source, dest, mask, alloctype, totelem);
    }

    public static void customData_free_layer__internal(CustomDataLayer layer, int totelem) {
        LayerTypeInfo typeInfo;

        if ((layer.flag & CustomDataTypes.CD_FLAG_NOFREE) == 0 && layer.data != null) {
            typeInfo = layerType_getInfo(layer.type);

            if (typeInfo.free != null) {
                typeInfo.free.exec(layer.data, 0, totelem, typeInfo.size);
            }

//            if(layer.data!=null)
//                MEM_freeN(layer.data);
        }
    }

    public static void CustomData_free(CustomData data, int totelem) {
        for (int i = 0; i < data.totlayer; ++i) {
            customData_free_layer__internal(data.layers.myarray[i], totelem);
        }

//	if(data.layers!=null)
//            MEM_freeN(data.layers);

        data.myarray = null;
        data.layers = null;
        data.totlayer = 0;
        data.maxlayer = 0;
        data.totsize = 0;
        //data.pad = 0;
        data.pool = null;
    }

    public static void customData_update_offsets(CustomData data) {
        LayerTypeInfo typeInfo;
        int offset = 0;

        for (int i = 0; i < data.totlayer; ++i) {
            typeInfo = layerType_getInfo(data.layers.myarray[i].type);

            data.layers.myarray[i].offset = offset;
            offset += typeInfo.size;
        }

        data.totsize = offset;
    }

    public static int CustomData_get_layer_index(CustomData data, int type) {
        for (int i = 0; i < data.totlayer; ++i) {
            if (data.layers.myarray[i].type == type) {
                return i;
            }
        }
        return -1;
    }

//int CustomData_get_named_layer_index(const CustomData *data, int type, char *name)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type && strcmp(data->layers[i].name, name)==0)
//			return i;
//
//	return -1;
//}

    public static int CustomData_get_active_layer_index(CustomData data, int type) {
        for (int i = 0; i < data.totlayer; ++i) {
            if (data.layers.myarray[i].type == type) {
                return i + data.layers.myarray[i].active;
            }
        }
        return -1;
    }

//int CustomData_get_render_layer_index(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return i + data->layers[i].active_rnd;
//
//	return -1;
//}
//
//int CustomData_get_clone_layer_index(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return i + data->layers[i].active_clone;
//
//	return -1;
//}
//
//int CustomData_get_mask_layer_index(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return i + data->layers[i].active_mask;
//
//	return -1;
//}
//
//int CustomData_get_active_layer(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return data->layers[i].active;
//
//	return -1;
//}
//
//int CustomData_get_render_layer(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return data->layers[i].active_rnd;
//
//	return -1;
//}
//
//int CustomData_get_clone_layer(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return data->layers[i].active_clone;
//
//	return -1;
//}
//
//int CustomData_get_mask_layer(const CustomData *data, int type)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			return data->layers[i].active_mask;
//
//	return -1;
//}
//
//void CustomData_set_layer_active(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active = n;
//}
//
//void CustomData_set_layer_render(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_rnd = n;
//}
//
//void CustomData_set_layer_clone(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_clone = n;
//}
//
//void CustomData_set_layer_mask(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_mask = n;
//}
//
///* for using with an index from CustomData_get_active_layer_index and CustomData_get_render_layer_index */
//void CustomData_set_layer_active_index(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active = n-i;
//}
//
//void CustomData_set_layer_render_index(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_rnd = n-i;
//}
//
//void CustomData_set_layer_clone_index(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_clone = n-i;
//}
//
//void CustomData_set_layer_mask_index(CustomData *data, int type, int n)
//{
//	int i;
//
//	for(i=0; i < data->totlayer; ++i)
//		if(data->layers[i].type == type)
//			data->layers[i].active_mask = n-i;
//}

    public static void CustomData_set_layer_flag(CustomData data, int type, int flag) {
        for (int i = 0; i < data.totlayer; ++i) {
            if (data.layers.myarray[i].type == type) {
                data.layers.myarray[i].flag |= flag;
            }
        }
    }

    public static boolean customData_resize(CustomData data, int amount) {
        CustomDataLayer tmp;
        if (data.layers != null) {
            tmp = data.layers;
        } else {
            tmp = new CustomDataLayer();
        }

        tmp.myarray = new CustomDataLayer[data.maxlayer + amount];
        if (tmp.myarray.length > 0) {
            tmp.myarray[0] = tmp;
        }

        if (tmp == null || tmp.myarray == null) {
            return false;
        }

        data.maxlayer += amount;
        if (data.layers != null && data.layers.myarray != null) {
            System.arraycopy(data.layers.myarray, 0, tmp.myarray, 0, data.totlayer);
        }
        for (int i = data.totlayer; i < data.maxlayer; i++) {
            tmp.myarray[i] = new CustomDataLayer();
        }
        data.layers = tmp;

        return true;
    }

    public static CustomDataLayer customData_add_layer__internal(CustomData data,
            int type, int alloctype, Object layerdata, int totelem, byte[] name) {

        LayerTypeInfo typeInfo = layerType_getInfo(type);
        Class myclass = typeInfo.type;
        int size = typeInfo.size * totelem;
        int flag = 0, index = data.totlayer;
        Object newlayerdata;

        if (typeInfo.defaultname == null && CustomData_has_layer(data, type)) {
            return data.layers.myarray[CustomData_get_layer_index(data, type)];
        }

        if ((alloctype == CD_ASSIGN) || (alloctype == CD_REFERENCE)) {
            newlayerdata = layerdata;
        } else {
            newlayerdata = Array.newInstance(myclass, size);
            if (newlayerdata == null) {
                return null;
            }
        }

        if (alloctype == CD_DUPLICATE) {
            if (typeInfo.copy != null) {
                typeInfo.copy.exec(layerdata, 0, newlayerdata, 0, totelem);
            } else {
                System.arraycopy(layerdata, 0, newlayerdata, 0, size);
            }
        } else if (alloctype == CD_DEFAULT) {
            if (typeInfo.set_default != null) {
                typeInfo.set_default.exec(newlayerdata, 0, totelem);
            }
        } else if (alloctype == CD_REFERENCE) {
            flag |= CustomDataTypes.CD_FLAG_NOFREE;
        }

        if (index >= data.maxlayer) {
            if (!customData_resize(data, CUSTOMDATA_GROW)) {
                if (newlayerdata != layerdata) {
//                    MEM_freeN(newlayerdata);
                    return null;
                }
            }
        }

        data.totlayer++;

        /* keep layers ordered by type */
        for (; index > 0 && data.layers.myarray[index - 1].type > type; --index) {
            data.layers.myarray[index] = data.layers.myarray[index - 1];
        }

        data.layers.myarray[index].type = type;
        data.layers.myarray[index].flag = flag;
        data.layers.myarray[index].data = newlayerdata;

        if (name != null) {
            StringUtil.strcpy(data.layers.myarray[index].name, 0, name, 0);
            CustomData_set_layer_unique_name(data, index);
        } else {
            data.layers.myarray[index].name[0] = '\0';
        }

        if (index > 0 && data.layers.myarray[index - 1].type == type) {
            data.layers.myarray[index].active = data.layers.myarray[index - 1].active;
            data.layers.myarray[index].active_rnd = data.layers.myarray[index - 1].active_rnd;
//            data.layers.myarray[index].active_clone = data.layers.myarray[index-1].active_clone;
//            data.layers.myarray[index].active_mask = data.layers.myarray[index-1].active_mask;
        } else {
            data.layers.myarray[index].active = 0;
            data.layers.myarray[index].active_rnd = 0;
//            data.layers.myarray[index].active_clone = 0;
//            data.layers.myarray[index].active_mask = 0;
        }

        customData_update_offsets(data);

        return data.layers.myarray[index];
    }

    public static Object CustomData_add_layer(CustomData data, int type, int alloctype, Object layerdata, int totelem) {
        CustomDataLayer layer;
        LayerTypeInfo typeInfo = layerType_getInfo(type);

        layer = customData_add_layer__internal(data, type, alloctype, layerdata,
                totelem, typeInfo.defaultname);

        if (layer != null) {
            return layer.data;
        }

        return null;
    }

///*same as above but accepts a name*/
//void *CustomData_add_layer_named(CustomData *data, int type, int alloctype,
//                           void *layerdata, int totelem, char *name)
//{
//	CustomDataLayer *layer;
//
//	layer = customData_add_layer__internal(data, type, alloctype, layerdata,
//	                                       totelem, name);
//
//	if(layer)
//		return layer->data;
//
//	return NULL;
//}
//
//
//int CustomData_free_layer(CustomData *data, int type, int totelem, int index)
//{
//	int i;
//
//	if (index < 0) return 0;
//
//	customData_free_layer__internal(&data->layers[index], totelem);
//
//	for (i=index+1; i < data->totlayer; ++i)
//		data->layers[i-1] = data->layers[i];
//
//	data->totlayer--;
//
//	/* if layer was last of type in array, set new active layer */
//	if ((index >= data->totlayer) || (data->layers[index].type != type)) {
//		i = CustomData_get_layer_index(data, type);
//
//		if (i >= 0)
//			for (; i < data->totlayer && data->layers[i].type == type; i++) {
//				data->layers[i].active--;
//				data->layers[i].active_rnd--;
//				data->layers[i].active_clone--;
//				data->layers[i].active_mask--;
//			}
//	}
//
//	if (data->totlayer <= data->maxlayer-CUSTOMDATA_GROW)
//		customData_resize(data, -CUSTOMDATA_GROW);
//
//	customData_update_offsets(data);
//
//	return 1;
//}
//
//int CustomData_free_layer_active(CustomData *data, int type, int totelem)
//{
//	int index = 0;
//	index = CustomData_get_active_layer_index(data, type);
//	if (index < 0) return 0;
//	return CustomData_free_layer(data, type, totelem, index);
//}
//
//
//void CustomData_free_layers(CustomData *data, int type, int totelem)
//{
//	while (CustomData_has_layer(data, type))
//		CustomData_free_layer_active(data, type, totelem);
//}

    public static boolean CustomData_has_layer(CustomData data, int type) {
        return (CustomData_get_layer_index(data, type) != -1);
    }

    public static int CustomData_number_of_layers(CustomData data, int type) {
        int number = 0;

        for (int i = 0; i < data.totlayer; i++) {
            if (data.layers.myarray[i].type == type) {
                number++;
            }
        }

        return number;
    }

//void *CustomData_duplicate_referenced_layer(struct CustomData *data, int type)
//{
//	CustomDataLayer *layer;
//	int layer_index;
//
//	/* get the layer index of the first layer of type */
//	layer_index = CustomData_get_active_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	layer = &data->layers[layer_index];
//
//	if (layer->flag & CD_FLAG_NOFREE) {
//		layer->data = MEM_dupallocN(layer->data);
//		layer->flag &= ~CD_FLAG_NOFREE;
//	}
//
//	return layer->data;
//}
//
//void *CustomData_duplicate_referenced_layer_named(struct CustomData *data,
//                                                  int type, char *name)
//{
//	CustomDataLayer *layer;
//	int layer_index;
//
//	/* get the layer index of the desired layer */
//	layer_index = CustomData_get_named_layer_index(data, type, name);
//	if(layer_index < 0) return NULL;
//
//	layer = &data->layers[layer_index];
//
//	if (layer->flag & CD_FLAG_NOFREE) {
//		layer->data = MEM_dupallocN(layer->data);
//		layer->flag &= ~CD_FLAG_NOFREE;
//	}
//
//	return layer->data;
//}

    public static void CustomData_free_temporary(CustomData data, int totelem) {
        CustomDataLayer layer;
        int i, j;

        for (i = 0, j = 0; i < data.totlayer; ++i) {
            layer = data.layers.myarray[i];

            if (i != j) {
                data.layers.myarray[j] = data.layers.myarray[i];
            }

            if ((layer.flag & CustomDataTypes.CD_FLAG_TEMPORARY) == CustomDataTypes.CD_FLAG_TEMPORARY) {
                customData_free_layer__internal(layer, totelem);
            } else {
                j++;
            }
        }

        data.totlayer = j;

        if (data.totlayer <= data.maxlayer - CUSTOMDATA_GROW) {
            customData_resize(data, -CUSTOMDATA_GROW);
        }

        customData_update_offsets(data);
    }

//void CustomData_set_only_copy(const struct CustomData *data,
//                              CustomDataMask mask)
//{
//	int i;
//
//	for(i = 0; i < data->totlayer; ++i)
//		if(!((int)mask & (int)(1 << (int)data->layers[i].type)))
//			data->layers[i].flag |= CD_FLAG_NOCOPY;
//}
//
//void CustomData_copy_data(const CustomData *source, CustomData *dest,
//                          int source_index, int dest_index, int count)
//{
//	const LayerTypeInfo *typeInfo;
//	int src_i, dest_i;
//	int src_offset;
//	int dest_offset;
//
//	/* copies a layer at a time */
//	dest_i = 0;
//	for(src_i = 0; src_i < source->totlayer; ++src_i) {
//
//		/* find the first dest layer with type >= the source type
//		 * (this should work because layers are ordered by type)
//		 */
//		while(dest_i < dest->totlayer
//		      && dest->layers[dest_i].type < source->layers[src_i].type)
//			++dest_i;
//
//		/* if there are no more dest layers, we're done */
//		if(dest_i >= dest->totlayer) return;
//
//		/* if we found a matching layer, copy the data */
//		if(dest->layers[dest_i].type == source->layers[src_i].type) {
//			char *src_data = source->layers[src_i].data;
//			char *dest_data = dest->layers[dest_i].data;
//
//			typeInfo = layerType_getInfo(source->layers[src_i].type);
//
//			src_offset = source_index * typeInfo->size;
//			dest_offset = dest_index * typeInfo->size;
//
//			if(typeInfo->copy)
//				typeInfo->copy(src_data + src_offset,
//				                dest_data + dest_offset,
//				                count);
//			else
//				memcpy(dest_data + dest_offset,
//				       src_data + src_offset,
//				       count * typeInfo->size);
//
//			/* if there are multiple source & dest layers of the same type,
//			 * we don't want to copy all source layers to the same dest, so
//			 * increment dest_i
//			 */
//			++dest_i;
//		}
//	}
//}
//
//void CustomData_free_elem(CustomData *data, int index, int count)
//{
//	int i;
//	const LayerTypeInfo *typeInfo;
//
//	for(i = 0; i < data->totlayer; ++i) {
//		if(!(data->layers[i].flag & CD_FLAG_NOFREE)) {
//			typeInfo = layerType_getInfo(data->layers[i].type);
//
//			if(typeInfo->free) {
//				int offset = typeInfo->size * index;
//
//				typeInfo->free((char *)data->layers[i].data + offset,
//				               count, typeInfo->size);
//			}
//		}
//	}
//}
//
//#define SOURCE_BUF_SIZE 100
//
//void CustomData_interp(const CustomData *source, CustomData *dest,
//                       int *src_indices, float *weights, float *sub_weights,
//                       int count, int dest_index)
//{
//	int src_i, dest_i;
//	int dest_offset;
//	int j;
//	void *source_buf[SOURCE_BUF_SIZE];
//	void **sources = source_buf;
//
//	/* slow fallback in case we're interpolating a ridiculous number of
//	 * elements
//	 */
//	if(count > SOURCE_BUF_SIZE)
//		sources = MEM_callocN(sizeof(*sources) * count,
//		                      "CustomData_interp sources");
//
//	/* interpolates a layer at a time */
//	dest_i = 0;
//	for(src_i = 0; src_i < source->totlayer; ++src_i) {
//		const LayerTypeInfo *typeInfo= layerType_getInfo(source->layers[src_i].type);
//		if(!typeInfo->interp) continue;
//
//		/* find the first dest layer with type >= the source type
//		 * (this should work because layers are ordered by type)
//		 */
//		while(dest_i < dest->totlayer
//		      && dest->layers[dest_i].type < source->layers[src_i].type)
//			++dest_i;
//
//		/* if there are no more dest layers, we're done */
//		if(dest_i >= dest->totlayer) return;
//
//		/* if we found a matching layer, copy the data */
//		if(dest->layers[dest_i].type == source->layers[src_i].type) {
//			void *src_data = source->layers[src_i].data;
//
//			for(j = 0; j < count; ++j)
//				sources[j] = (char *)src_data
//							 + typeInfo->size * src_indices[j];
//
//			dest_offset = dest_index * typeInfo->size;
//
//			typeInfo->interp(sources, weights, sub_weights, count,
//						   (char *)dest->layers[dest_i].data + dest_offset);
//
//			/* if there are multiple source & dest layers of the same type,
//			 * we don't want to copy all source layers to the same dest, so
//			 * increment dest_i
//			 */
//			++dest_i;
//		}
//	}
//
//	if(count > SOURCE_BUF_SIZE) MEM_freeN(sources);
//}

    public static void CustomData_swap(CustomData data, int index, int[] corner_indices) {
        LayerTypeInfo typeInfo;

        for (int i = 0; i < data.totlayer; ++i) {
            typeInfo = layerType_getInfo(data.layers.myarray[i].type);

            if (typeInfo.swap != null) {
                int offset = typeInfo.size * index;
                typeInfo.swap.exec(data.layers.myarray[i].data, offset, corner_indices);
            }
        }
    }

//void *CustomData_get(const CustomData *data, int index, int type)
//{
//	int offset;
//	int layer_index;
//
//	/* get the layer index of the active layer of type */
//	layer_index = CustomData_get_active_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	/* get the offset of the desired element */
//	offset = layerType_getInfo(type)->size * index;
//
//	return (char *)data->layers[layer_index].data + offset;
//}

    public static Object CustomData_get_layer(CustomData data, int type) {
        /* get the layer index of the active layer of type */
        int layer_index = CustomData_get_active_layer_index(data, type);
        if (layer_index < 0) {
            return null;
        }
        return data.layers.myarray[layer_index].data;
    }

//void *CustomData_get_layer_n(const CustomData *data, int type, int n)
//{
//	/* get the layer index of the active layer of type */
//	int layer_index = CustomData_get_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	return data->layers[layer_index+n].data;
//}
//
//void *CustomData_get_layer_named(const struct CustomData *data, int type,
//                                 char *name)
//{
//	int layer_index = CustomData_get_named_layer_index(data, type, name);
//	if(layer_index < 0) return NULL;
//
//	return data->layers[layer_index].data;
//}

    public static Object CustomData_set_layer(CustomData data, int type, Object ptr) {
        /* get the layer index of the first layer of type */
        int layer_index = CustomData_get_active_layer_index(data, type);
        if (layer_index < 0) {
            return null;
        }
        data.layers.myarray[layer_index].data = ptr;
        return ptr;
    }

//void *CustomData_set_layer_n(const struct CustomData *data, int type, int n, void *ptr)
//{
//	/* get the layer index of the first layer of type */
//	int layer_index = CustomData_get_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	data->layers[layer_index+n].data = ptr;
//
//	return ptr;
//}
//
//void CustomData_set(const CustomData *data, int index, int type, void *source)
//{
//	void *dest = CustomData_get(data, index, type);
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	if(!dest) return;
//
//	if(typeInfo->copy)
//		typeInfo->copy(source, dest, 1);
//	else
//		memcpy(dest, source, typeInfo->size);
//}
//
///* EditMesh functions */

    public static void CustomData_em_free_block(CustomData data, Object[] block) {
        LayerTypeInfo typeInfo;

        if (block[0] == null) {
            return;
        }

        for (int i = 0; i < data.totlayer; ++i) {
            if ((data.layers.myarray[i].flag & CustomDataTypes.CD_FLAG_NOFREE) == 0) {
                typeInfo = layerType_getInfo(data.layers.myarray[i].type);

                if (typeInfo.free != null) {
                    int offset = data.layers.myarray[i].offset;
                    typeInfo.free.exec((byte[]) block[0], offset, 1, typeInfo.size);
                }
            }
        }

        block[0] = null;
    }

    public static void CustomData_em_alloc_block(CustomData data, Object[] block, Class type) {
        /* TODO: optimize free/alloc */
        if (block[0] != null) {
            CustomData_em_free_block(data, block);
        }

        if (data.totsize > 0) {
            block[0] = Array.newInstance(type, data.totsize);
        } else {
            block[0] = null;
        }
    }

    public static void CustomData_em_copy_data(CustomData source, CustomData dest,
            Object src_block, Object[] dest_block) {

        LayerTypeInfo typeInfo;
        int dest_i, src_i;

        if (dest_block[0] == null) {
            CustomData_em_alloc_block(dest, dest_block, src_block.getClass());
        }

        /* copies a layer at a time */
        dest_i = 0;
        for (src_i = 0; src_i < source.totlayer; ++src_i) {
            /* find the first dest layer with type >= the source type
             * (this should work because layers are ordered by type)
             */
            while (dest_i < dest.totlayer && dest.layers.myarray[dest_i].type < source.layers.myarray[src_i].type) {
                ++dest_i;
            }

            /* if there are no more dest layers, we're done */
            if (dest_i >= dest.totlayer) {
                return;
            }

            /* if we found a matching layer, copy the data */
            if (dest.layers.myarray[dest_i].type == source.layers.myarray[src_i].type &&
                    StringUtil.strcmp(dest.layers.myarray[dest_i].name, 0, source.layers.myarray[src_i].name, 0) == 0) {
                int src_data = source.layers.myarray[src_i].offset;
                int dest_data = dest.layers.myarray[dest_i].offset;

                typeInfo = layerType_getInfo(source.layers.myarray[src_i].type);

                if (typeInfo.copy != null) {
                    typeInfo.copy.exec(src_block, src_data, dest_block[0], dest_data, 1);
                } else {
                    System.arraycopy(src_block, src_data, dest_block[0], dest_data, typeInfo.size);
                }

                /* if there are multiple source & dest layers of the same type,
                 * we don't want to copy all source layers to the same dest, so
                 * increment dest_i
                 */
                ++dest_i;
            }
        }
    }

    public static Object CustomData_em_get(CustomData data, Object block, int type) {
//	int layer_index;
//
//	/* get the layer index of the first layer of type */
//	layer_index = CustomData_get_active_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	return (char *)block + data.layers[layer_index].offset;
        return null;
    }
//
//void *CustomData_em_get_n(const CustomData *data, void *block, int type, int n)
//{
//	int layer_index;
//
//	/* get the layer index of the first layer of type */
//	layer_index = CustomData_get_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	return (char *)block + data->layers[layer_index+n].offset;
//}
//
//void CustomData_em_set(CustomData *data, void *block, int type, void *source)
//{
//	void *dest = CustomData_em_get(data, block, type);
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	if(!dest) return;
//
//	if(typeInfo->copy)
//		typeInfo->copy(source, dest, 1);
//	else
//		memcpy(dest, source, typeInfo->size);
//}
//
//void CustomData_em_set_n(CustomData *data, void *block, int type, int n, void *source)
//{
//	void *dest = CustomData_em_get_n(data, block, type, n);
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	if(!dest) return;
//
//	if(typeInfo->copy)
//		typeInfo->copy(source, dest, 1);
//	else
//		memcpy(dest, source, typeInfo->size);
//}
//
//void CustomData_em_interp(CustomData *data, void **src_blocks, float *weights,
//                          float *sub_weights, int count, void *dest_block)
//{
//	int i, j;
//	void *source_buf[SOURCE_BUF_SIZE];
//	void **sources = source_buf;
//
//	/* slow fallback in case we're interpolating a ridiculous number of
//	 * elements
//	 */
//	if(count > SOURCE_BUF_SIZE)
//		sources = MEM_callocN(sizeof(*sources) * count,
//		                      "CustomData_interp sources");
//
//	/* interpolates a layer at a time */
//	for(i = 0; i < data->totlayer; ++i) {
//		CustomDataLayer *layer = &data->layers[i];
//		const LayerTypeInfo *typeInfo = layerType_getInfo(layer->type);
//
//		if(typeInfo->interp) {
//			for(j = 0; j < count; ++j)
//				sources[j] = (char *)src_blocks[j] + layer->offset;
//
//			typeInfo->interp(sources, weights, sub_weights, count,
//			                  (char *)dest_block + layer->offset);
//		}
//	}
//
//	if(count > SOURCE_BUF_SIZE) MEM_freeN(sources);
//}

    public static void CustomData_em_set_default(CustomData data, Object[] block) {
        LayerTypeInfo typeInfo;

        if (block[0] == null) {
            CustomData_em_alloc_block(data, block, byte.class);
        }

        for (int i = 0; i < data.totlayer; ++i) {
            int offset = data.layers.myarray[i].offset;
            typeInfo = layerType_getInfo(data.layers.myarray[i].type);

            if (typeInfo.set_default != null) {
                typeInfo.set_default.exec((byte[]) block[0], offset, 1);
            }
        }
    }

    public static void CustomData_to_em_block(CustomData source, CustomData dest,
            int src_index, Object[] dest_block) {

        LayerTypeInfo typeInfo;
        int dest_i, src_i, src_offset;

        if (dest_block[0] == null) {
            CustomData_em_alloc_block(dest, dest_block, source.layers.getClass());
        }

        /* copies a layer at a time */
        dest_i = 0;
        for (src_i = 0; src_i < source.totlayer; ++src_i) {
            /* find the first dest layer with type >= the source type
             * (this should work because layers are ordered by type)
             */
            while (dest_i < dest.totlayer && dest.layers.myarray[dest_i].type < source.layers.myarray[src_i].type) {
                ++dest_i;
            }

            /* if there are no more dest layers, we're done */
            if (dest_i >= dest.totlayer) {
                return;
            }

            /* if we found a matching layer, copy the data */
            if (dest.layers.myarray[dest_i].type == source.layers.myarray[src_i].type) {
                int offset = dest.layers.myarray[dest_i].offset;
                Object src_data = source.layers.myarray[src_i].data;
                Object dest_data = dest_block;

                typeInfo = layerType_getInfo(dest.layers.myarray[dest_i].type);
                src_offset = src_index * typeInfo.size;

                if (typeInfo.copy != null) {
                    typeInfo.copy.exec(src_data, src_offset, dest_data, offset, 1);
                } else {
                    System.arraycopy(src_data, src_offset, dest_data, offset, typeInfo.size);
                }

                /* if there are multiple source & dest layers of the same type,
                 * we don't want to copy all source layers to the same dest, so
                 * increment dest_i
                 */
                ++dest_i;
            }
        }
    }

    public static void CustomData_from_em_block(CustomData source, CustomData dest,
            Object src_block, int dest_index) {

        LayerTypeInfo typeInfo;
        int dest_i, src_i, dest_offset;

        /* copies a layer at a time */
        dest_i = 0;
        for (src_i = 0; src_i < source.totlayer; ++src_i) {
            /* find the first dest layer with type >= the source type
             * (this should work because layers are ordered by type)
             */
            while (dest_i < dest.totlayer && dest.layers.myarray[dest_i].type < source.layers.myarray[src_i].type) {
                ++dest_i;
            }

            /* if there are no more dest layers, we're done */
            if (dest_i >= dest.totlayer) {
                return;
            }

            /* if we found a matching layer, copy the data */
            if (dest.layers.myarray[dest_i].type == source.layers.myarray[src_i].type) {
                int offset = source.layers.myarray[src_i].offset;
                Object src_data = src_block;
                Object dest_data = dest.layers.myarray[dest_i].data;

                typeInfo = layerType_getInfo(dest.layers.myarray[dest_i].type);
                dest_offset = dest_index * typeInfo.size;

                if (typeInfo.copy != null) {
                    typeInfo.copy.exec(src_data, offset, dest_data, dest_offset, 1);
                } else {
                    System.arraycopy(src_data, offset, dest_data, dest_offset, typeInfo.size);
                }

                /* if there are multiple source & dest layers of the same type,
                 * we don't want to copy all source layers to the same dest, so
                 * increment dest_i
                 */
                ++dest_i;
            }
        }
    }

///*Bmesh functions*/
///*needed to convert to/from different face reps*/
//void CustomData_to_bmeshpoly(CustomData *fdata, CustomData *pdata, CustomData *ldata)
//{
//	int i;
//	for(i=0; i < fdata->totlayer; i++){
//		if(fdata->layers[i].type == CD_MTFACE){
//			CustomData_add_layer(pdata, CD_MTEXPOLY, CD_CALLOC, &(fdata->layers[i].name), 0);
//			CustomData_add_layer(ldata, CD_MLOOPUV, CD_CALLOC, &(fdata->layers[i].name), 0);
//		}
//		else if(fdata->layers[i].type == CD_MCOL)
//			CustomData_add_layer(ldata, CD_MLOOPCOL, CD_CALLOC, &(fdata->layers[i].name), 0);
//	}
//}
//void CustomData_from_bmeshpoly(CustomData *fdata, CustomData *pdata, CustomData *ldata, int total){
//	int i;
//	for(i=0; i < pdata->totlayer; i++){
//		if(pdata->layers[i].type == CD_MTEXPOLY)
//			CustomData_add_layer(fdata, CD_MTFACE, CD_CALLOC, &(pdata->layers[i].name), total);
//	}
//	for(i=0; i < ldata->totlayer; i++){
//		if(ldata->layers[i].type == CD_MLOOPCOL)
//			CustomData_add_layer(fdata, CD_MCOL, CD_CALLOC, &(ldata->layers[i].name), total);
//	}
//}
//
//
//void CustomData_bmesh_init_pool(CustomData *data, int allocsize){
//	if(data->totlayer)data->pool = BLI_mempool_create(data->totsize, allocsize, allocsize);
//}
//
//void CustomData_bmesh_free_block(CustomData *data, void **block)
//{
//    const LayerTypeInfo *typeInfo;
//    int i;
//
//	if(!*block) return;
//    for(i = 0; i < data->totlayer; ++i) {
//        if(!(data->layers[i].flag & CD_FLAG_NOFREE)) {
//            typeInfo = layerType_getInfo(data->layers[i].type);
//
//            if(typeInfo->free) {
//				int offset = data->layers[i].offset;
//				typeInfo->free((char*)*block + offset, 1, typeInfo->size);
//			}
//        }
//    }
//
//	BLI_mempool_free(data->pool, *block);
//	*block = NULL;
//}
//
//static void CustomData_bmesh_alloc_block(CustomData *data, void **block)
//{
//
//	if (*block)
//		CustomData_bmesh_free_block(data, block);
//
//	if (data->totsize > 0)
//		*block = BLI_mempool_calloc(data->pool);
//	else
//		*block = NULL;
//}
//
//void CustomData_bmesh_copy_data(const CustomData *source, CustomData *dest,
//                            void *src_block, void **dest_block)
//{
//	const LayerTypeInfo *typeInfo;
//	int dest_i, src_i;
//
//	if (!*dest_block)
//		CustomData_bmesh_alloc_block(dest, dest_block);
//
//	/* copies a layer at a time */
//	dest_i = 0;
//	for(src_i = 0; src_i < source->totlayer; ++src_i) {
//
//		/* find the first dest layer with type >= the source type
//		 * (this should work because layers are ordered by type)
//		 */
//		while(dest_i < dest->totlayer
//		      && dest->layers[dest_i].type < source->layers[src_i].type)
//			++dest_i;
//
//		/* if there are no more dest layers, we're done */
//		if(dest_i >= dest->totlayer) return;
//
//		/* if we found a matching layer, copy the data */
//		if(dest->layers[dest_i].type == source->layers[src_i].type &&
//			strcmp(dest->layers[dest_i].name, source->layers[src_i].name) == 0) {
//			char *src_data = (char*)src_block + source->layers[src_i].offset;
//			char *dest_data = (char*)*dest_block + dest->layers[dest_i].offset;
//
//			typeInfo = layerType_getInfo(source->layers[src_i].type);
//
//			if(typeInfo->copy)
//				typeInfo->copy(src_data, dest_data, 1);
//			else
//				memcpy(dest_data, src_data, typeInfo->size);
//
//			/* if there are multiple source & dest layers of the same type,
//			 * we don't want to copy all source layers to the same dest, so
//			 * increment dest_i
//			 */
//			++dest_i;
//		}
//	}
//}
//
///*Bmesh Custom Data Functions. Should replace editmesh ones with these as well, due to more effecient memory alloc*/
//void *CustomData_bmesh_get(const CustomData *data, void *block, int type)
//{
//	int layer_index;
//
//	/* get the layer index of the first layer of type */
//	layer_index = CustomData_get_active_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	return (char *)block + data->layers[layer_index].offset;
//}
//
//void *CustomData_bmesh_get_n(const CustomData *data, void *block, int type, int n)
//{
//	int layer_index;
//
//	/* get the layer index of the first layer of type */
//	layer_index = CustomData_get_layer_index(data, type);
//	if(layer_index < 0) return NULL;
//
//	return (char *)block + data->layers[layer_index+n].offset;
//}
//
//void CustomData_bmesh_set(const CustomData *data, void *block, int type, void *source)
//{
//	void *dest = CustomData_bmesh_get(data, block, type);
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	if(!dest) return;
//
//	if(typeInfo->copy)
//		typeInfo->copy(source, dest, 1);
//	else
//		memcpy(dest, source, typeInfo->size);
//}
//
//void CustomData_bmesh_set_n(CustomData *data, void *block, int type, int n, void *source)
//{
//	void *dest = CustomData_bmesh_get_n(data, block, type, n);
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	if(!dest) return;
//
//	if(typeInfo->copy)
//		typeInfo->copy(source, dest, 1);
//	else
//		memcpy(dest, source, typeInfo->size);
//}
//
//void CustomData_bmesh_interp(CustomData *data, void **src_blocks, float *weights,
//                          float *sub_weights, int count, void *dest_block)
//{
//	int i, j;
//	void *source_buf[SOURCE_BUF_SIZE];
//	void **sources = source_buf;
//
//	/* slow fallback in case we're interpolating a ridiculous number of
//	 * elements
//	 */
//	if(count > SOURCE_BUF_SIZE)
//		sources = MEM_callocN(sizeof(*sources) * count,
//		                      "CustomData_interp sources");
//
//	/* interpolates a layer at a time */
//	for(i = 0; i < data->totlayer; ++i) {
//		CustomDataLayer *layer = &data->layers[i];
//		const LayerTypeInfo *typeInfo = layerType_getInfo(layer->type);
//		if(typeInfo->interp) {
//			for(j = 0; j < count; ++j)
//				sources[j] = (char *)src_blocks[j] + layer->offset;
//
//			typeInfo->interp(sources, weights, sub_weights, count,
//			                  (char *)dest_block + layer->offset);
//		}
//	}
//
//	if(count > SOURCE_BUF_SIZE) MEM_freeN(sources);
//}
//
//void CustomData_bmesh_set_default(CustomData *data, void **block)
//{
//	const LayerTypeInfo *typeInfo;
//	int i;
//
//	if (!*block)
//		CustomData_bmesh_alloc_block(data, block);
//
//	for(i = 0; i < data->totlayer; ++i) {
//		int offset = data->layers[i].offset;
//
//		typeInfo = layerType_getInfo(data->layers[i].type);
//
//		if(typeInfo->set_default)
//			typeInfo->set_default((char*)*block + offset, 1);
//	}
//}
//
//void CustomData_to_bmesh_block(const CustomData *source, CustomData *dest,
//                            int src_index, void **dest_block)
//{
//	const LayerTypeInfo *typeInfo;
//	int dest_i, src_i, src_offset;
//
//	if (!*dest_block)
//		CustomData_bmesh_alloc_block(dest, dest_block);
//
//	/* copies a layer at a time */
//	dest_i = 0;
//	for(src_i = 0; src_i < source->totlayer; ++src_i) {
//
//		/* find the first dest layer with type >= the source type
//		 * (this should work because layers are ordered by type)
//		 */
//		while(dest_i < dest->totlayer
//		      && dest->layers[dest_i].type < source->layers[src_i].type)
//			++dest_i;
//
//		/* if there are no more dest layers, we're done */
//		if(dest_i >= dest->totlayer) return;
//
//		/* if we found a matching layer, copy the data */
//		if(dest->layers[dest_i].type == source->layers[src_i].type) {
//			int offset = dest->layers[dest_i].offset;
//			char *src_data = source->layers[src_i].data;
//			char *dest_data = (char*)*dest_block + offset;
//
//			typeInfo = layerType_getInfo(dest->layers[dest_i].type);
//			src_offset = src_index * typeInfo->size;
//
//			if(typeInfo->copy)
//				typeInfo->copy(src_data + src_offset, dest_data, 1);
//			else
//				memcpy(dest_data, src_data + src_offset, typeInfo->size);
//
//			/* if there are multiple source & dest layers of the same type,
//			 * we don't want to copy all source layers to the same dest, so
//			 * increment dest_i
//			 */
//			++dest_i;
//		}
//	}
//}
//
//void CustomData_from_bmesh_block(const CustomData *source, CustomData *dest,
//                              void *src_block, int dest_index)
//{
//	const LayerTypeInfo *typeInfo;
//	int dest_i, src_i, dest_offset;
//
//	/* copies a layer at a time */
//	dest_i = 0;
//	for(src_i = 0; src_i < source->totlayer; ++src_i) {
//
//		/* find the first dest layer with type >= the source type
//		 * (this should work because layers are ordered by type)
//		 */
//		while(dest_i < dest->totlayer
//		      && dest->layers[dest_i].type < source->layers[src_i].type)
//			++dest_i;
//
//		/* if there are no more dest layers, we're done */
//		if(dest_i >= dest->totlayer) return;
//
//		/* if we found a matching layer, copy the data */
//		if(dest->layers[dest_i].type == source->layers[src_i].type) {
//			int offset = source->layers[src_i].offset;
//			char *src_data = (char*)src_block + offset;
//			char *dest_data = dest->layers[dest_i].data;
//
//			typeInfo = layerType_getInfo(dest->layers[dest_i].type);
//			dest_offset = dest_index * typeInfo->size;
//
//			if(typeInfo->copy)
//				typeInfo->copy(src_data, dest_data + dest_offset, 1);
//			else
//				memcpy(dest_data + dest_offset, src_data, typeInfo->size);
//
//			/* if there are multiple source & dest layers of the same type,
//			 * we don't want to copy all source layers to the same dest, so
//			 * increment dest_i
//			 */
//			++dest_i;
//		}
//	}
//
//}

    public static void CustomData_file_write_info(int type, byte[][] structname, int[] structnum) {
        LayerTypeInfo typeInfo = layerType_getInfo(type);
        structname[0] = typeInfo.structname;
        structnum[0] = typeInfo.structnum;
    }
//
//int CustomData_sizeof(int type)
//{
//	const LayerTypeInfo *typeInfo = layerType_getInfo(type);
//
//	return typeInfo->size;
//}
//
//const char *CustomData_layertype_name(int type)
//{
//	return layerType_getName(type);
//}

    public static boolean CustomData_is_property_layer(int type) {
        if ((type == CustomDataTypes.CD_PROP_FLT) || (type == CustomDataTypes.CD_PROP_INT) || (type == CustomDataTypes.CD_PROP_STR)) {
            return true;
        }
        return false;
    }

    public static void CustomData_set_layer_unique_name(CustomData data, int index) {
        String tempname;
        int number, i, type;
        byte[] name;
        int dot;
        CustomDataLayer layer, nlayer = data.layers.myarray[index];
        LayerTypeInfo typeInfo = layerType_getInfo(nlayer.type);

        if (typeInfo.defaultname == null) {
            return;
        }

        type = nlayer.type;
        name = nlayer.name;

        if (name[0] == '\0') {
            StringUtil.BLI_strncpy(nlayer.name,0, typeInfo.defaultname,0, nlayer.name.length);
        }

        /* see if there is a duplicate */
        for (i = 0; i < data.totlayer; i++) {
            layer = data.layers.myarray[i];

            if (CustomData_is_property_layer(type)) {
                if (i != index && CustomData_is_property_layer(layer.type) &&
                        StringUtil.strcmp(layer.name, 0, name, 0) == 0) {
                    break;
                }

            } else {
                if (i != index && layer.type == type && StringUtil.strcmp(layer.name, 0, name, 0) == 0) {
                    break;
                }
            }
        }

        if (i == data.totlayer) {
            return;
        }

        /* strip off the suffix */
        dot = StringUtil.strchr(nlayer.name, 0, '.');
        if (dot >= 0) {
            nlayer.name[dot] = 0;
        }

        for (number = 1; number <= 999; number++) {
            tempname = String.format("%s.%03d", StringUtil.toJString(nlayer.name, 0), number);

            for (i = 0; i < data.totlayer; i++) {
                layer = data.layers.myarray[i];

                if (CustomData_is_property_layer(type)) {
                    if (i != index && CustomData_is_property_layer(layer.type) &&
                            StringUtil.strcmp(layer.name, 0, StringUtil.toCString(tempname), 0) == 0) {
                        break;
                    }
                } else {
                    if (i != index && layer.type == type && StringUtil.strcmp(layer.name, 0, StringUtil.toCString(tempname), 0) == 0) {
                        break;
                    }
                }
            }

            if (i == data.totlayer) {
                StringUtil.BLI_strncpy(nlayer.name,0, StringUtil.toCString(tempname),0, nlayer.name.length);
                return;
            }
        }
    }

    public static int CustomData_verify_versions(CustomData data, int index) {
        LayerTypeInfo typeInfo;
        CustomDataLayer layer = data.layers.myarray[index];
        int i, keeplayer = 1;

        if (layer.type >= CustomDataTypes.CD_NUMTYPES) {
            keeplayer = 0; /* unknown layer type from future version */
        } else {
            typeInfo = layerType_getInfo(layer.type);

            if (typeInfo.defaultname == null && (index > 0) &&
                    data.layers.myarray[index - 1].type == layer.type) {
                keeplayer = 0; /* multiple layers of which we only support one */
            }
        }

        if (keeplayer == 0) {
            for (i = index + 1; i < data.totlayer; ++i) {
                data.layers.myarray[i - 1] = data.layers.myarray[i];
            }
            data.totlayer--;
        }

        return keeplayer;
    }

}

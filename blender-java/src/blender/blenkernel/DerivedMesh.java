/**
 * $Id: DerivedMesh.c 20932 2009-06-16 14:21:58Z campbellbarton $
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
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * The Original Code is Copyright (C) 2005 Blender Foundation.
 * All rights reserved.
 *
 * The Original Code is: all of this file.
 *
 * Contributor(s): none yet.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.blenkernel;

import blender.blenlib.Arithb;
import blender.blenlib.EditVertUtil.EditEdge;
import blender.blenlib.EditVertUtil.EditFace;
import blender.blenlib.EditVertUtil.EditMesh;
import blender.blenlib.EditVertUtil.EditVert;
import blender.blenlib.ListBaseUtil;
import blender.editors.screen.GlUtil;
import blender.makesdna.CustomDataTypes;
import blender.makesdna.MeshDataTypes;
import blender.makesdna.sdna.CustomData;
import blender.makesdna.sdna.MEdge;
import blender.makesdna.sdna.MFace;
import blender.makesdna.sdna.MTFace;
import blender.makesdna.sdna.MVert;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bObject;
import static blender.blenkernel.Blender.G;
import javax.media.opengl.GL2;

public class DerivedMesh {
    
/* TODO (Probably)
 *
 *  o Make drawMapped* functions take a predicate function that
 *    determines whether to draw the edge (this predicate can
 *    also set color, etc). This will be slightly more general 
 *    and allow some of the functions to be collapsed.
 *  o Once accessor functions are added then single element draw
 *    functions can be implemented using primitive accessors.
 *  o Add function to dispatch to renderer instead of using
 *    conversion to DLM.
 */

///* number of sub-elements each mesh element has (for interpolation) */
//#define SUB_ELEMS_VERT 0
//#define SUB_ELEMS_EDGE 2
//#define SUB_ELEMS_FACE 4

	/* Private DerivedMesh data, only for internal DerivedMesh use */
    public CustomData vertData = new CustomData(), edgeData = new CustomData(), faceData = new CustomData();
    public int numVertData, numEdgeData, numFaceData;
    public int needsFree; /* checked on .release, is set to 0 for cached results */
    public int deformedOnly; /* set by modifier stack if only deformed from original */
//	BVHCache bvhCache;

	/* Misc. Queries */
    
    public static interface GetNum {
        public int run(DerivedMesh dm);
    };

    /* Also called in Editmode */
    public GetNum getNumVerts;
    /* Also called in Editmode */
    public GetNum getNumFaces;
    
    public GetNum getNumEdges;

//	/* copy a single vert/edge/face from the derived mesh into
//	 * *{vert/edge/face}_r. note that the current implementation
//	 * of this function can be quite slow, iterating over all
//	 * elements (editmesh)
//	 */
//	void (*getVert)(DerivedMesh *dm, int index, struct MVert *vert_r);
//	void (*getEdge)(DerivedMesh *dm, int index, struct MEdge *edge_r);
//	void (*getFace)(DerivedMesh *dm, int index, struct MFace *face_r);

    /* return a pointer to the entire array of verts/edges/face from the
     * derived mesh. if such an array does not exist yet, it will be created,
     * and freed on the next .release(). consider using getVert/Edge/Face if
     * you are only interested in a few verts/edges/faces.
     */
    public static interface GetArray<T> {
        public T run(DerivedMesh dm);
    };

    public GetArray<MVert[]> getVertArray;
    public GetArray<MEdge[]> getEdgeArray;
    public GetArray<MFace[]> getFaceArray;

    /* copy all verts/edges/faces from the derived mesh into
     * *{vert/edge/face}_r (must point to a buffer large enough)
     */
    public static interface CopyArray<T> {
        public void run(DerivedMesh dm, T r);
    };

    public CopyArray<MVert[]> copyVertArray;
    public CopyArray<MEdge[]> copyEdgeArray;
    public CopyArray<MFace[]> copyFaceArray;

//	/* return a copy of all verts/edges/faces from the derived mesh
//	 * it is the caller's responsibility to free the returned pointer
//	 */
//	struct MVert *(*dupVertArray)(DerivedMesh *dm);
//	struct MEdge *(*dupEdgeArray)(DerivedMesh *dm);
//	struct MFace *(*dupFaceArray)(DerivedMesh *dm);
//
//	/* return a pointer to a single element of vert/edge/face custom data
//	 * from the derived mesh (this gives a pointer to the actual data, not
//	 * a copy)
//	 */
//	void *(*getVertData)(DerivedMesh *dm, int index, int type);
//	void *(*getEdgeData)(DerivedMesh *dm, int index, int type);
//	void *(*getFaceData)(DerivedMesh *dm, int index, int type);

    /* return a pointer to the entire array of vert/edge/face custom data
     * from the derived mesh (this gives a pointer to the actual data, not
     * a copy)
     */
    public static interface GetDataArray {
        public Object run(DerivedMesh dm, int type);
    };

    public GetDataArray getVertDataArray;
    public GetDataArray getEdgeDataArray;
    public GetDataArray getFaceDataArray;

	/* Iterate over each mapped vertex in the derived mesh, calling the
	 * given function with the original vert and the mapped vert's new
	 * coordinate and normal. For historical reasons the normal can be
	 * passed as a float or short array, only one should be non-NULL.
	 */
    public static interface ForeachMappedVertFunc {
        public void func(GL2 gl, Object userData, int index, float[] co, float[] no_f, short[] no_s);
    };

    public static interface ForeachMappedVert {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedVertFunc func, Object userData);
    };
    
    public ForeachMappedVert foreachMappedVert;

    /* Iterate over each mapped edge in the derived mesh, calling the
     * given function with the original edge and the mapped edge's new
     * coordinates.
     */
    public static interface ForeachMappedEdge {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedEdgeFunc func, Object userData);
    };

    public static interface ForeachMappedEdgeFunc {
        public void func(GL2 gl, Object userData, int index, float[] v0co, float[] v1co);
    };
    
    public ForeachMappedEdge foreachMappedEdge;

    /* Iterate over each mapped face in the derived mesh, calling the
     * given function with the original face and the mapped face's (or
     * faces') center and normal.
     */
    public static interface ForeachMappedFaceCenter {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedFaceCenterFunc func, Object userData);
    };

    public static interface ForeachMappedFaceCenterFunc {
        public void func(GL2 gl, Object userData, int index, float[] cent, float[] no);
    };

    public ForeachMappedFaceCenter foreachMappedFaceCenter;

    /* Iterate over all vertex points, calling DO_MINMAX with given args.
     *
     * Also called in Editmode
     */
    public static interface GetMinMax {
        public void run(DerivedMesh dm, float[] min_r, float[] max_r);
    };

    public GetMinMax getMinMax;

//	/* Direct Access Operations */
//	/*  o Can be undefined */
//	/*  o Must be defined for modifiers that only deform however */
//
//	/* Get vertex location, undefined if index is not valid */
//	void (*getVertCo)(DerivedMesh *dm, int index, float co_r[3]);
//
//	/* Fill the array (of length .getNumVerts()) with all vertex locations */
//	void (*getVertCos)(DerivedMesh *dm, float (*cos_r)[3]);
//
//	/* Get vertex normal, undefined if index is not valid */
//	void (*getVertNo)(DerivedMesh *dm, int index, float no_r[3]);

	/* Drawing Operations */

//	/* Draw all vertices as bgl points (no options) */
//	void (*drawVerts)(DerivedMesh *dm);
//
//	/* Draw edges in the UV mesh (if exists) */
//	void (*drawUVEdges)(DerivedMesh *dm);

    /* Draw all edges as lines (no options)
     *
     * Also called for *final* editmode DerivedMeshes
     */
    public static interface DrawEdges {
        public void run(GL2 gl, DerivedMesh dm, boolean drawLooseEdges);
    };

    public DrawEdges drawEdges;
	
    /* Draw all loose edges (edges w/ no adjoining faces) */
    public static interface DrawLooseEdges {
        public void run(GL2 gl, DerivedMesh dm);
    };

    public DrawLooseEdges drawLooseEdges;

    /* Draw all faces
     *  o Set face normal or vertex normal based on inherited face flag
     *  o Use inherited face material index to call setMaterial
     *  o Only if setMaterial returns true
     *
     * Also called for *final* editmode DerivedMeshes
     */
//	void (*drawFacesSolid)(DerivedMesh *dm, int (*setMaterial)(int, void *attribs));
    public static interface DrawFacesSolid {
        public void run(GL2 gl, DerivedMesh dm);
    };

    public DrawFacesSolid drawFacesSolid;

    /* Draw all faces
     *  o If useTwoSided, draw front and back using col arrays
     *  o col1,col2 are arrays of length numFace*4 of 4 component colors
     *    in ABGR format, and should be passed as per-face vertex color.
     */
    public static interface DrawFacesColored {
        public void run(GL2 gl, DerivedMesh dm, boolean useTwoSided, byte[] col1, byte[] col2);
    };

    public DrawFacesColored drawFacesColored;

    /* Draw all faces using MTFace
     *  o Drawing options too complicated to enumerate, look at code.
     */
//	void (*drawFacesTex)(DerivedMesh *dm, int (*setDrawOptions)(struct MTFace *tface, struct MCol *mcol, int matnr));
    public static interface DrawFacesTex {
        public void run(GL2 gl, DerivedMesh dm);
    };

    public DrawFacesTex drawFacesTex;

//	/* Draw all faces with GLSL materials
//	 *  o setMaterial is called for every different material nr
//	 *  o Only if setMaterial returns true
//	 */
//	void (*drawFacesGLSL)(DerivedMesh *dm,
//		int (*setMaterial)(int, void *attribs));

    /* Draw mapped faces (no color, or texture)
     *  o Only if !setDrawOptions or
     *    setDrawOptions(userData, mapped-face-index, drawSmooth_r)
     *    returns true
     *
     * If drawSmooth is set to true then vertex normals should be set and
     * glShadeModel called with GL_SMOOTH. Otherwise the face normal should
     * be set and glShadeModel called with GL_FLAT.
     *
     * The setDrawOptions is allowed to not set drawSmooth (for example, when
     * lighting is disabled), in which case the implementation should draw as
     * smooth shaded.
     */
    public static interface DrawMappedFaces {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedFacesFunc setDrawOptions, Object userData, int useColors);
    };

    public static interface DrawMappedFacesFunc {
        public int run(GL2 gl, Object userData, int index, int[] drawSmooth_r);
    };

    public DrawMappedFaces drawMappedFaces;

    /* Draw mapped faces using MTFace
     *  o Drawing options too complicated to enumerate, look at code.
     */
//	void (*drawMappedFacesTex)(DerivedMesh *dm, int (*setDrawOptions)(void *userData, int index), void *userData);
    public static interface DrawMappedFacesTex {
        public void run(GL2 gl, DerivedMesh dm);
    };

    public DrawMappedFacesTex drawMappedFacesTex;

//	/* Draw mapped faces with GLSL materials
//	 *  o setMaterial is called for every different material nr
//	 *  o setDrawOptions is called for every face
//	 *  o Only if setMaterial and setDrawOptions return true
//	 */
//	void (*drawMappedFacesGLSL)(DerivedMesh *dm,
//		int (*setMaterial)(int, void *attribs),
//		int (*setDrawOptions)(void *userData, int index), void *userData);

    /* Draw mapped edges as lines
     *  o Only if !setDrawOptions or setDrawOptions(userData, mapped-edge)
     *    returns true
     */
    public static interface DrawMappedEdges {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedEdgesFunc setDrawOptions, Object userData);
    };

    public static interface DrawMappedEdgesFunc {
        public boolean run(GL2 gl, Object userData, int index);
    };

    public DrawMappedEdges drawMappedEdges;

//	/* Draw mapped edges as lines with interpolation values
//	 *  o Only if !setDrawOptions or
//	 *    setDrawOptions(userData, mapped-edge, mapped-v0, mapped-v1, t)
//	 *    returns true
//	 *
//	 * NOTE: This routine is optional!
//	 */
//	void (*drawMappedEdgesInterp)(DerivedMesh *dm, 
//	                              int (*setDrawOptions)(void *userData,
//	                                                    int index), 
//	                              void (*setDrawInterpOptions)(void *userData,
//	                                                           int index,
//	                                                           float t),
//	                              void *userData);

    /* Release reference to the DerivedMesh. This function decides internally
     * if the DerivedMesh will be freed, or cached for later use. */
    public static interface Release {
        public void run(DerivedMesh dm);
    };

    public Release release;

///* convert layers requested by a GLSL material to actually available layers in
// * the DerivedMesh, with both a pointer for arrays and an offset for editmesh */
//typedef struct DMVertexAttribs {
//	struct {
//		struct MTFace *array;
//		int emOffset, glIndex;
//	} tface[MAX_MTFACE];
//
//	struct {
//		struct MCol *array;
//		int emOffset, glIndex;
//	} mcol[MAX_MCOL];
//
//	struct {
//		float (*array)[3];
//		int emOffset, glIndex;
//	} tang;
//
//	struct {
//		float (*array)[3];
//		int emOffset, glIndex;
//	} orco;
//
//	int tottface, totmcol, tottang, totorco;
//} DMVertexAttribs;

    ///////////////////////////////////
    ///////////////////////////////////

    public static GetArray<MVert[]> dm_getVertArray = new GetArray<MVert[]>() {
        public MVert[] run(DerivedMesh dm) {
            MVert[] mvert = ((MVert) CustomDataUtil.CustomData_get_layer(dm.vertData, CustomDataTypes.CD_MVERT)).myarray;
            if (mvert == null) {
                mvert = ((MVert) CustomDataUtil.CustomData_add_layer(dm.vertData, CustomDataTypes.CD_MVERT, CustomDataUtil.CD_CALLOC, null, dm.getNumVerts.run(dm))).myarray;
                CustomDataUtil.CustomData_set_layer_flag(dm.vertData, CustomDataTypes.CD_MVERT, CustomDataTypes.CD_FLAG_TEMPORARY);
                dm.copyVertArray.run(dm, mvert);
            }
            return mvert;
        }
    };

    public static GetArray<MEdge[]> dm_getEdgeArray = new GetArray<MEdge[]>() {
        public MEdge[] run(DerivedMesh dm) {
            MEdge[] medge = ((MEdge) CustomDataUtil.CustomData_get_layer(dm.edgeData, CustomDataTypes.CD_MEDGE)).myarray;
            if (medge == null) {
                medge = ((MEdge) CustomDataUtil.CustomData_add_layer(dm.edgeData, CustomDataTypes.CD_MEDGE, CustomDataUtil.CD_CALLOC, null, dm.getNumEdges.run(dm))).myarray;
                CustomDataUtil.CustomData_set_layer_flag(dm.edgeData, CustomDataTypes.CD_MEDGE, CustomDataTypes.CD_FLAG_TEMPORARY);
                dm.copyEdgeArray.run(dm, medge);
            }
            return medge;
        }
    };

    public static GetArray<MFace[]> dm_getFaceArray = new GetArray<MFace[]>() {
        public MFace[] run(DerivedMesh dm) {
            MFace[] mface = ((MFace) CustomDataUtil.CustomData_get_layer(dm.faceData, CustomDataTypes.CD_MFACE)).myarray;
            if (mface == null) {
                mface = ((MFace) CustomDataUtil.CustomData_add_layer(dm.faceData, CustomDataTypes.CD_MFACE, CustomDataUtil.CD_CALLOC, null, dm.getNumFaces.run(dm))).myarray;
                CustomDataUtil.CustomData_set_layer_flag(dm.faceData, CustomDataTypes.CD_MFACE, CustomDataTypes.CD_FLAG_TEMPORARY);
                dm.copyFaceArray.run(dm, mface);
            }
            return mface;
        }
    };

//static MVert *dm_dupVertArray(DerivedMesh *dm)
//{
//	MVert *tmp = MEM_callocN(sizeof(*tmp) * dm.getNumVerts(dm),
//	                         "dm_dupVertArray tmp");
//
//	if(tmp) dm.copyVertArray(dm, tmp);
//
//	return tmp;
//}
//
//static MEdge *dm_dupEdgeArray(DerivedMesh *dm)
//{
//	MEdge *tmp = MEM_callocN(sizeof(*tmp) * dm.getNumEdges(dm),
//	                         "dm_dupEdgeArray tmp");
//
//	if(tmp) dm.copyEdgeArray(dm, tmp);
//
//	return tmp;
//}
//
//static MFace *dm_dupFaceArray(DerivedMesh *dm)
//{
//	MFace *tmp = MEM_callocN(sizeof(*tmp) * dm.getNumFaces(dm),
//	                         "dm_dupFaceArray tmp");
//
//	if(tmp) dm.copyFaceArray(dm, tmp);
//
//	return tmp;
//}

    public static void DM_init_funcs(DerivedMesh dm) {
        /* default function implementations */
        dm.getVertArray = dm_getVertArray;
        dm.getEdgeArray = dm_getEdgeArray;
        dm.getFaceArray = dm_getFaceArray;
//	dm.dupVertArray = dm_dupVertArray;
//	dm.dupEdgeArray = dm_dupEdgeArray;
//	dm.dupFaceArray = dm_dupFaceArray;
//
//	dm.getVertData = DM_get_vert_data;
//	dm.getEdgeData = DM_get_edge_data;
//	dm.getFaceData = DM_get_face_data;
//	dm.getVertDataArray = DM_get_vert_data_layer;
//	dm.getEdgeDataArray = DM_get_edge_data_layer;
//	dm.getFaceDataArray = DM_get_face_data_layer;

//        bvhcache_init(dm.bvhCache);
    }

    public static void DM_init(DerivedMesh dm, int numVerts, int numEdges, int numFaces) {
        dm.numVertData = numVerts;
        dm.numEdgeData = numEdges;
        dm.numFaceData = numFaces;
        DM_init_funcs(dm);
        dm.needsFree = 1;
    }

//void DM_from_template(DerivedMesh *dm, DerivedMesh *source,
//                      int numVerts, int numEdges, int numFaces)
//{
//	CustomData_copy(&source.vertData, &dm.vertData, CD_MASK_DERIVEDMESH,
//	                CD_CALLOC, numVerts);
//	CustomData_copy(&source.edgeData, &dm.edgeData, CD_MASK_DERIVEDMESH,
//	                CD_CALLOC, numEdges);
//	CustomData_copy(&source.faceData, &dm.faceData, CD_MASK_DERIVEDMESH,
//	                CD_CALLOC, numFaces);
//
//	dm.numVertData = numVerts;
//	dm.numEdgeData = numEdges;
//	dm.numFaceData = numFaces;
//
//	DM_init_funcs(dm);
//
//	dm.needsFree = 1;
//}

    public static boolean DM_release(DerivedMesh dm) {
        if (dm.needsFree != 0) {
//            bvhcache_free(dm.bvhCache);
            CustomDataUtil.CustomData_free(dm.vertData, dm.numVertData);
            CustomDataUtil.CustomData_free(dm.edgeData, dm.numEdgeData);
            CustomDataUtil.CustomData_free(dm.faceData, dm.numFaceData);
            return true;
        } else {
            CustomDataUtil.CustomData_free_temporary(dm.vertData, dm.numVertData);
            CustomDataUtil.CustomData_free_temporary(dm.edgeData, dm.numEdgeData);
            CustomDataUtil.CustomData_free_temporary(dm.faceData, dm.numFaceData);
            return false;
        }
    }

//void DM_to_mesh(DerivedMesh *dm, Mesh *me)
//{
//	/* dm might depend on me, so we need to do everything with a local copy */
//	Mesh tmp = *me;
//	int totvert, totedge, totface;
//
//	memset(&tmp.vdata, 0, sizeof(tmp.vdata));
//	memset(&tmp.edata, 0, sizeof(tmp.edata));
//	memset(&tmp.fdata, 0, sizeof(tmp.fdata));
//
//	totvert = tmp.totvert = dm.getNumVerts(dm);
//	totedge = tmp.totedge = dm.getNumEdges(dm);
//	totface = tmp.totface = dm.getNumFaces(dm);
//
//	CustomData_copy(&dm.vertData, &tmp.vdata, CD_MASK_MESH, CD_DUPLICATE, totvert);
//	CustomData_copy(&dm.edgeData, &tmp.edata, CD_MASK_MESH, CD_DUPLICATE, totedge);
//	CustomData_copy(&dm.faceData, &tmp.fdata, CD_MASK_MESH, CD_DUPLICATE, totface);
//
//	/* not all DerivedMeshes store their verts/edges/faces in CustomData, so
//	   we set them here in case they are missing */
//	if(!CustomData_has_layer(&tmp.vdata, CD_MVERT))
//		CustomData_add_layer(&tmp.vdata, CD_MVERT, CD_ASSIGN, dm.dupVertArray(dm), totvert);
//	if(!CustomData_has_layer(&tmp.edata, CD_MEDGE))
//		CustomData_add_layer(&tmp.edata, CD_MEDGE, CD_ASSIGN, dm.dupEdgeArray(dm), totedge);
//	if(!CustomData_has_layer(&tmp.fdata, CD_MFACE))
//		CustomData_add_layer(&tmp.fdata, CD_MFACE, CD_ASSIGN, dm.dupFaceArray(dm), totface);
//
//	mesh_update_customdata_pointers(&tmp);
//
//	CustomData_free(&me.vdata, me.totvert);
//	CustomData_free(&me.edata, me.totedge);
//	CustomData_free(&me.fdata, me.totface);
//
//	/* if the number of verts has changed, remove invalid data */
//	if(tmp.totvert != me.totvert) {
//		if(me.key) me.key.id.us--;
//		me.key = NULL;
//	}
//
//	*me = tmp;
//}
//
//void DM_set_only_copy(DerivedMesh *dm, CustomDataMask mask)
//{
//	CustomData_set_only_copy(&dm.vertData, mask);
//	CustomData_set_only_copy(&dm.edgeData, mask);
//	CustomData_set_only_copy(&dm.faceData, mask);
//}
//
//void DM_add_vert_layer(DerivedMesh *dm, int type, int alloctype, void *layer)
//{
//	CustomData_add_layer(&dm.vertData, type, alloctype, layer, dm.numVertData);
//}
//
//void DM_add_edge_layer(DerivedMesh *dm, int type, int alloctype, void *layer)
//{
//	CustomData_add_layer(&dm.edgeData, type, alloctype, layer, dm.numEdgeData);
//}
//
//void DM_add_face_layer(DerivedMesh *dm, int type, int alloctype, void *layer)
//{
//	CustomData_add_layer(&dm.faceData, type, alloctype, layer, dm.numFaceData);
//}
//
//void *DM_get_vert_data(DerivedMesh *dm, int index, int type)
//{
//	return CustomData_get(&dm.vertData, index, type);
//}
//
//void *DM_get_edge_data(DerivedMesh *dm, int index, int type)
//{
//	return CustomData_get(&dm.edgeData, index, type);
//}
//
//void *DM_get_face_data(DerivedMesh *dm, int index, int type)
//{
//	return CustomData_get(&dm.faceData, index, type);
//}

    public static GetDataArray DM_get_vert_data_layer = new GetDataArray() {
        public Object run(DerivedMesh dm, int type) {
            return CustomDataUtil.CustomData_get_layer(dm.vertData, type);
        }
    };

    public static GetDataArray DM_get_edge_data_layer = new GetDataArray() {
        public Object run(DerivedMesh dm, int type) {
            return CustomDataUtil.CustomData_get_layer(dm.edgeData, type);
        }
    };

    public static GetDataArray DM_get_face_data_layer = new GetDataArray() {
        public Object run(DerivedMesh dm, int type) {
            return CustomDataUtil.CustomData_get_layer(dm.faceData, type);
        }
    };

//void DM_set_vert_data(DerivedMesh *dm, int index, int type, void *data)
//{
//	CustomData_set(&dm.vertData, index, type, data);
//}
//
//void DM_set_edge_data(DerivedMesh *dm, int index, int type, void *data)
//{
//	CustomData_set(&dm.edgeData, index, type, data);
//}
//
//void DM_set_face_data(DerivedMesh *dm, int index, int type, void *data)
//{
//	CustomData_set(&dm.faceData, index, type, data);
//}
//
//void DM_copy_vert_data(DerivedMesh *source, DerivedMesh *dest,
//                       int source_index, int dest_index, int count)
//{
//	CustomData_copy_data(&source.vertData, &dest.vertData,
//	                     source_index, dest_index, count);
//}
//
//void DM_copy_edge_data(DerivedMesh *source, DerivedMesh *dest,
//                       int source_index, int dest_index, int count)
//{
//	CustomData_copy_data(&source.edgeData, &dest.edgeData,
//	                     source_index, dest_index, count);
//}
//
//void DM_copy_face_data(DerivedMesh *source, DerivedMesh *dest,
//                       int source_index, int dest_index, int count)
//{
//	CustomData_copy_data(&source.faceData, &dest.faceData,
//	                     source_index, dest_index, count);
//}
//
//void DM_free_vert_data(struct DerivedMesh *dm, int index, int count)
//{
//	CustomData_free_elem(&dm.vertData, index, count);
//}
//
//void DM_free_edge_data(struct DerivedMesh *dm, int index, int count)
//{
//	CustomData_free_elem(&dm.edgeData, index, count);
//}
//
//void DM_free_face_data(struct DerivedMesh *dm, int index, int count)
//{
//	CustomData_free_elem(&dm.faceData, index, count);
//}
//
//void DM_interp_vert_data(DerivedMesh *source, DerivedMesh *dest,
//                         int *src_indices, float *weights,
//                         int count, int dest_index)
//{
//	CustomData_interp(&source.vertData, &dest.vertData, src_indices,
//	                  weights, NULL, count, dest_index);
//}
//
//void DM_interp_edge_data(DerivedMesh *source, DerivedMesh *dest,
//                         int *src_indices,
//                         float *weights, EdgeVertWeight *vert_weights,
//                         int count, int dest_index)
//{
//	CustomData_interp(&source.edgeData, &dest.edgeData, src_indices,
//	                  weights, (float*)vert_weights, count, dest_index);
//}
//
//void DM_interp_face_data(DerivedMesh *source, DerivedMesh *dest,
//                         int *src_indices,
//                         float *weights, FaceVertWeight *vert_weights,
//                         int count, int dest_index)
//{
//	CustomData_interp(&source.faceData, &dest.faceData, src_indices,
//	                  weights, (float*)vert_weights, count, dest_index);
//}
//
//void DM_swap_face_data(DerivedMesh *dm, int index, int *corner_indices)
//{
//	CustomData_swap(&dm.faceData, index, corner_indices);
//}
//
/////
//
//static DerivedMesh *getMeshDerivedMesh(Mesh *me, Object *ob, float (*vertCos)[3])
//{
//	DerivedMesh *dm = CDDM_from_mesh(me, ob);
//
//	if(!dm)
//		return NULL;
//
//	if (vertCos)
//		CDDM_apply_vert_coords(dm, vertCos);
//
//	CDDM_calc_normals(dm);
//
//	return dm;
//}

public static class EditMeshDerivedMesh extends DerivedMesh {
	public DerivedMesh dm = this;

	public EditMesh em;
	public float[][] vertexCos;
        public float[][] vertexNos;
        public float[][] faceNos;
};

    public static ForeachMappedVert emDM_foreachMappedVert = new ForeachMappedVert() {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedVertFunc func, Object userData) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            EditVert eve;
            int i;

            for (i = 0, eve = emdm.em.verts.first; eve != null; i++, eve = eve.next) {
                if (emdm.vertexCos != null) {
                    func.func(gl, userData, i, emdm.vertexCos[i], emdm.vertexNos[i], null);
                } else {
                    func.func(gl, userData, i, eve.co, eve.no, null);
                }
            }
        }
    };

    public static ForeachMappedEdge emDM_foreachMappedEdge = new ForeachMappedEdge() {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedEdgeFunc func, Object userData) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            EditEdge eed;
            int i;

            if (emdm.vertexCos != null) {
                EditVert eve;

                for (i = 0, eve = emdm.em.verts.first; eve != null; eve = eve.next) {
                    eve.tmp.l(i++);
                }
                for (i = 0, eed = emdm.em.edges.first; eed != null; i++, eed = eed.next) {
                    func.func(gl, userData, i, emdm.vertexCos[(int) eed.v1.tmp.l()], emdm.vertexCos[(int) eed.v2.tmp.l()]);
                }
            } else {
                for (i = 0, eed = emdm.em.edges.first; eed != null; i++, eed = eed.next) {
                    func.func(gl, userData, i, eed.v1.co, eed.v2.co);
                }
            }
        }
    };

    public static DrawMappedEdges emDM_drawMappedEdges = new DrawMappedEdges() {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedEdgesFunc setDrawOptions, Object userData) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            EditEdge eed;
            int i;

            if (emdm.vertexCos != null) {
                EditVert eve;

                for (i = 0, eve = (EditVert) emdm.em.verts.first; eve != null; eve = eve.next) {
                    eve.tmp.l(i++);
                }

                gl.glBegin(GL2.GL_LINES);
                for (i = 0, eed = (EditEdge) emdm.em.edges.first; eed != null; i++, eed = eed.next) {
                    if (setDrawOptions == null || setDrawOptions.run(gl, userData, i)) {
                        gl.glVertex3fv(emdm.vertexCos[(int) eed.next.tmp.l()], 0);
                        gl.glVertex3fv(emdm.vertexCos[(int) eed.v2.tmp.l()], 0);
                    }
                }
                gl.glEnd();
            } else {
                gl.glBegin(GL2.GL_LINES);
                for (i = 0, eed = (EditEdge) emdm.em.edges.first; eed != null; i++, eed = eed.next) {
                    if (setDrawOptions == null || setDrawOptions.run(gl, userData, i)) {
                        gl.glVertex3fv(eed.v1.co, 0);
                        gl.glVertex3fv(eed.v2.co, 0);
                    }
                }
                gl.glEnd();
            }
        }
    };

    public static DrawEdges emDM_drawEdges = new DrawEdges() {
        public void run(GL2 gl, DerivedMesh dm, boolean drawLooseEdges) {
            emDM_drawMappedEdges.run(gl, dm, null, null);
        }
    };

//static void emDM_drawMappedEdgesInterp(DerivedMesh *dm, int (*setDrawOptions)(void *userData, int index), void (*setDrawInterpOptions)(void *userData, int index, float t), void *userData)
//{
//	EditMeshDerivedMesh *emdm= (EditMeshDerivedMesh*) dm;
//	EditEdge *eed;
//	int i;
//
//	if (emdm.vertexCos) {
//		EditVert *eve;
//
//		for (i=0,eve=emdm.em.verts.first; eve; eve= eve.next)
//			eve.tmp.l = (intptr_t) i++;
//
//		glBegin(GL_LINES);
//		for (i=0,eed= emdm.em.edges.first; eed; i++,eed= eed.next) {
//			if(!setDrawOptions || setDrawOptions(userData, i)) {
//				setDrawInterpOptions(userData, i, 0.0);
//				glVertex3fv(emdm.vertexCos[(int) eed.v1.tmp.l]);
//				setDrawInterpOptions(userData, i, 1.0);
//				glVertex3fv(emdm.vertexCos[(int) eed.v2.tmp.l]);
//			}
//		}
//		glEnd();
//	} else {
//		glBegin(GL_LINES);
//		for (i=0,eed= emdm.em.edges.first; eed; i++,eed= eed.next) {
//			if(!setDrawOptions || setDrawOptions(userData, i)) {
//				setDrawInterpOptions(userData, i, 0.0);
//				glVertex3fv(eed.v1.co);
//				setDrawInterpOptions(userData, i, 1.0);
//				glVertex3fv(eed.v2.co);
//			}
//		}
//		glEnd();
//	}
//}
//
//static void emDM_drawUVEdges(DerivedMesh *dm)
//{
//	EditMeshDerivedMesh *emdm= (EditMeshDerivedMesh*) dm;
//	EditFace *efa;
//	MTFace *tf;
//
//	glBegin(GL_LINES);
//	for(efa= emdm.em.faces.first; efa; efa= efa.next) {
//		tf = CustomData_em_get(&emdm.em.fdata, efa.data, CD_MTFACE);
//
//		if(tf && !(efa.h)) {
//			glVertex2fv(tf.uv[0]);
//			glVertex2fv(tf.uv[1]);
//
//			glVertex2fv(tf.uv[1]);
//			glVertex2fv(tf.uv[2]);
//
//			if (!efa.v4) {
//				glVertex2fv(tf.uv[2]);
//				glVertex2fv(tf.uv[0]);
//			} else {
//				glVertex2fv(tf.uv[2]);
//				glVertex2fv(tf.uv[3]);
//				glVertex2fv(tf.uv[3]);
//				glVertex2fv(tf.uv[0]);
//			}
//		}
//	}
//	glEnd();
//}

    public static void emDM__calcFaceCent(EditFace efa, float[] cent, float[][] vertexCos) {
        if (vertexCos != null) {
            UtilDefines.VECCOPY(cent, vertexCos[(int) efa.v1.tmp.l()]);
            Arithb.VecAddf(cent, cent, vertexCos[(int) efa.v2.tmp.l()]);
            Arithb.VecAddf(cent, cent, vertexCos[(int) efa.v3.tmp.l()]);
            if (efa.v4 != null) {
                Arithb.VecAddf(cent, cent, vertexCos[(int) efa.v4.tmp.l()]);
            }
        } else {
            UtilDefines.VECCOPY(cent, efa.v1.co);
            Arithb.VecAddf(cent, cent, efa.v2.co);
            Arithb.VecAddf(cent, cent, efa.v3.co);
            if (efa.v4 != null) {
                Arithb.VecAddf(cent, cent, efa.v4.co);
            }
        }

        if (efa.v4 != null) {
            Arithb.VecMulf(cent, 0.25f);
        } else {
            Arithb.VecMulf(cent, 0.33333333333f);
        }
    }

    public static ForeachMappedFaceCenter emDM_foreachMappedFaceCenter = new ForeachMappedFaceCenter() {
        public void run(GL2 gl, DerivedMesh dm, ForeachMappedFaceCenterFunc func, Object userData) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            EditVert eve;
            EditFace efa;
            float[] cent = new float[3];
            int i;

            if (emdm.vertexCos != null) {
                for (i = 0, eve = emdm.em.verts.first; eve != null; eve = eve.next) {
                    eve.tmp.l(i++);
                }
            }

            for (i = 0, efa = emdm.em.faces.first; efa != null; i++, efa = efa.next) {
                emDM__calcFaceCent(efa, cent, emdm.vertexCos);
                func.func(gl, userData, i, cent, emdm.vertexCos != null ? emdm.faceNos[i] : efa.n);
            }
        }
    };

    public static DrawMappedFaces emDM_drawMappedFaces = new DrawMappedFaces() {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedFacesFunc setDrawOptions, Object userData, int useColors) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            EditFace efa;
            int i, draw;

            if (emdm.vertexCos != null) {
                EditVert eve;

                for (i = 0, eve = emdm.em.verts.first; eve != null; eve = eve.next) {
                    eve.tmp.l(i++);
                }

                for (i = 0, efa = (EditFace) emdm.em.faces.first; efa != null; i++, efa = efa.next) {
                    int[] drawSmooth = {efa.flag & MeshDataTypes.ME_SMOOTH};
                    draw = setDrawOptions == null ? 1 : setDrawOptions.run(gl, userData, i, drawSmooth);
                    if (draw != 0) {
                        if (draw == 2) { /* enabled with stipple */
                            gl.glEnable(GL2.GL_POLYGON_STIPPLE);
                            gl.glPolygonStipple(GlUtil.stipple_quarttone, 0);
                        }

                        gl.glShadeModel(drawSmooth[0] != 0 ? GL2.GL_SMOOTH : GL2.GL_FLAT);

                        gl.glBegin(efa.v4 != null ? GL2.GL_QUADS : GL2.GL_TRIANGLES);
                        if (drawSmooth[0] == 0) {
                            gl.glNormal3fv(emdm.faceNos[i], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v1.tmp.l()], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v2.tmp.l()], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v3.tmp.l()], 0);
                            if (efa.v4 != null) {
                                gl.glVertex3fv(emdm.vertexCos[(int) efa.v4.tmp.l()], 0);
                            }
                        } else {
                            gl.glNormal3fv(emdm.vertexNos[(int) efa.v1.tmp.l()], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v1.tmp.l()], 0);
                            gl.glNormal3fv(emdm.vertexNos[(int) efa.v2.tmp.l()], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v2.tmp.l()], 0);
                            gl.glNormal3fv(emdm.vertexNos[(int) efa.v3.tmp.l()], 0);
                            gl.glVertex3fv(emdm.vertexCos[(int) efa.v3.tmp.l()], 0);
                            if (efa.v4 != null) {
                                gl.glNormal3fv(emdm.vertexNos[(int) efa.v4.tmp.l()], 0);
                                gl.glVertex3fv(emdm.vertexCos[(int) efa.v4.tmp.l()], 0);
                            }
                        }
                        gl.glEnd();

                        if (draw == 2) {
                            gl.glDisable(GL2.GL_POLYGON_STIPPLE);
                        }
                    }
                }
            } else {
                for (i = 0, efa = (EditFace) emdm.em.faces.first; efa != null; i++, efa = efa.next) {
                    int[] drawSmooth = {efa.flag & MeshDataTypes.ME_SMOOTH};
                    draw = setDrawOptions == null ? 1 : setDrawOptions.run(gl, userData, i, drawSmooth);
                    if (draw != 0) {
                        if (draw == 2) { /* enabled with stipple */
                            gl.glEnable(GL2.GL_POLYGON_STIPPLE);
                            gl.glPolygonStipple(GlUtil.stipple_quarttone, 0);
                        }
                        gl.glShadeModel(drawSmooth[0] != 0 ? GL2.GL_SMOOTH : GL2.GL_FLAT);

                        gl.glBegin(efa.v4 != null ? GL2.GL_QUADS : GL2.GL_TRIANGLES);
                        if (drawSmooth[0] == 0) {
                            gl.glNormal3fv(efa.n, 0);
                            gl.glVertex3fv(efa.v1.co, 0);
                            gl.glVertex3fv(efa.v2.co, 0);
                            gl.glVertex3fv(efa.v3.co, 0);
                            if (efa.v4 != null) {
                                gl.glVertex3fv(efa.v4.co, 0);
                            }
                        } else {
                            gl.glNormal3fv(efa.v1.no, 0);
                            gl.glVertex3fv(efa.v1.co, 0);
                            gl.glNormal3fv(efa.v2.no, 0);
                            gl.glVertex3fv(efa.v2.co, 0);
                            gl.glNormal3fv(efa.v3.no, 0);
                            gl.glVertex3fv(efa.v3.co, 0);
                            if (efa.v4 != null) {
                                gl.glNormal3fv(efa.v4.no, 0);
                                gl.glVertex3fv(efa.v4.co, 0);
                            }
                        }
                        gl.glEnd();

                        if (draw == 2) {
                            gl.glDisable(GL2.GL_POLYGON_STIPPLE);
                        }
                    }
                }
            }
        }
    };

    private static void emDM_drawFacesTex_common(GL2 gl, DerivedMesh dm) //,
    //               int (*drawParams)(MTFace *tface, MCol *mcol, int matnr),
    //               int (*drawParamsMapped)(void *userData, int index),
    //               void *userData)
    {
        EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
        EditMesh em = emdm.em;
        float[][] vertexCos = emdm.vertexCos;
        float[][] vertexNos = emdm.vertexNos;
        EditFace efa;
        int i;

        /* always use smooth shading even for flat faces, else vertex colors wont interpolate */
        gl.glShadeModel(GL2.GL_SMOOTH);

        if (vertexCos != null) {
//		EditVert *eve;
//
//		for (i=0,eve=em.verts.first; eve; eve= eve.next)
//			eve.tmp.l = (intptr_t) i++;

            for (i = 0, efa = (EditFace) em.faces.first; efa != null; i++, efa = efa.next) {
                MTFace tf = (MTFace) CustomDataUtil.CustomData_em_get(em.fdata, efa.data, CustomDataTypes.CD_MTFACE);
//                MCol *mcol= CustomData_em_get(&em.fdata, efa.data, CD_MCOL);
                byte[] cp = null;
                boolean drawSmooth = ((efa.flag & MeshDataTypes.ME_SMOOTH) != 0);
//                int flag;
//
//                if(drawParams)
//                        flag= drawParams(tf, mcol, efa.mat_nr);
//                else if(drawParamsMapped)
//                        flag= drawParamsMapped(userData, i);
//                else
//                        flag= 1;
//
//                if(flag != 0) { /* flag 0 == the face is hidden or invisible */
//
//                        /* we always want smooth here since otherwise vertex colors dont interpolate */
//                        if (mcol) {
//                                if (flag==1) {
//                                        cp= (unsigned char*)mcol;
//                                }
//                        } else {
//                                glShadeModel(drawSmooth?GL_SMOOTH:GL_FLAT);
//                        }

                gl.glBegin(efa.v4 != null ? GL2.GL_QUADS : GL2.GL_TRIANGLES);
                if (!drawSmooth) {
                    gl.glNormal3fv(emdm.faceNos[i], 0);

                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[0], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[3], cp[2], cp[1]);
                    }
                    gl.glVertex3fv(vertexCos[(int) efa.v1.tmp.l()], 0);

                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[1], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[7], cp[6], cp[5]);
                    }
                    gl.glVertex3fv(vertexCos[(int) efa.v2.tmp.l()], 0);

                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[2], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[11], cp[10], cp[9]);
                    }
                    gl.glVertex3fv(vertexCos[(int) efa.v3.tmp.l()], 0);

                    if (efa.v4 != null) {
                        if (tf != null) {
                            gl.glTexCoord2fv(tf.uv[3], 0);
                        }
                        if (cp != null) {
                            gl.glColor3ub(cp[15], cp[14], cp[13]);
                        }
                        gl.glVertex3fv(vertexCos[(int) efa.v4.tmp.l()], 0);
                    }
                } else {
                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[0], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[3], cp[2], cp[1]);
                    }
                    gl.glNormal3fv(vertexNos[(int) efa.v1.tmp.l()], 0);
                    gl.glVertex3fv(vertexCos[(int) efa.v1.tmp.l()], 0);

                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[1], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[7], cp[6], cp[5]);
                    }
                    gl.glNormal3fv(vertexNos[(int) efa.v2.tmp.l()], 0);
                    gl.glVertex3fv(vertexCos[(int) efa.v2.tmp.l()], 0);

                    if (tf != null) {
                        gl.glTexCoord2fv(tf.uv[2], 0);
                    }
                    if (cp != null) {
                        gl.glColor3ub(cp[11], cp[10], cp[9]);
                    }
                    gl.glNormal3fv(vertexNos[(int) efa.v3.tmp.l()], 0);
                    gl.glVertex3fv(vertexCos[(int) efa.v3.tmp.l()], 0);

                    if (efa.v4 != null) {
                        if (tf != null) {
                            gl.glTexCoord2fv(tf.uv[3], 0);
                        }
                        if (cp != null) {
                            gl.glColor3ub(cp[15], cp[14], cp[13]);
                        }
                        gl.glNormal3fv(vertexNos[(int) efa.v4.tmp.l()], 0);
                        gl.glVertex3fv(vertexCos[(int) efa.v4.tmp.l()], 0);
                    }
                }
                gl.glEnd();
//			}
            }
        } else {
//		for (i=0,efa= em.faces.first; efa; i++,efa= efa.next) {
//			MTFace *tf= CustomData_em_get(&em.fdata, efa.data, CD_MTFACE);
//			MCol *mcol= CustomData_em_get(&em.fdata, efa.data, CD_MCOL);
//			unsigned char *cp= NULL;
//			int drawSmooth= (efa.flag & ME_SMOOTH);
//			int flag;
//
//			if(drawParams)
//				flag= drawParams(tf, mcol, efa.mat_nr);
//			else if(drawParamsMapped)
//				flag= drawParamsMapped(userData, i);
//			else
//				flag= 1;
//
//			if(flag != 0) { /* flag 0 == the face is hidden or invisible */
//				/* we always want smooth here since otherwise vertex colors dont interpolate */
//				if (mcol) {
//					if (flag==1) {
//						cp= (unsigned char*)mcol;
//					}
//				} else {
//					glShadeModel(drawSmooth?GL_SMOOTH:GL_FLAT);
//				}
//
//				glBegin(efa.v4?GL_QUADS:GL_TRIANGLES);
//				if (!drawSmooth) {
//					glNormal3fv(efa.n);
//
//					if(tf) glTexCoord2fv(tf.uv[0]);
//					if(cp) glColor3ub(cp[3], cp[2], cp[1]);
//					glVertex3fv(efa.v1.co);
//
//					if(tf) glTexCoord2fv(tf.uv[1]);
//					if(cp) glColor3ub(cp[7], cp[6], cp[5]);
//					glVertex3fv(efa.v2.co);
//
//					if(tf) glTexCoord2fv(tf.uv[2]);
//					if(cp) glColor3ub(cp[11], cp[10], cp[9]);
//					glVertex3fv(efa.v3.co);
//
//					if(efa.v4) {
//						if(tf) glTexCoord2fv(tf.uv[3]);
//						if(cp) glColor3ub(cp[15], cp[14], cp[13]);
//						glVertex3fv(efa.v4.co);
//					}
//				} else {
//					if(tf) glTexCoord2fv(tf.uv[0]);
//					if(cp) glColor3ub(cp[3], cp[2], cp[1]);
//					glNormal3fv(efa.v1.no);
//					glVertex3fv(efa.v1.co);
//
//					if(tf) glTexCoord2fv(tf.uv[1]);
//					if(cp) glColor3ub(cp[7], cp[6], cp[5]);
//					glNormal3fv(efa.v2.no);
//					glVertex3fv(efa.v2.co);
//
//					if(tf) glTexCoord2fv(tf.uv[2]);
//					if(cp) glColor3ub(cp[11], cp[10], cp[9]);
//					glNormal3fv(efa.v3.no);
//					glVertex3fv(efa.v3.co);
//
//					if(efa.v4) {
//						if(tf) glTexCoord2fv(tf.uv[3]);
//						if(cp) glColor3ub(cp[15], cp[14], cp[13]);
//						glNormal3fv(efa.v4.no);
//						glVertex3fv(efa.v4.co);
//					}
//				}
//				glEnd();
//			}
//		}
        }
    }

    public static DrawFacesTex emDM_drawFacesTex = new DrawFacesTex() {
        public void run(GL2 gl, DerivedMesh dm) //, int (*setDrawOptions)(MTFace *tface, MCol *mcol, int matnr))
        {
            emDM_drawFacesTex_common(gl, dm); //, setDrawOptions, NULL, NULL);
        }
    };

    public static DrawMappedFacesTex emDM_drawMappedFacesTex = new DrawMappedFacesTex() {
        public void run(GL2 gl, DerivedMesh dm) //, int (*setDrawOptions)(void *userData, int index), void *userData)
        {
            emDM_drawFacesTex_common(gl, dm); //, null, setDrawOptions, userData);
        }
    };

//static void emDM_drawMappedFacesGLSL(DerivedMesh *dm,
//               int (*setMaterial)(int, void *attribs),
//               int (*setDrawOptions)(void *userData, int index), void *userData)
//{
//	EditMeshDerivedMesh *emdm= (EditMeshDerivedMesh*) dm;
//	EditMesh *em= emdm.em;
//	float (*vertexCos)[3]= emdm.vertexCos;
//	float (*vertexNos)[3]= emdm.vertexNos;
//	EditVert *eve;
//	EditFace *efa;
//	DMVertexAttribs attribs;
//	GPUVertexAttribs gattribs;
//	MTFace *tf;
//	int transp, new_transp, orig_transp, tfoffset;
//	int i, b, matnr, new_matnr, dodraw, layer;
//
//	dodraw = 0;
//	matnr = -1;
//
//	transp = GPU_get_material_blend_mode();
//	orig_transp = transp;
//	layer = CustomData_get_layer_index(&em.fdata, CD_MTFACE);
//	tfoffset = (layer == -1)? -1: em.fdata.layers[layer].offset;
//
//	memset(&attribs, 0, sizeof(attribs));
//
//	/* always use smooth shading even for flat faces, else vertex colors wont interpolate */
//	glShadeModel(GL_SMOOTH);
//
//	for (i=0,eve=em.verts.first; eve; eve= eve.next)
//		eve.tmp.l = (intptr_t) i++;
//
//#define PASSATTRIB(efa, eve, vert) {											\
//	if(attribs.totorco) {														\
//		float *orco = attribs.orco.array[eve.tmp.l];							\
//		glVertexAttrib3fvARB(attribs.orco.glIndex, orco);						\
//	}																			\
//	for(b = 0; b < attribs.tottface; b++) {										\
//		MTFace *_tf = (MTFace*)((char*)efa.data + attribs.tface[b].emOffset);	\
//		glVertexAttrib2fvARB(attribs.tface[b].glIndex, _tf.uv[vert]);			\
//	}																			\
//	for(b = 0; b < attribs.totmcol; b++) {										\
//		MCol *cp = (MCol*)((char*)efa.data + attribs.mcol[b].emOffset);		\
//		GLubyte col[4];															\
//		col[0]= cp.b; col[1]= cp.g; col[2]= cp.r; col[3]= cp.a;				\
//		glVertexAttrib4ubvARB(attribs.mcol[b].glIndex, col);					\
//	}																			\
//	if(attribs.tottang) {														\
//		float *tang = attribs.tang.array[i*4 + vert];							\
//		glVertexAttrib3fvARB(attribs.tang.glIndex, tang);						\
//	}																			\
//}
//
//	for (i=0,efa= em.faces.first; efa; i++,efa= efa.next) {
//		int drawSmooth= (efa.flag & ME_SMOOTH);
//
//		if(setDrawOptions && !setDrawOptions(userData, i))
//			continue;
//
//		new_matnr = efa.mat_nr + 1;
//		if(new_matnr != matnr) {
//			dodraw = setMaterial(matnr = new_matnr, &gattribs);
//			if(dodraw)
//				DM_vertex_attributes_from_gpu(dm, &gattribs, &attribs);
//		}
//
//		if(tfoffset != -1) {
//			tf = (MTFace*)((char*)efa.data)+tfoffset;
//			new_transp = tf.transp;
//
//			if(new_transp != transp) {
//				if(new_transp == GPU_BLEND_SOLID && orig_transp != GPU_BLEND_SOLID)
//					GPU_set_material_blend_mode(orig_transp);
//				else
//					GPU_set_material_blend_mode(new_transp);
//				transp = new_transp;
//			}
//		}
//
//		if(dodraw) {
//			glBegin(efa.v4?GL_QUADS:GL_TRIANGLES);
//			if (!drawSmooth) {
//				if(vertexCos) glNormal3fv(emdm.faceNos[i]);
//				else glNormal3fv(efa.n);
//
//				PASSATTRIB(efa, efa.v1, 0);
//				if(vertexCos) glVertex3fv(vertexCos[(int) efa.v1.tmp.l]);
//				else glVertex3fv(efa.v1.co);
//
//				PASSATTRIB(efa, efa.v2, 1);
//				if(vertexCos) glVertex3fv(vertexCos[(int) efa.v2.tmp.l]);
//				else glVertex3fv(efa.v2.co);
//
//				PASSATTRIB(efa, efa.v3, 2);
//				if(vertexCos) glVertex3fv(vertexCos[(int) efa.v3.tmp.l]);
//				else glVertex3fv(efa.v3.co);
//
//				if(efa.v4) {
//					PASSATTRIB(efa, efa.v4, 3);
//					if(vertexCos) glVertex3fv(vertexCos[(int) efa.v4.tmp.l]);
//					else glVertex3fv(efa.v4.co);
//				}
//			} else {
//				PASSATTRIB(efa, efa.v1, 0);
//				if(vertexCos) {
//					glNormal3fv(vertexNos[(int) efa.v1.tmp.l]);
//					glVertex3fv(vertexCos[(int) efa.v1.tmp.l]);
//				}
//				else {
//					glNormal3fv(efa.v1.no);
//					glVertex3fv(efa.v1.co);
//				}
//
//				PASSATTRIB(efa, efa.v2, 1);
//				if(vertexCos) {
//					glNormal3fv(vertexNos[(int) efa.v2.tmp.l]);
//					glVertex3fv(vertexCos[(int) efa.v2.tmp.l]);
//				}
//				else {
//					glNormal3fv(efa.v2.no);
//					glVertex3fv(efa.v2.co);
//				}
//
//				PASSATTRIB(efa, efa.v3, 2);
//				if(vertexCos) {
//					glNormal3fv(vertexNos[(int) efa.v3.tmp.l]);
//					glVertex3fv(vertexCos[(int) efa.v3.tmp.l]);
//				}
//				else {
//					glNormal3fv(efa.v3.no);
//					glVertex3fv(efa.v3.co);
//				}
//
//				if(efa.v4) {
//					PASSATTRIB(efa, efa.v4, 3);
//					if(vertexCos) {
//						glNormal3fv(vertexNos[(int) efa.v4.tmp.l]);
//						glVertex3fv(vertexCos[(int) efa.v4.tmp.l]);
//					}
//					else {
//						glNormal3fv(efa.v4.no);
//						glVertex3fv(efa.v4.co);
//					}
//				}
//			}
//			glEnd();
//		}
//	}
//}
//
//static void emDM_drawFacesGLSL(DerivedMesh *dm,
//               int (*setMaterial)(int, void *attribs))
//{
//	dm.drawMappedFacesGLSL(dm, setMaterial, NULL, NULL);
//}

    public static GetMinMax emDM_getMinMax = new GetMinMax() {
        public void run(DerivedMesh dm, float[] min_r, float[] max_r) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
//	EditVert *eve;
//	int i;
//
//	if (emdm.em.verts.first) {
//		for (i=0,eve= emdm.em.verts.first; eve; i++,eve= eve.next) {
//			if (emdm.vertexCos) {
//				DO_MINMAX(emdm.vertexCos[i], min_r, max_r);
//			} else {
//				DO_MINMAX(eve.co, min_r, max_r);
//			}
//		}
//	} else {
//		min_r[0] = min_r[1] = min_r[2] = max_r[0] = max_r[1] = max_r[2] = 0.0;
//	}
        }
    };

    public static GetNum emDM_getNumVerts = new GetNum() {
        public int run(DerivedMesh dm) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            return ListBaseUtil.BLI_countlist(emdm.em.verts);
        }
    };

    public static GetNum emDM_getNumEdges = new GetNum() {
        public int run(DerivedMesh dm) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            return ListBaseUtil.BLI_countlist(emdm.em.edges);
        }
    };

    public static GetNum emDM_getNumFaces = new GetNum() {
        public int run(DerivedMesh dm) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            return ListBaseUtil.BLI_countlist(emdm.em.faces);
        }
    };

//static void emDM_getVert(DerivedMesh *dm, int index, MVert *vert_r)
//{
//	EditVert *ev = ((EditMeshDerivedMesh *)dm).em.verts.first;
//	int i;
//
//	for(i = 0; i < index; ++i) ev = ev.next;
//
//	VECCOPY(vert_r.co, ev.co);
//
//	vert_r.no[0] = ev.no[0] * 32767.0;
//	vert_r.no[1] = ev.no[1] * 32767.0;
//	vert_r.no[2] = ev.no[2] * 32767.0;
//
//	/* TODO what to do with vert_r.flag and vert_r.mat_nr? */
//	vert_r.mat_nr = 0;
//	vert_r.bweight = (unsigned char) (ev.bweight*255.0f);
//}
//
//static void emDM_getEdge(DerivedMesh *dm, int index, MEdge *edge_r)
//{
//	EditMesh *em = ((EditMeshDerivedMesh *)dm).em;
//	EditEdge *ee = em.edges.first;
//	EditVert *ev, *v1, *v2;
//	int i;
//
//	for(i = 0; i < index; ++i) ee = ee.next;
//
//	edge_r.crease = (unsigned char) (ee.crease*255.0f);
//	edge_r.bweight = (unsigned char) (ee.bweight*255.0f);
//	/* TODO what to do with edge_r.flag? */
//	edge_r.flag = ME_EDGEDRAW|ME_EDGERENDER;
//	if (ee.seam) edge_r.flag |= ME_SEAM;
//	if (ee.sharp) edge_r.flag |= ME_SHARP;
//#if 0
//	/* this needs setup of f2 field */
//	if (!ee.f2) edge_r.flag |= ME_LOOSEEDGE;
//#endif
//
//	/* goddamn, we have to search all verts to find indices */
//	v1 = ee.v1;
//	v2 = ee.v2;
//	for(i = 0, ev = em.verts.first; v1 || v2; i++, ev = ev.next) {
//		if(ev == v1) {
//			edge_r.v1 = i;
//			v1 = NULL;
//		}
//		if(ev == v2) {
//			edge_r.v2 = i;
//			v2 = NULL;
//		}
//	}
//}
//
//static void emDM_getFace(DerivedMesh *dm, int index, MFace *face_r)
//{
//	EditMesh *em = ((EditMeshDerivedMesh *)dm).em;
//	EditFace *ef = em.faces.first;
//	EditVert *ev, *v1, *v2, *v3, *v4;
//	int i;
//
//	for(i = 0; i < index; ++i) ef = ef.next;
//
//	face_r.mat_nr = ef.mat_nr;
//	face_r.flag = ef.flag;
//
//	/* goddamn, we have to search all verts to find indices */
//	v1 = ef.v1;
//	v2 = ef.v2;
//	v3 = ef.v3;
//	v4 = ef.v4;
//	if(!v4) face_r.v4 = 0;
//
//	for(i = 0, ev = em.verts.first; v1 || v2 || v3 || v4;
//	    i++, ev = ev.next) {
//		if(ev == v1) {
//			face_r.v1 = i;
//			v1 = NULL;
//		}
//		if(ev == v2) {
//			face_r.v2 = i;
//			v2 = NULL;
//		}
//		if(ev == v3) {
//			face_r.v3 = i;
//			v3 = NULL;
//		}
//		if(ev == v4) {
//			face_r.v4 = i;
//			v4 = NULL;
//		}
//	}
//
//	test_index_face(face_r, NULL, 0, ef.v4?4:3);
//}
//
//static void emDM_copyVertArray(DerivedMesh *dm, MVert *vert_r)
//{
//	EditVert *ev = ((EditMeshDerivedMesh *)dm).em.verts.first;
//
//	for( ; ev; ev = ev.next, ++vert_r) {
//		VECCOPY(vert_r.co, ev.co);
//
//		vert_r.no[0] = ev.no[0] * 32767.0;
//		vert_r.no[1] = ev.no[1] * 32767.0;
//		vert_r.no[2] = ev.no[2] * 32767.0;
//
//		/* TODO what to do with vert_r.flag and vert_r.mat_nr? */
//		vert_r.mat_nr = 0;
//		vert_r.flag = 0;
//		vert_r.bweight = (unsigned char) (ev.bweight*255.0f);
//	}
//}
//
//static void emDM_copyEdgeArray(DerivedMesh *dm, MEdge *edge_r)
//{
//	EditMesh *em = ((EditMeshDerivedMesh *)dm).em;
//	EditEdge *ee = em.edges.first;
//	EditVert *ev;
//	int i;
//
//	/* store vertex indices in tmp union */
//	for(ev = em.verts.first, i = 0; ev; ev = ev.next, ++i)
//		ev.tmp.l = (intptr_t) i;
//
//	for( ; ee; ee = ee.next, ++edge_r) {
//		edge_r.crease = (unsigned char) (ee.crease*255.0f);
//		edge_r.bweight = (unsigned char) (ee.bweight*255.0f);
//		/* TODO what to do with edge_r.flag? */
//		edge_r.flag = ME_EDGEDRAW|ME_EDGERENDER;
//		if (ee.seam) edge_r.flag |= ME_SEAM;
//		if (ee.sharp) edge_r.flag |= ME_SHARP;
//#if 0
//		/* this needs setup of f2 field */
//		if (!ee.f2) edge_r.flag |= ME_LOOSEEDGE;
//#endif
//
//		edge_r.v1 = (int)ee.v1.tmp.l;
//		edge_r.v2 = (int)ee.v2.tmp.l;
//	}
//}
//
//static void emDM_copyFaceArray(DerivedMesh *dm, MFace *face_r)
//{
//	EditMesh *em = ((EditMeshDerivedMesh *)dm).em;
//	EditFace *ef = em.faces.first;
//	EditVert *ev;
//	int i;
//
//	/* store vertexes indices in tmp union */
//	for(ev = em.verts.first, i = 0; ev; ev = ev.next, ++i)
//		ev.tmp.l = (intptr_t) i;
//
//	for( ; ef; ef = ef.next, ++face_r) {
//		face_r.mat_nr = ef.mat_nr;
//		face_r.flag = ef.flag;
//
//		face_r.v1 = (int)ef.v1.tmp.l;
//		face_r.v2 = (int)ef.v2.tmp.l;
//		face_r.v3 = (int)ef.v3.tmp.l;
//		if(ef.v4) face_r.v4 = (int)ef.v4.tmp.l;
//		else face_r.v4 = 0;
//
//		test_index_face(face_r, NULL, 0, ef.v4?4:3);
//	}
//}
//
//static void *emDM_getFaceDataArray(DerivedMesh *dm, int type)
//{
//	EditMeshDerivedMesh *emdm= (EditMeshDerivedMesh*) dm;
//	EditMesh *em= emdm.em;
//	EditFace *efa;
//	char *data, *emdata;
//	void *datalayer;
//	int index, offset, size;
//
//	datalayer = DM_get_face_data_layer(dm, type);
//	if(datalayer)
//		return datalayer;
//
//	/* layers are store per face for editmesh, we convert to a temporary
//	 * data layer array in the derivedmesh when these are requested */
//	if(type == CD_MTFACE || type == CD_MCOL) {
//		index = CustomData_get_layer_index(&em.fdata, type);
//
//		if(index != -1) {
//			offset = em.fdata.layers[index].offset;
//			size = CustomData_sizeof(type);
//
//			DM_add_face_layer(dm, type, CD_CALLOC, NULL);
//			index = CustomData_get_layer_index(&dm.faceData, type);
//			dm.faceData.layers[index].flag |= CD_FLAG_TEMPORARY;
//
//			data = datalayer = DM_get_face_data_layer(dm, type);
//			for(efa=em.faces.first; efa; efa=efa.next, data+=size) {
//				emdata = CustomData_em_get(&em.fdata, efa.data, type);
//				memcpy(data, emdata, size);
//			}
//		}
//	}
//
//	return datalayer;
//}

    public static Release emDM_release = new Release() {
        public void run(DerivedMesh dm) {
            EditMeshDerivedMesh emdm = (EditMeshDerivedMesh) dm;
            if (DM_release(dm)) {
                if (emdm.vertexCos != null) {
//			MEM_freeN(emdm.vertexCos);
//			MEM_freeN(emdm.vertexNos);
//			MEM_freeN(emdm.faceNos);
                }
            }
        }
    };

    public static DerivedMesh getEditMeshDerivedMesh(EditMesh em, bObject ob, float[][] vertexCos) {
        EditMeshDerivedMesh emdm = new EditMeshDerivedMesh();

        DM_init(emdm.dm, ListBaseUtil.BLI_countlist(em.verts),
                ListBaseUtil.BLI_countlist(em.edges), ListBaseUtil.BLI_countlist(em.faces));

        emdm.dm.getMinMax = emDM_getMinMax;

        emdm.dm.getNumVerts = emDM_getNumVerts;
        emdm.dm.getNumEdges = emDM_getNumEdges;
        emdm.dm.getNumFaces = emDM_getNumFaces;

//	emdm.dm.getVert = emDM_getVert;
//	emdm.dm.getEdge = emDM_getEdge;
//	emdm.dm.getFace = emDM_getFace;
//	emdm.dm.copyVertArray = emDM_copyVertArray;
//	emdm.dm.copyEdgeArray = emDM_copyEdgeArray;
//	emdm.dm.copyFaceArray = emDM_copyFaceArray;
//	emdm.dm.getFaceDataArray = emDM_getFaceDataArray;

        emdm.dm.foreachMappedVert = emDM_foreachMappedVert;
        emdm.dm.foreachMappedEdge = emDM_foreachMappedEdge;
        emdm.dm.foreachMappedFaceCenter = emDM_foreachMappedFaceCenter;

        emdm.dm.drawEdges = emDM_drawEdges;
        emdm.dm.drawMappedEdges = emDM_drawMappedEdges;
//	emdm.dm.drawMappedEdgesInterp = emDM_drawMappedEdgesInterp;
        emdm.dm.drawMappedFaces = emDM_drawMappedFaces;
//	emdm.dm.drawMappedFacesTex = emDM_drawMappedFacesTex;
//	emdm.dm.drawMappedFacesGLSL = emDM_drawMappedFacesGLSL;
//	emdm.dm.drawFacesTex = emDM_drawFacesTex;
//	emdm.dm.drawFacesGLSL = emDM_drawFacesGLSL;
//	emdm.dm.drawUVEdges = emDM_drawUVEdges;

        emdm.dm.release = emDM_release;

        emdm.em = em;
        emdm.vertexCos = vertexCos;

//	if(CustomData_has_layer(&em.vdata, CD_MDEFORMVERT)) {
//		EditVert *eve;
//		int i;
//
//		DM_add_vert_layer(&emdm.dm, CD_MDEFORMVERT, CD_CALLOC, NULL);
//
//		for(eve = em.verts.first, i = 0; eve; eve = eve.next, ++i)
//			DM_set_vert_data(&emdm.dm, i, CD_MDEFORMVERT,
//			                 CustomData_em_get(&em.vdata, eve.data, CD_MDEFORMVERT));
//	}
//
//	if(vertexCos) {
//		EditVert *eve;
//		EditFace *efa;
//		int totface = BLI_countlist(&em.faces);
//		int i;
//
//		for (i=0,eve=em.verts.first; eve; eve= eve.next)
//			eve.tmp.l = (intptr_t) i++;
//
//		emdm.vertexNos = MEM_callocN(sizeof(*emdm.vertexNos)*i, "emdm_vno");
//		emdm.faceNos = MEM_mallocN(sizeof(*emdm.faceNos)*totface, "emdm_vno");
//
//		for(i=0, efa= em.faces.first; efa; i++, efa=efa.next) {
//			float *v1 = vertexCos[(int) efa.v1.tmp.l];
//			float *v2 = vertexCos[(int) efa.v2.tmp.l];
//			float *v3 = vertexCos[(int) efa.v3.tmp.l];
//			float *no = emdm.faceNos[i];
//
//			if(efa.v4) {
//				float *v4 = vertexCos[(int) efa.v4.tmp.l];
//
//				CalcNormFloat4(v1, v2, v3, v4, no);
//				VecAddf(emdm.vertexNos[(int) efa.v4.tmp.l], emdm.vertexNos[(int) efa.v4.tmp.l], no);
//			}
//			else {
//				CalcNormFloat(v1, v2, v3, no);
//			}
//
//			VecAddf(emdm.vertexNos[(int) efa.v1.tmp.l], emdm.vertexNos[(int) efa.v1.tmp.l], no);
//			VecAddf(emdm.vertexNos[(int) efa.v2.tmp.l], emdm.vertexNos[(int) efa.v2.tmp.l], no);
//			VecAddf(emdm.vertexNos[(int) efa.v3.tmp.l], emdm.vertexNos[(int) efa.v3.tmp.l], no);
//		}
//
//		for(i=0, eve= em.verts.first; eve; i++, eve=eve.next) {
//			float *no = emdm.vertexNos[i];
//			/* following Mesh convention; we use vertex coordinate itself
//			 * for normal in this case */
//			if (Normalize(no)==0.0) {
//				VECCOPY(no, vertexCos[i]);
//				Normalize(no);
//			}
//		}
//	}

	return (DerivedMesh) emdm;
    }

///***/
//
//DerivedMesh *mesh_create_derived_for_modifier(Scene *scene, Object *ob, ModifierData *md)
//{
//	Mesh *me = ob.data;
//	ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//	DerivedMesh *dm;
//
//	md.scene= scene;
//
//	if (!(md.mode&eModifierMode_Realtime)) return NULL;
//	if (mti.isDisabled && mti.isDisabled(md)) return NULL;
//
//	if (mti.type==eModifierTypeType_OnlyDeform) {
//		int numVerts;
//		float (*deformedVerts)[3] = mesh_getVertexCos(me, &numVerts);
//
//		mti.deformVerts(md, ob, NULL, deformedVerts, numVerts, 0, 0);
//		dm = getMeshDerivedMesh(me, ob, deformedVerts);
//
//		MEM_freeN(deformedVerts);
//	} else {
//		DerivedMesh *tdm = getMeshDerivedMesh(me, ob, NULL);
//		dm = mti.applyModifier(md, ob, tdm, 0, 0);
//
//		if(tdm != dm) tdm.release(tdm);
//	}
//
//	return dm;
//}
//
//static float *get_editmesh_orco_verts(EditMesh *em)
//{
//	EditVert *eve;
//	float *orco;
//	int a, totvert;
//
//	/* these may not really be the orco's, but it's only for preview.
//	 * could be solver better once, but isn't simple */
//
//	totvert= 0;
//	for(eve=em.verts.first; eve; eve=eve.next)
//		totvert++;
//
//	orco = MEM_mallocN(sizeof(float)*3*totvert, "EditMesh Orco");
//
//	for(a=0, eve=em.verts.first; eve; eve=eve.next, a+=3)
//		VECCOPY(orco+a, eve.co);
//
//	return orco;
//}
//
///* orco custom data layer */
//
//static DerivedMesh *create_orco_dm(Object *ob, Mesh *me, EditMesh *em)
//{
//	DerivedMesh *dm;
//	float (*orco)[3];
//
//	if(em) {
//		dm= CDDM_from_editmesh(em, me);
//		orco= (float(*)[3])get_editmesh_orco_verts(em);
//	}
//	else {
//		dm= CDDM_from_mesh(me, ob);
//		orco= (float(*)[3])get_mesh_orco_verts(ob);
//	}
//
//	CDDM_apply_vert_coords(dm, orco);
//	CDDM_calc_normals(dm);
//	MEM_freeN(orco);
//
//	return dm;
//}
//
//static void add_orco_dm(Object *ob, EditMesh *em, DerivedMesh *dm, DerivedMesh *orcodm)
//{
//	float (*orco)[3], (*layerorco)[3];
//	int totvert;
//
//	totvert= dm.getNumVerts(dm);
//
//	if(orcodm) {
//		orco= MEM_callocN(sizeof(float)*3*totvert, "dm orco");
//
//		if(orcodm.getNumVerts(orcodm) == totvert)
//			orcodm.getVertCos(orcodm, orco);
//		else
//			dm.getVertCos(dm, orco);
//	}
//	else {
//		if(em) orco= (float(*)[3])get_editmesh_orco_verts(em);
//		else orco= (float(*)[3])get_mesh_orco_verts(ob);
//	}
//
//	transform_mesh_orco_verts(ob.data, orco, totvert, 0);
//
//	if((layerorco = DM_get_vert_data_layer(dm, CD_ORCO))) {
//		memcpy(layerorco, orco, sizeof(float)*totvert);
//		MEM_freeN(orco);
//	}
//	else
//		DM_add_vert_layer(dm, CD_ORCO, CD_ASSIGN, orco);
//}
//
///* weight paint colors */
//
///* Something of a hack, at the moment deal with weightpaint
// * by tucking into colors during modifier eval, only in
// * wpaint mode. Works ok but need to make sure recalc
// * happens on enter/exit wpaint.
// */
//
//void weight_to_rgb(float input, float *fr, float *fg, float *fb)
//{
//	float blend;
//
//	blend= ((input/2.0f)+0.5f);
//
//	if (input<=0.25f){	// blue.cyan
//		*fr= 0.0f;
//		*fg= blend*input*4.0f;
//		*fb= blend;
//	}
//	else if (input<=0.50f){	// cyan.green
//		*fr= 0.0f;
//		*fg= blend;
//		*fb= blend*(1.0f-((input-0.25f)*4.0f));
//	}
//	else if (input<=0.75){	// green.yellow
//		*fr= blend * ((input-0.50f)*4.0f);
//		*fg= blend;
//		*fb= 0.0f;
//	}
//	else if (input<=1.0){ // yellow.red
//		*fr= blend;
//		*fg= blend * (1.0f-((input-0.75f)*4.0f));
//		*fb= 0.0f;
//	}
//}
//
//static void calc_weightpaint_vert_color(Object *ob, ColorBand *coba, int vert, unsigned char *col)
//{
//	Mesh *me = ob.data;
//	float colf[4], input = 0.0f;
//	int i;
//
//	if (me.dvert) {
//		for (i=0; i<me.dvert[vert].totweight; i++)
//			if (me.dvert[vert].dw[i].def_nr==ob.actdef-1)
//				input+=me.dvert[vert].dw[i].weight;
//	}
//
//	CLAMP(input, 0.0f, 1.0f);
//
//	if(coba)
//		do_colorband(coba, input, colf);
//	else
//		weight_to_rgb(input, colf, colf+1, colf+2);
//
//	col[3] = (unsigned char)(colf[0] * 255.0f);
//	col[2] = (unsigned char)(colf[1] * 255.0f);
//	col[1] = (unsigned char)(colf[2] * 255.0f);
//	col[0] = 255;
//}
//
//static ColorBand *stored_cb= NULL;
//
//void vDM_ColorBand_store(ColorBand *coba)
//{
//	stored_cb= coba;
//}
//
//static void add_weight_mcol_dm(Object *ob, DerivedMesh *dm)
//{
//	Mesh *me = ob.data;
//	MFace *mf = me.mface;
//	ColorBand *coba= stored_cb;	/* warning, not a local var */
//	unsigned char *wtcol;
//	int i;
//
//	wtcol = MEM_callocN (sizeof (unsigned char) * me.totface*4*4, "weightmap");
//
//	memset(wtcol, 0x55, sizeof (unsigned char) * me.totface*4*4);
//	for (i=0; i<me.totface; i++, mf++) {
//		calc_weightpaint_vert_color(ob, coba, mf.v1, &wtcol[(i*4 + 0)*4]);
//		calc_weightpaint_vert_color(ob, coba, mf.v2, &wtcol[(i*4 + 1)*4]);
//		calc_weightpaint_vert_color(ob, coba, mf.v3, &wtcol[(i*4 + 2)*4]);
//		if (mf.v4)
//			calc_weightpaint_vert_color(ob, coba, mf.v4, &wtcol[(i*4 + 3)*4]);
//	}
//
//	CustomData_add_layer(&dm.faceData, CD_WEIGHT_MCOL, CD_ASSIGN, wtcol, dm.numFaceData);
//}

/* new value for useDeform -1  (hack for the gameengine):
 * - apply only the modifier stack of the object, skipping the virtual modifiers,
 * - don't apply the key
 * - apply deform modifiers and input vertexco
 */
public static void mesh_calc_modifiers(Scene scene, bObject ob, float[][] inputVertexCos,
                                DerivedMesh[] deform_r, DerivedMesh[] final_r,
                                int useRenderParams, boolean useDeform,
                                boolean needMapping, int dataMask, int index)
{
	Mesh me = (Mesh)ob.data;
//	ModifierData *firstmd, *md;
//	LinkNode *datamasks, *curr;
//	CustomDataMask mask;
	float[][] deformedVerts = null;
	DerivedMesh dm, orcodm, finaldm;
	int[] numVerts = {me.totvert};
//	int required_mode;

//	md = firstmd = (useDeform<0) ? ob.modifiers.first : modifiers_getVirtualModifierList(ob);
//
//	modifiers_clearErrors(ob);
//
//	if(useRenderParams) required_mode = eModifierMode_Render;
//	else required_mode = eModifierMode_Realtime;

	/* we always want to keep original indices */
	dataMask |= CustomDataTypes.CD_MASK_ORIGINDEX;

//	datamasks = modifiers_calcDataMasks(ob, md, dataMask, required_mode);
//	curr = datamasks;

	if(deform_r!=null)
            deform_r[0] = null;
	final_r[0] = null;

	if(useDeform) {
//		if(useDeform > 0 && do_ob_key(scene, ob)) /* shape key makes deform verts */
//			deformedVerts = mesh_getVertexCos(me, &numVerts);
//		else if(inputVertexCos)
//			deformedVerts = inputVertexCos;
//
//		/* Apply all leading deforming modifiers */
//		for(;md; md = md.next, curr = curr.next) {
//			ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//
//			md.scene= scene;
//
//			if(!modifier_isEnabled(md, required_mode)) continue;
//			if(useDeform < 0 && mti.dependsOnTime && mti.dependsOnTime(md)) continue;
//
//			if(mti.type == eModifierTypeType_OnlyDeform) {
//				if(!deformedVerts)
//					deformedVerts = mesh_getVertexCos(me, &numVerts);
//
//				mti.deformVerts(md, ob, NULL, deformedVerts, numVerts, useRenderParams, useDeform);
//			} else {
//				break;
//			}
//
//			/* grab modifiers until index i */
//			if((index >= 0) && (modifiers_indexInObject(ob, md) >= index))
//				break;
//		}
//
//		/* Result of all leading deforming modifiers is cached for
//		 * places that wish to use the original mesh but with deformed
//		 * coordinates (vpaint, etc.)
//		 */
//		if (deform_r) {
//			*deform_r = CDDM_from_mesh(me, ob);
//
//			if(deformedVerts) {
//				CDDM_apply_vert_coords(*deform_r, deformedVerts);
//				CDDM_calc_normals(*deform_r);
//			}
//		}
	} else {
		/* default behaviour for meshes */
		if(inputVertexCos!=null)
			deformedVerts = inputVertexCos;
//		else
//			deformedVerts = mesh_getRefKeyCos(me, &numVerts);
	}


	/* Now apply all remaining modifiers. If useDeform is off then skip
	 * OnlyDeform ones.
	 */
	dm = null;
//	orcodm = NULL;

//	for(;md; md = md.next, curr = curr.next) {
//		ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//
//		md.scene= scene;
//
//		if(!modifier_isEnabled(md, required_mode)) continue;
//		if(mti.type == eModifierTypeType_OnlyDeform && !useDeform) continue;
//		if((mti.flags & eModifierTypeFlag_RequiresOriginalData) && dm) {
//			modifier_setError(md, "Modifier requires original data, bad stack position.");
//			continue;
//		}
//		if(needMapping && !modifier_supportsMapping(md)) continue;
//		if(useDeform < 0 && mti.dependsOnTime && mti.dependsOnTime(md)) continue;
//
//		/* add an orco layer if needed by this modifier */
//		if(dm && mti.requiredDataMask) {
//			mask = mti.requiredDataMask(ob, md);
//			if(mask & CD_MASK_ORCO)
//				add_orco_dm(ob, NULL, dm, orcodm);
//		}
//
//		/* How to apply modifier depends on (a) what we already have as
//		 * a result of previous modifiers (could be a DerivedMesh or just
//		 * deformed vertices) and (b) what type the modifier is.
//		 */
//
//		if(mti.type == eModifierTypeType_OnlyDeform) {
//
//			/* No existing verts to deform, need to build them. */
//			if(!deformedVerts) {
//				if(dm) {
//					/* Deforming a derived mesh, read the vertex locations
//					 * out of the mesh and deform them. Once done with this
//					 * run of deformers verts will be written back.
//					 */
//					numVerts = dm.getNumVerts(dm);
//					deformedVerts =
//					    MEM_mallocN(sizeof(*deformedVerts) * numVerts, "dfmv");
//					dm.getVertCos(dm, deformedVerts);
//				} else {
//					deformedVerts = mesh_getVertexCos(me, &numVerts);
//				}
//			}
//
//			mti.deformVerts(md, ob, dm, deformedVerts, numVerts, useRenderParams, useDeform);
//		} else {
//			DerivedMesh *ndm;
//
//			/* apply vertex coordinates or build a DerivedMesh as necessary */
//			if(dm) {
//				if(deformedVerts) {
//					DerivedMesh *tdm = CDDM_copy(dm);
//					dm.release(dm);
//					dm = tdm;
//
//					CDDM_apply_vert_coords(dm, deformedVerts);
//					CDDM_calc_normals(dm);
//				}
//			} else {
//				dm = CDDM_from_mesh(me, ob);
//
//				if(deformedVerts) {
//					CDDM_apply_vert_coords(dm, deformedVerts);
//					CDDM_calc_normals(dm);
//				}
//
//				if(dataMask & CD_MASK_WEIGHT_MCOL)
//					add_weight_mcol_dm(ob, dm);
//			}
//
//			/* create an orco derivedmesh in parallel */
//			mask= (CustomDataMask)GET_INT_FROM_POINTER(curr.link);
//			if(mask & CD_MASK_ORCO) {
//				if(!orcodm)
//					orcodm= create_orco_dm(ob, me, NULL);
//
//				mask &= ~CD_MASK_ORCO;
//				DM_set_only_copy(orcodm, mask);
//				ndm = mti.applyModifier(md, ob, orcodm, useRenderParams, !inputVertexCos);
//
//				if(ndm) {
//					/* if the modifier returned a new dm, release the old one */
//					if(orcodm && orcodm != ndm) orcodm.release(orcodm);
//					orcodm = ndm;
//				}
//			}
//
//			/* set the DerivedMesh to only copy needed data */
//			DM_set_only_copy(dm, mask);
//
//			/* add an origspace layer if needed */
//			if(((CustomDataMask)GET_INT_FROM_POINTER(curr.link)) & CD_MASK_ORIGSPACE)
//				if(!CustomData_has_layer(&dm.faceData, CD_ORIGSPACE))
//					DM_add_face_layer(dm, CD_ORIGSPACE, CD_DEFAULT, NULL);
//
//			ndm = mti.applyModifier(md, ob, dm, useRenderParams, !inputVertexCos);
//
//			if(ndm) {
//				/* if the modifier returned a new dm, release the old one */
//				if(dm && dm != ndm) dm.release(dm);
//
//				dm = ndm;
//
//				if(deformedVerts) {
//					if(deformedVerts != inputVertexCos)
//						MEM_freeN(deformedVerts);
//
//					deformedVerts = NULL;
//				}
//			}
//		}
//
//		/* grab modifiers until index i */
//		if((index >= 0) && (modifiers_indexInObject(ob, md) >= index))
//			break;
//	}
//
//	for(md=firstmd; md; md=md.next)
//		modifier_freeTemporaryData(md);

	/* Yay, we are done. If we have a DerivedMesh and deformed vertices
	 * need to apply these back onto the DerivedMesh. If we have no
	 * DerivedMesh then we need to build one.
	 */
	if(dm!=null && deformedVerts!=null) {
		finaldm = CDDerivedMesh.CDDM_copy(dm);

//		dm.release(dm);

//		CDDM_apply_vert_coords(finaldm, deformedVerts);
//		CDDM_calc_normals(finaldm);

//		if(dataMask & CD_MASK_WEIGHT_MCOL)
//			add_weight_mcol_dm(ob, finaldm);
	} else if(dm!=null) {
		finaldm = dm;
	} else {
		finaldm = CDDerivedMesh.CDDM_from_mesh(me, ob);

		if(deformedVerts!=null) {
			CDDerivedMesh.CDDM_apply_vert_coords(finaldm, deformedVerts);
                        CDDerivedMesh.CDDM_calc_normals(finaldm);
		}

//		if(dataMask & CD_MASK_WEIGHT_MCOL)
//			add_weight_mcol_dm(ob, finaldm);
	}

//	/* add an orco layer if needed */
//	if(dataMask & CD_MASK_ORCO) {
//		add_orco_dm(ob, NULL, finaldm, orcodm);
//
//		if(deform_r && *deform_r)
//			add_orco_dm(ob, NULL, *deform_r, NULL);
//	}

	final_r[0] = finaldm;

//	if(orcodm)
//		orcodm.release(orcodm);
//
//	if(deformedVerts && deformedVerts != inputVertexCos)
//		MEM_freeN(deformedVerts);
//
//	BLI_linklist_free(datamasks, NULL);
}

//static float (*editmesh_getVertexCos(EditMesh *em, int *numVerts_r))[3]
//{
//	int i, numVerts = *numVerts_r = BLI_countlist(&em.verts);
//	float (*cos)[3];
//	EditVert *eve;
//
//	cos = MEM_mallocN(sizeof(*cos)*numVerts, "vertexcos");
//	for (i=0,eve=em.verts.first; i<numVerts; i++,eve=eve.next) {
//		VECCOPY(cos[i], eve.co);
//	}
//
//	return cos;
//}
//
//static int editmesh_modifier_is_enabled(ModifierData *md, DerivedMesh *dm)
//{
//	ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//	int required_mode = eModifierMode_Realtime | eModifierMode_Editmode;
//
//	if(!modifier_isEnabled(md, required_mode)) return 0;
//	if((mti.flags & eModifierTypeFlag_RequiresOriginalData) && dm) {
//		modifier_setError(md, "Modifier requires original data, bad stack position.");
//		return 0;
//	}
//
//	return 1;
//}

public static void editmesh_calc_modifiers(Scene scene, bObject ob, EditMesh em,
                DerivedMesh[] cage_r, DerivedMesh[] final_r, int dataMask)
{
//	ModifierData *md;
	float[][] deformedVerts = null;
//	CustomDataMask mask;
	DerivedMesh dm, orcodm = null;
//	int i, numVerts = 0, cageIndex = modifiers_getCageIndex(ob, null);
        int cageIndex = -1; // TMP
//	LinkNode *datamasks, *curr;
//	int required_mode = eModifierMode_Realtime | eModifierMode_Editmode;
//
//	modifiers_clearErrors(ob);

	if(cage_r!=null && cageIndex == -1) {
		cage_r[0] = getEditMeshDerivedMesh(em, ob, null);
	}

	dm = null;
//	md = ob.modifiers.first;

	/* we always want to keep original indices */
	dataMask |= CustomDataTypes.CD_MASK_ORIGINDEX;

//	datamasks = modifiers_calcDataMasks(ob, md, dataMask, required_mode);
//
//	curr = datamasks;
//	for(i = 0; md; i++, md = md.next, curr = curr.next) {
//		ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//
//		md.scene= scene;
//
//		if(!editmesh_modifier_is_enabled(md, dm))
//			continue;
//
//		/* add an orco layer if needed by this modifier */
//		if(dm && mti.requiredDataMask) {
//			mask = mti.requiredDataMask(ob, md);
//			if(mask & CD_MASK_ORCO)
//				add_orco_dm(ob, em, dm, orcodm);
//		}
//
//		/* How to apply modifier depends on (a) what we already have as
//		 * a result of previous modifiers (could be a DerivedMesh or just
//		 * deformed vertices) and (b) what type the modifier is.
//		 */
//
//		if(mti.type == eModifierTypeType_OnlyDeform) {
//			/* No existing verts to deform, need to build them. */
//			if(!deformedVerts) {
//				if(dm) {
//					/* Deforming a derived mesh, read the vertex locations
//					 * out of the mesh and deform them. Once done with this
//					 * run of deformers verts will be written back.
//					 */
//					numVerts = dm.getNumVerts(dm);
//					deformedVerts =
//					    MEM_mallocN(sizeof(*deformedVerts) * numVerts, "dfmv");
//					dm.getVertCos(dm, deformedVerts);
//				} else {
//					deformedVerts = editmesh_getVertexCos(em, &numVerts);
//				}
//			}
//
//			mti.deformVertsEM(md, ob, em, dm, deformedVerts, numVerts);
//		} else {
//			DerivedMesh *ndm;
//
//			/* apply vertex coordinates or build a DerivedMesh as necessary */
//			if(dm) {
//				if(deformedVerts) {
//					DerivedMesh *tdm = CDDM_copy(dm);
//					if(!(cage_r && dm == *cage_r)) dm.release(dm);
//					dm = tdm;
//
//					CDDM_apply_vert_coords(dm, deformedVerts);
//					CDDM_calc_normals(dm);
//				} else if(cage_r && dm == *cage_r) {
//					/* dm may be changed by this modifier, so we need to copy it
//					 */
//					dm = CDDM_copy(dm);
//				}
//
//			} else {
//				dm = CDDM_from_editmesh(em, ob.data);
//
//				if(deformedVerts) {
//					CDDM_apply_vert_coords(dm, deformedVerts);
//					CDDM_calc_normals(dm);
//				}
//			}
//
//			/* create an orco derivedmesh in parallel */
//			mask= (CustomDataMask)GET_INT_FROM_POINTER(curr.link);
//			if(mask & CD_MASK_ORCO) {
//				if(!orcodm)
//					orcodm= create_orco_dm(ob, ob.data, em);
//
//				mask &= ~CD_MASK_ORCO;
//				DM_set_only_copy(orcodm, mask);
//				ndm = mti.applyModifierEM(md, ob, em, orcodm);
//
//				if(ndm) {
//					/* if the modifier returned a new dm, release the old one */
//					if(orcodm && orcodm != ndm) orcodm.release(orcodm);
//					orcodm = ndm;
//				}
//			}
//
//			/* set the DerivedMesh to only copy needed data */
//			DM_set_only_copy(dm, (CustomDataMask)GET_INT_FROM_POINTER(curr.link));
//
//			if(((CustomDataMask)GET_INT_FROM_POINTER(curr.link)) & CD_MASK_ORIGSPACE)
//				if(!CustomData_has_layer(&dm.faceData, CD_ORIGSPACE))
//					DM_add_face_layer(dm, CD_ORIGSPACE, CD_DEFAULT, NULL);
//
//			ndm = mti.applyModifierEM(md, ob, em, dm);
//
//			if (ndm) {
//				if(dm && dm != ndm)
//					dm.release(dm);
//
//				dm = ndm;
//
//				if (deformedVerts) {
//					MEM_freeN(deformedVerts);
//					deformedVerts = NULL;
//				}
//			}
//		}
//
//		if(cage_r && i == cageIndex) {
//			if(dm && deformedVerts) {
//				*cage_r = CDDM_copy(dm);
//				CDDM_apply_vert_coords(*cage_r, deformedVerts);
//			} else if(dm) {
//				*cage_r = dm;
//			} else {
//				*cage_r =
//				    getEditMeshDerivedMesh(em, ob,
//				        deformedVerts ? MEM_dupallocN(deformedVerts) : NULL);
//			}
//		}
//	}
//
//	BLI_linklist_free(datamasks, NULL);

	/* Yay, we are done. If we have a DerivedMesh and deformed vertices need
	 * to apply these back onto the DerivedMesh. If we have no DerivedMesh
	 * then we need to build one.
	 */
	if(dm!=null && deformedVerts!=null) {
//		*final_r = CDDM_copy(dm);
//
//		if(!(cage_r && dm == *cage_r)) dm.release(dm);
//
//		CDDM_apply_vert_coords(*final_r, deformedVerts);
//		CDDM_calc_normals(*final_r);
	} else if (dm!=null) {
		final_r[0] = dm;
	} else if (deformedVerts==null && cage_r!=null && cage_r[0]!=null) {
		final_r[0] = cage_r[0];
	} else {
		final_r[0] = getEditMeshDerivedMesh(em, ob, deformedVerts);
		deformedVerts = null;
	}

//	/* add an orco layer if needed */
//	if(dataMask & CD_MASK_ORCO)
//		add_orco_dm(ob, em, *final_r, orcodm);
//
//	if(orcodm)
//		orcodm.release(orcodm);
//
//	if(deformedVerts)
//		MEM_freeN(deformedVerts);
}

//static void clear_mesh_caches(Object *ob)
//{
//	Mesh *me= ob.data;
//
//		/* also serves as signal to remake texspace */
//	if (ob.bb) {
//		MEM_freeN(ob.bb);
//		ob.bb = NULL;
//	}
//	if (me.bb) {
//		MEM_freeN(me.bb);
//		me.bb = NULL;
//	}
//
//	freedisplist(&ob.disp);
//
//	if (ob.derivedFinal) {
//		ob.derivedFinal.needsFree = 1;
//		ob.derivedFinal.release(ob.derivedFinal);
//		ob.derivedFinal= NULL;
//	}
//	if (ob.derivedDeform) {
//		ob.derivedDeform.needsFree = 1;
//		ob.derivedDeform.release(ob.derivedDeform);
//		ob.derivedDeform= NULL;
//	}
//}

public static void mesh_build_data(Scene scene, bObject ob, int dataMask)
{
	bObject obact = scene.basact!=null?scene.basact.object:null;
	int editing = (Global.FACESEL_PAINT_TEST()?1:0)|(G.f & Global.G_PARTICLEEDIT);
	boolean needMapping = editing!=0 && (ob==obact);
	float[] min = new float[3], max = new float[3];

//	clear_mesh_caches(ob);

        DerivedMesh[] derivedDeform={null}, derivedFinal={null};
        mesh_calc_modifiers(scene, ob, null, derivedDeform, derivedFinal, 0, true, needMapping, dataMask, -1);
        ob.derivedDeform = derivedDeform[0];
        ob.derivedFinal = derivedFinal[0];

//	INIT_MINMAX(min, max);

	((DerivedMesh)ob.derivedFinal).getMinMax.run((DerivedMesh)ob.derivedFinal, min, max);

//	if(!ob.bb)
//		ob.bb= MEM_callocN(sizeof(BoundBox), "bb");
//	boundbox_set_from_min_max(ob.bb, min, max);

	((DerivedMesh)ob.derivedFinal).needsFree = 0;
//	ob.derivedDeform.needsFree = 0;
	ob.lastDataMask = dataMask;
}

public static void editmesh_build_data(Scene scene, bObject obedit, EditMesh em, int dataMask)
{
//	float min[3], max[3];
//
//	clear_mesh_caches(obedit);
//
	if (em.derivedFinal!=null) {
		if (em.derivedFinal!=em.derivedCage) {
			em.derivedFinal.needsFree = 1;
//			em.derivedFinal.release(em.derivedFinal);
		}
		em.derivedFinal = null;
	}
	if (em.derivedCage!=null) {
		em.derivedCage.needsFree = 1;
//		em.derivedCage.release(em.derivedCage);
		em.derivedCage = null;
	}

        DerivedMesh[] dcage = {em.derivedCage}, dfinal = {em.derivedFinal};
	editmesh_calc_modifiers(scene, obedit, em, dcage, dfinal, dataMask);
        em.derivedCage = dcage[0];
        em.derivedFinal = dfinal[0];

        em.lastDataMask = dataMask;

//	INIT_MINMAX(min, max);
//
//	em.derivedFinal.getMinMax(em.derivedFinal, min, max);
//
//	if(!obedit.bb)
//		obedit.bb= MEM_callocN(sizeof(BoundBox), "bb");
//	boundbox_set_from_min_max(obedit.bb, min, max);

	em.derivedFinal.needsFree = 0;
	em.derivedCage.needsFree = 0;
}

public static void makeDerivedMesh(Scene scene, bObject ob, EditMesh em, int dataMask)
{
	if (em!=null) {
		editmesh_build_data(scene, ob, em, dataMask);
	} else {
		mesh_build_data(scene, ob, dataMask);
	}
}

    /***/

public static DerivedMesh mesh_get_derived_final(Scene scene, bObject ob, int dataMask)
{
	/* if there's no derived mesh or the last data mask used doesn't include
	 * the data we need, rebuild the derived mesh
	 */
	if(ob.derivedFinal==null || (dataMask & ob.lastDataMask) != dataMask)
		mesh_build_data(scene, ob, dataMask);

	return (DerivedMesh)ob.derivedFinal;
}

//DerivedMesh *mesh_get_derived_deform(Scene *scene, Object *ob, CustomDataMask dataMask)
//{
//	/* if there's no derived mesh or the last data mask used doesn't include
//	 * the data we need, rebuild the derived mesh
//	 */
//	if(!ob.derivedDeform || (dataMask & ob.lastDataMask) != dataMask)
//		mesh_build_data(scene, ob, dataMask);
//
//	return ob.derivedDeform;
//}

public static DerivedMesh mesh_create_derived_render(Scene scene, bObject ob, int dataMask)
{
	DerivedMesh[] finaldm={null};

	mesh_calc_modifiers(scene, ob, null, null, finaldm, 1, true, false, dataMask, -1);

	return finaldm[0];
}

//DerivedMesh *mesh_create_derived_index_render(Scene *scene, Object *ob, CustomDataMask dataMask, int index)
//{
//	DerivedMesh *final;
//
//	mesh_calc_modifiers(scene, ob, NULL, NULL, &final, 1, 1, 0, dataMask, index);
//
//	return final;
//}
//
//DerivedMesh *mesh_create_derived_view(Scene *scene, Object *ob, CustomDataMask dataMask)
//{
//	DerivedMesh *final;
//
//	mesh_calc_modifiers(scene, ob, NULL, NULL, &final, 0, 1, 0, dataMask, -1);
//
//	return final;
//}
//
//DerivedMesh *mesh_create_derived_no_deform(Scene *scene, Object *ob, float (*vertCos)[3],
//                                           CustomDataMask dataMask)
//{
//	DerivedMesh *final;
//
//	mesh_calc_modifiers(scene, ob, vertCos, NULL, &final, 0, 0, 0, dataMask, -1);
//
//	return final;
//}
//
//DerivedMesh *mesh_create_derived_no_virtual(Scene *scene, Object *ob, float (*vertCos)[3],
//                                            CustomDataMask dataMask)
//{
//	DerivedMesh *final;
//
//	mesh_calc_modifiers(scene, ob, vertCos, NULL, &final, 0, -1, 0, dataMask, -1);
//
//	return final;
//}
//
//DerivedMesh *mesh_create_derived_no_deform_render(Scene *scene, Object *ob,
//                                                  float (*vertCos)[3],
//                                                  CustomDataMask dataMask)
//{
//	DerivedMesh *final;
//
//	mesh_calc_modifiers(scene, ob, vertCos, NULL, &final, 1, 0, 0, dataMask, -1);
//
//	return final;
//}

/***/

public static DerivedMesh editmesh_get_derived_cage_and_final(Scene scene, bObject obedit, EditMesh em, DerivedMesh[] final_r,
                                                 int dataMask)
{
	/* if there's no derived mesh or the last data mask used doesn't include
	 * the data we need, rebuild the derived mesh
	 */
	if(em.derivedCage==null ||
	   (em.lastDataMask & dataMask) != dataMask)
		editmesh_build_data(scene, obedit, em, dataMask);

	final_r[0] = em.derivedFinal;
	return em.derivedCage;
}

//DerivedMesh *editmesh_get_derived_cage(Scene *scene, Object *obedit, EditMesh *em, CustomDataMask dataMask)
//{
//	/* if there's no derived mesh or the last data mask used doesn't include
//	 * the data we need, rebuild the derived mesh
//	 */
//	if(!em.derivedCage ||
//	   (em.lastDataMask & dataMask) != dataMask)
//		editmesh_build_data(scene, obedit, em, dataMask);
//
//	return em.derivedCage;
//}

public static DerivedMesh editmesh_get_derived_base(bObject obedit, EditMesh em)
{
	return getEditMeshDerivedMesh(em, obedit, null);
}


///* ********* For those who don't grasp derived stuff! (ton) :) *************** */
//
//static void make_vertexcosnos__mapFunc(void *userData, int index, float *co, float *no_f, short *no_s)
//{
//	float *vec = userData;
//
//	vec+= 6*index;
//
//	/* check if we've been here before (normal should not be 0) */
//	if(vec[3] || vec[4] || vec[5]) return;
//
//	VECCOPY(vec, co);
//	vec+= 3;
//	if(no_f) {
//		VECCOPY(vec, no_f);
//	}
//	else {
//		VECCOPY(vec, no_s);
//	}
//}
//
///* always returns original amount me.totvert of vertices and normals, but fully deformed and subsurfered */
///* this is needed for all code using vertexgroups (no subsurf support) */
///* it stores the normals as floats, but they can still be scaled as shorts (32767 = unit) */
///* in use now by vertex/weight paint and particle generating */
//
//float *mesh_get_mapped_verts_nors(Scene *scene, Object *ob)
//{
//	Mesh *me= ob.data;
//	DerivedMesh *dm;
//	float *vertexcosnos;
//
//	/* lets prevent crashing... */
//	if(ob.type!=OB_MESH || me.totvert==0)
//		return NULL;
//
//	dm= mesh_get_derived_final(scene, ob, CD_MASK_BAREMESH);
//	vertexcosnos= MEM_callocN(6*sizeof(float)*me.totvert, "vertexcosnos map");
//
//	if(dm.foreachMappedVert) {
//		dm.foreachMappedVert(dm, make_vertexcosnos__mapFunc, vertexcosnos);
//	}
//	else {
//		float *fp= vertexcosnos;
//		int a;
//
//		for(a=0; a< me.totvert; a++, fp+=6) {
//			dm.getVertCo(dm, a, fp);
//			dm.getVertNo(dm, a, fp+3);
//		}
//	}
//
//	dm.release(dm);
//	return vertexcosnos;
//}
//
///* ********* crazyspace *************** */
//
//int editmesh_get_first_deform_matrices(Object *ob, EditMesh *em, float (**deformmats)[3][3], float (**deformcos)[3])
//{
//	ModifierData *md;
//	DerivedMesh *dm;
//	int i, a, numleft = 0, numVerts = 0;
//	int cageIndex = modifiers_getCageIndex(ob, NULL);
//	float (*defmats)[3][3] = NULL, (*deformedVerts)[3] = NULL;
//
//	modifiers_clearErrors(ob);
//
//	dm = NULL;
//	md = ob.modifiers.first;
//
//	/* compute the deformation matrices and coordinates for the first
//	   modifiers with on cage editing that are enabled and support computing
//	   deform matrices */
//	for(i = 0; md && i <= cageIndex; i++, md = md.next) {
//		ModifierTypeInfo *mti = modifierType_getInfo(md.type);
//
//		if(!editmesh_modifier_is_enabled(md, dm))
//			continue;
//
//		if(mti.type==eModifierTypeType_OnlyDeform && mti.deformMatricesEM) {
//			if(!defmats) {
//				dm= getEditMeshDerivedMesh(em, ob, NULL);
//				deformedVerts= editmesh_getVertexCos(em, &numVerts);
//				defmats= MEM_callocN(sizeof(*defmats)*numVerts, "defmats");
//
//				for(a=0; a<numVerts; a++)
//					Mat3One(defmats[a]);
//			}
//
//			mti.deformMatricesEM(md, ob, em, dm, deformedVerts, defmats,
//				numVerts);
//		}
//		else
//			break;
//	}
//
//	for(; md && i <= cageIndex; md = md.next, i++)
//		if(editmesh_modifier_is_enabled(md, dm) && modifier_isDeformer(md))
//			numleft++;
//
//	if(dm)
//		dm.release(dm);
//
//	*deformmats= defmats;
//	*deformcos= deformedVerts;
//
//	return numleft;
//}
//
///* ******************* GLSL ******************** */
//
//void DM_add_tangent_layer(DerivedMesh *dm)
//{
//	/* mesh vars */
//	MTFace *mtface, *tf;
//	MFace *mface, *mf;
//	MVert *mvert, *v1, *v2, *v3, *v4;
//	MemArena *arena= NULL;
//	VertexTangent **vtangents= NULL;
//	float (*orco)[3]= NULL, (*tangent)[3];
//	float *uv1, *uv2, *uv3, *uv4, *vtang;
//	float fno[3], tang[3], uv[4][2];
//	int i, j, len, mf_vi[4], totvert, totface;
//
//	if(CustomData_get_layer_index(&dm.faceData, CD_TANGENT) != -1)
//		return;
//
//	/* check we have all the needed layers */
//	totvert= dm.getNumVerts(dm);
//	totface= dm.getNumFaces(dm);
//
//	mvert= dm.getVertArray(dm);
//	mface= dm.getFaceArray(dm);
//	mtface= dm.getFaceDataArray(dm, CD_MTFACE);
//
//	if(!mtface) {
//		orco= dm.getVertDataArray(dm, CD_ORCO);
//		if(!orco)
//			return;
//	}
//
//	/* create tangent layer */
//	DM_add_face_layer(dm, CD_TANGENT, CD_CALLOC, NULL);
//	tangent= DM_get_face_data_layer(dm, CD_TANGENT);
//
//	/* allocate some space */
//	arena= BLI_memarena_new(BLI_MEMARENA_STD_BUFSIZE);
//	BLI_memarena_use_calloc(arena);
//	vtangents= MEM_callocN(sizeof(VertexTangent*)*totvert, "VertexTangent");
//
//	/* sum tangents at connected vertices */
//	for(i=0, tf=mtface, mf=mface; i < totface; mf++, tf++, i++) {
//		v1= &mvert[mf.v1];
//		v2= &mvert[mf.v2];
//		v3= &mvert[mf.v3];
//
//		if (mf.v4) {
//			v4= &mvert[mf.v4];
//			CalcNormFloat4(v4.co, v3.co, v2.co, v1.co, fno);
//		}
//		else {
//			v4= NULL;
//			CalcNormFloat(v3.co, v2.co, v1.co, fno);
//		}
//
//		if(mtface) {
//			uv1= tf.uv[0];
//			uv2= tf.uv[1];
//			uv3= tf.uv[2];
//			uv4= tf.uv[3];
//		}
//		else {
//			uv1= uv[0]; uv2= uv[1]; uv3= uv[2]; uv4= uv[3];
//			spheremap(orco[mf.v1][0], orco[mf.v1][1], orco[mf.v1][2], &uv[0][0], &uv[0][1]);
//			spheremap(orco[mf.v2][0], orco[mf.v2][1], orco[mf.v2][2], &uv[1][0], &uv[1][1]);
//			spheremap(orco[mf.v3][0], orco[mf.v3][1], orco[mf.v3][2], &uv[2][0], &uv[2][1]);
//			if(v4)
//				spheremap(orco[mf.v4][0], orco[mf.v4][1], orco[mf.v4][2], &uv[3][0], &uv[3][1]);
//		}
//
//		tangent_from_uv(uv1, uv2, uv3, v1.co, v2.co, v3.co, fno, tang);
//		sum_or_add_vertex_tangent(arena, &vtangents[mf.v1], tang, uv1);
//		sum_or_add_vertex_tangent(arena, &vtangents[mf.v2], tang, uv2);
//		sum_or_add_vertex_tangent(arena, &vtangents[mf.v3], tang, uv3);
//
//		if(mf.v4) {
//			v4= &mvert[mf.v4];
//
//			tangent_from_uv(uv1, uv3, uv4, v1.co, v3.co, v4.co, fno, tang);
//			sum_or_add_vertex_tangent(arena, &vtangents[mf.v1], tang, uv1);
//			sum_or_add_vertex_tangent(arena, &vtangents[mf.v3], tang, uv3);
//			sum_or_add_vertex_tangent(arena, &vtangents[mf.v4], tang, uv4);
//		}
//	}
//
//	/* write tangent to layer */
//	for(i=0, tf=mtface, mf=mface; i < totface; mf++, tf++, i++, tangent+=4) {
//		len= (mf.v4)? 4 : 3;
//
//		if(mtface) {
//			uv1= tf.uv[0];
//			uv2= tf.uv[1];
//			uv3= tf.uv[2];
//			uv4= tf.uv[3];
//		}
//		else {
//			uv1= uv[0]; uv2= uv[1]; uv3= uv[2]; uv4= uv[3];
//			spheremap(orco[mf.v1][0], orco[mf.v1][1], orco[mf.v1][2], &uv[0][0], &uv[0][1]);
//			spheremap(orco[mf.v2][0], orco[mf.v2][1], orco[mf.v2][2], &uv[1][0], &uv[1][1]);
//			spheremap(orco[mf.v3][0], orco[mf.v3][1], orco[mf.v3][2], &uv[2][0], &uv[2][1]);
//			if(len==4)
//				spheremap(orco[mf.v4][0], orco[mf.v4][1], orco[mf.v4][2], &uv[3][0], &uv[3][1]);
//		}
//
//		mf_vi[0]= mf.v1;
//		mf_vi[1]= mf.v2;
//		mf_vi[2]= mf.v3;
//		mf_vi[3]= mf.v4;
//
//		for(j=0; j<len; j++) {
//			vtang= find_vertex_tangent(vtangents[mf_vi[j]], mtface ? tf.uv[j] : uv[j]);
//
//			VECCOPY(tangent[j], vtang);
//			Normalize(tangent[j]);
//		}
//	}
//
//	BLI_memarena_free(arena);
//	MEM_freeN(vtangents);
//}
//
//void DM_vertex_attributes_from_gpu(DerivedMesh *dm, GPUVertexAttribs *gattribs, DMVertexAttribs *attribs)
//{
//	CustomData *vdata, *fdata, *tfdata = NULL;
//	int a, b, layer;
//
//	/* From the layers requested by the GLSL shader, figure out which ones are
//	 * actually available for this derivedmesh, and retrieve the pointers */
//
//	memset(attribs, 0, sizeof(DMVertexAttribs));
//
//	vdata = &dm.vertData;
//	fdata = &dm.faceData;
//
//	/* ugly hack, editmesh derivedmesh doesn't copy face data, this way we
//	 * can use offsets instead */
//	if(dm.release == emDM_release)
//		tfdata = &((EditMeshDerivedMesh*)dm).em.fdata;
//	else
//		tfdata = fdata;
//
//	/* add a tangent layer if necessary */
//	for(b = 0; b < gattribs.totlayer; b++)
//		if(gattribs.layer[b].type == CD_TANGENT)
//			if(CustomData_get_layer_index(fdata, CD_TANGENT) == -1)
//				DM_add_tangent_layer(dm);
//
//	for(b = 0; b < gattribs.totlayer; b++) {
//		if(gattribs.layer[b].type == CD_MTFACE) {
//			/* uv coordinates */
//			if(gattribs.layer[b].name[0])
//				layer = CustomData_get_named_layer_index(tfdata, CD_MTFACE,
//					gattribs.layer[b].name);
//			else
//				layer = CustomData_get_active_layer_index(tfdata, CD_MTFACE);
//
//			if(layer != -1) {
//				a = attribs.tottface++;
//
//				attribs.tface[a].array = tfdata.layers[layer].data;
//				attribs.tface[a].emOffset = tfdata.layers[layer].offset;
//				attribs.tface[a].glIndex = gattribs.layer[b].glindex;
//			}
//		}
//		else if(gattribs.layer[b].type == CD_MCOL) {
//			/* vertex colors */
//			if(gattribs.layer[b].name[0])
//				layer = CustomData_get_named_layer_index(tfdata, CD_MCOL,
//					gattribs.layer[b].name);
//			else
//				layer = CustomData_get_active_layer_index(tfdata, CD_MCOL);
//
//			if(layer != -1) {
//				a = attribs.totmcol++;
//
//				attribs.mcol[a].array = tfdata.layers[layer].data;
//				attribs.mcol[a].emOffset = tfdata.layers[layer].offset;
//				attribs.mcol[a].glIndex = gattribs.layer[b].glindex;
//			}
//		}
//		else if(gattribs.layer[b].type == CD_TANGENT) {
//			/* tangents */
//			layer = CustomData_get_layer_index(fdata, CD_TANGENT);
//
//			if(layer != -1) {
//				attribs.tottang = 1;
//
//				attribs.tang.array = fdata.layers[layer].data;
//				attribs.tang.emOffset = fdata.layers[layer].offset;
//				attribs.tang.glIndex = gattribs.layer[b].glindex;
//			}
//		}
//		else if(gattribs.layer[b].type == CD_ORCO) {
//			/* original coordinates */
//			layer = CustomData_get_layer_index(vdata, CD_ORCO);
//
//			if(layer != -1) {
//				attribs.totorco = 1;
//
//				attribs.orco.array = vdata.layers[layer].data;
//				attribs.orco.emOffset = vdata.layers[layer].offset;
//				attribs.orco.glIndex = gattribs.layer[b].glindex;
//			}
//		}
//	}
//}
}

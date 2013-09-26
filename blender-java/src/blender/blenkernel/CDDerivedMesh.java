/*
* $Id: cdderivedmesh.c 21283 2009-07-01 12:19:00Z blendix $
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
* Implementation of CDDerivedMesh.
*
* BKE_cdderivedmesh.h contains the function prototypes for this file.
*
*/
package blender.blenkernel;

import blender.blenlib.Arithb;
import blender.makesdna.CustomDataTypes;
import blender.makesdna.MeshDataTypes;
import blender.makesdna.sdna.MEdge;
import blender.makesdna.sdna.MFace;
import blender.makesdna.sdna.MVert;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.bObject;
import javax.media.opengl.GL2;

public class CDDerivedMesh extends DerivedMesh {

	public DerivedMesh dm = this;

	/* these point to data in the DerivedMesh custom data layers,
	   they are only here for efficiency and convenience **/
	public MVert[] mvert;
        public MEdge[] medge;
        public MFace[] mface;

    /**************** DerivedMesh interface functions ****************/

    public static GetNum cdDM_getNumVerts = new GetNum() {
        public int run(DerivedMesh dm) {
            return dm.numVertData;
        }
    };

    public static GetNum cdDM_getNumEdges = new GetNum() {
        public int run(DerivedMesh dm) {
            return dm.numEdgeData;
        }
    };

    public static GetNum cdDM_getNumFaces = new GetNum() {
        public int run(DerivedMesh dm) {
            return dm.numFaceData;
        }
    };

//static void cdDM_getVert(DerivedMesh *dm, int index, MVert *vert_r)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh *)dm;
//	*vert_r = cddm->mvert[index];
//}
//
//static void cdDM_getEdge(DerivedMesh *dm, int index, MEdge *edge_r)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh *)dm;
//	*edge_r = cddm->medge[index];
//}
//
//static void cdDM_getFace(DerivedMesh *dm, int index, MFace *face_r)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh *)dm;
//	*face_r = cddm->mface[index];
//}

    public static CopyArray<MVert[]> cdDM_copyVertArray = new CopyArray<MVert[]>() {
        public void run(DerivedMesh dm, MVert[] vert_r) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            System.arraycopy(cddm.mvert, 0, vert_r, 0, dm.numVertData);
        }
    };

    public static CopyArray<MEdge[]> cdDM_copyEdgeArray = new CopyArray<MEdge[]>() {
        public void run(DerivedMesh dm, MEdge[] edge_r) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            System.arraycopy(cddm.medge, 0, edge_r, 0, dm.numEdgeData);
        }
    };
    public static CopyArray<MFace[]> cdDM_copyFaceArray = new CopyArray<MFace[]>() {
        public void run(DerivedMesh dm, MFace[] face_r) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            System.arraycopy(cddm.mface, 0, face_r, 0, dm.numFaceData);
        }
    };

    public static GetMinMax cdDM_getMinMax = new GetMinMax() {
        public void run(DerivedMesh dm, float[] min_r, float[] max_r) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            int i;
            if (dm.numVertData != 0) {
                for (i = 0; i < dm.numVertData; i++) {
//                    DO_MINMAX(cddm->mvert[i].co, min_r, max_r);
                }
            } else {
                min_r[0] = min_r[1] = min_r[2] = max_r[0] = max_r[1] = max_r[2] = 0.0f;
            }
        }
    };

//static void cdDM_getVertCo(DerivedMesh *dm, int index, float co_r[3])
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//
//	VECCOPY(co_r, cddm->mvert[index].co);
//}
//
//static void cdDM_getVertCos(DerivedMesh *dm, float (*cos_r)[3])
//{
//	MVert *mv = CDDM_get_verts(dm);
//	int i;
//
//	for(i = 0; i < dm->numVertData; i++, mv++)
//		VECCOPY(cos_r[i], mv->co);
//}
//
//static void cdDM_getVertNo(DerivedMesh *dm, int index, float no_r[3])
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//	short *no = cddm->mvert[index].no;
//
//	no_r[0] = no[0]/32767.f;
//	no_r[1] = no[1]/32767.f;
//	no_r[2] = no[2]/32767.f;
//}
//
//static void cdDM_drawVerts(DerivedMesh *dm)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//	MVert *mv = cddm->mvert;
//	int i;
//
//	glBegin(GL_POINTS);
//	for(i = 0; i < dm->numVertData; i++, mv++)
//		glVertex3fv(mv->co);
//	glEnd();
//}
//
//static void cdDM_drawUVEdges(DerivedMesh *dm)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//	MFace *mf = cddm->mface;
//	MTFace *tf = DM_get_face_data_layer(dm, CD_MTFACE);
//	int i;
//
//	if(mf) {
//		glBegin(GL_LINES);
//		for(i = 0; i < dm->numFaceData; i++, mf++, tf++) {
//			if(!(mf->flag&ME_HIDE)) {
//				glVertex2fv(tf->uv[0]);
//				glVertex2fv(tf->uv[1]);
//
//				glVertex2fv(tf->uv[1]);
//				glVertex2fv(tf->uv[2]);
//
//				if(!mf->v4) {
//					glVertex2fv(tf->uv[2]);
//					glVertex2fv(tf->uv[0]);
//				} else {
//					glVertex2fv(tf->uv[2]);
//					glVertex2fv(tf->uv[3]);
//
//					glVertex2fv(tf->uv[3]);
//					glVertex2fv(tf->uv[0]);
//				}
//			}
//		}
//		glEnd();
//	}
//}

    public static DrawEdges cdDM_drawEdges = new DrawEdges() {
        public void run(GL2 gl, DerivedMesh dm, boolean drawLooseEdges) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            MVert[] mvert = cddm.mvert;
            MEdge[] medge = cddm.medge;
            int i;

            gl.glBegin(GL2.GL_LINES);
            for (i = 0; i < dm.numEdgeData; i++) {
                if ((medge[i].flag & MeshDataTypes.ME_EDGEDRAW) != 0 && (drawLooseEdges || (medge[i].flag & MeshDataTypes.ME_LOOSEEDGE) == 0)) {
                    gl.glVertex3fv(mvert[medge[i].v1].co, 0);
                    gl.glVertex3fv(mvert[medge[i].v2].co, 0);
                }
            }
            gl.glEnd();
        }
    };

    public static DrawLooseEdges cdDM_drawLooseEdges = new DrawLooseEdges() {
        public void run(GL2 gl, DerivedMesh dm) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            MVert[] mverts = cddm.mvert;
            MEdge[] medges = cddm.medge;
            int medge_p = 0;
            int i;

            gl.glBegin(GL2.GL_LINES);
            for (i = 0; i < dm.numEdgeData; i++, medge_p++) {
                MEdge medge = medges[medge_p];
                if ((medge.flag & MeshDataTypes.ME_LOOSEEDGE) != 0) {
                    gl.glVertex3fv(mverts[medge.v1].co, 0);
                    gl.glVertex3fv(mverts[medge.v2].co, 0);
                }
            }
            gl.glEnd();
        }
    };

    public static DrawFacesSolid cdDM_drawFacesSolid = new DrawFacesSolid() {
        public void run(GL2 gl, DerivedMesh dm) //, int (*setMaterial)(int, void *attribs))
        {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            MVert[] mverts = cddm.mvert;
            MFace[] mfaces = cddm.mface;
            int mface_p = 0;
            float[] nors = (float[]) dm.getFaceDataArray.run(dm, CustomDataTypes.CD_NORMAL);
            int nors_p = 0;
            int a, glmode = -1, shademodel = -1, matnr = -1, drawCurrentMat = 1;

            gl.glBegin(glmode = GL2.GL_QUADS);
            for (a = 0; a < dm.numFaceData; a++, mface_p++) {
                int new_glmode, new_matnr, new_shademodel;
                MFace mface = mfaces[mface_p];

                new_glmode = mface.v4 != 0 ? GL2.GL_QUADS : GL2.GL_TRIANGLES;
                new_matnr = mface.mat_nr + 1;
                new_shademodel = (mface.flag & MeshDataTypes.ME_SMOOTH) != 0 ? GL2.GL_SMOOTH : GL2.GL_FLAT;

                if (new_glmode != glmode || new_matnr != matnr || new_shademodel != shademodel) {
                    gl.glEnd();

//			drawCurrentMat = setMaterial(matnr = new_matnr, null);

                    gl.glShadeModel(shademodel = new_shademodel);
                    gl.glBegin(glmode = new_glmode);
                }

                if (drawCurrentMat != 0) {
                    if (shademodel == GL2.GL_FLAT) {
                        if (nors != null) {
                            gl.glNormal3fv(nors, nors_p);
                        } else {
                            /* TODO make this better (cache facenormals as layer?) */
                            float[] nor = new float[3];
                            if (mface.v4 != 0) {
                                Arithb.CalcNormFloat4(mverts[mface.v1].co, mverts[mface.v2].co,
                                        mverts[mface.v3].co, mverts[mface.v4].co, nor);
                            } else {
                                Arithb.CalcNormFloat(mverts[mface.v1].co, mverts[mface.v2].co,
                                        mverts[mface.v3].co, nor);
                            }
                            gl.glNormal3fv(nor, 0);
                        }
                    }

                    if (shademodel == GL2.GL_SMOOTH) {
                        short[] no = mverts[mface.v1].no;
                        gl.glNormal3sv(no, 0);
                    }
                    gl.glVertex3fv(mverts[mface.v1].co, 0);
                    if (shademodel == GL2.GL_SMOOTH) {
                        short[] no = mverts[mface.v2].no;
                        gl.glNormal3sv(no, 0);
                    }
                    gl.glVertex3fv(mverts[mface.v2].co, 0);
                    if (shademodel == GL2.GL_SMOOTH) {
                        short[] no = mverts[mface.v3].no;
                        gl.glNormal3sv(no, 0);
                    }
                    gl.glVertex3fv(mverts[mface.v3].co, 0);
                    if (mface.v4 != 0) {
                        if (shademodel == GL2.GL_SMOOTH) {
                            short[] no = mverts[mface.v4].no;
                            gl.glNormal3sv(no, 0);
                        }
                        gl.glVertex3fv(mverts[mface.v4].co, 0);
                    }
                }

                if (nors != null) {
                    nors_p += 3;
                }
            }
            gl.glEnd();

            gl.glShadeModel(GL2.GL_FLAT);
        }
    };

    public static DrawFacesColored cdDM_drawFacesColored = new DrawFacesColored() {
        public void run(GL2 gl, DerivedMesh dm, boolean useTwoSided, byte[] col1, byte[] col2) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            int a, glmode;
            int cp1, cp2;
            MVert[] mverts = cddm.mvert;
            MFace[] mfaces = cddm.mface;
            int mface_p = 0;

            cp1 = 0;
            if (col2 != null) {
                cp2 = 0;
            } else {
                cp2 = -1;
                useTwoSided = false;
            }

            /* there's a conflict here... twosided colors versus culling...? */
            /* defined by history, only texture faces have culling option */
            /* we need that as mesh option builtin, next to double sided lighting */
            if (col1 != null && col2 != null) {
                gl.glEnable(GL2.GL_CULL_FACE);
            }

            gl.glShadeModel(GL2.GL_SMOOTH);
            gl.glBegin(glmode = GL2.GL_QUADS);
            for (a = 0; a < dm.numFaceData; a++, mface_p++, cp1 += 16) {
                MFace mface = mfaces[mface_p];
                int new_glmode = mface.v4 != 0 ? GL2.GL_QUADS : GL2.GL_TRIANGLES;

                if (new_glmode != glmode) {
                    gl.glEnd();
                    gl.glBegin(glmode = new_glmode);
                }

                gl.glColor3ub(col1[cp1 + 0], col1[cp1 + 1], col1[cp1 + 2]);
                gl.glVertex3fv(mverts[mface.v1].co, 0);
                gl.glColor3ub(col1[cp1 + 4], col1[cp1 + 5], col1[cp1 + 6]);
                gl.glVertex3fv(mverts[mface.v2].co, 0);
                gl.glColor3ub(col1[cp1 + 8], col1[cp1 + 9], col1[cp1 + 10]);
                gl.glVertex3fv(mverts[mface.v3].co, 0);
                if (mface.v4 != 0) {
                    gl.glColor3ub(col1[cp1 + 12], col1[cp1 + 13], col1[cp1 + 14]);
                    gl.glVertex3fv(mverts[mface.v4].co, 0);
                }

                if (useTwoSided) {
                    gl.glColor3ub(col2[cp2 + 8], col2[cp2 + 9], col2[cp2 + 10]);
                    gl.glVertex3fv(mverts[mface.v3].co, 0);
                    gl.glColor3ub(col2[cp2 + 4], col2[cp2 + 5], col2[cp2 + 6]);
                    gl.glVertex3fv(mverts[mface.v2].co, 0);
                    gl.glColor3ub(col2[cp2 + 0], col2[cp2 + 1], col2[cp2 + 2]);
                    gl.glVertex3fv(mverts[mface.v1].co, 0);
                    if (mface.v4 != 0) {
                        gl.glColor3ub(col2[cp2 + 12], col2[cp2 + 13], col2[cp2 + 14]);
                        gl.glVertex3fv(mverts[mface.v4].co, 0);
                    }
                }
                if (col2 != null) {
                    cp2 += 16;
                }
            }
            gl.glEnd();

            gl.glShadeModel(GL2.GL_FLAT);
            gl.glDisable(GL2.GL_CULL_FACE);
        }
    };

    private static void cdDM_drawFacesTex_common(GL2 gl, DerivedMesh dm) //,
    //               int (*drawParams)(MTFace *tface, MCol *mcol, int matnr),
    //               int (*drawParamsMapped)(void *userData, int index),
    //               void *userData)
    {
        CDDerivedMesh cddm = (CDDerivedMesh) dm;
//	MVert *mv = cddm->mvert;
//	MFace *mf = cddm->mface;
//	MCol *mcol = dm->getFaceDataArray(dm, CD_MCOL);
//	float *nors= dm->getFaceDataArray(dm, CD_NORMAL);
//	MTFace *tf = DM_get_face_data_layer(dm, CD_MTFACE);
//	int i, orig, *index = DM_get_face_data_layer(dm, CD_ORIGINDEX);
//
//	for(i = 0; i < dm->numFaceData; i++, mf++) {
//		MVert *mvert;
//		int flag;
//		unsigned char *cp = NULL;
//
//		if(drawParams) {
//			flag = drawParams(tf? &tf[i]: NULL, mcol? &mcol[i*4]: NULL, mf->mat_nr);
//		}
//		else {
//			if(index) {
//				orig = *index++;
//				if(orig == ORIGINDEX_NONE)		{ if(nors) nors += 3; continue; }
//				if(drawParamsMapped) flag = drawParamsMapped(userData, orig);
//				else	{ if(nors) nors += 3; continue; }
//			}
//			else
//				if(drawParamsMapped) flag = drawParamsMapped(userData, i);
//				else	{ if(nors) nors += 3; continue; }
//		}
//
//		if(flag != 0) { /* if the flag is 0 it means the face is hidden or invisible */
//			if (flag==1 && mcol)
//				cp= (unsigned char*) &mcol[i*4];
//
//			if(!(mf->flag&ME_SMOOTH)) {
//				if (nors) {
//					glNormal3fv(nors);
//				}
//				else {
//					/* TODO make this better (cache facenormals as layer?) */
//					float nor[3];
//					if(mf->v4) {
//						CalcNormFloat4(mv[mf->v1].co, mv[mf->v2].co,
//									   mv[mf->v3].co, mv[mf->v4].co,
//									   nor);
//					} else {
//						CalcNormFloat(mv[mf->v1].co, mv[mf->v2].co,
//									  mv[mf->v3].co, nor);
//					}
//					glNormal3fv(nor);
//				}
//			}
//
//			glBegin(mf->v4?GL_QUADS:GL_TRIANGLES);
//			if(tf) glTexCoord2fv(tf[i].uv[0]);
//			if(cp) glColor3ub(cp[3], cp[2], cp[1]);
//			mvert = &mv[mf->v1];
//			if(mf->flag&ME_SMOOTH) glNormal3sv(mvert->no);
//			glVertex3fv(mvert->co);
//
//			if(tf) glTexCoord2fv(tf[i].uv[1]);
//			if(cp) glColor3ub(cp[7], cp[6], cp[5]);
//			mvert = &mv[mf->v2];
//			if(mf->flag&ME_SMOOTH) glNormal3sv(mvert->no);
//			glVertex3fv(mvert->co);
//
//			if(tf) glTexCoord2fv(tf[i].uv[2]);
//			if(cp) glColor3ub(cp[11], cp[10], cp[9]);
//			mvert = &mv[mf->v3];
//			if(mf->flag&ME_SMOOTH) glNormal3sv(mvert->no);
//			glVertex3fv(mvert->co);
//
//			if(mf->v4) {
//				if(tf) glTexCoord2fv(tf[i].uv[3]);
//				if(cp) glColor3ub(cp[15], cp[14], cp[13]);
//				mvert = &mv[mf->v4];
//				if(mf->flag&ME_SMOOTH) glNormal3sv(mvert->no);
//				glVertex3fv(mvert->co);
//			}
//			glEnd();
//		}
//
//		if(nors) nors += 3;
//	}
    }

    public static DrawFacesTex cdDM_drawFacesTex = new DrawFacesTex() {
        public void run(GL2 gl, DerivedMesh dm) //, int (*setDrawOptions)(MTFace *tface, MCol *mcol, int matnr))
        {
            cdDM_drawFacesTex_common(gl, dm); //, setDrawOptions, NULL, NULL);
        }
    };

    public static DrawMappedFaces cdDM_drawMappedFaces = new DrawMappedFaces() {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedFacesFunc setDrawOptions, Object userData, int useColors) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
//	MVert *mv = cddm->mvert;
//	MFace *mf = cddm->mface;
//	MCol *mc;
//	float *nors= dm->getFaceDataArray(dm, CD_NORMAL);
//	int i, orig, *index = DM_get_face_data_layer(dm, CD_ORIGINDEX);
//
//	mc = DM_get_face_data_layer(dm, CD_WEIGHT_MCOL);
//	if(!mc)
//		mc = DM_get_face_data_layer(dm, CD_MCOL);
//
//	for(i = 0; i < dm->numFaceData; i++, mf++) {
//		int drawSmooth = (mf->flag & ME_SMOOTH);
//
//		if(index) {
//			orig = *index++;
//			if(setDrawOptions && orig == ORIGINDEX_NONE)
//				{ if(nors) nors += 3; continue; }
//		}
//		else
//			orig = i;
//
//		if(!setDrawOptions || setDrawOptions(userData, orig, &drawSmooth)) {
//			unsigned char *cp = NULL;
//
//			if(useColors && mc)
//				cp = (unsigned char *)&mc[i * 4];
//
//			glShadeModel(drawSmooth?GL_SMOOTH:GL_FLAT);
//			glBegin(mf->v4?GL_QUADS:GL_TRIANGLES);
//
//			if (!drawSmooth) {
//				if (nors) {
//					glNormal3fv(nors);
//				}
//				else {
//					/* TODO make this better (cache facenormals as layer?) */
//					float nor[3];
//					if(mf->v4) {
//						CalcNormFloat4(mv[mf->v1].co, mv[mf->v2].co,
//									   mv[mf->v3].co, mv[mf->v4].co,
//									   nor);
//					} else {
//						CalcNormFloat(mv[mf->v1].co, mv[mf->v2].co,
//									  mv[mf->v3].co, nor);
//					}
//					glNormal3fv(nor);
//				}
//
//				if(cp) glColor3ub(cp[3], cp[2], cp[1]);
//				glVertex3fv(mv[mf->v1].co);
//				if(cp) glColor3ub(cp[7], cp[6], cp[5]);
//				glVertex3fv(mv[mf->v2].co);
//				if(cp) glColor3ub(cp[11], cp[10], cp[9]);
//				glVertex3fv(mv[mf->v3].co);
//				if(mf->v4) {
//					if(cp) glColor3ub(cp[15], cp[14], cp[13]);
//					glVertex3fv(mv[mf->v4].co);
//				}
//			} else {
//				if(cp) glColor3ub(cp[3], cp[2], cp[1]);
//				glNormal3sv(mv[mf->v1].no);
//				glVertex3fv(mv[mf->v1].co);
//				if(cp) glColor3ub(cp[7], cp[6], cp[5]);
//				glNormal3sv(mv[mf->v2].no);
//				glVertex3fv(mv[mf->v2].co);
//				if(cp) glColor3ub(cp[11], cp[10], cp[9]);
//				glNormal3sv(mv[mf->v3].no);
//				glVertex3fv(mv[mf->v3].co);
//				if(mf->v4) {
//					if(cp) glColor3ub(cp[15], cp[14], cp[13]);
//					glNormal3sv(mv[mf->v4].no);
//					glVertex3fv(mv[mf->v4].co);
//				}
//			}
//
//			glEnd();
//		}
//
//		if (nors) nors += 3;
//	}
        }
    };

    public static DrawMappedFacesTex cdDM_drawMappedFacesTex = new DrawMappedFacesTex() {
        public void run(GL2 gl, DerivedMesh dm) //, int (*setDrawOptions)(void *userData, int index), void *userData)
        {
            cdDM_drawFacesTex_common(gl, dm); //, NULL, setDrawOptions, userData);
        }
    };

//static void cdDM_drawMappedFacesGLSL(DerivedMesh *dm, int (*setMaterial)(int, void *attribs), int (*setDrawOptions)(void *userData, int index), void *userData)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//	GPUVertexAttribs gattribs;
//	DMVertexAttribs attribs;
//	MVert *mvert = cddm->mvert;
//	MFace *mface = cddm->mface;
//	MTFace *tf = dm->getFaceDataArray(dm, CD_MTFACE);
//	float (*nors)[3] = dm->getFaceDataArray(dm, CD_NORMAL);
//	int a, b, dodraw, smoothnormal, matnr, new_matnr;
//	int transp, new_transp, orig_transp;
//	int orig, *index = dm->getFaceDataArray(dm, CD_ORIGINDEX);
//
//	matnr = -1;
//	smoothnormal = 0;
//	dodraw = 0;
//	transp = GPU_get_material_blend_mode();
//	orig_transp = transp;
//
//	memset(&attribs, 0, sizeof(attribs));
//
//	glShadeModel(GL_SMOOTH);
//	glBegin(GL_QUADS);
//
//	for(a = 0; a < dm->numFaceData; a++, mface++) {
//		new_matnr = mface->mat_nr + 1;
//
//		if(new_matnr != matnr) {
//			glEnd();
//
//			dodraw = setMaterial(matnr = new_matnr, &gattribs);
//			if(dodraw)
//				DM_vertex_attributes_from_gpu(dm, &gattribs, &attribs);
//
//			glBegin(GL_QUADS);
//		}
//
//		if(!dodraw) {
//			continue;
//		}
//		else if(setDrawOptions) {
//			orig = index[a];
//
//			if(orig == ORIGINDEX_NONE)
//				continue;
//			else if(!setDrawOptions(userData, orig))
//				continue;
//		}
//
//		if(tf) {
//			new_transp = tf[a].transp;
//
//			if(new_transp != transp) {
//				glEnd();
//
//				if(new_transp == GPU_BLEND_SOLID && orig_transp != GPU_BLEND_SOLID)
//					GPU_set_material_blend_mode(orig_transp);
//				else
//					GPU_set_material_blend_mode(new_transp);
//				transp = new_transp;
//
//				glBegin(GL_QUADS);
//			}
//		}
//
//		smoothnormal = (mface->flag & ME_SMOOTH);
//
//		if(!smoothnormal) {
//			if(nors) {
//				glNormal3fv(nors[a]);
//			}
//			else {
//				/* TODO ideally a normal layer should always be available */
//				float nor[3];
//				if(mface->v4) {
//					CalcNormFloat4(mvert[mface->v1].co, mvert[mface->v2].co,
//								   mvert[mface->v3].co, mvert[mface->v4].co,
//								   nor);
//				} else {
//					CalcNormFloat(mvert[mface->v1].co, mvert[mface->v2].co,
//								  mvert[mface->v3].co, nor);
//				}
//				glNormal3fv(nor);
//			}
//		}
//
//#define PASSVERT(index, vert) {													\
//	if(attribs.totorco)															\
//		glVertexAttrib3fvARB(attribs.orco.glIndex, attribs.orco.array[index]);	\
//	for(b = 0; b < attribs.tottface; b++) {										\
//		MTFace *tf = &attribs.tface[b].array[a];								\
//		glVertexAttrib2fvARB(attribs.tface[b].glIndex, tf->uv[vert]);			\
//	}																			\
//	for(b = 0; b < attribs.totmcol; b++) {										\
//		MCol *cp = &attribs.mcol[b].array[a*4 + vert];							\
//		GLubyte col[4];															\
//		col[0]= cp->b; col[1]= cp->g; col[2]= cp->r; col[3]= cp->a;				\
//		glVertexAttrib4ubvARB(attribs.mcol[b].glIndex, col);					\
//	}																			\
//	if(attribs.tottang) {														\
//		float *tang = attribs.tang.array[a*4 + vert];							\
//		glVertexAttrib3fvARB(attribs.tang.glIndex, tang);						\
//	}																			\
//	if(smoothnormal)															\
//		glNormal3sv(mvert[index].no);											\
//	glVertex3fv(mvert[index].co);												\
//}
//
//		PASSVERT(mface->v1, 0);
//		PASSVERT(mface->v2, 1);
//		PASSVERT(mface->v3, 2);
//		if(mface->v4)
//			PASSVERT(mface->v4, 3)
//		else
//			PASSVERT(mface->v3, 2)
//
//#undef PASSVERT
//	}
//	glEnd();
//
//	glShadeModel(GL_FLAT);
//}
//
//static void cdDM_drawFacesGLSL(DerivedMesh *dm, int (*setMaterial)(int, void *attribs))
//{
//	dm->drawMappedFacesGLSL(dm, setMaterial, NULL, NULL);
//}

public static DrawMappedEdges cdDM_drawMappedEdges = new DrawMappedEdges() {
        public void run(GL2 gl, DerivedMesh dm, DrawMappedEdgesFunc setDrawOptions, Object userData) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            MVert[] verts = cddm.mvert;
            MEdge[] edges = cddm.medge;
            int edge_p = 0;
            int i, orig, index = 0;
            float[] edgedata = (float[]) DM_get_edge_data_layer.run(dm, CustomDataTypes.CD_ORIGINDEX);

            gl.glBegin(GL2.GL_LINES);
            for (i = 0; i < dm.numEdgeData; i++, edge_p++) {
                MEdge edge = edges[edge_p];
                if (edgedata != null) {
                    orig = (int) edgedata[index++];
                    if (setDrawOptions != null && orig == CustomDataUtil.ORIGINDEX_NONE) {
                        continue;
                    }
                } else {
                    orig = i;
                }

                if (setDrawOptions == null || setDrawOptions.run(gl, userData, orig)) {
                    gl.glVertex3fv(verts[edge.v1].co, 0);
                    gl.glVertex3fv(verts[edge.v2].co, 0);
                }
            }
            gl.glEnd();
        }
    };

//static void cdDM_foreachMappedVert(
//                           DerivedMesh *dm,
//                           void (*func)(void *userData, int index, float *co,
//                                        float *no_f, short *no_s),
//                           void *userData)
//{
//	MVert *mv = CDDM_get_verts(dm);
//	int i, orig, *index = DM_get_vert_data_layer(dm, CD_ORIGINDEX);
//
//	for(i = 0; i < dm->numVertData; i++, mv++) {
//		if(index) {
//			orig = *index++;
//			if(orig == ORIGINDEX_NONE) continue;
//			func(userData, orig, mv->co, NULL, mv->no);
//		}
//		else
//			func(userData, i, mv->co, NULL, mv->no);
//	}
//}
//
//static void cdDM_foreachMappedEdge(
//                           DerivedMesh *dm,
//                           void (*func)(void *userData, int index,
//                                        float *v0co, float *v1co),
//                           void *userData)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*) dm;
//	MVert *mv = cddm->mvert;
//	MEdge *med = cddm->medge;
//	int i, orig, *index = DM_get_edge_data_layer(dm, CD_ORIGINDEX);
//
//	for(i = 0; i < dm->numEdgeData; i++, med++) {
//		if (index) {
//			orig = *index++;
//			if(orig == ORIGINDEX_NONE) continue;
//			func(userData, orig, mv[med->v1].co, mv[med->v2].co);
//		}
//		else
//			func(userData, i, mv[med->v1].co, mv[med->v2].co);
//	}
//}
//
//static void cdDM_foreachMappedFaceCenter(
//                           DerivedMesh *dm,
//                           void (*func)(void *userData, int index,
//                                        float *cent, float *no),
//                           void *userData)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	MVert *mv = cddm->mvert;
//	MFace *mf = cddm->mface;
//	int i, orig, *index = DM_get_face_data_layer(dm, CD_ORIGINDEX);
//
//	for(i = 0; i < dm->numFaceData; i++, mf++) {
//		float cent[3];
//		float no[3];
//
//		if (index) {
//			orig = *index++;
//			if(orig == ORIGINDEX_NONE) continue;
//		}
//		else
//			orig = i;
//
//		VECCOPY(cent, mv[mf->v1].co);
//		VecAddf(cent, cent, mv[mf->v2].co);
//		VecAddf(cent, cent, mv[mf->v3].co);
//
//		if (mf->v4) {
//			CalcNormFloat4(mv[mf->v1].co, mv[mf->v2].co,
//			               mv[mf->v3].co, mv[mf->v4].co, no);
//			VecAddf(cent, cent, mv[mf->v4].co);
//			VecMulf(cent, 0.25f);
//		} else {
//			CalcNormFloat(mv[mf->v1].co, mv[mf->v2].co,
//			              mv[mf->v3].co, no);
//			VecMulf(cent, 0.33333333333f);
//		}
//
//		func(userData, orig, cent, no);
//	}
//}

    public static Release cdDM_release = new Release() {
        public void run(DerivedMesh dm) {
            CDDerivedMesh cddm = (CDDerivedMesh) dm;
            if (DM_release(dm)) {

            }
        }
    };

    /**************** CDDM interface functions ****************/

    private static CDDerivedMesh cdDM_create(String desc) {
        CDDerivedMesh cddm;
        DerivedMesh dm;

        cddm = new CDDerivedMesh();
        dm = cddm.dm;

        dm.getMinMax = cdDM_getMinMax;

        dm.getNumVerts = cdDM_getNumVerts;
        dm.getNumFaces = cdDM_getNumFaces;
        dm.getNumEdges = cdDM_getNumEdges;

//	dm->getVert = cdDM_getVert;
//	dm->getEdge = cdDM_getEdge;
//	dm->getFace = cdDM_getFace;
        dm.copyVertArray = cdDM_copyVertArray;
        dm.copyEdgeArray = cdDM_copyEdgeArray;
        dm.copyFaceArray = cdDM_copyFaceArray;
//	dm->getVertData = DM_get_vert_data;
//	dm->getEdgeData = DM_get_edge_data;
//	dm->getFaceData = DM_get_face_data;
        dm.getVertDataArray = DM_get_vert_data_layer;
        dm.getEdgeDataArray = DM_get_edge_data_layer;
        dm.getFaceDataArray = DM_get_face_data_layer;

//	dm->getVertCos = cdDM_getVertCos;
//	dm->getVertCo = cdDM_getVertCo;
//	dm->getVertNo = cdDM_getVertNo;
//
//	dm->drawVerts = cdDM_drawVerts;
//
//	dm->drawUVEdges = cdDM_drawUVEdges;
        dm.drawEdges = cdDM_drawEdges;
        dm.drawLooseEdges = cdDM_drawLooseEdges;
        dm.drawMappedEdges = cdDM_drawMappedEdges;

        dm.drawFacesSolid = cdDM_drawFacesSolid;
        dm.drawFacesColored = cdDM_drawFacesColored;
        dm.drawFacesTex = cdDM_drawFacesTex;
//	dm->drawFacesGLSL = cdDM_drawFacesGLSL;
        dm.drawMappedFaces = cdDM_drawMappedFaces;
        dm.drawMappedFacesTex = cdDM_drawMappedFacesTex;
//	dm->drawMappedFacesGLSL = cdDM_drawMappedFacesGLSL;
//
//	dm->foreachMappedVert = cdDM_foreachMappedVert;
//	dm->foreachMappedEdge = cdDM_foreachMappedEdge;
//	dm->foreachMappedFaceCenter = cdDM_foreachMappedFaceCenter;

        dm.release = cdDM_release;

        return cddm;
    }

//DerivedMesh *CDDM_new(int numVerts, int numEdges, int numFaces)
//{
//	CDDerivedMesh *cddm = cdDM_create("CDDM_new dm");
//	DerivedMesh *dm = &cddm->dm;
//
//	DM_init(dm, numVerts, numEdges, numFaces);
//
//	CustomData_add_layer(&dm->vertData, CD_ORIGINDEX, CD_CALLOC, NULL, numVerts);
//	CustomData_add_layer(&dm->edgeData, CD_ORIGINDEX, CD_CALLOC, NULL, numEdges);
//	CustomData_add_layer(&dm->faceData, CD_ORIGINDEX, CD_CALLOC, NULL, numFaces);
//
//	CustomData_add_layer(&dm->vertData, CD_MVERT, CD_CALLOC, NULL, numVerts);
//	CustomData_add_layer(&dm->edgeData, CD_MEDGE, CD_CALLOC, NULL, numEdges);
//	CustomData_add_layer(&dm->faceData, CD_MFACE, CD_CALLOC, NULL, numFaces);
//
//	cddm->mvert = CustomData_get_layer(&dm->vertData, CD_MVERT);
//	cddm->medge = CustomData_get_layer(&dm->edgeData, CD_MEDGE);
//	cddm->mface = CustomData_get_layer(&dm->faceData, CD_MFACE);
//
//	return dm;
//}

public static DerivedMesh CDDM_from_mesh(Mesh mesh, bObject ob)
{
	CDDerivedMesh cddm = cdDM_create("CDDM_from_mesh dm");
	DerivedMesh dm = cddm.dm;
	int mask = CustomDataUtil.CD_MASK_MESH & (~CustomDataTypes.CD_MASK_MDISPS);
	int i, alloctype;
        Integer[] index;

	/* this does a referenced copy, the only new layers being ORIGINDEX,
	 * with an exception for fluidsim */

        DerivedMesh.DM_init(dm, mesh.totvert, mesh.totedge, mesh.totface);

        CustomDataUtil.CustomData_add_layer(dm.vertData, CustomDataTypes.CD_ORIGINDEX, CustomDataUtil.CD_CALLOC, null, mesh.totvert);
        CustomDataUtil.CustomData_add_layer(dm.edgeData, CustomDataTypes.CD_ORIGINDEX, CustomDataUtil.CD_CALLOC, null, mesh.totedge);
        CustomDataUtil.CustomData_add_layer(dm.faceData, CustomDataTypes.CD_ORIGINDEX, CustomDataUtil.CD_CALLOC, null, mesh.totface);

        dm.deformedOnly = 1;

        alloctype = CustomDataUtil.CD_REFERENCE;

        CustomDataUtil.CustomData_merge(mesh.vdata, dm.vertData, CustomDataUtil.CD_MASK_MESH, alloctype, mesh.totvert);
        CustomDataUtil.CustomData_merge(mesh.edata, dm.edgeData, CustomDataUtil.CD_MASK_MESH, alloctype, mesh.totedge);
        CustomDataUtil.CustomData_merge(mesh.fdata, dm.faceData, CustomDataUtil.CD_MASK_MESH, alloctype, mesh.totface);

        cddm.mvert = ((MVert) CustomDataUtil.CustomData_get_layer(dm.vertData, CustomDataTypes.CD_MVERT)).myarray;
        cddm.medge = ((MEdge) CustomDataUtil.CustomData_get_layer(dm.edgeData, CustomDataTypes.CD_MEDGE)).myarray;
        cddm.mface = ((MFace) CustomDataUtil.CustomData_get_layer(dm.faceData, CustomDataTypes.CD_MFACE)).myarray;

//	index = CustomData_get_layer(&dm->vertData, CD_ORIGINDEX);
//	for(i = 0; i < mesh->totvert; ++i, ++index)
//		*index = i;
//
//	index = CustomData_get_layer(&dm->edgeData, CD_ORIGINDEX);
//	for(i = 0; i < mesh->totedge; ++i, ++index)
//		*index = i;
//
//	index = CustomData_get_layer(&dm->faceData, CD_ORIGINDEX);
//	for(i = 0; i < mesh->totface; ++i, ++index)
//		*index = i;

	return dm;
}

//DerivedMesh *CDDM_from_editmesh(EditMesh *em, Mesh *me)
//{
//	DerivedMesh *dm = CDDM_new(BLI_countlist(&em->verts),
//	                           BLI_countlist(&em->edges),
//	                           BLI_countlist(&em->faces));
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	EditVert *eve;
//	EditEdge *eed;
//	EditFace *efa;
//	MVert *mvert = cddm->mvert;
//	MEdge *medge = cddm->medge;
//	MFace *mface = cddm->mface;
//	int i, *index;
//
//	dm->deformedOnly = 1;
//
//	CustomData_merge(&em->vdata, &dm->vertData, CD_MASK_DERIVEDMESH,
//	                 CD_CALLOC, dm->numVertData);
//	/* CustomData_merge(&em->edata, &dm->edgeData, CD_MASK_DERIVEDMESH,
//	                 CD_CALLOC, dm->numEdgeData); */
//	CustomData_merge(&em->fdata, &dm->faceData, CD_MASK_DERIVEDMESH,
//	                 CD_CALLOC, dm->numFaceData);
//
//	/* set eve->hash to vert index */
//	for(i = 0, eve = em->verts.first; eve; eve = eve->next, ++i)
//		eve->tmp.l = i;
//
//	/* Need to be able to mark loose edges */
//	for(eed = em->edges.first; eed; eed = eed->next) {
//		eed->f2 = 0;
//	}
//	for(efa = em->faces.first; efa; efa = efa->next) {
//		efa->e1->f2 = 1;
//		efa->e2->f2 = 1;
//		efa->e3->f2 = 1;
//		if(efa->e4) efa->e4->f2 = 1;
//	}
//
//	index = dm->getVertDataArray(dm, CD_ORIGINDEX);
//	for(i = 0, eve = em->verts.first; i < dm->numVertData;
//	    i++, eve = eve->next, index++) {
//		MVert *mv = &mvert[i];
//
//		VECCOPY(mv->co, eve->co);
//
//		mv->no[0] = eve->no[0] * 32767.0;
//		mv->no[1] = eve->no[1] * 32767.0;
//		mv->no[2] = eve->no[2] * 32767.0;
//		mv->bweight = (unsigned char) (eve->bweight * 255.0f);
//
//		mv->mat_nr = 0;
//		mv->flag = 0;
//
//		*index = i;
//
//		CustomData_from_em_block(&em->vdata, &dm->vertData, eve->data, i);
//	}
//
//	index = dm->getEdgeDataArray(dm, CD_ORIGINDEX);
//	for(i = 0, eed = em->edges.first; i < dm->numEdgeData;
//	    i++, eed = eed->next, index++) {
//		MEdge *med = &medge[i];
//
//		med->v1 = eed->v1->tmp.l;
//		med->v2 = eed->v2->tmp.l;
//		med->crease = (unsigned char) (eed->crease * 255.0f);
//		med->bweight = (unsigned char) (eed->bweight * 255.0f);
//		med->flag = ME_EDGEDRAW|ME_EDGERENDER;
//
//		if(eed->seam) med->flag |= ME_SEAM;
//		if(eed->sharp) med->flag |= ME_SHARP;
//		if(!eed->f2) med->flag |= ME_LOOSEEDGE;
//
//		*index = i;
//
//		/* CustomData_from_em_block(&em->edata, &dm->edgeData, eed->data, i); */
//	}
//
//	index = dm->getFaceDataArray(dm, CD_ORIGINDEX);
//	for(i = 0, efa = em->faces.first; i < dm->numFaceData;
//	    i++, efa = efa->next, index++) {
//		MFace *mf = &mface[i];
//
//		mf->v1 = efa->v1->tmp.l;
//		mf->v2 = efa->v2->tmp.l;
//		mf->v3 = efa->v3->tmp.l;
//		mf->v4 = efa->v4 ? efa->v4->tmp.l : 0;
//		mf->mat_nr = efa->mat_nr;
//		mf->flag = efa->flag;
//
//		*index = i;
//
//		CustomData_from_em_block(&em->fdata, &dm->faceData, efa->data, i);
//		test_index_face(mf, &dm->faceData, i, efa->v4?4:3);
//	}
//
//	return dm;
//}

    public static DerivedMesh CDDM_copy(DerivedMesh source) {
        CDDerivedMesh cddm = cdDM_create("CDDM_copy cddm");
        DerivedMesh dm = cddm.dm;
//	int numVerts = source->numVertData;
//	int numEdges = source->numEdgeData;
//	int numFaces = source->numFaceData;
//
//	/* this initializes dm, and copies all non mvert/medge/mface layers */
//	DM_from_template(dm, source, numVerts, numEdges, numFaces);
//	dm->deformedOnly = source->deformedOnly;
//
//	CustomData_copy_data(&source->vertData, &dm->vertData, 0, 0, numVerts);
//	CustomData_copy_data(&source->edgeData, &dm->edgeData, 0, 0, numEdges);
//	CustomData_copy_data(&source->faceData, &dm->faceData, 0, 0, numFaces);
//
//	/* now add mvert/medge/mface layers */
//	cddm->mvert = source->dupVertArray(source);
//	cddm->medge = source->dupEdgeArray(source);
//	cddm->mface = source->dupFaceArray(source);
//
//	CustomData_add_layer(&dm->vertData, CD_MVERT, CD_ASSIGN, cddm->mvert, numVerts);
//	CustomData_add_layer(&dm->edgeData, CD_MEDGE, CD_ASSIGN, cddm->medge, numEdges);
//	CustomData_add_layer(&dm->faceData, CD_MFACE, CD_ASSIGN, cddm->mface, numFaces);

        return dm;
    }

//DerivedMesh *CDDM_from_template(DerivedMesh *source,
//                                int numVerts, int numEdges, int numFaces)
//{
//	CDDerivedMesh *cddm = cdDM_create("CDDM_from_template dest");
//	DerivedMesh *dm = &cddm->dm;
//
//	/* this does a copy of all non mvert/medge/mface layers */
//	DM_from_template(dm, source, numVerts, numEdges, numFaces);
//
//	/* now add mvert/medge/mface layers */
//	CustomData_add_layer(&dm->vertData, CD_MVERT, CD_CALLOC, NULL, numVerts);
//	CustomData_add_layer(&dm->edgeData, CD_MEDGE, CD_CALLOC, NULL, numEdges);
//	CustomData_add_layer(&dm->faceData, CD_MFACE, CD_CALLOC, NULL, numFaces);
//
//	cddm->mvert = CustomData_get_layer(&dm->vertData, CD_MVERT);
//	cddm->medge = CustomData_get_layer(&dm->edgeData, CD_MEDGE);
//	cddm->mface = CustomData_get_layer(&dm->faceData, CD_MFACE);
//
//	return dm;
//}

public static void CDDM_apply_vert_coords(DerivedMesh dm, float[][] vertCoords)
{
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	MVert *vert;
//	int i;
//
//	/* this will just return the pointer if it wasn't a referenced layer */
//	vert = CustomData_duplicate_referenced_layer(&dm->vertData, CD_MVERT);
//	cddm->mvert = vert;
//
//	for(i = 0; i < dm->numVertData; ++i, ++vert)
//		VECCOPY(vert->co, vertCoords[i]);
}

//void CDDM_apply_vert_normals(DerivedMesh *dm, short (*vertNormals)[3])
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	MVert *vert;
//	int i;
//
//	/* this will just return the pointer if it wasn't a referenced layer */
//	vert = CustomData_duplicate_referenced_layer(&dm->vertData, CD_MVERT);
//	cddm->mvert = vert;
//
//	for(i = 0; i < dm->numVertData; ++i, ++vert)
//		VECCOPY(vert->no, vertNormals[i]);
//}

/* adapted from mesh_calc_normals */
public static void CDDM_calc_normals(DerivedMesh dm)
{
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	float (*temp_nors)[3];
//	float (*face_nors)[3];
//	int i;
//	int numVerts = dm->numVertData;
//	int numFaces = dm->numFaceData;
//	MFace *mf;
//	MVert *mv;
//
//	if(numVerts == 0) return;
//
//	temp_nors = MEM_callocN(numVerts * sizeof(*temp_nors),
//	                        "CDDM_calc_normals temp_nors");
//
//	/* we don't want to overwrite any referenced layers */
//	mv = CustomData_duplicate_referenced_layer(&dm->vertData, CD_MVERT);
//	cddm->mvert = mv;
//
//	/* make a face normal layer if not present */
//	face_nors = CustomData_get_layer(&dm->faceData, CD_NORMAL);
//	if(!face_nors)
//		face_nors = CustomData_add_layer(&dm->faceData, CD_NORMAL, CD_CALLOC,
//		                                 NULL, dm->numFaceData);
//
//	/* calculate face normals and add to vertex normals */
//	mf = CDDM_get_faces(dm);
//	for(i = 0; i < numFaces; i++, mf++) {
//		float *f_no = face_nors[i];
//
//		if(mf->v4)
//			CalcNormFloat4(mv[mf->v1].co, mv[mf->v2].co,
//			               mv[mf->v3].co, mv[mf->v4].co, f_no);
//		else
//			CalcNormFloat(mv[mf->v1].co, mv[mf->v2].co,
//			              mv[mf->v3].co, f_no);
//
//		VecAddf(temp_nors[mf->v1], temp_nors[mf->v1], f_no);
//		VecAddf(temp_nors[mf->v2], temp_nors[mf->v2], f_no);
//		VecAddf(temp_nors[mf->v3], temp_nors[mf->v3], f_no);
//		if(mf->v4)
//			VecAddf(temp_nors[mf->v4], temp_nors[mf->v4], f_no);
//	}
//
//	/* normalize vertex normals and assign */
//	for(i = 0; i < numVerts; i++, mv++) {
//		float *no = temp_nors[i];
//
//		if (Normalize(no) == 0.0) {
//			VECCOPY(no, mv->co);
//			Normalize(no);
//		}
//
//		mv->no[0] = (short)(no[0] * 32767.0);
//		mv->no[1] = (short)(no[1] * 32767.0);
//		mv->no[2] = (short)(no[2] * 32767.0);
//	}
//
//	MEM_freeN(temp_nors);
}

//void CDDM_calc_edges(DerivedMesh *dm)
//{
//	CDDerivedMesh *cddm = (CDDerivedMesh*)dm;
//	CustomData edgeData;
//	EdgeHashIterator *ehi;
//	MFace *mf = cddm->mface;
//	MEdge *med;
//	EdgeHash *eh = BLI_edgehash_new();
//	int i, *index, numEdges, maxFaces = dm->numFaceData;
//
//	for (i = 0; i < maxFaces; i++, mf++) {
//		if (!BLI_edgehash_haskey(eh, mf->v1, mf->v2))
//			BLI_edgehash_insert(eh, mf->v1, mf->v2, NULL);
//		if (!BLI_edgehash_haskey(eh, mf->v2, mf->v3))
//			BLI_edgehash_insert(eh, mf->v2, mf->v3, NULL);
//
//		if (mf->v4) {
//			if (!BLI_edgehash_haskey(eh, mf->v3, mf->v4))
//				BLI_edgehash_insert(eh, mf->v3, mf->v4, NULL);
//			if (!BLI_edgehash_haskey(eh, mf->v4, mf->v1))
//				BLI_edgehash_insert(eh, mf->v4, mf->v1, NULL);
//		} else {
//			if (!BLI_edgehash_haskey(eh, mf->v3, mf->v1))
//				BLI_edgehash_insert(eh, mf->v3, mf->v1, NULL);
//		}
//	}
//
//	numEdges = BLI_edgehash_size(eh);
//
//	/* write new edges into a temporary CustomData */
//	memset(&edgeData, 0, sizeof(edgeData));
//	CustomData_add_layer(&edgeData, CD_MEDGE, CD_CALLOC, NULL, numEdges);
//	CustomData_add_layer(&edgeData, CD_ORIGINDEX, CD_CALLOC, NULL, numEdges);
//
//	ehi = BLI_edgehashIterator_new(eh);
//	med = CustomData_get_layer(&edgeData, CD_MEDGE);
//	index = CustomData_get_layer(&edgeData, CD_ORIGINDEX);
//	for(i = 0; !BLI_edgehashIterator_isDone(ehi);
//	    BLI_edgehashIterator_step(ehi), ++i, ++med, ++index) {
//		BLI_edgehashIterator_getKey(ehi, (int*)&med->v1, (int*)&med->v2);
//
//		med->flag = ME_EDGEDRAW|ME_EDGERENDER;
//		*index = ORIGINDEX_NONE;
//	}
//	BLI_edgehashIterator_free(ehi);
//
//	/* free old CustomData and assign new one */
//	CustomData_free(&dm->edgeData, dm->numEdgeData);
//	dm->edgeData = edgeData;
//	dm->numEdgeData = numEdges;
//
//	cddm->medge = CustomData_get_layer(&dm->edgeData, CD_MEDGE);
//
//	BLI_edgehash_free(eh, NULL);
//}
//
//void CDDM_lower_num_verts(DerivedMesh *dm, int numVerts)
//{
//	if (numVerts < dm->numVertData)
//		CustomData_free_elem(&dm->vertData, numVerts, dm->numVertData-numVerts);
//
//	dm->numVertData = numVerts;
//}
//
//void CDDM_lower_num_edges(DerivedMesh *dm, int numEdges)
//{
//	if (numEdges < dm->numEdgeData)
//		CustomData_free_elem(&dm->edgeData, numEdges, dm->numEdgeData-numEdges);
//
//	dm->numEdgeData = numEdges;
//}
//
//void CDDM_lower_num_faces(DerivedMesh *dm, int numFaces)
//{
//	if (numFaces < dm->numFaceData)
//		CustomData_free_elem(&dm->faceData, numFaces, dm->numFaceData-numFaces);
//
//	dm->numFaceData = numFaces;
//}
//
//MVert *CDDM_get_vert(DerivedMesh *dm, int index)
//{
//	return &((CDDerivedMesh*)dm)->mvert[index];
//}
//
//MEdge *CDDM_get_edge(DerivedMesh *dm, int index)
//{
//	return &((CDDerivedMesh*)dm)->medge[index];
//}
//
//MFace *CDDM_get_face(DerivedMesh *dm, int index)
//{
//	return &((CDDerivedMesh*)dm)->mface[index];
//}
//
//MVert *CDDM_get_verts(DerivedMesh *dm)
//{
//	return ((CDDerivedMesh*)dm)->mvert;
//}
//
//MEdge *CDDM_get_edges(DerivedMesh *dm)
//{
//	return ((CDDerivedMesh*)dm)->medge;
//}
//
//MFace *CDDM_get_faces(DerivedMesh *dm)
//{
//	return ((CDDerivedMesh*)dm)->mface;
//}
//
///* Multires DerivedMesh, extends CDDM */
//typedef struct MultiresDM {
//	CDDerivedMesh cddm;
//
//	MultiresModifierData *mmd;
//
//	int lvl, totlvl;
//	float (*orco)[3];
//	MVert *subco;
//
//	ListBase *vert_face_map, *vert_edge_map;
//	IndexNode *vert_face_map_mem, *vert_edge_map_mem;
//	int *face_offsets;
//
//	Mesh *me;
//	int modified;
//
//	void (*update)(DerivedMesh*);
//} MultiresDM;
//
//static void MultiresDM_release(DerivedMesh *dm)
//{
//	MultiresDM *mrdm = (MultiresDM*)dm;
//	int mvert_layer;
//
//	/* Before freeing, need to update the displacement map */
//	if(dm->needsFree && mrdm->modified)
//		mrdm->update(dm);
//
//	/* If the MVert data is being used as the sculpt undo store, don't free it */
//	mvert_layer = CustomData_get_layer_index(&dm->vertData, CD_MVERT);
//	if(mvert_layer != -1) {
//		CustomDataLayer *cd = &dm->vertData.layers[mvert_layer];
//		if(cd->data == mrdm->mmd->undo_verts)
//			cd->flag |= CD_FLAG_NOFREE;
//	}
//
//	if(DM_release(dm)) {
//		MEM_freeN(mrdm->subco);
//		MEM_freeN(mrdm->orco);
//		if(mrdm->vert_face_map)
//			MEM_freeN(mrdm->vert_face_map);
//		if(mrdm->vert_face_map_mem)
//			MEM_freeN(mrdm->vert_face_map_mem);
//		if(mrdm->vert_edge_map)
//			MEM_freeN(mrdm->vert_edge_map);
//		if(mrdm->vert_edge_map_mem)
//			MEM_freeN(mrdm->vert_edge_map_mem);
//		if(mrdm->face_offsets)
//			MEM_freeN(mrdm->face_offsets);
//		MEM_freeN(mrdm);
//	}
//}
//
//DerivedMesh *MultiresDM_new(MultiresSubsurf *ms, DerivedMesh *orig, int numVerts, int numEdges, int numFaces)
//{
//	MultiresDM *mrdm = MEM_callocN(sizeof(MultiresDM), "MultiresDM");
//	CDDerivedMesh *cddm = cdDM_create("MultiresDM CDDM");
//	DerivedMesh *dm = NULL;
//
//	mrdm->cddm = *cddm;
//	MEM_freeN(cddm);
//	dm = &mrdm->cddm.dm;
//
//	mrdm->mmd = ms->mmd;
//	mrdm->me = ms->me;
//
//	if(dm) {
//		MDisps *disps;
//		MVert *mvert;
//		int i;
//
//		DM_from_template(dm, orig, numVerts, numEdges, numFaces);
//		CustomData_free_layers(&dm->faceData, CD_MDISPS, numFaces);
//
//		disps = CustomData_get_layer(&orig->faceData, CD_MDISPS);
//		if(disps)
//			CustomData_add_layer(&dm->faceData, CD_MDISPS, CD_REFERENCE, disps, numFaces);
//
//
//		mvert = CustomData_get_layer(&orig->vertData, CD_MVERT);
//		mrdm->orco = MEM_callocN(sizeof(float) * 3 * orig->getNumVerts(orig), "multires orco");
//		for(i = 0; i < orig->getNumVerts(orig); ++i)
//			VecCopyf(mrdm->orco[i], mvert[i].co);
//	}
//	else
//		DM_init(dm, numVerts, numEdges, numFaces);
//
//	CustomData_add_layer(&dm->vertData, CD_MVERT, CD_CALLOC, NULL, numVerts);
//	CustomData_add_layer(&dm->edgeData, CD_MEDGE, CD_CALLOC, NULL, numEdges);
//	CustomData_add_layer(&dm->faceData, CD_MFACE, CD_CALLOC, NULL, numFaces);
//
//	mrdm->cddm.mvert = CustomData_get_layer(&dm->vertData, CD_MVERT);
//	mrdm->cddm.medge = CustomData_get_layer(&dm->edgeData, CD_MEDGE);
//	mrdm->cddm.mface = CustomData_get_layer(&dm->faceData, CD_MFACE);
//
//	mrdm->lvl = ms->mmd->lvl;
//	mrdm->totlvl = ms->mmd->totlvl;
//	mrdm->subco = MEM_callocN(sizeof(MVert)*numVerts, "multires subdivided verts");
//	mrdm->modified = 0;
//
//	dm->release = MultiresDM_release;
//
//	return dm;
//}
//
//Mesh *MultiresDM_get_mesh(DerivedMesh *dm)
//{
//	return ((MultiresDM*)dm)->me;
//}
//
//void *MultiresDM_get_orco(DerivedMesh *dm)
//{
//	return ((MultiresDM*)dm)->orco;
//
//}
//
//MVert *MultiresDM_get_subco(DerivedMesh *dm)
//{
//	return ((MultiresDM*)dm)->subco;
//}
//
//int MultiresDM_get_totlvl(DerivedMesh *dm)
//{
//	return ((MultiresDM*)dm)->totlvl;
//}
//
//int MultiresDM_get_lvl(DerivedMesh *dm)
//{
//	return ((MultiresDM*)dm)->lvl;
//}
//
//void MultiresDM_set_orco(DerivedMesh *dm, float (*orco)[3])
//{
//	((MultiresDM*)dm)->orco = orco;
//}
//
//void MultiresDM_set_update(DerivedMesh *dm, void (*update)(DerivedMesh*))
//{
//	((MultiresDM*)dm)->update = update;
//}
//
//ListBase *MultiresDM_get_vert_face_map(DerivedMesh *dm)
//{
//	MultiresDM *mrdm = (MultiresDM*)dm;
//
//	if(!mrdm->vert_face_map)
//		create_vert_face_map(&mrdm->vert_face_map, &mrdm->vert_face_map_mem, mrdm->me->mface,
//				     mrdm->me->totvert, mrdm->me->totface);
//
//	return mrdm->vert_face_map;
//}
//
//ListBase *MultiresDM_get_vert_edge_map(DerivedMesh *dm)
//{
//	MultiresDM *mrdm = (MultiresDM*)dm;
//
//	if(!mrdm->vert_edge_map)
//		create_vert_edge_map(&mrdm->vert_edge_map, &mrdm->vert_edge_map_mem, mrdm->me->medge,
//				     mrdm->me->totvert, mrdm->me->totedge);
//
//	return mrdm->vert_edge_map;
//}
//
//int *MultiresDM_get_face_offsets(DerivedMesh *dm)
//{
//	MultiresDM *mrdm = (MultiresDM*)dm;
//	int i, accum = 0;
//
//	if(!mrdm->face_offsets) {
//		int len = (int)pow(2, mrdm->lvl - 2) - 1;
//		int area = len * len;
//		int t = 1 + len * 3 + area * 3, q = t + len + area;
//
//		mrdm->face_offsets = MEM_callocN(sizeof(int) * mrdm->me->totface, "mrdm face offsets");
//		for(i = 0; i < mrdm->me->totface; ++i) {
//			mrdm->face_offsets[i] = accum;
//
//			accum += (mrdm->me->mface[i].v4 ? q : t);
//		}
//	}
//
//	return mrdm->face_offsets;
//}
//
//void MultiresDM_mark_as_modified(DerivedMesh *dm)
//{
//	((MultiresDM*)dm)->modified = 1;
//}
}
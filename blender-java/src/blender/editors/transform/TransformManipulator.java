/**
* $Id:
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
 * The Original Code is Copyright (C) 2005 Blender Foundation
 * All rights reserved.
 *
 * The Original Code is: all of this file.
 *
 * Contributor(s): none yet.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.transform;

//#include <stdlib.h>

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import blender.blenkernel.Blender;
import blender.blenkernel.Global;
import blender.blenkernel.MeshUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.blenlib.EditVertUtil.EditMesh;
import blender.blenlib.EditVertUtil.EditSelection;
import blender.blenlib.EditVertUtil.EditVert;
import blender.editors.mesh.EditMeshLib;
import blender.editors.space_view3d.DrawObject;
import blender.editors.space_view3d.View3dView;
import blender.editors.uinterface.Resources;
import blender.makesdna.ObjectTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.rctf;
import blender.makesdna.sdna.wmOperator;
import blender.makesrna.RnaAccess;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmSubWindow;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmEvent;

//import com.sun.opengl.util.BufferUtil;
import com.jogamp.common.nio.Buffers;

//#include <string.h>
//#include <math.h>
//
//#ifndef WIN32
//#include <unistd.h>
//#else
//#include <io.h>
//#endif
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_armature_types.h"
//#include "DNA_action_types.h"
//#include "DNA_curve_types.h"
//#include "DNA_lattice_types.h"
//#include "DNA_mesh_types.h"
//#include "DNA_meta_types.h"
//#include "DNA_object_types.h"
//#include "DNA_particle_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_space_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_view3d_types.h"
//
//#include "RNA_access.h"
//
//#include "BKE_armature.h"
//#include "BKE_context.h"
//#include "BKE_global.h"
//#include "BKE_lattice.h"
//#include "BKE_mesh.h"
//#include "BKE_object.h"
//#include "BKE_particle.h"
//#include "BKE_utildefines.h"
//
//#include "BLI_arithb.h"
//#include "BLI_editVert.h"
//
//#include "BIF_gl.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "ED_armature.h"
//#include "ED_mesh.h"
//#include "ED_particle.h"
//#include "ED_space_api.h"
//#include "ED_transform.h"
//#include "ED_view3d.h"
//
//#include "UI_resources.h"
//
///* local module include */
//#include "transform.h"

public class TransformManipulator {

/* return codes for select, and drawing flags */

public static final int MAN_TRANS_X=		1;
public static final int MAN_TRANS_Y=		2;
public static final int MAN_TRANS_Z=		4;
public static final int MAN_TRANS_C=		7;

public static final int MAN_ROT_X=		8;
public static final int MAN_ROT_Y=		16;
public static final int MAN_ROT_Z=		32;
public static final int MAN_ROT_V=		64;
public static final int MAN_ROT_T=		128;
public static final int MAN_ROT_C=		248;

public static final int MAN_SCALE_X=		256;
public static final int MAN_SCALE_Y=		512;
public static final int MAN_SCALE_Z=		1024;
public static final int MAN_SCALE_C=		1792;

/* color codes */

public static final int MAN_RGB=		0;
public static final int MAN_GHOST=	1;
public static final int MAN_MOVECOL=	2;


static boolean is_mat4_flipped(float[][] mat)
{
	float[] vec = new float[3];

	Arithb.Crossf(vec, mat[0], mat[1]);
	if( Arithb.Inpf(vec, mat[2]) < 0.0f ) return true;
	return false;
}

/* transform widget center calc helper for below */
static void calc_tw_center(Scene scene, float[] co)
{
	float[] twcent= scene.twcent;
	float[] min= scene.twmin;
	float[] max= scene.twmax;

	UtilDefines.DO_MINMAX(co, min, max);
	Arithb.VecAddf(twcent, twcent, co);
}

static short protectflag_to_drawflags(short protectflag, short drawflags)
{
	if((protectflag & ObjectTypes.OB_LOCK_LOCX)!=0)
		drawflags &= ~MAN_TRANS_X;
	if((protectflag & ObjectTypes.OB_LOCK_LOCY)!=0)
		drawflags &= ~MAN_TRANS_Y;
	if((protectflag & ObjectTypes.OB_LOCK_LOCZ)!=0)
		drawflags &= ~MAN_TRANS_Z;

	if((protectflag & ObjectTypes.OB_LOCK_ROTX)!=0)
		drawflags &= ~MAN_ROT_X;
	if((protectflag & ObjectTypes.OB_LOCK_ROTY)!=0)
		drawflags &= ~MAN_ROT_Y;
	if((protectflag & ObjectTypes.OB_LOCK_ROTZ)!=0)
		drawflags &= ~MAN_ROT_Z;

	if((protectflag & ObjectTypes.OB_LOCK_SCALEX)!=0)
		drawflags &= ~MAN_SCALE_X;
	if((protectflag & ObjectTypes.OB_LOCK_SCALEY)!=0)
		drawflags &= ~MAN_SCALE_Y;
	if((protectflag & ObjectTypes.OB_LOCK_SCALEZ)!=0)
		drawflags &= ~MAN_SCALE_Z;
        return drawflags;
}

///* for pose mode */
//static void stats_pose(Scene *scene, View3D *v3d, bPoseChannel *pchan)
//{
//	Bone *bone= pchan.bone;
//
//	if(bone) {
//		if (bone.flag & BONE_TRANSFORM) {
//			calc_tw_center(scene, pchan.pose_head);
//			protectflag_to_drawflags(pchan.protectflag, &v3d.twdrawflag);
//		}
//	}
//}
//
///* for editmode*/
//static void stats_editbone(View3D *v3d, EditBone *ebo)
//{
//	if (ebo.flag & BONE_EDITMODE_LOCKED)
//		protectflag_to_drawflags(OB_LOCK_LOC|OB_LOCK_ROT|OB_LOCK_SCALE, &v3d.twdrawflag);
//}

/* centroid, boundbox, of selection */
/* returns total items selected */
public static int calc_manipulator_stats(bContext C)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	Scene scene= bContext.CTX_data_scene(C);
	bObject obedit= bContext.CTX_data_edit_object(C);
	View3D v3d= (View3D)sa.spacedata.first;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	Base base;
	bObject ob= (scene.basact!=null? scene.basact.object: null);
	float[] normal={0.0f, 0.0f, 0.0f};
	float[] plane={0.0f, 0.0f, 0.0f};
	int a, totsel= 0;

	/* transform widget matrix */
	Arithb.Mat4One(rv3d.twmat);

	//v3d.twdrawflag= (short)0xFFFF;
	rv3d.twdrawflag= (short)0xFFFF;

	/* transform widget centroid/center */
	scene.twcent[0]= scene.twcent[1]= scene.twcent[2]= 0.0f;
	UtilDefines.INIT_MINMAX(scene.twmin, scene.twmax);

	if(obedit!=null) {
		ob= obedit;
		if((ob.lay & v3d.lay)==0) return 0;

		if(obedit.type==ObjectTypes.OB_MESH) {
			EditMesh em = MeshUtil.BKE_mesh_get_editmesh((Mesh)obedit.data);
			EditVert eve;
			EditSelection ese = new EditSelection();
			float[] vec= {0,0,0};

			/* USE LAST SELECTE WITH ACTIVE */
			if (v3d.around==View3dTypes.V3D_ACTIVE && EditMeshLib.EM_get_actSelection(em, ese)) {
				EditMeshLib.EM_editselection_center(vec, ese);
				calc_tw_center(scene, vec);
				totsel= 1;
			} else {
				/* do vertices for center, and if still no normal found, use vertex normals */
				for(eve= em.verts.first; eve!=null; eve= eve.next) {
					if((eve.f & Blender.SELECT)!=0) {
						totsel++;
						calc_tw_center(scene, eve.co);
					}
				}
			}
		} /* end editmesh */
//		else if (obedit.type==OB_ARMATURE){
//			bArmature *arm= obedit.data;
//			EditBone *ebo;
//			for (ebo= arm.edbo.first; ebo; ebo=ebo.next){
//				if(ebo.layer & arm.layer) {
//					if (ebo.flag & BONE_TIPSEL) {
//						calc_tw_center(scene, ebo.tail);
//						totsel++;
//					}
//					if (ebo.flag & BONE_ROOTSEL) {
//						calc_tw_center(scene, ebo.head);
//						totsel++;
//					}
//					if (ebo.flag & BONE_SELECTED) {
//						stats_editbone(v3d, ebo);
//					}
//				}
//			}
//		}
//		else if ELEM(obedit.type, OB_CURVE, OB_SURF) {
//			Curve *cu= obedit.data;
//			Nurb *nu;
//			BezTriple *bezt;
//			BPoint *bp;
//
//			nu= cu.editnurb.first;
//			while(nu) {
//				if((nu.type & 7)==CU_BEZIER) {
//					bezt= nu.bezt;
//					a= nu.pntsu;
//					while(a--) {
//						/* exceptions
//						 * if handles are hidden then only check the center points.
//						 * If 2 or more are selected then only use the center point too.
//						 */
//						if (G.f & G_HIDDENHANDLES) {
//							if (bezt.f2 & SELECT) {
//								calc_tw_center(scene, bezt.vec[1]);
//								totsel++;
//							}
//						}
//						else if ( (bezt.f1 & SELECT) + (bezt.f2 & SELECT) + (bezt.f3 & SELECT) > SELECT ) {
//							calc_tw_center(scene, bezt.vec[1]);
//							totsel++;
//						}
//						else {
//							if(bezt.f1) {
//								calc_tw_center(scene, bezt.vec[0]);
//								totsel++;
//							}
//							if(bezt.f2) {
//								calc_tw_center(scene, bezt.vec[1]);
//								totsel++;
//							}
//							if(bezt.f3) {
//								calc_tw_center(scene, bezt.vec[2]);
//								totsel++;
//							}
//						}
//						bezt++;
//					}
//				}
//				else {
//					bp= nu.bp;
//					a= nu.pntsu*nu.pntsv;
//					while(a--) {
//						if(bp.f1 & SELECT) {
//							calc_tw_center(scene, bp.vec);
//							totsel++;
//						}
//						bp++;
//					}
//				}
//				nu= nu.next;
//			}
//		}
//		else if(obedit.type==OB_MBALL) {
//			/* editmball.c */
//			ListBase editelems= {NULL, NULL};  /* XXX */
//			MetaElem *ml, *ml_sel=NULL;
//
//			ml= editelems.first;
//			while(ml) {
//				if(ml.flag & SELECT) {
//					calc_tw_center(scene, &ml.x);
//					ml_sel = ml;
//					totsel++;
//				}
//				ml= ml.next;
//			}
//		}
//		else if(obedit.type==OB_LATTICE) {
//			BPoint *bp;
//			Lattice *lt= obedit.data;
//
//			bp= lt.editlatt.def;
//
//			a= lt.editlatt.pntsu*lt.editlatt.pntsv*lt.editlatt.pntsw;
//			while(a--) {
//				if(bp.f1 & SELECT) {
//					calc_tw_center(scene, bp.vec);
//					totsel++;
//				}
//				bp++;
//			}
//		}

		/* selection center */
		if(totsel!=0) {
			Arithb.VecMulf(scene.twcent, 1.0f/(float)totsel);	// centroid!
			Arithb.Mat4MulVecfl(obedit.obmat, scene.twcent);
			Arithb.Mat4MulVecfl(obedit.obmat, scene.twmin);
			Arithb.Mat4MulVecfl(obedit.obmat, scene.twmax);
		}
	}
//	else if(ob && (ob.flag & OB_POSEMODE)) {
//		bPoseChannel *pchan;
//		int mode = TFM_ROTATION; // mislead counting bones... bah. We don't know the manipulator mode, could be mixed
//
//		if((ob.lay & v3d.lay)==0) return 0;
//
//		totsel = count_set_pose_transflags(&mode, 0, ob);
//
//		if(totsel) {
//			/* use channels to get stats */
//			for(pchan= ob.pose.chanbase.first; pchan; pchan= pchan.next) {
//				stats_pose(scene, v3d, pchan);
//			}
//
//			VecMulf(scene.twcent, 1.0f/(float)totsel);	// centroid!
//			Mat4MulVecfl(ob.obmat, scene.twcent);
//			Mat4MulVecfl(ob.obmat, scene.twmin);
//			Mat4MulVecfl(ob.obmat, scene.twmax);
//		}
//	}
//	else if(G.f & (G_VERTEXPAINT + G_TEXTUREPAINT + G_WEIGHTPAINT + G_SCULPTMODE)) {
//		;
//	}
//	else if(G.f & G_PARTICLEEDIT) {
//		ParticleSystem *psys= PE_get_current(scene, ob);
//		ParticleData *pa = psys.particles;
//		ParticleEditKey *ek;
//		int k;
//
//		if(psys.edit) {
//			for(a=0; a<psys.totpart; a++,pa++) {
//				if(pa.flag & PARS_HIDE) continue;
//
//				for(k=0, ek=psys.edit.keys[a]; k<pa.totkey; k++, ek++) {
//					if(ek.flag & PEK_SELECT) {
//						calc_tw_center(scene, ek.world_co);
//						totsel++;
//					}
//				}
//			}
//
//			/* selection center */
//			if(totsel)
//				VecMulf(scene.twcent, 1.0f/(float)totsel);	// centroid!
//		}
//	}
	else {

		/* we need the one selected object, if its not active */
		ob= (scene.basact!=null? scene.basact.object: null);
		if(ob!=null && (ob.flag & Blender.SELECT)==0) ob= null;

		for(base= (Base)scene.base.first; base!=null; base= base.next) {
//			if TESTBASELIB(scene, base) {
                        if ((base.flag & Blender.SELECT)!=0 && (base.lay & scene.lay)!=0 && (base.object.id.lib==null) && ((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0)) {
				if(ob==null)
					ob= base.object;
				calc_tw_center(scene, base.object.obmat[3]);
				//v3d.twdrawflag = protectflag_to_drawflags(base.object.protectflag, v3d.twdrawflag);
				rv3d.twdrawflag = protectflag_to_drawflags(base.object.protectflag, rv3d.twdrawflag);
				totsel++;
			}
		}

		/* selection center */
		if(totsel!=0) {
			Arithb.VecMulf(scene.twcent, 1.0f/(float)totsel);	// centroid!
		}
	}

	/* global, local or normal orientation? */
	if(ob!=null && totsel!=0) {

		switch(v3d.twmode) {

		case View3dTypes.V3D_MANIP_NORMAL:
			if(obedit!=null || (ob.flag & ObjectTypes.OB_POSEMODE)!=0) {
				float[][] mat = new float[3][3];
				int type;

				type = TransformOrientations.getTransformOrientation(C, normal, plane, (v3d.around == View3dTypes.V3D_ACTIVE));

				switch (type)
				{
					case Transform.ORIENTATION_NORMAL:
						if (TransformOrientations.createSpaceNormalTangent(mat, normal, plane) == false)
						{
							type = Transform.ORIENTATION_NONE;
						}
						break;
					case Transform.ORIENTATION_VERT:
						if (TransformOrientations.createSpaceNormal(mat, normal) == false)
						{
							type = Transform.ORIENTATION_NONE;
						}
						break;
					case Transform.ORIENTATION_EDGE:
						if (TransformOrientations.createSpaceNormalTangent(mat, normal, plane) == false)
						{
							type = Transform.ORIENTATION_NONE;
						}
						break;
					case Transform.ORIENTATION_FACE:
						if (TransformOrientations.createSpaceNormalTangent(mat, normal, plane) == false)
						{
							type = Transform.ORIENTATION_NONE;
						}
						break;
				}

				if (type == Transform.ORIENTATION_NONE)
				{
					Arithb.Mat4One(rv3d.twmat);
				}
				else
				{
					Arithb.Mat4CpyMat3(rv3d.twmat, mat);
				}
				break;
			}
			/* no break we define 'normal' as 'local' in Object mode */
		case View3dTypes.V3D_MANIP_LOCAL:
			Arithb.Mat4CpyMat4(rv3d.twmat, ob.obmat);
			Arithb.Mat4Ortho(rv3d.twmat);
			break;

		case View3dTypes.V3D_MANIP_VIEW:
			{
				float[][] mat = new float[3][3];
				Arithb.Mat3CpyMat4(mat, rv3d.viewinv);
				Arithb.Mat3Ortho(mat);
				Arithb.Mat4CpyMat3(rv3d.twmat, mat);
			}
			break;
		default: /* V3D_MANIP_CUSTOM */
			// XXX 			applyTransformOrientation(C, t);
			break;
		}

	}

	return totsel;
}

/* ******************** DRAWING STUFFIES *********** */

static float screen_aligned(GL2 gl, RegionView3D rv3d, float[][] mat)
{
	float[] vec = new float[3];
        float size;

	UtilDefines.VECCOPY(vec, mat[0]);
	size= Arithb.Normalize(vec);

	gl.glTranslatef(mat[3][0], mat[3][1], mat[3][2]);

	/* sets view screen aligned */
	gl.glRotatef( -360.0f*Arithb.saacos(rv3d.viewquat[0])/(float)Arithb.M_PI, rv3d.viewquat[1], rv3d.viewquat[2], rv3d.viewquat[3]);

	return size;
}


/* radring = radius of donut rings
   radhole = radius hole
   start = starting segment (based on nrings)
   end   = end segment
   nsides = amount of points in ring
   nrigns = amount of rings
*/
static void partial_donut(GL2 gl, float radring, float radhole, int start, int end, int nsides, int nrings)
{
	float theta, phi, theta1;
	float cos_theta, sin_theta;
	float cos_theta1, sin_theta1;
	float ring_delta, side_delta;
	int i, j, docaps= 1;

	if(start==0 && end==nrings) docaps= 0;

	ring_delta= 2.0f*(float)Arithb.M_PI/(float)nrings;
	side_delta= 2.0f*(float)Arithb.M_PI/(float)nsides;

	theta= (float)Arithb.M_PI+0.5f*ring_delta;
	cos_theta= (float)StrictMath.cos(theta);
	sin_theta= (float)StrictMath.sin(theta);

	for(i= nrings - 1; i >= 0; i--) {
		theta1= theta + ring_delta;
		cos_theta1= (float)StrictMath.cos(theta1);
		sin_theta1= (float)StrictMath.sin(theta1);

		if(docaps!=0 && i==start) {	// cap
			gl.glBegin(GL2.GL_POLYGON);
			phi= 0.0f;
			for(j= nsides; j >= 0; j--) {
				float cos_phi, sin_phi, dist;

				phi += side_delta;
				cos_phi= (float)StrictMath.cos(phi);
				sin_phi= (float)StrictMath.sin(phi);
				dist= radhole + radring * cos_phi;

				gl.glVertex3f(cos_theta1 * dist, -sin_theta1 * dist,  radring * sin_phi);
			}
			gl.glEnd();
		}
		if(i>=start && i<=end) {
			gl.glBegin(GL2.GL_QUAD_STRIP);
			phi= 0.0f;
			for(j= nsides; j >= 0; j--) {
				float cos_phi, sin_phi, dist;

				phi += side_delta;
				cos_phi= (float)StrictMath.cos(phi);
				sin_phi= (float)StrictMath.sin(phi);
				dist= radhole + radring * cos_phi;

				gl.glVertex3f(cos_theta1 * dist, -sin_theta1 * dist, radring * sin_phi);
				gl.glVertex3f(cos_theta * dist, -sin_theta * dist,  radring * sin_phi);
			}
			gl.glEnd();
		}

		if(docaps!=0 && i==end) {	// cap
			gl.glBegin(GL2.GL_POLYGON);
			phi= 0.0f;
			for(j= nsides; j >= 0; j--) {
				float cos_phi, sin_phi, dist;

				phi -= side_delta;
				cos_phi= (float)StrictMath.cos(phi);
				sin_phi= (float)StrictMath.sin(phi);
				dist= radhole + radring * cos_phi;

				gl.glVertex3f(cos_theta * dist, -sin_theta * dist,  radring * sin_phi);
			}
			gl.glEnd();
		}


		theta= theta1;
		cos_theta= cos_theta1;
		sin_theta= sin_theta1;
	}
}

/* three colors can be set;
   grey for ghosting
   moving: in transform theme color
   else the red/green/blue
*/
static void manipulator_setcolor(GL2 gl, View3D v3d, char axis, int colcode)
{
	float[] vec = new float[4];
	byte[] col = new byte[4];

	vec[3]= 0.7f; // alpha set on 0.5, can be glEnabled or not

	if(colcode==MAN_GHOST) {
		gl.glColor4ub((byte)0, (byte)0, (byte)0, (byte)70);
	}
	else if(colcode==MAN_MOVECOL) {
		Resources.UI_GetThemeColor3ubv(Resources.TH_TRANSFORM, col);
		gl.glColor4ub(col[0], col[1], col[2], (byte)128);
	}
	else {
		switch(axis) {
		case 'c':
			Resources.UI_GetThemeColor3ubv(Resources.TH_TRANSFORM, col);
			if(v3d.twmode == View3dTypes.V3D_MANIP_LOCAL) {
				col[0]= (byte)((col[0]&0xFF)>200?255:(col[0]&0xFF)+55);
				col[1]= (byte)((col[1]&0xFF)>200?255:(col[1]&0xFF)+55);
				col[2]= (byte)((col[2]&0xFF)>200?255:(col[2]&0xFF)+55);
			}
			else if(v3d.twmode == View3dTypes.V3D_MANIP_NORMAL) {
				col[0]= (byte)((col[0]&0xFF)<55?0:(col[0]&0xFF)-55);
				col[1]= (byte)((col[1]&0xFF)<55?0:(col[1]&0xFF)-55);
				col[2]= (byte)((col[2]&0xFF)<55?0:(col[2]&0xFF)-55);
			}
			gl.glColor4ub(col[0], col[1], col[2], (byte)128);
			break;
		case 'x':
			gl.glColor4ub((byte)220, (byte)0, (byte)0, (byte)128);
			break;
		case 'y':
			gl.glColor4ub((byte)0, (byte)220, (byte)0, (byte)128);
			break;
		case 'z':
			gl.glColor4ub((byte)30, (byte)30, (byte)220, (byte)128);
			break;
		}
	}
}

/* viewmatrix should have been set OK, also no shademode! */
static void draw_manipulator_axes(GL2 gl, View3D v3d, int colcode, int flagx, int flagy, int flagz)
{

	/* axes */
	if(flagx!=0) {
		manipulator_setcolor(gl, v3d, 'x', colcode);
		if((flagx & MAN_SCALE_X)!=0) gl.glLoadName(MAN_SCALE_X);
		else if((flagx & MAN_TRANS_X)!=0) gl.glLoadName(MAN_TRANS_X);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.2f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glEnd();
	}
	if(flagy!=0) {
		if((flagy & MAN_SCALE_Y)!=0) gl.glLoadName(MAN_SCALE_Y);
		else if((flagy & MAN_TRANS_Y)!=0) gl.glLoadName(MAN_TRANS_Y);
		manipulator_setcolor(gl, v3d, 'y', colcode);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.0f, 0.2f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glEnd();
	}
	if(flagz!=0) {
		if((flagz & MAN_SCALE_Z)!=0) gl.glLoadName(MAN_SCALE_Z);
		else if((flagz & MAN_TRANS_Z)!=0) gl.glLoadName(MAN_TRANS_Z);
		manipulator_setcolor(gl, v3d, 'z', colcode);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.2f);
		gl.glVertex3f(0.0f, 0.0f, 1.0f);
		gl.glEnd();
	}
}

///* only called while G.moving */
//static void draw_manipulator_rotate_ghost(View3D *v3d, RegionView3D *rv3d, int drawflags)
//{
//	GLUquadricObj *qobj;
//	float size, phi, startphi, vec[3], svec[3], matt[4][4], cross[3], tmat[3][3];
//	int arcs= (G.rt!=2);
//
//	glDisable(GL_DEPTH_TEST);
//
//	qobj= gluNewQuadric();
//	gluQuadricDrawStyle(qobj, GLU_FILL);
//
//	glColor4ub(0,0,0,64);
//	glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
//	glEnable(GL_BLEND);
//
//	/* we need both [4][4] transforms, t.mat seems to be premul, not post for mat[][4] */
//	Mat4CpyMat4(matt, rv3d.twmat); // to copy the parts outside of [3][3]
//// XXX	Mat4MulMat34(matt, t.mat, rv3d.twmat);
//
//	/* Screen aligned view rot circle */
//	if(drawflags & MAN_ROT_V) {
//
//		/* prepare for screen aligned draw */
//		glPushMatrix();
//		size= screen_aligned(rv3d, rv3d.twmat);
//
//		vec[0]= 0; // XXX (float)(t.con.imval[0] - t.center2d[0]);
//		vec[1]= 0; // XXX (float)(t.con.imval[1] - t.center2d[1]);
//		vec[2]= 0.0f;
//		Normalize(vec);
//
//		startphi= saacos( vec[1] );
//		if(vec[0]<0.0) startphi= -startphi;
//
//		phi= 0; // XXX (float)fmod(180.0*t.val/M_PI, 360.0);
//		if(phi > 180.0) phi-= 360.0;
//		else if(phi<-180.0) phi+= 360.0;
//
//		gluPartialDisk(qobj, 0.0, size, 32, 1, 180.0*startphi/M_PI, phi);
//
//		glPopMatrix();
//	}
//	else if(arcs) {
//		float imat[3][3], ivmat[3][3];
//		/* try to get the start rotation */
//
//		svec[0]= 0; // XXX (float)(t.con.imval[0] - t.center2d[0]);
//		svec[1]= 0; // XXX (float)(t.con.imval[1] - t.center2d[1]);
//		svec[2]= 0.0f;
//
//		/* screen aligned vec transform back to manipulator space */
//		Mat3CpyMat4(ivmat, rv3d.viewinv);
//		Mat3CpyMat4(tmat, rv3d.twmat);
//		Mat3Inv(imat, tmat);
//		Mat3MulMat3(tmat, imat, ivmat);
//
//		Mat3MulVecfl(tmat, svec);	// tmat is used further on
//		Normalize(svec);
//	}
//
//	wmMultMatrix(rv3d.twmat);	// aligns with original widget
//
//	/* Z disk */
//	if(drawflags & MAN_ROT_Z) {
//		if(arcs) {
//			/* correct for squeezed arc */
//			svec[0]+= tmat[2][0];
//			svec[1]+= tmat[2][1];
//			Normalize(svec);
//
//			startphi= (float)atan2(svec[0], svec[1]);
//		}
//		else startphi= 0.5f*(float)M_PI;
//
//		VECCOPY(vec, rv3d.twmat[0]);	// use x axis to detect rotation
//		Normalize(vec);
//		Normalize(matt[0]);
//		phi= saacos( Inpf(vec, matt[0]) );
//		if(phi!=0.0) {
//			Crossf(cross, vec, matt[0]);	// results in z vector
//			if(Inpf(cross, rv3d.twmat[2]) > 0.0) phi= -phi;
//			gluPartialDisk(qobj, 0.0, 1.0, 32, 1, 180.0*startphi/M_PI, 180.0*(phi)/M_PI);
//		}
//	}
//	/* X disk */
//	if(drawflags & MAN_ROT_X) {
//		if(arcs) {
//			/* correct for squeezed arc */
//			svec[1]+= tmat[2][1];
//			svec[2]+= tmat[2][2];
//			Normalize(svec);
//
//			startphi= (float)(M_PI + atan2(svec[2], -svec[1]));
//		}
//		else startphi= 0.0f;
//
//		VECCOPY(vec, rv3d.twmat[1]);	// use y axis to detect rotation
//		Normalize(vec);
//		Normalize(matt[1]);
//		phi= saacos( Inpf(vec, matt[1]) );
//		if(phi!=0.0) {
//			Crossf(cross, vec, matt[1]);	// results in x vector
//			if(Inpf(cross, rv3d.twmat[0]) > 0.0) phi= -phi;
//			glRotatef(90.0, 0.0, 1.0, 0.0);
//			gluPartialDisk(qobj, 0.0, 1.0, 32, 1, 180.0*startphi/M_PI, 180.0*phi/M_PI);
//			glRotatef(-90.0, 0.0, 1.0, 0.0);
//		}
//	}
//	/* Y circle */
//	if(drawflags & MAN_ROT_Y) {
//		if(arcs) {
//			/* correct for squeezed arc */
//			svec[0]+= tmat[2][0];
//			svec[2]+= tmat[2][2];
//			Normalize(svec);
//
//			startphi= (float)(M_PI + atan2(-svec[0], svec[2]));
//		}
//		else startphi= (float)M_PI;
//
//		VECCOPY(vec, rv3d.twmat[2]);	// use z axis to detect rotation
//		Normalize(vec);
//		Normalize(matt[2]);
//		phi= saacos( Inpf(vec, matt[2]) );
//		if(phi!=0.0) {
//			Crossf(cross, vec, matt[2]);	// results in y vector
//			if(Inpf(cross, rv3d.twmat[1]) > 0.0) phi= -phi;
//			glRotatef(-90.0, 1.0, 0.0, 0.0);
//			gluPartialDisk(qobj, 0.0, 1.0, 32, 1, 180.0*startphi/M_PI, 180.0*phi/M_PI);
//			glRotatef(90.0, 1.0, 0.0, 0.0);
//		}
//	}
//
//	glDisable(GL_BLEND);
//	wmLoadMatrix(rv3d.viewmat);
//}

static void draw_manipulator_rotate(GL2 gl, View3D v3d, RegionView3D rv3d, boolean moving, int drawflags, int combo)
{
        GLU glu = new GLU();
	GLUquadric qobj;
	double[] plane = new double[4];
	float size;
        float[] vec = new float[3];
        float[][] unitmat = new float[4][4];
	float cywid= 0.33f*0.01f*(float)U.tw_handlesize;
	float cusize= cywid*0.65f;
	boolean arcs= (G.rt!=2);
	int colcode;

	if(moving) colcode= MAN_MOVECOL;
	else colcode= MAN_RGB;

	/* when called while moving in mixed mode, do not draw when... */
	if((drawflags & MAN_ROT_C)==0) return;

	/* Init stuff */
	gl.glDisable(GL2.GL_DEPTH_TEST);
	Arithb.Mat4One(unitmat);

	qobj= glu.gluNewQuadric();
	glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);

	/* prepare for screen aligned draw */
	UtilDefines.VECCOPY(vec, rv3d.twmat[0]);
	size= Arithb.Normalize(vec);
	gl.glPushMatrix();
	gl.glTranslatef(rv3d.twmat[3][0], rv3d.twmat[3][1], rv3d.twmat[3][2]);

	if(arcs) {
		/* clipplane makes nice handles, calc here because of multmatrix but with translate! */
		UtilDefines.VECCOPY(plane, rv3d.viewinv[2]);
		plane[3]= -0.02*size; // clip just a bit more
		gl.glClipPlane(GL2.GL_CLIP_PLANE0, plane,0);
	}
	/* sets view screen aligned */
	gl.glRotatef( -360.0f*Arithb.saacos(rv3d.viewquat[0])/(float)Arithb.M_PI, rv3d.viewquat[1], rv3d.viewquat[2], rv3d.viewquat[3]);

	/* Screen aligned help circle */
	if(arcs) {
		if((G.f & Global.G_PICKSEL)==0) {
			Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -30);
			DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], size, unitmat);
		}
	}
	/* Screen aligned view rot circle */
	if((drawflags & MAN_ROT_V)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_V);
		Resources.UI_ThemeColor(gl, Resources.TH_TRANSFORM);
		DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 1.2f*size, unitmat);

		if(moving) {
//			float[] vec = new float[3];
			vec[0]= 0; // XXX (float)(t.imval[0] - t.center2d[0]);
			vec[1]= 0; // XXX (float)(t.imval[1] - t.center2d[1]);
			vec[2]= 0.0f;
			Arithb.Normalize(vec);
			Arithb.VecMulf(vec, 1.2f*size);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3fv(vec,0);
			gl.glEnd();
		}
	}
	gl.glPopMatrix();

	/* apply the transform delta */
	if(moving) {
		float[][] matt = new float[4][4];
		Arithb.Mat4CpyMat4(matt, rv3d.twmat); // to copy the parts outside of [3][3]
		// XXX Mat4MulMat34(matt, t.mat, rv3d.twmat);
		WmSubWindow.wmMultMatrix(gl, matt);
		gl.glFrontFace( is_mat4_flipped(matt)?GL2.GL_CW:GL2.GL_CCW);
	}
	else {
		gl.glFrontFace( is_mat4_flipped(rv3d.twmat)?GL2.GL_CW:GL2.GL_CCW);
		WmSubWindow.wmMultMatrix(gl, rv3d.twmat);
	}

	/* axes */
	if(arcs==false) {
		if((G.f & Global.G_PICKSEL)==0) {
			if( (combo & View3dTypes.V3D_MANIP_SCALE)==0) {
				/* axis */
				gl.glBegin(GL2.GL_LINES);
				if( (drawflags & MAN_ROT_X)!=0 || (moving && (drawflags & MAN_ROT_Z)!=0) ) {
					manipulator_setcolor(gl, v3d, 'x', colcode);
					gl.glVertex3f(0.2f, 0.0f, 0.0f);
					gl.glVertex3f(1.0f, 0.0f, 0.0f);
				}
				if( (drawflags & MAN_ROT_Y)!=0 || (moving && (drawflags & MAN_ROT_X)!=0) ) {
					manipulator_setcolor(gl, v3d, 'y', colcode);
					gl.glVertex3f(0.0f, 0.2f, 0.0f);
					gl.glVertex3f(0.0f, 1.0f, 0.0f);
				}
				if( (drawflags & MAN_ROT_Z)!=0 || (moving && (drawflags & MAN_ROT_Y)!=0) ) {
					manipulator_setcolor(gl, v3d, 'z', colcode);
					gl.glVertex3f(0.0f, 0.0f, 0.2f);
					gl.glVertex3f(0.0f, 0.0f, 1.0f);
				}
				gl.glEnd();
			}
		}
	}

	if(arcs==false && moving) {

		/* Z circle */
		if((drawflags & MAN_ROT_Z)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Z);
			manipulator_setcolor(gl, v3d, 'z', colcode);
			DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 1.0f, unitmat);
		}
		/* X circle */
		if((drawflags & MAN_ROT_X)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_X);
			gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			manipulator_setcolor(gl, v3d, 'x', colcode);
			DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 1.0f, unitmat);
			gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
		}
		/* Y circle */
		if((drawflags & MAN_ROT_Y)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Y);
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			manipulator_setcolor(gl, v3d, 'y', colcode);
			DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 1.0f, unitmat);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		}

		if(arcs) gl.glDisable(GL2.GL_CLIP_PLANE0);
	}
	// donut arcs
	if(arcs) {
		gl.glEnable(GL2.GL_CLIP_PLANE0);

		/* Z circle */
		if((drawflags & MAN_ROT_Z)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Z);
			manipulator_setcolor(gl, v3d, 'z', colcode);
			partial_donut(gl, cusize/4.0f, 1.0f, 0, 48, 8, 48);
		}
		/* X circle */
		if((drawflags & MAN_ROT_X)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_X);
			gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			manipulator_setcolor(gl, v3d, 'x', colcode);
			partial_donut(gl, cusize/4.0f, 1.0f, 0, 48, 8, 48);
			gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
		}
		/* Y circle */
		if((drawflags & MAN_ROT_Y)!=0) {
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Y);
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			manipulator_setcolor(gl, v3d, 'y', colcode);
			partial_donut(gl, cusize/4.0f, 1.0f, 0, 48, 8, 48);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		}

		gl.glDisable(GL2.GL_CLIP_PLANE0);
	}

	if(arcs==false) {

		/* Z handle on X axis */
		if((drawflags & MAN_ROT_Z)!=0) {
			gl.glPushMatrix();
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Z);
			manipulator_setcolor(gl, v3d, 'z', colcode);

			partial_donut(gl, 0.7f*cusize, 1.0f, 31, 33, 8, 64);

			gl.glPopMatrix();
		}

		/* Y handle on X axis */
		if((drawflags & MAN_ROT_Y)!=0) {
			gl.glPushMatrix();
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Y);
			manipulator_setcolor(gl, v3d, 'y', colcode);

			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			partial_donut(gl, 0.7f*cusize, 1.0f, 31, 33, 8, 64);

			gl.glPopMatrix();
		}

		/* X handle on Z axis */
		if((drawflags & MAN_ROT_X)!=0) {
			gl.glPushMatrix();
			if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_X);
			manipulator_setcolor(gl, v3d, 'x', colcode);

			gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			partial_donut(gl, 0.7f*cusize, 1.0f, 31, 33, 8, 64);

			gl.glPopMatrix();
		}

	}

	/* restore */
	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);
	glu.gluDeleteQuadric(qobj);
	if(v3d.zbuf!=0) gl.glEnable(GL2.GL_DEPTH_TEST);

}

static float[][] cube = {
	{-1.0f, -1.0f, -1.0f},
	{-1.0f, -1.0f,  1.0f},
	{-1.0f,  1.0f,  1.0f},
	{-1.0f,  1.0f, -1.0f},
	{ 1.0f, -1.0f, -1.0f},
	{ 1.0f, -1.0f,  1.0f},
	{ 1.0f,  1.0f,  1.0f},
	{ 1.0f,  1.0f, -1.0f}
};
static void drawsolidcube(GL2 gl, float size)
{
//	static float cube[8][3] = {
//	{-1.0, -1.0, -1.0},
//	{-1.0, -1.0,  1.0},
//	{-1.0,  1.0,  1.0},
//	{-1.0,  1.0, -1.0},
//	{ 1.0, -1.0, -1.0},
//	{ 1.0, -1.0,  1.0},
//	{ 1.0,  1.0,  1.0},
//	{ 1.0,  1.0, -1.0},	};
	float[] n = new float[3];

	gl.glPushMatrix();
	gl.glScalef(size, size, size);

	n[0]=0; n[1]=0; n[2]=0;
	gl.glBegin(GL2.GL_QUADS);
	n[0]= -1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[0],0); gl.glVertex3fv(cube[1],0); gl.glVertex3fv(cube[2],0); gl.glVertex3fv(cube[3],0);
	n[0]=0;
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	n[1]= -1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[0],0); gl.glVertex3fv(cube[4],0); gl.glVertex3fv(cube[5],0); gl.glVertex3fv(cube[1],0);
	n[1]=0;
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	n[0]= 1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[4],0); gl.glVertex3fv(cube[7],0); gl.glVertex3fv(cube[6],0); gl.glVertex3fv(cube[5],0);
	n[0]=0;
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	n[1]= 1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[7],0); gl.glVertex3fv(cube[3],0); gl.glVertex3fv(cube[2],0); gl.glVertex3fv(cube[6],0);
	n[1]=0;
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	n[2]= 1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[1],0); gl.glVertex3fv(cube[5],0); gl.glVertex3fv(cube[6],0); gl.glVertex3fv(cube[2],0);
	n[2]=0;
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	n[2]= -1.0f;
	gl.glNormal3fv(n,0);
	gl.glVertex3fv(cube[7],0); gl.glVertex3fv(cube[4],0); gl.glVertex3fv(cube[0],0); gl.glVertex3fv(cube[3],0);
	gl.glEnd();

	gl.glPopMatrix();
}


static void draw_manipulator_scale(GL2 gl, View3D v3d, RegionView3D rv3d, boolean moving, int drawflags, int combo, int colcode)
{
	float cywid= 0.25f*0.01f*(float)U.tw_handlesize;
	float cusize= cywid*0.75f, dz;

	/* when called while moving in mixed mode, do not draw when... */
	if((drawflags & MAN_SCALE_C)==0) return;

	gl.glDisable(GL2.GL_DEPTH_TEST);

	/* not in combo mode */
	if( (combo & (View3dTypes.V3D_MANIP_TRANSLATE|View3dTypes.V3D_MANIP_ROTATE))==0) {
		float size;
                float[][] unitmat = new float[4][4];
		int shift= 0; // XXX

		/* center circle, do not add to selection when shift is pressed (planar constraint)  */
		if( (G.f & Global.G_PICKSEL)!=0 && shift==0) gl.glLoadName(MAN_SCALE_C);

		manipulator_setcolor(gl, v3d, 'c', colcode);
		gl.glPushMatrix();
		size= screen_aligned(gl, rv3d, rv3d.twmat);
		Arithb.Mat4One(unitmat);
		DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 0.2f*size, unitmat);
		gl.glPopMatrix();

		dz= 1.0f;
	}
	else dz= 1.0f-4.0f*cusize;

	if(moving) {
		float[][] matt = new float[4][4];

		Arithb.Mat4CpyMat4(matt, rv3d.twmat); // to copy the parts outside of [3][3]
		// XXX Mat4MulMat34(matt, t.mat, rv3d.twmat);
		WmSubWindow.wmMultMatrix(gl, matt);
		gl.glFrontFace( is_mat4_flipped(matt)?GL2.GL_CW:GL2.GL_CCW);
	}
	else {
		WmSubWindow.wmMultMatrix(gl, rv3d.twmat);
		gl.glFrontFace( is_mat4_flipped(rv3d.twmat)?GL2.GL_CW:GL2.GL_CCW);
	}

	/* axis */

	/* in combo mode, this is always drawn as first type */
	draw_manipulator_axes(gl, v3d, colcode, drawflags & MAN_SCALE_X, drawflags & MAN_SCALE_Y, drawflags & MAN_SCALE_Z);

	/* Z cube */
	gl.glTranslatef(0.0f, 0.0f, dz);
	if((drawflags & MAN_SCALE_Z)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_SCALE_Z);
		manipulator_setcolor(gl, v3d, 'z', colcode);
		drawsolidcube(gl, cusize);
	}
	/* X cube */
	gl.glTranslatef(dz, 0.0f, -dz);
	if((drawflags & MAN_SCALE_X)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_SCALE_X);
		manipulator_setcolor(gl, v3d, 'x', colcode);
		drawsolidcube(gl, cusize);
	}
	/* Y cube */
	gl.glTranslatef(-dz, dz, 0.0f);
	if((drawflags & MAN_SCALE_Y)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_SCALE_Y);
		manipulator_setcolor(gl, v3d, 'y', colcode);
		drawsolidcube(gl, cusize);
	}

	/* if shiftkey, center point as last, for selectbuffer order */
	if((G.f & Global.G_PICKSEL)!=0) {
		int shift= 0; // XXX

		if(shift!=0) {
			gl.glTranslatef(0.0f, -dz, 0.0f);
			gl.glLoadName(MAN_SCALE_C);
			gl.glBegin(GL2.GL_POINTS);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glEnd();
		}
	}

	/* restore */
	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

	if(v3d.zbuf!=0) gl.glEnable(GL2.GL_DEPTH_TEST);
	gl.glFrontFace(GL2.GL_CCW);
}


static void draw_cone(GL2 gl, GLU glu, GLUquadric qobj, float len, float width)
{
	gl.glTranslatef(0.0f, 0.0f, -0.5f*len);
	glu.gluCylinder(qobj, width, 0.0, len, 8, 1);
	glu.gluQuadricOrientation(qobj, GLU.GLU_INSIDE);
	glu.gluDisk(qobj, 0.0, width, 8, 1);
	glu.gluQuadricOrientation(qobj, GLU.GLU_OUTSIDE);
	gl.glTranslatef(0.0f, 0.0f, 0.5f*len);
}

static void draw_cylinder(GL2 gl, GLU glu, GLUquadric qobj, float len, float width)
{

	width*= 0.8f;	// just for beauty

	gl.glTranslatef(0.0f, 0.0f, -0.5f*len);
	glu.gluCylinder(qobj, width, width, len, 8, 1);
	glu.gluQuadricOrientation(qobj, GLU.GLU_INSIDE);
	glu.gluDisk(qobj, 0.0, width, 8, 1);
	glu.gluQuadricOrientation(qobj, GLU.GLU_OUTSIDE);
	gl.glTranslatef(0.0f, 0.0f, len);
	glu.gluDisk(qobj, 0.0, width, 8, 1);
	gl.glTranslatef(0.0f, 0.0f, -0.5f*len);
}


static void draw_manipulator_translate(GL2 gl, View3D v3d, RegionView3D rv3d, boolean moving, int drawflags, int combo, int colcode)
{
        GLU glu = new GLU();
	GLUquadric qobj;
	float cylen= 0.01f*(float)U.tw_handlesize;
	float cywid= 0.25f*cylen, dz, size;
	float[][] unitmat = new float[4][4];
	int shift= 0; // XXX

	/* when called while moving in mixed mode, do not draw when... */
	if((drawflags & MAN_TRANS_C)==0) return;

	// XXX if(moving) glTranslatef(t.vec[0], t.vec[1], t.vec[2]);
	gl.glDisable(GL2.GL_DEPTH_TEST);

	qobj= glu.gluNewQuadric();
	glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);

	/* center circle, do not add to selection when shift is pressed (planar constraint) */
	if((G.f & Global.G_PICKSEL)!=0 && shift==0) gl.glLoadName(MAN_TRANS_C);

	manipulator_setcolor(gl, v3d, 'c', colcode);
	gl.glPushMatrix();
	size= screen_aligned(gl, rv3d, rv3d.twmat);
	Arithb.Mat4One(unitmat);
	DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 0.2f*size, unitmat);
	gl.glPopMatrix();

	/* and now apply matrix, we move to local matrix drawing */
	WmSubWindow.wmMultMatrix(gl, rv3d.twmat);

	/* axis */
	gl.glLoadName(-1);

	// translate drawn as last, only axis when no combo with scale, or for ghosting
	if((combo & View3dTypes.V3D_MANIP_SCALE)==0 || colcode==MAN_GHOST)
		draw_manipulator_axes(gl, v3d, colcode, drawflags & MAN_TRANS_X, drawflags & MAN_TRANS_Y, drawflags & MAN_TRANS_Z);


	/* offset in combo mode, for rotate a bit more */
	if((combo & View3dTypes.V3D_MANIP_ROTATE)!=0) dz= 1.0f+2.0f*cylen;
	else if((combo & View3dTypes.V3D_MANIP_SCALE)!=0) dz= 1.0f+0.5f*cylen;
	else dz= 1.0f;

	/* Z Cone */
	gl.glTranslatef(0.0f, 0.0f, dz);
	if((drawflags & MAN_TRANS_Z)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_TRANS_Z);
		manipulator_setcolor(gl, v3d, 'z', colcode);
		draw_cone(gl, glu, qobj, cylen, cywid);
	}
	/* X Cone */
	gl.glTranslatef(dz, 0.0f, -dz);
	if((drawflags & MAN_TRANS_X)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_TRANS_X);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		manipulator_setcolor(gl, v3d, 'x', colcode);
		draw_cone(gl, glu, qobj, cylen, cywid);
		gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
	}
	/* Y Cone */
	gl.glTranslatef(-dz, dz, 0.0f);
	if((drawflags & MAN_TRANS_Y)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_TRANS_Y);
		gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		manipulator_setcolor(gl, v3d, 'y', colcode);
		draw_cone(gl, glu, qobj, cylen, cywid);
	}

	glu.gluDeleteQuadric(qobj);
	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

	if(v3d.zbuf!=0) gl.glEnable(GL2.GL_DEPTH_TEST);

}

static void draw_manipulator_rotate_cyl(GL2 gl, View3D v3d, RegionView3D rv3d, boolean moving, int drawflags, int combo, int colcode)
{
	GLUquadric qobj;
	float size;
	float cylen= 0.01f*(float)U.tw_handlesize;
	float cywid= 0.25f*cylen;

	/* when called while moving in mixed mode, do not draw when... */
	if((drawflags & MAN_ROT_C)==0) return;

	/* prepare for screen aligned draw */
	gl.glPushMatrix();
	size= screen_aligned(gl, rv3d, rv3d.twmat);

	gl.glDisable(GL2.GL_DEPTH_TEST);

        GLU glu = new GLU();

	qobj= glu.gluNewQuadric();

	/* Screen aligned view rot circle */
	if((drawflags & MAN_ROT_V)!=0) {
		float[][] unitmat = new float[4][4];
		Arithb.Mat4One(unitmat);

		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_V);
		Resources.UI_ThemeColor(gl, Resources.TH_TRANSFORM);
		DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, unitmat[3], 1.2f*size, unitmat);

		if(moving) {
			float[] vec = new float[3];
			vec[0]= 0; // XXX (float)(t.imval[0] - t.center2d[0]);
			vec[1]= 0; // XXX (float)(t.imval[1] - t.center2d[1]);
			vec[2]= 0.0f;
			Arithb.Normalize(vec);
			Arithb.VecMulf(vec, 1.2f*size);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3fv(vec,0);
			gl.glEnd();
		}
	}
	gl.glPopMatrix();

	/* apply the transform delta */
	if(moving) {
		float[][] matt = new float[4][4];
		Arithb.Mat4CpyMat4(matt, rv3d.twmat); // to copy the parts outside of [3][3]
		// XXX 		if (t.flag & T_USES_MANIPULATOR) {
		// XXX 			Mat4MulMat34(matt, t.mat, rv3d.twmat);
		// XXX }
		WmSubWindow.wmMultMatrix(gl, matt);
	}
	else {
		WmSubWindow.wmMultMatrix(gl, rv3d.twmat);
	}

	gl.glFrontFace( is_mat4_flipped(rv3d.twmat)?GL2.GL_CW:GL2.GL_CCW);

	/* axis */
	if( (G.f & Global.G_PICKSEL)==0 ) {

		// only draw axis when combo didn't draw scale axes
		if((combo & View3dTypes.V3D_MANIP_SCALE)==0)
			draw_manipulator_axes(gl, v3d, colcode, drawflags & MAN_ROT_X, drawflags & MAN_ROT_Y, drawflags & MAN_ROT_Z);

		/* only has to be set when not in picking */
		glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
	}

	/* Z cyl */
	gl.glTranslatef(0.0f, 0.0f, 1.0f);
	if((drawflags & MAN_ROT_Z)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Z);
		manipulator_setcolor(gl, v3d, 'z', colcode);
		draw_cylinder(gl, glu, qobj, cylen, cywid);
	}
	/* X cyl */
	gl.glTranslatef(1.0f, 0.0f, -1.0f);
	if((drawflags & MAN_ROT_X)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_X);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		manipulator_setcolor(gl, v3d, 'x', colcode);
		draw_cylinder(gl, glu, qobj, cylen, cywid);
		gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
	}
	/* Y cylinder */
	gl.glTranslatef(-1.0f, 1.0f, 0.0f);
	if((drawflags & MAN_ROT_Y)!=0) {
		if((G.f & Global.G_PICKSEL)!=0) gl.glLoadName(MAN_ROT_Y);
		gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		manipulator_setcolor(gl, v3d, 'y', colcode);
		draw_cylinder(gl, glu, qobj, cylen, cywid);
	}

	/* restore */

	glu.gluDeleteQuadric(qobj);
	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

	if(v3d.zbuf!=0) gl.glEnable(GL2.GL_DEPTH_TEST);
}


/* ********************************************* */

static float get_manipulator_drawsize(ARegion ar)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float size = TransformGenerics.get_drawsize(ar, rv3d.twmat[3]);

	size*= (float)U.tw_size;

	return size;
}


/* main call, does calc centers & orientation too */
/* uses global G.moving */
static int drawflags= 0xFFFF;		// only for the calls below, belongs in scene...?

public static void BIF_draw_manipulator(GL2 gl, bContext C)
{
//        System.out.println("BIF_draw_manipulator");
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	Scene scene= bContext.CTX_data_scene(C);
	View3D v3d= (View3D)sa.spacedata.first;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	int totsel;

	if((v3d.twflag & View3dTypes.V3D_USE_MANIPULATOR)==0) return;
	if(G.moving!=0 && (G.moving & Global.G_TRANSFORM_MANIP)==0) return;

	if(G.moving==0) {
		v3d.twflag &= ~View3dTypes.V3D_DRAW_MANIPULATOR;

		totsel= calc_manipulator_stats(C);
		if(totsel==0) return;
		//drawflags= v3d.twdrawflag;	/* set in calc_manipulator_stats */
		drawflags= rv3d.twdrawflag;	/* set in calc_manipulator_stats */

		v3d.twflag |= View3dTypes.V3D_DRAW_MANIPULATOR;

		/* now we can define center */
		switch(v3d.around) {
		case View3dTypes.V3D_CENTER:
		case View3dTypes.V3D_ACTIVE:
			rv3d.twmat[3][0]= (scene.twmin[0] + scene.twmax[0])/2.0f;
			rv3d.twmat[3][1]= (scene.twmin[1] + scene.twmax[1])/2.0f;
			rv3d.twmat[3][2]= (scene.twmin[2] + scene.twmax[2])/2.0f;
			if(v3d.around==View3dTypes.V3D_ACTIVE && scene.obedit==null) {
				bObject ob= (scene.basact!=null? scene.basact.object: null);
				if(ob!=null && (ob.flag & ObjectTypes.OB_POSEMODE)==0)
					UtilDefines.VECCOPY(rv3d.twmat[3], ob.obmat[3]);
			}
			break;
		case View3dTypes.V3D_LOCAL:
		case View3dTypes.V3D_CENTROID:
			UtilDefines.VECCOPY(rv3d.twmat[3], scene.twcent);
			break;
		case View3dTypes.V3D_CURSOR:
			UtilDefines.VECCOPY(rv3d.twmat[3], View3dView.give_cursor(scene, v3d));
			break;
		}

		Arithb.Mat4MulFloat3(rv3d.twmat, get_manipulator_drawsize(ar));
	}

	if((v3d.twflag & View3dTypes.V3D_DRAW_MANIPULATOR)!=0) {

		if((v3d.twtype & View3dTypes.V3D_MANIP_ROTATE)!=0) {

//			/* rotate has special ghosting draw, for pie chart */
//			if(G.moving) draw_manipulator_rotate_ghost(v3d, rv3d, drawflags);
//
//			if(G.moving) glEnable(GL_BLEND);

			if(G.rt==3) {
//				if(G.moving) draw_manipulator_rotate_cyl(v3d, rv3d, 1, drawflags, v3d.twtype, MAN_MOVECOL);
//				else draw_manipulator_rotate_cyl(v3d, rv3d, 0, drawflags, v3d.twtype, MAN_RGB);
			}
			else
				draw_manipulator_rotate(gl, v3d, rv3d, G.moving!=0, drawflags, v3d.twtype);

			gl.glDisable(GL2.GL_BLEND);
		}
		if((v3d.twtype & View3dTypes.V3D_MANIP_SCALE)!=0) {
			if(G.moving!=0) {
				gl.glEnable(GL2.GL_BLEND);
				draw_manipulator_scale(gl, v3d, rv3d, false, drawflags, v3d.twtype, MAN_GHOST);
				draw_manipulator_scale(gl, v3d, rv3d, true, drawflags, v3d.twtype, MAN_MOVECOL);
				gl.glDisable(GL2.GL_BLEND);
			}
			else draw_manipulator_scale(gl, v3d, rv3d, false, drawflags, v3d.twtype, MAN_RGB);
		}
		if((v3d.twtype & View3dTypes.V3D_MANIP_TRANSLATE)!=0) {
			if(G.moving!=0) {
				gl.glEnable(GL2.GL_BLEND);
				draw_manipulator_translate(gl, v3d, rv3d, false, drawflags, v3d.twtype, MAN_GHOST);
				draw_manipulator_translate(gl, v3d, rv3d, true, drawflags, v3d.twtype, MAN_MOVECOL);
				gl.glDisable(GL2.GL_BLEND);
			}
			else draw_manipulator_translate(gl, v3d, rv3d, false, drawflags, v3d.twtype, MAN_RGB);
		}
	}
}

static int manipulator_selectbuf(ScrArea sa, ARegion ar, int[] mval, float hotspot)
{
	View3D v3d= (View3D)sa.spacedata.first;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	rctf rect = new rctf();
	//IntBuffer buffer = BufferUtil.newIntBuffer(64);		// max 4 items per select, so large enuf
	IntBuffer buffer = Buffers.newDirectIntBuffer(64);		// max 4 items per select, so large enuf
	short hits;
//	extern void setwinmatrixview3d(ARegion *ar, View3D *v3d, rctf *rect); // XXX check a bit later on this... (ton)

	G.f |= Global.G_PICKSEL;

	rect.xmin= mval[0]-hotspot;
	rect.xmax= mval[0]+hotspot;
	rect.ymin= mval[1]-hotspot;
	rect.ymax= mval[1]+hotspot;

        GL2 gl = (GL2)GLU.getCurrentGL();

	View3dView.setwinmatrixview3d(gl, ar, v3d, rect);
	Arithb.Mat4MulMat4(rv3d.persmat, rv3d.viewmat, rv3d.winmat);

	gl.glSelectBuffer( 64, buffer);
	gl.glRenderMode(GL2.GL_SELECT);
	gl.glInitNames();	/* these two calls whatfor? It doesnt work otherwise */
	gl.glPushName(-2);

	/* do the drawing */
	if((v3d.twtype & View3dTypes.V3D_MANIP_ROTATE)!=0) {
//		if(G.rt==3) draw_manipulator_rotate_cyl(gl, v3d, rv3d, false, MAN_ROT_C & v3d.twdrawflag, v3d.twtype, MAN_RGB);
//		else draw_manipulator_rotate(gl, v3d, rv3d, false, MAN_ROT_C & v3d.twdrawflag, v3d.twtype);
		if(G.rt==3) draw_manipulator_rotate_cyl(gl, v3d, rv3d, false, MAN_ROT_C & rv3d.twdrawflag, v3d.twtype, MAN_RGB);
		else draw_manipulator_rotate(gl, v3d, rv3d, false, MAN_ROT_C & rv3d.twdrawflag, v3d.twtype);
	}
	if((v3d.twtype & View3dTypes.V3D_MANIP_SCALE)!=0)
		//draw_manipulator_scale(gl, v3d, rv3d, false, MAN_SCALE_C & v3d.twdrawflag, v3d.twtype, MAN_RGB);
		draw_manipulator_scale(gl, v3d, rv3d, false, MAN_SCALE_C & rv3d.twdrawflag, v3d.twtype, MAN_RGB);
	if((v3d.twtype & View3dTypes.V3D_MANIP_TRANSLATE)!=0)
		//draw_manipulator_translate(gl, v3d, rv3d, false, MAN_TRANS_C & v3d.twdrawflag, v3d.twtype, MAN_RGB);
		draw_manipulator_translate(gl, v3d, rv3d, false, MAN_TRANS_C & rv3d.twdrawflag, v3d.twtype, MAN_RGB);

	gl.glPopName();
	hits= (short)gl.glRenderMode(GL2.GL_RENDER);

	G.f &= ~Global.G_PICKSEL;
	View3dView.setwinmatrixview3d(gl, ar, v3d, null);
	Arithb.Mat4MulMat4(rv3d.persmat, rv3d.viewmat, rv3d.winmat);

	if(hits==1) return buffer.get(3);
	else if(hits>1) {
		int val, dep, mindep=0, mindeprot=0, minval=0, minvalrot=0;
		int a;

		/* we compare the hits in buffer, but value centers highest */
		/* we also store the rotation hits separate (because of arcs) and return hits on other widgets if there are */

		for(a=0; a<hits; a++) {
			dep= buffer.get(4*a + 1);
			val= buffer.get(4*a + 3);

			if(val==MAN_TRANS_C) return MAN_TRANS_C;
			else if(val==MAN_SCALE_C) return MAN_SCALE_C;
			else {
				if((val & MAN_ROT_C)!=0) {
					if(minvalrot==0 || dep<mindeprot) {
						mindeprot= dep;
						minvalrot= val;
					}
				}
				else {
					if(minval==0 || dep<mindep) {
						mindep= dep;
						minval= val;
					}
				}
			}
		}

		if(minval!=0)
			return minval;
		else
			return minvalrot;
	}
	return 0;
}

/* return 0; nothing happened */
public static int BIF_do_manipulator(bContext C, wmEvent event, wmOperator op)
{
//        System.out.println("BIF_do_manipulator");
	ScrArea sa= bContext.CTX_wm_area(C);
	View3D v3d= (View3D)sa.spacedata.first;
	ARegion ar= bContext.CTX_wm_region(C);
	boolean[] constraint_axis = {false, false, false};
	int val;
	int shift = event.shift;

	if((v3d.twflag & View3dTypes.V3D_USE_MANIPULATOR)==0) return 0;
	if((v3d.twflag & View3dTypes.V3D_DRAW_MANIPULATOR)==0) return 0;

	// find the hotspots first test narrow hotspot
	val= manipulator_selectbuf(sa, ar, event.mval, 0.5f*(float)U.tw_hotspot);
	if(val!=0) {

		// drawflags still global, for drawing call above
		drawflags= manipulator_selectbuf(sa, ar, event.mval, 0.2f*(float)U.tw_hotspot);
		if(drawflags==0) drawflags= val;

		if ((drawflags & MAN_TRANS_C)!=0) {
			switch(drawflags) {
			case MAN_TRANS_C:
				break;
			case MAN_TRANS_X:
				if(shift!=0) {
					constraint_axis[1] = true;
					constraint_axis[2] = true;
				}
				else
					constraint_axis[0] = true;
				break;
			case MAN_TRANS_Y:
				if(shift!=0) {
					constraint_axis[0] = true;
					constraint_axis[2] = true;
				}
				else
					constraint_axis[1] = true;
				break;
			case MAN_TRANS_Z:
				if(shift!=0) {
					constraint_axis[0] = true;
					constraint_axis[1] = true;
				}
				else
					constraint_axis[2] = true;
				break;
			}
			RnaAccess.RNA_boolean_set_array(op.ptr, "constraint_axis", constraint_axis);
			WmEventSystem.WM_operator_name_call(C, "TFM_OT_translate", WmTypes.WM_OP_INVOKE_REGION_WIN, op.ptr);
		}
		else if ((drawflags & MAN_SCALE_C)!=0) {
			switch(drawflags) {
			case MAN_SCALE_X:
				if(shift!=0) {
					constraint_axis[1] = true;
					constraint_axis[2] = true;
				}
				else
					constraint_axis[0] = true;
				break;
			case MAN_SCALE_Y:
				if(shift!=0) {
					constraint_axis[0] = true;
					constraint_axis[2] = true;
				}
				else
					constraint_axis[1] = true;
				break;
			case MAN_SCALE_Z:
				if(shift!=0) {
					constraint_axis[0] = true;
					constraint_axis[1] = true;
				}
				else
					constraint_axis[2] = true;
				break;
			}
			RnaAccess.RNA_boolean_set_array(op.ptr, "constraint_axis", constraint_axis);
			WmEventSystem.WM_operator_name_call(C, "TFM_OT_resize", WmTypes.WM_OP_INVOKE_REGION_WIN, op.ptr);
		}
		else if (drawflags == MAN_ROT_T) { /* trackball need special case, init is different */
			WmEventSystem.WM_operator_name_call(C, "TFM_OT_trackball", WmTypes.WM_OP_INVOKE_REGION_WIN, op.ptr);
		}
		else if ((drawflags & MAN_ROT_C)!=0) {
			switch(drawflags) {
			case MAN_ROT_X:
				constraint_axis[0] = true;
				break;
			case MAN_ROT_Y:
				constraint_axis[1] = true;
				break;
			case MAN_ROT_Z:
				constraint_axis[2] = true;
				break;
			}
			RnaAccess.RNA_boolean_set_array(op.ptr, "constraint_axis", constraint_axis);
			WmEventSystem.WM_operator_name_call(C, "TFM_OT_rotate", WmTypes.WM_OP_INVOKE_REGION_WIN, op.ptr);
		}
	}
	/* after transform, restore drawflags */
	drawflags= 0xFFFF;

	return val;
}

}

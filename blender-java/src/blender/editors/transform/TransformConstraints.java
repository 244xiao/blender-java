/**
 * $Id: TransformConstraints.java,v 1.1 2009/09/18 05:15:10 jladere Exp $
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
 * The Original Code is Copyright (C) 2001-2002 by NaN Holding BV.
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

import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.blenlib.StringUtil;
import blender.editors.screen.GlUtil;
import blender.editors.space_view3d.DrawObject;
import blender.editors.transform.Transform.TransCon;
import blender.editors.transform.Transform.TransCon.ApplyRot;
import blender.editors.transform.Transform.TransCon.ApplySize;
import blender.editors.transform.Transform.TransCon.ApplyVec;
import blender.editors.transform.Transform.TransData;
import blender.editors.transform.Transform.TransInfo;
import blender.editors.uinterface.Resources;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.RegionView3D;
import java.util.Arrays;
import javax.media.opengl.GL2;

//#include <stdio.h>
//#include <string.h>
//#include <math.h>
//
//#ifdef HAVE_CONFIG_H
//#include <config.h>
//#endif
//
//#ifndef WIN32
//#include <unistd.h>
//#else
//#include <io.h>
//#endif
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_action_types.h"
//#include "DNA_armature_types.h"
//#include "DNA_camera_types.h"
//#include "DNA_curve_types.h"
//#include "DNA_effect_types.h"
//#include "DNA_image_types.h"
//#include "DNA_ipo_types.h"
//#include "DNA_key_types.h"
//#include "DNA_lamp_types.h"
//#include "DNA_lattice_types.h"
//#include "DNA_mesh_types.h"
//#include "DNA_meshdata_types.h"
//#include "DNA_meta_types.h"
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_space_types.h"
//#include "DNA_view3d_types.h"
//
//#include "BIF_gl.h"
//#include "BIF_glutil.h"
//
//#include "BKE_context.h"
//#include "BKE_global.h"
//#include "BKE_utildefines.h"
//
//#include "ED_image.h"
//#include "ED_view3d.h"
//
//#include "BLI_arithb.h"
//
////#include "blendef.h"
////
////#include "mydevice.h"
//
//#include "WM_types.h"
//#include "UI_resources.h"
//
//
//#include "transform.h"

public class TransformConstraints {

/* ************************** CONSTRAINTS ************************* */
public static void constraintAutoValues(TransInfo t, float[] vec)
{
	int mode = t.con.mode;
	if ((mode & Transform.CON_APPLY)!=0)
	{
		float nval = (t.flag & Transform.T_NULL_ONE)!=0?1.0f:0.0f;

		if ((mode & Transform.CON_AXIS0) == 0)
		{
			vec[0] = nval;
		}
		if ((mode & Transform.CON_AXIS1) == 0)
		{
			vec[1] = nval;
		}
		if ((mode & Transform.CON_AXIS2) == 0)
		{
			vec[2] = nval;
		}
	}
}

//void constraintNumInput(TransInfo *t, float vec[3])
//{
//	int mode = t.con.mode;
//	if (mode & CON_APPLY) {
//		float nval = (t.flag & T_NULL_ONE)?1.0f:0.0f;
//
//		if (getConstraintSpaceDimension(t) == 2) {
//			int axis = mode & (CON_AXIS0|CON_AXIS1|CON_AXIS2);
//			if (axis == (CON_AXIS0|CON_AXIS1)) {
//				vec[0] = vec[0];
//				vec[1] = vec[1];
//				vec[2] = nval;
//			}
//			else if (axis == (CON_AXIS1|CON_AXIS2)) {
//				vec[2] = vec[1];
//				vec[1] = vec[0];
//				vec[0] = nval;
//			}
//			else if (axis == (CON_AXIS0|CON_AXIS2)) {
//				vec[0] = vec[0];
//				vec[2] = vec[1];
//				vec[1] = nval;
//			}
//		}
//		else if (getConstraintSpaceDimension(t) == 1) {
//			if (mode & CON_AXIS0) {
//				vec[0] = vec[0];
//				vec[1] = nval;
//				vec[2] = nval;
//			}
//			else if (mode & CON_AXIS1) {
//				vec[1] = vec[0];
//				vec[0] = nval;
//				vec[2] = nval;
//			}
//			else if (mode & CON_AXIS2) {
//				vec[2] = vec[0];
//				vec[0] = nval;
//				vec[1] = nval;
//			}
//		}
//	}
//}

static void postConstraintChecks(TransInfo t, float[] vec, float[] pvec) {
	int i = 0;

	Arithb.Mat3MulVecfl(t.con.imtx, vec);

//	snapGrid(t, vec);

	if ((t.num.flag & Transform.T_NULL_ONE)!=0) {
		if ((t.con.mode & Transform.CON_AXIS0)==0)
			vec[0] = 1.0f;

		if ((t.con.mode & Transform.CON_AXIS1)==0)
			vec[1] = 1.0f;

		if ((t.con.mode & Transform.CON_AXIS2)==0)
			vec[2] = 1.0f;
	}

//	if (hasNumInput(&t.num)) {
//		applyNumInput(&t.num, vec);
//		constraintNumInput(t, vec);
//	}

	/* autovalues is operator param, use that directly but not if snapping is forced */
	if ((t.flag & Transform.T_AUTOVALUES)!=0 && (t.tsnap.status & Transform.SNAP_FORCED) == 0)
	{
		UtilDefines.VECCOPY(vec, t.auto_values);
		constraintAutoValues(t, vec);
	}

	if ((t.con.mode & Transform.CON_AXIS0)!=0) {
		pvec[i++] = vec[0];
	}
	if ((t.con.mode & Transform.CON_AXIS1)!=0) {
		pvec[i++] = vec[1];
	}
	if ((t.con.mode & Transform.CON_AXIS2)!=0) {
		pvec[i++] = vec[2];
	}

	Arithb.Mat3MulVecfl(t.con.mtx, vec);
}

static void axisProjection(TransInfo t, float[] axis, float[] in, float[] out) {
	float[] norm = new float[3], vec = new float[3];
        float factor;

	if(in[0]==0.0f && in[1]==0.0f && in[2]==0.0f)
		return;

	/* For when view is parallel to constraint... will cause NaNs otherwise
	   So we take vertical motion in 3D space and apply it to the
	   constraint axis. Nice for camera grab + MMB */
	if(1.0f - (float)StrictMath.abs(Arithb.Inpf(axis, t.viewinv[2])) < 0.000001f) {
		Arithb.Projf(vec, in, t.viewinv[1]);
		factor = Arithb.Inpf(t.viewinv[1], vec) * 2.0f;
		/* since camera distance is quite relative, use quadratic relationship. holding shift can compensate */
		if(factor<0.0f) factor*= -factor;
		else factor*= factor;

		UtilDefines.VECCOPY(out, axis);
		Arithb.Normalize(out);
		Arithb.VecMulf(out, -factor);	/* -factor makes move down going backwards */
	}
	else {
		float[] cb = new float[3], ab = new float[3];

		UtilDefines.VECCOPY(out, axis);

		/* Get view vector on axis to define a plane */
		Arithb.VecAddf(vec, t.con.center, in);
		TransformGenerics.getViewVector(t, vec, norm);

		Arithb.Crossf(vec, norm, axis);

		/* Project input vector on the plane passing on axis */
		Arithb.Projf(vec, in, vec);
		Arithb.VecSubf(vec, in, vec);

		/* intersect the two lines: axis and norm */
		Arithb.Crossf(cb, vec, norm);
		Arithb.Crossf(ab, axis, norm);

		Arithb.VecMulf(out, Arithb.Inpf(cb, ab) / Arithb.Inpf(ab, ab));
	}
}

static void planeProjection(TransInfo t, float[] in, float[] out) {
	float[] vec = new float[3], norm = new float[3];
        float factor;

	Arithb.VecAddf(vec, in, t.con.center);
	TransformGenerics.getViewVector(t, vec, norm);

	Arithb.VecSubf(vec, out, in);

	factor = Arithb.Inpf(vec, norm);
	if ((float)StrictMath.abs(factor) <= 0.001f) {
		return; /* prevent divide by zero */
	}
	factor = Arithb.Inpf(vec, vec) / factor;

	UtilDefines.VECCOPY(vec, norm);
	Arithb.VecMulf(vec, factor);

	Arithb.VecAddf(out, in, vec);
}

/*
 * Generic callback for constant spacial constraints applied to linear motion
 *
 * The IN vector in projected into the constrained space and then further
 * projected along the view vector.
 * (in perspective mode, the view vector is relative to the position on screen)
 *
 */

public static ApplyVec applyAxisConstraintVec = new ApplyVec() {
public void run(TransInfo t, TransData td, float[] in, float[] out, float[] pvec)
//static void applyAxisConstraintVec(TransInfo *t, TransData *td, float in[3], float out[3], float pvec[3])
{
//        System.out.println("applyAxisConstraintVec: "+Arrays.toString(in));
	UtilDefines.VECCOPY(out, in);
	if (td==null && (t.con.mode & Transform.CON_APPLY)!=0) {
		Arithb.Mat3MulVecfl(t.con.pmtx, out);

		// With snap, a projection is alright, no need to correct for view alignment
		if ((t.tsnap.status & Transform.SNAP_ON) == 0) {
			if (getConstraintSpaceDimension(t) == 2) {
				if (out[0] != 0.0f || out[1] != 0.0f || out[2] != 0.0f) {
					planeProjection(t, in, out);
				}
			}
			else if (getConstraintSpaceDimension(t) == 1) {
				float[] c = new float[3];

				if ((t.con.mode & Transform.CON_AXIS0)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[0]);
				}
				else if ((t.con.mode & Transform.CON_AXIS1)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[1]);
				}
				else if ((t.con.mode & Transform.CON_AXIS2)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[2]);
				}
				axisProjection(t, c, in, out);
			}
		}
		postConstraintChecks(t, out, pvec);
	}
}};

/*
 * Generic callback for object based spacial constraints applied to linear motion
 *
 * At first, the following is applied to the first data in the array
 * The IN vector in projected into the constrained space and then further
 * projected along the view vector.
 * (in perspective mode, the view vector is relative to the position on screen)
 *
 * Further down, that vector is mapped to each data's space.
 */

public static ApplyVec applyObjectConstraintVec = new ApplyVec() {
public void run(TransInfo t, TransData td, float[] in, float[] out, float[] pvec)
//static void applyObjectConstraintVec(TransInfo *t, TransData *td, float in[3], float out[3], float pvec[3])
{
        System.out.println("applyObjectConstraintVec: "+Arrays.toString(in));
	UtilDefines.VECCOPY(out, in);
	if ((t.con.mode & Transform.CON_APPLY)!=0) {
		if (td==null) {
			Arithb.Mat3MulVecfl(t.con.pmtx, out);
			if (getConstraintSpaceDimension(t) == 2) {
				if (out[0] != 0.0f || out[1] != 0.0f || out[2] != 0.0f) {
					planeProjection(t, in, out);
				}
			}
			else if (getConstraintSpaceDimension(t) == 1) {
				float[] c = new float[3];

				if ((t.con.mode & Transform.CON_AXIS0)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[0]);
				}
				else if ((t.con.mode & Transform.CON_AXIS1)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[1]);
				}
				else if ((t.con.mode & Transform.CON_AXIS2)!=0) {
					UtilDefines.VECCOPY(c, t.con.mtx[2]);
				}
				axisProjection(t, c, in, out);
			}
			postConstraintChecks(t, out, pvec);
			UtilDefines.VECCOPY(out, pvec);
		}
		else {
			int i=0;

			out[0] = out[1] = out[2] = 0.0f;
			if ((t.con.mode & Transform.CON_AXIS0)!=0) {
				out[0] = in[i++];
			}
			if ((t.con.mode & Transform.CON_AXIS1)!=0) {
				out[1] = in[i++];
			}
			if ((t.con.mode & Transform.CON_AXIS2)!=0) {
				out[2] = in[i++];
			}
			Arithb.Mat3MulVecfl(td.axismtx, out);
		}
	}
}};

/*
 * Generic callback for constant spacial constraints applied to resize motion
 *
 *
 */

public static ApplySize applyAxisConstraintSize = new ApplySize() {
public void run(TransInfo t, TransData td, float[][] smat)
//static void applyAxisConstraintSize(TransInfo *t, TransData *td, float smat[3][3])
{
	if (td==null && (t.con.mode & Transform.CON_APPLY)!=0) {
		float[][] tmat = new float[3][3];

		if ((t.con.mode & Transform.CON_AXIS0)==0) {
			smat[0][0] = 1.0f;
		}
		if ((t.con.mode & Transform.CON_AXIS1)==0) {
			smat[1][1] = 1.0f;
		}
		if ((t.con.mode & Transform.CON_AXIS2)==0) {
			smat[2][2] = 1.0f;
		}

		Arithb.Mat3MulMat3(tmat, smat, t.con.imtx);
		Arithb.Mat3MulMat3(smat, t.con.mtx, tmat);
	}
}};

/*
 * Callback for object based spacial constraints applied to resize motion
 *
 *
 */

public static ApplySize applyObjectConstraintSize = new ApplySize() {
public void run(TransInfo t, TransData td, float[][] smat)
//static void applyObjectConstraintSize(TransInfo *t, TransData *td, float smat[3][3])
{
	if (td!=null && (t.con.mode & Transform.CON_APPLY)!=0) {
		float[][] tmat = new float[3][3];
		float[][] imat = new float[3][3];

		Arithb.Mat3Inv(imat, td.axismtx);

		if ((t.con.mode & Transform.CON_AXIS0)==0) {
			smat[0][0] = 1.0f;
		}
		if ((t.con.mode & Transform.CON_AXIS1)==0) {
			smat[1][1] = 1.0f;
		}
		if ((t.con.mode & Transform.CON_AXIS2)==0) {
			smat[2][2] = 1.0f;
		}

		Arithb.Mat3MulMat3(tmat, smat, imat);
		Arithb.Mat3MulMat3(smat, td.axismtx, tmat);
	}
}};

/*
 * Generic callback for constant spacial constraints applied to rotations
 *
 * The rotation axis is copied into VEC.
 *
 * In the case of single axis constraints, the rotation axis is directly the one constrained to.
 * For planar constraints (2 axis), the rotation axis is the normal of the plane.
 *
 * The following only applies when CON_NOFLIP is not set.
 * The vector is then modified to always point away from the screen (in global space)
 * This insures that the rotation is always logically following the mouse.
 * (ie: not doing counterclockwise rotations when the mouse moves clockwise).
 */

public static ApplyRot applyAxisConstraintRot = new ApplyRot() {
public void run(TransInfo t, TransData td, float[] vec, float[] angle)
//static void applyAxisConstraintRot(TransInfo *t, TransData *td, float vec[3], float *angle)
{
	if (td==null && (t.con.mode & Transform.CON_APPLY)!=0) {
		int mode = t.con.mode & (Transform.CON_AXIS0|Transform.CON_AXIS1|Transform.CON_AXIS2);

		switch(mode) {
		case Transform.CON_AXIS0:
		case (Transform.CON_AXIS1|Transform.CON_AXIS2):
			UtilDefines.VECCOPY(vec, t.con.mtx[0]);
			break;
		case Transform.CON_AXIS1:
		case (Transform.CON_AXIS0|Transform.CON_AXIS2):
			UtilDefines.VECCOPY(vec, t.con.mtx[1]);
			break;
		case Transform.CON_AXIS2:
		case (Transform.CON_AXIS0|Transform.CON_AXIS1):
			UtilDefines.VECCOPY(vec, t.con.mtx[2]);
			break;
		}
		/* don't flip axis if asked to or if num input */
		if (angle!=null && (mode & Transform.CON_NOFLIP) == 0 && TransformNumInput.hasNumInput(t.num) == false) {
			if (Arithb.Inpf(vec, t.viewinv[2]) > 0.0f) {
				angle[0] = -(angle[0]);
			}
		}
	}
}};

/*
 * Callback for object based spacial constraints applied to rotations
 *
 * The rotation axis is copied into VEC.
 *
 * In the case of single axis constraints, the rotation axis is directly the one constrained to.
 * For planar constraints (2 axis), the rotation axis is the normal of the plane.
 *
 * The following only applies when CON_NOFLIP is not set.
 * The vector is then modified to always point away from the screen (in global space)
 * This insures that the rotation is always logically following the mouse.
 * (ie: not doing counterclockwise rotations when the mouse moves clockwise).
 */

public static ApplyRot applyObjectConstraintRot = new ApplyRot() {
public void run(TransInfo t, TransData td, float[] vec, float[] angle)
//static void applyObjectConstraintRot(TransInfo *t, TransData *td, float vec[3], float *angle)
{
	if ((t.con.mode & Transform.CON_APPLY)!=0) {
		int mode = t.con.mode & (Transform.CON_AXIS0|Transform.CON_AXIS1|Transform.CON_AXIS2);

		/* on setup call, use first object */
		if (td == null) {
			td= t.data[0];
		}

		switch(mode) {
		case Transform.CON_AXIS0:
		case (Transform.CON_AXIS1|Transform.CON_AXIS2):
			UtilDefines.VECCOPY(vec, td.axismtx[0]);
			break;
		case Transform.CON_AXIS1:
		case (Transform.CON_AXIS0|Transform.CON_AXIS2):
			UtilDefines.VECCOPY(vec, td.axismtx[1]);
			break;
		case Transform.CON_AXIS2:
		case (Transform.CON_AXIS0|Transform.CON_AXIS1):
			UtilDefines.VECCOPY(vec, td.axismtx[2]);
			break;
		}
		if (angle!=null && (mode & Transform.CON_NOFLIP) == 0 && TransformNumInput.hasNumInput(t.num) == false) {
			if (Arithb.Inpf(vec, t.viewinv[2]) > 0.0f) {
				angle[0] = -(angle[0]);
			}
		}
	}
}};

/*--------------------- INTERNAL SETUP CALLS ------------------*/

public static void setConstraint(TransInfo t, float[][] space, int mode, String text) {
	StringUtil.strncpy(t.con.text,1, StringUtil.toCString(text),0, 48);
	Arithb.Mat3CpyMat3(t.con.mtx, space);
	t.con.mode = mode;
	getConstraintMatrix(t);

	startConstraint(t);

	t.con.drawExtra = null;
	t.con.applyVec = applyAxisConstraintVec;
	t.con.applySize = applyAxisConstraintSize;
	t.con.applyRot = applyAxisConstraintRot;
	t.redraw = 1;
}

public static void setLocalConstraint(TransInfo t, int mode, String text) {
	if ((t.flag & Transform.T_EDIT)!=0) {
		float[][] obmat = new float[3][3];
		Arithb.Mat3CpyMat4(obmat, t.scene.obedit.obmat);
		setConstraint(t, obmat, mode, text);
	}
	else {
		if (t.total == 1) {
			setConstraint(t, t.data[0].axismtx, mode, text);
		}
		else {
			StringUtil.strncpy(t.con.text,1, StringUtil.toCString(text),0, 48);
			Arithb.Mat3CpyMat3(t.con.mtx, t.data[0].axismtx);
			t.con.mode = mode;
			getConstraintMatrix(t);

			startConstraint(t);

			t.con.drawExtra = drawObjectConstraint;
			t.con.applyVec = applyObjectConstraintVec;
			t.con.applySize = applyObjectConstraintSize;
			t.con.applyRot = applyObjectConstraintRot;
			t.redraw = 1;
		}
	}
}

/*
	Set the constraint according to the user defined orientation

	ftext is a format string passed to sprintf. It will add the name of
	the orientation where %s is (logically).
*/
public static void setUserConstraint(TransInfo t, int mode, String ftext) {
//	char text[40];
        String text;
	//short twmode= (t.spacetype==SPACE_VIEW3D)? ((View3D*)t.view).twmode: V3D_MANIP_GLOBAL;

	switch(t.current_orientation) {
	case View3dTypes.V3D_MANIP_GLOBAL:
		{
			float[][] mtx = new float[3][3];
//			sprintf(text, ftext, "global");
                        text = String.format(ftext, "global");
			Arithb.Mat3One(mtx);
			setConstraint(t, mtx, mode, text);
		}
		break;
	case View3dTypes.V3D_MANIP_LOCAL:
//		sprintf(text, ftext, "local");
                text = String.format(ftext, "local");
		setLocalConstraint(t, mode, text);
		break;
	case View3dTypes.V3D_MANIP_NORMAL:
//		sprintf(text, ftext, "normal");
                text = String.format(ftext, "normal");
		setConstraint(t, t.spacemtx, mode, text);
		break;
	case View3dTypes.V3D_MANIP_VIEW:
//		sprintf(text, ftext, "view");
                text = String.format(ftext, "view");
		setConstraint(t, t.spacemtx, mode, text);
		break;
	default: /* V3D_MANIP_CUSTOM */
//		sprintf(text, ftext, t.spacename);
                text = String.format(ftext, StringUtil.toJString(t.spacename,0));
		setConstraint(t, t.spacemtx, mode, text);
		break;
	}

	t.con.mode |= Transform.CON_USER;
}

/*----------------- DRAWING CONSTRAINTS -------------------*/

public static void drawConstraint(GL2 gl, bContext C, TransInfo t)
{
	TransCon tc = t.con;

	if (!(t.spacetype==SpaceTypes.SPACE_VIEW3D || t.spacetype==SpaceTypes.SPACE_IMAGE))
		return;
	if ((tc.mode & Transform.CON_APPLY)==0)
		return;
	if ((t.flag & Transform.T_USES_MANIPULATOR)!=0)
		return;
	if ((t.flag & Transform.T_NO_CONSTRAINT)!=0)
		return;

	/* nasty exception for Z constraint in camera view */
	// TRANSFORM_FIX_ME
//	if((t.flag & T_OBJECT) && G.vd.camera==OBACT && G.vd.persp==V3D_CAMOB)
//		return;

	if (tc.drawExtra!=null) {
		tc.drawExtra.run(gl, t);
	}
	else {
		if ((tc.mode & Transform.CON_SELECT)!=0) {
			float[] vec = new float[3];
			byte[] col2 = {(byte)255,(byte)255,(byte)255};
			Transform.convertViewVec(t, vec, (short)(t.mval[0] - t.con.imval[0]), (short)(t.mval[1] - t.con.imval[1]));
			Arithb.VecAddf(vec, vec, tc.center);

			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[0], 'x', 0);
			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[1], 'y', 0);
			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[2], 'z', 0);

			gl.glColor3ubv(col2,0);

			gl.glDisable(GL2.GL_DEPTH_TEST);
			GlUtil.setlinestyle(gl, 1);
			gl.glBegin(GL2.GL_LINE_STRIP);
				gl.glVertex3fv(tc.center,0);
				gl.glVertex3fv(vec,0);
			gl.glEnd();
			GlUtil.setlinestyle(gl, 0);
			// TRANSFORM_FIX_ME
			//if(G.vd.zbuf)
				gl.glEnable(GL2.GL_DEPTH_TEST);
		}

		if ((tc.mode & Transform.CON_AXIS0)!=0) {
			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[0], 'x', Transform.DRAWLIGHT);
		}
		if ((tc.mode & Transform.CON_AXIS1)!=0) {
			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[1], 'y', Transform.DRAWLIGHT);
		}
		if ((tc.mode & Transform.CON_AXIS2)!=0) {
			TransformGenerics.drawLine(gl, t, tc.center, tc.mtx[2], 'z', Transform.DRAWLIGHT);
		}
	}
}

/* called from drawview.c, as an extra per-window draw option */
public static void drawPropCircle(GL2 gl, bContext C, TransInfo t)
{
	if ((t.flag & Transform.T_PROP_EDIT)!=0) {
		RegionView3D rv3d = bContext.CTX_wm_region_view3d(C);
		float[][] tmat = new float[4][4], imat = new float[4][4];

		Resources.UI_ThemeColor(gl, Resources.TH_GRID);

		if(t.spacetype == SpaceTypes.SPACE_VIEW3D && rv3d != null)
		{
			Arithb.Mat4CpyMat4(tmat, rv3d.viewmat);
			Arithb.Mat4Invert(imat, tmat);
		}
		else
		{
			Arithb.Mat4One(tmat);
			Arithb.Mat4One(imat);
		}

		gl.glPushMatrix();

		if((t.spacetype == SpaceTypes.SPACE_VIEW3D) && t.obedit!=null)
		{
			gl.glMultMatrixf(Arithb.convertArray(t.obedit.obmat),0); /* because t.center is in local space */
		}
		else if(t.spacetype == SpaceTypes.SPACE_IMAGE)
		{
//			float aspx, aspy;
//
//			ED_space_image_uv_aspect(t.sa.spacedata.first, &aspx, &aspy);
//			glScalef(1.0f/aspx, 1.0f/aspy, 1.0);
		}

		GlUtil.set_inverted_drawing(gl, true);
		DrawObject.drawcircball(gl, GL2.GL_LINE_LOOP, t.center, t.prop_size, imat);
		GlUtil.set_inverted_drawing(gl, false);

		gl.glPopMatrix();
	}
}

public static TransCon.DrawExtra drawObjectConstraint = new TransCon.DrawExtra() {
public void run(GL2 gl, TransInfo t)
//static void drawObjectConstraint(TransInfo *t)
{
	int i;
	TransData[] td = t.data;
        int td_p = 0;

	/* Draw the first one lighter because that's the one who controls the others.
	   Meaning the transformation is projected on that one and just copied on the others
	   constraint space.
	   In a nutshell, the object with light axis is controlled by the user and the others follow.
	   Without drawing the first light, users have little clue what they are doing.
	 */
	if ((t.con.mode & Transform.CON_AXIS0)!=0) {
		TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[0], 'x', Transform.DRAWLIGHT);
	}
	if ((t.con.mode & Transform.CON_AXIS1)!=0) {
		TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[1], 'y', Transform.DRAWLIGHT);
	}
	if ((t.con.mode & Transform.CON_AXIS2)!=0) {
		TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[2], 'z', Transform.DRAWLIGHT);
	}

	td_p++;

	for(i=1;i<t.total;i++,td_p++) {
		if ((t.con.mode & Transform.CON_AXIS0)!=0) {
			TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[0], 'x', 0);
		}
		if ((t.con.mode & Transform.CON_AXIS1)!=0) {
			TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[1], 'y', 0);
		}
		if ((t.con.mode & Transform.CON_AXIS2)!=0) {
			TransformGenerics.drawLine(gl, t, td[td_p].ob.obmat[3], td[td_p].axismtx[2], 'z', 0);
		}
	}
}};

/*--------------------- START / STOP CONSTRAINTS ---------------------- */

public static void startConstraint(TransInfo t) {
	t.con.mode |= Transform.CON_APPLY;
	t.con.text[0] = ' ';
	t.num.idx_max = (short)UtilDefines.MIN2(getConstraintSpaceDimension(t) - 1, t.idx_max);
}

public static void stopConstraint(TransInfo t) {
	t.con.mode &= ~(Transform.CON_APPLY|Transform.CON_SELECT);
	t.con.text[0] = '\0';
	t.num.idx_max = t.idx_max;
}

public static void getConstraintMatrix(TransInfo t)
{
	float[][] mat = new float[3][3];
	Arithb.Mat3Inv(t.con.imtx, t.con.mtx);
	Arithb.Mat3One(t.con.pmtx);

	if ((t.con.mode & Transform.CON_AXIS0)==0) {
		t.con.pmtx[0][0]		=
			t.con.pmtx[0][1]	=
			t.con.pmtx[0][2]	= 0.0f;
	}

	if ((t.con.mode & Transform.CON_AXIS1)==0) {
		t.con.pmtx[1][0]		=
			t.con.pmtx[1][1]	=
			t.con.pmtx[1][2]	= 0.0f;
	}

	if ((t.con.mode & Transform.CON_AXIS2)==0) {
		t.con.pmtx[2][0]		=
			t.con.pmtx[2][1]	=
			t.con.pmtx[2][2]	= 0.0f;
	}

	Arithb.Mat3MulMat3(mat, t.con.pmtx, t.con.imtx);
	Arithb.Mat3MulMat3(t.con.pmtx, t.con.mtx, mat);
}

/*------------------------- MMB Select -------------------------------*/

public static void initSelectConstraint(TransInfo t, float[][] mtx)
{
	Arithb.Mat3CpyMat3(t.con.mtx, mtx);
	t.con.mode |= Transform.CON_APPLY;
	t.con.mode |= Transform.CON_SELECT;

	setNearestAxis(t);
	t.con.drawExtra = null;
	t.con.applyVec = applyAxisConstraintVec;
	t.con.applySize = applyAxisConstraintSize;
	t.con.applyRot = applyAxisConstraintRot;
}

public static void selectConstraint(TransInfo t) {
	if ((t.con.mode & Transform.CON_SELECT)!=0) {
		setNearestAxis(t);
		startConstraint(t);
	}
}

public static void postSelectConstraint(TransInfo t)
{
	if ((t.con.mode & Transform.CON_SELECT)==0)
		return;

	t.con.mode &= ~Transform.CON_AXIS0;
	t.con.mode &= ~Transform.CON_AXIS1;
	t.con.mode &= ~Transform.CON_AXIS2;
	t.con.mode &= ~Transform.CON_SELECT;

	setNearestAxis(t);

	startConstraint(t);
	t.redraw = 1;
}

static void setNearestAxis2d(TransInfo t)
{
	/* no correction needed... just use whichever one is lower */
	if ( StrictMath.abs(t.mval[0]-t.con.imval[0]) < StrictMath.abs(t.mval[1]-t.con.imval[1]) ) {
		t.con.mode |= Transform.CON_AXIS1;
//		sprintf(t.con.text, " along Y axis");
                StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(" along Y axis"), 0);
	}
	else {
		t.con.mode |= Transform.CON_AXIS0;
//		sprintf(t.con.text, " along X axis");
                StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(" along X axis"), 0);
	}
}

static void setNearestAxis3d(TransInfo t)
{
	float zfac;
	float[] mvec = new float[3], axis = new float[3], proj = new float[3];
	float[] len = new float[3];
	int i;
        int[] icoord = new int[2];

	/* calculate mouse movement */
	mvec[0] = (float)(t.mval[0] - t.con.imval[0]);
	mvec[1] = (float)(t.mval[1] - t.con.imval[1]);
	mvec[2] = 0.0f;

	/* we need to correct axis length for the current zoomlevel of view,
	   this to prevent projected values to be clipped behind the camera
	   and to overflow the short integers.
	   The formula used is a bit stupid, just a simplification of the substraction
	   of two 2D points 30 pixels apart (that's the last factor in the formula) after
	   projecting them with window_to_3d_delta and then get the length of that vector.
	*/
	zfac= t.persmat[0][3]*t.center[0]+ t.persmat[1][3]*t.center[1]+ t.persmat[2][3]*t.center[2]+ t.persmat[3][3];
	zfac = Arithb.VecLength(t.persinv[0]) * 2.0f/t.ar.winx * zfac * 30.0f;

	for (i = 0; i<3; i++) {
		UtilDefines.VECCOPY(axis, t.con.mtx[i]);

		Arithb.VecMulf(axis, zfac);
		/* now we can project to get window coordinate */
		Arithb.VecAddf(axis, axis, t.con.center);
		Transform.projectIntView(t, axis, icoord);

		axis[0] = (float)(icoord[0] - t.center2d[0]);
		axis[1] = (float)(icoord[1] - t.center2d[1]);
		axis[2] = 0.0f;

 		if (Arithb.Normalize(axis) != 0.0f) {
			Arithb.Projf(proj, mvec, axis);
			Arithb.VecSubf(axis, mvec, proj);
			len[i] = Arithb.Normalize(axis);
		}
		else {
			len[i] = 10000000000.0f;
		}
	}

	if (len[0] <= len[1] && len[0] <= len[2]) {
		if ((t.modifiers & Transform.MOD_CONSTRAINT_PLANE)!=0) {
			t.con.mode |= (Transform.CON_AXIS1|Transform.CON_AXIS2);
//			sprintf(t.con.text, " locking %s X axis", t.spacename);
                        String text = String.format(" locking %s X axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
		else {
			t.con.mode |= Transform.CON_AXIS0;
//			sprintf(t.con.text, " along %s X axis", t.spacename);
                        String text = String.format(" along %s X axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
	}
	else if (len[1] <= len[0] && len[1] <= len[2]) {
		if ((t.modifiers & Transform.MOD_CONSTRAINT_PLANE)!=0) {
			t.con.mode |= (Transform.CON_AXIS0|Transform.CON_AXIS2);
//			sprintf(t.con.text, " locking %s Y axis", t.spacename);
                        String text = String.format(" locking %s Y axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
		else {
			t.con.mode |= Transform.CON_AXIS1;
//			sprintf(t.con.text, " along %s Y axis", t.spacename);
                        String text = String.format(" along %s Y axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
	}
	else if (len[2] <= len[1] && len[2] <= len[0]) {
		if ((t.modifiers & Transform.MOD_CONSTRAINT_PLANE)!=0) {
			t.con.mode |= (Transform.CON_AXIS0|Transform.CON_AXIS1);
//			sprintf(t.con.text, " locking %s Z axis", t.spacename);
                        String text = String.format(" locking %s Z axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
		else {
			t.con.mode |= Transform.CON_AXIS2;
//			sprintf(t.con.text, " along %s Z axis", t.spacename);
                        String text = String.format(" along %s Z axis", StringUtil.toJString(t.spacename,0));
                        StringUtil.strcpy(t.con.text, 0, StringUtil.toCString(text), 0);
		}
	}
}

public static void setNearestAxis(TransInfo t)
{
	/* clear any prior constraint flags */
	t.con.mode &= ~Transform.CON_AXIS0;
	t.con.mode &= ~Transform.CON_AXIS1;
	t.con.mode &= ~Transform.CON_AXIS2;

	/* constraint setting - depends on spacetype */
	if (t.spacetype == SpaceTypes.SPACE_VIEW3D) {
		/* 3d-view */
		setNearestAxis3d(t);
	}
	else {
		/* assume that this means a 2D-Editor */
		setNearestAxis2d(t);
	}

	getConstraintMatrix(t);
}

/*-------------- HELPER FUNCTIONS ----------------*/

public static char constraintModeToChar(TransInfo t) {
	if ((t.con.mode & Transform.CON_APPLY)==0) {
		return '\0';
	}
	switch (t.con.mode & (Transform.CON_AXIS0|Transform.CON_AXIS1|Transform.CON_AXIS2)) {
	case (Transform.CON_AXIS0):
	case (Transform.CON_AXIS1|Transform.CON_AXIS2):
		return 'X';
	case (Transform.CON_AXIS1):
	case (Transform.CON_AXIS0|Transform.CON_AXIS2):
		return 'Y';
	case (Transform.CON_AXIS2):
	case (Transform.CON_AXIS0|Transform.CON_AXIS1):
		return 'Z';
	default:
		return '\0';
	}
}


//int isLockConstraint(TransInfo *t) {
//	int mode = t.con.mode;
//
//	if ( (mode & (CON_AXIS0|CON_AXIS1)) == (CON_AXIS0|CON_AXIS1))
//		return 1;
//
//	if ( (mode & (CON_AXIS1|CON_AXIS2)) == (CON_AXIS1|CON_AXIS2))
//		return 1;
//
//	if ( (mode & (CON_AXIS0|CON_AXIS2)) == (CON_AXIS0|CON_AXIS2))
//		return 1;
//
//	return 0;
//}

/*
 * Returns the dimension of the constraint space.
 *
 * For that reason, the flags always needs to be set to properly evaluate here,
 * even if they aren't actually used in the callback function. (Which could happen
 * for weird constraints not yet designed. Along a path for example.)
 */

public static int getConstraintSpaceDimension(TransInfo t)
{
	int n = 0;

	if ((t.con.mode & Transform.CON_AXIS0)!=0)
		n++;

	if ((t.con.mode & Transform.CON_AXIS1)!=0)
		n++;

	if ((t.con.mode & Transform.CON_AXIS2)!=0)
		n++;

	return n;
/*
  Someone willing to do it criptically could do the following instead:

  return t.con & (CON_AXIS0|CON_AXIS1|CON_AXIS2);

  Based on the assumptions that the axis flags are one after the other and start at 1
*/
}

}
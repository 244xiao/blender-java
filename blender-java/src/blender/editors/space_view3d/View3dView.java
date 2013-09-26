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
 * The Original Code is Copyright (C) 2008 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_view3d;

import blender.blenkernel.Global;
import blender.blenkernel.ObjectUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.editors.screen.Area;
import blender.editors.space_view3d.View3dStruct.ViewContext;
import blender.editors.space_view3d.View3dStruct.ViewDepths;
import blender.makesdna.CameraTypes;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.BoundBox;
import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.Lamp;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.rctf;
import blender.makesdna.sdna.rcti;
import blender.windowmanager.WmSubWindow;
import static blender.blenkernel.Blender.G;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class View3dView {

    /* Projection */
    public static final int IS_CLIPPED=        12000;

    public static final float BL_NEAR_CLIP= 0.001f;

    /* drawing flags: */
    public static final int DRAW_PICKING=	1;
    public static final int DRAW_CONSTCOLOR=	2;
    public static final int DRAW_SCENESET=	4;

    public static final int V3D_XRAY=	1;
    public static final int V3D_TRANSP=	2;

    public static final int V3D_SELECT_MOUSE=	1;


/* use this call when executing an operator,
   event system doesn't set for each event the
   opengl drawing context */
public static void view3d_operator_needs_opengl(bContext C)
{
	ARegion ar= bContext.CTX_wm_region(C);

	/* for debugging purpose, context should always be OK */
	if(ar.regiontype!=ScreenTypes.RGN_TYPE_WINDOW)
		System.out.printf("view3d_operator_needs_opengl error, wrong region\n");
	else {
		RegionView3D rv3d= (RegionView3D)ar.regiondata;

                GL2 gl = (GL2)GLU.getCurrentGL();
		WmSubWindow.wmSubWindowSet(gl, bContext.CTX_wm_window(C), ar.swinid);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		WmSubWindow.wmLoadMatrix(gl, rv3d.winmat);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);
	}
}

    public static float[] give_cursor(Scene scene, View3D v3d) {
//        if (v3d != null && v3d.localview != 0) {
    	if (v3d != null && v3d.localvd != null) {
            return v3d.cursor;
        } else {
            return scene.cursor;
        }
    }

/* Gets the lens and clipping values from a camera of lamp type object */
static void object_lens_clip_settings(bObject ob, float[] lens, float[] clipsta, float[] clipend)
{
	if (ob==null) return;

	if(ob.type==ObjectTypes.OB_LAMP ) {
		Lamp la = (Lamp)ob.data;
		if (lens!=null) {
			float x1, fac;
			fac= (float)StrictMath.cos( Arithb.M_PI*la.spotsize/360.0);
			x1= Arithb.saacos(fac);
			lens[0]= 16.0f*fac/(float)StrictMath.sin(x1);
		}
		if (clipsta!=null)	clipsta[0]= la.clipsta;
		if (clipend!=null)	clipend[0]= la.clipend;
	}
	else if(ob.type==ObjectTypes.OB_CAMERA) {
		Camera cam= (Camera)ob.data;
		if (lens!=null)		lens[0]= cam.lens;
		if (clipsta!=null)	clipsta[0]= cam.clipsta;
		if (clipend!=null)	clipend[0]= cam.clipend;
	}
	else {
		if (lens!=null)		lens[0]= 35.0f;
	}
}


/* Gets the view trasnformation from a camera
* currently dosnt take camzoom into account
*
* The dist is not modified for this function, if NULL its assimed zero
* */
static void view_settings_from_ob(bObject ob, float[] ofs, float[] quat, float[] dist, float[] lens)
{
	float[][] bmat=new float[4][4];
	float[][] imat=new float[4][4];
	float[][] tmat=new float[3][3];

	if (ob==null) return;

	/* Offset */
	if (ofs!=null) {
		UtilDefines.VECCOPY(ofs, ob.obmat[3]);
		Arithb.VecMulf(ofs, -1.0f); /*flip the vector*/
	}

	/* Quat */
	if (quat!=null) {
		Arithb.Mat4CpyMat4(bmat, ob.obmat);
		Arithb.Mat4Ortho(bmat);
		Arithb.Mat4Invert(imat, bmat);
		Arithb.Mat3CpyMat4(tmat, imat);
		Arithb.Mat3ToQuat(tmat, quat);
	}

	if (dist!=null) {
		float[] vec=new float[3];
		Arithb.Mat3CpyMat4(tmat, ob.obmat);

		vec[0]= vec[1] = 0.0f;
		vec[2]= -(dist[0]);
		Arithb.Mat3MulVecfl(tmat, vec);
		Arithb.VecSubf(ofs, ofs, vec);
	}

	/* Lens */
	if (lens!=null)
		object_lens_clip_settings(ob, lens, null, null);
}


/* ****************** smooth view operator ****************** */

static class SmoothViewStore {
	public float orig_dist, new_dist;
	public float orig_lens, new_lens;
	public float[] orig_quat=new float[4], new_quat=new float[4];
	public float[] orig_ofs=new float[3], new_ofs=new float[3];

	public int to_camera, orig_view;

	public double time_allowed;
};

/* will start timer if appropriate */
/* the arguments are the desired situation */
public static void smooth_view(bContext C, bObject oldcamera, bObject camera, float[] ofs, float[] quat, float[] dist, float[] lens)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	SmoothViewStore sms = new SmoothViewStore();

	/* initialize sms */
//	memset(&sms,0,sizeof(struct SmoothViewStore));
	UtilDefines.VECCOPY(sms.new_ofs, rv3d.ofs);
	UtilDefines.QUATCOPY(sms.new_quat, rv3d.viewquat);
	sms.new_dist= rv3d.dist;
	sms.new_lens= v3d.lens;
	sms.to_camera= 0;

	/* store the options we want to end with */
	if(ofs!=null) UtilDefines.VECCOPY(sms.new_ofs, ofs);
	if(quat!=null) UtilDefines.QUATCOPY(sms.new_quat, quat);
	if(dist!=null) sms.new_dist= dist[0];
	if(lens!=null) sms.new_lens= lens[0];

	if (camera!=null) {
                float[] new_dist={sms.new_dist}, new_lens={sms.new_lens};
		view_settings_from_ob(camera, sms.new_ofs, sms.new_quat, new_dist, new_lens);
                sms.new_dist=new_dist[0]; sms.new_lens=new_lens[0];
		sms.to_camera= 1; /* restore view3d values in end */
	}

//	if (C && U.smooth_viewtx) {
//		int changed = 0; /* zero means no difference */
//
//		if (sms.new_dist != rv3d.dist)
//			changed = 1;
//		if (sms.new_lens != v3d.lens)
//			changed = 1;
//
//		if ((sms.new_ofs[0]!=rv3d.ofs[0]) ||
//			(sms.new_ofs[1]!=rv3d.ofs[1]) ||
//			(sms.new_ofs[2]!=rv3d.ofs[2]) )
//			changed = 1;
//
//		if ((sms.new_quat[0]!=rv3d.viewquat[0]) ||
//			(sms.new_quat[1]!=rv3d.viewquat[1]) ||
//			(sms.new_quat[2]!=rv3d.viewquat[2]) ||
//			(sms.new_quat[3]!=rv3d.viewquat[3]) )
//			changed = 1;
//
//		/* The new view is different from the old one
//			* so animate the view */
//		if (changed) {
//
//			sms.time_allowed= (double)U.smooth_viewtx / 1000.0;
//
//			/* if this is view rotation only
//				* we can decrease the time allowed by
//				* the angle between quats
//				* this means small rotations wont lag */
//			if (quat && !ofs && !dist) {
//			 	float vec1[3], vec2[3];
//
//			 	VECCOPY(vec1, sms.new_quat);
//			 	VECCOPY(vec2, sms.orig_quat);
//			 	Normalize(vec1);
//			 	Normalize(vec2);
//			 	/* scale the time allowed by the rotation */
//			 	sms.time_allowed *= NormalizedVecAngle2(vec1, vec2)/(M_PI/2);
//			}
//
//			/* original values */
//			if (oldcamera) {
//				sms.orig_dist= rv3d.dist; // below function does weird stuff with it...
//				view_settings_from_ob(oldcamera, sms.orig_ofs, sms.orig_quat, &sms.orig_dist, &sms.orig_lens);
//			}
//			else {
//				VECCOPY(sms.orig_ofs, rv3d.ofs);
//				QUATCOPY(sms.orig_quat, rv3d.viewquat);
//				sms.orig_dist= rv3d.dist;
//				sms.orig_lens= v3d.lens;
//			}
//			/* grid draw as floor */
//			sms.orig_view= rv3d.view;
//			rv3d.view= 0;
//
//			/* ensure it shows correct */
//			if(sms.to_camera) rv3d.persp= V3D_PERSP;
//
//			/* keep track of running timer! */
//			if(rv3d.sms==NULL)
//				rv3d.sms= MEM_mallocN(sizeof(struct SmoothViewStore), "smoothview v3d");
//			*rv3d.sms= sms;
//			if(rv3d.smooth_timer)
//				WM_event_remove_window_timer(CTX_wm_window(C), rv3d.smooth_timer);
//			/* TIMER1 is hardcoded in keymap */
//			rv3d.smooth_timer= WM_event_add_window_timer(CTX_wm_window(C), TIMER1, 1.0/30.0);	/* max 30 frs/sec */
//
//			return;
//		}
//	}

	/* if we get here nothing happens */
	if(sms.to_camera==0) {
		UtilDefines.VECCOPY(rv3d.ofs, sms.new_ofs);
		UtilDefines.QUATCOPY(rv3d.viewquat, sms.new_quat);
		rv3d.dist = sms.new_dist;
		v3d.lens = sms.new_lens;
	}
	Area.ED_region_tag_redraw(bContext.CTX_wm_region(C));
}

///* only meant for timer usage */
//static int view3d_smoothview_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	View3D *v3d = CTX_wm_view3d(C);
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//	struct SmoothViewStore *sms= rv3d.sms;
//	double step, step_inv;
//
//	/* escape if not our timer */
//	if(rv3d.smooth_timer==NULL || rv3d.smooth_timer!=event.customdata)
//		return OPERATOR_PASS_THROUGH;
//
//	step =  (rv3d.smooth_timer.duration)/sms.time_allowed;
//
//	/* end timer */
//	if(step >= 1.0f) {
//
//		/* if we went to camera, store the original */
//		if(sms.to_camera) {
//			rv3d.persp= V3D_CAMOB;
//			VECCOPY(rv3d.ofs, sms.orig_ofs);
//			QUATCOPY(rv3d.viewquat, sms.orig_quat);
//			rv3d.dist = sms.orig_dist;
//			v3d.lens = sms.orig_lens;
//		}
//		else {
//			VECCOPY(rv3d.ofs, sms.new_ofs);
//			QUATCOPY(rv3d.viewquat, sms.new_quat);
//			rv3d.dist = sms.new_dist;
//			v3d.lens = sms.new_lens;
//		}
//		rv3d.view= sms.orig_view;
//
//		MEM_freeN(rv3d.sms);
//		rv3d.sms= NULL;
//
//		WM_event_remove_window_timer(CTX_wm_window(C), rv3d.smooth_timer);
//		rv3d.smooth_timer= NULL;
//	}
//	else {
//		int i;
//
//		/* ease in/out */
//		if (step < 0.5)	step = (float)pow(step*2, 2)/2;
//		else			step = (float)1-(pow(2*(1-step),2)/2);
//
//		step_inv = 1.0-step;
//
//		for (i=0; i<3; i++)
//			rv3d.ofs[i] = sms.new_ofs[i]*step + sms.orig_ofs[i]*step_inv;
//
//		QuatInterpol(rv3d.viewquat, sms.orig_quat, sms.new_quat, step);
//
//		rv3d.dist = sms.new_dist*step + sms.orig_dist*step_inv;
//		v3d.lens = sms.new_lens*step + sms.orig_lens*step_inv;
//	}
//
//	ED_region_tag_redraw(CTX_wm_region(C));
//
//	return OPERATOR_FINISHED;
//}
//
//void VIEW3D_OT_smoothview(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Smooth View";
//	ot.idname= "VIEW3D_OT_smoothview";
//
//	/* api callbacks */
//	ot.invoke= view3d_smoothview_invoke;
//
//	ot.poll= ED_operator_view3d_active;
//}
//
//static int view3d_setcameratoview_exec(bContext *C, wmOperator *op)
//{
//	View3D *v3d = CTX_wm_view3d(C);
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//	Object *ob;
//	float dvec[3];
//
//	ob= v3d.camera;
//	dvec[0]= rv3d.dist*rv3d.viewinv[2][0];
//	dvec[1]= rv3d.dist*rv3d.viewinv[2][1];
//	dvec[2]= rv3d.dist*rv3d.viewinv[2][2];
//
//	VECCOPY(ob.loc, dvec);
//	VecSubf(ob.loc, ob.loc, v3d.ofs);
//	rv3d.viewquat[0]= -rv3d.viewquat[0];
//
//	QuatToEul(rv3d.viewquat, ob.rot);
//	rv3d.viewquat[0]= -rv3d.viewquat[0];
//
//	ob.recalc= OB_RECALC_OB;
//
//	WM_event_add_notifier(C, NC_OBJECT|ND_TRANSFORM, CTX_data_scene(C));
//
//	return OPERATOR_FINISHED;
//
//}
//
//void VIEW3D_OT_setcameratoview(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Align Camera To View";
//	ot.idname= "VIEW3D_OT_camera_to_view";
//
//	/* api callbacks */
//	ot.exec= view3d_setcameratoview_exec;
//	ot.poll= ED_operator_view3d_active;
//
//	/* flags */
//	ot.flag= OPTYPE_REGISTER|OPTYPE_UNDO;
//}
//
///* ********************************** */
//
///* create intersection coordinates in view Z direction at mouse coordinates */
//void viewline(ARegion *ar, View3D *v3d, short mval[2], float ray_start[3], float ray_end[3])
//{
//	RegionView3D *rv3d= ar.regiondata;
//	float vec[4];
//
//	if(rv3d.persp != V3D_ORTHO){
//		vec[0]= 2.0f * mval[0] / ar.winx - 1;
//		vec[1]= 2.0f * mval[1] / ar.winy - 1;
//		vec[2]= -1.0f;
//		vec[3]= 1.0f;
//
//		Mat4MulVec4fl(rv3d.persinv, vec);
//		VecMulf(vec, 1.0f / vec[3]);
//
//		VECCOPY(ray_start, rv3d.viewinv[3]);
//		VECSUB(vec, vec, ray_start);
//		Normalize(vec);
//
//		VECADDFAC(ray_start, rv3d.viewinv[3], vec, v3d.near);
//		VECADDFAC(ray_end, rv3d.viewinv[3], vec, v3d.far);
//	}
//	else {
//		vec[0] = 2.0f * mval[0] / ar.winx - 1;
//		vec[1] = 2.0f * mval[1] / ar.winy - 1;
//		vec[2] = 0.0f;
//		vec[3] = 1.0f;
//
//		Mat4MulVec4fl(rv3d.persinv, vec);
//
//		VECADDFAC(ray_start, vec, rv3d.viewinv[2],  1000.0f);
//		VECADDFAC(ray_end, vec, rv3d.viewinv[2], -1000.0f);
//	}
//}
//
///* create intersection ray in view Z direction at mouse coordinates */
//void viewray(ARegion *ar, View3D *v3d, short mval[2], float ray_start[3], float ray_normal[3])
//{
//	float ray_end[3];
//
//	viewline(ar, v3d, mval, ray_start, ray_end);
//	VecSubf(ray_normal, ray_end, ray_start);
//	Normalize(ray_normal);
//}


public static void initgrabz(RegionView3D rv3d, float x, float y, float z)
{
	if(rv3d==null)
            return;
	rv3d.zfac= rv3d.persmat[0][3]*x+ rv3d.persmat[1][3]*y+ rv3d.persmat[2][3]*z+ rv3d.persmat[3][3];

	/* if x,y,z is exactly the viewport offset, zfac is 0 and we don't want that
		* (accounting for near zero values)
		* */
	if (rv3d.zfac < 1.e-6f && rv3d.zfac > -1.e-6f)
            rv3d.zfac = 1.0f;

	/* Negative zfac means x, y, z was behind the camera (in perspective).
		* This gives flipped directions, so revert back to ok default case.
	*/
	if (rv3d.zfac < 0.0f)
            rv3d.zfac = 1.0f;
}

///* always call initgrabz */
//void window_to_3d(ARegion *ar, float *vec, short mx, short my)
//{
//	RegionView3D *rv3d= ar.regiondata;
//
//	float dx= ((float)(mx-(ar.winx/2)))*rv3d.zfac/(ar.winx/2);
//	float dy= ((float)(my-(ar.winy/2)))*rv3d.zfac/(ar.winy/2);
//
//	float fz= rv3d.persmat[0][3]*vec[0]+ rv3d.persmat[1][3]*vec[1]+ rv3d.persmat[2][3]*vec[2]+ rv3d.persmat[3][3];
//	fz= fz/rv3d.zfac;
//
//	vec[0]= (rv3d.persinv[0][0]*dx + rv3d.persinv[1][0]*dy+ rv3d.persinv[2][0]*fz)-rv3d.ofs[0];
//	vec[1]= (rv3d.persinv[0][1]*dx + rv3d.persinv[1][1]*dy+ rv3d.persinv[2][1]*fz)-rv3d.ofs[1];
//	vec[2]= (rv3d.persinv[0][2]*dx + rv3d.persinv[1][2]*dy+ rv3d.persinv[2][2]*fz)-rv3d.ofs[2];
//
//}

/* always call initgrabz */
/* only to detect delta motion */
public static void window_to_3d_delta(ARegion ar, float[] vec, int mx, int my)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float dx, dy;

	dx= 2.0f*mx*rv3d.zfac/ar.winx;
	dy= 2.0f*my*rv3d.zfac/ar.winy;

	vec[0]= (rv3d.persinv[0][0]*dx + rv3d.persinv[1][0]*dy);
	vec[1]= (rv3d.persinv[0][1]*dx + rv3d.persinv[1][1]*dy);
	vec[2]= (rv3d.persinv[0][2]*dx + rv3d.persinv[1][2]*dy);
}

//float read_cached_depth(ViewContext *vc, int x, int y)
//{
//	ViewDepths *vd = vc.rv3d.depths;
//
//	x -= vc.ar.winrct.xmin;
//	y -= vc.ar.winrct.ymin;
//
//	if(vd && vd.depths && x > 0 && y > 0 && x < vd.w && y < vd.h)
//		return vd.depths[y * vd.w + x];
//	else
//		return 1;
//}

public static void request_depth_update(RegionView3D rv3d)
{
	if(rv3d.depths!=null)
		((ViewDepths)rv3d.depths).damaged= 1;
}

//void view3d_get_object_project_mat(RegionView3D *rv3d, Object *ob, float pmat[4][4])
//{
//	float vmat[4][4];
//
//	Mat4MulMat4(vmat, ob.obmat, rv3d.viewmat);
//	Mat4MulMat4(pmat, vmat, rv3d.winmat);
//}
//
//
///* use above call to get projecting mat */
//void view3d_project_float(ARegion *ar, float *vec, float *adr, float mat[4][4])
//{
//	float vec4[4];
//
//	adr[0]= IS_CLIPPED;
//	VECCOPY(vec4, vec);
//	vec4[3]= 1.0;
//
//	Mat4MulVec4fl(mat, vec4);
//
//	if( vec4[3]>FLT_EPSILON ) {
//		adr[0] = (float)(ar.winx/2.0f)+(ar.winx/2.0f)*vec4[0]/vec4[3];
//		adr[1] = (float)(ar.winy/2.0f)+(ar.winy/2.0f)*vec4[1]/vec4[3];
//	} else {
//		adr[0] = adr[1] = 0.0f;
//	}
//}

public static boolean boundbox_clip(RegionView3D rv3d, float[][] obmat, BoundBox bb)
{
	/* return 1: draw */

	float[][] mat = new float[4][4];
        float[] vec = new float[4];
        float min, max;
	int a, flag= -1, fl;

	if(bb==null)
            return true;
	if((bb.flag & ObjectTypes.OB_BB_DISABLED)!=0)
            return true;

	Arithb.Mat4MulMat4(mat, obmat, rv3d.persmat);

	for(a=0; a<8; a++) {
		UtilDefines.VECCOPY(vec, bb.vec[a]);
		vec[3]= 1.0f;
		Arithb.Mat4MulVec4fl(mat, vec);
		max= vec[3];
		min= -vec[3];

		fl= 0;
		if(vec[0] < min) fl+= 1;
		if(vec[0] > max) fl+= 2;
		if(vec[1] < min) fl+= 4;
		if(vec[1] > max) fl+= 8;
		if(vec[2] < min) fl+= 16;
		if(vec[2] > max) fl+= 32;

		flag &= fl;
		if(flag==0)
                    return true;
	}

	return false;
}

public static void project_short(ARegion ar, float[] vec, short[] adr)	/* clips */
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float fx, fy;
        float[] vec4 = new float[4];

	adr[0]= IS_CLIPPED;

	if((rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0) {
		if(View3dDraw.view3d_test_clipping(rv3d, vec))
			return;
	}

	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;
	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);

	if( vec4[3]>BL_NEAR_CLIP ) {	/* 0.001 is the NEAR clipping cutoff for picking */
		fx= (ar.winx/2)*(1 + vec4[0]/vec4[3]);

		if( fx>0 && fx<ar.winx) {

			fy= (ar.winy/2)*(1 + vec4[1]/vec4[3]);

			if(fy>0.0 && fy< (float)ar.winy) {
				adr[0]= (short)StrictMath.floor(fx);
				adr[1]= (short)StrictMath.floor(fy);
			}
		}
	}
}

//void project_int(ARegion *ar, float *vec, int *adr)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	float fx, fy, vec4[4];
//
//	adr[0]= (int)2140000000.0f;
//	VECCOPY(vec4, vec);
//	vec4[3]= 1.0;
//
//	Mat4MulVec4fl(rv3d.persmat, vec4);
//
//	if( vec4[3]>BL_NEAR_CLIP ) {	/* 0.001 is the NEAR clipping cutoff for picking */
//		fx= (ar.winx/2)*(1 + vec4[0]/vec4[3]);
//
//		if( fx>-2140000000.0f && fx<2140000000.0f) {
//			fy= (ar.winy/2)*(1 + vec4[1]/vec4[3]);
//
//			if(fy>-2140000000.0f && fy<2140000000.0f) {
//				adr[0]= (int)floor(fx);
//				adr[1]= (int)floor(fy);
//			}
//		}
//	}
//}

public static void project_int_noclip(ARegion ar, float[] vec, int[] adr)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float fx, fy;
        float[] vec4 = new float[4];

	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;

	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);

	if( StrictMath.abs(vec4[3]) > BL_NEAR_CLIP ) {
		fx = (ar.winx/2)*(1 + vec4[0]/vec4[3]);
		fy = (ar.winy/2)*(1 + vec4[1]/vec4[3]);

		adr[0] = (int)StrictMath.floor(fx);
		adr[1] = (int)StrictMath.floor(fy);
	}
	else
	{
		adr[0] = ar.winx / 2;
		adr[1] = ar.winy / 2;
	}
}

public static void project_short_noclip(ARegion ar, float[] vec, short[] adr)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float fx, fy;
        float[] vec4=new float[4];

	adr[0]= IS_CLIPPED;
	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;

	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);

	if( vec4[3]>BL_NEAR_CLIP ) {	/* 0.001 is the NEAR clipping cutoff for picking */
		fx= (ar.winx/2)*(1 + vec4[0]/vec4[3]);

		if( fx>-32700 && fx<32700) {

			fy= (ar.winy/2)*(1 + vec4[1]/vec4[3]);

			if(fy>-32700.0 && fy<32700.0) {
				adr[0]= (short)StrictMath.floor(fx);
				adr[1]= (short)StrictMath.floor(fy);
			}
		}
	}
}

//void project_float(ARegion *ar, float *vec, float *adr)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	float vec4[4];
//
//	adr[0]= IS_CLIPPED;
//	VECCOPY(vec4, vec);
//	vec4[3]= 1.0;
//
//	Mat4MulVec4fl(rv3d.persmat, vec4);
//
//	if( vec4[3]>BL_NEAR_CLIP ) {
//		adr[0] = (float)(ar.winx/2.0)+(ar.winx/2.0)*vec4[0]/vec4[3];
//		adr[1] = (float)(ar.winy/2.0)+(ar.winy/2.0)*vec4[1]/vec4[3];
//	}
//}

public static void project_float_noclip(ARegion ar, float[] vec, float[] adr)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float[] vec4 = new float[4];

	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;

	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);

	if( StrictMath.abs(vec4[3]) > BL_NEAR_CLIP ) {
		adr[0] = (float)(ar.winx/2.0)+(ar.winx/2.0f)*vec4[0]/vec4[3];
		adr[1] = (float)(ar.winy/2.0)+(ar.winy/2.0f)*vec4[1]/vec4[3];
	}
	else
	{
		adr[0] = ar.winx / 2.0f;
		adr[1] = ar.winy / 2.0f;
	}
}

//int get_view3d_ortho(View3D *v3d, RegionView3D *rv3d)
//{
//  Camera *cam;
//
//  if(rv3d.persp==V3D_CAMOB) {
//      if(v3d.camera && v3d.camera.type==OB_CAMERA) {
//          cam= v3d.camera.data;
//
//          if(cam && cam.type==CAM_ORTHO)
//              return 1;
//          else
//              return 0;
//      }
//      else
//          return 0;
//  }
//
//  if(rv3d.persp==V3D_ORTHO)
//      return 1;
//
//  return 0;
//}

/* also exposed in previewrender.c */
public static boolean get_view3d_viewplane(View3D v3d, RegionView3D rv3d, int winxi, int winyi, rctf viewplane, float[] clipsta, float[] clipend, float[] pixsize)
{
	Camera cam=null;
	float lens, fac, x1, y1, x2, y2;
	float winx= (float)winxi, winy= (float)winyi;
	boolean orth= false;

	lens= v3d.lens;

	clipsta[0]= v3d.near;
	clipend[0]= v3d.far;

	if(rv3d.persp==View3dTypes.V3D_CAMOB) {
		if(v3d.camera!=null) {
			if(v3d.camera.type==ObjectTypes.OB_LAMP ) {
//				Lamp *la;
//
//				la= v3d.camera.data;
//				fac= cos( M_PI*la.spotsize/360.0);
//
//				x1= saacos(fac);
//				lens= 16.0*fac/sin(x1);
//
//				*clipsta= la.clipsta;
//				*clipend= la.clipend;
			}
			else if(v3d.camera.type==ObjectTypes.OB_CAMERA) {
				cam= (Camera)v3d.camera.data;
				lens= cam.lens;
				clipsta[0]= cam.clipsta;
				clipend[0]= cam.clipend;
			}
		}
	}

	if(rv3d.persp==View3dTypes.V3D_ORTHO) {
		if(winx>winy) x1= -rv3d.dist;
		else x1= -winx*rv3d.dist/winy;
		x2= -x1;

		if(winx>winy) y1= -winy*rv3d.dist/winx;
		else y1= -rv3d.dist;
		y2= -y1;

		clipend[0] *= 0.5f;	// otherwise too extreme low zbuffer quality
		clipsta[0]= - clipend[0];
		orth= true;
	}
	else {
		/* fac for zoom, also used for camdx */
		if(rv3d.persp==View3dTypes.V3D_CAMOB) {
			fac= (1.41421f+( (float)rv3d.camzoom )/50.0f);
			fac*= fac;
		}
		else fac= 2.0f;

		/* viewplane size depends... */
		if(cam!=null && cam.type==CameraTypes.CAM_ORTHO) {
			/* ortho_scale == 1 means exact 1 to 1 mapping */
			float dfac= 2.0f*cam.ortho_scale/fac;

			if(winx>winy) x1= -dfac;
			else x1= -winx*dfac/winy;
			x2= -x1;

			if(winx>winy) y1= -winy*dfac/winx;
			else y1= -dfac;
			y2= -y1;
			orth= true;
		}
		else {
			float dfac;

			if(winx>winy) dfac= 64.0f/(fac*winx*lens);
			else dfac= 64.0f/(fac*winy*lens);

			x1= - clipsta[0] * winx*dfac;
			x2= -x1;
			y1= - clipsta[0] * winy*dfac;
			y2= -y1;
			orth= false;
		}
		/* cam view offset */
		if(cam!=null) {
			float dx= 0.5f*fac*rv3d.camdx*(x2-x1);
			float dy= 0.5f*fac*rv3d.camdy*(y2-y1);
			x1+= dx;
			x2+= dx;
			y1+= dy;
			y2+= dy;
		}
	}

	if(pixsize!=null) {
		float viewfac;

		if(orth) {
			viewfac= (winx >= winy)? winx: winy;
			pixsize[0]= 1.0f/viewfac;
		}
		else {
			viewfac= (((winx >= winy)? winx: winy)*lens)/32.0f;
			pixsize[0]= clipsta[0]/viewfac;
		}
	}

	viewplane.xmin= x1;
	viewplane.ymin= y1;
	viewplane.xmax= x2;
	viewplane.ymax= y2;

	return orth;
}

public static void setwinmatrixview3d(GL2 gl, ARegion ar, View3D v3d, rctf rect)		/* rect: for picking */
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	rctf viewplane = new rctf();
	float[] clipsta={0}, clipend={0};
        float x1, y1, x2, y2;
	boolean orth;

	orth= get_view3d_viewplane(v3d, rv3d, ar.winx, ar.winy, viewplane, clipsta, clipend, null);
	//	printf("%d %d %f %f %f %f %f %f\n", winx, winy, viewplane.xmin, viewplane.ymin, viewplane.xmax, viewplane.ymax, clipsta, clipend);
	x1= viewplane.xmin;
	y1= viewplane.ymin;
	x2= viewplane.xmax;
	y2= viewplane.ymax;

	if(rect!=null) {		/* picking */
		rect.xmin/= (float)ar.winx;
		rect.xmin= x1+rect.xmin*(x2-x1);
		rect.ymin/= (float)ar.winy;
		rect.ymin= y1+rect.ymin*(y2-y1);
		rect.xmax/= (float)ar.winx;
		rect.xmax= x1+rect.xmax*(x2-x1);
		rect.ymax/= (float)ar.winy;
		rect.ymax= y1+rect.ymax*(y2-y1);

		if(orth)
                    WmSubWindow.wmOrtho(gl, rect.xmin, rect.xmax, rect.ymin, rect.ymax, -clipend[0], clipend[0]);
		else
                    WmSubWindow.wmFrustum(gl, rect.xmin, rect.xmax, rect.ymin, rect.ymax, clipsta[0], clipend[0]);

	}
	else {
		if(orth)
                    WmSubWindow.wmOrtho(gl, x1, x2, y1, y2, clipsta[0], clipend[0]);
		else
                    WmSubWindow.wmFrustum(gl, x1, x2, y1, y2, clipsta[0], clipend[0]);
	}

	/* not sure what this was for? (ton) */
	gl.glMatrixMode(GL2.GL_PROJECTION);
	WmSubWindow.wmGetMatrix(gl, rv3d.winmat);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
}


static void obmat_to_viewmat(View3D v3d, RegionView3D rv3d, bObject ob, boolean smooth)
{
	float[][] bmat = new float[4][4];
        float[][] tmat = new float[3][3];

	rv3d.view= 0; /* dont show the grid */

	Arithb.Mat4CpyMat4(bmat, ob.obmat);
	Arithb.Mat4Ortho(bmat);
	Arithb.Mat4Invert(rv3d.viewmat, bmat);

	/* view quat calculation, needed for add object */
	Arithb.Mat3CpyMat4(tmat, rv3d.viewmat);
	if (smooth) {
//		float new_quat[4];
//		if (rv3d.persp==V3D_CAMOB && v3d.camera) {
//			/* were from a camera view */
//
//			float orig_ofs[3];
//			float orig_dist= rv3d.dist;
//			float orig_lens= v3d.lens;
//			VECCOPY(orig_ofs, rv3d.ofs);
//
//			/* Switch from camera view */
//			Mat3ToQuat(tmat, new_quat);
//
//			rv3d.persp=V3D_PERSP;
//			rv3d.dist= 0.0;
//
//			view_settings_from_ob(v3d.camera, rv3d.ofs, NULL, NULL, &v3d.lens);
//			smooth_view(NULL, NULL, NULL, orig_ofs, new_quat, &orig_dist, &orig_lens); // XXX
//
//			rv3d.persp=V3D_CAMOB; /* just to be polite, not needed */
//
//		} else {
//			Mat3ToQuat(tmat, new_quat);
//			smooth_view(NULL, NULL, NULL, NULL, new_quat, NULL, NULL); // XXX
//		}
	} else {
		Arithb.Mat3ToQuat(tmat, rv3d.viewquat);
	}
}

//#define QUATSET(a, b, c, d, e)	a[0]=b; a[1]=c; a[2]=d; a[3]=e;
static final void QUATSET(float[] a, float b, float c, float d, float e) {
    a[0]=b; a[1]=c; a[2]=d; a[3]=e;
}

static void view3d_viewlock(RegionView3D rv3d)
{
	switch(rv3d.view) {
	case View3dTypes.V3D_VIEW_BOTTOM :
		QUATSET(rv3d.viewquat,0.0f, -1.0f, 0.0f, 0.0f);
		break;

	case View3dTypes.V3D_VIEW_BACK:
		QUATSET(rv3d.viewquat,0.0f, 0.0f, (float)-StrictMath.cos(Arithb.M_PI/4.0), (float)-StrictMath.cos(Arithb.M_PI/4.0));
		break;

	case View3dTypes.V3D_VIEW_LEFT:
		QUATSET(rv3d.viewquat,0.5f, -0.5f, 0.5f, 0.5f);
		break;

	case View3dTypes.V3D_VIEW_TOP:
		QUATSET(rv3d.viewquat,1.0f, 0.0f, 0.0f, 0.0f);
		break;

	case View3dTypes.V3D_VIEW_FRONT:
		QUATSET(rv3d.viewquat,(float)StrictMath.cos(Arithb.M_PI/4.0), (float)-StrictMath.sin(Arithb.M_PI/4.0), 0.0f, 0.0f);
		break;

	case View3dTypes.V3D_VIEW_RIGHT:
		QUATSET(rv3d.viewquat, 0.5f, -0.5f, -0.5f, -0.5f);
		break;
	}
}

/* dont set windows active in in here, is used by renderwin too */
public static void setviewmatrixview3d(Scene scene, View3D v3d, RegionView3D rv3d)
{
	if(rv3d.persp==View3dTypes.V3D_CAMOB) {	    /* obs/camera */
		if(v3d.camera!=null) {
			ObjectUtil.where_is_object(scene, v3d.camera);
			obmat_to_viewmat(v3d, rv3d, v3d.camera, false);
		}
		else {
			Arithb.QuatToMat4(rv3d.viewquat, rv3d.viewmat);
			rv3d.viewmat[3][2]-= rv3d.dist;
		}
	}
	else {
		/* should be moved to better initialize later on XXX */
		if(rv3d.viewlock!=0)
			view3d_viewlock(rv3d);

		Arithb.QuatToMat4(rv3d.viewquat, rv3d.viewmat);
		if(rv3d.persp==View3dTypes.V3D_PERSP) rv3d.viewmat[3][2]-= rv3d.dist;
		if(v3d.ob_centre!=null) {
//			Object *ob= v3d.ob_centre;
//			float vec[3];
//
//			VECCOPY(vec, ob.obmat[3]);
//			if(ob.type==OB_ARMATURE && v3d.ob_centre_bone[0]) {
//				bPoseChannel *pchan= get_pose_channel(ob.pose, v3d.ob_centre_bone);
//				if(pchan) {
//					VECCOPY(vec, pchan.pose_mat[3]);
//					Mat4MulVecfl(ob.obmat, vec);
//				}
//			}
//			i_translate(-vec[0], -vec[1], -vec[2], rv3d.viewmat);
		}
		else Arithb.i_translate(rv3d.ofs[0], rv3d.ofs[1], rv3d.ofs[2], rv3d.viewmat);
	}
}

/* IGLuint. GLuint*/
/* Warning: be sure to account for a negative return value
*   This is an error, "Too many objects in select buffer"
*   and no action should be taken (can crash blender) if this happens
*/
public static short view3d_opengl_select(ViewContext vc, IntBuffer buffer, int bufsize, rcti input)
{
	Scene scene= vc.scene;
	View3D v3d= vc.v3d;
	ARegion ar= vc.ar;
	rctf rect = new rctf();
	short code, hits;

	G.f |= Global.G_PICKSEL;

	/* case not a border select */
	if(input.xmin==input.xmax) {
		rect.xmin= input.xmin-12;	// seems to be default value for bones only now
		rect.xmax= input.xmin+12;
		rect.ymin= input.ymin-12;
		rect.ymax= input.ymin+12;
	}
	else {
		rect.xmin= input.xmin;
		rect.xmax= input.xmax;
		rect.ymin= input.ymin;
		rect.ymax= input.ymax;
	}

        GL2 gl = (GL2)GLU.getCurrentGL();

	setwinmatrixview3d(gl, ar, v3d, rect);
	Arithb.Mat4MulMat4(vc.rv3d.persmat, vc.rv3d.viewmat, vc.rv3d.winmat);

	if(v3d.drawtype > ObjectTypes.OB_WIRE) {
		v3d.zbuf= 1;
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	if((vc.rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0)
		View3dDraw.view3d_set_clipping(gl, vc.rv3d);

	gl.glSelectBuffer(bufsize, buffer);
	gl.glRenderMode(GL2.GL_SELECT);
	gl.glInitNames();	/* these two calls whatfor? It doesnt work otherwise */
	gl.glPushName(-1);
	code= 1;

	if(vc.obedit!=null && vc.obedit.type==ObjectTypes.OB_MBALL) {
		DrawObject.draw_object(gl, scene, ar, v3d, scene.basact, DRAW_PICKING|DRAW_CONSTCOLOR);
	}
	else if((vc.obedit!=null  && vc.obedit.type==ObjectTypes.OB_ARMATURE)) {
		/* if not drawing sketch, draw bones */
//		if(!BDR_drawSketchNames(vc)) {
//			draw_object(scene, ar, v3d, BASACT, DRAW_PICKING|DRAW_CONSTCOLOR);
//		}
	}
	else {
		Base base;

		v3d.xray= 1;	// otherwise it postpones drawing
		for(base= (Base)scene.base.first; base!=null; base= base.next) {
			if((base.lay & v3d.lay)!=0) {

				if ((base.object.restrictflag & ObjectTypes.OB_RESTRICT_SELECT)!=0)
					base.selcol= 0;
				else {
					base.selcol= code;
					gl.glLoadName(code);
					DrawObject.draw_object(gl, scene, ar, v3d, base, DRAW_PICKING|DRAW_CONSTCOLOR);

					/* we draw group-duplicators for selection too */
					if((base.object.transflag & ObjectTypes.OB_DUPLI)!=0 && base.object.dup_group!=null) {
//						ListBase *lb;
//						DupliObject *dob;
//						Base tbase;
//
//						tbase.flag= OB_FROMDUPLI;
//						lb= object_duplilist(scene, base.object);
//
//						for(dob= lb.first; dob; dob= dob.next) {
//							tbase.object= dob.ob;
//							Mat4CpyMat4(dob.ob.obmat, dob.mat);
//
//							draw_object(scene, ar, v3d, &tbase, DRAW_PICKING|DRAW_CONSTCOLOR);
//
//							Mat4CpyMat4(dob.ob.obmat, dob.omat);
//						}
//						free_object_duplilist(lb);
					}
					code++;
				}
			}
		}
		v3d.xray= 0;	// restore
	}

	gl.glPopName();	/* see above (pushname) */
	hits= (short)gl.glRenderMode(GL2.GL_RENDER);

	G.f &= ~Global.G_PICKSEL;
	setwinmatrixview3d(gl, ar, v3d, null);
	Arithb.Mat4MulMat4(vc.rv3d.persmat, vc.rv3d.viewmat, vc.rv3d.winmat);

	if(v3d.drawtype > ObjectTypes.OB_WIRE) {
		v3d.zbuf= 0;
		gl.glDisable(GL2.GL_DEPTH_TEST);
	}
// XXX	persp(PERSP_WIN);

	if((vc.rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0)
		View3dDraw.view3d_clr_clipping(gl);

	if(hits<0) System.out.printf("Too many objects in select buffer\n");	// XXX make error message

	return hits;
}

///* ********************** local view operator ******************** */
//
//static unsigned int free_localbit(void)
//{
//	unsigned int lay;
//	ScrArea *sa;
//	bScreen *sc;
//
//	lay= 0;
//
//	/* sometimes we loose a localview: when an area is closed */
//	/* check all areas: which localviews are in use? */
//	for(sc= G.main.screen.first; sc; sc= sc.id.next) {
//		for(sa= sc.areabase.first; sa; sa= sa.next) {
//			SpaceLink *sl= sa.spacedata.first;
//			for(; sl; sl= sl.next) {
//				if(sl.spacetype==SPACE_VIEW3D) {
//					View3D *v3d= (View3D*) sl;
//					lay |= v3d.lay;
//				}
//			}
//		}
//	}
//
//	if( (lay & 0x01000000)==0) return 0x01000000;
//	if( (lay & 0x02000000)==0) return 0x02000000;
//	if( (lay & 0x04000000)==0) return 0x04000000;
//	if( (lay & 0x08000000)==0) return 0x08000000;
//	if( (lay & 0x10000000)==0) return 0x10000000;
//	if( (lay & 0x20000000)==0) return 0x20000000;
//	if( (lay & 0x40000000)==0) return 0x40000000;
//	if( (lay & 0x80000000)==0) return 0x80000000;
//
//	return 0;
//}
//
//
//static void initlocalview(Scene *scene, ScrArea *sa)
//{
//	View3D *v3d= sa.spacedata.first;
//	Base *base;
//	float size = 0.0, min[3], max[3], box[3];
//	unsigned int locallay;
//	int ok=0;
//
//	if(v3d.localvd) return;
//
//	INIT_MINMAX(min, max);
//
//	locallay= free_localbit();
//
//	if(locallay==0) {
//		printf("Sorry, no more than 8 localviews\n");	// XXX error
//		ok= 0;
//	}
//	else {
//		if(scene.obedit) {
//			minmax_object(scene.obedit, min, max);
//
//			ok= 1;
//
//			BASACT.lay |= locallay;
//			scene.obedit.lay= BASACT.lay;
//		}
//		else {
//			for(base= FIRSTBASE; base; base= base.next) {
//				if(TESTBASE(v3d, base))  {
//					minmax_object(base.object, min, max);
//					base.lay |= locallay;
//					base.object.lay= base.lay;
//					ok= 1;
//				}
//			}
//		}
//
//		box[0]= (max[0]-min[0]);
//		box[1]= (max[1]-min[1]);
//		box[2]= (max[2]-min[2]);
//		size= MAX3(box[0], box[1], box[2]);
//		if(size<=0.01) size= 0.01;
//	}
//
//	if(ok) {
//		ARegion *ar;
//
//		v3d.localvd= MEM_mallocN(sizeof(View3D), "localview");
//
//		memcpy(v3d.localvd, v3d, sizeof(View3D));
//
//		for(ar= sa.regionbase.first; ar; ar= ar.next) {
//			if(ar.regiontype == RGN_TYPE_WINDOW) {
//				RegionView3D *rv3d= ar.regiondata;
//
//				rv3d.localvd= MEM_mallocN(sizeof(RegionView3D), "localview region");
//				memcpy(rv3d.localvd, rv3d, sizeof(RegionView3D));
//
//				rv3d.ofs[0]= -(min[0]+max[0])/2.0;
//				rv3d.ofs[1]= -(min[1]+max[1])/2.0;
//				rv3d.ofs[2]= -(min[2]+max[2])/2.0;
//
//				rv3d.dist= size;
//				/* perspective should be a bit farther away to look nice */
//				if(rv3d.persp==V3D_ORTHO)
//					rv3d.dist*= 0.7;
//
//				// correction for window aspect ratio
//				if(ar.winy>2 && ar.winx>2) {
//					float asp= (float)ar.winx/(float)ar.winy;
//					if(asp<1.0) asp= 1.0/asp;
//					rv3d.dist*= asp;
//				}
//
//				if (rv3d.persp==V3D_CAMOB) rv3d.persp= V3D_PERSP;
//
//				v3d.cursor[0]= -rv3d.ofs[0];
//				v3d.cursor[1]= -rv3d.ofs[1];
//				v3d.cursor[2]= -rv3d.ofs[2];
//			}
//		}
//		if (v3d.near> 0.1) v3d.near= 0.1;
//
//		v3d.lay= locallay;
//	}
//	else {
//		/* clear flags */
//		for(base= FIRSTBASE; base; base= base.next) {
//			if( base.lay & locallay ) {
//				base.lay-= locallay;
//				if(base.lay==0) base.lay= v3d.layact;
//				if(base.object != scene.obedit) base.flag |= SELECT;
//				base.object.lay= base.lay;
//			}
//		}
//		v3d.localview= 0;
//	}
//
//}
//
//static void restore_localviewdata(ScrArea *sa, int free)
//{
//	ARegion *ar;
//	View3D *v3d= sa.spacedata.first;
//
//	if(v3d.localvd==NULL) return;
//
//	v3d.near= v3d.localvd.near;
//	v3d.far= v3d.localvd.far;
//	v3d.lay= v3d.localvd.lay;
//	v3d.layact= v3d.localvd.layact;
//	v3d.drawtype= v3d.localvd.drawtype;
//	v3d.camera= v3d.localvd.camera;
//
//	if(free) {
//		MEM_freeN(v3d.localvd);
//		v3d.localvd= NULL;
//		v3d.localview= 0;
//	}
//
//	for(ar= sa.regionbase.first; ar; ar= ar.next) {
//		if(ar.regiontype == RGN_TYPE_WINDOW) {
//			RegionView3D *rv3d= ar.regiondata;
//
//			if(rv3d.localvd) {
//				rv3d.dist= rv3d.localvd.dist;
//				VECCOPY(rv3d.ofs, rv3d.localvd.ofs);
//				QUATCOPY(rv3d.viewquat, rv3d.localvd.viewquat);
//				rv3d.view= rv3d.localvd.view;
//				rv3d.persp= rv3d.localvd.persp;
//				rv3d.camzoom= rv3d.localvd.camzoom;
//
//				if(free) {
//					MEM_freeN(rv3d.localvd);
//					rv3d.localvd= NULL;
//				}
//			}
//		}
//	}
//}
//
//static void endlocalview(Scene *scene, ScrArea *sa)
//{
//	View3D *v3d= sa.spacedata.first;
//	struct Base *base;
//	unsigned int locallay;
//
//	if(v3d.localvd) {
//
//		locallay= v3d.lay & 0xFF000000;
//
//		restore_localviewdata(sa, 1); // 1 = free
//
//		/* for when in other window the layers have changed */
//		if(v3d.scenelock) v3d.lay= scene.lay;
//
//		for(base= FIRSTBASE; base; base= base.next) {
//			if( base.lay & locallay ) {
//				base.lay-= locallay;
//				if(base.lay==0) base.lay= v3d.layact;
//				if(base.object != scene.obedit) {
//					base.flag |= SELECT;
//					base.object.flag |= SELECT;
//				}
//				base.object.lay= base.lay;
//			}
//		}
//	}
//}
//
//static int localview_exec(bContext *C, wmOperator *unused)
//{
//	View3D *v3d= CTX_wm_view3d(C);
//
//	if(v3d.localvd)
//		endlocalview(CTX_data_scene(C), CTX_wm_area(C));
//	else
//		initlocalview(CTX_data_scene(C), CTX_wm_area(C));
//
//	ED_area_tag_redraw(CTX_wm_area(C));
//
//	return OPERATOR_FINISHED;
//}
//
//void VIEW3D_OT_localview(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Local View";
//	ot.idname= "VIEW3D_OT_localview";
//
//	/* api callbacks */
//	ot.exec= localview_exec;
//
//	ot.poll= ED_operator_view3d_active;
//}
//
//#if GAMEBLENDER == 1
//
//static ListBase queue_back;
//static void SaveState(bContext *C)
//{
//	wmWindow *win= CTX_wm_window(C);
//
//	glPushAttrib(GL_ALL_ATTRIB_BITS);
//
//	GPU_state_init();
//
//	if(G.f & G_TEXTUREPAINT)
//		GPU_paint_set_mipmap(1);
//
//	queue_back= win.queue;
//
//	win.queue.first= win.queue.last= NULL;
//
//	//XXX waitcursor(1);
//}
//
//static void RestoreState(bContext *C)
//{
//	wmWindow *win= CTX_wm_window(C);
//
//	if(G.f & G_TEXTUREPAINT)
//		GPU_paint_set_mipmap(0);
//
//	//XXX curarea.win_swap = 0;
//	//XXX curarea.head_swap=0;
//	//XXX allqueue(REDRAWVIEW3D, 1);
//	//XXX allqueue(REDRAWBUTSALL, 0);
//	//XXX reset_slowparents();
//	//XXX waitcursor(0);
//	//XXX G.qual= 0;
//
//	win.queue= queue_back;
//
//	glPopAttrib();
//}
//
///* maybe we need this defined somewhere else */
//extern void StartKetsjiShell(struct bContext *C, struct ARegion *ar, int always_use_expand_framing);
//
//#endif // GAMEBLENDER == 1
//
//static int game_engine_exec(bContext *C, wmOperator *unused)
//{
//#if GAMEBLENDER == 1
//	Scene *startscene = CTX_data_scene(C);
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa, *prevsa= CTX_wm_area(C);
//	ARegion *ar, *prevar= CTX_wm_region(C);
//
//	sa= prevsa;
//	if(sa.spacetype != SPACE_VIEW3D) {
//		for(sa=sc.areabase.first; sa; sa= sa.next)
//			if(sa.spacetype==SPACE_VIEW3D)
//				break;
//	}
//
//	if(!sa)
//		return OPERATOR_CANCELLED;
//
//	for(ar=sa.regionbase.first; ar; ar=ar.next)
//		if(ar.regiontype == RGN_TYPE_WINDOW)
//			break;
//
//	if(!ar)
//		return OPERATOR_CANCELLED;
//
//	// bad context switch ..
//	CTX_wm_area_set(C, sa);
//	CTX_wm_region_set(C, ar);
//
//	view3d_operator_needs_opengl(C);
//
//	SaveState(C);
//	StartKetsjiShell(C, ar, 1);
//	RestoreState(C);
//
//	CTX_wm_region_set(C, prevar);
//	CTX_wm_area_set(C, prevsa);
//
//	//XXX restore_all_scene_cfra(scene_cfra_store);
//	set_scene_bg(startscene);
//	//XXX scene_update_for_newframe(G.scene, G.scene.lay);
//
//#else
//	printf("GameEngine Disabled\n");
//#endif
//	ED_area_tag_redraw(CTX_wm_area(C));
//	return OPERATOR_FINISHED;
//}
//
//void VIEW3D_OT_game_start(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Start Game Engine";
//	ot.idname= "VIEW3D_OT_game_start";
//
//	/* api callbacks */
//	ot.exec= game_engine_exec;
//
//	//ot.poll= ED_operator_view3d_active;
//}
//
///* ************************************** */
//
//void view3d_align_axis_to_vector(View3D *v3d, RegionView3D *rv3d, int axisidx, float vec[3])
//{
//	float alignaxis[3] = {0.0, 0.0, 0.0};
//	float norm[3], axis[3], angle, new_quat[4];
//
//	if(axisidx > 0) alignaxis[axisidx-1]= 1.0;
//	else alignaxis[-axisidx-1]= -1.0;
//
//	VECCOPY(norm, vec);
//	Normalize(norm);
//
//	angle= (float)acos(Inpf(alignaxis, norm));
//	Crossf(axis, alignaxis, norm);
//	VecRotToQuat(axis, -angle, new_quat);
//
//	rv3d.view= 0;
//
//	if (rv3d.persp==V3D_CAMOB && v3d.camera) {
//		/* switch out of camera view */
//		float orig_ofs[3];
//		float orig_dist= rv3d.dist;
//		float orig_lens= v3d.lens;
//
//		VECCOPY(orig_ofs, rv3d.ofs);
//		rv3d.persp= V3D_PERSP;
//		rv3d.dist= 0.0;
//		view_settings_from_ob(v3d.camera, rv3d.ofs, NULL, NULL, &v3d.lens);
//		smooth_view(NULL, NULL, NULL, orig_ofs, new_quat, &orig_dist, &orig_lens); // XXX
//	} else {
//		if (rv3d.persp==V3D_CAMOB) rv3d.persp= V3D_PERSP; /* switch out of camera mode */
//		smooth_view(NULL, NULL, NULL, NULL, new_quat, NULL, NULL); // XXX
//	}
//}
}

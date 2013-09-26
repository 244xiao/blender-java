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

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import java.util.Arrays;

import blender.blenkernel.Blender;
import blender.blenkernel.Global;
import blender.blenkernel.ObjectUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenOps;
import blender.editors.transform.TransformManipulator;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.UserDefTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.BoundBox;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmOperator;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaDefine;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmOperators.OpFunc;
import blender.windowmanager.WmTypes.wmEvent;

public class View3dEdit {

/* ********************** view3d_edit: view manipulations ********************* */

/* ********************* box view support ***************** */

static void view3d_boxview_clip(ScrArea sa)
{
	ARegion ar;
	BoundBox bb = new BoundBox();
	float[][] clip=new float[6][4];
	float x1= 0.0f, y1= 0.0f, z1= 0.0f;
        float[] ofs=new float[3];
	int val;

	/* create bounding box */
	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.regiontype==ScreenTypes.RGN_TYPE_WINDOW) {
			RegionView3D rv3d= (RegionView3D)ar.regiondata;

			if((rv3d.viewlock & View3dTypes.RV3D_BOXCLIP)!=0) {
				if(rv3d.view==View3dTypes.V3D_VIEW_TOP || rv3d.view==View3dTypes.V3D_VIEW_BOTTOM) {
					if(ar.winx>ar.winy) x1= rv3d.dist;
					else x1= ar.winx*rv3d.dist/ar.winy;

					if(ar.winx>ar.winy) y1= ar.winy*rv3d.dist/ar.winx;
					else y1= rv3d.dist;

					ofs[0]= rv3d.ofs[0];
					ofs[1]= rv3d.ofs[1];
				}
				else if(rv3d.view==View3dTypes.V3D_VIEW_FRONT || rv3d.view==View3dTypes.V3D_VIEW_BACK) {
					ofs[2]= rv3d.ofs[2];

					if(ar.winx>ar.winy) z1= ar.winy*rv3d.dist/ar.winx;
					else z1= rv3d.dist;
				}
			}
		}
	}

	for(val=0; val<8; val++) {
		if(val==0 || val==3 || val==4 || val==7)
			bb.vec[val][0]= -x1 - ofs[0];
		else
			bb.vec[val][0]=  x1 - ofs[0];

		if(val==0 || val==1 || val==4 || val==5)
			bb.vec[val][1]= -y1 - ofs[1];
		else
			bb.vec[val][1]=  y1 - ofs[1];

		if(val > 3)
			bb.vec[val][2]= -z1 - ofs[2];
		else
			bb.vec[val][2]=  z1 - ofs[2];
	}

	/* normals for plane equations */
	Arithb.CalcNormFloat(bb.vec[0], bb.vec[1], bb.vec[4], clip[0]);
	Arithb.CalcNormFloat(bb.vec[1], bb.vec[2], bb.vec[5], clip[1]);
	Arithb.CalcNormFloat(bb.vec[2], bb.vec[3], bb.vec[6], clip[2]);
	Arithb.CalcNormFloat(bb.vec[3], bb.vec[0], bb.vec[7], clip[3]);
	Arithb.CalcNormFloat(bb.vec[4], bb.vec[5], bb.vec[6], clip[4]);
	Arithb.CalcNormFloat(bb.vec[0], bb.vec[2], bb.vec[1], clip[5]);

	/* then plane equations */
	for(val=0; val<5; val++) {
		clip[val][3]= - clip[val][0]*bb.vec[val][0] - clip[val][1]*bb.vec[val][1] - clip[val][2]*bb.vec[val][2];
	}
	clip[5][3]= - clip[5][0]*bb.vec[0][0] - clip[5][1]*bb.vec[0][1] - clip[5][2]*bb.vec[0][2];

	/* create bounding box */
	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.regiontype==ScreenTypes.RGN_TYPE_WINDOW) {
			RegionView3D rv3d= (RegionView3D)ar.regiondata;

			if((rv3d.viewlock & View3dTypes.RV3D_BOXCLIP)!=0) {
				rv3d.rflag |= View3dTypes.RV3D_CLIPPING;
//				memcpy(rv3d.clip, clip, sizeof(clip));
                                for (int i=0; i<rv3d.clip.length; i++)
                                        rv3d.clip[i] = Arrays.copyOf(clip[i], clip[i].length);
			}
		}
	}
//	MEM_freeN(bb);
}

/* sync center/zoom view of region to others, for view transforms */
static void view3d_boxview_sync(ScrArea sa, ARegion ar)
{
	ARegion artest;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;

	for(artest= (ARegion)sa.regionbase.first; artest!=null; artest= artest.next) {
		if(artest!=ar && artest.regiontype==ScreenTypes.RGN_TYPE_WINDOW) {
			RegionView3D rv3dtest= (RegionView3D)artest.regiondata;

			if(rv3dtest.viewlock!=0) {
				rv3dtest.dist= rv3d.dist;

				if(rv3d.view==View3dTypes.V3D_VIEW_TOP || rv3d.view==View3dTypes.V3D_VIEW_BOTTOM) {
					if(rv3dtest.view==View3dTypes.V3D_VIEW_FRONT || rv3dtest.view==View3dTypes.V3D_VIEW_BACK)
						rv3dtest.ofs[0]= rv3d.ofs[0];
					else if(rv3dtest.view==View3dTypes.V3D_VIEW_RIGHT || rv3dtest.view==View3dTypes.V3D_VIEW_LEFT)
						rv3dtest.ofs[1]= rv3d.ofs[1];
				}
				else if(rv3d.view==View3dTypes.V3D_VIEW_FRONT || rv3d.view==View3dTypes.V3D_VIEW_BACK) {
					if(rv3dtest.view==View3dTypes.V3D_VIEW_TOP || rv3dtest.view==View3dTypes.V3D_VIEW_BOTTOM)
						rv3dtest.ofs[0]= rv3d.ofs[0];
					else if(rv3dtest.view==View3dTypes.V3D_VIEW_RIGHT || rv3dtest.view==View3dTypes.V3D_VIEW_LEFT)
						rv3dtest.ofs[2]= rv3d.ofs[2];
				}
				else if(rv3d.view==View3dTypes.V3D_VIEW_RIGHT || rv3d.view==View3dTypes.V3D_VIEW_LEFT) {
					if(rv3dtest.view==View3dTypes.V3D_VIEW_TOP || rv3dtest.view==View3dTypes.V3D_VIEW_BOTTOM)
						rv3dtest.ofs[1]= rv3d.ofs[1];
					if(rv3dtest.view==View3dTypes.V3D_VIEW_FRONT || rv3dtest.view==View3dTypes.V3D_VIEW_BACK)
						rv3dtest.ofs[2]= rv3d.ofs[2];
				}

				Area.ED_region_tag_redraw(artest);
			}
		}
	}
	view3d_boxview_clip(sa);
}

/* for home, center etc */
public static void view3d_boxview_copy(ScrArea sa, ARegion ar)
{
	ARegion artest;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;

	for(artest= (ARegion)sa.regionbase.first; artest!=null; artest= artest.next) {
		if(artest!=ar && artest.regiontype==ScreenTypes.RGN_TYPE_WINDOW) {
			RegionView3D rv3dtest= (RegionView3D)artest.regiondata;

			if(rv3dtest.viewlock!=0) {
				rv3dtest.dist= rv3d.dist;
				UtilDefines.VECCOPY(rv3dtest.ofs, rv3d.ofs);
				Area.ED_region_tag_redraw(artest);
			}
		}
	}
	view3d_boxview_clip(sa);
}

/* ************************** init for view ops **********************************/

public static class ViewOpsData {
	public ScrArea sa;
	public ARegion ar;
	public RegionView3D rv3d;

	public float[] oldquat=new float[4];
	public float[] trackvec=new float[3];
	public float[] ofs=new float[3], obofs=new float[3];
	public float reverse, dist0;
	public float grid, far;

	public int origx, origy, oldx, oldy;
	public int origkey;
};

public static final float TRACKBALLSIZE=  1.1f;

static void calctrackballvec(rcti rect, int mx, int my, float[] vec)
{
	float x, y, radius, d, z, t;

	radius= TRACKBALLSIZE;

	/* normalize x and y */
	x= (rect.xmax + rect.xmin)/2 - mx;
	x/= (float)((rect.xmax - rect.xmin)/4);
	y= (rect.ymax + rect.ymin)/2 - my;
	y/= (float)((rect.ymax - rect.ymin)/2);

	d = (float)StrictMath.sqrt(x*x + y*y);
	if (d < radius*Arithb.M_SQRT1_2)  	/* Inside sphere */
		z = (float)StrictMath.sqrt(radius*radius - d*d);
	else
	{ 			/* On hyperbola */
		t = radius / Arithb.M_SQRT2;
		z = t*t / d;
	}

	vec[0]= x;
	vec[1]= y;
	vec[2]= -z;		/* yah yah! */
}


static void viewops_data(bContext C, wmOperator op, wmEvent event)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d;
	ViewOpsData vod= new ViewOpsData();

	/* store data */
	op.customdata= vod;
	vod.sa= bContext.CTX_wm_area(C);
	vod.ar= bContext.CTX_wm_region(C);
	vod.rv3d= rv3d= (RegionView3D)vod.ar.regiondata;
	vod.dist0= rv3d.dist;
	UtilDefines.QUATCOPY(vod.oldquat, rv3d.viewquat);
	vod.origx= vod.oldx= event.x;
	vod.origy= vod.oldy= event.y;
	vod.origkey= event.type;

	/* lookup, we dont pass on v3d to prevent confusement */
	vod.grid= v3d.grid;
	vod.far= v3d.far;

	calctrackballvec(vod.ar.winrct, event.x, event.y, vod.trackvec);

	View3dView.initgrabz(rv3d, -rv3d.ofs[0], -rv3d.ofs[1], -rv3d.ofs[2]);

	vod.reverse= 1.0f;
	if (rv3d.persmat[2][1] < 0.0f)
		vod.reverse= -1.0f;

}

///* ************************** viewrotate **********************************/
//
//static const float thres = 0.93f; //cos(20 deg);
//
//#define COS45 0.70710678118654746
//#define SIN45 COS45
//
//static float snapquats[39][6] = {
//	/*{q0, q1, q3, q4, view, oposite_direction}*/
//{COS45, -SIN45, 0.0, 0.0, V3D_VIEW_FRONT, 0},  //front
//{0.0, 0.0, -SIN45, -SIN45, V3D_VIEW_BACK, 0}, //back
//{1.0, 0.0, 0.0, 0.0, V3D_VIEW_TOP, 0},       //top
//{0.0, -1.0, 0.0, 0.0, V3D_VIEW_BOTTOM, 0},      //bottom
//{0.5, -0.5, -0.5, -0.5, V3D_VIEW_LEFT, 0},    //left
//{0.5, -0.5, 0.5, 0.5, V3D_VIEW_RIGHT, 0},      //right
//
//	/* some more 45 deg snaps */
//{0.65328145027160645, -0.65328145027160645, 0.27059805393218994, 0.27059805393218994, 0, 0},
//{0.92387950420379639, 0.0, 0.0, 0.38268342614173889, 0, 0},
//{0.0, -0.92387950420379639, 0.38268342614173889, 0.0, 0, 0},
//{0.35355335474014282, -0.85355335474014282, 0.35355338454246521, 0.14644660055637360, 0, 0},
//{0.85355335474014282, -0.35355335474014282, 0.14644660055637360, 0.35355338454246521, 0, 0},
//{0.49999994039535522, -0.49999994039535522, 0.49999997019767761, 0.49999997019767761, 0, 0},
//{0.27059802412986755, -0.65328145027160645, 0.65328145027160645, 0.27059802412986755, 0, 0},
//{0.65328145027160645, -0.27059802412986755, 0.27059802412986755, 0.65328145027160645, 0, 0},
//{0.27059799432754517, -0.27059799432754517, 0.65328139066696167, 0.65328139066696167, 0, 0},
//{0.38268336653709412, 0.0, 0.0, 0.92387944459915161, 0, 0},
//{0.0, -0.38268336653709412, 0.92387944459915161, 0.0, 0, 0},
//{0.14644658565521240, -0.35355335474014282, 0.85355335474014282, 0.35355335474014282, 0, 0},
//{0.35355335474014282, -0.14644658565521240, 0.35355335474014282, 0.85355335474014282, 0, 0},
//{0.0, 0.0, 0.92387944459915161, 0.38268336653709412, 0, 0},
//{-0.0, 0.0, 0.38268336653709412, 0.92387944459915161, 0, 0},
//{-0.27059802412986755, 0.27059802412986755, 0.65328133106231689, 0.65328133106231689, 0, 0},
//{-0.38268339633941650, 0.0, 0.0, 0.92387938499450684, 0, 0},
//{0.0, 0.38268339633941650, 0.92387938499450684, 0.0, 0, 0},
//{-0.14644658565521240, 0.35355338454246521, 0.85355329513549805, 0.35355332493782043, 0, 0},
//{-0.35355338454246521, 0.14644658565521240, 0.35355332493782043, 0.85355329513549805, 0, 0},
//{-0.49999991059303284, 0.49999991059303284, 0.49999985098838806, 0.49999985098838806, 0, 0},
//{-0.27059799432754517, 0.65328145027160645, 0.65328139066696167, 0.27059799432754517, 0, 0},
//{-0.65328145027160645, 0.27059799432754517, 0.27059799432754517, 0.65328139066696167, 0, 0},
//{-0.65328133106231689, 0.65328133106231689, 0.27059793472290039, 0.27059793472290039, 0, 0},
//{-0.92387932538986206, 0.0, 0.0, 0.38268333673477173, 0, 0},
//{0.0, 0.92387932538986206, 0.38268333673477173, 0.0, 0, 0},
//{-0.35355329513549805, 0.85355329513549805, 0.35355329513549805, 0.14644657075405121, 0, 0},
//{-0.85355329513549805, 0.35355329513549805, 0.14644657075405121, 0.35355329513549805, 0, 0},
//{-0.38268330693244934, 0.92387938499450684, 0.0, 0.0, 0, 0},
//{-0.92387938499450684, 0.38268330693244934, 0.0, 0.0, 0, 0},
//{-COS45, 0.0, 0.0, SIN45, 0, 0},
//{COS45, 0.0, 0.0, SIN45, 0, 0},
//{0.0, 0.0, 0.0, 1.0, 0, 0}
//};


static void viewrotate_apply(ViewOpsData vod, int x, int y, int ctrl)
{
	RegionView3D rv3d= vod.rv3d;
	boolean use_sel= false;	/* XXX */

	rv3d.view= 0; /* need to reset everytime because of view snapping */

	if ((U.flag & UserDefTypes.USER_TRACKBALL)!=0) {
		float phi, si;
                float[] q1=new float[4], dvec=new float[3], newvec=new float[3];

		calctrackballvec(vod.ar.winrct, x, y, newvec);

		Arithb.VecSubf(dvec, newvec, vod.trackvec);

		si= (float)StrictMath.sqrt(dvec[0]*dvec[0]+ dvec[1]*dvec[1]+ dvec[2]*dvec[2]);
		si/= (2.0*TRACKBALLSIZE);

                float[] q1v = new float[3];
		Arithb.Crossf(q1v, vod.trackvec, newvec);
		Arithb.Normalize(q1v);
                q1[1] = q1v[0];
                q1[2] = q1v[1];
                q1[3] = q1v[2];

		/* Allow for rotation beyond the interval
			* [-pi, pi] */
		while (si > 1.0)
			si -= 2.0;

		/* This relation is used instead of
			* phi = asin(si) so that the angle
			* of rotation is linearly proportional
			* to the distance that the mouse is
			* dragged. */
		phi = si * Arithb.M_PI / 2.0f;

		si= (float)StrictMath.sin(phi);
		q1[0]= (float)StrictMath.cos(phi);
		q1[1]*= si;
		q1[2]*= si;
		q1[3]*= si;
		Arithb.QuatMul(rv3d.viewquat, q1, vod.oldquat);

		if (use_sel) {
			/* compute the post multiplication quat, to rotate the offset correctly */
			UtilDefines.QUATCOPY(q1, vod.oldquat);
			Arithb.QuatConj(q1);
			Arithb.QuatMul(q1, q1, rv3d.viewquat);

			Arithb.QuatConj(q1); /* conj == inv for unit quat */
			UtilDefines.VECCOPY(rv3d.ofs, vod.ofs);
			Arithb.VecSubf(rv3d.ofs, rv3d.ofs, vod.obofs);
			Arithb.QuatMulVecf(q1, rv3d.ofs);
			Arithb.VecAddf(rv3d.ofs, rv3d.ofs, vod.obofs);
		}
	}
	else {
		/* New turntable view code by John Aughey */
		float si, phi;
                float[] q1=new float[4];
		float[][] m=new float[3][3];
		float[][] m_inv=new float[3][3];
		float[] xvec = {1.0f, 0.0f, 0.0f};
		/* Sensitivity will control how fast the viewport rotates.  0.0035 was
			obtained experimentally by looking at viewport rotation sensitivities
			on other modeling programs. */
		/* Perhaps this should be a configurable user parameter. */
		final float sensitivity = 0.0035f;

		/* Get the 3x3 matrix and its inverse from the quaternion */
		Arithb.QuatToMat3(rv3d.viewquat, m);
		Arithb.Mat3Inv(m_inv,m);

		/* Determine the direction of the x vector (for rotating up and down) */
		/* This can likely be compuated directly from the quaternion. */
		Arithb.Mat3MulVecfl(m_inv,xvec);

		/* Perform the up/down rotation */
		phi = sensitivity * -(y - vod.oldy);
		si = (float) StrictMath.sin(phi);
		q1[0] = (float) StrictMath.cos(phi);
		q1[1] = si * xvec[0];
		q1[2] = si * xvec[1];
		q1[3] = si * xvec[2];
		Arithb.QuatMul(rv3d.viewquat, rv3d.viewquat, q1);

		if (use_sel) {
			Arithb.QuatConj(q1); /* conj == inv for unit quat */
			Arithb.VecSubf(rv3d.ofs, rv3d.ofs, vod.obofs);
			Arithb.QuatMulVecf(q1, rv3d.ofs);
			Arithb.VecAddf(rv3d.ofs, rv3d.ofs, vod.obofs);
		}

		/* Perform the orbital rotation */
		phi = sensitivity * vod.reverse * (x - vod.oldx);
		q1[0] = (float) StrictMath.cos(phi);
		q1[1] = q1[2] = 0.0f;
		q1[3] = (float) StrictMath.sin(phi);
		Arithb.QuatMul(rv3d.viewquat, rv3d.viewquat, q1);

		if (use_sel) {
			Arithb.QuatConj(q1);
			Arithb.VecSubf(rv3d.ofs, rv3d.ofs, vod.obofs);
			Arithb.QuatMulVecf(q1, rv3d.ofs);
			Arithb.VecAddf(rv3d.ofs, rv3d.ofs, vod.obofs);
		}
	}

//	/* check for view snap */
//	if (ctrl){
//		int i;
//		float viewmat[3][3];
//
//
//		QuatToMat3(rv3d.viewquat, viewmat);
//
//		for (i = 0 ; i < 39; i++){
//			float snapmat[3][3];
//			float view = (int)snapquats[i][4];
//
//			QuatToMat3(snapquats[i], snapmat);
//
//			if ((Inpf(snapmat[0], viewmat[0]) > thres) &&
//				(Inpf(snapmat[1], viewmat[1]) > thres) &&
//				(Inpf(snapmat[2], viewmat[2]) > thres)){
//
//				QUATCOPY(rv3d.viewquat, snapquats[i]);
//
//				rv3d.view = view;
//
//				break;
//			}
//		}
//	}
	vod.oldx= x;
	vod.oldy= y;

	Area.ED_region_tag_redraw(vod.ar);
}

public static wmOperatorType.Operator viewrotate_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewrotate_modal(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("viewrotate_modal");
	ViewOpsData vod= (ViewOpsData)op.customdata;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			viewrotate_apply(vod, event.x, event.y, event.ctrl);
			break;

		default:
			/* origkey may be zero when invoked from a button */
			if((event.type==WmEventTypes.ESCKEY || event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.RIGHTMOUSE) || (event.type==vod.origkey && event.val==WmTypes.KM_RELEASE)) {
				View3dView.request_depth_update(bContext.CTX_wm_region_view3d(C));

//				MEM_freeN(vod);
				op.customdata= null;

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator viewrotate_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewrotate_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("viewrotate_invoke");
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	ViewOpsData vod;

	if(rv3d.viewlock!=0)
		return WindowManagerTypes.OPERATOR_CANCELLED;

	/* makes op.customdata */
	viewops_data(C, op, event);
	vod= (ViewOpsData)op.customdata;

	/* switch from camera view when: */
	if(vod.rv3d.persp != View3dTypes.V3D_PERSP) {

		if ((U.uiflag & UserDefTypes.USER_AUTOPERSP)!=0)
			vod.rv3d.persp= View3dTypes.V3D_PERSP;
		else if(vod.rv3d.persp==View3dTypes.V3D_CAMOB)
			vod.rv3d.persp= View3dTypes.V3D_PERSP;
		Area.ED_region_tag_redraw(vod.ar);
	}

	/* add temp handler */
//	WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
	WmEventSystem.WM_event_add_modal_handler(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc VIEW3D_OT_viewrotate = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_viewrotate(wmOperatorType *ot)
{

	/* identifiers */
	ot.name= "Rotate view";
	ot.description = "Rotate the view.";
	ot.idname= "VIEW3D_OT_viewrotate";

	/* api callbacks */
	ot.invoke= viewrotate_invoke;
	ot.modal= viewrotate_modal;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_BLOCKING;
}};

/* ************************ viewmove ******************************** */

static void viewmove_apply(ViewOpsData vod, int x, int y)
{
	if(vod.rv3d.persp==View3dTypes.V3D_CAMOB) {
		float max= (float)UtilDefines.MAX2(vod.ar.winx, vod.ar.winy);

		vod.rv3d.camdx += (vod.oldx - x)/(max);
		vod.rv3d.camdy += (vod.oldy - y)/(max);
		vod.rv3d.camdx = Arithb.CLAMP(vod.rv3d.camdx, -1.0f, 1.0f);
		vod.rv3d.camdy = Arithb.CLAMP(vod.rv3d.camdy, -1.0f, 1.0f);
// XXX		preview3d_event= 0;
	}
	else {
		float[] dvec=new float[3];

		View3dView.window_to_3d_delta(vod.ar, dvec, x-vod.oldx, y-vod.oldy);
		Arithb.VecAddf(vod.rv3d.ofs, vod.rv3d.ofs, dvec);

		if((vod.rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
			view3d_boxview_sync(vod.sa, vod.ar);
	}

	vod.oldx= x;
	vod.oldy= y;

	Area.ED_region_tag_redraw(vod.ar);
}

public static wmOperatorType.Operator viewmove_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewmove_modal(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("viewmove_modal");
	ViewOpsData vod= (ViewOpsData)op.customdata;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			viewmove_apply(vod, event.x, event.y);
			break;

		default:
			/* origkey may be zero when invoked from a button */
			if((event.type==WmEventTypes.ESCKEY || event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.RIGHTMOUSE) || (event.type==vod.origkey && event.val==WmTypes.KM_RELEASE)) {
				View3dView.request_depth_update(bContext.CTX_wm_region_view3d(C));

//				MEM_freeN(vod);
				op.customdata= null;

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator viewmove_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewmove_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("viewmove_invoke");
	/* makes op.customdata */
	viewops_data(C, op, event);

	/* add temp handler */
//	WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
	WmEventSystem.WM_event_add_modal_handler(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc VIEW3D_OT_viewmove = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_viewmove(wmOperatorType *ot)
{

	/* identifiers */
	ot.name= "Move view";
	ot.description = "Move the view.";
	ot.idname= "VIEW3D_OT_viewmove";

	/* api callbacks */
	ot.invoke= viewmove_invoke;
	ot.modal= viewmove_modal;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_BLOCKING;
}};

/* ************************ viewzoom ******************************** */

static void view_zoom_mouseloc(ARegion ar, float dfac, int mx, int my)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;

	if((U.uiflag & UserDefTypes.USER_ZOOM_TO_MOUSEPOS)!=0) {
		float[] dvec=new float[3];
		float[] tvec=new float[3];
		float[] tpos=new float[3];
		float new_dist;
		short[] vb=new short[2], mouseloc=new short[2];

		mouseloc[0]= (short)(mx - ar.winrct.xmin);
		mouseloc[1]= (short)(my - ar.winrct.ymin);

		/* find the current window width and height */
		vb[0] = ar.winx;
		vb[1] = ar.winy;

		tpos[0] = -rv3d.ofs[0];
		tpos[1] = -rv3d.ofs[1];
		tpos[2] = -rv3d.ofs[2];

		/* Project cursor position into 3D space */
		View3dView.initgrabz(rv3d, tpos[0], tpos[1], tpos[2]);
		View3dView.window_to_3d_delta(ar, dvec, mouseloc[0]-vb[0]/2, mouseloc[1]-vb[1]/2);

		/* Calculate view target position for dolly */
		tvec[0] = -(tpos[0] + dvec[0]);
		tvec[1] = -(tpos[1] + dvec[1]);
		tvec[2] = -(tpos[2] + dvec[2]);

		/* Offset to target position and dolly */
		new_dist = rv3d.dist * dfac;

		UtilDefines.VECCOPY(rv3d.ofs, tvec);
		rv3d.dist = new_dist;

		/* Calculate final offset */
		dvec[0] = tvec[0] + dvec[0] * dfac;
		dvec[1] = tvec[1] + dvec[1] * dfac;
		dvec[2] = tvec[2] + dvec[2] * dfac;

		UtilDefines.VECCOPY(rv3d.ofs, dvec);
	} else {
		rv3d.dist *= dfac;
	}
}


static void viewzoom_apply(ViewOpsData vod, int x, int y)
{
        System.out.println("viewzoom_apply");
	float zfac=1.0f;

	if(U.viewzoom==UserDefTypes.USER_ZOOM_CONT) {
		// oldstyle zoom
		zfac = 1.0f+(float)(vod.origx - x + vod.origy - y)/1000.0f;
	}
	else if(U.viewzoom==UserDefTypes.USER_ZOOM_SCALE) {
		int[] ctr=new int[2];
                int len1, len2;
		// method which zooms based on how far you move the mouse

		ctr[0] = (vod.ar.winrct.xmax + vod.ar.winrct.xmin)/2;
		ctr[1] = (vod.ar.winrct.ymax + vod.ar.winrct.ymin)/2;

		len1 = (int)StrictMath.sqrt((ctr[0] - x)*(ctr[0] - x) + (ctr[1] - y)*(ctr[1] - y)) + 5;
		len2 = (int)StrictMath.sqrt((ctr[0] - vod.origx)*(ctr[0] - vod.origx) + (ctr[1] - vod.origy)*(ctr[1] - vod.origy)) + 5;

		zfac = vod.dist0 * ((float)len2/len1) / vod.rv3d.dist;
	}
	else {	/* USER_ZOOM_DOLLY */
		float len1 = (vod.ar.winrct.ymax - y) + 5;
		float len2 = (vod.ar.winrct.ymax - vod.origy) + 5;
		zfac = vod.dist0 * (2.0f*((len2/len1)-1.0f) + 1.0f) / vod.rv3d.dist;
	}

	if(zfac != 1.0f && zfac*vod.rv3d.dist > 0.001f*vod.grid &&
				zfac*vod.rv3d.dist < 10.0f*vod.far)
		view_zoom_mouseloc(vod.ar, zfac, vod.oldx, vod.oldy);


	if ((U.uiflag & UserDefTypes.USER_ORBIT_ZBUF)!=0 && (U.viewzoom==UserDefTypes.USER_ZOOM_CONT) && (vod.rv3d.persp==View3dTypes.V3D_PERSP)) {
		float[] upvec=new float[3];
                float[][] mat=new float[3][3];

		/* Secret apricot feature, translate the view when in continues mode */
		upvec[0] = upvec[1] = 0.0f;
		upvec[2] = (vod.dist0 - vod.rv3d.dist) * vod.grid;
		vod.rv3d.dist = vod.dist0;
		Arithb.Mat3CpyMat4(mat, vod.rv3d.viewinv);
		Arithb.Mat3MulVecfl(mat, upvec);
		Arithb.VecAddf(vod.rv3d.ofs, vod.rv3d.ofs, upvec);
	} else {
		/* these limits were in old code too */
		if(vod.rv3d.dist<0.001f*vod.grid)
                    vod.rv3d.dist= 0.001f*vod.grid;
		if(vod.rv3d.dist>10.0f*vod.far)
                    vod.rv3d.dist=10.0f*vod.far;
	}

// XXX	if(vod.rv3d.persp==V3D_ORTHO || vod.rv3d.persp==V3D_CAMOB) preview3d_event= 0;

	if((vod.rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
		view3d_boxview_sync(vod.sa, vod.ar);

	Area.ED_region_tag_redraw(vod.ar);
}

public static wmOperatorType.Operator viewzoom_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewzoom_modal(bContext C, wmOperator op, wmEvent event)
{
        System.out.println("viewzoom_modal");
	ViewOpsData vod= (ViewOpsData)op.customdata;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			viewzoom_apply(vod, event.x, event.y);
			break;

		default:
			/* origkey may be zero when invoked from a button */
			if((event.type==WmEventTypes.ESCKEY || event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.RIGHTMOUSE) || (event.type==vod.origkey && event.val==WmTypes.KM_RELEASE)) {
				View3dView.request_depth_update(bContext.CTX_wm_region_view3d(C));

//				MEM_freeN(vod);
				op.customdata= null;

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator viewzoom_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent unused)
//static int viewzoom_exec(bContext *C, wmOperator *op)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	int delta= RnaAccess.RNA_int_get(op.ptr, "delta");
        System.out.println("viewzoom_exec: "+delta);

	if(delta < 0) {
		/* this min and max is also in viewmove() */
		if(rv3d.persp==View3dTypes.V3D_CAMOB) {
			rv3d.camzoom-= 10;
			if(rv3d.camzoom<-30) rv3d.camzoom= -30;
		}
		else if(rv3d.dist<10.0f*v3d.far) rv3d.dist*=1.2f;
	}
	else {
		if(rv3d.persp==View3dTypes.V3D_CAMOB) {
			rv3d.camzoom+= 10;
			if(rv3d.camzoom>300) rv3d.camzoom= 300;
		}
		else if(rv3d.dist> 0.001f*v3d.grid) rv3d.dist*=.83333f;
	}

	if((rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
		view3d_boxview_sync(bContext.CTX_wm_area(C), bContext.CTX_wm_region(C));

	View3dView.request_depth_update(bContext.CTX_wm_region_view3d(C));
	Area.ED_region_tag_redraw(bContext.CTX_wm_region(C));

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static wmOperatorType.Operator viewzoom_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewzoom_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("viewzoom_invoke");
	int delta= RnaAccess.RNA_int_get(op.ptr, "delta");

	if(delta!=0) {
		viewzoom_exec.run(C, op, null);
	}
	else {
		/* makes op.customdata */
		viewops_data(C, op, event);

		/* add temp handler */
//		WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
		WmEventSystem.WM_event_add_modal_handler(C, op);

		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	}
	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_zoom = new OpFunc() {
public void run(wmOperatorType ot)
//public static void VIEW3D_OT_zoom(wmOperatorType ot)
{
//        System.out.println("VIEW3D_OT_zoom");
	/* identifiers */
	ot.name= "Zoom view";
	ot.description = "Zoom in/out in the view.";
	ot.idname= "VIEW3D_OT_zoom";

	/* api callbacks */
	ot.invoke= viewzoom_invoke;
	ot.exec= viewzoom_exec;
	ot.modal= viewzoom_modal;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_BLOCKING;

	RnaDefine.RNA_def_int(ot.srna, "delta", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Delta", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
}};

public static wmOperatorType.Operator viewhome_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewhome_exec(bContext *C, wmOperator *op) /* was view3d_home() in 2.4x */
{
	ARegion ar= bContext.CTX_wm_region(C);
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	Scene scene= bContext.CTX_data_scene(C);
	Base base;

	boolean center= RnaAccess.RNA_boolean_get(op.ptr, "center");

	float size;
        float[] min=new float[3], max=new float[3], afm=new float[3];
	int ok= 1, onedone=0;

	if(center) {
		min[0]= min[1]= min[2]= 0.0f;
		max[0]= max[1]= max[2]= 0.0f;
	}
	else {
		UtilDefines.INIT_MINMAX(min, max);
	}

	for(base= (Base)scene.base.first; base!=null; base= base.next) {
		if(base.lay!=0 & v3d.lay!=0) {
			onedone= 1;
			ObjectUtil.minmax_object(base.object, min, max);
		}
	}
	if(onedone==0) return WindowManagerTypes.OPERATOR_FINISHED; /* TODO - should this be cancel? */

	afm[0]= (max[0]-min[0]);
	afm[1]= (max[1]-min[1]);
	afm[2]= (max[2]-min[2]);
	size= 0.7f*UtilDefines.MAX3(afm[0], afm[1], afm[2]);
	if(size==0.0) ok= 0;

	if(ok!=0) {
		float[] new_dist={0};
		float[] new_ofs=new float[3];

		new_dist[0] = size;
		new_ofs[0]= -(min[0]+max[0])/2.0f;
		new_ofs[1]= -(min[1]+max[1])/2.0f;
		new_ofs[2]= -(min[2]+max[2])/2.0f;

		// correction for window aspect ratio
		if(ar.winy>2 && ar.winx>2) {
			size= (float)ar.winx/(float)ar.winy;
			if(size<1.0) size= 1.0f/size;
			new_dist[0]*= size;
		}

		if (rv3d.persp==View3dTypes.V3D_CAMOB) {
			rv3d.persp= View3dTypes.V3D_PERSP;
			View3dView.smooth_view(C, null, v3d.camera, new_ofs, null, new_dist, null);
		}
	}
// XXX	BIF_view3d_previewrender_signal(curarea, PR_DBASE|PR_DISPRECT);

	if((rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
		view3d_boxview_copy(bContext.CTX_wm_area(C), ar);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_view_all = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_view_all(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View home";
	ot.description = "View all objects in scene.";
	ot.idname= "VIEW3D_OT_view_all";

	/* api callbacks */
	ot.exec= viewhome_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;

	RnaDefine.RNA_def_boolean(ot.srna, "center", 0, "Center", "");
}};

public static wmOperatorType.Operator viewcenter_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewcenter_exec(bContext *C, wmOperator *op) /* like a localview without local!, was centerview() in 2.4x */
{
	ARegion ar= bContext.CTX_wm_region(C);
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	Scene scene= bContext.CTX_data_scene(C);
	bObject ob= (scene.basact!=null? scene.basact.object: null);
//	bObject obedit= bContext.CTX_data_edit_object(C);
        bObject obedit= null;
	float size;
        float[] min=new float[3], max=new float[3], afm=new float[3];
	int ok=0;

	/* SMOOTHVIEW */
	float[] new_ofs=new float[3];
	float[] new_dist={0};

	UtilDefines.INIT_MINMAX(min, max);

//	if (G.f & G_WEIGHTPAINT) {
//		/* hardcoded exception, we look for the one selected armature */
//		/* this is weak code this way, we should make a generic active/selection callback interface once... */
//		Base *base;
//		for(base=scene.base.first; base; base= base.next) {
//			if(TESTBASELIB(v3d, base)) {
//				if(base.object.type==OB_ARMATURE)
//					if(base.object.flag & OB_POSEMODE)
//						break;
//			}
//		}
//		if(base)
//			ob= base.object;
//	}


	if(obedit!=null) {
//		ok = minmax_verts(obedit, min, max);	/* only selected */
	}
	else if(ob!=null && (ob.flag & ObjectTypes.OB_POSEMODE)!=0) {
//		if(ob.pose) {
//			bArmature *arm= ob.data;
//			bPoseChannel *pchan;
//			float vec[3];
//
//			for(pchan= ob.pose.chanbase.first; pchan; pchan= pchan.next) {
//				if(pchan.bone.flag & BONE_SELECTED) {
//					if(pchan.bone.layer & arm.layer) {
//						ok= 1;
//						VECCOPY(vec, pchan.pose_head);
//						Mat4MulVecfl(ob.obmat, vec);
//						DO_MINMAX(vec, min, max);
//						VECCOPY(vec, pchan.pose_tail);
//						Mat4MulVecfl(ob.obmat, vec);
//						DO_MINMAX(vec, min, max);
//					}
//				}
//			}
//		}
	}
	else if (Global.FACESEL_PAINT_TEST()) {
//// XXX		ok= minmax_tface(min, max);
	}
	else if ((G.f & Global.G_PARTICLEEDIT)!=0) {
//		ok= PE_minmax(scene, min, max);
	}
	else {
		Base base= (Base)scene.base.first;
		while(base!=null) {
//			if(TESTBASE(v3d, base))  {
                        if((base.flag & Blender.SELECT)!=0 && (base.lay & v3d.lay)!=0 && ((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0))  {
				ObjectUtil.minmax_object(base.object, min, max);
				/* account for duplis */
//				minmax_object_duplis(scene, base.object, min, max);

				ok= 1;
			}
			base= base.next;
		}
	}

	if(ok==0) return WindowManagerTypes.OPERATOR_FINISHED;

	afm[0]= (max[0]-min[0]);
	afm[1]= (max[1]-min[1]);
	afm[2]= (max[2]-min[2]);
	size= UtilDefines.MAX3(afm[0], afm[1], afm[2]);
	/* perspective should be a bit farther away to look nice */
	if(rv3d.persp==View3dTypes.V3D_ORTHO)
		size*= 0.7;

	if(size <= v3d.near*1.5f) size= v3d.near*1.5f;

	new_ofs[0]= -(min[0]+max[0])/2.0f;
	new_ofs[1]= -(min[1]+max[1])/2.0f;
	new_ofs[2]= -(min[2]+max[2])/2.0f;

	new_dist[0] = size;

	/* correction for window aspect ratio */
	if(ar.winy>2 && ar.winx>2) {
		size= (float)ar.winx/(float)ar.winy;
		if(size<1.0f) size= 1.0f/size;
		new_dist[0]*= size;
	}

	v3d.cursor[0]= -new_ofs[0];
	v3d.cursor[1]= -new_ofs[1];
	v3d.cursor[2]= -new_ofs[2];

	if (rv3d.persp==View3dTypes.V3D_CAMOB) {
		rv3d.persp= View3dTypes.V3D_PERSP;
		View3dView.smooth_view(C, v3d.camera, null, new_ofs, null, new_dist, null);
	}
	else {
		View3dView.smooth_view(C, null, null, new_ofs, null, new_dist, null);
	}

// XXX	BIF_view3d_previewrender_signal(curarea, PR_DBASE|PR_DISPRECT);
	if((rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
		view3d_boxview_copy(bContext.CTX_wm_area(C), ar);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_view_center = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_view_center(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View center";
	ot.description = "Move the view to the selection center.";
	ot.idname= "VIEW3D_OT_view_center";

	/* api callbacks */
	ot.exec= viewcenter_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;
}};

///* ********************* Set render border operator ****************** */
//
//static int render_border_exec(bContext *C, wmOperator *op)
//{
//	View3D *v3d = CTX_wm_view3d(C);
//	ARegion *ar= CTX_wm_region(C);
//	Scene *scene= CTX_data_scene(C);
//
//	rcti rect;
//	rctf vb;
//
//	/* get border select values using rna */
//	rect.xmin= RNA_int_get(op.ptr, "xmin");
//	rect.ymin= RNA_int_get(op.ptr, "ymin");
//	rect.xmax= RNA_int_get(op.ptr, "xmax");
//	rect.ymax= RNA_int_get(op.ptr, "ymax");
//
//	/* calculate range */
//	calc_viewborder(scene, ar, v3d, &vb);
//
//	scene.r.border.xmin= ((float)rect.xmin-vb.xmin)/(vb.xmax-vb.xmin);
//	scene.r.border.ymin= ((float)rect.ymin-vb.ymin)/(vb.ymax-vb.ymin);
//	scene.r.border.xmax= ((float)rect.xmax-vb.xmin)/(vb.xmax-vb.xmin);
//	scene.r.border.ymax= ((float)rect.ymax-vb.ymin)/(vb.ymax-vb.ymin);
//
//	/* actually set border */
//	CLAMP(scene.r.border.xmin, 0.0, 1.0);
//	CLAMP(scene.r.border.ymin, 0.0, 1.0);
//	CLAMP(scene.r.border.xmax, 0.0, 1.0);
//	CLAMP(scene.r.border.ymax, 0.0, 1.0);
//
//	/* drawing a border surrounding the entire camera view switches off border rendering
//	 * or the border covers no pixels */
//	if ((scene.r.border.xmin <= 0.0 && scene.r.border.xmax >= 1.0 &&
//		scene.r.border.ymin <= 0.0 && scene.r.border.ymax >= 1.0) ||
//	   (scene.r.border.xmin == scene.r.border.xmax ||
//		scene.r.border.ymin == scene.r.border.ymax ))
//	{
//		scene.r.mode &= ~R_BORDER;
//	} else {
//		scene.r.mode |= R_BORDER;
//	}
//
//	return OPERATOR_FINISHED;
//
//}
//
//static int view3d_render_border_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//
//	/* if not in camera view do not exec the operator*/
//	if (rv3d.persp == V3D_CAMOB) return WM_border_select_invoke(C, op, event);
//	else return OPERATOR_PASS_THROUGH;
//}
//
//void VIEW3D_OT_render_border(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Set Render Border";
//	ot.description = "Set the boundries of the border render and enables border render .";
//	ot.idname= "VIEW3D_OT_render_border";
//
//	/* api callbacks */
//	ot.invoke= view3d_render_border_invoke;
//	ot.exec= render_border_exec;
//	ot.modal= WM_border_select_modal;
//
//	ot.poll= ED_operator_view3d_active;
//
//	/* flags */
//	ot.flag= OPTYPE_REGISTER|OPTYPE_UNDO;
//
//	/* rna */
//	RNA_def_int(ot.srna, "xmin", 0, INT_MIN, INT_MAX, "X Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "xmax", 0, INT_MIN, INT_MAX, "X Max", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymin", 0, INT_MIN, INT_MAX, "Y Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymax", 0, INT_MIN, INT_MAX, "Y Max", "", INT_MIN, INT_MAX);
//
//}
///* ********************* Border Zoom operator ****************** */
//
//static int view3d_zoom_border_exec(bContext *C, wmOperator *op)
//{
//	ARegion *ar= CTX_wm_region(C);
//	View3D *v3d = CTX_wm_view3d(C);
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//	Scene *scene= CTX_data_scene(C);
//
//	/* Zooms in on a border drawn by the user */
//	rcti rect;
//	float dvec[3], vb[2], xscale, yscale, scale;
//
//	/* SMOOTHVIEW */
//	float new_dist;
//	float new_ofs[3];
//
//	/* ZBuffer depth vars */
//	bglMats mats;
//	float depth, depth_close= MAXFLOAT;
//	int had_depth = 0;
//	double cent[2],  p[3];
//	int xs, ys;
//
//	/* note; otherwise opengl won't work */
//	view3d_operator_needs_opengl(C);
//
//	/* get border select values using rna */
//	rect.xmin= RNA_int_get(op.ptr, "xmin");
//	rect.ymin= RNA_int_get(op.ptr, "ymin");
//	rect.xmax= RNA_int_get(op.ptr, "xmax");
//	rect.ymax= RNA_int_get(op.ptr, "ymax");
//
//	/* Get Z Depths, needed for perspective, nice for ortho */
//	bgl_get_mats(&mats);
//	draw_depth(scene, ar, v3d, NULL);
//
//	/* force updating */
//	if (rv3d.depths) {
//		had_depth = 1;
//		rv3d.depths.damaged = 1;
//	}
//
//	view3d_update_depths(ar, v3d);
//
//	/* Constrain rect to depth bounds */
//	if (rect.xmin < 0) rect.xmin = 0;
//	if (rect.ymin < 0) rect.ymin = 0;
//	if (rect.xmax >= rv3d.depths.w) rect.xmax = rv3d.depths.w-1;
//	if (rect.ymax >= rv3d.depths.h) rect.ymax = rv3d.depths.h-1;
//
//	/* Find the closest Z pixel */
//	for (xs=rect.xmin; xs < rect.xmax; xs++) {
//		for (ys=rect.ymin; ys < rect.ymax; ys++) {
//			depth= rv3d.depths.depths[ys*rv3d.depths.w+xs];
//			if(depth < rv3d.depths.depth_range[1] && depth > rv3d.depths.depth_range[0]) {
//				if (depth_close > depth) {
//					depth_close = depth;
//				}
//			}
//		}
//	}
//
//	if (had_depth==0) {
//		MEM_freeN(rv3d.depths.depths);
//		rv3d.depths.depths = NULL;
//	}
//	rv3d.depths.damaged = 1;
//
//	cent[0] = (((double)rect.xmin)+((double)rect.xmax)) / 2;
//	cent[1] = (((double)rect.ymin)+((double)rect.ymax)) / 2;
//
//	if (rv3d.persp==V3D_PERSP) {
//		double p_corner[3];
//
//		/* no depths to use, we cant do anything! */
//		if (depth_close==MAXFLOAT){
//			BKE_report(op.reports, RPT_ERROR, "Depth Too Large");
//			return OPERATOR_CANCELLED;
//		}
//		/* convert border to 3d coordinates */
//		if ((	!gluUnProject(cent[0], cent[1], depth_close, mats.modelview, mats.projection, (GLint *)mats.viewport, &p[0], &p[1], &p[2])) ||
//			(	!gluUnProject((double)rect.xmin, (double)rect.ymin, depth_close, mats.modelview, mats.projection, (GLint *)mats.viewport, &p_corner[0], &p_corner[1], &p_corner[2])))
//			return OPERATOR_CANCELLED;
//
//		dvec[0] = p[0]-p_corner[0];
//		dvec[1] = p[1]-p_corner[1];
//		dvec[2] = p[2]-p_corner[2];
//
//		new_dist = VecLength(dvec);
//		if(new_dist <= v3d.near*1.5) new_dist= v3d.near*1.5;
//
//		new_ofs[0] = -p[0];
//		new_ofs[1] = -p[1];
//		new_ofs[2] = -p[2];
//
//	} else { /* othographic */
//		/* find the current window width and height */
//		vb[0] = ar.winx;
//		vb[1] = ar.winy;
//
//		new_dist = rv3d.dist;
//
//		/* convert the drawn rectangle into 3d space */
//		if (depth_close!=MAXFLOAT && gluUnProject(cent[0], cent[1], depth_close, mats.modelview, mats.projection, (GLint *)mats.viewport, &p[0], &p[1], &p[2])) {
//			new_ofs[0] = -p[0];
//			new_ofs[1] = -p[1];
//			new_ofs[2] = -p[2];
//		} else {
//			/* We cant use the depth, fallback to the old way that dosnt set the center depth */
//			new_ofs[0] = rv3d.ofs[0];
//			new_ofs[1] = rv3d.ofs[1];
//			new_ofs[2] = rv3d.ofs[2];
//
//			initgrabz(rv3d, -new_ofs[0], -new_ofs[1], -new_ofs[2]);
//
//			window_to_3d_delta(ar, dvec, (rect.xmin+rect.xmax-vb[0])/2, (rect.ymin+rect.ymax-vb[1])/2);
//			/* center the view to the center of the rectangle */
//			VecSubf(new_ofs, new_ofs, dvec);
//		}
//
//		/* work out the ratios, so that everything selected fits when we zoom */
//		xscale = ((rect.xmax-rect.xmin)/vb[0]);
//		yscale = ((rect.ymax-rect.ymin)/vb[1]);
//		scale = (xscale >= yscale)?xscale:yscale;
//
//		/* zoom in as required, or as far as we can go */
//		new_dist = ((new_dist*scale) >= 0.001*v3d.grid)? new_dist*scale:0.001*v3d.grid;
//	}
//
//	smooth_view(C, NULL, NULL, new_ofs, NULL, &new_dist, NULL);
//
//	if(rv3d.viewlock & RV3D_BOXVIEW)
//		view3d_boxview_sync(CTX_wm_area(C), ar);
//
//	return OPERATOR_FINISHED;
//}
//
//static int view3d_zoom_border_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//
//	/* if in camera view do not exec the operator so we do not conflict with set render border*/
//	if (rv3d.persp != V3D_CAMOB)
//		return WM_border_select_invoke(C, op, event);
//	else
//		return OPERATOR_PASS_THROUGH;
//}
//
//void VIEW3D_OT_zoom_border(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Border Zoom";
//	ot.description = "Zoom in the view to the nearest object contained in the border.";
//	ot.idname= "VIEW3D_OT_zoom_border";
//
//	/* api callbacks */
//	ot.invoke= view3d_zoom_border_invoke;
//	ot.exec= view3d_zoom_border_exec;
//	ot.modal= WM_border_select_modal;
//
//	ot.poll= ED_operator_view3d_active;
//
//	/* flags */
//	ot.flag= OPTYPE_REGISTER;
//
//	/* rna */
//	RNA_def_int(ot.srna, "xmin", 0, INT_MIN, INT_MAX, "X Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "xmax", 0, INT_MIN, INT_MAX, "X Max", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymin", 0, INT_MIN, INT_MAX, "Y Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymax", 0, INT_MIN, INT_MAX, "Y Max", "", INT_MIN, INT_MAX);
//
//}
/* ********************* Changing view operator ****************** */

static EnumPropertyItem[] prop_view_items = {
	new EnumPropertyItem(View3dTypes.V3D_VIEW_FRONT, "FRONT", 0, "Front", "View From the Front"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_BACK, "BACK", 0, "Back", "View From the Back"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_LEFT, "LEFT", 0, "Left", "View From the Left"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_RIGHT, "RIGHT", 0, "Right", "View From the Right"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_TOP, "TOP", 0, "Top", "View From the Top"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_BOTTOM, "BOTTOM", 0, "Bottom", "View From the Bottom"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_CAMERA, "CAMERA", 0, "Camera", "View From the active amera"),
	new EnumPropertyItem(0, null, 0, null, null)};

static void axis_set_view(bContext C, float q1, float q2, float q3, float q4, int view, int perspo)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	float[] new_quat=new float[4];

	if(rv3d.viewlock!=0) {
		/* only pass on if */
		if(rv3d.view==View3dTypes.V3D_VIEW_FRONT && view==View3dTypes.V3D_VIEW_BACK);
		else if(rv3d.view==View3dTypes.V3D_VIEW_BACK && view==View3dTypes.V3D_VIEW_FRONT);
		else if(rv3d.view==View3dTypes.V3D_VIEW_RIGHT && view==View3dTypes.V3D_VIEW_LEFT);
		else if(rv3d.view==View3dTypes.V3D_VIEW_LEFT && view==View3dTypes.V3D_VIEW_RIGHT);
		else if(rv3d.view==View3dTypes.V3D_VIEW_BOTTOM && view==View3dTypes.V3D_VIEW_TOP);
		else if(rv3d.view==View3dTypes.V3D_VIEW_TOP && view==View3dTypes.V3D_VIEW_BOTTOM);
		else return;
	}

	new_quat[0]= q1; new_quat[1]= q2;
	new_quat[2]= q3; new_quat[3]= q4;

	//rv3d.view= (short)view;
	rv3d.view= (byte)view;

	if(rv3d.viewlock!=0) {
		Area.ED_region_tag_redraw(bContext.CTX_wm_region(C));
		return;
	}

	if (rv3d.persp==View3dTypes.V3D_CAMOB && v3d.camera!=null) {

		if ((U.uiflag & UserDefTypes.USER_AUTOPERSP)!=0) rv3d.persp= View3dTypes.V3D_ORTHO;
		//else if(rv3d.persp==View3dTypes.V3D_CAMOB) rv3d.persp= (short)perspo;
		else if(rv3d.persp==View3dTypes.V3D_CAMOB) rv3d.persp= (byte)perspo;

		View3dView.smooth_view(C, v3d.camera, null, rv3d.ofs, new_quat, null, null);
	}
	else {

		if ((U.uiflag & UserDefTypes.USER_AUTOPERSP)!=0) rv3d.persp= View3dTypes.V3D_ORTHO;
		//else if(rv3d.persp==View3dTypes.V3D_CAMOB) rv3d.persp= (short)perspo;
		else if(rv3d.persp==View3dTypes.V3D_CAMOB) rv3d.persp= (byte)perspo;

		View3dView.smooth_view(C, null, null, null, new_quat, null, null);
	}

}

static int perspo=View3dTypes.V3D_PERSP;
public static wmOperatorType.Operator viewnumpad_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent unused)
//static int viewnumpad_exec(bContext *C, wmOperator *op)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	Scene scene= bContext.CTX_data_scene(C);
//	static int perspo=V3D_PERSP;
	int viewnum;

	viewnum = RnaAccess.RNA_enum_get(op.ptr, "type");

	/* Use this to test if we started out with a camera */

	switch (viewnum) {
		case View3dTypes.V3D_VIEW_BOTTOM :
			axis_set_view(C, 0.0f, -1.0f, 0.0f, 0.0f, viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_BACK:
			axis_set_view(C, 0.0f, 0.0f, (float)-StrictMath.cos(Arithb.M_PI/4.0), (float)-StrictMath.cos(Arithb.M_PI/4.0), viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_LEFT:
			axis_set_view(C, 0.5f, -0.5f, 0.5f, 0.5f, viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_TOP:
			axis_set_view(C, 1.0f, 0.0f, 0.0f, 0.0f, viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_FRONT:
			axis_set_view(C, (float)StrictMath.cos(Arithb.M_PI/4.0), (float)-StrictMath.sin(Arithb.M_PI/4.0), 0.0f, 0.0f, viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_RIGHT:
			axis_set_view(C, 0.5f, -0.5f, -0.5f, -0.5f, viewnum, perspo);
			break;

		case View3dTypes.V3D_VIEW_CAMERA:
			if(rv3d.viewlock==0) {
				/* lastview -  */

				if(rv3d.persp != View3dTypes.V3D_CAMOB) {
					/* store settings of current view before allowing overwriting with camera view */
					UtilDefines.QUATCOPY(rv3d.lviewquat, rv3d.viewquat);
					rv3d.lview= rv3d.view;
					rv3d.lpersp= rv3d.persp;

//	#if 0
//					if(G.qual==LR_ALTKEY) {
//						if(oldcamera && is_an_active_object(oldcamera)) {
//							v3d.camera= oldcamera;
//						}
//						handle_view3d_lock();
//					}
//	#endif

					if(scene.basact!=null) {
						/* check both G.vd as G.scene cameras */
//						if((v3d.camera==null || scene.camera==null) && (scene.basact!=null? scene.basact.object: null).type==ObjectTypes.OB_CAMERA) {
//							v3d.camera= (scene.basact!=null? scene.basact.object: null);
//							/*handle_view3d_lock();*/
//						}
					}

					if(v3d.camera==null) {
//						v3d.camera= scene_find_camera(scene);
						/*handle_view3d_lock();*/
					}
					rv3d.persp= View3dTypes.V3D_CAMOB;
                                        float[] rv3d_dist={rv3d.dist}, v3d_lens={v3d.lens};
					View3dView.smooth_view(C, null, v3d.camera, rv3d.ofs, rv3d.viewquat, rv3d_dist, v3d_lens);
                                        rv3d.dist=rv3d_dist[0]; v3d.lens=v3d_lens[0];
				}
				else{
					/* return to settings of last view */
					/* does smooth_view too */
					axis_set_view(C, rv3d.lviewquat[0], rv3d.lviewquat[1], rv3d.lviewquat[2], rv3d.lviewquat[3], rv3d.lview, rv3d.lpersp);
				}
			}
			break;

		default :
			break;
	}

	if(rv3d.persp != View3dTypes.V3D_CAMOB) perspo= rv3d.persp;

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_viewnumpad = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_viewnumpad(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View numpad";
	ot.description = "Set the view.";
	ot.idname= "VIEW3D_OT_viewnumpad";

	/* api callbacks */
	ot.exec= viewnumpad_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;

	RnaDefine.RNA_def_enum(ot.srna, "type", prop_view_items, 0, "View", "The Type of view");
}};

static EnumPropertyItem[] prop_view_orbit_items = {
	new EnumPropertyItem(View3dTypes.V3D_VIEW_STEPLEFT, "ORBITLEFT", 0, "Orbit Left", "Orbit the view around to the Left"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_STEPRIGHT, "ORBITRIGHT", 0, "Orbit Right", "Orbit the view around to the Right"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_STEPUP, "ORBITUP", 0, "Orbit Up", "Orbit the view Up"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_STEPDOWN, "ORBITDOWN", 0, "Orbit Down", "Orbit the view Down"),
	new EnumPropertyItem(0, null, 0, null, null)};

public static wmOperatorType.Operator vieworbit_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int vieworbit_exec(bContext *C, wmOperator *op)
{
	ARegion ar= bContext.CTX_wm_region(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	float phi, si;
        float[] q1=new float[4];
	int orbitdir;

	orbitdir = RnaAccess.RNA_enum_get(op.ptr, "type");

	if(rv3d.viewlock==0) {

		if(rv3d.persp != View3dTypes.V3D_CAMOB) {
			if(orbitdir == View3dTypes.V3D_VIEW_STEPLEFT || orbitdir == View3dTypes.V3D_VIEW_STEPRIGHT) {
				/* z-axis */
				phi= (float)(Arithb.M_PI/360.0)*U.pad_rot_angle;
				if(orbitdir == View3dTypes.V3D_VIEW_STEPRIGHT) phi= -phi;
				si= (float)StrictMath.sin(phi);
				q1[0]= (float)StrictMath.cos(phi);
				q1[1]= q1[2]= 0.0f;
				q1[3]= si;
				Arithb.QuatMul(rv3d.viewquat, rv3d.viewquat, q1);
				rv3d.view= 0;
			}
			if(orbitdir == View3dTypes.V3D_VIEW_STEPDOWN || orbitdir == View3dTypes.V3D_VIEW_STEPUP) {
				/* horizontal axis */
                                float[] q1t={q1[1], q1[2], q1[3]};
//				UtilDefines.VECCOPY(q1+1, rv3d.viewinv[0]);
                                UtilDefines.VECCOPY(q1t, rv3d.viewinv[0]);

//				Arithb.Normalize(q1+1);
                                Arithb.Normalize(q1t);
                                q1[1]=q1t[0]; q1[2]=q1t[1]; q1[3]=q1t[2];
				phi= (float)(Arithb.M_PI/360.0)*U.pad_rot_angle;
				if(orbitdir == View3dTypes.V3D_VIEW_STEPDOWN) phi= -phi;
				si= (float)StrictMath.sin(phi);
				q1[0]= (float)StrictMath.cos(phi);
				q1[1]*= si;
				q1[2]*= si;
				q1[3]*= si;
				Arithb.QuatMul(rv3d.viewquat, rv3d.viewquat, q1);
				rv3d.view= 0;
			}
			Area.ED_region_tag_redraw(ar);
		}
	}

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_view_orbit = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_view_orbit(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View Orbit";
	ot.description = "Orbit the view.";
	ot.idname= "VIEW3D_OT_view_orbit";

	/* api callbacks */
	ot.exec= vieworbit_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;
	RnaDefine.RNA_def_enum(ot.srna, "type", prop_view_orbit_items, 0, "Orbit", "Direction of View Orbit");
}};

static EnumPropertyItem[] prop_view_pan_items = {
	new EnumPropertyItem(View3dTypes.V3D_VIEW_PANLEFT, "PANLEFT", 0, "Pan Left", "Pan the view to the Left"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_PANRIGHT, "PANRIGHT", 0, "Pan Right", "Pan the view to the Right"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_PANUP, "PANUP", 0, "Pan Up", "Pan the view Up"),
	new EnumPropertyItem(View3dTypes.V3D_VIEW_PANDOWN, "PANDOWN", 0, "Pan Down", "Pan the view Down"),
	new EnumPropertyItem(0, null, 0, null, null)};

public static wmOperatorType.Operator viewpan_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewpan_exec(bContext *C, wmOperator *op)
{
	ARegion ar= bContext.CTX_wm_region(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	float[] vec= new float[3];
	int pandir;

	pandir = RnaAccess.RNA_enum_get(op.ptr, "type");

	View3dView.initgrabz(rv3d, 0.0f, 0.0f, 0.0f);

	if(pandir == View3dTypes.V3D_VIEW_PANRIGHT) View3dView.window_to_3d_delta(ar, vec, -32, 0);
	else if(pandir == View3dTypes.V3D_VIEW_PANLEFT) View3dView.window_to_3d_delta(ar, vec, 32, 0);
	else if(pandir == View3dTypes.V3D_VIEW_PANUP) View3dView.window_to_3d_delta(ar, vec, 0, -25);
	else if(pandir == View3dTypes.V3D_VIEW_PANDOWN) View3dView.window_to_3d_delta(ar, vec, 0, 25);
	rv3d.ofs[0]+= vec[0];
	rv3d.ofs[1]+= vec[1];
	rv3d.ofs[2]+= vec[2];

	if((rv3d.viewlock & View3dTypes.RV3D_BOXVIEW)!=0)
		view3d_boxview_sync(bContext.CTX_wm_area(C), ar);

	Area.ED_region_tag_redraw(ar);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_view_pan = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_view_pan(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View Pan";
	ot.description = "Pan the view.";
	ot.idname= "VIEW3D_OT_view_pan";

	/* api callbacks */
	ot.exec= viewpan_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;
	RnaDefine.RNA_def_enum(ot.srna, "type", prop_view_pan_items, 0, "Pan", "Direction of View Pan");
}};

public static wmOperatorType.Operator viewpersportho_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int viewpersportho_exec(bContext *C, wmOperator *op)
{
	ARegion ar= bContext.CTX_wm_region(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);

	if(rv3d.viewlock==0) {
		if(rv3d.persp!=View3dTypes.V3D_ORTHO)
			rv3d.persp=View3dTypes.V3D_ORTHO;
		else rv3d.persp=View3dTypes.V3D_PERSP;
		Area.ED_region_tag_redraw(ar);
	}

	return WindowManagerTypes.OPERATOR_FINISHED;

}};

public static OpFunc VIEW3D_OT_view_persportho = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_view_persportho(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "View persp/ortho";
	ot.description = "Switch the current view from perspective/orthographic.";
	ot.idname= "VIEW3D_OT_view_persportho";

	/* api callbacks */
	ot.exec= viewpersportho_exec;
	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;
}};


///* ********************* set clipping operator ****************** */
//
//static int view3d_clipping_exec(bContext *C, wmOperator *op)
//{
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//	rcti rect;
//	double mvmatrix[16];
//	double projmatrix[16];
//	double xs, ys, p[3];
//	GLint viewport[4];
//	short val;
//
//	rect.xmin= RNA_int_get(op.ptr, "xmin");
//	rect.ymin= RNA_int_get(op.ptr, "ymin");
//	rect.xmax= RNA_int_get(op.ptr, "xmax");
//	rect.ymax= RNA_int_get(op.ptr, "ymax");
//
//	rv3d.rflag |= RV3D_CLIPPING;
//	rv3d.clipbb= MEM_callocN(sizeof(BoundBox), "clipbb");
//
//	/* note; otherwise opengl won't work */
//	view3d_operator_needs_opengl(C);
//
//	/* Get the matrices needed for gluUnProject */
//	glGetIntegerv(GL_VIEWPORT, viewport);
//	glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix);
//	glGetDoublev(GL_PROJECTION_MATRIX, projmatrix);
//
//	/* near zero floating point values can give issues with gluUnProject
//		in side view on some implementations */
//	if(fabs(mvmatrix[0]) < 1e-6) mvmatrix[0]= 0.0;
//	if(fabs(mvmatrix[5]) < 1e-6) mvmatrix[5]= 0.0;
//
//	/* Set up viewport so that gluUnProject will give correct values */
//	viewport[0] = 0;
//	viewport[1] = 0;
//
//	/* four clipping planes and bounding volume */
//	/* first do the bounding volume */
//	for(val=0; val<4; val++) {
//
//		xs= (val==0||val==3)?rect.xmin:rect.xmax;
//		ys= (val==0||val==1)?rect.ymin:rect.ymax;
//
//		gluUnProject(xs, ys, 0.0, mvmatrix, projmatrix, viewport, &p[0], &p[1], &p[2]);
//		VECCOPY(rv3d.clipbb.vec[val], p);
//
//		gluUnProject(xs, ys, 1.0, mvmatrix, projmatrix, viewport, &p[0], &p[1], &p[2]);
//		VECCOPY(rv3d.clipbb.vec[4+val], p);
//	}
//
//	/* then plane equations */
//	for(val=0; val<4; val++) {
//
//		CalcNormFloat(rv3d.clipbb.vec[val], rv3d.clipbb.vec[val==3?0:val+1], rv3d.clipbb.vec[val+4],
//					  rv3d.clip[val]);
//
//		rv3d.clip[val][3]= - rv3d.clip[val][0]*rv3d.clipbb.vec[val][0]
//			- rv3d.clip[val][1]*rv3d.clipbb.vec[val][1]
//			- rv3d.clip[val][2]*rv3d.clipbb.vec[val][2];
//	}
//	return OPERATOR_FINISHED;
//}
//
//static int view3d_clipping_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//	ARegion *ar= CTX_wm_region(C);
//
//	if(rv3d.rflag & RV3D_CLIPPING) {
//		rv3d.rflag &= ~RV3D_CLIPPING;
//		ED_region_tag_redraw(ar);
//		if(rv3d.clipbb) MEM_freeN(rv3d.clipbb);
//		rv3d.clipbb= NULL;
//		return OPERATOR_FINISHED;
//	}
//	else {
//		return WM_border_select_invoke(C, op, event);
//	}
//}
//
///* toggles */
//void VIEW3D_OT_clip_border(wmOperatorType *ot)
//{
//
//	/* identifiers */
//	ot.name= "Clipping Border";
//	ot.description = "Set the view clipping border.";
//	ot.idname= "VIEW3D_OT_clip_border";
//
//	/* api callbacks */
//	ot.invoke= view3d_clipping_invoke;
//	ot.exec= view3d_clipping_exec;
//	ot.modal= WM_border_select_modal;
//
//	ot.poll= ED_operator_view3d_active;
//
//	/* flags */
//	ot.flag= OPTYPE_REGISTER;
//
//	/* rna */
//	RNA_def_int(ot.srna, "xmin", 0, INT_MIN, INT_MAX, "X Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "xmax", 0, INT_MIN, INT_MAX, "X Max", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymin", 0, INT_MIN, INT_MAX, "Y Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymax", 0, INT_MIN, INT_MAX, "Y Max", "", INT_MIN, INT_MAX);
//}

/* ********************* draw type operator ****************** */

public static wmOperatorType.Operator view3d_drawtype_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int view3d_drawtype_exec(bContext *C, wmOperator *op)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	int dt, dt_alt;

	dt  = RnaAccess.RNA_int_get(op.ptr, "draw_type");
	dt_alt = RnaAccess.RNA_int_get(op.ptr, "draw_type_alternate");

	if (dt_alt != -1) {
		if (v3d.drawtype == dt)
			v3d.drawtype = (short)dt_alt;
		else
			v3d.drawtype = (short)dt;
	}
	else
		v3d.drawtype = (short)dt;

	Area.ED_area_tag_redraw(bContext.CTX_wm_area(C));

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static wmOperatorType.Operator view3d_drawtype_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int view3d_drawtype_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	return view3d_drawtype_exec.run(C, op, null);
}};

/* toggles */
public static OpFunc VIEW3D_OT_drawtype = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_drawtype(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Change draw type";
	ot.description = "Change the draw type of the view.";
	ot.idname= "VIEW3D_OT_drawtype";

	/* api callbacks */
	ot.invoke= view3d_drawtype_invoke;
	ot.exec= view3d_drawtype_exec;

	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* flags */
	ot.flag= WmTypes.OPTYPE_REGISTER;

	/* rna XXX should become enum */
	RnaDefine.RNA_def_int(ot.srna, "draw_type", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Draw Type", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "draw_type_alternate", -1, Integer.MIN_VALUE, Integer.MAX_VALUE, "Draw Type Alternate", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
}};

/* ***************** 3d cursor cursor op ******************* */

/* mx my in region coords */
public static wmOperatorType.Operator set_3dcursor_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int set_3dcursor_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	Scene scene= bContext.CTX_data_scene(C);
	ARegion ar= bContext.CTX_wm_region(C);
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	float dx, dy, fz;
        float[] fp = null;
        float[] dvec=new float[3], oldcurs=new float[3];
	short mx, my;
        short[] mval=new short[2];
	//short ctrl= 0; // XXX

	fp= View3dView.give_cursor(scene, v3d);

	//if(obedit && ctrl) lr_click= 1;
	UtilDefines.VECCOPY(oldcurs, fp);

	mx= (short)(event.x - ar.winrct.xmin);
	my= (short)(event.y - ar.winrct.ymin);
	View3dView.project_short_noclip(ar, fp, mval);

	View3dView.initgrabz(rv3d, fp[0], fp[1], fp[2]);

	if(mval[0]!=View3dView.IS_CLIPPED) {

		View3dView.window_to_3d_delta(ar, dvec, mval[0]-mx, mval[1]-my);
		Arithb.VecSubf(fp, fp, dvec);
	}
	else {

		dx= ((float)(mx-(ar.winx/2)))*rv3d.zfac/(ar.winx/2);
		dy= ((float)(my-(ar.winy/2)))*rv3d.zfac/(ar.winy/2);

		fz= rv3d.persmat[0][3]*fp[0]+ rv3d.persmat[1][3]*fp[1]+ rv3d.persmat[2][3]*fp[2]+ rv3d.persmat[3][3];
		fz= fz/rv3d.zfac;

		fp[0]= (rv3d.persinv[0][0]*dx + rv3d.persinv[1][0]*dy+ rv3d.persinv[2][0]*fz)-rv3d.ofs[0];
		fp[1]= (rv3d.persinv[0][1]*dx + rv3d.persinv[1][1]*dy+ rv3d.persinv[2][1]*fz)-rv3d.ofs[1];
		fp[2]= (rv3d.persinv[0][2]*dx + rv3d.persinv[1][2]*dy+ rv3d.persinv[2][2]*fz)-rv3d.ofs[2];
	}

//	if(lr_click) {
		// XXX		if(obedit.type==OB_MESH) add_click_mesh();
		//		else if ELEM(obedit.type, OB_CURVE, OB_SURF) addvert_Nurb(0);
		//		else if (obedit.type==OB_ARMATURE) addvert_armature();
//		VECCOPY(fp, oldcurs);
//	}
	// XXX notifier for scene */
	Area.ED_area_tag_redraw(bContext.CTX_wm_area(C));

	/* prevent other mouse ops to fail */
	return WindowManagerTypes.OPERATOR_PASS_THROUGH;
}};

public static OpFunc VIEW3D_OT_cursor3d = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_cursor3d(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Set 3D Cursor";
	ot.description = "Set the location of the 3D cursor.";
	ot.idname= "VIEW3D_OT_cursor3d";

	/* api callbacks */
	ot.invoke= set_3dcursor_invoke;

	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* rna later */
}};

/* ***************** manipulator op ******************* */


public static wmOperatorType.Operator manipulator_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int manipulator_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("manipulator_invoke");
	View3D v3d = bContext.CTX_wm_view3d(C);

	if((v3d.twflag & View3dTypes.V3D_USE_MANIPULATOR)==0) return WindowManagerTypes.OPERATOR_PASS_THROUGH;
	if((v3d.twflag & View3dTypes.V3D_DRAW_MANIPULATOR)==0) return WindowManagerTypes.OPERATOR_PASS_THROUGH;

	/* note; otherwise opengl won't work */
	View3dView.view3d_operator_needs_opengl(C);

	if(0==TransformManipulator.BIF_do_manipulator(C, event, op))
		return WindowManagerTypes.OPERATOR_PASS_THROUGH;

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_manipulator = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_manipulator(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "3D Manipulator";
	ot.description = "";
	ot.idname= "VIEW3D_OT_manipulator";

	/* api callbacks */
	ot.invoke= manipulator_invoke;

	ot.poll= ScreenOps.ED_operator_view3d_active;

	/* rna later */
	RnaDefine.RNA_def_boolean_vector(ot.srna, "constraint_axis", 3, null, "Constraint Axis", "");
}};


///* ************************* below the line! *********************** */
//
//
///* XXX todo Zooms in on a border drawn by the user */
//int view_autodist(Scene *scene, ARegion *ar, View3D *v3d, short *mval, float mouse_worldloc[3] ) //, float *autodist )
//{
//	RegionView3D *rv3d= ar.regiondata;
//	bglMats mats; /* ZBuffer depth vars */
//	rcti rect;
//	float depth, depth_close= MAXFLOAT;
//	int had_depth = 0;
//	double cent[2],  p[3];
//	int xs, ys;
//
//	rect.xmax = mval[0] + 4;
//	rect.ymax = mval[1] + 4;
//
//	rect.xmin = mval[0] - 4;
//	rect.ymin = mval[1] - 4;
//
//	/* Get Z Depths, needed for perspective, nice for ortho */
//	bgl_get_mats(&mats);
//	draw_depth(scene, ar, v3d, NULL);
//
//	/* force updating */
//	if (rv3d.depths) {
//		had_depth = 1;
//		rv3d.depths.damaged = 1;
//	}
//
//	view3d_update_depths(ar, v3d);
//
//	/* Constrain rect to depth bounds */
//	if (rect.xmin < 0) rect.xmin = 0;
//	if (rect.ymin < 0) rect.ymin = 0;
//	if (rect.xmax >= rv3d.depths.w) rect.xmax = rv3d.depths.w-1;
//	if (rect.ymax >= rv3d.depths.h) rect.ymax = rv3d.depths.h-1;
//
//	/* Find the closest Z pixel */
//	for (xs=rect.xmin; xs < rect.xmax; xs++) {
//		for (ys=rect.ymin; ys < rect.ymax; ys++) {
//			depth= rv3d.depths.depths[ys*rv3d.depths.w+xs];
//			if(depth < rv3d.depths.depth_range[1] && depth > rv3d.depths.depth_range[0]) {
//				if (depth_close > depth) {
//					depth_close = depth;
//				}
//			}
//		}
//	}
//
//	if (depth_close==MAXFLOAT)
//		return 0;
//
//	if (had_depth==0) {
//		MEM_freeN(rv3d.depths.depths);
//		rv3d.depths.depths = NULL;
//	}
//	rv3d.depths.damaged = 1;
//
//	cent[0] = (double)mval[0];
//	cent[1] = (double)mval[1];
//
//	if (!gluUnProject(cent[0], cent[1], depth_close, mats.modelview, mats.projection, (GLint *)mats.viewport, &p[0], &p[1], &p[2]))
//		return 0;
//
//	mouse_worldloc[0] = (float)p[0];
//	mouse_worldloc[1] = (float)p[1];
//	mouse_worldloc[2] = (float)p[2];
//	return 1;
//}
//
//
//
///* ********************* NDOF ************************ */
///* note: this code is confusing and unclear... (ton) */
///* **************************************************** */
//
//// ndof scaling will be moved to user setting.
//// In the mean time this is just a place holder.
//
//// Note: scaling in the plugin and ghostwinlay.c
//// should be removed. With driver default setting,
//// each axis returns approx. +-200 max deflection.
//
//// The values I selected are based on the older
//// polling i/f. With event i/f, the sensistivity
//// can be increased for improved response from
//// small deflections of the device input.
//
//
//// lukep notes : i disagree on the range.
//// the normal 3Dconnection driver give +/-400
//// on defaut range in other applications
//// and up to +/- 1000 if set to maximum
//// because i remove the scaling by delta,
//// which was a bad idea as it depend of the system
//// speed and os, i changed the scaling values, but
//// those are still not ok
//
//
//float ndof_axis_scale[6] = {
//	+0.01,	// Tx
//	+0.01,	// Tz
//	+0.01,	// Ty
//	+0.0015,	// Rx
//	+0.0015,	// Rz
//	+0.0015	// Ry
//};
//
//void filterNDOFvalues(float *sbval)
//{
//	int i=0;
//	float max  = 0.0;
//
//	for (i =0; i<6;i++)
//		if (fabs(sbval[i]) > max)
//			max = fabs(sbval[i]);
//	for (i =0; i<6;i++)
//		if (fabs(sbval[i]) != max )
//			sbval[i]=0.0;
//}
//
//// statics for controlling rv3d.dist corrections.
//// viewmoveNDOF zeros and adjusts rv3d.ofs.
//// viewmove restores based on dz_flag state.
//
//int dz_flag = 0;
//float m_dist;
//
//void viewmoveNDOFfly(ARegion *ar, View3D *v3d, int mode)
//{
//	RegionView3D *rv3d= ar.regiondata;
//    int i;
//    float phi;
//    float dval[7];
//	// static fval[6] for low pass filter; device input vector is dval[6]
//	static float fval[6];
//    float tvec[3],rvec[3];
//    float q1[4];
//	float mat[3][3];
//	float upvec[3];
//
//
//    /*----------------------------------------------------
//	 * sometimes this routine is called from headerbuttons
//     * viewmove needs to refresh the screen
//     */
//// XXX	areawinset(ar.win);
//
//
//	// fetch the current state of the ndof device
//// XXX	getndof(dval);
//
//	if (v3d.ndoffilter)
//		filterNDOFvalues(fval);
//
//	// Scale input values
//
////	if(dval[6] == 0) return; // guard against divide by zero
//
//	for(i=0;i<6;i++) {
//
//		// user scaling
//		dval[i] = dval[i] * ndof_axis_scale[i];
//	}
//
//
//	// low pass filter with zero crossing reset
//
//	for(i=0;i<6;i++) {
//		if((dval[i] * fval[i]) >= 0)
//			dval[i] = (fval[i] * 15 + dval[i]) / 16;
//		else
//			fval[i] = 0;
//	}
//
//
//	// force perspective mode. This is a hack and is
//	// incomplete. It doesn't actually effect the view
//	// until the first draw and doesn't update the menu
//	// to reflect persp mode.
//
//	rv3d.persp = V3D_PERSP;
//
//
//	// Correct the distance jump if rv3d.dist != 0
//
//	// This is due to a side effect of the original
//	// mouse view rotation code. The rotation point is
//	// set a distance in front of the viewport to
//	// make rotating with the mouse look better.
//	// The distance effect is written at a low level
//	// in the view management instead of the mouse
//	// view function. This means that all other view
//	// movement devices must subtract this from their
//	// view transformations.
//
//	if(rv3d.dist != 0.0) {
//		dz_flag = 1;
//		m_dist = rv3d.dist;
//		upvec[0] = upvec[1] = 0;
//		upvec[2] = rv3d.dist;
//		Mat3CpyMat4(mat, rv3d.viewinv);
//		Mat3MulVecfl(mat, upvec);
//		VecSubf(rv3d.ofs, rv3d.ofs, upvec);
//		rv3d.dist = 0.0;
//	}
//
//
//	// Apply rotation
//	// Rotations feel relatively faster than translations only in fly mode, so
//	// we have no choice but to fix that here (not in the plugins)
//	rvec[0] = -0.5 * dval[3];
//	rvec[1] = -0.5 * dval[4];
//	rvec[2] = -0.5 * dval[5];
//
//	// rotate device x and y by view z
//
//	Mat3CpyMat4(mat, rv3d.viewinv);
//	mat[2][2] = 0.0f;
//	Mat3MulVecfl(mat, rvec);
//
//	// rotate the view
//
//	phi = Normalize(rvec);
//	if(phi != 0) {
//		VecRotToQuat(rvec,phi,q1);
//		QuatMul(rv3d.viewquat, rv3d.viewquat, q1);
//	}
//
//
//	// Apply translation
//
//	tvec[0] = dval[0];
//	tvec[1] = dval[1];
//	tvec[2] = -dval[2];
//
//	// the next three lines rotate the x and y translation coordinates
//	// by the current z axis angle
//
//	Mat3CpyMat4(mat, rv3d.viewinv);
//	mat[2][2] = 0.0f;
//	Mat3MulVecfl(mat, tvec);
//
//	// translate the view
//
//	VecSubf(rv3d.ofs, rv3d.ofs, tvec);
//
//
//	/*----------------------------------------------------
//     * refresh the screen XXX
//      */
//
//	// update render preview window
//
//// XXX	BIF_view3d_previewrender_signal(ar, PR_DBASE|PR_DISPRECT);
//}
//
//void viewmoveNDOF(Scene *scene, ARegion *ar, View3D *v3d, int mode)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	float fval[7];
//	float dvec[3];
//	float sbadjust = 1.0f;
//	float len;
//	short use_sel = 0;
//	Object *ob = OBACT;
//	float m[3][3];
//	float m_inv[3][3];
//	float xvec[3] = {1,0,0};
//	float yvec[3] = {0,-1,0};
//	float zvec[3] = {0,0,1};
//	float phi, si;
//	float q1[4];
//	float obofs[3];
//	float reverse;
//	//float diff[4];
//	float d, curareaX, curareaY;
//	float mat[3][3];
//	float upvec[3];
//
//    /* Sensitivity will control how fast the view rotates.  The value was
//     * obtained experimentally by tweaking until the author didn't get dizzy watching.
//     * Perhaps this should be a configurable user parameter.
//     */
//	float psens = 0.005f * (float) U.ndof_pan;   /* pan sensitivity */
//	float rsens = 0.005f * (float) U.ndof_rotate;  /* rotate sensitivity */
//	float zsens = 0.3f;   /* zoom sensitivity */
//
//	const float minZoom = -30.0f;
//	const float maxZoom = 300.0f;
//
//	//reset view type
//	rv3d.view = 0;
////printf("passing here \n");
////
//	if (scene.obedit==NULL && ob && !(ob.flag & OB_POSEMODE)) {
//		use_sel = 1;
//	}
//
//	if((dz_flag)||rv3d.dist==0) {
//		dz_flag = 0;
//		rv3d.dist = m_dist;
//		upvec[0] = upvec[1] = 0;
//		upvec[2] = rv3d.dist;
//		Mat3CpyMat4(mat, rv3d.viewinv);
//		Mat3MulVecfl(mat, upvec);
//		VecAddf(rv3d.ofs, rv3d.ofs, upvec);
//	}
//
//    /*----------------------------------------------------
//	 * sometimes this routine is called from headerbuttons
//     * viewmove needs to refresh the screen
//     */
//// XXX	areawinset(curarea.win);
//
//    /*----------------------------------------------------
//     * record how much time has passed. clamp at 10 Hz
//     * pretend the previous frame occured at the clamped time
//     */
////    now = PIL_check_seconds_timer();
// //   frametime = (now - prevTime);
// //   if (frametime > 0.1f){        /* if more than 1/10s */
// //       frametime = 1.0f/60.0;      /* clamp at 1/60s so no jumps when starting to move */
////    }
////    prevTime = now;
// //   sbadjust *= 60 * frametime;             /* normalize ndof device adjustments to 100Hz for framerate independence */
//
//    /* fetch the current state of the ndof device & enforce dominant mode if selected */
//// XXX    getndof(fval);
//	if (v3d.ndoffilter)
//		filterNDOFvalues(fval);
//
//
//    // put scaling back here, was previously in ghostwinlay
//	fval[0] = fval[0] * (1.0f/600.0f);
//	fval[1] = fval[1] * (1.0f/600.0f);
//	fval[2] = fval[2] * (1.0f/1100.0f);
//	fval[3] = fval[3] * 0.00005f;
//	fval[4] =-fval[4] * 0.00005f;
//	fval[5] = fval[5] * 0.00005f;
//	fval[6] = fval[6] / 1000000.0f;
//
//    // scale more if not in perspective mode
//	if (rv3d.persp == V3D_ORTHO) {
//		fval[0] = fval[0] * 0.05f;
//		fval[1] = fval[1] * 0.05f;
//		fval[2] = fval[2] * 0.05f;
//		fval[3] = fval[3] * 0.9f;
//		fval[4] = fval[4] * 0.9f;
//		fval[5] = fval[5] * 0.9f;
//		zsens *= 8;
//	}
//
//    /* set object offset */
//	if (ob) {
//		obofs[0] = -ob.obmat[3][0];
//		obofs[1] = -ob.obmat[3][1];
//		obofs[2] = -ob.obmat[3][2];
//	}
//	else {
//		VECCOPY(obofs, rv3d.ofs);
//	}
//
//    /* calc an adjustment based on distance from camera
//       disabled per patch 14402 */
//     d = 1.0f;
//
///*    if (ob) {
//        VecSubf(diff, obofs, rv3d.ofs);
//        d = VecLength(diff);
//    }
//*/
//
//    reverse = (rv3d.persmat[2][1] < 0.0f) ? -1.0f : 1.0f;
//
//    /*----------------------------------------------------
//     * ndof device pan
//     */
//    psens *= 1.0f + d;
//    curareaX = sbadjust * psens * fval[0];
//    curareaY = sbadjust * psens * fval[1];
//    dvec[0] = curareaX * rv3d.persinv[0][0] + curareaY * rv3d.persinv[1][0];
//    dvec[1] = curareaX * rv3d.persinv[0][1] + curareaY * rv3d.persinv[1][1];
//    dvec[2] = curareaX * rv3d.persinv[0][2] + curareaY * rv3d.persinv[1][2];
//    VecAddf(rv3d.ofs, rv3d.ofs, dvec);
//
//    /*----------------------------------------------------
//     * ndof device dolly
//     */
//    len = zsens * sbadjust * fval[2];
//
//    if (rv3d.persp==V3D_CAMOB) {
//        if(rv3d.persp==V3D_CAMOB) { /* This is stupid, please fix - TODO */
//            rv3d.camzoom+= 10.0f * -len;
//        }
//        if (rv3d.camzoom < minZoom) rv3d.camzoom = minZoom;
//        else if (rv3d.camzoom > maxZoom) rv3d.camzoom = maxZoom;
//    }
//    else if ((rv3d.dist> 0.001*v3d.grid) && (rv3d.dist<10.0*v3d.far)) {
//        rv3d.dist*=(1.0 + len);
//    }
//
//
//    /*----------------------------------------------------
//     * ndof device turntable
//     * derived from the turntable code in viewmove
//     */
//
//    /* Get the 3x3 matrix and its inverse from the quaternion */
//    QuatToMat3(rv3d.viewquat, m);
//    Mat3Inv(m_inv,m);
//
//    /* Determine the direction of the x vector (for rotating up and down) */
//    /* This can likely be compuated directly from the quaternion. */
//    Mat3MulVecfl(m_inv,xvec);
//    Mat3MulVecfl(m_inv,yvec);
//    Mat3MulVecfl(m_inv,zvec);
//
//    /* Perform the up/down rotation */
//    phi = sbadjust * rsens * /*0.5f * */ fval[3]; /* spin vertically half as fast as horizontally */
//    si = sin(phi);
//    q1[0] = cos(phi);
//    q1[1] = si * xvec[0];
//    q1[2] = si * xvec[1];
//    q1[3] = si * xvec[2];
//    QuatMul(rv3d.viewquat, rv3d.viewquat, q1);
//
//    if (use_sel) {
//        QuatConj(q1); /* conj == inv for unit quat */
//        VecSubf(v3d.ofs, v3d.ofs, obofs);
//        QuatMulVecf(q1, rv3d.ofs);
//        VecAddf(rv3d.ofs, rv3d.ofs, obofs);
//    }
//
//    /* Perform the orbital rotation */
//    /* Perform the orbital rotation
//       If the seen Up axis is parallel to the zoom axis, rotation should be
//       achieved with a pure Roll motion (no Spin) on the device. When you start
//       to tilt, moving from Top to Side view, Spinning will increasingly become
//       more relevant while the Roll component will decrease. When a full
//       Side view is reached, rotations around the world's Up axis are achieved
//       with a pure Spin-only motion.  In other words the control of the spinning
//       around the world's Up axis should move from the device's Spin axis to the
//       device's Roll axis depending on the orientation of the world's Up axis
//       relative to the screen. */
//    //phi = sbadjust * rsens * reverse * fval[4];  /* spin the knob, y axis */
//    phi = sbadjust * rsens * (yvec[2] * fval[4] + zvec[2] * fval[5]);
//    q1[0] = cos(phi);
//    q1[1] = q1[2] = 0.0;
//    q1[3] = sin(phi);
//    QuatMul(rv3d.viewquat, rv3d.viewquat, q1);
//
//    if (use_sel) {
//        QuatConj(q1);
//        VecSubf(rv3d.ofs, rv3d.ofs, obofs);
//        QuatMulVecf(q1, rv3d.ofs);
//        VecAddf(rv3d.ofs, rv3d.ofs, obofs);
//    }
//
//    /*----------------------------------------------------
//     * refresh the screen
//     */
//// XXX    scrarea_do_windraw(curarea);
//}
}




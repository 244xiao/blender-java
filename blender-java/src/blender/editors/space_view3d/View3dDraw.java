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

import blender.blenkernel.Blender;
import blender.blenkernel.CustomDataUtil;
import blender.blenkernel.Global;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.editors.screen.Area;
import blender.editors.screen.GlUtil;
import blender.editors.space_api.SpaceTypeUtil;
import blender.editors.transform.TransformManipulator;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.UIDraw;
import blender.makesdna.CameraTypes;
import blender.makesdna.CustomDataTypes;
import blender.makesdna.ObjectTypes;
import blender.makesdna.SceneTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.BoundBox;
import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rctf;
import blender.windowmanager.WmSubWindow;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import static blender.blenkernel.Blender.G;

public class View3dDraw {
//
//
//static void star_stuff_init_func(void)
//{
//	cpack(-1);
//	glPointSize(1.0);
//	glBegin(GL_POINTS);
//}
//static void star_stuff_vertex_func(float* i)
//{
//	glVertex3fv(i);
//}
//static void star_stuff_term_func(void)
//{
//	glEnd();
//}
//
//void circf(float x, float y, float rad)
//{
//	GLUquadricObj *qobj = gluNewQuadric();
//
//	gluQuadricDrawStyle(qobj, GLU_FILL);
//
//	glPushMatrix();
//
//	glTranslatef(x,  y, 0.);
//
//	gluDisk( qobj, 0.0,  rad, 32, 1);
//
//	glPopMatrix();
//
//	gluDeleteQuadric(qobj);
//}

    public static void circ(GL2 gl, float x, float y, float rad) {
        GLU glu = new GLU();

        GLUquadric qobj = glu.gluNewQuadric();

        glu.gluQuadricDrawStyle(qobj, GLU.GLU_SILHOUETTE);

        gl.glPushMatrix();

        gl.glTranslatef(x, y, 0.0f);

        glu.gluDisk(qobj, 0.0, rad, 32, 1);

        gl.glPopMatrix();

        glu.gluDeleteQuadric(qobj);
    }

/* ********* custom clipping *********** */

static void view3d_draw_clipping(GL2 gl, RegionView3D rv3d)
{
	BoundBox bb= rv3d.clipbb;

	if(bb!=null) {
		Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -8);

		gl.glBegin(GL2.GL_QUADS);

		gl.glVertex3fv(bb.vec[0],0); gl.glVertex3fv(bb.vec[1],0); gl.glVertex3fv(bb.vec[2],0); gl.glVertex3fv(bb.vec[3],0);
		gl.glVertex3fv(bb.vec[0],0); gl.glVertex3fv(bb.vec[4],0); gl.glVertex3fv(bb.vec[5],0); gl.glVertex3fv(bb.vec[1],0);
		gl.glVertex3fv(bb.vec[4],0); gl.glVertex3fv(bb.vec[7],0); gl.glVertex3fv(bb.vec[6],0); gl.glVertex3fv(bb.vec[5],0);
		gl.glVertex3fv(bb.vec[7],0); gl.glVertex3fv(bb.vec[3],0); gl.glVertex3fv(bb.vec[2],0); gl.glVertex3fv(bb.vec[6],0);
		gl.glVertex3fv(bb.vec[1],0); gl.glVertex3fv(bb.vec[5],0); gl.glVertex3fv(bb.vec[6],0); gl.glVertex3fv(bb.vec[2],0);
		gl.glVertex3fv(bb.vec[7],0); gl.glVertex3fv(bb.vec[4],0); gl.glVertex3fv(bb.vec[0],0); gl.glVertex3fv(bb.vec[3],0);

		gl.glEnd();
	}
}

public static void view3d_set_clipping(GL2 gl, RegionView3D rv3d)
{
	double[] plane=new double[4];
	int a, tot=4;

	if(rv3d.viewlock!=0) tot= 6;

	for(a=0; a<tot; a++) {
		UtilDefines.QUATCOPY(plane, rv3d.clip[a]);
		gl.glClipPlane(GL2.GL_CLIP_PLANE0+a, plane,0);
		gl.glEnable(GL2.GL_CLIP_PLANE0+a);
	}
}

public static void view3d_clr_clipping(GL2 gl)
{
	int a;

	for(a=0; a<6; a++) {
		gl.glDisable(GL2.GL_CLIP_PLANE0+a);
	}
}

    public static boolean view3d_test_clipping(RegionView3D rv3d, float[] vec)
{
	/* vec in world coordinates, returns 1 if clipped */
	float[] view=new float[3];

	UtilDefines.VECCOPY(view, vec);

	if(0.0f < rv3d.clip[0][3] + UtilDefines.INPR(view, rv3d.clip[0]))
		if(0.0f < rv3d.clip[1][3] + UtilDefines.INPR(view, rv3d.clip[1]))
			if(0.0f < rv3d.clip[2][3] + UtilDefines.INPR(view, rv3d.clip[2]))
				if(0.0f < rv3d.clip[3][3] + UtilDefines.INPR(view, rv3d.clip[3]))
					return false;

	return true;
}

/* ********* end custom clipping *********** */


static void drawgrid_draw(GL2 gl, ARegion ar, float wx, float wy, float x, float y, float dx)
{
	float fx, fy;

	x+= (wx);
	y+= (wy);
	fx= x/dx;
	fx= x-dx*(int)StrictMath.floor(fx);

	while(fx< ar.winx) {
		GlUtil.fdrawline(gl, fx,  0.0f,  fx,  (float)ar.winy);
		fx+= dx;
	}

	fy= y/dx;
	fy= y-dx*(int)StrictMath.floor(fy);


	while(fy< ar.winy) {
		GlUtil.fdrawline(gl, 0.0f,  fy,  (float)ar.winx,  fy);
		fy+= dx;
	}

}

static void drawgrid(GL2 gl, ARegion ar, View3D v3d)
{
	/* extern short bgpicmode; */
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float wx, wy, x, y, fw, fx, fy, dx;
	float[] vec4=new float[4];
	byte[] col=new byte[3], col2=new byte[3];
	short sublines = v3d.gridsubdiv;

	vec4[0]=vec4[1]=vec4[2]=0.0f;
	vec4[3]= 1.0f;
	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);
	fx= vec4[0];
	fy= vec4[1];
	fw= vec4[3];

	wx= (ar.winx/2.0f);	/* because of rounding errors, grid at wrong location */
	wy= (ar.winy/2.0f);

	x= (wx)*fx/fw;
	y= (wy)*fy/fw;

	vec4[0]=vec4[1]=v3d.grid;
	vec4[2]= 0.0f;
	vec4[3]= 1.0f;
	Arithb.Mat4MulVec4fl(rv3d.persmat, vec4);
	fx= vec4[0];
	fy= vec4[1];
	fw= vec4[3];

	dx= StrictMath.abs(x-(wx)*fx/fw);
	if(dx==0) dx= StrictMath.abs(y-(wy)*fy/fw);

	gl.glDepthMask(false);		// disable write in zbuffer

	/* check zoom out */
	Resources.UI_ThemeColor(gl, Resources.TH_GRID);

	if(dx<6.0f) {
		//v3d.gridview*= sublines;
		rv3d.gridview*= sublines;
		dx*= sublines;

		if(dx<6.0f) {
			//v3d.gridview*= sublines;
			rv3d.gridview*= sublines;
			dx*= sublines;

			if(dx<6.0f) {
				//v3d.gridview*= sublines;
				rv3d.gridview*= sublines;
				dx*=sublines;
				if(dx<6.0f);
				else {
					Resources.UI_ThemeColor(gl,Resources.TH_GRID);
					drawgrid_draw(gl, ar, wx, wy, x, y, dx);
				}
			}
			else {	// start blending out
				Resources.UI_ThemeColorBlend(gl, Resources.TH_BACK, Resources.TH_GRID, dx/60.0f);
				drawgrid_draw(gl, ar, wx, wy, x, y, dx);

				Resources.UI_ThemeColor(gl, Resources.TH_GRID);
				drawgrid_draw(gl, ar, wx, wy, x, y, sublines*dx);
			}
		}
		else {	// start blending out (6 < dx < 60)
			Resources.UI_ThemeColorBlend(gl, Resources.TH_BACK, Resources.TH_GRID, dx/60.0f);
			drawgrid_draw(gl, ar, wx, wy, x, y, dx);

			Resources.UI_ThemeColor(gl, Resources.TH_GRID);
			drawgrid_draw(gl, ar, wx, wy, x, y, sublines*dx);
		}
	}
	else {
		if(dx>60.0f) {		// start blending in
			//v3d.gridview/= sublines;
			rv3d.gridview/= sublines;
			dx/= sublines;
			if(dx>60.0f) {		// start blending in
				//v3d.gridview/= sublines;
				rv3d.gridview/= sublines;
				dx/= sublines;
				if(dx>60.0f) {
					Resources.UI_ThemeColor(gl, Resources.TH_GRID);
					drawgrid_draw(gl, ar, wx, wy, x, y, dx);
				}
				else {
					Resources.UI_ThemeColorBlend(gl, Resources.TH_BACK, Resources.TH_GRID, dx/60.0f);
					drawgrid_draw(gl, ar, wx, wy, x, y, dx);
					Resources.UI_ThemeColor(gl, Resources.TH_GRID);
					drawgrid_draw(gl, ar, wx, wy, x, y, dx*sublines);
				}
			}
			else {
				Resources.UI_ThemeColorBlend(gl, Resources.TH_BACK, Resources.TH_GRID, dx/60.0f);
				drawgrid_draw(gl, ar, wx, wy, x, y, dx);
				Resources.UI_ThemeColor(gl, Resources.TH_GRID);
				drawgrid_draw(gl, ar, wx, wy, x, y, dx*sublines);
			}
		}
		else {
			Resources.UI_ThemeColorBlend(gl, Resources.TH_BACK, Resources.TH_GRID, dx/60.0f);
			drawgrid_draw(gl, ar, wx, wy, x, y, dx);
			Resources.UI_ThemeColor(gl, Resources.TH_GRID);
			drawgrid_draw(gl, ar, wx, wy, x, y, dx*sublines);
		}
	}

	x+= (wx);
	y+= (wy);
	Resources.UI_GetThemeColor3ubv(Resources.TH_GRID, col);

	GlUtil.setlinestyle(gl, 0);

	/* center cross */
	if(rv3d.view==View3dTypes.V3D_VIEW_RIGHT || rv3d.view==View3dTypes.V3D_VIEW_LEFT)
		Resources.UI_make_axis_color(col, col2, 'y');
	else
            Resources.UI_make_axis_color(col, col2, 'x');
	gl.glColor3ubv(col2,0);

	GlUtil.fdrawline(gl, 0.0f,  y,  (float)ar.winx,  y);

	if(rv3d.view==View3dTypes.V3D_VIEW_TOP || rv3d.view==View3dTypes.V3D_VIEW_BOTTOM)
		Resources.UI_make_axis_color(col, col2, 'y');
	else
            Resources.UI_make_axis_color(col, col2, 'z');
	gl.glColor3ubv(col2,0);

	GlUtil.fdrawline(gl, x, 0.0f, x, (float)ar.winy);

	gl.glDepthMask(true);		// enable write in zbuffer
}


static void drawfloor(GL2 gl, Scene scene, View3D v3d)
{
	float[] vert = new float[3];
        float grid;
	int a, gridlines, emphasise;
	byte[] col=new byte[3], col2=new byte[3];
	boolean draw_line = false;

	vert[2]= 0.0f;

	if(v3d.gridlines<3) return;

	if(v3d.zbuf!=0 && scene.obedit!=null) gl.glDepthMask(false);	// for zbuffer-select

	gridlines= v3d.gridlines/2;
	grid= gridlines*v3d.grid;

	Resources.UI_GetThemeColor3ubv(Resources.TH_GRID, col);
	Resources.UI_GetThemeColor3ubv(Resources.TH_BACK, col2);

	/* emphasise division lines lighter instead of darker, if background is darker than grid */
	if ( ((col[0]+col[1]+col[2])/3+10) > (col2[0]+col2[1]+col2[2])/3 )
		emphasise = 20;
	else
		emphasise = -10;

	/* draw the Y axis and/or grid lines */
	for(a= -gridlines;a<=gridlines;a++) {
		if(a==0) {
			/* check for the 'show Y axis' preference */
			if ((v3d.gridflag & View3dTypes.V3D_SHOW_Y)!=0) {
				Resources.UI_make_axis_color(col, col2, 'y');
				gl.glColor3ubv(col2,0);

				draw_line = true;
			} else if ((v3d.gridflag & View3dTypes.V3D_SHOW_FLOOR)!=0) {
				Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, emphasise);
			} else {
				draw_line = false;
			}
		} else {
			/* check for the 'show grid floor' preference */
			if ((v3d.gridflag & View3dTypes.V3D_SHOW_FLOOR)!=0) {
				if( (a % 10)==0) {
					Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, emphasise);
				}
				else Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, 10);

				draw_line = true;
			} else {
				draw_line = false;
			}
		}

		if (draw_line) {
			gl.glBegin(GL2.GL_LINE_STRIP);
	        vert[0]= a*v3d.grid;
	        vert[1]= grid;
	        gl.glVertex3fv(vert,0);
	        vert[1]= -grid;
	        gl.glVertex3fv(vert,0);
			gl.glEnd();
		}
	}

	/* draw the X axis and/or grid lines */
	for(a= -gridlines;a<=gridlines;a++) {
		if(a==0) {
			/* check for the 'show X axis' preference */
			if ((v3d.gridflag & View3dTypes.V3D_SHOW_X)!=0) {
				Resources.UI_make_axis_color(col, col2, 'x');
				gl.glColor3ubv(col2,0);

				draw_line = true;
			} else if ((v3d.gridflag & View3dTypes.V3D_SHOW_FLOOR)!=0) {
				Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, emphasise);
			} else {
				draw_line = false;
			}
		} else {
			/* check for the 'show grid floor' preference */
			if ((v3d.gridflag & View3dTypes.V3D_SHOW_FLOOR)!=0) {
				if( (a % 10)==0) {
					Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, emphasise);
				}
				else Resources.UI_ThemeColorShade(gl, Resources.TH_GRID, 10);

				draw_line = true;
			} else {
				draw_line = false;
			}
		}

		if (draw_line) {
			gl.glBegin(GL2.GL_LINE_STRIP);
	        vert[1]= a*v3d.grid;
	        vert[0]= grid;
	        gl.glVertex3fv(vert,0);
	        vert[0]= -grid;
	        gl.glVertex3fv(vert,0);
			gl.glEnd();
		}
	}

	/* draw the Z axis line */
	/* check for the 'show Z axis' preference */
	if ((v3d.gridflag & View3dTypes.V3D_SHOW_Z)!=0) {
		Resources.UI_make_axis_color(col, col2, 'z');
		gl.glColor3ubv(col2,0);

		gl.glBegin(GL2.GL_LINE_STRIP);
		vert[0]= 0;
		vert[1]= 0;
		vert[2]= grid;
		gl.glVertex3fv(vert,0);
		vert[2]= -grid;
		gl.glVertex3fv(vert,0);
		gl.glEnd();
	}

	if(v3d.zbuf!=0 && scene.obedit!=null) gl.glDepthMask(true);

}

//static void drawcursor(Scene *scene, ARegion *ar, View3D *v3d)
//{
//	short mx,my,co[2];
//	int flag;
//
//	/* we dont want the clipping for cursor */
//	flag= v3d.flag;
//	v3d.flag= 0;
//	project_short(ar, give_cursor(scene, v3d), co);
//	v3d.flag= flag;
//
//	mx = co[0];
//	my = co[1];
//
//	if(mx!=IS_CLIPPED) {
//		setlinestyle(0);
//		cpack(0xFF);
//		circ((float)mx, (float)my, 10.0);
//		setlinestyle(4);
//		cpack(0xFFFFFF);
//		circ((float)mx, (float)my, 10.0);
//		setlinestyle(0);
//		cpack(0x0);
//
//		sdrawline(mx-20, my, mx-5, my);
//		sdrawline(mx+5, my, mx+20, my);
//		sdrawline(mx, my-20, mx, my-5);
//		sdrawline(mx, my+5, mx, my+20);
//	}
//}
//
///* Draw a live substitute of the view icon, which is always shown */
//static void draw_view_axis(RegionView3D *rv3d)
//{
//	const float k = U.rvisize;   /* axis size */
//	const float toll = 0.5;      /* used to see when view is quasi-orthogonal */
//	const float start = k + 1.0; /* axis center in screen coordinates, x=y */
//	float ydisp = 0.0;          /* vertical displacement to allow obj info text */
//
//	/* rvibright ranges approx. from original axis icon color to gizmo color */
//	float bright = U.rvibright / 15.0f;
//
//	unsigned char col[3];
//	unsigned char gridcol[3];
//	float colf[3];
//
//	float vec[4];
//	float dx, dy;
//	float h, s, v;
//
//	/* thickness of lines is proportional to k */
//	/*	(log(k)-1) gives a more suitable thickness, but fps decreased by about 3 fps */
//	glLineWidth(k / 10);
//	//glLineWidth(log(k)-1); // a bit slow
//
//	UI_GetThemeColor3ubv(TH_GRID, (char *)gridcol);
//
//	/* X */
//	vec[0] = vec[3] = 1;
//	vec[1] = vec[2] = 0;
//	QuatMulVecf(rv3d.viewquat, vec);
//
//	UI_make_axis_color((char *)gridcol, (char *)col, 'x');
//	rgb_to_hsv(col[0]/255.0f, col[1]/255.0f, col[2]/255.0f, &h, &s, &v);
//	s = s<0.5 ? s+0.5 : 1.0;
//	v = 0.3;
//	v = (v<1.0-(bright) ? v+bright : 1.0);
//	hsv_to_rgb(h, s, v, colf, colf+1, colf+2);
//	glColor3fv(colf);
//
//	dx = vec[0] * k;
//	dy = vec[1] * k;
//	fdrawline(start, start + ydisp, start + dx, start + dy + ydisp);
//	if (fabs(dx) > toll || fabs(dy) > toll) {
//		BLF_draw_default(start + dx + 2, start + dy + ydisp + 2, 0.0f, "x");
//	}
//
//	/* Y */
//	vec[1] = vec[3] = 1;
//	vec[0] = vec[2] = 0;
//	QuatMulVecf(rv3d.viewquat, vec);
//
//	UI_make_axis_color((char *)gridcol, (char *)col, 'y');
//	rgb_to_hsv(col[0]/255.0f, col[1]/255.0f, col[2]/255.0f, &h, &s, &v);
//	s = s<0.5 ? s+0.5 : 1.0;
//	v = 0.3;
//	v = (v<1.0-(bright) ? v+bright : 1.0);
//	hsv_to_rgb(h, s, v, colf, colf+1, colf+2);
//	glColor3fv(colf);
//
//	dx = vec[0] * k;
//	dy = vec[1] * k;
//	fdrawline(start, start + ydisp, start + dx, start + dy + ydisp);
//	if (fabs(dx) > toll || fabs(dy) > toll) {
//		BLF_draw_default(start + dx + 2, start + dy + ydisp + 2, 0.0f, "y");
//	}
//
//	/* Z */
//	vec[2] = vec[3] = 1;
//	vec[1] = vec[0] = 0;
//	QuatMulVecf(rv3d.viewquat, vec);
//
//	UI_make_axis_color((char *)gridcol, (char *)col, 'z');
//	rgb_to_hsv(col[0]/255.0f, col[1]/255.0f, col[2]/255.0f, &h, &s, &v);
//	s = s<0.5 ? s+0.5 : 1.0;
//	v = 0.5;
//	v = (v<1.0-(bright) ? v+bright : 1.0);
//	hsv_to_rgb(h, s, v, colf, colf+1, colf+2);
//	glColor3fv(colf);
//
//	dx = vec[0] * k;
//	dy = vec[1] * k;
//	fdrawline(start, start + ydisp, start + dx, start + dy + ydisp);
//	if (fabs(dx) > toll || fabs(dy) > toll) {
//		BLF_draw_default(start + dx + 2, start + dy + ydisp + 2, 0.0f, "z");
//	}
//
//	/* restore line-width */
//	glLineWidth(1.0);
//}
//
//
//static void draw_view_icon(RegionView3D *rv3d)
//{
//	BIFIconID icon;
//
//	if( ELEM(rv3d.view, V3D_VIEW_TOP, V3D_VIEW_BOTTOM))
//		icon= ICON_AXIS_TOP;
//	else if( ELEM(rv3d.view, V3D_VIEW_FRONT, V3D_VIEW_BACK))
//		icon= ICON_AXIS_FRONT;
//	else if( ELEM(rv3d.view, V3D_VIEW_RIGHT, V3D_VIEW_LEFT))
//		icon= ICON_AXIS_SIDE;
//	else return ;
//
//	glEnable(GL_BLEND);
//	glBlendFunc(GL_SRC_ALPHA,  GL_ONE_MINUS_SRC_ALPHA);
//
//	UI_icon_draw(5.0, 5.0, icon);
//
//	glDisable(GL_BLEND);
//}
//
//static char *view3d_get_name(View3D *v3d, RegionView3D *rv3d)
//{
//	char *name = NULL;
//
//	switch (rv3d.view) {
//		case V3D_VIEW_FRONT:
//			if (rv3d.persp == V3D_ORTHO) name = "Front Ortho";
//			else name = "Front Persp";
//			break;
//		case V3D_VIEW_BACK:
//			if (rv3d.persp == V3D_ORTHO) name = "Back Ortho";
//			else name = "Back Persp";
//			break;
//		case V3D_VIEW_TOP:
//			if (rv3d.persp == V3D_ORTHO) name = "Top Ortho";
//			else name = "Top Persp";
//			break;
//		case V3D_VIEW_BOTTOM:
//			if (rv3d.persp == V3D_ORTHO) name = "Bottom Ortho";
//			else name = "Bottom Persp";
//			break;
//		case V3D_VIEW_RIGHT:
//			if (rv3d.persp == V3D_ORTHO) name = "Right Ortho";
//			else name = "Right Persp";
//			break;
//		case V3D_VIEW_LEFT:
//			if (rv3d.persp == V3D_ORTHO) name = "Left Ortho";
//			else name = "Left Persp";
//			break;
//
//		default:
//			if (rv3d.persp==V3D_CAMOB) {
//				if ((v3d.camera) && (v3d.camera.type == OB_CAMERA)) {
//					Camera *cam;
//					cam = v3d.camera.data;
//					name = (cam.type != CAM_ORTHO) ? "Camera Persp" : "Camera Ortho";
//				} else {
//					name = "Object as Camera";
//				}
//			} else {
//				name = (rv3d.persp == V3D_ORTHO) ? "User Ortho" : "User Persp";
//			}
//			break;
//	}
//
//	return name;
//}
//
//static void draw_viewport_name(ARegion *ar, View3D *v3d)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	char *name = view3d_get_name(v3d, rv3d);
//	char *printable = NULL;
//
//	if (v3d.localview) {
//		printable = malloc(strlen(name) + strlen(" (Local)_")); /* '_' gives space for '\0' */
//												 strcpy(printable, name);
//												 strcat(printable, " (Local)");
//	} else {
//		printable = name;
//	}
//
//	if (printable) {
//		UI_ThemeColor(TH_TEXT_HI);
//		BLF_draw_default(10,  ar.winy-20, 0.0f, printable);
//	}
//
//	if (v3d.localview) {
//		free(printable);
//	}
//}
//
//
//static char *get_cfra_marker_name(Scene *scene)
//{
//	ListBase *markers= &scene.markers;
//	TimeMarker *m1, *m2;
//
//	/* search through markers for match */
//	for (m1=markers.first, m2=markers.last; m1 && m2; m1=m1.next, m2=m2.prev) {
//		if (m1.frame==CFRA)
//			return m1.name;
//
//		if (m1 == m2)
//			break;
//
//		if (m2.frame==CFRA)
//			return m2.name;
//	}
//
//	return NULL;
//}
//
///* draw info beside axes in bottom left-corner:
//* 	framenum, object name, bone name (if available), marker name (if available)
//*/
//static void draw_selected_name(Scene *scene, Object *ob, View3D *v3d)
//{
//	char info[256], *markern;
//	short offset=30;
//
//	/* get name of marker on current frame (if available) */
//	markern= get_cfra_marker_name(scene);
//
//	/* check if there is an object */
//	if(ob) {
//		/* name(s) to display depends on type of object */
//		if(ob.type==OB_ARMATURE) {
//			bArmature *arm= ob.data;
//			char *name= NULL;
//
//			/* show name of active bone too (if possible) */
//			if(arm.edbo) {
//				EditBone *ebo;
//				for (ebo=arm.edbo.first; ebo; ebo=ebo.next){
//					if ((ebo.flag & BONE_ACTIVE) && (ebo.layer & arm.layer)) {
//						name= ebo.name;
//						break;
//					}
//				}
//			}
//			else if(ob.pose && (ob.flag & OB_POSEMODE)) {
//				bPoseChannel *pchan;
//				for(pchan= ob.pose.chanbase.first; pchan; pchan= pchan.next) {
//					if((pchan.bone.flag & BONE_ACTIVE) && (pchan.bone.layer & arm.layer)) {
//						name= pchan.name;
//						break;
//					}
//				}
//			}
//			if(name && markern)
//				sprintf(info, "(%d) %s %s <%s>", CFRA, ob.id.name+2, name, markern);
//			else if(name)
//				sprintf(info, "(%d) %s %s", CFRA, ob.id.name+2, name);
//			else
//				sprintf(info, "(%d) %s", CFRA, ob.id.name+2);
//		}
//		else if(ELEM3(ob.type, OB_MESH, OB_LATTICE, OB_CURVE)) {
//			Key *key= NULL;
//			KeyBlock *kb = NULL;
//			char shapes[75];
//
//			/* try to display active shapekey too */
//			shapes[0] = 0;
//			key = ob_get_key(ob);
//			if(key){
//				kb = BLI_findlink(&key.block, ob.shapenr-1);
//				if(kb){
//					sprintf(shapes, ": %s ", kb.name);
//					if(ob.shapeflag == OB_SHAPE_LOCK){
//						sprintf(shapes, "%s (Pinned)",shapes);
//					}
//				}
//			}
//
//			if(markern)
//				sprintf(info, "(%d) %s %s <%s>", CFRA, ob.id.name+2, shapes, markern);
//			else
//				sprintf(info, "(%d) %s %s", CFRA, ob.id.name+2, shapes);
//		}
//		else {
//			/* standard object */
//			if (markern)
//				sprintf(info, "(%d) %s <%s>", CFRA, ob.id.name+2, markern);
//			else
//				sprintf(info, "(%d) %s", CFRA, ob.id.name+2);
//		}
//
//		/* colour depends on whether there is a keyframe */
//		if (id_frame_has_keyframe((ID *)ob, /*frame_to_float(scene, CFRA)*/(float)(CFRA), v3d.keyflags))
//			UI_ThemeColor(TH_VERTEX_SELECT);
//		else
//			UI_ThemeColor(TH_TEXT_HI);
//	}
//	else {
//		/* no object */
//		if (markern)
//			sprintf(info, "(%d) <%s>", CFRA, markern);
//		else
//			sprintf(info, "(%d)", CFRA);
//
//		/* colour is always white */
//		UI_ThemeColor(TH_TEXT_HI);
//	}
//
//	if (U.uiflag & USER_SHOW_ROTVIEWICON)
//		offset = 14 + (U.rvisize * 2);
//
//	BLF_draw_default(offset,  10, 0.0f, info);
//}

static void view3d_get_viewborder_size(Scene scene, ARegion ar, float[] size_r)
{
	float winmax= UtilDefines.MAX2(ar.winx, ar.winy);
	float aspect= (float) (scene.r.xsch*scene.r.xasp)/(scene.r.ysch*scene.r.yasp);

	if(aspect>1.0f) {
		size_r[0]= winmax;
		size_r[1]= winmax/aspect;
	} else {
		size_r[0]= winmax*aspect;
		size_r[1]= winmax;
	}
}

public static void calc_viewborder(Scene scene, ARegion ar, View3D v3d, rctf viewborder_r)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float zoomfac;
        float[] size=new float[2];
	float dx= 0.0f, dy= 0.0f;

	view3d_get_viewborder_size(scene, ar, size);

	/* magic zoom calculation, no idea what
		* it signifies, if you find out, tell me! -zr
		*/
	/* simple, its magic dude!
		* well, to be honest, this gives a natural feeling zooming
		* with multiple keypad presses (ton)
		*/

	zoomfac= (Arithb.M_SQRT2 + rv3d.camzoom/50.0f);
	zoomfac= (zoomfac*zoomfac)*0.25f;

	size[0]= size[0]*zoomfac;
	size[1]= size[1]*zoomfac;

	/* center in window */
	viewborder_r.xmin= 0.5f*ar.winx - 0.5f*size[0];
	viewborder_r.ymin= 0.5f*ar.winy - 0.5f*size[1];
	viewborder_r.xmax= viewborder_r.xmin + size[0];
	viewborder_r.ymax= viewborder_r.ymin + size[1];

	dx= ar.winx*rv3d.camdx*zoomfac*2.0f;
	dy= ar.winy*rv3d.camdy*zoomfac*2.0f;

	/* apply offset */
	viewborder_r.xmin-= dx;
	viewborder_r.ymin-= dy;
	viewborder_r.xmax-= dx;
	viewborder_r.ymax-= dy;

	if(v3d.camera!=null && v3d.camera.type==ObjectTypes.OB_CAMERA) {
		Camera cam= (Camera)v3d.camera.data;
		float w = viewborder_r.xmax - viewborder_r.xmin;
		float h = viewborder_r.ymax - viewborder_r.ymin;
		float side = UtilDefines.MAX2(w, h);

		viewborder_r.xmin+= cam.shiftx*side;
		viewborder_r.xmax+= cam.shiftx*side;
		viewborder_r.ymin+= cam.shifty*side;
		viewborder_r.ymax+= cam.shifty*side;
	}
}

//void view3d_set_1_to_1_viewborder(Scene *scene, ARegion *ar)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	float size[2];
//	int im_width= (scene.r.size*scene.r.xsch)/100;
//
//	view3d_get_viewborder_size(scene, ar, size);
//
//	rv3d.camzoom= (sqrt(4.0*im_width/size[0]) - M_SQRT2)*50.0;
//	rv3d.camzoom= CLAMPIS(rv3d.camzoom, -30, 300);
//}
//
//
//static void drawviewborder_flymode(ARegion *ar)
//{
//	/* draws 4 edge brackets that frame the safe area where the
//	mouse can move during fly mode without spinning the view */
//	float x1, x2, y1, y2;
//
//	x1= 0.45*(float)ar.winx;
//	y1= 0.45*(float)ar.winy;
//	x2= 0.55*(float)ar.winx;
//	y2= 0.55*(float)ar.winy;
//	cpack(0);
//
//
//	glBegin(GL_LINES);
//	/* bottom left */
//	glVertex2f(x1,y1);
//	glVertex2f(x1,y1+5);
//
//	glVertex2f(x1,y1);
//	glVertex2f(x1+5,y1);
//
//	/* top right */
//	glVertex2f(x2,y2);
//	glVertex2f(x2,y2-5);
//
//	glVertex2f(x2,y2);
//	glVertex2f(x2-5,y2);
//
//	/* top left */
//	glVertex2f(x1,y2);
//	glVertex2f(x1,y2-5);
//
//	glVertex2f(x1,y2);
//	glVertex2f(x1+5,y2);
//
//	/* bottom right */
//	glVertex2f(x2,y1);
//	glVertex2f(x2,y1+5);
//
//	glVertex2f(x2,y1);
//	glVertex2f(x2-5,y1);
//	glEnd();
//}


static void drawviewborder(GL2 gl, Scene scene, ARegion ar, View3D v3d)
{
//	extern void gl_round_box(int mode, float minx, float miny, float maxx, float maxy, float rad);          // interface_panel.c
	float fac, a;
	float x1, x2, y1, y2;
	float x3, y3, x4, y4;
	rctf viewborder = new rctf();;
	Camera ca= null;

	if(v3d.camera==null)
		return;
	if(v3d.camera.type==ObjectTypes.OB_CAMERA)
		ca = (Camera)v3d.camera.data;

	calc_viewborder(scene, ar, v3d, viewborder);
	x1= viewborder.xmin;
	y1= viewborder.ymin;
	x2= viewborder.xmax;
	y2= viewborder.ymax;

	/* passepartout, specified in camera edit buttons */
	if (ca!=null && (ca.flag & CameraTypes.CAM_SHOWPASSEPARTOUT)!=0 && ca.passepartalpha > 0.000001f) {
		if (ca.passepartalpha == 1.0f) {
			gl.glColor3f(0, 0, 0);
		} else {
			gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA );
			gl.glEnable(GL2.GL_BLEND);
			gl.glColor4f(0, 0, 0, ca.passepartalpha);
		}
		if (x1 > 0.0f)
			gl.glRectf(0.0f, (float)ar.winy, x1, 0.0f);
		if (x2 < (float)ar.winx)
			gl.glRectf(x2, (float)ar.winy, (float)ar.winx, 0.0f);
		if (y2 < (float)ar.winy)
			gl.glRectf(x1, (float)ar.winy, x2, y2);
		if (y2 > 0.0f)
			gl.glRectf(x1, y1, x2, 0.0f);

		gl.glDisable(GL2.GL_BLEND);
	}

	/* edge */
	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

	GlUtil.setlinestyle(gl, 0);
	Resources.UI_ThemeColor(gl, Resources.TH_BACK);
	gl.glRectf(x1, y1, x2, y2);

	GlUtil.setlinestyle(gl, 3);
	Resources.UI_ThemeColor(gl, Resources.TH_WIRE);
	gl.glRectf(x1, y1, x2, y2);

	/* camera name - draw in highlighted text color */
//	if (ca!=null && (ca.flag & CAM_SHOWNAME)) {
//		UI_ThemeColor(TH_TEXT_HI);
//		BLF_draw_default(x1, y1-15, 0.0f, v3d.camera.id.name+2);
//		UI_ThemeColor(TH_WIRE);
//	}


	/* border */
	if((scene.r.mode & SceneTypes.R_BORDER)!=0) {

		GlUtil.cpack(gl, 0);
		x3= x1+ scene.r.border.xmin*(x2-x1);
		y3= y1+ scene.r.border.ymin*(y2-y1);
		x4= x1+ scene.r.border.xmax*(x2-x1);
		y4= y1+ scene.r.border.ymax*(y2-y1);

		GlUtil.cpack(gl, 0x4040FF);
		gl.glRectf(x3,  y3,  x4,  y4);
	}

	/* safety border */
	if (ca!=null && (ca.flag & CameraTypes.CAM_SHOWTITLESAFE)!=0) {
		fac= 0.1f;

		a= fac*(x2-x1);
		x1+= a;
		x2-= a;

		a= fac*(y2-y1);
		y1+= a;
		y2-= a;

		Resources.UI_ThemeColorBlendShade(gl, Resources.TH_WIRE, Resources.TH_BACK, 0.25f, 0);

		UIDraw.uiSetRoundBox(15);
		UIDraw.uiDrawBox(gl, GL2.GL_LINE_LOOP, x1, y1, x2, y2, 12.0f);
	}

	GlUtil.setlinestyle(gl, 0);
	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

}

///* *********************** backdraw for selection *************** */
//
//void backdrawview3d(Scene *scene, ARegion *ar, View3D *v3d)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	struct Base *base;
//
///*for 2.43 release, don't use glext and just define the constant.
//  this to avoid possibly breaking platforms before release.*/
//#ifndef GL_MULTISAMPLE_ARB
//	#define GL_MULTISAMPLE_ARB	0x809D
//#endif
//
//#ifdef GL_MULTISAMPLE_ARB
//	int m;
//#endif
//
//	if(G.f & G_VERTEXPAINT || G.f & G_WEIGHTPAINT || (FACESEL_PAINT_TEST));
//	else if((G.f & G_TEXTUREPAINT) && scene.toolsettings && (scene.toolsettings.imapaint.flag & IMAGEPAINT_PROJECT_DISABLE));
//	else if((G.f & G_PARTICLEEDIT) && v3d.drawtype>OB_WIRE && (v3d.flag & V3D_ZBUF_SELECT));
//	else if(scene.obedit && v3d.drawtype>OB_WIRE && (v3d.flag & V3D_ZBUF_SELECT));
//	else {
//		v3d.flag &= ~V3D_NEEDBACKBUFDRAW;
//		return;
//	}
//
//	if( !(v3d.flag & V3D_NEEDBACKBUFDRAW) ) return;
//
////	if(test) {
////		if(qtest()) {
////			addafterqueue(ar.win, BACKBUFDRAW, 1);
////			return;
////		}
////	}
//
//	/* Disable FSAA for backbuffer selection.
//
//	Only works if GL_MULTISAMPLE_ARB is defined by the header
//	file, which is should be for every OS that supports FSAA.*/
//
//#ifdef GL_MULTISAMPLE_ARB
//	m = glIsEnabled(GL_MULTISAMPLE_ARB);
//	if (m) glDisable(GL_MULTISAMPLE_ARB);
//#endif
//
//	if(v3d.drawtype > OB_WIRE) v3d.zbuf= TRUE;
//
//	glDisable(GL_DITHER);
//
//	glClearColor(0.0, 0.0, 0.0, 0.0);
//	if(v3d.zbuf) {
//		glEnable(GL_DEPTH_TEST);
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//	}
//	else {
//		glClear(GL_COLOR_BUFFER_BIT);
//		glDisable(GL_DEPTH_TEST);
//	}
//
//	if(rv3d.rflag & RV3D_CLIPPING)
//		view3d_set_clipping(rv3d);
//
//	G.f |= G_BACKBUFSEL;
//
//	base= (scene.basact);
//	if(base && (base.lay & v3d.lay)) {
//		draw_object_backbufsel(scene, v3d, rv3d, base.object);
//	}
//
//	v3d.flag &= ~V3D_NEEDBACKBUFDRAW;
//
//	G.f &= ~G_BACKBUFSEL;
//	v3d.zbuf= FALSE;
//	glDisable(GL_DEPTH_TEST);
//	glEnable(GL_DITHER);
//
//	if(rv3d.rflag & RV3D_CLIPPING)
//		view3d_clr_clipping();
//
//#ifdef GL_MULTISAMPLE_ARB
//	if (m) glEnable(GL_MULTISAMPLE_ARB);
//#endif
//
//	/* it is important to end a view in a transform compatible with buttons */
////	persp(PERSP_WIN);  // set ortho
//
//}
//
//void view3d_validate_backbuf(ViewContext *vc)
//{
//	if(vc.v3d.flag & V3D_NEEDBACKBUFDRAW)
//		backdrawview3d(vc.scene, vc.ar, vc.v3d);
//}
//
///* samples a single pixel (copied from vpaint) */
//unsigned int view3d_sample_backbuf(ViewContext *vc, int x, int y)
//{
//	unsigned int col;
//
//	if(x >= vc.ar.winx || y >= vc.ar.winy) return 0;
//	x+= vc.ar.winrct.xmin;
//	y+= vc.ar.winrct.ymin;
//
//	view3d_validate_backbuf(vc);
//
//	glReadPixels(x,  y, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE,  &col);
//	glReadBuffer(GL_BACK);
//
//	if(ENDIAN_ORDER==B_ENDIAN) SWITCH_INT(col);
//
//	return WM_framebuffer_to_index(col);
//}
//
///* reads full rect, converts indices */
//ImBuf *view3d_read_backbuf(ViewContext *vc, short xmin, short ymin, short xmax, short ymax)
//{
//	unsigned int *dr, *rd;
//	struct ImBuf *ibuf, *ibuf1;
//	int a;
//	short xminc, yminc, xmaxc, ymaxc, xs, ys;
//
//	/* clip */
//	if(xmin<0) xminc= 0; else xminc= xmin;
//	if(xmax >= vc.ar.winx) xmaxc= vc.ar.winx-1; else xmaxc= xmax;
//	if(xminc > xmaxc) return NULL;
//
//	if(ymin<0) yminc= 0; else yminc= ymin;
//	if(ymax >= vc.ar.winy) ymaxc= vc.ar.winy-1; else ymaxc= ymax;
//	if(yminc > ymaxc) return NULL;
//
//	ibuf= IMB_allocImBuf((xmaxc-xminc+1), (ymaxc-yminc+1), 32, IB_rect,0);
//
//	view3d_validate_backbuf(vc);
//
//	glReadPixels(vc.ar.winrct.xmin+xminc, vc.ar.winrct.ymin+yminc, (xmaxc-xminc+1), (ymaxc-yminc+1), GL_RGBA, GL_UNSIGNED_BYTE, ibuf.rect);
//	glReadBuffer(GL_BACK);
//
//	if(ENDIAN_ORDER==B_ENDIAN) IMB_convert_rgba_to_abgr(ibuf);
//
//	a= (xmaxc-xminc+1)*(ymaxc-yminc+1);
//	dr= ibuf.rect;
//	while(a--) {
//		if(*dr) *dr= WM_framebuffer_to_index(*dr);
//		dr++;
//	}
//
//	/* put clipped result back, if needed */
//	if(xminc==xmin && xmaxc==xmax && yminc==ymin && ymaxc==ymax)
//		return ibuf;
//
//	ibuf1= IMB_allocImBuf( (xmax-xmin+1),(ymax-ymin+1),32,IB_rect,0);
//	rd= ibuf.rect;
//	dr= ibuf1.rect;
//
//	for(ys= ymin; ys<=ymax; ys++) {
//		for(xs= xmin; xs<=xmax; xs++, dr++) {
//			if( xs>=xminc && xs<=xmaxc && ys>=yminc && ys<=ymaxc) {
//				*dr= *rd;
//				rd++;
//			}
//		}
//	}
//	IMB_freeImBuf(ibuf);
//	return ibuf1;
//}
//
///* smart function to sample a rect spiralling outside, nice for backbuf selection */
//unsigned int view3d_sample_backbuf_rect(ViewContext *vc, short mval[2], int size,
//										unsigned int min, unsigned int max, int *dist, short strict,
//										void *handle, unsigned int (*indextest)(void *handle, unsigned int index))
//{
//	struct ImBuf *buf;
//	unsigned int *bufmin, *bufmax, *tbuf;
//	int minx, miny;
//	int a, b, rc, nr, amount, dirvec[4][2];
//	int distance=0;
//	unsigned int index = 0;
//	short indexok = 0;
//
//	amount= (size-1)/2;
//
//	minx = mval[0]-(amount+1);
//	miny = mval[1]-(amount+1);
//	buf = view3d_read_backbuf(vc, minx, miny, minx+size-1, miny+size-1);
//	if (!buf) return 0;
//
//	rc= 0;
//
//	dirvec[0][0]= 1; dirvec[0][1]= 0;
//	dirvec[1][0]= 0; dirvec[1][1]= -size;
//	dirvec[2][0]= -1; dirvec[2][1]= 0;
//	dirvec[3][0]= 0; dirvec[3][1]= size;
//
//	bufmin = buf.rect;
//	tbuf = buf.rect;
//	bufmax = buf.rect + size*size;
//	tbuf+= amount*size+ amount;
//
//	for(nr=1; nr<=size; nr++) {
//
//		for(a=0; a<2; a++) {
//			for(b=0; b<nr; b++, distance++) {
//				if (*tbuf && *tbuf>=min && *tbuf<max) { //we got a hit
//					if(strict){
//						indexok =  indextest(handle, *tbuf - min+1);
//						if(indexok){
//							*dist= (short) sqrt( (float)distance   );
//							index = *tbuf - min+1;
//							goto exit;
//						}
//					}
//					else{
//						*dist= (short) sqrt( (float)distance ); // XXX, this distance is wrong -
//						index = *tbuf - min+1; // messy yah, but indices start at 1
//						goto exit;
//					}
//				}
//
//				tbuf+= (dirvec[rc][0]+dirvec[rc][1]);
//
//				if(tbuf<bufmin || tbuf>=bufmax) {
//					goto exit;
//				}
//			}
//			rc++;
//			rc &= 3;
//		}
//	}
//
//exit:
//	IMB_freeImBuf(buf);
//	return index;
//}
//
//
///* ************************************************************* */
//
//static void draw_bgpic(Scene *scene, ARegion *ar, View3D *v3d)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	BGpic *bgpic;
//	Image *ima;
//	ImBuf *ibuf= NULL;
//	float vec[4], fac, asp, zoomx, zoomy;
//	float x1, y1, x2, y2, cx, cy;
//
//	bgpic= v3d.bgpic;
//	if(bgpic==NULL) return;
//
//	ima= bgpic.ima;
//
//	if(ima)
//		ibuf= BKE_image_get_ibuf(ima, &bgpic.iuser);
//	if(ibuf==NULL || (ibuf.rect==NULL && ibuf.rect_float==NULL) )
//		return;
//	if(ibuf.channels!=4)
//		return;
//	if(ibuf.rect==NULL)
//		IMB_rect_from_float(ibuf);
//
//	if(rv3d.persp==2) {
//		rctf vb;
//
//		calc_viewborder(scene, ar, v3d, &vb);
//
//		x1= vb.xmin;
//		y1= vb.ymin;
//		x2= vb.xmax;
//		y2= vb.ymax;
//	}
//	else {
//		float sco[2];
//
//		/* calc window coord */
//		initgrabz(rv3d, 0.0, 0.0, 0.0);
//		window_to_3d_delta(ar, vec, 1, 0);
//		fac= MAX3( fabs(vec[0]), fabs(vec[1]), fabs(vec[1]) );
//		fac= 1.0/fac;
//
//		asp= ( (float)ibuf.y)/(float)ibuf.x;
//
//		vec[0] = vec[1] = vec[2] = 0.0;
//		view3d_project_float(ar, vec, sco, rv3d.persmat);
//		cx = sco[0];
//		cy = sco[1];
//
//		x1=  cx+ fac*(bgpic.xof-bgpic.size);
//		y1=  cy+ asp*fac*(bgpic.yof-bgpic.size);
//		x2=  cx+ fac*(bgpic.xof+bgpic.size);
//		y2=  cy+ asp*fac*(bgpic.yof+bgpic.size);
//	}
//
//	/* complete clip? */
//
//	if(x2 < 0 ) return;
//	if(y2 < 0 ) return;
//	if(x1 > ar.winx ) return;
//	if(y1 > ar.winy ) return;
//
//	zoomx= (x2-x1)/ibuf.x;
//	zoomy= (y2-y1)/ibuf.y;
//
//	/* for some reason; zoomlevels down refuses to use GL_ALPHA_SCALE */
//	if(zoomx < 1.0f || zoomy < 1.0f) {
//		float tzoom= MIN2(zoomx, zoomy);
//		int mip= 0;
//
//		if(ibuf.mipmap[0]==NULL)
//			IMB_makemipmap(ibuf, 0, 0);
//
//		while(tzoom < 1.0f && mip<8 && ibuf.mipmap[mip]) {
//			tzoom*= 2.0f;
//			zoomx*= 2.0f;
//			zoomy*= 2.0f;
//			mip++;
//		}
//		if(mip>0)
//			ibuf= ibuf.mipmap[mip-1];
//	}
//
//	if(v3d.zbuf) glDisable(GL_DEPTH_TEST);
//
//	glBlendFunc(GL_SRC_ALPHA,  GL_ONE_MINUS_SRC_ALPHA);
//
//	glMatrixMode(GL_PROJECTION);
//	glPushMatrix();
//	glMatrixMode(GL_MODELVIEW);
//	glPushMatrix();
//
//	glaDefine2DArea(&ar.winrct);
//
//	glEnable(GL_BLEND);
//
//	glPixelZoom(zoomx, zoomy);
//	glColor4f(1.0, 1.0, 1.0, 1.0-bgpic.blend);
//	glaDrawPixelsTex(x1, y1, ibuf.x, ibuf.y, GL_UNSIGNED_BYTE, ibuf.rect);
//
//	glPixelZoom(1.0, 1.0);
//	glPixelTransferf(GL_ALPHA_SCALE, 1.0f);
//
//	glMatrixMode(GL_PROJECTION);
//	glPopMatrix();
//	glMatrixMode(GL_MODELVIEW);
//	glPopMatrix();
//
//	glDisable(GL_BLEND);
//	if(v3d.zbuf) glEnable(GL_DEPTH_TEST);
//}
//
///* ****************** View3d afterdraw *************** */
//
//typedef struct View3DAfter {
//	struct View3DAfter *next, *prev;
//	struct Base *base;
//	int type, flag;
//} View3DAfter;
//
///* temp storage of Objects that need to be drawn as last */
//void add_view3d_after(View3D *v3d, Base *base, int type, int flag)
//{
//	View3DAfter *v3da= MEM_callocN(sizeof(View3DAfter), "View 3d after");
//
//	BLI_addtail(&v3d.afterdraw, v3da);
//	v3da.base= base;
//	v3da.type= type;
//	v3da.flag= flag;
//}
//
///* clears zbuffer and draws it over */
//static void view3d_draw_xray(Scene *scene, ARegion *ar, View3D *v3d, int clear)
//{
//	View3DAfter *v3da, *next;
//	int doit= 0;
//
//	for(v3da= v3d.afterdraw.first; v3da; v3da= v3da.next)
//		if(v3da.type==V3D_XRAY) doit= 1;
//
//	if(doit) {
//		if(clear && v3d.zbuf) glClear(GL_DEPTH_BUFFER_BIT);
//		v3d.xray= TRUE;
//
//		for(v3da= v3d.afterdraw.first; v3da; v3da= next) {
//			next= v3da.next;
//			if(v3da.type==V3D_XRAY) {
//				draw_object(scene, ar, v3d, v3da.base, v3da.flag);
//				BLI_remlink(&v3d.afterdraw, v3da);
//				MEM_freeN(v3da);
//			}
//		}
//		v3d.xray= FALSE;
//	}
//}
//
///* disables write in zbuffer and draws it over */
//static void view3d_draw_transp(Scene *scene, ARegion *ar, View3D *v3d)
//{
//	View3DAfter *v3da, *next;
//
//	glDepthMask(0);
//	v3d.transp= TRUE;
//
//	for(v3da= v3d.afterdraw.first; v3da; v3da= next) {
//		next= v3da.next;
//		if(v3da.type==V3D_TRANSP) {
//			draw_object(scene, ar, v3d, v3da.base, v3da.flag);
//			BLI_remlink(&v3d.afterdraw, v3da);
//			MEM_freeN(v3da);
//		}
//	}
//	v3d.transp= FALSE;
//
//	glDepthMask(1);
//
//}
//
///* *********************** */
//
///*
//	In most cases call draw_dupli_objects,
//	draw_dupli_objects_color was added because when drawing set dupli's
//	we need to force the color
// */
//static void draw_dupli_objects_color(Scene *scene, ARegion *ar, View3D *v3d, Base *base, int color)
//{
//	RegionView3D *rv3d= ar.regiondata;
//	ListBase *lb;
//	DupliObject *dob;
//	Base tbase;
//	BoundBox *bb= NULL;
//	GLuint displist=0;
//	short transflag, use_displist= -1;	/* -1 is initialize */
//	char dt, dtx;
//
//	if (base.object.restrictflag & OB_RESTRICT_VIEW) return;
//
//	tbase.flag= OB_FROMDUPLI|base.flag;
//	lb= object_duplilist(scene, base.object);
//
//	for(dob= lb.first; dob; dob= dob.next) {
//		if(dob.no_draw);
//		else {
//			tbase.object= dob.ob;
//
//			/* extra service: draw the duplicator in drawtype of parent */
//			/* MIN2 for the drawtype to allow bounding box objects in groups for lods */
//			dt= tbase.object.dt;	tbase.object.dt= MIN2(tbase.object.dt, base.object.dt);
//			dtx= tbase.object.dtx; tbase.object.dtx= base.object.dtx;
//
//			/* negative scale flag has to propagate */
//			transflag= tbase.object.transflag;
//			if(base.object.transflag & OB_NEG_SCALE)
//				tbase.object.transflag ^= OB_NEG_SCALE;
//
//			UI_ThemeColorBlend(color, TH_BACK, 0.5);
//
//			/* generate displist, test for new object */
//			if(use_displist==1 && dob.prev && dob.prev.ob!=dob.ob) {
//				use_displist= -1;
//				glDeleteLists(displist, 1);
//			}
//			/* generate displist */
//			if(use_displist == -1) {
//
//				/* lamp drawing messes with matrices, could be handled smarter... but this works */
//				if(dob.ob.type==OB_LAMP || dob.type==OB_DUPLIGROUP)
//					use_displist= 0;
//				else {
//					/* disable boundbox check for list creation */
//					object_boundbox_flag(dob.ob, OB_BB_DISABLED, 1);
//					/* need this for next part of code */
//					bb= object_get_boundbox(dob.ob);
//
//					Mat4One(dob.ob.obmat);	/* obmat gets restored */
//
//					displist= glGenLists(1);
//					glNewList(displist, GL_COMPILE);
//					draw_object(scene, ar, v3d, &tbase, DRAW_CONSTCOLOR);
//					glEndList();
//
//					use_displist= 1;
//					object_boundbox_flag(dob.ob, OB_BB_DISABLED, 0);
//				}
//			}
//			if(use_displist) {
//				wmMultMatrix(dob.mat);
//				if(boundbox_clip(rv3d, dob.mat, bb))
//					glCallList(displist);
//				wmLoadMatrix(rv3d.viewmat);
//			}
//			else {
//				Mat4CpyMat4(dob.ob.obmat, dob.mat);
//				draw_object(scene, ar, v3d, &tbase, DRAW_CONSTCOLOR);
//			}
//
//			tbase.object.dt= dt;
//			tbase.object.dtx= dtx;
//			tbase.object.transflag= transflag;
//		}
//	}
//
//	/* Transp afterdraw disabled, afterdraw only stores base pointers, and duplis can be same obj */
//
//	free_object_duplilist(lb);	/* does restore */
//
//	if(use_displist)
//		glDeleteLists(displist, 1);
//}
//
//static void draw_dupli_objects(Scene *scene, ARegion *ar, View3D *v3d, Base *base)
//{
//	/* define the color here so draw_dupli_objects_color can be called
//	* from the set loop */
//
//	int color= (base.flag & SELECT)?TH_SELECT:TH_WIRE;
//	/* debug */
//	if(base.object.dup_group && base.object.dup_group.id.us<1)
//		color= TH_REDALERT;
//
//	draw_dupli_objects_color(scene, ar, v3d, base, color);
//}
//
//
//void view3d_update_depths(ARegion *ar, View3D *v3d)
//{
//	RegionView3D *rv3d= ar.regiondata;
//
//	/* Create storage for, and, if necessary, copy depth buffer */
//	if(!rv3d.depths) rv3d.depths= MEM_callocN(sizeof(ViewDepths),"ViewDepths");
//	if(rv3d.depths) {
//		ViewDepths *d= rv3d.depths;
//		if(d.w != ar.winx ||
//		   d.h != ar.winy ||
//		   !d.depths) {
//			d.w= ar.winx;
//			d.h= ar.winy;
//			if(d.depths)
//				MEM_freeN(d.depths);
//			d.depths= MEM_mallocN(sizeof(float)*d.w*d.h,"View depths");
//			d.damaged= 1;
//		}
//
//		if(d.damaged) {
//			glReadPixels(ar.winrct.xmin,ar.winrct.ymin,d.w,d.h,
//						 GL_DEPTH_COMPONENT,GL_FLOAT, d.depths);
//
//			glGetDoublev(GL_DEPTH_RANGE,d.depth_range);
//
//			d.damaged= 0;
//		}
//	}
//}
//
///* Enable sculpting in wireframe mode by drawing sculpt object only to the depth buffer */
//static void draw_sculpt_depths(Scene *scene, ARegion *ar, View3D *v3d)
//{
//	Object *ob = OBACT;
//
//	int dt= MIN2(v3d.drawtype, ob.dt);
//	if(v3d.zbuf==0 && dt>OB_WIRE)
//		dt= OB_WIRE;
//	if(dt == OB_WIRE) {
//		GLboolean depth_on;
//		int orig_vdt = v3d.drawtype;
//		int orig_zbuf = v3d.zbuf;
//		int orig_odt = ob.dt;
//
//		glGetBooleanv(GL_DEPTH_TEST, &depth_on);
//		v3d.drawtype = ob.dt = OB_SOLID;
//		v3d.zbuf = 1;
//
//		glColorMask(GL_FALSE, GL_FALSE, GL_FALSE, GL_FALSE);
//		glEnable(GL_DEPTH_TEST);
//		draw_object(scene, ar, v3d, BASACT, 0);
//		glColorMask(GL_TRUE, GL_TRUE, GL_TRUE, GL_TRUE);
//		if(!depth_on)
//			glDisable(GL_DEPTH_TEST);
//
//		v3d.drawtype = orig_vdt;
//		v3d.zbuf = orig_zbuf;
//		ob.dt = orig_odt;
//	}
//}
//
//void draw_depth(Scene *scene, ARegion *ar, View3D *v3d, int (* func)(void *))
//{
//	RegionView3D *rv3d= ar.regiondata;
//	Base *base;
//	Scene *sce;
//	short zbuf, flag;
//	float glalphaclip;
//	/* temp set drawtype to solid */
//
//	/* Setting these temporarily is not nice */
//	zbuf = v3d.zbuf;
//	flag = v3d.flag;
//	glalphaclip = U.glalphaclip;
//
//	U.glalphaclip = 0.5; /* not that nice but means we wont zoom into billboards */
//	v3d.flag &= ~V3D_SELECT_OUTLINE;
//
//	setwinmatrixview3d(ar, v3d, NULL);	/* 0= no pick rect */
//	setviewmatrixview3d(scene, v3d, rv3d);	/* note: calls where_is_object for camera... */
//
//	Mat4MulMat4(rv3d.persmat, rv3d.viewmat, rv3d.winmat);
//	Mat4Invert(rv3d.persinv, rv3d.persmat);
//	Mat4Invert(rv3d.viewinv, rv3d.viewmat);
//
//	glClear(GL_DEPTH_BUFFER_BIT);
//
//	wmLoadMatrix(rv3d.viewmat);
////	persp(PERSP_STORE);  // store correct view for persp(PERSP_VIEW) calls
//
//	if(rv3d.rflag & RV3D_CLIPPING) {
//		view3d_set_clipping(rv3d);
//	}
//
//	v3d.zbuf= TRUE;
//	glEnable(GL_DEPTH_TEST);
//
//	/* draw set first */
//	if(scene.set) {
//		for(SETLOOPER(scene.set, base)) {
//			if(v3d.lay & base.lay) {
//				if (func == NULL || func(base)) {
//					draw_object(scene, ar, v3d, base, 0);
//					if(base.object.transflag & OB_DUPLI) {
//						draw_dupli_objects_color(scene, ar, v3d, base, TH_WIRE);
//					}
//				}
//			}
//		}
//	}
//
//	for(base= scene.base.first; base; base= base.next) {
//		if(v3d.lay & base.lay) {
//			if (func == NULL || func(base)) {
//				/* dupli drawing */
//				if(base.object.transflag & OB_DUPLI) {
//					draw_dupli_objects(scene, ar, v3d, base);
//				}
//				draw_object(scene, ar, v3d, base, 0);
//			}
//		}
//	}
//
//	/* this isnt that nice, draw xray objects as if they are normal */
//	if (v3d.afterdraw.first) {
//		View3DAfter *v3da, *next;
//		int num = 0;
//		v3d.xray= TRUE;
//
//		glDepthFunc(GL_ALWAYS); /* always write into the depth bufer, overwriting front z values */
//		for(v3da= v3d.afterdraw.first; v3da; v3da= next) {
//			next= v3da.next;
//			if(v3da.type==V3D_XRAY) {
//				draw_object(scene, ar, v3d, v3da.base, 0);
//				num++;
//			}
//			/* dont remove this time */
//		}
//		v3d.xray= FALSE;
//
//		glDepthFunc(GL_LEQUAL); /* Now write the depth buffer normally */
//		for(v3da= v3d.afterdraw.first; v3da; v3da= next) {
//			next= v3da.next;
//			if(v3da.type==V3D_XRAY) {
//				v3d.xray= TRUE; v3d.transp= FALSE;
//			} else if (v3da.type==V3D_TRANSP) {
//				v3d.xray= FALSE; v3d.transp= TRUE;
//			}
//
//			draw_object(scene, ar, v3d, v3da.base, 0); /* Draw Xray or Transp objects normally */
//			BLI_remlink(&v3d.afterdraw, v3da);
//			MEM_freeN(v3da);
//		}
//		v3d.xray= FALSE;
//		v3d.transp= FALSE;
//	}
//
//	v3d.zbuf = zbuf;
//	U.glalphaclip = glalphaclip;
//	v3d.flag = flag;
//}
//
//typedef struct View3DShadow {
//	struct View3DShadow *next, *prev;
//	GPULamp *lamp;
//} View3DShadow;
//
//static void gpu_render_lamp_update(Scene *scene, View3D *v3d, Object *ob, Object *par, float obmat[][4], ListBase *shadows)
//{
//	GPULamp *lamp;
//	Lamp *la = (Lamp*)ob.data;
//	View3DShadow *shadow;
//
//	lamp = GPU_lamp_from_blender(scene, ob, par);
//
//	if(lamp) {
//		GPU_lamp_update(lamp, ob.lay, obmat);
//		GPU_lamp_update_colors(lamp, la.r, la.g, la.b, la.energy);
//
//		if((ob.lay & v3d.lay) && GPU_lamp_has_shadow_buffer(lamp)) {
//			shadow= MEM_callocN(sizeof(View3DShadow), "View3DShadow");
//			shadow.lamp = lamp;
//			BLI_addtail(shadows, shadow);
//		}
//	}
//}
//
//static void gpu_update_lamps_shadows(Scene *scene, View3D *v3d)
//{
//	ListBase shadows;
//	View3DShadow *shadow;
//	Scene *sce;
//	Base *base;
//	Object *ob;
//
//	shadows.first= shadows.last= NULL;
//
//	/* update lamp transform and gather shadow lamps */
//	for(SETLOOPER(scene, base)) {
//		ob= base.object;
//
//		if(ob.type == OB_LAMP)
//			gpu_render_lamp_update(scene, v3d, ob, NULL, ob.obmat, &shadows);
//
//		if (ob.transflag & OB_DUPLI) {
//			DupliObject *dob;
//			ListBase *lb = object_duplilist(scene, ob);
//
//			for(dob=lb.first; dob; dob=dob.next)
//				if(dob.ob.type==OB_LAMP)
//					gpu_render_lamp_update(scene, v3d, dob.ob, ob, dob.mat, &shadows);
//
//			free_object_duplilist(lb);
//		}
//	}
//
//	/* render shadows after updating all lamps, nested object_duplilist
//		* don't work correct since it's replacing object matrices */
//	for(shadow=shadows.first; shadow; shadow=shadow.next) {
//		/* this needs to be done better .. */
//		float viewmat[4][4], winmat[4][4];
//		int drawtype, lay, winsize, flag2;
//
//		drawtype= v3d.drawtype;
//		lay= v3d.lay;
//		flag2= v3d.flag2 & V3D_SOLID_TEX;
//
//		v3d.drawtype = OB_SOLID;
//		v3d.lay &= GPU_lamp_shadow_layer(shadow.lamp);
//		v3d.flag2 &= ~V3D_SOLID_TEX;
//
//		GPU_lamp_shadow_buffer_bind(shadow.lamp, viewmat, &winsize, winmat);
//// XXX		drawview3d_render(v3d, viewmat, winsize, winsize, winmat, 1);
//		GPU_lamp_shadow_buffer_unbind(shadow.lamp);
//
//		v3d.drawtype= drawtype;
//		v3d.lay= lay;
//		v3d.flag2 |= flag2;
//	}
//
//	BLI_freelistN(&shadows);
//}

/* *********************** customdata **************** */

/* goes over all modes and view3d settings */
static int get_viewedit_datamask(bScreen screen)
{
	int mask = CustomDataUtil.CD_MASK_BAREMESH;
	ScrArea sa;

	/* check if we need tfaces & mcols due to face select or texture paint */
	if(Global.FACESEL_PAINT_TEST() || (G.f & Global.G_TEXTUREPAINT)!=0)
		mask |= CustomDataTypes.CD_MASK_MTFACE | CustomDataTypes.CD_MASK_MCOL;

	/* check if we need tfaces & mcols due to view mode */
	for(sa = (ScrArea)screen.areabase.first; sa!=null; sa = sa.next) {
		if(sa.spacetype == SpaceTypes.SPACE_VIEW3D) {
			View3D view = (View3D)sa.spacedata.first;
			if(view.drawtype == ObjectTypes.OB_SHADED) {
				/* this includes normals for mesh_create_shadedColors */
				mask |= CustomDataTypes.CD_MASK_MTFACE | CustomDataTypes.CD_MASK_MCOL | CustomDataTypes.CD_MASK_NORMAL | CustomDataTypes.CD_MASK_ORCO;
			}
			if((view.drawtype == ObjectTypes.OB_TEXTURE) || ((view.drawtype == ObjectTypes.OB_SOLID) && (view.flag2 & View3dTypes.V3D_SOLID_TEX)!=0)) {
				mask |= CustomDataTypes.CD_MASK_MTFACE | CustomDataTypes.CD_MASK_MCOL;

				if((G.fileflags & Global.G_FILE_GAME_MAT)!=0 &&
				   (G.fileflags & Global.G_FILE_GAME_MAT_GLSL)!=0) {
					mask |= CustomDataTypes.CD_MASK_ORCO;
				}
			}
		}
	}

	/* check if we need mcols due to vertex paint or weightpaint */
	if((G.f & Global.G_VERTEXPAINT)!=0)
		mask |= CustomDataTypes.CD_MASK_MCOL;
	if((G.f & Global.G_WEIGHTPAINT)!=0)
		mask |= CustomDataTypes.CD_MASK_WEIGHT_MCOL;

	if((G.f & Global.G_SCULPTMODE)!=0)
		mask |= CustomDataTypes.CD_MASK_MDISPS;

	return mask;
}

public static ARegionType.Draw view3d_main_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//void view3d_main_area_draw(const bContext *C, ARegion *ar)
{
//        System.out.println("view3d_main_area_draw");
	Scene scene= bContext.CTX_data_scene(C);
	View3D v3d = bContext.CTX_wm_view3d(C);
	RegionView3D rv3d= bContext.CTX_wm_region_view3d(C);
	Scene sce;
	Base base;
	bObject ob;
//	int retopo= 0, sculptparticle= 0;
//	Object *obact = OBACT;

//	/* from now on all object derived meshes check this */
//	v3d.customdata_mask= get_viewedit_datamask(bContext.CTX_wm_screen(C));

//	/* shadow buffers, before we setup matrices */
//	if(draw_glsl_material(scene, NULL, v3d, v3d.drawtype))
//		gpu_update_lamps_shadows(scene, v3d);

	View3dView.setwinmatrixview3d(gl, ar, v3d, null);	/* 0= no pick rect */
	View3dView.setviewmatrixview3d(scene, v3d, rv3d);	/* note: calls where_is_object for camera... */

	Arithb.Mat4MulMat4(rv3d.persmat, rv3d.viewmat, rv3d.winmat);
	Arithb.Mat4Invert(rv3d.persinv, rv3d.persmat);
	Arithb.Mat4Invert(rv3d.viewinv, rv3d.viewmat);

	/* calculate pixelsize factor once, is used for lamps and obcenters */
        {
		float len1, len2;
                float[] vec=new float[3];

		UtilDefines.VECCOPY(vec, rv3d.persinv[0]);
		len1= Arithb.Normalize(vec);
		UtilDefines.VECCOPY(vec, rv3d.persinv[1]);
		len2= Arithb.Normalize(vec);

		rv3d.pixsize= 2.0f*(len1>len2?len1:len2);

		/* correct for window size */
		if(ar.winx > ar.winy)
                rv3d.pixsize/= (float)ar.winx;
		else
                rv3d.pixsize/= (float)ar.winy;
	}

	if(v3d.drawtype > ObjectTypes.OB_WIRE) {
		float[] col=new float[3];
		Resources.UI_GetThemeColor3fv(Resources.TH_BACK, col);
		gl.glClearColor(col[0], col[1], col[2], 0.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
	}
	else {
		float[] col = new float[3];
		Resources.UI_GetThemeColor3fv(Resources.TH_BACK, col);
		gl.glClearColor(col[0], col[1], col[2], 0.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
	}

	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

	if((rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0)
		view3d_draw_clipping(gl, rv3d);

	/* set zbuffer after we draw clipping region */
	if(v3d.drawtype > ObjectTypes.OB_WIRE) {
		v3d.zbuf= 1;
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	// needs to be done always, gridview is adjusted in drawgrid() now
	//v3d.gridview= v3d.grid;
	rv3d.gridview= v3d.grid;

	if(rv3d.view==0 || rv3d.persp!=0) {
		drawfloor(gl, scene, v3d);
//		if(rv3d.persp==2) {
//			if(scene.world) {
//				if(scene.world.mode & WO_STARS) {
//					RE_make_stars(NULL, scene, star_stuff_init_func, star_stuff_vertex_func,
//								  star_stuff_term_func);
//				}
//			}
//			if(v3d.flag & V3D_DISPBGPIC) draw_bgpic(scene, ar, v3d);
//		}
	}
	else {
		Area.ED_region_pixelspace(gl, ar);
		drawgrid(gl, ar, v3d);
		/* XXX make function? replaces persp(1) */
		gl.glMatrixMode(GL2.GL_PROJECTION);
		WmSubWindow.wmLoadMatrix(gl, rv3d.winmat);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

//		if(v3d.flag & V3D_DISPBGPIC) {
//			draw_bgpic(scene, ar, v3d);
//		}
	}

	if((rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0)
		view3d_set_clipping(gl, rv3d);

	/* draw set first */
	if(scene.set!=null) {
//		for(SETLOOPER(scene.set, base)) {
//
//			if(v3d.lay & base.lay) {
//
//				UI_ThemeColorBlend(TH_WIRE, TH_BACK, 0.6f);
//				draw_object(scene, ar, v3d, base, DRAW_CONSTCOLOR|DRAW_SCENESET);
//
//				if(base.object.transflag & OB_DUPLI) {
//					draw_dupli_objects_color(scene, ar, v3d, base, TH_WIRE);
//				}
//			}
//		}

		/* Transp and X-ray afterdraw stuff for sets is done later */
	}

	/* extra service in layerbuttons, showing used layers */
	v3d.lay_used = 0;

	/* then draw not selected and the duplis, but skip editmode object */
	for(base= (Base)scene.base.first; base!=null; base= base.next) {
		v3d.lay_used |= base.lay;

		if((v3d.lay & base.lay)!=0) {

			/* dupli drawing */
//			if(base.object.transflag & OB_DUPLI) {
//				draw_dupli_objects(scene, ar, v3d, base);
//			}
			if((base.flag & Blender.SELECT)==0) {
				if(base.object!=scene.obedit) {
					DrawObject.draw_object(gl, scene, ar, v3d, base, 0);
                                }
			}
		}
	}

//	sculptparticle= (G.f & (G_SCULPTMODE|G_PARTICLEEDIT)) && !scene.obedit;
//	if(retopo)
//		view3d_update_depths(ar, v3d);

	/* draw selected and editmode */
	for(base= (Base)scene.base.first; base!=null; base= base.next) {
		if((v3d.lay & base.lay)!=0) {
			if (base.object==scene.obedit || (base.flag & Blender.SELECT)!=0) {
				DrawObject.draw_object(gl, scene, ar, v3d, base, 0);
                        }
		}
	}

//	if(!retopo && sculptparticle && !(obact && (obact.dtx & OB_DRAWXRAY))) {
//		if(G.f & G_SCULPTMODE)
//			draw_sculpt_depths(scene, ar, v3d);
//		view3d_update_depths(ar, v3d);
//	}

	SpaceTypeUtil.ED_region_draw_cb_draw(gl, C, ar, SpaceTypeUtil.REGION_DRAW_POST);

////	REEB_draw();
//
////	if(scene.radio) RAD_drawall(v3d.drawtype>=OB_SOLID);
//
//	/* Transp and X-ray afterdraw stuff */
//	view3d_draw_transp(scene, ar, v3d);
//	view3d_draw_xray(scene, ar, v3d, 1);	// clears zbuffer if it is used!
//
//	if(!retopo && sculptparticle && (obact && (OBACT.dtx & OB_DRAWXRAY))) {
//		if(G.f & G_SCULPTMODE)
//			draw_sculpt_depths(scene, ar, v3d);
//		view3d_update_depths(ar, v3d);
//	}

	if((rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0)
		view3d_clr_clipping(gl);

	TransformManipulator.BIF_draw_manipulator(gl, C);

	if(v3d.zbuf!=0) {
		v3d.zbuf= 0;
		gl.glDisable(GL2.GL_DEPTH_TEST);
	}

//	/* draw grease-pencil stuff */
////	if (v3d.flag2 & V3D_DISPGP)
////		draw_gpencil_3dview(ar, 1);
//
//	BDR_drawSketch(C);

	Area.ED_region_pixelspace(gl, ar);

//	/* Draw Sculpt Mode brush XXX (removed) */
//
////	retopo_paint_view_update(v3d);
////	retopo_draw_paint_lines();
//
//	/* Draw particle edit brush XXX (removed) */

	if(rv3d.persp>1)
            drawviewborder(gl, scene, ar, v3d);
//	if(rv3d.rflag & RV3D_FLYMODE) drawviewborder_flymode(ar);

//	/* draw grease-pencil stuff */
////	if (v3d.flag2 & V3D_DISPGP)
////		draw_gpencil_3dview(ar, 0);

//	drawcursor(scene, ar, v3d);

//	if(U.uiflag & USER_SHOW_ROTVIEWICON)
//		draw_view_axis(rv3d);
//	else
//		draw_view_icon(rv3d);
//
//	/* XXX removed viewport fps */
//	if(U.uiflag & USER_SHOW_VIEWPORTNAME) {
//		draw_viewport_name(ar, v3d);
//	}
//
//	ob= OBACT;
//	if(U.uiflag & USER_DRAWVIEWINFO)
//		draw_selected_name(scene, ob, v3d);
//
//	/* XXX here was the blockhandlers for floating panels */
//
//	if(G.f & G_VERTEXPAINT || G.f & G_WEIGHTPAINT || G.f & G_TEXTUREPAINT) {
//		v3d.flag |= V3D_NEEDBACKBUFDRAW;
//		// XXX addafterqueue(ar.win, BACKBUFDRAW, 1);
//	}
//
//	if((G.f & G_PARTICLEEDIT) && v3d.drawtype>OB_WIRE && (v3d.flag & V3D_ZBUF_SELECT)) {
//		v3d.flag |= V3D_NEEDBACKBUFDRAW;
//		// XXX addafterqueue(ar.win, BACKBUFDRAW, 1);
//	}
//
//	// test for backbuf select
//	if(scene.obedit && v3d.drawtype>OB_WIRE && (v3d.flag & V3D_ZBUF_SELECT)) {
//
//		v3d.flag |= V3D_NEEDBACKBUFDRAW;
//		// XXX if(afterqtest(ar.win, BACKBUFDRAW)==0) {
//		//	addafterqueue(ar.win, BACKBUFDRAW, 1);
//		//}
//	}
}};

}


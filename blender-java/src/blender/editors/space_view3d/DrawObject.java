/**
 * $Id: drawobject.c 21737 2009-07-20 23:52:53Z jhk $
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
 * Contributor(s): Blender Foundation, full recode and added functions
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_view3d;

import blender.blenkernel.Blender;
import blender.blenkernel.DerivedMesh;
import blender.blenkernel.DerivedMesh.DrawMappedEdgesFunc;
import blender.blenkernel.DerivedMesh.DrawMappedFacesFunc;
import blender.blenkernel.DerivedMesh.ForeachMappedVertFunc;
import blender.blenkernel.Global;
import blender.blenkernel.ObjectUtil;
import blender.blenkernel.UtilDefines;
import blender.blenlib.Arithb;
import blender.blenlib.EditVertUtil;
import blender.blenlib.EditVertUtil.EditEdge;
import blender.blenlib.EditVertUtil.EditFace;
import blender.blenlib.EditVertUtil.EditMesh;
import blender.blenlib.EditVertUtil.EditSelection;
import blender.blenlib.EditVertUtil.EditVert;
import blender.blenlib.MatrixOps;
import blender.editors.mesh.EditMeshLib;
import blender.editors.space_view3d.View3dStruct.ViewContext;
import blender.editors.mesh.EditMeshUtil;
import blender.editors.screen.GlUtil;
import blender.editors.uinterface.Resources;
import blender.makesdna.CameraTypes;
import blender.makesdna.LampTypes;
import blender.makesdna.MeshTypes;
import blender.makesdna.ObjectTypes;
import blender.makesdna.SceneTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.Camera;
import blender.makesdna.sdna.Lamp;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ToolSettings;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.World;
import blender.makesdna.sdna.bObject;
import blender.windowmanager.WmSubWindow;
import javax.media.opengl.GL2;
import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

public class DrawObject {

    public static final float BL_NEAR_CLIP= 0.001f;

    /* drawing flags: */
    public static final int DRAW_PICKING=	1;
    public static final int DRAW_CONSTCOLOR=	2;
    public static final int DRAW_SCENESET=	4;

    public static final int V3D_XRAY=	1;
    public static final int V3D_TRANSP=	2;

    public static final int V3D_SELECT_MOUSE=	1;

///* this condition has been made more complex since editmode can draw textures */
//#define CHECK_OB_DRAWTEXTURE(vd, dt) \
//((vd.drawtype==OB_TEXTURE && dt>OB_SOLID) || \
//	(vd.drawtype==OB_SOLID && vd.flag2 & V3D_SOLID_TEX))
//
//#define CHECK_OB_DRAWFACEDOT(sce, vd, dt) \
//(	(sce.toolsettings.selectmode & SCE_SELECT_FACE) && \
//	(vd.drawtype<=OB_SOLID) && \
//	(((vd.drawtype==OB_SOLID) && (dt>=OB_SOLID) && (vd.flag2 & V3D_SOLID_TEX) && (vd.flag & V3D_ZBUF_SELECT)) == 0) \
//	)
//
//
///* pretty stupid */
///* editmball.c */
//extern ListBase editelems;
//
//static void draw_bounding_volume(Scene *scene, Object *ob);
//
//static void drawcube_size(float size);
//static void drawcircle_size(float size);
//static void draw_empty_sphere(float size);
//static void draw_empty_cone(float size);

    static class FaceData {
        byte[][] cols=new byte[3][];
        EditFace efa_act;
    };

    public static class EdgeSelData {
        public byte[] baseCol, selCol, actCol;
        public EditEdge eed_act;
    };

    static class Data {
        public int sel;
        public EditVert eve_act;
    };


/* ************* only use while object drawing ************** */
public static void view3d_project_short_clip(ARegion ar, float[] vec, short[] adr)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float fx, fy;
        float[] vec4 = new float[4];

	adr[0]= View3dStruct.IS_CLIPPED;

	/* clipplanes in eye space */
	if((rv3d.rflag & View3dTypes.RV3D_CLIPPING)!=0) {
		UtilDefines.VECCOPY(vec4, vec);
		Arithb.Mat4MulVecfl(rv3d.viewmatob, vec4);
		if(View3dDraw.view3d_test_clipping(rv3d, vec4))
			return;
	}

	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;

	Arithb.Mat4MulVec4fl(rv3d.persmatob, vec4);

	/* clipplanes in window space */
	if( vec4[3]>BL_NEAR_CLIP ) {	/* is the NEAR clipping cutoff for picking */
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

/* only use while object drawing */
public static void view3d_project_short_noclip(ARegion ar, float[] vec, short[] adr)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	float fx, fy;
        float[] vec4 = new float[4];

	adr[0]= View3dStruct.IS_CLIPPED;

	UtilDefines.VECCOPY(vec4, vec);
	vec4[3]= 1.0f;

	Arithb.Mat4MulVec4fl(rv3d.persmatob, vec4);

	if( vec4[3]>BL_NEAR_CLIP ) {	/* is the NEAR clipping cutoff for picking */
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

/* ************************ */

/* check for glsl drawing */

public static boolean draw_glsl_material(Scene scene, bObject ob, View3D v3d, int dt)
{
//	if(!GPU_extensions_minimum_support())
//		return 0;
//	if(G.f & G_PICKSEL)
//		return 0;
//	if(!CHECK_OB_DRAWTEXTURE(v3d, dt))
//		return 0;
//	if(ob==OBACT && (G.f & G_WEIGHTPAINT))
//		return 0;
//
//	return ((G.fileflags & G_FILE_GAME_MAT) &&
//	   (G.fileflags & G_FILE_GAME_MAT_GLSL) && (dt >= OB_SHADED));

    return false; // TMP
}

public static boolean check_material_alpha(Base base, Mesh me, boolean glsl)
{
	if((base.flag & ObjectTypes.OB_FROMDUPLI)!=0)
		return false;

	if((G.f & Global.G_PICKSEL)!=0)
		return false;
			
//	if(me.edit_mesh!=null)
//		return false;
	
	return (glsl || (base.object.dtx & ObjectTypes.OB_DRAWTRANSP)!=0);
}

	/***/

    public static int[] colortab = {
        0x0, 0xFF88FF, 0xFFBBFF,
        0x403000, 0xFFFF88, 0xFFFFBB,
        0x104040, 0x66CCCC, 0x77CCCC,
        0x104010, 0x55BB55, 0x66FF66,
        0xFFFFFF, 0x0, 0x0,
        0x0, 0x0, 0x0,
        0x0, 0x0, 0x0,
        0x0, 0x0, 0x0
    };


public static float[][] cube = {
	{-1.0f, -1.0f, -1.0f},
	{-1.0f, -1.0f,  1.0f},
	{-1.0f,  1.0f,  1.0f},
	{-1.0f,  1.0f, -1.0f},
	{ 1.0f, -1.0f, -1.0f},
	{ 1.0f, -1.0f,  1.0f},
	{ 1.0f,  1.0f,  1.0f},
	{ 1.0f,  1.0f, -1.0f},
};

/* ----------------- OpenGL Circle Drawing - Tables for Optimised Drawing Speed ------------------ */
/* 32 values of sin function (still same result!) */
public static float[] sinval = {
	0.00000000f,
	0.20129852f,
	0.39435585f,
	0.57126821f,
	0.72479278f,
	0.84864425f,
	0.93775213f,
	0.98846832f,
	0.99871650f,
	0.96807711f,
	0.89780453f,
	0.79077573f,
	0.65137248f,
	0.48530196f,
	0.29936312f,
	0.10116832f,
	-0.10116832f,
	-0.29936312f,
	-0.48530196f,
	-0.65137248f,
	-0.79077573f,
	-0.89780453f,
	-0.96807711f,
	-0.99871650f,
	-0.98846832f,
	-0.93775213f,
	-0.84864425f,
	-0.72479278f,
	-0.57126821f,
	-0.39435585f,
	-0.20129852f,
	0.00000000f
};

/* 32 values of cos function (still same result!) */
public static float[] cosval ={
	1.00000000f,
	0.97952994f,
	0.91895781f,
	0.82076344f,
	0.68896691f,
	0.52896401f,
	0.34730525f,
	0.15142777f,
	-0.05064916f,
	-0.25065253f,
	-0.44039415f,
	-0.61210598f,
	-0.75875812f,
	-0.87434661f,
	-0.95413925f,
	-0.99486932f,
	-0.99486932f,
	-0.95413925f,
	-0.87434661f,
	-0.75875812f,
	-0.61210598f,
	-0.44039415f,
	-0.25065253f,
	-0.05064916f,
	0.15142777f,
	0.34730525f,
	0.52896401f,
	0.68896691f,
	0.82076344f,
	0.91895781f,
	0.97952994f,
	1.00000000f
};

    /* flag is same as for draw_object */
    public static void drawaxes(GL2 gl, float size, int flag, byte drawtype) {
        int axis;
//	float[] v1 = {0.0f, 0.0f, 0.0f};
//	float[] v2 = {0.0f, 0.0f, 0.0f};
//	float[] v3 = {0.0f, 0.0f, 0.0f};

        if ((G.f & Global.G_RENDER_SHADOW) != 0) {
            return;
        }

        switch (drawtype) {

            case ObjectTypes.OB_PLAINAXES: {
                for (axis = 0; axis < 3; axis++) {
                    float[] v1 = {0.0f, 0.0f, 0.0f};
                    float[] v2 = {0.0f, 0.0f, 0.0f};

                    gl.glBegin(GL2.GL_LINES);

                    v1[axis] = size;
                    v2[axis] = -size;
                    gl.glVertex3fv(v1, 0);
                    gl.glVertex3fv(v2, 0);

                    gl.glEnd();
                }
            }
            break;
            case ObjectTypes.OB_SINGLE_ARROW: {
                float[] v1 = {0.0f, 0.0f, 0.0f};
                float[] v2 = {0.0f, 0.0f, 0.0f};
                float[] v3 = {0.0f, 0.0f, 0.0f};
                gl.glBegin(GL2.GL_LINES);
                /* in positive z direction only */
                v1[2] = size;
                gl.glVertex3fv(v1, 0);
                gl.glVertex3fv(v2, 0);
                gl.glEnd();

                /* square pyramid */
                gl.glBegin(GL2.GL_TRIANGLES);

                v2[0] = size * 0.035f;
                v2[1] = size * 0.035f;
                v3[0] = size * -0.035f;
                v3[1] = size * 0.035f;
                v2[2] = v3[2] = size * 0.75f;

                for (axis = 0; axis < 4; axis++) {
                    if (axis % 2 == 1) {
                        v2[0] *= -1;
                        v3[1] *= -1;
                    } else {
                        v2[1] *= -1;
                        v3[0] *= -1;
                    }

                    gl.glVertex3fv(v1, 0);
                    gl.glVertex3fv(v2, 0);
                    gl.glVertex3fv(v3, 0);

                }
                gl.glEnd();
            }
            break;
//	case OB_CUBE:
//		drawcube_size(size);
//		break;
//
//	case OB_CIRCLE:
//		drawcircle_size(size);
//		break;
//
//	case OB_EMPTY_SPHERE:
//		 draw_empty_sphere(size);
//	     break;
//
//	case OB_EMPTY_CONE:
//		 draw_empty_cone(size);
//	     break;

            case ObjectTypes.OB_ARROWS:
            default:
                for (axis = 0; axis < 3; axis++) {
                    float[] v1 = {0.0f, 0.0f, 0.0f};
                    float[] v2 = {0.0f, 0.0f, 0.0f};
                    int arrow_axis = (axis == 0) ? 1 : 0;

                    gl.glBegin(GL2.GL_LINES);

                    v2[axis] = size;
                    gl.glVertex3fv(v1, 0);
                    gl.glVertex3fv(v2, 0);

                    v1[axis] = size * 0.8f;
                    v1[arrow_axis] = -size * 0.125f;
                    gl.glVertex3fv(v1, 0);
                    gl.glVertex3fv(v2, 0);

                    v1[arrow_axis] = size * 0.125f;
                    gl.glVertex3fv(v1, 0);
                    gl.glVertex3fv(v2, 0);

                    gl.glEnd();

                    v2[axis] += size * 0.125f;
                    gl.glRasterPos3fv(v2, 0);

//			// patch for 3d cards crashing on glSelect for text drawing (IBM)
//			if((flag & DRAW_PICKING) == 0) {
//				if (axis==0)
//					BMF_DrawString(G.font, "x");
//				else if (axis==1)
//					BMF_DrawString(G.font, "y");
//				else
//					BMF_DrawString(G.font, "z");
//			}
                }
                break;
        }
    }

    public static void drawcircball(GL2 gl, int mode, float[] cent, float rad, float[][] tmat) {
        float[] vec = new float[3], vx = new float[3], vy = new float[3];
        int a, tot = 32;

        UtilDefines.VECCOPY(vx, tmat[0]);
        UtilDefines.VECCOPY(vy, tmat[1]);
        Arithb.VecMulf(vx, rad);
        Arithb.VecMulf(vy, rad);

        gl.glBegin(mode);
        for (a = 0; a < tot; a++) {
            vec[0] = cent[0] + sinval[a] * vx[0] + cosval[a] * vy[0];
            vec[1] = cent[1] + sinval[a] * vx[1] + cosval[a] * vy[1];
            vec[2] = cent[2] + sinval[a] * vx[2] + cosval[a] * vy[2];

            gl.glVertex3fv(vec, 0);
        }
        gl.glEnd();
    }

/* circle for object centers, special_color is for library or ob users */
public static void drawcentercircle(GL2 gl, View3D v3d, RegionView3D rv3d, float[] vec, int selstate, boolean special_color)
{
	float size;

	size= rv3d.persmat[0][3]*vec[0]+ rv3d.persmat[1][3]*vec[1]+ rv3d.persmat[2][3]*vec[2]+ rv3d.persmat[3][3];
//	size*= rv3d.pixsize*((float)U.obcenter_dia*0.5f);
        size*= rv3d.pixsize*((float)6*0.5f);

	/* using gldepthfunc guarantees that it does write z values, but not checks for it, so centers remain visible independt order of drawing */
	if(v3d.zbuf!=0)  gl.glDepthFunc(GL2.GL_ALWAYS);
	gl.glEnable(GL2.GL_BLEND);

	if(special_color) {
		if (selstate==Blender.ACTIVE || selstate==Blender.SELECT) gl.glColor4ub((byte)0x88, (byte)0xFF, (byte)0xFF, (byte)155);

		else gl.glColor4ub((byte)0x55, (byte)0xCC, (byte)0xCC, (byte)155);
	}
	else {
		if (selstate == Blender.ACTIVE) Resources.UI_ThemeColorShadeAlpha(gl, Resources.TH_ACTIVE, 0, -80);
		else if (selstate == Blender.SELECT) Resources.UI_ThemeColorShadeAlpha(gl, Resources.TH_SELECT, 0, -80);
		else if (selstate == Blender.DESELECT) Resources.UI_ThemeColorShadeAlpha(gl, Resources.TH_TRANSFORM, 0, -80);
	}
	drawcircball(gl, GL2.GL_POLYGON, vec, size, rv3d.viewinv);

	Resources.UI_ThemeColorShadeAlpha(gl, Resources.TH_WIRE, 0, -30);
	drawcircball(gl, GL2.GL_LINE_LOOP, vec, size, rv3d.viewinv);

	gl.glDisable(GL2.GL_BLEND);
	if(v3d.zbuf!=0)  gl.glDepthFunc(GL2.GL_LEQUAL);
}

///* *********** text drawing for object ************* */
//static ListBase strings= {NULL, NULL};
//
//typedef struct ViewObjectString {
//	struct ViewObjectString *next, *prev;
//	float vec[3], col[4];
//	char str[128];
//	short mval[2];
//	short xoffs;
//} ViewObjectString;
//
//
//void view3d_object_text_draw_add(float x, float y, float z, char *str, short xoffs)
//{
//	ViewObjectString *vos= MEM_callocN(sizeof(ViewObjectString), "ViewObjectString");
//
//	BLI_addtail(&strings, vos);
//	BLI_strncpy(vos.str, str, 128);
//	vos.vec[0]= x;
//	vos.vec[1]= y;
//	vos.vec[2]= z;
//	glGetFloatv(GL_CURRENT_COLOR, vos.col);
//	vos.xoffs= xoffs;
//}
//
//static void view3d_object_text_draw(View3D *v3d, ARegion *ar)
//{
//	ViewObjectString *vos;
//	int tot= 0;
//
//	/* project first and test */
//	for(vos= strings.first; vos; vos= vos.next) {
//		view3d_project_short_clip(ar, vos.vec, vos.mval);
//		if(vos.mval[0]!=IS_CLIPPED)
//			tot++;
//	}
//
//	if(tot) {
//		RegionView3D *rv3d= ar.regiondata;
//		int a;
//
//		if(rv3d.rflag & RV3D_CLIPPING)
//			for(a=0; a<6; a++)
//				glDisable(GL_CLIP_PLANE0+a);
//
//		wmPushMatrix();
//		ED_region_pixelspace(ar);
//
//		if(v3d.zbuf) glDisable(GL_DEPTH_TEST);
//
//		for(vos= strings.first; vos; vos= vos.next) {
//			if(vos.mval[0]!=IS_CLIPPED) {
//				glColor3fv(vos.col);
//				BLF_draw_default((float)vos.mval[0]+vos.xoffs, (float)vos.mval[1], 0.0, vos.str);
//			}
//		}
//
//		if(v3d.zbuf) glEnable(GL_DEPTH_TEST);
//
//		wmPopMatrix();
//
//		if(rv3d.rflag & RV3D_CLIPPING)
//			for(a=0; a<6; a++)
//				glEnable(GL_CLIP_PLANE0+a);
//	}
//
//	if(strings.first)
//		BLI_freelistN(&strings);
//}
//
//static void drawcube(void)
//{
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[0]); glVertex3fv(cube[1]);glVertex3fv(cube[2]); glVertex3fv(cube[3]);
//		glVertex3fv(cube[0]); glVertex3fv(cube[4]);glVertex3fv(cube[5]); glVertex3fv(cube[6]);
//		glVertex3fv(cube[7]); glVertex3fv(cube[4]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[1]); glVertex3fv(cube[5]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[2]); glVertex3fv(cube[6]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[3]); glVertex3fv(cube[7]);
//	glEnd();
//}
//
///* draws a cube on given the scaling of the cube, assuming that
// * all required matrices have been set (used for drawing empties)
// */
//static void drawcube_size(float size)
//{
//	glBegin(GL_LINE_STRIP);
//		glVertex3f(-size,-size,-size); glVertex3f(-size,-size,size);glVertex3f(-size,size,size); glVertex3f(-size,size,-size);
//		glVertex3f(-size,-size,-size); glVertex3f(size,-size,-size);glVertex3f(size,-size,size); glVertex3f(size,size,size);
//		glVertex3f(size,size,-size); glVertex3f(size,-size,-size);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3f(-size,-size,size); glVertex3f(size,-size,size);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3f(-size,size,size); glVertex3f(size,size,size);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3f(-size,size,-size); glVertex3f(size,size,-size);
//	glEnd();
//}
//
///* this is an unused (old) cube-drawing function based on a given size */
//#if 0
//static void drawcube_size(float *size)
//{
//
//	glPushMatrix();
//	glScalef(size[0],  size[1],  size[2]);
//
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[0]); glVertex3fv(cube[1]);glVertex3fv(cube[2]); glVertex3fv(cube[3]);
//		glVertex3fv(cube[0]); glVertex3fv(cube[4]);glVertex3fv(cube[5]); glVertex3fv(cube[6]);
//		glVertex3fv(cube[7]); glVertex3fv(cube[4]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[1]); glVertex3fv(cube[5]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[2]); glVertex3fv(cube[6]);
//	glEnd();
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(cube[3]); glVertex3fv(cube[7]);
//	glEnd();
//
//	glPopMatrix();
//}
//#endif
//
//static void drawshadbuflimits(Lamp *la, float mat[][4])
//{
//	float sta[3], end[3], lavec[3];
//
//	lavec[0]= -mat[2][0];
//	lavec[1]= -mat[2][1];
//	lavec[2]= -mat[2][2];
//	Normalize(lavec);
//
//	sta[0]= mat[3][0]+ la.clipsta*lavec[0];
//	sta[1]= mat[3][1]+ la.clipsta*lavec[1];
//	sta[2]= mat[3][2]+ la.clipsta*lavec[2];
//
//	end[0]= mat[3][0]+ la.clipend*lavec[0];
//	end[1]= mat[3][1]+ la.clipend*lavec[1];
//	end[2]= mat[3][2]+ la.clipend*lavec[2];
//
//
//	glBegin(GL_LINE_STRIP);
//		glVertex3fv(sta);
//		glVertex3fv(end);
//	glEnd();
//
//	glPointSize(3.0);
//	bglBegin(GL_POINTS);
//	bglVertex3fv(sta);
//	bglVertex3fv(end);
//	bglEnd();
//	glPointSize(1.0);
//}

    public static void spotvolume(float[] lvec, float[] vvec, float inp) {
        /* camera is at 0,0,0 */
        float[] temp = new float[3], plane = new float[3], q = new float[4];
        float[][] mat1 = new float[3][3], mat2 = new float[3][3], mat3 = new float[3][3], mat4 = new float[3][3];
        float co, si, angle;

        Arithb.Normalize(lvec);
        Arithb.Normalize(vvec);				/* is this the correct vector ? */

        Arithb.Crossf(temp, vvec, lvec);		/* equation for a plane through vvec en lvec */
        Arithb.Crossf(plane, lvec, temp);		/* a plane perpendicular to this, parrallel with lvec */

        Arithb.Normalize(plane);

        /* now we've got two equations: one of a cone and one of a plane, but we have
        three unknowns. We remove one unkown by rotating the plane to z=0 (the plane normal) */

        /* rotate around cross product vector of (0,0,1) and plane normal, dot product degrees */
        /* according definition, we derive cross product is (plane[1],-plane[0],0), en cos = plane[2]);*/

        /* translating this comment to english didnt really help me understanding the math! :-) (ton) */

        q[1] = plane[1];
        q[2] = -plane[0];
        q[3] = 0;
        float[] tmp = {q[1], q[2], q[3], 0.0f};
        Arithb.Normalize(tmp);
        q[1] = tmp[1];
        q[2] = tmp[2];
        q[3] = tmp[3];

        angle = Arithb.saacos(plane[2]) / 2.0f;
        co = (float) StrictMath.cos(angle);
        si = (float) StrictMath.sqrt(1 - co * co);

        q[0] = co;
        q[1] *= si;
        q[2] *= si;
        q[3] = 0;

        Arithb.QuatToMat3(q, mat1);

        /* rotate lamp vector now over acos(inp) degrees */

        vvec[0] = lvec[0];
        vvec[1] = lvec[1];
        vvec[2] = lvec[2];

        Arithb.Mat3One(mat2);
        co = inp;
        si = (float) StrictMath.sqrt(1 - inp * inp);

        mat2[0][0] = co;
        mat2[1][0] = -si;
        mat2[0][1] = si;
        mat2[1][1] = co;
        Arithb.Mat3MulMat3(mat3, mat2, mat1);

        mat2[1][0] = si;
        mat2[0][1] = -si;
        Arithb.Mat3MulMat3(mat4, mat2, mat1);
        Arithb.Mat3Transp(mat1);

        Arithb.Mat3MulMat3(mat2, mat1, mat3);
        Arithb.Mat3MulVecfl(mat2, lvec);
        Arithb.Mat3MulMat3(mat2, mat1, mat4);
        Arithb.Mat3MulVecfl(mat2, vvec);
    }

    public static void drawlamp(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, bObject ob) {
        Lamp la;
        float[] vec = new float[3], lvec = new float[3], vvec = new float[3];
        float circrad, x, y, z;
        float pixsize, lampsize;
        float[][] imat = new float[4][4];
        float[] curcol = new float[4];
        byte[] col = new byte[4];

        if ((G.f & Global.G_RENDER_SHADOW) != 0) {
            return;
        }

        la = (Lamp) ob.data;

        /* we first draw only the screen aligned & fixed scale stuff */
        gl.glPushMatrix();
        WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

        /* lets calculate the scale: */
        pixsize = rv3d.persmat[0][3] * ob.obmat[3][0] + rv3d.persmat[1][3] * ob.obmat[3][1] + rv3d.persmat[2][3] * ob.obmat[3][2] + rv3d.persmat[3][3];
        pixsize *= rv3d.pixsize;
        lampsize = pixsize * ((float) U.obcenter_dia * 0.5f);

        /* and view aligned matrix: */
        Arithb.Mat4CpyMat4(imat, rv3d.viewinv);
        Arithb.Normalize(imat[0]);
        Arithb.Normalize(imat[1]);

        /* for AA effects */
        gl.glGetFloatv(GL2.GL_CURRENT_COLOR, curcol, 0);
        curcol[3] = 0.6f;
        gl.glColor4fv(curcol, 0);

//	if(ob.id.us>1) {
//		if (ob==OBACT || (ob.flag & SELECT)) glColor4ub(0x88, 0xFF, 0xFF, 155);
//		else glColor4ub(0x77, 0xCC, 0xCC, 155);
//	}

        /* Inner Circle */
        UtilDefines.VECCOPY(vec, ob.obmat[3]);
        gl.glEnable(GL2.GL_BLEND);
        drawcircball(gl, GL2.GL_LINE_LOOP, vec, lampsize, imat);
        gl.glDisable(GL2.GL_BLEND);
        drawcircball(gl, GL2.GL_POLYGON, vec, lampsize, imat);

//	/* restore */
//	if(ob.id.us>1)
//		glColor4fv(curcol);

        /* Outer circle */
        circrad = 3.0f * lampsize;
        drawcircball(gl, GL2.GL_LINE_LOOP, vec, circrad, imat);

        GlUtil.setlinestyle(gl, 3);

        /* draw dashed outer circle if shadow is on. remember some lamps can't have certain shadows! */
        if (la.type != LampTypes.LA_HEMI) {
            if ((la.mode & LampTypes.LA_SHAD_RAY) != 0 ||
                    ((la.mode & LampTypes.LA_SHAD_BUF) != 0 && (la.type == LampTypes.LA_SPOT))) {
                drawcircball(gl, GL2.GL_LINE_LOOP, vec, circrad + 3.0f * pixsize, imat);
            }
        }

        /* draw the pretty sun rays */
        if (la.type == LampTypes.LA_SUN) {
            float[] v1 = new float[3], v2 = new float[3];
            float[][] mat = new float[3][3];
            short axis;

            /* setup a 45 degree rotation matrix */
            Arithb.VecRotToMat3(imat[2], Arithb.M_PI / 4.0f, mat);

            /* vectors */
            UtilDefines.VECCOPY(v1, imat[0]);
            Arithb.VecMulf(v1, circrad * 1.2f);
            UtilDefines.VECCOPY(v2, imat[0]);
            Arithb.VecMulf(v2, circrad * 2.5f);

            /* center */
            gl.glTranslatef(vec[0], vec[1], vec[2]);

            GlUtil.setlinestyle(gl, 3);

            gl.glBegin(GL2.GL_LINES);
            for (axis = 0; axis < 8; axis++) {
                gl.glVertex3fv(v1, 0);
                gl.glVertex3fv(v2, 0);
                Arithb.Mat3MulVecfl(mat, v1);
                Arithb.Mat3MulVecfl(mat, v2);
            }
            gl.glEnd();

            gl.glTranslatef(-vec[0], -vec[1], -vec[2]);

        }

        if (la.type == LampTypes.LA_LOCAL) {
            if ((la.mode & LampTypes.LA_SPHERE) != 0) {
                drawcircball(gl, GL2.GL_LINE_LOOP, vec, la.dist, imat);
            }
            /* yafray: for photonlight also draw lightcone as for spot */
        }

        gl.glPopMatrix();	/* back in object space */
        vec[0] = vec[1] = vec[2] = 0.0f;

        if (la.type == LampTypes.LA_SPOT || la.type == LampTypes.LA_YF_PHOTON) {
            lvec[0] = lvec[1] = 0.0f;
            lvec[2] = 1.0f;
            x = rv3d.persmat[0][2];
            y = rv3d.persmat[1][2];
            z = rv3d.persmat[2][2];
            vvec[0] = x * ob.obmat[0][0] + y * ob.obmat[0][1] + z * ob.obmat[0][2];
            vvec[1] = x * ob.obmat[1][0] + y * ob.obmat[1][1] + z * ob.obmat[1][2];
            vvec[2] = x * ob.obmat[2][0] + y * ob.obmat[2][1] + z * ob.obmat[2][2];

            y = (float) StrictMath.cos(Arithb.M_PI * la.spotsize / 360.0);
            spotvolume(lvec, vvec, y);
            x = -la.dist;
            lvec[0] *= x;
            lvec[1] *= x;
            lvec[2] *= x;
            vvec[0] *= x;
            vvec[1] *= x;
            vvec[2] *= x;

            /* draw the angled sides of the cone */
            gl.glBegin(GL2.GL_LINE_STRIP);
            gl.glVertex3fv(vvec, 0);
            gl.glVertex3fv(vec, 0);
            gl.glVertex3fv(lvec, 0);
            gl.glEnd();

            z = x * (float) StrictMath.sqrt(1.0 - y * y);
            x *= y;

            /* draw the circle/square at the end of the cone */
            gl.glTranslatef(0.0f, 0.0f, x);
            if ((la.mode & LampTypes.LA_SQUARE) != 0) {
                vvec[0] = StrictMath.abs(z);
                vvec[1] = StrictMath.abs(z);
                vvec[2] = 0.0f;
                gl.glBegin(GL2.GL_LINE_LOOP);
                gl.glVertex3fv(vvec, 0);
                vvec[1] = -StrictMath.abs(z);
                gl.glVertex3fv(vvec, 0);
                vvec[0] = -StrictMath.abs(z);
                gl.glVertex3fv(vvec, 0);
                vvec[1] = StrictMath.abs(z);
                gl.glVertex3fv(vvec, 0);
                gl.glEnd();
            } else {
                View3dDraw.circ(gl, 0.0f, 0.0f, StrictMath.abs(z));
            }

            /* draw the circle/square representing spotbl */
            if (la.type == LampTypes.LA_SPOT) {
                float spotblcirc = (float) (StrictMath.abs(z) * (1 - StrictMath.pow(la.spotblend, 2)));
                /* make sure the line is always visible - prevent it from reaching the outer border (or 0)
                 * values are kinda arbitrary - just what seemed to work well */
                if (spotblcirc == 0) {
                    spotblcirc = 0.15f;
                } else if (spotblcirc == StrictMath.abs(z)) {
                    spotblcirc = StrictMath.abs(z) - 0.07f;
                }
                View3dDraw.circ(gl, 0.0f, 0.0f, spotblcirc);
            }

        } else if (la.type == LampTypes.LA_HEMI || la.type == LampTypes.LA_SUN) {

            /* draw the line from the circle along the dist */
            gl.glBegin(GL2.GL_LINE_STRIP);
            vec[2] = -circrad;
            gl.glVertex3fv(vec, 0);
            vec[2] = -la.dist;
            gl.glVertex3fv(vec, 0);
            gl.glEnd();

            if (la.type == LampTypes.LA_HEMI) {
                /* draw the hemisphere curves */
                short axis, steps;
                int dir;
                float outdist, zdist, mul;
                vec[0] = vec[1] = vec[2] = 0.0f;
                outdist = 0.14f;
                mul = 1.4f;
                dir = 1;

                GlUtil.setlinestyle(gl, 4);
                /* loop over the 4 compass points, and draw each arc as a LINE_STRIP */
                for (axis = 0; axis < 4; axis++) {
                    float[] v = {0.0f, 0.0f, 0.0f};
                    zdist = 0.02f;

                    gl.glBegin(GL2.GL_LINE_STRIP);

                    for (steps = 0; steps < 6; steps++) {
                        if (axis == 0 || axis == 1) { 		/* x axis up, x axis down */
                            /* make the arcs start at the edge of the energy circle */
                            if (steps == 0) {
                                v[0] = dir * circrad;
                            } else {
                                v[0] = v[0] + dir * (steps * outdist);
                            }
                        } else if (axis == 2 || axis == 3) { 		/* y axis up, y axis down */
                            /* make the arcs start at the edge of the energy circle */
                            if (steps == 0) {
                                v[1] = dir * circrad;
                            } else {
                                v[1] = v[1] + dir * (steps * outdist);
                            }
                        }

                        v[2] = v[2] - steps * zdist;

                        gl.glVertex3fv(v, 0);

                        zdist = zdist * mul;
                    }

                    gl.glEnd();
                    /* flip the direction */
                    dir = -dir;
                }
            }
        } else if (la.type == LampTypes.LA_AREA) {
            GlUtil.setlinestyle(gl, 3);
            if (la.area_shape == LampTypes.LA_AREA_SQUARE) {
                GlUtil.fdrawbox(gl, -la.area_size * 0.5f, -la.area_size * 0.5f, la.area_size * 0.5f, la.area_size * 0.5f);
            } else if (la.area_shape == LampTypes.LA_AREA_RECT) {
                GlUtil.fdrawbox(gl, -la.area_size * 0.5f, -la.area_sizey * 0.5f, la.area_size * 0.5f, la.area_sizey * 0.5f);
            }

            gl.glBegin(GL2.GL_LINE_STRIP);
            gl.glVertex3f(0.0f, 0.0f, -circrad);
            gl.glVertex3f(0.0f, 0.0f, -la.dist);
            gl.glEnd();
        }

        /* and back to viewspace */
        WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);
        UtilDefines.VECCOPY(vec, ob.obmat[3]);

        GlUtil.setlinestyle(gl, 0);

//	if(la.type==LA_SPOT && (la.mode & LA_SHAD_BUF) ) {
//		drawshadbuflimits(la, ob.obmat);
//	}

        Resources.UI_GetThemeColor4ubv(Resources.TH_LAMP, col);
        gl.glColor4ub(col[0], col[1], col[2], col[3]);

        gl.glEnable(GL2.GL_BLEND);

        if (vec[2] > 0) {
            vec[2] -= circrad;
        } else {
            vec[2] += circrad;
        }

        gl.glBegin(GL2.GL_LINE_STRIP);
        gl.glVertex3fv(vec, 0);
        vec[2] = 0;
        gl.glVertex3fv(vec, 0);
        gl.glEnd();

        gl.glPointSize(2.0f);
        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex3fv(vec, 0);
        gl.glEnd();
        gl.glPointSize(1.0f);

        gl.glDisable(GL2.GL_BLEND);

        /* restore for drawing extra stuff */
        gl.glColor3fv(curcol, 0);
    }

    public static void draw_limit_line(GL2 gl, float sta, float end, int col) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.0f, 0.0f, -sta);
        gl.glVertex3f(0.0f, 0.0f, -end);
        gl.glEnd();

        gl.glPointSize(3.0f);
        gl.glBegin(GL2.GL_POINTS);
        gl.glColor3ub((byte) (col), (byte) (col >> 8), (byte) (col >> 16));
        gl.glVertex3f(0.0f, 0.0f, -sta);
        gl.glVertex3f(0.0f, 0.0f, -end);
        gl.glEnd();
        gl.glPointSize(1.0f);
    }

    /* yafray: draw camera focus point (cross, similar to aqsis code in tuhopuu) */
    /* qdn: now also enabled for Blender to set focus point for defocus composit node */
    static void draw_focus_cross(GL2 gl, float dist, float size) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(-size, 0.f, -dist);
        gl.glVertex3f(size, 0.f, -dist);
        gl.glVertex3f(0.f, -size, -dist);
        gl.glVertex3f(0.f, size, -dist);
        gl.glEnd();
    }

    /* flag similar to draw_object() */
    public static void drawcamera(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, bObject ob, int flag) {
        /* a standing up pyramid with (0,0,0) as top */
        Camera cam;
        World wrld;
        float[][] vec = new float[8][4], tmat = new float[4][4];
        float fac, facx, facy, depth;
        int i;

        if ((G.f & Global.G_RENDER_SHADOW) != 0) {
            return;
        }

        cam = (Camera) ob.data;

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_CULL_FACE);

        if (rv3d.persp >= 2 && cam.type == CameraTypes.CAM_ORTHO && ob == v3d.camera) {
            facx = 0.5f * cam.ortho_scale * 1.28f;
            facy = 0.5f * cam.ortho_scale * 1.024f;
            depth = -cam.clipsta - 0.1f;
        } else {
            fac = cam.drawsize;
            if (rv3d.persp >= 2 && ob == v3d.camera) {
                fac = cam.clipsta + 0.1f; /* that way it's always visible */
            }

            depth = -fac * cam.lens / 16.0f;
            facx = fac * 1.28f;
            facy = fac * 1.024f;
        }

        vec[0][0] = 0.0f;
        vec[0][1] = 0.0f;
        vec[0][2] = 0.001f;	/* GLBUG: for picking at iris Entry (well thats old!) */
        vec[1][0] = facx;
        vec[1][1] = facy;
        vec[1][2] = depth;
        vec[2][0] = facx;
        vec[2][1] = -facy;
        vec[2][2] = depth;
        vec[3][0] = -facx;
        vec[3][1] = -facy;
        vec[3][2] = depth;
        vec[4][0] = -facx;
        vec[4][1] = facy;
        vec[4][2] = depth;

        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex3fv(vec[1], 0);
        gl.glVertex3fv(vec[2], 0);
        gl.glVertex3fv(vec[3], 0);
        gl.glVertex3fv(vec[4], 0);
        gl.glEnd();

        if (rv3d.persp >= 2 && ob == v3d.camera) {
            return;
        }

        gl.glBegin(GL2.GL_LINE_STRIP);
        gl.glVertex3fv(vec[2], 0);
        gl.glVertex3fv(vec[0], 0);
        gl.glVertex3fv(vec[1], 0);
        gl.glVertex3fv(vec[4], 0);
        gl.glVertex3fv(vec[0], 0);
        gl.glVertex3fv(vec[3], 0);
        gl.glEnd();

        /* arrow on top */
        vec[0][2] = depth;

        /* draw an outline arrow for inactive cameras and filled
         * for active cameras. We actually draw both outline+filled
         * for active cameras so the wire can be seen side-on */
        for (i = 0; i < 2; i++) {
            if (i == 0) {
                gl.glBegin(GL2.GL_LINE_LOOP);
            } else if (i == 1 && (ob == v3d.camera)) {
                gl.glBegin(GL2.GL_TRIANGLES);
            } else {
                break;
            }

            vec[0][0] = -0.7f * cam.drawsize;
            vec[0][1] = 1.1f * cam.drawsize;
            gl.glVertex3fv(vec[0], 0);

            vec[0][0] = 0.0f;
            vec[0][1] = 1.8f * cam.drawsize;
            gl.glVertex3fv(vec[0], 0);

            vec[0][0] = 0.7f * cam.drawsize;
            vec[0][1] = 1.1f * cam.drawsize;
            gl.glVertex3fv(vec[0], 0);

            gl.glEnd();
        }

        if (flag == 0) {
            if ((cam.flag & (CameraTypes.CAM_SHOWLIMITS + CameraTypes.CAM_SHOWMIST)) != 0) {
                WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);
                Arithb.Mat4CpyMat4(vec, ob.obmat);
                Arithb.Mat4Ortho(vec);
                WmSubWindow.wmMultMatrix(gl, vec);

                MatrixOps.MTC_Mat4SwapMat4(rv3d.persmat, tmat);
                WmSubWindow.wmGetSingleMatrix(gl, rv3d.persmat);

                if ((cam.flag & CameraTypes.CAM_SHOWLIMITS) != 0) {
                    draw_limit_line(gl, cam.clipsta, cam.clipend, 0x77FFFF);
                    /* qdn: was yafray only, now also enabled for Blender to be used with defocus composit node */
                    draw_focus_cross(gl, ObjectUtil.dof_camera(ob), cam.drawsize);
                }

                wrld = scene.world;
                if ((cam.flag & CameraTypes.CAM_SHOWMIST) != 0) {
                    if (wrld != null) {
                        draw_limit_line(gl, wrld.miststa, wrld.miststa + wrld.mistdist, 0xFFFFFF);
                    }
                }

                MatrixOps.MTC_Mat4SwapMat4(rv3d.persmat, tmat);
            }
        }
    }

//static void lattice_draw_verts(Lattice *lt, DispList *dl, short sel)
//{
//	BPoint *bp = lt.def;
//	float *co = dl?dl.verts:NULL;
//	int u, v, w;
//
//	UI_ThemeColor(sel?TH_VERTEX_SELECT:TH_VERTEX);
//	glPointSize(UI_GetThemeValuef(TH_VERTEX_SIZE));
//	bglBegin(GL_POINTS);
//
//	for(w=0; w<lt.pntsw; w++) {
//		int wxt = (w==0 || w==lt.pntsw-1);
//		for(v=0; v<lt.pntsv; v++) {
//			int vxt = (v==0 || v==lt.pntsv-1);
//			for(u=0; u<lt.pntsu; u++, bp++, co+=3) {
//				int uxt = (u==0 || u==lt.pntsu-1);
//				if(!(lt.flag & LT_OUTSIDE) || uxt || vxt || wxt) {
//					if(bp.hide==0) {
//						if((bp.f1 & SELECT)==sel) {
//							bglVertex3fv(dl?co:bp.vec);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	glPointSize(1.0);
//	bglEnd();
//}
//
//void lattice_foreachScreenVert(ViewContext *vc, void (*func)(void *userData, BPoint *bp, int x, int y), void *userData)
//{
//	Object *obedit= vc.obedit;
//	Lattice *lt= obedit.data;
//	BPoint *bp = lt.editlatt.def;
//	DispList *dl = find_displist(&obedit.disp, DL_VERTS);
//	float *co = dl?dl.verts:NULL;
//	int i, N = lt.editlatt.pntsu*lt.editlatt.pntsv*lt.editlatt.pntsw;
//	short s[2] = {IS_CLIPPED, 0};
//
//	for (i=0; i<N; i++, bp++, co+=3) {
//		if (bp.hide==0) {
//			view3d_project_short_clip(vc.ar, dl?co:bp.vec, s);
//			if (s[0] != IS_CLIPPED)
//				func(userData, bp, s[0], s[1]);
//		}
//	}
//}
//
//static void drawlattice__point(Lattice *lt, DispList *dl, int u, int v, int w, int use_wcol)
//{
//	int index = ((w*lt.pntsv + v)*lt.pntsu) + u;
//
//	if(use_wcol) {
//		float col[3];
//		MDeformWeight *mdw= get_defweight (lt.dvert+index, use_wcol-1);
//
//		weight_to_rgb(mdw?mdw.weight:0.0f, col, col+1, col+2);
//		glColor3fv(col);
//
//	}
//
//	if (dl) {
//		glVertex3fv(&dl.verts[index*3]);
//	} else {
//		glVertex3fv(lt.def[index].vec);
//	}
//}
//
///* lattice color is hardcoded, now also shows weightgroup values in edit mode */
//static void drawlattice(Scene *scene, View3D *v3d, Object *ob)
//{
//	Lattice *lt= ob.data;
//	DispList *dl;
//	int u, v, w;
//	int use_wcol= 0;
//
//	/* now we default make displist, this will modifiers work for non animated case */
//	if(ob.disp.first==NULL)
//		lattice_calc_modifiers(scene, ob);
//	dl= find_displist(&ob.disp, DL_VERTS);
//
//	if(lt.editlatt) {
//		cpack(0x004000);
//
//		if(ob.defbase.first && lt.dvert) {
//			use_wcol= ob.actdef;
//			glShadeModel(GL_SMOOTH);
//		}
//	}
//
//	if(lt.editlatt) lt= lt.editlatt;
//
//	glBegin(GL_LINES);
//	for(w=0; w<lt.pntsw; w++) {
//		int wxt = (w==0 || w==lt.pntsw-1);
//		for(v=0; v<lt.pntsv; v++) {
//			int vxt = (v==0 || v==lt.pntsv-1);
//			for(u=0; u<lt.pntsu; u++) {
//				int uxt = (u==0 || u==lt.pntsu-1);
//
//				if(w && ((uxt || vxt) || !(lt.flag & LT_OUTSIDE))) {
//					drawlattice__point(lt, dl, u, v, w-1, use_wcol);
//					drawlattice__point(lt, dl, u, v, w, use_wcol);
//				}
//				if(v && ((uxt || wxt) || !(lt.flag & LT_OUTSIDE))) {
//					drawlattice__point(lt, dl, u, v-1, w, use_wcol);
//					drawlattice__point(lt, dl, u, v, w, use_wcol);
//				}
//				if(u && ((vxt || wxt) || !(lt.flag & LT_OUTSIDE))) {
//					drawlattice__point(lt, dl, u-1, v, w, use_wcol);
//					drawlattice__point(lt, dl, u, v, w, use_wcol);
//				}
//			}
//		}
//	}
//	glEnd();
//
//	/* restoration for weight colors */
//	if(use_wcol)
//		glShadeModel(GL_FLAT);
//
//	if( ((Lattice *)ob.data).editlatt ) {
//		if(v3d.zbuf) glDisable(GL_DEPTH_TEST);
//
//		lattice_draw_verts(lt, dl, 0);
//		lattice_draw_verts(lt, dl, 1);
//
//		if(v3d.zbuf) glEnable(GL_DEPTH_TEST);
//	}
//}

    /* ***************** ******************** */

    public static interface NearestVertFunc {
        public void run(Object userData, EditVert eve, int x, int y, int index);
    }

    public static class ForeachMappedVertData {
        public NearestVertFunc func;
        public Object userData;
        public ViewContext vc;
        public int clipVerts;
        public float[][] pmat = new float[4][4], vmat = new float[4][4];
    };

public static ForeachMappedVertFunc mesh_foreachScreenVert__mapFunc = new ForeachMappedVertFunc() {
public void func(GL2 gl, Object userData, int index, float[] co, float[] no_f, short[] no_s)
{
//	struct { void (*func)(void *userData, EditVert *eve, int x, int y, int index); void *userData; ViewContext vc; int clipVerts; } *data = userData;
        ForeachMappedVertData data = (ForeachMappedVertData)userData;
	EditVert eve = EditMeshUtil.EM_get_vert_for_index(index);

	if (eve.h==0) {
		short[] s= {View3dStruct.IS_CLIPPED, 0};

		if (data.clipVerts!=0) {
			view3d_project_short_clip(data.vc.ar, co, s);
		} else {
			view3d_project_short_noclip(data.vc.ar, co, s);
		}

		if (s[0]!=View3dStruct.IS_CLIPPED)
			data.func.run(data.userData, eve, s[0], s[1], index);
	}
}
};

//void mesh_foreachScreenVert(ViewContext *vc, void (*func)(void *userData, EditVert *eve, int x, int y, int index), void *userData, int clipVerts)
//{
//	struct { void (*func)(void *userData, EditVert *eve, int x, int y, int index); void *userData; ViewContext vc; int clipVerts; } data;
//	DerivedMesh *dm = editmesh_get_derived_cage(vc.scene, vc.obedit, vc.em, CD_MASK_BAREMESH);
//
//	data.vc= *vc;
//	data.func = func;
//	data.userData = userData;
//	data.clipVerts = clipVerts;
//
//	EM_init_index_arrays(vc.em, 1, 0, 0);
//	dm.foreachMappedVert(dm, mesh_foreachScreenVert__mapFunc, &data);
//	EM_free_index_arrays();
//
//	dm.release(dm);
//}
//
//static void mesh_foreachScreenEdge__mapFunc(void *userData, int index, float *v0co, float *v1co)
//{
//	struct { void (*func)(void *userData, EditEdge *eed, int x0, int y0, int x1, int y1, int index); void *userData; ViewContext vc; int clipVerts; } *data = userData;
//	EditEdge *eed = EM_get_edge_for_index(index);
//	short s[2][2];
//
//	if (eed.h==0) {
//		if (data.clipVerts==1) {
//			view3d_project_short_clip(data.vc.ar, v0co, s[0]);
//			view3d_project_short_clip(data.vc.ar, v1co, s[1]);
//		} else {
//			view3d_project_short_noclip(data.vc.ar, v0co, s[0]);
//			view3d_project_short_noclip(data.vc.ar, v1co, s[1]);
//
//			if (data.clipVerts==2) {
//                if (!(s[0][0]>=0 && s[0][1]>= 0 && s[0][0]<data.vc.ar.winx && s[0][1]<data.vc.ar.winy))
//					if (!(s[1][0]>=0 && s[1][1]>= 0 && s[1][0]<data.vc.ar.winx && s[1][1]<data.vc.ar.winy))
//						return;
//			}
//		}
//
//		data.func(data.userData, eed, s[0][0], s[0][1], s[1][0], s[1][1], index);
//	}
//}
//
//void mesh_foreachScreenEdge(ViewContext *vc, void (*func)(void *userData, EditEdge *eed, int x0, int y0, int x1, int y1, int index), void *userData, int clipVerts)
//{
//	struct { void (*func)(void *userData, EditEdge *eed, int x0, int y0, int x1, int y1, int index); void *userData; ViewContext vc; int clipVerts; } data;
//	DerivedMesh *dm = editmesh_get_derived_cage(vc.scene, vc.obedit, vc.em, CD_MASK_BAREMESH);
//
//	data.vc= *vc;
//	data.func = func;
//	data.userData = userData;
//	data.clipVerts = clipVerts;
//
//	EM_init_index_arrays(vc.em, 0, 1, 0);
//	dm.foreachMappedEdge(dm, mesh_foreachScreenEdge__mapFunc, &data);
//	EM_free_index_arrays();
//
//	dm.release(dm);
//}
//
//static void mesh_foreachScreenFace__mapFunc(void *userData, int index, float *cent, float *no)
//{
//	struct { void (*func)(void *userData, EditFace *efa, int x, int y, int index); void *userData; ViewContext vc; } *data = userData;
//	EditFace *efa = EM_get_face_for_index(index);
//	short s[2];
//
//	if (efa && efa.h==0 && efa.fgonf!=EM_FGON) {
//		view3d_project_short_clip(data.vc.ar, cent, s);
//
//		data.func(data.userData, efa, s[0], s[1], index);
//	}
//}
//
//void mesh_foreachScreenFace(ViewContext *vc, void (*func)(void *userData, EditFace *efa, int x, int y, int index), void *userData)
//{
//	struct { void (*func)(void *userData, EditFace *efa, int x, int y, int index); void *userData; ViewContext vc; } data;
//	DerivedMesh *dm = editmesh_get_derived_cage(vc.scene, vc.obedit, vc.em, CD_MASK_BAREMESH);
//
//	data.vc= *vc;
//	data.func = func;
//	data.userData = userData;
//
//	EM_init_index_arrays(vc.em, 0, 0, 1);
//	dm.foreachMappedFaceCenter(dm, mesh_foreachScreenFace__mapFunc, &data);
//	EM_free_index_arrays();
//
//	dm.release(dm);
//}
//
//void nurbs_foreachScreenVert(ViewContext *vc, void (*func)(void *userData, Nurb *nu, BPoint *bp, BezTriple *bezt, int beztindex, int x, int y), void *userData)
//{
//	Curve *cu= vc.obedit.data;
//	short s[2] = {IS_CLIPPED, 0};
//	Nurb *nu;
//	int i;
//
//	for (nu= cu.editnurb.first; nu; nu=nu.next) {
//		if((nu.type & 7)==CU_BEZIER) {
//			for (i=0; i<nu.pntsu; i++) {
//				BezTriple *bezt = &nu.bezt[i];
//
//				if(bezt.hide==0) {
//					if (G.f & G_HIDDENHANDLES) {
//						view3d_project_short_clip(vc.ar, bezt.vec[1], s);
//						if (s[0] != IS_CLIPPED)
//							func(userData, nu, NULL, bezt, 1, s[0], s[1]);
//					} else {
//						view3d_project_short_clip(vc.ar, bezt.vec[0], s);
//						if (s[0] != IS_CLIPPED)
//							func(userData, nu, NULL, bezt, 0, s[0], s[1]);
//						view3d_project_short_clip(vc.ar, bezt.vec[1], s);
//						if (s[0] != IS_CLIPPED)
//							func(userData, nu, NULL, bezt, 1, s[0], s[1]);
//						view3d_project_short_clip(vc.ar, bezt.vec[2], s);
//						if (s[0] != IS_CLIPPED)
//							func(userData, nu, NULL, bezt, 2, s[0], s[1]);
//					}
//				}
//			}
//		}
//		else {
//			for (i=0; i<nu.pntsu*nu.pntsv; i++) {
//				BPoint *bp = &nu.bp[i];
//
//				if(bp.hide==0) {
//					view3d_project_short_clip(vc.ar, bp.vec, s);
//					if (s[0] != IS_CLIPPED)
//						func(userData, nu, bp, NULL, -1, s[0], s[1]);
//				}
//			}
//		}
//	}
//}
//
///* ************** DRAW MESH ****************** */
//
///* First section is all the "simple" draw routines,
// * ones that just pass some sort of primitive to GL2,
// * with perhaps various options to control lighting,
// * color, etc.
// *
// * These routines should not have user interface related
// * logic!!!
// */
//
//static void draw_dm_face_normals__mapFunc(void *userData, int index, float *cent, float *no)
//{
//	ToolSettings *ts= ((Scene *)userData).toolsettings;
//	EditFace *efa = EM_get_face_for_index(index);
//
//	if (efa.h==0 && efa.fgonf!=EM_FGON) {
//		glVertex3fv(cent);
//		glVertex3f(	cent[0] + no[0]*ts.normalsize,
//					cent[1] + no[1]*ts.normalsize,
//					cent[2] + no[2]*ts.normalsize);
//	}
//}
//static void draw_dm_face_normals(Scene *scene, DerivedMesh *dm)
//{
//	glBegin(GL_LINES);
//	dm.foreachMappedFaceCenter(dm, draw_dm_face_normals__mapFunc, scene);
//	glEnd();
//}
//
//static void draw_dm_face_centers__mapFunc(void *userData, int index, float *cent, float *no)
//{
//	EditFace *efa = EM_get_face_for_index(index);
//	int sel = *((int*) userData);
//
//	if (efa.h==0 && efa.fgonf!=EM_FGON && (efa.f&SELECT)==sel) {
//		bglVertex3fv(cent);
//	}
//}
//static void draw_dm_face_centers(DerivedMesh *dm, int sel)
//{
//	bglBegin(GL_POINTS);
//	dm.foreachMappedFaceCenter(dm, draw_dm_face_centers__mapFunc, &sel);
//	bglEnd();
//}
//
//static void draw_dm_vert_normals__mapFunc(void *userData, int index, float *co, float *no_f, short *no_s)
//{
//	Scene *scene= (Scene *)userData;
//	ToolSettings *ts= scene.toolsettings;
//	EditVert *eve = EM_get_vert_for_index(index);
//
//	if (eve.h==0) {
//		glVertex3fv(co);
//
//		if (no_f) {
//			glVertex3f(	co[0] + no_f[0]*ts.normalsize,
//						co[1] + no_f[1]*ts.normalsize,
//						co[2] + no_f[2]*ts.normalsize);
//		} else {
//			glVertex3f(	co[0] + no_s[0]*ts.normalsize/32767.0f,
//						co[1] + no_s[1]*ts.normalsize/32767.0f,
//						co[2] + no_s[2]*ts.normalsize/32767.0f);
//		}
//	}
//}
//static void draw_dm_vert_normals(Scene *scene, DerivedMesh *dm)
//{
//	glBegin(GL_LINES);
//	dm.foreachMappedVert(dm, draw_dm_vert_normals__mapFunc, scene);
//	glEnd();
//}
//
//	/* Draw verts with color set based on selection */
public static ForeachMappedVertFunc draw_dm_verts__mapFunc = new ForeachMappedVertFunc() {
public void func(GL2 gl, Object userData, int index, float[] co, float[] no_f, short[] no_s)
//static void draw_dm_verts__mapFunc(void *userData, int index, float *co, float *no_f, short *no_s)
{
//	struct { int sel; EditVert *eve_act; } * data = userData;
        Data data = (Data)userData;
	EditVert eve = EditMeshUtil.EM_get_vert_for_index(index);

	if (eve.h==0 && (eve.f&Blender.SELECT)==data.sel) {
		/* draw active larger - need to stop/start point drawing for this :/ */
		if (eve==data.eve_act) {
			float size = Resources.UI_GetThemeValuef(Resources.TH_VERTEX_SIZE);
			Resources.UI_ThemeColor4(gl, Resources.TH_EDITMESH_ACTIVE);

			GlUtil.bglEnd(gl);

			gl.glPointSize(size);
			GlUtil.bglBegin(gl, GL2.GL_POINTS);
			GlUtil.bglVertex3fv(gl, co);
			GlUtil.bglEnd(gl);

			Resources.UI_ThemeColor4(gl, data.sel!=0?Resources.TH_VERTEX_SELECT:Resources.TH_VERTEX);
			gl.glPointSize(size);
			GlUtil.bglBegin(gl, GL2.GL_POINTS);
		} else {
			GlUtil.bglVertex3fv(gl, co);
		}
	}
}};

static void draw_dm_verts(GL2 gl, DerivedMesh dm, int sel, EditVert eve_act)
{
//	struct { int sel; EditVert *eve_act; } data;
        Data data = new Data();
	data.sel = sel;
	data.eve_act = eve_act;

	GlUtil.bglBegin(gl, GL2.GL_POINTS);
	dm.foreachMappedVert.run(gl, dm, draw_dm_verts__mapFunc, data);
	GlUtil.bglEnd(gl);
}

//	/* Draw edges with color set based on selection */
public static DrawMappedEdgesFunc draw_dm_edges_sel__setDrawOptions = new DrawMappedEdgesFunc() {
public boolean run(GL2 gl, Object userData, int index)
//static int draw_dm_edges_sel__setDrawOptions(void *userData, int index)
{
	EditEdge eed = EditMeshUtil.EM_get_edge_for_index(index);
//	//unsigned char **cols = userData, *col;
//	struct { unsigned char *baseCol, *selCol, *actCol; EditEdge *eed_act; } * data = userData;
        EdgeSelData data = (EdgeSelData)userData;
	byte[] col;

	if (eed.h==0) {
		if (eed==data.eed_act) {
			gl.glColor4ubv(data.actCol,0);
		} else {
			if ((eed.f&Blender.SELECT)!=0) {
				col = data.selCol;
			} else {
				col = data.baseCol;
			}
			/* no alpha, this is used so a transparent color can disable drawing unselected edges in editmode  */
			if (col[3]==0)
                            return false;

			gl.glColor4ubv(col,0);
		}
		return true;
	} else {
		return false;
	}
}};

static void draw_dm_edges_sel(GL2 gl, DerivedMesh dm, byte[] baseCol, byte[] selCol, byte[] actCol, EditEdge eed_act)
{
//	struct { unsigned char *baseCol, *selCol, *actCol; EditEdge *eed_act; } data;
        EdgeSelData data = new EdgeSelData();

	data.baseCol = baseCol;
	data.selCol = selCol;
	data.actCol = actCol;
	data.eed_act = eed_act;
	dm.drawMappedEdges.run(gl, dm, draw_dm_edges_sel__setDrawOptions, data);
}

//	/* Draw edges */
public static DrawMappedEdgesFunc draw_dm_edges__setDrawOptions = new DrawMappedEdgesFunc() {
public boolean run(GL2 gl, Object userData, int index)
//static int draw_dm_edges__setDrawOptions(void *userData, int index)
{
	return EditMeshUtil.EM_get_edge_for_index(index).h==0;
}};
static void draw_dm_edges(GL2 gl, DerivedMesh dm)
{
	dm.drawMappedEdges.run(gl, dm, draw_dm_edges__setDrawOptions, null);
}

//	/* Draw edges with color interpolated based on selection */
//static int draw_dm_edges_sel_interp__setDrawOptions(void *userData, int index)
//{
//	return EM_get_edge_for_index(index).h==0;
//}
//static void draw_dm_edges_sel_interp__setDrawInterpOptions(void *userData, int index, float t)
//{
//	EditEdge *eed = EM_get_edge_for_index(index);
//	unsigned char **cols = userData;
//	unsigned char *col0 = cols[(eed.v1.f&SELECT)?1:0];
//	unsigned char *col1 = cols[(eed.v2.f&SELECT)?1:0];
//
//	glColor4ub(	col0[0] + (col1[0]-col0[0])*t,
//				col0[1] + (col1[1]-col0[1])*t,
//				col0[2] + (col1[2]-col0[2])*t,
//				col0[3] + (col1[3]-col0[3])*t);
//}
//static void draw_dm_edges_sel_interp(DerivedMesh *dm, unsigned char *baseCol, unsigned char *selCol)
//{
//	unsigned char *cols[2];
//	cols[0] = baseCol;
//	cols[1] = selCol;
//	dm.drawMappedEdgesInterp(dm, draw_dm_edges_sel_interp__setDrawOptions, draw_dm_edges_sel_interp__setDrawInterpOptions, cols);
//}
//
//	/* Draw only seam edges */
//static int draw_dm_edges_seams__setDrawOptions(void *userData, int index)
//{
//	EditEdge *eed = EM_get_edge_for_index(index);
//
//	return (eed.h==0 && eed.seam);
//}
//static void draw_dm_edges_seams(DerivedMesh *dm)
//{
//	dm.drawMappedEdges(dm, draw_dm_edges_seams__setDrawOptions, NULL);
//}
//
//	/* Draw only sharp edges */
//static int draw_dm_edges_sharp__setDrawOptions(void *userData, int index)
//{
//	EditEdge *eed = EM_get_edge_for_index(index);
//
//	return (eed.h==0 && eed.sharp);
//}
//static void draw_dm_edges_sharp(DerivedMesh *dm)
//{
//	dm.drawMappedEdges(dm, draw_dm_edges_sharp__setDrawOptions, NULL);
//}
//
//
//	/* Draw faces with color set based on selection
//	 * return 2 for the active face so it renders with stipple enabled */
public static DrawMappedFacesFunc draw_dm_faces_sel__setDrawOptions = new DrawMappedFacesFunc() {
public int run(GL2 gl, Object userData, int index, int[] drawSmooth_r)
//static int draw_dm_faces_sel__setDrawOptions(void *userData, int index, int *drawSmooth_r)
{
//	struct { unsigned char *cols[3]; EditFace *efa_act; } * data = userData;
        FaceData data = (FaceData)userData;
        EditFace efa = EditMeshUtil.EM_get_face_for_index(index);
	byte[] col;

	if (efa.h==0) {
		if (efa == data.efa_act) {
			gl.glColor4ubv(data.cols[2],0);
			return 2; /* stipple */
		} else {
			col = data.cols[(efa.f&Blender.SELECT)!=0?1:0];
			if (col[3]==0) return 0;
			gl.glColor4ubv(col,0);
			return 1;
		}
	}
	return 0;
}};

/* also draws the active face */
static void draw_dm_faces_sel(GL2 gl, DerivedMesh dm, byte[] baseCol, byte[] selCol, byte[] actCol, EditFace efa_act)
{
//	struct { unsigned char *cols[3]; EditFace *efa_act; } data;
        FaceData data = new FaceData();
	data.cols[0] = baseCol;
	data.cols[1] = selCol;
	data.cols[2] = actCol;
	data.efa_act = efa_act;

	dm.drawMappedFaces.run(gl, dm, draw_dm_faces_sel__setDrawOptions, data, 0);
}

//static int draw_dm_creases__setDrawOptions(void *userData, int index)
//{
//	EditEdge *eed = EM_get_edge_for_index(index);
//
//	if (eed.h==0 && eed.crease!=0.0) {
//		UI_ThemeColorBlend(TH_WIRE, TH_EDGE_SELECT, eed.crease);
//		return 1;
//	} else {
//		return 0;
//	}
//}
//static void draw_dm_creases(DerivedMesh *dm)
//{
//	glLineWidth(3.0);
//	dm.drawMappedEdges(dm, draw_dm_creases__setDrawOptions, NULL);
//	glLineWidth(1.0);
//}
//
//static int draw_dm_bweights__setDrawOptions(void *userData, int index)
//{
//	EditEdge *eed = EM_get_edge_for_index(index);
//
//	if (eed.h==0 && eed.bweight!=0.0) {
//		UI_ThemeColorBlend(TH_WIRE, TH_EDGE_SELECT, eed.bweight);
//		return 1;
//	} else {
//		return 0;
//	}
//}
//static void draw_dm_bweights__mapFunc(void *userData, int index, float *co, float *no_f, short *no_s)
//{
//	EditVert *eve = EM_get_vert_for_index(index);
//
//	if (eve.h==0 && eve.bweight!=0.0) {
//		UI_ThemeColorBlend(TH_VERTEX, TH_VERTEX_SELECT, eve.bweight);
//		bglVertex3fv(co);
//	}
//}
//static void draw_dm_bweights(Scene *scene, DerivedMesh *dm)
//{
//	ToolSettings *ts= scene.toolsettings;
//
//	if (ts.selectmode & SCE_SELECT_VERTEX) {
//		glPointSize(UI_GetThemeValuef(TH_VERTEX_SIZE) + 2);
//		bglBegin(GL_POINTS);
//		dm.foreachMappedVert(dm, draw_dm_bweights__mapFunc, NULL);
//		bglEnd();
//	}
//	else {
//		glLineWidth(3.0);
//		dm.drawMappedEdges(dm, draw_dm_bweights__setDrawOptions, NULL);
//		glLineWidth(1.0);
//	}
//}

/* Second section of routines: Combine first sets to form fancy
 * drawing routines (for example rendering twice to get overlays).
 *
 * Also includes routines that are basic drawing but are too
 * specialized to be split out (like drawing creases or measurements).
 */

/* EditMesh drawing routines*/

static void draw_em_fancy_verts(GL2 gl, Scene scene, View3D v3d, bObject obedit, EditMesh em, DerivedMesh cageDM, EditVert eve_act)
{
	ToolSettings ts= scene.toolsettings;
	int sel;

	if(v3d.zbuf!=0) gl.glDepthMask(false);		// disable write in zbuffer, zbuf select

	for (sel=0; sel<2; sel++) {
		byte[] col=new byte[4], fcol=new byte[4];
		int pass;

		Resources.UI_GetThemeColor3ubv(sel!=0?Resources.TH_VERTEX_SELECT:Resources.TH_VERTEX, col);
		Resources.UI_GetThemeColor3ubv(sel!=0?Resources.TH_FACE_DOT:Resources.TH_WIRE, fcol);

		for (pass=0; pass<2; pass++) {
			float size = Resources.UI_GetThemeValuef(Resources.TH_VERTEX_SIZE);
			float fsize = Resources.UI_GetThemeValuef(Resources.TH_FACEDOT_SIZE);

			if (pass==0) {
				if(v3d.zbuf!=0 && (v3d.flag&View3dTypes.V3D_ZBUF_SELECT)==0) {
					gl.glDisable(GL2.GL_DEPTH_TEST);

					gl.glEnable(GL2.GL_BLEND);
				} else {
					continue;
				}

				size = (size>2.1f?size/2.0f:size);
				fsize = (fsize>2.1f?fsize/2.0f:fsize);
				col[3] = fcol[3] = 100;
			} else {
				col[3] = fcol[3] = (byte)255;
			}

			if((ts.selectmode & SceneTypes.SCE_SELECT_VERTEX)!=0) {
				gl.glPointSize(size);
				gl.glColor4ubv(col,0);
				draw_dm_verts(gl, cageDM, sel, eve_act);
			}

//			if( CHECK_OB_DRAWFACEDOT(scene, v3d, obedit.dt) ) {
//				glPointSize(fsize);
//				glColor4ubv((GLubyte *)fcol);
//				draw_dm_face_centers(cageDM, sel);
//			}

			if (pass==0) {
				gl.glDisable(GL2.GL_BLEND);
				gl.glEnable(GL2.GL_DEPTH_TEST);
			}
		}
	}

	if(v3d.zbuf!=0) gl.glDepthMask(true);
	gl.glPointSize(1.0f);
}

static void draw_em_fancy_edges(GL2 gl, Scene scene, View3D v3d, Mesh me, DerivedMesh cageDM, boolean sel_only, EditEdge eed_act)
{
	ToolSettings ts= scene.toolsettings;
	int pass;
	byte[] wireCol=new byte[4], selCol=new byte[4], actCol=new byte[4];

	/* since this function does transparant... */
	Resources.UI_GetThemeColor4ubv(Resources.TH_EDGE_SELECT, selCol);
	Resources.UI_GetThemeColor4ubv(Resources.TH_WIRE, wireCol);
	Resources.UI_GetThemeColor4ubv(Resources.TH_EDITMESH_ACTIVE, actCol);

	/* when sel only is used, dont render wire, only selected, this is used for
	 * textured draw mode when the 'edges' option is disabled */
	if (sel_only)
		wireCol[3] = 0;

	for (pass=0; pass<2; pass++) {
			/* show wires in transparant when no zbuf clipping for select */
		if (pass==0) {
			if (v3d.zbuf!=0 && (v3d.flag & View3dTypes.V3D_ZBUF_SELECT)==0) {
				gl.glEnable(GL2.GL_BLEND);
				gl.glDisable(GL2.GL_DEPTH_TEST);
				selCol[3] = 85;
				if (!sel_only) wireCol[3] = 85;
			} else {
				continue;
			}
		} else {
			selCol[3] = (byte)255;
			if (!sel_only) wireCol[3] = (byte)255;
		}

		if(ts.selectmode == SceneTypes.SCE_SELECT_FACE) {
			draw_dm_edges_sel(gl, cageDM, wireCol, selCol, actCol, eed_act);
		}
		else if( (me.drawflag & MeshTypes.ME_DRAWEDGES)!=0 || (ts.selectmode & SceneTypes.SCE_SELECT_EDGE)!=0 ) {
//			if(cageDM.drawMappedEdgesInterp && (ts.selectmode & SceneTypes.SCE_SELECT_VERTEX)) {
//				glShadeModel(GL_SMOOTH);
//				draw_dm_edges_sel_interp(cageDM, wireCol, selCol);
//				glShadeModel(GL_FLAT);
//			} else {
				draw_dm_edges_sel(gl, cageDM, wireCol, selCol, actCol, eed_act);
//			}
		}
		else {
			if (!sel_only) {
				gl.glColor4ubv(wireCol,0);
				draw_dm_edges(gl, cageDM);
			}
		}

		if (pass==0) {
			gl.glDisable(GL2.GL_BLEND);
			gl.glEnable(GL2.GL_DEPTH_TEST);
		}
	}
}

//static void draw_em_measure_stats(View3D *v3d, RegionView3D *rv3d, Object *ob, EditMesh *em)
//{
//	Mesh *me= ob.data;
//	EditEdge *eed;
//	EditFace *efa;
//	float v1[3], v2[3], v3[3], v4[3], x, y, z;
//	float fvec[3];
//	char val[32]; /* Stores the measurement display text here */
//	char conv_float[5]; /* Use a float conversion matching the grid size */
//	float area, col[3]; /* area of the face,  color of the text to draw */
//
//	if(G.f & (G_RENDER_OGL|G_RENDER_SHADOW))
//		return;
//
//	/* make the precission of the pronted value proportionate to the gridsize */
//	if ((v3d.grid) < 0.01)
//		strcpy(conv_float, "%.6f");
//	else if ((v3d.grid) < 0.1)
//		strcpy(conv_float, "%.5f");
//	else if ((v3d.grid) < 1.0)
//		strcpy(conv_float, "%.4f");
//	else if ((v3d.grid) < 10.0)
//		strcpy(conv_float, "%.3f");
//	else
//		strcpy(conv_float, "%.2f");
//
//
//	if(v3d.zbuf && (v3d.flag & V3D_ZBUF_SELECT)==0)
//		glDisable(GL_DEPTH_TEST);
//
//	if(v3d.zbuf) bglPolygonOffset(rv3d.dist, 5.0);
//
//	if(me.drawflag & ME_DRAW_EDGELEN) {
//		UI_GetThemeColor3fv(TH_TEXT, col);
//		/* make color a bit more red */
//		if(col[0]> 0.5) {col[1]*=0.7; col[2]*= 0.7;}
//		else col[0]= col[0]*0.7 + 0.3;
//		glColor3fv(col);
//
//		for(eed= em.edges.first; eed; eed= eed.next) {
//			/* draw non fgon edges, or selected edges, or edges next to selected verts while draging */
//			if((eed.h != EM_FGON) && ((eed.f & SELECT) || (G.moving && ((eed.v1.f & SELECT) || (eed.v2.f & SELECT)) ))) {
//				VECCOPY(v1, eed.v1.co);
//				VECCOPY(v2, eed.v2.co);
//
//				x= 0.5*(v1[0]+v2[0]);
//				y= 0.5*(v1[1]+v2[1]);
//				z= 0.5*(v1[2]+v2[2]);
//
//				if(v3d.flag & V3D_GLOBAL_STATS) {
//					Mat4MulVecfl(ob.obmat, v1);
//					Mat4MulVecfl(ob.obmat, v2);
//				}
//
//				sprintf(val, conv_float, VecLenf(v1, v2));
//				view3d_object_text_draw_add(x, y, z, val, 0);
//			}
//		}
//	}
//
//	if(me.drawflag & ME_DRAW_FACEAREA) {
//// XXX		extern int faceselectedOR(EditFace *efa, int flag);		// editmesh.h shouldn't be in this file... ok for now?
//
//		UI_GetThemeColor3fv(TH_TEXT, col);
//		/* make color a bit more green */
//		if(col[1]> 0.5) {col[0]*=0.7; col[2]*= 0.7;}
//		else col[1]= col[1]*0.7 + 0.3;
//		glColor3fv(col);
//
//		for(efa= em.faces.first; efa; efa= efa.next) {
//			if((efa.f & SELECT)) { // XXX || (G.moving && faceselectedOR(efa, SELECT)) ) {
//				VECCOPY(v1, efa.v1.co);
//				VECCOPY(v2, efa.v2.co);
//				VECCOPY(v3, efa.v3.co);
//				if (efa.v4) {
//					VECCOPY(v4, efa.v4.co);
//				}
//				if(v3d.flag & V3D_GLOBAL_STATS) {
//					Mat4MulVecfl(ob.obmat, v1);
//					Mat4MulVecfl(ob.obmat, v2);
//					Mat4MulVecfl(ob.obmat, v3);
//					if (efa.v4) Mat4MulVecfl(ob.obmat, v4);
//				}
//
//				if (efa.v4)
//					area=  AreaQ3Dfl(v1, v2, v3, v4);
//				else
//					area = AreaT3Dfl(v1, v2, v3);
//
//				sprintf(val, conv_float, area);
//				view3d_object_text_draw_add(efa.cent[0], efa.cent[1], efa.cent[2], val, 0);
//			}
//		}
//	}
//
//	if(me.drawflag & ME_DRAW_EDGEANG) {
//		EditEdge *e1, *e2, *e3, *e4;
//
//		UI_GetThemeColor3fv(TH_TEXT, col);
//		/* make color a bit more blue */
//		if(col[2]> 0.5) {col[0]*=0.7; col[1]*= 0.7;}
//		else col[2]= col[2]*0.7 + 0.3;
//		glColor3fv(col);
//
//		for(efa= em.faces.first; efa; efa= efa.next) {
//			VECCOPY(v1, efa.v1.co);
//			VECCOPY(v2, efa.v2.co);
//			VECCOPY(v3, efa.v3.co);
//			if(efa.v4) {
//				VECCOPY(v4, efa.v4.co);
//			}
//			else {
//				VECCOPY(v4, v3);
//			}
//			if(v3d.flag & V3D_GLOBAL_STATS) {
//				Mat4MulVecfl(ob.obmat, v1);
//				Mat4MulVecfl(ob.obmat, v2);
//				Mat4MulVecfl(ob.obmat, v3);
//				Mat4MulVecfl(ob.obmat, v4);
//			}
//
//			e1= efa.e1;
//			e2= efa.e2;
//			e3= efa.e3;
//			if(efa.e4) e4= efa.e4; else e4= e3;
//
//			/* Calculate the angles */
//
//			if( (e4.f & e1.f & SELECT) || (G.moving && (efa.v1.f & SELECT)) ) {
//				/* Vec 1 */
//				sprintf(val,"%.3f", VecAngle3(v4, v1, v2));
//				VecLerpf(fvec, efa.cent, efa.v1.co, 0.8);
//				view3d_object_text_draw_add(efa.cent[0], efa.cent[1], efa.cent[2], val, 0);
//			}
//			if( (e1.f & e2.f & SELECT) || (G.moving && (efa.v2.f & SELECT)) ) {
//				/* Vec 2 */
//				sprintf(val,"%.3f", VecAngle3(v1, v2, v3));
//				VecLerpf(fvec, efa.cent, efa.v2.co, 0.8);
//				view3d_object_text_draw_add(fvec[0], fvec[1], fvec[2], val, 0);
//			}
//			if( (e2.f & e3.f & SELECT) || (G.moving && (efa.v3.f & SELECT)) ) {
//				/* Vec 3 */
//				if(efa.v4)
//					sprintf(val,"%.3f", VecAngle3(v2, v3, v4));
//				else
//					sprintf(val,"%.3f", VecAngle3(v2, v3, v1));
//				VecLerpf(fvec, efa.cent, efa.v3.co, 0.8);
//				view3d_object_text_draw_add(fvec[0], fvec[1], fvec[2], val, 0);
//			}
//				/* Vec 4 */
//			if(efa.v4) {
//				if( (e3.f & e4.f & SELECT) || (G.moving && (efa.v4.f & SELECT)) ) {
//					sprintf(val,"%.3f", VecAngle3(v3, v4, v1));
//					VecLerpf(fvec, efa.cent, efa.v4.co, 0.8);
//					view3d_object_text_draw_add(fvec[0], fvec[1], fvec[2], val, 0);
//				}
//			}
//		}
//	}
//
//	if(v3d.zbuf) {
//		glEnable(GL_DEPTH_TEST);
//		bglPolygonOffset(rv3d.dist, 0.0);
//	}
//}
//
public static DrawMappedFacesFunc draw_em_fancy__setFaceOpts = new DrawMappedFacesFunc() {
public int run(GL2 gl, Object userData, int index, int[] drawSmooth_r)
//static int draw_em_fancy__setFaceOpts(void *userData, int index, int *drawSmooth_r)
{
	EditFace efa = EditMeshUtil.EM_get_face_for_index(index);

	if (efa.h==0) {
//		GpuDraw.GPU_enable_material(efa.mat_nr+1, null);
		return 1;
	}
	else
		return 0;
}};

//static int draw_em_fancy__setGLSLFaceOpts(void *userData, int index)
//{
//	EditFace *efa = EM_get_face_for_index(index);
//
//	return (efa.h==0);
//}

static void draw_em_fancy(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, bObject ob, EditMesh em, DerivedMesh cageDM, DerivedMesh finalDM, int dt)
{
	Mesh me = (Mesh)ob.data;
	EditFace efa_act = EditMeshLib.EM_get_actFace(em, false); /* annoying but active faces is stored differently */
	EditEdge eed_act = null;
	EditVert eve_act = null;

	if (em.selected.last!=null) {
		EditSelection ese = em.selected.last;
		/* face is handeled above */
		/*if (ese.type == EDITFACE ) {
			efa_act = (EditFace *)ese.data;
		} else */ if ( ese.type == EditVertUtil.EDITEDGE ) {
			eed_act = (EditEdge)ese.data;
		} else if ( ese.type == EditVertUtil.EDITVERT ) {
			eve_act = (EditVert)ese.data;
		}
	}

	EditMeshUtil.EM_init_index_arrays(em, true, true, true);

	if(dt>ObjectTypes.OB_WIRE) {
		if(ObjectTypes.CHECK_OB_DRAWTEXTURE(v3d, dt)) {
//			if(draw_glsl_material(scene, ob, v3d, dt)) {
//				glFrontFace((ob.transflag&OB_NEG_SCALE)?GL_CW:GL_CCW);
//
//				finalDM.drawMappedFacesGLSL(finalDM, GPU_enable_material,
//					draw_em_fancy__setGLSLFaceOpts, NULL);
//				GPU_disable_material();
//
//				glFrontFace(GL_CCW);
//			}
//			else {
//				draw_mesh_textured(scene, v3d, rv3d, ob, finalDM, 0);
//			}
		}
		else {
			gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, me.flag & MeshTypes.ME_TWOSIDED);

			gl.glEnable(GL2.GL_LIGHTING);
			gl.glFrontFace((ob.transflag&ObjectTypes.OB_NEG_SCALE)!=0?GL2.GL_CW:GL2.GL_CCW);

			finalDM.drawMappedFaces.run(gl, finalDM, draw_em_fancy__setFaceOpts, 0, 0);

			gl.glFrontFace(GL2.GL_CCW);
			gl.glDisable(GL2.GL_LIGHTING);
		}

		// Setup for drawing wire over, disable zbuffer
		// write to show selected edge wires better
		Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

		GlUtil.bglPolygonOffset(gl, rv3d.dist, 1.0f);
		gl.glDepthMask(false);
	}
	else {
		if (cageDM!=finalDM) {
			Resources.UI_ThemeColorBlend(gl, Resources.TH_WIRE, Resources.TH_BACK, 0.7f);
			finalDM.drawEdges.run(gl, finalDM, true);
		}
	}

	if((me.drawflag & MeshTypes.ME_DRAWFACES)!=0 || Global.FACESEL_PAINT_TEST()) {	/* transp faces */
		byte[] col1=new byte[4], col2=new byte[4], col3=new byte[4];

		Resources.UI_GetThemeColor4ubv(Resources.TH_FACE, col1);
		Resources.UI_GetThemeColor4ubv(Resources.TH_FACE_SELECT, col2);
		Resources.UI_GetThemeColor4ubv(Resources.TH_EDITMESH_ACTIVE, col3);

		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask(false);		// disable write in zbuffer, needed for nice transp

		/* dont draw unselected faces, only selected, this is MUCH nicer when texturing */
		if (ObjectTypes.CHECK_OB_DRAWTEXTURE(v3d, dt))
			col1[3] = 0;

		draw_dm_faces_sel(gl, cageDM, col1, col2, col3, efa_act);

		gl.glDisable(GL2.GL_BLEND);
		gl.glDepthMask(true);		// restore write in zbuffer
	} else if (efa_act!=null) {
		/* even if draw faces is off it would be nice to draw the stipple face
		 * Make all other faces zero alpha except for the active
		 * */
		byte[] col1=new byte[4], col2=new byte[4], col3=new byte[4];
		col1[3] = col2[3] = 0; /* dont draw */
		Resources.UI_GetThemeColor4ubv(Resources.TH_EDITMESH_ACTIVE, col3);

		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask(false);		// disable write in zbuffer, needed for nice transp

		draw_dm_faces_sel(gl, cageDM, col1, col2, col3, efa_act);

		gl.glDisable(GL2.GL_BLEND);
		gl.glDepthMask(true);		// restore write in zbuffer

	}

//	/* here starts all fancy draw-extra over */
//	if((me.drawflag & ME_DRAWEDGES)==0 && CHECK_OB_DRAWTEXTURE(v3d, dt)) {
//		/* we are drawing textures and 'ME_DRAWEDGES' is disabled, dont draw any edges */
//
//		/* only draw selected edges otherwise there is no way of telling if a face is selected */
//		draw_em_fancy_edges(scene, v3d, me, cageDM, 1, eed_act);
//
//	} else {
//		if(me.drawflag & ME_DRAWSEAMS) {
//			UI_ThemeColor(TH_EDGE_SEAM);
//			glLineWidth(2);
//
//			draw_dm_edges_seams(cageDM);
//
//			glColor3ub(0,0,0);
//			glLineWidth(1);
//		}
//
//		if(me.drawflag & ME_DRAWSHARP) {
//			UI_ThemeColor(TH_EDGE_SHARP);
//			glLineWidth(2);
//
//			draw_dm_edges_sharp(cageDM);
//
//			glColor3ub(0,0,0);
//			glLineWidth(1);
//		}
//
//		if(me.drawflag & ME_DRAWCREASES) {
//			draw_dm_creases(cageDM);
//		}
//		if(me.drawflag & ME_DRAWBWEIGHTS) {
//			draw_dm_bweights(scene, cageDM);
//		}

		draw_em_fancy_edges(gl, scene, v3d, me, cageDM, false, eed_act);
//	}
	if(em!=null) {
//// XXX		retopo_matrix_update(v3d);

		draw_em_fancy_verts(gl, scene, v3d, ob, em, cageDM, eve_act);

//		if(me.drawflag & ME_DRAWNORMALS) {
//			UI_ThemeColor(TH_NORMAL);
//			draw_dm_face_normals(scene, cageDM);
//		}
//		if(me.drawflag & ME_DRAW_VNORMALS) {
//			UI_ThemeColor(TH_NORMAL);
//			draw_dm_vert_normals(scene, cageDM);
//		}
//
//		if(me.drawflag & (ME_DRAW_EDGELEN|ME_DRAW_FACEAREA|ME_DRAW_EDGEANG))
//			draw_em_measure_stats(v3d, rv3d, ob, em);
	}

	if(dt>ObjectTypes.OB_WIRE) {
		gl.glDepthMask(true);
		GlUtil.bglPolygonOffset(gl, rv3d.dist, 0.0f);
//		GPU_disable_material();
	}

	EditMeshUtil.EM_free_index_arrays();
}

/* Mesh drawing routines */

static void draw_mesh_object_outline(GL2 gl, View3D v3d, bObject ob, DerivedMesh dm)
{

	if(v3d.transp==0) {	// not when we draw the transparent pass
		gl.glLineWidth(2.0f);
		gl.glDepthMask(false);

		/* if transparent, we cannot draw the edges for solid select... edges have no material info.
		   drawFacesSolid() doesn't draw the transparent faces */
		if((ob.dtx & ObjectTypes.OB_DRAWTRANSP)!=0) {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			dm.drawFacesSolid.run(gl, dm); //, GPU_enable_material);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
//			GPU_disable_material();
		}
		else {
			dm.drawEdges.run(gl, dm, false);
		}

		gl.glLineWidth(1.0f);
		gl.glDepthMask(true);
	}
}

//static int wpaint__setSolidDrawOptions(void *userData, int index, int *drawSmooth_r)
//{
//	*drawSmooth_r = 1;
//	return 1;
//}

static void draw_mesh_fancy(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, Base base, int dt, int flag)
{
	bObject ob= base.object;
	Mesh me = (Mesh)ob.data;
//	Material *ma= give_current_material(ob, 1);
//	int hasHaloMat = (ma && (ma.material_type == MA_TYPE_HALO));
	int draw_wire = 0;
	int totvert, totedge, totface;
//	DispList dl;
//	DerivedMesh dm= DerivedMesh.mesh_get_derived_final(scene, ob, v3d.customdata_mask);
	DerivedMesh dm= DerivedMesh.mesh_get_derived_final(scene, ob, scene.customdata_mask);

	if(dm==null)
		return;

	if ((ob.dtx&ObjectTypes.OB_DRAWWIRE)!=0) {
		draw_wire = 2; /* draw wire after solid using zoffset and depth buffer adjusment */
	}

	totvert = dm.getNumVerts.run(dm);
	totedge = dm.getNumEdges.run(dm);
	totface = dm.getNumFaces.run(dm);

	/* vertexpaint, faceselect wants this, but it doesnt work for shaded? */
	if(dt!=ObjectTypes.OB_SHADED)
		gl.glFrontFace((ob.transflag&ObjectTypes.OB_NEG_SCALE)!=0?GL2.GL_CW:GL2.GL_CCW);

		// Unwanted combination.
	if (ob==(scene.basact!=null? scene.basact.object: null) && Global.FACESEL_PAINT_TEST()) draw_wire = 0;

	if(dt==ObjectTypes.OB_BOUNDBOX) {
//		draw_bounding_volume(scene, ob);
	}
//	else if(hasHaloMat || (totface==0 && totedge==0)) {
//		glPointSize(1.5);
//		dm.drawVerts(dm);
//		glPointSize(1.0);
//	}
	else if(dt==ObjectTypes.OB_WIRE || totface==0) {
		draw_wire = 1; /* draw wire only, no depth buffer stuff  */
	}
//	else if(	(ob==OBACT && (G.f & G_TEXTUREPAINT || FACESEL_PAINT_TEST)) ||
//				CHECK_OB_DRAWTEXTURE(v3d, dt))
//	{
//		int faceselect= (ob==OBACT && FACESEL_PAINT_TEST);
//
//		if ((v3d.flag&V3D_SELECT_OUTLINE) && (base.flag&SELECT) && !(G.f&G_PICKSEL || FACESEL_PAINT_TEST) && !draw_wire) {
//			draw_mesh_object_outline(v3d, ob, dm);
//		}
//
//		if(draw_glsl_material(scene, ob, v3d, dt)) {
//			glFrontFace((ob.transflag&OB_NEG_SCALE)?GL_CW:GL_CCW);
//
//			dm.drawFacesGLSL(dm, GPU_enable_material);
////			if(get_ob_property(ob, "Text"))
//// XXX				draw_mesh_text(ob, 1);
//			GPU_disable_material();
//
//			glFrontFace(GL_CCW);
//		}
//		else {
//			draw_mesh_textured(scene, v3d, rv3d, ob, dm, faceselect);
//		}
//
//		if(!faceselect) {
//			if(base.flag & SELECT)
//				UI_ThemeColor((ob==OBACT)?TH_ACTIVE:TH_SELECT);
//			else
//				UI_ThemeColor(TH_WIRE);
//
//			dm.drawLooseEdges(dm);
//		}
//	}
	else if(dt==ObjectTypes.OB_SOLID) {
		if((v3d.flag&View3dTypes.V3D_SELECT_OUTLINE)!=0 && (base.flag&Blender.SELECT)!=0 && draw_wire==0)
			draw_mesh_object_outline(gl, v3d, ob, dm);

		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, me.flag & MeshTypes.ME_TWOSIDED );

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glFrontFace((ob.transflag&ObjectTypes.OB_NEG_SCALE)!=0?GL2.GL_CW:GL2.GL_CCW);

		dm.drawFacesSolid.run(gl, dm);//, GPU_enable_material);
//		GPU_disable_material();

		gl.glFrontFace(GL2.GL_CCW);
		gl.glDisable(GL2.GL_LIGHTING);

		if((base.flag & Blender.SELECT)!=0) {
			Resources.UI_ThemeColor(gl, (ob==(scene.basact!=null? scene.basact.object: null))?Resources.TH_ACTIVE:Resources.TH_SELECT);
		} else {
			Resources.UI_ThemeColor(gl, Resources.TH_WIRE);
		}
		dm.drawLooseEdges.run(gl, dm);
	}
	else if(dt==ObjectTypes.OB_SHADED) {
//		int do_draw= 1;	/* to resolve all G.f settings below... */

//		if(ob==OBACT) {
//			do_draw= 0;
//			if( (G.f & G_WEIGHTPAINT)) {
//				/* enforce default material settings */
//				GPU_enable_material(0, NULL);
//
//				/* but set default spec */
//				glColorMaterial(GL_FRONT_AND_BACK, GL_SPECULAR);
//				glEnable(GL_COLOR_MATERIAL);	/* according manpages needed */
//				glColor3ub(120, 120, 120);
//				glDisable(GL_COLOR_MATERIAL);
//				/* diffuse */
//				glColorMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE);
//				glEnable(GL_LIGHTING);
//				glEnable(GL_COLOR_MATERIAL);
//
//				dm.drawMappedFaces(dm, wpaint__setSolidDrawOptions, me.mface, 1);
//				glDisable(GL_COLOR_MATERIAL);
//				glDisable(GL_LIGHTING);
//
//				GPU_disable_material();
//			}
//			else if((G.f & (G_VERTEXPAINT+G_TEXTUREPAINT)) && me.mcol) {
//				dm.drawMappedFaces(dm, wpaint__setSolidDrawOptions, NULL, 1);
//			}
//			else if(G.f & (G_VERTEXPAINT+G_TEXTUREPAINT)) {
//				glColor3f(1.0f, 1.0f, 1.0f);
//				dm.drawMappedFaces(dm, wpaint__setSolidDrawOptions, NULL, 0);
//			}
//			else do_draw= 1;
//		}
//		if(do_draw!=0) {
//			dl = ob.disp.first;
//			if (!dl || !dl.col1) {
//				/* release and reload derivedmesh because it might be freed in
//				   shadeDispList due to a different datamask */
//				dm.release(dm);
//				shadeDispList(scene, base);
//				dl = find_displist(&ob.disp, DL_VERTCOL);
//				dm= mesh_get_derived_final(scene, ob, v3d.customdata_mask);
//			}
//
//			if ((v3d.flag&V3D_SELECT_OUTLINE) && (base.flag&SELECT) && !draw_wire) {
//				draw_mesh_object_outline(v3d, ob, dm);
//			}
//
//				/* False for dupliframe objects */
//			if (dl) {
//				unsigned int *obCol1 = dl.col1;
//				unsigned int *obCol2 = dl.col2;
//
//				dm.drawFacesColored(dm, me.flag&ME_TWOSIDED, (unsigned char*) obCol1, (unsigned char*) obCol2);
//			}
//
//			if(base.flag & SELECT) {
//				UI_ThemeColor((ob==OBACT)?TH_ACTIVE:TH_SELECT);
//			} else {
//				UI_ThemeColor(TH_WIRE);
//			}
//			dm.drawLooseEdges(dm);
//		}
	}

//	/* set default draw color back for wire or for draw-extra later on */
//	if (dt!=OB_WIRE) {
//		if(base.flag & SELECT) {
//			if(ob==OBACT && ob.flag & OB_FROMGROUP)
//				UI_ThemeColor(TH_GROUP_ACTIVE);
//			else if(ob.flag & OB_FROMGROUP)
//				UI_ThemeColorShade(TH_GROUP_ACTIVE, -16);
//			else if(flag!=DRAW_CONSTCOLOR)
//				UI_ThemeColor((ob==OBACT)?TH_ACTIVE:TH_SELECT);
//			else
//				glColor3ub(80,80,80);
//		} else {
//			if (ob.flag & OB_FROMGROUP)
//				UI_ThemeColor(TH_GROUP);
//			else {
//				if(ob.dtx & OB_DRAWWIRE && flag==DRAW_CONSTCOLOR)
//					glColor3ub(80,80,80);
//				else
//					UI_ThemeColor(TH_WIRE);
//			}
//		}
//	}
	if (draw_wire!=0) {
			/* If drawing wire and drawtype is not OB_WIRE then we are
				* overlaying the wires.
				*
				* UPDATE bug #10290 - With this wire-only objects can draw
				* behind other objects depending on their order in the scene. 2x if 0's below. undo'ing zr's commit: r4059
				*
				* if draw wire is 1 then just drawing wire, no need for depth buffer stuff,
				* otherwise this wire is to overlay solid mode faces so do some depth buffer tricks.
				*/
//		if (dt!=OB_WIRE && draw_wire==2) {
//			bglPolygonOffset(rv3d.dist, 1.0);
//			glDepthMask(0);	// disable write in zbuffer, selected edge wires show better
//		}

		dm.drawEdges.run(gl, dm, (dt==ObjectTypes.OB_WIRE || totface==0));

//		if (dt!=OB_WIRE && draw_wire==2) {
//			glDepthMask(1);
//			bglPolygonOffset(rv3d.dist, 0.0);
//		}
	}

	dm.release.run(dm);
}

/* returns 1 if nothing was drawn, for detecting to draw an object center */
static int draw_mesh_object(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, Base base, int dt, int flag)
{
	bObject ob= base.object;
	bObject obedit= scene.obedit;
	Mesh me= (Mesh)ob.data;
	EditMesh em= (EditMesh)me.edit_mesh;
	int do_alpha_pass= 0, drawlinked= 0, retval= 0;
        boolean glsl, check_alpha;

	if(obedit!=null && ob!=obedit && ob.data==obedit.data) {
//		if(ob_get_key(ob));
//		else
                    drawlinked= 1;
	}

	if(ob==obedit || drawlinked!=0) {
		DerivedMesh[] finalDM={null}, cageDM={null};

		if (obedit!=ob)
			finalDM[0] = cageDM[0] = DerivedMesh.editmesh_get_derived_base(ob, em);
		else
//			cageDM[0] = DerivedMesh.editmesh_get_derived_cage_and_final(scene, ob, em, finalDM, v3d.customdata_mask);
			cageDM[0] = DerivedMesh.editmesh_get_derived_cage_and_final(scene, ob, em, finalDM, scene.customdata_mask);

//		if(dt>OB_WIRE) {
//			// no transp in editmode, the fancy draw over goes bad then
//			glsl = draw_glsl_material(scene, ob, v3d, dt);
//			GPU_begin_object_materials(v3d, rv3d, scene, ob, glsl, NULL);
//		}

		draw_em_fancy(gl, scene, v3d, rv3d, ob, em, cageDM[0], finalDM[0], dt);

//		GPU_end_object_materials();

//		if (obedit!=ob && finalDM)
//			finalDM.release(finalDM);
	}
//	else if(!em && (G.f & G_SCULPTMODE) &&(scene.sculptdata.flags & SCULPT_DRAW_FAST) &&
//	        OBACT==ob && !sculpt_modifiers_active(ob)) {
// XXX		sculptmode_draw_mesh(0);
//	}
	else {
		/* don't create boundbox here with mesh_get_bb(), the derived system will make it, puts deformed bb's OK */
		if(me.totface<=4 || View3dView.boundbox_clip(rv3d, ob.obmat, (ob.bb!=null)? ob.bb: me.bb)) {
//			glsl = draw_glsl_material(scene, ob, v3d, dt);
//			check_alpha = check_material_alpha(base, me, glsl);
//
//			if(dt==OB_SOLID || glsl) {
//				GPU_begin_object_materials(v3d, rv3d, scene, ob, glsl,
//					(check_alpha)? &do_alpha_pass: NULL);
//			}

			draw_mesh_fancy(gl, scene, v3d, rv3d, base, dt, flag);

//			GPU_end_object_materials();

			if(me.totvert==0) retval= 1;
		}
	}

	/* GPU_begin_object_materials checked if this is needed */
//	if(do_alpha_pass) add_view3d_after(v3d, base, V3D_TRANSP, flag);

	return retval;
}

///* ************** DRAW DISPLIST ****************** */
//
//static int draw_index_wire= 1;
//static int index3_nors_incr= 1;
//
///* returns 1 when nothing was drawn */
//static int drawDispListwire(ListBase *dlbase)
//{
//	DispList *dl;
//	int parts, nr;
//	float *data;
//
//	if(dlbase==NULL) return 1;
//
//	glDisableClientState(GL_NORMAL_ARRAY);
//	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
//
//	for(dl= dlbase.first; dl; dl= dl.next) {
//		if(dl.parts==0 || dl.nr==0)
//			continue;
//
//		data= dl.verts;
//
//		switch(dl.type) {
//		case DL_SEGM:
//
//			glVertexPointer(3, GL_FLOAT, 0, data);
//
//			for(parts=0; parts<dl.parts; parts++)
//				glDrawArrays(GL_LINE_STRIP, parts*dl.nr, dl.nr);
//
//			break;
//		case DL_POLY:
//
//			glVertexPointer(3, GL_FLOAT, 0, data);
//
//			for(parts=0; parts<dl.parts; parts++)
//				glDrawArrays(GL_LINE_LOOP, parts*dl.nr, dl.nr);
//
//			break;
//		case DL_SURF:
//
//			glVertexPointer(3, GL_FLOAT, 0, data);
//
//			for(parts=0; parts<dl.parts; parts++) {
//				if(dl.flag & DL_CYCL_U)
//					glDrawArrays(GL_LINE_LOOP, parts*dl.nr, dl.nr);
//				else
//					glDrawArrays(GL_LINE_STRIP, parts*dl.nr, dl.nr);
//			}
//
//			for(nr=0; nr<dl.nr; nr++) {
//				int ofs= 3*dl.nr;
//
//				data= (  dl.verts )+3*nr;
//				parts= dl.parts;
//
//				if(dl.flag & DL_CYCL_V) glBegin(GL_LINE_LOOP);
//				else glBegin(GL_LINE_STRIP);
//
//				while(parts--) {
//					glVertex3fv(data);
//					data+=ofs;
//				}
//				glEnd();
//
//				/* (ton) this code crashes for me when resolv is 86 or higher... no clue */
////				glVertexPointer(3, GL_FLOAT, sizeof(float)*3*dl.nr, data + 3*nr);
////				if(dl.flag & DL_CYCL_V)
////					glDrawArrays(GL_LINE_LOOP, 0, dl.parts);
////				else
////					glDrawArrays(GL_LINE_STRIP, 0, dl.parts);
//			}
//			break;
//
//		case DL_INDEX3:
//			if(draw_index_wire) {
//				glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				glDrawElements(GL_TRIANGLES, 3*dl.parts, GL_UNSIGNED_INT, dl.index);
//			}
//			break;
//
//		case DL_INDEX4:
//			if(draw_index_wire) {
//				glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				glDrawElements(GL_QUADS, 4*dl.parts, GL_UNSIGNED_INT, dl.index);
//			}
//			break;
//		}
//	}
//
//	glEnableClientState(GL_NORMAL_ARRAY);
//	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//
//	return 0;
//}
//
//static void drawDispListsolid(ListBase *lb, Object *ob, int glsl)
//{
//	DispList *dl;
//	GPUVertexAttribs gattribs;
//	float *data, curcol[4];
//	float *ndata;
//
//	if(lb==NULL) return;
//
//	/* for drawing wire */
//	glGetFloatv(GL_CURRENT_COLOR, curcol);
//
//	glEnable(GL_LIGHTING);
//
//	if(ob.transflag & OB_NEG_SCALE) glFrontFace(GL_CW);
//	else glFrontFace(GL_CCW);
//
//	if(ob.type==OB_MBALL) {	// mball always smooth shaded
//		glShadeModel(GL_SMOOTH);
//	}
//
//	dl= lb.first;
//	while(dl) {
//		data= dl.verts;
//		ndata= dl.nors;
//
//		switch(dl.type) {
//		case DL_SEGM:
//			if(ob.type==OB_SURF) {
//				int nr;
//
//				glDisable(GL_LIGHTING);
//				glColor3fv(curcol);
//
//				// glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				// glDrawArrays(GL_LINE_STRIP, 0, dl.nr);
//
//				glBegin(GL_LINE_STRIP);
//				for(nr= dl.nr; nr; nr--, data+=3)
//					glVertex3fv(data);
//				glEnd();
//
//				glEnable(GL_LIGHTING);
//			}
//			break;
//		case DL_POLY:
//			if(ob.type==OB_SURF) {
//				int nr;
//
//				UI_ThemeColor(TH_WIRE);
//				glDisable(GL_LIGHTING);
//
//				/* for some reason glDrawArrays crashes here in half of the platforms (not osx) */
//				//glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				//glDrawArrays(GL_LINE_LOOP, 0, dl.nr);
//
//				glBegin(GL_LINE_LOOP);
//				for(nr= dl.nr; nr; nr--, data+=3)
//					glVertex3fv(data);
//				glEnd();
//
//				glEnable(GL_LIGHTING);
//				break;
//			}
//		case DL_SURF:
//
//			if(dl.index) {
//				GPU_enable_material(dl.col+1, (glsl)? &gattribs: NULL);
//
//				if(dl.rt & CU_SMOOTH) glShadeModel(GL_SMOOTH);
//				else glShadeModel(GL_FLAT);
//
//				glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				glNormalPointer(GL_FLOAT, 0, dl.nors);
//				glDrawElements(GL_QUADS, 4*dl.totindex, GL_UNSIGNED_INT, dl.index);
//			}
//			break;
//
//		case DL_INDEX3:
//			GPU_enable_material(dl.col+1, (glsl)? &gattribs: NULL);
//
//			glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//
//			/* voor polys only one normal needed */
//			if(index3_nors_incr==0) {
//				glDisableClientState(GL_NORMAL_ARRAY);
//				glNormal3fv(ndata);
//			}
//			else
//				glNormalPointer(GL_FLOAT, 0, dl.nors);
//
//			glDrawElements(GL_TRIANGLES, 3*dl.parts, GL_UNSIGNED_INT, dl.index);
//
//			if(index3_nors_incr==0)
//				glEnableClientState(GL_NORMAL_ARRAY);
//
//			break;
//
//		case DL_INDEX4:
//			GPU_enable_material(dl.col+1, (glsl)? &gattribs: NULL);
//
//			glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//			glNormalPointer(GL_FLOAT, 0, dl.nors);
//			glDrawElements(GL_QUADS, 4*dl.parts, GL_UNSIGNED_INT, dl.index);
//
//			break;
//		}
//		dl= dl.next;
//	}
//
//	glShadeModel(GL_FLAT);
//	glDisable(GL_LIGHTING);
//	glFrontFace(GL_CCW);
//}
//
//static void drawDispListshaded(ListBase *lb, Object *ob)
//{
//	DispList *dl, *dlob;
//	unsigned int *cdata;
//
//	if(lb==NULL) return;
//
//	glShadeModel(GL_SMOOTH);
//	glDisableClientState(GL_NORMAL_ARRAY);
//	glEnableClientState(GL_COLOR_ARRAY);
//
//	dl= lb.first;
//	dlob= ob.disp.first;
//	while(dl && dlob) {
//
//		cdata= dlob.col1;
//		if(cdata==NULL) break;
//
//		switch(dl.type) {
//		case DL_SURF:
//			if(dl.index) {
//				glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//				glColorPointer(4, GL_UNSIGNED_BYTE, 0, cdata);
//				glDrawElements(GL_QUADS, 4*dl.totindex, GL_UNSIGNED_INT, dl.index);
//			}
//			break;
//
//		case DL_INDEX3:
//
//			glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//			glColorPointer(4, GL_UNSIGNED_BYTE, 0, cdata);
//			glDrawElements(GL_TRIANGLES, 3*dl.parts, GL_UNSIGNED_INT, dl.index);
//			break;
//
//		case DL_INDEX4:
//
//			glVertexPointer(3, GL_FLOAT, 0, dl.verts);
//			glColorPointer(4, GL_UNSIGNED_BYTE, 0, cdata);
//			glDrawElements(GL_QUADS, 4*dl.parts, GL_UNSIGNED_INT, dl.index);
//			break;
//		}
//
//		dl= dl.next;
//		dlob= dlob.next;
//	}
//
//	glShadeModel(GL_FLAT);
//	glEnableClientState(GL_NORMAL_ARRAY);
//	glDisableClientState(GL_COLOR_ARRAY);
//}
//
///* returns 1 when nothing was drawn */
//static int drawDispList(Scene *scene, View3D *v3d, RegionView3D *rv3d, Base *base, int dt)
//{
//	Object *ob= base.object;
//	ListBase *lb=0;
//	DispList *dl;
//	Curve *cu;
//	int solid, retval= 0;
//
//	solid= (dt > OB_WIRE);
//
//	switch(ob.type) {
//	case OB_FONT:
//	case OB_CURVE:
//		cu= ob.data;
//
//		lb= &cu.disp;
//
//		if(solid) {
//			dl= lb.first;
//			if(dl==NULL) return 1;
//
//			if(dl.nors==0) addnormalsDispList(ob, lb);
//			index3_nors_incr= 0;
//
//			if( displist_has_faces(lb)==0) {
//				draw_index_wire= 0;
//				drawDispListwire(lb);
//				draw_index_wire= 1;
//			}
//			else {
//				if(draw_glsl_material(scene, ob, v3d, dt)) {
//					GPU_begin_object_materials(v3d, rv3d, scene, ob, 1, NULL);
//					drawDispListsolid(lb, ob, 1);
//					GPU_end_object_materials();
//				}
//				else if(dt == OB_SHADED) {
//					if(ob.disp.first==0) shadeDispList(scene, base);
//					drawDispListshaded(lb, ob);
//				}
//				else {
//					GPU_begin_object_materials(v3d, rv3d, scene, ob, 0, NULL);
//					glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, 0);
//					drawDispListsolid(lb, ob, 0);
//					GPU_end_object_materials();
//				}
//				if(cu.editnurb && cu.bevobj==NULL && cu.taperobj==NULL && cu.ext1 == 0.0 && cu.ext2 == 0.0) {
//					cpack(0);
//					draw_index_wire= 0;
//					drawDispListwire(lb);
//					draw_index_wire= 1;
//				}
//			}
//			index3_nors_incr= 1;
//		}
//		else {
//			draw_index_wire= 0;
//			retval= drawDispListwire(lb);
//			draw_index_wire= 1;
//		}
//		break;
//	case OB_SURF:
//
//		lb= &((Curve *)ob.data).disp;
//
//		if(solid) {
//			dl= lb.first;
//			if(dl==NULL) return 1;
//
//			if(dl.nors==NULL) addnormalsDispList(ob, lb);
//
//			if(draw_glsl_material(scene, ob, v3d, dt)) {
//				GPU_begin_object_materials(v3d, rv3d, scene, ob, 1, NULL);
//				drawDispListsolid(lb, ob, 1);
//				GPU_end_object_materials();
//			}
//			else if(dt==OB_SHADED) {
//				if(ob.disp.first==NULL) shadeDispList(scene, base);
//				drawDispListshaded(lb, ob);
//			}
//			else {
//				GPU_begin_object_materials(v3d, rv3d, scene, ob, 0, NULL);
//				glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, 0);
//				drawDispListsolid(lb, ob, 0);
//				GPU_end_object_materials();
//			}
//		}
//		else {
//			retval= drawDispListwire(lb);
//		}
//		break;
//	case OB_MBALL:
//
//		if( is_basis_mball(ob)) {
//			lb= &ob.disp;
//			if(lb.first==NULL) makeDispListMBall(scene, ob);
//			if(lb.first==NULL) return 1;
//
//			if(solid) {
//
//				if(draw_glsl_material(scene, ob, v3d, dt)) {
//					GPU_begin_object_materials(v3d, rv3d, scene, ob, 1, NULL);
//					drawDispListsolid(lb, ob, 1);
//					GPU_end_object_materials();
//				}
//				else if(dt == OB_SHADED) {
//					dl= lb.first;
//					if(dl && dl.col1==0) shadeDispList(scene, base);
//					drawDispListshaded(lb, ob);
//				}
//				else {
//					GPU_begin_object_materials(v3d, rv3d, scene, ob, 0, NULL);
//					glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, 0);
//					drawDispListsolid(lb, ob, 0);
//					GPU_end_object_materials();
//				}
//			}
//			else{
//				/* MetaBalls use DL_INDEX4 type of DispList */
//				retval= drawDispListwire(lb);
//			}
//		}
//		break;
//	}
//
//	return retval;
//}
//
///* *********** text drawing for particles ************* */
//static ListBase pstrings= {NULL, NULL};
//
//typedef struct ViewParticleString {
//	struct ViewParticleString *next, *prev;
//	float vec[3], col[4];
//	char str[128];
//	short mval[2];
//	short xoffs;
//} ViewParticleString;
//
//
//void view3d_particle_text_draw_add(float x, float y, float z, char *str, short xoffs)
//{
//	ViewObjectString *vos= MEM_callocN(sizeof(ViewObjectString), "ViewObjectString");
//
//	BLI_addtail(&pstrings, vos);
//	BLI_strncpy(vos.str, str, 128);
//	vos.vec[0]= x;
//	vos.vec[1]= y;
//	vos.vec[2]= z;
//	glGetFloatv(GL_CURRENT_COLOR, vos.col);
//	vos.xoffs= xoffs;
//}
//
//static void view3d_particle_text_draw(View3D *v3d, ARegion *ar)
//{
//	ViewObjectString *vos;
//	int tot= 0;
//
//	/* project first and test */
//	for(vos= pstrings.first; vos; vos= vos.next) {
//		project_short(ar, vos.vec, vos.mval);
//		if(vos.mval[0]!=IS_CLIPPED)
//			tot++;
//	}
//
//	if(tot) {
//		RegionView3D *rv3d= ar.regiondata;
//		int a;
//
//		if(rv3d.rflag & RV3D_CLIPPING)
//			for(a=0; a<6; a++)
//				glDisable(GL_CLIP_PLANE0+a);
//
//		wmPushMatrix();
//		ED_region_pixelspace(ar);
//
//		if(v3d.zbuf) glDepthMask(0);
//
//		for(vos= pstrings.first; vos; vos= vos.next) {
//			if(vos.mval[0]!=IS_CLIPPED) {
//				glColor3fv(vos.col);
//				BLF_draw_default((float)vos.mval[0]+vos.xoffs, (float)vos.mval[1], 2.0, vos.str);
//			}
//		}
//
//		if(v3d.zbuf) glDepthMask(1);
//
//		wmPopMatrix();
//
//		if(rv3d.rflag & RV3D_CLIPPING)
//			for(a=0; a<6; a++)
//				glEnable(GL_CLIP_PLANE0+a);
//	}
//
//	if(pstrings.first)
//		BLI_freelistN(&pstrings);
//}
//typedef struct ParticleDrawData {
//	float *vdata, *vd;
//	float *ndata, *nd;
//	float *cdata, *cd;
//	float *vedata, *ved;
//	float *ma_r, *ma_g, *ma_b;
//} ParticleDrawData;
//static void draw_particle(ParticleKey *state, int draw_as, short draw, float pixsize, float imat[4][4], float *draw_line, ParticleBillboardData *bb, ParticleDrawData *pdd)
//{
//	float vec[3], vec2[3];
//	float *vd = pdd.vd;
//	float *cd = pdd.cd;
//	float ma_r=0.0f;
//	float ma_g=0.0f;
//	float ma_b=0.0f;
//
//	if(pdd.ma_r) {
//		ma_r = *pdd.ma_r;
//		ma_g = *pdd.ma_g;
//		ma_b = *pdd.ma_b;
//	}
//
//	switch(draw_as){
//		case PART_DRAW_DOT:
//		{
//			if(vd) {
//				VECCOPY(vd,state.co) pdd.vd+=3;
//			}
//			if(cd) {
//				cd[0]=ma_r;
//				cd[1]=ma_g;
//				cd[2]=ma_b;
//				pdd.cd+=3;
//			}
//			break;
//		}
//		case PART_DRAW_CROSS:
//		case PART_DRAW_AXIS:
//		{
//			vec[0]=2.0f*pixsize;
//			vec[1]=vec[2]=0.0;
//			QuatMulVecf(state.rot,vec);
//			if(draw_as==PART_DRAW_AXIS) {
//				cd[1]=cd[2]=cd[4]=cd[5]=0.0;
//				cd[0]=cd[3]=1.0;
//				cd[6]=cd[8]=cd[9]=cd[11]=0.0;
//				cd[7]=cd[10]=1.0;
//				cd[13]=cd[12]=cd[15]=cd[16]=0.0;
//				cd[14]=cd[17]=1.0;
//				cd+=18;
//
//				VECCOPY(vec2,state.co);
//			}
//			else {
//				if(cd) {
//					cd[0]=cd[3]=cd[6]=cd[9]=cd[12]=cd[15]=ma_r;
//					cd[1]=cd[4]=cd[7]=cd[10]=cd[13]=cd[16]=ma_g;
//					cd[2]=cd[5]=cd[8]=cd[11]=cd[14]=cd[17]=ma_b;
//					pdd.cd+=18;
//				}
//				VECSUB(vec2,state.co,vec);
//			}
//
//			VECADD(vec,state.co,vec);
//			VECCOPY(pdd.vd,vec); pdd.vd+=3;
//			VECCOPY(pdd.vd,vec2); pdd.vd+=3;
//
//			vec[1]=2.0f*pixsize;
//			vec[0]=vec[2]=0.0;
//			QuatMulVecf(state.rot,vec);
//			if(draw_as==PART_DRAW_AXIS){
//				VECCOPY(vec2,state.co);
//			}
//			else VECSUB(vec2,state.co,vec);
//
//			VECADD(vec,state.co,vec);
//			VECCOPY(pdd.vd,vec); pdd.vd+=3;
//			VECCOPY(pdd.vd,vec2); pdd.vd+=3;
//
//			vec[2]=2.0f*pixsize;
//			vec[0]=vec[1]=0.0;
//			QuatMulVecf(state.rot,vec);
//			if(draw_as==PART_DRAW_AXIS){
//				VECCOPY(vec2,state.co);
//			}
//			else VECSUB(vec2,state.co,vec);
//
//			VECADD(vec,state.co,vec);
//
//			VECCOPY(pdd.vd,vec); pdd.vd+=3;
//			VECCOPY(pdd.vd,vec2); pdd.vd+=3;
//			break;
//		}
//		case PART_DRAW_LINE:
//		{
//			VECCOPY(vec,state.vel);
//			Normalize(vec);
//			if(draw & PART_DRAW_VEL_LENGTH)
//				VecMulf(vec,VecLength(state.vel));
//			VECADDFAC(pdd.vd,state.co,vec,-draw_line[0]); pdd.vd+=3;
//			VECADDFAC(pdd.vd,state.co,vec,draw_line[1]); pdd.vd+=3;
//			if(cd) {
//				cd[0]=cd[3]=ma_r;
//				cd[1]=cd[4]=ma_g;
//				cd[2]=cd[5]=ma_b;
//				pdd.cd+=6;
//			}
//			break;
//		}
//		case PART_DRAW_CIRC:
//		{
//			if(pdd.ma_r)
//				glColor3f(ma_r,ma_g,ma_b);
//			drawcircball(GL_LINE_LOOP, state.co, pixsize, imat);
//			break;
//		}
//		case PART_DRAW_BB:
//		{
//			float xvec[3], yvec[3], zvec[3], bb_center[3];
//			if(cd) {
//				cd[0]=cd[3]=cd[6]=cd[9]=ma_r;
//				cd[1]=cd[4]=cd[7]=cd[10]=ma_g;
//				cd[2]=cd[5]=cd[8]=cd[11]=ma_b;
//				pdd.cd+=12;
//			}
//
//
//			VECCOPY(bb.vec, state.co);
//			VECCOPY(bb.vel, state.vel);
//
//			psys_make_billboard(bb, xvec, yvec, zvec, bb_center);
//
//			VECADD(pdd.vd,bb_center,xvec);
//			VECADD(pdd.vd,pdd.vd,yvec); pdd.vd+=3;
//
//			VECSUB(pdd.vd,bb_center,xvec);
//			VECADD(pdd.vd,pdd.vd,yvec); pdd.vd+=3;
//
//			VECSUB(pdd.vd,bb_center,xvec);
//			VECSUB(pdd.vd,pdd.vd,yvec); pdd.vd+=3;
//
//			VECADD(pdd.vd,bb_center,xvec);
//			VECSUB(pdd.vd,pdd.vd,yvec); pdd.vd+=3;
//
//			VECCOPY(pdd.nd, zvec); pdd.nd+=3;
//			VECCOPY(pdd.nd, zvec); pdd.nd+=3;
//			VECCOPY(pdd.nd, zvec); pdd.nd+=3;
//			VECCOPY(pdd.nd, zvec); pdd.nd+=3;
//			break;
//		}
//	}
//}
///* unified drawing of all new particle systems draw types except dupli ob & group	*/
///* mostly tries to use vertex arrays for speed										*/
//
///* 1. check that everything is ok & updated */
///* 2. start initialising things				*/
///* 3. initialize according to draw type		*/
///* 4. allocate drawing data arrays			*/
///* 5. start filling the arrays				*/
///* 6. draw the arrays						*/
///* 7. clean up								*/
//static void draw_new_particle_system(Scene *scene, View3D *v3d, RegionView3D *rv3d, Base *base, ParticleSystem *psys, int ob_dt)
//{
//	Object *ob=base.object;
//	ParticleSystemModifierData *psmd;
//	ParticleSettings *part;
//	ParticleData *pars, *pa;
//	ParticleKey state, *states=0;
//	ParticleBillboardData bb;
//	ParticleDrawData pdd;
//	Material *ma;
//	float vel[3], imat[4][4];
//	float timestep, pixsize=1.0, pa_size, r_tilt, r_length;
//	float pa_time, pa_birthtime, pa_dietime, pa_health;
//	float cfra= bsystem_time(scene, ob,(float)CFRA,0.0);
//	float ma_r=0.0f, ma_g=0.0f, ma_b=0.0f;
//	int a, totpart, totpoint=0, totve=0, drawn, draw_as, totchild=0;
//	int select=ob.flag&SELECT, create_cdata=0, need_v=0;
//	GLint polygonmode[2];
//	char val[32];
//
///* 1. */
//	if(psys==0)
//		return;
//
//	part=psys.part;
//	pars=psys.particles;
//
//	if(part==0 || !psys_check_enabled(ob, psys))
//		return;
//
//	if(pars==0) return;
//
//	// XXX what logic is this?
//	if(!scene.obedit && psys_in_edit_mode(scene, psys)
//		&& psys.flag & PSYS_HAIR_DONE && part.draw_as==PART_DRAW_PATH)
//		return;
//
//	if(part.draw_as==PART_DRAW_NOT) return;
//
///* 2. */
//	if(part.phystype==PART_PHYS_KEYED){
//		if(psys.flag&PSYS_KEYED){
//			psys_count_keyed_targets(ob,psys);
//			if(psys.totkeyed==0)
//				return;
//		}
//	}
//
//	if(select){
//		select=0;
//		if(psys_get_current(ob)==psys)
//			select=1;
//	}
//
//	psys.flag|=PSYS_DRAWING;
//
//	if(part.type==PART_HAIR && !psys.childcache)
//		totchild=0;
//	else
//		totchild=psys.totchild*part.disp/100;
//
//	memset(&pdd, 0, sizeof(ParticleDrawData));
//
//	ma= give_current_material(ob,part.omat);
//
//	if(v3d.zbuf) glDepthMask(1);
//
//	if(select)
//		cpack(0xFFFFFF);
//	else if((ma) && (part.draw&PART_DRAW_MAT_COL)) {
//		glColor3f(ma.r,ma.g,ma.b);
//
//		ma_r = ma.r;
//		ma_g = ma.g;
//		ma_b = ma.b;
//
//		pdd.ma_r = &ma_r;
//		pdd.ma_g = &ma_g;
//		pdd.ma_b = &ma_b;
//
//		create_cdata = 1;
//	}
//	else
//		cpack(0);
//
//	psmd= psys_get_modifier(ob,psys);
//
//	timestep= psys_get_timestep(part);
//
//	if( (base.flag & OB_FROMDUPLI) && (ob.flag & OB_FROMGROUP) ) {
//		float mat[4][4];
//		Mat4MulMat4(mat, psys.imat, ob.obmat);
//		wmMultMatrix(mat);
//	}
//
//	totpart=psys.totpart;
//
//	if(part.draw_as==PART_DRAW_REND)
//		draw_as = part.ren_as;
//	else
//		draw_as = part.draw_as;
//
//	//if(part.flag&PART_GLOB_TIME)
//	cfra=bsystem_time(scene, 0, (float)CFRA, 0.0f);
//
//	if(draw_as==PART_DRAW_PATH && psys.pathcache==NULL)
//		draw_as=PART_DRAW_DOT;
//
///* 3. */
//	switch(draw_as){
//		case PART_DRAW_DOT:
//			if(part.draw_size)
//				glPointSize(part.draw_size);
//			else
//				glPointSize(2.0); /* default dot size */
//			break;
//		case PART_DRAW_CIRC:
//			/* calculate view aligned matrix: */
//			Mat4CpyMat4(imat, rv3d.viewinv);
//			Normalize(imat[0]);
//			Normalize(imat[1]);
//			/* no break! */
//		case PART_DRAW_CROSS:
//		case PART_DRAW_AXIS:
//			/* lets calculate the scale: */
//			pixsize= rv3d.persmat[0][3]*ob.obmat[3][0]+ rv3d.persmat[1][3]*ob.obmat[3][1]+ rv3d.persmat[2][3]*ob.obmat[3][2]+ rv3d.persmat[3][3];
//			pixsize*= rv3d.pixsize;
//			if(part.draw_size==0.0)
//				pixsize*=2.0;
//			else
//				pixsize*=part.draw_size;
//			break;
//		case PART_DRAW_OB:
//			if(part.dup_ob==0)
//				draw_as=PART_DRAW_DOT;
//			else
//				draw_as=0;
//			break;
//		case PART_DRAW_GR:
//			if(part.dup_group==0)
//				draw_as=PART_DRAW_DOT;
//			else
//				draw_as=0;
//			break;
//		case PART_DRAW_BB:
//			if(v3d.camera==0 && part.bb_ob==0){
//				printf("Billboards need an active camera or a target object!\n");
//
//				draw_as=part.draw_as=PART_DRAW_DOT;
//
//				if(part.draw_size)
//					glPointSize(part.draw_size);
//				else
//					glPointSize(2.0); /* default dot size */
//			}
//			else if(part.bb_ob)
//				bb.ob=part.bb_ob;
//			else
//				bb.ob=v3d.camera;
//
//			bb.align = part.bb_align;
//			bb.anim = part.bb_anim;
//			bb.lock = part.draw & PART_DRAW_BB_LOCK;
//			bb.offset[0] = part.bb_offset[0];
//			bb.offset[1] = part.bb_offset[1];
//			break;
//		case PART_DRAW_PATH:
//			break;
//		case PART_DRAW_LINE:
//			need_v=1;
//			break;
//	}
//	if(part.draw & PART_DRAW_SIZE && part.draw_as!=PART_DRAW_CIRC){
//		Mat4CpyMat4(imat, rv3d.viewinv);
//		Normalize(imat[0]);
//		Normalize(imat[1]);
//	}
//
///* 4. */
//	if(draw_as && draw_as!=PART_DRAW_PATH) {
//		int tot_vec_size = (totpart + totchild) * 3 * sizeof(float);
//
//		if(part.draw_as == PART_DRAW_REND && part.trail_count > 1) {
//			tot_vec_size *= part.trail_count;
//			psys_make_temp_pointcache(ob, psys);
//		}
//
//		if(draw_as!=PART_DRAW_CIRC) {
//			switch(draw_as) {
//				case PART_DRAW_AXIS:
//				case PART_DRAW_CROSS:
//					if(draw_as != PART_DRAW_CROSS || create_cdata)
//						pdd.cdata = MEM_callocN(tot_vec_size * 6, "particle_cdata");
//					pdd.vdata = MEM_callocN(tot_vec_size * 6, "particle_vdata");
//					break;
//				case PART_DRAW_LINE:
//					if(create_cdata)
//						pdd.cdata = MEM_callocN(tot_vec_size * 2, "particle_cdata");
//					pdd.vdata = MEM_callocN(tot_vec_size * 2, "particle_vdata");
//					break;
//				case PART_DRAW_BB:
//					if(create_cdata)
//						pdd.cdata = MEM_callocN(tot_vec_size * 4, "particle_cdata");
//					pdd.vdata = MEM_callocN(tot_vec_size * 4, "particle_vdata");
//					pdd.ndata = MEM_callocN(tot_vec_size * 4, "particle_vdata");
//					break;
//				default:
//					if(create_cdata)
//						pdd.cdata=MEM_callocN(tot_vec_size, "particle_cdata");
//					pdd.vdata=MEM_callocN(tot_vec_size, "particle_vdata");
//			}
//		}
//
//		if(part.draw & PART_DRAW_VEL && draw_as != PART_DRAW_LINE) {
//			pdd.vedata = MEM_callocN(tot_vec_size * 2, "particle_vedata");
//			need_v = 1;
//		}
//
//		pdd.vd= pdd.vdata;
//		pdd.ved= pdd.vedata;
//		pdd.cd= pdd.cdata;
//		pdd.nd= pdd.ndata;
//
//		psys.lattice= psys_get_lattice(scene, ob, psys);
//	}
//
//	if(draw_as){
///* 5. */
//		for(a=0,pa=pars; a<totpart+totchild; a++, pa++){
//			/* setup per particle individual stuff */
//			if(a<totpart){
//				if(totchild && (part.draw&PART_DRAW_PARENT)==0) continue;
//				if(pa.flag & PARS_NO_DISP || pa.flag & PARS_UNEXIST) continue;
//
//				pa_time=(cfra-pa.time)/pa.lifetime;
//				pa_birthtime=pa.time;
//				pa_dietime = pa.dietime;
//				pa_size=pa.size;
//				if(part.phystype==PART_PHYS_BOIDS)
//					pa_health = pa.boid.health;
//				else
//					pa_health = -1.0;
//
//#if 0 // XXX old animation system
//				if((part.flag&PART_ABS_TIME)==0){
//					if(ma && ma.ipo){
//						IpoCurve *icu;
//
//						/* correction for lifetime */
//						calc_ipo(ma.ipo, 100.0f*pa_time);
//
//						for(icu = ma.ipo.curve.first; icu; icu=icu.next) {
//							if(icu.adrcode == MA_COL_R)
//								ma_r = icu.curval;
//							else if(icu.adrcode == MA_COL_G)
//								ma_g = icu.curval;
//							else if(icu.adrcode == MA_COL_B)
//								ma_b = icu.curval;
//						}
//					}
//					if(part.ipo) {
//						IpoCurve *icu;
//
//						/* correction for lifetime */
//						calc_ipo(part.ipo, 100*pa_time);
//
//						for(icu = part.ipo.curve.first; icu; icu=icu.next) {
//							if(icu.adrcode == PART_SIZE)
//								pa_size = icu.curval;
//						}
//					}
//				}
//#endif // XXX old animation system
//
//				r_tilt = 1.0f + pa.r_ave[0];
//				r_length = 0.5f * (1.0f + pa.r_ave[1]);
//			}
//			else{
//				ChildParticle *cpa= &psys.child[a-totpart];
//
//				pa_time=psys_get_child_time(psys,cpa,cfra,&pa_birthtime,&pa_dietime);
//
//#if 0 // XXX old animation system
//				if((part.flag&PART_ABS_TIME)==0) {
//					if(ma && ma.ipo){
//						IpoCurve *icu;
//
//						/* correction for lifetime */
//						calc_ipo(ma.ipo, 100.0f*pa_time);
//
//						for(icu = ma.ipo.curve.first; icu; icu=icu.next) {
//							if(icu.adrcode == MA_COL_R)
//								ma_r = icu.curval;
//							else if(icu.adrcode == MA_COL_G)
//								ma_g = icu.curval;
//							else if(icu.adrcode == MA_COL_B)
//								ma_b = icu.curval;
//						}
//					}
//				}
//#endif // XXX old animation system
//
//				pa_size=psys_get_child_size(psys,cpa,cfra,0);
//
//				pa_health = -1.0;
//
//				r_tilt = 2.0f * cpa.rand[2];
//				r_length = cpa.rand[1];
//			}
//
//			if(draw_as!=PART_DRAW_PATH){
//				drawn = 0;
//				if(part.draw_as == PART_DRAW_REND && part.trail_count > 1) {
//					float length = part.path_end * (1.0 - part.randlength * r_length);
//					int trail_count = part.trail_count * (1.0 - part.randlength * r_length);
//					float ct = ((part.draw & PART_ABS_PATH_TIME) ? cfra : pa_time) - length;
//					float dt = length / (trail_count ? (float)trail_count : 1.0f);
//					int i=0;
//
//					ct+=dt;
//					for(i=0; i < trail_count; i++, ct += dt) {
//						if(part.draw & PART_ABS_PATH_TIME) {
//							if(ct < pa_birthtime || ct > pa_dietime)
//								continue;
//						}
//						else if(ct < 0.0f || ct > 1.0f)
//							continue;
//
//						state.time = (part.draw & PART_ABS_PATH_TIME) ? -ct : -(pa_birthtime + ct * (pa_dietime - pa_birthtime));
//						psys_get_particle_on_path(scene,ob,psys,a,&state,need_v);
//
//						if(psys.parent)
//							Mat4MulVecfl(psys.parent.obmat, state.co);
//
//						/* create actiual particle data */
//						if(draw_as == PART_DRAW_BB) {
//							bb.size = pa_size;
//							bb.tilt = part.bb_tilt * (1.0f - part.bb_rand_tilt * r_tilt);
//							bb.time = ct;
//						}
//
//						draw_particle(&state, draw_as, part.draw, pixsize, imat, part.draw_line, &bb, &pdd);
//
//						totpoint++;
//						drawn = 1;
//					}
//				}
//				else
//				{
//					state.time=cfra;
//					if(psys_get_particle_state(scene,ob,psys,a,&state,0)){
//						if(psys.parent)
//							Mat4MulVecfl(psys.parent.obmat, state.co);
//
//						/* create actiual particle data */
//						if(draw_as == PART_DRAW_BB) {
//							bb.size = pa_size;
//							bb.tilt = part.bb_tilt * (1.0f - part.bb_rand_tilt * r_tilt);
//							bb.time = pa_time;
//						}
//
//						draw_particle(&state, draw_as, part.draw, pixsize, imat, part.draw_line, &bb, &pdd);
//
//						totpoint++;
//						drawn = 1;
//					}
//				}
//
//				if(drawn) {
//					/* additional things to draw for each particle	*/
//					/* (velocity, size and number)					*/
//					if(pdd.vedata){
//						VECCOPY(pdd.ved,state.co);
//						pdd.ved+=3;
//						VECCOPY(vel,state.vel);
//						VecMulf(vel,timestep);
//						VECADD(pdd.ved,state.co,vel);
//						pdd.ved+=3;
//
//						totve++;
//					}
//
//					if(part.draw & PART_DRAW_SIZE){
//						setlinestyle(3);
//						drawcircball(GL_LINE_LOOP, state.co, pa_size, imat);
//						setlinestyle(0);
//					}
//
//					if((part.draw&PART_DRAW_NUM || part.draw&PART_DRAW_HEALTH) && !(G.f & G_RENDER_SHADOW)){
//						strcpy(val, "");
//
//						if(part.draw&PART_DRAW_NUM)
//							sprintf(val, " %i", a);
//
//						if(part.draw&PART_DRAW_NUM && part.draw&PART_DRAW_HEALTH)
//							sprintf(val, "%s:", val);
//
//						if(part.draw&PART_DRAW_HEALTH && a < totpart && part.phystype==PART_PHYS_BOIDS)
//							sprintf(val, "%s %.2f", val, pa_health);
//
//						/* in path drawing state.co is the end point */
//						view3d_particle_text_draw_add(state.co[0],  state.co[1],  state.co[2], val, 0);
//					}
//				}
//			}
//		}
///* 6. */
//
//		glGetIntegerv(GL_POLYGON_MODE, polygonmode);
//		glDisableClientState(GL_NORMAL_ARRAY);
//
//		if(draw_as==PART_DRAW_PATH){
//			ParticleCacheKey **cache, *path;
//			float *cd2=0,*cdata2=0;
//
//			glEnableClientState(GL_VERTEX_ARRAY);
//
//			/* setup gl flags */
//			if(ob_dt > OB_WIRE) {
//				glEnableClientState(GL_NORMAL_ARRAY);
//
//				if(part.draw&PART_DRAW_MAT_COL)
//					glEnableClientState(GL_COLOR_ARRAY);
//
//				glEnable(GL_LIGHTING);
//				glColorMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE);
//				glEnable(GL_COLOR_MATERIAL);
//			}
//			else {
//				glDisableClientState(GL_NORMAL_ARRAY);
//
//				glDisable(GL_COLOR_MATERIAL);
//				glDisable(GL_LIGHTING);
//				UI_ThemeColor(TH_WIRE);
//			}
//
//			if(totchild && (part.draw&PART_DRAW_PARENT)==0)
//				totpart=0;
//
//			/* draw actual/parent particles */
//			cache=psys.pathcache;
//			for(a=0, pa=psys.particles; a<totpart; a++, pa++){
//				path=cache[a];
//				if(path.steps > 0) {
//					glVertexPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), path.co);
//
//					if(ob_dt > OB_WIRE) {
//						glNormalPointer(GL_FLOAT, sizeof(ParticleCacheKey), path.vel);
//						if(part.draw&PART_DRAW_MAT_COL)
//							glColorPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), path.col);
//					}
//
//					glDrawArrays(GL_LINE_STRIP, 0, path.steps + 1);
//				}
//			}
//
//			/* draw child particles */
//			cache=psys.childcache;
//			for(a=0; a<totchild; a++){
//				path=cache[a];
//				glVertexPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), path.co);
//
//				if(ob_dt > OB_WIRE) {
//					glNormalPointer(GL_FLOAT, sizeof(ParticleCacheKey), path.vel);
//					if(part.draw&PART_DRAW_MAT_COL)
//						glColorPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), path.col);
//				}
//
//				glDrawArrays(GL_LINE_STRIP, 0, path.steps + 1);
//			}
//
//
//			/* restore & clean up */
//			if(ob_dt > OB_WIRE) {
//				if(part.draw&PART_DRAW_MAT_COL)
//					glDisable(GL_COLOR_ARRAY);
//				glDisable(GL_COLOR_MATERIAL);
//			}
//
//			if(cdata2)
//				MEM_freeN(cdata2);
//			cd2=cdata2=0;
//
//			glLineWidth(1.0f);
//		}
//		else if(draw_as!=PART_DRAW_CIRC){
//			glDisableClientState(GL_COLOR_ARRAY);
//
//			/* setup created data arrays */
//			if(pdd.vdata){
//				glEnableClientState(GL_VERTEX_ARRAY);
//				glVertexPointer(3, GL_FLOAT, 0, pdd.vdata);
//			}
//			else
//				glDisableClientState(GL_VERTEX_ARRAY);
//
//			/* billboards are drawn this way */
//			if(pdd.ndata && ob_dt>OB_WIRE){
//				glEnableClientState(GL_NORMAL_ARRAY);
//				glNormalPointer(GL_FLOAT, 0, pdd.ndata);
//				glEnable(GL_LIGHTING);
//			}
//			else{
//				glDisableClientState(GL_NORMAL_ARRAY);
//				glDisable(GL_LIGHTING);
//			}
//
//			if(pdd.cdata){
//				glEnableClientState(GL_COLOR_ARRAY);
//				glColorPointer(3, GL_FLOAT, 0, pdd.cdata);
//			}
//
//			/* draw created data arrays */
//			switch(draw_as){
//				case PART_DRAW_AXIS:
//				case PART_DRAW_CROSS:
//					glDrawArrays(GL_LINES, 0, 6*totpoint);
//					break;
//				case PART_DRAW_LINE:
//					glDrawArrays(GL_LINES, 0, 2*totpoint);
//					break;
//				case PART_DRAW_BB:
//					if(ob_dt<=OB_WIRE)
//						glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
//
//					glDrawArrays(GL_QUADS, 0, 4*totpoint);
//					break;
//				default:
//					glDrawArrays(GL_POINTS, 0, totpoint);
//					break;
//			}
//		}
//
//		if(pdd.vedata){
//			glDisableClientState(GL_COLOR_ARRAY);
//			cpack(0xC0C0C0);
//
//			glEnableClientState(GL_VERTEX_ARRAY);
//			glVertexPointer(3, GL_FLOAT, 0, pdd.vedata);
//
//			glDrawArrays(GL_LINES, 0, 2*totve);
//		}
//
//		glPolygonMode(GL_FRONT, polygonmode[0]);
//		glPolygonMode(GL_BACK, polygonmode[1]);
//	}
//
///* 7. */
//
//	glDisable(GL_LIGHTING);
//	glDisableClientState(GL_COLOR_ARRAY);
//	glEnableClientState(GL_NORMAL_ARRAY);
//
//	if(states)
//		MEM_freeN(states);
//	if(pdd.vdata)
//		MEM_freeN(pdd.vdata);
//	if(pdd.vedata)
//		MEM_freeN(pdd.vedata);
//	if(pdd.cdata)
//		MEM_freeN(pdd.cdata);
//	if(pdd.ndata)
//		MEM_freeN(pdd.ndata);
//
//	psys.flag &= ~PSYS_DRAWING;
//
//	if(psys.lattice){
//		end_latt_deform(psys.lattice);
//		psys.lattice= NULL;
//	}
//
//	if( (base.flag & OB_FROMDUPLI) && (ob.flag & OB_FROMGROUP) )
//		wmLoadMatrix(rv3d.viewmat);
//}
//
//static void draw_particle_edit(Scene *scene, View3D *v3d, RegionView3D *rv3d, Object *ob, ParticleSystem *psys, int dt)
//{
//	ParticleEdit *edit = psys.edit;
//	ParticleData *pa;
//	ParticleCacheKey **path;
//	ParticleEditKey *key;
//	ParticleEditSettings *pset = PE_settings(scene);
//	int i, k, totpart = psys.totpart, totchild=0, timed = pset.draw_timed;
//	char nosel[4], sel[4];
//	float sel_col[3];
//	float nosel_col[3];
//	char val[32];
//
//	/* create path and child path cache if it doesn't exist already */
//	if(psys.pathcache==0){
//		PE_hide_keys_time(scene, psys,CFRA);
//		psys_cache_paths(scene, ob, psys, CFRA,0);
//	}
//	if(psys.pathcache==0)
//		return;
//
//	if(pset.flag & PE_SHOW_CHILD && psys.part.draw_as == PART_DRAW_PATH) {
//		if(psys.childcache==0)
//			psys_cache_child_paths(scene, ob, psys, CFRA, 0);
//	}
//	else if(!(pset.flag & PE_SHOW_CHILD) && psys.childcache)
//		free_child_path_cache(psys);
//
//	/* opengl setup */
//	if((v3d.flag & V3D_ZBUF_SELECT)==0)
//		glDisable(GL_DEPTH_TEST);
//
//	/* get selection theme colors */
//	UI_GetThemeColor3ubv(TH_VERTEX_SELECT, sel);
//	UI_GetThemeColor3ubv(TH_VERTEX, nosel);
//	sel_col[0]=(float)sel[0]/255.0f;
//	sel_col[1]=(float)sel[1]/255.0f;
//	sel_col[2]=(float)sel[2]/255.0f;
//	nosel_col[0]=(float)nosel[0]/255.0f;
//	nosel_col[1]=(float)nosel[1]/255.0f;
//	nosel_col[2]=(float)nosel[2]/255.0f;
//
//	if(psys.childcache)
//		totchild = psys.totchildcache;
//
//	/* draw paths */
//	if(timed)
//		glEnable(GL_BLEND);
//
//	glEnableClientState(GL_VERTEX_ARRAY);
//
//	if(dt > OB_WIRE) {
//		/* solid shaded with lighting */
//		glEnableClientState(GL_NORMAL_ARRAY);
//		glEnableClientState(GL_COLOR_ARRAY);
//
//		glEnable(GL_COLOR_MATERIAL);
//		glColorMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE);
//	}
//	else {
//		/* flat wire color */
//		glDisableClientState(GL_NORMAL_ARRAY);
//		glDisable(GL_LIGHTING);
//		UI_ThemeColor(TH_WIRE);
//	}
//
//	/* only draw child paths with lighting */
//	if(dt > OB_WIRE)
//		glEnable(GL_LIGHTING);
//
//	if(psys.part.draw_as == PART_DRAW_PATH) {
//		for(i=0, path=psys.childcache; i<totchild; i++,path++){
//			glVertexPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), (*path).co);
//			if(dt > OB_WIRE) {
//				glNormalPointer(GL_FLOAT, sizeof(ParticleCacheKey), (*path).vel);
//				glColorPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), (*path).col);
//			}
//
//			glDrawArrays(GL_LINE_STRIP, 0, (int)(*path).steps + 1);
//		}
//	}
//
//	if(dt > OB_WIRE)
//		glDisable(GL_LIGHTING);
//
//	if(pset.brushtype == PE_BRUSH_WEIGHT) {
//		glLineWidth(2.0f);
//		glEnableClientState(GL_COLOR_ARRAY);
//		glDisable(GL_LIGHTING);
//	}
//
//	/* draw parents last without lighting */
//	for(i=0, pa=psys.particles, path = psys.pathcache; i<totpart; i++, pa++, path++){
//		glVertexPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), (*path).co);
//		if(dt > OB_WIRE)
//			glNormalPointer(GL_FLOAT, sizeof(ParticleCacheKey), (*path).vel);
//		if(dt > OB_WIRE || pset.brushtype == PE_BRUSH_WEIGHT)
//			glColorPointer(3, GL_FLOAT, sizeof(ParticleCacheKey), (*path).col);
//
//		glDrawArrays(GL_LINE_STRIP, 0, (int)(*path).steps + 1);
//	}
//
//	/* draw edit vertices */
//	if(pset.selectmode!=SCE_SELECT_PATH){
//		glDisableClientState(GL_NORMAL_ARRAY);
//		glEnableClientState(GL_COLOR_ARRAY);
//		glDisable(GL_LIGHTING);
//		glPointSize(UI_GetThemeValuef(TH_VERTEX_SIZE));
//
//		if(pset.selectmode==SCE_SELECT_POINT){
//			float *cd=0,*cdata=0;
//			cd=cdata=MEM_callocN(edit.totkeys*(timed?4:3)*sizeof(float), "particle edit color data");
//
//			for(i=0, pa=psys.particles; i<totpart; i++, pa++){
//				for(k=0, key=edit.keys[i]; k<pa.totkey; k++, key++){
//					if(key.flag&PEK_SELECT){
//						VECCOPY(cd,sel_col);
//					}
//					else{
//						VECCOPY(cd,nosel_col);
//					}
//					if(timed)
//						*(cd+3) = (key.flag&PEK_HIDE)?0.0f:1.0f;
//					cd += (timed?4:3);
//				}
//			}
//			cd=cdata;
//			for(i=0, pa=psys.particles; i<totpart; i++, pa++){
//				if((pa.flag & PARS_HIDE)==0){
//					glVertexPointer(3, GL_FLOAT, sizeof(ParticleEditKey), edit.keys[i].world_co);
//					glColorPointer((timed?4:3), GL_FLOAT, (timed?4:3)*sizeof(float), cd);
//					glDrawArrays(GL_POINTS, 0, pa.totkey);
//				}
//				cd += (timed?4:3) * pa.totkey;
//
//				if((pset.flag&PE_SHOW_TIME) && (pa.flag&PARS_HIDE)==0 && !(G.f & G_RENDER_SHADOW)){
//					for(k=0, key=edit.keys[i]+k; k<pa.totkey; k++, key++){
//						if(key.flag & PEK_HIDE) continue;
//
//						sprintf(val," %.1f",*key.time);
//						view3d_particle_text_draw_add(key.world_co[0], key.world_co[1], key.world_co[2], val, 0);
//					}
//				}
//			}
//			if(cdata)
//				MEM_freeN(cdata);
//			cd=cdata=0;
//		}
//		else if(pset.selectmode == SCE_SELECT_END){
//			for(i=0, pa=psys.particles; i<totpart; i++, pa++){
//				if((pa.flag & PARS_HIDE)==0){
//					key = edit.keys[i] + pa.totkey - 1;
//					if(key.flag & PEK_SELECT)
//						glColor3fv(sel_col);
//					else
//						glColor3fv(nosel_col);
//					/* has to be like this.. otherwise selection won't work, have try glArrayElement later..*/
//					glBegin(GL_POINTS);
//					glVertex3fv(key.world_co);
//					glEnd();
//
//					if((pset.flag & PE_SHOW_TIME) && !(G.f & G_RENDER_SHADOW)){
//						sprintf(val," %.1f",*key.time);
//						view3d_particle_text_draw_add(key.world_co[0], key.world_co[1], key.world_co[2], val, 0);
//					}
//				}
//			}
//		}
//	}
//
//	glDisable(GL_BLEND);
//	glDisable(GL_LIGHTING);
//	glDisable(GL_COLOR_MATERIAL);
//	glDisableClientState(GL_COLOR_ARRAY);
//	glEnableClientState(GL_NORMAL_ARRAY);
//	glEnable(GL_DEPTH_TEST);
//	glLineWidth(1.0f);
//
//	glPointSize(1.0);
//}
//
//unsigned int nurbcol[8]= {
//	0, 0x9090, 0x409030, 0x603080, 0, 0x40fff0, 0x40c033, 0xA090F0 };
//
//static void tekenhandlesN(Nurb *nu, short sel)
//{
//	BezTriple *bezt;
//	float *fp;
//	unsigned int *col;
//	int a;
//
//	if(nu.hide || (G.f & G_HIDDENHANDLES)) return;
//
//	glBegin(GL_LINES);
//
//	if( (nu.type & 7)==CU_BEZIER) {
//		if(sel) col= nurbcol+4;
//		else col= nurbcol;
//
//		bezt= nu.bezt;
//		a= nu.pntsu;
//		while(a--) {
//			if(bezt.hide==0) {
//				if( (bezt.f2 & SELECT)==sel) {
//					fp= bezt.vec[0];
//
//					cpack(col[(int)bezt.h1]);
//					glVertex3fv(fp);
//					glVertex3fv(fp+3);
//
//					cpack(col[(int)bezt.h2]);
//					glVertex3fv(fp+3);
//					glVertex3fv(fp+6);
//				}
//				else if( (bezt.f1 & SELECT)==sel) {
//					fp= bezt.vec[0];
//
//					cpack(col[(int)bezt.h1]);
//					glVertex3fv(fp);
//					glVertex3fv(fp+3);
//				}
//				else if( (bezt.f3 & SELECT)==sel) {
//					fp= bezt.vec[1];
//
//					cpack(col[(int)bezt.h2]);
//					glVertex3fv(fp);
//					glVertex3fv(fp+3);
//				}
//			}
//			bezt++;
//		}
//	}
//	glEnd();
//}
//
//static void tekenvertsN(Nurb *nu, short sel)
//{
//	BezTriple *bezt;
//	BPoint *bp;
//	float size;
//	int a;
//
//	if(nu.hide) return;
//
//	if(sel) UI_ThemeColor(TH_VERTEX_SELECT);
//	else UI_ThemeColor(TH_VERTEX);
//
//	size= UI_GetThemeValuef(TH_VERTEX_SIZE);
//	glPointSize(size);
//
//	bglBegin(GL_POINTS);
//
//	if((nu.type & 7)==CU_BEZIER) {
//
//		bezt= nu.bezt;
//		a= nu.pntsu;
//		while(a--) {
//			if(bezt.hide==0) {
//				if (G.f & G_HIDDENHANDLES) {
//					if((bezt.f2 & SELECT)==sel) bglVertex3fv(bezt.vec[1]);
//				} else {
//					if((bezt.f1 & SELECT)==sel) bglVertex3fv(bezt.vec[0]);
//					if((bezt.f2 & SELECT)==sel) bglVertex3fv(bezt.vec[1]);
//					if((bezt.f3 & SELECT)==sel) bglVertex3fv(bezt.vec[2]);
//				}
//			}
//			bezt++;
//		}
//	}
//	else {
//		bp= nu.bp;
//		a= nu.pntsu*nu.pntsv;
//		while(a--) {
//			if(bp.hide==0) {
//				if((bp.f1 & SELECT)==sel) bglVertex3fv(bp.vec);
//			}
//			bp++;
//		}
//	}
//
//	bglEnd();
//	glPointSize(1.0);
//}
//
//static void draw_editnurb(Object *ob, Nurb *nurb, int sel)
//{
//	Nurb *nu;
//	BPoint *bp, *bp1;
//	int a, b, ofs;
//
//	nu= nurb;
//	while(nu) {
//		if(nu.hide==0) {
//			switch(nu.type & 7) {
//			case CU_POLY:
//				cpack(nurbcol[3]);
//				bp= nu.bp;
//				for(b=0; b<nu.pntsv; b++) {
//					if(nu.flagu & 1) glBegin(GL_LINE_LOOP);
//					else glBegin(GL_LINE_STRIP);
//
//					for(a=0; a<nu.pntsu; a++, bp++) {
//						glVertex3fv(bp.vec);
//					}
//
//					glEnd();
//				}
//				break;
//			case CU_NURBS:
//
//				bp= nu.bp;
//				for(b=0; b<nu.pntsv; b++) {
//					bp1= bp;
//					bp++;
//					for(a=nu.pntsu-1; a>0; a--, bp++) {
//						if(bp.hide==0 && bp1.hide==0) {
//							if(sel) {
//								if( (bp.f1 & SELECT) && ( bp1.f1 & SELECT ) ) {
//									cpack(nurbcol[5]);
//
//									glBegin(GL_LINE_STRIP);
//									glVertex3fv(bp.vec);
//									glVertex3fv(bp1.vec);
//									glEnd();
//								}
//							}
//							else {
//								if( (bp.f1 & SELECT) && ( bp1.f1 & SELECT) );
//								else {
//									cpack(nurbcol[1]);
//
//									glBegin(GL_LINE_STRIP);
//									glVertex3fv(bp.vec);
//									glVertex3fv(bp1.vec);
//									glEnd();
//								}
//							}
//						}
//						bp1= bp;
//					}
//				}
//				if(nu.pntsv > 1) {	/* surface */
//
//					ofs= nu.pntsu;
//					for(b=0; b<nu.pntsu; b++) {
//						bp1= nu.bp+b;
//						bp= bp1+ofs;
//						for(a=nu.pntsv-1; a>0; a--, bp+=ofs) {
//							if(bp.hide==0 && bp1.hide==0) {
//								if(sel) {
//									if( (bp.f1 & SELECT) && ( bp1.f1 & SELECT) ) {
//										cpack(nurbcol[7]);
//
//										glBegin(GL_LINE_STRIP);
//										glVertex3fv(bp.vec);
//										glVertex3fv(bp1.vec);
//										glEnd();
//									}
//								}
//								else {
//									if( (bp.f1 & SELECT) && ( bp1.f1 & SELECT) );
//									else {
//										cpack(nurbcol[3]);
//
//										glBegin(GL_LINE_STRIP);
//										glVertex3fv(bp.vec);
//										glVertex3fv(bp1.vec);
//										glEnd();
//									}
//								}
//							}
//							bp1= bp;
//						}
//					}
//
//				}
//				break;
//			}
//		}
//		nu= nu.next;
//	}
//}
//
//static void drawnurb(Scene *scene, View3D *v3d, RegionView3D *rv3d, Base *base, Nurb *nurb, int dt)
//{
//	ToolSettings *ts= scene.toolsettings;
//	Object *ob= base.object;
//	Curve *cu = ob.data;
//	Nurb *nu;
//	BevList *bl;
//
//// XXX	retopo_matrix_update(v3d);
//
//	/* DispList */
//	UI_ThemeColor(TH_WIRE);
//	drawDispList(scene, v3d, rv3d, base, dt);
//
//	if(v3d.zbuf) glDisable(GL_DEPTH_TEST);
//
//	/* first non-selected handles */
//	for(nu=nurb; nu; nu=nu.next) {
//		if((nu.type & 7)==CU_BEZIER) {
//			tekenhandlesN(nu, 0);
//		}
//	}
//	draw_editnurb(ob, nurb, 0);
//	draw_editnurb(ob, nurb, 1);
//	/* selected handles */
//	for(nu=nurb; nu; nu=nu.next) {
//		if((nu.type & 7)==1) tekenhandlesN(nu, 1);
//		tekenvertsN(nu, 0);
//	}
//
//	if(v3d.zbuf) glEnable(GL_DEPTH_TEST);
//
//	/*	direction vectors for 3d curve paths
//		when at its lowest, dont render normals */
//	if(cu.flag & CU_3D && ts.normalsize > 0.0015) {
//		UI_ThemeColor(TH_WIRE);
//		for (bl=cu.bev.first,nu=nurb; nu && bl; bl=bl.next,nu=nu.next) {
//			BevPoint *bevp= (BevPoint *)(bl+1);
//			int nr= bl.nr;
//			int skip= nu.resolu/16;
//
//			while (nr-.0) { /* accounts for empty bevel lists */
//				float fac= bevp.radius * ts.normalsize;
//				float ox,oy,oz; // Offset perpendicular to the curve
//				float dx,dy,dz; // Delta along the curve
//
//				ox = fac*bevp.mat[0][0];
//				oy = fac*bevp.mat[0][1];
//				oz = fac*bevp.mat[0][2];
//
//				dx = fac*bevp.mat[2][0];
//				dy = fac*bevp.mat[2][1];
//				dz = fac*bevp.mat[2][2];
//
//				glBegin(GL_LINE_STRIP);
//				glVertex3f(bevp.x - ox - dx, bevp.y - oy - dy, bevp.z - oz - dz);
//				glVertex3f(bevp.x, bevp.y, bevp.z);
//				glVertex3f(bevp.x + ox - dx, bevp.y + oy - dy, bevp.z + oz - dz);
//				glEnd();
//
//				bevp += skip+1;
//				nr -= skip;
//			}
//		}
//	}
//
//	if(v3d.zbuf) glDisable(GL_DEPTH_TEST);
//
//	for(nu=nurb; nu; nu=nu.next) {
//		tekenvertsN(nu, 1);
//	}
//
//	if(v3d.zbuf) glEnable(GL_DEPTH_TEST);
//}
//
///* draw a sphere for use as an empty drawtype */
//static void draw_empty_sphere (float size)
//{
//	static GLuint displist=0;
//
//	if (displist == 0) {
//		GLUquadricObj	*qobj;
//
//		displist= glGenLists(1);
//		glNewList(displist, GL_COMPILE_AND_EXECUTE);
//
//		glPushMatrix();
//
//		qobj	= gluNewQuadric();
//		gluQuadricDrawStyle(qobj, GLU_SILHOUETTE);
//		gluDisk(qobj, 0.0,  1, 16, 1);
//
//		glRotatef(90, 0, 1, 0);
//		gluDisk(qobj, 0.0,  1, 16, 1);
//
//		glRotatef(90, 1, 0, 0);
//		gluDisk(qobj, 0.0,  1, 16, 1);
//
//		gluDeleteQuadric(qobj);
//
//		glPopMatrix();
//		glEndList();
//	}
//
//	glScalef(size, size, size);
//		glCallList(displist);
//	glScalef(1/size, 1/size, 1/size);
//}
//
///* draw a cone for use as an empty drawtype */
//static void draw_empty_cone (float size)
//{
//	float cent=0;
//    float radius;
//	GLUquadricObj *qobj = gluNewQuadric();
//	gluQuadricDrawStyle(qobj, GLU_SILHOUETTE);
//
//
//	glPushMatrix();
//
//	radius = size;
//	glTranslatef(cent,cent, cent);
//	glScalef(radius, 2.0*size, radius);
//	glRotatef(-90., 1.0, 0.0, 0.0);
//	gluCylinder(qobj, 1.0, 0.0, 1.0, 8, 1);
//
//	glPopMatrix();
//
//	gluDeleteQuadric(qobj);
//}
//
///* draw points on curve speed handles */
//static void curve_draw_speed(Scene *scene, Object *ob)
//{
//#if 0 // XXX old animation system stuff
//	Curve *cu= ob.data;
//	IpoCurve *icu;
//	BezTriple *bezt;
//	float loc[4], dir[3];
//	int a;
//
//	if(cu.ipo==NULL)
//		return;
//
//	icu= cu.ipo.curve.first;
//	if(icu==NULL || icu.totvert<2)
//		return;
//
//	glPointSize( UI_GetThemeValuef(TH_VERTEX_SIZE) );
//	bglBegin(GL_POINTS);
//
//	for(a=0, bezt= icu.bezt; a<icu.totvert; a++, bezt++) {
//		if( where_on_path(ob, bezt.vec[1][1], loc, dir)) {
//			UI_ThemeColor((bezt.f2 & SELECT) && ob==OBACT?TH_VERTEX_SELECT:TH_VERTEX);
//			bglVertex3fv(loc);
//		}
//	}
//
//	glPointSize(1.0);
//	bglEnd();
//#endif // XXX old animation system stuff
//}
//
//
//static void draw_textcurs(float textcurs[][2])
//{
//	cpack(0);
//
//	set_inverted_drawing(1);
//	glBegin(GL_QUADS);
//	glVertex2fv(textcurs[0]);
//	glVertex2fv(textcurs[1]);
//	glVertex2fv(textcurs[2]);
//	glVertex2fv(textcurs[3]);
//	glEnd();
//	set_inverted_drawing(0);
//}
//
//static void drawspiral(float *cent, float rad, float tmat[][4], int start)
//{
//	float vec[3], vx[3], vy[3];
//	int a, tot=32;
//	char inverse=0;
//
//	if (start < 0) {
//		inverse = 1;
//		start *= -1;
//	}
//
//	VECCOPY(vx, tmat[0]);
//	VECCOPY(vy, tmat[1]);
//	VecMulf(vx, rad);
//	VecMulf(vy, rad);
//
//	VECCOPY(vec, cent);
//
//	if (inverse==0) {
//		for(a=0; a<tot; a++) {
//			if (a+start>31)
//				start=-a + 1;
//			glBegin(GL_LINES);
//			glVertex3fv(vec);
//			vec[0]= cent[0] + *(sinval+a+start) * (vx[0] * (float)a/(float)tot) + *(cosval+a+start) * (vy[0] * (float)a/(float)tot);
//			vec[1]= cent[1] + *(sinval+a+start) * (vx[1] * (float)a/(float)tot) + *(cosval+a+start) * (vy[1] * (float)a/(float)tot);
//			vec[2]= cent[2] + *(sinval+a+start) * (vx[2] * (float)a/(float)tot) + *(cosval+a+start) * (vy[2] * (float)a/(float)tot);
//			glVertex3fv(vec);
//			glEnd();
//		}
//	}
//	else {
//		a=0;
//		vec[0]= cent[0] + *(sinval+a+start) * (vx[0] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[0] * (float)(-a+31)/(float)tot);
//		vec[1]= cent[1] + *(sinval+a+start) * (vx[1] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[1] * (float)(-a+31)/(float)tot);
//		vec[2]= cent[2] + *(sinval+a+start) * (vx[2] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[2] * (float)(-a+31)/(float)tot);
//		for(a=0; a<tot; a++) {
//			if (a+start>31)
//				start=-a + 1;
//			glBegin(GL_LINES);
//			glVertex3fv(vec);
//			vec[0]= cent[0] + *(sinval+a+start) * (vx[0] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[0] * (float)(-a+31)/(float)tot);
//			vec[1]= cent[1] + *(sinval+a+start) * (vx[1] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[1] * (float)(-a+31)/(float)tot);
//			vec[2]= cent[2] + *(sinval+a+start) * (vx[2] * (float)(-a+31)/(float)tot) + *(cosval+a+start) * (vy[2] * (float)(-a+31)/(float)tot);
//			glVertex3fv(vec);
//			glEnd();
//		}
//	}
//}
//
///* draws a circle on x-z plane given the scaling of the circle, assuming that
// * all required matrices have been set (used for drawing empties)
// */
//static void drawcircle_size(float size)
//{
//    float x, y;
//	short degrees;
//
//	glBegin(GL_LINE_LOOP);
//
//	/* coordinates are: cos(degrees*11.25)=x, sin(degrees*11.25)=y, 0.0f=z */
//	for (degrees=0; degrees<32; degrees++) {
//		x= *(cosval + degrees);
//		y= *(sinval + degrees);
//
//		glVertex3f(x*size, 0.0f, y*size);
//	}
//
//	glEnd();
//
//}
//
///* needs fixing if non-identity matrice used */
//static void drawtube(float *vec, float radius, float height, float tmat[][4])
//{
//	float cur[3];
//	drawcircball(GL_LINE_LOOP, vec, radius, tmat);
//
//	VecCopyf(cur,vec);
//	cur[2]+=height;
//
//	drawcircball(GL_LINE_LOOP, cur, radius, tmat);
//
//	glBegin(GL_LINES);
//		glVertex3f(vec[0]+radius,vec[1],vec[2]);
//		glVertex3f(cur[0]+radius,cur[1],cur[2]);
//		glVertex3f(vec[0]-radius,vec[1],vec[2]);
//		glVertex3f(cur[0]-radius,cur[1],cur[2]);
//		glVertex3f(vec[0],vec[1]+radius,vec[2]);
//		glVertex3f(cur[0],cur[1]+radius,cur[2]);
//		glVertex3f(vec[0],vec[1]-radius,vec[2]);
//		glVertex3f(cur[0],cur[1]-radius,cur[2]);
//	glEnd();
//}
///* needs fixing if non-identity matrice used */
//static void drawcone(float *vec, float radius, float height, float tmat[][4])
//{
//	float cur[3];
//
//	VecCopyf(cur,vec);
//	cur[2]+=height;
//
//	drawcircball(GL_LINE_LOOP, cur, radius, tmat);
//
//	glBegin(GL_LINES);
//		glVertex3f(vec[0],vec[1],vec[2]);
//		glVertex3f(cur[0]+radius,cur[1],cur[2]);
//		glVertex3f(vec[0],vec[1],vec[2]);
//		glVertex3f(cur[0]-radius,cur[1],cur[2]);
//		glVertex3f(vec[0],vec[1],vec[2]);
//		glVertex3f(cur[0],cur[1]+radius,cur[2]);
//		glVertex3f(vec[0],vec[1],vec[2]);
//		glVertex3f(cur[0],cur[1]-radius,cur[2]);
//	glEnd();
//}
///* return 1 if nothing was drawn */
//static int drawmball(Scene *scene, View3D *v3d, RegionView3D *rv3d, Base *base, int dt)
//{
//	Object *ob= base.object;
//	MetaBall *mb;
//	MetaElem *ml;
//	float imat[4][4], tmat[4][4];
//	int code= 1;
//
//	mb= ob.data;
//
//	if(mb.editelems) {
//		UI_ThemeColor(TH_WIRE);
//		if((G.f & G_PICKSEL)==0 ) drawDispList(scene, v3d, rv3d, base, dt);
//		ml= mb.editelems.first;
//	}
//	else {
//		if((base.flag & OB_FROMDUPLI)==0)
//			drawDispList(scene, v3d, rv3d, base, dt);
//		ml= mb.elems.first;
//	}
//
//	if(ml==NULL) return 1;
//
//	/* in case solid draw, reset wire colors */
//	if(mb.editelems && (ob.flag & SELECT)) {
//		if(ob==OBACT) UI_ThemeColor(TH_ACTIVE);
//		else UI_ThemeColor(TH_SELECT);
//	}
//	else UI_ThemeColor(TH_WIRE);
//
//	wmGetMatrix(tmat);
//	Mat4Invert(imat, tmat);
//	Normalize(imat[0]);
//	Normalize(imat[1]);
//
//	while(ml) {
//
//		/* draw radius */
//		if(mb.editelems) {
//			if((ml.flag & SELECT) && (ml.flag & MB_SCALE_RAD)) cpack(0xA0A0F0);
//			else cpack(0x3030A0);
//
//			if(G.f & G_PICKSEL) {
//				ml.selcol1= code;
//				glLoadName(code++);
//			}
//		}
//		drawcircball(GL_LINE_LOOP, &(ml.x), ml.rad, imat);
//
//		/* draw stiffness */
//		if(mb.editelems) {
//			if((ml.flag & SELECT) && !(ml.flag & MB_SCALE_RAD)) cpack(0xA0F0A0);
//			else cpack(0x30A030);
//
//			if(G.f & G_PICKSEL) {
//				ml.selcol2= code;
//				glLoadName(code++);
//			}
//			drawcircball(GL_LINE_LOOP, &(ml.x), ml.rad*atan(ml.s)/M_PI_2, imat);
//		}
//
//		ml= ml.next;
//	}
//	return 0;
//}
//
//static void draw_forcefield(Scene *scene, Object *ob)
//{
//	PartDeflect *pd= ob.pd;
//	float imat[4][4], tmat[4][4];
//	float vec[3]= {0.0, 0.0, 0.0};
//	int curcol;
//	float size;
//
//	if(G.f & G_RENDER_SHADOW)
//		return;
//
//	/* XXX why? */
//	if(ob!=scene.obedit && (ob.flag & SELECT)) {
//		if(ob==OBACT) curcol= TH_ACTIVE;
//		else curcol= TH_SELECT;
//	}
//	else curcol= TH_WIRE;
//
//	/* scale size of circle etc with the empty drawsize */
//	if (ob.type == OB_EMPTY) size = ob.empty_drawsize;
//	else size = 1.0;
//
//	/* calculus here, is reused in PFIELD_FORCE */
//	wmGetMatrix(tmat);
//	Mat4Invert(imat, tmat);
////	Normalize(imat[0]);		// we don't do this because field doesnt scale either... apart from wind!
////	Normalize(imat[1]);
//
//	if (pd.forcefield == PFIELD_WIND) {
//		float force_val;
//
//		Mat4One(tmat);
//		UI_ThemeColorBlend(curcol, TH_BACK, 0.5);
//
//		//if (has_ipo_code(ob.ipo, OB_PD_FSTR))
//		//	force_val = IPO_GetFloatValue(ob.ipo, OB_PD_FSTR, scene.r.cfra);
//		//else
//			force_val = pd.f_strength;
//		force_val*= 0.1;
//		drawcircball(GL_LINE_LOOP, vec, size, tmat);
//		vec[2]= 0.5*force_val;
//		drawcircball(GL_LINE_LOOP, vec, size, tmat);
//		vec[2]= 1.0*force_val;
//		drawcircball(GL_LINE_LOOP, vec, size, tmat);
//		vec[2]= 1.5*force_val;
//		drawcircball(GL_LINE_LOOP, vec, size, tmat);
//		vec[2] = 0; /* reset vec for max dist circle */
//
//	}
//	else if (pd.forcefield == PFIELD_FORCE) {
//		float ffall_val;
//
//		//if (has_ipo_code(ob.ipo, OB_PD_FFALL))
//		//	ffall_val = IPO_GetFloatValue(ob.ipo, OB_PD_FFALL, scene.r.cfra);
//		//else
//			ffall_val = pd.f_power;
//
//		UI_ThemeColorBlend(curcol, TH_BACK, 0.5);
//		drawcircball(GL_LINE_LOOP, vec, size, imat);
//		UI_ThemeColorBlend(curcol, TH_BACK, 0.9 - 0.4 / pow(1.5, (double)ffall_val));
//		drawcircball(GL_LINE_LOOP, vec, size*1.5, imat);
//		UI_ThemeColorBlend(curcol, TH_BACK, 0.9 - 0.4 / pow(2.0, (double)ffall_val));
//		drawcircball(GL_LINE_LOOP, vec, size*2.0, imat);
//	}
//	else if (pd.forcefield == PFIELD_VORTEX) {
//		float ffall_val, force_val;
//
//		Mat4One(tmat);
//		//if (has_ipo_code(ob.ipo, OB_PD_FFALL))
//		//	ffall_val = IPO_GetFloatValue(ob.ipo, OB_PD_FFALL, scene.r.cfra);
//		//else
//			ffall_val = pd.f_power;
//
//		//if (has_ipo_code(ob.ipo, OB_PD_FSTR))
//		//	force_val = IPO_GetFloatValue(ob.ipo, OB_PD_FSTR, scene.r.cfra);
//		//else
//			force_val = pd.f_strength;
//
//		UI_ThemeColorBlend(curcol, TH_BACK, 0.7);
//		if (force_val < 0) {
//			drawspiral(vec, size*1.0, tmat, 1);
//			drawspiral(vec, size*1.0, tmat, 16);
//		}
//		else {
//			drawspiral(vec, size*1.0, tmat, -1);
//			drawspiral(vec, size*1.0, tmat, -16);
//		}
//	}
//	else if (pd.forcefield == PFIELD_GUIDE && ob.type==OB_CURVE) {
//		Curve *cu= ob.data;
//		if((cu.flag & CU_PATH) && cu.path && cu.path.data) {
//			float mindist, guidevec1[4], guidevec2[3];
//
//			//if (has_ipo_code(ob.ipo, OB_PD_FSTR))
//			//	mindist = IPO_GetFloatValue(ob.ipo, OB_PD_FSTR, scene.r.cfra);
//			//else
//				mindist = pd.f_strength;
//
//			/*path end*/
//			setlinestyle(3);
//			where_on_path(ob, 1.0f, guidevec1, guidevec2);
//			UI_ThemeColorBlend(curcol, TH_BACK, 0.5);
//			drawcircball(GL_LINE_LOOP, guidevec1, mindist, imat);
//
//			/*path beginning*/
//			setlinestyle(0);
//			where_on_path(ob, 0.0f, guidevec1, guidevec2);
//			UI_ThemeColorBlend(curcol, TH_BACK, 0.5);
//			drawcircball(GL_LINE_LOOP, guidevec1, mindist, imat);
//
//			VECCOPY(vec, guidevec1);	/* max center */
//		}
//	}
//
//	setlinestyle(3);
//	UI_ThemeColorBlend(curcol, TH_BACK, 0.5);
//
//	if(pd.falloff==PFIELD_FALL_SPHERE){
//		/* as last, guide curve alters it */
//		if(pd.flag & PFIELD_USEMAX)
//			drawcircball(GL_LINE_LOOP, vec, pd.maxdist, imat);
//
//		if(pd.flag & PFIELD_USEMIN)
//			drawcircball(GL_LINE_LOOP, vec, pd.mindist, imat);
//	}
//	else if(pd.falloff==PFIELD_FALL_TUBE){
//		float radius,distance;
//
//		Mat4One(tmat);
//
//		vec[0]=vec[1]=0.0f;
//		radius=(pd.flag&PFIELD_USEMAXR)?pd.maxrad:1.0f;
//		distance=(pd.flag&PFIELD_USEMAX)?pd.maxdist:0.0f;
//		vec[2]=distance;
//		distance=(pd.flag&PFIELD_POSZ)?-distance:-2.0f*distance;
//
//		if(pd.flag & (PFIELD_USEMAX|PFIELD_USEMAXR))
//			drawtube(vec,radius,distance,tmat);
//
//		radius=(pd.flag&PFIELD_USEMINR)?pd.minrad:1.0f;
//		distance=(pd.flag&PFIELD_USEMIN)?pd.mindist:0.0f;
//		vec[2]=distance;
//		distance=(pd.flag&PFIELD_POSZ)?-distance:-2.0f*distance;
//
//		if(pd.flag & (PFIELD_USEMIN|PFIELD_USEMINR))
//			drawtube(vec,radius,distance,tmat);
//	}
//	else if(pd.falloff==PFIELD_FALL_CONE){
//		float radius,distance;
//
//		Mat4One(tmat);
//
//		radius=(pd.flag&PFIELD_USEMAXR)?pd.maxrad:1.0f;
//		radius*=(float)M_PI/180.0f;
//		distance=(pd.flag&PFIELD_USEMAX)?pd.maxdist:0.0f;
//
//		if(pd.flag & (PFIELD_USEMAX|PFIELD_USEMAXR)){
//			drawcone(vec,distance*sin(radius),distance*cos(radius),tmat);
//			if((pd.flag & PFIELD_POSZ)==0)
//				drawcone(vec,distance*sin(radius),-distance*cos(radius),tmat);
//		}
//
//		radius=(pd.flag&PFIELD_USEMINR)?pd.minrad:1.0f;
//		radius*=(float)M_PI/180.0f;
//		distance=(pd.flag&PFIELD_USEMIN)?pd.mindist:0.0f;
//
//		if(pd.flag & (PFIELD_USEMIN|PFIELD_USEMINR)){
//			drawcone(vec,distance*sin(radius),distance*cos(radius),tmat);
//			if((pd.flag & PFIELD_POSZ)==0)
//				drawcone(vec,distance*sin(radius),-distance*cos(radius),tmat);
//		}
//	}
//	setlinestyle(0);
//}
//
//static void draw_box(float vec[8][3])
//{
//	glBegin(GL_LINE_STRIP);
//	glVertex3fv(vec[0]); glVertex3fv(vec[1]);glVertex3fv(vec[2]); glVertex3fv(vec[3]);
//	glVertex3fv(vec[0]); glVertex3fv(vec[4]);glVertex3fv(vec[5]); glVertex3fv(vec[6]);
//	glVertex3fv(vec[7]); glVertex3fv(vec[4]);
//	glEnd();
//
//	glBegin(GL_LINES);
//	glVertex3fv(vec[1]); glVertex3fv(vec[5]);
//	glVertex3fv(vec[2]); glVertex3fv(vec[6]);
//	glVertex3fv(vec[3]); glVertex3fv(vec[7]);
//	glEnd();
//}
//
///* uses boundbox, function used by Ketsji */
//void get_local_bounds(Object *ob, float *center, float *size)
//{
//	BoundBox *bb= object_get_boundbox(ob);
//
//	if(bb==NULL) {
//		center[0]= center[1]= center[2]= 0.0;
//		VECCOPY(size, ob.size);
//	}
//	else {
//		size[0]= 0.5*fabs(bb.vec[0][0] - bb.vec[4][0]);
//		size[1]= 0.5*fabs(bb.vec[0][1] - bb.vec[2][1]);
//		size[2]= 0.5*fabs(bb.vec[0][2] - bb.vec[1][2]);
//
//		center[0]= (bb.vec[0][0] + bb.vec[4][0])/2.0;
//		center[1]= (bb.vec[0][1] + bb.vec[2][1])/2.0;
//		center[2]= (bb.vec[0][2] + bb.vec[1][2])/2.0;
//	}
//}
//
//
//
//static void draw_bb_quadric(BoundBox *bb, short type)
//{
//	float size[3], cent[3];
//	GLUquadricObj *qobj = gluNewQuadric();
//
//	gluQuadricDrawStyle(qobj, GLU_SILHOUETTE);
//
//	size[0]= 0.5*fabs(bb.vec[0][0] - bb.vec[4][0]);
//	size[1]= 0.5*fabs(bb.vec[0][1] - bb.vec[2][1]);
//	size[2]= 0.5*fabs(bb.vec[0][2] - bb.vec[1][2]);
//
//	cent[0]= (bb.vec[0][0] + bb.vec[4][0])/2.0;
//	cent[1]= (bb.vec[0][1] + bb.vec[2][1])/2.0;
//	cent[2]= (bb.vec[0][2] + bb.vec[1][2])/2.0;
//
//	glPushMatrix();
//	if(type==OB_BOUND_SPHERE) {
//		glTranslatef(cent[0], cent[1], cent[2]);
//		glScalef(size[0], size[1], size[2]);
//		gluSphere(qobj, 1.0, 8, 5);
//	}
//	else if(type==OB_BOUND_CYLINDER) {
//		float radius = size[0] > size[1] ? size[0] : size[1];
//		glTranslatef(cent[0], cent[1], cent[2]-size[2]);
//		glScalef(radius, radius, 2.0*size[2]);
//		gluCylinder(qobj, 1.0, 1.0, 1.0, 8, 1);
//	}
//	else if(type==OB_BOUND_CONE) {
//		float radius = size[0] > size[1] ? size[0] : size[1];
//		glTranslatef(cent[0], cent[2]-size[2], cent[1]);
//		glScalef(radius, 2.0*size[2], radius);
//		glRotatef(-90., 1.0, 0.0, 0.0);
//		gluCylinder(qobj, 1.0, 0.0, 1.0, 8, 1);
//	}
//	glPopMatrix();
//
//	gluDeleteQuadric(qobj);
//}
//
//static void draw_bounding_volume(Scene *scene, Object *ob)
//{
//	BoundBox *bb=0;
//
//	if(ob.type==OB_MESH) {
//		bb= mesh_get_bb(ob);
//	}
//	else if ELEM3(ob.type, OB_CURVE, OB_SURF, OB_FONT) {
//		bb= ( (Curve *)ob.data ).bb;
//	}
//	else if(ob.type==OB_MBALL) {
//		bb= ob.bb;
//		if(bb==0) {
//			makeDispListMBall(scene, ob);
//			bb= ob.bb;
//		}
//	}
//	else {
//		drawcube();
//		return;
//	}
//
//	if(bb==0) return;
//
//	if(ob.boundtype==OB_BOUND_BOX) draw_box(bb.vec);
//	else draw_bb_quadric(bb, ob.boundtype);
//
//}
//
//static void drawtexspace(Object *ob)
//{
//	float vec[8][3], loc[3], size[3];
//
//	if(ob.type==OB_MESH) {
//		mesh_get_texspace(ob.data, loc, NULL, size);
//	}
//	else if ELEM3(ob.type, OB_CURVE, OB_SURF, OB_FONT) {
//		Curve *cu= ob.data;
//		VECCOPY(size, cu.size);
//		VECCOPY(loc, cu.loc);
//	}
//	else if(ob.type==OB_MBALL) {
//		MetaBall *mb= ob.data;
//		VECCOPY(size, mb.size);
//		VECCOPY(loc, mb.loc);
//	}
//	else return;
//
//	vec[0][0]=vec[1][0]=vec[2][0]=vec[3][0]= loc[0]-size[0];
//	vec[4][0]=vec[5][0]=vec[6][0]=vec[7][0]= loc[0]+size[0];
//
//	vec[0][1]=vec[1][1]=vec[4][1]=vec[5][1]= loc[1]-size[1];
//	vec[2][1]=vec[3][1]=vec[6][1]=vec[7][1]= loc[1]+size[1];
//
//	vec[0][2]=vec[3][2]=vec[4][2]=vec[7][2]= loc[2]-size[2];
//	vec[1][2]=vec[2][2]=vec[5][2]=vec[6][2]= loc[2]+size[2];
//
//	setlinestyle(2);
//
//	draw_box(vec);
//
//	setlinestyle(0);
//}

/* draws wire outline */
static void drawSolidSelect(GL2 gl, Scene scene, View3D v3d, RegionView3D rv3d, Base base)
{
	bObject ob= base.object;

	gl.glLineWidth(2.0f);
	gl.glDepthMask(false);

	if(ob.type==ObjectTypes.OB_FONT || ob.type==ObjectTypes.OB_CURVE || ob.type==ObjectTypes.OB_SURF) {
//		Curve *cu = ob.data;
//		if (displist_has_faces(&cu.disp) && boundbox_clip(rv3d, ob.obmat, cu.bb)) {
//			draw_index_wire= 0;
//			drawDispListwire(&cu.disp);
//			draw_index_wire= 1;
//		}
	} else if (ob.type==ObjectTypes.OB_MBALL) {
//		if((base.flag & OB_FROMDUPLI)==0)
//			drawDispListwire(&ob.disp);
	}
	else if(ob.type==ObjectTypes.OB_ARMATURE) {
//		if(!(ob.flag & OB_POSEMODE))
//			draw_armature(scene, v3d, rv3d, base, OB_WIRE, 0);
	}

	gl.glLineWidth(1.0f);
	gl.glDepthMask(true);
}

//static void drawWireExtra(Scene *scene, RegionView3D *rv3d, Object *ob)
//{
//	if(ob!=scene.obedit && (ob.flag & SELECT)) {
//		if(ob==OBACT) {
//			if(ob.flag & OB_FROMGROUP) UI_ThemeColor(TH_GROUP_ACTIVE);
//			else UI_ThemeColor(TH_ACTIVE);
//		}
//		else if(ob.flag & OB_FROMGROUP)
//			UI_ThemeColorShade(TH_GROUP_ACTIVE, -16);
//		else
//			UI_ThemeColor(TH_SELECT);
//	}
//	else {
//		if(ob.flag & OB_FROMGROUP)
//			UI_ThemeColor(TH_GROUP);
//		else {
//			if(ob.dtx & OB_DRAWWIRE) {
//				glColor3ub(80,80,80);
//			} else {
//				UI_ThemeColor(TH_WIRE);
//			}
//		}
//	}
//
//	bglPolygonOffset(rv3d.dist, 1.0);
//	glDepthMask(0);	// disable write in zbuffer, selected edge wires show better
//
//	if (ELEM3(ob.type, OB_FONT, OB_CURVE, OB_SURF)) {
//		Curve *cu = ob.data;
//		if (boundbox_clip(rv3d, ob.obmat, cu.bb)) {
//			if (ob.type==OB_CURVE)
//				draw_index_wire= 0;
//			drawDispListwire(&cu.disp);
//			if (ob.type==OB_CURVE)
//				draw_index_wire= 1;
//		}
//	} else if (ob.type==OB_MBALL) {
//		drawDispListwire(&ob.disp);
//	}
//
//	glDepthMask(1);
//	bglPolygonOffset(rv3d.dist, 0.0);
//}
//
///* should be called in view space */
//static void draw_hooks(Object *ob)
//{
//	ModifierData *md;
//	float vec[3];
//
//	for (md=ob.modifiers.first; md; md=md.next) {
//		if (md.type==eModifierType_Hook) {
//			HookModifierData *hmd = (HookModifierData*) md;
//
//			VecMat4MulVecfl(vec, ob.obmat, hmd.cent);
//
//			if(hmd.object) {
//				setlinestyle(3);
//				glBegin(GL_LINES);
//				glVertex3fv(hmd.object.obmat[3]);
//				glVertex3fv(vec);
//				glEnd();
//				setlinestyle(0);
//			}
//
//			glPointSize(3.0);
//			bglBegin(GL_POINTS);
//			bglVertex3fv(vec);
//			bglEnd();
//			glPointSize(1.0);
//		}
//	}
//}
//
////<rcruiz>
//void drawRBpivot(bRigidBodyJointConstraint *data)
//{
//	float radsPerDeg = 6.283185307179586232f / 360.f;
//	int axis;
//	float v1[3]= {data.pivX, data.pivY, data.pivZ};
//	float eu[3]= {radsPerDeg*data.axX, radsPerDeg*data.axY, radsPerDeg*data.axZ};
//	float mat[4][4];
//
//	if(G.f & G_RENDER_SHADOW)
//		return;
//
//	EulToMat4(eu,mat);
//	glLineWidth (4.0f);
//	setlinestyle(2);
//	for (axis=0; axis<3; axis++) {
//		float dir[3] = {0,0,0};
//		float v[3]= {data.pivX, data.pivY, data.pivZ};
//
//		dir[axis] = 1.f;
//		glBegin(GL_LINES);
//		Mat4MulVecfl(mat,dir);
//		v[0] += dir[0];
//		v[1] += dir[1];
//		v[2] += dir[2];
//		glVertex3fv(v1);
//		glVertex3fv(v);
//		glEnd();
//		if (axis==0)
//			view3d_object_text_draw_add(v[0], v[1], v[2], "px", 0);
//		else if (axis==1)
//			view3d_object_text_draw_add(v[0], v[1], v[2], "py", 0);
//		else
//			view3d_object_text_draw_add(v[0], v[1], v[2], "pz", 0);
//	}
//	glLineWidth (1.0f);
//	setlinestyle(0);
//}

static int warning_recursive= 0;

/* flag can be DRAW_PICKING	and/or DRAW_CONSTCOLOR, DRAW_SCENESET */
public static void draw_object(GL2 gl, Scene scene, ARegion ar, View3D v3d, Base base, int flag)
{
//	static int warning_recursive= 0;
	bObject ob;
//	Curve *cu;
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
//	//float cfraont;
//	float vec1[3], vec2[3];
	int col=0;
	int /*sel, drawtype,*/ colindex= 0;
	int i, selstart, selend, empty_object=0;
	short dt, dtx, zbufoff= 0;

	/* only once set now, will be removed too, should become a global standard */
	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

	ob= base.object;

	if (ob!=scene.obedit) {
		if ((ob.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)!=0)
			return;
	}

//	/* xray delay? */
//	if((flag & DRAW_PICKING)==0 && (base.flag & OB_FROMDUPLI)==0) {
//		/* don't do xray in particle mode, need the z-buffer */
//		if(!(G.f & G_PARTICLEEDIT)) {
//			/* xray and transp are set when it is drawing the 2nd/3rd pass */
//			if(!v3d.xray && !v3d.transp && (ob.dtx & OB_DRAWXRAY) && !(ob.dtx & OB_DRAWTRANSP)) {
//				add_view3d_after(v3d, base, V3D_XRAY, flag);
//				return;
//			}
//		}
//	}
//
//	/* draw keys? */
//#if 0 // XXX old animation system
//	if(base==(scene.basact) || (base.flag & (SELECT+BA_WAS_SEL))) {
//		if(flag==0 && warning_recursive==0 && ob!=scene.obedit) {
//			if(ob.ipo && ob.ipo.showkey && (ob.ipoflag & OB_DRAWKEY)) {
//				ListBase elems;
//				CfraElem *ce;
//				float temp[7][3];
//
//				warning_recursive= 1;
//
//				elems.first= elems.last= 0;
//				// warning: no longer checks for certain ob-keys only... (so does this need to use the proper ipokeys then?)
//				make_cfra_list(ob.ipo, &elems);
//
//				cfraont= (scene.r.cfra);
//				drawtype= v3d.drawtype;
//				if(drawtype>OB_WIRE) v3d.drawtype= OB_WIRE;
//				sel= base.flag;
//				memcpy(temp, &ob.loc, 7*3*sizeof(float));
//
//				ipoflag= ob.ipoflag;
//				ob.ipoflag &= ~OB_OFFS_OB;
//
//				set_no_parent_ipo(1);
//				disable_speed_curve(1);
//
//				if ((ob.ipoflag & OB_DRAWKEYSEL)==0) {
//					ce= elems.first;
//					while(ce) {
//						if(!ce.sel) {
//							(scene.r.cfra)= ce.cfra/scene.r.framelen;
//
//							base.flag= 0;
//
//							where_is_object_time(scene, ob, (scene.r.cfra));
//							draw_object(scene, ar, v3d, base, 0);
//						}
//						ce= ce.next;
//					}
//				}
//
//				ce= elems.first;
//				while(ce) {
//					if(ce.sel) {
//						(scene.r.cfra)= ce.cfra/scene.r.framelen;
//
//						base.flag= SELECT;
//
//						where_is_object_time(scene, ob, (scene.r.cfra));
//						draw_object(scene, ar, v3d, base, 0);
//					}
//					ce= ce.next;
//				}
//
//				set_no_parent_ipo(0);
//				disable_speed_curve(0);
//
//				base.flag= sel;
//				ob.ipoflag= ipoflag;
//
//				/* restore icu.curval */
//				(scene.r.cfra)= cfraont;
//
//				memcpy(&ob.loc, temp, 7*3*sizeof(float));
//				where_is_object(scene, ob);
//				v3d.drawtype= drawtype;
//
//				BLI_freelistN(&elems);
//
//				warning_recursive= 0;
//			}
//		}
//	}
//#endif // XXX old animation system
//
//	/* patch? children objects with a timeoffs change the parents. How to solve! */
//	/* if( ((int)ob.ctime) != F_(scene.r.cfra)) where_is_object(scene, ob); */

	/* multiply view with object matrix */
	WmSubWindow.wmMultMatrix(gl, ob.obmat);
	/* local viewmat and persmat, to calculate projections */
	WmSubWindow.wmGetMatrix(gl, rv3d.viewmatob);
	WmSubWindow.wmGetSingleMatrix(gl, rv3d.persmatob);

	/* which wire color */
	if((flag & DRAW_CONSTCOLOR) == 0) {
                short[] base_p = new short[2];
		View3dView.project_short(ar, ob.obmat[3], base_p);
                base.sx = base_p[0];
                base.sy = base_p[1];

		if((G.moving & Global.G_TRANSFORM_OBJ)!=0 && (base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0)
                    Resources.UI_ThemeColor(gl, Resources.TH_TRANSFORM);
		else {

			if(ob.type==ObjectTypes.OB_LAMP)
                            Resources.UI_ThemeColor(gl, Resources.TH_LAMP);
			else
                            Resources.UI_ThemeColor(gl, Resources.TH_WIRE);

			if((scene.basact)==base) {
				if((base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0)
                                    Resources.UI_ThemeColor(gl, Resources.TH_ACTIVE);
			}
			else {
				if((base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0)
                                    Resources.UI_ThemeColor(gl, Resources.TH_SELECT);
			}

			// no theme yet
			if(ob.id.lib!=null) {
				if((base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0)
                                    colindex = 4;
				else
                                    colindex = 3;
			}
			else if(warning_recursive==1) {
				if((base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0) {
					if(scene.basact==base) colindex = 8;
					else colindex= 7;
				}
				else
                                    colindex = 6;
			}
			else if((ob.flag & ObjectTypes.OB_FROMGROUP)!=0) {
				if((base.flag & (Blender.SELECT+ObjectTypes.BA_WAS_SEL))!=0) {
					if(scene.basact==base)
                                            Resources.UI_ThemeColor(gl, Resources.TH_GROUP_ACTIVE);
					else
                                            Resources.UI_ThemeColorShade(gl, Resources.TH_GROUP_ACTIVE, -16);
				}
				else
                                    Resources.UI_ThemeColor(gl, Resources.TH_GROUP);
				colindex= 0;
			}

		}

		if(colindex!=0) {
			col= colortab[colindex];
			GlUtil.cpack(gl, col);
		}
	}

	/* maximum drawtype */
	dt= (short)UtilDefines.MIN2(v3d.drawtype, ob.dt);
	if(v3d.zbuf==0 && dt>ObjectTypes.OB_WIRE)
            dt= ObjectTypes.OB_WIRE;
	dtx= 0;

//	/* faceselect exception: also draw solid when dt==wire, except in editmode */
//	if(ob==OBACT && (G.f & (G_VERTEXPAINT+G_TEXTUREPAINT+G_WEIGHTPAINT))) {
//		if(ob.type==OB_MESH) {
//
//			if(ob==scene.obedit);
//			else {
//				if(dt<OB_SOLID)
//					zbufoff= 1;
//
//				dt= OB_SHADED;
//				glEnable(GL_DEPTH_TEST);
//			}
//		}
//		else {
//			if(dt<OB_SOLID) {
//				dt= OB_SOLID;
//				glEnable(GL_DEPTH_TEST);
//				zbufoff= 1;
//			}
//		}
//	}

	/* draw-extra supported for boundbox drawmode too */
	if(dt>=ObjectTypes.OB_BOUNDBOX ) {

		dtx= ob.dtx;
		if(scene.obedit==ob) {
			// the only 2 extra drawtypes alowed in editmode
			dtx= (short)(dtx & (ObjectTypes.OB_DRAWWIRE|ObjectTypes.OB_TEXSPACE));
		}

	}

//	/* bad exception, solve this! otherwise outline shows too late */
//	if(ELEM3(ob.type, OB_CURVE, OB_SURF, OB_FONT)) {
//		cu= ob.data;
//		/* still needed for curves hidden in other layers. depgraph doesnt handle that yet */
//		if (cu.disp.first==NULL) makeDispListCurveTypes(scene, ob, 0);
//	}

	/* draw outline for selected solid objects, mesh does itself */
	if((v3d.flag & View3dTypes.V3D_SELECT_OUTLINE)!=0 && ob.type!=ObjectTypes.OB_MESH) {
		if(dt>ObjectTypes.OB_WIRE && dt<ObjectTypes.OB_TEXTURE && ob!=scene.obedit && (flag & DRAW_SCENESET)==0) {
			if ((ob.dtx&ObjectTypes.OB_DRAWWIRE)==0 && (ob.flag&Blender.SELECT)!=0 && (flag&DRAW_PICKING)==0) {

				drawSolidSelect(gl, scene, v3d, rv3d, base);
			}
		}
	}

	switch( ob.type) {
		case ObjectTypes.OB_MESH:
			if ((base.flag&ObjectTypes.OB_RADIO)==0) {
				empty_object= draw_mesh_object(gl, scene, v3d, rv3d, base, dt, flag);
				if(flag!=DRAW_CONSTCOLOR)
                                    dtx &= ~ObjectTypes.OB_DRAWWIRE; // mesh draws wire itself
			}

			break;
		case ObjectTypes.OB_FONT:
//			cu= ob.data;
//			if(cu.editfont) {
//				draw_textcurs(cu.editfont.textcurs);
//
//				if (cu.flag & CU_FAST) {
//					cpack(0xFFFFFF);
//					set_inverted_drawing(1);
//					drawDispList(scene, v3d, rv3d, base, OB_WIRE);
//					set_inverted_drawing(0);
//				} else {
//					drawDispList(scene, v3d, rv3d, base, dt);
//				}
//
//				if (cu.linewidth != 0.0) {
//					cpack(0xff44ff);
//					UI_ThemeColor(TH_WIRE);
//					VECCOPY(vec1, ob.orig);
//					VECCOPY(vec2, ob.orig);
//					vec1[0] += cu.linewidth;
//					vec2[0] += cu.linewidth;
//					vec1[1] += cu.linedist * cu.fsize;
//					vec2[1] -= cu.lines * cu.linedist * cu.fsize;
//					setlinestyle(3);
//					glBegin(GL_LINE_STRIP);
//					glVertex2fv(vec1);
//					glVertex2fv(vec2);
//					glEnd();
//					setlinestyle(0);
//				}
//
//				setlinestyle(3);
//				for (i=0; i<cu.totbox; i++) {
//					if (cu.tb[i].w != 0.0) {
//						if (i == (cu.actbox-1))
//							UI_ThemeColor(TH_ACTIVE);
//						else
//							UI_ThemeColor(TH_WIRE);
//						vec1[0] = cu.tb[i].x;
//						vec1[1] = cu.tb[i].y + cu.fsize;
//						vec1[2] = 0.001;
//						glBegin(GL_LINE_STRIP);
//						glVertex3fv(vec1);
//						vec1[0] += cu.tb[i].w;
//						glVertex3fv(vec1);
//						vec1[1] -= cu.tb[i].h;
//						glVertex3fv(vec1);
//						vec1[0] -= cu.tb[i].w;
//						glVertex3fv(vec1);
//						vec1[1] += cu.tb[i].h;
//						glVertex3fv(vec1);
//						glEnd();
//					}
//				}
//				setlinestyle(0);
//
//
//				if (BKE_font_getselection(ob, &selstart, &selend) && cu.selboxes) {
//					float selboxw;
//
//					cpack(0xffffff);
//					set_inverted_drawing(1);
//					for (i=0; i<(selend-selstart+1); i++) {
//						SelBox *sb = &(cu.selboxes[i]);
//
//						if (i<(selend-selstart)) {
//							if (cu.selboxes[i+1].y == sb.y)
//								selboxw= cu.selboxes[i+1].x - sb.x;
//							else
//								selboxw= sb.w;
//						}
//						else {
//							selboxw= sb.w;
//						}
//						glBegin(GL_QUADS);
//						glVertex3f(sb.x, sb.y, 0.001);
//						glVertex3f(sb.x+selboxw, sb.y, 0.001);
//						glVertex3f(sb.x+selboxw, sb.y+sb.h, 0.001);
//						glVertex3f(sb.x, sb.y+sb.h, 0.001);
//						glEnd();
//					}
//					set_inverted_drawing(0);
//				}
//			}
//			else if(dt==OB_BOUNDBOX)
//				draw_bounding_volume(scene, ob);
//			else if(boundbox_clip(rv3d, ob.obmat, cu.bb))
//				empty_object= drawDispList(scene, v3d, rv3d, base, dt);

			break;
		case ObjectTypes.OB_CURVE:
		case ObjectTypes.OB_SURF:
//			cu= ob.data;
//
//			if(cu.editnurb) {
//				drawnurb(scene, v3d, rv3d, base, cu.editnurb.first, dt);
//			}
//			else if(dt==OB_BOUNDBOX)
//				draw_bounding_volume(scene, ob);
//			else if(boundbox_clip(rv3d, ob.obmat, cu.bb)) {
//				empty_object= drawDispList(scene, v3d, rv3d, base, dt);
//
//				if(cu.path)
//					curve_draw_speed(scene, ob);
//			}
			break;
		case ObjectTypes.OB_MBALL:
		{
//			MetaBall *mb= ob.data;
//
//			if(mb.editelems)
//				drawmball(scene, v3d, rv3d, base, dt);
//			else if(dt==OB_BOUNDBOX)
//				draw_bounding_volume(scene, ob);
//			else
//				empty_object= drawmball(scene, v3d, rv3d, base, dt);
			break;
		}
		case ObjectTypes.OB_EMPTY:
			drawaxes(gl, ob.empty_drawsize, flag, ob.empty_drawtype);
			break;
		case ObjectTypes.OB_LAMP:
			drawlamp(gl, scene, v3d, rv3d, ob);
			if(dtx!=0 || (base.flag & Blender.SELECT)!=0)
                            WmSubWindow.wmMultMatrix(gl, ob.obmat);
			break;
		case ObjectTypes.OB_CAMERA:
			drawcamera(gl, scene, v3d, rv3d, ob, flag);
			break;
		case ObjectTypes.OB_LATTICE:
//			drawlattice(scene, v3d, ob);
			break;
		case ObjectTypes.OB_ARMATURE:
//			if(dt>OB_WIRE) GPU_enable_material(0, NULL); // we use default material
//			empty_object= draw_armature(scene, v3d, rv3d, base, dt, flag);
//			if(dt>OB_WIRE) GPU_disable_material();
			break;
		default:
			drawaxes(gl, 1.0f, flag, (byte)ObjectTypes.OB_ARROWS);
	}
//	if(ob.pd && ob.pd.forcefield) draw_forcefield(scene, ob);
//
//	/* code for new particle system */
//	if(		(warning_recursive==0) &&
//			(ob.particlesystem.first) &&
//			(flag & DRAW_PICKING)==0 &&
//			(ob!=scene.obedit)
//	  ) {
//		ParticleSystem *psys;
//		if(col || (ob.flag & SELECT)) cpack(0xFFFFFF);	/* for visibility, also while wpaint */
//		//glDepthMask(GL_FALSE);
//
//		wmLoadMatrix(rv3d.viewmat);
//
//		for(psys=ob.particlesystem.first; psys; psys=psys.next)
//			draw_new_particle_system(scene, v3d, rv3d, base, psys, dt);
//
//		if(G.f & G_PARTICLEEDIT && ob==OBACT) {
//			psys= PE_get_current(scene, ob);
//			if(psys && !scene.obedit && psys_in_edit_mode(scene, psys))
//				draw_particle_edit(scene, v3d, rv3d, ob, psys, dt);
//		}
//		view3d_particle_text_draw(v3d, ar);
//
//		wmMultMatrix(ob.obmat);
//
//		//glDepthMask(GL_TRUE);
//		if(col) cpack(col);
//	}
//
//	{
//		bConstraint *con;
//		for(con=ob.constraints.first; con; con= con.next)
//		{
//			if(con.type==CONSTRAINT_TYPE_RIGIDBODYJOINT)
//			{
//				bRigidBodyJointConstraint *data = (bRigidBodyJointConstraint*)con.data;
//				if(data.flag&CONSTRAINT_DRAW_PIVOT)
//					drawRBpivot(data);
//			}
//		}
//	}
//
//	/* draw extra: after normal draw because of makeDispList */
//	if(dtx && !(G.f & (G_RENDER_OGL|G_RENDER_SHADOW))) {
//		if(dtx & OB_AXIS) {
//			drawaxes(1.0f, flag, OB_ARROWS);
//		}
//		if(dtx & OB_BOUNDBOX) draw_bounding_volume(scene, ob);
//		if(dtx & OB_TEXSPACE) drawtexspace(ob);
//		if(dtx & OB_DRAWNAME) {
//			/* patch for several 3d cards (IBM mostly) that crash on glSelect with text drawing */
//			/* but, we also dont draw names for sets or duplicators */
//			if(flag == 0) {
//				view3d_object_text_draw_add(0.0f, 0.0f, 0.0f, ob.id.name+2, 10);
//			}
//		}
//		/*if(dtx & OB_DRAWIMAGE) drawDispListwire(&ob.disp);*/
//		if((dtx & OB_DRAWWIRE) && dt>=OB_SOLID) drawWireExtra(scene, rv3d, ob);
//	}
//
//	if(dt<OB_SHADED) {
//		if((ob.gameflag & OB_DYNAMIC) ||
//			((ob.gameflag & OB_BOUNDS) && (ob.boundtype == OB_BOUND_SPHERE))) {
//			float tmat[4][4], imat[4][4], vec[3];
//
//			vec[0]= vec[1]= vec[2]= 0.0;
//			wmGetMatrix(tmat);
//			Mat4Invert(imat, tmat);
//
//			setlinestyle(2);
//			drawcircball(GL_LINE_LOOP, vec, ob.inertia, imat);
//			setlinestyle(0);
//		}
//	}
//
//	/* return warning, this is cached text draw */
//	view3d_object_text_draw(v3d, ar);

	WmSubWindow.wmLoadMatrix(gl, rv3d.viewmat);

	if(zbufoff!=0) gl.glDisable(GL2.GL_DEPTH_TEST);

	if(warning_recursive!=0)
            return;
	if((base.flag & (ObjectTypes.OB_FROMDUPLI|ObjectTypes.OB_RADIO))!=0)
            return;
	if((G.f & Global.G_RENDER_SHADOW)!=0)
            return;

	/* object centers, need to be drawn in viewmat space for speed, but OK for picking select */
	if(ob!=(scene.basact!=null?scene.basact.object:null) || (G.f & (Global.G_VERTEXPAINT|Global.G_TEXTUREPAINT|Global.G_WEIGHTPAINT))==0) {
		int do_draw_center= -1;	/* defines below are zero or positive... */

		if((scene.basact)==base)
			do_draw_center= Blender.ACTIVE;
		else if((base.flag & Blender.SELECT)!=0)
			do_draw_center= Blender.SELECT;
		else if(empty_object!=0 || (v3d.flag & View3dTypes.V3D_DRAW_CENTERS)!=0)
			do_draw_center= Blender.DESELECT;

		if(do_draw_center != -1) {
			if((flag & DRAW_PICKING)!=0) {
				/* draw a single point for opengl selection */
				gl.glBegin(GL2.GL_POINTS);
				gl.glVertex3fv(ob.obmat[3],0);
				gl.glEnd();
			}
			else if((flag & DRAW_CONSTCOLOR)==0) {
				/* we don't draw centers for duplicators and sets */
				drawcentercircle(gl, v3d, rv3d, ob.obmat[3], do_draw_center, ob.id.lib!=null || ob.id.us>1);
			}
		}
	}

//	/* not for sets, duplicators or picking */
//	if(flag==0 && (!(v3d.flag & V3D_HIDE_HELPLINES))) {
//		ListBase *list;
//
//		/* draw hook center and offset line */
//		if(ob!=scene.obedit) draw_hooks(ob);
//
//		/* help lines and so */
//		if(ob!=scene.obedit && ob.parent && (ob.parent.lay & v3d.lay)) {
//			setlinestyle(3);
//			glBegin(GL_LINES);
//			glVertex3fv(ob.obmat[3]);
//			glVertex3fv(ob.orig);
//			glEnd();
//			setlinestyle(0);
//		}
//
//		/* Drawing the constraint lines */
//		list = &ob.constraints;
//		if (list) {
//			bConstraint *curcon;
//			bConstraintOb *cob;
//			char col[4], col2[4];
//
//			UI_GetThemeColor3ubv(TH_GRID, col);
//			UI_make_axis_color(col, col2, 'z');
//			glColor3ubv((GLubyte *)col2);
//
//			cob= constraints_make_evalob(scene, ob, NULL, CONSTRAINT_OBTYPE_OBJECT);
//
//			for (curcon = list.first; curcon; curcon=curcon.next) {
//				bConstraintTypeInfo *cti= constraint_get_typeinfo(curcon);
//				ListBase targets = {NULL, NULL};
//				bConstraintTarget *ct;
//
//				if ((curcon.flag & CONSTRAINT_EXPAND) && (cti) && (cti.get_constraint_targets)) {
//					cti.get_constraint_targets(curcon, &targets);
//
//					for (ct= targets.first; ct; ct= ct.next) {
//						/* calculate target's matrix */
//						if (cti.get_target_matrix)
//							cti.get_target_matrix(curcon, cob, ct, bsystem_time(scene, ob, (float)(scene.r.cfra), give_timeoffset(ob)));
//						else
//							Mat4One(ct.matrix);
//
//						setlinestyle(3);
//						glBegin(GL_LINES);
//						glVertex3fv(ct.matrix[3]);
//						glVertex3fv(ob.obmat[3]);
//						glEnd();
//						setlinestyle(0);
//					}
//
//					if (cti.flush_constraint_targets)
//						cti.flush_constraint_targets(curcon, &targets, 1);
//				}
//			}
//
//			constraints_clear_evalob(cob);
//		}
//	}
//
//	free_old_images();
}

///* ***************** BACKBUF SEL (BBS) ********* */
//
//static void bbs_mesh_verts__mapFunc(void *userData, int index, float *co, float *no_f, short *no_s)
//{
//	int offset = (intptr_t) userData;
//	EditVert *eve = EM_get_vert_for_index(index);
//
//	if (eve.h==0) {
//		WM_set_framebuffer_index_color(offset+index);
//		bglVertex3fv(co);
//	}
//}
//static void bbs_mesh_verts(DerivedMesh *dm, int offset)
//{
//	glPointSize( UI_GetThemeValuef(TH_VERTEX_SIZE) );
//	bglBegin(GL_POINTS);
//	dm.foreachMappedVert(dm, bbs_mesh_verts__mapFunc, (void*)(intptr_t) offset);
//	bglEnd();
//	glPointSize(1.0);
//}
//
//static int bbs_mesh_wire__setDrawOptions(void *userData, int index)
//{
//	int offset = (intptr_t) userData;
//	EditEdge *eed = EM_get_edge_for_index(index);
//
//	if (eed.h==0) {
//		WM_set_framebuffer_index_color(offset+index);
//		return 1;
//	} else {
//		return 0;
//	}
//}
//static void bbs_mesh_wire(DerivedMesh *dm, int offset)
//{
//	dm.drawMappedEdges(dm, bbs_mesh_wire__setDrawOptions, (void*)(intptr_t) offset);
//}
//
//static int bbs_mesh_solid__setSolidDrawOptions(void *userData, int index, int *drawSmooth_r)
//{
//	if (EM_get_face_for_index(index).h==0) {
//		if (userData) {
//			WM_set_framebuffer_index_color(index+1);
//		}
//		return 1;
//	} else {
//		return 0;
//	}
//}
//
//static void bbs_mesh_solid__drawCenter(void *userData, int index, float *cent, float *no)
//{
//	EditFace *efa = EM_get_face_for_index(index);
//
//	if (efa.h==0 && efa.fgonf!=EM_FGON) {
//		WM_set_framebuffer_index_color(index+1);
//
//		bglVertex3fv(cent);
//	}
//}
//
///* two options, facecolors or black */
//static void bbs_mesh_solid_EM(Scene *scene, View3D *v3d, Object *ob, DerivedMesh *dm, int facecol)
//{
//	cpack(0);
//
//	if (facecol) {
//		dm.drawMappedFaces(dm, bbs_mesh_solid__setSolidDrawOptions, (void*)(intptr_t) 1, 0);
//
//		if( CHECK_OB_DRAWFACEDOT(scene, v3d, ob.dt) ) {
//			glPointSize(UI_GetThemeValuef(TH_FACEDOT_SIZE));
//
//			bglBegin(GL_POINTS);
//			dm.foreachMappedFaceCenter(dm, bbs_mesh_solid__drawCenter, NULL);
//			bglEnd();
//		}
//
//	} else {
//		dm.drawMappedFaces(dm, bbs_mesh_solid__setSolidDrawOptions, (void*) 0, 0);
//	}
//}
//
//static int bbs_mesh_solid__setDrawOpts(void *userData, int index, int *drawSmooth_r)
//{
//	Mesh *me = userData;
//
//	if (!(me.mface[index].flag&ME_HIDE)) {
//		WM_set_framebuffer_index_color(index+1);
//		return 1;
//	} else {
//		return 0;
//	}
//}
//
///* TODO remove this - since face select mode now only works with painting */
//static void bbs_mesh_solid(Scene *scene, View3D *v3d, Object *ob)
//{
//	DerivedMesh *dm = mesh_get_derived_final(scene, ob, v3d.customdata_mask);
//	Mesh *me = (Mesh*)ob.data;
//
//	glColor3ub(0, 0, 0);
//	dm.drawMappedFaces(dm, bbs_mesh_solid__setDrawOpts, me, 0);
//
//	dm.release(dm);
//}
//
//void draw_object_backbufsel(Scene *scene, View3D *v3d, RegionView3D *rv3d, Object *ob)
//{
//	ToolSettings *ts= scene.toolsettings;
//
//	wmMultMatrix(ob.obmat);
//
//	glClearDepth(1.0); glClear(GL_DEPTH_BUFFER_BIT);
//	glEnable(GL_DEPTH_TEST);
//
//	switch( ob.type) {
//	case OB_MESH:
//	{
//		if(ob == scene.obedit) {
//			Mesh *me= ob.data;
//			EditMesh *em= me.edit_mesh;
//
//			DerivedMesh *dm = editmesh_get_derived_cage(scene, ob, em, CD_MASK_BAREMESH);
//
//			EM_init_index_arrays(em, 1, 1, 1);
//
//			bbs_mesh_solid_EM(scene, v3d, ob, dm, ts.selectmode & SCE_SELECT_FACE);
//			if(ts.selectmode & SCE_SELECT_FACE)
//				em_solidoffs = 1+em.totface;
//			else
//				em_solidoffs= 1;
//
//			bglPolygonOffset(rv3d.dist, 1.0);
//
//			// we draw edges always, for loop (select) tools
//			bbs_mesh_wire(dm, em_solidoffs);
//			em_wireoffs= em_solidoffs + em.totedge;
//
//			// we draw verts if vert select mode or if in transform (for snap).
//			if(ts.selectmode & SCE_SELECT_VERTEX || G.moving & G_TRANSFORM_EDIT) {
//				bbs_mesh_verts(dm, em_wireoffs);
//				em_vertoffs= em_wireoffs + em.totvert;
//			}
//			else em_vertoffs= em_wireoffs;
//
//			bglPolygonOffset(rv3d.dist, 0.0);
//
//			dm.release(dm);
//
//			EM_free_index_arrays();
//		}
//		else bbs_mesh_solid(scene, v3d, ob);
//	}
//		break;
//	case OB_CURVE:
//	case OB_SURF:
//		break;
//	}
//
//	wmLoadMatrix(rv3d.viewmat);
//}
//
//
///* ************* draw object instances for bones, for example ****************** */
///*               assumes all matrices/etc set OK */
//
///* helper function for drawing object instances - meshes */
//static void draw_object_mesh_instance(Scene *scene, View3D *v3d, RegionView3D *rv3d,
//									  Object *ob, int dt, int outline)
//{
//	Mesh *me= ob.data;
//	DerivedMesh *dm=NULL, *edm=NULL;
//	int glsl;
//
//	if(ob == scene.obedit)
//		edm= editmesh_get_derived_base(ob, me.edit_mesh);
//	else
//		dm = mesh_get_derived_final(scene, ob, CD_MASK_BAREMESH);
//
//	if(dt<=OB_WIRE) {
//		if(dm)
//			dm.drawEdges(dm, 1);
//		else if(edm)
//			edm.drawEdges(edm, 1);
//	}
//	else {
//		if(outline)
//			draw_mesh_object_outline(v3d, ob, dm?dm:edm);
//
//		if(dm) {
//			glsl = draw_glsl_material(scene, ob, v3d, dt);
//			GPU_begin_object_materials(v3d, rv3d, scene, ob, glsl, NULL);
//		}
//		else {
//			glEnable(GL_COLOR_MATERIAL);
//			UI_ThemeColor(TH_BONE_SOLID);
//			glDisable(GL_COLOR_MATERIAL);
//		}
//
//		glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, 0);
//		glFrontFace((ob.transflag&OB_NEG_SCALE)?GL_CW:GL_CCW);
//		glEnable(GL_LIGHTING);
//
//		if(dm) {
//			dm.drawFacesSolid(dm, GPU_enable_material);
//			GPU_end_object_materials();
//		}
//		else if(edm)
//			edm.drawMappedFaces(edm, NULL, NULL, 0);
//
//		glDisable(GL_LIGHTING);
//	}
//
//	if(edm) edm.release(edm);
//	if(dm) dm.release(dm);
//}
//
//void draw_object_instance(Scene *scene, View3D *v3d, RegionView3D *rv3d, Object *ob, int dt, int outline)
//{
//	if (ob == NULL)
//		return;
//
//	switch (ob.type) {
//		case OB_MESH:
//			draw_object_mesh_instance(scene, v3d, rv3d, ob, dt, outline);
//			break;
//		case OB_EMPTY:
//			drawaxes(ob.empty_drawsize, 0, ob.empty_drawtype);
//			break;
//	}
//}
}
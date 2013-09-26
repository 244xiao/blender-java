/**
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
 * The Original Code is Copyright (C) 2009 Blender Foundation.
 * All rights reserved.
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.uinterface;

import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import blender.blenfont.Blf;
import blender.blenfont.BlfTypes;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.StringUtil;
import blender.editors.screen.GlUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UI.uiWidgetTypeEnum;
import blender.makesdna.UserDefTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ThemeUI;
import blender.makesdna.sdna.bTheme;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.uiFontStyle;
import blender.makesdna.sdna.uiStyle;
import blender.makesdna.sdna.uiWidgetColors;
import blender.makesdna.sdna.uiWidgetStateColors;

public class UIWidgets {

/* ************** widget base functions ************** */
/*
     - in: roundbox codes for corner types and radius
     - return: array of [size][2][x,y] points, the edges of the roundbox, + UV coords
 
     - draw black box with alpha 0 on exact button boundbox
     - for ever AA step:
        - draw the inner part for a round filled box, with color blend codes or texture coords
        - draw outline in outline color
        - draw outer part, bottom half, extruded 1 pixel to bottom, for emboss shadow
        - draw extra decorations
     - draw background color box with alpha 1 on exact button boundbox
 
 */

/* fill this struct with polygon info to draw AA'ed */
/* it has outline, back, and two optional tria meshes */

public static class uiWidgetTrias {
	public int tot;

	public float[][] vec=new float[32][2];
//	public int (*index)[3];
    public int[][] index;

};

public static class uiWidgetBase {

	public int totvert, halfwayvert;
	public float[][] outer_v=new float[64][2];
	public float[][] inner_v=new float[64][2];
	public float[][] inner_uv=new float[64][2];

	public short inner, outline, emboss; /* set on/off */
	public short shadedir;

	public uiWidgetTrias tria1 = new uiWidgetTrias();
	public uiWidgetTrias tria2 = new uiWidgetTrias();

};

/* uiWidgetType: for time being only for visual appearance,
   later, a handling callback can be added too 
*/
public static class uiWidgetType {

    public static interface State {
        public void run(uiWidgetType wt, int state);
    }

    public static interface Draw {
        public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign);
    }

    public static interface Custom {
        public void run(uiBut but, uiWidgetColors wcol, rcti rect, int state, int roundboxalign);
    }

    public static interface Text {
        public void run(GL2 gl, uiFontStyle fstyle, uiWidgetColors wcol, uiBut but, rcti rect);
    }
	
	/* pointer to theme color definition */
	public uiWidgetColors wcol_theme;
	public uiWidgetStateColors wcol_state;

	/* converted colors for state */
	public uiWidgetColors wcol = new uiWidgetColors();

	public State state;
	public Draw draw;
	public Custom custom;
	public Text text;

        public void wcolSet(uiWidgetColors wcolNew) {
            System.arraycopy(wcolNew.outline, 0, wcol.outline, 0, wcol.outline.length);
            System.arraycopy(wcolNew.inner, 0, wcol.inner, 0, wcol.inner.length);
            System.arraycopy(wcolNew.inner_sel, 0, wcol.inner_sel, 0, wcol.inner_sel.length);
            System.arraycopy(wcolNew.item, 0, wcol.item, 0, wcol.item.length);
            System.arraycopy(wcolNew.text, 0, wcol.text, 0, wcol.text.length);
            System.arraycopy(wcolNew.text_sel, 0, wcol.text_sel, 0, wcol.text_sel.length);
            wcol.shaded = wcolNew.shaded;
            wcol.shadetop = wcolNew.shadetop;
            wcol.shadedown = wcolNew.shadedown;
//            wcol.pad = wcolNew.pad;
        }
};


/* *********************** draw data ************************** */

static float[][] cornervec= {{0.0f, 0.0f}, {0.195f, 0.02f}, {0.383f, 0.067f}, {0.55f, 0.169f},
{0.707f, 0.293f}, {0.831f, 0.45f}, {0.924f, 0.617f}, {0.98f, 0.805f}, {1.0f, 1.0f}};

static float[][] jit= {{0.468813f , -0.481430f}, {-0.155755f , -0.352820f},
{0.219306f , -0.238501f},  {-0.393286f , -0.110949f}, {-0.024699f , 0.013908f},
{0.343805f , 0.147431f}, {-0.272855f , 0.269918f}, {0.095909f , 0.388710f}};

//static float[][] num_tria_vert= {
//{0.382684f, 0.923879f}, {0.000001f, 1.000000f}, {-0.382683f, 0.923880f}, {-0.707107f, 0.707107f},
//{-0.923879f, 0.382684f}, {-1.000000f, 0.000000f}, {-0.923880f, -0.382684f}, {-0.707107f, -0.707107f},
//{-0.382683f, -0.923880f}, {0.000000f, -1.000000f}, {0.382684f, -0.923880f}, {0.707107f, -0.707107f},
//{0.923880f, -0.382684f}, {1.000000f, -0.000000f}, {0.923880f, 0.382683f}, {0.707107f, 0.707107f},
//{-0.352077f, 0.532607f}, {-0.352077f, -0.549313f}, {0.729843f, -0.008353f}};
static float[][] num_tria_vert= { 
{-0.352077f, 0.532607f}, {-0.352077f, -0.549313f}, {0.330000f, -0.008353f}};

//static int[][] num_tria_face= {
//{13, 14, 18}, {17, 5, 6}, {12, 13, 18}, {17, 6, 7}, {15, 18, 14}, {16, 4, 5}, {16, 5, 17}, {18, 11, 12},
//{18, 17, 10}, {18, 10, 11}, {17, 9, 10}, {15, 0, 18}, {18, 0, 16}, {3, 4, 16}, {8, 9, 17}, {8, 17, 7},
//{2, 3, 16}, {1, 2, 16}, {16, 0, 1}};
static int[][] num_tria_face= {
{0, 1, 2}};

static float[][] scroll_circle_vert= {
{0.382684f, 0.923879f}, {0.000001f, 1.000000f}, {-0.382683f, 0.923880f}, {-0.707107f, 0.707107f},
{-0.923879f, 0.382684f}, {-1.000000f, 0.000000f}, {-0.923880f, -0.382684f}, {-0.707107f, -0.707107f},
{-0.382683f, -0.923880f}, {0.000000f, -1.000000f}, {0.382684f, -0.923880f}, {0.707107f, -0.707107f},
{0.923880f, -0.382684f}, {1.000000f, -0.000000f}, {0.923880f, 0.382683f}, {0.707107f, 0.707107f}};

static int[][] scroll_circle_face= {
{0, 1, 2}, {2, 0, 3}, {3, 0, 15}, {3, 15, 4}, {4, 15, 14}, {4, 14, 5}, {5, 14, 13}, {5, 13, 6}, 
{6, 13, 12}, {6, 12, 7}, {7, 12, 11}, {7, 11, 8}, {8, 11, 10}, {8, 10, 9}};

static float[][] menu_tria_vert= {
{-0.41f, 0.16f}, {0.41f, 0.16f}, {0.0f, 0.82f},
{0.0f, -0.82f}, {-0.41f, -0.16f}, {0.41f, -0.16f}};

static int[][] menu_tria_face= {{2, 0, 1}, {3, 5, 4}};

static float[][] check_tria_vert= {
{-0.578579f, 0.253369f}, 	{-0.392773f, 0.412794f}, 	{-0.004241f, -0.328551f},
{-0.003001f, 0.034320f}, 	{1.055313f, 0.864744f}, 	{0.866408f, 1.026895f}};

static int[][] check_tria_face= {
{3, 2, 4}, {3, 4, 5}, {1, 0, 3}, {0, 2, 3}};

/* ************************************************* */

public static void ui_draw_anti_tria(GL2 gl, float x1, float y1, float x2, float y2, float x3, float y3)
{
	float[] color = new float[4];
	int j;

	gl.glEnable(GL2.GL_BLEND);
	gl.glGetFloatv(GL2.GL_CURRENT_COLOR, color,0);
	color[3]= 0.125f;
	gl.glColor4fv(color,0);

	/* for each AA step */
	for(j=0; j<8; j++) {
		gl.glTranslatef(1.0f*jit[j][0], 1.0f*jit[j][1], 0.0f);

		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glVertex2f(x3, y3);
		gl.glEnd();

		gl.glTranslatef(-1.0f*jit[j][0], -1.0f*jit[j][1], 0.0f);
	}

	gl.glDisable(GL2.GL_BLEND);

}

static void widget_init(uiWidgetBase wtb)
{
	wtb.totvert= wtb.halfwayvert= 0;
	wtb.tria1.tot= 0;
	wtb.tria2.tot= 0;

	wtb.inner= 1;
	wtb.outline= 1;
	wtb.emboss= 1;
	wtb.shadedir= 1;
}

/* helper call, makes shadow rect, with 'sun' above menu, so only shadow to left/right/bottom */
/* return tot */
static int round_box_shadow_edges(float[][] vert, rcti rect, float rad, int roundboxalign, float step)
{
	float[][] vec=new float[9][2];
	float minx, miny, maxx, maxy;
	int a, tot= 0;

	rad+= step;

	if(2.0f*rad > rect.ymax-rect.ymin)
		rad= 0.5f*(rect.ymax-rect.ymin);

	minx= rect.xmin-step;
	miny= rect.ymin-step;
	maxx= rect.xmax+step;
	maxy= rect.ymax+step;

	/* mult */
	for(a=0; a<9; a++) {
		vec[a][0]= rad*cornervec[a][0];
		vec[a][1]= rad*cornervec[a][1];
	}

	/* start with left-top, anti clockwise */
	if((roundboxalign & 1)!=0) {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= minx+rad-vec[a][0];
			vert[tot][1]= maxy-vec[a][1];
		}
	}
	else {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= minx;
			vert[tot][1]= maxy;
		}
	}

	if((roundboxalign & 8)!=0) {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= minx+vec[a][1];
			vert[tot][1]= miny+rad-vec[a][0];
		}
	}
	else {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= minx;
			vert[tot][1]= miny;
		}
	}

	if((roundboxalign & 4)!=0) {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= maxx-rad+vec[a][0];
			vert[tot][1]= miny+vec[a][1];
		}
	}
	else {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= maxx;
			vert[tot][1]= miny;
		}
	}

	if((roundboxalign & 2)!=0) {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= maxx-vec[a][1];
			vert[tot][1]= maxy-rad+vec[a][0];
		}
	}
	else {
		for(a=0; a<9; a++, tot++) {
			vert[tot][0]= maxx;
			vert[tot][1]= maxy;
		}
	}
	return tot;
}

/* this call has 1 extra arg to allow mask outline */
static void round_box__edges(uiWidgetBase wt, int roundboxalign, rcti rect, float rad, float radi)
{
	float[][] vec=new float[9][2], veci=new float[9][2];
	float minx= rect.xmin, miny= rect.ymin, maxx= rect.xmax, maxy= rect.ymax;
	float minxi= minx + 1.0f; /* boundbox inner */
	float maxxi= maxx - 1.0f;
	float minyi= miny + 1.0f;
	float maxyi= maxy - 1.0f;
	float facxi= (maxxi!=minxi) ? 1.0f/(maxxi-minxi) : 0.0f; /* for uv, can divide by zero */
	float facyi= (maxyi!=minyi) ? 1.0f/(maxyi-minyi) : 0.0f;
	int a, tot= 0, minsize;
	final int hnum= ((roundboxalign & (1|2))==(1|2) || (roundboxalign & (4|8))==(4|8)) ? 1 : 2;
	final int vnum= ((roundboxalign & (1|8))==(1|8) || (roundboxalign & (2|4))==(2|4)) ? 1 : 2;

	minsize= UtilDefines.MIN2((rect.xmax-rect.xmin)*hnum, (rect.ymax-rect.ymin)*vnum);

	if(2.0f*rad > minsize)
		rad= 0.5f*minsize;

	if(2.0f*(radi+1.0f) > minsize)
		radi= 0.5f*minsize - 1.0f;

	/* mult */
	for(a=0; a<9; a++) {
		veci[a][0]= radi*cornervec[a][0];
		veci[a][1]= radi*cornervec[a][1];
		vec[a][0]= rad*cornervec[a][0];
		vec[a][1]= rad*cornervec[a][1];
	}

	/* corner left-bottom */
	if((roundboxalign & 8)!=0) {

		for(a=0; a<9; a++, tot++) {
			wt.inner_v[tot][0]= minxi+veci[a][1];
			wt.inner_v[tot][1]= minyi+radi-veci[a][0];

			wt.outer_v[tot][0]= minx+vec[a][1];
			wt.outer_v[tot][1]= miny+rad-vec[a][0];

			wt.inner_uv[tot][0]= facxi*(wt.inner_v[tot][0] - minxi);
			wt.inner_uv[tot][1]= facyi*(wt.inner_v[tot][1] - minyi);
		}
	}
	else {
		wt.inner_v[tot][0]= minxi;
		wt.inner_v[tot][1]= minyi;

		wt.outer_v[tot][0]= minx;
		wt.outer_v[tot][1]= miny;

		wt.inner_uv[tot][0]= 0.0f;
		wt.inner_uv[tot][1]= 0.0f;

		tot++;
	}

	/* corner right-bottom */
	if((roundboxalign & 4)!=0) {

		for(a=0; a<9; a++, tot++) {
			wt.inner_v[tot][0]= maxxi-radi+veci[a][0];
			wt.inner_v[tot][1]= minyi+veci[a][1];

			wt.outer_v[tot][0]= maxx-rad+vec[a][0];
			wt.outer_v[tot][1]= miny+vec[a][1];

			wt.inner_uv[tot][0]= facxi*(wt.inner_v[tot][0] - minxi);
			wt.inner_uv[tot][1]= facyi*(wt.inner_v[tot][1] - minyi);
		}
	}
	else {
		wt.inner_v[tot][0]= maxxi;
		wt.inner_v[tot][1]= minyi;

		wt.outer_v[tot][0]= maxx;
		wt.outer_v[tot][1]= miny;

		wt.inner_uv[tot][0]= 1.0f;
		wt.inner_uv[tot][1]= 0.0f;

		tot++;
	}

	wt.halfwayvert= tot;

	/* corner right-top */
	if((roundboxalign & 2)!=0) {

		for(a=0; a<9; a++, tot++) {
			wt.inner_v[tot][0]= maxxi-veci[a][1];
			wt.inner_v[tot][1]= maxyi-radi+veci[a][0];

			wt.outer_v[tot][0]= maxx-vec[a][1];
			wt.outer_v[tot][1]= maxy-rad+vec[a][0];

			wt.inner_uv[tot][0]= facxi*(wt.inner_v[tot][0] - minxi);
			wt.inner_uv[tot][1]= facyi*(wt.inner_v[tot][1] - minyi);
		}
	}
	else {
		wt.inner_v[tot][0]= maxxi;
		wt.inner_v[tot][1]= maxyi;

		wt.outer_v[tot][0]= maxx;
		wt.outer_v[tot][1]= maxy;

		wt.inner_uv[tot][0]= 1.0f;
		wt.inner_uv[tot][1]= 1.0f;

		tot++;
	}

	/* corner left-top */
	if((roundboxalign & 1)!=0) {

		for(a=0; a<9; a++, tot++) {
			wt.inner_v[tot][0]= minxi+radi-veci[a][0];
			wt.inner_v[tot][1]= maxyi-veci[a][1];

			wt.outer_v[tot][0]= minx+rad-vec[a][0];
			wt.outer_v[tot][1]= maxy-vec[a][1];

			wt.inner_uv[tot][0]= facxi*(wt.inner_v[tot][0] - minxi);
			wt.inner_uv[tot][1]= facyi*(wt.inner_v[tot][1] - minyi);
		}

	}
	else {

		wt.inner_v[tot][0]= minxi;
		wt.inner_v[tot][1]= maxyi;

		wt.outer_v[tot][0]= minx;
		wt.outer_v[tot][1]= maxy;

		wt.inner_uv[tot][0]= 0.0f;
		wt.inner_uv[tot][1]= 1.0f;

		tot++;
	}

	wt.totvert= tot;
}

static void round_box_edges(uiWidgetBase wt, int roundboxalign, rcti rect, float rad)
{
	round_box__edges(wt, roundboxalign, rect, rad, rad-1.0f);
}


/* based on button rect, return scaled array of triangles */
static void widget_num_tria(uiWidgetTrias tria, rcti rect, float triasize, char where)
{
	float centx, centy, sizex, sizey, minsize;
	int a, i1=0, i2=1;

	minsize= UtilDefines.MIN2(rect.xmax-rect.xmin, rect.ymax-rect.ymin);

	/* center position and size */
	centx= (float)rect.xmin + 0.5f*minsize;
	centy= (float)rect.ymin + 0.5f*minsize;
	sizex= sizey= -0.5f*triasize*minsize;

	if(where=='r') {
		centx= (float)rect.xmax - 0.5f*minsize;
		sizex= -sizex;
	}
	else if(where=='t') {
		centy= (float)rect.ymax - 0.5f*minsize;
		sizey= -sizey;
		i2=0; i1= 1;
	}
	else if(where=='b') {
		sizex= -sizex;
		i2=0; i1= 1;
	}

//	for(a=0; a<19; a++) {
	for(a=0; a<3; a++) {
		tria.vec[a][0]= sizex*num_tria_vert[a][i1] + centx;
		tria.vec[a][1]= sizey*num_tria_vert[a][i2] + centy;
	}

//	tria.tot= 19;
	tria.tot= 1;
	tria.index= num_tria_face;
}

public static void widget_scroll_circle(uiWidgetTrias tria, rcti rect, float triasize, char where)
{
	float centx, centy, sizex, sizey, minsize;
	int a, i1=0, i2=1;
	
	minsize= UtilDefines.MIN2(rect.xmax-rect.xmin, rect.ymax-rect.ymin);
	
	/* center position and size */
	centx= (float)rect.xmin + 0.5f*minsize;
	centy= (float)rect.ymin + 0.5f*minsize;
	sizex= sizey= -0.5f*triasize*minsize;

	if(where=='r') {
		centx= (float)rect.xmax - 0.5f*minsize;
		sizex= -sizex;
	}	
	else if(where=='t') {
		centy= (float)rect.ymax - 0.5f*minsize;
		sizey= -sizey;
		i2=0; i1= 1;
	}	
	else if(where=='b') {
		sizex= -sizex;
		i2=0; i1= 1;
	}	
	
	for(a=0; a<16; a++) {
		tria.vec[a][0]= sizex*scroll_circle_vert[a][i1] + centx;
		tria.vec[a][1]= sizey*scroll_circle_vert[a][i2] + centy;
	}
	
	tria.tot= 14;
	tria.index= scroll_circle_face;
}

static void widget_trias_draw(GL2 gl, uiWidgetTrias tria)
{
	int a;

	gl.glBegin(GL2.GL_TRIANGLES);
	for(a=0; a<tria.tot; a++) {
		gl.glVertex2fv(tria.vec[ tria.index[a][0] ],0);
		gl.glVertex2fv(tria.vec[ tria.index[a][1] ],0);
		gl.glVertex2fv(tria.vec[ tria.index[a][2] ],0);
	}
	gl.glEnd();

}

static void widget_menu_trias(uiWidgetTrias tria, rcti rect)
{
	float centx, centy, size, asp;
	int a;

	/* center position and size */
	centx= rect.xmax - 0.5f*(rect.ymax-rect.ymin);
	centy= rect.ymin + 0.5f*(rect.ymax-rect.ymin);
	size= 0.4f*(rect.ymax-rect.ymin);

	/* XXX exception */
	asp= ((float)rect.xmax-rect.xmin)/((float)rect.ymax-rect.ymin);
	if(asp > 1.2f && asp < 2.6f)
		centx= rect.xmax - 0.3f*(rect.ymax-rect.ymin);

	for(a=0; a<6; a++) {
		tria.vec[a][0]= size*menu_tria_vert[a][0] + centx;
		tria.vec[a][1]= size*menu_tria_vert[a][1] + centy;
	}

	tria.tot= 2;
	tria.index= menu_tria_face;
}

static void widget_check_trias(uiWidgetTrias tria, rcti rect)
{
	float centx, centy, size;
	int a;

	/* center position and size */
	centx= rect.xmin + 0.5f*(rect.ymax-rect.ymin);
	centy= rect.ymin + 0.5f*(rect.ymax-rect.ymin);
	size= 0.5f*(rect.ymax-rect.ymin);

	for(a=0; a<6; a++) {
		tria.vec[a][0]= size*check_tria_vert[a][0] + centx;
		tria.vec[a][1]= size*check_tria_vert[a][1] + centy;
	}

	tria.tot= 4;
	tria.index= check_tria_face;
}


/* prepares shade colors */
static void shadecolors4(byte[] coltop, byte[] coldown, byte[] color, short shadetop, short shadedown)
{

	coltop[0]= (byte)UtilDefines.CLAMPIS((color[0]&0xFF)+shadetop, 0, 255);
	coltop[1]= (byte)UtilDefines.CLAMPIS((color[1]&0xFF)+shadetop, 0, 255);
	coltop[2]= (byte)UtilDefines.CLAMPIS((color[2]&0xFF)+shadetop, 0, 255);
	coltop[3]= color[3];

	coldown[0]= (byte)UtilDefines.CLAMPIS((color[0]&0xFF)+shadedown, 0, 255);
	coldown[1]= (byte)UtilDefines.CLAMPIS((color[1]&0xFF)+shadedown, 0, 255);
	coldown[2]= (byte)UtilDefines.CLAMPIS((color[2]&0xFF)+shadedown, 0, 255);
	coldown[3]= color[3];
}

static void round_box_shade_col4(GL2 gl, byte[] col1, byte[] col2, float fac)
{
	int faci, facm;
	byte[] col=new byte[4];

	faci= (int)StrictMath.floor(255.1f*fac);
	facm= 255-faci;

	col[0]= (byte)((faci*(col1[0]&0xFF) + facm*(col2[0]&0xFF))>>8);
	col[1]= (byte)((faci*(col1[1]&0xFF) + facm*(col2[1]&0xFF))>>8);
	col[2]= (byte)((faci*(col1[2]&0xFF) + facm*(col2[2]&0xFF))>>8);
	col[3]= (byte)((faci*(col1[3]&0xFF) + facm*(col2[3]&0xFF))>>8);

	gl.glColor4ubv(col,0);
}

//static void widgetbase_outline(uiWidgetBase *wtb)
//{
//	int a;
//
//	/* outline */
//	glBegin(GL_QUAD_STRIP);
//	for(a=0; a<wtb.totvert; a++) {
//		glVertex2fv(wtb.outer_v[a]);
//		glVertex2fv(wtb.inner_v[a]);
//	}
//	glVertex2fv(wtb.outer_v[0]);
//	glVertex2fv(wtb.inner_v[0]);
//	glEnd();
//}

static void widgetbase_draw(GL2 gl, uiWidgetBase wtb, uiWidgetColors wcol)
{
	int j, a;

	gl.glEnable(GL2.GL_BLEND);

	/* backdrop non AA */
	if(wtb.inner!=0) {
		if(wcol.shaded==0) {
//			if (wcol.alpha_check) {
//				GLubyte checker_stipple_sml[32*32/8] =
//				{
//					255,0,255,0,255,0,255,0,255,0,255,0,255,0,255,0, \
//					255,0,255,0,255,0,255,0,255,0,255,0,255,0,255,0, \
//					0,255,0,255,0,255,0,255,0,255,0,255,0,255,0,255, \
//					0,255,0,255,0,255,0,255,0,255,0,255,0,255,0,255, \
//					255,0,255,0,255,0,255,0,255,0,255,0,255,0,255,0, \
//					255,0,255,0,255,0,255,0,255,0,255,0,255,0,255,0, \
//					0,255,0,255,0,255,0,255,0,255,0,255,0,255,0,255, \
//					0,255,0,255,0,255,0,255,0,255,0,255,0,255,0,255, \
//				};
//
//				float x_mid= 0.0f; /* used for dumb clamping of values */
//
//				/* dark checkers */
//				glColor4ub(100, 100, 100, 255);
//				glBegin(GL_POLYGON);
//				for(a=0; a<wtb->totvert; a++) {
//					glVertex2fv(wtb->inner_v[a]);
//				}
//				glEnd();
//
//				/* light checkers */
//				glEnable(GL_POLYGON_STIPPLE);
//				glColor4ub(160, 160, 160, 255);
//				glPolygonStipple(checker_stipple_sml);
//				glBegin(GL_POLYGON);
//				for(a=0; a<wtb->totvert; a++) {
//					glVertex2fv(wtb->inner_v[a]);
//				}
//				glEnd();
//				glDisable(GL_POLYGON_STIPPLE);
//
//				/* alpha fill */
//				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//				glColor4ubv((unsigned char*)wcol->inner);
//				glBegin(GL_POLYGON);
//				for(a=0; a<wtb->totvert; a++) {
//					glVertex2fv(wtb->inner_v[a]);
//					x_mid += wtb->inner_v[a][0];
//				}
//				x_mid /= wtb->totvert;
//				glEnd();
//
//				/* 1/2 solid color */
//				glColor4ub(wcol->inner[0], wcol->inner[1], wcol->inner[2], 255);
//				glBegin(GL_POLYGON);
//				for(a=0; a<wtb->totvert; a++)
//					glVertex2f(MIN2(wtb->inner_v[a][0], x_mid), wtb->inner_v[a][1]);
//				glEnd();
//			}
//			else {
				/* simple fill */
				gl.glColor4ubv(wcol.inner,0);
				gl.glBegin(GL2.GL_POLYGON);
				for(a=0; a<wtb.totvert; a++)
					gl.glVertex2fv(wtb.inner_v[a],0);
				gl.glEnd();
//				if ((wcol.inner[2]&0xFF) == 194) {
//					System.out.println("widgetbase_draw color: "+(wcol.inner[0]&0xFF)+", "+(wcol.inner[1]&0xFF)+", "+(wcol.inner[2]&0xFF)+", "+(wcol.inner[3]&0xFF));
//					System.out.println("widgetbase_draw vert: "+wtb.inner_v[0][0]+", "+wtb.inner_v[0][1]);
//				}
//			}
		}
		else {
			byte[] col1=new byte[4], col2=new byte[4];

			shadecolors4(col1, col2, wcol.inner, wcol.shadetop, wcol.shadedown);

			gl.glShadeModel(GL2.GL_SMOOTH);
			gl.glBegin(GL2.GL_POLYGON);
			for(a=0; a<wtb.totvert; a++) {
				round_box_shade_col4(gl, col1, col2, wtb.inner_uv[a][wtb.shadedir]);
				gl.glVertex2fv(wtb.inner_v[a],0);
			}
			gl.glEnd();
			gl.glShadeModel(GL2.GL_FLAT);
		}
	}

	/* for each AA step */
	if(wtb.outline!=0) {
		for(j=0; j<8; j++) {
			gl.glTranslatef(1.0f*jit[j][0], 1.0f*jit[j][1], 0.0f);

			/* outline */
			gl.glColor4ub(wcol.outline[0], wcol.outline[1], wcol.outline[2], (byte)32);
			gl.glBegin(GL2.GL_QUAD_STRIP);
			for(a=0; a<wtb.totvert; a++) {
				gl.glVertex2fv(wtb.outer_v[a],0);
				gl.glVertex2fv(wtb.inner_v[a],0);
			}
			gl.glVertex2fv(wtb.outer_v[0],0);
			gl.glVertex2fv(wtb.inner_v[0],0);
			gl.glEnd();

			/* emboss bottom shadow */
			if(wtb.emboss!=0) {
				gl.glColor4f(1.0f, 1.0f, 1.0f, 0.02f);
				gl.glBegin(GL2.GL_QUAD_STRIP);
				for(a=0; a<wtb.halfwayvert; a++) {
					gl.glVertex2fv(wtb.outer_v[a],0);
					gl.glVertex2f(wtb.outer_v[a][0], wtb.outer_v[a][1]-1.0f);
				}
				gl.glEnd();
			}

			gl.glTranslatef(-1.0f*jit[j][0], -1.0f*jit[j][1], 0.0f);
		}
	}

	/* decoration */
	if(wtb.tria1.tot!=0 || wtb.tria2.tot!=0) {
		/* for each AA step */
		for(j=0; j<8; j++) {
			gl.glTranslatef(1.0f*jit[j][0], 1.0f*jit[j][1], 0.0f);

			if(wtb.tria1.tot!=0) {
				gl.glColor4ub(wcol.item[0], wcol.item[1], wcol.item[2], (byte)32);
				widget_trias_draw(gl, wtb.tria1);
			}
			if(wtb.tria2.tot!=0) {
				gl.glColor4ub(wcol.item[0], wcol.item[1], wcol.item[2], (byte)32);
				widget_trias_draw(gl, wtb.tria2);
			}

			gl.glTranslatef(-1.0f*jit[j][0], -1.0f*jit[j][1], 0.0f);
		}
	}

	gl.glDisable(GL2.GL_BLEND);

}

/* *********************** text/icon ************************************** */

//public static final int PREVIEW_PAD=	4;

public static void widget_draw_preview(int icon, float aspect, float alpha, rcti rect)
{
//	int w, h, x, y, size;
//
//	if(icon==ICON_NULL)
//		return;
//
//	w = rect->xmax - rect->xmin;
//	h = rect->ymax - rect->ymin;
//	size = MIN2(w, h);
//	size -= PREVIEW_PAD*2;	/* padding */
//	
//	x = rect->xmin + w/2 - size/2;
//	y = rect->ymin + h/2 - size/2;
//	
//	UI_icon_draw_preview_aspect_size(x, y, icon, aspect, size);
}

/* icons have been standardized... and this call draws in untransformed coordinates */
public static final float ICON_HEIGHT=		16.0f;

//static void widget_draw_icon(GL2 gl, uiBut but, int icon, int blend, rcti rect)
static void widget_draw_icon(GL2 gl, uiBut but, int icon, float alpha, rcti rect)
{
	int xs=0, ys=0;
	float aspect, height;
	
//	if ((but.flag & UI.UI_ICON_PREVIEW)!=0) {
//		widget_draw_preview(icon, but.block.aspect, alpha, rect);
//		return;
//	}

	/* this icon doesn't need draw... */
	if(icon==BIFIconID.ICON_BLANK1.ordinal() && (but.flag & UI.UI_ICON_SUBMENU)==0)
            return;

	/* we need aspect from block, for menus... these buttons are scaled in uiPositionBlock() */
	aspect= but.block.aspect;
	if(aspect != but.aspect) {
		/* prevent scaling up icon in pupmenu */
		if (aspect < 1.0f) {
			height= ICON_HEIGHT;
			aspect = 1.0f;

		}
		else
			height= ICON_HEIGHT/aspect;
	}
	else
		height= ICON_HEIGHT;

	/* calculate blend color */
	if (but.type==UI.TOG || but.type==UI.ROW || but.type==UI.TOGN || but.type==UI.LISTROW) {
		if((but.flag & UI.UI_SELECT)!=0);
		else if((but.flag & UI.UI_ACTIVE)!=0);
//		else blend= -60;
		else alpha= 0.5f;
	}
	
	/* extra feature allows more alpha blending */
	if(but.type==UI.LABEL && but.a1==1.0f) alpha *= but.a2;

	gl.glEnable(GL.GL_BLEND);

	if(icon!=0 && icon!=BIFIconID.ICON_BLANK1.ordinal()) {
		if((but.flag & UI.UI_ICON_LEFT)!=0) {
			if (but.type==UI.BUT_TOGDUAL) {
				if (but.drawstr[0]!=0) {
					xs= rect.xmin-1;
				} else {
					xs= (int)((rect.xmin+rect.xmax- height)/2);
				}
			}
			else if ((but.block.flag & UI.UI_BLOCK_LOOP)!=0) {
				if(but.type==UI.SEARCH_MENU)
					xs= rect.xmin+4;
				else
					xs= rect.xmin+1;
			}
			else if ((but.type==UI.ICONROW) || (but.type==UI.ICONTEXTROW)) {
				xs= rect.xmin+3;
			}
			else {
				xs= rect.xmin+4;
			}
			ys= (int)((rect.ymin+rect.ymax- height)/2);
		}
		else {
			xs= (int)((rect.xmin+rect.xmax- height)/2);
			ys= (int)((rect.ymin+rect.ymax- height)/2);
		}

//		/* to indicate draggable */
//		if(but.dragpoin!=null && (but.flag & UI.UI_ACTIVE)!=0) {
//			float[] rgb= {1.25f, 1.25f, 1.25f};
//			UIIcons.UI_icon_draw_aspect_color(xs, ys, icon, aspect, rgb);
//		}
//		else
			UIIcons.UI_icon_draw_aspect(gl, xs, ys, icon, aspect, alpha);
//		UIIcons.UI_icon_draw_aspect_blended(gl, xs, ys, icon, aspect, blend);
		
	}

	if((but.flag & UI.UI_ICON_SUBMENU)!=0) {
		xs= rect.xmax-17;
		ys= (int)((rect.ymin+rect.ymax- height)/2);

//		UIIcons.UI_icon_draw_aspect_blended(gl, xs, ys, BIFIconID.ICON_RIGHTARROW_THIN.ordinal(), aspect, blend);
		UIIcons.UI_icon_draw_aspect(gl, xs, ys, BIFIconID.ICON_RIGHTARROW_THIN.ordinal(), aspect, alpha);
	}

	gl.glDisable(GL.GL_BLEND);
}

/* sets but.ofs to make sure text is correctly visible */
static void ui_text_leftclip(uiFontStyle fstyle, uiBut but, rcti rect)
{
//	int border= (but.flag & UI.UI_BUT_ALIGN_RIGHT)!=0? 8: 10;
//	int okwidth= rect.xmax-rect.xmin - border;
//	
//	if ((but.flag & UI.UI_HAS_ICON)!=0) okwidth -= 16;
//	
//	/* need to set this first */
//	UIStyle.uiStyleFontSet(fstyle);
//	
////	if (fstyle.kerning==1)	/* for BLF_width */
////		BLF.BLF_enable(fstyle.uifont_id, BLF.BLF_KERNING_DEFAULT);
//
//	but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,0);
//	but.ofs= 0;
//	
//	while(but.strwidth > okwidth ) {
//		
//		but.ofs++;
//		but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
//		
//		/* textbut exception */
//		if(but.editstr!=null && but.pos != -1) {
//			int pos= but.pos+1;
//			
//			if(pos-1 < but.ofs) {
//				pos= but.ofs-pos+1;
//				but.ofs -= pos;
//				if(but.ofs<0) {
//					but.ofs= 0;
//					pos--;
//				}
//				but.drawstr[ StringUtil.strlen(but.drawstr,0)-pos ]= 0;
//			}
//		}
//		
//		if(but.strwidth < 10) break;
//	}
//	
////	if (fstyle.kerning==1)
////		BLF.BLF_disable(fstyle.uifont_id, BLF.BLF_KERNING_DEFAULT);
	
	int border= (but.flag & UI.UI_BUT_ALIGN_RIGHT)!=0? 8: 10;
	int okwidth= rect.xmax-rect.xmin - border;
	
	if ((but.flag & UI.UI_HAS_ICON)!=0) okwidth -= 16;
	
	/* need to set this first */
	UIStyle.uiStyleFontSet(fstyle);
	
	if (fstyle.kerning==1)	/* for BLF_width */
		Blf.BLF_enable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);

	/* if text editing we define ofs dynamically */
	if(but.editstr!=null && but.pos >= 0) {
		if(but.ofs > but.pos)
			but.ofs= but.pos;
	}
	else but.ofs= 0;
	
	but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
	
	while(but.strwidth > okwidth) {
		
		/* textbut exception, clip right when... */
		if(but.editstr!=null && but.pos >= 0) {
			float width;
			byte[] buf=new byte[256];
			
			/* copy draw string */
			StringUtil.BLI_strncpy(buf,0, but.drawstr,0, buf.length);
			/* string position of cursor */
			buf[but.pos]= 0;
			width= Blf.BLF_width(fstyle.uifont_id, buf,but.ofs);
			
			/* if cursor is at 20 pixels of right side button we clip left */
			if(width > okwidth-20)
				but.ofs++;
			else {
				/* shift string to the left */
				if(width < 20 && but.ofs > 0)
					but.ofs--;
				but.drawstr[ StringUtil.strlen(but.drawstr,0)-1 ]= 0;
			}
		}
		else
			but.ofs++;

		but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
		
		if(but.strwidth < 10) break;
	}
	
	if (fstyle.kerning==1)
		Blf.BLF_disable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
}

public static void ui_text_label_rightclip(uiFontStyle fstyle, uiBut but, rcti rect)
{
	int border= (but.flag & UI.UI_BUT_ALIGN_RIGHT)!=0? 8: 10;
	int okwidth= rect.xmax-rect.xmin - border;
	byte[] cpoin=null;
	int cpoin_p = 0;
	byte[] cpend = but.drawstr;
	int cpend_p = StringUtil.strlen(but.drawstr,0);
	
	/* need to set this first */
	UIStyle.uiStyleFontSet(fstyle);
	
	if (fstyle.kerning==1)	/* for BLF_width */
		Blf.BLF_enable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
	
	but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,0);
	but.ofs= 0;
	
	/* find the space after ':' separator */
	cpoin = but.drawstr;
	cpoin_p= StringUtil.strrchr(but.drawstr,0, ':');
	
	if (cpoin_p!=-1 && (cpoin_p < cpend_p-2)) {
		byte[] cp2 = cpoin;
		int cp2_p = cpoin_p;
		
		/* chop off the leading text, starting from the right */
		while (but.strwidth > okwidth && cp2_p > 0) {
			/* shift the text after and including cp2 back by 1 char, +1 to include null terminator */
			System.arraycopy(cp2, cp2_p, cp2, cp2_p-1, StringUtil.strlen(cp2,cp2_p)+1);
			cp2_p--;
			
			but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
			if(but.strwidth < 10) break;
		}
	
	
		/* after the leading text is gone, chop off the : and following space, with ofs */
		while ((but.strwidth > okwidth) && (but.ofs < 2))
		{
			but.ofs++;
			but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
			if(but.strwidth < 10) break;
		}
		
	}

	/* once the label's gone, chop off the least significant digits */
	while(but.strwidth > okwidth ) {
		int pos= StringUtil.strlen(but.drawstr,0);
		
		but.drawstr[ pos-1 ] = 0;
		pos--;
		
		but.strwidth= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
		if(but.strwidth < 10) break;
	}
	
	if (fstyle.kerning==1)
		Blf.BLF_disable(fstyle.uifont_id, BlfTypes.BLF_KERNING_DEFAULT);
}

static void widget_draw_text(GL2 gl, uiFontStyle fstyle, uiWidgetColors wcol, uiBut but, rcti rect)
{
	//int transopts;
//	char *cpoin = NULL;
    int cpoin = -1;
    
    /* for underline drawing */
	float[] font_xofs={0}, font_yofs={0};

	UIStyle.uiStyleFontSet(fstyle);

	if(but.editstr!=null || (but.flag & UI.UI_TEXT_LEFT)!=0)
		fstyle.align= UserDefTypes.UI_STYLE_TEXT_LEFT;
	else
		fstyle.align= UserDefTypes.UI_STYLE_TEXT_CENTER;
	
//	if (fstyle->kerning==1)	/* for BLF_width */
//		BLF_enable(fstyle->uifont_id, BLF_KERNING_DEFAULT);

	/* text button selection and cursor */
	if(but.editstr!=null && but.pos != -1) {
		short t=0, pos=0, ch;
		short selsta_tmp, selend_tmp, selsta_draw, selwidth_draw;

		if ((but.selend - but.selsta) > 0) {
			/* text button selection */
			selsta_tmp = but.selsta;
			selend_tmp = but.selend;

			if(but.drawstr[0]!=0) {
				
				if (but.selsta >= but.ofs) {
					ch= (short)(but.drawstr[selsta_tmp]&0xFF);
					but.drawstr[selsta_tmp]= 0;
					
					selsta_draw = (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
					
					but.drawstr[selsta_tmp]= (byte)ch;
				} else
					selsta_draw = 0;

				ch= (short)(but.drawstr[selend_tmp]&0xFF);
				but.drawstr[selend_tmp]= 0;

				selwidth_draw = (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);

				but.drawstr[selend_tmp]= (byte)ch;

				gl.glColor3ubv(wcol.item,0);
//				gl.glRects((short)(rect.xmin+selsta_draw+1), (short)(rect.ymin+2), (short)(rect.xmin+selwidth_draw+1), (short)(rect.ymax-2));
				gl.glRects((short)(rect.xmin+selsta_draw), (short)(rect.ymin+2), (short)(rect.xmin+selwidth_draw), (short)(rect.ymax-2));
			}
		} else {
			/* text cursor */
			pos= but.pos;
			if(pos >= but.ofs) {
				if(but.drawstr[0]!=0) {
					ch= (short)(but.drawstr[pos]&0xFF);
					but.drawstr[pos]= 0;

//					t= (short)Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs);
					t= (short)(Blf.BLF_width(fstyle.uifont_id, but.drawstr,but.ofs) / but.aspect);

					but.drawstr[pos]= (byte)ch;
				}

//				gl.glColor3ub((byte)255,(byte)0,(byte)0);
				gl.glColor3f(0.20f, 0.6f, 0.9f);
				gl.glRects((short)(rect.xmin+t), (short)(rect.ymin+2), (short)(rect.xmin+t+2), (short)(rect.ymax-2));
			}
		}
	}
	
//	if (fstyle->kerning == 1)
//		BLF_disable(fstyle->uifont_id, BLF_KERNING_DEFAULT);
	
	//	ui_rasterpos_safe(x, y, but.aspect);
//	if(but.type==IDPOIN) transopts= 0;	// no translation, of course!
//	else transopts= ui_translate_buttons();

	/* cut string in 2 parts - only for menu entries */
	if((but.block.flag & UI.UI_BLOCK_LOOP)!=0) {
		if((but.type == UI.SLI || but.type == UI.NUM || but.type == UI.TEX || but.type == UI.NUMSLI || but.type == UI.NUMABS)==false) {
			cpoin= StringUtil.strchr(but.drawstr,0, '|');
			if(cpoin!=-1) but.drawstr[cpoin]= 0;		
		}
	}

	gl.glColor3ubv(wcol.text,0);
	
//	if (but.tip != null && StringUtil.toJString(but.tip,0).startsWith("Method to display/shade objects in the 3D View")) {
//	if (but.icon == BIFIconID.ICON_BBOX) {
//	if (but.x1 == 346.0f) {
//		System.out.println("viewport_shade draw: <"+StringUtil.toJString(but.drawstr,0)+">, icon: "+but.icon+", posx: "+but.x1+", descr: "+StringUtil.toJString(but.tip,0));
//	}
//	else {
	
//	UIStyle.uiStyleFontDraw(fstyle, rect, but.drawstr,but.ofs);
	UIStyle.uiStyleFontDrawExt(fstyle, rect, but.drawstr,but.ofs, font_xofs, font_yofs);
//	if (StringUtil.toJString(but.drawstr,but.ofs).startsWith("Bounding Box Center")) {
//		System.out.println("viewport_shade draw uiStyleFontDrawExt: <"+StringUtil.toJString(but.drawstr,0)+">, icon: "+but.icon+", posx: "+but.x1+", descr: "+StringUtil.toJString(but.tip,0));
//	}
//	}
	
//	if(but.menu_key != '\0') {
//		char fixedbuf[128];
//		char *str;
//
//		BLI_strncpy(fixedbuf, but->drawstr + but->ofs, sizeof(fixedbuf));
//
//		str= strchr(fixedbuf, but->menu_key-32); /* upper case */
//		if(str==NULL)
//			str= strchr(fixedbuf, but->menu_key);
//
//		if(str) {
//			int ul_index= -1;
//			float ul_advance;
//
//			ul_index= (int)(str - fixedbuf);
//
//			if (fstyle->kerning == 1) {
//				BLF_enable(fstyle->uifont_id, BLF_KERNING_DEFAULT);
//			}
//
//			fixedbuf[ul_index]= '\0';
//			ul_advance= BLF_width(fstyle->uifont_id, fixedbuf);
//
//			BLF_position(fstyle->uifont_id, rect->xmin+font_xofs + ul_advance, rect->ymin+font_yofs, 0.0f);
//			BLF_draw(fstyle->uifont_id, "_", 2);
//
//			if (fstyle->kerning == 1) {
//				BLF_disable(fstyle->uifont_id, BLF_KERNING_DEFAULT);
//			}
//		}
//	}

	/* part text right aligned */
	if(cpoin!=-1) {
		fstyle.align= UserDefTypes.UI_STYLE_TEXT_RIGHT;
		rect.xmax-=5;
		UIStyle.uiStyleFontDraw(fstyle, rect, but.drawstr,cpoin+1);
		but.drawstr[cpoin]= '|';
	}
}

/* draws text and icons for buttons */
public static uiWidgetType.Text widget_draw_text_icon = new uiWidgetType.Text() {
public void run(GL2 gl, uiFontStyle fstyle, uiWidgetColors wcol, uiBut but, rcti rect)
{

	if(but==null) return;

	/* clip but->drawstr to fit in available space */
	if (but.editstr!=null && but.pos >= 0) {
		ui_text_leftclip(fstyle, but, rect);
	}
	else if ((but.type == UI.NUM || but.type == UI.NUMABS || but.type == UI.NUMSLI || but.type == UI.SLI)) {
		ui_text_label_rightclip(fstyle, but, rect);
	}
	else if ((but.type == UI.TEX || but.type == UI.SEARCH_MENU)) {
		ui_text_leftclip(fstyle, but, rect);
	}
	else if ((but.block.flag & UI.UI_BLOCK_LOOP)!=0 && (but.type == UI.BUT)) {
		ui_text_leftclip(fstyle, but, rect);
	}
	else but.ofs= 0;

	/* check for button text label */
	if (but.type == UI.ICONTEXTROW) {
		widget_draw_icon(gl, but, but.icon.ordinal()+but.iconadd, 1.0f, rect);
	}
	else {

		if(but.type==UI.BUT_TOGDUAL) {
//			int dualset= 0;
//			if(but.pointype==UI.SHO)
//				dualset= BTST( *(((short *)but.poin)+1), but.bitnr);
//			else if(but.pointype==UI.INT)
//				dualset= BTST( *(((int *)but.poin)+1), but.bitnr);
//
//			widget_draw_icon(gl, but, BIFIconID.ICON_DOT, dualset!=0?0:-100, rect);
		}

		/* If there's an icon too (made with uiDefIconTextBut) then draw the icon
		and offset the text label to accomodate it */

		if ((but.flag & UI.UI_HAS_ICON)!=0) {
			widget_draw_icon(gl, but, but.icon.ordinal()+but.iconadd, 1.0f, rect);

			rect.xmin += UIIcons.UI_icon_get_width(but.icon.ordinal()+but.iconadd);

			if(but.editstr!=null || (but.flag & UI.UI_TEXT_LEFT)!=0)
				rect.xmin += 5;
		}
		else if((but.flag & UI.UI_TEXT_LEFT)!=0)
			rect.xmin += 5;

		/* always draw text for textbutton cursor */
		widget_draw_text(gl, fstyle, wcol, but, rect);

	}
}};



///* *********************** widget types ************************************* */
//
//
///*   uiWidgetStateColors
// char inner_anim[4];
// char inner_anim_sel[4];
// char inner_key[4];
// char inner_key_sel[4];
// char inner_driven[4];
// char inner_driven_sel[4];
// float blend;
//
//*/
//
//static struct uiWidgetStateColors wcol_state= {
//	{115, 190, 76, 255},
//	{90, 166, 51, 255},
//	{240, 235, 100, 255},
//	{215, 211, 75, 255},
//	{180, 0, 255, 255},
//	{153, 0, 230, 255},
//	0.5f, 0.0f
//};
//
///*  uiWidgetColors
// float outline[3];
// float inner[4];
// float inner_sel[4];
// float item[3];
// float text[3];
// float text_sel[3];
//
// short shaded;
// float shadetop, shadedown;
//*/
//
//static struct uiWidgetColors wcol_num= {
//	{25, 25, 25, 255},
//	{180, 180, 180, 255},
//	{153, 153, 153, 255},
//	{90, 90, 90, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	-20, 0
//};
//
//static struct uiWidgetColors wcol_numslider= {
//	{25, 25, 25, 255},
//	{180, 180, 180, 255},
//	{153, 153, 153, 255},
//	{128, 128, 128, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	-20, 0
//};
//
//static struct uiWidgetColors wcol_text= {
//	{25, 25, 25, 255},
//	{153, 153, 153, 255},
//	{153, 153, 153, 255},
//	{90, 90, 90, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	0, 25
//};
//
//static struct uiWidgetColors wcol_option= {
//	{0, 0, 0, 255},
//	{70, 70, 70, 255},
//	{70, 70, 70, 255},
//	{255, 255, 255, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	15, -15
//};
//
///* button that shows popup */
//static struct uiWidgetColors wcol_menu= {
//	{0, 0, 0, 255},
//	{70, 70, 70, 255},
//	{70, 70, 70, 255},
//	{255, 255, 255, 255},
//
//	{255, 255, 255, 255},
//	{204, 204, 204, 255},
//
//	1,
//	15, -15
//};
//
///* button that starts pulldown */
//static struct uiWidgetColors wcol_pulldown= {
//	{0, 0, 0, 255},
//	{63, 63, 63, 255},
//	{86, 128, 194, 255},
//	{255, 255, 255, 255},
//
//	{0, 0, 0, 255},
//	{0, 0, 0, 255},
//
//	0,
//	25, -20
//};
//
///* button inside menu */
//static struct uiWidgetColors wcol_menu_item= {
//	{0, 0, 0, 255},
//	{0, 0, 0, 0},
//	{86, 128, 194, 255},
//	{255, 255, 255, 255},
//
//	{255, 255, 255, 255},
//	{0, 0, 0, 255},
//
//	0,
//	38, 0
//};
//
///* backdrop menu + title text color */
//static struct uiWidgetColors wcol_menu_back= {
//	{0, 0, 0, 255},
//	{25, 25, 25, 230},
//	{45, 45, 45, 230},
//	{100, 100, 100, 255},
//
//	{160, 160, 160, 255},
//	{255, 255, 255, 255},
//
//	0,
//	25, -20
//};
//
//
//static struct uiWidgetColors wcol_radio= {
//	{0, 0, 0, 255},
//	{70, 70, 70, 255},
//	{86, 128, 194, 255},
//	{255, 255, 255, 255},
//
//	{255, 255, 255, 255},
//	{0, 0, 0, 255},
//
//	1,
//	15, -15
//};
//
//static struct uiWidgetColors wcol_regular= {
//	{25, 25, 25, 255},
//	{153, 153, 153, 255},
//	{100, 100, 100, 255},
//	{25, 25, 25, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	0,
//	0, 0
//};
//
//static struct uiWidgetColors wcol_tool= {
//	{25, 25, 25, 255},
//	{153, 153, 153, 255},
//	{100, 100, 100, 255},
//	{25, 25, 25, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	25, -25
//};
//
//static struct uiWidgetColors wcol_box= {
//	{25, 25, 25, 255},
//	{128, 128, 128, 255},
//	{100, 100, 100, 255},
//	{25, 25, 25, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	0,
//	0, 0
//};
//
//static struct uiWidgetColors wcol_toggle= {
//	{25, 25, 25, 255},
//	{153, 153, 153, 255},
//	{100, 100, 100, 255},
//	{25, 25, 25, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	0,
//	0, 0
//};
//
//static struct uiWidgetColors wcol_scroll= {
//	{50, 50, 50, 180},
//	{80, 80, 80, 180},
//	{100, 100, 100, 180},
//	{128, 128, 128, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	1,
//	5, -5
//};
//
//static struct uiWidgetColors wcol_list_item= {
//	{0, 0, 0, 255},
//	{0, 0, 0, 0},
//	{86, 128, 194, 255},
//	{0, 0, 0, 255},
//
//	{0, 0, 0, 255},
//	{0, 0, 0, 255},
//
//	0,
//	0, 0
//};
//
///* free wcol struct to play with */
//static struct uiWidgetColors wcol_tmp= {
//	{0, 0, 0, 255},
//	{128, 128, 128, 255},
//	{100, 100, 100, 255},
//	{25, 25, 25, 255},
//
//	{0, 0, 0, 255},
//	{255, 255, 255, 255},
//
//	0,
//	0, 0
//};
//
//
///* called for theme init (new theme) and versions */
//void ui_widget_color_init(ThemeUI *tui)
//{
//	tui.wcol_regular= wcol_regular;
//	tui.wcol_tool= wcol_tool;
//	tui.wcol_text= wcol_text;
//	tui.wcol_radio= wcol_radio;
//	tui.wcol_option= wcol_option;
//	tui.wcol_toggle= wcol_toggle;
//	tui.wcol_num= wcol_num;
//	tui.wcol_numslider= wcol_numslider;
//	tui.wcol_menu= wcol_menu;
//	tui.wcol_pulldown= wcol_pulldown;
//	tui.wcol_menu_back= wcol_menu_back;
//	tui.wcol_menu_item= wcol_menu_item;
//	tui.wcol_box= wcol_box;
//	tui.wcol_scroll= wcol_scroll;
//	tui.wcol_list_item= wcol_list_item;
//
//	tui.wcol_state= wcol_state;
//}

/* ************ button callbacks, state ***************** */

static void widget_state_blend(byte[] cp, byte[] cpstate, float fac)
{
	if(fac != 0.0f) {
		cp[0]= (byte)((1.0f-fac)*(cp[0]&0xFF) + fac*(cpstate[0]&0xFF));
		cp[1]= (byte)((1.0f-fac)*(cp[1]&0xFF) + fac*(cpstate[1]&0xFF));
		cp[2]= (byte)((1.0f-fac)*(cp[2]&0xFF) + fac*(cpstate[2]&0xFF));
	}
}

/* copy colors from theme, and set changes in it based on state */
public static uiWidgetType.State widget_state = new uiWidgetType.State() {
public void run(uiWidgetType wt, int state)
{
	uiWidgetStateColors wcol_state= wt.wcol_state;

    wt.wcolSet(wt.wcol_theme);

	if((state & UI.UI_SELECT)!=0) {
		UtilDefines.QUATCOPY(wt.wcol.inner, wt.wcol.inner_sel);

		if((state & UI.UI_BUT_ANIMATED_KEY)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_key_sel, wcol_state.blend);
		else if((state & UI.UI_BUT_ANIMATED)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_anim_sel, wcol_state.blend);
		else if((state & UI.UI_BUT_DRIVEN)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_driven_sel, wcol_state.blend);

		UtilDefines.VECCOPY(wt.wcol.text, wt.wcol.text_sel);

//		if(wt.wcol.shaded!=0 && wt.wcol.shadetop>wt.wcol.shadedown) {
		if((state & UI.UI_SELECT)!=0) {
//			SWAP(short, wt.wcol.shadetop, wt.wcol.shadedown);
            short sw_ap; sw_ap=wt.wcol.shadetop; wt.wcol.shadetop=wt.wcol.shadedown; wt.wcol.shadedown=sw_ap;
		}
	}
	else {
		if((state & UI.UI_BUT_ANIMATED_KEY)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_key, wcol_state.blend);
		else if((state & UI.UI_BUT_ANIMATED)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_anim, wcol_state.blend);
		else if((state & UI.UI_BUT_DRIVEN)!=0)
			widget_state_blend(wt.wcol.inner, wcol_state.inner_driven, wcol_state.blend);

		if((state & UI.UI_ACTIVE)!=0) { /* mouse over? */
			wt.wcol.inner[0]= (byte)((wt.wcol.inner[0]&0xFF)>=240? 255 : (wt.wcol.inner[0]&0xFF)+15);
			wt.wcol.inner[1]= (byte)((wt.wcol.inner[1]&0xFF)>=240? 255 : (wt.wcol.inner[1]&0xFF)+15);
			wt.wcol.inner[2]= (byte)((wt.wcol.inner[2]&0xFF)>=240? 255 : (wt.wcol.inner[2]&0xFF)+15);
		}
	}
	
	if((state & UI.UI_BUT_REDALERT)!=0) {
		byte[] red= {(byte)255, 0, 0, 0};
		widget_state_blend(wt.wcol.inner, red, 0.4f);
	}
}};

///* sliders use special hack which sets 'item' as inner when drawing filling */
//static void widget_state_numslider(uiWidgetType *wt, int state)
//{
//	uiWidgetStateColors *wcol_state= wt->wcol_state;
//	float blend= wcol_state->blend - 0.2f; // XXX special tweak to make sure that bar will still be visible
//
//	/* call this for option button */
//	widget_state(wt, state);
//	
//	/* now, set the inner-part so that it reflects state settings too */
//	// TODO: maybe we should have separate settings for the blending colors used for this case?
//	if(state & UI_SELECT) {
//		
//		if(state & UI_BUT_ANIMATED_KEY)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_key_sel, blend);
//		else if(state & UI_BUT_ANIMATED)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_anim_sel, blend);
//		else if(state & UI_BUT_DRIVEN)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_driven_sel, blend);
//		
//		if(state & UI_SELECT)
//			SWAP(short, wt->wcol.shadetop, wt->wcol.shadedown);
//	}
//	else {
//		if(state & UI_BUT_ANIMATED_KEY)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_key, blend);
//		else if(state & UI_BUT_ANIMATED)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_anim, blend);
//		else if(state & UI_BUT_DRIVEN)
//			widget_state_blend(wt->wcol.item, wcol_state->inner_driven, blend);
//	}
//}

/* labels use theme colors for text */
public static uiWidgetType.State widget_state_label = new uiWidgetType.State() {
public void run(uiWidgetType wt, int state)
{
	/* call this for option button */
	widget_state.run(wt, state);

	if((state & UI.UI_SELECT)!=0)
		Resources.UI_GetThemeColor4ubv(Resources.TH_TEXT_HI, wt.wcol.text);
	else
		Resources.UI_GetThemeColor4ubv(Resources.TH_TEXT, wt.wcol.text);
}};

public static uiWidgetType.State widget_state_nothing = new uiWidgetType.State() {
public void run(uiWidgetType wt, int state)
{
        wt.wcolSet(wt.wcol_theme);
}};

/* special case, button that calls pulldown */
public static uiWidgetType.State widget_state_pulldown = new uiWidgetType.State() {
public void run(uiWidgetType wt, int state)
{
    wt.wcolSet(wt.wcol_theme);

	UtilDefines.QUATCOPY(wt.wcol.inner, wt.wcol.inner_sel);
	UtilDefines.VECCOPY(wt.wcol.outline, wt.wcol.inner);

	if((state & UI.UI_ACTIVE)!=0)
		UtilDefines.VECCOPY(wt.wcol.text, wt.wcol.text_sel);
}};

/* special case, menu items */
public static uiWidgetType.State widget_state_menu_item = new uiWidgetType.State() {
public void run(uiWidgetType wt, int state)
{
    wt.wcolSet(wt.wcol_theme);

	if((state & (UI.UI_BUT_DISABLED|UI.UI_BUT_INACTIVE))!=0) {
		wt.wcol.text[0]= (byte)(0.5f*((wt.wcol.text[0]&0xFF)+(wt.wcol.text_sel[0]&0xFF)));
		wt.wcol.text[1]= (byte)(0.5f*((wt.wcol.text[1]&0xFF)+(wt.wcol.text_sel[1]&0xFF)));
		wt.wcol.text[2]= (byte)(0.5f*((wt.wcol.text[2]&0xFF)+(wt.wcol.text_sel[2]&0xFF)));
	}
	else if((state & UI.UI_ACTIVE)!=0) {
		UtilDefines.QUATCOPY(wt.wcol.inner, wt.wcol.inner_sel);
		UtilDefines.VECCOPY(wt.wcol.text, wt.wcol.text_sel);

		wt.wcol.shaded= 1;
	}
}};


/* ************ menu backdrop ************************* */

/* outside of rect, rad to left/bottom/right */
static void widget_softshadow(GL2 gl, rcti rect, int roundboxalign, float radin, float radout)
{
	uiWidgetBase wtb = new uiWidgetBase();
    rcti rect1 = rect.copy();
	float alpha, alphastep;
	int step, tot, a;

	/* prevent tooltips to not show round shadow */
	if( 2.0f*radout > 0.2f*(rect1.ymax-rect1.ymin) )
		rect1.ymax -= 0.2f*(rect1.ymax-rect1.ymin);
	else
		rect1.ymax -= 2.0f*radout;

	/* inner part */
	tot= round_box_shadow_edges(wtb.inner_v, rect1, radin, roundboxalign & 12, 0.0f);

	/* inverse linear shadow alpha */
	alpha= 0.15f;
	alphastep= 0.67f;

	for(step= 1; step<=radout; step++, alpha*=alphastep) {
		round_box_shadow_edges(wtb.outer_v, rect1, radin, 15, (float)step);

		gl.glColor4f(0.0f, 0.0f, 0.0f, alpha);

		gl.glBegin(GL2.GL_QUAD_STRIP);
		for(a=0; a<tot; a++) {
			gl.glVertex2fv(wtb.outer_v[a],0);
			gl.glVertex2fv(wtb.inner_v[a],0);
		}
		gl.glEnd();
	}

}

public static uiWidgetType.Draw widget_menu_back = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int flag, int direction)
{
	uiWidgetBase wtb = new uiWidgetBase();
	int roundboxalign= 15;

	widget_init(wtb);

	/* menu is 2nd level or deeper */
	if ((flag & UI.UI_BLOCK_POPUP)!=0) {
		//rect.ymin -= 4.0;
		//rect.ymax += 4.0;
	}
	else if (direction == UI.UI_DOWN) {
		roundboxalign= 12;
		rect.ymin -= 4.0;
	}
	else if (direction == UI.UI_TOP) {
		roundboxalign= 3;
		rect.ymax += 4.0;
	}

	gl.glEnable(GL2.GL_BLEND);
	widget_softshadow(gl, rect, roundboxalign, 5.0f, 8.0f);

	round_box_edges(wtb, roundboxalign, rect, 5.0f);
	wtb.emboss= 0;
	widgetbase_draw(gl,wtb, wcol);

	gl.glDisable(GL2.GL_BLEND);
}};


//static void ui_hsv_cursor(float x, float y)
//{
//
//	glPushMatrix();
//	glTranslatef(x, y, 0.0f);
//
//	glColor3f(1.0f, 1.0f, 1.0f);
//	glutil_draw_filled_arc(0.0f, M_PI*2.0, 3.0f, 8);
//
//	glEnable(GL_BLEND);
//	glEnable(GL_LINE_SMOOTH );
//	glColor3f(0.0f, 0.0f, 0.0f);
//	glutil_draw_lined_arc(0.0f, M_PI*2.0, 3.0f, 12);
//	glDisable(GL_BLEND);
//	glDisable(GL_LINE_SMOOTH );
//
//	glPopMatrix();
//
//}
//
//void ui_hsvcircle_vals_from_pos(float *valrad, float *valdist, rcti *rect, float mx, float my)
//{
//	/* duplication of code... well, simple is better now */
//	float centx= (float)(rect.xmin + rect.xmax)/2;
//	float centy= (float)(rect.ymin + rect.ymax)/2;
//	float radius, dist;
//
//	if( rect.xmax-rect.xmin > rect.ymax-rect.ymin )
//		radius= (float)(rect.ymax - rect.ymin)/2;
//	else
//		radius= (float)(rect.xmax - rect.xmin)/2;
//
//	mx-= centx;
//	my-= centy;
//	dist= sqrt( mx*mx + my*my);
//	if(dist < radius)
//		*valdist= dist/radius;
//	else
//		*valdist= 1.0f;
//
//	*valrad= atan2(mx, my)/(2.0f*M_PI) + 0.5f;
//}
//
//void ui_draw_but_HSVCIRCLE(uiBut *but, rcti *rect)
//{
//	/* gouraud triangle fan */
//	float radstep, ang= 0.0f;
//	float centx, centy, radius;
//	float hsv[3], col[3], colcent[3];
//	int a, tot= 32;
//
//	radstep= 2.0f*M_PI/(float)tot;
//	centx= (float)(rect.xmin + rect.xmax)/2;
//	centy= (float)(rect.ymin + rect.ymax)/2;
//
//	if( rect.xmax-rect.xmin > rect.ymax-rect.ymin )
//		radius= (float)(rect.ymax - rect.ymin)/2;
//	else
//		radius= (float)(rect.xmax - rect.xmin)/2;
//
//	/* color */
//	VECCOPY(hsv, but.hsv);
//	hsv[0]= hsv[1]= 0.0f;
//	hsv_to_rgb(hsv[0], hsv[1], hsv[2], colcent, colcent+1, colcent+2);
//
//	glShadeModel(GL_SMOOTH);
//
//	glBegin(GL_TRIANGLE_FAN);
//	glColor3fv(colcent);
//	glVertex2f( centx, centy);
//
//	for(a=0; a<=tot; a++, ang+=radstep) {
//		float si= sin(ang);
//		float co= cos(ang);
//
//		ui_hsvcircle_vals_from_pos(hsv, hsv+1, rect, centx + co*radius, centy + si*radius);
//		hsv_to_rgb(hsv[0], hsv[1], hsv[2], col, col+1, col+2);
//		glColor3fv(col);
//		glVertex2f( centx + co*radius, centy + si*radius);
//	}
//	glEnd();
//
//	glShadeModel(GL_FLAT);
//
//	/* fully rounded outline */
//	glPushMatrix();
//	glTranslatef(centx, centy, 0.0f);
//	glEnable(GL_BLEND);
//	glEnable(GL_LINE_SMOOTH );
//	glColor3f(0.0f, 0.0f, 0.0f);
//	glutil_draw_lined_arc(0.0f, M_PI*2.0, radius, tot);
//	glDisable(GL_BLEND);
//	glDisable(GL_LINE_SMOOTH );
//	glPopMatrix();
//
//	/* cursor */
//	ang= 2.0f*M_PI*but.hsv[0] + 0.5f*M_PI;
//	radius= but.hsv[1]*radius;
//	ui_hsv_cursor(centx + cos(-ang)*radius, centy + sin(-ang)*radius);
//
//}
//
///* ************ custom buttons, old stuff ************** */
//
///* draws in resolution of 20x4 colors */
//static void ui_draw_but_HSVCUBE(uiBut *but, rcti *rect)
//{
//	int a;
//	float h,s,v;
//	float dx, dy, sx1, sx2, sy, x=0.0f, y=0.0f;
//	float col0[4][3];	// left half, rect bottom to top
//	float col1[4][3];	// right half, rect bottom to top
//
//	h= but.hsv[0];
//	s= but.hsv[1];
//	v= but.hsv[2];
//
//	/* draw series of gouraud rects */
//	glShadeModel(GL_SMOOTH);
//
//	if(but.a1==0) {	// H and V vary
//		hsv_to_rgb(0.0, s, 0.0,   &col1[0][0], &col1[0][1], &col1[0][2]);
//		hsv_to_rgb(0.0, s, 0.333, &col1[1][0], &col1[1][1], &col1[1][2]);
//		hsv_to_rgb(0.0, s, 0.666, &col1[2][0], &col1[2][1], &col1[2][2]);
//		hsv_to_rgb(0.0, s, 1.0,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		x= h; y= v;
//	}
//	else if(but.a1==1) {	// H and S vary
//		hsv_to_rgb(0.0, 0.0, v,   &col1[0][0], &col1[0][1], &col1[0][2]);
//		hsv_to_rgb(0.0, 0.333, v, &col1[1][0], &col1[1][1], &col1[1][2]);
//		hsv_to_rgb(0.0, 0.666, v, &col1[2][0], &col1[2][1], &col1[2][2]);
//		hsv_to_rgb(0.0, 1.0, v,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		x= h; y= s;
//	}
//	else if(but.a1==2) {	// S and V vary
//		hsv_to_rgb(h, 0.0, 0.0,   &col1[0][0], &col1[0][1], &col1[0][2]);
//		hsv_to_rgb(h, 0.333, 0.0, &col1[1][0], &col1[1][1], &col1[1][2]);
//		hsv_to_rgb(h, 0.666, 0.0, &col1[2][0], &col1[2][1], &col1[2][2]);
//		hsv_to_rgb(h, 1.0, 0.0,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		x= v; y= s;
//	}
//	else if(but.a1==3) {		// only hue slider
//		hsv_to_rgb(0.0, 1.0, 1.0,   &col1[0][0], &col1[0][1], &col1[0][2]);
//		VECCOPY(col1[1], col1[0]);
//		VECCOPY(col1[2], col1[0]);
//		VECCOPY(col1[3], col1[0]);
//		x= h; y= 0.5;
//	}
//
//	for(dx=0.0; dx<1.0; dx+= 0.05) {
//		// previous color
//		VECCOPY(col0[0], col1[0]);
//		VECCOPY(col0[1], col1[1]);
//		VECCOPY(col0[2], col1[2]);
//		VECCOPY(col0[3], col1[3]);
//
//		// new color
//		if(but.a1==0) {	// H and V vary
//			hsv_to_rgb(dx, s, 0.0,   &col1[0][0], &col1[0][1], &col1[0][2]);
//			hsv_to_rgb(dx, s, 0.333, &col1[1][0], &col1[1][1], &col1[1][2]);
//			hsv_to_rgb(dx, s, 0.666, &col1[2][0], &col1[2][1], &col1[2][2]);
//			hsv_to_rgb(dx, s, 1.0,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		}
//		else if(but.a1==1) {	// H and S vary
//			hsv_to_rgb(dx, 0.0, v,   &col1[0][0], &col1[0][1], &col1[0][2]);
//			hsv_to_rgb(dx, 0.333, v, &col1[1][0], &col1[1][1], &col1[1][2]);
//			hsv_to_rgb(dx, 0.666, v, &col1[2][0], &col1[2][1], &col1[2][2]);
//			hsv_to_rgb(dx, 1.0, v,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		}
//		else if(but.a1==2) {	// S and V vary
//			hsv_to_rgb(h, 0.0, dx,   &col1[0][0], &col1[0][1], &col1[0][2]);
//			hsv_to_rgb(h, 0.333, dx, &col1[1][0], &col1[1][1], &col1[1][2]);
//			hsv_to_rgb(h, 0.666, dx, &col1[2][0], &col1[2][1], &col1[2][2]);
//			hsv_to_rgb(h, 1.0, dx,   &col1[3][0], &col1[3][1], &col1[3][2]);
//		}
//		else if(but.a1==3) {	// only H
//			hsv_to_rgb(dx, 1.0, 1.0,   &col1[0][0], &col1[0][1], &col1[0][2]);
//			VECCOPY(col1[1], col1[0]);
//			VECCOPY(col1[2], col1[0]);
//			VECCOPY(col1[3], col1[0]);
//		}
//
//		// rect
//		sx1= rect.xmin + dx*(rect.xmax-rect.xmin);
//		sx2= rect.xmin + (dx+0.05)*(rect.xmax-rect.xmin);
//		sy= rect.ymin;
//		dy= (rect.ymax-rect.ymin)/3.0;
//
//		glBegin(GL_QUADS);
//		for(a=0; a<3; a++, sy+=dy) {
//			glColor3fv(col0[a]);
//			glVertex2f(sx1, sy);
//
//			glColor3fv(col1[a]);
//			glVertex2f(sx2, sy);
//
//			glColor3fv(col1[a+1]);
//			glVertex2f(sx2, sy+dy);
//
//			glColor3fv(col0[a+1]);
//			glVertex2f(sx1, sy+dy);
//		}
//		glEnd();
//	}
//
//	glShadeModel(GL_FLAT);
//
//	/* cursor */
//	x= rect.xmin + x*(rect.xmax-rect.xmin);
//	y= rect.ymin + y*(rect.ymax-rect.ymin);
//	CLAMP(x, rect.xmin+3.0, rect.xmax-3.0);
//	CLAMP(y, rect.ymin+3.0, rect.ymax-3.0);
//
//	ui_hsv_cursor(x, y);
//
//	/* outline */
//	glColor3ub(0,  0,  0);
//	fdrawbox((rect.xmin), (rect.ymin), (rect.xmax), (rect.ymax));
//}
//
///* vertical 'value' slider, using new widget code */
//static void ui_draw_but_HSV_v(uiBut *but, rcti *rect)
//{
//	uiWidgetBase wtb;
//	float rad= 0.5f*(rect.xmax - rect.xmin);
//	float x, y;
//
//	widget_init(&wtb);
//
//	/* fully rounded */
//	round_box_edges(&wtb, 15, rect, rad);
//
//	/* setup temp colors */
//	wcol_tmp.outline[0]= wcol_tmp.outline[1]= wcol_tmp.outline[2]= 0;
//	wcol_tmp.inner[0]= wcol_tmp.inner[1]= wcol_tmp.inner[2]= 128;
//	wcol_tmp.shadetop= 127;
//	wcol_tmp.shadedown= -128;
//	wcol_tmp.shaded= 1;
//
//	widgetbase_draw(&wtb, &wcol_tmp);
//
//	/* cursor */
//	x= rect.xmin + 0.5f*(rect.xmax-rect.xmin);
//	y= rect.ymin + but.hsv[2]*(rect.ymax-rect.ymin);
//	CLAMP(y, rect.ymin+3.0, rect.ymax-3.0);
//
//	ui_hsv_cursor(x, y);
//
//}

/* ************ separator, for menus etc ***************** */
static void ui_draw_separator(GL2 gl, rcti rect,  uiWidgetColors wcol)
{
	int y = rect.ymin + (rect.ymax - rect.ymin)/2 - 1;
	byte[] col=new byte[4];
	
	col[0] = wcol.text[0];
	col[1] = wcol.text[1];
	col[2] = wcol.text[2];
	col[3] = 7;
	
	gl.glEnable(GL2.GL_BLEND);
	gl.glColor4ubv(col,0);
	GlUtil.sdrawline(gl, rect.xmin, y, rect.xmax, y);
	gl.glDisable(GL2.GL_BLEND);
}

/* ************ button callbacks, draw ***************** */

public static uiWidgetType.Draw widget_numbut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();
	float rad= 0.5f*(rect.ymax - rect.ymin);
	float textofs = rad*0.75f;
	
	if((state & UI.UI_SELECT)!=0) {
//		SWAP(short, wcol->shadetop, wcol->shadedown);
		short sw_ap; sw_ap=wt.wcol.shadetop; wt.wcol.shadetop=wt.wcol.shadedown; wt.wcol.shadedown=sw_ap;
	}

	widget_init(wtb);

	/* fully rounded */
	round_box_edges(wtb, roundboxalign, rect, rad);

	/* decoration */
	if((state & UI.UI_TEXTINPUT)==0) {
		widget_num_tria(wtb.tria1, rect, 0.6f, 'l');
		widget_num_tria(wtb.tria2, rect, 0.6f, 'r');
	}
	
	widgetbase_draw(gl, wtb, wcol);

	/* text space */
//	if((state & UI.UI_TEXTINPUT)==0) {
//		rect.xmin += (rect.ymax-rect.ymin);
//		rect.xmax -= (rect.ymax-rect.ymin);
//	}
//	else {
//		textoffs= (int)rad;
		rect.xmin += textofs;
		rect.xmax -= textofs;
//	}
}};


//static int ui_link_bezier_points(rcti *rect, float coord_array[][2], int resol)
//{
//	float dist, vec[4][2];
//
//	vec[0][0]= rect.xmin;
//	vec[0][1]= rect.ymin;
//	vec[3][0]= rect.xmax;
//	vec[3][1]= rect.ymax;
//
//	dist= 0.5f*ABS(vec[0][0] - vec[3][0]);
//
//	vec[1][0]= vec[0][0]+dist;
//	vec[1][1]= vec[0][1];
//
//	vec[2][0]= vec[3][0]-dist;
//	vec[2][1]= vec[3][1];
//
//	forward_diff_bezier(vec[0][0], vec[1][0], vec[2][0], vec[3][0], coord_array[0], resol, 2);
//	forward_diff_bezier(vec[0][1], vec[1][1], vec[2][1], vec[3][1], coord_array[0]+1, resol, 2);
//
//	return 1;
//}
//
//#define LINK_RESOL	24
//void ui_draw_link_bezier(rcti *rect)
//{
//	float coord_array[LINK_RESOL+1][2];
//
//	if(ui_link_bezier_points(rect, coord_array, LINK_RESOL)) {
//		float dist;
//		int i;
//
//		/* we can reuse the dist variable here to increment the GL2 curve eval amount*/
//		dist = 1.0f/(float)LINK_RESOL;
//
//		glEnable(GL_BLEND);
//		glEnable(GL_LINE_SMOOTH);
//
//		glBegin(GL_LINE_STRIP);
//		for(i=0; i<=LINK_RESOL; i++) {
//			glVertex2fv(coord_array[i]);
//		}
//		glEnd();
//
//		glDisable(GL_BLEND);
//		glDisable(GL_LINE_SMOOTH);
//
//	}
//}

/* function in use for buttons and for view2d sliders */
public static void uiWidgetScrollDraw(GL2 gl, uiWidgetColors wcol, rcti rect, rcti slider, int state)
{
	uiWidgetBase wtb = new uiWidgetBase();
	float rad;
	boolean horizontal;
	short outline=0;

	widget_init(wtb);

	/* determine horizontal/vertical */
	horizontal= (rect.xmax - rect.xmin > rect.ymax - rect.ymin);

	if(horizontal)
		rad= 0.5f*(rect.ymax - rect.ymin);
	else
		rad= 0.5f*(rect.xmax - rect.xmin);

	wtb.shadedir= (short)((horizontal)? 1: 0);

	/* draw back part, colors swapped and shading inverted */
	if(horizontal) {
//		SWAP(short, wcol.shadetop, wcol.shadedown);
        short sw_ap; sw_ap=wcol.shadetop; wcol.shadetop=wcol.shadedown; wcol.shadedown=sw_ap;
    }

	round_box_edges(wtb, 15, rect, rad);
	widgetbase_draw(gl, wtb, wcol);

	/* slider */
	if(slider.xmax-slider.xmin<2 || slider.ymax-slider.ymin<2);
	else {

//		SWAP(short, wcol.shadetop, wcol.shadedown);
        short sw_ap; sw_ap=wcol.shadetop; wcol.shadetop=wcol.shadedown; wcol.shadedown=sw_ap;

		UtilDefines.QUATCOPY(wcol.inner, wcol.item);

		if(wcol.shadetop>wcol.shadedown)
			wcol.shadetop+= 20;	/* XXX violates themes... */
		else wcol.shadedown+= 20;

		if((state & UI.UI_SCROLL_PRESSED)!=0) {
			wcol.inner[0]= (byte)((wcol.inner[0]&0xFF)>=250? 255 : (wcol.inner[0]&0xFF)+5);
			wcol.inner[1]= (byte)((wcol.inner[1]&0xFF)>=250? 255 : (wcol.inner[1]&0xFF)+5);
			wcol.inner[2]= (byte)((wcol.inner[2]&0xFF)>=250? 255 : (wcol.inner[2]&0xFF)+5);
		}

		/* draw */
		wtb.emboss= 0; /* only emboss once */
		
		/* exception for progress bar */
		if ((state & UI.UI_SCROLL_NO_OUTLINE)!=0) {	
//			SWAP(short, outline, wtb.outline);
			sw_ap=outline; outline=wtb.outline; wtb.outline=sw_ap;
		}

		round_box_edges(wtb, 15, slider, rad);

		if((state & UI.UI_SCROLL_ARROWS)!=0) {
			if((wcol.item[0]&0xFF) > 48)
                wcol.item[0]= (byte)((wcol.item[0]&0xFF)-48);
			if((wcol.item[1]&0xFF) > 48)
                wcol.item[1]= (byte)((wcol.item[1]&0xFF)-48);
			if((wcol.item[2]&0xFF) > 48)
                wcol.item[2]= (byte)((wcol.item[2]&0xFF)-48);
			wcol.item[3]= (byte)255;

			if(horizontal) {
				widget_num_tria(wtb.tria1, slider, 0.6f, 'l');
				widget_num_tria(wtb.tria2, slider, 0.6f, 'r');
			}
			else {
				widget_num_tria(wtb.tria1, slider, 0.6f, 'b');
				widget_num_tria(wtb.tria2, slider, 0.6f, 't');
			}
		}
		widgetbase_draw(gl, wtb, wcol);
		
		if ((state & UI.UI_SCROLL_NO_OUTLINE)!=0) {
//			SWAP(short, outline, wtb.outline);
			sw_ap=outline; outline=wtb.outline; wtb.outline=sw_ap;
		}
	}
}

//static void widget_scroll(uiBut *but, uiWidgetColors *wcol, rcti *rect, int state, int roundboxalign)
//{
//	rcti rect1;
//	double value;
//	float fac, size, min;
//	int horizontal;
//
//	/* calculate slider part */
//	value= ui_get_but_val(but);
//
//	size= (but.softmax + but.a1 - but.softmin);
//	size= MAX2(size, 2);
//
//	/* position */
//	rect1= *rect;
//
//	/* determine horizontal/vertical */
//	horizontal= (rect.xmax - rect.xmin > rect.ymax - rect.ymin);
//
//	if(horizontal) {
//		fac= (rect.xmax - rect.xmin)/(size);
//		rect1.xmin= rect1.xmin + ceil(fac*(value - but.softmin));
//		rect1.xmax= rect1.xmin + ceil(fac*(but.a1 - but.softmin));
//
//		/* ensure minimium size */
//		min= rect.ymax - rect.ymin;
//
//		if(rect1.xmax - rect1.xmin < min) {
//			rect1.xmax= rect1.xmin + min;
//
//			if(rect1.xmax > rect.xmax) {
//				rect1.xmax= rect.xmax;
//				rect1.xmin= MAX2(rect1.xmax - min, rect.xmin);
//			}
//		}
//	}
//	else {
//		fac= (rect.ymax - rect.ymin)/(size);
//		rect1.ymax= rect1.ymax - ceil(fac*(value - but.softmin));
//		rect1.ymin= rect1.ymax - ceil(fac*(but.a1 - but.softmin));
//
//		/* ensure minimium size */
//		min= rect.xmax - rect.xmin;
//
//		if(rect1.ymax - rect1.ymin < min) {
//			rect1.ymax= rect1.ymin + min;
//
//			if(rect1.ymax > rect.ymax) {
//				rect1.ymax= rect.ymax;
//				rect1.ymin= MAX2(rect1.ymax - min, rect.ymin);
//			}
//		}
//	}
//
//	if(state & UI_SELECT)
//		state= UI_SCROLL_PRESSED;
//	else
//		state= 0;
//	uiWidgetScrollDraw(wcol, rect, &rect1, state);
//}
//
//static void widget_link(uiBut *but, uiWidgetColors *wcol, rcti *rect, int state, int roundboxalign)
//{
//
//	if(but.flag & UI_SELECT) {
//		rcti rectlink;
//
//		UI_ThemeColor(TH_TEXT_HI);
//
//		rectlink.xmin= (rect.xmin+rect.xmax)/2;
//		rectlink.ymin= (rect.ymin+rect.ymax)/2;
//		rectlink.xmax= but.linkto[0];
//		rectlink.ymax= but.linkto[1];
//
//		ui_draw_link_bezier(&rectlink);
//	}
//}

//static void widget_numslider(uiBut *but, uiWidgetColors *wcol, rcti *rect, int state, int roundboxalign)
//{
//	uiWidgetBase wtb = new uiWidgetBase(), wtb1 = new uiWidgetBase();
//	rcti rect1 = new rcti();
//	double value;
//	float offs, fac;
//	int textoffs;
//	byte[] outline = new byte[3];
//
//	widget_init(wtb);
//	widget_init(wtb1);
//
//	/* backdrop first */
//
//	/* fully rounded */
//	offs= 0.5f*(rect.ymax - rect.ymin);
//	textoffs= (int)offs;
//	round_box_edges(wtb, roundboxalign, rect, offs);
//
//	wtb.outline= 0;
//	widgetbase_draw(gl, wtb, wcol);
//
//	/* slider part */
//	UtilDefines.VECCOPY(outline, wcol.outline);
//	UtilDefines.VECCOPY(wcol.outline, wcol.item);
//	UtilDefines.VECCOPY(wcol.inner, wcol.item);
////	SWAP(short, wcol.shadetop, wcol.shadedown);
//        short sw_ap; sw_ap=wcol.shadetop; wcol.shadetop=wcol.shadedown; wcol.shadedown=sw_ap;
//
//	rect1= rect.copy();
//
//	value= ui_get_but_val(but);
//	fac= (value-but.softmin)*(rect1.xmax - rect1.xmin - offs)/(but.softmax - but.softmin);
//
//	/* left part of slider, always rounded */
//	rect1.xmax= rect1.xmin + ceil(offs+1.0f);
//	round_box_edges(&wtb1, roundboxalign & ~6, &rect1, offs);
//	wtb1.outline= 0;
//	widgetbase_draw(&wtb1, wcol);
//
//	/* right part of slider, interpolate roundness */
//	rect1.xmax= rect1.xmin + fac + offs;
//	rect1.xmin+=  floor(offs-1.0f);
//	if(rect1.xmax + offs > rect.xmax)
//		offs*= (rect1.xmax + offs - rect.xmax)/offs;
//	else
//		offs= 0.0f;
//	round_box_edges(&wtb1, roundboxalign & ~9, &rect1, offs);
//
//	widgetbase_draw(&wtb1, wcol);
//	VECCOPY(wcol.outline, outline);
//	SWAP(short, wcol.shadetop, wcol.shadedown);
//
//	/* outline */
//	wtb.outline= 1;
//	wtb.inner= 0;
//	widgetbase_draw(&wtb, wcol);
//
//	/* text space */
//	rect.xmin += textoffs;
//	rect.xmax -= textoffs;
//}

//static void widget_swatch(uiBut *but, uiWidgetColors *wcol, rcti *rect, int state, int roundboxalign)
//{
//	uiWidgetBase wtb;
//	float col[4];
//
//	widget_init(&wtb);
//
//	/* half rounded */
//	round_box_edges(&wtb, roundboxalign, rect, 4.0f);
//
//	ui_get_but_vectorf(but, col);
//	wcol.inner[0]= FTOCHAR(col[0]);
//	wcol.inner[1]= FTOCHAR(col[1]);
//	wcol.inner[2]= FTOCHAR(col[2]);
//
//	widgetbase_draw(&wtb, wcol);
//
//}


public static uiWidgetType.Draw widget_textbut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();
	
	if((state & UI.UI_SELECT)!=0) {
//		SWAP(short, wcol->shadetop, wcol->shadedown);
		short sw_ap; sw_ap=wcol.shadetop; wcol.shadetop=wcol.shadedown; wcol.shadedown=sw_ap;
	}

	widget_init(wtb);

	/* half rounded */
	round_box_edges(wtb, roundboxalign, rect, 4.0f);

	widgetbase_draw(gl, wtb, wcol);

}};

public static uiWidgetType.Draw widget_menubut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();

	widget_init(wtb);

	/* half rounded */
	round_box_edges(wtb, roundboxalign, rect, 4.0f);

	/* decoration */
	widget_menu_trias(wtb.tria1, rect);

	widgetbase_draw(gl, wtb, wcol);

	/* text space */
	rect.xmax -= (rect.ymax-rect.ymin);
}};

//static void widget_menuiconbut(uiWidgetColors *wcol, rcti *rect, int UNUSED(state), int roundboxalign)
//{
//	uiWidgetBase wtb;
//	
//	widget_init(&wtb);
//	
//	/* half rounded */
//	round_box_edges(&wtb, roundboxalign, rect, 4.0f);
//	
//	/* decoration */
//	widgetbase_draw(&wtb, wcol);
//}

public static uiWidgetType.Draw widget_pulldownbut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	if((state & UI.UI_ACTIVE)!=0) {
		uiWidgetBase wtb = new uiWidgetBase();
		float rad= 0.5f*(rect.ymax - rect.ymin); // 4.0f

		widget_init(wtb);

		/* fully rounded */
		round_box_edges(wtb, 15, rect, rad);
		
//		System.out.println("widget_pulldownbut: "+rect.xmin+", "+rect.xmax+", "+rect.ymin+", "+rect.ymax);

		widgetbase_draw(gl, wtb, wcol);
	}
}};

public static uiWidgetType.Draw widget_menu_itembut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();

	widget_init(wtb);

	/* not rounded, no outline */
	wtb.outline= 0;
	round_box_edges(wtb, 0, rect, 0.0f);

	widgetbase_draw(gl, wtb, wcol);
}};

public static uiWidgetType.Draw widget_list_itembut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();

	widget_init(wtb);

	/* rounded, but no outline */
	wtb.outline= 0;
	round_box_edges(wtb, 15, rect, 4.0f);

	widgetbase_draw(gl, wtb, wcol);
}};

public static uiWidgetType.Draw widget_optionbut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();
    rcti recttemp = rect.copy();
	int delta;

	widget_init(wtb);

	/* square */
	recttemp.xmax= recttemp.xmin + (recttemp.ymax-recttemp.ymin);

	/* smaller */
	delta= 1 + (recttemp.ymax-recttemp.ymin)/8;
	recttemp.xmin+= delta;
	recttemp.ymin+= delta;
	recttemp.xmax-= delta;
	recttemp.ymax-= delta;

	/* half rounded */
	round_box_edges(wtb, 15, recttemp, 4.0f);

	/* decoration */
	if((state & UI.UI_SELECT)!=0) {
		widget_check_trias(wtb.tria1, recttemp);
	}

	widgetbase_draw(gl, wtb, wcol);

	/* text space */
	rect.xmin += (rect.ymax-rect.ymin)*0.7 + delta;
}};

public static uiWidgetType.Draw widget_radiobut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();

	widget_init(wtb);

	/* half rounded */
	round_box_edges(wtb, roundboxalign, rect, 4.0f);

	widgetbase_draw(gl, wtb, wcol);

}};

//static void widget_box(uiBut *but, uiWidgetColors *wcol, rcti *rect, int UNUSED(state), int roundboxalign)
//{
//	uiWidgetBase wtb;
//	char old_col[3];
//	
//	widget_init(&wtb);
//	
//	VECCOPY(old_col, wcol->inner);
//	
//	/* abuse but->hsv - if it's non-zero, use this color as the box's background */
//	if (but->col[3]) {
//		wcol->inner[0] = but->col[0];
//		wcol->inner[1] = but->col[1];
//		wcol->inner[2] = but->col[2];
//	}
//	
//	/* half rounded */
//	round_box_edges(&wtb, roundboxalign, rect, 4.0f);
//	
//	widgetbase_draw(&wtb, wcol);
//	
//	/* store the box bg as gl clearcolor, to retrieve later when drawing semi-transparent rects
//	 * over the top to indicate disabled buttons */
//	glClearColor(wcol->inner[0]/255.0, wcol->inner[1]/255.0, wcol->inner[2]/255.0, 1.0);
//	
//	VECCOPY(wcol->inner, old_col);
//}

public static uiWidgetType.Draw widget_but = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();

	widget_init(wtb);

	/* half rounded */
	round_box_edges(wtb, roundboxalign, rect, 4.0f);

	widgetbase_draw(gl, wtb, wcol);

}};

public static uiWidgetType.Draw widget_roundbut = new uiWidgetType.Draw() {
public void run(GL2 gl, uiWidgetColors wcol, rcti rect, int state, int roundboxalign)
{
	uiWidgetBase wtb = new uiWidgetBase();
	float rad= 5.0f; //0.5f*(rect.ymax - rect.ymin);

	widget_init(wtb);

	/* fully rounded */
	round_box_edges(wtb, roundboxalign, rect, rad);

	widgetbase_draw(gl, wtb, wcol);
}};

//static void widget_draw_extra_mask(const bContext *C, uiBut *but, uiWidgetType *wt, rcti *rect)
//{
//	uiWidgetBase wtb;
//	char col[4];
//
//	/* state copy! */
//	wt.wcol= *(wt.wcol_theme);
//
//	widget_init(&wtb);
//
//	if(but.block.drawextra) {
//		/* note: drawextra can change rect +1 or -1, to match round errors of existing previews */
//		but.block.drawextra(C, but.poin, but.block.drawextra_arg, rect);
//
//		/* make mask to draw over image */
//		UI_GetThemeColor3ubv(TH_BACK, col);
//		glColor3ubv((unsigned char*)col);
//
//		round_box__edges(&wtb, 15, rect, 0.0f, 4.0);
//		widgetbase_outline(&wtb);
//	}
//
//	/* outline */
//	round_box_edges(&wtb, 15, rect, 5.0f);
//	wtb.outline= 1;
//	wtb.inner= 0;
//	widgetbase_draw(&wtb, &wt.wcol);
//
//}


static void widget_disabled(GL2 gl, rcti rect)
{
	float[] col=new float[4];

	gl.glEnable(GL2.GL_BLEND);

	/* can't use theme TH_BACK or TH_PANEL... undefined */
	gl.glGetFloatv(GL2.GL_COLOR_CLEAR_VALUE, col,0);
	gl.glColor4f(col[0], col[1], col[2], 0.5f);
	
	/* need -1 and +1 to make it work right for aligned buttons,
	 * but problem may be somewhere else? */
	gl.glRectf(rect.xmin-1, rect.ymin-1, rect.xmax, rect.ymax+1);

	gl.glDisable(GL2.GL_BLEND);
}

static uiWidgetType wt = new uiWidgetType();
static uiWidgetType widget_type(uiWidgetTypeEnum type)
{
	bTheme btheme= (bTheme)U.themes.first;
	
	/* defaults */
	wt.wcol_theme= btheme.tui.wcol_regular;
	wt.wcol_state= btheme.tui.wcol_state;
	wt.state= widget_state;
	wt.draw= widget_but;
	wt.custom= null;
	wt.text= widget_draw_text_icon;
	
	switch(type) {
		case UI_WTYPE_REGULAR:
			break;

		case UI_WTYPE_LABEL:
			wt.draw= null;
			wt.state= widget_state_label;
			break;
			
		case UI_WTYPE_TOGGLE:
			wt.wcol_theme= btheme.tui.wcol_toggle;
			break;
			
		case UI_WTYPE_OPTION:
			wt.wcol_theme= btheme.tui.wcol_option;
			wt.draw= widget_optionbut;
			break;
			
		case UI_WTYPE_RADIO:
			wt.wcol_theme= btheme.tui.wcol_radio;
			wt.draw= widget_radiobut;
			break;

		case UI_WTYPE_NUMBER:
			wt.wcol_theme= btheme.tui.wcol_num;
			wt.draw= widget_numbut;
			break;
			
		case UI_WTYPE_SLIDER:
			wt.wcol_theme= btheme.tui.wcol_numslider;
//			wt.custom= widget_numslider;
//			wt.state= widget_state_numslider;
			break;
			
		case UI_WTYPE_EXEC:
			wt.wcol_theme= btheme.tui.wcol_tool;
			wt.draw= widget_roundbut;
			break;
			
			
			/* strings */
		case UI_WTYPE_NAME:
			wt.wcol_theme= btheme.tui.wcol_text;
			wt.draw= widget_textbut;
			break;
			
		case UI_WTYPE_NAME_LINK:
			break;
			
		case UI_WTYPE_POINTER_LINK:
			break;
			
		case UI_WTYPE_FILENAME:
			break;
			
			
			/* start menus */
		case UI_WTYPE_MENU_RADIO:
			wt.wcol_theme= btheme.tui.wcol_menu;
			wt.draw= widget_menubut;
			break;
			
//		case UI_WTYPE_MENU_ICON_RADIO:
//			wt.wcol_theme= &btheme->tui.wcol_menu;
//			wt.draw= widget_menuiconbut;
//			break;
			
		case UI_WTYPE_MENU_POINTER_LINK:
			wt.wcol_theme= btheme.tui.wcol_menu;
			wt.draw= widget_menubut;
			break;
			
			
		case UI_WTYPE_PULLDOWN:
			wt.wcol_theme= btheme.tui.wcol_pulldown;
			wt.draw= widget_pulldownbut;
			wt.state= widget_state_pulldown;
			break;
			
			/* in menus */
		case UI_WTYPE_MENU_ITEM:
			wt.wcol_theme= btheme.tui.wcol_menu_item;
			wt.draw= widget_menu_itembut;
			wt.state= widget_state_menu_item;
			break;
			
		case UI_WTYPE_MENU_BACK:
			wt.wcol_theme= btheme.tui.wcol_menu_back;
			wt.draw= widget_menu_back;
			break;
			
			/* specials */
		case UI_WTYPE_ICON:
//			wt.draw= null;
//			wt.custom= widget_icon_has_anim;
			break;
			
		case UI_WTYPE_SWATCH:
//			wt.custom= widget_swatch;
			break;
			
		case UI_WTYPE_BOX:
//			wt.custom= widget_box;
			wt.wcol_theme= btheme.tui.wcol_box;
			break;
			
		case UI_WTYPE_RGB_PICKER:
			break;
			
		case UI_WTYPE_NORMAL:
			break;

		case UI_WTYPE_SCROLL:
			wt.wcol_theme= btheme.tui.wcol_scroll;
			wt.state= widget_state_nothing;
//			wt.custom= widget_scroll;
			break;

		case UI_WTYPE_LISTITEM:
			wt.wcol_theme= btheme.tui.wcol_list_item;
			wt.draw= widget_list_itembut;
			break;
			
//		case UI_WTYPE_PROGRESSBAR:
//			wt.wcol_theme= &btheme->tui.wcol_progress;
//			wt.custom= widget_progressbar;
//			break;
	}
	
	return wt;
}


static int widget_roundbox_set(uiBut but, rcti rect)
{
	/* alignment */
	if((but.flag & UI.UI_BUT_ALIGN)!=0) {

		if((but.flag & UI.UI_BUT_ALIGN_TOP)!=0)
			rect.ymax+= 1;
		if((but.flag & UI.UI_BUT_ALIGN_LEFT)!=0)
			rect.xmin-= 1;

		switch(but.flag & UI.UI_BUT_ALIGN) {
			case UI.UI_BUT_ALIGN_TOP:
				return (12);
//				break;
			case UI.UI_BUT_ALIGN_DOWN:
				return (3);
//				break;
			case UI.UI_BUT_ALIGN_LEFT:
				return (6);
//				break;
			case UI.UI_BUT_ALIGN_RIGHT:
				return (9);
//				break;

			case UI.UI_BUT_ALIGN_DOWN|UI.UI_BUT_ALIGN_RIGHT:
				return (1);
//				break;
			case UI.UI_BUT_ALIGN_DOWN|UI.UI_BUT_ALIGN_LEFT:
				return (2);
//				break;
			case UI.UI_BUT_ALIGN_TOP|UI.UI_BUT_ALIGN_RIGHT:
				return (8);
//				break;
			case UI.UI_BUT_ALIGN_TOP|UI.UI_BUT_ALIGN_LEFT:
				return (4);
//				break;

			default:
				return (0);
//				break;
		}
	}
	return 15;
}

/* conversion from old to new buttons, so still messy */
public static void ui_draw_but(GL2 gl, bContext C, ARegion ar, uiStyle style, uiBut but, rcti rect)
{
	bTheme btheme= (bTheme)U.themes.first;
	ThemeUI tui= btheme.tui;
	uiFontStyle fstyle= style.widget;
	uiWidgetType wt= null;

	/* handle menus seperately */
	if(but.dt==UI.UI_EMBOSSP) {
		switch (but.type) {
			case UI.LABEL:
				widget_draw_text_icon.run(gl, style.widgetlabel, tui.wcol_menu_back, but, rect);
				break;
			case UI.SEPR:
				ui_draw_separator(gl, rect, tui.wcol_menu_item);
				break;

			default:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_ITEM);
		}
	}
	else if(but.dt==UI.UI_EMBOSSN) {
		/* "nothing" */
		wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_ICON);
	}
	else {

		switch (but.type) {
			case UI.LABEL:
				if((but.block.flag & UI.UI_BLOCK_LOOP)!=0)
					widget_draw_text_icon.run(gl, style.widgetlabel, tui.wcol_menu_back, but, rect);
				else {
					wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_LABEL);
					fstyle= style.widgetlabel;
				}
				break;

			case UI.SEPR:
				break;

			case UI.BUT:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_EXEC);
				break;

			case UI.NUM:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_NUMBER);
				break;

			case UI.NUMSLI:
			case UI.HSVSLI:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_SLIDER);
				break;

			case UI.ROW:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_RADIO);
				break;

			case UI.LISTROW:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_LISTITEM);
				break;

			case UI.TEX:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_NAME);
				break;

			case UI.SEARCH_MENU:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_NAME);
				if((but.block.flag & UI.UI_BLOCK_LOOP)!=0)
					wt.wcol_theme= btheme.tui.wcol_menu_back;
				break;

			case UI.TOGBUT:
			case UI.TOG:
			case UI.TOGN:
			case UI.TOG3:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_TOGGLE);
				break;

			case UI.OPTION:
			case UI.OPTIONN:
				if ((but.flag & UI.UI_HAS_ICON)==0) {
					wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_OPTION);
					but.flag |= UI.UI_TEXT_LEFT;
				}
				else
					wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_TOGGLE);
				break;

			case UI.MENU:
			case UI.BLOCK:
			case UI.ICONTEXTROW:
//				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_RADIO);
				if(but.str[0]==0 && but.icon!=null) {
					wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_ICON_RADIO);
				}
				else {
					wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_RADIO);
				}
				break;

			case UI.PULLDOWN:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_PULLDOWN);
				break;

			case UI.BUTM:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_ITEM);
				break;

			case UI.COL:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_SWATCH);
				break;

			case UI.ROUNDBOX:
			case UI.LISTBOX:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_BOX);
				break;

			case UI.LINK:
			case UI.INLINK:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_ICON);
//				wt.custom= widget_link;

				break;

			case UI.BUT_EXTRA:
//				widget_draw_extra_mask(C, but, widget_type(UI_WTYPE_BOX), rect);
				break;

			case UI.HSVCUBE:
//				if(but.a1 == UI_GRAD_V_ALT) // vertical V slider, uses new widget draw now
//					ui_draw_but_HSV_v(but, rect);
//				else  // other HSV pickers...
//					ui_draw_but_HSVCUBE(but, rect);
				break;

			case UI.HSVCIRCLE:
//				ui_draw_but_HSVCIRCLE(but, tui.wcol_regular, rect);
				break;

			case UI.BUT_COLORBAND:
//				ui_draw_but_COLORBAND(but, tui.wcol_regular, rect);
				break;

			case UI.BUT_NORMAL:
//				ui_draw_but_NORMAL(but, tui.wcol_regular, rect);
				break;
				
			case UI.BUT_IMAGE:
//				ui_draw_but_IMAGE(ar, but, tui.wcol_regular, rect);
				break;
			
			case UI.HISTOGRAM:
//				ui_draw_but_HISTOGRAM(ar, but, tui.wcol_regular, rect);
				break;
				
			case UI.WAVEFORM:
//				ui_draw_but_WAVEFORM(ar, but, tui.wcol_regular, rect);
				break;
				
			case UI.VECTORSCOPE:
//				ui_draw_but_VECTORSCOPE(ar, but, tui.wcol_regular, rect);
				break;

			case UI.BUT_CURVE:
//				ui_draw_but_CURVE(ar, but, tui.wcol_regular, rect);
				break;
				
			case UI.PROGRESSBAR:
//				wt= widget_type(UI_WTYPE_PROGRESSBAR);
//				fstyle= style.widgetlabel;
				break;

			case UI.SCROLL:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_SCROLL);
				break;

			default:
				wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_REGULAR);
		}
	}

	if(wt!=null) {
		rcti disablerect= rect.copy(); /* rect gets clipped smaller for text */
		int roundboxalign, state;

		roundboxalign= widget_roundbox_set(but, rect);

		state= but.flag;
		if(but.editstr!=null)
                    state |= UI.UI_TEXTINPUT;

		wt.state.run(wt, state);
		if(wt.custom!=null) {
			wt.custom.run(but, wt.wcol, rect, state, roundboxalign);
        }
		else if(wt.draw!=null) {
			wt.draw.run(gl, wt.wcol, rect, state, roundboxalign);
        }
		wt.text.run(gl, fstyle, wt.wcol, but, rect);

		if((state & (UI.UI_BUT_DISABLED|UI.UI_BUT_INACTIVE))!=0)
			if(but.dt!=UI.UI_EMBOSSP)
				widget_disabled(gl, disablerect);
	}
}

public static void ui_draw_menu_back(GL2 gl, uiStyle style, uiBlock block, rcti rect)
{
	uiWidgetType wt= widget_type(uiWidgetTypeEnum.UI_WTYPE_MENU_BACK);
	
	wt.state.run(wt, 0);
	if(block!=null)
		wt.draw.run(gl, wt.wcol, rect, block.flag, block.direction);
	else
		wt.draw.run(gl, wt.wcol, rect, 0, 0);
	
}

//void ui_draw_search_back(uiStyle *style, uiBlock *block, rcti *rect)
//{
//	uiWidgetType *wt= widget_type(UI_WTYPE_BOX);
//
//	glEnable(GL_BLEND);
//	widget_softshadow(rect, 15, 5.0f, 8.0f);
//	glDisable(GL_BLEND);
//
//	wt.state(wt, 0);
//	if(block)
//		wt.draw(&wt.wcol, rect, block.flag, 15);
//	else
//		wt.draw(&wt.wcol, rect, 0, 15);
//
//}
//
//
///* helper call to draw a menu item without button */
///* state: UI_ACTIVE or 0 */
//void ui_draw_menu_item(uiFontStyle *fstyle, rcti *rect, char *name, int iconid, int state)
//{
//	uiWidgetType *wt= widget_type(UI_WTYPE_MENU_ITEM);
//	rcti _rect= *rect;
//	char *cpoin;
//
//	wt.state(wt, state);
//	wt.draw(&wt.wcol, rect, 0, 0);
//
//	uiStyleFontSet(fstyle);
//	fstyle.align= UI_STYLE_TEXT_LEFT;
//
//	/* text location offset */
//	rect.xmin+=5;
//	if(iconid) rect.xmin+= ICON_HEIGHT;
//
//	/* cut string in 2 parts? */
//	cpoin= strchr(name, '|');
//	if(cpoin) {
//		*cpoin= 0;
//		rect.xmax -= BLF_width(cpoin+1) + 10;
//	}
//
//	glColor3ubv((unsigned char*)wt.wcol.text);
//	uiStyleFontDraw(fstyle, rect, name);
//
//	/* part text right aligned */
//	if(cpoin) {
//		fstyle.align= UI_STYLE_TEXT_RIGHT;
//		rect.xmax= _rect.xmax - 5;
//		uiStyleFontDraw(fstyle, rect, cpoin+1);
//		*cpoin= '|';
//	}
//
//	/* restore rect, was messed with */
//	*rect= _rect;
//
//	if(iconid) {
//		int xs= rect.xmin+4;
//		int ys= 1 + (rect.ymin+rect.ymax- ICON_HEIGHT)/2;
//		glEnable(GL_BLEND);
//		UI_icon_draw_aspect_blended(xs, ys, iconid, 1.2f, 0); /* XXX scale weak get from fstyle? */
//		glDisable(GL_BLEND);
//	}
//}
}

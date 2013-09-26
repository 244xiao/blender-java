/**
 * $Id: WmSubWindow.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): 2007 Blender Foundation (refactor)
 *
 * ***** END GPL LICENSE BLOCK *****
 *
 *
 * Subwindow opengl handling. 
 * BTW: subwindows open/close in X11 are way too slow, tried it, and choose for my own system... (ton)
 * 
 */
package blender.windowmanager;

//#include <string.h>

import java.util.Arrays;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.editors.screen.GlUtil;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmWindow;

//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_windowmanager_types.h"
//#include "DNA_screen_types.h"
//
//#include "BLI_blenlib.h"
//#include "BLI_arithb.h"
//
//#include "BKE_context.h"
//#include "BKE_global.h"
//
//#include "BIF_gl.h"
//#include "BIF_glutil.h"
//
//#include "WM_api.h"
//#include "wm_subwindow.h"
//#include "wm_window.h"
//

public class WmSubWindow {
///* wmSubWindow stored in wmWindow... but not exposed outside this C file */
///* it seems a bit redundant (area regions can store it too, but we keep it
//   because we can store all kind of future opengl fanciness here */
//
///* we use indices and array because:
//   - index has safety, no pointers from this C file hanging around
//   - fast lookups of indices with array, list would give overhead
//   - old code used it this way...
//   - keep option open to have 2 screens using same window
//*/

public static class wmSubWindow extends Link<wmSubWindow> implements Cloneable {
	public rcti winrct;
	public int swinid;

//	float viewmat[4][4], winmat[4][4];
//	float viewmat1[4][4], winmat1[4][4];
        public float[] viewmat=new float[16], winmat=new float[16];
	public float[] viewmat1=new float[16], winmat1=new float[16];
	
	public wmSubWindow copy() { try {return (wmSubWindow)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
};


/* ******************* open, free, set, get data ******************** */

/* not subwindow itself */
public static void wm_subwindow_free(wmSubWindow swin)
{
	/* future fancy stuff */
}

public static void wm_subwindows_free(wmWindow win)
{
	wmSubWindow swin;

	for(swin= (wmSubWindow)win.subwindows.first; swin!=null; swin= swin.next)
		wm_subwindow_free(swin);

	ListBaseUtil.BLI_freelistN(win.subwindows);
}


//int wm_subwindow_get(wmWindow *win)
//{
//	if(win.curswin)
//		return win.curswin.swinid;
//	return 0;
//}

public static wmSubWindow swin_from_swinid(wmWindow win, int swinid)
{
	wmSubWindow swin;

	for(swin= (wmSubWindow)win.subwindows.first; swin!=null; swin= swin.next)
		if(swin.swinid==swinid)
			break;
	return swin;
}

public static void wm_subwindow_getsize(wmWindow win, int swinid, int[] x, int[] y)
{
	wmSubWindow swin= swin_from_swinid(win, swinid);

	if(swin!=null) {
		x[0]= swin.winrct.xmax - swin.winrct.xmin + 1;
		y[0]= swin.winrct.ymax - swin.winrct.ymin + 1;
	}
}

//void wm_subwindow_getorigin(wmWindow *win, int swinid, int *x, int *y)
//{
//	wmSubWindow *swin= swin_from_swinid(win, swinid);
//
//	if(swin) {
//		*x= swin.winrct.xmin;
//		*y= swin.winrct.ymin;
//	}
//}

public static void wm_subwindow_getmatrix(wmWindow win, int swinid, float[][] mat)
{
	wmSubWindow swin= swin_from_swinid(win, swinid);

	if(swin!=null)
		Arithb.Mat4MulMat4(mat, Arithb.convertArray(swin.viewmat), Arithb.convertArray(swin.winmat));

        // TMP
        if (Double.isNaN(mat[0][0])) {
            System.out.printf("viewmat:%s, winmat:%s\n", Arrays.toString(swin.viewmat), Arrays.toString(swin.winmat));
        }
}

/* always sets pixel-precise 2D window/view matrices */
/* coords is in whole pixels. xmin = 15, xmax= 16: means window is 2 pix big */
public static int wm_subwindow_open(GL2 gl, wmWindow win, rcti winrct)
//public static int wm_subwindow_open(wmWindow win, rcti winrct)
{
	wmSubWindow swin;
	int[] width={0}, height={0};
	int freewinid= 1;

	for(swin= (wmSubWindow)win.subwindows.first; swin!=null; swin= swin.next)
		if(freewinid <= swin.swinid)
			freewinid= swin.swinid+1;

	win.curswin= swin= new wmSubWindow();
	ListBaseUtil.BLI_addtail(win.subwindows, swin);

//	if(G.f & G_DEBUG) printf("swin %d added\n", freewinid);
	swin.swinid= freewinid;
//	swin.winrct= *winrct;
//        try {
//            swin.winrct= (rcti)winrct.clone();
//        } catch(Exception ex) { ex.printStackTrace(); }
        swin.winrct= winrct.copy();

	Arithb.Mat4One(swin.viewmat);
	Arithb.Mat4One(swin.winmat);

//	try {
//		GL2 gl = (GL2)GLU.getCurrentGL();
		/* and we appy it all right away */
		wmSubWindowSet(gl, win, swin.swinid);
	
		/* extra service */
		wm_subwindow_getsize(win, swin.swinid, width, height);
		wmOrtho2(gl, -0.375f, (float)width[0]-0.375f, -0.375f, (float)height[0]-0.375f);
		wmLoadIdentity(gl);
//	} catch (GLException e) {
//		
//	}

	return swin.swinid;
}


public static void wm_subwindow_close(wmWindow win, int swinid)
{
	wmSubWindow swin= swin_from_swinid(win, swinid);

	if (swin!=null) {
		if (swin==win.curswin)
			win.curswin= null;
		wm_subwindow_free(swin);
		ListBaseUtil.BLI_remlink(win.subwindows, swin);
//		MEM_freeN(swin);
	}
	else {
		System.out.printf("wm_subwindow_close: Internal error, bad winid: %d\n", swinid);
	}

}

/* pixels go from 0-99 for a 100 pixel window */
public static void wm_subwindow_position(GL2 gl, wmWindow win, int swinid, rcti winrct)
//public static void wm_subwindow_position(wmWindow win, int swinid, rcti winrct)
{
	wmSubWindow swin= swin_from_swinid(win, swinid);

	if(swin!=null) {
		int[] width={0}, height={0};

//		swin.winrct= *winrct;
        swin.winrct= winrct;

		/* CRITICAL, this clamping ensures that
			* the viewport never goes outside the screen
			* edges (assuming the x, y coords aren't
					 * outside). This caused a hardware lock
			* on Matrox cards if it happens.
			*
			* Really Blender should never _ever_ try
			* to do such a thing, but just to be safe
			* clamp it anyway (or fix the bScreen
		    * scaling routine, and be damn sure you
		    * fixed it). - zr  (2001!)
			*/

		if (swin.winrct.xmax > win.sizex)
			swin.winrct.xmax= win.sizex;
		if (swin.winrct.ymax > win.sizey)
			swin.winrct.ymax= win.sizey;

		/* extra service */
//		try {
//			GL2 gl = (GL2)GLU.getCurrentGL();
			wmSubWindowSet(gl, win, swinid);
			wm_subwindow_getsize(win, swinid, width, height);
			wmOrtho2(gl, -0.375f, (float)width[0]-0.375f, -0.375f, (float)height[0]-0.375f);
//		} catch (GLException e) {
//			
//		}
	}
	else {
		System.out.printf("wm_subwindow_position: Internal error, bad winid: %d\n", swinid);
	}
}

///* ---------------- WM versions of OpenGL calls, using glBlah() syntax ------------------------ */
///* ----------------- exported in WM_api.h ------------------------------------------------------ */

/* internal state, no threaded opengl! XXX */
public static wmWindow _curwindow= null;
public static wmSubWindow _curswin= null;

public static void wmSubWindowScissorSet(GL2 gl, wmWindow win, int swinid, rcti srct)
{
	int width, height;
	_curswin= swin_from_swinid(win, swinid);

	if(_curswin==null) {
		System.out.printf("wmSubWindowSet %d: doesn't exist\n", swinid);
		return;
	}

	win.curswin= _curswin;
	_curwindow= win;

	width= _curswin.winrct.xmax - _curswin.winrct.xmin + 1;
	height= _curswin.winrct.ymax - _curswin.winrct.ymin + 1;
	gl.glViewport(_curswin.winrct.xmin, _curswin.winrct.ymin, width, height);

	if(srct!=null) {
		width= srct.xmax - srct.xmin + 1;
		height= srct.ymax - srct.ymin + 1;
		gl.glScissor(srct.xmin, srct.ymin, width, height);
	}
	else
		gl.glScissor(_curswin.winrct.xmin, _curswin.winrct.ymin, width, height);

	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadMatrixf(_curswin.winmat,0);
        // TMP
        if (Double.isNaN(_curswin.winmat[12])) {
            System.out.println("wmSubWindowScissorSet NAN");
        }
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadMatrixf(_curswin.viewmat,0);

	gl.glFlush();
}


/* enable the WM versions of opengl calls */
public static void wmSubWindowSet(GL2 gl, wmWindow win, int swinid)
{
	wmSubWindowScissorSet(gl, win, swinid, null);
}

    public static void wmLoadMatrix(GL2 gl, float[][] mat) {
        if (_curswin == null) {
            return;
        }

        float[] tmp = Arithb.convertArray(mat);
        gl.glLoadMatrixf(tmp, 0);

        if (GlUtil.glaGetOneInteger(gl, GL2.GL_MATRIX_MODE) == GL2.GL_MODELVIEW) {
//            Arithb.Mat4CpyMat4(_curswin.viewmat, mat);
            System.arraycopy(tmp, 0, _curswin.viewmat, 0, 16);
        } else {
//            Arithb.Mat4CpyMat4(_curswin.winmat, mat);
            System.arraycopy(tmp, 0, _curswin.winmat, 0, 16);
        }

        // TMP
        if (Double.isNaN(_curswin.winmat[12])) {
            System.out.println("wmLoadMatrix NAN");
        }
    }

    public static void wmGetMatrix(GL2 gl, float[][] mat) {
        if (_curswin == null) {
            return;
        }

        if (GlUtil.glaGetOneInteger(gl, GL2.GL_MATRIX_MODE) == GL2.GL_MODELVIEW) {
            Arithb.Mat4CpyMat4(mat, Arithb.convertArray(_curswin.viewmat));
        } else {
            Arithb.Mat4CpyMat4(mat, Arithb.convertArray(_curswin.winmat));
        }
    }

public static void wmMultMatrix(GL2 gl, float[][] mat)
{
	if(_curswin==null) return;

	gl.glMultMatrixf(Arithb.convertArray(mat),0);

	if (GlUtil.glaGetOneInteger(gl, GL2.GL_MATRIX_MODE)==GL2.GL_MODELVIEW)
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, _curswin.viewmat,0);
	else
		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, _curswin.winmat,0);

        // TMP
        if (Double.isNaN(_curswin.winmat[12])) {
            System.out.println("wmMultMatrix NAN");
        }
}

//static int debugpush= 0;

public static void wmPushMatrix()
{
	if(_curswin==null) return;

//	if(debugpush)
//		printf("wmPushMatrix error already pushed\n");
//	debugpush= 1;

//	Arithb.Mat4CpyMat4(_curswin.viewmat1, _curswin.viewmat);
        System.arraycopy(_curswin.viewmat, 0, _curswin.viewmat1, 0, 16);
//	Arithb.Mat4CpyMat4(_curswin.winmat1, _curswin.winmat);
        System.arraycopy(_curswin.winmat, 0, _curswin.winmat1, 0, 16);
}

public static void wmPopMatrix(GL2 gl)
{
	if(_curswin==null) return;

//	if(debugpush==0)
//		printf("wmPopMatrix error nothing popped\n");
//	debugpush= 0;

//	Mat4CpyMat4(_curswin.viewmat, _curswin.viewmat1);
        System.arraycopy(_curswin.viewmat1, 0, _curswin.viewmat, 0, 16);
//	Mat4CpyMat4(_curswin.winmat, _curswin.winmat1);
        System.arraycopy(_curswin.winmat1, 0, _curswin.winmat, 0, 16);

        // TMP
        if (Double.isNaN(_curswin.winmat[12])) {
            System.out.println("wmPopMatrix NAN");
        }

	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadMatrixf(_curswin.winmat,0);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadMatrixf(_curswin.viewmat,0);

}

public static void wmGetSingleMatrix(GL2 gl, float[][] mat)
{
	if(_curswin!=null)
		Arithb.Mat4MulMat4(mat, Arithb.convertArray(_curswin.viewmat), Arithb.convertArray(_curswin.winmat));
}

//void wmScale(float x, float y, float z)
//{
//	if(_curswin==NULL) return;
//
//	glScalef(x, y, z);
//
//	if (glaGetOneInteger(GL_MATRIX_MODE)==GL_MODELVIEW)
//		glGetFloatv(GL_MODELVIEW_MATRIX, (float *)_curswin.viewmat);
//	else
//		glGetFloatv(GL_MODELVIEW_MATRIX, (float *)_curswin.winmat);
//
//}

public static void wmLoadIdentity(GL2 gl)
{
	if(_curswin==null) return;

	if (GlUtil.glaGetOneInteger(gl, GL2.GL_MATRIX_MODE)==GL2.GL_MODELVIEW)
		Arithb.Mat4One(_curswin.viewmat);
	else
		Arithb.Mat4One(_curswin.winmat);

	gl.glLoadIdentity();
}

public static void wmFrustum(GL2 gl, float x1, float x2, float y1, float y2, float n, float f)
{
	if(_curswin!=null) {

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(x1, x2, y1, y2, n, f);

		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, _curswin.winmat,0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

                // TMP
                if (Double.isNaN(_curswin.winmat[12])) {
                    System.out.println("wmFrustum NAN");
                }
	}
}

public static void wmOrtho(GL2 gl, float x1, float x2, float y1, float y2, float n, float f)
{
	if(_curswin!=null) {

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(x1, x2, y1, y2, n, f);

		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, _curswin.winmat,0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

        // TMP
        if (Double.isNaN(_curswin.winmat[12])) {
            System.out.printf("wmOrtho NAN. x1:%f, x2:%f, y1:%f, y2:%f, n:%f, f:%f\n", x1, x2, y1, y2, n, f);
            Thread.dumpStack();
        }
	}
}

public static void wmOrtho2(GL2 gl, float x1, float x2, float y1, float y2)
{
	/* prevent opengl from generating errors */
	if(x1==x2) x2+=1.0;
	if(y1==y2) y2+=1.0;
	wmOrtho(gl, x1, x2, y1, y2, -100, 100);
}


///* *************************** Framebuffer color depth, for selection codes ********************** */
//
//static int wm_get_colordepth(void)
//{
//	static int mainwin_color_depth= 0;
//
//	if(mainwin_color_depth==0) {
//		GLint r, g, b;
//
//		glGetIntegerv(GL_RED_BITS, &r);
//		glGetIntegerv(GL_GREEN_BITS, &g);
//		glGetIntegerv(GL_BLUE_BITS, &b);
//
//		mainwin_color_depth= r + g + b;
//		if(G.f & G_DEBUG) {
//			printf("Color depth r %d g %d b %d\n", (int)r, (int)g, (int)b);
//			glGetIntegerv(GL_AUX_BUFFERS, &r);
//			printf("Aux buffers: %d\n", (int)r);
//		}
//	}
//	return mainwin_color_depth;
//}
//
//
//#ifdef __APPLE__
//
///* apple seems to round colors to below and up on some configs */
//
//static unsigned int index_to_framebuffer(int index)
//{
//	unsigned int i= index;
//
//	switch(wm_get_colordepth()) {
//	case 12:
//		i= ((i & 0xF00)<<12) + ((i & 0xF0)<<8) + ((i & 0xF)<<4);
//		/* sometimes dithering subtracts! */
//		i |= 0x070707;
//		break;
//	case 15:
//	case 16:
//		i= ((i & 0x7C00)<<9) + ((i & 0x3E0)<<6) + ((i & 0x1F)<<3);
//		i |= 0x030303;
//		break;
//	case 24:
//		break;
//	default:	// 18 bits...
//		i= ((i & 0x3F000)<<6) + ((i & 0xFC0)<<4) + ((i & 0x3F)<<2);
//		i |= 0x010101;
//		break;
//	}
//
//	return i;
//}
//
//#else
//
///* this is the old method as being in use for ages.... seems to work? colors are rounded to lower values */
//
//static unsigned int index_to_framebuffer(int index)
//{
//	unsigned int i= index;
//
//	switch(wm_get_colordepth()) {
//		case 8:
//			i= ((i & 48)<<18) + ((i & 12)<<12) + ((i & 3)<<6);
//			i |= 0x3F3F3F;
//			break;
//		case 12:
//			i= ((i & 0xF00)<<12) + ((i & 0xF0)<<8) + ((i & 0xF)<<4);
//			/* sometimes dithering subtracts! */
//			i |= 0x0F0F0F;
//			break;
//		case 15:
//		case 16:
//			i= ((i & 0x7C00)<<9) + ((i & 0x3E0)<<6) + ((i & 0x1F)<<3);
//			i |= 0x070707;
//			break;
//		case 24:
//			break;
//		default:	// 18 bits...
//			i= ((i & 0x3F000)<<6) + ((i & 0xFC0)<<4) + ((i & 0x3F)<<2);
//			i |= 0x030303;
//			break;
//	}
//
//	return i;
//}
//
//#endif
//
//void WM_set_framebuffer_index_color(int index)
//{
//	cpack(index_to_framebuffer(index));
//}
//
//int WM_framebuffer_to_index(unsigned int col)
//{
//	if (col==0) return 0;
//
//	switch(wm_get_colordepth()) {
//	case 8:
//		return ((col & 0xC00000)>>18) + ((col & 0xC000)>>12) + ((col & 0xC0)>>6);
//	case 12:
//		return ((col & 0xF00000)>>12) + ((col & 0xF000)>>8) + ((col & 0xF0)>>4);
//	case 15:
//	case 16:
//		return ((col & 0xF80000)>>9) + ((col & 0xF800)>>6) + ((col & 0xF8)>>3);
//	case 24:
//		return col & 0xFFFFFF;
//	default: // 18 bits...
//		return ((col & 0xFC0000)>>6) + ((col & 0xFC00)>>4) + ((col & 0xFC)>>2);
//	}
//}
//
//
///* ********** END MY WINDOW ************** */
//
//#if 0 // XXX not used...
//#ifdef WIN32
//static int is_a_really_crappy_nvidia_card(void) {
//	static int well_is_it= -1;
//
//		/* Do you understand the implication? Do you? */
//	if (well_is_it==-1)
//		well_is_it= (strcmp((char*) glGetString(GL_VENDOR), "NVIDIA Corporation") == 0);
//
//	return well_is_it;
//}
//#endif
//#endif // XXX not used...
}

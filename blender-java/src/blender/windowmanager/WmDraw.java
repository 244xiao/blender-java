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
 * The Original Code is Copyright (C) 2007 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

import static blender.blenkernel.Blender.U;

import javax.media.opengl.GL2;

import blender.blenkernel.bContext;
import blender.blenlib.Rct;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenEdit;
import blender.makesdna.SpaceTypes;
import blender.makesdna.UserDefTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;

public class WmDraw {

/* swap */
public static final int WIN_NONE_OK=		0;
public static final int WIN_BACK_OK=     1;
public static final int WIN_FRONT_OK=    2;
public static final int WIN_BOTH_OK=		3;

/* ********************* drawing, swap ****************** */

public static void wm_paintcursor_draw(bContext C, ARegion ar)
{
//	wmWindowManager wm= bContext.CTX_wm_manager(C);
//
//	if(wm.paintcursors.first!=null) {
//		wmWindow *win= CTX_wm_window(C);
//		bScreen *screen= win.screen;
//		wmPaintCursor *pc;
//
//		if(screen.subwinactive == ar.swinid) {
//			for(pc= wm.paintcursors.first; pc; pc= pc.next) {
//				if(pc.poll(C)) {
//					ARegion *ar= CTX_wm_region(C);
//					pc.draw(C, win.eventstate.x - ar.winrct.xmin, win.eventstate.y - ar.winrct.ymin, pc.customdata);
//				}
//			}
//		}
//	}
}

/* ********************* drawing, swap ****************** */

static void wm_area_mark_invalid_backbuf(ScrArea sa)
{
	if(sa.spacetype == SpaceTypes.SPACE_VIEW3D)
		((View3D)sa.spacedata.first).flag |= View3dTypes.V3D_INVALID_BACKBUF;
}

static int wm_area_test_invalid_backbuf(ScrArea sa)
{
	if(sa.spacetype == SpaceTypes.SPACE_VIEW3D)
		return (((View3D)sa.spacedata.first).flag & View3dTypes.V3D_INVALID_BACKBUF);
	else
		return 1;
}

/********************** draw all **************************/
/* - reference method, draw all each time                 */

public static void wm_method_draw_full(GL2 gl, bContext C, wmWindow win)
{
	bScreen screen= win.screen;
	ScrArea sa;
	ARegion ar;

	/* draw area regions */
	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
		bContext.CTX_wm_area_set(C, sa);

		for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if(ar.swinid!=0) {
				bContext.CTX_wm_region_set(C, ar);
				Area.ED_region_do_draw(gl, C, ar);
				wm_paintcursor_draw(C, ar);
				Area.ED_area_overdraw_flush(sa, ar);
				bContext.CTX_wm_region_set(C, null);
			}
		}

		wm_area_mark_invalid_backbuf(sa);
		bContext.CTX_wm_area_set(C, null);
	}

	ScreenEdit.ED_screen_draw(gl, win);
	Area.ED_area_overdraw(gl, C);

	/* draw overlapping regions */
	for(ar=(ARegion)screen.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.swinid!=0) {
			bContext.CTX_wm_menu_set(C, ar);
			Area.ED_region_do_draw(gl, C, ar);
			bContext.CTX_wm_menu_set(C, null);
		}
	}

//	if(screen.do_draw_gesture!=0)
//		wm_gesture_draw(win);
}

/****************** draw overlap all **********************/
/* - redraw marked areas, and anything that overlaps it   */
/* - it also handles swap exchange optionally, assuming   */
/*   that on swap no clearing happens and we get back the */
/*   same buffer as we swapped to the front               */

/* mark area-regions to redraw if overlapped with rect */
public static void wm_flush_regions_down(bScreen screen, rcti dirty)
{
	ScrArea sa;
	ARegion ar;

	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
		for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if(Rct.BLI_isect_rcti(dirty, ar.winrct, null)) {
				ar.do_draw= 1;
//				memset(&ar.drawrct, 0, sizeof(ar.drawrct));
//                ar.drawrct = new rcti();
                Rct.clear(ar.drawrct);
				ar.swap= WIN_NONE_OK;
			}
		}
	}
}

/* mark menu-regions to redraw if overlapped with rect */
public static void wm_flush_regions_up(bScreen screen, rcti dirty)
{
	ARegion ar;
	
	for(ar= (ARegion)screen.regionbase.first; ar!=null; ar= ar.next) {
		if(Rct.BLI_isect_rcti(dirty, ar.winrct, null)) {
			ar.do_draw= 1;
//			memset(&ar.drawrct, 0, sizeof(ar.drawrct));
//            ar.drawrct = new rcti();
            Rct.clear(ar.drawrct);
			ar.swap= WIN_NONE_OK;
		}
	}
}

private static rcti rect= new rcti();
public static void wm_method_draw_overlap_all(GL2 gl, bContext C, wmWindow win, boolean exchange)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	bScreen screen= win.screen;
	ScrArea sa;
	ARegion ar;
//	boolean exchange= (G.f & Global.G_SWAP_EXCHANGE)!=0;
	
	/* after backbuffer selection draw, we need to redraw */
	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
		for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
			if(ar.swinid!=0 && wm_area_test_invalid_backbuf(sa)==0)
					Area.ED_region_tag_redraw(ar);

	/* flush overlapping regions */
	if(screen.regionbase.first!=null) {
		/* flush redraws of area regions up to overlapping regions */
		for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
			for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
				if(ar.swinid!=0 && ar.do_draw!=0)
					wm_flush_regions_up(screen, ar.winrct);
		
		/* flush between overlapping regions */
		for(ar= (ARegion)screen.regionbase.last; ar!=null; ar= ar.prev)
			if(ar.swinid!=0 && ar.do_draw!=0)
				wm_flush_regions_up(screen, ar.winrct);
		
		/* flush redraws of overlapping regions down to area regions */
		for(ar= (ARegion)screen.regionbase.last; ar!=null; ar= ar.prev)
			if(ar.swinid!=0 && ar.do_draw!=0)
				wm_flush_regions_down(screen, ar.winrct);
	}
	
	/* flush drag item */
	if(rect.xmin!=rect.xmax) {
		wm_flush_regions_down(screen, rect);
		rect.xmin= rect.xmax = 0;
	}
//	if(wm.drags.first) {
//		/* doesnt draw, fills rect with boundbox */
//		wm_drags_draw(C, win, rect);
//	}

	/* draw marked area regions */
	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
		bContext.CTX_wm_area_set(C, sa);

		for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if(ar.swinid!=0) {
				if(ar.do_draw!=0) {
					bContext.CTX_wm_region_set(C, ar);
					Area.ED_region_do_draw(gl, C, ar);
					wm_paintcursor_draw(C, ar);
					Area.ED_area_overdraw_flush(sa, ar);
					bContext.CTX_wm_region_set(C, null);

					if(exchange)
						ar.swap= WIN_FRONT_OK;
				}
				else if(exchange) {
					if(ar.swap == WIN_FRONT_OK) {
						bContext.CTX_wm_region_set(C, ar);
						Area.ED_region_do_draw(gl, C, ar);
						wm_paintcursor_draw(C, ar);
						Area.ED_area_overdraw_flush(sa, ar);
						bContext.CTX_wm_region_set(C, null);

						ar.swap= WIN_BOTH_OK;
						System.out.printf("draws swap exchange %d\n", ar.swinid);
					}
					else if(ar.swap == WIN_BACK_OK)
						ar.swap= WIN_FRONT_OK;
					else if(ar.swap == WIN_BOTH_OK)
						ar.swap= WIN_BOTH_OK;
				}
			}
		}
		
		wm_area_mark_invalid_backbuf(sa);
		bContext.CTX_wm_area_set(C, null);
	}

	/* after area regions so we can do area 'overlay' drawing */
	if(screen.do_draw!=0) {
		ScreenEdit.ED_screen_draw(gl, win);

		if(exchange)
			screen.swap= WIN_FRONT_OK;
	}
	else if(exchange) {
		if(screen.swap==WIN_FRONT_OK) {
			ScreenEdit.ED_screen_draw(gl, win);
			screen.swap= WIN_BOTH_OK;
		}
		else if(screen.swap==WIN_BACK_OK)
			screen.swap= WIN_FRONT_OK;
		else if(screen.swap==WIN_BOTH_OK)
			screen.swap= WIN_BOTH_OK;
	}

	Area.ED_area_overdraw(gl, C);

	/* draw marked overlapping regions */
	for(ar=(ARegion)screen.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.swinid!=0 && ar.do_draw!=0) {
			bContext.CTX_wm_menu_set(C, ar);
			Area.ED_region_do_draw(gl, C, ar);
			bContext.CTX_wm_menu_set(C, null);
		}
	}

//	if(screen.do_draw_gesture!=0)
//		wm_gesture_draw(win);
	
//	/* needs pixel coords in screen */
//	if(wm.drags.first) {
//		wm_drags_draw(C, win, null);
//	}
}

//#if 0
///******************** draw damage ************************/
///* - not implemented                                      */
//
//static void wm_method_draw_damage(bContext *C, wmWindow *win)
//{
//	wm_method_draw_all(C, win);
//}
//#endif
//
///****************** draw triple buffer ********************/
///* - area regions are written into a texture, without any */
///*   of the overlapping menus, brushes, gestures. these   */
///*   are redrawn each time.                               */
///*                                                        */
///* - if non-power of two textures are supported, that is  */
///*   used. if not, multiple smaller ones are used, with   */
///*   worst case wasted space being 23.4% for 3x3 textures */
//
//#define MAX_N_TEX 3

public static class wmDrawTriple {
//	public GLuint bind[MAX_N_TEX*MAX_N_TEX];
//	public int x[MAX_N_TEX], y[MAX_N_TEX];
//	public int nx, ny;
//	public GLenum target;
};

//static int is_pow2(int n)
//{
//	return ((n)&(n-1))==0;
//}
//
//static int smaller_pow2(int n)
//{
//    while (!is_pow2(n))
//		n= n&(n-1);
//
//	return n;
//}
//
//static int larger_pow2(int n)
//{
//	if (is_pow2(n))
//		return n;
//
//	while(!is_pow2(n))
//		n= n&(n-1);
//
//	return n*2;
//}
//
//static void split_width(int x, int n, int *splitx, int *nx)
//{
//	int a, newnx, waste;
//
//	/* if already power of two just use it */
//	if(is_pow2(x)) {
//		splitx[0]= x;
//		(*nx)++;
//		return;
//	}
//
//	if(n == 1) {
//		/* last part, we have to go larger */
//		splitx[0]= larger_pow2(x);
//		(*nx)++;
//	}
//	else {
//		/* two or more parts to go, use smaller part */
//		splitx[0]= smaller_pow2(x);
//		newnx= ++(*nx);
//		split_width(x-splitx[0], n-1, splitx+1, &newnx);
//
//		for(waste=0, a=0; a<n; a++)
//			waste += splitx[a];
//
//		/* if we waste more space or use the same amount,
//		 * revert deeper splits and just use larger */
//		if(waste >= larger_pow2(x)) {
//			splitx[0]= larger_pow2(x);
//			memset(splitx+1, 0, sizeof(int)*(n-1));
//		}
//		else
//			*nx= newnx;
//	}
//}

public static void wm_draw_triple_free(wmWindow win)
{
	if(win.drawdata!=null) {
//		wmDrawTriple triple= win.drawdata;
//
//		glDeleteTextures(triple.nx*triple.ny, triple.bind);
//		MEM_freeN(triple);

		win.drawdata= null;
	}
}

//static void wm_draw_triple_fail(bContext *C, wmWindow *win)
//{
//	wm_draw_window_clear(win);
//
//	win.drawfail= 1;
//	wm_method_draw_overlap_all(C, win);
//}
//
//static int wm_triple_gen_textures(wmWindow *win, wmDrawTriple *triple)
//{
//	GLint maxsize;
//	int x, y;
//
//	/* compute texture sizes */
//	if(GLEW_ARB_texture_rectangle || GLEW_NV_texture_rectangle || GLEW_EXT_texture_rectangle) {
//		triple.target= GL_TEXTURE_RECTANGLE_ARB;
//		triple.nx= 1;
//		triple.ny= 1;
//		triple.x[0]= win.sizex;
//		triple.y[0]= win.sizey;
//	}
//	else if(GLEW_ARB_texture_non_power_of_two) {
//		triple.target= GL_TEXTURE_2D;
//		triple.nx= 1;
//		triple.ny= 1;
//		triple.x[0]= win.sizex;
//		triple.y[0]= win.sizey;
//	}
//	else {
//		triple.target= GL_TEXTURE_2D;
//		triple.nx= 0;
//		triple.ny= 0;
//		split_width(win.sizex, MAX_N_TEX, triple.x, &triple.nx);
//		split_width(win.sizey, MAX_N_TEX, triple.y, &triple.ny);
//	}
//
//	/* generate texture names */
//	glGenTextures(triple.nx*triple.ny, triple.bind);
//
//	if(!triple.bind[0]) {
//		/* not the typical failure case but we handle it anyway */
//		printf("WM: failed to allocate texture for triple buffer drawing (glGenTextures).\n");
//		return 0;
//	}
//
//	for(y=0; y<triple.ny; y++) {
//		for(x=0; x<triple.nx; x++) {
//			/* proxy texture is only guaranteed to test for the cases that
//			 * there is only one texture in use, which may not be the case */
//			glGetIntegerv(GL_MAX_TEXTURE_SIZE, &maxsize);
//
//			if(triple.x[x] > maxsize || triple.y[y] > maxsize) {
//				glBindTexture(triple.target, 0);
//				printf("WM: failed to allocate texture for triple buffer drawing (texture too large for graphics card).\n");
//				return 0;
//			}
//
//			/* setup actual texture */
//			glBindTexture(triple.target, triple.bind[x + y*triple.nx]);
//			glTexImage2D(triple.target, 0, GL_RGB8, triple.x[x], triple.y[y], 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
//			glTexParameteri(triple.target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//			glTexParameteri(triple.target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//			// glColor still used with this enabled?
//			// glTexEnvi(triple.target, GL_TEXTURE_ENV_MODE, GL_REPLACE);
//			glBindTexture(triple.target, 0);
//
//			/* not sure if this works everywhere .. */
//			if(glGetError() == GL_OUT_OF_MEMORY) {
//				printf("WM: failed to allocate texture for triple buffer drawing (out of memory).\n");
//				return 0;
//			}
//		}
//	}
//
//	return 1;
//}
//
//static void wm_triple_draw_textures(wmWindow *win, wmDrawTriple *triple)
//{
//	float halfx, halfy, ratiox, ratioy;
//	int x, y, sizex, sizey, offx, offy;
//
//	glEnable(triple.target);
//
//	for(y=0, offy=0; y<triple.ny; offy+=triple.y[y], y++) {
//		for(x=0, offx=0; x<triple.nx; offx+=triple.x[x], x++) {
//			sizex= (x == triple.nx-1)? win.sizex-offx: triple.x[x];
//			sizey= (y == triple.ny-1)? win.sizey-offy: triple.y[y];
//
//			/* wmOrtho for the screen has this same offset */
//			ratiox= sizex;
//			ratioy= sizey;
//			halfx= 0.375f;
//			halfy= 0.375f;
//
//			/* texture rectangle has unnormalized coordinates */
//			if(triple.target == GL_TEXTURE_2D) {
//				ratiox /= triple.x[x];
//				ratioy /= triple.y[y];
//				halfx /= triple.x[x];
//				halfy /= triple.y[y];
//			}
//
//			glBindTexture(triple.target, triple.bind[x + y*triple.nx]);
//
//			glColor3f(1.0f, 1.0f, 1.0f);
//			glBegin(GL_QUADS);
//				glTexCoord2f(halfx, halfy);
//				glVertex2f(offx, offy);
//
//				glTexCoord2f(ratiox+halfx, halfy);
//				glVertex2f(offx+sizex, offy);
//
//				glTexCoord2f(ratiox+halfx, ratioy+halfy);
//				glVertex2f(offx+sizex, offy+sizey);
//
//				glTexCoord2f(halfx, ratioy+halfy);
//				glVertex2f(offx, offy+sizey);
//			glEnd();
//		}
//	}
//
//	glBindTexture(triple.target, 0);
//	glDisable(triple.target);
//}
//
//static void wm_triple_copy_textures(wmWindow *win, wmDrawTriple *triple)
//{
//	int x, y, sizex, sizey, offx, offy;
//
//	for(y=0, offy=0; y<triple.ny; offy+=triple.y[y], y++) {
//		for(x=0, offx=0; x<triple.nx; offx+=triple.x[x], x++) {
//			sizex= (x == triple.nx-1)? win.sizex-offx: triple.x[x];
//			sizey= (y == triple.ny-1)? win.sizey-offy: triple.y[y];
//
//			glBindTexture(triple.target, triple.bind[x + y*triple.nx]);
//			glCopyTexSubImage2D(triple.target, 0, 0, 0, offx, offy, sizex, sizey);
//		}
//	}
//
//	glBindTexture(triple.target, 0);
//}

public static void wm_method_draw_triple(GL2 gl, bContext C, wmWindow win)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmDrawTriple triple;
	bScreen screen= win.screen;
	ScrArea sa;
	ARegion ar;
	int copytex= 0;

	if(win.drawdata!=null) {
		gl.glClearColor(0, 0, 0, 0);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);

		WmSubWindow.wmSubWindowSet(gl, win, screen.mainwin);

//		wm_triple_draw_textures(win, win.drawdata);

//		triple= win.drawdata;
	}
	else {
		win.drawdata= new wmDrawTriple();

//		if(!wm_triple_gen_textures(win, win.drawdata)) {
//			wm_draw_triple_fail(C, win);
//			return;
//		}
	}

//	triple= (wmDrawTriple)win.drawdata;

	/* draw marked area regions */
	for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
		bContext.CTX_wm_area_set(C, sa);

		for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if(ar.swinid!=0 && ar.do_draw!=0) {
				bContext.CTX_wm_region_set(C, ar);
				Area.ED_region_do_draw(gl, C, ar);
				Area.ED_area_overdraw_flush(sa, ar);
				bContext.CTX_wm_region_set(C, null);
				copytex= 1;
			}
		}

		bContext.CTX_wm_area_set(C, null);
	}

	if(copytex!=0) {
		WmSubWindow.wmSubWindowSet(gl, win, screen.mainwin);
		Area.ED_area_overdraw(gl, C);

//		wm_triple_copy_textures(win, triple);
	}

	/* after area regions so we can do area 'overlay' drawing */
	ScreenEdit.ED_screen_draw(gl, win);

	/* draw overlapping regions */
	for(ar=(ARegion)screen.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.swinid!=0) {
			bContext.CTX_wm_menu_set(C, ar);
			Area.ED_region_do_draw(gl, C, ar);
			bContext.CTX_wm_menu_set(C, null);
		}
	}

//	if(win.screen.do_draw_gesture)
//		wm_gesture_draw(win);

	if(wm.paintcursors.first!=null) {
		for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next) {
			for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
				if(ar.swinid == screen.subwinactive) {
					bContext.CTX_wm_area_set(C, sa);
					bContext.CTX_wm_region_set(C, ar);

					/* make region ready for draw, scissor, pixelspace */
					Area.ED_region_set(gl, C, ar);
					wm_paintcursor_draw(C, ar);

					bContext.CTX_wm_region_set(C, null);
					bContext.CTX_wm_area_set(C, null);
				}
			}
		}

		WmSubWindow.wmSubWindowSet(gl, win, screen.mainwin);
	}
}

/****************** main update call **********************/

/* quick test to prevent changing window drawable */
public static boolean wm_draw_update_test_window(wmWindow win)
{
//	ScrArea sa;
//	ARegion ar;
//	
////	for(ar= (ARegion)win.screen.regionbase.first; ar!=null; ar= ar.next) {
////		if(ar.do_draw_overlay) {
////			wm_tag_redraw_overlay(win, ar);
////			ar.do_draw_overlay= 0;
////		}
////	}
//
//	if(win.screen.do_refresh!=0)
//		return true;
//	if(win.screen.do_draw!=0)
//		return true;
//	if(win.screen.do_draw_gesture!=0)
//		return true;
//	if(win.screen.do_draw_paintcursor!=0)
//		return true;
////	if(win.screen.do_draw_drag)
////		return true;
//
//	for(ar= (ARegion)win.screen.regionbase.first; ar!=null; ar= ar.next)
//		if(ar.swinid!=0 && ar.do_draw!=0)
//			return true;
//
//	for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next)
//		for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
//			if(ar.swinid!=0 && ar.do_draw!=0)
//				return true;
//
//	return false;
	
    return true; // TMP
}

public static void wm_draw_update(GL2 gl, bContext C)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmWindow win;
//	int drawmethod;
	
//	GPU_free_unused_buffers();
	
	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
		if(win.drawmethod != U.wmdrawmethod) {
			wm_draw_window_clear(win);
			win.drawmethod= U.wmdrawmethod;
		}

		if(wm_draw_update_test_window(win)) {
			bContext.CTX_wm_window_set(C, win);
			
			/* sets context window+screen */
			WmWindow.wm_window_make_drawable(C, win);

			/* notifiers for screen redraw */
			if(win.screen.do_refresh!=0)
				ScreenEdit.ED_screen_refresh(gl, wm, win);
//				ScreenEdit.ED_screen_refresh(wm, win);
			
//			drawmethod= wm_automatic_draw_method(win);

//			if(win.drawfail!=0)
//				wm_method_draw_overlap_all(gl, C, win);
//			else if(win.drawmethod == UserDefTypes.USER_DRAW_FULL)
				wm_method_draw_full(gl, C, win);
//			else if(win.drawmethod == UserDefTypes.USER_DRAW_OVERLAP)
//				wm_method_draw_overlap_all(gl, C, win);
			/*else if(win.drawmethod == USER_DRAW_DAMAGE)
				wm_method_draw_damage(C, win);*/
//			else // if(win.drawmethod == USER_DRAW_TRIPLE)
//				wm_method_draw_triple(gl, C, win);

			win.screen.do_draw_gesture= 0;
			win.screen.do_draw_paintcursor= 0;
//			win.screen.do_draw_drag= 0;

			WmWindow.wm_window_swap_buffers(win);

			bContext.CTX_wm_window_set(C, null);
		}
	}
}

public static void wm_draw_window_clear(wmWindow win)
{
	bScreen screen= win.screen;
	ScrArea sa;
	ARegion ar;
//	int drawmethod= wm_automatic_draw_method(win);

	if(win.drawmethod == UserDefTypes.USER_DRAW_TRIPLE)
		wm_draw_triple_free(win);

	/* clear screen swap flags */
	if(screen!=null) {
		for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
			for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
				ar.swap= WIN_NONE_OK;
		
		screen.swap= WIN_NONE_OK;
	}
}

public static void wm_draw_region_clear(wmWindow win, ARegion ar)
{
//	int drawmethod= wm_automatic_draw_method(win);
	
	if(win.drawmethod == UserDefTypes.USER_DRAW_OVERLAP)
		wm_flush_regions_down(win.screen, ar.winrct);

	win.screen.do_draw= 1;
}

//void wm_draw_region_modified(wmWindow *win, ARegion *ar)
//{
//	int drawmethod= wm_automatic_draw_method(win);
//
//	if(ELEM(drawmethod, USER_DRAW_OVERLAP, USER_DRAW_OVERLAP_FLIP))
//		ED_region_tag_redraw(ar);
//}

}

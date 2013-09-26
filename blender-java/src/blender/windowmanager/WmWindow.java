/**
 * $Id: wm_window.c
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
 * The Original Code is Copyright (C) 2007 Blender Foundation but based 
 * on ghostwinlay.c (C) 2001-2002 by NaN Holding BV
 * All rights reserved.
 *
 * Contributor(s): Blender Foundation, 2008
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

import static blender.blenkernel.Blender.G;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenkernel.bContext;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.blenlib.Time;
import blender.editors.screen.ScreenEdit;
import blender.ghost.GhostAPI;
import blender.ghost.GhostEventCallbackProcPtr;
import blender.ghost.GhostTypes.GHOST_TEventType;
import blender.ghost.GhostDrawingContextTypeOpenGL;
import blender.makesdna.ScreenTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmTypes.wmEvent;
import blender.windowmanager.WmTypes.wmTimer;

public class WmWindow {

/* the global to talk to ghost */
private static Object g_system;

/* set by commandline */
static int[] prefsizx={0}, prefsizy={0}, prefstax={0}, prefstay={0}/*, initialstate= GHOST_kWindowStateNormal*/;
static short useprefsize= 0;

/* ******** win open & close ************ */

/* XXX this one should correctly check for apple top header...
done for Cocoa : returns window contents (and not frame) max size*/
public static void wm_get_screensize(int[] width_r, int[] height_r)
{
	int[] uiwidth = {0};
	int[] uiheight = {0};

	GhostAPI.GHOST_GetMainDisplayDimensions(g_system, uiwidth, uiheight);
	width_r[0]= uiwidth[0];
	height_r[0]= uiheight[0];
}

///* keeps offset and size within monitor bounds */
///* XXX solve dual screen... */
//static void wm_window_check_position(rcti *rect)
//{
//	int width, height, d;
//
//	wm_get_screensize(&width, &height);
//
//#ifdef __APPLE__
//	height -= 42;
//#endif
//
//	if(rect.xmin < 0) {
//		d= rect.xmin;
//		rect.xmax -= d;
//		rect.xmin -= d;
//	}
//	if(rect.ymin < 0) {
//		d= rect.ymin;
//		rect.ymax -= d;
//		rect.ymin -= d;
//	}
//	if(rect.xmax > width) {
//		d= rect.xmax - width;
//		rect.xmax -= d;
//		rect.xmin -= d;
//	}
//	if(rect.ymax > height) {
//		d= rect.ymax - height;
//		rect.ymax -= d;
//		rect.ymin -= d;
//	}
//
//	if(rect.xmin < 0) rect.xmin= 0;
//	if(rect.ymin < 0) rect.ymin= 0;
//}


public static void wm_ghostwindow_destroy(wmWindow win)
{
	if(win.ghostwin!=null) {
		GhostAPI.GHOST_DisposeWindow(g_system, win.ghostwin);
		win.ghostwin= null;
	}
}

/* including window itself, C can be NULL.
   ED_screen_exit should have been called */
public static void wm_window_free(bContext C, wmWindowManager wm, wmWindow win)
{
	wmTimer wt, wtnext;

	/* update context */
	if(C!=null) {
//		wmWindowManager wm= bContext.CTX_wm_manager(C);
		WmEventSystem.WM_event_remove_handlers(C, win.handlers);
//		WmEventSystem.WM_event_remove_handlers(C, win.modalhandlers);

//		if(wm.windrawable==win)
//			wm.windrawable= null;
//		if(wm.winactive==win)
//			wm.winactive= null;
		
		if(bContext.CTX_wm_window(C)==win)
			bContext.CTX_wm_window_set(C, null);
	}
	
	/* always set drawable and active to NULL, prevents non-drawable state of main windows (bugs #22967 and #25071, possibly #22477 too) */
	wm.windrawable= null;
	wm.winactive= null;

//	/* end running jobs, a job end also removes its timer */
//	for(wt= wm->timers.first; wt; wt= wtnext) {
//		wtnext= wt->next;
//		if(wt->win==win && wt->event_type==TIMERJOBS)
//			wm_jobs_timer_ended(wm, wt);
//	}

	/* timer removing, need to call this api function */
	for(wt= (wmTimer)wm.timers.first; wt!=null; wt=wtnext) {
		wtnext= wt.next;
		if(wt.win==win)
			WM_event_remove_timer(wm, win, wt);
	}
	
//	if(win->eventstate) MEM_freeN(win->eventstate);

//	wm_event_free_all(win);
	WmSubWindow.wm_subwindows_free(win);

//	if(win.drawdata)
//		MEM_freeN(win.drawdata);

	wm_ghostwindow_destroy(win);

//	MEM_freeN(win);
}

static int find_free_winid(wmWindowManager wm)
{
	wmWindow win;
	int id= 1;

	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next)
		if(id <= win.winid)
			id= win.winid+1;

	return id;
}

/* dont change context itself */
public static wmWindow wm_window_new(bContext C)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmWindow win= new wmWindow();

	ListBaseUtil.BLI_addtail(wm.windows, win);
	win.winid= find_free_winid(wm);

	return win;
}


/* part of wm_window.c api */
public static wmWindow wm_window_copy(bContext C, wmWindow winorig)
{
	wmWindow win= wm_window_new(C);

	win.posx= (short)(winorig.posx+10);
	win.posy= winorig.posy;
	win.sizex= winorig.sizex;
	win.sizey= winorig.sizey;

	/* duplicate assigns to window */
	ScreenEdit.ED_screen_duplicate(win, winorig.screen);
	StringUtil.BLI_strncpy(win.screenname,0, win.screen.id.name,2, win.screenname.length);
	win.screen.winid= (short)win.winid;
	
	win.screen.do_refresh= 1;
	win.screen.do_draw= 1;

	win.drawmethod= -1;
	win.drawdata= null;

	return win;
}

/* this is event from ghost, or exit-blender op */
public static void wm_window_close(bContext C, wmWindowManager wm, wmWindow win)
{
//	wmWindowManager wm= bContext.CTX_wm_manager(C);
	ListBaseUtil.BLI_remlink(wm.windows, win);

	WmDraw.wm_draw_window_clear(win);
	bContext.CTX_wm_window_set(C, win);	/* needed by handlers */
	WmEventSystem.WM_event_remove_handlers(C, win.handlers);
//	WmEventSystem.WM_event_remove_handlers(C, win.modalhandlers);
	ScreenEdit.ED_screen_exit(C, win, win.screen);
	
//	/* if temp screen, delete it */
//	if(win.screen.temp) {
//		Main bmain= bContext.CTX_data_main(C);
//		free_libblock(bmain.screen, win.screen);
//	}
	
	wm_window_free(C, wm, win);

	/* check remaining windows */
	if(wm.windows.first!=null) {
		for(win= (wmWindow)wm.windows.first; win!=null; win= win.next)
			if(win.screen.full!=ScreenTypes.SCREENTEMP)
				break;
		/* in this case we close all */
		if(win==null)
			//WmInitExit.WM_exit(C);
			bContext.CTX_wm_manager_close(C);
	}
	else
		//WmInitExit.WM_exit(C);
		bContext.CTX_wm_manager_close(C);
}

public static void wm_window_title(bContext C, wmWindowManager wm, wmWindow win)
{
	/* handle the 'temp' window */
	if(win.screen!=null && win.screen.temp!=0) {
		GhostAPI.GHOST_SetTitle(win.ghostwin, "Blender");
	}
	else {
		
		/* this is set to 1 if you don't have startup.blend open */
		//if(G.save_over!=0 && G.main.name[0]!=0) {
		if(G.save_over!=0 && bContext.wm_title(C)!="") {
//			char str[sizeof(G.main.name) + 12];
//			BLI_snprintf(str, sizeof(str), "Blender%s [%s]", wm.file_saved ? "":"*", G.main.name);
			//String str = String.format("Blender%s [%s]", wm.file_saved!=0 ? "":"*", StringUtil.toJString(G.main.name,0));
			String str = String.format("Blender%s [%s]", wm.file_saved!=0 ? "":"*", bContext.wm_title(C));
			GhostAPI.GHOST_SetTitle(win.ghostwin, str);
		}
		else
			GhostAPI.GHOST_SetTitle(win.ghostwin, "Blender");

		/* Informs GHOST of unsaved changes, to set window modified visual indicator (MAC OS X)
		 and to give hint of unsaved changes for a user warning mechanism
		 in case of OS application terminate request (e.g. OS Shortcut Alt+F4, Cmd+Q, (...), or session end) */
//		GHOST_SetWindowModifiedState(win.ghostwin, (GHOST_TUns8)!wm.file_saved);
		
//#if defined(__APPLE__) && !defined(GHOST_COCOA)
//		if(wm->file_saved)
//			GHOST_SetWindowState(win->ghostwin, GHOST_kWindowStateUnModified);
//		else
//			GHOST_SetWindowState(win->ghostwin, GHOST_kWindowStateModified);
//#endif
	}
}

/* belongs to below */
private static void wm_window_add_ghostwindow(bContext C, wmWindowManager wm, String title, wmWindow win)
{
    Object ghostwin;
	int[] scr_w={0}, scr_h={0};
	int posy;
	Object initial_state = null;
	
	/* when there is no window open uses the initial state */
//	if(bContext.CTX_wm_window(C)==null)
//		initial_state= initialstate;
//	else
//		initial_state= GHOST_kWindowStateNormal;
	
	WmWindow.wm_get_screensize(scr_w, scr_h);
	posy= (scr_h[0] - win.posy - win.sizey);
    
	/* Disable AA for now, as GL_SELECT (used for border, lasso, ... select)
	 doesn't work well when AA is initialized, even if not used. */
	ghostwin= GhostAPI.GHOST_CreateWindow(g_system, title, 
		 win.posx, posy, win.sizex, win.sizey, 
		 initial_state, 
		 //GhostAPI.GHOST_kDrawingContextTypeOpenGL,
		 new GhostDrawingContextTypeOpenGL(C),
		 0 /* no stereo */,
		 0 /* no AA */);

	if (ghostwin!=null) {
		/* needed so we can detect the graphics card below */
//		GPU_extensions_init();
		
		/* set the state*/
//		GHOST_SetWindowState(ghostwin, initial_state);
		
	    win.ghostwin= ghostwin;
	    GhostAPI.GHOST_SetWindowUserData(ghostwin, win); /* pointer back */

	    if(win.eventstate==null)
	        win.eventstate= new wmEvent();
	    
//	    /* until screens get drawn, make it nice grey */
//		glClearColor(.55, .55, .55, 0.0);
//		/* Crash on OSS ATI: bugs.launchpad.net/ubuntu/+source/mesa/+bug/656100 */
//		if(!GPU_type_matches(GPU_DEVICE_ATI, GPU_OS_UNIX, GPU_DRIVER_OPENSOURCE)) {
//			glClear(GL_COLOR_BUFFER_BIT);
//		}
//
//		wm_window_swap_buffers(win);
//		
//		//GHOST_SetWindowState(ghostwin, GHOST_kWindowStateModified);
//		
//		/* standard state vars for window */
//		glEnable(GL_SCISSOR_TEST);
//		
//		GPU_state_init();
	    
	    // TMP
	    GhostAPI.GHOST_ShowWindow(g_system, ghostwin);
	}
}

/* for wmWindows without ghostwin, open these and clear */
/* window size is read from window, if 0 it uses prefsize */
/* called in wm_check, also inits stuff after file read */
public static void wm_window_add_ghostwindows(bContext C, wmWindowManager wm)
{
	wmKeyMap keymap;
	wmWindow win;

	/* no commandline prefsize? then we set this.
	 * Note that these values will be used only
	 * when there is no startup.blend yet.
	 */
	if (prefsizx[0]==0) {
		wm_get_screensize(prefsizx, prefsizy);
		prefstax[0]= 0;
		prefstay[0]= 0;
	}

	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
		if(win.ghostwin==null) {
			if(win.sizex==0) {
				win.posx= (short)prefstax[0];
				win.posy= (short)prefstay[0];
				win.sizex= (short)prefsizx[0];
				win.sizey= (short)prefsizy[0];
				win.windowstate= 0;
			}
			wm_window_add_ghostwindow(C, wm, "Blender", win);
		}
		/* happens after fileread */
		if(win.eventstate==null)
		   win.eventstate= new wmEvent();

		/* add keymap handlers (1 handler for all keys in map!) */
		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Window", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(win.handlers, keymap);

		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Screen", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(win.handlers, keymap);
		
		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Screen Editing", 0, 0);
		WmEventSystem.WM_event_add_keymap_handler(win.handlers, keymap);

//		/* add drop boxes */
//		{
//			ListBase lb= WM_dropboxmap_find("Window", 0, 0);
//			WM_event_add_dropbox_handler(win.handlers, lb);
//		}
		wm_window_title(C, wm, win);
	}
}

/* new window, no screen yet, but we open ghostwindow for it */
/* also gets the window level handlers */
/* area-rip calls this */
public static wmWindow WM_window_open(bContext C, rcti rect)
{
	wmWindow win= wm_window_new(C);

	win.posx= (short)rect.xmin;
	win.posy= (short)rect.ymin;
	win.sizex= (short)(rect.xmax - rect.xmin);
	win.sizey= (short)(rect.ymax - rect.ymin);

	win.drawmethod= -1;
	win.drawdata= null;

	Wm.wm_check(C);

	return win;
}

///* uses screen.full tag to define what to do, currently it limits
//   to only one "temp" window for render out, preferences, filewindow, etc */
///* type is #define in WM_api.h */
//
//void WM_window_open_temp(bContext *C, rcti *position, int type)
//{
//	wmWindow *win;
//	ScrArea *sa;
//
//	/* changes rect to fit within desktop */
//	wm_window_check_position(position);
//
//	/* test if we have a temp screen already */
//	for(win= CTX_wm_manager(C).windows.first; win; win= win.next)
//		if(win.screen.full == SCREENTEMP)
//			break;
//
//	/* add new window? */
//	if(win==NULL) {
//		win= wm_window_new(C);
//
//		win.posx= position.xmin;
//		win.posy= position.ymin;
//	}
//
//	win.sizex= position.xmax - position.xmin;
//	win.sizey= position.ymax - position.ymin;
//
//	if(win.ghostwin) {
//		wm_window_set_size(win, win.sizex, win.sizey) ;
//		wm_window_raise(win);
//	}
//
//	/* add new screen? */
//	if(win.screen==NULL)
//		win.screen= ED_screen_add(win, CTX_data_scene(C), "temp");
//	win.screen.full = SCREENTEMP;
//
//	/* make window active, and validate/resize */
//	CTX_wm_window_set(C, win);
//	wm_check(C);
//
//	/* ensure it shows the right spacetype editor */
//	sa= win.screen.areabase.first;
//	CTX_wm_area_set(C, sa);
//
//	if(type==WM_WINDOW_RENDER) {
//		ED_area_newspace(C, sa, SPACE_IMAGE);
//	}
//	else {
//		ED_area_newspace(C, sa, SPACE_INFO);
//	}
//
//	ED_screen_set(C, win.screen);
//
//	if(sa.spacetype==SPACE_IMAGE)
//		GHOST_SetTitle(win.ghostwin, "Blender Render");
//	else if(ELEM(sa.spacetype, SPACE_OUTLINER, SPACE_INFO))
//		GHOST_SetTitle(win.ghostwin, "Blender User Preferences");
//	else if(sa.spacetype==SPACE_FILE)
//		GHOST_SetTitle(win.ghostwin, "Blender File View");
//	else
//		GHOST_SetTitle(win.ghostwin, "Blender");
//}


/* ****************** Operators ****************** */

/* operator callback */
public static wmOperatorType.Operator wm_window_duplicate_op = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	wm_window_copy(C, bContext.CTX_wm_window(C));
	Wm.wm_check(C);
	
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW|WmTypes.NA_ADDED, null);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};


/* fullscreen operator callback */
public static wmOperatorType.Operator wm_window_fullscreen_toggle_op = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	wmWindow *window= CTX_wm_window(C);
//	GHOST_TWindowState state = GHOST_GetWindowState(window.ghostwin);
//	if(state!=GHOST_kWindowStateFullScreen)
//		GHOST_SetWindowState(window.ghostwin, GHOST_kWindowStateFullScreen);
//	else
//		GHOST_SetWindowState(window.ghostwin, GHOST_kWindowStateNormal);

	return WindowManagerTypes.OPERATOR_FINISHED;

}};


/* ************ events *************** */

//static int query_qual(char qual)
//{
//	GHOST_TModifierKeyMask left, right;
//	int val= 0;
//
//	if (qual=='s') {
//		left= GHOST_kModifierKeyLeftShift;
//		right= GHOST_kModifierKeyRightShift;
//	} else if (qual=='c') {
//		left= GHOST_kModifierKeyLeftControl;
//		right= GHOST_kModifierKeyRightControl;
//	} else if (qual=='C') {
//		left= right= GHOST_kModifierKeyCommand;
//	} else {
//		left= GHOST_kModifierKeyLeftAlt;
//		right= GHOST_kModifierKeyRightAlt;
//	}
//
//	GHOST_GetModifierKeyState(g_system, left, &val);
//	if (!val)
//		GHOST_GetModifierKeyState(g_system, right, &val);
//
//	return val;
//}

public static void wm_window_make_drawable(bContext C, wmWindow win)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);

	if (win != wm.windrawable && win.ghostwin!=null) {
//		win.lmbut= 0;	/* keeps hanging when mousepressed while other window opened */

		wm.windrawable= win;
//		if(G.f & G_DEBUG) printf("set drawable %d\n", win.winid);
//		GHOST_ActivateWindowDrawingContext(win.ghostwin);
	}
}

/* called by ghost, here we handle events for windows themselves or send to event system */
public static GhostEventCallbackProcPtr ghost_event_proc = new GhostEventCallbackProcPtr() {
public int run(Object evt, Object privateObj)
{
	bContext C= (bContext)privateObj;
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	GHOST_TEventType type= GhostAPI.GHOST_GetEventType(evt);

	if (type == GHOST_TEventType.GHOST_kEventQuit) {
		//WmInitExit.WM_exit(C);
		bContext.CTX_wm_manager_close(C);
	} else {
		Object ghostwin= GhostAPI.GHOST_GetEventWindow(evt);
		Object data= GhostAPI.GHOST_GetEventData(evt);
		wmWindow win;

		if (ghostwin==null) {
			// XXX - should be checked, why are we getting an event here, and
			//	what is it?

			return 1;
		} else if (!GhostAPI.GHOST_ValidWindow(g_system, ghostwin)) {
			// XXX - should be checked, why are we getting an event here, and
			//	what is it?

			return 1;
		} else {
			win= (wmWindow)GhostAPI.GHOST_GetWindowUserData(ghostwin);
		}

		switch(type) {
			case GHOST_kEventWindowDeactivate:
				win.active= 0; /* XXX */
				break;
			case GHOST_kEventWindowActivate:
			{
//				GHOST_TEventKeyData kdata;
//				int cx, cy, wx, wy;

				bContext.CTX_wm_manager(C).winactive= win; /* no context change! c.wm.windrawable is drawable, or for area queues */

				win.active= 1;
//				window_handle(win, WmEventTypes.INPUTCHANGE, win.active);

//				/* bad ghost support for modifier keys... so on activate we set the modifiers again */
//				kdata.ascii= 0;
//				if (win.eventstate.shift && !query_qual('s')) {
//					kdata.key= GHOST_kKeyLeftShift;
//					wm_event_add_ghostevent(win, GHOST_kEventKeyUp, &kdata);
//				}
//				if (win.eventstate.ctrl && !query_qual('c')) {
//					kdata.key= GHOST_kKeyLeftControl;
//					wm_event_add_ghostevent(win, GHOST_kEventKeyUp, &kdata);
//				}
//				if (win.eventstate.alt && !query_qual('a')) {
//					kdata.key= GHOST_kKeyLeftAlt;
//					wm_event_add_ghostevent(win, GHOST_kEventKeyUp, &kdata);
//				}
//				if (win.eventstate.oskey && !query_qual('C')) {
//					kdata.key= GHOST_kKeyCommand;
//					wm_event_add_ghostevent(win, GHOST_kEventKeyUp, &kdata);
//				}
//
//				/* entering window, update mouse pos. but no event */
//				GHOST_GetCursorPosition(g_system, &wx, &wy);
//
//				GHOST_ScreenToClient(win.ghostwin, wx, wy, &cx, &cy);
//				win.eventstate.x= cx;
//				win.eventstate.y= (win.sizey-1) - cy;

				wm_window_make_drawable(C, win);
				break;
			}
			case GHOST_kEventWindowClose: {
				wm_window_close(C, wm, win);
				break;
			}
			case GHOST_kEventWindowUpdate: {
//				if(G.f & G_DEBUG) printf("ghost redraw\n");

				wm_window_make_drawable(C, win);
				WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

				break;
			}
			case GHOST_kEventWindowSize:
			case GHOST_kEventWindowMove: {
//				GHOST_TWindowState state;
//				state = GHOST_GetWindowState(win.ghostwin);
//
//				 /* win32: gives undefined window size when minimized */
//				if(state!=GHOST_kWindowStateMinimized) {
//					GHOST_RectangleHandle client_rect;
//					int l, t, r, b, scr_w, scr_h;
//
//					client_rect= GHOST_GetClientBounds(win.ghostwin);
//					GHOST_GetRectangle(client_rect, &l, &t, &r, &b);
//
//					GHOST_DisposeRectangle(client_rect);
//
//					wm_get_screensize(&scr_w, &scr_h);
//					win.sizex= r-l;
//					win.sizey= b-t;
//					win.posx= l;
//					win.posy= scr_h - t - win.sizey;
//
//					/* debug prints */
//					if(0) {
//						state = GHOST_GetWindowState(win.ghostwin);
//
//						if(state==GHOST_kWindowStateNormal) {
//							if(G.f & G_DEBUG) printf("window state: normal\n");
//						}
//						else if(state==GHOST_kWindowStateMinimized) {
//							if(G.f & G_DEBUG) printf("window state: minimized\n");
//						}
//						else if(state==GHOST_kWindowStateMaximized) {
//							if(G.f & G_DEBUG) printf("window state: maximized\n");
//						}
//						else if(state==GHOST_kWindowStateFullScreen) {
//							if(G.f & G_DEBUG) printf("window state: fullscreen\n");
//						}
//
//						if(type!=GHOST_kEventWindowSize) {
//							if(G.f & G_DEBUG) printf("win move event pos %d %d size %d %d\n", win.posx, win.posy, win.sizex, win.sizey);
//						}
//
//					}

//					wm_window_make_drawable(C, win);
//					wm_draw_window_clear(win);
//					WM_event_add_notifier(C, NC_SCREEN|NA_EDITED, NULL);
//				}
				break;
			}
			default:
				WmEventSystem.wm_event_add_ghostevent(win, type, data);
				break;
		}

	}
	return 1;
}};


/* This timer system only gives maximum 1 timer event per redraw cycle,
to prevent queues to get overloaded. 
Timer handlers should check for delta to decide if they just
update, or follow real time.
Timer handlers can also set duration to match frames passed
*/
static int wm_window_timer(bContext C)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmTimer wt, wtnext;
	wmWindow win;
	double time= Time.PIL_check_seconds_timer();
	int retval= 0;
	
	for(wt= (wmTimer)wm.timers.first; wt!=null; wt= wtnext) {
		wtnext= wt.next; /* in case timer gets removed */
		win= wt.win;

		if(wt.sleep==0) {
			if(time > wt.ntime) {
				wt.delta= time - wt.ltime;
				wt.duration += wt.delta;
				wt.ltime= time;
				wt.ntime= wt.stime + wt.timestep*Math.ceil(wt.duration/wt.timestep);

//				if(wt.event_type == WmEventTypes.TIMERJOBS)
//					wm_jobs_timer(C, wm, wt);
//				else if(wt.event_type == WmEventTypes.TIMERAUTOSAVE)
//					wm_autosave_timer(C, wm, wt);
//				else
					if(win!=null) {
					wmEvent event= WmEventSystem.eventCopy(win.eventstate);
					
					event.type= (short)wt.event_type;
					event.custom= WmEventTypes.EVT_DATA_TIMER;
					event.customdata= wt;
					WmEventSystem.wm_event_add(win, event);

					retval= 1;
				}
			}
		}
	}
	return retval;
}

public static void wm_window_process_events(bContext C)
{
//	int hasevent= GHOST_ProcessEvents(g_system, 0);	/* 0 is no wait */
//
//	if(hasevent)
//		GHOST_DispatchEvents(g_system);
//
//	hasevent |= wm_window_timer(C);
	wm_window_timer(C);
//
//	/* no event, we sleep 5 milliseconds */
//	if(hasevent==0)
//		PIL_sleep_ms(5);
}

//void wm_window_process_events_nosleep(const bContext *C)
//{
//	if(GHOST_ProcessEvents(g_system, 0))
//		GHOST_DispatchEvents(g_system);
//}
//
///* exported as handle callback to bke blender.c */
//void wm_window_testbreak(void)
//{
//	static double ltime= 0;
//	double curtime= PIL_check_seconds_timer();
//
//	/* only check for breaks every 50 milliseconds
//		* if we get called more often.
//		*/
//	if ((curtime-ltime)>.05) {
//		int hasevent= GHOST_ProcessEvents(g_system, 0);	/* 0 is no wait */
//
//		if(hasevent)
//			GHOST_DispatchEvents(g_system);
//
//		ltime= curtime;
//	}
//}

/* **************** init ********************** */

public static void wm_ghost_init(bContext C)
{
	if (g_system==null) {
		Object consumer= GhostAPI.GHOST_CreateEventConsumer(ghost_event_proc, C);
		g_system= GhostAPI.GHOST_CreateSystem();
		GhostAPI.GHOST_AddEventConsumer(g_system, consumer);
	}
}

/* **************** timer ********************** */

/* to (de)activate running timers temporary */
//void WM_event_window_timer_sleep(wmWindow *win, wmTimer *timer, int dosleep)
//{
//	wmTimer *wt;
//
//	for(wt= win.timers.first; wt; wt= wt.next)
//		if(wt==timer)
//			break;
//	if(wt) {
//		wt.sleep= dosleep;
//	}
//}

public static wmTimer WM_event_add_timer(wmWindowManager wm, wmWindow win, int event_type, double timestep)
{
	wmTimer wt= new wmTimer();
	
	wt.event_type= event_type;
	wt.ltime= Time.PIL_check_seconds_timer();
	wt.ntime= wt.ltime + timestep;
	wt.stime= wt.ltime;
	wt.timestep= timestep;
	wt.win= win;
	
	ListBaseUtil.BLI_addtail(wm.timers, wt);
	
	return wt;
}

public static void WM_event_remove_timer(wmWindowManager wm, wmWindow win, wmTimer timer)
{
	wmTimer wt;
	
	/* extra security check */
	for(wt= (wmTimer)wm.timers.first; wt!=null; wt= wt.next)
		if(wt==timer)
			break;
	if(wt!=null) {
		
		ListBaseUtil.BLI_remlink(wm.timers, wt);
		if(wt.customdata!=null) {
			//MEM_freeN(wt.customdata);
			wt.customdata = null;
		}
		//MEM_freeN(wt);
	}
}

/* ******************* clipboard **************** */

//char *WM_clipboard_text_get(int selection)
//{
//	char *p, *p2, *buf, *newbuf;
//
//	buf= (char*)GHOST_getClipboard(selection);
//	if(!buf)
//		return NULL;
//
//	/* always convert from \r\n to \n */
//	newbuf= MEM_callocN(strlen(buf)+1, "WM_clipboard_text_get");
//
//	for(p= buf, p2= newbuf; *p; p++) {
//		if(*p != '\r')
//			*(p2++)= *p;
//	}
//	*p2= '\0';
//
//	free(buf); /* ghost uses regular malloc */
//
//	return newbuf;
//}
//
//void WM_clipboard_text_set(char *buf, int selection)
//{
//#ifdef _WIN32
//	/* do conversion from \n to \r\n on Windows */
//	char *p, *p2, *newbuf;
//	int newlen= 0;
//
//	for(p= buf; *p; p++) {
//		if(*p == '\n')
//			newlen += 2;
//		else
//			newlen++;
//	}
//
//	newbuf= MEM_callocN(newlen+1, "WM_clipboard_text_set");
//
//	for(p= buf, p2= newbuf; *p; p++, p2++) {
//		if(*p == '\n') {
//			*(p2++)= '\r'; *p2= '\n';
//		}
//		else *p2= *p;
//	}
//	*p2= '\0';
//
//	GHOST_putClipboard((GHOST_TInt8*)newbuf, selection);
//	MEM_freeN(newbuf);
//#else
//	GHOST_putClipboard((GHOST_TInt8*)buf, selection);
//#endif
//}
//
///* ************************************ */
//
//void wm_window_get_position(wmWindow *win, int *posx_r, int *posy_r)
//{
//	*posx_r= win.posx;
//	*posy_r= win.posy;
//}

public static void wm_window_get_size(wmWindow win, int[] width_r, int[] height_r)
{
	width_r[0]= win.sizex;
	height_r[0]= win.sizey;
}

//void wm_window_set_size(wmWindow *win, int width, int height)
//{
//	GHOST_SetClientSize(win.ghostwin, width, height);
//}
//
//void wm_window_lower(wmWindow *win)
//{
//	GHOST_SetWindowOrder(win.ghostwin, GHOST_kWindowOrderBottom);
//}
//
//void wm_window_raise(wmWindow *win)
//{
//	GHOST_SetWindowOrder(win.ghostwin, GHOST_kWindowOrderTop);
//}

public static void wm_window_swap_buffers(wmWindow win)
{
	GL2 gl = (GL2)GLU.getCurrentGL();
//#ifdef WIN32
	gl.glDisable(GL2.GL_SCISSOR_TEST);
//	gl.glFinish();
	GhostAPI.GHOST_SwapWindowBuffers(win.ghostwin);
	gl.glEnable(GL2.GL_SCISSOR_TEST);
//#else
//	GHOST_SwapWindowBuffers(win->ghostwin);
//#endif
}

/* ******************* exported api ***************** */


/* called whem no ghost system was initialized */
public static void wm_setprefsize(int stax, int stay, int sizx, int sizy)
{
	prefstax[0]= stax;
	prefstay[0]= stay;
	prefsizx[0]= sizx;
	prefsizy[0]= sizy;
}

}

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

//#include "DNA_windowmanager_types.h"

import static blender.blenkernel.Blender.G;

import javax.media.opengl.GL2;

import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.MenuType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.ScreenEdit;
import blender.editors.space_api.SpaceTypeUtil;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RNATypes.PointerRNA;

//
//#include "MEM_guardedalloc.h"
//
//#include "GHOST_C-api.h"
//
//#include "BLI_blenlib.h"
//
//#include "BKE_blender.h"
//#include "BKE_context.h"
//#include "BKE_idprop.h"
//#include "BKE_library.h"
//#include "BKE_main.h"
//#include "BKE_report.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//#include "wm_window.h"
//#include "wm_event_system.h"
//#include "wm_event_types.h"
//#include "wm_draw.h"
//#include "wm.h"
//
//#include "ED_screen.h"
//
//#include "RNA_types.h"

public class Wm {

/* ****************************************************** */

public static final int MAX_OP_REGISTERED=	32;

public static void WM_operator_free(wmOperator op)
{
//	if(op.py_instance) {
//		/* do this first incase there are any __del__ functions or
//		 * similar that use properties */
//		BPY_DECREF(op.py_instance);
//	}
	
	if(op.ptr!=null) {
		op.properties= (IDProperty)((PointerRNA)op.ptr).data;
//		MEM_freeN(op.ptr);
        op.ptr = null;
	}

	if(op.properties!=null) {
//		IDP_FreeProperty(op.properties);
//		MEM_freeN(op.properties);
        op.properties = null;
	}

	if(op.reports!=null /*&& (op.reports.flag & RPT_FREE)!=0*/) {
//		BKE_reports_clear(op.reports);
//		MEM_freeN(op.reports);
        op.reports = null;
	}
	
//	if(op.macro.first) {
//		wmOperator *opm, *opmnext;
//		for(opm= op->macro.first; opm; opm= opmnext) {
//			opmnext = opm->next;
//			WM_operator_free(opm);
//		}
//	}

//	MEM_freeN(op);
}

//static void wm_reports_free(wmWindowManager *wm)
//{
//	BKE_reports_clear(&wm->reports);
//	WM_event_remove_timer(wm, NULL, wm->reports.reporttimer);
//}

/* all operations get registered in the windowmanager here */
/* called on event handling by event_system.c */
public static void wm_operator_register(bContext C, wmOperator op)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	int tot;

	ListBaseUtil.BLI_addtail(wm.operators, op);
	tot= ListBaseUtil.BLI_countlist(wm.operators);

	while(tot>MAX_OP_REGISTERED) {
		wmOperator opt= (wmOperator)wm.operators.first;
		ListBaseUtil.BLI_remlink(wm.operators, opt);
//		WM_operator_free(opt);
		tot--;
	}

	/* so the console is redrawn */
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SPACE|WmTypes.ND_SPACE_INFO_REPORT, null);
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WM|WmTypes.ND_HISTORY, null);
}


//void WM_operator_stack_clear(bContext *C)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmOperator *op;
//
//	while((op= wm.operators.first)) {
//		BLI_remlink(&wm.operators, op);
//		WM_operator_free(op);
//	}
//
//}

/* ****************************************** */

static ListBaseUtil.OffsetOf MenuType_idname = new ListBaseUtil.OffsetOf() {
public String get(Link link) {
	return StringUtil.toJString(((MenuType)link).idname,0);
}};

static ListBase<MenuType> menutypes = new ListBase(); /* global menutype list */

public static MenuType WM_menutype_find(String idname, boolean quiet)
{
	MenuType mt;

	if (idname!=null && !idname.isEmpty()) {
		mt= (MenuType)ListBaseUtil.BLI_findstring(menutypes, idname, MenuType_idname);
		if(mt!=null)
			return mt;
	}

	if(!quiet)
		System.out.printf("search for unknown menutype %s\n", idname);

	return null;
}

public static boolean WM_menutype_add(MenuType mt)
{
	ListBaseUtil.BLI_addtail(menutypes, mt);
	return true;
}

public static void WM_menutype_freelink(MenuType mt)
{
	ListBaseUtil.BLI_freelinkN(menutypes, mt);
}

public static void WM_menutype_free()
{
	MenuType mt= menutypes.first, mt_next;

	while(mt!=null) {
		mt_next= mt.next;

		if(mt.ext.free!=null)
			mt.ext.free.run(mt.ext.data);

		WM_menutype_freelink(mt);

		mt= mt_next;
	}
}

/* ****************************************** */

public static void WM_keymap_init(bContext C)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	
	if(wm.defaultconf==null)
		wm.defaultconf= WmKeymap.WM_keyconfig_new(wm, "Blender");

	if(wm!=null && bContext.CTX_py_init_get(C) && (wm.initialized & WindowManagerTypes.WM_INIT_KEYMAP) == 0) {
		/* create default key config */
//		WmOperators.wm_window_keymap(wm);
		WmOperators.wm_window_keymap(wm.defaultconf);
//		SpaceTypeUtil.ED_spacetypes_keymap(wm);
		SpaceTypeUtil.ED_spacetypes_keymap(wm.defaultconf);
		WmKeymap.WM_keyconfig_userdef();

		wm.initialized |= WindowManagerTypes.WM_INIT_KEYMAP;
	}
}

public static void wm_check(bContext C)
{
	// TMP
    //wmContext = C;
    
	wmWindowManager wm= bContext.CTX_wm_manager(C);

	/* wm context */
	if(wm==null) {
		wm= bContext.CTX_data_main(C).wm.first;
		bContext.CTX_wm_manager_set(C, wm);
	}
	if(wm==null) return;
	if(wm.windows.first==null) return;
	
	if (G.background==0) {
		/* case: fileread */
		if((wm.initialized & WindowManagerTypes.WM_INIT_WINDOW) == 0) {
			WM_keymap_init(C);
//			WM_autosave_init(wm);
		}

		/* case: no open windows at all, for old file reads */
		WmWindow.wm_window_add_ghostwindows(C, wm);
	}

//	/* case: fileread */
//	/* note: this runs in bg mode to set the screen context cb */
//	if((wm.initialized & WindowManagerTypes.WM_INIT_WINDOW) == 0) {
//		ScreenEdit.ED_screens_initialize((GL2)GLU.getCurrentGL(), wm);
////		ScreenEdit.ED_screens_initialize(wm);
//		wm.initialized |= WindowManagerTypes.WM_INIT_WINDOW;
//	}
}

public static void WM_init(GL2 gl, bContext C) {
	/* case: fileread */
	/* note: this runs in bg mode to set the screen context cb */
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	if((wm.initialized & WindowManagerTypes.WM_INIT_WINDOW) == 0) {
		ScreenEdit.ED_screens_initialize(gl, wm);
//		ScreenEdit.ED_screens_initialize(wm);
		wm.initialized |= WindowManagerTypes.WM_INIT_WINDOW;
	}
}

//void wm_clear_default_size(bContext *C)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmWindow *win;
//
//	/* wm context */
//	if(wm==NULL) {
//		wm= CTX_data_main(C).wm.first;
//		CTX_wm_manager_set(C, wm);
//	}
//	if(wm==NULL) return;
//	if(wm.windows.first==NULL) return;
//
//	for(win= wm.windows.first; win; win= win.next) {
//		win.sizex = 0;
//		win.sizey = 0;
//		win.posx = 0;
//		win.posy = 0;
//	}
//
//}

/* on startup, it adds all data, for matching */
public static void wm_add_default(bContext C, wmWindowManager wm)
{
	//wmWindowManager wm= (wmWindowManager)LibraryUtil.alloc_libblock(bContext.CTX_data_main(C).wm, DNA_ID.ID_WM, StringUtil.toCString("WinMan"),0);
	wmWindow win;
	bScreen screen= bContext.CTX_wm_screen(C); /* XXX from file read hrmf */
	
//	// TMP
//	ListBaseUtil.BLI_freelistN(wm.timers);

	bContext.CTX_wm_manager_set(C, wm);
	win= WmWindow.wm_window_new(C);
	win.screen= screen;
	screen.winid= (short)win.winid;
//	StringUtil.BLI_strncpy(win.screenname,0, screen.id.name,2, 21);
	StringUtil.BLI_strncpy(win.screenname,0, screen.id.name,2, win.screenname.length);

	wm.winactive= win;
	wm.file_saved= 1;
	WmWindow.wm_window_make_drawable(C, win);
}


/* context is allowed to be NULL, do not free wm itself (library.c) */
public static void wm_close_and_free(bContext C, wmWindowManager wm)
{
	wmWindow win;
	wmOperator op;
//	wmKeyMap *km;
//	wmKeymapItem *kmi;

	while((win= (wmWindow)wm.windows.first)!=null) {
		ListBaseUtil.BLI_remlink(wm.windows, win);
		win.screen= null; /* prevent draw clear to use screen */
		WmDraw.wm_draw_window_clear(win);
		WmWindow.wm_window_free(C, wm, win);
	}

	while((op= (wmOperator)wm.operators.first)!=null) {
		ListBaseUtil.BLI_remlink(wm.operators, op);
		WM_operator_free(op);
	}

//	while((keyconf=wm.keyconfigs.first)) {
//		ListBaseUtil.BLI_remlink(wm.keyconfigs, keyconf);
//		WM_keyconfig_free(keyconf);
//	}

	ListBaseUtil.BLI_freelistN(wm.queue);

//	BLI_freelistN(&wm.paintcursors);
//	BLI_freelistN(&wm->drags);
	
//	wm_reports_free(wm);

	if(C!=null && bContext.CTX_wm_manager(C)==wm)
            bContext.CTX_wm_manager_set(C, null);
}

public static void wm_close_and_free_all(bContext C, ListBase<wmWindowManager> wmlist)
{
	wmWindowManager wm;

	while((wm=wmlist.first)!=null) {
		wm_close_and_free(C, wm);
		ListBaseUtil.BLI_remlink(wmlist, wm);
//		MEM_freeN(wm);
	}
}

// TMP
//public static bContext wmContext;
public static boolean initialized;

public static void WM_main(GL2 gl, bContext C)
{
//	while(1) {

		/* get events from ghost, handle window events, add to window queues */
		WmWindow.wm_window_process_events(C);

		/* per window, all events to the window, screen, area and region handlers */
		WmEventSystem.wm_event_do_handlers(C);

		/* events have left notes about changes, we handle and cache it */
//		WmEventSystem.wm_event_do_notifiers(gl, C);
		WmEventSystem.wm_event_do_notifiers(C);

		/* execute cached changes draw */
		WmDraw.wm_draw_update(gl, C);
//	}
}

}


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

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL2;
import javax.swing.JFileChooser;

import blender.blenkernel.Global;
import blender.blenkernel.Main;
import blender.blenkernel.ObjectUtil;
import blender.blenkernel.bContext;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenEdit;
import blender.ghost.GhostTypes.GHOST_TEventType;
import blender.makesdna.ScreenTypes;
import blender.makesdna.UserDefTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.ReportList;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmKeyMapItem;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.PointerRNA;
import blender.windowmanager.WmTypes.WmUIHandlerFunc;
import blender.windowmanager.WmTypes.WmUIHandlerRemoveFunc;
import blender.windowmanager.WmTypes.wmEvent;
import blender.windowmanager.WmTypes.wmNotifier;

public class WmEventSystem {

    /* return value of handler-operator call */
    public static final int WM_HANDLER_CONTINUE=	0;
    public static final int WM_HANDLER_BREAK=	1;

    public static class wmEventHandler extends Link<wmEventHandler> {
		public int type, flag;				/* type default=0, rest is custom */
	
		/* keymap handler */
//		public ListBase keymap;			/* pointer to builtin/custom keymaps */
		public wmKeyMap keymap;			/* pointer to builtin/custom keymaps */
		public rcti bblocal, bbwin;		/* optional local and windowspace bb */
	
		/* modal operator handler */
		public wmOperator op;						/* for derived/modal handlers */
		public ScrArea op_area;			/* for derived/modal handlers */
		public ARegion op_region;			/* for derived/modal handlers */
	
		/* ui handler */
		public WmUIHandlerFunc ui_handle;  		/* callback receiving events */
		public WmUIHandlerRemoveFunc ui_remove;	/* callback when handler is removed */
		public Object ui_userdata;					/* user data pointer */
		public ScrArea ui_area;			/* for derived/modal handlers */
		public ARegion ui_region;			/* for derived/modal handlers */
		public ARegion ui_menu;			/* for derived/modal handlers */
	
		/* fileselect handler re-uses modal operator data */
		public bScreen filescreen;			/* screen it started in, to validate exec */
    };

    /* handler flag */
		/* after this handler all others are ignored */
    public static final int WM_HANDLER_BLOCKING=		1;



//    /* custom types for handlers, for signalling, freeing */
//    enum {
//            WM_HANDLER_DEFAULT,
//            WM_HANDLER_FILESELECT
//    };
    public static final int WM_HANDLER_DEFAULT = 0;
    public static final int WM_HANDLER_FILESELECT = 1;

/* ************ event management ************** */

public static void wm_event_add(wmWindow win, wmEvent event_to_add)
{
	wmEvent event; //= new wmEvent();

//        event= event_to_add.copy();
    event= eventCopy(event_to_add);
//    if (event.type == WmEventTypes.MOUSEMOVE) {
//		System.out.printf("wm_event_add: %d,%d\n", event.x, event.y);
//	}
	ListBaseUtil.BLI_addtail(win.queue, event);
}

public static void wm_event_free(wmEvent event)
{
//	if(event.customdata && event.customdatafree)
//		MEM_freeN(event.customdata);
//	MEM_freeN(event);
}

public static void wm_event_free_all(wmWindow win)
{
	wmEvent event;
	
	while((event= (wmEvent)win.queue.first) != null) {
		ListBaseUtil.BLI_remlink(win.queue, event);
		wm_event_free(event);
	}
}

/* ********************* notifiers, listeners *************** */

public static boolean wm_test_duplicate_notifier(wmWindowManager wm, int type, Object reference)
{
	wmNotifier note;

	for(note=(wmNotifier)wm.queue.first; note!=null; note=note.next)
		if((note.category|note.data|note.subtype|note.action) == type && note.reference == reference)
			return true;
	
	return false;
}

/* XXX: in future, which notifiers to send to other windows? */
public static void WM_event_add_notifier(bContext C, int type, Object reference)
{
	wmNotifier note= new wmNotifier();

	note.wm= bContext.CTX_wm_manager(C);
	ListBaseUtil.BLI_addtail(note.wm.queue, note);

	note.window= bContext.CTX_wm_window(C);

	if(bContext.CTX_wm_region(C)!=null)
		note.swinid= bContext.CTX_wm_region(C).swinid;

	note.category= type & WmTypes.NOTE_CATEGORY;
	note.data= type & WmTypes.NOTE_DATA;
	note.subtype= type & WmTypes.NOTE_SUBTYPE;
	note.action= type & WmTypes.NOTE_ACTION;

	note.reference= reference;
}

public static void WM_main_add_notifier(int type, Object reference)
{
	Main bmain= G.main;
	wmWindowManager wm= bmain.wm.first;

	if(wm!=null && !wm_test_duplicate_notifier(wm, type, reference)) {
//		wmNotifier *note= MEM_callocN(sizeof(wmNotifier), "notifier");
		wmNotifier note= new wmNotifier();
		
		note.wm= wm;
		ListBaseUtil.BLI_addtail(note.wm.queue, note);
		
		note.category= type & WmTypes.NOTE_CATEGORY;
		note.data= type & WmTypes.NOTE_DATA;
		note.subtype= type & WmTypes.NOTE_SUBTYPE;
		note.action= type & WmTypes.NOTE_ACTION;
		
		note.reference= reference;
	}
}

static wmNotifier wm_notifier_next(wmWindowManager wm)
{
	wmNotifier note= (wmNotifier)wm.queue.first;

	if(note!=null)
            ListBaseUtil.BLI_remlink(wm.queue, note);
	return note;
}

/* called in mainloop */
//public static void wm_event_do_notifiers(GL2 gl, bContext C)
public static void wm_event_do_notifiers(bContext C)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmNotifier note;
	wmWindow win;
	
	if(wm==null)
		return;
	
	/* cache & catch WM level notifiers, such as frame change, scene/screen set */
	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
		int do_anim= 0;

		bContext.CTX_wm_window_set(C, win);

		for(note= (wmNotifier)wm.queue.first; note!=null; note= note.next) {
			if(note.category==WmTypes.NC_WM) {
				if(note.data==WmTypes.ND_FILEREAD || note.data==WmTypes.ND_FILESAVE) {
					wm.file_saved= 1;
					WmWindow.wm_window_title(C, wm, win);
				}
				else if(note.data==WmTypes.ND_DATACHANGED)
					WmWindow.wm_window_title(C, wm, win);
			}
			if(note.window==win) {
				if(note.category==WmTypes.NC_SCREEN) {
					if(note.data==WmTypes.ND_SCREENBROWSE) {
//						ScreenEdit.ED_screen_set(gl, C, (bScreen)note.reference);	// XXX hrms, think this over!
//						ScreenEdit.ED_screen_set(C, (bScreen)note.reference);	// XXX hrms, think this over!
						System.out.printf("screen set %p\n", note.reference);
					}
				}
				else if(note.category==WmTypes.NC_SCENE) {
					if(note.data==WmTypes.ND_SCENEBROWSE) {
						ScreenEdit.ED_screen_set_scene(C, (Scene)note.reference);	// XXX hrms, think this over!
						System.out.printf("scene set %p\n", note.reference);
					}
					else if(note.data==WmTypes.ND_FRAME)
						do_anim= 1;
				}
			}
		}
		if(do_anim!=0) {
			/* depsgraph gets called, might send more notifiers */
			ScreenEdit.ED_update_for_newframe(C, 1);
		}
	}

	/* the notifiers are sent without context, to keep it clean */
	while( (note=wm_notifier_next(wm))!=null ) {
//		wmWindow win;

		for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {

			/* filter out notifiers */
			if(note.category==WmTypes.NC_SCREEN && note.reference!=null && note.reference!=win.screen);
			else if(note.category==WmTypes.NC_SCENE && note.reference!=null && note.reference!=win.screen.scene);
			else {
				ScrArea sa;
				ARegion ar;

				/* XXX context in notifiers? */
				bContext.CTX_wm_window_set(C, win);

				/* printf("notifier win %d screen %s cat %x\n", win.winid, win.screen.id.name+2, note.category); */
				ScreenEdit.ED_screen_do_listen(win, note);

				for(ar=(ARegion)win.screen.regionbase.first; ar!=null; ar= ar.next) {
					Area.ED_region_do_listen(ar, note);
				}

				for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next) {
					Area.ED_area_do_listen(sa, note);
					for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
						Area.ED_region_do_listen(ar, note);
					}
				}
			}
		}

//		MEM_freeN(note);
	}

	/* cached: editor refresh callbacks now, they get context */
	for(win= (wmWindow)wm.windows.first; win!=null; win= win.next) {
		Scene sce, scene= win.screen.scene;
		ScrArea sa;
		Base base;

		bContext.CTX_wm_window_set(C, win);
		for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next) {
			if(sa.do_refresh!=0) {
				bContext.CTX_wm_area_set(C, sa);
				Area.ED_area_do_refresh(C, sa);
			}
		}

		if(G.rendering==0) { // XXX make lock in future, or separated derivedmesh users in scene

			/* update all objects, ipos, matrices, displists, etc. Flags set by depgraph or manual,
				no layer check here, gets correct flushed */
			/* sets first, we allow per definition current scene to have dependencies on sets */
			if(scene.set!=null) {
//				for(SETLOOPER(scene.set, base))
                                for(sce= scene.set, base= (Base)sce.base.first; base!=null; base= (Base)(base.next!=null?base.next:sce.set!=null?(sce=sce.set).base.first:null))
					ObjectUtil.object_handle_update(scene, base.object);
			}

			for(base= (Base)scene.base.first; base!=null; base= base.next) {
				ObjectUtil.object_handle_update(scene, base.object);
			}

//			BKE_ptcache_quick_cache_all(scene);
		}
	}
	bContext.CTX_wm_window_set(C, null);
}

/* ********************* operators ******************* */

public static boolean WM_operator_poll(bContext C, wmOperatorType ot)
{
//	wmOperatorTypeMacro *otmacro;
//	
//	for(otmacro= ot->macro.first; otmacro; otmacro= otmacro->next) {
//		wmOperatorType *ot_macro= WM_operatortype_find(otmacro->idname, 0);
//		
//		if(0==WM_operator_poll(C, ot_macro))
//			return 0;
//	}
	
	/* python needs operator type, so we added exception for it */
//	if(ot.pyop_poll)
//		return ot.pyop_poll(C, ot);
//	else
	if(ot.poll!=null)
		return ot.poll.run(C, null,null)!=0;

	return true;
}

/* sets up the new context and calls 'wm_operator_invoke()' with poll_only */
public static boolean WM_operator_poll_context(bContext C, wmOperatorType ot, int context)
{
//	return wm_operator_call_internal(C, ot, NULL, NULL, context, TRUE);
	return (ot.poll!=null && ot.poll.run(C, null, null)==0);
}

///* if repeat is true, it doesn't register again, nor does it free */
//static int wm_operator_exec(bContext *C, wmOperator *op, int repeat)
//{
//	int retval= OPERATOR_CANCELLED;
//
//	if(op==NULL || op.type==NULL)
//		return retval;
//
//	if(op.type.poll && op.type.poll(C)==0)
//		return retval;
//
//	if(op.type.exec)
//		retval= op.type.exec(C, op);
//
//	if(!(retval & OPERATOR_RUNNING_MODAL))
//		if(op.reports.list.first)
//			uiPupMenuReports(C, op.reports);
//
//	if(retval & OPERATOR_FINISHED) {
//		if(op.type.flag & OPTYPE_UNDO)
//			ED_undo_push_op(C, op);
//
//		if(repeat==0) {
//			if((op.type.flag & OPTYPE_REGISTER) || (G.f & G_DEBUG))
//				wm_operator_register(C, op);
//			else
//				WM_operator_free(op);
//		}
//	}
//	else if(repeat==0)
//		WM_operator_free(op);
//
//	return retval;
//
//}
//
///* for running operators with frozen context (modal handlers, menus) */
//int WM_operator_call(bContext *C, wmOperator *op)
//{
//	return wm_operator_exec(C, op, 0);
//}
//
///* do this operator again, put here so it can share above code */
//int WM_operator_repeat(bContext *C, wmOperator *op)
//{
//	return wm_operator_exec(C, op, 1);
//}

static wmOperator wm_operator_create(wmWindowManager wm, wmOperatorType ot, PointerRNA properties, ReportList reports)
{
	wmOperator op= new wmOperator();	/* XXX operatortype names are static still. for debug */

	/* XXX adding new operator could be function, only happens here now */
	op.type= ot;
	StringUtil.BLI_strncpy(op.idname,0, StringUtil.toCString(ot.idname),0, WindowManagerTypes.OP_MAX_TYPENAME);

	/* initialize properties, either copy or create */
	op.ptr= new PointerRNA();
//        op.ptr= properties;
	if(properties!=null && properties.data!=null) {
//                System.out.println("wm_operator_create properties.data!=null");
//		op.properties= IDP_CopyProperty(properties.data);
	}
	else {
//                System.out.println("wm_operator_create properties.data==null");
//		IDPropertyTemplate val = {0};
//		op.properties= IDP_New(DNA_ID.IDP_GROUP, val, "wmOperatorProperties");
	}
	RnaAccess.RNA_pointer_create(wm.id, ot.srna, op.properties, (PointerRNA)op.ptr);

	/* initialize error reports */
	if (reports!=null) {
		op.reports= reports; /* must be initialized alredy */
	}
	else {
		op.reports= new ReportList();
//		BKE_reports_init(op.reports, RPT_STORE);
	}

	return op;
}

//static void wm_operator_print(wmOperator *op)
//{
//	char *buf = WM_operator_pystring(op);
//	printf("%s\n", buf);
//	MEM_freeN(buf);
//}

static void wm_region_mouse_co(bContext C, wmEvent event)
{
	ARegion ar= bContext.CTX_wm_region(C);
	if(ar!=null) {
		/* compatibility convention */
		event.mval[0]= (short)(event.x - ar.winrct.xmin);
		event.mval[1]= (short)(event.y - ar.winrct.ymin);
	}
}

static int wm_operator_invoke(bContext C, wmOperatorType ot, wmEvent event, PointerRNA properties)
{
//        System.out.printf("wm_operator_invoke name:%s event:%d\n", ot.name, event.type);
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	int retval= WindowManagerTypes.OPERATOR_PASS_THROUGH;

	if(ot.poll==null || ot.poll.run(C, null, null)!=0) {
//                System.out.printf("wm_operator_invoke name:%s event:%d\n", ot.name, event.type);
		wmOperator op= wm_operator_create(wm, ot, properties, null);

		if((G.f & Global.G_DEBUG)!=0 && event!=null && event.type!=WmEventTypes.MOUSEMOVE)
			System.out.printf("handle evt %d win %d op %s\n", event!=null?event.type:0, bContext.CTX_wm_screen(C).subwinactive, ot.idname);

		if(((wmOperatorType)op.type).invoke!=null && event!=null) {
			wm_region_mouse_co(C, event);
			retval= ((wmOperatorType)op.type).invoke.run(C, op, event);
		}
		else if(((wmOperatorType)op.type).exec!=null)
			retval= ((wmOperatorType)op.type).exec.run(C, op, null);
		else
			System.out.printf("invalid operator call %s\n", ot.idname); /* debug, important to leave a while, should never happen */

		if((retval & WindowManagerTypes.OPERATOR_RUNNING_MODAL)==0) {
//			if(op.reports.list.first!=null) /* only show the report if the report list was not given in the function */
//				uiPupMenuReports(C, op.reports);

//                        if ((retval & WindowManagerTypes.OPERATOR_FINISHED)!=0) /* todo - this may conflict with the other wm_operator_print, if theres ever 2 prints for 1 action will may need to add modal check here */
//                                if((G.f & Global.G_DEBUG)!=0)
//                                        wm_operator_print(op);
		}

		if((retval & WindowManagerTypes.OPERATOR_FINISHED)!=0) {
//			if((ot.flag & WmTypes.OPTYPE_UNDO)!=0)
//				ED_undo_push_op(C, op);

			if((ot.flag & WmTypes.OPTYPE_REGISTER)!=0 || (G.f & Global.G_DEBUG)!=0)
				Wm.wm_operator_register(C, op);
			else
				Wm.WM_operator_free(op);
		}
		else if((retval & WindowManagerTypes.OPERATOR_RUNNING_MODAL)!=0) {
			/* grab cursor during blocking modal ops (X11) */
//			if((ot.flag & WmTypes.OPTYPE_BLOCKING)!=0)
//				WM_cursor_grab(bContext.CTX_wm_window(C), 1);
		}
		else
			Wm.WM_operator_free(op);
	}

	return retval;
}

/* invokes operator in context */
public static int WM_operator_name_call(bContext C, String opstring, int context, Object properties)
{
	wmOperatorType ot= WmOperators.WM_operatortype_find(opstring, false);
	wmWindow window= bContext.CTX_wm_window(C);
	wmEvent event;

	int retval;

	/* dummie test */
	if(ot!=null && C!=null && window!=null) {
		event= (wmEvent)window.eventstate;
		switch(context) {

			case WmTypes.WM_OP_EXEC_REGION_WIN:
				event= null;	/* pass on without break */
			case WmTypes.WM_OP_INVOKE_REGION_WIN:
			{
				/* forces operator to go to the region window, for header menus */
				ARegion ar= bContext.CTX_wm_region(C);
				ScrArea area= bContext.CTX_wm_area(C);

				if(area!=null) {
					ARegion ar1= (ARegion)area.regionbase.first;
					for(; ar1!=null; ar1= ar1.next)
						if(ar1.regiontype==ScreenTypes.RGN_TYPE_WINDOW)
							break;
					if(ar1!=null)
						bContext.CTX_wm_region_set(C, ar1);
				}

				retval= wm_operator_invoke(C, ot, event, (PointerRNA)properties);

				/* set region back */
				bContext.CTX_wm_region_set(C, ar);

				return retval;
			}
			case WmTypes.WM_OP_EXEC_AREA:
				event= null;	/* pass on without break */
			case WmTypes.WM_OP_INVOKE_AREA:
			{
					/* remove region from context */
				ARegion ar= bContext.CTX_wm_region(C);

				bContext.CTX_wm_region_set(C, null);
				retval= wm_operator_invoke(C, ot, event, (PointerRNA)properties);
				bContext.CTX_wm_region_set(C, ar);

				return retval;
			}
			case WmTypes.WM_OP_EXEC_SCREEN:
				event= null;	/* pass on without break */
			case WmTypes.WM_OP_INVOKE_SCREEN:
			{
				/* remove region + area from context */
				ARegion ar= bContext.CTX_wm_region(C);
				ScrArea area= bContext.CTX_wm_area(C);

				bContext.CTX_wm_region_set(C, null);
				bContext.CTX_wm_area_set(C, null);
				retval= wm_operator_invoke(C, ot, event, (PointerRNA)properties);
				bContext.CTX_wm_region_set(C, ar);
				bContext.CTX_wm_area_set(C, area);

				return retval;
			}
			case WmTypes.WM_OP_EXEC_DEFAULT:
				event= null;	/* pass on without break */
			case WmTypes.WM_OP_INVOKE_DEFAULT:
				return wm_operator_invoke(C, ot, event, (PointerRNA)properties);
		}
	}

	return 0;
}

///* Similar to WM_operator_name_call called with WM_OP_EXEC_DEFAULT context.
//   - wmOperatorType is used instead of operator name since python alredy has the operator type
//   - poll() must be called by python before this runs.
//   - reports can be passed to this function (so python can report them as exceptions)
//*/
//int WM_operator_call_py(bContext *C, wmOperatorType *ot, PointerRNA *properties, ReportList *reports)
//{
//	wmWindowManager *wm=	CTX_wm_manager(C);
//	wmOperator *op=			wm_operator_create(wm, ot, properties, reports);
//	int retval= OPERATOR_CANCELLED;
//
//	if (op.type.exec)
//		retval= op.type.exec(C, op);
//	else
//		printf("error \"%s\" operator has no exec function, python cannot call it\n", op.type.name);
//
//	if (reports)
//		op.reports= NULL; /* dont let the operator free reports passed to this function */
//	WM_operator_free(op);
//
//	return retval;
//}


/* ********************* handlers *************** */

/* future extra customadata free? */
public static void wm_event_free_handler(wmEventHandler handler)
{
//	MEM_freeN(handler);
}

/* only set context when area/region is part of screen */
static void wm_handler_op_context(bContext C, wmEventHandler handler)
{
	bScreen screen= bContext.CTX_wm_screen(C);

	if(screen!=null && handler.op!=null) {
		if(handler.op_area==null)
			bContext.CTX_wm_area_set(C, null);
		else {
			ScrArea sa;

			for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
				if(sa==handler.op_area)
					break;
			if(sa==null) {
				/* when changing screen layouts with running modal handlers (like render display), this
				   is not an error to print */
				if(handler.op==null)
					System.out.printf("internal error: handler (%s) has invalid area\n", ((wmOperatorType)handler.op.type).idname);
			}
			else {
				ARegion ar;
				bContext.CTX_wm_area_set(C, sa);
				for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
					if(ar==handler.op_region)
						break;
				/* XXX no warning print here, after full-area and back regions are remade */
				if(ar!=null)
					bContext.CTX_wm_region_set(C, ar);
			}
		}
	}
}

/* called on exit or remove area, only here call cancel callback */
public static void WM_event_remove_handlers(bContext C, ListBase<wmEventHandler> handlers)
{
	wmEventHandler handler;

	/* C is zero on freeing database, modal handlers then already were freed */
	while((handler=handlers.first)!=null) {
		ListBaseUtil.BLI_remlink(handlers, handler);

		if(handler.op!=null) {
			if(((wmOperatorType)handler.op.type).cancel!=null) {
				ScrArea area= bContext.CTX_wm_area(C);
				ARegion region= bContext.CTX_wm_region(C);

				wm_handler_op_context(C, handler);

				((wmOperatorType)handler.op.type).cancel.run(C, handler.op, null);

				bContext.CTX_wm_area_set(C, area);
				bContext.CTX_wm_region_set(C, region);
			}

			Wm.WM_operator_free(handler.op);
//			WM_cursor_grab(CTX_wm_window(C), 0);
		}
		else if(handler.ui_remove!=null) {
			ScrArea area= bContext.CTX_wm_area(C);
			ARegion region= bContext.CTX_wm_region(C);
			ARegion menu= bContext.CTX_wm_menu(C);

			if(handler.ui_area!=null) bContext.CTX_wm_area_set(C, handler.ui_area);
			if(handler.ui_region!=null) bContext.CTX_wm_region_set(C, handler.ui_region);
			if(handler.ui_menu!=null) bContext.CTX_wm_menu_set(C, handler.ui_menu);

			handler.ui_remove.run(C, handler.ui_userdata);

			bContext.CTX_wm_area_set(C, area);
			bContext.CTX_wm_region_set(C, region);
			bContext.CTX_wm_menu_set(C, menu);
		}

		wm_event_free_handler(handler);
	}
}

/* do userdef mappings */
static int wm_userdef_event_map(int kmitype)
{
	switch(kmitype) {
		case WmEventTypes.SELECTMOUSE:
			if((U.flag & UserDefTypes.USER_LMOUSESELECT)!=0)
				return WmEventTypes.LEFTMOUSE;
			else
				return WmEventTypes.RIGHTMOUSE;

		case WmEventTypes.ACTIONMOUSE:
			if((U.flag & UserDefTypes.USER_LMOUSESELECT)!=0)
				return WmEventTypes.RIGHTMOUSE;
			else
				return WmEventTypes.LEFTMOUSE;

		case WmEventTypes.WHEELOUTMOUSE:
			if((U.uiflag & UserDefTypes.USER_WHEELZOOMDIR)!=0)
				return WmEventTypes.WHEELUPMOUSE;
			else
				return WmEventTypes.WHEELDOWNMOUSE;

		case WmEventTypes.WHEELINMOUSE:
			if((U.uiflag & UserDefTypes.USER_WHEELZOOMDIR)!=0)
				return WmEventTypes.WHEELDOWNMOUSE;
			else
				return WmEventTypes.WHEELUPMOUSE;

		case WmEventTypes.EVT_TWEAK_A:
			if((U.flag & UserDefTypes.USER_LMOUSESELECT)!=0)
				return WmEventTypes.EVT_TWEAK_R;
			else
				return WmEventTypes.EVT_TWEAK_L;

		case WmEventTypes.EVT_TWEAK_S:
			if((U.flag & UserDefTypes.USER_LMOUSESELECT)!=0)
				return WmEventTypes.EVT_TWEAK_L;
			else
				return WmEventTypes.EVT_TWEAK_R;
	}

	return kmitype;
}

static boolean wm_eventmatch(wmEvent winevent, wmKeyMapItem kmi)
{
	int kmitype= wm_userdef_event_map(kmi.type);
//        System.out.println("wm_eventmatch kmitype: "+kmitype);
//        System.out.println("wm_eventmatch winevent.type: "+winevent.type);

//	if(kmi.inactive) return false;

	/* the matching rules */
	if(kmitype==WmTypes.KM_TEXTINPUT)
		if(WmEventTypes.ISKEYBOARD(winevent.type)) return true;
	if(kmitype!=WmTypes.KM_ANY)
		if(winevent.type!=kmitype) return false;

	if(kmi.val!=WmTypes.KM_ANY)
		if(winevent.val!=kmi.val) return false;

	/* modifiers also check bits, so it allows modifier order */
	if(kmi.shift!=WmTypes.KM_ANY)
		if(winevent.shift != kmi.shift && (winevent.shift & kmi.shift)==0) return false;
	if(kmi.ctrl!=WmTypes.KM_ANY)
		if(winevent.ctrl != kmi.ctrl && (winevent.ctrl & kmi.ctrl)==0) return false;
	if(kmi.alt!=WmTypes.KM_ANY)
		if(winevent.alt != kmi.alt && (winevent.alt & kmi.alt)==0) return false;
	if(kmi.oskey!=WmTypes.KM_ANY)
		if(winevent.oskey != kmi.oskey && (winevent.oskey & kmi.oskey)==0) return false;
	if(kmi.keymodifier!=0)
		if(winevent.keymodifier!=kmi.keymodifier) return false;

//        System.out.println("wm_eventmatch");
	return true;
}

static boolean wm_event_always_pass(wmEvent event)
{
	/* some events we always pass on, to ensure proper communication */
	return (event.type==WmEventTypes.TIMER || event.type==WmEventTypes.TIMER0 || event.type==WmEventTypes.TIMER1 || event.type==WmEventTypes.TIMER2 || event.type==WmEventTypes.TIMERJOBS);
}

/* operator exists */
static void wm_event_modalkeymap(wmOperator op, wmEvent event)
{
	if(((wmOperatorType)op.type).modalkeymap!=null) {
		wmKeyMapItem kmi;

		for(kmi= (wmKeyMapItem)((wmOperatorType)op.type).modalkeymap.items.first; kmi!=null; kmi= kmi.next) {
			if(wm_eventmatch(event, kmi)) {

				event.type= WmEventTypes.EVT_MODAL_MAP;
				event.val= kmi.propvalue;
			}
		}
	}
}

/* Warning: this function removes a modal handler, when finished */
static int wm_handler_operator_call(bContext C, ListBase handlers, wmEventHandler handler, wmEvent event, PointerRNA properties)
{
	int retval= WindowManagerTypes.OPERATOR_PASS_THROUGH;

	/* derived, modal or blocking operator */
	if(handler.op!=null) {
//                System.out.println("wm_handler_operator_call 1");
		wmOperator op= handler.op;
		wmOperatorType ot= (wmOperatorType)op.type;

		if(ot.modal!=null) {
			/* we set context to where modal handler came from */
			ScrArea area= bContext.CTX_wm_area(C);
			ARegion region= bContext.CTX_wm_region(C);

			wm_handler_op_context(C, handler);
			wm_region_mouse_co(C, event);
			wm_event_modalkeymap(op, event);

			retval= ot.modal.run(C, op, event);

			/* putting back screen context, reval can pass trough after modal failures! */
			if((retval & WindowManagerTypes.OPERATOR_PASS_THROUGH)!=0 || wm_event_always_pass(event)) {
				bContext.CTX_wm_area_set(C, area);
				bContext.CTX_wm_region_set(C, region);
			}
			else {
				/* this special cases is for areas and regions that get removed */
				bContext.CTX_wm_area_set(C, null);
				bContext.CTX_wm_region_set(C, null);
			}

			if((retval & WindowManagerTypes.OPERATOR_RUNNING_MODAL)==0) {
//				if(op.reports.list.first!=null)
//					uiPupMenuReports(C, op.reports);
                        }

			if ((retval & WindowManagerTypes.OPERATOR_FINISHED)!=0) {
//				if(G.f & G_DEBUG)
//					wm_operator_print(op); /* todo - this print may double up, might want to check more flags then the FINISHED */
			}

			if((retval & WindowManagerTypes.OPERATOR_FINISHED)!=0) {
//				if(ot.flag & OPTYPE_UNDO)
//					ED_undo_push_op(C, op);

				if((ot.flag & WmTypes.OPTYPE_REGISTER)!=0 || (G.f & Global.G_DEBUG)!=0)
					Wm.wm_operator_register(C, op);
				else
					Wm.WM_operator_free(op);
				handler.op= null;
			}
			else if((retval & (WindowManagerTypes.OPERATOR_CANCELLED|WindowManagerTypes.OPERATOR_FINISHED))!=0) {
				Wm.WM_operator_free(op);
				handler.op= null;
			}

			/* remove modal handler, operator itself should have been cancelled and freed */
			if((retval & (WindowManagerTypes.OPERATOR_CANCELLED|WindowManagerTypes.OPERATOR_FINISHED))!=0) {
//				WM_cursor_grab(bContext.CTX_wm_window(C), 0);

				ListBaseUtil.BLI_remlink(handlers, handler);
				wm_event_free_handler(handler);

				/* prevent silly errors from operator users */
				//retval &= ~OPERATOR_PASS_THROUGH;
			}

		}
		else
			System.out.printf("wm_handler_operator_call error\n");
	}
	else {
//                System.out.println("wm_handler_operator_call 2");
		wmOperatorType ot= WmOperators.WM_operatortype_find(StringUtil.toJString((byte[])event.keymap_idname,0), false);

		if(ot!=null)
			retval= wm_operator_invoke(C, ot, event, properties);
	}

	if((retval & WindowManagerTypes.OPERATOR_PASS_THROUGH)!=0)
		return WM_HANDLER_CONTINUE;

	return WM_HANDLER_BREAK;
}

static int wm_handler_ui_call(bContext C, wmEventHandler handler, wmEvent event)
{
//        if (event.type != WmEventTypes.MOUSEMOVE) {
//            System.out.println("wm_handler_ui_call");
//        }
//	if (event.type == WmEventTypes.MOUSEMOVE) {
//		System.out.printf("wm_handler_ui_call: %d,%d\n", event.x, event.y);
//	}
	
	ScrArea area= bContext.CTX_wm_area(C);
	ARegion region= bContext.CTX_wm_region(C);
	ARegion menu= bContext.CTX_wm_menu(C);
	int retval;
        boolean always_pass;

	/* we set context to where ui handler came from */
	if(handler.ui_area!=null) bContext.CTX_wm_area_set(C, handler.ui_area);
	if(handler.ui_region!=null) bContext.CTX_wm_region_set(C, handler.ui_region);
	if(handler.ui_menu!=null) bContext.CTX_wm_menu_set(C, handler.ui_menu);

	/* in advance to avoid access to freed event on window close */
	always_pass= wm_event_always_pass(event);

	retval= handler.ui_handle.run(C, event, handler.ui_userdata);

	/* putting back screen context */
	if((retval != WmTypes.WM_UI_HANDLER_BREAK) || always_pass) {
		bContext.CTX_wm_area_set(C, area);
		bContext.CTX_wm_region_set(C, region);
		bContext.CTX_wm_menu_set(C, menu);
	}
	else {
		/* this special cases is for areas and regions that get removed */
		bContext.CTX_wm_area_set(C, null);
		bContext.CTX_wm_region_set(C, null);
		bContext.CTX_wm_menu_set(C, null);
	}

	if(retval == WmTypes.WM_UI_HANDLER_BREAK)
		return WM_HANDLER_BREAK;

	return WM_HANDLER_CONTINUE;
}

/* fileselect handlers are only in the window queue, so it's save to switch screens or area types */
static int wm_handler_fileselect_call(bContext C, ListBase handlers, wmEventHandler handler, wmEvent event)
{
//	SpaceFile sfile;
	int action= WM_HANDLER_CONTINUE;

	if(event.type != WmEventTypes.EVT_FILESELECT)
		return action;
	if(handler.op != (wmOperator)event.customdata)
		return action;

	switch(event.val) {
		case WmEventTypes.EVT_FILESELECT_OPEN:
		case WmEventTypes.EVT_FILESELECT_FULL_OPEN:
			{
				System.out.println("wm_handler_fileselect_call: OPEN");
//				short flag =0; short display =FILE_SHORTDISPLAY; short filter =0; short sort =FILE_SORT_ALPHA;
//				char *dir= NULL; char *path= RNA_string_get_alloc(handler.op.ptr, "filename", NULL, 0);
//
//				if(event.val==EVT_FILESELECT_OPEN)
//					ED_area_newspace(C, handler.op_area, SPACE_FILE);
//				else
//					ED_screen_full_newspace(C, handler.op_area, SPACE_FILE);
//
//				/* settings for filebrowser, sfile is not operator owner but sends events */
//				sfile= (SpaceFile*)CTX_wm_space_data(C);
//				sfile.op= handler.op;
//
//				ED_fileselect_set_params(sfile);
//				dir = NULL;
//				MEM_freeN(path);
				
				final bContext context = C;
				final wmOperator ophandle = handler.op;
				Thread open = new Thread(new Runnable() {
					public void run() {
						JFileChooser fc = new JFileChooser();
						if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							try {
//								System.out.println("wm_handler_fileselect_call");
//								WmFiles.WM_read_file(
//										context,
//										fc.getSelectedFile().toURI().toURL().toString(),
//										null);
								String openname = fc.getSelectedFile().toURI().toURL().toString();
								RnaAccess.RNA_string_set(ophandle.ptr, "filepath", openname);
								WM_event_fileselect_event(context, ophandle, WmEventTypes.EVT_FILESELECT_EXEC);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
				open.start();

				action= WM_HANDLER_BREAK;
			}
			break;

		case WmEventTypes.EVT_FILESELECT_EXEC:
		case WmEventTypes.EVT_FILESELECT_CANCEL:
			{
				System.out.println("wm_handler_fileselect_call: EXEC");
//				/* XXX validate area and region? */
//				bScreen *screen= CTX_wm_screen(C);
//				char *path= RNA_string_get_alloc(handler.op.ptr, "filename", NULL, 0);
//
//				if(screen != handler.filescreen)
//					ED_screen_full_prevspace(C);
//				else
//					ED_area_prevspace(C);

				/* remlink now, for load file case */
				ListBaseUtil.BLI_remlink(handlers, handler);

				if(event.val==WmEventTypes.EVT_FILESELECT_EXEC) {
					wm_handler_op_context(C, handler);

//					/* a bit weak, might become arg for WM_event_fileselect? */
//					/* XXX also extension code in image-save doesnt work for this yet */
//					if(strncmp(handler.op.type.name, "Save", 4)==0) {
//						/* this gives ownership to pupmenu */
//						uiPupMenuSaveOver(C, handler.op, path);
//					}
//					else {
						int retval= ((wmOperatorType)handler.op.type).exec.run(C, handler.op, null);

//						if (retval & OPERATOR_FINISHED)
//							if(G.f & G_DEBUG)
//								wm_operator_print(handler.op);

						Wm.WM_operator_free(handler.op);
//					}

					bContext.CTX_wm_area_set(C, null);
				}
				else
					Wm.WM_operator_free(handler.op);

				WmEventSystem.wm_event_free_handler(handler);
//				MEM_freeN(path);

				action= WM_HANDLER_BREAK;
			}
			break;
	}

	return action;
}

static boolean handler_boundbox_test(wmEventHandler handler, wmEvent event)
{
	if(handler.bbwin!=null) {
		if(handler.bblocal!=null) {
			rcti rect= handler.bblocal.copy();

			Rct.BLI_translate_rcti(rect, handler.bbwin.xmin, handler.bbwin.ymin);

			if(Rct.BLI_in_rcti(rect, event.x, event.y))
				return true;
			else if(event.type==WmEventTypes.MOUSEMOVE && Rct.BLI_in_rcti(rect, event.prevx, event.prevy))
				return true;
			else
				return false;
		}
		else {
			if(Rct.BLI_in_rcti(handler.bbwin, event.x, event.y))
				return true;
			else if(event.type==WmEventTypes.MOUSEMOVE && Rct.BLI_in_rcti(handler.bbwin, event.prevx, event.prevy))
				return true;
			else
				return false;
		}
	}
	return true;
}

static int wm_handlers_do(bContext C, wmEvent event, ListBase<wmEventHandler> handlers)
{
//        if (event.type != WmEventTypes.MOUSEMOVE) {
//            System.out.println("wm_handlers_do: "+event.type);
//        }
//	if (event.type != WmEventTypes.RIGHTMOUSE) {
//		System.out.println("wm_handlers_do: "+event.type);
//	}
//	if (event.type == WmEventTypes.MOUSEMOVE) {
//		System.out.printf("wm_handlers_do: %d,%d\n", event.x, event.y);
////		Thread.dumpStack();
//	}

	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmEventHandler handler, nexthandler;
	int action= WM_HANDLER_CONTINUE;
	boolean always_pass;

	if(handlers==null) return action;

	/* modal handlers can get removed in this loop, we keep the loop this way */
	for(handler= handlers.first; handler!=null; handler= nexthandler) {
		nexthandler= handler.next;
//                System.err.println("wm_handlers_do handle");

		/* optional boundbox */
		if(handler_boundbox_test(handler, event)) {
//                        System.err.println("wm_handlers_do boundbox");
			/* in advance to avoid access to freed event on window close */
			always_pass= wm_event_always_pass(event);

			/* modal+blocking handler */
			if((handler.flag & WM_HANDLER_BLOCKING)!=0)
				action= WM_HANDLER_BREAK;

			if(handler.keymap!=null) {
//                                System.err.println("wm_handlers_do keymap");
				wmKeyMap keymap= WmKeymap.WM_keymap_active(wm, handler.keymap);
				wmKeyMapItem kmi;

//                                if (handler.keymap.first == null && event.type == WmEventTypes.WHEELDOWNMOUSE) {
//                                    System.err.println("wm_handlers_do handler keymap empty");
//                                }

//				for(kmi= (wmKeyMapItem)handler.keymap.first; kmi!=null; kmi= kmi.next) {
				for(kmi= (wmKeyMapItem)keymap.items.first; kmi!=null; kmi= kmi.next) {
//                                        System.err.println("wm_handlers_do keymap item");
					if(wm_eventmatch(event, kmi)) {
//                                                System.err.println("wm_handlers_do keymap item event");
						event.keymap_idname= kmi.idname;	/* weak, but allows interactive callback to not use rawkey */

						action= wm_handler_operator_call(C, handlers, handler, event, (PointerRNA)kmi.ptr);
						if(action==WM_HANDLER_BREAK)  /* not wm_event_always_pass(event) here, it denotes removed handler */
							break;
					}
				}
			}
			else if(handler.ui_handle!=null) {
				action= wm_handler_ui_call(C, handler, event);
			}
			else if(handler.type==WM_HANDLER_FILESELECT) {
				/* screen context changes here */
//				System.out.println("handler.type==WM_HANDLER_FILESELECT");
				action= wm_handler_fileselect_call(C, handlers, handler, event);
			}
			else {
//                                System.err.println("wm_handlers_do modal");
				/* modal, swallows all */
				action= wm_handler_operator_call(C, handlers, handler, event, null);
			}

			if(!always_pass && action==WM_HANDLER_BREAK)
				break;
		}

		/* fileread case */
		if(bContext.CTX_wm_window(C)==null)
			break;
	}
	return action;
}

static boolean wm_event_inside_i(wmEvent event, rcti rect)
{
	if(Rct.BLI_in_rcti(rect, event.x, event.y))
	   return true;
	if(event.type==WmEventTypes.MOUSEMOVE) {
		if(Rct.BLI_in_rcti(rect, event.prevx, event.prevy)) {
			return true;
		}
		return false;
	}
	return false;
}

public static ScrArea area_event_inside(bContext C, int x, int y)
{
	bScreen screen= bContext.CTX_wm_screen(C);
	ScrArea sa;
	
	if(screen!=null)
		for(sa= (ScrArea)screen.areabase.first; sa!=null; sa= sa.next)
			if(Rct.BLI_in_rcti(sa.totrct, x, y))
				return sa;
	return null;
}

public static ARegion region_event_inside(bContext C, int x, int y)
{
	bScreen screen= bContext.CTX_wm_screen(C);
	ScrArea area= bContext.CTX_wm_area(C);
	ARegion ar;
	
	if(screen!=null && area!=null)
		for(ar= (ARegion)area.regionbase.first; ar!=null; ar= ar.next)
			if(Rct.BLI_in_rcti(ar.winrct, x, y))
				return ar;
	return null;
}

//static void wm_paintcursor_tag(bContext *C, wmPaintCursor *pc, ARegion *ar)
//{
//	if(ar) {
//		for(; pc; pc= pc.next) {
//			if(pc.poll(C)) {
//				wmWindow *win= CTX_wm_window(C);
//				win.screen.do_draw_paintcursor= 1;
//
//				if(win.drawmethod != USER_DRAW_TRIPLE)
//					ED_region_tag_redraw(ar);
//			}
//		}
//	}
//}
//
///* called on mousemove, check updates for paintcursors */
///* context was set on active area and region */
//static void wm_paintcursor_test(bContext *C, wmEvent *event)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//
//	if(wm.paintcursors.first) {
//		ARegion *ar= CTX_wm_region(C);
//		if(ar)
//			wm_paintcursor_tag(C, wm.paintcursors.first, ar);
//
//		/* if previous position was not in current region, we have to set a temp new context */
//		if(ar==NULL || !BLI_in_rcti(&ar.winrct, event.prevx, event.prevy)) {
//			ScrArea *sa= CTX_wm_area(C);
//
//			CTX_wm_area_set(C, area_event_inside(C, event.prevx, event.prevy));
//			CTX_wm_region_set(C, region_event_inside(C, event.prevx, event.prevy));
//
//			wm_paintcursor_tag(C, wm.paintcursors.first, CTX_wm_region(C));
//
//			CTX_wm_area_set(C, sa);
//			CTX_wm_region_set(C, ar);
//		}
//	}
//}

/* called in main loop */
/* goes over entire hierarchy:  events . window . screen . area . region */
public static void wm_event_do_handlers(bContext C)
{
	wmWindow win;

	for(win= (wmWindow)bContext.CTX_wm_manager(C).windows.first; win!=null; win= win.next) {
		wmEvent event;
		
		if( win.screen==null )
			wm_event_free_all(win);
		
		while( (event= (wmEvent)win.queue.first) != null ) {
			
//			if (event.type == WmEventTypes.MOUSEMOVE) {
//				System.out.printf("wm_event_do_handlers: %d,%d\n", event.x, event.y);
//			}
			
			
			int action;
			
			bContext.CTX_wm_window_set(C, win);
			
			/* we let modal handlers get active area/region, also wm_paintcursor_test needs it */
			bContext.CTX_wm_area_set(C, area_event_inside(C, event.x, event.y));
			bContext.CTX_wm_region_set(C, region_event_inside(C, event.x, event.y));
			
			/* MVC demands to not draw in event handlers... but we need to leave it for ogl selecting etc */
			WmWindow.wm_window_make_drawable(C, win);
			
//			if (event.type == WmEventTypes.MOUSEMOVE) {
//				System.out.printf("wm_event_do_handlers 1: %d,%d\n", event.x, event.y);
//			}
			action= wm_handlers_do(C, event, win.handlers);
			
			/* fileread case */
			if(bContext.CTX_wm_window(C)==null) {
				return;
			}

//            if (event.type == WmEventTypes.PADPLUSKEY) {
//                System.out.println("PADPLUSKEY: "+event.x+" "+event.y);
//            }
//			if (event.type == WmEventTypes.RIGHTMOUSE) {
//                System.out.println("RIGHTMOUSE: "+event.x+" "+event.y);
//            }
			
			/* builtin tweak, if action is break it removes tweak */
			if(!wm_event_always_pass(event))
				WmOperators.wm_tweakevent_test(C, event, action);

			if(wm_event_always_pass(event) || action==WM_HANDLER_CONTINUE) {
				ScrArea sa;
				ARegion ar;
				int doit= 0;

				/* XXX to solve, here screen handlers? */
				if(!wm_event_always_pass(event)) {
					if(event.type==WmEventTypes.MOUSEMOVE) {
						/* state variables in screen, cursors */
						ScreenEdit.ED_screen_set_subwinactive(win, event);
						/* for regions having custom cursors */
//						wm_paintcursor_test(C, event);
					}
				}

				for(sa= (ScrArea)win.screen.areabase.first; sa!=null; sa= sa.next) {
					if(wm_event_always_pass(event) || wm_event_inside_i(event, sa.totrct)) {

						bContext.CTX_wm_area_set(C, sa);
						bContext.CTX_wm_region_set(C, null);
//						if (event.type == WmEventTypes.MOUSEMOVE) {
//							System.out.printf("wm_event_do_handlers 2: %d,%d\n", event.x, event.y);
//						}
						action= wm_handlers_do(C, event, sa.handlers);

						if(wm_event_always_pass(event) || action==WM_HANDLER_CONTINUE) {
							for(ar=(ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
								if(wm_event_always_pass(event) || wm_event_inside_i(event, ar.winrct)) {
									bContext.CTX_wm_region_set(C, ar);

//                                                                        if (event.type == WmEventTypes.PADPLUSKEY
//                                                                                || event.type == WmEventTypes.WHEELUPMOUSE
//                                                                                || event.type == WmEventTypes.WHEELDOWNMOUSE
//                                                                                || event.type == WmEventTypes.WHEELINMOUSE
//                                                                                || event.type == WmEventTypes.WHEELOUTMOUSE) {
//                                                                            System.out.println("wm_event_do_handlers handlers:");
//                                                                            wmEventHandler nexthandler;
//                                                                            for (wmEventHandler handler = (wmEventHandler) ar.handlers.first; handler != null; handler = nexthandler) {
//                                                                                nexthandler = handler.next;
//                                                                                if (handler.keymap != null) {
//                                                                                    for (wmKeymapItem kmi = (wmKeymapItem) handler.keymap.first; kmi != null; kmi = kmi.next) {
//                                                                                        System.out.println(kmi);
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        }

//									if (event.type == WmEventTypes.MOUSEMOVE) {
//										System.out.printf("wm_event_do_handlers 3: %d,%d\n", event.x, event.y);
//									}
									action= wm_handlers_do(C, event, ar.handlers);
//									if (event.type == WmEventTypes.MOUSEMOVE) {
//										System.out.printf("wm_event_do_handlers 3 after: %d,%d\n", event.x, event.y);
//									}

									doit |= (Rct.BLI_in_rcti(ar.winrct, event.x, event.y))?1:0;

									if(!wm_event_always_pass(event)) {
										if(action==WM_HANDLER_BREAK)
											break;
									}
								}
							}
						}
						/* NOTE: do not escape on WM_HANDLER_BREAK, mousemove needs handled for previous area */
					}
				}

				/* XXX hrmf, this gives reliable previous mouse coord for area change, feels bad?
				   doing it on ghost queue gives errors when mousemoves go over area borders */
				if(doit!=0 && win.screen.subwinactive != win.screen.mainwin) {
					((wmEvent)win.eventstate).prevx= event.x;
					((wmEvent)win.eventstate).prevy= event.y;
				}
			}

			/* unlink and free here, blender-quit then frees all */
			ListBaseUtil.BLI_remlink(win.queue, event);
			wm_event_free(event);

		}
		
		/* only add mousemove when queue was read entirely */
		if(win.addmousemove!=0) {
//                        wmEvent evt= win.eventstate.copy();
                        wmEvent evt= eventCopy(win.eventstate);
			evt.type= WmEventTypes.MOUSEMOVE;
			evt.prevx= evt.x;
			evt.prevy= evt.y;
			wm_event_add(win, evt);
			win.addmousemove= 0;
		}
		
		bContext.CTX_wm_window_set(C, null);
	}
}

/* ********** filesector handling ************ */

public static void WM_event_fileselect_event(bContext C, Object ophandle, int eventval)
{
	/* add to all windows! */
	wmWindow win;

	for(win= (wmWindow)bContext.CTX_wm_manager(C).windows.first; win!=null; win= win.next) {
		wmEvent event= (wmEvent)win.eventstate;

		event.type= WmEventTypes.EVT_FILESELECT;
		event.val= (short)eventval;
		event.customdata= ophandle;		// only as void pointer type check

		wm_event_add(win, event);
	}
}

/* operator is supposed to have a filled "filename" property */
/* optional property: filetype (XXX enum?) */

/* Idea is to keep a handler alive on window queue, owning the operator.
   The filewindow can send event to make it execute, thus ensuring
   executing happens outside of lower level queues, with UI refreshed.
   Should also allow multiwin solutions */

public static void WM_event_add_fileselect(bContext C, wmOperator op)
{
//	wmEventHandler handler= MEM_callocN(sizeof(wmEventHandler), "fileselect handler");
	wmEventHandler handler= new wmEventHandler();
	wmWindow win= bContext.CTX_wm_window(C);
	boolean full= false;	// XXX preset?

	handler.type= WM_HANDLER_FILESELECT;
	handler.op= op;
	handler.op_area= bContext.CTX_wm_area(C);
	handler.op_region= bContext.CTX_wm_region(C);
	handler.filescreen= bContext.CTX_wm_screen(C);

	ListBaseUtil.BLI_addhead(win.handlers, handler);

	WM_event_fileselect_event(C, op, full?WmEventTypes.EVT_FILESELECT_FULL_OPEN:WmEventTypes.EVT_FILESELECT_OPEN);
}

///* lets not expose struct outside wm? */
//void WM_event_set_handler_flag(wmEventHandler *handler, int flag)
//{
//	handler.flag= flag;
//}

//public static wmEventHandler WM_event_add_modal_handler(bContext C, ListBase<wmEventHandler> handlers, wmOperator op)
public static wmEventHandler WM_event_add_modal_handler(bContext C, wmOperator op)
{
	wmEventHandler handler= new wmEventHandler();
	wmWindow win= bContext.CTX_wm_window(C);
	
	handler.op= op;
	handler.op_area= bContext.CTX_wm_area(C);		/* means frozen screen context for modal handlers! */
	handler.op_region= bContext.CTX_wm_region(C);

//	ListBaseUtil.BLI_addhead(handlers, handler);
	ListBaseUtil.BLI_addhead(win.handlers, handler);

	return handler;
}

//public static wmEventHandler WM_event_add_keymap_handler(ListBase<wmEventHandler> handlers, ListBase keymap)
public static wmEventHandler WM_event_add_keymap_handler(ListBase<wmEventHandler> handlers, wmKeyMap keymap)
{
	wmEventHandler handler;
	
	if(keymap==null) {
		System.out.printf("WM_event_add_keymap_handler called with NULL keymap\n");
		return null;
	}

	/* only allow same keymap once */
	for(handler= handlers.first; handler!=null; handler= handler.next)
		if(handler.keymap==keymap)
			return handler;

	handler= new wmEventHandler();
	ListBaseUtil.BLI_addtail(handlers, handler);
	handler.keymap= keymap;

	return handler;
}

/* priorities not implemented yet, for time being just insert in begin of list */
//public static wmEventHandler WM_event_add_keymap_handler_priority(ListBase<wmEventHandler> handlers, ListBase keymap, int priority)
public static wmEventHandler WM_event_add_keymap_handler_priority(ListBase<wmEventHandler> handlers, wmKeyMap keymap, int priority)
{
	wmEventHandler handler;

	WM_event_remove_keymap_handler(handlers, keymap);

	handler= new wmEventHandler();
	ListBaseUtil.BLI_addhead(handlers, handler);
	handler.keymap= keymap;

	return handler;
}

//public static wmEventHandler WM_event_add_keymap_handler_bb(ListBase handlers, ListBase keymap, rcti bblocal, rcti bbwin)
public static wmEventHandler WM_event_add_keymap_handler_bb(ListBase<wmEventHandler> handlers, wmKeyMap keymap, rcti bblocal, rcti bbwin)
{
	wmEventHandler handler= WM_event_add_keymap_handler(handlers, keymap);
	
	if(handler!=null) {
		handler.bblocal= bblocal;
		handler.bbwin= bbwin;
	}
	return handler;
}

//public static void WM_event_remove_keymap_handler(ListBase<wmEventHandler> handlers, ListBase keymap)
public static void WM_event_remove_keymap_handler(ListBase<wmEventHandler> handlers, wmKeyMap keymap)
{
	wmEventHandler handler;

	for(handler= handlers.first; handler!=null; handler= handler.next) {
		if(handler.keymap==keymap) {
			ListBaseUtil.BLI_remlink(handlers, handler);
//			wm_event_free_handler(handler);
			break;
		}
	}
}

public static wmEventHandler WM_event_add_ui_handler(bContext C, ListBase<wmEventHandler> handlers, WmUIHandlerFunc func, WmUIHandlerRemoveFunc remove, Object userdata)
{
	wmEventHandler handler= new wmEventHandler();
	handler.ui_handle= func;
	handler.ui_remove= remove;
	handler.ui_userdata= userdata;
	handler.ui_area= (C!=null)? bContext.CTX_wm_area(C): null;
	handler.ui_region= (C!=null)? bContext.CTX_wm_region(C): null;
	handler.ui_menu= (C!=null)? bContext.CTX_wm_menu(C): null;

	ListBaseUtil.BLI_addhead(handlers, handler);

	return handler;
}

public static void WM_event_remove_ui_handler(ListBase<wmEventHandler> handlers, WmUIHandlerFunc func, WmUIHandlerRemoveFunc remove, Object userdata)
{
	wmEventHandler handler;

	for(handler= handlers.first; handler!=null; handler= handler.next) {
		if(handler.ui_handle == func && handler.ui_remove == remove && handler.ui_userdata == userdata) {
			ListBaseUtil.BLI_remlink(handlers, handler);
			wm_event_free_handler(handler);
			break;
		}
	}
}

public static void WM_event_add_mousemove(bContext C)
{
	wmWindow window= bContext.CTX_wm_window(C);

	window.addmousemove= 1;
}

///* for modal callbacks, check configuration for how to interpret exit with tweaks  */
//int WM_modal_tweak_exit(wmEvent *evt, int tweak_event)
//{
//	/* user preset or keymap? dunno... */
//	int tweak_modal= (U.flag & USER_DRAGIMMEDIATE)==0;
//
//	switch(tweak_event) {
//		case EVT_TWEAK_L:
//		case EVT_TWEAK_M:
//		case EVT_TWEAK_R:
//			if(evt.val==tweak_modal)
//				return 1;
//		default:
//			/* this case is when modal callcback didnt get started with a tweak */
//			if(evt.val)
//				return 1;
//	}
//	return 0;
//}


/* ********************* ghost stuff *************** */

static int convert_key(int key, int loc)
{
        if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z) {
            return (WmEventTypes.AKEY + (key - KeyEvent.VK_A));
        } else if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
            return (WmEventTypes.ZEROKEY + (key - KeyEvent.VK_0));
        } else if (key >= KeyEvent.VK_NUMPAD0 && key <= KeyEvent.VK_NUMPAD9) {
            return (WmEventTypes.PAD0 + (key - KeyEvent.VK_NUMPAD0));
        } else if (key >= KeyEvent.VK_F1 && key <= KeyEvent.VK_F12) {
            return (WmEventTypes.F1KEY + (key - KeyEvent.VK_F1));
        } else {
		switch (key) {
			case KeyEvent.VK_BACK_SPACE:		return WmEventTypes.BACKSPACEKEY;
			case KeyEvent.VK_TAB:                   return WmEventTypes.TABKEY;
//			case GHOST_kKeyLinefeed:		return LINEFEEDKEY;
			case KeyEvent.VK_CLEAR:			return 0;
                        case KeyEvent.VK_ENTER:			return loc==KeyEvent.KEY_LOCATION_STANDARD?WmEventTypes.RETKEY:WmEventTypes.PADENTER;

			case KeyEvent.VK_ESCAPE:		return WmEventTypes.ESCKEY;
			case KeyEvent.VK_SPACE:			return WmEventTypes.SPACEKEY;
			case KeyEvent.VK_QUOTE:			return WmEventTypes.QUOTEKEY;
			case KeyEvent.VK_COMMA:			return WmEventTypes.COMMAKEY;
			case KeyEvent.VK_MINUS:			return WmEventTypes.MINUSKEY;
                        case KeyEvent.VK_PERIOD:		return loc==KeyEvent.KEY_LOCATION_STANDARD?WmEventTypes.PERIODKEY:WmEventTypes.PADPERIOD;
                        case KeyEvent.VK_SLASH:			return loc==KeyEvent.KEY_LOCATION_STANDARD?WmEventTypes.SLASHKEY:WmEventTypes.PADSLASHKEY;

			case KeyEvent.VK_SEMICOLON:		return WmEventTypes.SEMICOLONKEY;
			case KeyEvent.VK_EQUALS:		return WmEventTypes.EQUALKEY;

			case KeyEvent.VK_BRACELEFT:		return WmEventTypes.LEFTBRACKETKEY;
			case KeyEvent.VK_BRACERIGHT:            return WmEventTypes.RIGHTBRACKETKEY;
			case KeyEvent.VK_BACK_SLASH:		return WmEventTypes.BACKSLASHKEY;
//			case GHOST_kKeyAccentGrave:		return ACCENTGRAVEKEY;

                        case KeyEvent.VK_SHIFT:                 return loc==KeyEvent.KEY_LOCATION_LEFT?WmEventTypes.LEFTSHIFTKEY:WmEventTypes.RIGHTSHIFTKEY;
//			case GHOST_kKeyRightShift:		return RIGHTSHIFTKEY;
                        case KeyEvent.VK_CONTROL:		return loc==KeyEvent.KEY_LOCATION_LEFT?WmEventTypes.LEFTCTRLKEY:WmEventTypes.RIGHTCTRLKEY;
//			case GHOST_kKeyRightControl:            return RIGHTCTRLKEY;
//			case GHOST_kKeyCommand:			return COMMANDKEY;
                        case KeyEvent.VK_ALT:			return loc==KeyEvent.KEY_LOCATION_LEFT?WmEventTypes.LEFTALTKEY:WmEventTypes.RIGHTALTKEY;
//			case GHOST_kKeyRightAlt:		return RIGHTALTKEY;

			case KeyEvent.VK_CAPS_LOCK:		return WmEventTypes.CAPSLOCKKEY;
			case KeyEvent.VK_NUM_LOCK:		return 0;
			case KeyEvent.VK_SCROLL_LOCK:		return 0;

			case KeyEvent.VK_LEFT:                  return WmEventTypes.LEFTARROWKEY;
			case KeyEvent.VK_RIGHT:                 return WmEventTypes.RIGHTARROWKEY;
			case KeyEvent.VK_UP:			return WmEventTypes.UPARROWKEY;
			case KeyEvent.VK_DOWN:                  return WmEventTypes.DOWNARROWKEY;

			case KeyEvent.VK_PRINTSCREEN:		return 0;
			case KeyEvent.VK_PAUSE:			return WmEventTypes.PAUSEKEY;

			case KeyEvent.VK_INSERT:		return WmEventTypes.INSERTKEY;
			case KeyEvent.VK_DELETE:		return WmEventTypes.DELKEY;
			case KeyEvent.VK_HOME:			return WmEventTypes.HOMEKEY;
			case KeyEvent.VK_END:			return WmEventTypes.ENDKEY;
			case KeyEvent.VK_PAGE_UP:		return WmEventTypes.PAGEUPKEY;
			case KeyEvent.VK_PAGE_DOWN:		return WmEventTypes.PAGEDOWNKEY;

//			case GHOST_kKeyNumpadPeriod:            return PADPERIOD;
//			case GHOST_kKeyNumpadEnter:		return PADENTER;
			case KeyEvent.VK_ADD:                   return WmEventTypes.PADPLUSKEY;
			case KeyEvent.VK_SUBTRACT:		return WmEventTypes.PADMINUS;
			case KeyEvent.VK_ASTERISK:              return WmEventTypes.PADASTERKEY;
//			case GHOST_kKeyNumpadSlash:		return PADSLASHKEY;
//
//			case GHOST_kKeyGrLess:		    return GRLESSKEY;

			default:
				return WmEventTypes.UNKNOWNKEY;	/* GHOST_kKeyUnknown */
		}
	}
}

///* adds customdata to event */
//static void update_tablet_data(wmWindow *win, wmEvent *event)
//{
//	const GHOST_TabletData *td= GHOST_GetTabletData(win.ghostwin);
//
//	/* if there's tablet data from an active tablet device then add it */
//	if ((td != NULL) && td.Active) {
//		struct wmTabletData *wmtab= MEM_mallocN(sizeof(wmTabletData), "customdata tablet");
//
//		wmtab.Active = td.Active;
//		wmtab.Pressure = td.Pressure;
//		wmtab.Xtilt = td.Xtilt;
//		wmtab.Ytilt = td.Ytilt;
//
//		event.custom= EVT_DATA_TABLET;
//		event.customdata= wmtab;
//		event.customdatafree= 1;
//	}
//}


/* windows store own event queues, no bContext here */
public static void wm_event_add_ghostevent(wmWindow win, GHOST_TEventType type, Object customdata)
{
	wmEvent event, evt= (wmEvent)win.eventstate;

	/* initialize and copy state (only mouse x y and modifiers) */
//	event= evt.copy();
    event= eventCopy(evt);

	switch (type) {
		/* mouse move */
		case GHOST_kEventCursorMove: {
//                        System.out.println("GHOST_kEventCursorMove");
			if(win.active!=0) {
//				GHOST_TEventCursorData *cd= customdata;
                MouseEvent cd = (MouseEvent) customdata;
                wmEvent lastevent= (wmEvent)win.queue.last;
                
				int cx, cy;
//
//				GHOST_ScreenToClient(win.ghostwin, cd.x, cd.y, &cx, &cy);
                cx = cd.getX();
                cy = cd.getY();

//				event.type= WmEventTypes.MOUSEMOVE;
//				event.x= evt.x= (short)cx;
//				event.y= evt.y= (short)((win.sizey-1) - cy);
                evt.x= (short)cx;
				evt.y= (short)((win.sizey-1) - cy);
				
				event.x= evt.x;
				event.y= evt.y;
				
				event.type= WmEventTypes.MOUSEMOVE;
				
				/* some painting operators want accurate mouse events, they can
				   handle in between mouse move moves, others can happily ignore
				   them for better performance */
				if(lastevent!=null && lastevent.type == WmEventTypes.MOUSEMOVE)
					lastevent.type = WmEventTypes.INBETWEEN_MOUSEMOVE;

//				update_tablet_data(win, &event);
				wm_event_add(win, event);

//				cx = StrictMath.abs((win.downx - event.x));
//				cy = StrictMath.abs((win.downy - event.y));
//
//				/* probably minimum drag size should be #defined instead of hardcoded 3
//				 * also, cy seems always to be 6 pixels off, not sure why
//				 */
//				if ((win.downstate == WmEventTypes.LEFTMOUSE || win.downstate == WmEventTypes.MOUSEDRAG) && (cx > 3 || cy > 9)) {
////                                        wmEvent dragevt= evt.copy();
//                    wmEvent dragevt= eventCopy(evt);
//					dragevt.type= WmEventTypes.MOUSEDRAG;
//					dragevt.customdata= null;
//					dragevt.customdatafree= 0;
//
//					win.downstate= WmEventTypes.MOUSEDRAG;
//
//					wm_event_add(win, dragevt);
//				}
			}
			break;
		}
		/* mouse button */
		case GHOST_kEventButtonDown:
		case GHOST_kEventButtonUp: {
//            System.out.println("GHOST_kEventButtonDown|GHOST_kEventButtonUp");
//			GHOST_TEventButtonData *bd= customdata;
            MouseEvent bd = (MouseEvent) customdata;
//			event.val= (short)((type==GHOST_TEventType.GHOST_kEventButtonDown)?1:0);
            event.val= (short)((type==GHOST_TEventType.GHOST_kEventButtonDown)?WmTypes.KM_PRESS:WmTypes.KM_RELEASE); /* Note!, this starts as 0/1 but later is converted to KM_PRESS/KM_RELEASE by tweak */

//			if (bd.button == GHOST_TEventType.GHOST_kButtonMaskLeft)
            if (bd.getButton() == MouseEvent.BUTTON1) {
				event.type= WmEventTypes.LEFTMOUSE;
//				System.out.println("LEFTMOUSE");
            }
//			else if (bd.button == GHOST_TEventType.GHOST_kButtonMaskRight)
            else if (bd.getButton() == MouseEvent.BUTTON3) {
				event.type= WmEventTypes.RIGHTMOUSE;
//				System.out.println("RIGHTMOUSE");
            }
			else {
				event.type= WmEventTypes.MIDDLEMOUSE;
//				System.out.println("MIDDLEMOUSE");
			}

//			if(event.val!=0)
			if(event.val == WmTypes.KM_PRESS)
				event.keymodifier= evt.keymodifier= event.type;
			else
				event.keymodifier= evt.keymodifier= 0;

//			update_tablet_data(win, &event);
			wm_event_add(win, event);

//			if (event.val!=0) {
//				win.downstate= event.type;
//				win.downx= event.x;
//				win.downy= event.y;
//			}
//			else {
//				short downstate= win.downstate;
//
//				win.downstate= 0;
//				win.downx= 0;
//				win.downy= 0;
//
//				if (downstate == WmEventTypes.MOUSEDRAG) {
////					wmEvent dropevt= *evt;
//                    wmEvent dropevt= eventCopy(evt);
//					dropevt.type= WmEventTypes.MOUSEDROP;
//					dropevt.customdata= null;
//					dropevt.customdatafree= 0;
//
//					wm_event_add(win, dropevt);
//				}
//			}

			break;
		}
		/* keyboard */
		case GHOST_kEventKeyDown:
		case GHOST_kEventKeyUp: {
            System.out.println("GHOST_kEventKeyDown|GHOST_kEventKeyUp");
//			GHOST_TEventKeyData *kd= customdata;
            KeyEvent kd = (KeyEvent) customdata;
//                        System.out.println("java key event: "+kd.getKeyCode());
			event.type= (short)convert_key(kd.getKeyCode(), kd.getKeyLocation());
//                        System.out.println("blender key event: "+event.type);
			event.ascii= (byte)kd.getKeyChar();
			event.val= (short)((type==GHOST_TEventType.GHOST_kEventKeyDown)?WmTypes.KM_PRESS:WmTypes.KM_RELEASE);

			/* exclude arrow keys, esc, etc from text input */
			if(type==GHOST_TEventType.GHOST_kEventKeyUp || (event.ascii<32 && event.ascii>14))
				event.ascii= '\0';

			/* modifiers */
			if (event.type==WmEventTypes.LEFTSHIFTKEY || event.type==WmEventTypes.RIGHTSHIFTKEY) {
				event.shift= evt.shift= (short)((event.val==WmTypes.KM_PRESS)?1:0);
				if(event.val==WmTypes.KM_PRESS && (evt.ctrl!=0 || evt.alt!=0 || evt.oskey!=0))
				   event.shift= evt.shift = 3;		// define?
			}
			else if (event.type==WmEventTypes.LEFTCTRLKEY || event.type==WmEventTypes.RIGHTCTRLKEY) {
				event.ctrl= evt.ctrl= (short)((event.val==WmTypes.KM_PRESS)?1:0);
				if(event.val==WmTypes.KM_PRESS && (evt.shift!=0 || evt.alt!=0 || evt.oskey!=0))
				   event.ctrl= evt.ctrl = 3;		// define?
			}
			else if (event.type==WmEventTypes.LEFTALTKEY || event.type==WmEventTypes.RIGHTALTKEY) {
				event.alt= evt.alt= (short)((event.val==WmTypes.KM_PRESS)?1:0);
				if(event.val==WmTypes.KM_PRESS && (evt.ctrl!=0 || evt.shift!=0 || evt.oskey!=0))
				   event.alt= evt.alt = 3;		// define?
			}
			else if (event.type==WmEventTypes.COMMANDKEY) {
				event.oskey= evt.oskey= (short)((event.val==WmTypes.KM_PRESS)?1:0);
				if(event.val==WmTypes.KM_PRESS && (evt.ctrl!=0 || evt.alt!=0 || evt.shift!=0))
				   event.oskey= evt.oskey = 3;		// define?
			}

			/* if test_break set, it catches this. XXX Keep global for now? */
			if(event.type==WmEventTypes.ESCKEY)
				G.afbreek= 1;

			wm_event_add(win, event);

			break;
		}

		case GHOST_kEventWheel:	{
//                        System.out.println("GHOST_kEventWheel");
//			GHOST_TEventWheelData* wheelData = customdata;
                        MouseWheelEvent wheelData = (MouseWheelEvent) customdata;

			if (-wheelData.getWheelRotation() > 0) {
				event.type= WmEventTypes.WHEELUPMOUSE;
                                System.out.println("WHEELUPMOUSE");
                        }
			else {
				event.type= WmEventTypes.WHEELDOWNMOUSE;
                                System.out.println("WHEELDOWNMOUSE");
                        }

			event.val= WmTypes.KM_PRESS;
			wm_event_add(win, event);

			break;
		}
//		case GHOST_kEventTimer: {
//			event.type= TIMER;
//			event.custom= EVT_DATA_TIMER;
//			event.customdata= customdata;
//			wm_event_add(win, &event);
//
//			break;
//		}

		case GHOST_kEventUnknown:
		case GHOST_kNumEventTypes:
			break;
	}
}

public static wmEvent eventCopy(Object event) {
	if (event == null)
		return null;
    wmEvent evt = ((wmEvent)event).copy();
    evt.mval = new int[2];
    System.arraycopy(((wmEvent)event).mval, 0, evt.mval, 0, 2);
    return evt;
}

}
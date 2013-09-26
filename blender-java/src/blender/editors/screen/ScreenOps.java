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
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.screen;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.Arithb;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.Rct;
import blender.editors.screen.Area.AZone;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UIRegions;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.editors.uinterface.UIRegions.uiPopupMenu;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.WindowManagerTypes.wmOperatorType.Operator;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.ScrEdge;
import blender.makesdna.sdna.ScrVert;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.rcti;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaDefine;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmWindow;
import blender.windowmanager.WmOperators.OpFunc;
import blender.windowmanager.WmTypes.wmEvent;

public class ScreenOps {

public static final int KM_MODAL_CANCEL=		1;
public static final int KM_MODAL_APPLY=		2;
public static final int KM_MODAL_STEP10=		3;
public static final int KM_MODAL_STEP10_OFF=	4;

/* ************** Exported Poll tests ********************** */

//int ED_operator_regionactive(bContext *C)
//{
//	if(CTX_wm_window(C)==NULL) return 0;
//	if(CTX_wm_screen(C)==NULL) return 0;
//	if(CTX_wm_region(C)==NULL) return 0;
//	return 1;
//}

public static wmOperatorType.Operator ED_operator_areaactive = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//public static boolean ED_operator_areaactive(bContext C)
{
	if(bContext.CTX_wm_window(C)==null) return 0;
	if(bContext.CTX_wm_screen(C)==null) return 0;
	if(bContext.CTX_wm_area(C)==null) return 0;
	return 1;
}};

public static wmOperatorType.Operator ED_operator_screenactive = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int ED_operator_screenactive(bContext *C)
{
	if(bContext.CTX_wm_window(C)==null) return 0;
	if(bContext.CTX_wm_screen(C)==null) return 0;
	return 1;
}};

/* when mouse is over area-edge */
public static wmOperatorType.Operator ED_operator_screen_mainwinactive = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int ED_operator_screen_mainwinactive(bContext *C)
{
	if(bContext.CTX_wm_window(C)==null) return 0;
	if(bContext.CTX_wm_screen(C)==null) return 0;
	if (bContext.CTX_wm_screen(C).subwinactive!=bContext.CTX_wm_screen(C).mainwin) return 0;
	return 1;
}};

public static Operator ED_operator_scene_editable = new Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int ED_operator_scene_editable(bContext *C)
{
	Scene scene= bContext.CTX_data_scene(C);
	if(scene!=null && scene.id.lib==null)
		return 1;
	return 0;
}};

static int ed_spacetype_test(bContext C, int type)
{
	if(ED_operator_areaactive.run(C, null, null)!=0) {
		SpaceLink sl= (SpaceLink)bContext.CTX_wm_space_data(C);
		return (sl!=null && (sl.spacetype == type))?1:0;
	}
	return 0;
}

public static wmOperatorType.Operator ED_operator_view3d_active = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent unused)
//int ED_operator_view3d_active(bContext *C)
{
	return ed_spacetype_test(C, SpaceTypes.SPACE_VIEW3D);
}};

public static wmOperatorType.Operator ED_operator_region_view3d_active = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	if(bContext.CTX_wm_region_view3d(C)!=null)
		return 1;

	bContext.CTX_wm_operator_poll_msg_set(C, "expected a view3d region");
	return 0;
}};

//int ED_operator_timeline_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_TIME);
//}

public static wmOperatorType.Operator ED_operator_outliner_active = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	return ed_spacetype_test(C, SpaceTypes.SPACE_OUTLINER);
}};

//int ED_operator_file_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_FILE);
//}
//
//int ED_operator_action_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_ACTION);
//}
//
//int ED_operator_buttons_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_BUTS);
//}
//
//int ED_operator_node_active(bContext *C)
//{
//	SpaceNode *snode= CTX_wm_space_node(C);
//
//	if(snode && snode.edittree)
//		return 1;
//
//	return 0;
//}
//
//// XXX rename
//int ED_operator_ipo_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_IPO);
//}
//
//int ED_operator_sequencer_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_SEQ);
//}
//
//int ED_operator_image_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_IMAGE);
//}
//
//int ED_operator_nla_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_NLA);
//}
//
//int ED_operator_logic_active(bContext *C)
//{
//	return ed_spacetype_test(C, SPACE_LOGIC);
//}
//
//int ED_operator_object_active(bContext *C)
//{
//	return NULL != CTX_data_active_object(C);
//}
//
//int ED_operator_editmesh(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_MESH)
//		return NULL != ((Mesh *)obedit.data).edit_mesh;
//	return 0;
//}
//
//int ED_operator_editarmature(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_ARMATURE)
//		return NULL != ((bArmature *)obedit.data).edbo;
//	return 0;
//}
//
//int ED_operator_posemode(bContext *C)
//{
//	Object *obact= CTX_data_active_object(C);
//	Object *obedit= CTX_data_edit_object(C);
//
//	if ((obact != obedit) && (obact) && (obact.type==OB_ARMATURE))
//		return (obact.flag & OB_POSEMODE)!=0;
//
//	return 0;
//}
//
//
//int ED_operator_uvedit(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	EditMesh *em= NULL;
//
//	if(obedit && obedit.type==OB_MESH)
//		em= BKE_mesh_get_editmesh((Mesh *)obedit.data);
//
//	if(em && (em.faces.first) && (CustomData_has_layer(&em.fdata, CD_MTFACE))) {
//		BKE_mesh_end_editmesh(obedit.data, em);
//		return 1;
//	}
//
//	if(obedit)
//		BKE_mesh_end_editmesh(obedit.data, em);
//	return 0;
//}
//
//int ED_operator_uvmap(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	EditMesh *em= NULL;
//
//	if(obedit && obedit.type==OB_MESH)
//		em= BKE_mesh_get_editmesh((Mesh *)obedit.data);
//
//	if(em && (em.faces.first)) {
//		BKE_mesh_end_editmesh(obedit.data, em);
//		return 1;
//	}
//
//	if(obedit)
//		BKE_mesh_end_editmesh(obedit.data, em);
//	return 0;
//}
//
//int ED_operator_editsurfcurve(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && ELEM(obedit.type, OB_CURVE, OB_SURF))
//		return NULL != ((Curve *)obedit.data).editnurb;
//	return 0;
//}
//
//
//int ED_operator_editcurve(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_CURVE)
//		return NULL != ((Curve *)obedit.data).editnurb;
//	return 0;
//}
//
//int ED_operator_editsurf(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_SURF)
//		return NULL != ((Curve *)obedit.data).editnurb;
//	return 0;
//}
//
//int ED_operator_editfont(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_FONT)
//		return NULL != ((Curve *)obedit.data).editfont;
//	return 0;
//}
//
//int ED_operator_editlattice(bContext *C)
//{
//	Object *obedit= CTX_data_edit_object(C);
//	if(obedit && obedit.type==OB_LATTICE)
//		return NULL != ((Lattice *)obedit.data).editlatt;
//	return 0;
//}

/* *************************** action zone operator ************************** */

/* operator state vars used:
	none

functions:

	apply() set actionzone event

	exit()	free customdata

callbacks:

	exec()	never used

	invoke() check if in zone
		add customdata, put mouseco and area in it
		add modal handler

	modal()	accept modal events while doing it
		call apply() with gesture info, active window, nonactive window
		call exit() and remove handler when LMB confirm

*/

public static class sActionzoneData {
	public ScrArea sa1, sa2;
	public AZone az;
	public int x, y, gesture_dir, modifier;
};

/* used by other operators too */
static ScrArea screen_areahascursor(bScreen scr, int x, int y)
{
	ScrArea sa= null;
	sa= (ScrArea)scr.areabase.first;
	while(sa!=null) {
		if(Rct.BLI_in_rcti(sa.totrct, x, y)) break;
		sa= sa.next;
	}

	return sa;
}

/* quick poll to save operators to be created and handled */
public static wmOperatorType.Operator actionzone_area_poll = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int actionzone_area_poll(bContext *C)
{
	wmWindow win= bContext.CTX_wm_window(C);
	ScrArea sa= bContext.CTX_wm_area(C);

	if(sa!=null && win!=null) {
		AZone az;
		int x= ((wmEvent)win.eventstate).x;
		int y= ((wmEvent)win.eventstate).y;

		for(az= (AZone)sa.actionzones.first; az!=null; az= az.next)
			if(Rct.BLI_in_rcti(az.rect, x, y))
			   return 1;
	}
	return 0;
}};

public static AZone is_in_area_actionzone(ScrArea sa, int x, int y)
{
	AZone az= null;

	for(az= (AZone)sa.actionzones.first; az!=null; az= az.next) {
		if(Rct.BLI_in_rcti(az.rect, x, y)) {
			if(az.type == Area.AZONE_AREA) {
				if(Arithb.IsPointInTri2DInts(az.x1, az.y1, az.x2, az.y2, x, y))
					break;
			}
			else if(az.type == Area.AZONE_REGION) {
				float[] v1=new float[2], v2=new float[2], v3=new float[2], pt=new float[2];

				v1[0]= az.x1; v1[1]= az.y1;
				v2[0]= az.x2; v2[1]= az.y2;
				v3[0]= az.x3; v3[1]= az.y3;
				pt[0]= x; pt[1]=y;

				if(Arithb.IsPointInTri2D(v1, v2, v3, pt))
					break;
			}
		}
	}

	return az;
}


static void actionzone_exit(bContext C, wmOperator op)
{
//	if(op.customdata!=null)
//		MEM_freeN(op.customdata);
	op.customdata= null;
}

/* send EVT_ACTIONZONE event */
static void actionzone_apply(bContext C, wmOperator op, int type)
{
	wmEvent event = new wmEvent();
	wmWindow win= bContext.CTX_wm_window(C);
	sActionzoneData sad= (sActionzoneData)op.customdata;

	sad.modifier= RnaAccess.RNA_int_get(op.ptr, "modifier");

//	event= win.eventstate.copy();	/* XXX huh huh? make api call */
        event= WmEventSystem.eventCopy(win.eventstate);
	if(type==Area.AZONE_AREA)
		event.type= WmEventTypes.EVT_ACTIONZONE_AREA;
	else
		event.type= WmEventTypes.EVT_ACTIONZONE_REGION;
	event.customdata= op.customdata;
	event.customdatafree= 1;
	op.customdata= null;

	WmEventSystem.wm_event_add(win, event);
}

public static wmOperatorType.Operator actionzone_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	System.out.println(actionzone_invoke);
	AZone az= is_in_area_actionzone(bContext.CTX_wm_area(C), event.x, event.y);
	sActionzoneData sad;

	/* quick escape */
	if(az==null)
		return WindowManagerTypes.OPERATOR_PASS_THROUGH;

	/* ok we do the actionzone */
	op.customdata= sad = new sActionzoneData();
	sad.sa1= bContext.CTX_wm_area(C);
	sad.az= az;
	sad.x= event.x; sad.y= event.y;

	/* region azone directly reacts on mouse clicks */
	if(sad.az.type==Area.AZONE_REGION) {
		actionzone_apply(C, op, Area.AZONE_REGION);
		actionzone_exit(C, op);
		return WindowManagerTypes.OPERATOR_FINISHED;
	}
	else {
		/* add modal handler */
//		WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
		WmEventSystem.WM_event_add_modal_handler(C, op);

		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	}
}};


public static wmOperatorType.Operator actionzone_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int actionzone_modal(bContext *C, wmOperator *op, wmEvent *event)
{
	sActionzoneData sad= (sActionzoneData)op.customdata;
	int deltax, deltay;
	int mindelta= sad.az.type==Area.AZONE_REGION?1:12;

	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			/* calculate gesture direction */
			deltax= (event.x - sad.x);
			deltay= (event.y - sad.y);

			if(deltay > UtilDefines.ABS(deltax))
				sad.gesture_dir= 'n';
			else if(deltax > UtilDefines.ABS(deltay))
				sad.gesture_dir= 'e';
			else if(deltay < -UtilDefines.ABS(deltax))
				sad.gesture_dir= 's';
			else
				sad.gesture_dir= 'w';

			/* gesture is large enough? */
			if(UtilDefines.ABS(deltax) > mindelta || UtilDefines.ABS(deltay) > mindelta) {

				/* second area, for join */
				sad.sa2= screen_areahascursor(bContext.CTX_wm_screen(C), event.x, event.y);
				/* apply sends event */
				actionzone_apply(C, op, sad.az.type);
				actionzone_exit(C, op);

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
				break;
		case WmEventTypes.ESCKEY:
			actionzone_exit(C, op);
			return WindowManagerTypes.OPERATOR_CANCELLED;
		case WmEventTypes.LEFTMOUSE:
			actionzone_exit(C, op);
			return WindowManagerTypes.OPERATOR_CANCELLED;

	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc SCREEN_OT_actionzone = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Handle area action zones";
	ot.idname= "SCREEN_OT_actionzone";

	ot.invoke= actionzone_invoke;
	ot.modal= actionzone_modal;
	ot.poll= actionzone_area_poll;

	ot.flag= WmTypes.OPTYPE_BLOCKING;

	RnaDefine.RNA_def_int(ot.srna, "modifier", 0, 0, 2, "modifier", "modifier state", 0, 2);
}};

/* ************** swap area operator *********************************** */

/* operator state vars used:
 					sa1		start area
					sa2		area to swap with

	functions:

	init()   set custom data for operator, based on actionzone event custom data

	cancel()	cancel the operator

	exit()	cleanup, send notifier

	callbacks:

	invoke() gets called on shift+lmb drag in actionzone
            call init(), add handler

	modal()  accept modal events while doing it

*/

public static class sAreaSwapData {
	public ScrArea sa1, sa2;
};

static boolean area_swap_init(bContext C, wmOperator op, wmEvent event)
{
	sAreaSwapData sd= null;
	sActionzoneData sad= (sActionzoneData)event.customdata;

	if(sad==null || sad.sa1==null)
					return false;

	sd= new sAreaSwapData();
	sd.sa1= sad.sa1;
	sd.sa2= sad.sa2;
	op.customdata= sd;

	return true;
}


static void area_swap_exit(bContext C, wmOperator op)
{
//	if(op.customdata)
//		MEM_freeN(op.customdata);
	op.customdata= null;
}

static int area_swap_cancel(bContext C, wmOperator op)
{
	area_swap_exit(C, op);
	return WindowManagerTypes.OPERATOR_CANCELLED;
}

public static wmOperatorType.Operator area_swap_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_swap_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	if(!area_swap_init(C, op, event))
		return WindowManagerTypes.OPERATOR_PASS_THROUGH;

	/* add modal handler */
//	WM_cursor_modal(bContext.CTX_wm_window(C), BC_SWAPAREA_CURSOR);
//	WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
	WmEventSystem.WM_event_add_modal_handler(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator area_swap_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_swap_modal(bContext *C, wmOperator *op, wmEvent *event)
{
	sActionzoneData sad= (sActionzoneData)op.customdata;

	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			/* second area, for join */
			sad.sa2= screen_areahascursor(bContext.CTX_wm_screen(C), event.x, event.y);
			break;
		case WmEventTypes.LEFTMOUSE: /* release LMB */
			if(event.val==WmTypes.KM_RELEASE) {
				if(sad.sa1 == sad.sa2) {

					return area_swap_cancel(C, op);
				}
				Area.ED_area_swapspace((GL2)GLU.getCurrentGL(), C, sad.sa1, sad.sa2);
//				Area.ED_area_swapspace(C, sad.sa1, sad.sa2);

				area_swap_exit(C, op);

				WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
			break;

		case WmEventTypes.ESCKEY:
			return area_swap_cancel(C, op);
	}
	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc SCREEN_OT_area_swap = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_area_swap(wmOperatorType *ot)
{
	ot.name= "Swap areas";
	ot.idname= "SCREEN_OT_area_swap";

	ot.invoke= area_swap_invoke;
	ot.modal= area_swap_modal;
	ot.poll= ED_operator_areaactive;

	ot.flag= WmTypes.OPTYPE_BLOCKING;
}};

/* *********** Duplicate area as new window operator ****************** */

/* operator callback */
public static wmOperatorType.Operator area_dupli_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_dupli_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	wmWindow newwin, win;
	bScreen newsc, sc;
	ScrArea sa;
	rcti rect = new rcti();

	win= bContext.CTX_wm_window(C);
	sc= bContext.CTX_wm_screen(C);
	sa= bContext.CTX_wm_area(C);

	/* XXX hrmf! */
	if(event.type==WmEventTypes.EVT_ACTIONZONE_AREA) {
		sActionzoneData sad= (sActionzoneData)event.customdata;

		if(sad==null)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		sa= sad.sa1;
	}

	/*  poll() checks area context, but we don't accept full-area windows */
	if(sc.full != ScreenTypes.SCREENNORMAL) {
		if(event.type==WmEventTypes.EVT_ACTIONZONE_AREA)
			actionzone_exit(C, op);
		return WindowManagerTypes.OPERATOR_CANCELLED;
	}

	/* adds window to WM */
//	rect= sa.totrct;
        rect= sa.totrct.copy();
	Rct.BLI_translate_rcti(rect, win.posx, win.posy);
	newwin= WmWindow.WM_window_open(C, rect);

	/* allocs new screen and adds to newly created window, using window size */
	newsc= ScreenEdit.ED_screen_add(newwin, bContext.CTX_data_scene(C), sc.id.name,2);
	newwin.screen= newsc;

	/* copy area to new screen */
	Area.area_copy_data((ScrArea)newsc.areabase.first, sa, 0);

	/* screen, areas init */
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

	if(event.type==WmEventTypes.EVT_ACTIONZONE_AREA)
		actionzone_exit(C, op);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_area_dupli = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_area_dupli(wmOperatorType *ot)
{
	ot.name= "Duplicate Area into New Window";
	ot.idname= "SCREEN_OT_area_dupli";

	ot.invoke= area_dupli_invoke;
	ot.poll= ED_operator_areaactive;
}};


/* ************** move area edge operator *********************************** */

/* operator state vars used:
           x, y   			mouse coord near edge
           delta            movement of edge

	functions:

	init()   set default property values, find edge based on mouse coords, test
            if the edge can be moved, select edges, calculate min and max movement

	apply()	apply delta on selection

	exit()	cleanup, send notifier

	cancel() cancel moving

	callbacks:

	exec()   execute without any user interaction, based on properties
            call init(), apply(), exit()

	invoke() gets called on mouse click near edge
            call init(), add handler

	modal()  accept modal events while doing it
			call apply() with delta motion
            call exit() and remove handler

*/

public static class sAreaMoveData {
	public int bigger, smaller, origval, step;
	public char dir;
};

/* helper call to move area-edge, sets limits */
static void area_move_set_limits(bScreen sc, int dir, int[] bigger, int[] smaller)
{
	ScrArea sa;

	/* we check all areas and test for free space with MINSIZE */
	bigger[0]= smaller[0]= 100000;

	for(sa= (ScrArea)sc.areabase.first; sa!=null; sa= sa.next) {
		if(dir=='h') {
			int y1= sa.v2.vec.y - sa.v1.vec.y-ScreenTypes.AREAMINY;

			/* if top or down edge selected, test height */
			if(sa.v1.flag!=0 && sa.v4.flag!=0)
				bigger[0]= UtilDefines.MIN2(bigger[0], y1);
			else if(sa.v2.flag!=0 && sa.v3.flag!=0)
				smaller[0]= UtilDefines.MIN2(smaller[0], y1);
		}
		else {
			int x1= sa.v4.vec.x - sa.v1.vec.x-ScreenTypes.AREAMINX;

			/* if left or right edge selected, test width */
			if(sa.v1.flag!=0 && sa.v2.flag!=0)
				bigger[0]= UtilDefines.MIN2(bigger[0], x1);
			else if(sa.v3.flag!=0 && sa.v4.flag!=0)
				smaller[0]= UtilDefines.MIN2(smaller[0], x1);
		}
	}
}

/* validate selection inside screen, set variables OK */
/* return 0: init failed */
static boolean area_move_init (bContext C, wmOperator op)
{
//        System.out.println("area_move_init");
	bScreen sc= bContext.CTX_wm_screen(C);
	ScrEdge actedge;
	sAreaMoveData md;
	int x, y;

	/* required properties */
	x= RnaAccess.RNA_int_get(op.ptr, "x");
	y= RnaAccess.RNA_int_get(op.ptr, "y");

	/* setup */
	actedge= ScreenEdit.screen_find_active_scredge(sc, x, y);
	if(actedge==null) return false;

	md= new sAreaMoveData();
	op.customdata= md;

	md.dir= ScreenEdit.scredge_is_horizontal(actedge)?'h':'v';
	if(md.dir=='h') md.origval= actedge.v1.vec.y;
	else md.origval= actedge.v1.vec.x;

	ScreenEdit.select_connected_scredge(sc, actedge);
	/* now all vertices with 'flag==1' are the ones that can be moved. */

        int[] md_bigger={md.bigger}, md_smaller={md.smaller};
	area_move_set_limits(sc, md.dir, md_bigger, md_smaller);
        md.bigger=md_bigger[0]; md.smaller=md_smaller[0];

	return true;
}

/* moves selected screen edge amount of delta, used by split & move */
static void area_move_apply_do(bContext C, int origval, int delta, char dir, int bigger, int smaller)
{
	wmWindow win= bContext.CTX_wm_window(C);
	bScreen sc= bContext.CTX_wm_screen(C);
	ScrVert v1;

	delta= UtilDefines.CLAMPIS(delta, -smaller, bigger);

//        System.out.println("area_move_apply_do delta:"+delta);

	for (v1= (ScrVert)sc.vertbase.first; v1!=null; v1= v1.next) {
		if (v1.flag!=0) {
			/* that way a nice AREAGRID  */
			if((dir=='v') && v1.vec.x>0 && v1.vec.x<win.sizex-1) {
				v1.vec.x= (short)(origval + delta);
				if(delta != bigger && delta != -smaller) v1.vec.x-= (v1.vec.x % ScreenTypes.AREAGRID);
			}
			if((dir=='h') && v1.vec.y>0 && v1.vec.y<win.sizey-1) {
				v1.vec.y= (short)(origval + delta);

				v1.vec.y+= ScreenTypes.AREAGRID-1;
				v1.vec.y-= (v1.vec.y % ScreenTypes.AREAGRID);

				/* prevent too small top header */
				if(v1.vec.y > win.sizey-ScreenTypes.AREAMINY)
					v1.vec.y= (short)(win.sizey-ScreenTypes.AREAMINY);
			}
		}
	}

	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
}

static void area_move_apply(bContext C, wmOperator op)
{
//        System.out.println("area_move_apply");
	sAreaMoveData md= (sAreaMoveData)op.customdata;
	int delta;

	delta= RnaAccess.RNA_int_get(op.ptr, "delta");
	area_move_apply_do(C, md.origval, delta, md.dir, md.bigger, md.smaller);
}

static void area_move_exit(bContext C, wmOperator op)
{
//        System.out.println("area_move_exit");
//	if(op.customdata!=null)
//		MEM_freeN(op.customdata);
	op.customdata= null;

	/* this makes sure aligned edges will result in aligned grabbing */
	ScreenEdit.removedouble_scrverts(bContext.CTX_wm_screen(C));
	ScreenEdit.removedouble_scredges(bContext.CTX_wm_screen(C));
}

public static wmOperatorType.Operator area_move_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_move_exec(bContext *C, wmOperator *op)
{
        System.out.println("area_move_exec");
	if(!area_move_init(C, op))
		return WindowManagerTypes.OPERATOR_CANCELLED;

	area_move_apply(C, op);
	area_move_exit(C, op);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

/* interaction callback */
public static wmOperatorType.Operator area_move_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_move_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("area_move_invoke");
	RnaAccess.RNA_int_set(op.ptr, "x", event.x);
	RnaAccess.RNA_int_set(op.ptr, "y", event.y);

	if(!area_move_init(C, op))
		return WindowManagerTypes.OPERATOR_PASS_THROUGH;

	/* add temp handler */
//	WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
	WmEventSystem.WM_event_add_modal_handler(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator area_move_cancel = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_move_cancel(bContext *C, wmOperator *op)
{
        System.out.println("area_move_cancel");
	RnaAccess.RNA_int_set(op.ptr, "delta", 0);
	area_move_apply(C, op);
	area_move_exit(C, op);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

/* modal callback for while moving edges */
public static wmOperatorType.Operator area_move_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_move_modal(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("area_move_modal");
	sAreaMoveData md= (sAreaMoveData)op.customdata;
	int delta, x, y;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:

			x= RnaAccess.RNA_int_get(op.ptr, "x");
			y= RnaAccess.RNA_int_get(op.ptr, "y");

			delta= (md.dir == 'v')? event.x - x: event.y - y;
			if(md.step!=0) delta= delta - (delta % md.step);
			RnaAccess.RNA_int_set(op.ptr, "delta", delta);

			area_move_apply(C, op);
			break;

		case WmEventTypes.EVT_MODAL_MAP:
//                        System.out.println("event = EVT_MODAL_MAP");
			switch (event.val) {
				case KM_MODAL_APPLY:
					area_move_exit(C, op);
					return WindowManagerTypes.OPERATOR_FINISHED;

				case KM_MODAL_CANCEL:
					return area_move_cancel.run(C, op, null);

				case KM_MODAL_STEP10:
					md.step= 10;
					break;
				case KM_MODAL_STEP10_OFF:
					md.step= 0;
					break;
			}
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc SCREEN_OT_area_move = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_area_move(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Move area edges";
	ot.idname= "SCREEN_OT_area_move";

	ot.exec= area_move_exec;
	ot.invoke= area_move_invoke;
	ot.cancel= area_move_cancel;
	ot.modal= area_move_modal;
	ot.poll= ED_operator_screen_mainwinactive; /* when mouse is over area-edge */

	ot.flag= WmTypes.OPTYPE_BLOCKING;

	/* rna */
	RnaDefine.RNA_def_int(ot.srna, "x", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "X", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "y", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Y", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "delta", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Delta", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
}};

/* ************** split area operator *********************************** */

/*
operator state vars:
	fac              spit point
	dir              direction 'v' or 'h'

operator customdata:
	area   			pointer to (active) area
	x, y			last used mouse pos
	(more, see below)

functions:

	init()   set default property values, find area based on context

	apply()	split area based on state vars

	exit()	cleanup, send notifier

	cancel() remove duplicated area

callbacks:

	exec()   execute without any user interaction, based on state vars
            call init(), apply(), exit()

	invoke() gets called on mouse click in action-widget
            call init(), add modal handler
			call apply() with initial motion

	modal()  accept modal events while doing it
            call move-areas code with delta motion
            call exit() or cancel() and remove handler

*/

public static final int SPLIT_STARTED=	1;
public static final int SPLIT_PROGRESS=	2;

public static class sAreaSplitData
{
	public int x, y;	/* last used mouse position */

	public int origval;			/* for move areas */
	public int bigger, smaller;	/* constraints for moving new edge */
	public int delta;				/* delta move edge */
	public int origmin, origsize;	/* to calculate fac, for property storage */

	public ScrEdge nedge;			/* new edge */
	public ScrArea sarea;			/* start area */
	public ScrArea narea;			/* new area */
};

/* generic init, no UI stuff here */
static boolean area_split_init(bContext C, wmOperator op)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	sAreaSplitData sd;
	int dir;

	/* required context */
	if(sa==null) return false;

	/* required properties */
	dir= RnaAccess.RNA_enum_get(op.ptr, "direction");

	/* minimal size */
	if(dir=='v' && sa.winx < 2*ScreenTypes.AREAMINX) return false;
	if(dir=='h' && sa.winy < 2*ScreenTypes.AREAMINY) return false;

	/* custom data */
	sd= new sAreaSplitData();
	op.customdata= sd;

	sd.sarea= sa;
	sd.origsize= dir=='v' ? sa.winx:sa.winy;
	sd.origmin = dir=='v' ? sa.totrct.xmin:sa.totrct.ymin;

	return true;
}

/* with sa as center, sb is located at: 0=W, 1=N, 2=E, 3=S */
/* used with split operator */
static ScrEdge area_findsharededge(bScreen screen, ScrArea sa, ScrArea sb)
{
	ScrVert sav1= sa.v1;
	ScrVert sav2= sa.v2;
	ScrVert sav3= sa.v3;
	ScrVert sav4= sa.v4;
	ScrVert sbv1= sb.v1;
	ScrVert sbv2= sb.v2;
	ScrVert sbv3= sb.v3;
	ScrVert sbv4= sb.v4;

	if(sav1==sbv4 && sav2==sbv3) { /* sa to right of sb = W */
		return ScreenEdit.screen_findedge(screen, sav1, sav2);
	}
	else if(sav2==sbv1 && sav3==sbv4) { /* sa to bottom of sb = N */
		return ScreenEdit.screen_findedge(screen, sav2, sav3);
	}
	else if(sav3==sbv2 && sav4==sbv1) { /* sa to left of sb = E */
		return ScreenEdit.screen_findedge(screen, sav3, sav4);
	}
	else if(sav1==sbv2 && sav4==sbv3) { /* sa on top of sb = S*/
		return ScreenEdit.screen_findedge(screen, sav1, sav4);
	}

	return null;
}


/* do the split, return success */
static boolean area_split_apply(bContext C, wmOperator op)
{
	bScreen sc= bContext.CTX_wm_screen(C);
	sAreaSplitData sd= (sAreaSplitData)op.customdata;
	float fac;
	int dir;

	fac= RnaAccess.RNA_float_get(op.ptr, "factor");
	dir= RnaAccess.RNA_enum_get(op.ptr, "direction");

	sd.narea= ScreenEdit.area_split(bContext.CTX_wm_window(C), sc, sd.sarea, (char)dir, fac);

	if(sd.narea!=null) {
		ScrVert sv;

		sd.nedge= area_findsharededge(sc, sd.sarea, sd.narea);

		/* select newly created edge, prepare for moving edge */
		for(sv= (ScrVert)sc.vertbase.first; sv!=null; sv= sv.next)
			sv.flag = 0;

		sd.nedge.v1.flag= 1;
		sd.nedge.v2.flag= 1;

		if(dir=='h') sd.origval= sd.nedge.v1.vec.y;
		else sd.origval= sd.nedge.v1.vec.x;

		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

		return true;
	}

	return false;
}

static void area_split_exit(bContext C, wmOperator op)
{
        System.out.println("area_split_exit");
	if (op.customdata!=null) {
//		MEM_freeN(op.customdata);
		op.customdata = null;
	}

	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

	/* this makes sure aligned edges will result in aligned grabbing */
	ScreenEdit.removedouble_scrverts(bContext.CTX_wm_screen(C));
	ScreenEdit.removedouble_scredges(bContext.CTX_wm_screen(C));
}


/* UI callback, adds new handler */
public static wmOperatorType.Operator area_split_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_split_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
        System.out.println("area_split_invoke");
	sAreaSplitData sd;

	if(event.type==WmEventTypes.EVT_ACTIONZONE_AREA) {
		sActionzoneData sad= (sActionzoneData)event.customdata;
		int dir;

		if(sad.modifier>0) {
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;
		}

		/* no full window splitting allowed */
		if(bContext.CTX_wm_area(C).full!=null)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* verify *sad itself */
		if(sad==null || sad.sa1==null || sad.az==null)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* is this our *sad? if areas not equal it should be passed on */
		if(bContext.CTX_wm_area(C)!=sad.sa1 || sad.sa1!=sad.sa2)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* prepare operator state vars */
		if(sad.gesture_dir=='n' || sad.gesture_dir=='s') {
			dir= 'h';
			RnaAccess.RNA_float_set(op.ptr, "factor", ((float)(event.x - sad.sa1.v1.vec.x)) / (float)sad.sa1.winx);
		}
		else {
			dir= 'v';
			RnaAccess.RNA_float_set(op.ptr, "factor", ((float)(event.y - sad.sa1.v1.vec.y)) / (float)sad.sa1.winy);
		}
		RnaAccess.RNA_enum_set(op.ptr, "direction", dir);

		/* general init, also non-UI case, adds customdata, sets area and defaults */
		if(!area_split_init(C, op))
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		sd= (sAreaSplitData)op.customdata;

		sd.x= event.x;
		sd.y= event.y;

		/* do the split */
		if(area_split_apply(C, op)) {
                        int[] sd_bigger={sd.bigger}, sd_smaller={sd.smaller};
			area_move_set_limits(bContext.CTX_wm_screen(C), dir, sd_bigger, sd_smaller);
                        sd.bigger=sd_bigger[0]; sd.smaller=sd_smaller[0];

			/* add temp handler for edge move or cancel */
//			WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
            WmEventSystem.WM_event_add_modal_handler(C, op);

			return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
		}

	}
	else {
		/* nonmodal for now */
		return ((wmOperatorType)op.type).exec.run(C, op, null);
	}

	return WindowManagerTypes.OPERATOR_PASS_THROUGH;
}};

/* function to be called outside UI context, or for redo */
public static wmOperatorType.Operator area_split_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_split_exec(bContext *C, wmOperator *op)
{
        System.out.println("area_split_exec");
	if(!area_split_init(C, op))
		return WindowManagerTypes.OPERATOR_CANCELLED;

	area_split_apply(C, op);
	area_split_exit(C, op);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};


static int area_split_cancel(bContext C, wmOperator op)
{
	sAreaSplitData sd= (sAreaSplitData)op.customdata;

	if (ScreenEdit.screen_area_join(C, bContext.CTX_wm_screen(C), sd.sarea, sd.narea)) {
		if (bContext.CTX_wm_area(C) == sd.narea) {
			bContext.CTX_wm_area_set(C, null);
			bContext.CTX_wm_region_set(C, null);
		}
		sd.narea = null;
	}
	area_split_exit(C, op);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}

public static wmOperatorType.Operator area_split_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_split_modal(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("area_split_modal");
	sAreaSplitData sd= (sAreaSplitData)op.customdata;
	float fac;
	char dir;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:
			dir= (char)RnaAccess.RNA_enum_get(op.ptr, "direction");

			sd.delta= (dir == 'v')? event.x - sd.origval: event.y - sd.origval;
			area_move_apply_do(C, sd.origval, sd.delta, dir, sd.bigger, sd.smaller);

			fac= (dir == 'v') ? event.x-sd.origmin : event.y-sd.origmin;
			RnaAccess.RNA_float_set(op.ptr, "factor", fac / (float)sd.origsize);

			WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
			break;

		case WmEventTypes.LEFTMOUSE:
			if(event.val==WmTypes.KM_RELEASE) { /* mouse up */
//				System.out.println("area_split_modal: mouse up");
				area_split_exit(C, op);
				return WindowManagerTypes.OPERATOR_FINISHED;
			}
			break;
		case WmEventTypes.RIGHTMOUSE: /* cancel operation */
		case WmEventTypes.ESCKEY:
			return area_split_cancel(C, op);
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

static EnumPropertyItem prop_direction_items[] = {
	new EnumPropertyItem('h', "HORIZONTAL", 0, "Horizontal", ""),
	new EnumPropertyItem('v', "VERTICAL", 0, "Vertical", ""),
	new EnumPropertyItem(0, null, 0, null, null)
};

public static OpFunc SCREEN_OT_area_split = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_area_split(wmOperatorType *ot)
{
	ot.name = "Split area";
	ot.idname = "SCREEN_OT_area_split";

	ot.exec= area_split_exec;
	ot.invoke= area_split_invoke;
	ot.modal= area_split_modal;

	ot.poll= ED_operator_areaactive;
	ot.flag= WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_BLOCKING;

	/* rna */
	RnaDefine.RNA_def_enum(ot.srna, "direction", prop_direction_items, 'h', "Direction", "");
	RnaDefine.RNA_def_float(ot.srna, "factor", 0.5f, 0.0f, 1.0f, "Factor", "", 0.0f, 1.0f);
}};



/* ************** scale region edge operator *********************************** */

public static class RegionMoveData {
	public ARegion ar;
	public int bigger, smaller, origval;
	public int origx, origy;
	public char edge;

};

public static wmOperatorType.Operator region_scale_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int region_scale_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	sActionzoneData sad= (sActionzoneData)event.customdata;
	AZone az= sad.az;

	if(az.ar!=null) {
		RegionMoveData rmd= new RegionMoveData();

		op.customdata= rmd;

		rmd.ar= az.ar;
		rmd.edge= (char)az.edge;
		rmd.origx= event.x;
		rmd.origy= event.y;
		if(rmd.edge=='l' || rmd.edge=='r')
			rmd.origval= ((ARegionType)rmd.ar.type).minsizex;
		else
			rmd.origval= ((ARegionType)rmd.ar.type).minsizey;

		/* add temp handler */
//		WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
		WmEventSystem.WM_event_add_modal_handler(C, op);

		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	}

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static wmOperatorType.Operator region_scale_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int region_scale_modal(bContext *C, wmOperator *op, wmEvent *event)
{
	RegionMoveData rmd= (RegionMoveData)op.customdata;
	int delta;

	/* execute the events */
	switch(event.type) {
		case WmEventTypes.MOUSEMOVE:

			if(rmd.edge=='l' || rmd.edge=='r') {
				delta= event.x - rmd.origx;
				if(rmd.edge=='l') delta= -delta;
				((ARegionType)rmd.ar.type).minsizex= rmd.origval + delta;
				((ARegionType)rmd.ar.type).minsizex = Arithb.CLAMP(((ARegionType)rmd.ar.type).minsizex, 0, 1000);
				if(((ARegionType)rmd.ar.type).minsizex < 10) {
					((ARegionType)rmd.ar.type).minsizex= 10;
					rmd.ar.flag |= ScreenTypes.RGN_FLAG_HIDDEN;
				}
				else
					rmd.ar.flag &= ~ScreenTypes.RGN_FLAG_HIDDEN;
			}
			else {
				delta= event.y - rmd.origy;
				if(rmd.edge=='b') delta= -delta;
				((ARegionType)rmd.ar.type).minsizey= rmd.origval + delta;
				((ARegionType)rmd.ar.type).minsizey = Arithb.CLAMP(((ARegionType)rmd.ar.type).minsizey, 0, 1000);
				if(((ARegionType)rmd.ar.type).minsizey < 10) {
					((ARegionType)rmd.ar.type).minsizey= 10;
					rmd.ar.flag |= ScreenTypes.RGN_FLAG_HIDDEN;
				}
				else
					rmd.ar.flag &= ~ScreenTypes.RGN_FLAG_HIDDEN;
			}

			WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

			break;

		case WmEventTypes.LEFTMOUSE:
			if(event.val==WmTypes.KM_RELEASE) {

				if(UtilDefines.ABS(event.x - rmd.origx) < 2 && UtilDefines.ABS(event.y - rmd.origy) < 2) {
					rmd.ar.flag ^= ScreenTypes.RGN_FLAG_HIDDEN;
					WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
				}
//				MEM_freeN(op.customdata);
				op.customdata = null;

				return WindowManagerTypes.OPERATOR_FINISHED;
			}
			break;

		case WmEventTypes.ESCKEY:
			;
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};


public static OpFunc SCREEN_OT_region_scale = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_region_scale(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Scale Region Size";
	ot.idname= "SCREEN_OT_region_scale";

	ot.invoke= region_scale_invoke;
	ot.modal= region_scale_modal;

	ot.poll= ED_operator_areaactive;

	ot.flag= WmTypes.OPTYPE_BLOCKING;
}};


///* ************** frame change operator ***************************** */
//
///* function to be called outside UI context, or for redo */
//static int frame_offset_exec(bContext *C, wmOperator *op)
//{
//	int delta;
//
//	delta = RNA_int_get(op.ptr, "delta");
//
//	CTX_data_scene(C).r.cfra += delta;
//
//	WM_event_add_notifier(C, NC_SCENE|ND_FRAME, CTX_data_scene(C));
//
//	return OPERATOR_FINISHED;
//}
//
//static void SCREEN_OT_frame_offset(wmOperatorType *ot)
//{
//	ot.name = "Frame Offset";
//	ot.idname = "SCREEN_OT_frame_offset";
//
//	ot.exec= frame_offset_exec;
//
//	ot.poll= ED_operator_screenactive;
//	ot.flag= 0;
//
//	/* rna */
//	RNA_def_int(ot.srna, "delta", 0, INT_MIN, INT_MAX, "Delta", "", INT_MIN, INT_MAX);
//}
//
///* ************** jump to keyframe operator ***************************** */
//
///* helper function - find actkeycolumn that occurs on cframe, or the nearest one if not found */
//// TODO: make this an API func?
//static ActKeyColumn *cfra_find_nearest_next_ak (ActKeyColumn *ak, float cframe, short next)
//{
//	ActKeyColumn *akn= NULL;
//
//	/* sanity checks */
//	if (ak == NULL)
//		return NULL;
//
//	/* check if this is a match, or whether it is in some subtree */
//	if (cframe < ak.cfra)
//		akn= cfra_find_nearest_next_ak(ak.left, cframe, next);
//	else if (cframe > ak.cfra)
//		akn= cfra_find_nearest_next_ak(ak.right, cframe, next);
//
//	/* if no match found (or found match), just use the current one */
//	if (akn == NULL)
//		return ak;
//	else
//		return akn;
//}

/* function to be called outside UI context, or for redo */
public static wmOperatorType.Operator frame_jump_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op,  wmEvent event)
{
//	Scene *scene= CTX_data_scene(C);
//	wmTimer *animtimer= CTX_wm_screen(C)->animtimer;
//
//	/* Don't change CFRA directly if animtimer is running as this can cause
//	 * first/last frame not to be actually shown (bad since for example physics
//	 * simulations aren't reset properly).
//	 */
//	if(animtimer) {
//		ScreenAnimData *sad = animtimer->customdata;
//		
//		sad->flag |= ANIMPLAY_FLAG_USE_NEXT_FRAME;
//		
//		if (RNA_boolean_get(op->ptr, "end"))
//			sad->nextfra= PEFRA;
//		else
//			sad->nextfra= PSFRA;
//	}
//	else {
//		if (RNA_boolean_get(op->ptr, "end"))
//			CFRA= PEFRA;
//		else
//			CFRA= PSFRA;
//		
//		sound_seek_scene(C);
//
//		WM_event_add_notifier(C, NC_SCENE|ND_FRAME, scene);
//	}
	
	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_frame_jump = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name = "Jump to Endpoint";
	ot.description= "Jump to first/last frame in frame range";
	ot.idname = "SCREEN_OT_frame_jump";
	
	ot.exec= frame_jump_exec;
	
	ot.poll= ED_operator_screenactive;
	ot.flag= WmTypes.OPTYPE_UNDO;
	
	/* rna */
	RnaDefine.RNA_def_boolean(ot.srna, "end", 0, "Last Frame", "Jump to the last frame of the frame range.");
}};

/* function to be called outside UI context, or for redo */
public static wmOperatorType.Operator keyframe_jump_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op,  wmEvent event)
{
//	Scene scene= bContext.CTX_data_scene(C);
//	bObject ob= bContext.CTX_data_active_object(C);
//	DLRBT_Tree keys;
//	ActKeyColumn ak;
//	boolean next= RnaAccess.RNA_boolean_get(op.ptr, "next");
//
//	/* sanity checks */
//	if (scene == null)
//		return WindowManagerTypes.OPERATOR_CANCELLED;
//
//	/* init binarytree-list for getting keyframes */
//	BLI_dlrbTree_init(keys);
//
//	/* populate tree with keyframe nodes */
//	if (scene!=null && scene.adt!=null)
//		scene_to_keylist(null, scene, keys, null);
//	if (ob!=null && ob.adt!=null)
//		ob_to_keylist(null, ob, keys, null);
//
//	/* build linked-list for searching */
//	BLI_dlrbTree_linkedlist_sync(keys);
//
//	/* find nearest keyframe in the right direction */
//	ak= cfra_find_nearest_next_ak(keys.root, (float)scene.r.cfra, next);
//
//	/* set the new frame (if keyframe found) */
//	if (ak!=null) {
//		if (next && ak.next!=null)
//			scene.r.cfra= (int)ak.next.cfra;
//		else if (!next && ak.prev!=null)
//			scene.r.cfra= (int)ak.prev.cfra;
//		else {
//			System.out.printf("ERROR: no suitable keyframe found. Using %f as new frame \n", ak.cfra);
//			scene.r.cfra= (int)ak.cfra; // XXX
//		}
//	}
//
//	/* free temp stuff */
//	BLI_dlrbTree_free(keys);
//
//	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCENE|WmTypes.ND_FRAME, bContext.CTX_data_scene(C));

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_keyframe_jump = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name = "Jump to Keyframe";
	ot.idname = "SCREEN_OT_keyframe_jump";

	ot.exec= keyframe_jump_exec;

	ot.poll= ED_operator_screenactive;
	ot.flag= 0;

	/* rna */
	RnaDefine.RNA_def_boolean(ot.srna, "next", 1, "Next Keyframe", "");
}};

/* ************** switch screen operator ***************************** */


/* function to be called outside UI context, or for redo */
public static wmOperatorType.Operator screen_set_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int screen_set_exec(bContext *C, wmOperator *op)
{
	bScreen screen= bContext.CTX_wm_screen(C);
	ScrArea sa= bContext.CTX_wm_area(C);
	int tot= ListBaseUtil.BLI_countlist(bContext.CTX_data_main(C).screen);
	int delta= RnaAccess.RNA_int_get(op.ptr, "delta");

	/* this screen is 'fake', solve later XXX */
	if(sa!=null && sa.full!=null)
		return WindowManagerTypes.OPERATOR_CANCELLED;

	if(delta==1) {
		while((tot--)!=0) {
			screen= (bScreen)screen.id.next;
			if(screen==null) screen= bContext.CTX_data_main(C).screen.first;
			if(screen.winid==0 && screen.full==0)
				break;
		}
	}
	else if(delta== -1) {
		while((tot--)!=0) {
			screen= (bScreen)screen.id.prev;
			if(screen==null) screen= bContext.CTX_data_main(C).screen.last;
			if(screen.winid==0 && screen.full==0)
				break;
		}
	}
	else {
		screen= null;
	}

	if(screen!=null) {
		ScreenEdit.ED_screen_set((GL2)GLU.getCurrentGL(), C, screen);
//		ScreenEdit.ED_screen_set(C, screen);
		return WindowManagerTypes.OPERATOR_FINISHED;
	}
	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

public static OpFunc SCREEN_OT_screen_set = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_screen_set(wmOperatorType *ot)
{
	ot.name = "Set Screen";
	ot.idname = "SCREEN_OT_screen_set";

	ot.exec= screen_set_exec;
	ot.poll= ED_operator_screenactive;

	/* rna */
//	RnaDefine.RNA_def_pointer_runtime(ot.srna, "screen", &RNA_Screen, "Screen", "");
	RnaDefine.RNA_def_int(ot.srna, "delta", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Delta", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
}};

///* ************** screen full-area operator ***************************** */
//
//
///* function to be called outside UI context, or for redo */
//static int screen_full_area_exec(bContext *C, wmOperator *op)
//{
//	ed_screen_fullarea(C, CTX_wm_area(C));
//	return OPERATOR_FINISHED;
//}
//
//static void SCREEN_OT_screen_full_area(wmOperatorType *ot)
//{
//	ot.name = "Toggle Make Area Fullscreen";
//	ot.idname = "SCREEN_OT_screen_full_area";
//
//	ot.exec= screen_full_area_exec;
//	ot.poll= ED_operator_areaactive;
//	ot.flag= 0;
//
//}



/* ************** join area operator ********************************************** */

/* operator state vars used:
			x1, y1     mouse coord in first area, which will disappear
			x2, y2     mouse coord in 2nd area, which will become joined

functions:

   init()   find edge based on state vars
			test if the edge divides two areas,
			store active and nonactive area,

   apply()  do the actual join

   exit()	cleanup, send notifier

callbacks:

   exec()	calls init, apply, exit

   invoke() sets mouse coords in x,y
            call init()
            add modal handler

   modal()	accept modal events while doing it
			call apply() with active window and nonactive window
            call exit() and remove handler when LMB confirm

*/

public static class sAreaJoinData
{
	public ScrArea sa1;	/* first area to be considered */
	public ScrArea sa2;	/* second area to be considered */
	public ScrArea scr;	/* designed for removal */

};


/* validate selection inside screen, set variables OK */
/* return 0: init failed */
/* XXX todo: find edge based on (x,y) and set other area? */
static boolean area_join_init(bContext C, wmOperator op)
{
	ScrArea sa1, sa2;
	sAreaJoinData jd= null;
	int x1, y1;
	int x2, y2;

	/* required properties, make negative to get return 0 if not set by caller */
	x1= RnaAccess.RNA_int_get(op.ptr, "x1");
	y1= RnaAccess.RNA_int_get(op.ptr, "y1");
	x2= RnaAccess.RNA_int_get(op.ptr, "x2");
	y2= RnaAccess.RNA_int_get(op.ptr, "y2");

	sa1 = screen_areahascursor(bContext.CTX_wm_screen(C), x1, y1);
	sa2 = screen_areahascursor(bContext.CTX_wm_screen(C), x2, y2);
	if(sa1==null || sa2==null || sa1==sa2)
		return false;

	jd = new sAreaJoinData();

	jd.sa1 = sa1;
	jd.sa1.flag |= ScreenTypes.AREA_FLAG_DRAWJOINFROM;
	jd.sa2 = sa2;
	jd.sa2.flag |= ScreenTypes.AREA_FLAG_DRAWJOINTO;

	op.customdata= jd;

	return true;
}

/* apply the join of the areas (space types) */
static boolean area_join_apply(bContext C, wmOperator op)
{
	sAreaJoinData jd = (sAreaJoinData)op.customdata;
	if (jd==null) return false;

	if(!ScreenEdit.screen_area_join(C, bContext.CTX_wm_screen(C), jd.sa1, jd.sa2)){
		return false;
	}
	if (bContext.CTX_wm_area(C) == jd.sa2) {
		bContext.CTX_wm_area_set(C, null);
		bContext.CTX_wm_region_set(C, null);
	}

	return true;
}

/* finish operation */
static void area_join_exit(bContext C, wmOperator op)
{
	if (op.customdata!=null) {
//		MEM_freeN(op.customdata);
		op.customdata = null;
	}

	/* this makes sure aligned edges will result in aligned grabbing */
	ScreenEdit.removedouble_scredges(bContext.CTX_wm_screen(C));
	ScreenEdit.removenotused_scredges(bContext.CTX_wm_screen(C));
	ScreenEdit.removenotused_scrverts(bContext.CTX_wm_screen(C));
}

public static wmOperatorType.Operator area_join_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_join_exec(bContext *C, wmOperator *op)
{
	if(!area_join_init(C, op))
		return WindowManagerTypes.OPERATOR_CANCELLED;

	area_join_apply(C, op);
	area_join_exit(C, op);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

/* interaction callback */
public static wmOperatorType.Operator area_join_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_join_invoke(bContext *C, wmOperator *op, wmEvent *event)
{

	if(event.type==WmEventTypes.EVT_ACTIONZONE_AREA) {
		sActionzoneData sad= (sActionzoneData)event.customdata;

		if(sad.modifier>0) {
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;
		}

		/* verify *sad itself */
		if(sad==null || sad.sa1==null || sad.sa2==null)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* is this our *sad? if areas equal it should be passed on */
		if(sad.sa1==sad.sa2)
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* prepare operator state vars */
		RnaAccess.RNA_int_set(op.ptr, "x1", sad.x);
		RnaAccess.RNA_int_set(op.ptr, "y1", sad.y);
		RnaAccess.RNA_int_set(op.ptr, "x2", event.x);
		RnaAccess.RNA_int_set(op.ptr, "y2", event.y);

		if(!area_join_init(C, op))
			return WindowManagerTypes.OPERATOR_PASS_THROUGH;

		/* add temp handler */
//		WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
		WmEventSystem.WM_event_add_modal_handler(C, op);

		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	}

	return WindowManagerTypes.OPERATOR_PASS_THROUGH;
}};

static int area_join_cancel(bContext C, wmOperator op)
{
	sAreaJoinData jd = (sAreaJoinData)op.customdata;

	if (jd.sa1!=null) {
		jd.sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;
		jd.sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
	}
	if (jd.sa2!=null) {
		jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;
		jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
	}

	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

	area_join_exit(C, op);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}

/* modal callback while selecting area (space) that will be removed */
public static wmOperatorType.Operator area_join_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int area_join_modal(bContext *C, wmOperator *op, wmEvent *event)
{
	bScreen sc= bContext.CTX_wm_screen(C);
	sAreaJoinData jd = (sAreaJoinData)op.customdata;

	/* execute the events */
	switch(event.type) {

		case WmEventTypes.MOUSEMOVE:
			{
				ScrArea sa = screen_areahascursor(sc, event.x, event.y);
				int dir;

				if (sa!=null) {
					if (jd.sa1 != sa) {
						dir = ScreenEdit.area_getorientation(sc, jd.sa1, sa);
						if (dir >= 0) {
							if (jd.sa2!=null) jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
							jd.sa2 = sa;
							jd.sa2.flag |= ScreenTypes.AREA_FLAG_DRAWJOINTO;
						}
						else {
							/* we are not bordering on the previously selected area
							   we check if area has common border with the one marked for removal
							   in this case we can swap areas.
							*/
							dir = ScreenEdit.area_getorientation(sc, sa, jd.sa2);
							if (dir >= 0) {
								if (jd.sa1!=null) jd.sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;
								if (jd.sa2!=null) jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
								jd.sa1 = jd.sa2;
								jd.sa2 = sa;
								if (jd.sa1!=null) jd.sa1.flag |= ScreenTypes.AREA_FLAG_DRAWJOINFROM;
								if (jd.sa2!=null) jd.sa2.flag |= ScreenTypes.AREA_FLAG_DRAWJOINTO;
							}
							else {
								if (jd.sa2!=null) jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
								jd.sa2 = null;
							}
						}
						WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);
					}
					else {
						/* we are back in the area previously selected for keeping
						 * we swap the areas if possible to allow user to choose */
						if (jd.sa2 != null) {
							if (jd.sa1!=null) jd.sa1.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINFROM;
							if (jd.sa2!=null) jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
							jd.sa1 = jd.sa2;
							jd.sa2 = sa;
							if (jd.sa1!=null) jd.sa1.flag |= ScreenTypes.AREA_FLAG_DRAWJOINFROM;
							if (jd.sa2!=null) jd.sa2.flag |= ScreenTypes.AREA_FLAG_DRAWJOINTO;
							dir = ScreenEdit.area_getorientation(sc, jd.sa1, jd.sa2);
							if (dir < 0) {
								System.out.printf("oops, didn't expect that!\n");
							}
						}
						else {
							dir = ScreenEdit.area_getorientation(sc, jd.sa1, sa);
							if (dir >= 0) {
								if (jd.sa2!=null) jd.sa2.flag &= ~ScreenTypes.AREA_FLAG_DRAWJOINTO;
								jd.sa2 = sa;
								jd.sa2.flag |= ScreenTypes.AREA_FLAG_DRAWJOINTO;
							}
						}
						WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);
					}
				}
			}
			break;
		case WmEventTypes.LEFTMOUSE:
			if(event.val==WmTypes.KM_RELEASE) {
				area_join_apply(C, op);
				WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
				area_join_exit(C, op);
				return WindowManagerTypes.OPERATOR_FINISHED;
			}
			break;

		case WmEventTypes.ESCKEY:
			return area_join_cancel(C, op);
	}

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

/* Operator for joining two areas (space types) */
public static OpFunc SCREEN_OT_area_join = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_area_join(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Join area";
	ot.idname= "SCREEN_OT_area_join";

	/* api callbacks */
	ot.exec= area_join_exec;
	ot.invoke= area_join_invoke;
	ot.modal= area_join_modal;
	ot.poll= ED_operator_areaactive;

	ot.flag= WmTypes.OPTYPE_BLOCKING;

	/* rna */
	RnaDefine.RNA_def_int(ot.srna, "x1", -100, Integer.MIN_VALUE, Integer.MAX_VALUE, "X 1", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "y1", -100, Integer.MIN_VALUE, Integer.MAX_VALUE, "Y 1", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "x2", -100, Integer.MIN_VALUE, Integer.MAX_VALUE, "X 2", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
	RnaDefine.RNA_def_int(ot.srna, "y2", -100, Integer.MIN_VALUE, Integer.MAX_VALUE, "Y 2", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
}};

///* ************** repeat last operator ***************************** */
//
//static int repeat_last_exec(bContext *C, wmOperator *op)
//{
//	wmOperator *lastop= CTX_wm_manager(C).operators.last;
//
//	if(lastop)
//		WM_operator_repeat(C, lastop);
//
//	return OPERATOR_CANCELLED;
//}
//
//static void SCREEN_OT_repeat_last(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Repeat Last";
//	ot.idname= "SCREEN_OT_repeat_last";
//
//	/* api callbacks */
//	ot.exec= repeat_last_exec;
//
//	ot.poll= ED_operator_screenactive;
//
//}
//
//static int repeat_history_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmOperator *lastop;
//	uiPopupMenu *pup;
//	uiLayout *layout;
//	int items, i;
//
//	items= BLI_countlist(&wm.operators);
//	if(items==0)
//		return OPERATOR_CANCELLED;
//
//	pup= uiPupMenuBegin(C, op.type.name, 0);
//	layout= uiPupMenuLayout(pup);
//
//	for (i=items-1, lastop= wm.operators.last; lastop; lastop= lastop.prev, i--)
//		uiItemIntO(layout, lastop.type.name, 0, op.type.idname, "index", i);
//
//	uiPupMenuEnd(C, pup);
//
//	return OPERATOR_CANCELLED;
//}
//
//static int repeat_history_exec(bContext *C, wmOperator *op)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//
//	op= BLI_findlink(&wm.operators, RNA_int_get(op.ptr, "index"));
//	if(op) {
//		/* let's put it as last operator in list */
//		BLI_remlink(&wm.operators, op);
//		BLI_addtail(&wm.operators, op);
//
//		WM_operator_repeat(C, op);
//	}
//
//	return OPERATOR_FINISHED;
//}
//
//static void SCREEN_OT_repeat_history(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Repeat History";
//	ot.idname= "SCREEN_OT_repeat_history";
//
//	/* api callbacks */
//	ot.invoke= repeat_history_invoke;
//	ot.exec= repeat_history_exec;
//
//	ot.poll= ED_operator_screenactive;
//
//	RNA_def_int(ot.srna, "index", 0, 0, INT_MAX, "Index", "", 0, 1000);
//}
//
///* ********************** redo operator ***************************** */
//
//static int redo_last_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmOperator *lastop;
//
//	/* only for operators that are registered and did an undo push */
//	for(lastop= wm.operators.last; lastop; lastop= lastop.prev)
//		if((lastop.type.flag & OPTYPE_REGISTER) && (lastop.type.flag & OPTYPE_UNDO))
//			break;
//
//	if(lastop)
//		WM_operator_redo_popup(C, lastop);
//
//	return OPERATOR_CANCELLED;
//}
//
//static void SCREEN_OT_redo_last(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Redo Last";
//	ot.idname= "SCREEN_OT_redo_last";
//
//	/* api callbacks */
//	ot.invoke= redo_last_invoke;
//
//	ot.poll= ED_operator_screenactive;
//}

/* ************** region split operator ***************************** */

/* insert a region in the area region list */
public static wmOperatorType.Operator region_split_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int region_split_exec(bContext *C, wmOperator *op)
{
	ARegion ar= bContext.CTX_wm_region(C);

//	if(ar.regiontype==ScreenTypes.RGN_TYPE_HEADER)
//		BKE_report(op.reports, RPT_ERROR, "Cannot split header");
//	else if(ar.alignment==ScreenTypes.RGN_ALIGN_QSPLIT)
//		BKE_report(op.reports, RPT_ERROR, "Cannot split further");
//	else {
		ScrArea sa= bContext.CTX_wm_area(C);
		ARegion newar= ScreenUtil.BKE_area_region_copy((SpaceType)sa.type, ar);
		int dir= RnaAccess.RNA_enum_get(op.ptr, "type");

		ListBaseUtil.BLI_insertlinkafter(sa.regionbase, ar, newar);

		newar.alignment= ar.alignment;

		if(dir=='h')
			ar.alignment= ScreenTypes.RGN_ALIGN_HSPLIT;
		else
			ar.alignment= ScreenTypes.RGN_ALIGN_VSPLIT;

		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
//	}

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_region_split = new OpFunc() {
public void run(wmOperatorType ot)
//static void SCREEN_OT_region_split(wmOperatorType *ot)
{
	/* identifiers */
	ot.name= "Split Region";
	ot.idname= "SCREEN_OT_region_split";

	/* api callbacks */
	ot.invoke= WmOperators.WM_menu_invoke;
	ot.exec= region_split_exec;
	ot.poll= ED_operator_areaactive;

	RnaDefine.RNA_def_enum(ot.srna, "type", prop_direction_items, 'h', "Direction", "");
}};

/* ************** region four-split operator ***************************** */

/* insert a region in the area region list */
public static wmOperatorType.Operator region_quadview_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	ARegion ar= bContext.CTX_wm_region(C);

	/* some rules... */
	if(ar.regiontype!=ScreenTypes.RGN_TYPE_WINDOW) {
//		BKE_report(op.reports, RPT_ERROR, "Only window region can be 4-splitted");
        }
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_QSPLIT) {
		ScrArea sa= bContext.CTX_wm_area(C);
		ARegion arn;

		/* keep current region */
		ar.alignment= 0;

		if(sa.spacetype==SpaceTypes.SPACE_VIEW3D) {
			RegionView3D rv3d= (RegionView3D)ar.regiondata;
			rv3d.viewlock= 0;
			rv3d.rflag &= ~View3dTypes.RV3D_CLIPPING;
		}

		for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= arn) {
			arn= ar.next;
			if(ar.alignment==ScreenTypes.RGN_ALIGN_QSPLIT) {
				ScreenEdit.ED_region_exit(C, ar);
				ScreenUtil.BKE_area_region_free((SpaceType)sa.type, ar);
				ListBaseUtil.BLI_remlink(sa.regionbase, ar);
//				MEM_freeN(ar);
			}
		}
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	}
	else if(ar.next!=null) {
//		BKE_report(op.reports, RPT_ERROR, "Only last region can be 4-splitted");
        }
	else {
		ScrArea sa= bContext.CTX_wm_area(C);
		ARegion newar;
		int count;

		ar.alignment= ScreenTypes.RGN_ALIGN_QSPLIT;

		for(count=0; count<3; count++) {
			newar= ScreenUtil.BKE_area_region_copy((SpaceType)sa.type, ar);
			ListBaseUtil.BLI_addtail(sa.regionbase, newar);
		}

		/* lock views and set them */
		if(sa.spacetype==SpaceTypes.SPACE_VIEW3D) {
			RegionView3D rv3d;

			rv3d= (RegionView3D)ar.regiondata;
			rv3d.viewlock= View3dTypes.RV3D_LOCKED; rv3d.view= View3dTypes.V3D_VIEW_FRONT; rv3d.persp= View3dTypes.V3D_ORTHO;

			ar= ar.next;
			rv3d= (RegionView3D)ar.regiondata;
			rv3d.viewlock= View3dTypes.RV3D_LOCKED; rv3d.view= View3dTypes.V3D_VIEW_TOP; rv3d.persp= View3dTypes.V3D_ORTHO;

			ar= ar.next;
			rv3d= (RegionView3D)ar.regiondata;
			rv3d.viewlock= View3dTypes.RV3D_LOCKED; rv3d.view= View3dTypes.V3D_VIEW_RIGHT; rv3d.persp= View3dTypes.V3D_ORTHO;

			ar= ar.next;
			rv3d= (RegionView3D)ar.regiondata;
			rv3d.view= View3dTypes.V3D_VIEW_CAMERA; rv3d.persp= View3dTypes.V3D_CAMOB;
		}

		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	}

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_region_quadview = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Toggle Quad View";
	ot.description= "Split selected area into camera, front, right & top views";
	ot.idname= "SCREEN_OT_region_quadview";

	/* api callbacks */
//	ot.invoke= WM_operator_confirm;
	ot.exec= region_quadview_exec;
//	ot.poll= ED_operator_areaactive;
	ot.poll= ED_operator_region_view3d_active;
//	ot.flag= WmTypes.OPTYPE_REGISTER;
	ot.flag= 0;
}};



/* ************** region flip operator ***************************** */

/* flip a region alignment */
public static wmOperatorType.Operator region_flip_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int region_flip_exec(bContext *C, wmOperator *op)
{
	ARegion ar= bContext.CTX_wm_region(C);

	if(ar.alignment==ScreenTypes.RGN_ALIGN_TOP)
		ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_BOTTOM)
		ar.alignment= ScreenTypes.RGN_ALIGN_TOP;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_LEFT)
		ar.alignment= ScreenTypes.RGN_ALIGN_RIGHT;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_RIGHT)
		ar.alignment= ScreenTypes.RGN_ALIGN_LEFT;

	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	System.out.printf("executed region flip\n");

	return WindowManagerTypes.OPERATOR_FINISHED;
}};


public static OpFunc SCREEN_OT_region_flip = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Flip Region";
	ot.idname= "SCREEN_OT_region_flip";

	/* api callbacks */
	ot.exec= region_flip_exec;

	ot.poll= ED_operator_areaactive;
	ot.flag= WmTypes.OPTYPE_REGISTER;
}};

/* ************** header flip operator ***************************** */

/* flip a header region alignment */
public static wmOperatorType.Operator header_flip_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	ARegion ar= bContext.CTX_wm_region(C);
	
	/* find the header region 
	 *	- try context first, but upon failing, search all regions in area...
	 */
	if((ar == null) || (ar.regiontype != ScreenTypes.RGN_TYPE_HEADER)) {
		ScrArea sa= bContext.CTX_wm_area(C);
		
		/* loop over all regions until a matching one is found */
		for (ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
			if(ar.regiontype == ScreenTypes.RGN_TYPE_HEADER)
				break;
		}
		
		/* don't do anything if no region */
		if(ar == null)
			return WindowManagerTypes.OPERATOR_CANCELLED;
	}	
	
	/* copied from SCREEN_OT_region_flip */
	if(ar.alignment==ScreenTypes.RGN_ALIGN_TOP)
		ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_BOTTOM)
		ar.alignment= ScreenTypes.RGN_ALIGN_TOP;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_LEFT)
		ar.alignment= ScreenTypes.RGN_ALIGN_RIGHT;
	else if(ar.alignment==ScreenTypes.RGN_ALIGN_RIGHT)
		ar.alignment= ScreenTypes.RGN_ALIGN_LEFT;

	Area.ED_area_tag_redraw(bContext.CTX_wm_area(C));

	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	
	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_header_flip = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Flip Header Region";
	ot.idname= "SCREEN_OT_header_flip";
	
	/* api callbacks */
	ot.exec= header_flip_exec;
	
	ot.poll= ED_operator_areaactive;
	ot.flag= 0;
}};

/* ************** header tools operator ***************************** */

public static wmOperatorType.Operator header_toolbox_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	System.out.println("header_toolbox_invoke");
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= bContext.CTX_wm_region(C);
	uiPopupMenu pup;
	uiLayout layout;
	
	pup= UIRegions.uiPupMenuBegin(C, "Header", UI.ICON_NULL);
	layout= UIRegions.uiPupMenuLayout(pup);
	
	// XXX SCREEN_OT_region_flip doesn't work - gets wrong context for active region, so added custom operator
	if (ar.alignment == ScreenTypes.RGN_ALIGN_TOP)
		UILayout.uiItemO(layout, "Flip to Bottom", UI.ICON_NULL, "SCREEN_OT_header_flip");
	else
		UILayout.uiItemO(layout, "Flip to Top", UI.ICON_NULL, "SCREEN_OT_header_flip");
	
	UILayout.uiItemS(layout);
	
	/* file browser should be fullscreen all the time, but other regions can be maximised/restored... */
	if (sa.spacetype != SpaceTypes.SPACE_FILE) {
		if (sa.full!=null) 
			UILayout.uiItemO(layout, "Tile Area", UI.ICON_NULL, "SCREEN_OT_screen_full_area");
		else
			UILayout.uiItemO(layout, "Maximize Area", UI.ICON_NULL, "SCREEN_OT_screen_full_area");
	}
	
	UIRegions.uiPupMenuEnd(C, pup);
	
	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

public static OpFunc SCREEN_OT_header_toolbox = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Header Toolbox";
	ot.description="Display header region toolbox";
	ot.idname= "SCREEN_OT_header_toolbox";
	
	/* api callbacks */
	ot.invoke= header_toolbox_invoke;
}};

///* ****************** anim player, with timer ***************** */
//
//static int match_region_with_redraws(int spacetype, int regiontype, int redraws)
//{
//	if(regiontype==RGN_TYPE_WINDOW) {
//
//		switch (spacetype) {
//			case SPACE_VIEW3D:
//				if(redraws & TIME_ALL_3D_WIN)
//					return 1;
//				break;
//			case SPACE_IPO:
//			case SPACE_ACTION:
//			case SPACE_NLA:
//				if(redraws & TIME_ALL_ANIM_WIN)
//					return 1;
//				break;
//			case SPACE_TIME:
//				/* if only 1 window or 3d windows, we do timeline too */
//				if(redraws & (TIME_ALL_ANIM_WIN|TIME_REGION|TIME_ALL_3D_WIN))
//					return 1;
//				break;
//			case SPACE_BUTS:
//				if(redraws & TIME_ALL_BUTS_WIN)
//					return 1;
//				break;
//			case SPACE_SEQ:
//				if(redraws & (TIME_SEQ|TIME_ALL_ANIM_WIN))
//					return 1;
//				break;
//			case SPACE_IMAGE:
//				if(redraws & TIME_ALL_IMAGE_WIN)
//					return 1;
//				break;
//
//		}
//	}
//	else if(regiontype==RGN_TYPE_UI) {
//		if(redraws & TIME_ALL_BUTS_WIN)
//			return 1;
//	}
//	else if(regiontype==RGN_TYPE_HEADER) {
//		if(spacetype==SPACE_TIME)
//			return 1;
//	}
//	return 0;
//}
//
//static int screen_animation_step(bContext *C, wmOperator *op, wmEvent *event)
//{
//	bScreen *screen= CTX_wm_screen(C);
//
//	if(screen.animtimer==event.customdata) {
//		Scene *scene= CTX_data_scene(C);
//		wmTimer *wt= screen.animtimer;
//		ScreenAnimData *sad= wt.customdata;
//		ScrArea *sa;
//
//		if(scene.audio.flag & AUDIO_SYNC) {
//			int step = floor(wt.duration * FPS);
//			if (sad.reverse) // XXX does this option work with audio?
//				scene.r.cfra -= step;
//			else
//				scene.r.cfra += step;
//			wt.duration -= ((float)step)/FPS;
//		}
//		else {
//			if (sad.reverse)
//				scene.r.cfra--;
//			else
//				scene.r.cfra++;
//		}
//
//		if (sad.reverse) {
//			/* jump back to end */
//			if (scene.r.psfra) {
//				if(scene.r.cfra < scene.r.psfra)
//					scene.r.cfra= scene.r.pefra;
//			}
//			else {
//				if(scene.r.cfra < scene.r.sfra)
//					scene.r.cfra= scene.r.efra;
//			}
//		}
//		else {
//			/* jump back to start */
//			if (scene.r.psfra) {
//				if(scene.r.cfra > scene.r.pefra)
//					scene.r.cfra= scene.r.psfra;
//			}
//			else {
//				if(scene.r.cfra > scene.r.efra)
//					scene.r.cfra= scene.r.sfra;
//			}
//		}
//
//		/* since we follow drawflags, we can't send notifier but tag regions ourselves */
//		ED_update_for_newframe(C, 1);
//
//		for(sa= screen.areabase.first; sa; sa= sa.next) {
//			ARegion *ar;
//			for(ar= sa.regionbase.first; ar; ar= ar.next) {
//				if(ar==sad.ar)
//					ED_region_tag_redraw(ar);
//				else
//					if(match_region_with_redraws(sa.spacetype, ar.regiontype, sad.redraws))
//						ED_region_tag_redraw(ar);
//			}
//		}
//
//		//WM_event_add_notifier(C, NC_SCENE|ND_FRAME, scene);
//
//		return OPERATOR_FINISHED;
//	}
//	return OPERATOR_PASS_THROUGH;
//}
//
//static void SCREEN_OT_animation_step(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Animation Step";
//	ot.idname= "SCREEN_OT_animation_step";
//
//	/* api callbacks */
//	ot.invoke= screen_animation_step;
//
//	ot.poll= ED_operator_screenactive;
//
//}

/* ****************** anim player, starts or ends timer ***************** */

/* toggle operator */
public static wmOperatorType.Operator screen_animation_play = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	bScreen screen= bContext.CTX_wm_screen(C);
//
//	if(screen.animtimer) {
//		ED_screen_animation_timer(C, 0, 0);
//	}
//	else {
//		int mode= (RNA_boolean_get(op.ptr, "reverse")) ? -1 : 1;
//
//		ED_screen_animation_timer(C, TIME_REGION|TIME_ALL_3D_WIN, mode);
//
//		if(screen.animtimer) {
//			wmTimer wt= screen.animtimer;
//			ScreenAnimData sad= wt.customdata;
//
//			sad.ar= CTX_wm_region(C);
//		}
//	}

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_animation_play = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Animation player";
	ot.idname= "SCREEN_OT_animation_play";

	/* api callbacks */
	ot.invoke= screen_animation_play;

	ot.poll= ED_operator_screenactive;

	RnaDefine.RNA_def_boolean(ot.srna, "reverse", 0, "Play in Reverse", "Animation is played backwards");
}};

///* ************** border select operator (template) ***************************** */
//
///* operator state vars used: (added by default WM callbacks)
//	xmin, ymin
//	xmax, ymax
//
//	customdata: the wmGesture pointer
//
//callbacks:
//
//	exec()	has to be filled in by user
//
//	invoke() default WM function
//			 adds modal handler
//
//	modal()	default WM function
//			accept modal events while doing it, calls exec(), handles ESC and border drawing
//
//	poll()	has to be filled in by user for context
//*/
//#if 0
//static int border_select_do(bContext *C, wmOperator *op)
//{
//	int event_type= RNA_int_get(op.ptr, "event_type");
//
//	if(event_type==LEFTMOUSE)
//		printf("border select do select\n");
//	else if(event_type==RIGHTMOUSE)
//		printf("border select deselect\n");
//	else
//		printf("border select do something\n");
//
//	return 1;
//}
//
//static void SCREEN_OT_border_select(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Border select";
//	ot.idname= "SCREEN_OT_border_select";
//
//	/* api callbacks */
//	ot.exec= border_select_do;
//	ot.invoke= WM_border_select_invoke;
//	ot.modal= WM_border_select_modal;
//
//	ot.poll= ED_operator_areaactive;
//
//	/* rna */
//	RNA_def_int(ot.srna, "event_type", 0, INT_MIN, INT_MAX, "Event Type", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "xmin", 0, INT_MIN, INT_MAX, "X Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "xmax", 0, INT_MIN, INT_MAX, "X Max", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymin", 0, INT_MIN, INT_MAX, "Y Min", "", INT_MIN, INT_MAX);
//	RNA_def_int(ot.srna, "ymax", 0, INT_MIN, INT_MAX, "Y Max", "", INT_MIN, INT_MAX);
//
//}
//#endif
//
///* ****************************** render invoking ***************** */
//
///* set callbacks, exported to sequence render too.
//Only call in foreground (UI) renders. */
//
///* returns biggest area that is not uv/image editor. Note that it uses buttons */
///* window as the last possible alternative.									   */
//static ScrArea *biggest_non_image_area(bContext *C)
//{
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa, *big= NULL;
//	int size, maxsize= 0, bwmaxsize= 0;
//	short foundwin= 0;
//
//	for(sa= sc.areabase.first; sa; sa= sa.next) {
//		if(sa.winx > 30 && sa.winy > 30) {
//			size= sa.winx*sa.winy;
//			if(sa.spacetype == SPACE_BUTS) {
//				if(foundwin == 0 && size > bwmaxsize) {
//					bwmaxsize= size;
//					big= sa;
//				}
//			}
//			else if(sa.spacetype != SPACE_IMAGE && size > maxsize) {
//				maxsize= size;
//				big= sa;
//				foundwin= 1;
//			}
//		}
//	}
//
//	return big;
//}
//
//static ScrArea *biggest_area(bContext *C)
//{
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa, *big= NULL;
//	int size, maxsize= 0;
//
//	for(sa= sc.areabase.first; sa; sa= sa.next) {
//		size= sa.winx*sa.winy;
//		if(size > maxsize) {
//			maxsize= size;
//			big= sa;
//		}
//	}
//	return big;
//}
//
//
//static ScrArea *find_area_showing_r_result(bContext *C)
//{
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa;
//	SpaceImage *sima;
//
//	/* find an imagewindow showing render result */
//	for(sa=sc.areabase.first; sa; sa= sa.next) {
//		if(sa.spacetype==SPACE_IMAGE) {
//			sima= sa.spacedata.first;
//			if(sima.image && sima.image.type==IMA_TYPE_R_RESULT)
//				break;
//		}
//	}
//	return sa;
//}
//
//static ScrArea *find_area_image_empty(bContext *C)
//{
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa;
//	SpaceImage *sima;
//
//	/* find an imagewindow showing render result */
//	for(sa=sc.areabase.first; sa; sa= sa.next) {
//		if(sa.spacetype==SPACE_IMAGE) {
//			sima= sa.spacedata.first;
//			if(!sima.image)
//				break;
//		}
//	}
//	return sa;
//}
//
//#if 0 // XXX not used
//static ScrArea *find_empty_image_area(bContext *C)
//{
//	bScreen *sc= CTX_wm_screen(C);
//	ScrArea *sa;
//	SpaceImage *sima;
//
//	/* find an imagewindow showing render result */
//	for(sa=sc.areabase.first; sa; sa= sa.next) {
//		if(sa.spacetype==SPACE_IMAGE) {
//			sima= sa.spacedata.first;
//			if(!sima.image)
//				break;
//		}
//	}
//	return sa;
//}
//#endif // XXX not used
//
///* new window uses x,y to set position */
//static void screen_set_image_output(bContext *C, int mx, int my)
//{
//	Scene *scene= CTX_data_scene(C);
//	ScrArea *sa;
//	SpaceImage *sima;
//
//	if(scene.r.displaymode==R_OUTPUT_WINDOW) {
//		rcti rect;
//		int sizex, sizey;
//
//		sizex= 10 + (scene.r.xsch*scene.r.size)/100;
//		sizey= 40 + (scene.r.ysch*scene.r.size)/100;
//
//		/* arbitrary... miniature image window views don't make much sense */
//		if(sizex < 320) sizex= 320;
//		if(sizey < 256) sizey= 256;
//
//		/* XXX some magic to calculate postition */
//		rect.xmin= mx + CTX_wm_window(C).posx - sizex/2;
//		rect.ymin= my + CTX_wm_window(C).posy - sizey/2;
//		rect.xmax= rect.xmin + sizex;
//		rect.ymax= rect.ymin + sizey;
//
//		/* changes context! */
//		WM_window_open_temp(C, &rect, WM_WINDOW_RENDER);
//
//		sa= CTX_wm_area(C);
//	}
//	else if(scene.r.displaymode==R_OUTPUT_SCREEN) {
//		/* this function returns with changed context */
//		ED_screen_full_newspace(C, CTX_wm_area(C), SPACE_IMAGE);
//		sa= CTX_wm_area(C);
//	}
//	else {
//
//		sa= find_area_showing_r_result(C);
//		if(sa==NULL)
//			sa= find_area_image_empty(C);
//
//		if(sa==NULL) {
//			/* find largest open non-image area */
//			sa= biggest_non_image_area(C);
//			if(sa) {
//				ED_area_newspace(C, sa, SPACE_IMAGE);
//				sima= sa.spacedata.first;
//
//				/* makes ESC go back to prev space */
//				sima.flag |= SI_PREVSPACE;
//			}
//			else {
//				/* use any area of decent size */
//				sa= biggest_area(C);
//				if(sa.spacetype!=SPACE_IMAGE) {
//					// XXX newspace(sa, SPACE_IMAGE);
//					sima= sa.spacedata.first;
//
//					/* makes ESC go back to prev space */
//					sima.flag |= SI_PREVSPACE;
//				}
//			}
//		}
//	}
//	sima= sa.spacedata.first;
//
//	/* get the correct image, and scale it */
//	sima.image= BKE_image_verify_viewer(IMA_TYPE_R_RESULT, "Render Result");
//
////	if(G.displaymode==2) { // XXX
//		if(sa.full) {
//			sima.flag |= SI_FULLWINDOW|SI_PREVSPACE;
//
////			ed_screen_fullarea(C, sa);
//		}
////	}
//
//}
//
///* executes blocking render */
//static int screen_render_exec(bContext *C, wmOperator *op)
//{
//	Scene *scene= CTX_data_scene(C);
//	Render *re= RE_GetRender(scene.id.name);
//
//	if(re==NULL) {
//		re= RE_NewRender(scene.id.name);
//	}
//	RE_test_break_cb(re, NULL, (int (*)(void *)) blender_test_break);
//
//	if(RNA_boolean_get(op.ptr, "animation"))
//		RE_BlenderAnim(re, scene, scene.r.sfra, scene.r.efra, scene.frame_step);
//	else
//		RE_BlenderFrame(re, scene, scene.r.cfra);
//
//	// no redraw needed, we leave state as we entered it
//	ED_update_for_newframe(C, 1);
//
//	WM_event_add_notifier(C, NC_SCENE|ND_RENDER_RESULT, scene);
//
//	return OPERATOR_FINISHED;
//}
//
//typedef struct RenderJob {
//	Scene *scene;
//	Render *re;
//	wmWindow *win;
//	int anim;
//	Image *image;
//	ImageUser iuser;
//	short *stop;
//	short *do_update;
//} RenderJob;
//
//static void render_freejob(void *rjv)
//{
//	RenderJob *rj= rjv;
//
//	MEM_freeN(rj);
//}
//
///* str is IMA_RW_MAXTEXT in size */
//static void make_renderinfo_string(RenderStats *rs, Scene *scene, char *str)
//{
//	char info_time_str[32];	// used to be extern to header_info.c
//	uintptr_t mem_in_use, mmap_in_use;
//	float megs_used_memory, mmap_used_memory;
//	char *spos= str;
//
//	mem_in_use= MEM_get_memory_in_use();
//	mmap_in_use= MEM_get_mapped_memory_in_use();
//
//	megs_used_memory= (mem_in_use-mmap_in_use)/(1024.0*1024.0);
//	mmap_used_memory= (mmap_in_use)/(1024.0*1024.0);
//
//	if(scene.lay & 0xFF000000)
//		spos+= sprintf(spos, "Localview | ");
//	else if(scene.r.scemode & R_SINGLE_LAYER)
//		spos+= sprintf(spos, "Single Layer | ");
//
//	if(rs.statstr) {
//		spos+= sprintf(spos, "%s ", rs.statstr);
//	}
//	else {
//		spos+= sprintf(spos, "Fra:%d  Ve:%d Fa:%d ", (scene.r.cfra), rs.totvert, rs.totface);
//		if(rs.tothalo) spos+= sprintf(spos, "Ha:%d ", rs.tothalo);
//		if(rs.totstrand) spos+= sprintf(spos, "St:%d ", rs.totstrand);
//		spos+= sprintf(spos, "La:%d Mem:%.2fM (%.2fM) ", rs.totlamp, megs_used_memory, mmap_used_memory);
//
//		if(rs.curfield)
//			spos+= sprintf(spos, "Field %d ", rs.curfield);
//		if(rs.curblur)
//			spos+= sprintf(spos, "Blur %d ", rs.curblur);
//	}
//
//	BLI_timestr(rs.lastframetime, info_time_str);
//	spos+= sprintf(spos, "Time:%s ", info_time_str);
//
//	if(rs.infostr && rs.infostr[0])
//		spos+= sprintf(spos, "| %s ", rs.infostr);
//
//	/* very weak... but 512 characters is quite safe */
//	if(spos >= str+IMA_RW_MAXTEXT)
//		printf("WARNING! renderwin text beyond limit \n");
//
//}
//
//static void image_renderinfo_cb(void *rjv, RenderStats *rs)
//{
//	RenderJob *rj= rjv;
//
//	/* malloc OK here, stats_draw is not in tile threads */
//	if(rj.image.render_text==NULL)
//		rj.image.render_text= MEM_callocN(IMA_RW_MAXTEXT, "rendertext");
//
//	make_renderinfo_string(rs, rj.scene, rj.image.render_text);
//
//	/* make jobs timer to send notifier */
//	*(rj.do_update)= 1;
//
//}
//
///* called inside thread! */
//static void image_rect_update(void *rjv, RenderResult *rr, volatile rcti *renrect)
//{
//	RenderJob *rj= rjv;
//	ImBuf *ibuf;
//	float x1, y1, *rectf= NULL;
//	int ymin, ymax, xmin, xmax;
//	int rymin, rxmin;
//	char *rectc;
//
//	ibuf= BKE_image_get_ibuf(rj.image, &rj.iuser);
//	if(ibuf==NULL) return;
//
//	/* if renrect argument, we only refresh scanlines */
//	if(renrect) {
//		/* if ymax==recty, rendering of layer is ready, we should not draw, other things happen... */
//		if(rr.renlay==NULL || renrect.ymax>=rr.recty)
//			return;
//
//		/* xmin here is first subrect x coord, xmax defines subrect width */
//		xmin = renrect.xmin + rr.crop;
//		xmax = renrect.xmax - xmin - rr.crop;
//		if (xmax<2) return;
//
//		ymin= renrect.ymin + rr.crop;
//		ymax= renrect.ymax - ymin - rr.crop;
//		if(ymax<2)
//			return;
//		renrect.ymin= renrect.ymax;
//
//	}
//	else {
//		xmin = ymin = rr.crop;
//		xmax = rr.rectx - 2*rr.crop;
//		ymax = rr.recty - 2*rr.crop;
//	}
//
//	/* xmin ymin is in tile coords. transform to ibuf */
//	rxmin= rr.tilerect.xmin + xmin;
//	if(rxmin >= ibuf.x) return;
//	rymin= rr.tilerect.ymin + ymin;
//	if(rymin >= ibuf.y) return;
//
//	if(rxmin + xmax > ibuf.x)
//		xmax= ibuf.x - rxmin;
//	if(rymin + ymax > ibuf.y)
//		ymax= ibuf.y - rymin;
//
//	if(xmax < 1 || ymax < 1) return;
//
//	/* find current float rect for display, first case is after composit... still weak */
//	if(rr.rectf)
//		rectf= rr.rectf;
//	else {
//		if(rr.rect32)
//			return;
//		else {
//			if(rr.renlay==NULL || rr.renlay.rectf==NULL) return;
//			rectf= rr.renlay.rectf;
//		}
//	}
//	if(rectf==NULL) return;
//
//	rectf+= 4*(rr.rectx*ymin + xmin);
//	rectc= (char *)(ibuf.rect + ibuf.x*rymin + rxmin);
//
//	/* XXX make nice consistent functions for this */
//	if (rj.scene.r.color_mgt_flag & R_COLOR_MANAGEMENT) {
//		for(y1= 0; y1<ymax; y1++) {
//			float *rf= rectf;
//			float srgb[3];
//			char *rc= rectc;
//
//			/* XXX temp. because crop offset */
//			if( rectc >= (char *)(ibuf.rect)) {
//				for(x1= 0; x1<xmax; x1++, rf += 4, rc+=4) {
//					srgb[0]= linearrgb_to_srgb(rf[0]);
//					srgb[1]= linearrgb_to_srgb(rf[1]);
//					srgb[2]= linearrgb_to_srgb(rf[2]);
//
//					rc[0]= FTOCHAR(srgb[0]);
//					rc[1]= FTOCHAR(srgb[1]);
//					rc[2]= FTOCHAR(srgb[2]);
//					rc[3]= FTOCHAR(rf[3]);
//				}
//			}
//			rectf += 4*rr.rectx;
//			rectc += 4*ibuf.x;
//		}
//	} else {
//		for(y1= 0; y1<ymax; y1++) {
//			float *rf= rectf;
//			char *rc= rectc;
//
//			/* XXX temp. because crop offset */
//			if( rectc >= (char *)(ibuf.rect)) {
//				for(x1= 0; x1<xmax; x1++, rf += 4, rc+=4) {
//					rc[0]= FTOCHAR(rf[0]);
//					rc[1]= FTOCHAR(rf[1]);
//					rc[2]= FTOCHAR(rf[2]);
//					rc[3]= FTOCHAR(rf[3]);
//				}
//			}
//			rectf += 4*rr.rectx;
//			rectc += 4*ibuf.x;
//		}
//	}
//
//	/* make jobs timer to send notifier */
//	*(rj.do_update)= 1;
//}
//
//static void render_startjob(void *rjv, short *stop, short *do_update)
//{
//	RenderJob *rj= rjv;
//
//	rj.stop= stop;
//	rj.do_update= do_update;
//
//	if(rj.anim)
//		RE_BlenderAnim(rj.re, rj.scene, rj.scene.r.sfra, rj.scene.r.efra, rj.scene.frame_step);
//	else
//		RE_BlenderFrame(rj.re, rj.scene, rj.scene.r.cfra);
//}
//
///* called by render, check job 'stop' value or the global */
//static int render_breakjob(void *rjv)
//{
//	RenderJob *rj= rjv;
//
//	if(G.afbreek)
//		return 1;
//	if(rj.stop && *(rj.stop))
//		return 1;
//	return 0;
//}
//
///* catch esc */
//static int screen_render_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	/* no running blender, remove handler and pass through */
//	if(0==WM_jobs_test(CTX_wm_manager(C), CTX_data_scene(C)))
//	   return OPERATOR_FINISHED|OPERATOR_PASS_THROUGH;
//
//	/* running render */
//	switch (event.type) {
//		case ESCKEY:
//			return OPERATOR_RUNNING_MODAL;
//			break;
//	}
//	return OPERATOR_PASS_THROUGH;
//}
//
///* using context, starts job */
//static int screen_render_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	/* new render clears all callbacks */
//	Scene *scene= CTX_data_scene(C);
//	Render *re;
//	wmJob *steve;
//	RenderJob *rj;
//	Image *ima;
//
//	/* only one job at a time */
//	if(WM_jobs_test(CTX_wm_manager(C), scene))
//		return OPERATOR_CANCELLED;
//
//	/* handle UI stuff */
//	WM_cursor_wait(1);
//
//	/* flush multires changes (for sculpt) */
//	multires_force_update(CTX_data_active_object(C));
//
//	/* get editmode results */
//	ED_object_exit_editmode(C, 0);	/* 0 = does not exit editmode */
//
//	// store spare
//	// get view3d layer, local layer, make this nice api call to render
//	// store spare
//
//	/* ensure at least 1 area shows result */
//	screen_set_image_output(C, event.x, event.y);
//
//	/* job custom data */
//	rj= MEM_callocN(sizeof(RenderJob), "render job");
//	rj.scene= scene;
//	rj.win= CTX_wm_window(C);
//	rj.anim= RNA_boolean_get(op.ptr, "animation");
//	rj.iuser.scene= scene;
//	rj.iuser.ok= 1;
//
//	/* setup job */
//	steve= WM_jobs_get(CTX_wm_manager(C), CTX_wm_window(C), scene);
//	WM_jobs_customdata(steve, rj, render_freejob);
//	WM_jobs_timer(steve, 0.2, NC_SCENE|ND_RENDER_RESULT, 0);
//	WM_jobs_callbacks(steve, render_startjob, NULL, NULL);
//
//	/* get a render result image, and make sure it is empty */
//	ima= BKE_image_verify_viewer(IMA_TYPE_R_RESULT, "Render Result");
//	BKE_image_signal(ima, NULL, IMA_SIGNAL_FREE);
//	rj.image= ima;
//
//	/* setup new render */
//	re= RE_NewRender(scene.id.name);
//	RE_test_break_cb(re, rj, render_breakjob);
//	RE_display_draw_cb(re, rj, image_rect_update);
//	RE_stats_draw_cb(re, rj, image_renderinfo_cb);
//
//	rj.re= re;
//	G.afbreek= 0;
//
//	//	BKE_report in render!
//	//	RE_error_cb(re, error_cb);
//
//	WM_jobs_start(CTX_wm_manager(C), steve);
//
//	G.afbreek= 0;
//
//	WM_cursor_wait(0);
//	WM_event_add_notifier(C, NC_SCENE|ND_RENDER_RESULT, scene);
//
//	/* add modal handler for ESC */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//
///* contextual render, using current scene, view3d? */
//static void SCREEN_OT_render(wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Render";
//	ot.idname= "SCREEN_OT_render";
//
//	/* api callbacks */
//	ot.invoke= screen_render_invoke;
//	ot.modal= screen_render_modal;
//	ot.exec= screen_render_exec;
//
//	ot.poll= ED_operator_screenactive;
//
//	RNA_def_int(ot.srna, "layers", 0, 0, INT_MAX, "Layers", "", 0, INT_MAX);
//	RNA_def_boolean(ot.srna, "animation", 0, "Animation", "");
//}
//
///* *********************** cancel render viewer *************** */
//
//static int render_view_cancel_exec(bContext *C, wmOperator *unused)
//{
//	ScrArea *sa= CTX_wm_area(C);
//	SpaceImage *sima= sa.spacedata.first;
//
//	/* test if we have a temp screen in front */
//	if(CTX_wm_window(C).screen.full==SCREENTEMP) {
//		wm_window_lower(CTX_wm_window(C));
//	}
//	/* determine if render already shows */
//	else if(sima.flag & SI_PREVSPACE) {
//		sima.flag &= ~SI_PREVSPACE;
//
//		if(sima.flag & SI_FULLWINDOW) {
//			sima.flag &= ~SI_FULLWINDOW;
//			ED_screen_full_prevspace(C);
//		}
//		else
//			ED_area_prevspace(C);
//	}
//	else if(sima.flag & SI_FULLWINDOW) {
//		sima.flag &= ~SI_FULLWINDOW;
//		ed_screen_fullarea(C, sa);
//	}
//
//	return OPERATOR_FINISHED;
//}
//
//static void SCREEN_OT_render_view_cancel(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Cancel Render View";
//	ot.idname= "SCREEN_OT_render_view_cancel";
//
//	/* api callbacks */
//	ot.exec= render_view_cancel_exec;
//	ot.poll= ED_operator_image_active;
//}
//
///* *********************** show render viewer *************** */
//
//static int render_view_show_invoke(bContext *C, wmOperator *unused, wmEvent *event)
//{
//	ScrArea *sa= find_area_showing_r_result(C);
//
//	/* test if we have a temp screen in front */
//	if(CTX_wm_window(C).screen.full==SCREENTEMP) {
//		wm_window_lower(CTX_wm_window(C));
//	}
//	/* determine if render already shows */
//	else if(sa) {
//		SpaceImage *sima= sa.spacedata.first;
//
//		if(sima.flag & SI_PREVSPACE) {
//			sima.flag &= ~SI_PREVSPACE;
//
//			if(sima.flag & SI_FULLWINDOW) {
//				sima.flag &= ~SI_FULLWINDOW;
//				ED_screen_full_prevspace(C);
//			}
//			else if(sima.next) {
//				ED_area_newspace(C, sa, sima.next.spacetype);
//				ED_area_tag_redraw(sa);
//			}
//		}
//	}
//	else {
//		screen_set_image_output(C, event.x, event.y);
//	}
//
//	return OPERATOR_FINISHED;
//}
//
//static void SCREEN_OT_render_view_show(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name= "Show/Hide Render View";
//	ot.idname= "SCREEN_OT_render_view_show";
//
//	/* api callbacks */
//	ot.invoke= render_view_show_invoke;
//	ot.poll= ED_operator_screenactive;
//}

/* *********** show user pref window ****** */

public static wmOperatorType.Operator userpref_show_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator unused, wmEvent event)
{
//	ScrArea *sa;
//	rcti rect;
//	int sizex, sizey;
//
//	sizex= 800;
//	sizey= 480;
//
//	/* some magic to calculate postition */
//	rect.xmin= event.x + CTX_wm_window(C).posx - sizex/2;
//	rect.ymin= event.y + CTX_wm_window(C).posy - sizey/2;
//	rect.xmax= rect.xmin + sizex;
//	rect.ymax= rect.ymin + sizey;
//
//	/* changes context! */
//	WM_window_open_temp(C, &rect, WM_WINDOW_USERPREFS);
//
//	sa= CTX_wm_area(C);


	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc SCREEN_OT_userpref_show = new OpFunc() {
public void run(wmOperatorType ot)
{
	/* identifiers */
	ot.name= "Show/Hide User Preferences";
	ot.idname= "SCREEN_OT_userpref_show";

	/* api callbacks */
	ot.invoke= userpref_show_invoke;
	ot.poll= ED_operator_screenactive;
}};



/* ****************  Assigning operatortypes to global list, adding handlers **************** */

/* called in spacetypes.c */
public static void ED_operatortypes_screen()
{
	/* generic UI stuff */
	WmOperators.WM_operatortype_append(SCREEN_OT_actionzone);
//	WmOperators.WM_operatortype_append(SCREEN_OT_repeat_last);
//	WmOperators.WM_operatortype_append(SCREEN_OT_repeat_history);
//	WmOperators.WM_operatortype_append(SCREEN_OT_redo_last);

	/* screen tools */
	WmOperators.WM_operatortype_append(SCREEN_OT_area_move);
	WmOperators.WM_operatortype_append(SCREEN_OT_area_split);
	WmOperators.WM_operatortype_append(SCREEN_OT_area_join);
	WmOperators.WM_operatortype_append(SCREEN_OT_area_dupli);
	WmOperators.WM_operatortype_append(SCREEN_OT_area_swap);
	WmOperators.WM_operatortype_append(SCREEN_OT_region_quadview);
	WmOperators.WM_operatortype_append(SCREEN_OT_region_scale);
	WmOperators.WM_operatortype_append(SCREEN_OT_region_flip);
	WmOperators.WM_operatortype_append(SCREEN_OT_header_flip);
	WmOperators.WM_operatortype_append(SCREEN_OT_header_toolbox);
	WmOperators.WM_operatortype_append(SCREEN_OT_screen_set);
//	WmOperators.WM_operatortype_append(SCREEN_OT_screen_full_area);
//	WmOperators.WM_operatortype_append(SCREEN_OT_back_to_previous);
//	WmOperators.WM_operatortype_append(SCREEN_OT_screenshot);
//	WmOperators.WM_operatortype_append(SCREEN_OT_screencast);
	WmOperators.WM_operatortype_append(SCREEN_OT_userpref_show);

	/*frame changes*/
//	WmOperators.WM_operatortype_append(SCREEN_OT_frame_offset);
	WmOperators.WM_operatortype_append(SCREEN_OT_frame_jump);
	WmOperators.WM_operatortype_append(SCREEN_OT_keyframe_jump);

//	WmOperators.WM_operatortype_append(SCREEN_OT_animation_step);
	WmOperators.WM_operatortype_append(SCREEN_OT_animation_play);
//	WM_operatortype_append(SCREEN_OT_animation_cancel);
	
//	/* new/delete */
//	WM_operatortype_append(SCREEN_OT_new);
//	WM_operatortype_append(SCREEN_OT_delete);
//	WM_operatortype_append(SCENE_OT_new);
//	WM_operatortype_append(SCENE_OT_delete);

//	/* render */
//	WmOperators.WM_operatortype_append(SCREEN_OT_render);
//	WmOperators.WM_operatortype_append(SCREEN_OT_render_view_cancel);
//	WmOperators.WM_operatortype_append(SCREEN_OT_render_view_show);
//
//	/* tools shared by more space types */
//	WmOperators.WM_operatortype_append(ED_OT_undo);
//	WmOperators.WM_operatortype_append(ED_OT_redo);

}

static EnumPropertyItem[] modal_items = {
        new EnumPropertyItem(KM_MODAL_CANCEL, "CANCEL", 0, "Cancel", ""),
        new EnumPropertyItem(KM_MODAL_APPLY, "APPLY", 0, "Apply", ""),
        new EnumPropertyItem(KM_MODAL_STEP10, "STEP10", 0, "Steps on", ""),
        new EnumPropertyItem(KM_MODAL_STEP10_OFF, "STEP10_OFF", 0, "Steps off", ""),
        new EnumPropertyItem(0, null, 0, null, null)
};

//static void keymap_modal_set(wmWindowManager wm)
static void keymap_modal_set(wmKeyConfig keyconf)
{
//	static EnumPropertyItem modal_items[] = {
//		{KM_MODAL_CANCEL, "CANCEL", 0, "Cancel", ""},
//		{KM_MODAL_APPLY, "APPLY", 0, "Apply", ""},
//		{KM_MODAL_STEP10, "STEP10", 0, "Steps on", ""},
//		{KM_MODAL_STEP10_OFF, "STEP10_OFF", 0, "Steps off", ""},
//		{0, NULL, 0, NULL, NULL}};
	wmKeyMap keymap;

	/* Standard Modal keymap ------------------------------------------------ */
	keymap= WmKeymap.WM_modalkeymap_add(keyconf, "Standard Modal Map", modal_items);

	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.ESCKEY,    WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, KM_MODAL_CANCEL);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTMOUSE, WmTypes.KM_ANY, WmTypes.KM_ANY, 0, KM_MODAL_APPLY);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.RETKEY, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, KM_MODAL_APPLY);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.PADENTER, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, KM_MODAL_APPLY);

	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTCTRLKEY, WmTypes.KM_PRESS, WmTypes.KM_ANY, 0, KM_MODAL_STEP10);
	WmKeymap.WM_modalkeymap_add_item(keymap, WmEventTypes.LEFTCTRLKEY, WmTypes.KM_RELEASE, WmTypes.KM_ANY, 0, KM_MODAL_STEP10_OFF);

	WmKeymap.WM_modalkeymap_assign(keymap, "SCREEN_OT_area_move");

}

/* called in spacetypes.c */
//public static void ED_keymap_screen(wmWindowManager wm)
public static void ED_keymap_screen(wmKeyConfig keyconf)
{
	wmKeyMap keymap;

	/* Screen Editing ------------------------------------------------ */
	keymap= WmKeymap.WM_keymap_find(keyconf, "Screen Editing", 0, 0);

	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_actionzone", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, 0, 0).ptr, "modifier", 0);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_actionzone", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "modifier", 1);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_actionzone", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, WmTypes.KM_ALT, 0).ptr, "modifier", 2);

	/* screen tools */
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_area_split", WmEventTypes.EVT_ACTIONZONE_AREA, 0, 0, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_area_join", WmEventTypes.EVT_ACTIONZONE_AREA, 0, 0, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_area_dupli", WmEventTypes.EVT_ACTIONZONE_AREA, 0, WmTypes.KM_SHIFT, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_area_swap", WmEventTypes.EVT_ACTIONZONE_AREA, 0, WmTypes.KM_ALT, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_region_scale", WmEventTypes.EVT_ACTIONZONE_REGION, 0, 0, 0);
	/* area move after action zones */
	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_area_move", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, 0, 0);

	/* Header Editing ------------------------------------------------ */
	keymap= WmKeymap.WM_keymap_find(keyconf, "Header", 0, 0);
	
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_header_toolbox", WmEventTypes.RIGHTMOUSE, WmTypes.KM_PRESS, 0, 0);
	
	/* Screen General ------------------------------------------------ */
	keymap= WmKeymap.WM_keymap_find(keyconf, "Screen", 0, 0);
	
	/* standard timers */
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_animation_step", WmEventTypes.TIMER0, WmTypes.KM_ANY, WmTypes.KM_ANY, 0);
	
	
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screen_set", WmEventTypes.RIGHTARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "delta", 1);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screen_set", WmEventTypes.LEFTARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "delta", -1);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screen_full_area", WmEventTypes.UPARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screen_full_area", WmEventTypes.DOWNARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screenshot", WmEventTypes.F3KEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_screencast", WmEventTypes.F3KEY, WmTypes.KM_PRESS, WmTypes.KM_ALT, 0);

	 /* tests */
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_region_split", WmEventTypes.SKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL|WmTypes.KM_ALT, 0);
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_region_quadview", WmEventTypes.QKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL|WmTypes.KM_ALT, 0);
//	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_repeat_history", WmEventTypes.F3KEY, WmTypes.KM_PRESS, 0, 0);
//	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_repeat_last", WmEventTypes.F4KEY, WmTypes.KM_PRESS, 0, 0);
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_region_flip", WmEventTypes.F5KEY, WmTypes.KM_PRESS, 0, 0);
//	WmKeymap.WM_keymap_verify_item(keymap, "SCREEN_OT_redo_last", WmEventTypes.F6KEY, WmTypes.KM_PRESS, 0, 0);
//	RnaAccess.RNA_string_set(WmKeymap.WM_keymap_add_item(keymap, "SCRIPT_OT_python_file_run", WmEventTypes.F7KEY, WmTypes.KM_PRESS, 0, 0).ptr, "filename", "test.py");
//	WmKeymap.WM_keymap_verify_item(keymap, "SCRIPT_OT_python_run_ui_scripts", WmEventTypes.F8KEY, WmTypes.KM_PRESS, 0, 0);

	/* files */
//	WmKeymap.WM_keymap_add_item(keymap, "FILE_OT_exec", WmEventTypes.RETKEY, WmTypes.KM_PRESS, 0, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "FILE_OT_cancel", WmEventTypes.ESCKEY, WmTypes.KM_PRESS, 0, 0);

	/* undo */
//	WmKeymap.WM_keymap_add_item(keymap, "ED_OT_undo", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "ED_OT_undo", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_OSKEY, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "ED_OT_redo", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT|WmTypes.KM_CTRL, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "ED_OT_redo", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT|WmTypes.KM_OSKEY, 0);

	/* render */
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_render", WmEventTypes.F12KEY, WmTypes.KM_PRESS, 0, 0);
//	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_render", WmEventTypes.F12KEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "animation", true);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_render_view_cancel", WmEventTypes.ESCKEY, WmTypes.KM_PRESS, 0, 0);
//	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_render_view_show", WmEventTypes.F11KEY, WmTypes.KM_PRESS, 0, 0);

	/* user prefs */
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_userpref_show", WmEventTypes.UKEY, WmTypes.KM_PRESS, WmTypes.KM_ALT, 0);

	/* Anim Playback ------------------------------------------------ */
	keymap= WmKeymap.WM_keymap_find(keyconf, "Frames", 0, 0);

	/* frame offsets */
//	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WmEventTypes.UPARROWKEY, WmTypes.KM_PRESS, 0, 0).ptr, "delta", 10);
//	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WmEventTypes.DOWNARROWKEY, WmTypes.KM_PRESS, 0, 0).ptr, "delta", -10);
//	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WmEventTypes.LEFTARROWKEY, WmTypes.KM_PRESS, 0, 0).ptr, "delta", -1);
//	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WmEventTypes.RIGHTARROWKEY, WmTypes.KM_PRESS, 0, 0).ptr, "delta", 1);

//	RNA_int_set(WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WHEELDOWNMOUSE, KM_PRESS, KM_ALT, 0)->ptr, "delta", 1);
//	RNA_int_set(WM_keymap_add_item(keymap, "SCREEN_OT_frame_offset", WHEELUPMOUSE, KM_PRESS, KM_ALT, 0)->ptr, "delta", -1);
	
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_jump", WmEventTypes.UPARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "end", true);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_jump", WmEventTypes.DOWNARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "end", false);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_jump", WmEventTypes.RIGHTARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "end", true);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_frame_jump", WmEventTypes.LEFTARROWKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "end", false);
	
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_keyframe_jump", WmEventTypes.PAGEUPKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_keyframe_jump", WmEventTypes.PAGEDOWNKEY, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "next", true);

	/* play (forward and backwards) */
	WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_animation_play", WmEventTypes.AKEY, WmTypes.KM_PRESS, WmTypes.KM_ALT, 0);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "SCREEN_OT_animation_play", WmEventTypes.AKEY, WmTypes.KM_PRESS, WmTypes.KM_ALT|WmTypes.KM_SHIFT, 0).ptr, "reverse", true);

	keymap_modal_set(keyconf);
}

}

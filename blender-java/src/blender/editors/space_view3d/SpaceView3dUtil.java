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

import javax.media.opengl.GL2;

import blender.blenkernel.Blender;
import blender.blenkernel.ObjectUtil;
import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenkernel.bContext.bContextDataCallback;
import blender.blenkernel.bContext.bContextDataResult;
import blender.blenlib.ListBaseUtil;
import blender.editors.screen.Area;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceView3dUtil {

/* ******************** manage regions ********************* */

public static ARegion view3d_has_buttons_region(ScrArea sa)
{
	ARegion ar, arnew;

	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
		if(ar.regiontype==ScreenTypes.RGN_TYPE_UI)
			return ar;

	/* add subdiv level; after header */
	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next)
		if(ar.regiontype==ScreenTypes.RGN_TYPE_HEADER)
			break;

	/* is error! */
	if(ar==null) return null;

	arnew= new ARegion();

	ListBaseUtil.BLI_insertlinkafter(sa.regionbase, ar, arnew);
	arnew.regiontype= ScreenTypes.RGN_TYPE_UI;
	arnew.alignment= ScreenTypes.RGN_ALIGN_RIGHT;

	arnew.flag = ScreenTypes.RGN_FLAG_HIDDEN;

	return arnew;
}

public static ARegion view3d_has_tools_region(ScrArea sa)
{
	ARegion ar, artool=null, arprops=null, arhead;

	for(ar= (ARegion)sa.regionbase.first; ar!=null; ar= ar.next) {
		if(ar.regiontype==ScreenTypes.RGN_TYPE_TOOLS)
			artool= ar;
		if(ar.regiontype==ScreenTypes.RGN_TYPE_TOOL_PROPS)
			arprops= ar;
	}

	/* tool region hide/unhide also hides props */
	if(arprops!=null && artool!=null) return artool;

	if(artool==null) {
		/* add subdiv level; after header */
		for(arhead= (ARegion)sa.regionbase.first; arhead!=null; arhead= arhead.next)
			if(arhead.regiontype==ScreenTypes.RGN_TYPE_HEADER)
				break;

		/* is error! */
		if(arhead==null) return null;

		artool= new ARegion();

		ListBaseUtil.BLI_insertlinkafter(sa.regionbase, arhead, artool);
		artool.regiontype= ScreenTypes.RGN_TYPE_TOOLS;
		artool.alignment= ScreenTypes.RGN_OVERLAP_LEFT;
		artool.flag = ScreenTypes.RGN_FLAG_HIDDEN;
	}

	if(arprops==null) {
		/* add extra subdivided region for tool properties */
		arprops= new ARegion();

		ListBaseUtil.BLI_insertlinkafter(sa.regionbase, artool, arprops);
		arprops.regiontype= ScreenTypes.RGN_TYPE_TOOL_PROPS;
		arprops.alignment= ScreenTypes.RGN_ALIGN_BOTTOM|ScreenTypes.RGN_SPLIT_PREV;
	}

	return artool;
}

///* ****************************************************** */
//
///* function to always find a regionview3d context inside 3D window */
//RegionView3D *ED_view3d_context_rv3d(bContext *C)
//{
//	RegionView3D *rv3d= CTX_wm_region_view3d(C);
//
//	if(rv3d==NULL) {
//		ScrArea *sa =CTX_wm_area(C);
//		if(sa.spacetype==SPACE_VIEW3D) {
//			ARegion *ar;
//			for(ar= sa.regionbase.first; ar; ar= ar.next)
//				if(ar.regiontype==RGN_TYPE_WINDOW)
//					return ar.regiondata;
//		}
//	}
//	return rv3d;
//}


/* ******************** default callbacks for view3d space ***************** */

public static SpaceType.New view3d_new = new SpaceType.New() {
public SpaceLink run(bContext C)
//static SpaceLink *view3d_new(const bContext *C)
{
	Scene scene= bContext.CTX_data_scene(C);
	ARegion ar;
	View3D v3d;
	RegionView3D rv3d;

	v3d= new View3D();
	v3d.spacetype= SpaceTypes.SPACE_VIEW3D;
	v3d.blockscale= 0.7f;
	v3d.lay= v3d.layact= 1;
	if(scene!=null) {
		v3d.lay= v3d.layact= scene.lay;
		v3d.camera= scene.camera;
	}
	v3d.scenelock= 1;
	v3d.grid= 1.0f;
	v3d.gridlines= 16;
	v3d.gridsubdiv = 10;
	v3d.drawtype= ObjectTypes.OB_WIRE;

	v3d.gridflag |= View3dTypes.V3D_SHOW_X;
	v3d.gridflag |= View3dTypes.V3D_SHOW_Y;
	v3d.gridflag |= View3dTypes.V3D_SHOW_FLOOR;
	v3d.gridflag &= ~View3dTypes.V3D_SHOW_Z;

	v3d.lens= 35.0f;
	v3d.near= 0.01f;
	v3d.far= 500.0f;

	/* header */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(v3d.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
	ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;

	/* buttons/list view */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(v3d.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_UI;
	ar.alignment= ScreenTypes.RGN_ALIGN_LEFT;
	ar.flag = ScreenTypes.RGN_FLAG_HIDDEN;

	/* main area */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(v3d.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;

	ar.regiondata= new RegionView3D();
	rv3d= (RegionView3D)ar.regiondata;
	rv3d.viewquat[0]= 1.0f;
	rv3d.persp= 1;
	rv3d.view= 7;
	rv3d.dist= 10.0f;

	return (SpaceLink)v3d;
}};

///* not spacelink itself */
public static SpaceType.Free view3d_free = new SpaceType.Free() {
public void run(SpaceLink sl)
//static void view3d_free(SpaceLink *sl)
{
	View3D vd= (View3D) sl;

	if(vd.bgpic!=null) {
		if(vd.bgpic.ima!=null)
                    vd.bgpic.ima.id.us--;
//		MEM_freeN(vd.bgpic);
	}

//	if(vd.localvd)
//            MEM_freeN(vd.localvd);

//	if(vd.properties_storage)
//            MEM_freeN(vd.properties_storage);

}};


///* spacetype; init callback */
public static SpaceType.Init view3d_init = new SpaceType.Init() {
public void run(wmWindowManager wm, ScrArea sa)
//static void view3d_init(struct wmWindowManager *wm, ScrArea *sa)
{

}};

public static SpaceType.Duplicate view3d_duplicate = new SpaceType.Duplicate() {
public SpaceLink run(SpaceLink sl)
//static SpaceLink *view3d_duplicate(SpaceLink *sl)
{
	View3D v3do= (View3D)sl;
        View3D v3dn= v3do.copy();

	/* clear or remove stuff from old */

// XXX	BIF_view3d_previewrender_free(v3do);

	if(v3do.localvd!=null) {
		v3do.localvd= null;
		v3do.properties_storage= null;
//		v3do.localview= 0;
		v3do.lay= v3dn.localvd.lay;
		v3do.lay &= 0xFFFFFF;
	}

	/* copy or clear inside new stuff */

	if(v3dn.bgpic!=null) {
//		v3dn.bgpic= MEM_dupallocN(v3dn.bgpic);
//                try {
//                    v3dn.bgpic= (BGpic)v3dn.bgpic.clone();
//                } catch(Exception ex) { }
                v3dn.bgpic= v3dn.bgpic.copy();
		if(v3dn.bgpic.ima!=null)
                    v3dn.bgpic.ima.id.us++;
	}
	v3dn.properties_storage= null;

	return (SpaceLink)v3dn;
}};

static void view3d_modal_keymaps(wmWindowManager wm, ARegion ar, int stype)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	wmKeyMap keymap;

	/* copy last mode, then we can re-init the region maps */
//	rv3d.lastmode= stype;

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Object Mode", 0, 0);
//	if(stype==0 || stype==WmTypes.NS_MODE_OBJECT)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "EditMesh", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_MESH)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Curve", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_CURVE)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Armature", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_ARMATURE)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Lattice", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_LATTICE)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	/* armature sketching needs to take over mouse */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Armature_Sketch", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_TEXT)
		WmEventSystem.WM_event_add_keymap_handler_priority(ar.handlers, keymap, 10);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Particle", 0, 0);
//	if(stype==WmTypes.NS_MODE_PARTICLE)
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);

	/* editfont keymap swallows all... */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Font", 0, 0);
//	if(stype==WmTypes.NS_EDITMODE_TEXT)
		WmEventSystem.WM_event_add_keymap_handler_priority(ar.handlers, keymap, 10);
//	else
//		WmEventSystem.WM_event_remove_keymap_handler(ar.handlers, keymap.keymap);
}

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init view3d_main_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void view3d_main_area_init(wmWindowManager *wm, ARegion *ar)
{
//        System.out.println("view3d_main_area_init");
	RegionView3D rv3d= (RegionView3D)ar.regiondata;
	wmKeyMap keymap;

	/* own keymap */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View3D Generic", SpaceTypes.SPACE_VIEW3D, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View3D", SpaceTypes.SPACE_VIEW3D, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);

//        System.out.println("view3d_main_area_init handlers:");
//        wmEventHandler nexthandler;
//        for (wmEventHandler handler = (wmEventHandler) ar.handlers.first; handler != null; handler = nexthandler) {
//            nexthandler = handler.next;
//            if (handler.keymap != null) {
//                for (wmKeymapItem kmi = (wmKeymapItem) handler.keymap.first; kmi != null; kmi = kmi.next) {
//                    System.out.println(kmi);
//                }
//            }
//        }

	/* object ops. */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Object Non-modal", 0, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);

	/* pose is not modal, operator poll checks for this */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Pose", 0, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);

	/* modal ops keymaps */
//	view3d_modal_keymaps(wm, ar, rv3d.lastmode);
	view3d_modal_keymaps(wm, ar, 0);
	/* operator poll checks for modes */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "ImagePaint", 0, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
}};

///* type callback, not region itself */
public static ARegionType.Free view3d_main_area_free = new ARegionType.Free() {
public void run(ARegion ar)
//static void view3d_main_area_free(ARegion *ar)
{
	RegionView3D rv3d= (RegionView3D)ar.regiondata;

	if(rv3d!=null) {
//		if(rv3d.localvd) MEM_freeN(rv3d.localvd);
//		if(rv3d.clipbb) MEM_freeN(rv3d.clipbb);

		// XXX	retopo_free_view_data(rv3d);
		if(rv3d.ri!=null) {
			// XXX		BIF_view3d_previewrender_free(rv3d);
		}

		if(rv3d.depths!=null) {
//			if(rv3d.depths.depths) MEM_freeN(rv3d.depths.depths);
//			MEM_freeN(rv3d.depths);
		}
//		MEM_freeN(rv3d);
		ar.regiondata= null;
	}
}};

///* copy regiondata */
public static ARegionType.Duplicate view3d_main_area_duplicate = new ARegionType.Duplicate() {
public Object run(Object poin)
//static void *view3d_main_area_duplicate(void *poin)
{
	if(poin!=null) {
		RegionView3D rv3d= (RegionView3D)poin, newrv3d;

//		newrv3d= MEM_dupallocN(rv3d);
                newrv3d= rv3d.copy();
		if(rv3d.localvd!=null)
//			newrv3d.localvd= MEM_dupallocN(rv3d.localvd);
                        newrv3d.localvd= rv3d.localvd.copy();
		if(rv3d.clipbb!=null)
//			newrv3d.clipbb= MEM_dupallocN(rv3d.clipbb);
                        newrv3d.clipbb= rv3d.clipbb.copy();

		newrv3d.depths= null;
//		newrv3d.retopo_view_data= null;
		newrv3d.ri= null;
		newrv3d.gpd= null;
		newrv3d.sms= null;
		newrv3d.smooth_timer= null;

		return newrv3d;
	}
	return null;
}};

public static ARegionType.Listener view3d_main_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void view3d_main_area_listener(ARegion *ar, wmNotifier *wmn)
{
//        System.out.println("view3d_main_area_listener");
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_ANIMATION:
//			switch(wmn.data) {
//				case WmTypes.ND_KEYFRAME_EDIT:
//				case WmTypes.ND_KEYFRAME_PROP:
//				case WmTypes.ND_NLA_EDIT:
//				case WmTypes.ND_NLA_ACTCHANGE:
//				case WmTypes.ND_ANIMCHAN_SELECT:
//					Area.ED_region_tag_redraw(ar);
//					break;
//			}
			switch(wmn.data) {
				case WmTypes.ND_KEYFRAME_PROP:
				case WmTypes.ND_NLA_ACTCHANGE:
					Area.ED_region_tag_redraw(ar);
					break;
				case WmTypes.ND_NLA:
				case WmTypes.ND_KEYFRAME:
					if (wmn.action == WmTypes.NA_EDITED)
						Area.ED_region_tag_redraw(ar);
					break;
				case WmTypes.ND_ANIMCHAN:
					if (wmn.action == WmTypes.NA_SELECTED)
						Area.ED_region_tag_redraw(ar);
					break;
			}
			break;
		case WmTypes.NC_SCENE:
			switch(wmn.data) {
				case WmTypes.ND_TRANSFORM:
				case WmTypes.ND_FRAME:
				case WmTypes.ND_OB_ACTIVE:
				case WmTypes.ND_OB_SELECT:
					Area.ED_region_tag_redraw(ar);
					break;
				case WmTypes.ND_MODE:
					view3d_modal_keymaps(wmn.wm, ar, wmn.subtype);
					Area.ED_region_tag_redraw(ar);
					break;
			}
			break;
		case WmTypes.NC_OBJECT:
			switch(wmn.data) {
				case WmTypes.ND_BONE_ACTIVE:
				case WmTypes.ND_BONE_SELECT:
				case WmTypes.ND_TRANSFORM:
				case WmTypes.ND_GEOM_SELECT:
				case WmTypes.ND_GEOM_DATA:
				case WmTypes.ND_DRAW:
				case WmTypes.ND_MODIFIER:
				case WmTypes.ND_CONSTRAINT:
				case WmTypes.ND_KEYS:
				case WmTypes.ND_PARTICLE:
					Area.ED_region_tag_redraw(ar);
					break;
			}
		case WmTypes.NC_GROUP:
			/* all group ops for now */
			Area.ED_region_tag_redraw(ar);
			break;
		case WmTypes.NC_MATERIAL:
			switch(wmn.data) {
				case WmTypes.ND_SHADING_DRAW:
					Area.ED_region_tag_redraw(ar);
					break;
			}
		case WmTypes.NC_LAMP:
			switch(wmn.data) {
				case WmTypes.ND_LIGHTING_DRAW:
					Area.ED_region_tag_redraw(ar);
					break;
			}
		case WmTypes.NC_IMAGE:
			/* this could be more fine grained checks if we had
			 * more context than just the region */
			Area.ED_region_tag_redraw(ar);
			break;
	}
}};

///* concept is to retrieve cursor type context-less */
//static void view3d_main_area_cursor(wmWindow *win, ScrArea *sa, ARegion *ar)
//{
//	Scene *scene= win.screen.scene;
//
//	if(scene.obedit) {
//		WM_cursor_set(win, CURSOR_EDIT);
//	}
//	else {
//		WM_cursor_set(win, CURSOR_STD);
//	}
//}
//
//
///* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init view3d_header_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void view3d_header_area_init(wmWindowManager *wm, ARegion *ar)
{
	wmKeyMap keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View3D Generic", SpaceTypes.SPACE_VIEW3D, 0);

	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);

	Area.ED_region_header_init(ar);
}};

public static ARegionType.Draw view3d_header_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//static void view3d_header_area_draw(const bContext *C, ARegion *ar)
{
	Area.ED_region_header(gl, C, ar);

//    // TMP
//        float[] col=new float[3];
//
//	/* clear */
//	if(ScreenEdit.ED_screen_area_active(C))
//		Resources.UI_GetThemeColor3fv(Resources.TH_HEADER, col);
//	else
//		Resources.UI_GetThemeColor3fv(Resources.TH_HEADERDESEL, col);
//
//	gl.glClearColor(col[0], col[1], col[2], 0.0f);
//	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
//
//	/* set view2d view matrix for scrolling (without scrollers) */
//	View2dUtil.UI_view2d_view_ortho(gl, C, ar.v2d);
//
//	View3dHeader.uiTemplateHeader3D(gl, null, C);
//
//	/* restore view matrix? */
//	View2dUtil.UI_view2d_view_restore(gl, C);
}};

public static ARegionType.Listener view3d_header_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void view3d_header_area_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCENE:
			switch(wmn.data) {
				case WmTypes.ND_FRAME:
				case WmTypes.ND_OB_ACTIVE:
				case WmTypes.ND_OB_SELECT:
				case WmTypes.ND_MODE:
					Area.ED_region_tag_redraw(ar);
					break;
			}
			break;
	}
}};

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init view3d_buttons_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void view3d_buttons_area_init(wmWindowManager *wm, ARegion *ar)
{
	wmKeyMap keymap;

	Area.ED_region_panels_init(wm, ar);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View3D Generic", SpaceTypes.SPACE_VIEW3D, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
}};

public static ARegionType.Draw view3d_buttons_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//static void view3d_buttons_area_draw(const bContext *C, ARegion *ar)
{
	Area.ED_region_panels(gl, C, ar, true, null, -1);
}};

public static ARegionType.Listener view3d_buttons_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void view3d_buttons_area_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_ANIMATION:
//			switch(wmn.data) {
//				case WmTypes.ND_KEYFRAME_EDIT:
//				case WmTypes.ND_KEYFRAME_PROP:
//				case WmTypes.ND_NLA_EDIT:
//				case WmTypes.ND_NLA_ACTCHANGE:
//					Area.ED_region_tag_redraw(ar);
//					break;
//			}
			switch(wmn.data) {
				case WmTypes.ND_KEYFRAME_PROP:
				case WmTypes.ND_NLA_ACTCHANGE:
					Area.ED_region_tag_redraw(ar);
					break;
				case WmTypes.ND_NLA:
				case WmTypes.ND_KEYFRAME:
					if (wmn.action == WmTypes.NA_EDITED)
						Area.ED_region_tag_redraw(ar);
					break;	
			}
			break;
		case WmTypes.NC_SCENE:
			switch(wmn.data) {
				case WmTypes.ND_FRAME:
				case WmTypes.ND_OB_ACTIVE:
				case WmTypes.ND_OB_SELECT:
				case WmTypes.ND_MODE:
					Area.ED_region_tag_redraw(ar);
					break;
			}
			break;
		case WmTypes.NC_OBJECT:
			switch(wmn.data) {
				case WmTypes.ND_BONE_ACTIVE:
				case WmTypes.ND_BONE_SELECT:
				case WmTypes.ND_TRANSFORM:
				case WmTypes.ND_GEOM_SELECT:
				case WmTypes.ND_GEOM_DATA:
				case WmTypes.ND_DRAW:
				case WmTypes.ND_KEYS:
					Area.ED_region_tag_redraw(ar);
					break;
			}
	}
}};

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init view3d_tools_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void view3d_tools_area_init(wmWindowManager *wm, ARegion *ar)
{
	wmKeyMap keymap;

	Area.ED_region_panels_init(wm, ar);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "View3D Generic", SpaceTypes.SPACE_VIEW3D, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
}};


public static ARegionType.Draw view3d_tools_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//static void view3d_tools_area_draw(const bContext *C, ARegion *ar)
{
//	Area.ED_region_panels(gl, C, ar, true, View3dToolbar.view3d_context_string(C));
	Area.ED_region_panels(gl, C, ar, true, bContext.CTX_data_mode_string(C), -1);
}};

static String[] dir = {
			"selected_objects", "selected_bases", "selected_editable_objects",
			"selected_editable_bases", "visible_objects", "visible_bases", "selectable_objects", "selectable_bases",
			"active_base", "active_object", "visible_bones", "editable_bones",
			"selected_bones", "selected_editable_bones", "visible_pchans",
			"selected_pchans", "active_bone", "active_pchan", null
};

public static bContextDataCallback view3d_context = new bContextDataCallback() {
public boolean run(bContext C, byte[] member, bContextDataResult result)
//static int view3d_context(const bContext *C, const char *member, bContextDataResult *result)
{
	View3D v3d= bContext.CTX_wm_view3d(C);
	Scene scene= bContext.CTX_data_scene(C);
	Base base;
	int lay = v3d!=null ? v3d.lay:scene.lay; /* fallback to the scene layer, allows duplicate and other oject operators to run outside the 3d view */

	if(bContext.CTX_data_dir(member)) {
//		static const char *dir[] = {
//			"selected_objects", "selected_bases" "selected_editable_objects",
//			"selected_editable_bases" "visible_objects", "visible_bases", "selectable_objects", "selectable_bases",
//			"active_base", "active_object", "visible_bones", "editable_bones",
//			"selected_bones", "selected_editable_bones" "visible_pchans",
//			"selected_pchans", "active_bone", "active_pchan", NULL};

		bContext.CTX_data_dir_set(result, dir);
	}
	else if(bContext.CTX_data_equals(member, "selected_objects") || bContext.CTX_data_equals(member, "selected_bases")) {
		boolean selected_objects= bContext.CTX_data_equals(member, "selected_objects");

		for(base=(Base)scene.base.first; base!=null; base=base.next) {
			if((base.flag & Blender.SELECT)!=0 && (base.lay & lay)!=0) {
				if((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0) {
					if(selected_objects)
						bContext.CTX_data_id_list_add(result, base.object.id);
					else
						bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
				}
			}
		}

		return true;
	}
	else if(bContext.CTX_data_equals(member, "selected_editable_objects") || bContext.CTX_data_equals(member, "selected_editable_bases")) {
		boolean selected_editable_objects= bContext.CTX_data_equals(member, "selected_editable_objects");

		for(base=(Base)scene.base.first; base!=null; base=base.next) {
			if((base.flag & Blender.SELECT)!=0 && (base.lay & lay)!=0) {
				if((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0) {
					if(false==ObjectUtil.object_is_libdata(base.object)) {
						if(selected_editable_objects)
							bContext.CTX_data_id_list_add(result, base.object.id);
						else
							bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
					}
				}
			}
		}

		return true;
	}
	else if(bContext.CTX_data_equals(member, "visible_objects") || bContext.CTX_data_equals(member, "visible_bases")) {
		boolean visible_objects= bContext.CTX_data_equals(member, "visible_objects");

		for(base=(Base)scene.base.first; base!=null; base=base.next) {
			if((base.lay & lay)!=0) {
				if((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0) {
					if(visible_objects)
						bContext.CTX_data_id_list_add(result, base.object.id);
					else
						bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
				}
			}
		}

		return true;
	}
	else if(bContext.CTX_data_equals(member, "selectable_objects") || bContext.CTX_data_equals(member, "selectable_bases")) {
		boolean selectable_objects= bContext.CTX_data_equals(member, "selectable_objects");

		for(base=(Base)scene.base.first; base!=null; base=base.next) {
			if((base.lay & lay)!=0) {
				if((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0 && (base.object.restrictflag & ObjectTypes.OB_RESTRICT_SELECT)==0) {
					if(selectable_objects)
						bContext.CTX_data_id_list_add(result, base.object.id);
					else
						bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
				}
			}
		}

		return true;
	}
	else if(bContext.CTX_data_equals(member, "active_base")) {
		if(scene.basact!=null && (scene.basact.lay & lay)!=0)
			if((scene.basact.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0)
				bContext.CTX_data_pointer_set(result, scene.id, RnaAccess.RNA_UnknownType, scene.basact);

		return true;
	}
	else if(bContext.CTX_data_equals(member, "active_object")) {
		if(scene.basact!=null && (scene.basact.lay & lay)!=0)
			if((scene.basact.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0)
				bContext.CTX_data_id_pointer_set(result, scene.basact.object.id);

		return true;
	}
//	else if(bContext.CTX_data_equals(member, "visible_bones") || bContext.CTX_data_equals(member, "editable_bones")) {
//		bObject obedit= scene.obedit; // XXX get from context?
//		bArmature arm= (obedit) ? obedit.data : NULL;
//		EditBone ebone, *flipbone=NULL;
//		int editable_bones= CTX_data_equals(member, "editable_bones");
//
//		if (arm && arm.edbo) {
//			/* Attention: X-Axis Mirroring is also handled here... */
//			for (ebone= arm.edbo.first; ebone; ebone= ebone.next) {
//				/* first and foremost, bone must be visible and selected */
//				if (EBONE_VISIBLE(arm, ebone)) {
//					/* Get 'x-axis mirror equivalent' bone if the X-Axis Mirroring option is enabled
//					 * so that most users of this data don't need to explicitly check for it themselves.
//					 *
//					 * We need to make sure that these mirrored copies are not selected, otherwise some
//					 * bones will be operated on twice.
//					 */
//					if (arm.flag & ARM_MIRROR_EDIT)
//						flipbone = ED_armature_bone_get_mirrored(arm.edbo, ebone);
//
//					/* if we're filtering for editable too, use the check for that instead, as it has selection check too */
//					if (editable_bones) {
//						/* only selected + editable */
//						if (EBONE_EDITABLE(ebone)) {
//							CTX_data_list_add(result, &arm.id, &RNA_UnknownType, ebone);
//
//							if ((flipbone) && !(flipbone.flag & BONE_SELECTED))
//								CTX_data_list_add(result, &arm.id, &RNA_UnknownType, flipbone);
//						}
//					}
//					else {
//						/* only include bones if visible */
//						CTX_data_list_add(result, &arm.id, &RNA_UnknownType, ebone);
//
//						if ((flipbone) && EBONE_VISIBLE(arm, flipbone)==0)
//							CTX_data_list_add(result, &arm.id, &RNA_UnknownType, flipbone);
//					}
//				}
//			}
//
//			return 1;
//		}
//	}
//	else if(CTX_data_equals(member, "selected_bones") || CTX_data_equals(member, "selected_editable_bones")) {
//		Object *obedit= scene.obedit; // XXX get from context?
//		bArmature *arm= (obedit) ? obedit.data : NULL;
//		EditBone *ebone, *flipbone=NULL;
//		int selected_editable_bones= CTX_data_equals(member, "selected_editable_bones");
//
//		if (arm && arm.edbo) {
//			/* Attention: X-Axis Mirroring is also handled here... */
//			for (ebone= arm.edbo.first; ebone; ebone= ebone.next) {
//				/* first and foremost, bone must be visible and selected */
//				if (EBONE_VISIBLE(arm, ebone) && (ebone.flag & BONE_SELECTED)) {
//					/* Get 'x-axis mirror equivalent' bone if the X-Axis Mirroring option is enabled
//					 * so that most users of this data don't need to explicitly check for it themselves.
//					 *
//					 * We need to make sure that these mirrored copies are not selected, otherwise some
//					 * bones will be operated on twice.
//					 */
//					if (arm.flag & ARM_MIRROR_EDIT)
//						flipbone = ED_armature_bone_get_mirrored(arm.edbo, ebone);
//
//					/* if we're filtering for editable too, use the check for that instead, as it has selection check too */
//					if (selected_editable_bones) {
//						/* only selected + editable */
//						if (EBONE_EDITABLE(ebone)) {
//							CTX_data_list_add(result, &arm.id, &RNA_UnknownType, ebone);
//
//							if ((flipbone) && !(flipbone.flag & BONE_SELECTED))
//								CTX_data_list_add(result, &arm.id, &RNA_UnknownType, flipbone);
//						}
//					}
//					else {
//						/* only include bones if selected */
//						CTX_data_list_add(result, &arm.id, &RNA_UnknownType, ebone);
//
//						if ((flipbone) && !(flipbone.flag & BONE_SELECTED))
//							CTX_data_list_add(result, &arm.id, &RNA_UnknownType, flipbone);
//					}
//				}
//			}
//
//			return 1;
//		}
//	}
//	else if(CTX_data_equals(member, "visible_pchans")) {
//		Object *obact= OBACT;
//		bArmature *arm= (obact) ? obact.data : NULL;
//		bPoseChannel *pchan;
//
//		if (obact && arm) {
//			for (pchan= obact.pose.chanbase.first; pchan; pchan= pchan.next) {
//				/* ensure that PoseChannel is on visible layer and is not hidden in PoseMode */
//				if ((pchan.bone) && (arm.layer & pchan.bone.layer) && !(pchan.bone.flag & BONE_HIDDEN_P)) {
//					CTX_data_list_add(result, &obact.id, &RNA_PoseChannel, pchan);
//				}
//			}
//
//			return 1;
//		}
//	}
//	else if(CTX_data_equals(member, "selected_pchans")) {
//		Object *obact= OBACT;
//		bArmature *arm= (obact) ? obact.data : NULL;
//		bPoseChannel *pchan;
//
//		if (obact && arm) {
//			for (pchan= obact.pose.chanbase.first; pchan; pchan= pchan.next) {
//				/* ensure that PoseChannel is on visible layer and is not hidden in PoseMode */
//				if ((pchan.bone) && (arm.layer & pchan.bone.layer) && !(pchan.bone.flag & BONE_HIDDEN_P)) {
//					if (pchan.bone.flag & (BONE_SELECTED|BONE_ACTIVE))
//						CTX_data_list_add(result, &obact.id, &RNA_PoseChannel, pchan);
//				}
//			}
//
//			return 1;
//		}
//	}
//	else if(CTX_data_equals(member, "active_bone")) {
//		Object *obedit= scene.obedit; // XXX get from context?
//		bArmature *arm= (obedit) ? obedit.data : NULL;
//		EditBone *ebone;
//
//		if (arm && arm.edbo) {
//			for (ebone= arm.edbo.first; ebone; ebone= ebone.next) {
//				if (EBONE_VISIBLE(arm, ebone)) {
//					if (ebone.flag & BONE_ACTIVE) {
//						CTX_data_pointer_set(result, &arm.id, &RNA_UnknownType, ebone);
//
//						return 1;
//					}
//				}
//			}
//		}
//
//	}
//	else if(CTX_data_equals(member, "active_pchan")) {
//		Object *obact= OBACT;
//		bPoseChannel *pchan;
//
//		pchan= get_active_posechannel(obact);
//		if (pchan) {
//			CTX_data_pointer_set(result, &obact.id, &RNA_PoseChannel, pchan);
//			return 1;
//		}
//	}

	return false;
}};

/* only called once, from space/spacetypes.c */
public static void ED_spacetype_view3d()
{
	SpaceType st= new SpaceType();
	ARegionType art;

	st.spaceid= SpaceTypes.SPACE_VIEW3D;

	st.newInstance= view3d_new;
	st.free= view3d_free;
	st.init= view3d_init;
	st.duplicate= view3d_duplicate;
	st.operatortypes= View3dOps.view3d_operatortypes;
	st.keymap= View3dOps.view3d_keymap;
	st.context= view3d_context;

	/* regions: main window */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
	art.keymapflag= Area.ED_KEYMAP_GPENCIL;
	art.draw= View3dDraw.view3d_main_area_draw;
	art.init= view3d_main_area_init;
	art.free= view3d_main_area_free;
	art.duplicate= view3d_main_area_duplicate;
	art.listener= view3d_main_area_listener;
//	art.cursor= view3d_main_area_cursor;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	/* regions: listview/buttons */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_UI;
	art.minsizex= 220; // XXX
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_FRAMES;
	art.listener= view3d_buttons_area_listener;
	art.init= view3d_buttons_area_init;
	art.draw= view3d_buttons_area_draw;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	View3dButtons.view3d_buttons_register(art);

	/* regions: tool(bar) */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_TOOLS;
	art.minsizex= 120; // XXX
	art.minsizey= 50; // XXX
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_FRAMES;
	art.listener= view3d_buttons_area_listener;
	art.init= view3d_tools_area_init;
	art.draw= view3d_tools_area_draw;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	View3dToolbar.view3d_toolbar_register(art);

	/* regions: tool properties */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_TOOL_PROPS;
	art.minsizex= 0;
	art.minsizey= 120;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_FRAMES;
	art.listener= view3d_buttons_area_listener;
	art.init= view3d_tools_area_init;
	art.draw= view3d_tools_area_draw;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	View3dToolbar.view3d_tool_props_register(art);


	/* regions: header */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_HEADER;
	art.minsizey= ScreenTypes.HEADERY;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;
	art.listener= view3d_header_area_listener;
	art.init= view3d_header_area_init;
	art.draw= view3d_header_area_draw;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	ScreenUtil.BKE_spacetype_register(st);
}

}

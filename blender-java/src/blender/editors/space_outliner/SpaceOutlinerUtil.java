/**
 * $Id: SpaceOutlinerUtil.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
package blender.editors.space_outliner;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.View2dUtil;
import blender.editors.uinterface.View2dUtil.View2DScrollers;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View2dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.SpaceOops;
import blender.makesdna.sdna.View2D;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceOutlinerUtil {

public static ARegionType.Init outliner_main_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	wmKeyMap keymap;

	View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_LIST, ar.winx, ar.winy);

	/* own keymap */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Outliner", SpaceTypes.SPACE_OUTLINER, 0);
	/* don't pass on view2d mask, it's always set with scrollbar space, hide fails */
	WmEventSystem.WM_event_add_keymap_handler_bb(ar.handlers, keymap, null, ar.winrct);
}};

public static ARegionType.Draw outliner_main_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	View2D v2d= ar.v2d;
	View2DScrollers scrollers;

	/* clear */
	Resources.UI_ThemeClearColor(gl, Resources.TH_BACK);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

	Outliner.draw_outliner(gl, C);

	/* reset view matrix */
	View2dUtil.UI_view2d_view_restore(gl, C);

	/* scrollers */
	scrollers= View2dUtil.UI_view2d_scrollers_calc(C, v2d, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY);
	View2dUtil.UI_view2d_scrollers_draw(gl, C, v2d, scrollers);
	View2dUtil.UI_view2d_scrollers_free(scrollers);
}};

public static ARegionType.Free outliner_main_area_free = new ARegionType.Free() {
public void run(ARegion ar)
//static void outliner_main_area_free(ARegion *ar)
{
}};

public static ARegionType.Listener outliner_main_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void outliner_main_area_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCENE:
			switch(wmn.data) {
				case WmTypes.ND_OB_ACTIVE:
				case WmTypes.ND_OB_SELECT:
				case WmTypes.ND_MODE:
				case WmTypes.ND_KEYINGSET:
				case WmTypes.ND_FRAME:
				case WmTypes.ND_RENDER_OPTIONS:
					Area.ED_region_tag_redraw(ar);
					break;
			}
			break;
		case WmTypes.NC_OBJECT:
			switch(wmn.data) {
				case WmTypes.ND_BONE_ACTIVE:
				case WmTypes.ND_BONE_SELECT:
				case WmTypes.ND_TRANSFORM:
					Area.ED_region_tag_redraw(ar);
					break;
			}
		case WmTypes.NC_GROUP:
			/* all actions now, todo: check outliner view mode? */
			Area.ED_region_tag_redraw(ar);
			break;
	}
}};


/* ************************ header outliner area region *********************** */

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init outliner_header_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	Area.ED_region_header_init(ar);
}};

public static ARegionType.Draw outliner_header_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	Area.ED_region_header(gl, C, ar);
}};

public static ARegionType.Free outliner_header_area_free = new ARegionType.Free() {
public void run(ARegion ar)
{
	
}};

public static ARegionType.Listener outliner_header_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCENE:
			if(wmn.data == WmTypes.ND_KEYINGSET)
				Area.ED_region_tag_redraw(ar);
			break;
		case WmTypes.NC_SPACE:
			if(wmn.data == WmTypes.ND_SPACE_OUTLINER)
				Area.ED_region_tag_redraw(ar);
			break;
	}
}};

/* ******************** default callbacks for outliner space ***************** */

public static SpaceType.New outliner_new = new SpaceType.New() {
public SpaceLink run(bContext C)
{
	ARegion ar;
	SpaceOops soutliner;

	soutliner= new SpaceOops();
	soutliner.spacetype= SpaceTypes.SPACE_OUTLINER;

	/* header */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(soutliner.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
	ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;

	/* main area */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(soutliner.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;

	ar.v2d.scroll = (View2dTypes.V2D_SCROLL_RIGHT|View2dTypes.V2D_SCROLL_BOTTOM_O);
	ar.v2d.align = (View2dTypes.V2D_ALIGN_NO_NEG_X|View2dTypes.V2D_ALIGN_NO_POS_Y);
	ar.v2d.keepzoom = (View2dTypes.V2D_LOCKZOOM_X|View2dTypes.V2D_LOCKZOOM_Y|View2dTypes.V2D_LIMITZOOM|View2dTypes.V2D_KEEPASPECT);
	ar.v2d.keeptot= View2dTypes.V2D_KEEPTOT_STRICT;
	ar.v2d.minzoom= ar.v2d.maxzoom= 1.0f;

	return (SpaceLink)soutliner;
}};

/* not spacelink itself */
public static SpaceType.Free outliner_free = new SpaceType.Free() {
public void run(SpaceLink sl)
{
//	SpaceOops *soutliner= (SpaceOops*)sl;
//
//	outliner_free_tree(&soutliner.tree);
//	if(soutliner.treestore) {
//		if(soutliner.treestore.data) MEM_freeN(soutliner.treestore.data);
//		MEM_freeN(soutliner.treestore);
//	}
//
}};

/* spacetype; init callback */
public static SpaceType.Init outliner_init = new SpaceType.Init() {
public void run(wmWindowManager wm, ScrArea sa)
{

}};

public static SpaceType.Duplicate outliner_duplicate = new SpaceType.Duplicate() {
public SpaceLink run(SpaceLink sl)
{
	SpaceOops soutliner= (SpaceOops)sl;
	SpaceOops soutlinern= soutliner.copy();

	soutlinern.tree.first= soutlinern.tree.last= null;
	soutlinern.treestore= null;

	return (SpaceLink)soutlinern;
}};

/* only called once, from space_api/spacetypes.c */
public static void ED_spacetype_outliner()
{
	SpaceType st= new SpaceType();
	ARegionType art;

	st.spaceid= SpaceTypes.SPACE_OUTLINER;
	StringUtil.strncpy(st.name,0, StringUtil.toCString("Outliner"),0, ScreenUtil.BKE_ST_MAXNAME);

	st.newInstance= outliner_new;
	st.free= outliner_free;
	st.init= outliner_init;
	st.duplicate= outliner_duplicate;
	st.operatortypes= OutlinerOps.outliner_operatortypes;
	st.keymap= OutlinerOps.outliner_keymap;

	/* regions: main window */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D;

	art.init= outliner_main_area_init;
	art.draw= outliner_main_area_draw;
	art.free= outliner_main_area_free;
	art.listener= outliner_main_area_listener;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	/* regions: header */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_HEADER;
	art.minsizey= ScreenTypes.HEADERY;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;

	art.init= outliner_header_area_init;
	art.draw= outliner_header_area_draw;
	art.free= outliner_header_area_free;
	art.listener= outliner_header_area_listener;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	ScreenUtil.BKE_spacetype_register(st);
}

}

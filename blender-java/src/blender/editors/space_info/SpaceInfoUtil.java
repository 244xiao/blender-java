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
package blender.editors.space_info;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.Menu;
import blender.blenkernel.ScreenUtil.MenuType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceInfo;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.Wm;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceInfoUtil {

/* ******************** default callbacks for info space ***************** */

public static SpaceType.New info_new = new SpaceType.New() {
public SpaceLink run(bContext C)
//static SpaceLink *info_new(const bContext *C)
{
	ARegion ar;
	SpaceInfo sinfo;

	sinfo= new SpaceInfo();
	sinfo.spacetype= SpaceTypes.SPACE_INFO;

	/* header */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(sinfo.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
	ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;

	/* main area */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(sinfo.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;

	return (SpaceLink)sinfo;
}};

///* not spacelink itself */
public static SpaceType.Free info_free = new SpaceType.Free() {
public void run(SpaceLink sl)
//static void info_free(SpaceLink *sl)
{
//	SpaceInfo *sinfo= (SpaceInfo*) sl;

}};


///* spacetype; init callback */
public static SpaceType.Init info_init = new SpaceType.Init() {
public void run(wmWindowManager wm, ScrArea sa)
//static void info_init(struct wmWindowManager *wm, ScrArea *sa)
{

}};

public static SpaceType.Duplicate info_duplicate = new SpaceType.Duplicate() {
public SpaceLink run(SpaceLink sl)
//static SpaceLink *info_duplicate(SpaceLink *sl)
{
	SpaceInfo sinfon= ((SpaceInfo)sl).copy();

	/* clear or remove stuff from old */

	return (SpaceLink)sinfon;
}};


/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init info_main_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void info_main_area_init(wmWindowManager *wm, ARegion *ar)
{
	Area.ED_region_panels_init(wm, ar);
}};

public static ARegionType.Draw info_main_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//static void info_main_area_draw(const bContext *C, ARegion *ar)
{
//	Area.ED_region_panels(gl, C, ar, true, null);
}};

public static SpaceType.OperatorTypes info_operatortypes = new SpaceType.OperatorTypes() {
public void run()
//void info_operatortypes(void)
{
//	WM_operatortype_append(FILE_OT_pack_all);
//	WM_operatortype_append(FILE_OT_unpack_all);
//	WM_operatortype_append(FILE_OT_make_paths_relative);
//	WM_operatortype_append(FILE_OT_make_paths_absolute);
//	WM_operatortype_append(FILE_OT_report_missing_files);
//	WM_operatortype_append(FILE_OT_find_missing_files);
}};

public static SpaceType.KeyMap info_keymap = new SpaceType.KeyMap() {
public void run(wmKeyConfig keyconf)
//void info_keymap(struct wmWindowManager *wm)
{

}};

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init info_header_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
//static void info_header_area_init(wmWindowManager *wm, ARegion *ar)
{
	Area.ED_region_header_init(ar);
}};

public static ARegionType.Draw info_header_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
//static void info_header_area_draw(const bContext *C, ARegion *ar)
{
	Area.ED_region_header(gl, C, ar);
}};

public static ARegionType.Listener info_main_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void info_main_area_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
}};

public static ARegionType.Listener info_header_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void info_header_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCREEN:
			if(wmn.data==WmTypes.ND_SCREENCAST || wmn.data==WmTypes.ND_ANIMPLAY)
				Area.ED_region_tag_redraw(ar);
			break;
		case WmTypes.NC_SCENE:
			if(wmn.data==WmTypes.ND_RENDER_RESULT)
				Area.ED_region_tag_redraw(ar);
			break;
	}
}};

public static MenuType.Draw recent_files_menu_draw = new MenuType.Draw() {
public void run(bContext C, Menu menu)
{
//	struct RecentFile *recent;
	uiLayout layout= menu.layout;
//	uiLayoutSetOperatorContext(layout, WM_OP_EXEC_REGION_WIN);
//	if (G.recent_files.first) {
//		for(recent = G.recent_files.first; (recent); recent = recent->next) {
//			uiItemStringO(layout, BLI_path_basename(recent->filepath), ICON_FILE_BLEND, "WM_OT_open_mainfile", "filepath", recent->filepath);
//		}
//	} else {
		UILayout.uiItemL(layout, "No Recent Files", UI.ICON_NULL);
//	}
}};

static void recent_files_menu_register()
{
	MenuType mt;

	mt= new MenuType();
	StringUtil.strcpy(mt.idname,0, StringUtil.toCString("INFO_MT_file_open_recent"),0);
	StringUtil.strcpy(mt.label,0, StringUtil.toCString("Open Recent..."),0);
	mt.draw= recent_files_menu_draw;
	Wm.WM_menutype_add(mt);
}

/* only called once, from space/spacetypes.c */
public static void ED_spacetype_info()
{
	SpaceType st= new SpaceType();
	ARegionType art;

	st.spaceid= SpaceTypes.SPACE_INFO;
	StringUtil.strncpy(st.name,0, StringUtil.toCString("Info"),0, ScreenUtil.BKE_ST_MAXNAME);

	st.newInstance= info_new;
	st.free= info_free;
	st.init= info_init;
	st.duplicate= info_duplicate;
	st.operatortypes= info_operatortypes;
	st.keymap= info_keymap;

	/* regions: main window */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D;
	
	art.init= info_main_area_init;
	art.draw= info_main_area_draw;
	art.listener= info_main_area_listener;
	
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	/* regions: header */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_HEADER;
	art.minsizey= ScreenTypes.HEADERY;
	
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;
	art.listener= info_header_listener;
	art.init= info_header_area_init;
	art.draw= info_header_area_draw;

	ListBaseUtil.BLI_addhead(st.regiontypes, art);
	
	recent_files_menu_register();

	ScreenUtil.BKE_spacetype_register(st);
}

}

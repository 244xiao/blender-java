/**
 * $Id: spacetypes.c
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
 * The Original Code is Copyright (C) Blender Foundation, 2008
 *
 * ***** END GPL/BL DUAL LICENSE BLOCK *****
 */
package blender.editors.space_api;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.editors.animation.AnimOps;
import blender.editors.object.ObjectOps;
import blender.editors.render.RenderOps;
import blender.editors.screen.ScreenOps;
import blender.editors.space_buttons.SpaceButtonsUtil;
import blender.editors.space_info.SpaceInfoUtil;
import blender.editors.space_node.SpaceNodeUtil;
import blender.editors.space_outliner.SpaceOutlinerUtil;
import blender.editors.space_time.SpaceTimeUtil;
import blender.editors.space_view3d.SpaceView3dUtil;
import blender.editors.uinterface.View2dOps;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.wmKeyConfig;

public class SpaceTypeUtil {
	
public static final int REGION_DRAW_POST_VIEW=	0;
public static final int REGION_DRAW_POST_PIXEL=	1;
public static final int REGION_DRAW_PRE_VIEW=	2;

/* only call once on startup, storage is global in BKE kernel listbase */
public static void ED_spacetypes_init()
{
	ListBase<SpaceType> spacetypes;
	SpaceType type;

	/* create space types */
	SpaceOutlinerUtil.ED_spacetype_outliner();
	SpaceTimeUtil.ED_spacetype_time();
	SpaceView3dUtil.ED_spacetype_view3d();
//	ED_spacetype_ipo();
//	ED_spacetype_image();
	SpaceNodeUtil.ED_spacetype_node();
	SpaceButtonsUtil.ED_spacetype_buttons();
	SpaceInfoUtil.ED_spacetype_info();
//	ED_spacetype_file();
//	ED_spacetype_sound();
//	ED_spacetype_action();
//	ED_spacetype_nla();
//	ED_spacetype_script();
//	ED_spacetype_text();
//	ED_spacetype_sequencer();
//	ED_spacetype_logic();
//	ED_spacetype_console();
//	ED_spacetype_userpref();
//	...

	/* register operator types for screen and all spaces */
	ScreenOps.ED_operatortypes_screen();
	AnimOps.ED_operatortypes_anim();
//	ED_operatortypes_animchannels();
//	ED_operatortypes_gpencil();
	ObjectOps.ED_operatortypes_object();
//	ED_operatortypes_mesh();
//	ED_operatortypes_sculpt();
//	ED_operatortypes_uvedit();
//	ED_operatortypes_paint();
//	ED_operatortypes_physics();
//	ED_operatortypes_curve();
//	ED_operatortypes_armature();
//	ED_operatortypes_marker();
//	ED_operatortypes_metaball();
//	ED_operatortypes_sound();
	RenderOps.ED_operatortypes_render();
//	ED_operatortypes_logic();

	View2dOps.UI_view2d_operatortypes();
//	UI_buttons_operatortypes();

	/* register operators */
	spacetypes = ScreenUtil.BKE_spacetypes_list();
	for(type=spacetypes.first; type!=null; type=type.next) {
		if(type.operatortypes!=null)
			type.operatortypes.run();
	}
	
//	/* Macros's must go last since they reference other operators
//	 * maybe we'll need to have them go after python operators too? */
//	ED_operatormacros_armature();
//	ED_operatormacros_mesh();
//	ED_operatormacros_node();
//	ED_operatormacros_object();
//	ED_operatormacros_file();
//
//	/* register dropboxes (can use macros) */
//	spacetypes = BKE_spacetypes_list();
//	for(type=spacetypes->first; type; type=type->next) {
//		if(type->dropboxes)
//			type->dropboxes();
//	}
}

/* called in wm.c */
/* keymap definitions are registered only once per WM initialize, usually on file read,
   using the keymap the actual areas/regions add the handlers */
//public static void ED_spacetypes_keymap(wmWindowManager wm)
public static void ED_spacetypes_keymap(wmKeyConfig keyconf)
{
    ListBase<SpaceType> spacetypes;
	SpaceType stype;
	ARegionType atype;

	ScreenOps.ED_keymap_screen(keyconf);
	AnimOps.ED_keymap_anim(keyconf);
//	ED_keymap_animchannels(keyconf);
	ObjectOps.ED_keymap_object(keyconf);
//	ED_keymap_mesh(keyconf);
//	ED_keymap_uvedit(keyconf);
//	ED_keymap_curve(keyconf);
//	ED_keymap_armature(keyconf);
//	ED_keymap_particle(keyconf);
//	ED_marker_keymap(keyconf);

	View2dOps.UI_view2d_keymap(keyconf);

	spacetypes = ScreenUtil.BKE_spacetypes_list();
	for(stype=spacetypes.first; stype!=null; stype=stype.next) {
		if(stype.keymap!=null)
			stype.keymap.run(keyconf);
		for(atype=stype.regiontypes.first; atype!=null; atype=atype.next) {
			if(atype.keymap!=null)
				atype.keymap.run(keyconf);
		}
	}
}

/* ********************** custom drawcall api ***************** */

/* type */
public static final int REGION_DRAW_PRE=		1;
public static final int REGION_DRAW_POST=	0;

public static class RegionDrawCB extends Link<RegionDrawCB> {
        public static interface Draw {
            public void run(GL2 gl, bContext C, ARegion ar, Object arg);
        };
	public Draw draw;
	public Object customdata;

	public int type;
};

public static Object ED_region_draw_cb_activate(ARegionType art,
								 RegionDrawCB.Draw draw,
								 Object customdata, int type)
{
	RegionDrawCB rdc= new RegionDrawCB();

	ListBaseUtil.BLI_addtail(art.drawcalls, rdc);
	rdc.draw= draw;
	rdc.customdata= customdata;
	rdc.type= type;

	return rdc;
}

public static void ED_region_draw_cb_exit(ARegionType art, Object handle)
{
	RegionDrawCB rdc;

	for(rdc= (RegionDrawCB)art.drawcalls.first; rdc!=null; rdc= rdc.next) {
		if(rdc==(RegionDrawCB)handle) {
			ListBaseUtil.BLI_remlink(art.drawcalls, rdc);
//			MEM_freeN(rdc);
			return;
		}
	}
}

public static void ED_region_draw_cb_draw(GL2 gl, bContext C, ARegion ar, int type)
{
	RegionDrawCB rdc;

	for(rdc= (RegionDrawCB)((ARegionType)ar.type).drawcalls.first; rdc!=null; rdc= rdc.next) {
		if(rdc.type==type)
			rdc.draw.run(gl, C, ar, rdc.customdata);
	}
}



///* ********************* space template *********************** */
//
///* allocate and init some vars */
//static SpaceLink *xxx_new(const bContext *C)
//{
//	return NULL;
//}
//
///* not spacelink itself */
//static void xxx_free(SpaceLink *sl)
//{
//
//}
//
///* spacetype; init callback for usage, should be redoable */
//static void xxx_init(wmWindowManager *wm, ScrArea *sa)
//{
//
//	/* link area to SpaceXXX struct */
//
//	/* define how many regions, the order and types */
//
//	/* add types to regions */
//}
//
//static SpaceLink *xxx_duplicate(SpaceLink *sl)
//{
//
//	return NULL;
//}
//
//static void xxx_operatortypes(void)
//{
//	/* register operator types for this space */
//}
//
//static void xxx_keymap(wmWindowManager *wm)
//{
//	/* add default items to keymap */
//}
//
///* only called once, from screen/spacetypes.c */
//void ED_spacetype_xxx(void)
//{
//	static SpaceType st;
//
//	st.spaceid= SPACE_VIEW3D;
//
//	st.new= xxx_new;
//	st.free= xxx_free;
//	st.init= xxx_init;
//	st.duplicate= xxx_duplicate;
//	st.operatortypes= xxx_operatortypes;
//	st.keymap= xxx_keymap;
//
//	BKE_spacetype_register(&st);
//}
//
///* ****************************** end template *********************** */
}




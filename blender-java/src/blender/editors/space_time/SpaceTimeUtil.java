/**
 * $Id: SpaceTimeUtil.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
package blender.editors.space_time;

import static blender.blenkernel.Blender.G;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.GlUtil;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.View2dUtil;
import blender.editors.uinterface.View2dUtil.View2DGrid;
import blender.editors.uinterface.View2dUtil.View2DScrollers;
import blender.makesdna.SceneTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View2dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.SpaceTime;
import blender.makesdna.sdna.View2D;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceTimeUtil {

///* ************************ main time area region *********************** */
//
///* draws a current frame indicator for the TimeLine */
//static void time_draw_cfra_time(const bContext *C, SpaceTime *stime, ARegion *ar)
//{
//	Scene *scene= CTX_data_scene(C);
//	float vec[2];
//
//	vec[0]= scene.r.cfra*scene.r.framelen;
//
//	UI_ThemeColor(TH_CFRAME);	// no theme, should be global color once...
//	glLineWidth(3.0);
//
//	glBegin(GL_LINES);
//		vec[1]= ar.v2d.cur.ymin;
//		glVertex2fv(vec);
//		vec[1]= ar.v2d.cur.ymax;
//		glVertex2fv(vec);
//	glEnd();
//
//	glLineWidth(1.0);
//}

static void time_draw_sfra_efra(GL2 gl, Scene scene, View2D v2d)
{	
	/* draw darkened area outside of active timeline 
	 * frame range used is preview range or scene range */
	Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -25);

	if (SceneTypes.PSFRA(scene) < SceneTypes.PEFRA(scene)) {
		gl.glRectf(v2d.cur.xmin, v2d.cur.ymin, (float)SceneTypes.PSFRA(scene), v2d.cur.ymax);
		gl.glRectf((float)SceneTypes.PEFRA(scene), v2d.cur.ymin, v2d.cur.xmax, v2d.cur.ymax);
	}
	else {
		gl.glRectf(v2d.cur.xmin, v2d.cur.ymin, v2d.cur.xmax, v2d.cur.ymax);
	}

	Resources.UI_ThemeColorShade(gl, Resources.TH_BACK, -60);
	/* thin lines where the actual frames are */
	GlUtil.fdrawline(gl, (float)SceneTypes.PSFRA(scene), v2d.cur.ymin, (float)SceneTypes.PSFRA(scene), v2d.cur.ymax);
	GlUtil.fdrawline(gl, (float)SceneTypes.PEFRA(scene), v2d.cur.ymin, (float)SceneTypes.PEFRA(scene), v2d.cur.ymax);
}

///* helper function - find actkeycolumn that occurs on cframe, or the nearest one if not found */
//static ActKeyColumn *time_cfra_find_ak (ActKeyColumn *ak, float cframe)
//{
//	ActKeyColumn *akn= NULL;
//
//	/* sanity checks */
//	if (ak == NULL)
//		return NULL;
//
//	/* check if this is a match, or whether it is in some subtree */
//	if (cframe < ak.cfra)
//		akn= time_cfra_find_ak(ak.left, cframe);
//	else if (cframe > ak.cfra)
//		akn= time_cfra_find_ak(ak.right, cframe);
//
//	/* if no match found (or found match), just use the current one */
//	if (akn == NULL)
//		return ak;
//	else
//		return akn;
//}

/* helper for time_draw_keyframes() */
static void time_draw_idblock_keyframes(GL2 gl, View2D v2d, ID id, short onlysel)
{
//	bDopeSheet ads= {0};
//	DLRBT_Tree keys;
//	ActKeyColumn *ak;
//	
//	/* init binarytree-list for getting keyframes */
//	BLI_dlrbTree_init(&keys);
//	
//	/* init dopesheet settings */
//	// FIXME: the ob_to_keylist function currently doesn't take this into account...
//	if (onlysel)
//		ads.filterflag |= ADS_FILTER_ONLYSEL;
//	
//	/* populate tree with keyframe nodes */
//	switch (GS(id->name)) {
//		case ID_SCE:
//			scene_to_keylist(&ads, (Scene *)id, &keys, NULL);
//			break;
//		case ID_OB:
//			ob_to_keylist(&ads, (Object *)id, &keys, NULL);
//			break;
//	}
//		
//	/* build linked-list for searching */
//	BLI_dlrbTree_linkedlist_sync(&keys);
//	
//	/* start drawing keyframes 
//	 *	- we use the binary-search capabilities of the tree to only start from 
//	 *	  the first visible keyframe (last one can then be easily checked)
//	 *	- draw within a single GL block to be faster
//	 */
//	glBegin(GL_LINES);
//		for ( ak=time_cfra_find_ak(keys.root, v2d->cur.xmin); 
//			 (ak) && (ak->cfra <= v2d->cur.xmax); 
//			  ak=ak->next ) 
//		{
//			glVertex2f(ak->cfra, v2d->cur.ymin);
//			glVertex2f(ak->cfra, v2d->cur.ymax);
//		}
//	glEnd(); // GL_LINES
//		
//	/* free temp stuff */
//	BLI_dlrbTree_free(&keys);
}

/* draw keyframe lines for timeline */
static void time_draw_keyframes(GL2 gl, bContext C, SpaceTime stime, ARegion ar)
{
	Scene scene= bContext.CTX_data_scene(C);
	bObject ob= bContext.CTX_data_active_object(C);
	View2D v2d= ar.v2d;
	short onlysel= (short)(stime.flag & SpaceTypes.TIME_ONLYACTSEL);
	
	/* draw scene keyframes first 
	 *	- don't try to do this when only drawing active/selected data keyframes,
	 *	  since this can become quite slow
	 */
	if (scene!=null && onlysel==0) {
		/* set draw color */
		gl.glColor3ub((byte)0xDD, (byte)0xA7, (byte)0x00);
		time_draw_idblock_keyframes(gl, v2d, (ID)scene, onlysel);
	}
	
	/* draw keyframes from selected objects 
	 *	- only do the active object if in posemode (i.e. showing only keyframes for the bones)
	 *	  OR the onlysel flag was set, which means that only active object's keyframes should
	 *	  be considered
	 */
	gl.glColor3ub((byte)0xDD, (byte)0xD7, (byte)0x00);
	
//	if (ob!=null && ((ob.mode == ObjectTypes.OB_MODE_POSE) || onlysel!=0)) {
//		/* draw keyframes for active object only */
//		time_draw_idblock_keyframes(v2d, (ID)ob, onlysel);
//	}
//	else {
		short active_done = 0;
		
//		/* draw keyframes from all selected objects */
//		CTX_DATA_BEGIN(C, Object*, obsel, selected_objects) 
//		{
//			/* last arg is 0, since onlysel doesn't apply here... */
//			time_draw_idblock_keyframes(v2d, (ID)obsel, 0);
//			
//			/* if this object is the active one, set flag so that we don't draw again */
//			if (obsel == ob)
//				active_done= 1;
//		}
//		CTX_DATA_END;
		
		/* if active object hasn't been done yet, draw it... */
		if (ob!=null && (active_done == 0))
			time_draw_idblock_keyframes(gl, v2d, (ID)ob, (short)0);
//	}
}

/* ---------------- */

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init time_main_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	wmKeyMap keymap;

	View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_CUSTOM, ar.winx, ar.winy);

	/* own keymap */
	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "TimeLine", SpaceTypes.SPACE_TIME, 0);
	WmEventSystem.WM_event_add_keymap_handler_bb(ar.handlers, keymap, ar.v2d.mask, ar.winrct);
}};

public static ARegionType.Draw time_main_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	/* draw entirely, view changes should be handled here */
	Scene scene= bContext.CTX_data_scene(C);
	SpaceTime stime= bContext.CTX_wm_space_time(C);
	View2D v2d= ar.v2d;
	View2DGrid grid;
	View2DScrollers scrollers;
	int unit, flag=0;

	/* clear and setup matrix */
	Resources.UI_ThemeClearColor(gl, Resources.TH_BACK);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

	View2dUtil.UI_view2d_view_ortho(gl, v2d);

	/* start and end frame */
	time_draw_sfra_efra(gl, scene, v2d);

	/* grid */
	unit= (stime.flag & SpaceTypes.TIME_DRAWFRAMES)!=0? View2dUtil.V2D_UNIT_FRAMES: View2dUtil.V2D_UNIT_SECONDS;
	grid= View2dUtil.UI_view2d_grid_calc(C, v2d, unit, View2dUtil.V2D_GRID_CLAMP, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY, ar.winx, ar.winy);
	View2dUtil.UI_view2d_grid_draw(gl, C, v2d, grid, (View2dUtil.V2D_VERTICAL_LINES|View2dUtil.V2D_VERTICAL_AXIS));
	View2dUtil.UI_view2d_grid_free(grid);

	/* keyframes */
	if(G.rendering==0) /* ANIM_nla_mapping_apply_fcurve() modifies curve data while rendering, possible race condition */
		time_draw_keyframes(gl, C, stime, ar);

	/* current frame */
//	if ((stime.flag & SpaceTypes.TIME_DRAWFRAMES)==0) 	flag |= DRAWCFRA_UNIT_SECONDS;
//	if ((stime.flag & SpaceTypes.TIME_CFRA_NUM)!=0) 			flag |= DRAWCFRA_SHOW_NUMBOX;
//	ANIM_draw_cfra(C, v2d, flag);

	/* markers */
//	View2dUtil.UI_view2d_view_orthoSpecial(C, v2d, 1);
//	draw_markers_time(C, 0);

	/* reset view matrix */
	View2dUtil.UI_view2d_view_restore(gl, C);

	/* scrollers */
	scrollers= View2dUtil.UI_view2d_scrollers_calc(C, v2d, unit, View2dUtil.V2D_GRID_CLAMP, View2dUtil.V2D_ARG_DUMMY, View2dUtil.V2D_ARG_DUMMY);
	View2dUtil.UI_view2d_scrollers_draw(gl, C, v2d, scrollers);
	View2dUtil.UI_view2d_scrollers_free(scrollers);
}};

public static ARegionType.Listener time_main_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
//static void time_main_area_listener(ARegion *ar, wmNotifier *wmn)
{
	/* context changes */
	switch(wmn.category) {

		case WmTypes.NC_SCENE:
			/* any scene change for now */
			Area.ED_region_tag_redraw(ar);
			break;
	}
}};

/* ************************ header time area region *********************** */

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init time_header_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	Area.ED_region_header_init(ar);
}};

public static ARegionType.Draw time_header_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	Area.ED_region_header(gl, C, ar);
}};

public static ARegionType.Listener time_header_area_listener = new ARegionType.Listener() {
public void run(ARegion ar, wmNotifier wmn)
{
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCREEN:
			if(wmn.data==WmTypes.ND_ANIMPLAY)
				Area.ED_region_tag_redraw(ar);
			break;
			
		case WmTypes.NC_SCENE:
			switch (wmn.data) {
				case WmTypes.ND_OB_SELECT:
				case WmTypes.ND_FRAME:
				case WmTypes.ND_FRAME_RANGE:
				case WmTypes.ND_KEYINGSET:
				case WmTypes.ND_RENDER_OPTIONS:
					Area.ED_region_tag_redraw(ar);
				break;
			}
			
		case WmTypes.NC_SPACE:
			if(wmn.data == WmTypes.ND_SPACE_TIME)
				Area.ED_region_tag_redraw(ar);
			break;
	}
}};

/* ******************** default callbacks for time space ***************** */

public static SpaceType.New time_new = new SpaceType.New() {
public SpaceLink run(bContext C)
{
	Scene scene= bContext.CTX_data_scene(C);
	ARegion ar;
	SpaceTime stime;

	stime= new SpaceTime();

	stime.spacetype= SpaceTypes.SPACE_TIME;
	//stime.redraws= SpaceTypes.TIME_ALL_3D_WIN|SpaceTypes.TIME_ALL_ANIM_WIN;
	stime.flag |= SpaceTypes.TIME_DRAWFRAMES;

	/* header */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(stime.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
	ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;

	/* main area */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(stime.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;

	ar.v2d.tot.xmin= (float)(scene.r.sfra - 4);
	ar.v2d.tot.ymin= 0.0f;
	ar.v2d.tot.xmax= (float)(scene.r.efra + 4);
	ar.v2d.tot.ymax= 50.0f;

	ar.v2d.cur= ar.v2d.tot;

	ar.v2d.min[0]= 1.0f;
	ar.v2d.min[1]= 50.0f;

	ar.v2d.max[0]= SceneTypes.MAXFRAMEF;
	ar.v2d.max[1]= 50.0f;

	ar.v2d.minzoom= 0.1f;
	ar.v2d.maxzoom= 10.0f;

	ar.v2d.scroll |= (View2dTypes.V2D_SCROLL_BOTTOM|View2dTypes.V2D_SCROLL_SCALE_HORIZONTAL);
	ar.v2d.align |= View2dTypes.V2D_ALIGN_NO_NEG_Y;
	ar.v2d.keepofs |= View2dTypes.V2D_LOCKOFS_Y;
	ar.v2d.keepzoom |= View2dTypes.V2D_LOCKZOOM_Y;


	return (SpaceLink)stime;
}};

/* not spacelink itself */
public static SpaceType.Free time_free = new SpaceType.Free() {
public void run(SpaceLink sl)
{
//	SpaceTime stime= (SpaceTime)sl;
//	
//	time_cache_free(stime);
}};


/* spacetype; init callback in ED_area_initialize() */
/* init is called to (re)initialize an existing editor (file read, screen changes) */
/* validate spacedata, add own area level handlers */
public static SpaceType.Init time_init = new SpaceType.Init() {
public void run(wmWindowManager wm, ScrArea sa)
{
//	SpaceTime stime= (SpaceTime)sa.spacedata.first;
//	
//	time_cache_free(stime);
//	
//	/* enable all cache display */
//	stime.cache_display |= SpaceTypes.TIME_CACHE_DISPLAY;
//	stime.cache_display |= (SpaceTypes.TIME_CACHE_SOFTBODY|SpaceTypes.TIME_CACHE_PARTICLES);
//	stime.cache_display |= (SpaceTypes.TIME_CACHE_CLOTH|SpaceTypes.TIME_CACHE_SMOKE);
}};

public static SpaceType.Duplicate time_duplicate = new SpaceType.Duplicate() {
public SpaceLink run(SpaceLink sl)
{
	SpaceTime stime= (SpaceTime)sl;
	SpaceTime stimen= stime.copy();
	
//	time_cache_free(stimen);

	return (SpaceLink)stimen;
}};

/* only called once, from space_api/spacetypes.c */
/* it defines all callbacks to maintain spaces */
public static void ED_spacetype_time()
{
	SpaceType st= new SpaceType();
	ARegionType art;

	st.spaceid= SpaceTypes.SPACE_TIME;
	StringUtil.strncpy(st.name,0, StringUtil.toCString("Timeline"),0, ScreenUtil.BKE_ST_MAXNAME);

	st.newInstance= time_new;
	st.free= time_free;
	st.init= time_init;
	st.duplicate= time_duplicate;
	st.operatortypes= TimeOps.time_operatortypes;
	st.keymap= null;
//	st.listener= time_listener;
//	st.refresh= time_refresh;

	/* regions: main window */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
	art.keymapflag= Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_MARKERS|Area.ED_KEYMAP_ANIMATION|Area.ED_KEYMAP_FRAMES;

	art.init= time_main_area_init;
	art.draw= time_main_area_draw;
	art.listener= time_main_area_listener;
	art.keymap= TimeOps.time_keymap;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	/* regions: header */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_HEADER;
	art.minsizey= ScreenTypes.HEADERY;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;

	art.init= time_header_area_init;
	art.draw= time_header_area_draw;
	art.listener= time_header_area_listener;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	ScreenUtil.BKE_spacetype_register(st);
}

}

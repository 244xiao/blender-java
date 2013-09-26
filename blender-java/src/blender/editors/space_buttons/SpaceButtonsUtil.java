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
package blender.editors.space_buttons;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenEdit;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.View2dUtil;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceButs;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceButtonsUtil {

/* buts->scaflag */
public static final int BUTS_SENS_SEL=		1;
public static final int BUTS_SENS_ACT=		2;
public static final int BUTS_SENS_LINK=		4;
public static final int BUTS_CONT_SEL=		8;
public static final int BUTS_CONT_ACT=		16;
public static final int BUTS_CONT_LINK=		32;
public static final int BUTS_ACT_SEL=		64;
public static final int BUTS_ACT_ACT=		128;
public static final int BUTS_ACT_LINK=		256;
public static final int BUTS_SENS_STATE=		512;
public static final int BUTS_ACT_STATE=		1024;

public static final float BUTS_HEADERY=		(ScreenTypes.HEADERY*1.2f);
public static final float BUTS_UI_UNIT=		(UI.UI_UNIT_Y*1.2f);


/* ******************** default callbacks for buttons space ***************** */

public static SpaceType.New buttons_new = new SpaceType.New() {
public SpaceLink run(bContext C)
{
	ARegion ar;
	SpaceButs sbuts;

	sbuts= new SpaceButs();
	sbuts.spacetype= SpaceTypes.SPACE_BUTS;
	sbuts.align= SpaceTypes.BUT_AUTO;

	/* header */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(sbuts.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
	ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;

	/* main area */
	ar= new ARegion();

	ListBaseUtil.BLI_addtail(sbuts.regionbase, ar);
	ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;

	return (SpaceLink)sbuts;
}};

///* not spacelink itself */
public static SpaceType.Free buttons_free = new SpaceType.Free() {
public void run(SpaceLink sl)
{
	SpaceButs sbuts= (SpaceButs) sl;

	//if(sbuts.ri!=null) {
//		if (sbuts.ri.rect!=null)
//                    MEM_freeN(sbuts.ri.rect);
//		MEM_freeN(sbuts.ri);
	//}

//	if(sbuts.path!=null)
//		MEM_freeN(sbuts.path);
}};

/* spacetype; init callback */
public static SpaceType.Init buttons_init = new SpaceType.Init() {
public void run(wmWindowManager wm, ScrArea sa)
{
	SpaceButs sbuts= (SpaceButs)sa.spacedata.first;

	/* auto-align based on size */
	if(sbuts.align == SpaceTypes.BUT_AUTO || sbuts.align==0) {
		if(sa.winx > sa.winy)
			sbuts.align= SpaceTypes.BUT_HORIZONTAL;
		else
			sbuts.align= SpaceTypes.BUT_VERTICAL;
	}
}};

public static SpaceType.Duplicate buttons_duplicate = new SpaceType.Duplicate() {
public SpaceLink run(SpaceLink sl)
{
	SpaceButs sbutsn= ((SpaceButs)sl).copy();

	/* clear or remove stuff from old */
	//sbutsn.ri= null;
	sbutsn.path= null;

	return (SpaceLink)sbutsn;
}};

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init buttons_main_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	wmKeyMap keymap;

	Area.ED_region_panels_init(wm, ar);

	keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Buttons Generic", SpaceTypes.SPACE_BUTS, 0);
	WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
}};

public static ARegionType.Draw buttons_main_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	/* draw entirely, view changes should be handled here */
	SpaceButs sbuts= bContext.CTX_wm_space_buts(C);
	boolean vertical= (sbuts.align == SpaceTypes.BUT_VERTICAL);

	ButtonsContext.buttons_context_compute(C, sbuts);

	if(sbuts.mainb == SpaceTypes.BCONTEXT_SCENE)
		Area.ED_region_panels(gl, C, ar, vertical, "scene", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_RENDER)
		Area.ED_region_panels(gl, C, ar, vertical, "render", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_WORLD)
		Area.ED_region_panels(gl, C, ar, vertical, "world", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_OBJECT)
		Area.ED_region_panels(gl, C, ar, vertical, "object", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_DATA)
		Area.ED_region_panels(gl, C, ar, vertical, "data", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_MATERIAL)
		Area.ED_region_panels(gl, C, ar, vertical, "material", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_TEXTURE)
		Area.ED_region_panels(gl, C, ar, vertical, "texture", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_PARTICLE)
		Area.ED_region_panels(gl, C, ar, vertical, "particle", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_PHYSICS)
		Area.ED_region_panels(gl, C, ar, vertical, "physics", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_BONE)
		Area.ED_region_panels(gl, C, ar, vertical, "bone", sbuts.mainb);
	else if(sbuts.mainb == SpaceTypes.BCONTEXT_MODIFIER)
		Area.ED_region_panels(gl, C, ar, vertical, "modifier", sbuts.mainb);
	else if (sbuts.mainb == SpaceTypes.BCONTEXT_CONSTRAINT)
		Area.ED_region_panels(gl, C, ar, vertical, "constraint", sbuts.mainb);
	else if (sbuts.mainb == SpaceTypes.BCONTEXT_BONE_CONSTRAINT)
		Area.ED_region_panels(gl, C, ar, vertical, "bone_constraint", sbuts.mainb);

    sbuts.re_align= 0;
	sbuts.mainbo= sbuts.mainb;
}};

public static SpaceType.OperatorTypes buttons_operatortypes = new SpaceType.OperatorTypes() {
public void run()
{
//	WM_operatortype_append(BUTTONS_OT_toolbox);
//	WM_operatortype_append(BUTTONS_OT_file_browse);
}};

public static SpaceType.KeyMap buttons_keymap = new SpaceType.KeyMap() {
public void run(wmKeyConfig keyconf)
{
	wmKeyMap keymap= WmKeymap.WM_keymap_find(keyconf, "Buttons Generic", SpaceTypes.SPACE_BUTS, 0);

	WmKeymap.WM_keymap_add_item(keymap, "BUTTONS_OT_toolbox", WmEventTypes.RIGHTMOUSE, WmTypes.KM_PRESS, 0, 0);
}};

/* add handlers, stuff you only do once or on area/region changes */
public static ARegionType.Init buttons_header_area_init = new ARegionType.Init() {
public void run(wmWindowManager wm, ARegion ar)
{
	View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_HEADER, ar.winx, ar.winy);
}};

public static ARegionType.Draw buttons_header_area_draw = new ARegionType.Draw() {
public void run(GL2 gl, bContext C, ARegion ar)
{
	/* clear */
	Resources.UI_ThemeClearColor(gl, ScreenEdit.ED_screen_area_active(C)?Resources.TH_HEADER:Resources.TH_HEADERDESEL);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

	/* set view2d view matrix for scrolling (without scrollers) */
	View2dUtil.UI_view2d_view_ortho(gl, ar.v2d);

	ButtonsHeader.buttons_header_buttons(gl, C, ar);

	/* restore view matrix? */
	View2dUtil.UI_view2d_view_restore(gl, C);
}};

//#if 0
///* add handlers, stuff you only do once or on area/region changes */
//static void buttons_context_area_init(wmWindowManager *wm, ARegion *ar)
//{
//	UI_view2d_region_reinit(&ar.v2d, V2D_COMMONVIEW_HEADER, ar.winx, ar.winy);
//}
//
//#define CONTEXTY	30
//
//static void buttons_context_area_draw(const bContext *C, ARegion *ar)
//{
//	SpaceButs *sbuts= CTX_wm_space_buts(C);
//	uiStyle *style= U.uistyles.first;
//	uiBlock *block;
//	uiLayout *layout;
//	View2D *v2d= &ar.v2d;
//	float col[3];
//	int x, y, w, h;
//
//	buttons_context_compute(C, sbuts);
//
//	w= v2d.cur.xmax - v2d.cur.xmin;
//	h= v2d.cur.ymax - v2d.cur.ymin;
//	UI_view2d_view_ortho(C, v2d);
//
//	/* create UI */
//	block= uiBeginBlock(C, ar, "buttons_context", UI_EMBOSS);
//	layout= uiBlockLayout(block, UI_LAYOUT_HORIZONTAL, UI_LAYOUT_PANEL,
//		style.panelspace, h - (h-UI_UNIT_Y)/2, w, 20, style);
//
//	buttons_context_draw(C, layout);
//
//	uiBlockLayoutResolve(C, block, &x, &y);
//	uiEndBlock(C, block);
//
//	/* draw */
//	UI_SetTheme(SPACE_BUTS, RGN_TYPE_WINDOW); /* XXX */
//
//	UI_GetThemeColor3fv(TH_BACK, col);
//	glClearColor(col[0], col[1], col[2], 0.0);
//	glClear(GL_COLOR_BUFFER_BIT);
//
//	UI_view2d_totRect_set(v2d, x, -y);
//	UI_view2d_view_ortho(C, v2d);
//
//	uiDrawBlock(C, block);
//
//	/* restore view matrix */
//	UI_view2d_view_restore(C);
//}
//#endif

/* draw a certain button set only if properties area is currently
 * showing that button set, to reduce unnecessary drawing. */
public static void buttons_area_redraw(ScrArea sa, int buttons)
{
	SpaceButs sbuts= (SpaceButs)sa.spacedata.first;
	
	/* if the area's current button set is equal to the one to redraw */
	if((sbuts.mainb&0xFFFF) == buttons)
		Area.ED_area_tag_redraw(sa);
}

/* reused! */
public static SpaceType.Listener buttons_area_listener = new SpaceType.Listener() {
public void run(ScrArea sa, wmNotifier wmn)
{
	SpaceButs sbuts= (SpaceButs)sa.spacedata.first;

//	/* context changes */
//	switch(wmn.category) {
//		case WmTypes.NC_SCENE:
//			switch(wmn.data) {
//				case WmTypes.ND_FRAME:
//				case WmTypes.ND_MODE:
//				case WmTypes.ND_RENDER_OPTIONS:
//					Area.ED_area_tag_redraw(sa);
//					break;
//
//				case WmTypes.ND_OB_ACTIVE:
//					Area.ED_area_tag_redraw(sa);
//					sbuts.preview= 1;
//					break;
//			}
//			break;
//		case WmTypes.NC_OBJECT:
//			switch(wmn.data) {
//				case WmTypes.ND_TRANSFORM:
//				case WmTypes.ND_BONE_ACTIVE:
//				case WmTypes.ND_BONE_SELECT:
//				case WmTypes.ND_GEOM_SELECT:
//				case WmTypes.ND_CONSTRAINT:
//					Area.ED_area_tag_redraw(sa);
//					break;
//				case WmTypes.ND_SHADING:
//				case WmTypes.ND_SHADING_DRAW:
//					/* currently works by redraws... if preview is set, it (re)starts job */
//					sbuts.preview= 1;
//					break;
//			}
//			break;
//		case WmTypes.NC_MATERIAL:
//			Area.ED_area_tag_redraw(sa);
//
//			switch(wmn.data) {
//				case WmTypes.ND_SHADING:
//				case WmTypes.ND_SHADING_DRAW:
//					/* currently works by redraws... if preview is set, it (re)starts job */
//					sbuts.preview= 1;
//					break;
//			}
//			break;
//		case WmTypes.NC_WORLD:
//			Area.ED_area_tag_redraw(sa);
//			sbuts.preview= 1;
//		case WmTypes.NC_LAMP:
//			Area.ED_area_tag_redraw(sa);
//			sbuts.preview= 1;
//		case WmTypes.NC_TEXTURE:
//			Area.ED_area_tag_redraw(sa);
//			sbuts.preview= 1;
//	}
//
//	if(wmn.data == WmTypes.ND_KEYS)
//		Area.ED_area_tag_redraw(sa);
	
	/* context changes */
	switch(wmn.category) {
		case WmTypes.NC_SCENE:
			switch(wmn.data) {
				case WmTypes.ND_RENDER_OPTIONS:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_RENDER);
					break;
				case WmTypes.ND_FRAME:
					/* any buttons area can have animated properties so redraw all */
					Area.ED_area_tag_redraw(sa);
					sbuts.preview= 1;
					break;
				case WmTypes.ND_OB_ACTIVE:
					Area.ED_area_tag_redraw(sa);
					sbuts.preview= 1;
					break;
				case WmTypes.ND_KEYINGSET:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_SCENE);
					break;
				case WmTypes.ND_RENDER_RESULT:
					break;
				case WmTypes.ND_MODE:
				case WmTypes.ND_LAYER:
				default:
					Area.ED_area_tag_redraw(sa);
					break;
			}
			break;
		case WmTypes.NC_OBJECT:
			switch(wmn.data) {
				case WmTypes.ND_TRANSFORM:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_OBJECT);
					break;
				case WmTypes.ND_POSE:
				case WmTypes.ND_BONE_ACTIVE:
				case WmTypes.ND_BONE_SELECT:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_BONE);
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_BONE_CONSTRAINT);
					break;
				case WmTypes.ND_MODIFIER:
					if(wmn.action == WmTypes.NA_RENAME)
						Area.ED_area_tag_redraw(sa);
					else
						buttons_area_redraw(sa, SpaceTypes.BCONTEXT_MODIFIER);
						buttons_area_redraw(sa, SpaceTypes.BCONTEXT_PHYSICS);
					break;
//				case WmTypes.ND_GEOM_SELECT:
				case WmTypes.ND_CONSTRAINT:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_CONSTRAINT);
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_BONE_CONSTRAINT);
					break;
				case WmTypes.ND_PARTICLE:
					if (wmn.action == WmTypes.NA_EDITED)
						buttons_area_redraw(sa, SpaceTypes.BCONTEXT_PARTICLE);
					break;
				case WmTypes.ND_DRAW:
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_OBJECT);
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_DATA);
					buttons_area_redraw(sa, SpaceTypes.BCONTEXT_PHYSICS);
				case WmTypes.ND_SHADING:
				case WmTypes.ND_SHADING_DRAW:
					/* currently works by redraws... if preview is set, it (re)starts job */
					sbuts.preview= 1;
					break;
				default:
					/* Not all object RNA props have a ND_ notifier (yet) */
					Area.ED_area_tag_redraw(sa);
					break;
			}
			break;
		case WmTypes.NC_GEOM:
			switch(wmn.data) {
				case WmTypes.ND_SELECT:
				case WmTypes.ND_DATA:
					Area.ED_area_tag_redraw(sa);
					break;
			}
			break;
		case WmTypes.NC_MATERIAL:
			Area.ED_area_tag_redraw(sa);
			switch(wmn.data) {
				case WmTypes.ND_SHADING:
				case WmTypes.ND_SHADING_DRAW:
				case WmTypes.ND_NODES:
					/* currently works by redraws... if preview is set, it (re)starts job */
					sbuts.preview= 1;
					break;
			}
			break;
		case WmTypes.NC_WORLD:
			buttons_area_redraw(sa, SpaceTypes.BCONTEXT_WORLD);
			sbuts.preview= 1;
			break;
		case WmTypes.NC_LAMP:
			buttons_area_redraw(sa, SpaceTypes.BCONTEXT_DATA);
			sbuts.preview= 1;
			break;
		case WmTypes.NC_BRUSH:
			buttons_area_redraw(sa, SpaceTypes.BCONTEXT_TEXTURE);
			break;
		case WmTypes.NC_TEXTURE:
		case WmTypes.NC_IMAGE:
			Area.ED_area_tag_redraw(sa);
			sbuts.preview= 1;
			break;
		case WmTypes.NC_SPACE:
			if(wmn.data == WmTypes.ND_SPACE_PROPERTIES)
				Area.ED_area_tag_redraw(sa);
			break;
		case WmTypes.NC_ID:
			if(wmn.action == WmTypes.NA_RENAME)
				Area.ED_area_tag_redraw(sa);
			break;
		case WmTypes.NC_ANIMATION:
			switch(wmn.data) {
				case WmTypes.ND_KEYFRAME:
					if (wmn.action == WmTypes.NA_EDITED)
						Area.ED_area_tag_redraw(sa);
					break;
			}
			break;
		/* Listener for preview render, when doing an global undo. */
		case WmTypes.NC_WINDOW:
			Area.ED_area_tag_redraw(sa);
			sbuts.preview= 1;
			break;
	}

	if(wmn.data == WmTypes.ND_KEYS)
		Area.ED_area_tag_redraw(sa);
}};

/* only called once, from space/spacetypes.c */
public static void ED_spacetype_buttons()
{
	SpaceType st= new SpaceType();
	ARegionType art;
	
	st.spaceid= SpaceTypes.SPACE_BUTS;
	StringUtil.strncpy(st.name,0, StringUtil.toCString("Buttons"),0, ScreenUtil.BKE_ST_MAXNAME);
	
	st.newInstance= buttons_new;
	st.free= buttons_free;
	st.init= buttons_init;
	st.duplicate= buttons_duplicate;
	st.operatortypes= buttons_operatortypes;
	st.keymap= buttons_keymap;
	st.listener= buttons_area_listener;
	st.context= ButtonsContext.buttons_context;
	
	/* regions: main window */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
	art.init= buttons_main_area_init;
	art.draw= buttons_main_area_draw;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_FRAMES;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);

	ButtonsContext.buttons_context_register(art);
	
	/* regions: header */
	art= new ARegionType();
	art.regionid = ScreenTypes.RGN_TYPE_HEADER;
	art.minsizey= (int)BUTS_HEADERY;
	art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;
	
	art.init= buttons_header_area_init;
	art.draw= buttons_header_area_draw;
	ListBaseUtil.BLI_addhead(st.regiontypes, art);
	
	ScreenUtil.BKE_spacetype_register(st);
}

}

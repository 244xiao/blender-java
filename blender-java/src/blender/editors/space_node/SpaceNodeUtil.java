package blender.editors.space_node;

import javax.media.opengl.GL2;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.uinterface.View2dUtil;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View2dTypes;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.SpaceNode;
import blender.makesdna.sdna.SpaceTime;
import blender.makesdna.sdna.View2D;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmWindowManager;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmTypes.wmNotifier;

public class SpaceNodeUtil {
//	/**
//	 * $Id: space_node.c 34756 2011-02-10 10:24:05Z aligorith $
//	 *
//	 * ***** BEGIN GPL LICENSE BLOCK *****
//	 *
//	 * This program is free software; you can redistribute it and/or
//	 * modify it under the terms of the GNU General Public License
//	 * as published by the Free Software Foundation; either version 2
//	 * of the License, or (at your option) any later version. 
//	 *
//	 * This program is distributed in the hope that it will be useful,
//	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
//	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	 * GNU General Public License for more details.
//	 *
//	 * You should have received a copy of the GNU General Public License
//	 * along with this program; if not, write to the Free Software Foundation,
//	 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
//	 *
//	 * The Original Code is Copyright (C) 2008 Blender Foundation.
//	 * All rights reserved.
//	 *
//	 * 
//	 * Contributor(s): Blender Foundation
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <string.h>
//	#include <stdio.h>
//
//	#include "DNA_node_types.h"
//	#include "DNA_object_types.h"
//	#include "DNA_material_types.h"
//	#include "DNA_scene_types.h"
//
//	#include "MEM_guardedalloc.h"
//
//	#include "BLI_blenlib.h"
//	#include "BLI_math.h"
//	#include "BLI_rand.h"
//	#include "BLI_utildefines.h"
//
//	#include "BKE_context.h"
//	#include "BKE_screen.h"
//	#include "BKE_node.h"
//
//	#include "ED_render.h"
//	#include "ED_screen.h"
//
//
//	#include "WM_api.h"
//	#include "WM_types.h"
//
//	#include "UI_resources.h"
//	#include "UI_view2d.h"
//
//	#include "RNA_access.h"
//
//	#include "node_intern.h"	// own include
//
//	/* ******************** manage regions ********************* */
//
//	ARegion *node_has_buttons_region(ScrArea *sa)
//	{
//		ARegion *ar, *arnew;
//		
//		for(ar= sa->regionbase.first; ar; ar= ar->next)
//			if(ar->regiontype==RGN_TYPE_UI)
//				return ar;
//		
//		/* add subdiv level; after header */
//		for(ar= sa->regionbase.first; ar; ar= ar->next)
//			if(ar->regiontype==RGN_TYPE_HEADER)
//				break;
//		
//		/* is error! */
//		if(ar==NULL) return NULL;
//		
//		arnew= MEM_callocN(sizeof(ARegion), "buttons for node");
//		
//		BLI_insertlinkafter(&sa->regionbase, ar, arnew);
//		arnew->regiontype= RGN_TYPE_UI;
//		arnew->alignment= RGN_ALIGN_RIGHT;
//		
//		arnew->flag = RGN_FLAG_HIDDEN;
//		
//		return arnew;
//	}

	/* ******************** default callbacks for node space ***************** */

	public static SpaceType.New node_new = new SpaceType.New() {
	public SpaceLink run(bContext C)
	{
		ARegion ar;
		SpaceNode snode;
		
		snode= new SpaceNode();
		snode.spacetype= SpaceTypes.SPACE_NODE;	
		
		/* backdrop */
		snode.zoom = 1.0f;
		
		/* header */
		ar= new ARegion();
		
		ListBaseUtil.BLI_addtail(snode.regionbase, ar);
		ar.regiontype= ScreenTypes.RGN_TYPE_HEADER;
		ar.alignment= ScreenTypes.RGN_ALIGN_BOTTOM;
		
		/* buttons/list view */
		ar= new ARegion();
		
		ListBaseUtil.BLI_addtail(snode.regionbase, ar);
		ar.regiontype= ScreenTypes.RGN_TYPE_UI;
		ar.alignment= ScreenTypes.RGN_ALIGN_RIGHT;
		ar.flag = ScreenTypes.RGN_FLAG_HIDDEN;
		
		/* main area */
		ar= new ARegion();
		
		ListBaseUtil.BLI_addtail(snode.regionbase, ar);
		ar.regiontype= ScreenTypes.RGN_TYPE_WINDOW;
		
		ar.v2d.tot.xmin=  -256.0f;
		ar.v2d.tot.ymin=  -256.0f;
		ar.v2d.tot.xmax= 768.0f;
		ar.v2d.tot.ymax= 768.0f;
		
		ar.v2d.cur.xmin=  -256.0f;
		ar.v2d.cur.ymin=  -256.0f;
		ar.v2d.cur.xmax= 768.0f;
		ar.v2d.cur.ymax= 768.0f;
		
		ar.v2d.min[0]= 1.0f;
		ar.v2d.min[1]= 1.0f;
		
		ar.v2d.max[0]= 32000.0f;
		ar.v2d.max[1]= 32000.0f;
		
		ar.v2d.minzoom= 0.09f;
		ar.v2d.maxzoom= 2.31f;
		
		ar.v2d.scroll= (View2dTypes.V2D_SCROLL_RIGHT|View2dTypes.V2D_SCROLL_BOTTOM);
		ar.v2d.keepzoom= View2dTypes.V2D_LIMITZOOM|View2dTypes.V2D_KEEPASPECT;
		ar.v2d.keeptot= 0;
		
		return (SpaceLink)snode;
	}};

	/* not spacelink itself */
	public static SpaceType.Free node_free = new SpaceType.Free() {
	public void run(SpaceLink sl)
	{	
		
	}};


	/* spacetype; init callback */
	public static SpaceType.Init node_init = new SpaceType.Init() {
	public void run(wmWindowManager wm, ScrArea sa)
	{

	}};

//	static void node_area_listener(ScrArea *sa, wmNotifier *wmn)
//	{
//		/* note, ED_area_tag_refresh will re-execute compositor */
//		SpaceNode *snode= sa->spacedata.first;
//		int type= snode->treetype;
//		
//		/* preview renders */
//		switch(wmn->category) {
//			case NC_SCENE:
//				switch (wmn->data) {
//					case ND_NODES:
//					case ND_FRAME:
//						ED_area_tag_refresh(sa);
//						break;
//					case ND_TRANSFORM_DONE:
//						if(type==NTREE_COMPOSIT) {
//							if(snode->flag & SNODE_AUTO_RENDER) {
//								snode->recalc= 1;
//								ED_area_tag_refresh(sa);
//							}
//						}
//						break;
//				}
//				break;
//			case NC_WM:
//				if(wmn->data==ND_FILEREAD)
//					ED_area_tag_refresh(sa);
//				break;
//			
//			/* future: add ID checks? */
//			case NC_MATERIAL:
//				if(type==NTREE_SHADER) {
//					if(wmn->data==ND_SHADING)
//						ED_area_tag_refresh(sa);
//					else if(wmn->data==ND_SHADING_DRAW)
//						ED_area_tag_refresh(sa);
//				}
//				break;
//			case NC_TEXTURE:
//				if(type==NTREE_SHADER || type==NTREE_TEXTURE) {
//					if(wmn->data==ND_NODES)
//						ED_area_tag_refresh(sa);
//				}
//				break;
//			case NC_TEXT:
//				/* pynodes */
//				if(wmn->data==ND_SHADING)
//					ED_area_tag_refresh(sa);
//				break;
//			case NC_SPACE:
//				if(wmn->data==ND_SPACE_NODE)
//					ED_area_tag_refresh(sa);
//				else if(wmn->data==ND_SPACE_NODE_VIEW)
//					ED_area_tag_redraw(sa);
//				break;
//			case NC_NODE:
//				if (wmn->action == NA_EDITED)
//					ED_area_tag_refresh(sa);
//				break;
//
//			case NC_IMAGE:
//				if (wmn->action == NA_EDITED) {
//					if(type==NTREE_COMPOSIT) {
//						Scene *scene= wmn->window->screen->scene;
//						
//						/* note that NodeTagIDChanged is alredy called by BKE_image_signal() on all
//						 * scenes so really this is just to know if the images is used in the compo else
//						 * painting on images could become very slow when the compositor is open. */
//						if(NodeTagIDChanged(scene->nodetree, wmn->reference))
//							ED_area_tag_refresh(sa);
//					}
//				}
//				break;
//		}
//	}
//
//	static void node_area_refresh(const struct bContext *C, struct ScrArea *sa)
//	{
//		/* default now: refresh node is starting preview */
//		SpaceNode *snode= sa->spacedata.first;
//
//		snode_set_context(snode, CTX_data_scene(C));
//		
//		if(snode->nodetree) {
//			if(snode->treetype==NTREE_SHADER) {
//				Material *ma= (Material *)snode->id;
//				if(ma->use_nodes)
//					ED_preview_shader_job(C, sa, snode->id, NULL, NULL, 100, 100, PR_NODE_RENDER);
//			}
//			else if(snode->treetype==NTREE_COMPOSIT) {
//				Scene *scene= (Scene *)snode->id;
//				if(scene->use_nodes) {
//					/* recalc is set on 3d view changes for auto compo */
//					if(snode->recalc) {
//						snode->recalc= 0;
//						node_render_changed_exec((struct bContext*)C, NULL);
//					}
//					else 
//						snode_composite_job(C, sa);
//				}
//			}
//			else if(snode->treetype==NTREE_TEXTURE) {
//				Tex *tex= (Tex *)snode->id;
//				if(tex->use_nodes) {
//					ED_preview_shader_job(C, sa, snode->id, NULL, NULL, 100, 100, PR_NODE_RENDER);
//				}
//			}
//		}
//	}

	public static SpaceType.Duplicate node_duplicate = new SpaceType.Duplicate() {
	public SpaceLink run(SpaceLink sl)
	{
//		SpaceNode snoden= MEM_dupallocN(sl);
		SpaceNode snoden= ((SpaceNode)sl).copy();
		
		/* clear or remove stuff from old */
		snoden.nodetree= null;
		
		return (SpaceLink)snoden;
	}};


	/* add handlers, stuff you only do once or on area/region changes */
	public static ARegionType.Init node_buttons_area_init = new ARegionType.Init() {
	public void run(wmWindowManager wm, ARegion ar)
	{
		wmKeyMap keymap;

		Area.ED_region_panels_init(wm, ar);

		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Node Generic", SpaceTypes.SPACE_NODE, 0);
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
	}};

	public static ARegionType.Draw node_buttons_area_draw = new ARegionType.Draw() {
	public void run(GL2 gl, bContext C, ARegion ar)
	{
		Area.ED_region_panels(gl, C, ar, true, null, -1);
	}};

	/* Initialise main area, setting handlers. */
	public static ARegionType.Init node_main_area_init = new ARegionType.Init() {
	public void run(wmWindowManager wm, ARegion ar)
	{
		wmKeyMap keymap;
//		ListBase lb;
		
		View2dUtil.UI_view2d_region_reinit(ar.v2d, View2dUtil.V2D_COMMONVIEW_CUSTOM, ar.winx, ar.winy);
		
		/* own keymaps */
		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Node Generic", SpaceTypes.SPACE_NODE, 0);
		WmEventSystem.WM_event_add_keymap_handler(ar.handlers, keymap);
		
		keymap= WmKeymap.WM_keymap_find(wm.defaultconf, "Node Editor", SpaceTypes.SPACE_NODE, 0);
		WmEventSystem.WM_event_add_keymap_handler_bb(ar.handlers, keymap, ar.v2d.mask, ar.winrct);
		
		/* add drop boxes */
//		lb = WM_dropboxmap_find("Node Editor", SPACE_NODE, RGN_TYPE_WINDOW);
		
//		WM_event_add_dropbox_handler(&ar->handlers, lb);
	}};

	public static ARegionType.Draw node_main_area_draw = new ARegionType.Draw() {
	public void run(GL2 gl, bContext C, ARegion ar)
	{
		View2D v2d= ar.v2d;
		
		NodeDraw.drawnodespace(gl, C, ar, v2d);
	}};


	/* ************* dropboxes ************* */

//	static int node_drop_poll(bContext *UNUSED(C), wmDrag *drag, wmEvent *UNUSED(event))
//	{
//		if(drag->type==WM_DRAG_ID) {
//			ID *id= (ID *)drag->poin;
//			if( GS(id->name)==ID_IM )
//				return 1;
//		}
//		else if(drag->type==WM_DRAG_PATH){
//			if(ELEM(drag->icon, 0, ICON_FILE_IMAGE))	/* rule might not work? */
//				return 1;
//		}
//		return 0;
//	}
//
//	static void node_id_path_drop_copy(wmDrag *drag, wmDropBox *drop)
//	{
//		ID *id= (ID *)drag->poin;
//		
//		if(id) {
//			RNA_string_set(drop->ptr, "name", id->name+2);
//		}
//		if (drag->path[0]) {
//			RNA_string_set(drop->ptr, "filepath", drag->path);
//		}
//	}
//
//	/* this region dropbox definition */
//	static void node_dropboxes(void)
//	{
//		ListBase *lb= WM_dropboxmap_find("Node Editor", SPACE_NODE, RGN_TYPE_WINDOW);
//		
//		WM_dropbox_add(lb, "NODE_OT_add_file", node_drop_poll, node_id_path_drop_copy);
//		
//	}

	/* ************* end drop *********** */


	/* add handlers, stuff you only do once or on area/region changes */
	public static ARegionType.Init node_header_area_init = new ARegionType.Init() {
	public void run(wmWindowManager wm, ARegion ar)
	{
		Area.ED_region_header_init(ar);
	}};

	public static ARegionType.Draw node_header_area_draw = new ARegionType.Draw() {
	public void run(GL2 gl, bContext C, ARegion ar)
	{
//		SpaceNode snode= bContext.CTX_wm_space_node(C);
//		Scene scene= bContext.CTX_data_scene(C);
//
//		/* find and set the context */
//		snode_set_context(snode, scene);
		
		Area.ED_region_header(gl, C, ar);
	}};

	/* used for header + main area */
	public static ARegionType.Listener node_region_listener = new ARegionType.Listener() {
	public void run(ARegion ar, wmNotifier wmn)
	{
		/* context changes */
		switch(wmn.category) {
			case WmTypes.NC_SPACE:
				if(wmn.data==WmTypes.ND_SPACE_NODE)
					Area.ED_region_tag_redraw(ar);
				break;
			case WmTypes.NC_SCREEN:
//				if(wmn.data == WmTypes.ND_GPENCIL)	
//					Area.ED_region_tag_redraw(ar);
				break;
			case WmTypes.NC_SCENE:
			case WmTypes.NC_MATERIAL:
			case WmTypes.NC_TEXTURE:
			case WmTypes.NC_NODE:
				Area.ED_region_tag_redraw(ar);
				break;
			case WmTypes.NC_ID:
				if(wmn.action == WmTypes.NA_RENAME)
					Area.ED_region_tag_redraw(ar);
				break;
		}
	}};

//	static int node_context(const bContext *C, const char *member, bContextDataResult *result)
//	{
//		SpaceNode *snode= CTX_wm_space_node(C);
//		
//		if(CTX_data_dir(member)) {
//			static const char *dir[] = {"selected_nodes", NULL};
//			CTX_data_dir_set(result, dir);
//			return 1;
//		}
//		else if(CTX_data_equals(member, "selected_nodes")) {
//			bNode *node;
//			
//			for(next_node(snode->edittree); (node=next_node(NULL));) {
//				if(node->flag & SELECT) {
//					CTX_data_list_add(result, &snode->edittree->id, &RNA_Node, node);
//				}
//			}
//			CTX_data_type_set(result, CTX_DATA_TYPE_COLLECTION);
//			return 1;
//		}
//		
//		return 0;
//	}

	/* only called once, from space/spacetypes.c */
	public static void ED_spacetype_node()
	{
		SpaceType st= new SpaceType();
		ARegionType art;
		
		st.spaceid= SpaceTypes.SPACE_NODE;
		StringUtil.strncpy(st.name,0, StringUtil.toCString("Node"),0, ScreenUtil.BKE_ST_MAXNAME);
		
		st.newInstance= node_new;
		st.free= node_free;
		st.init= node_init;
		st.duplicate= node_duplicate;
		st.operatortypes= NodeOps.node_operatortypes;
		st.keymap= NodeOps.node_keymap;
//		st.listener= node_area_listener;
//		st.refresh= node_area_refresh;
//		st.context= node_context;
//		st.dropboxes = node_dropboxes;
		
		/* regions: main window */
		art= new ARegionType();
		art.regionid = ScreenTypes.RGN_TYPE_WINDOW;
		art.init= node_main_area_init;
		art.draw= node_main_area_draw;
		art.listener= node_region_listener;
		art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_GPENCIL;

		ListBaseUtil.BLI_addhead(st.regiontypes, art);
		
		/* regions: header */
		art= new ARegionType();
		art.regionid = ScreenTypes.RGN_TYPE_HEADER;
//		art.prefsizey= ScreenTypes.HEADERY;
		art.minsizey= ScreenTypes.HEADERY;
		art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_VIEW2D|Area.ED_KEYMAP_FRAMES|Area.ED_KEYMAP_HEADER;
		art.listener= node_region_listener;
		art.init= node_header_area_init;
		art.draw= node_header_area_draw;
		
		ListBaseUtil.BLI_addhead(st.regiontypes, art);

		NodeHeader.node_menus_register();
		
		/* regions: listview/buttons */
		art= new ARegionType();
		art.regionid = ScreenTypes.RGN_TYPE_UI;
//		art.prefsizex= 180; // XXX
		art.minsizex= 180; // XXX
		art.keymapflag= Area.ED_KEYMAP_UI|Area.ED_KEYMAP_FRAMES;
		art.listener= node_region_listener;
		art.init= node_buttons_area_init;
		art.draw= node_buttons_area_draw;
		ListBaseUtil.BLI_addhead(st.regiontypes, art);
		
//		NodeButtons.node_buttons_register(art);
		
		ScreenUtil.BKE_spacetype_register(st);
	}

}

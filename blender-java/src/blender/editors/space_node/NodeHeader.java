package blender.editors.space_node;

import blender.blenkernel.Node;
import blender.blenkernel.Ptr;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.Menu;
import blender.blenkernel.ScreenUtil.MenuType;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UI.uiMenuCreateFunc;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.NodeTypes;
import blender.makesdna.sdna.SpaceNode;
import blender.windowmanager.Wm;

public class NodeHeader {
//	/**
//	 * $Id: node_header.c 34159 2011-01-07 18:36:47Z campbellbarton $
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
//	#include "DNA_space_types.h"
//	#include "DNA_node_types.h"
//	#include "DNA_scene_types.h"
//	#include "DNA_screen_types.h"
//
//	#include "MEM_guardedalloc.h"
//
//	#include "BLI_blenlib.h"
//	#include "BLI_utildefines.h"
//
//	#include "BKE_context.h"
//	#include "BKE_screen.h"
//	#include "BKE_node.h"
//	#include "BKE_main.h"
//
//	#include "WM_api.h"
//	#include "WM_types.h"
//
//
//	#include "UI_interface.h"
//	#include "UI_view2d.h"
//
//	#include "node_intern.h"
//
//	/* ************************ add menu *********************** */
//
//	static void do_node_add(bContext *C, void *UNUSED(arg), int event)
//	{
//		SpaceNode *snode= CTX_wm_space_node(C);
//		ScrArea *sa= CTX_wm_area(C);
//		ARegion *ar;
//		bNode *node;
//		
//		/* get location to add node at mouse */
//		for(ar=sa->regionbase.first; ar; ar=ar->next) {
//			if(ar->regiontype == RGN_TYPE_WINDOW) {
//				wmWindow *win= CTX_wm_window(C);
//				int x= win->eventstate->x - ar->winrct.xmin;
//				int y= win->eventstate->y - ar->winrct.ymin;
//				
//				if(y < 60) y+= 60;
//				UI_view2d_region_to_view(&ar->v2d, x, y, &snode->mx, &snode->my);
//			}
//		}
//		
//		/* store selection in temp test flag */
//		for(node= snode->edittree->nodes.first; node; node= node->next) {
//			if(node->flag & NODE_SELECT) node->flag |= NODE_TEST;
//			else node->flag &= ~NODE_TEST;
//		}
//		
//		node= node_add_node(snode, CTX_data_scene(C), event, snode->mx, snode->my);
//		
//		/* select previous selection before autoconnect */
//		for(node= snode->edittree->nodes.first; node; node= node->next) {
//			if(node->flag & NODE_TEST) node->flag |= NODE_SELECT;
//		}
//		
//		snode_autoconnect(snode, 1, 0);
//		
//		/* deselect after autoconnection */
//		for(node= snode->edittree->nodes.first; node; node= node->next) {
//			if(node->flag & NODE_TEST) node->flag &= ~NODE_SELECT;
//		}
//			
//		snode_notify(C, snode);
//	}

	public static uiMenuCreateFunc node_auto_add_menu = new uiMenuCreateFunc() {
    public void run(bContext C, uiLayout layout, Object arg_nodeclass)
	{
//		Main *bmain= CTX_data_main(C);
//		SpaceNode *snode= CTX_wm_space_node(C);
//		bNodeTree *ntree;
//		int nodeclass= GET_INT_FROM_POINTER(arg_nodeclass);
//		int tot= 0, a;
//		
//		ntree = snode->nodetree;
//
//		if(!ntree) {
//			uiItemS(layout);
//			return;
//		}
//
//		/* mostly taken from toolbox.c, node_add_sublevel() */
//		if(nodeclass==NODE_CLASS_GROUP) {
//			bNodeTree *ngroup= bmain->nodetree.first;
//			for(; ngroup; ngroup= ngroup->id.next)
//				if(ngroup->type==ntree->type)
//					tot++;
//		}
//		else {
//			bNodeType *type = ntree->alltypes.first;
//			while(type) {
//				if(type->nclass == nodeclass)
//					tot++;
//				type= type->next;
//			}
//		}	
//		
//		if(tot==0) {
//			uiItemS(layout);
//			return;
//		}
//
//		uiLayoutSetFunc(layout, do_node_add, NULL);
//		
//		if(nodeclass==NODE_CLASS_GROUP) {
//			bNodeTree *ngroup= bmain->nodetree.first;
//
//			for(tot=0, a=0; ngroup; ngroup= ngroup->id.next, tot++) {
//				if(ngroup->type==ntree->type) {
//					uiItemV(layout, ngroup->id.name+2, ICON_NULL, NODE_GROUP_MENU+tot);
//					a++;
//				}
//			}
//		}
//		else {
//			bNodeType *type;
//			int script=0;
//
//			for(a=0, type= ntree->alltypes.first; type; type=type->next) {
//				if(type->nclass == nodeclass && type->name) {
//					if(type->type == NODE_DYNAMIC) {
//						uiItemV(layout, type->name, ICON_NULL, NODE_DYNAMIC_MENU+script);
//						script++;
//					}
//					else
//						uiItemV(layout, type->name, ICON_NULL, type->type);
//
//					a++;
//				}
//			}
//		}
	}};

	public static MenuType.Draw node_menu_add = new MenuType.Draw() {
    public void run(bContext C, Menu menu)
	{
		SpaceNode snode= bContext.CTX_wm_space_node(C);
		uiLayout layout= menu.layout;

		if(snode.nodetree==null)
			UILayout.uiLayoutSetActive(layout, 0);

		if(snode.treetype==NodeTypes.NTREE_SHADER) {
			UILayout.uiItemMenuF(layout, "Input", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_INPUT;}});
			UILayout.uiItemMenuF(layout, "Output", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OUTPUT;}});
			UILayout.uiItemMenuF(layout, "Color", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_COLOR;}});
			UILayout.uiItemMenuF(layout, "Vector", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_VECTOR;}});
			UILayout.uiItemMenuF(layout, "Convertor", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_CONVERTOR;}});
			UILayout.uiItemMenuF(layout, "Group", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_GROUP;}});
			UILayout.uiItemMenuF(layout, "Dynamic", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_DYNAMIC;}});
		}
		else if(snode.treetype==NodeTypes.NTREE_COMPOSIT) {
			UILayout.uiItemMenuF(layout, "Input", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_INPUT;}});
			UILayout.uiItemMenuF(layout, "Output", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OUTPUT;}});
			UILayout.uiItemMenuF(layout, "Color", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_COLOR;}});
			UILayout.uiItemMenuF(layout, "Vector", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_VECTOR;}});
			UILayout.uiItemMenuF(layout, "Filter", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_FILTER;}});
			UILayout.uiItemMenuF(layout, "Convertor", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_CONVERTOR;}});
			UILayout.uiItemMenuF(layout, "Matte", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_MATTE;}});
			UILayout.uiItemMenuF(layout, "Distort", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_DISTORT;}});
			UILayout.uiItemMenuF(layout, "Group", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_GROUP;}});
		}
		else if(snode.treetype==NodeTypes.NTREE_TEXTURE) {
			UILayout.uiItemMenuF(layout, "Input", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_INPUT;}});
			UILayout.uiItemMenuF(layout, "Output", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OUTPUT;}});
			UILayout.uiItemMenuF(layout, "Color", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_OP_COLOR;}});
			UILayout.uiItemMenuF(layout, "Patterns", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_PATTERN;}});
			UILayout.uiItemMenuF(layout, "Textures", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_TEXTURE;}});
			UILayout.uiItemMenuF(layout, "Convertor", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_CONVERTOR;}});
			UILayout.uiItemMenuF(layout, "Distort", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_DISTORT;}});
			UILayout.uiItemMenuF(layout, "Group", 0, node_auto_add_menu, new Ptr<Integer>(){public Integer get(){return Node.NODE_CLASS_GROUP;}});
		}
	}};

	public static void node_menus_register()
	{
		MenuType mt;

		mt= new MenuType();
		StringUtil.strcpy(mt.idname,0, StringUtil.toCString("NODE_MT_add"),0);
		StringUtil.strcpy(mt.label,0, StringUtil.toCString("Add"),0);
		mt.draw= node_menu_add;
		Wm.WM_menutype_add(mt);
	}

}

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
 * The Original Code is Copyright (C) 2009 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_view3d;

//#include <string.h>

import static blender.blenkernel.Blender.G;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import blender.blenkernel.Global;
import blender.blenkernel.Pointer;
import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.screen.Area;
import blender.editors.screen.ScreenOps;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UIRegions;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBlockCreateFunc;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.View2dTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.ARegion;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.ScrArea;
import blender.makesdna.sdna.SpaceLink;
import blender.makesdna.sdna.bObject;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.RnaAccess;
import blender.makesrna.RNATypes.PointerRNA;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmOperators.OpFunc;
import blender.windowmanager.WmTypes.wmEvent;

//#include <stdio.h>
//#include <math.h>
//#include <float.h>
//
//#include "DNA_ID.h"
//#include "DNA_action_types.h"
//#include "DNA_armature_types.h"
//#include "DNA_curve_types.h"
//#include "DNA_camera_types.h"
//#include "DNA_lamp_types.h"
//#include "DNA_lattice_types.h"
//#include "DNA_meta_types.h"
//#include "DNA_mesh_types.h"
//#include "DNA_meshdata_types.h"
//#include "DNA_object_types.h"
//#include "DNA_space_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_view3d_types.h"
//#include "DNA_world_types.h"
//
//#include "MEM_guardedalloc.h"
//
//#include "BLI_arithb.h"
//#include "BLI_blenlib.h"
//#include "BLI_editVert.h"
//#include "BLI_rand.h"
//
//#include "BKE_action.h"
//#include "BKE_brush.h"
//#include "BKE_context.h"
//#include "BKE_curve.h"
//#include "BKE_customdata.h"
//#include "BKE_depsgraph.h"
//#include "BKE_idprop.h"
//#include "BKE_mesh.h"
//#include "BKE_object.h"
//#include "BKE_global.h"
//#include "BKE_scene.h"
//#include "BKE_screen.h"
//#include "BKE_utildefines.h"
//
//#include "BIF_gl.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "RNA_access.h"
//#include "RNA_define.h"
//
//#include "ED_armature.h"
//#include "ED_curve.h"
//#include "ED_image.h"
//#include "ED_keyframing.h"
//#include "ED_mesh.h"
//#include "ED_object.h"
//#include "ED_particle.h"
//#include "ED_screen.h"
//#include "ED_transform.h"
//#include "ED_types.h"
//#include "ED_util.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"
//#include "UI_view2d.h"
//
//#include "view3d_intern.h"	// own include

public class View3dToolbar {
//
//
///* ******************* view3d space & buttons ************** */
//
//
///* op.invoke */
//static void redo_cb(bContext *C, void *arg_op, void *arg2)
//{
//	wmOperator *lastop= arg_op;
//
//	if(lastop) {
//		int retval;
//
//		printf("operator redo %s\n", lastop.type.name);
//		ED_undo_pop(C);
//		retval= WM_operator_repeat(C, lastop);
//		if((retval & OPERATOR_FINISHED)==0) {
//			printf("operator redo failed %s\n", lastop.type.name);
//			ED_undo_redo(C);
//		}
//	}
//}
//
public static PanelType.Draw view3d_panel_operator_redo = new PanelType.Draw() {
public void run(bContext C, Panel pa)
//static void view3d_panel_operator_redo(const bContext *C, Panel *pa)
{
	wmWindowManager wm= bContext.CTX_wm_manager(C);
	wmOperator op;
	PointerRNA ptr = new PointerRNA();
	uiBlock block;

	block= UILayout.uiLayoutGetBlock((uiLayout)pa.layout);

	/* only for operators that are registered and did an undo push */
	for(op= (wmOperator)wm.operators.last; op!=null; op= op.prev)
		if((((wmOperatorType)op.type).flag & WmTypes.OPTYPE_REGISTER)!=0 && (((wmOperatorType)op.type).flag & WmTypes.OPTYPE_UNDO)!=0)
			break;

	if(op==null)
		return;
	if(((wmOperatorType)op.type).poll!=null && ((wmOperatorType)op.type).poll.run(C, null, null)==0)
		return;

//	uiBlockSetFunc(block, redo_cb, op, null);

	if(op.properties==null) {
//		IDPropertyTemplate val = {0};
//		op.properties= IDP_New(IDP_GROUP, val, "wmOperatorProperties");
	}

	RnaAccess.RNA_pointer_create(wm.id, ((wmOperatorType)op.type).srna, op.properties, ptr);
//	uiDefAutoButsRNA(C, pa.layout, &ptr, 1);
}};

/* ******************* */

public static String view3d_context_string(bContext C)
{
	bObject obedit= bContext.CTX_data_edit_object(C);

	if(obedit!=null) {
		switch(obedit.type) {
			case ObjectTypes.OB_MESH:
				return "editmode_mesh";
			case ObjectTypes.OB_CURVE:
				return "editmode_curve";
			case ObjectTypes.OB_SURF:
				return "editmode_surface";
			case ObjectTypes.OB_FONT:
				return "editmode_text";
			case ObjectTypes.OB_ARMATURE:
				return "editmode_armature";
			case ObjectTypes.OB_MBALL:
				return "editmode_mball";
			case ObjectTypes.OB_LATTICE:
				return "editmode_lattice";
		}
	}
	else {
		bObject ob = bContext.CTX_data_active_object(C);
		
		if(ob!=null && (ob.flag & ObjectTypes.OB_POSEMODE)!=0) return "pose_mode";
		else if ((G.f & Global.G_SCULPTMODE)!=0)  return "sculpt_mode";
		else if ((G.f & Global.G_WEIGHTPAINT)!=0) return "weight_paint";
		else if ((G.f & Global.G_VERTEXPAINT)!=0) return "vertex_paint";
		else if ((G.f & Global.G_TEXTUREPAINT)!=0) return "texture_paint";
		else if ((G.f & Global.G_PARTICLEEDIT)!=0) return "particle_mode";
	}
	
	return "objectmode";
}

public static class CustomTool extends Link<CustomTool> {
	public byte[]  opname = new byte[WindowManagerTypes.OP_MAX_TYPENAME];
	public byte[] context = new byte[WindowManagerTypes.OP_MAX_TYPENAME];
};

//static void operator_call_cb(struct bContext *C, void *arg_listbase, void *arg2)
//{
//	wmOperatorType *ot= arg2;
//
//	if(ot) {
//		CustomTool *ct= MEM_callocN(sizeof(CustomTool), "CustomTool");
//
//		BLI_addtail(arg_listbase, ct);
//		BLI_strncpy(ct.opname, ot.idname, OP_MAX_TYPENAME);
//		BLI_strncpy(ct.context, view3d_context_string(C), OP_MAX_TYPENAME);
//	}
//
//}
//
//static void operator_search_cb(const struct bContext *C, void *arg, char *str, uiSearchItems *items)
//{
//	wmOperatorType *ot = WM_operatortype_first();
//
//	for(; ot; ot= ot.next) {
//
//		if(BLI_strcasestr(ot.name, str)) {
//			if(ot.poll==NULL || ot.poll((bContext *)C)) {
//
//				if(0==uiSearchItemAdd(items, ot.name, ot, 0))
//					break;
//			}
//		}
//	}
//}

static byte[] search = new byte[WindowManagerTypes.OP_MAX_TYPENAME];
/* ID Search browse menu, open */
public static uiBlockCreateFunc tool_search_menu = new uiBlockCreateFunc() {
public uiBlock run(bContext C, ARegion ar, Object arg_listbase)
//static uiBlock tool_search_menu(bContext *C, ARegion *ar, void *arg_listbase)
{
//	static char search[OP_MAX_TYPENAME];
	wmEvent event;
	wmWindow win= bContext.CTX_wm_window(C);
	uiBlock block;
	uiBut but;
	
	/* clear initial search string, then all items show */
	search[0]= 0;
	
	block= UI.uiBeginBlock(C, ar, "_popup", UI.UI_EMBOSS);
	UI.uiBlockSetFlag(block, UI.UI_BLOCK_LOOP|UI.UI_BLOCK_REDRAW|UI.UI_BLOCK_RET_1);
	
	/* fake button, it holds space for search items */
	UI.uiDefBut(block, UI.LABEL, 0, "", 10, 15, 150, UIRegions.uiSearchBoxhHeight(), null, 0, 0, 0, 0, null);

        Pointer<byte[]> search_p = new Pointer<byte[]>() {

                public byte[] get() {
                    return search;
                }

                public void set(byte[] obj) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

        };
	but= UI.uiDefSearchBut(block, search_p, 0, BIFIconID.ICON_VIEWZOOM, WindowManagerTypes.OP_MAX_TYPENAME, 10, 0, 150, 19, "");
//	uiButSetSearchFunc(but, operator_search_cb, arg_listbase, operator_call_cb, NULL);
	
	UI.uiBoundsBlock(block, 6);
	UI.uiBlockSetDirection(block, UI.UI_DOWN);
	UI.uiEndBlock(C, block);
	
//	event= win.eventstate.copy();	/* XXX huh huh? make api call */
        event= WmEventSystem.eventCopy(win.eventstate);
	event.type= WmEventTypes.EVT_BUT_OPEN;
	event.val= WmTypes.KM_PRESS;
	event.customdata= but;
	event.customdatafree= 0;
	WmEventSystem.wm_event_add(win, event);
	
	return block;
}};


public static PanelType.Draw view3d_panel_tool_shelf = new PanelType.Draw() {
public void run(bContext C, Panel pa)
//static void view3d_panel_tool_shelf(const bContext *C, Panel *pa)
{
	SpaceLink sl= bContext.CTX_wm_space_data(C);
	SpaceType st= null;
	uiLayout col;
	byte[] context= StringUtil.toCString(view3d_context_string(C));
	
	if(sl!=null)
		st= ScreenUtil.BKE_spacetype_from_id(sl.spacetype);
	
	if(st!=null && st.toolshelf.first!=null) {
		CustomTool ct;
		
		for(ct= (CustomTool)st.toolshelf.first; ct!=null; ct= ct.next) {
			if(0==StringUtil.strncmp(context,0, ct.context,0, WindowManagerTypes.OP_MAX_TYPENAME)) {
				col= UILayout.uiLayoutColumn((uiLayout)pa.layout, 1);
				UILayout.uiItemFullO(col, StringUtil.toJString(ct.opname,0), null, 0, null, WmTypes.WM_OP_INVOKE_REGION_WIN, 0);
			}
		}
	}
	col= UILayout.uiLayoutColumn((uiLayout)pa.layout, 1);
        final SpaceType stf = st;
        Pointer<ListBase> st_toolshelf = new Pointer<ListBase>() {

                public ListBase get() {
                    return stf.toolshelf;
                }

                public void set(ListBase obj) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

        };
	UI.uiDefBlockBut(UILayout.uiLayoutGetBlock((uiLayout)pa.layout), tool_search_menu, st_toolshelf, "Add Tool", 0, 0, UI.UI_UNIT_X, UI.UI_UNIT_Y, "Add Tool in shelf, gets saved in files");
}};


public static void view3d_toolbar_register(ARegionType art)
{
	PanelType pt;

	pt= new PanelType();
	StringUtil.strcpy(pt.idname,0, StringUtil.toCString("VIEW3D_PT_tool_shelf"),0);
	StringUtil.strcpy(pt.label,0, StringUtil.toCString("Tool Shelf"),0);
	pt.draw= view3d_panel_tool_shelf;
	ListBaseUtil.BLI_addtail(art.paneltypes, pt);
}

public static void view3d_tool_props_register(ARegionType art)
{
	PanelType pt;
	
	pt= new PanelType();
	StringUtil.strcpy(pt.idname,0, StringUtil.toCString("VIEW3D_PT_last_operator"),0);
	StringUtil.strcpy(pt.label,0, StringUtil.toCString("Last Operator"),0);
	pt.draw= view3d_panel_operator_redo;
	ListBaseUtil.BLI_addtail(art.paneltypes, pt);
}

/* ********** operator to open/close toolbar region */

public static wmOperatorType.Operator view3d_toolbar = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int view3d_toolbar(bContext *C, wmOperator *op)
{
	ScrArea sa= bContext.CTX_wm_area(C);
	ARegion ar= SpaceView3dUtil.view3d_has_tools_region(sa);
	
	if(ar!=null) {
		ar.flag ^= ScreenTypes.RGN_FLAG_HIDDEN;
		ar.v2d.flag &= ~View2dTypes.V2D_IS_INITIALISED; /* XXX should become hide/unhide api? */
		
		Area.ED_area_initialize((GL2)GLU.getCurrentGL(), bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa);
//		Area.ED_area_initialize(bContext.CTX_wm_manager(C), bContext.CTX_wm_window(C), sa);
		Area.ED_area_tag_redraw(sa);
	}
	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc VIEW3D_OT_toolbar = new OpFunc() {
public void run(wmOperatorType ot)
//void VIEW3D_OT_toolbar(wmOperatorType *ot)
{
	ot.name= "Toolbar";
	ot.idname= "VIEW3D_OT_toolbar";
	
	ot.exec= view3d_toolbar;
	ot.poll= ScreenOps.ED_operator_view3d_active;
	
	/* flags */
	ot.flag= 0;
}};

}
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
 * The Original Code is Copyright (C) 2007 Blender Foundation.
 * All rights reserved.
 *
 * 
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.windowmanager;

//#include <float.h>

import static blender.blenkernel.Blender.G;
import static blender.blenkernel.Blender.U;
import static blender.windowmanager.WmEventTypes.DKEY;
import static blender.windowmanager.WmEventTypes.F10KEY;
import static blender.windowmanager.WmEventTypes.F11KEY;
import static blender.windowmanager.WmEventTypes.F12KEY;
import static blender.windowmanager.WmEventTypes.F1KEY;
import static blender.windowmanager.WmEventTypes.F2KEY;
import static blender.windowmanager.WmEventTypes.F3KEY;
import static blender.windowmanager.WmEventTypes.F4KEY;
import static blender.windowmanager.WmEventTypes.F5KEY;
import static blender.windowmanager.WmEventTypes.F6KEY;
import static blender.windowmanager.WmEventTypes.F7KEY;
import static blender.windowmanager.WmEventTypes.F8KEY;
import static blender.windowmanager.WmEventTypes.F9KEY;
import static blender.windowmanager.WmEventTypes.NKEY;
import static blender.windowmanager.WmEventTypes.OKEY;
import static blender.windowmanager.WmEventTypes.QKEY;
import static blender.windowmanager.WmEventTypes.SKEY;
import static blender.windowmanager.WmEventTypes.SPACEKEY;
import static blender.windowmanager.WmEventTypes.TKEY;
import static blender.windowmanager.WmEventTypes.UKEY;
import static blender.windowmanager.WmEventTypes.WKEY;
import static blender.windowmanager.WmTypes.KM_ALT;
import static blender.windowmanager.WmTypes.KM_CTRL;
import static blender.windowmanager.WmTypes.KM_PRESS;
import static blender.windowmanager.WmTypes.KM_SHIFT;
import blender.blenkernel.Blender;
import blender.blenkernel.Global;
import blender.blenkernel.IdProp;
import blender.blenkernel.bContext;
import blender.blenkernel.IdProp.IDPropertyTemplate;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UIRegions;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.editors.uinterface.UIRegions.uiPopupMenu;
import blender.makesdna.DNA_ID;
import blender.makesdna.SpaceTypes;
import blender.makesdna.UserDefTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmKeyMapItem;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesrna.RNATypes;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaDefine;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.PropEnumItemFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.srna.RnaOperatorProperties;
import blender.windowmanager.WmTypes.RecentFile;
import blender.windowmanager.WmTypes.wmEvent;

//#define _USE_MATH_DEFINES
//#include <math.h>
//#include <string.h>
//#include <ctype.h>
//#include <stdio.h>
//
//#include "DNA_ID.h"
//#include "DNA_screen_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_userdef_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "MEM_guardedalloc.h"
//
//#include "PIL_time.h"
//
//#include "BLI_blenlib.h"
//#include "BLI_dynstr.h" /*for WM_operator_pystring */
//
//#include "BKE_blender.h"
//#include "BKE_context.h"
//#include "BKE_idprop.h"
//#include "BKE_library.h"
//#include "BKE_global.h"
//#include "BKE_main.h"
//#include "BKE_scene.h"
//#include "BKE_utildefines.h"
//
//#include "BIF_gl.h"
//#include "BIF_glutil.h" /* for paint cursor */
//
//#include "IMB_imbuf_types.h"
//
//#include "ED_screen.h"
//#include "ED_util.h"
//
//#include "RNA_access.h"
//#include "RNA_define.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "wm.h"
//#include "wm_draw.h"
//#include "wm_event_system.h"
//#include "wm_subwindow.h"
//#include "wm_window.h"
//
public class WmOperators {

public static interface OpFunc {
    public void run(wmOperatorType ot);
}

static ListBase<wmOperatorType> global_ops= new ListBase<wmOperatorType>();

/* ************ operator API, exported ********** */


public static wmOperatorType WM_operatortype_find(String idname, boolean quiet)
{
	wmOperatorType ot;

	byte[] idname_bl=new byte[WindowManagerTypes.OP_MAX_TYPENAME]; // XXX, needed to support python style names without the _OT_ syntax
	WM_operator_bl_idname(idname_bl, StringUtil.toCString(idname));

	for(ot= global_ops.first; ot!=null; ot= ot.next) {
		if(StringUtil.strncmp(StringUtil.toCString(ot.idname),0, idname_bl,0, WindowManagerTypes.OP_MAX_TYPENAME)==0)
		   return ot;
	}

//	if(!quiet)
//		System.out.printf("search for unknown operator %s, %s\n", StringUtil.toJString(idname_bl,0), idname);

	return null;
}

//wmOperatorType *WM_operatortype_exists(const char *idname)
//{
//	wmOperatorType *ot;
//
//	char idname_bl[OP_MAX_TYPENAME]; // XXX, needed to support python style names without the _OT_ syntax
//	WM_operator_bl_idname(idname_bl, idname);
//
//	for(ot= global_ops.first; ot; ot= ot.next) {
//		if(strncmp(ot.idname, idname_bl, OP_MAX_TYPENAME)==0)
//		   return ot;
//	}
//	return NULL;
//}
//
//wmOperatorType *WM_operatortype_first(void)
//{
//	return global_ops.first;
//}

/* all ops in 1 list (for time being... needs evaluation later) */
public static void WM_operatortype_append(OpFunc opfunc)
{
	wmOperatorType ot;
	
	ot= new wmOperatorType();
	ot.srna= RnaDefine.RNA_def_struct(new BlenderRNA(), "", "OperatorProperties");
//    ot.srna= new StructRNA();
	opfunc.run(ot);

	if(ot.name==null) {
//		static char dummy_name[] = "Dummy Name";
		System.err.printf("ERROR: Operator %s has no name property!\n", ot.idname);
//		ot.name= dummy_name;
                ot.name= "Dummy Name";
	}

//	RNA_def_struct_ui_text(ot.srna, ot.name, ot.description ? ot.description:"(undocumented operator)"); // XXX All ops should have a description but for now allow them not to.
//	RNA_def_struct_identifier(ot.srna, ot.idname);
	ListBaseUtil.BLI_addtail(global_ops, ot);
}

//void WM_operatortype_append_ptr(void (*opfunc)(wmOperatorType*, void*), void *userdata)
//{
//	wmOperatorType *ot;
//
//	ot= MEM_callocN(sizeof(wmOperatorType), "operatortype");
//	ot.srna= RNA_def_struct(&BLENDER_RNA, "", "OperatorProperties");
//	opfunc(ot, userdata);
//	RNA_def_struct_ui_text(ot.srna, ot.name, ot.description ? ot.description:"(undocumented operator)");
//	RNA_def_struct_identifier(ot.srna, ot.idname);
//	BLI_addtail(&global_ops, ot);
//}
//
//int WM_operatortype_remove(const char *idname)
//{
//	wmOperatorType *ot = WM_operatortype_find(idname, 0);
//
//	if (ot==NULL)
//		return 0;
//
//	BLI_remlink(&global_ops, ot);
//	RNA_struct_free(&BLENDER_RNA, ot.srna);
//	MEM_freeN(ot);
//
//	return 1;
//}
//
///* SOME_OT_op . some.op */
//void WM_operator_py_idname(char *to, const char *from)
//{
//	char *sep= strstr(from, "_OT_");
//	if(sep) {
//		int i, ofs= (sep-from);
//
//		for(i=0; i<ofs; i++)
//			to[i]= tolower(from[i]);
//
//		to[ofs] = '.';
//		BLI_strncpy(to+(ofs+1), sep+4, OP_MAX_TYPENAME);
//	}
//	else {
//		/* should not happen but support just incase */
//		BLI_strncpy(to, from, OP_MAX_TYPENAME);
//	}
//}

/* some.op . SOME_OT_op */
public static void WM_operator_bl_idname(byte[] to, byte[] from)
{
	int sep_p= StringUtil.strstr(from,0, StringUtil.toCString("."),0);

	if(sep_p!=-1) {
		int i, ofs= sep_p;

		for(i=0; i<ofs; i++)
//			to[i]= toupper(from[i]);
                        to[i]= (byte)Character.toUpperCase(from[i]&0xFF);

		StringUtil.BLI_strncpy(to,ofs, StringUtil.toCString("_OT_"),0, WindowManagerTypes.OP_MAX_TYPENAME);
		StringUtil.BLI_strncpy(to,(ofs+4), from,sep_p+1, WindowManagerTypes.OP_MAX_TYPENAME);
	}
	else {
		/* should not happen but support just incase */
		StringUtil.BLI_strncpy(to,0, from,0, WindowManagerTypes.OP_MAX_TYPENAME);
	}
}

///* print a string representation of the operator, with the args that it runs
// * so python can run it again */
//char *WM_operator_pystring(wmOperator *op)
//{
//	const char *arg_name= NULL;
//	char idname_py[OP_MAX_TYPENAME];
//
//	PropertyRNA *prop, *iterprop;
//
//	/* for building the string */
//	DynStr *dynstr= BLI_dynstr_new();
//	char *cstring, *buf;
//	int first_iter=1;
//
//	WM_operator_py_idname(idname_py, op.idname);
//	BLI_dynstr_appendf(dynstr, "bpy.ops.%s(", idname_py);
//
//	iterprop= RNA_struct_iterator_property(op.ptr.type);
//
//	RNA_PROP_BEGIN(op.ptr, propptr, iterprop) {
//		prop= propptr.data;
//		arg_name= RNA_property_identifier(prop);
//
//		if (strcmp(arg_name, "rna_type")==0) continue;
//
//		buf= RNA_property_as_string(op.ptr, prop);
//
//		BLI_dynstr_appendf(dynstr, first_iter?"%s=%s":", %s=%s", arg_name, buf);
//
//		MEM_freeN(buf);
//		first_iter = 0;
//	}
//	RNA_PROP_END;
//
//	BLI_dynstr_append(dynstr, ")");
//
//	cstring = BLI_dynstr_get_cstring(dynstr);
//	BLI_dynstr_free(dynstr);
//	return cstring;
//}

public static void WM_operator_properties_create_ptr(PointerRNA ptr, wmOperatorType ot)
{
	RnaAccess.RNA_pointer_create(null, ot.srna, null, ptr);
}

public static void WM_operator_properties_create(PointerRNA ptr, String opstring)
{
	wmOperatorType ot= WM_operatortype_find(opstring, false);

	if(ot!=null) {
		WM_operator_properties_create_ptr(ptr, ot);
    }
	else {
		RnaAccess.RNA_pointer_create(null, RnaOperatorProperties.RNA_OperatorProperties, null, ptr);
    }
}

///* similar to the function above except its uses ID properties
// * used for keymaps and macros */
////public static void WM_operator_properties_alloc(PointerRNA[] ptr, IDProperty[] properties, String opstring)
//public static void WM_operator_properties_alloc(wmKeyMapItem kmi)
//{
////	if(kmi.properties==null) {
////		IDPropertyTemplate val = {0};
////		kmi.properties= IDP_New(IDP_GROUP, val, "wmOpItemProp");
////	}
//
//	if(kmi.ptr==null) {
//		kmi.ptr= new PointerRNA();
//		WM_operator_properties_create((PointerRNA)kmi.ptr, StringUtil.toJString(kmi.idname,0));
//	}
//
////	kmi.ptr.data= kmi.properties;
//
//}
/* similar to the function above except its uses ID properties
 * used for keymaps and macros */
public static void WM_operator_properties_alloc(PointerRNA[] ptr, IDProperty[] properties, byte[] opstring, int offset)
{
	if(properties[0]==null) {
		IDPropertyTemplate val = new IDPropertyTemplate();
		properties[0]= IdProp.IDP_New(DNA_ID.IDP_GROUP, val, "wmOpItemProp");
	}

	if(ptr[0]==null) {
		ptr[0]= new PointerRNA();
		WM_operator_properties_create(ptr[0], StringUtil.toJString(opstring,offset));
	}

	ptr[0].data= properties[0];

}

public static void WM_operator_properties_sanitize(PointerRNA ptr, boolean no_context)
{
//	RNA_STRUCT_BEGIN(ptr, prop) {
//		switch(RNA_property_type(prop)) {
//		case PROP_ENUM:
//			if (no_context)
//				RNA_def_property_flag(prop, PROP_ENUM_NO_CONTEXT);
//			else
//				RNA_def_property_clear_flag(prop, PROP_ENUM_NO_CONTEXT);
//			break;
//		case PROP_POINTER:
//			{
//				StructRNA *ptype= RNA_property_pointer_type(ptr, prop);
//
//				/* recurse into operator properties */
//				if (RNA_struct_is_a(ptype, &RNA_OperatorProperties)) {
//					PointerRNA opptr = RNA_property_pointer_get(ptr, prop);
//					WM_operator_properties_sanitize(&opptr, no_context);
//				}
//				break;
//			}
//		default:
//			break;
//		}
//	}
//	RNA_STRUCT_END;
}

//void WM_operator_properties_free(PointerRNA *ptr)
//{
//	IDProperty *properties= ptr.data;
//
//	if(properties) {
//		IDP_FreeProperty(properties);
//		MEM_freeN(properties);
//	}
//}

/* ************ default op callbacks, exported *********** */

/* invoke callback, uses enum property named "type" */
/* only weak thing is the fixed property name... */
public static wmOperatorType.Operator WM_menu_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int WM_menu_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//	PropertyRNA prop= RNA_struct_find_property(op.ptr, "type");
	uiPopupMenu pup;
	uiLayout layout;

//	if(prop==null) {
//		printf("WM_menu_invoke: %s has no \"type\" enum property\n", op.type.idname);
//	}
//	else if (RNA_property_type(prop) != PROP_ENUM) {
//		printf("WM_menu_invoke: %s \"type\" is not an enum property\n", op.type.idname);
//	}
//	else {
		pup= UIRegions.uiPupMenuBegin(C, ((wmOperatorType)op.type).name, 0);
		layout= UIRegions.uiPupMenuLayout(pup);
		UILayout.uiItemsEnumO(layout, ((wmOperatorType)op.type).idname, "type");
		UIRegions.uiPupMenuEnd(C, pup);
//	}

	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

/* op.invoke */
public static wmOperatorType.Operator WM_operator_confirm = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int WM_operator_confirm(bContext *C, wmOperator *op, wmEvent *event)
{
	uiPopupMenu pup;
	uiLayout layout;

	pup= UIRegions.uiPupMenuBegin(C, "OK?", BIFIconID.ICON_QUESTION.ordinal());
	layout= UIRegions.uiPupMenuLayout(pup);
	UILayout.uiItemO(layout, null, 0, ((wmOperatorType)op.type).idname);
	UIRegions.uiPupMenuEnd(C, pup);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

///* op.invoke, opens fileselect if filename property not set, otherwise executes */
//int WM_operator_filesel(bContext *C, wmOperator *op, wmEvent *event)
//{
//	if (RNA_property_is_set(op.ptr, "filename")) {
//		return WM_operator_call(C, op);
//	}
//	else {
//		WM_event_add_fileselect(C, op);
//		return OPERATOR_RUNNING_MODAL;
//	}
//}

/* default properties for fileselect */
public static void WM_operator_properties_filesel(wmOperatorType ot, int filter, int type, int action, int flag)
{
	PropertyRNA prop;


	if((flag & WmTypes.WM_FILESEL_FILEPATH)!=0)
		RnaDefine.RNA_def_string_file_path(ot.srna, "filepath", "", Blender.FILE_MAX, "File Path", "Path to file");

	if((flag & WmTypes.WM_FILESEL_DIRECTORY)!=0)
		RnaDefine.RNA_def_string_dir_path(ot.srna, "directory", "", Blender.FILE_MAX, "Directory", "Directory of the file");

	if((flag & WmTypes.WM_FILESEL_FILENAME)!=0)
		RnaDefine.RNA_def_string_file_name(ot.srna, "filename", "", Blender.FILE_MAX, "File Name", "Name of the file");

	if (action == SpaceTypes.FILE_SAVE) {
		prop= RnaDefine.RNA_def_boolean(ot.srna, "check_existing", 1, "Check Existing", "Check and warn on overwriting existing files");
		RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	}
	
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_blender", (filter & SpaceTypes.BLENDERFILE), "Filter .blend files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_image", (filter & SpaceTypes.IMAGEFILE), "Filter image files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_movie", (filter & SpaceTypes.MOVIEFILE), "Filter movie files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_python", (filter & SpaceTypes.PYSCRIPTFILE), "Filter python files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_font", (filter & SpaceTypes.FTFONTFILE), "Filter font files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_sound", (filter & SpaceTypes.SOUNDFILE), "Filter sound files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_text", (filter & SpaceTypes.TEXTFILE), "Filter text files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_btx", (filter & SpaceTypes.BTXFILE), "Filter btx files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_collada", (filter & SpaceTypes.COLLADAFILE), "Filter COLLADA files", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);
	prop= RnaDefine.RNA_def_boolean(ot.srna, "filter_folder", (filter & SpaceTypes.FOLDERFILE), "Filter folders", "");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);

	prop= RnaDefine.RNA_def_int(ot.srna, "filemode", type, SpaceTypes.FILE_LOADLIB, SpaceTypes.FILE_SPECIAL, 
		"File Browser Mode", "The setting for the file browser mode to load a .blend file, a library or a special file",
		SpaceTypes.FILE_LOADLIB, SpaceTypes.FILE_SPECIAL);
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_HIDDEN);

	if((flag & WmTypes.WM_FILESEL_RELPATH)!=0)
		RnaDefine.RNA_def_boolean(ot.srna, "relative_path", (U.flag & UserDefTypes.USER_RELPATHS)!=0 ? 1:0, "Relative Path", "Select the file relative to the blend file");
	
	
	
	
//	RnaDefine.RNA_def_string_file_path(ot.srna, "filename", "", Blender.FILE_MAX, "Filename", "Path to file.");
//
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_blender", (filter & SpaceTypes.BLENDERFILE), "Filter .blend files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_image", (filter & SpaceTypes.IMAGEFILE), "Filter image files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_movie", (filter & SpaceTypes.MOVIEFILE), "Filter movie files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_python", (filter & SpaceTypes.PYSCRIPTFILE), "Filter python files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_font", (filter & SpaceTypes.FTFONTFILE), "Filter font files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_sound", (filter & SpaceTypes.SOUNDFILE), "Filter sound files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_text", (filter & SpaceTypes.TEXTFILE), "Filter text files", "");
//	RnaDefine.RNA_def_boolean(ot.srna, "filter_folder", (filter & SpaceTypes.FOLDERFILE), "Filter folders", "");
}

/* op.poll */
public static wmOperatorType.Operator WM_operator_winactive = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//int WM_operator_winactive(bContext *C)
{
	if(bContext.CTX_wm_window(C)==null) return 0;
	return 1;
}};

///* op.invoke */
//static void redo_cb(bContext *C, void *arg_op, void *arg2)
//{
//	wmOperator *lastop= arg_op;
//
//	if(lastop) {
//		ED_undo_pop(C);
//		WM_operator_repeat(C, lastop);
//	}
//}
//
//static uiBlock *wm_block_create_redo(bContext *C, ARegion *ar, void *arg_op)
//{
//	wmWindowManager *wm= CTX_wm_manager(C);
//	wmOperator *op= arg_op;
//	PointerRNA ptr;
//	uiBlock *block;
//	uiLayout *layout;
//	uiStyle *style= U.uistyles.first;
//
//	block= uiBeginBlock(C, ar, "redo_popup", UI_EMBOSS);
//	uiBlockClearFlag(block, UI_BLOCK_LOOP);
//	uiBlockSetFlag(block, UI_BLOCK_KEEP_OPEN|UI_BLOCK_RET_1);
//	uiBlockSetFunc(block, redo_cb, arg_op, NULL);
//
//	if(!op.properties) {
//		IDPropertyTemplate val = {0};
//		op.properties= IDP_New(IDP_GROUP, val, "wmOperatorProperties");
//	}
//
//	RNA_pointer_create(&wm.id, op.type.srna, op.properties, &ptr);
//	layout= uiBlockLayout(block, UI_LAYOUT_VERTICAL, UI_LAYOUT_PANEL, 0, 0, 300, 20, style);
//	uiDefAutoButsRNA(C, layout, &ptr, 2);
//
//	uiPopupBoundsBlock(block, 4.0f, 0, 0);
//	uiEndBlock(C, block);
//
//	return block;
//}
//
//int WM_operator_props_popup(bContext *C, wmOperator *op, wmEvent *event)
//{
//	int retval= OPERATOR_CANCELLED;
//
//	if(op.type.exec)
//		retval= op.type.exec(C, op);
//
//	if(retval != OPERATOR_CANCELLED)
//		uiPupBlock(C, wm_block_create_redo, op);
//
//	return retval;
//}
//
//int WM_operator_redo_popup(bContext *C, wmOperator *op)
//{
//	uiPupBlock(C, wm_block_create_redo, op);
//
//	return OPERATOR_CANCELLED;
//}
//
///* ***************** Debug menu ************************* */
//
//static uiBlock *wm_block_create_menu(bContext *C, ARegion *ar, void *arg_op)
//{
//	wmOperator *op= arg_op;
//	uiBlock *block;
//	uiLayout *layout;
//	uiStyle *style= U.uistyles.first;
//
//	block= uiBeginBlock(C, ar, "_popup", UI_EMBOSS);
//	uiBlockClearFlag(block, UI_BLOCK_LOOP);
//	uiBlockSetFlag(block, UI_BLOCK_KEEP_OPEN|UI_BLOCK_RET_1);
//
//	layout= uiBlockLayout(block, UI_LAYOUT_VERTICAL, UI_LAYOUT_PANEL, 0, 0, 300, 20, style);
//	uiDefAutoButsRNA(C, layout, op.ptr, 2);
//
//	uiPopupBoundsBlock(block, 4.0f, 0, 0);
//	uiEndBlock(C, block);
//
//	return block;
//}
//
//static int wm_debug_menu_exec(bContext *C, wmOperator *op)
//{
//	G.rt= RNA_int_get(op.ptr, "debugval");
//	ED_screen_refresh(CTX_wm_manager(C), CTX_wm_window(C));
//	WM_event_add_notifier(C, NC_WINDOW, NULL);
//
//	return OPERATOR_FINISHED;
//}
//
//static int wm_debug_menu_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//
//	RNA_int_set(op.ptr, "debugval", G.rt);
//
//	/* pass on operator, so return modal */
//	uiPupBlockOperator(C, wm_block_create_menu, op, WM_OP_EXEC_DEFAULT);
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//static void WM_OT_debug_menu(wmOperatorType *ot)
//{
//	ot.name= "Debug Menu";
//	ot.idname= "WM_OT_debug_menu";
//
//	ot.invoke= wm_debug_menu_invoke;
//	ot.exec= wm_debug_menu_exec;
//	ot.poll= WM_operator_winactive;
//
//	RNA_def_int(ot.srna, "debugval", 0, -10000, 10000, "Debug Value", "", INT_MIN, INT_MAX);
//}
//
///* ***************** Search menu ************************* */
//static void operator_call_cb(struct bContext *C, void *arg1, void *arg2)
//{
//	wmOperatorType *ot= arg2;
//
//	if(ot)
//		WM_operator_name_call(C, ot.idname, WM_OP_INVOKE_DEFAULT, NULL);
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
//				char name[256];
//				int len= strlen(ot.name);
//
//				/* display name for menu, can hold hotkey */
//				BLI_strncpy(name, ot.name, 256);
//
//				/* check for hotkey */
//				if(len < 256-6) {
//					if(WM_key_event_operator_string(C, ot.idname, WM_OP_EXEC_DEFAULT, NULL, &name[len+1], 256-len-1))
//						name[len]= '|';
//				}
//
//				if(0==uiSearchItemAdd(items, name, ot, 0))
//					break;
//			}
//		}
//	}
//}
//
//static uiBlock *wm_block_search_menu(bContext *C, ARegion *ar, void *arg_op)
//{
//	static char search[256]= "";
//	wmEvent event;
//	wmWindow *win= CTX_wm_window(C);
//	uiBlock *block;
//	uiBut *but;
//
//	block= uiBeginBlock(C, ar, "_popup", UI_EMBOSS);
//	uiBlockSetFlag(block, UI_BLOCK_LOOP|UI_BLOCK_RET_1);
//
//	but= uiDefSearchBut(block, search, 0, ICON_VIEWZOOM, 256, 10, 10, 180, 19, "");
//	uiButSetSearchFunc(but, operator_search_cb, NULL, operator_call_cb, NULL);
//
//	/* fake button, it holds space for search items */
//	uiDefBut(block, LABEL, 0, "", 10, 10 - uiSearchBoxhHeight(), 180, uiSearchBoxhHeight(), NULL, 0, 0, 0, 0, NULL);
//
//	uiPopupBoundsBlock(block, 6.0f, 0, -20); /* move it downwards, mouse over button */
//	uiEndBlock(C, block);
//
//	event= *(win.eventstate);	/* XXX huh huh? make api call */
//	event.type= EVT_BUT_OPEN;
//	event.val= KM_PRESS;
//	event.customdata= but;
//	event.customdatafree= FALSE;
//	wm_event_add(win, &event);
//
//	return block;
//}
//
//static int wm_search_menu_exec(bContext *C, wmOperator *op)
//{
//
//	return OPERATOR_FINISHED;
//}
//
//static int wm_search_menu_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//
//	uiPupBlock(C, wm_block_search_menu, op);
//
//	return OPERATOR_CANCELLED;
//}
//
///* op.poll */
//int wm_search_menu_poll(bContext *C)
//{
//	if(CTX_wm_window(C)==NULL) return 0;
//	if(CTX_wm_area(C) && CTX_wm_area(C).spacetype==SPACE_CONSOLE) return 0;  // XXX - so we can use the shortcut in the console
//	return 1;
//}
//
//static void WM_OT_search_menu(wmOperatorType *ot)
//{
//	ot.name= "Search Menu";
//	ot.idname= "WM_OT_search_menu";
//
//	ot.invoke= wm_search_menu_invoke;
//	ot.exec= wm_search_menu_exec;
//	ot.poll= wm_search_menu_poll;
//}


/* ************ window / screen operator definitions ************** */

public static OpFunc WM_OT_window_duplicate = new OpFunc() {
public void run(wmOperatorType ot)
//static void WM_OT_window_duplicate(wmOperatorType *ot)
{
	ot.name= "Duplicate Window";
	ot.idname= "WM_OT_window_duplicate";

	ot.invoke= WM_operator_confirm;
	ot.exec= WmWindow.wm_window_duplicate_op;
	ot.poll= WM_operator_winactive;
}};

public static OpFunc WM_OT_save_homefile = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Save User Settings";
	ot.idname= "WM_OT_save_homefile";

	ot.invoke= WM_operator_confirm;
	ot.exec= WmFiles.WM_write_homefile;
	ot.poll= WM_operator_winactive;
}};

public static OpFunc WM_OT_read_homefile = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Reload Start-Up File";
	ot.idname= "WM_OT_read_homefile";

	ot.invoke= WM_operator_confirm;
	ot.exec= WmFiles.WM_read_homefile;
	ot.poll= WM_operator_winactive;

	RnaDefine.RNA_def_boolean(ot.srna, "factory", 0, "Factory Settings", "");
}};

public static OpFunc WM_OT_read_factory_settings = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Load Factory Settings";
	ot.idname= "WM_OT_read_factory_settings";
	ot.description="Load default file and user preferences";
	
	ot.invoke= WM_operator_confirm;
	ot.exec= WmFiles.WM_read_homefile;
	ot.poll= WM_operator_winactive;
}};

/* **************** link/append *************** */

public static wmOperatorType.Operator wm_link_append_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	if(!RNA_property_is_set(op->ptr, "relative_path"))
//		RNA_boolean_set(op->ptr, "relative_path", U.flag & USER_RELPATHS);
//
//	if(RNA_property_is_set(op->ptr, "filepath")) {
//		return WM_operator_call(C, op);
//	} 
//	else {
//		/* XXX TODO solve where to get last linked library from */
//		RNA_string_set(op->ptr, "filepath", G.lib);
//		WM_event_add_fileselect(C, op);
		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
//	}
}};

//static short wm_link_append_flag(wmOperator *op)
//{
//	short flag= 0;
//
//	if(RNA_boolean_get(op->ptr, "autoselect")) flag |= FILE_AUTOSELECT;
//	if(RNA_boolean_get(op->ptr, "active_layer")) flag |= FILE_ACTIVELAY;
//	if(RNA_boolean_get(op->ptr, "relative_path")) flag |= FILE_RELPATH;
//	if(RNA_boolean_get(op->ptr, "link")) flag |= FILE_LINK;
//	if(RNA_boolean_get(op->ptr, "instance_groups")) flag |= FILE_GROUP_INSTANCE;
//
//	return flag;
//}
//
//static void wm_link_make_library_local(Main *main, const char *libname)
//{
//	Library *lib;
//
//	/* and now find the latest append lib file */
//	for(lib= main->library.first; lib; lib=lib->id.next)
//		if(BLI_streq(libname, lib->filepath))
//			break;
//	
//	/* make local */
//	if(lib) {
//		all_local(lib, 1);
//	}
//}

public static wmOperatorType.Operator wm_link_append_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	Main *bmain= CTX_data_main(C);
//	Scene *scene= CTX_data_scene(C);
//	Main *mainl= 0;
//	BlendHandle *bh;
//	PropertyRNA *prop;
//	char name[FILE_MAX], dir[FILE_MAX], libname[FILE_MAX], group[GROUP_MAX];
//	int idcode, totfiles=0;
//	short flag;
//
//	name[0] = '\0';
//	RNA_string_get(op->ptr, "filename", name);
//	RNA_string_get(op->ptr, "directory", dir);
//
//	/* test if we have a valid data */
//	if(BLO_is_a_library(dir, libname, group) == 0) {
//		BKE_report(op->reports, RPT_ERROR, "Not a library");
//		return OPERATOR_CANCELLED;
//	}
//	else if(group[0] == 0) {
//		BKE_report(op->reports, RPT_ERROR, "Nothing indicated");
//		return OPERATOR_CANCELLED;
//	}
//	else if(BLI_streq(bmain->name, libname)) {
//		BKE_report(op->reports, RPT_ERROR, "Cannot use current file as library");
//		return OPERATOR_CANCELLED;
//	}
//
//	/* check if something is indicated for append/link */
//	prop = RNA_struct_find_property(op->ptr, "files");
//	if(prop) {
//		totfiles= RNA_property_collection_length(op->ptr, prop);
//		if(totfiles == 0) {
//			if(name[0] == '\0') {
//				BKE_report(op->reports, RPT_ERROR, "Nothing indicated");
//				return OPERATOR_CANCELLED;
//			}
//		}
//	}
//	else if(name[0] == '\0') {
//		BKE_report(op->reports, RPT_ERROR, "Nothing indicated");
//		return OPERATOR_CANCELLED;
//	}
//
//	/* now we have or selected, or an indicated file */
//	if(RNA_boolean_get(op->ptr, "autoselect"))
//		scene_deselect_all(scene);
//
//	bh = BLO_blendhandle_from_file(libname);
//	idcode = BKE_idcode_from_name(group);
//	
//	flag = wm_link_append_flag(op);
//
//	/* sanity checks for flag */
//	if(scene->id.lib && (flag & FILE_GROUP_INSTANCE)) {
//		/* TODO, user never gets this message */
//		BKE_reportf(op->reports, RPT_WARNING, "Scene '%s' is linked, group instance disabled", scene->id.name+2);
//		flag &= ~FILE_GROUP_INSTANCE;
//	}
//
//
//	/* tag everything, all untagged data can be made local
//	 * its also generally useful to know what is new
//	 *
//	 * take extra care flag_all_listbases_ids(LIB_LINK_TAG, 0) is called after! */
//	flag_all_listbases_ids(LIB_PRE_EXISTING, 1);
//
//	/* here appending/linking starts */
//	mainl = BLO_library_append_begin(C, &bh, libname);
//	if(totfiles == 0) {
//		BLO_library_append_named_part(C, mainl, &bh, name, idcode, flag);
//	}
//	else {
//		RNA_BEGIN(op->ptr, itemptr, "files") {
//			RNA_string_get(&itemptr, "name", name);
//			BLO_library_append_named_part(C, mainl, &bh, name, idcode, flag);
//		}
//		RNA_END;
//	}
//	BLO_library_append_end(C, mainl, &bh, idcode, flag);
//	
//	/* mark all library linked objects to be updated */
//	recalc_all_library_objects(bmain);
//
//	/* append, rather than linking */
//	if((flag & FILE_LINK)==0)
//		wm_link_make_library_local(bmain, libname);
//
//	/* important we unset, otherwise these object wont
//	 * link into other scenes from this blend file */
//	flag_all_listbases_ids(LIB_PRE_EXISTING, 0);
//
//	/* recreate dependency graph to include new objects */
//	DAG_scene_sort(bmain, scene);
//	DAG_ids_flush_update(bmain, 0);
//
//	BLO_blendhandle_close(bh);
//
//	/* XXX TODO: align G.lib with other directory storage (like last opened image etc...) */
//	BLI_strncpy(G.lib, dir, FILE_MAX);
//
//	WM_event_add_notifier(C, NC_WINDOW, NULL);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc WM_OT_link_append = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Link/Append from Library";
	ot.idname= "WM_OT_link_append";
	ot.description= "Link or Append from a Library .blend file";
	
	ot.invoke= wm_link_append_invoke;
	ot.exec= wm_link_append_exec;
	ot.poll= WM_operator_winactive;
	
	ot.flag |= WmTypes.OPTYPE_UNDO;

//	WM_operator_properties_filesel(ot, FOLDERFILE|BLENDERFILE, FILE_LOADLIB, FILE_OPENFILE, WM_FILESEL_FILEPATH|WM_FILESEL_DIRECTORY|WM_FILESEL_FILENAME| WM_FILESEL_RELPATH);
	
	RnaDefine.RNA_def_boolean(ot.srna, "link", 1, "Link", "Link the objects or datablocks rather than appending");
	RnaDefine.RNA_def_boolean(ot.srna, "autoselect", 1, "Select", "Select the linked objects");
	RnaDefine.RNA_def_boolean(ot.srna, "active_layer", 1, "Active Layer", "Put the linked objects on the active layer");
	RnaDefine.RNA_def_boolean(ot.srna, "instance_groups", 1, "Instance Groups", "Create instances for each group as a DupliGroup");

//	RnaDefine.RNA_def_collection_runtime(ot.srna, "files", RNA_OperatorFileListElement, "Files", "");
}};	

/* ********* recent file *********** */

public static wmOperatorType.Operator recentfile_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent notUsed)
//static int recentfile_exec(bContext *C, wmOperator *op)
{
	int event= RnaAccess.RNA_enum_get(op.ptr, "file");

	// XXX wm in context is not set correctly after WM_read_file . crash
	// do it before for now, but is this correct with multiple windows?

	if(event>0) {
		if (G.sce[0]!=0 && (event==1)) {
			WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);
			WmFiles.WM_read_file(C, StringUtil.toJString(G.sce,0), op.reports);
		}
		else {
			RecentFile recent = (RecentFile)ListBaseUtil.BLI_findlink(G.recent_files, event-2);
			if(recent!=null) {
				WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);
				WmFiles.WM_read_file(C, recent.filename, op.reports);
			}
		}
	}
	return 0;
}};

public static wmOperatorType.Operator wm_recentfile_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_recentfile_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
	uiPopupMenu pup;
	uiLayout layout;

	pup= UIRegions.uiPupMenuBegin(C, "Open Recent", 0);
	layout= UIRegions.uiPupMenuLayout(pup);
	UILayout.uiItemsEnumO(layout, ((wmOperatorType)op.type).idname, "file");
	UIRegions.uiPupMenuEnd(C, pup);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

public static PropEnumItemFunc open_recentfile_itemf = new PropEnumItemFunc() {
public EnumPropertyItem[] itemEnum(bContext C, PointerRNA ptr, int[] free)
//static EnumPropertyItem *open_recentfile_itemf(bContext *C, PointerRNA *ptr, int *free)
{
//	EnumPropertyItem tmp = {0, "", 0, "", ""};
	EnumPropertyItem[] item= null;
//	struct RecentFile *recent;
//	int totitem= 0, i, ofs= 0;
//
//	if(G.sce[0]) {
//		tmp.value= 1;
//		tmp.identifier= G.sce;
//		tmp.name= G.sce;
//		RNA_enum_item_add(&item, &totitem, &tmp);
//		ofs = 1;
//	}
//
//	/* dynamically construct enum */
//	for(recent = G.recent_files.first, i=0; (i<U.recent_files) && (recent); recent = recent.next, i++) {
//		if(strcmp(recent.filename, G.sce)) {
//			tmp.value= i+ofs+1;
//			tmp.identifier= recent.filename;
//			tmp.name= recent.filename;
//			RNA_enum_item_add(&item, &totitem, &tmp);
//		}
//	}
//
//	RNA_enum_item_end(&item, &totitem);
//	*free= 1;

	return item;
}
public String getName() { return "open_recentfile_itemf"; }
};

private static EnumPropertyItem[] file_items= {
	new EnumPropertyItem(0, null, 0, null, null)};

public static OpFunc WM_OT_open_recentfile = new OpFunc() {
public void run(wmOperatorType ot)
//static void WM_OT_open_recentfile(wmOperatorType *ot)
{
	PropertyRNA prop;
//	static EnumPropertyItem file_items[]= {
//		{0, NULL, 0, NULL, NULL}};

	ot.name= "Open Recent File";
	ot.idname= "WM_OT_open_recentfile";

	ot.invoke= wm_recentfile_invoke;
	ot.exec= recentfile_exec;
	ot.poll= WM_operator_winactive;

	prop= RnaDefine.RNA_def_enum(ot.srna, "file", file_items, 1, "File", "");
	RnaDefine.RNA_def_enum_funcs(prop, open_recentfile_itemf);
}};

/* *************** open file **************** */

static void open_set_load_ui(wmOperator op)
{
	if(!RnaAccess.RNA_property_is_set(op.ptr, "load_ui"))
		RnaAccess.RNA_boolean_set(op.ptr, "load_ui", (U.flag & UserDefTypes.USER_FILENOUI)==0);
}

static void open_set_use_scripts(wmOperator op)
{
	if(!RnaAccess.RNA_property_is_set(op.ptr, "use_scripts")) {
		/* use G_SCRIPT_AUTOEXEC rather then the userpref because this means if
		 * the flag has been disabled from the command line, then opening
		 * from the menu wont enable this setting. */
		RnaAccess.RNA_boolean_set(op.ptr, "use_scripts", (G.f & Global.G_SCRIPT_AUTOEXEC)!=0);
	}
}

public static wmOperatorType.Operator wm_open_mainfile_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	System.out.println("wm_open_mainfile_invoke");
	String openname= StringUtil.toJString(G.main.name,0);

	/* if possible, get the name of the most recently used .blend file */
//	if (G.recent_files.first) {
//		struct RecentFile *recent = G.recent_files.first;
//		openname = recent.filepath;
//	}

	RnaAccess.RNA_string_set(op.ptr, "filepath", openname);
	open_set_load_ui(op);
	open_set_use_scripts(op);

	WmEventSystem.WM_event_add_fileselect(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	
	
	
//	System.out.println("wm_open_mainfile_invoke");
//	RnaAccess.RNA_string_set(op.ptr, "filename", StringUtil.toJString(G.sce,0));
//	WmEventSystem.WM_event_add_fileselect(C, op);
//	
////	JFileChooser fc = new JFileChooser();
////	if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
////		try {
////			// USIBlender.BIF_read_file(fc.getSelectedFile().toURI().toURL());
////			WmFiles.WM_read_file(C, fc.getSelectedFile().toURI()
////					.toURL().toString(), op.reports);
////		} catch (Exception ex) {
////			ex.printStackTrace();
////		}
////	}
//
//	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static wmOperatorType.Operator wm_open_mainfile_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
	System.out.println("wm_open_mainfile_exec");
	String path;

	path = RnaAccess.RNA_string_get(op.ptr, "filepath");
//	open_set_load_ui(op);
//	open_set_use_scripts(op);
//
//	if(RnaAccess.RNA_boolean_get(op.ptr, "load_ui"))
//		G.fileflags &= ~Global.G_FILE_NO_UI;
//	else
//		G.fileflags |= Global.G_FILE_NO_UI;
//		
//	if(RnaAccess.RNA_boolean_get(op.ptr, "use_scripts"))
//		G.f |= Global.G_SCRIPT_AUTOEXEC;
//	else
//		G.f &= ~Global.G_SCRIPT_AUTOEXEC;
	
	// XXX wm in context is not set correctly after WM_read_file -> crash
	// do it before for now, but is this correct with multiple windows?
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

	WmFiles.WM_read_file(C, path, op.reports);
	
	return WindowManagerTypes.OPERATOR_FINISHED;
	
	
	
////	char filename[FILE_MAX];
//	System.out.println("wm_open_mainfile_exec");
//	String filename;
//	filename = RnaAccess.RNA_string_get(op.ptr, "filename");
//
//	// XXX wm in context is not set correctly after WM_read_file . crash
//	// do it before for now, but is this correct with multiple windows?
//	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);
//
//	WmFiles.WM_read_file(C, filename, op.reports);
//
//	return 0;
}};

public static OpFunc WM_OT_open_mainfile = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Open Blender File";
	ot.idname= "WM_OT_open_mainfile";
	ot.description="Open a Blender file";
	
	ot.invoke= wm_open_mainfile_invoke;
	ot.exec= wm_open_mainfile_exec;
	ot.poll= WM_operator_winactive;
	
	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE, SpaceTypes.FILE_BLENDER, SpaceTypes.FILE_OPENFILE, WmTypes.WM_FILESEL_FILEPATH);

	RnaDefine.RNA_def_boolean(ot.srna, "load_ui", 1, "Load UI", "Load user interface setup in the .blend file");
	RnaDefine.RNA_def_boolean(ot.srna, "use_scripts", 1, "Trusted Source", "Allow blend file execute scripts automatically, default available from system preferences");
	
	
	
	
//	ot.name= "Open Blender File";
//	ot.idname= "WM_OT_open_mainfile";
//
//	ot.invoke= wm_open_mainfile_invoke;
//	ot.exec= wm_open_mainfile_exec;
//	ot.poll= WM_operator_winactive;
//
//	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE);
}};

public static wmOperatorType.Operator wm_recover_last_session_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_recover_last_session_exec(bContext *C, wmOperator *op)
{
//	char scestr[FILE_MAX], filename[FILE_MAX];
	byte[] scestr = new byte[Blender.FILE_MAX];
	byte[] filename = new byte[Blender.FILE_MAX];
	int save_over;

	/* back up some values */
	StringUtil.BLI_strncpy(scestr,0, G.sce,0, scestr.length);
	save_over = G.save_over;

	// XXX wm in context is not set correctly after WM_read_file . crash
	// do it before for now, but is this correct with multiple windows?
	WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_WINDOW, null);

	/* load file */
//	BLI_make_file_string("/", filename, btempdir, "quit.blend");
	WmFiles.WM_read_file(C, StringUtil.toJString(filename,0), op.reports);

	/* restore */
	G.save_over = save_over;
	StringUtil.BLI_strncpy(G.sce,0, scestr,0, G.sce.length);

	return 0;
}};

public static OpFunc WM_OT_recover_last_session = new OpFunc() {
public void run(wmOperatorType ot)
//static void WM_OT_recover_last_session(wmOperatorType *ot)
{
	ot.name= "Recover Last Session";
	ot.idname= "WM_OT_recover_last_session";

	ot.exec= wm_recover_last_session_exec;
	ot.poll= WM_operator_winactive;
}};

/* *************** recover auto save **************** */

public static wmOperatorType.Operator wm_recover_auto_save_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	char path[FILE_MAX];
//
//	RNA_string_get(op->ptr, "filepath", path);
//
//	G.fileflags |= G_FILE_RECOVER;
//
//	// XXX wm in context is not set correctly after WM_read_file -> crash
//	// do it before for now, but is this correct with multiple windows?
//	WM_event_add_notifier(C, NC_WINDOW, NULL);
//
//	/* load file */
//	WM_read_file(C, path, op->reports);
//
//	G.fileflags &= ~G_FILE_RECOVER;

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static wmOperatorType.Operator wm_recover_auto_save_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
{
//	char filename[FILE_MAX];
//
//	wm_autosave_location(filename);
//	RNA_string_set(op->ptr, "filepath", filename);
//	WM_event_add_fileselect(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc WM_OT_recover_auto_save = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Recover Auto Save";
	ot.idname= "WM_OT_recover_auto_save";
	ot.description="Open an automatically saved file to recover it";
	
	ot.exec= wm_recover_auto_save_exec;
	ot.invoke= wm_recover_auto_save_invoke;
	ot.poll= WM_operator_winactive;

//	WM_operator_properties_filesel(ot, BLENDERFILE, FILE_BLENDER, FILE_OPENFILE, WM_FILESEL_FILEPATH);
}};

//static void save_set_compress(wmOperator *op)
//{
//	if(!RNA_property_is_set(op.ptr, "compress")) {
//		if(G.save_over) /* keep flag for existing file */
//			RNA_boolean_set(op.ptr, "compress", G.fileflags & G_FILE_COMPRESS);
//		else /* use userdef for new file */
//			RNA_boolean_set(op.ptr, "compress", U.flag & USER_FILECOMPRESS);
//	}
//}
//
public static wmOperatorType.Operator wm_save_as_mainfile_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_save_as_mainfile_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//	char name[FILE_MAX];
	byte[] name = new byte[Blender.FILE_MAX];

//	save_set_compress(op);

	StringUtil.BLI_strncpy(name,0, G.sce,0, Blender.FILE_MAX);
//	untitled(name);
	RnaAccess.RNA_string_set(op.ptr, "filename", StringUtil.toJString(name,0));

	WmEventSystem.WM_event_add_fileselect(C, op);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

/* function used for WM_OT_save_mainfile too */
public static wmOperatorType.Operator wm_save_as_mainfile_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_save_as_mainfile_exec(bContext *C, wmOperator *op)
{
//	char filename[FILE_MAX];
//	int compress;
//
//	save_set_compress(op);
//	compress= RNA_boolean_get(op.ptr, "compress");
//
//	if(RNA_property_is_set(op.ptr, "filename"))
//		RNA_string_get(op.ptr, "filename", filename);
//	else {
//		BLI_strncpy(filename, G.sce, FILE_MAX);
//		untitled(filename);
//	}
//
//	WM_write_file(C, filename, compress, op.reports);
//
//	WM_event_add_notifier(C, NC_WM|ND_FILESAVE, NULL);

	return 0;
}};

public static OpFunc WM_OT_save_as_mainfile = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Save As Blender File";
	ot.idname= "WM_OT_save_as_mainfile";
	ot.description="Save the current file in the desired location";
	
	ot.invoke= wm_save_as_mainfile_invoke;
	ot.exec= wm_save_as_mainfile_exec;
//	ot.check= blend_save_check;
	/* ommit window poll so this can work in background mode */

	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE, SpaceTypes.FILE_BLENDER, SpaceTypes.FILE_SAVE, WmTypes.WM_FILESEL_FILEPATH);
	RnaDefine.RNA_def_boolean(ot.srna, "compress", 0, "Compress", "Write compressed .blend file");
	RnaDefine.RNA_def_boolean(ot.srna, "relative_remap", 1, "Remap Relative", "Remap relative paths when saving in a different directory");
	RnaDefine.RNA_def_boolean(ot.srna, "copy", 0, "Save Copy", "Save a copy of the actual working state but does not make saved file active.");
	
//	ot.name= "Save As Blender File";
//	ot.idname= "WM_OT_save_as_mainfile";
//
//	ot.invoke= wm_save_as_mainfile_invoke;
//	ot.exec= wm_save_as_mainfile_exec;
//	ot.poll= WM_operator_winactive;
//
//	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE);
//	RnaDefine.RNA_def_boolean(ot.srna, "compress", 0, "Compress", "Write compressed .blend file.");
}};

/* *************** Save file directly ******** */

public static wmOperatorType.Operator wm_save_mainfile_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_save_mainfile_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//	char name[FILE_MAX];
	byte[] name = new byte[Blender.FILE_MAX];

//	save_set_compress(op);

	StringUtil.BLI_strncpy(name,0, G.sce,0, Blender.FILE_MAX);
//	untitled(name);
	RnaAccess.RNA_string_set(op.ptr, "filename", StringUtil.toJString(name,0));
//	uiPupMenuSaveOver(C, op, name);

	return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
}};

public static OpFunc WM_OT_save_mainfile = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Save Blender File";
	ot.idname= "WM_OT_save_mainfile";
	ot.description="Save the current Blender file";
	
	ot.invoke= wm_save_mainfile_invoke;
	ot.exec= wm_save_as_mainfile_exec;
//	ot.check= blend_save_check;
	ot.poll= null;
	
	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE, SpaceTypes.FILE_BLENDER, SpaceTypes.FILE_SAVE, WmTypes.WM_FILESEL_FILEPATH);
	RnaDefine.RNA_def_boolean(ot.srna, "compress", 0, "Compress", "Write compressed .blend file");
	RnaDefine.RNA_def_boolean(ot.srna, "relative_remap", 0, "Remap Relative", "Remap relative paths when saving in a different directory");
	
//	ot.name= "Save Blender File";
//	ot.idname= "WM_OT_save_mainfile";
//
//	ot.invoke= wm_save_mainfile_invoke;
//	ot.exec= wm_save_as_mainfile_exec;
//	ot.poll= WM_operator_winactive;
//
//	WM_operator_properties_filesel(ot, SpaceTypes.FOLDERFILE|SpaceTypes.BLENDERFILE);
//	RnaDefine.RNA_def_boolean(ot.srna, "compress", 0, "Compress", "Write compressed .blend file.");
}};


/* *********************** */

public static OpFunc WM_OT_window_fullscreen_toggle = new OpFunc() {
public void run(wmOperatorType ot)
{
    ot.name= "Toggle Fullscreen";
    ot.idname= "WM_OT_window_fullscreen_toggle";

    ot.invoke= WM_operator_confirm;
    ot.exec= WmWindow.wm_window_fullscreen_toggle_op;
    ot.poll= WM_operator_winactive;
}};

public static wmOperatorType.Operator wm_exit_blender_op = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int wm_exit_blender_op(bContext *C, wmOperator *op)
{
	Wm.WM_operator_free(op);

	//WmInitExit.WM_exit(C);
	bContext.CTX_wm_manager_close(C);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static OpFunc WM_OT_quit_blender = new OpFunc() {
public void run(wmOperatorType ot)
{
	ot.name= "Quit Blender";
	ot.idname= "WM_OT_quit_blender";
	ot.description= "Quit Blender";

	ot.invoke= WM_operator_confirm;
	ot.exec= wm_exit_blender_op;
	ot.poll= WM_operator_winactive;
}};

///* ************ default paint cursors, draw always around cursor *********** */
///*
// - returns handler to free
// - poll(bContext): returns 1 if draw should happen
// - draw(bContext): drawing callback for paint cursor
//*/
//
//void *WM_paint_cursor_activate(wmWindowManager *wm, int (*poll)(bContext *C),
//			       void (*draw)(bContext *C, int, int, void *customdata), void *customdata)
//{
//	wmPaintCursor *pc= MEM_callocN(sizeof(wmPaintCursor), "paint cursor");
//
//	BLI_addtail(&wm.paintcursors, pc);
//
//	pc.customdata = customdata;
//	pc.poll= poll;
//	pc.draw= draw;
//
//	return pc;
//}
//
//void WM_paint_cursor_end(wmWindowManager *wm, void *handle)
//{
//	wmPaintCursor *pc;
//
//	for(pc= wm.paintcursors.first; pc; pc= pc.next) {
//		if(pc == (wmPaintCursor *)handle) {
//			BLI_remlink(&wm.paintcursors, pc);
//			MEM_freeN(pc);
//			return;
//		}
//	}
//}
//
///* ************ window gesture operator-callback definitions ************** */
///*
// * These are default callbacks for use in operators requiring gesture input
// */
//
///* **************** Border gesture *************** */
//
///* Border gesture has two types:
//   1) WM_GESTURE_CROSS_RECT: starts a cross, on mouse click it changes to border
//   2) WM_GESTURE_RECT: starts immediate as a border, on mouse click or release it ends
//
//   It stores 4 values (xmin, xmax, ymin, ymax) and event it ended with (event_type)
//*/
//
//static int border_apply(bContext *C, wmOperator *op, int event_type, int event_orig)
//{
//	wmGesture *gesture= op.customdata;
//	rcti *rect= gesture.customdata;
//
//	if(rect.xmin > rect.xmax)
//		SWAP(int, rect.xmin, rect.xmax);
//	if(rect.ymin > rect.ymax)
//		SWAP(int, rect.ymin, rect.ymax);
//
//	if(rect.xmin==rect.xmax || rect.ymin==rect.ymax)
//		return 0;
//
//	/* operator arguments and storage. */
//	RNA_int_set(op.ptr, "xmin", rect.xmin);
//	RNA_int_set(op.ptr, "ymin", rect.ymin);
//	RNA_int_set(op.ptr, "xmax", rect.xmax);
//	RNA_int_set(op.ptr, "ymax", rect.ymax);
//
//	/* XXX weak; border should be configured for this without reading event types */
//	if( RNA_struct_find_property(op.ptr, "event_type") ) {
//		if(ELEM4(event_orig, EVT_TWEAK_L, EVT_TWEAK_R, EVT_TWEAK_A, EVT_TWEAK_S))
//			event_type= LEFTMOUSE;
//
//		RNA_int_set(op.ptr, "event_type", event_type);
//	}
//	op.type.exec(C, op);
//
//	return 1;
//}
//
//static void wm_gesture_end(bContext *C, wmOperator *op)
//{
//	wmGesture *gesture= op.customdata;
//
//	WM_gesture_end(C, gesture);	/* frees gesture itself, and unregisters from window */
//	op.customdata= NULL;
//
//	ED_area_tag_redraw(CTX_wm_area(C));
//
//	if( RNA_struct_find_property(op.ptr, "cursor") )
//		WM_cursor_restore(CTX_wm_window(C));
//}
//
//int WM_border_select_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	if(WM_key_event_is_tweak(event.type))
//		op.customdata= WM_gesture_new(C, event, WM_GESTURE_RECT);
//	else
//		op.customdata= WM_gesture_new(C, event, WM_GESTURE_CROSS_RECT);
//
//	/* add modal handler */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	wm_gesture_tag_redraw(C);
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//int WM_border_select_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmGesture *gesture= op.customdata;
//	rcti *rect= gesture.customdata;
//	int sx, sy;
//
//	switch(event.type) {
//		case MOUSEMOVE:
//
//			wm_subwindow_getorigin(CTX_wm_window(C), gesture.swinid, &sx, &sy);
//
//			if(gesture.type==WM_GESTURE_CROSS_RECT && gesture.mode==0) {
//				rect.xmin= rect.xmax= event.x - sx;
//				rect.ymin= rect.ymax= event.y - sy;
//			}
//			else {
//				rect.xmax= event.x - sx;
//				rect.ymax= event.y - sy;
//			}
//
//			wm_gesture_tag_redraw(C);
//
//			break;
//
//		case LEFTMOUSE:
//		case MIDDLEMOUSE:
//		case RIGHTMOUSE:
//			if(event.val==KM_PRESS) {
//				if(gesture.type==WM_GESTURE_CROSS_RECT && gesture.mode==0) {
//					gesture.mode= 1;
//					wm_gesture_tag_redraw(C);
//				}
//			}
//			else {
//				if(border_apply(C, op, event.type, gesture.event_type)) {
//					wm_gesture_end(C, op);
//					return OPERATOR_FINISHED;
//				}
//				wm_gesture_end(C, op);
//				return OPERATOR_CANCELLED;
//			}
//			break;
//		case ESCKEY:
//			wm_gesture_end(C, op);
//			return OPERATOR_CANCELLED;
//	}
//	return OPERATOR_RUNNING_MODAL;
//}
//
///* **************** circle gesture *************** */
///* works now only for selection or modal paint stuff, calls exec while hold mouse, exit on release */
//
//int WM_gesture_circle_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	op.customdata= WM_gesture_new(C, event, WM_GESTURE_CIRCLE);
//
//	/* add modal handler */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	wm_gesture_tag_redraw(C);
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//static void gesture_circle_apply(bContext *C, wmOperator *op)
//{
//	wmGesture *gesture= op.customdata;
//	rcti *rect= gesture.customdata;
//
//	/* operator arguments and storage. */
//	RNA_int_set(op.ptr, "x", rect.xmin);
//	RNA_int_set(op.ptr, "y", rect.ymin);
//	RNA_int_set(op.ptr, "radius", rect.xmax);
//
//	if(op.type.exec)
//		op.type.exec(C, op);
//}
//
//int WM_gesture_circle_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmGesture *gesture= op.customdata;
//	rcti *rect= gesture.customdata;
//	int sx, sy;
//
//	switch(event.type) {
//		case MOUSEMOVE:
//
//			wm_subwindow_getorigin(CTX_wm_window(C), gesture.swinid, &sx, &sy);
//
//			rect.xmin= event.x - sx;
//			rect.ymin= event.y - sy;
//
//			wm_gesture_tag_redraw(C);
//
//			if(gesture.mode)
//				gesture_circle_apply(C, op);
//
//			break;
//		case WHEELUPMOUSE:
//			rect.xmax += 2 + rect.xmax/10;
//			wm_gesture_tag_redraw(C);
//			break;
//		case WHEELDOWNMOUSE:
//			rect.xmax -= 2 + rect.xmax/10;
//			if(rect.xmax < 1) rect.xmax= 1;
//			wm_gesture_tag_redraw(C);
//			break;
//		case LEFTMOUSE:
//		case MIDDLEMOUSE:
//		case RIGHTMOUSE:
//			if(event.val==0) {	/* key release */
//				wm_gesture_end(C, op);
//				return OPERATOR_FINISHED;
//			}
//			else {
//				if( RNA_struct_find_property(op.ptr, "event_type") )
//					RNA_int_set(op.ptr, "event_type", event.type);
//
//				/* apply first click */
//				gesture_circle_apply(C, op);
//				gesture.mode= 1;
//			}
//			break;
//		case ESCKEY:
//			wm_gesture_end(C, op);
//			return OPERATOR_CANCELLED;
//	}
//	return OPERATOR_RUNNING_MODAL;
//}
//
//#if 0
///* template to copy from */
//void WM_OT_circle_gesture(wmOperatorType *ot)
//{
//	ot.name= "Circle Gesture";
//	ot.idname= "WM_OT_circle_gesture";
//
//	ot.invoke= WM_gesture_circle_invoke;
//	ot.modal= WM_gesture_circle_modal;
//
//	ot.poll= WM_operator_winactive;
//
//	RNA_def_property(ot.srna, "x", PROP_INT, PROP_NONE);
//	RNA_def_property(ot.srna, "y", PROP_INT, PROP_NONE);
//	RNA_def_property(ot.srna, "radius", PROP_INT, PROP_NONE);
//
//}
//#endif
//
///* **************** Tweak gesture *************** */
//
//static void tweak_gesture_modal(bContext *C, wmEvent *event)
//{
//	wmWindow *window= CTX_wm_window(C);
//	wmGesture *gesture= window.tweak;
//	rcti *rect= gesture.customdata;
//	int sx, sy, val;
//
//	switch(event.type) {
//		case MOUSEMOVE:
//
//			wm_subwindow_getorigin(window, gesture.swinid, &sx, &sy);
//
//			rect.xmax= event.x - sx;
//			rect.ymax= event.y - sy;
//
//			if((val= wm_gesture_evaluate(C, gesture))) {
//				wmEvent event;
//
//				event= *(window.eventstate);
//				if(gesture.event_type==LEFTMOUSE)
//					event.type= EVT_TWEAK_L;
//				else if(gesture.event_type==RIGHTMOUSE)
//					event.type= EVT_TWEAK_R;
//				else
//					event.type= EVT_TWEAK_M;
//				event.val= val;
//				/* mouse coords! */
//				wm_event_add(window, &event);
//
//				WM_gesture_end(C, gesture);	/* frees gesture itself, and unregisters from window */
//				window.tweak= NULL;
//			}
//
//			break;
//
//		case LEFTMOUSE:
//		case RIGHTMOUSE:
//		case MIDDLEMOUSE:
//			if(gesture.event_type==event.type) {
//				WM_gesture_end(C, gesture);
//				window.tweak= NULL;
//
//				/* when tweak fails we should give the other keymap entries a chance */
//				event.val= KM_RELEASE;
//			}
//			break;
//		default:
//			WM_gesture_end(C, gesture);
//			window.tweak= NULL;
//	}
//}

/* standard tweak, called after window handlers passed on event */
public static void wm_tweakevent_test(bContext C, wmEvent event, int action)
{
	wmWindow win= bContext.CTX_wm_window(C);

	if(win.tweak==null) {
		if(bContext.CTX_wm_region(C)!=null) {
			if(event.val==WmTypes.KM_PRESS) { // pressed
//				if(event.type==WmEventTypes.LEFTMOUSE || event.type==WmEventTypes.MIDDLEMOUSE || event.type==WmEventTypes.RIGHTMOUSE)
//					win.tweak= WM_gesture_new(C, event, WmTypes.WM_GESTURE_TWEAK);
			}
		}
	}
	else {
		if(action==WmEventSystem.WM_HANDLER_BREAK) {
//			WM_gesture_end(C, win.tweak);
			win.tweak= null;
		}
//		else
//			tweak_gesture_modal(C, event);
	}
}

///* *********************** lasso gesture ****************** */
//
//int WM_gesture_lasso_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	op.customdata= WM_gesture_new(C, event, WM_GESTURE_LASSO);
//
//	/* add modal handler */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	wm_gesture_tag_redraw(C);
//
//	if( RNA_struct_find_property(op.ptr, "cursor") )
//		WM_cursor_modal(CTX_wm_window(C), RNA_int_get(op.ptr, "cursor"));
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//int WM_gesture_lines_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	op.customdata= WM_gesture_new(C, event, WM_GESTURE_LINES);
//
//	/* add modal handler */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	wm_gesture_tag_redraw(C);
//
//	if( RNA_struct_find_property(op.ptr, "cursor") )
//		WM_cursor_modal(CTX_wm_window(C), RNA_int_get(op.ptr, "cursor"));
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
//
//static void gesture_lasso_apply(bContext *C, wmOperator *op, int event_type)
//{
//	wmGesture *gesture= op.customdata;
//	PointerRNA itemptr;
//	float loc[2];
//	int i;
//	short *lasso= gesture.customdata;
//
//	/* operator storage as path. */
//
//	for(i=0; i<gesture.points; i++, lasso+=2) {
//		loc[0]= lasso[0];
//		loc[1]= lasso[1];
//		RNA_collection_add(op.ptr, "path", &itemptr);
//		RNA_float_set_array(&itemptr, "loc", loc);
//	}
//
//	wm_gesture_end(C, op);
//
//	if(op.type.exec)
//		op.type.exec(C, op);
//
//}
//
//int WM_gesture_lasso_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmGesture *gesture= op.customdata;
//	int sx, sy;
//
//	switch(event.type) {
//		case MOUSEMOVE:
//
//			wm_gesture_tag_redraw(C);
//
//			wm_subwindow_getorigin(CTX_wm_window(C), gesture.swinid, &sx, &sy);
//			if(gesture.points < WM_LASSO_MAX_POINTS) {
//				short *lasso= gesture.customdata;
//				lasso += 2 * gesture.points;
//				lasso[0] = event.x - sx;
//				lasso[1] = event.y - sy;
//				gesture.points++;
//			}
//			else {
//				gesture_lasso_apply(C, op, event.type);
//				return OPERATOR_FINISHED;
//			}
//			break;
//
//		case LEFTMOUSE:
//		case MIDDLEMOUSE:
//		case RIGHTMOUSE:
//			if(event.val==0) {	/* key release */
//				gesture_lasso_apply(C, op, event.type);
//				return OPERATOR_FINISHED;
//			}
//			break;
//		case ESCKEY:
//			wm_gesture_end(C, op);
//			return OPERATOR_CANCELLED;
//	}
//	return OPERATOR_RUNNING_MODAL;
//}
//
//int WM_gesture_lines_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	return WM_gesture_lasso_modal(C, op, event);
//}
//
//#if 0
///* template to copy from */
//
//static int gesture_lasso_exec(bContext *C, wmOperator *op)
//{
//	RNA_BEGIN(op.ptr, itemptr, "path") {
//		float loc[2];
//
//		RNA_float_get_array(&itemptr, "loc", loc);
//		printf("Location: %f %f\n", loc[0], loc[1]);
//	}
//	RNA_END;
//
//	return OPERATOR_FINISHED;
//}
//
//void WM_OT_lasso_gesture(wmOperatorType *ot)
//{
//	PropertyRNA *prop;
//
//	ot.name= "Lasso Gesture";
//	ot.idname= "WM_OT_lasso_gesture";
//
//	ot.invoke= WM_gesture_lasso_invoke;
//	ot.modal= WM_gesture_lasso_modal;
//	ot.exec= gesture_lasso_exec;
//
//	ot.poll= WM_operator_winactive;
//
//	prop= RNA_def_property(ot.srna, "path", PROP_COLLECTION, PROP_NONE);
//	RNA_def_property_struct_runtime(prop, &RNA_OperatorMousePath);
//}
//#endif
//
///* *********************** radial control ****************** */
//
//const int WM_RADIAL_CONTROL_DISPLAY_SIZE = 200;
//
//typedef struct wmRadialControl {
//	int mode;
//	float initial_value, value, max_value;
//	int initial_mouse[2];
//	void *cursor;
//	GLuint tex;
//} wmRadialControl;
//
//static void wm_radial_control_paint(bContext *C, int x, int y, void *customdata)
//{
//	wmRadialControl *rc = (wmRadialControl*)customdata;
//	ARegion *ar = CTX_wm_region(C);
//	float r1=0.0f, r2=0.0f, r3=0.0f, angle=0.0f;
//
//	/* Keep cursor in the original place */
//	x = rc.initial_mouse[0] - ar.winrct.xmin;
//	y = rc.initial_mouse[1] - ar.winrct.ymin;
//
//	glPushMatrix();
//
//	glTranslatef((float)x, (float)y, 0.0f);
//
//	if(rc.mode == WM_RADIALCONTROL_SIZE) {
//		r1= rc.value;
//		r2= rc.initial_value;
//		r3= r1;
//	} else if(rc.mode == WM_RADIALCONTROL_STRENGTH) {
//		r1= (1 - rc.value) * WM_RADIAL_CONTROL_DISPLAY_SIZE;
//		r2= WM_RADIAL_CONTROL_DISPLAY_SIZE;
//		r3= WM_RADIAL_CONTROL_DISPLAY_SIZE;
//	} else if(rc.mode == WM_RADIALCONTROL_ANGLE) {
//		r1= r2= WM_RADIAL_CONTROL_DISPLAY_SIZE;
//		r3= WM_RADIAL_CONTROL_DISPLAY_SIZE;
//		angle = rc.value;
//	}
//
//	glColor4ub(255, 255, 255, 128);
//	glEnable( GL_LINE_SMOOTH );
//	glEnable(GL_BLEND);
//
//	if(rc.mode == WM_RADIALCONTROL_ANGLE)
//		fdrawline(0, 0, WM_RADIAL_CONTROL_DISPLAY_SIZE, 0);
//
//	if(rc.tex) {
//		const float str = rc.mode == WM_RADIALCONTROL_STRENGTH ? (rc.value + 0.5) : 1;
//
//		if(rc.mode == WM_RADIALCONTROL_ANGLE) {
//			glRotatef(angle, 0, 0, 1);
//			fdrawline(0, 0, WM_RADIAL_CONTROL_DISPLAY_SIZE, 0);
//		}
//
//		glBindTexture(GL_TEXTURE_2D, rc.tex);
//
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//
//		glEnable(GL_TEXTURE_2D);
//		glBegin(GL_QUADS);
//		glColor4f(0,0,0, str);
//		glTexCoord2f(0,0);
//		glVertex2f(-r3, -r3);
//		glTexCoord2f(1,0);
//		glVertex2f(r3, -r3);
//		glTexCoord2f(1,1);
//		glVertex2f(r3, r3);
//		glTexCoord2f(0,1);
//		glVertex2f(-r3, r3);
//		glEnd();
//		glDisable(GL_TEXTURE_2D);
//	}
//
//	glColor4ub(255, 255, 255, 128);
//	glutil_draw_lined_arc(0.0, M_PI*2.0, r1, 40);
//	glutil_draw_lined_arc(0.0, M_PI*2.0, r2, 40);
//	glDisable(GL_BLEND);
//	glDisable( GL_LINE_SMOOTH );
//
//	glPopMatrix();
//}
//
//int WM_radial_control_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmRadialControl *rc = (wmRadialControl*)op.customdata;
//	int mode, initial_mouse[2], delta[2];
//	float dist;
//	double new_value = RNA_float_get(op.ptr, "new_value");
//	int ret = OPERATOR_RUNNING_MODAL;
//
//	mode = RNA_int_get(op.ptr, "mode");
//	RNA_int_get_array(op.ptr, "initial_mouse", initial_mouse);
//
//	switch(event.type) {
//	case MOUSEMOVE:
//		delta[0]= initial_mouse[0] - event.x;
//		delta[1]= initial_mouse[1] - event.y;
//		dist= sqrt(delta[0]*delta[0]+delta[1]*delta[1]);
//
//		if(mode == WM_RADIALCONTROL_SIZE)
//			new_value = dist;
//		else if(mode == WM_RADIALCONTROL_STRENGTH) {
//			new_value = 1 - dist / WM_RADIAL_CONTROL_DISPLAY_SIZE;
//		} else if(mode == WM_RADIALCONTROL_ANGLE)
//			new_value = ((int)(atan2(delta[1], delta[0]) * (180.0 / M_PI)) + 180);
//
//		if(event.ctrl) {
//			if(mode == WM_RADIALCONTROL_STRENGTH)
//				new_value = ((int)(new_value * 100) / 10*10) / 100.0f;
//			else
//				new_value = ((int)new_value + 5) / 10*10;
//		}
//
//		break;
//	case ESCKEY:
//	case RIGHTMOUSE:
//		ret = OPERATOR_CANCELLED;
//		break;
//	case LEFTMOUSE:
//	case PADENTER:
//		op.type.exec(C, op);
//		ret = OPERATOR_FINISHED;
//		break;
//	}
//
//	/* Clamp */
//	if(new_value > rc.max_value)
//		new_value = rc.max_value;
//	else if(new_value < 0)
//		new_value = 0;
//
//	/* Update paint data */
//	rc.value = new_value;
//
//	RNA_float_set(op.ptr, "new_value", new_value);
//
//	if(ret != OPERATOR_RUNNING_MODAL) {
//		WM_paint_cursor_end(CTX_wm_manager(C), rc.cursor);
//		MEM_freeN(rc);
//	}
//
//	ED_region_tag_redraw(CTX_wm_region(C));
//
//	return ret;
//}
//
///* Expects the operator customdata to be an ImBuf (or NULL) */
//int WM_radial_control_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	wmRadialControl *rc = MEM_callocN(sizeof(wmRadialControl), "radial control");
//	int mode = RNA_int_get(op.ptr, "mode");
//	float initial_value = RNA_float_get(op.ptr, "initial_value");
//	int mouse[2] = {event.x, event.y};
//
//	if(mode == WM_RADIALCONTROL_SIZE) {
//		rc.max_value = 200;
//		mouse[0]-= initial_value;
//	}
//	else if(mode == WM_RADIALCONTROL_STRENGTH) {
//		rc.max_value = 1;
//		mouse[0]-= WM_RADIAL_CONTROL_DISPLAY_SIZE * (1 - initial_value);
//	}
//	else if(mode == WM_RADIALCONTROL_ANGLE) {
//		rc.max_value = 360;
//		mouse[0]-= WM_RADIAL_CONTROL_DISPLAY_SIZE * cos(initial_value);
//		mouse[1]-= WM_RADIAL_CONTROL_DISPLAY_SIZE * sin(initial_value);
//		initial_value *= 180.0f/M_PI;
//	}
//
//	if(op.customdata) {
//		ImBuf *im = (ImBuf*)op.customdata;
//		/* Build GL texture */
//		glGenTextures(1, &rc.tex);
//		glBindTexture(GL_TEXTURE_2D, rc.tex);
//		glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, im.x, im.y, 0, GL_ALPHA, GL_FLOAT, im.rect_float);
//		MEM_freeN(im.rect_float);
//		MEM_freeN(im);
//	}
//
//	RNA_int_set_array(op.ptr, "initial_mouse", mouse);
//	RNA_float_set(op.ptr, "new_value", initial_value);
//
//	op.customdata = rc;
//	rc.mode = mode;
//	rc.initial_value = initial_value;
//	rc.initial_mouse[0] = mouse[0];
//	rc.initial_mouse[1] = mouse[1];
//	rc.cursor = WM_paint_cursor_activate(CTX_wm_manager(C), op.type.poll,
//					      wm_radial_control_paint, op.customdata);
//
//	/* add modal handler */
//	WM_event_add_modal_handler(C, &CTX_wm_window(C).handlers, op);
//
//	WM_radial_control_modal(C, op, event);
//
//	return OPERATOR_RUNNING_MODAL;
//}
//
///* Gets a descriptive string of the operation */
//void WM_radial_control_string(wmOperator *op, char str[], int maxlen)
//{
//	int mode = RNA_int_get(op.ptr, "mode");
//	float v = RNA_float_get(op.ptr, "new_value");
//
//	if(mode == WM_RADIALCONTROL_SIZE)
//		sprintf(str, "Size: %d", (int)v);
//	else if(mode == WM_RADIALCONTROL_STRENGTH)
//		sprintf(str, "Strength: %d", (int)v);
//	else if(mode == WM_RADIALCONTROL_ANGLE)
//		sprintf(str, "Angle: %d", (int)(v * 180.0f/M_PI));
//}
//
///** Important: this doesn't define an actual operator, it
//    just sets up the common parts of the radial control op. **/
//void WM_OT_radial_control_partial(wmOperatorType *ot)
//{
//	static EnumPropertyItem prop_mode_items[] = {
//		{WM_RADIALCONTROL_SIZE, "SIZE", 0, "Size", ""},
//		{WM_RADIALCONTROL_STRENGTH, "STRENGTH", 0, "Strength", ""},
//		{WM_RADIALCONTROL_ANGLE, "ANGLE", 0, "Angle", ""},
//		{0, NULL, 0, NULL, NULL}};
//
//	/* Should be set in custom invoke() */
//	RNA_def_float(ot.srna, "initial_value", 0, 0, FLT_MAX, "Initial Value", "", 0, FLT_MAX);
//
//	/* Set internally, should be used in custom exec() to get final value */
//	RNA_def_float(ot.srna, "new_value", 0, 0, FLT_MAX, "New Value", "", 0, FLT_MAX);
//
//	/* Should be set before calling operator */
//	RNA_def_enum(ot.srna, "mode", prop_mode_items, 0, "Mode", "");
//
//	/* Internal */
//	RNA_def_int_vector(ot.srna, "initial_mouse", 2, NULL, INT_MIN, INT_MAX, "initial_mouse", "", INT_MIN, INT_MAX);
//}
//
///* ************************** timer for testing ***************** */
//
///* uses no type defines, fully local testing function anyway... ;) */
//
//static int ten_timer_exec(bContext *C, wmOperator *op)
//{
//	ARegion *ar= CTX_wm_region(C);
//	double stime= PIL_check_seconds_timer();
//	int type = RNA_int_get(op.ptr, "type");
//	int a, time;
//	char tmpstr[128];
//
//	WM_cursor_wait(1);
//
//	for(a=0; a<10; a++) {
//		if (type==0) {
//			ED_region_do_draw(C, ar);
//		}
//		else if (type==1) {
//			wmWindow *win= CTX_wm_window(C);
//
//			ED_region_tag_redraw(ar);
//			wm_draw_update(C);
//
//			CTX_wm_window_set(C, win);	/* XXX context manipulation warning! */
//		}
//		else if (type==2) {
//			wmWindow *win= CTX_wm_window(C);
//			ScrArea *sa;
//
//			for(sa= CTX_wm_screen(C).areabase.first; sa; sa= sa.next)
//				ED_area_tag_redraw(sa);
//			wm_draw_update(C);
//
//			CTX_wm_window_set(C, win);	/* XXX context manipulation warning! */
//		}
//		else if (type==3) {
//			Scene *scene= CTX_data_scene(C);
//
//			if(a & 1) scene.r.cfra--;
//			else scene.r.cfra++;
//			scene_update_for_newframe(scene, scene.lay);
//		}
//		else {
//			ED_undo_pop(C);
//			ED_undo_redo(C);
//		}
//	}
//
//	time= (int) ((PIL_check_seconds_timer()-stime)*1000);
//
//	if(type==0) sprintf(tmpstr, "10 x Draw Region: %d ms", time);
//	if(type==1) sprintf(tmpstr, "10 x Draw Region and Swap: %d ms", time);
//	if(type==2) sprintf(tmpstr, "10 x Draw Window and Swap: %d ms", time);
//	if(type==3) sprintf(tmpstr, "Anim Step: %d ms", time);
//	if(type==4) sprintf(tmpstr, "10 x Undo/Redo: %d ms", time);
//
//	WM_cursor_wait(0);
//
//	uiPupMenuNotice(C, tmpstr);
//
//	return OPERATOR_FINISHED;
//}
//
//static void WM_OT_ten_timer(wmOperatorType *ot)
//{
//	static EnumPropertyItem prop_type_items[] = {
//	{0, "DRAW", 0, "Draw Region", ""},
//	{1, "DRAWSWAP", 0, "Draw Region + Swap", ""},
//	{2, "DRAWWINSWAP", 0, "Draw Window + Swap", ""},
//	{3, "ANIMSTEP", 0, "Anim Step", ""},
//	{4, "UNDO", 0, "Undo/Redo", ""},
//	{0, NULL, 0, NULL, NULL}};
//
//	ot.name= "Ten Timer";
//	ot.idname= "WM_OT_ten_timer";
//
//	ot.invoke= WM_menu_invoke;
//	ot.exec= ten_timer_exec;
//	ot.poll= WM_operator_winactive;
//
//	RNA_def_enum(ot.srna, "type", prop_type_items, 0, "Type", "");
//
//}



/* ******************************************************* */

/* called on initialize WM_exit() */
public static void wm_operatortype_free()
{
	ListBaseUtil.BLI_freelistN(global_ops);
}

/* called on initialize WM_init() */
public static void wm_operatortype_init()
{
	WM_operatortype_append(WM_OT_window_duplicate);
	WM_operatortype_append(WM_OT_read_homefile);
	WM_operatortype_append(WM_OT_read_factory_settings);
	WM_operatortype_append(WM_OT_save_homefile);
	WM_operatortype_append(WM_OT_window_fullscreen_toggle);
	WM_operatortype_append(WM_OT_quit_blender);
	WM_operatortype_append(WM_OT_open_mainfile);
	WM_operatortype_append(WM_OT_link_append);
	WM_operatortype_append(WM_OT_recover_last_session);
	WM_operatortype_append(WM_OT_recover_auto_save);
	WM_operatortype_append(WM_OT_save_as_mainfile);
	WM_operatortype_append(WM_OT_save_mainfile);
//	WM_operatortype_append(WM_OT_redraw_timer);
//	WM_operatortype_append(WM_OT_memory_statistics);
//	WM_operatortype_append(WM_OT_debug_menu);
//	WM_operatortype_append(WM_OT_splash);
//	WM_operatortype_append(WM_OT_search_menu);
//	WM_operatortype_append(WM_OT_call_menu);
//	WM_operatortype_append(WM_OT_radial_control);
//#if defined(WIN32)
//	WM_operatortype_append(WM_OT_console_toggle);
//#endif

//#ifdef WITH_COLLADA
//	/* XXX: move these */
//	WM_operatortype_append(WM_OT_collada_export);
//	WM_operatortype_append(WM_OT_collada_import);
//#endif
}

/* default keymap for windows and screens, only call once per WM */
public static void wm_window_keymap(wmKeyConfig keyconf)
{
	wmKeyMap keymap= WmKeymap.WM_keymap_find(keyconf, "Window", 0, 0);
	wmKeyMapItem kmi;
	
	/* note, this doesn't replace existing keymap items */
	WmKeymap.WM_keymap_verify_item(keymap, "WM_OT_window_duplicate", WKEY, KM_PRESS, KM_CTRL|KM_ALT, 0);
//	#ifdef __APPLE__
//	WM_keymap_add_item(keymap, "WM_OT_read_homefile", NKEY, KM_PRESS, KM_OSKEY, 0);
//	WM_keymap_add_menu(keymap, "INFO_MT_file_open_recent", OKEY, KM_PRESS, KM_SHIFT|KM_OSKEY, 0);
//	WM_keymap_add_item(keymap, "WM_OT_open_mainfile", OKEY, KM_PRESS, KM_OSKEY, 0);
//	WM_keymap_add_item(keymap, "WM_OT_save_mainfile", SKEY, KM_PRESS, KM_OSKEY, 0);
//	WM_keymap_add_item(keymap, "WM_OT_save_as_mainfile", SKEY, KM_PRESS, KM_SHIFT|KM_OSKEY, 0);
//	WM_keymap_add_item(keymap, "WM_OT_quit_blender", QKEY, KM_PRESS, KM_OSKEY, 0);
//	#endif
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_read_homefile", NKEY, KM_PRESS, KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_homefile", UKEY, KM_PRESS, KM_CTRL, 0); 
	WmKeymap.WM_keymap_add_menu(keymap, "INFO_MT_file_open_recent", OKEY, KM_PRESS, KM_SHIFT|KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_open_mainfile", OKEY, KM_PRESS, KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_open_mainfile", F1KEY, KM_PRESS, 0, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_link_append", OKEY, KM_PRESS, KM_CTRL|KM_ALT, 0);
	kmi= WmKeymap.WM_keymap_add_item(keymap, "WM_OT_link_append", F1KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_boolean_set(kmi.ptr, "link", false);
	RnaAccess.RNA_boolean_set(kmi.ptr, "instance_groups", false);

	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_mainfile", SKEY, KM_PRESS, KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_mainfile", WKEY, KM_PRESS, KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_as_mainfile", SKEY, KM_PRESS, KM_SHIFT|KM_CTRL, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_as_mainfile", F2KEY, KM_PRESS, 0, 0);
	kmi= WmKeymap.WM_keymap_add_item(keymap, "WM_OT_save_as_mainfile", SKEY, KM_PRESS, KM_ALT|KM_CTRL, 0);
	RnaAccess.RNA_boolean_set(kmi.ptr, "copy", true);

	WmKeymap.WM_keymap_verify_item(keymap, "WM_OT_window_fullscreen_toggle", F11KEY, KM_PRESS, KM_ALT, 0);
	WmKeymap.WM_keymap_add_item(keymap, "WM_OT_quit_blender", QKEY, KM_PRESS, KM_CTRL, 0);

	/* debug/testing */
	WmKeymap.WM_keymap_verify_item(keymap, "WM_OT_redraw_timer", TKEY, KM_PRESS, KM_ALT|KM_CTRL, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "WM_OT_debug_menu", DKEY, KM_PRESS, KM_ALT|KM_CTRL, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "WM_OT_search_menu", SPACEKEY, KM_PRESS, 0, 0);
	
	/* Space switching */


	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F2KEY, KM_PRESS, KM_SHIFT, 0); /* new in 2.5x, was DXF export */
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "LOGIC_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F3KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "NODE_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F4KEY, KM_PRESS, KM_SHIFT, 0); /* new in 2.5x, was data browser */
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "CONSOLE");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F5KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "VIEW_3D");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F6KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "GRAPH_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F7KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "PROPERTIES");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F8KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "SEQUENCE_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F9KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "OUTLINER");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F10KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "IMAGE_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F11KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "TEXT_EDITOR");

	kmi = WmKeymap.WM_keymap_add_item(keymap, "WM_OT_context_set_enum", F12KEY, KM_PRESS, KM_SHIFT, 0);
	RnaAccess.RNA_string_set(kmi.ptr, "data_path", "area.type");
	RnaAccess.RNA_string_set(kmi.ptr, "value", "DOPESHEET_EDITOR");

//	gesture_circle_modal_keymap(keyconf);
//	gesture_border_modal_keymap(keyconf);
//	gesture_zoom_border_modal_keymap(keyconf);
//	gesture_straightline_modal_keymap(keyconf);
}

}

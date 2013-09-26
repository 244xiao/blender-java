/**
 * $Id: RnaWm.java,v 1.1 2009/09/18 05:18:25 jladere Exp $
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
 * Contributor(s): Blender Foundation (2008).
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.makesrna;

//#include <stdlib.h>

import static blender.makesrna.RNATypes.PROP_BOOLEAN;
import static blender.makesrna.RNATypes.PROP_CONTEXT_UPDATE;
import static blender.makesrna.RNATypes.PROP_EDITABLE;
import static blender.makesrna.RNATypes.PROP_ENUM;
import static blender.makesrna.RNATypes.PROP_ENUM_FLAG;
import static blender.makesrna.RNATypes.PROP_NEVER_NULL;
import static blender.makesrna.RNATypes.PROP_NONE;
import static blender.makesrna.RNATypes.PROP_POINTER;
import static blender.makesrna.RNATypes.PROP_REGISTER;
import static blender.makesrna.RNATypes.PROP_REGISTER_OPTIONAL;
import static blender.makesrna.RNATypes.PROP_STRING;
import static blender.makesrna.RnaDefine.RNA_def_property;
import static blender.makesrna.RnaDefine.RNA_def_property_boolean_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_clear_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_items;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_pointer_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_string_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_string_maxlength;
import static blender.makesrna.RnaDefine.RNA_def_property_string_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_struct_type;
import static blender.makesrna.RnaDefine.RNA_def_property_ui_text;
import static blender.makesrna.RnaDefine.RNA_def_property_update;
import static blender.makesrna.RnaDefine.RNA_def_struct;
import static blender.makesrna.RnaDefine.RNA_def_struct_idprops_func;
import static blender.makesrna.RnaDefine.RNA_def_struct_name_property;
import static blender.makesrna.RnaDefine.RNA_def_struct_refine_func;
import static blender.makesrna.RnaDefine.RNA_def_struct_register_funcs;
import static blender.makesrna.RnaDefine.RNA_def_struct_sdna;
import static blender.makesrna.RnaDefine.RNA_def_struct_ui_text;
import blender.blenkernel.bContext;
import blender.blenlib.StringUtil;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.ReportList;
import blender.makesdna.sdna.bScreen;
import blender.makesdna.sdna.wmOperator;
import blender.makesdna.sdna.wmWindow;
import blender.makesdna.sdna.wmWindowManager;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RNATypes.StructCallbackFunc;
import blender.makesrna.RNATypes.StructFreeFunc;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.StructUnregisterFunc;
import blender.makesrna.RNATypes.StructValidateFunc;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.PropBooleanGetFunc;
import blender.makesrna.rna_internal_types.PropPointerGetFunc;
import blender.makesrna.rna_internal_types.PropStringGetFunc;
import blender.makesrna.rna_internal_types.PropStringLengthFunc;
import blender.makesrna.rna_internal_types.PropStringSetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.rna_internal_types.StructRefineFunc;
import blender.makesrna.srna.RnaOperator;
import blender.makesrna.srna.RnaOperatorProperties;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmTypes;

//
//#include "RNA_define.h"
//#include "RNA_types.h"
//
//#include "rna_internal.h"
//
//#include "DNA_windowmanager_types.h"
//#include "WM_types.h" /* wmEvent */
//

public class RnaWm {
	
	public static EnumPropertyItem[] operator_flag_items = {
		new EnumPropertyItem(WmTypes.OPTYPE_REGISTER, "REGISTER", 0, "Register", ""),
		new EnumPropertyItem(WmTypes.OPTYPE_UNDO, "UNDO", 0, "Undo", ""),
		new EnumPropertyItem(WmTypes.OPTYPE_BLOCKING, "BLOCKING", 0, "Finished", ""),
		new EnumPropertyItem(WmTypes.OPTYPE_MACRO, "MACRO", 0, "Macro", ""),
		new EnumPropertyItem(WmTypes.OPTYPE_GRAB_POINTER, "GRAB_POINTER", 0, "Grab Pointer", ""),
		new EnumPropertyItem(WmTypes.OPTYPE_PRESET, "PRESET", 0, "Preset", ""),
		new EnumPropertyItem(0, null, 0, null, null)};

//EnumPropertyItem event_value_items[] = {
//	{KM_ANY, "ANY", 0, "Any", ""},
//	{KM_NOTHING, "NOTHING", 0, "Nothing", ""},
//	{KM_PRESS, "PRESS", 0, "Press", ""},
//	{KM_RELEASE, "RELEASE", 0, "Release", ""},
//	{0, NULL, 0, NULL, NULL}};

/* not returned: CAPSLOCKKEY, UNKNOWNKEY, GRLESSKEY */
public static EnumPropertyItem[] event_type_items = {

	new EnumPropertyItem(WmEventTypes.LEFTMOUSE, "LEFTMOUSE", 0, "Left Mouse", ""),
	new EnumPropertyItem(WmEventTypes.MIDDLEMOUSE, "MIDDLEMOUSE", 0, "Middle Mouse", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTMOUSE, "RIGHTMOUSE", 0, "Right Mouse", ""),
	new EnumPropertyItem(WmEventTypes.ACTIONMOUSE, "ACTIONMOUSE", 0, "Action Mouse", ""),
	new EnumPropertyItem(WmEventTypes.SELECTMOUSE, "SELECTMOUSE", 0, "Select Mouse", ""),

	new EnumPropertyItem(WmEventTypes.MOUSEMOVE, "MOUSEMOVE", 0, "Mouse Move", ""),

	new EnumPropertyItem(WmEventTypes.WHEELUPMOUSE, "WHEELUPMOUSE", 0, "Wheel Up", ""),
	new EnumPropertyItem(WmEventTypes.WHEELDOWNMOUSE, "WHEELDOWNMOUSE", 0, "Wheel Down", ""),
	new EnumPropertyItem(WmEventTypes.WHEELINMOUSE, "WHEELINMOUSE", 0, "Wheel In", ""),
	new EnumPropertyItem(WmEventTypes.WHEELOUTMOUSE, "WHEELOUTMOUSE", 0, "Wheel Out", ""),

	new EnumPropertyItem(WmEventTypes.EVT_TWEAK_L, "EVT_TWEAK_L", 0, "Tweak Left", ""),
	new EnumPropertyItem(WmEventTypes.EVT_TWEAK_M, "EVT_TWEAK_M", 0, "Tweak Middle", ""),
	new EnumPropertyItem(WmEventTypes.EVT_TWEAK_R, "EVT_TWEAK_R", 0, "Tweak Right", ""),
	new EnumPropertyItem(WmEventTypes.EVT_TWEAK_A, "EVT_TWEAK_A", 0, "Tweak Action", ""),
	new EnumPropertyItem(WmEventTypes.EVT_TWEAK_S, "EVT_TWEAK_S", 0, "Tweak Select", ""),

	new EnumPropertyItem(WmEventTypes.AKEY, "A", 0, "A", ""),
	new EnumPropertyItem(WmEventTypes.BKEY, "B", 0, "B", ""),
	new EnumPropertyItem(WmEventTypes.CKEY, "C", 0, "C", ""),
	new EnumPropertyItem(WmEventTypes.DKEY, "D", 0, "D", ""),
	new EnumPropertyItem(WmEventTypes.EKEY, "E", 0, "E", ""),
	new EnumPropertyItem(WmEventTypes.FKEY, "F", 0, "F", ""),
	new EnumPropertyItem(WmEventTypes.GKEY, "G", 0, "G", ""),
	new EnumPropertyItem(WmEventTypes.HKEY, "H", 0, "H", ""),
	new EnumPropertyItem(WmEventTypes.IKEY, "I", 0, "I", ""),
	new EnumPropertyItem(WmEventTypes.JKEY, "J", 0, "J", ""),
	new EnumPropertyItem(WmEventTypes.KKEY, "K", 0, "K", ""),
	new EnumPropertyItem(WmEventTypes.LKEY, "L", 0, "L", ""),
	new EnumPropertyItem(WmEventTypes.MKEY, "M", 0, "M", ""),
	new EnumPropertyItem(WmEventTypes.NKEY, "N", 0, "N", ""),
	new EnumPropertyItem(WmEventTypes.OKEY, "O", 0, "O", ""),
	new EnumPropertyItem(WmEventTypes.PKEY, "P", 0, "P", ""),
	new EnumPropertyItem(WmEventTypes.QKEY, "Q", 0, "Q", ""),
	new EnumPropertyItem(WmEventTypes.RKEY, "R", 0, "R", ""),
	new EnumPropertyItem(WmEventTypes.SKEY, "S", 0, "S", ""),
	new EnumPropertyItem(WmEventTypes.TKEY, "T", 0, "T", ""),
	new EnumPropertyItem(WmEventTypes.UKEY, "U", 0, "U", ""),
	new EnumPropertyItem(WmEventTypes.VKEY, "V", 0, "V", ""),
	new EnumPropertyItem(WmEventTypes.WKEY, "W", 0, "W", ""),
	new EnumPropertyItem(WmEventTypes.XKEY, "X", 0, "X", ""),
	new EnumPropertyItem(WmEventTypes.YKEY, "Y", 0, "Y", ""),
	new EnumPropertyItem(WmEventTypes.ZKEY, "Z", 0, "Z", ""),
	
	new EnumPropertyItem(WmEventTypes.ZEROKEY, "ZERO",	0, "0", ""),
	new EnumPropertyItem(WmEventTypes.ONEKEY, "ONE",		0, "1", ""),
	new EnumPropertyItem(WmEventTypes.TWOKEY, "TWO",		0, "2", ""),
	new EnumPropertyItem(WmEventTypes.THREEKEY, "THREE",	0, "3", ""),
	new EnumPropertyItem(WmEventTypes.FOURKEY, "FOUR",	0, "4", ""),
	new EnumPropertyItem(WmEventTypes.FIVEKEY, "FIVE",	0, "5", ""),
	new EnumPropertyItem(WmEventTypes.SIXKEY, "SIX",		0, "6", ""),
	new EnumPropertyItem(WmEventTypes.SEVENKEY, "SEVEN",	0, "7", ""),
	new EnumPropertyItem(WmEventTypes.EIGHTKEY, "EIGHT",	0, "8", ""),
	new EnumPropertyItem(WmEventTypes.NINEKEY, "NINE",	0, "9", ""),
	
	new EnumPropertyItem(WmEventTypes.LEFTCTRLKEY,	"LEFT_CTRL",	0, "Left Ctrl", ""),
	new EnumPropertyItem(WmEventTypes.LEFTALTKEY,	"LEFT_ALT",		0, "Left Alt", ""),
	new EnumPropertyItem(WmEventTypes.LEFTSHIFTKEY,	"LEFT_SHIFT",	0, "Left Shift", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTALTKEY,	"RIGHT_ALT",	0, "Right Alt", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTCTRLKEY,	"RIGHT_CTRL",	0, "Right Ctrl", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTSHIFTKEY,	"RIGHT_SHIFT",	0, "Right Shift", ""),

	new EnumPropertyItem(WmEventTypes.COMMANDKEY,	"COMMAND",	0, "Command", ""),
	
	new EnumPropertyItem(WmEventTypes.ESCKEY, "ESC", 0, "Esc", ""),
	new EnumPropertyItem(WmEventTypes.TABKEY, "TAB", 0, "Tab", ""),
	new EnumPropertyItem(WmEventTypes.RETKEY, "RET", 0, "Return", ""),
	new EnumPropertyItem(WmEventTypes.SPACEKEY, "SPACE", 0, "Spacebar", ""),
	new EnumPropertyItem(WmEventTypes.LINEFEEDKEY, "LINE_FEED", 0, "Line Feed", ""),
	new EnumPropertyItem(WmEventTypes.BACKSPACEKEY, "BACK_SPACE", 0, "Back Space", ""),
	new EnumPropertyItem(WmEventTypes.DELKEY, "DEL", 0, "Delete", ""),
	new EnumPropertyItem(WmEventTypes.SEMICOLONKEY, "SEMI_COLON", 0, ";", ""),
	new EnumPropertyItem(WmEventTypes.PERIODKEY, "PERIOD", 0, ".", ""),
	new EnumPropertyItem(WmEventTypes.COMMAKEY, "COMMA", 0, ",", ""),
	new EnumPropertyItem(WmEventTypes.QUOTEKEY, "QUOTE", 0, "\"", ""),
	new EnumPropertyItem(WmEventTypes.ACCENTGRAVEKEY, "ACCENT_GRAVE", 0, "`", ""),
	new EnumPropertyItem(WmEventTypes.MINUSKEY, "MINUS", 0, "-", ""),
	new EnumPropertyItem(WmEventTypes.SLASHKEY, "SLASH", 0, "/", ""),
	new EnumPropertyItem(WmEventTypes.BACKSLASHKEY, "BACK_SLASH", 0, "\\", ""),
	new EnumPropertyItem(WmEventTypes.EQUALKEY, "EQUAL", 0, "=", ""),
	new EnumPropertyItem(WmEventTypes.LEFTBRACKETKEY, "LEFT_BRACKET", 0, "]", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTBRACKETKEY, "RIGHT_BRACKET", 0, "[", ""),
	new EnumPropertyItem(WmEventTypes.LEFTARROWKEY, "LEFT_ARROW", 0, "Left Arrow", ""),
	new EnumPropertyItem(WmEventTypes.DOWNARROWKEY, "DOWN_ARROW", 0, "Down Arrow", ""),
	new EnumPropertyItem(WmEventTypes.RIGHTARROWKEY, "RIGHT_ARROW", 0, "Right Arrow", ""),
	new EnumPropertyItem(WmEventTypes.UPARROWKEY, "UP_ARROW", 0, "Up Arrow", ""),
	new EnumPropertyItem(WmEventTypes.PAD2, "NUMPAD_2", 0, "Numpad 2", ""),
	new EnumPropertyItem(WmEventTypes.PAD4, "NUMPAD_4", 0, "Numpad 4", ""),
	new EnumPropertyItem(WmEventTypes.PAD6, "NUMPAD_6", 0, "Numpad 6", ""),
	new EnumPropertyItem(WmEventTypes.PAD8, "NUMPAD_8", 0, "Numpad 8", ""),
	new EnumPropertyItem(WmEventTypes.PAD1, "NUMPAD_1", 0, "Numpad 1", ""),
	new EnumPropertyItem(WmEventTypes.PAD3, "NUMPAD_3", 0, "Numpad 3", ""),
	new EnumPropertyItem(WmEventTypes.PAD5, "NUMPAD_5", 0, "Numpad 5", ""),
	new EnumPropertyItem(WmEventTypes.PAD7, "NUMPAD_7", 0, "Numpad 7", ""),
	new EnumPropertyItem(WmEventTypes.PAD9, "NUMPAD_9", 0, "Numpad 9", ""),
	new EnumPropertyItem(WmEventTypes.PADPERIOD, "NUMPAD_PERIOD", 0, "Numpad .", ""),
	new EnumPropertyItem(WmEventTypes.PADSLASHKEY, "NUMPAD_SLASH", 0, "Numpad /", ""),
	new EnumPropertyItem(WmEventTypes.PADASTERKEY, "NUMPAD_ASTERIX", 0, "Numpad *", ""),
	new EnumPropertyItem(WmEventTypes.PAD0, "NUMPAD_0", 0, "Numpad 0", ""),
	new EnumPropertyItem(WmEventTypes.PADMINUS, "NUMPAD_MINUS", 0, "Numpad -", ""),
	new EnumPropertyItem(WmEventTypes.PADENTER, "NUMPAD_ENTER", 0, "Numpad Enter", ""),
	new EnumPropertyItem(WmEventTypes.PADPLUSKEY, "NUMPAD_PLUS", 0, "Numpad +", ""),
	new EnumPropertyItem(WmEventTypes.F1KEY, "F1", 0, "F1", ""),
	new EnumPropertyItem(WmEventTypes.F2KEY, "F2", 0, "F2", ""),
	new EnumPropertyItem(WmEventTypes.F3KEY, "F3", 0, "F3", ""),
	new EnumPropertyItem(WmEventTypes.F4KEY, "F4", 0, "F4", ""),
	new EnumPropertyItem(WmEventTypes.F5KEY, "F5", 0, "F5", ""),
	new EnumPropertyItem(WmEventTypes.F6KEY, "F6", 0, "F6", ""),
	new EnumPropertyItem(WmEventTypes.F7KEY, "F7", 0, "F7", ""),
	new EnumPropertyItem(WmEventTypes.F8KEY, "F8", 0, "F8", ""),
	new EnumPropertyItem(WmEventTypes.F9KEY, "F9", 0, "F9", ""),
	new EnumPropertyItem(WmEventTypes.F10KEY, "F10", 0, "F10", ""),
	new EnumPropertyItem(WmEventTypes.F11KEY, "F11", 0, "F11", ""),
	new EnumPropertyItem(WmEventTypes.F12KEY, "F12", 0, "F12", ""),
	new EnumPropertyItem(WmEventTypes.PAUSEKEY, "PAUSE", 0, "Pause", ""),
	new EnumPropertyItem(WmEventTypes.INSERTKEY, "INSERT", 0, "Insert", ""),
	new EnumPropertyItem(WmEventTypes.HOMEKEY, "HOME", 0, "Home", ""),
	new EnumPropertyItem(WmEventTypes.PAGEUPKEY, "PAGE_UP", 0, "Page Up", ""),
	new EnumPropertyItem(WmEventTypes.PAGEDOWNKEY, "PAGE_DOWN", 0, "Page Down", ""),
	new EnumPropertyItem(WmEventTypes.ENDKEY, "END", 0, "End", ""),
	new EnumPropertyItem(0, null, 0, null, null)
};	

//#ifdef WITH_PYTHON
public static StructUnregisterFunc rna_Operator_unregister = new StructUnregisterFunc() {
public void run(bContext C, StructRNA type)
{
//	const char *idname;
//	wmOperatorType *ot= RNA_struct_blender_type_get(type);
//
//	if(!ot)
//		return;
//
//	/* update while blender is running */
//	if(C) {
//		WM_operator_stack_clear((bContext*)C);
//		WM_main_add_notifier(NC_SCREEN|NA_EDITED, NULL);
//	}
//
//	RNA_struct_free_extension(type, &ot->ext);
//
//	idname= ot->idname;
//	WM_operatortype_remove(ot->idname);
//	MEM_freeN((void *)idname);
//
//	/* not to be confused with the RNA_struct_free that WM_operatortype_remove calls, they are 2 different srna's */
//	RNA_struct_free(&BLENDER_RNA, type);
}
public String toString() { return "rna_Operator_unregister"; }
};

//static int operator_poll(bContext *C, wmOperatorType *ot)
//{
//	PointerRNA ptr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int visible;
//
//	RNA_pointer_create(NULL, ot->ext.srna, NULL, &ptr); /* dummy */
//	func= RNA_struct_find_function(&ptr, "poll");
//
//	RNA_parameter_list_create(&list, &ptr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	ot->ext.call(C, &ptr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "visible", &ret);
//	visible= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return visible;
//}
//
//static int operator_execute(bContext *C, wmOperator *op)
//{
//	PointerRNA opr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int result;
//
//	RNA_pointer_create(&CTX_wm_screen(C)->id, op->type->ext.srna, op, &opr);
//	func= RNA_struct_find_function(&opr, "execute");
//
//	RNA_parameter_list_create(&list, &opr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	op->type->ext.call(C, &opr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "result", &ret);
//	result= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return result;
//}
//
///* same as execute() but no return value */
//static int operator_check(bContext *C, wmOperator *op)
//{
//	PointerRNA opr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int result;
//
//	RNA_pointer_create(&CTX_wm_screen(C)->id, op->type->ext.srna, op, &opr);
//	func= RNA_struct_find_function(&opr, "check");
//
//	RNA_parameter_list_create(&list, &opr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	op->type->ext.call(C, &opr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "result", &ret);
//	result= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return result;
//}
//
//static int operator_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	PointerRNA opr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int result;
//
//	RNA_pointer_create(&CTX_wm_screen(C)->id, op->type->ext.srna, op, &opr);
//	func= RNA_struct_find_function(&opr, "invoke");
//
//	RNA_parameter_list_create(&list, &opr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	RNA_parameter_set_lookup(&list, "event", &event);
//	op->type->ext.call(C, &opr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "result", &ret);
//	result= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return result;
//}
//
///* same as invoke */
//static int operator_modal(bContext *C, wmOperator *op, wmEvent *event)
//{
//	PointerRNA opr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int result;
//
//	RNA_pointer_create(&CTX_wm_screen(C)->id, op->type->ext.srna, op, &opr);
//	func= RNA_struct_find_function(&opr, "modal");
//
//	RNA_parameter_list_create(&list, &opr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	RNA_parameter_set_lookup(&list, "event", &event);
//	op->type->ext.call(C, &opr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "result", &ret);
//	result= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return result;
//}
//
//static void operator_draw(bContext *C, wmOperator *op)
//{
//	PointerRNA opr;
//	ParameterList list;
//	FunctionRNA *func;
//
//	RNA_pointer_create(&CTX_wm_screen(C)->id, op->type->ext.srna, op, &opr);
//	func= RNA_struct_find_function(&opr, "draw");
//
//	RNA_parameter_list_create(&list, &opr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	op->type->ext.call(C, &opr, func, &list);
//
//	RNA_parameter_list_free(&list);
//}
//
//void operator_wrapper(wmOperatorType *ot, void *userdata);
//void macro_wrapper(wmOperatorType *ot, void *userdata);
//
//static char _operator_idname[OP_MAX_TYPENAME];
//static char _operator_name[OP_MAX_TYPENAME];
//static char _operator_descr[1024];
//static StructRNA *rna_Operator_register(bContext *C, ReportList *reports, void *data, const char *identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
public static StructRegisterFunc rna_Operator_register = new StructRegisterFunc() {
public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
{
	wmOperatorType dummyot = new wmOperatorType();
//	wmOperator dummyop= {0};
//	PointerRNA dummyotr;
//	int have_function[6];
//
//	/* setup dummy operator & operator type to store static properties in */
//	dummyop.type= &dummyot;
//	dummyot.idname= _operator_idname; /* only assigne the pointer, string is NULL'd */
//	dummyot.name= _operator_name; /* only assigne the pointer, string is NULL'd */
//	dummyot.description= _operator_descr; /* only assigne the pointer, string is NULL'd */
//	RNA_pointer_create(NULL, &RNA_Operator, &dummyop, &dummyotr);
//
//	/* clear incase they are left unset */
//	_operator_idname[0]= _operator_name[0]= _operator_descr[0]= '\0';
//
//	/* validate the python class */
//	if(validate(&dummyotr, data, have_function) != 0)
//		return NULL;
//
//	{	/* convert foo.bar to FOO_OT_bar
//		 * allocate the description and the idname in 1 go */
//		int idlen = strlen(_operator_idname) + 4;
//		int namelen = strlen(_operator_name) + 1;
//		int desclen = strlen(_operator_descr) + 1;
//		char *ch, *ch_arr;
//		ch_arr= ch= MEM_callocN(sizeof(char) * (idlen + namelen + desclen), "_operator_idname"); /* 2 terminators and 3 to convert a.b -> A_OT_b */
//		WM_operator_bl_idname(ch, _operator_idname); /* convert the idname from python */
//		dummyot.idname= ch;
//		ch += idlen;
//		strcpy(ch, _operator_name);
//		dummyot.name = ch;
//		ch += namelen;
//		strcpy(ch, _operator_descr);
//		dummyot.description = ch;
//	}
//
//	if(strlen(identifier) >= sizeof(dummyop.idname)) {
//		BKE_reportf(reports, RPT_ERROR, "registering operator class: '%s' is too long, maximum length is %d.", identifier, (int)sizeof(dummyop.idname));
//		return NULL;
//	}
//
//	/* check if we have registered this operator type before, and remove it */
//	{
//		wmOperatorType *ot= WM_operatortype_find(dummyot.idname, TRUE);
//		if(ot && ot->ext.srna)
//			rna_Operator_unregister(C, ot->ext.srna);
//	}
//
//	/* create a new menu type */
//	dummyot.ext.srna= RNA_def_struct(&BLENDER_RNA, dummyot.idname, "Operator");
	dummyot.ext.srna= new StructRNA(dummyot.idname);
//	RNA_def_struct_flag(dummyot.ext.srna, STRUCT_NO_IDPROPERTIES); /* operator properties are registered separately */
	dummyot.ext.data= data;
	dummyot.ext.call= call;
	dummyot.ext.free= free;

//	dummyot.pyop_poll=	(have_function[0])? operator_poll: NULL;
//	dummyot.exec=		(have_function[1])? operator_execute: NULL;
//	dummyot.check=		(have_function[2])? operator_check: NULL;
//	dummyot.invoke=		(have_function[3])? operator_invoke: NULL;
//	dummyot.modal=		(have_function[4])? operator_modal: NULL;
//	dummyot.ui=			(have_function[5])? operator_draw: NULL;
//	WM_operatortype_append_ptr(operator_wrapper, (void *)&dummyot);

	/* update while blender is running */
	if(C != null)
		WmEventSystem.WM_main_add_notifier(WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

	return dummyot.ext.srna;
}
public String toString() { return "rna_Operator_register"; }
};


//static StructRNA *rna_MacroOperator_register(bContext *C, ReportList *reports, void *data, const char *identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
//{
//	wmOperatorType dummyot = {0};
//	wmOperator dummyop= {0};
//	PointerRNA dummyotr;
//	int have_function[4];
//
//	/* setup dummy operator & operator type to store static properties in */
//	dummyop.type= &dummyot;
//	dummyot.idname= _operator_idname; /* only assigne the pointer, string is NULL'd */
//	dummyot.name= _operator_name; /* only assigne the pointer, string is NULL'd */
//	dummyot.description= _operator_descr; /* only assigne the pointer, string is NULL'd */
//	RNA_pointer_create(NULL, &RNA_Macro, &dummyop, &dummyotr);
//
//	/* validate the python class */
//	if(validate(&dummyotr, data, have_function) != 0)
//		return NULL;
//
//	{	/* convert foo.bar to FOO_OT_bar
//		 * allocate the description and the idname in 1 go */
//		int idlen = strlen(_operator_idname) + 4;
//		int namelen = strlen(_operator_name) + 1;
//		int desclen = strlen(_operator_descr) + 1;
//		char *ch, *ch_arr;
//		ch_arr= ch= MEM_callocN(sizeof(char) * (idlen + namelen + desclen), "_operator_idname"); /* 2 terminators and 3 to convert a.b -> A_OT_b */
//		WM_operator_bl_idname(ch, _operator_idname); /* convert the idname from python */
//		dummyot.idname= ch;
//		ch += idlen;
//		strcpy(ch, _operator_name);
//		dummyot.name = ch;
//		ch += namelen;
//		strcpy(ch, _operator_descr);
//		dummyot.description = ch;
//	}
//
//	if(strlen(identifier) >= sizeof(dummyop.idname)) {
//		BKE_reportf(reports, RPT_ERROR, "registering operator class: '%s' is too long, maximum length is %d.", identifier, (int)sizeof(dummyop.idname));
//		return NULL;
//	}
//
//	/* check if we have registered this operator type before, and remove it */
//	{
//		wmOperatorType *ot= WM_operatortype_find(dummyot.idname, TRUE);
//		if(ot && ot->ext.srna)
//			rna_Operator_unregister(C, ot->ext.srna);
//	}
//
//	/* create a new menu type */
//	dummyot.ext.srna= RNA_def_struct(&BLENDER_RNA, dummyot.idname, "Operator");
//	dummyot.ext.data= data;
//	dummyot.ext.call= call;
//	dummyot.ext.free= free;
//
//	dummyot.pyop_poll=	(have_function[0])? operator_poll: NULL;
//	dummyot.ui=			(have_function[3])? operator_draw: NULL;
//
//	WM_operatortype_append_macro_ptr(macro_wrapper, (void *)&dummyot);
//
//	/* update while blender is running */
//	if(C)
//		WM_main_add_notifier(NC_SCREEN|NA_EDITED, NULL);
//
//	return dummyot.ext.srna;
//}
//#endif /* WITH_PYTHON */

public static StructRefineFunc rna_Operator_refine = new StructRefineFunc() {
public StructRNA refine(PointerRNA opr)
{
	wmOperator op= (wmOperator)opr.data;
	return (op.type!=null && ((wmOperatorType)op.type).ext.srna!=null)? ((wmOperatorType)op.type).ext.srna: RnaOperator.RNA_Operator;
}
public String getName() { return "rna_Operator_refine"; }
};

//#ifdef RNA_RUNTIME
//
//#include "WM_api.h"
//
//#include "BKE_idprop.h"

static wmOperator rna_OperatorProperties_find_operator(PointerRNA ptr)
{
	wmWindowManager wm= (wmWindowManager)ptr.id.data;
	IDProperty properties= (IDProperty)ptr.data;
	wmOperator op;

	if(wm!=null)
		for(op=(wmOperator)wm.operators.first; op!=null; op=op.next)
			if(op.properties == properties)
				return op;
	
	return null;
}

public static StructRefineFunc rna_OperatorProperties_refine = new StructRefineFunc() {
public StructRNA refine(PointerRNA ptr)
{
	wmOperator op= rna_OperatorProperties_find_operator(ptr);

	if(op!=null)
		return ((wmOperatorType)op.type).srna;
	else
		return ptr.type;
}
public String getName() { return "rna_OperatorProperties_refine"; }
};

//IDProperty *rna_OperatorProperties_idproperties(PointerRNA *ptr, int create)
//{
//	if(create && !ptr->data) {
//		IDPropertyTemplate val = {0};
//		ptr->data= IDP_New(IDP_GROUP, val, "RNA_OperatorProperties group");
//	}
//
//	return ptr->data;
//}

public static PropStringGetFunc rna_Operator_name_get = new PropStringGetFunc() {
	public void getStr(PointerRNA ptr, byte[] value, int offset)
//static void rna_Operator_name_get(PointerRNA *ptr, char *value)
{
	wmOperator op= (wmOperator)ptr.data;
	StringUtil.strcpy(value,0, StringUtil.toCString(((wmOperatorType)op.type).name),0);
}};

public static PropStringLengthFunc rna_Operator_name_length = new PropStringLengthFunc() {
	public int lenStr(PointerRNA ptr)
//static int rna_Operator_name_length(PointerRNA *ptr)
{
	wmOperator op= (wmOperator)ptr.data;
	return StringUtil.strlen(StringUtil.toCString(((wmOperatorType)op.type).name),0);
}};

public static PropBooleanGetFunc rna_Operator_has_reports_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
//static int rna_Operator_has_reports_get(PointerRNA *ptr)
{
	wmOperator op= (wmOperator)ptr.data;
	return (op.reports!=null && op.reports.list.first!=null);
}};

public static PropPointerGetFunc rna_Operator_properties_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
//static PointerRNA rna_Operator_properties_get(PointerRNA *ptr)
{
	wmOperator op= (wmOperator)ptr.data;
	return RnaAccess.rna_pointer_inherit_refine(ptr, RnaOperatorProperties.RNA_OperatorProperties, op.properties);
}};


//static void rna_Event_ascii_get(PointerRNA *ptr, char *value)
//{
//	wmEvent *event= (wmEvent*)ptr->id.data;
//	value[0]= event->ascii;
//	value[1]= '\0';
//}
//
//static int rna_Event_ascii_length(PointerRNA *ptr)
//{
//	wmEvent *event= (wmEvent*)ptr->id.data;
//	return (event->ascii)? 1 : 0;
//}

public static void rna_Window_screen_set(PointerRNA ptr, PointerRNA value)
{
	wmWindow win= (wmWindow)ptr.data;

	if(value.data == null)
		return;

	/* exception: can't set screens inside of area/region handers */
	win.newscreen= (bScreen)value.data;
}

//static void rna_Window_screen_update(bContext *C, PointerRNA *ptr)
//{
//	wmWindow *win= (wmWindow*)ptr->data;
//
//	/* exception: can't set screens inside of area/region handers */
//	if(win->newscreen) {
//		WM_event_add_notifier(C, NC_SCREEN|ND_SCREENBROWSE, win->newscreen);
//		win->newscreen= NULL;
//	}
//}

/* just to work around 'const char *' warning and to ensure this is a python op */
public static PropStringSetFunc rna_Operator_bl_idname_set = new PropStringSetFunc() {
	public void setStr(PointerRNA ptr, byte[] value, int offset)
//static void rna_Operator_bl_idname_set(PointerRNA *ptr, const char *value)
{
	wmOperator data= (wmOperator)(ptr.data);
//	byte[] str= (byte[])((wmOperatorType)data.type).idname;
//	if(str[0]==0)	StringUtil.strcpy(str,0, value,0);
	if (((wmOperatorType)data.type).idname.isEmpty()) ((wmOperatorType)data.type).idname = StringUtil.toJString(value, offset);
//	else		assert(!"setting the bl_idname on a non-builtin operator");
	else		System.out.println("setting the bl_idname on a non-builtin operator");
}};

public static PropStringSetFunc rna_Operator_bl_label_set = new PropStringSetFunc() {
	public void setStr(PointerRNA ptr, byte[] value, int offset)
//static void rna_Operator_bl_label_set(PointerRNA *ptr, const char *value)
{
	wmOperator data= (wmOperator)(ptr.data);
//	byte[] str= (byte[])((wmOperatorType)data.type).name;
//	if(str[0]==0)	StringUtil.strcpy(str,0, value,0);
	if (((wmOperatorType)data.type).name.isEmpty()) ((wmOperatorType)data.type).name = StringUtil.toJString(value, offset);
//	else		assert(!"setting the bl_label on a non-builtin operator");
	else		System.out.println("setting the bl_label on a non-builtin operator");
}};

public static PropStringSetFunc rna_Operator_bl_description_set = new PropStringSetFunc() {
	public void setStr(PointerRNA ptr, byte[] value, int offset)
//static void rna_Operator_bl_description_set(PointerRNA *ptr, const char *value)
{
	wmOperator data= (wmOperator)(ptr.data);
//	byte[] str= (byte[])((wmOperatorType)data.type).description;
//	if(str[0]==0)	StringUtil.strcpy(str,0, value,0);
	if (((wmOperatorType)data.type).description.isEmpty()) ((wmOperatorType)data.type).description = StringUtil.toJString(value, offset);
//	else		assert(!"setting the bl_description on a non-builtin operator");
	else		System.out.println("setting the bl_description on a non-builtin operator");
}};

//#else

static void rna_def_operator(BlenderRNA brna)
{
	StructRNA srna;
	PropertyRNA prop;

	srna= RNA_def_struct(brna, "Operator", null);
	RNA_def_struct_ui_text(srna, "Operator", "Storage of an operator being executed, or registered after execution");
	RNA_def_struct_sdna(srna, "wmOperator");
	RNA_def_struct_refine_func(srna, "rna_Operator_refine");
//#ifdef WITH_PYTHON
	RNA_def_struct_register_funcs(srna, "rna_Operator_register", "rna_Operator_unregister");
//#endif

	prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
	RNA_def_property_string_funcs(prop, "rna_Operator_name_get", "rna_Operator_name_length", null);
	RNA_def_property_ui_text(prop, "Name", "");

	prop= RNA_def_property(srna, "properties", PROP_POINTER, PROP_NONE);
	RNA_def_property_flag(prop, PROP_NEVER_NULL);
	RNA_def_property_struct_type(prop, "OperatorProperties");
	RNA_def_property_ui_text(prop, "Properties", "");
	RNA_def_property_pointer_funcs(prop, "rna_Operator_properties_get", null, null, null);
	
	prop= RNA_def_property(srna, "has_reports", PROP_BOOLEAN, PROP_NONE);
	RNA_def_property_clear_flag(prop, PROP_EDITABLE); /* this is 'virtual' property */
	RNA_def_property_boolean_funcs(prop, "rna_Operator_has_reports_get", null);
	RNA_def_property_ui_text(prop, "Has Reports", "Operator has a set of reports (warnings and errors) from last execution");
	
	prop= RNA_def_property(srna, "layout", PROP_POINTER, PROP_NONE);
	RNA_def_property_struct_type(prop, "UILayout");

	/* Registration */
	prop= RNA_def_property(srna, "bl_idname", PROP_STRING, PROP_NONE);
	RNA_def_property_string_sdna(prop, null, "type->idname");
	RNA_def_property_string_maxlength(prop, WindowManagerTypes.OP_MAX_TYPENAME); /* else it uses the pointer size! */
	RNA_def_property_string_funcs(prop, null, null, "rna_Operator_bl_idname_set");
	// RNA_def_property_clear_flag(prop, PROP_EDITABLE);
	RNA_def_property_flag(prop, PROP_REGISTER);
	RNA_def_struct_name_property(srna, prop);

	prop= RNA_def_property(srna, "bl_label", PROP_STRING, PROP_NONE);
	RNA_def_property_string_sdna(prop, null, "type->name");
	RNA_def_property_string_maxlength(prop, 1024); /* else it uses the pointer size! */
	RNA_def_property_string_funcs(prop, null, null, "rna_Operator_bl_label_set");
	// RNA_def_property_clear_flag(prop, PROP_EDITABLE);
	RNA_def_property_flag(prop, PROP_REGISTER);

	prop= RNA_def_property(srna, "bl_description", PROP_STRING, PROP_NONE);
	RNA_def_property_string_sdna(prop, null, "type->description");
	RNA_def_property_string_maxlength(prop, 1024); /* else it uses the pointer size! */
	RNA_def_property_string_funcs(prop, null, null, "rna_Operator_bl_description_set");
	// RNA_def_property_clear_flag(prop, PROP_EDITABLE);
	RNA_def_property_flag(prop, PROP_REGISTER_OPTIONAL);

	prop= RNA_def_property(srna, "bl_options", PROP_ENUM, PROP_NONE);
	RNA_def_property_enum_sdna(prop, null, "type->flag");
	RNA_def_property_enum_items(prop, operator_flag_items);
	RNA_def_property_flag(prop, PROP_REGISTER_OPTIONAL|PROP_ENUM_FLAG);
	RNA_def_property_ui_text(prop, "Options",  "Options for this operator type");

//	RNA_api_operator(srna);

	srna= RNA_def_struct(brna, "OperatorProperties", null);
	RNA_def_struct_ui_text(srna, "Operator Properties", "Input properties of an Operator");
	RNA_def_struct_refine_func(srna, "rna_OperatorProperties_refine");
	RNA_def_struct_idprops_func(srna, "rna_OperatorProperties_idprops");
}

//static void rna_def_operator_utils(BlenderRNA *brna)
//{
//	StructRNA *srna;
//	PropertyRNA *prop;
//
//	srna= RNA_def_struct(brna, "OperatorMousePath", "IDPropertyGroup");
//	RNA_def_struct_ui_text(srna, "Operator Mouse Path", "Mouse path values for operators that record such paths.");
//
//	prop= RNA_def_property(srna, "loc", PROP_FLOAT, PROP_VECTOR);
//	RNA_def_property_flag(prop, PROP_IDPROPERTY);
//	RNA_def_property_array(prop, 2);
//	RNA_def_property_ui_text(prop, "Location", "Mouse location.");
//
//	prop= RNA_def_property(srna, "time", PROP_FLOAT, PROP_NONE);
//	RNA_def_property_flag(prop, PROP_IDPROPERTY);
//	RNA_def_property_ui_text(prop, "Time", "Time of mouse location.");
//}
//
//static void rna_def_operator_filelist_element(BlenderRNA *brna)
//{
//	StructRNA *srna;
//	PropertyRNA *prop;
//
//	srna= RNA_def_struct(brna, "OperatorFileListElement", "IDPropertyGroup");
//	RNA_def_struct_ui_text(srna, "Operator File List Element", "");
//
//
//	prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
//	RNA_def_property_flag(prop, PROP_IDPROPERTY);
//	RNA_def_property_ui_text(prop, "Name", "the name of a file or directory within a file list");
//}
//
//static void rna_def_event(BlenderRNA *brna)
//{
//	StructRNA *srna;
//	PropertyRNA *prop;
//
//	srna= RNA_def_struct(brna, "Event", NULL);
//	RNA_def_struct_ui_text(srna, "Event", "Window Manager Event");
//	RNA_def_struct_sdna(srna, "wmEvent");
//
//	/* strings */
//	prop= RNA_def_property(srna, "ascii", PROP_STRING, PROP_NONE);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_string_funcs(prop, "rna_Event_ascii_get", "rna_Event_ascii_length", NULL);
//	RNA_def_property_ui_text(prop, "ASCII", "Single ASCII character for this event.");
//
//
//	/* enums */
//	prop= RNA_def_property(srna, "value", PROP_ENUM, PROP_NONE);
//	RNA_def_property_enum_sdna(prop, NULL, "val");
//	RNA_def_property_enum_items(prop, event_value_items);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Value",  "The type of event, only applies to some.");
//
//	prop= RNA_def_property(srna, "type", PROP_ENUM, PROP_NONE);
//	RNA_def_property_enum_sdna(prop, NULL, "type");
//	RNA_def_property_enum_items(prop, event_type_items);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Type",  "");
//
//
//	/* mouse */
//	prop= RNA_def_property(srna, "mouse_x", PROP_INT, PROP_NONE);
//	RNA_def_property_int_sdna(prop, NULL, "x");
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Mouse X Position", "The window relative vertical location of the mouse.");
//
//	prop= RNA_def_property(srna, "mouse_y", PROP_INT, PROP_NONE);
//	RNA_def_property_int_sdna(prop, NULL, "y");
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Mouse Y Position", "The window relative horizontal location of the mouse.");
//
//	prop= RNA_def_property(srna, "mouse_prev_x", PROP_INT, PROP_NONE);
//	RNA_def_property_int_sdna(prop, NULL, "prevx");
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Mouse Previous X Position", "The window relative vertical location of the mouse.");
//
//	prop= RNA_def_property(srna, "mouse_prev_y", PROP_INT, PROP_NONE);
//	RNA_def_property_int_sdna(prop, NULL, "prevy");
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Mouse Previous Y Position", "The window relative horizontal location of the mouse.");
//
//
//	/* modifiers */
//	prop= RNA_def_property(srna, "shift", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_sdna(prop, NULL, "shift", 1);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Shift", "True when the shift key is held.");
//
//	prop= RNA_def_property(srna, "ctrl", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_sdna(prop, NULL, "ctrl", 1);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Ctrl", "True when the shift key is held.");
//
//	prop= RNA_def_property(srna, "alt", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_sdna(prop, NULL, "alt", 1);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "Alt", "True when the shift key is held.");
//
//	prop= RNA_def_property(srna, "oskey", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_sdna(prop, NULL, "oskey", 1);
//	RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//	RNA_def_property_ui_text(prop, "OS Key", "True when the shift key is held.");
//}

static void rna_def_window(BlenderRNA brna)
{
	StructRNA srna;
	PropertyRNA prop;

	srna= RNA_def_struct(brna, "Window", null);
	RNA_def_struct_ui_text(srna, "Window", "Open window");
	RNA_def_struct_sdna(srna, "wmWindow");

	prop= RNA_def_property(srna, "screen", PROP_POINTER, PROP_NONE);
	RNA_def_property_flag(prop, PROP_NEVER_NULL);
	RNA_def_property_struct_type(prop, "Screen");
	RNA_def_property_ui_text(prop, "Screen", "Active screen showing in the window");
	RNA_def_property_flag(prop, PROP_EDITABLE);
	RNA_def_property_pointer_funcs(prop, null, "rna_Window_screen_set", null, null);
	RNA_def_property_flag(prop, PROP_CONTEXT_UPDATE);
	RNA_def_property_update(prop, 0, "rna_Window_screen_update");
}

//static void rna_def_windowmanager(BlenderRNA *brna)
//{
//	StructRNA *srna;
//	PropertyRNA *prop;
//
//	srna= RNA_def_struct(brna, "WindowManager", "ID");
//	RNA_def_struct_ui_text(srna, "Window Manager", "Window manager datablock defining open windows and other user interface data.");
//	RNA_def_struct_sdna(srna, "wmWindowManager");
//
//	prop= RNA_def_property(srna, "operators", PROP_COLLECTION, PROP_NONE);
//	RNA_def_property_struct_type(prop, "Operator");
//	RNA_def_property_ui_text(prop, "Operators", "Operator registry.");
//
//	prop= RNA_def_property(srna, "windows", PROP_COLLECTION, PROP_NONE);
//	RNA_def_property_struct_type(prop, "Window");
//	RNA_def_property_ui_text(prop, "Windows", "Open windows.");
//
//	RNA_api_wm(srna);
//}

public static RNAProcess RNA_def_wm = new RNAProcess() {
public void run(BlenderRNA brna)
{
	rna_def_operator(brna);
//	rna_def_operator_utils(brna);
//	rna_def_operator_filelist_element(brna);
//	rna_def_event(brna);
	rna_def_window(brna);
//	rna_def_windowmanager(brna);
}};

//public static RNAProcess RNA_def_window = new RNAProcess() {
//public void run(BlenderRNA brna)
//{
//	rna_def_window(brna);
//}};

//#endif
}

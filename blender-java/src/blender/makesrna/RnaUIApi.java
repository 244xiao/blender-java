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
package blender.makesrna;

//#include <stdlib.h>

import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

//#include <stdio.h>
//
//#include "RNA_define.h"
//#include "RNA_types.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"

public class RnaUIApi {
//
//#ifdef RNA_RUNTIME
	
	public static void rna_uiItemR(uiLayout layout, PointerRNA ptr, String propname, String name, int icon, int expand, int slider, int toggle, int icon_only, int event, int full_event, int emboss, int index)
	{
		PropertyRNA prop= (PropertyRNA)RnaAccess.RNA_struct_find_property(ptr, propname, false);
		int flag= 0;

		if(prop==null) {
//			System.out.printf("rna_uiItemR: property not found: %s.%s\n", RnaAccess.RNA_struct_identifier(ptr.type), propname);
//			Thread.dumpStack();
			return;
		}

		flag |= (slider!=0)? UI.UI_ITEM_R_SLIDER: 0;
		flag |= (expand!=0)? UI.UI_ITEM_R_EXPAND: 0;
		flag |= (toggle!=0)? UI.UI_ITEM_R_TOGGLE: 0;
		flag |= (icon_only!=0)? UI.UI_ITEM_R_ICON_ONLY: 0;
		flag |= (event!=0)? UI.UI_ITEM_R_EVENT: 0;
		flag |= (full_event!=0)? UI.UI_ITEM_R_FULL_EVENT: 0;
		flag |= (emboss!=0)? 0: UI.UI_ITEM_R_NO_BG;

		UILayout.uiItemFullR(layout, ptr, prop, index, 0, flag, name, icon);
	}

	public static PointerRNA rna_uiItemO(uiLayout layout, String opname, String name, int icon, int emboss)
	{
		int flag= UI.UI_ITEM_O_RETURN_PROPS;
		flag |= (emboss!=0)? 0: UI.UI_ITEM_R_NO_BG;
		return UILayout.uiItemFullO(layout, opname, name, icon, null, UILayout.uiLayoutGetOperatorContext(layout), flag);
	}

//#else
//
//#define DEF_ICON(name) {name, #name, 0, #name, ""},
//static EnumPropertyItem[] icon_items = Resources.icon_items();
static EnumPropertyItem[] icon_items = icon_items();
private static EnumPropertyItem[] icon_items() {
	EnumPropertyItem[] iconEnums = new EnumPropertyItem[BIFIconID.values().length+1];
	BIFIconID[] icons = BIFIconID.values();
	for (int i=0; i<iconEnums.length-1; i++) {
		BIFIconID icon = icons[i];
		iconEnums[i] = new EnumPropertyItem(icon.ordinal(), icon.name(), 0, icon.name(), "");
	}
	iconEnums[iconEnums.length-1] = new EnumPropertyItem(0, null, 0, null, null);
	return iconEnums;
}
//#include "UI_icons.h"
//	{0, NULL, 0, NULL, NULL}};
//#undef DEF_ICON

static void api_ui_item_common(FunctionRNA func)
{
	PropertyRNA prop;

	RnaDefine.RNA_def_string(func, "text", "", 0, "", "Override automatic text of the item.");

	prop= RnaDefine.RNA_def_property(func, "icon", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_items(prop, icon_items);
	RnaDefine.RNA_def_property_ui_text(prop, "Icon", "Override automatic icon of the item");
}

static void api_ui_item_op(FunctionRNA func)
{
	PropertyRNA parm;
	parm= RnaDefine.RNA_def_string(func, "operator", "", 0, "", "Identifier of the operator.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
}

static void api_ui_item_op_common(FunctionRNA func)
{
	api_ui_item_op(func);
	api_ui_item_common(func);
}

static void api_ui_item_rna_common(FunctionRNA func)
{
	PropertyRNA parm;

	parm= RnaDefine.RNA_def_pointer(func, "data", "AnyType", "", "Data from which to take property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
}

	private static EnumPropertyItem[] curve_type_items = {
		new EnumPropertyItem(0, "NONE", 0, "None", ""),
		new EnumPropertyItem('v', "VECTOR", 0, "Vector", ""),
		new EnumPropertyItem('c', "COLOR", 0, "Color", ""),
		new EnumPropertyItem(0, null, 0, null, null)};
	
	private static EnumPropertyItem[] list_type_items = {
		new EnumPropertyItem(0, "DEFAULT", 0, "None", ""),
		new EnumPropertyItem('c', "COMPACT", 0, "Compact", ""),
		new EnumPropertyItem('i', "ICONS", 0, "Icons", ""),
		new EnumPropertyItem(0, null, 0, null, null)};
public static void RNA_api_ui_layout(StructRNA srna)
{
	FunctionRNA func;
	PropertyRNA parm;

//	static EnumPropertyItem curve_type_items[] = {
//		{0, "NONE", 0, "None", ""},
//		{'v', "VECTOR", 0, "Vector", ""},
//		{'c', "COLOR", 0, "Color", ""},
//		{0, NULL, 0, NULL, NULL}};
//	
//	static EnumPropertyItem list_type_items[] = {
//		{0, "DEFAULT", 0, "None", ""},
//		{'c', "COMPACT", 0, "Compact", ""},
//		{'i', "ICONS", 0, "Icons", ""},
//		{0, NULL, 0, NULL, NULL}};

	/* simple layout specifiers */
	func= RnaDefine.RNA_def_function(srna, "row", "uiLayoutRow");
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_function_ui_description(func, "Sub-layout. Items placed in this sublayout are placed next to each other in a row.");
	RnaDefine.RNA_def_boolean(func, "align", 0, "", "Align buttons to each other.");
	
	func= RnaDefine.RNA_def_function(srna, "column", "uiLayoutColumn");
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_function_ui_description(func, "Sub-layout. Items placed in this sublayout are placed under each other in a column.");
	RnaDefine.RNA_def_boolean(func, "align", 0, "", "Align buttons to each other.");

	func= RnaDefine.RNA_def_function(srna, "column_flow", "uiLayoutColumnFlow");
	parm= RnaDefine.RNA_def_int(func, "columns", 0, 0, RnaDefine.INT_MAX, "", "Number of columns, 0 is automatic.", 0, RnaDefine.INT_MAX);
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_boolean(func, "align", 0, "", "Align buttons to each other.");

	/* box layout */
	func= RnaDefine.RNA_def_function(srna, "box", "uiLayoutBox");
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_function_ui_description(func, "Sublayout. Items placed in this sublayout are placed under each other in a column and are surrounded by a box.");
	
	/* split layout */
	func= RnaDefine.RNA_def_function(srna, "split", "uiLayoutSplit");
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_float(func, "percentage", 0.0f, 0.0f, 1.0f, "Percentage", "Percentage of width to split at.", 0.0f, 1.0f);
	RnaDefine.RNA_def_boolean(func, "align", 0, "", "Align buttons to each other.");

	/* items */
	func= RnaDefine.RNA_def_function(srna, "prop", "rna_uiItemR");
	RnaDefine.RNA_def_function_ui_description(func, "Item. Exposes an RNA item and places it into the layout.");
	api_ui_item_rna_common(func);
	api_ui_item_common(func);
	RnaDefine.RNA_def_boolean(func, "expand", 0, "", "Expand button to show more detail.");
	RnaDefine.RNA_def_boolean(func, "slider", 0, "", "Use slider widget for numeric values.");
	RnaDefine.RNA_def_boolean(func, "toggle", 0, "", "Use toggle widget for boolean values.");
	RnaDefine.RNA_def_boolean(func, "icon_only", 0, "", "Draw only icons in buttons, no text.");
	RnaDefine.RNA_def_boolean(func, "event", 0, "", "Use button to input key events.");
	RnaDefine.RNA_def_boolean(func, "full_event", 0, "", "Use button to input full events including modifiers.");
	RnaDefine.RNA_def_boolean(func, "emboss", 1, "", "Draw the button itself, just the icon/text.");
	RnaDefine.RNA_def_int(func, "index", -1, -2, RnaDefine.INT_MAX, "", "The index of this button, when set a single member of an array can be accessed, when set to -1 all array members are used.", -2, RnaDefine.INT_MAX); /* RNA_NO_INDEX == -1 */

	func= RnaDefine.RNA_def_function(srna, "props_enum", "uiItemsEnumR");
	api_ui_item_rna_common(func);

	func= RnaDefine.RNA_def_function(srna, "prop_menu_enum", "uiItemMenuEnumR");
	api_ui_item_rna_common(func);
	api_ui_item_common(func);

	func= RnaDefine.RNA_def_function(srna, "prop_enum", "uiItemEnumR_string");
	api_ui_item_rna_common(func);
	parm= RnaDefine.RNA_def_string(func, "value", "", 0, "", "Enum property value.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	api_ui_item_common(func);

	func= RnaDefine.RNA_def_function(srna, "prop_search", "uiItemPointerR");
	api_ui_item_rna_common(func);
	parm= RnaDefine.RNA_def_pointer(func, "search_data", "AnyType", "", "Data from which to take collection to search in.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_string(func, "search_property", "", 0, "", "Identifier of search collection property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	api_ui_item_common(func);

	func= RnaDefine.RNA_def_function(srna, "operator", "rna_uiItemO");
	api_ui_item_op_common(func);
	RnaDefine.RNA_def_boolean(func, "emboss", 1, "", "Draw the button itself, just the icon/text.");
	parm= RnaDefine.RNA_def_pointer(func, "properties", "OperatorProperties", "", "Operator properties to fill in, return when 'properties' is set to true.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	RnaDefine.RNA_def_function_return(func, parm);
	RnaDefine.RNA_def_function_ui_description(func, "Item. Places a button into the layout to call an Operator.");

/*	func= RNA_def_function(srna, "operator_enum", "uiItemEnumO_string");
	api_ui_item_op_common(func);
	parm= RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RNA_def_property_flag(parm, PROP_REQUIRED);
	parm= RNA_def_string(func, "value", "", 0, "", "Enum property value.");
	RNA_def_property_flag(parm, PROP_REQUIRED); */

	func= RnaDefine.RNA_def_function(srna, "operator_enums", "uiItemsEnumO");
	parm= RnaDefine.RNA_def_string(func, "operator", "", 0, "", "Identifier of the operator.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	func= RnaDefine.RNA_def_function(srna, "operator_menu_enum", "uiItemMenuEnumO");
	api_ui_item_op(func); /* cant use api_ui_item_op_common because property must come right after */
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	api_ui_item_common(func);

/*	func= RNA_def_function(srna, "operator_boolean", "uiItemBooleanO");
	api_ui_item_op_common(func);
	parm= RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RNA_def_property_flag(parm, PROP_REQUIRED);
	parm= RNA_def_boolean(func, "value", 0, "", "Value of the property to call the operator with.");
	RNA_def_property_flag(parm, PROP_REQUIRED); */

/*	func= RNA_def_function(srna, "operator_int", "uiItemIntO");
	api_ui_item_op_common(func);
	parm= RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RNA_def_property_flag(parm, PROP_REQUIRED);
	parm= RNA_def_int(func, "value", 0, INT_MIN, INT_MAX, "", "Value of the property to call the operator with.", INT_MIN, INT_MAX);
	RNA_def_property_flag(parm, PROP_REQUIRED); */

/*	func= RNA_def_function(srna, "operator_float", "uiItemFloatO");
	api_ui_item_op_common(func);
	parm= RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RNA_def_property_flag(parm, PROP_REQUIRED);
	parm= RNA_def_float(func, "value", 0, -FLT_MAX, FLT_MAX, "", "Value of the property to call the operator with.", -FLT_MAX, FLT_MAX);
	RNA_def_property_flag(parm, PROP_REQUIRED); */

/*	func= RNA_def_function(srna, "operator_string", "uiItemStringO");
	api_ui_item_op_common(func);
	parm= RNA_def_string(func, "property", "", 0, "", "Identifier of property in operator.");
	RNA_def_property_flag(parm, PROP_REQUIRED);
	parm= RNA_def_string(func, "value", "", 0, "", "Value of the property to call the operator with.");
	RNA_def_property_flag(parm, PROP_REQUIRED); */

	func= RnaDefine.RNA_def_function(srna, "label", "uiItemL");
	RnaDefine.RNA_def_function_ui_description(func, "Item. Display text in the layout.");
	api_ui_item_common(func);

	func= RnaDefine.RNA_def_function(srna, "menu", "uiItemM");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	parm= RnaDefine.RNA_def_string(func, "menu", "", 0, "", "Identifier of the menu.");
	api_ui_item_common(func);
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	func= RnaDefine.RNA_def_function(srna, "separator", "uiItemS");
	RnaDefine.RNA_def_function_ui_description(func, "Item. Inserts empty space into the layout between items.");

	/* context */
	func= RnaDefine.RNA_def_function(srna, "context_pointer_set", "uiLayoutSetContextPointer");
	parm= RnaDefine.RNA_def_string(func, "name", "", 0, "Name", "Name of entry in the context.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_pointer(func, "data", "AnyType", "", "Pointer to put in context.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	
	/* templates */
	func= RnaDefine.RNA_def_function(srna, "template_header", "uiTemplateHeader");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	RnaDefine.RNA_def_boolean(func, "menus", 1, "", "The header has menus, and should show menu expander.");

	func= RnaDefine.RNA_def_function(srna, "template_ID", "uiTemplateID");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	api_ui_item_rna_common(func);
	RnaDefine.RNA_def_string(func, "new", "", 0, "", "Operator identifier to create a new ID block.");
	RnaDefine.RNA_def_string(func, "open", "", 0, "", "Operator identifier to open a file for creating a new ID block.");
	RnaDefine.RNA_def_string(func, "unlink", "", 0, "", "Operator identifier to unlink the ID block.");
	
	func= RnaDefine.RNA_def_function(srna, "template_ID_preview", "uiTemplateIDPreview");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	api_ui_item_rna_common(func);
	RnaDefine.RNA_def_string(func, "new", "", 0, "", "Operator identifier to create a new ID block.");
	RnaDefine.RNA_def_string(func, "open", "", 0, "", "Operator identifier to open a file for creating a new ID block.");
	RnaDefine.RNA_def_string(func, "unlink", "", 0, "", "Operator identifier to unlink the ID block.");
	RnaDefine.RNA_def_int(func, "rows", 0, 0, RnaDefine.INT_MAX, "Number of thumbnail preview rows to display", "", 0, RnaDefine.INT_MAX);
	RnaDefine.RNA_def_int(func, "cols", 0, 0, RnaDefine.INT_MAX, "Number of thumbnail preview columns to display", "", 0, RnaDefine.INT_MAX);
	
	func= RnaDefine.RNA_def_function(srna, "template_any_ID", "uiTemplateAnyID");
	parm= RnaDefine.RNA_def_pointer(func, "data", "AnyType", "", "Data from which to take property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_string(func, "type_property", "", 0, "", "Identifier of property in data giving the type of the ID-blocks to use.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_string(func, "text", "", 0, "", "Custom label to display in UI.");
	
	func= RnaDefine.RNA_def_function(srna, "template_path_builder", "uiTemplatePathBuilder");
	parm= RnaDefine.RNA_def_pointer(func, "data", "AnyType", "", "Data from which to take property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_pointer(func, "root", "ID", "", "ID-block from which path is evaluated from.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	parm= RnaDefine.RNA_def_string(func, "text", "", 0, "", "Custom label to display in UI.");
	
	func= RnaDefine.RNA_def_function(srna, "template_modifier", "uiTemplateModifier");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	RnaDefine.RNA_def_function_ui_description(func, "Layout . Generates the UI layout for modifiers.");
	parm= RnaDefine.RNA_def_pointer(func, "data", "Modifier", "", "Modifier data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);

	func= RnaDefine.RNA_def_function(srna, "template_constraint", "uiTemplateConstraint");
	RnaDefine.RNA_def_function_ui_description(func, "Layout . Generates the UI layout for constraints.");
	parm= RnaDefine.RNA_def_pointer(func, "data", "Constraint", "", "Constraint data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_pointer(func, "layout", "UILayout", "", "Sub-layout to put items in.");
	RnaDefine.RNA_def_function_return(func, parm);

	func= RnaDefine.RNA_def_function(srna, "template_preview", "uiTemplatePreview");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A preview window for materials, textures, lamps, etc.");
	parm= RnaDefine.RNA_def_pointer(func, "id", "ID", "", "ID datablock.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_boolean(func, "show_buttons", 1, "", "Show preview buttons?");
	RnaDefine.RNA_def_pointer(func, "parent", "ID", "", "ID datablock.");
	RnaDefine.RNA_def_pointer(func, "slot", "TextureSlot", "", "Texture slot.");

	func= RnaDefine.RNA_def_function(srna, "template_curve_mapping", "uiTemplateCurveMapping");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A curve mapping widget used for e.g falloff curves for lamps.");
	api_ui_item_rna_common(func);
	RnaDefine.RNA_def_enum(func, "type", curve_type_items, 0, "Type", "Type of curves to display.");
	RnaDefine.RNA_def_boolean(func, "levels", 0, "", "Show black/white levels.");
	RnaDefine.RNA_def_boolean(func, "brush", 0, "", "Show brush options.");

	func= RnaDefine.RNA_def_function(srna, "template_color_ramp", "uiTemplateColorRamp");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A color ramp widget.");
	api_ui_item_rna_common(func);
	RnaDefine.RNA_def_boolean(func, "expand", 0, "", "Expand button to show more detail.");
	
	func= RnaDefine.RNA_def_function(srna, "template_histogram", "uiTemplateHistogram");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A histogramm widget to analyze imaga data.");
	api_ui_item_rna_common(func);
	
	func= RnaDefine.RNA_def_function(srna, "template_waveform", "uiTemplateWaveform");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A waveform widget to analyze imaga data.");
	api_ui_item_rna_common(func);
	
	func= RnaDefine.RNA_def_function(srna, "template_vectorscope", "uiTemplateVectorscope");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A vectorscope widget to analyze imaga data.");
	api_ui_item_rna_common(func);
	
	func= RnaDefine.RNA_def_function(srna, "template_layers", "uiTemplateLayers");
	api_ui_item_rna_common(func);
	parm= RnaDefine.RNA_def_pointer(func, "used_layers_data", "AnyType", "", "Data from which to take property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	parm= RnaDefine.RNA_def_string(func, "used_layers_property", "", 0, "", "Identifier of property in data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_int(func, "active_layer", 0, 0, RnaDefine.INT_MAX, "Active Layer", "", 0, RnaDefine.INT_MAX);
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	
	func= RnaDefine.RNA_def_function(srna, "template_color_wheel", "uiTemplateColorWheel");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A color wheel widget to pick colors.");
	api_ui_item_rna_common(func);
	RnaDefine.RNA_def_boolean(func, "value_slider", 0, "", "Display the value slider to the right of the color wheel");
	RnaDefine.RNA_def_boolean(func, "lock", 0, "", "Lock the color wheel display to value 1.0 regardless of actual color");
	RnaDefine.RNA_def_boolean(func, "lock_luminosity", 0, "", "Keep the color at its original vector length");
	RnaDefine.RNA_def_boolean(func, "cubic", 1, "", "Cubic saturation for picking values close to white");

	func= RnaDefine.RNA_def_function(srna, "template_image_layers", "uiTemplateImageLayers");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	parm= RnaDefine.RNA_def_pointer(func, "image", "Image", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_pointer(func, "image_user", "ImageUser", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	func= RnaDefine.RNA_def_function(srna, "template_image", "uiTemplateImage");
	RnaDefine.RNA_def_function_ui_description(func, "Item(s). User interface for selecting images and their source paths.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	api_ui_item_rna_common(func);
	parm= RnaDefine.RNA_def_pointer(func, "image_user", "ImageUser", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	RnaDefine.RNA_def_boolean(func, "compact", 0, "", "Use more compact layout.");

	func= RnaDefine.RNA_def_function(srna, "template_list", "uiTemplateList");
	RnaDefine.RNA_def_function_ui_description(func, "Item. A list widget to display data. e.g. vertexgroups.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	parm= RnaDefine.RNA_def_pointer(func, "data", "AnyType", "", "Data from which to take property.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR);
	parm= RnaDefine.RNA_def_string(func, "property", "", 0, "", "Identifier of property in data.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_pointer(func, "active_data", "AnyType", "", "Data from which to take property for the active element.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED|RNATypes.PROP_RNAPTR|RNATypes.PROP_NEVER_NULL);
	parm= RnaDefine.RNA_def_string(func, "active_property", "", 0, "", "Identifier of property in data, for the active element.");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);
	parm= RnaDefine.RNA_def_int(func, "rows", 5, 0, RnaDefine.INT_MAX, "", "Number of rows to display.", 0, RnaDefine.INT_MAX);
	parm= RnaDefine.RNA_def_int(func, "maxrows", 5, 0, RnaDefine.INT_MAX, "", "Maximum number of rows to display.", 0, RnaDefine.INT_MAX);
	parm= RnaDefine.RNA_def_enum(func, "type", list_type_items, 0, "Type", "Type of list to use.");

	func= RnaDefine.RNA_def_function(srna, "template_running_jobs", "uiTemplateRunningJobs");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);

	func= RnaDefine.RNA_def_function(srna, "template_operator_search", "uiTemplateOperatorSearch");

	func= RnaDefine.RNA_def_function(srna, "template_header_3D", "uiTemplateHeader3D");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);
	
	func= RnaDefine.RNA_def_function(srna, "template_reports_banner", "uiTemplateReportsBanner");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_USE_CONTEXT);

	func= RnaDefine.RNA_def_function(srna, "introspect", "uiLayoutIntrospect");
	parm= RnaDefine.RNA_def_string(func, "string", "", 1024*1024, "Descr", "DESCR");
	RnaDefine.RNA_def_function_return(func, parm);
}

}

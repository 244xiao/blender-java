package blender.makesrna;

import static blender.makesrna.RNATypes.PROP_BOOLEAN;
import static blender.makesrna.RNATypes.PROP_COLLECTION;
import static blender.makesrna.RNATypes.PROP_CONTEXT_UPDATE;
import static blender.makesrna.RNATypes.PROP_EDITABLE;
import static blender.makesrna.RNATypes.PROP_ENUM;
import static blender.makesrna.RNATypes.PROP_INT;
import static blender.makesrna.RNATypes.PROP_NEVER_NULL;
import static blender.makesrna.RNATypes.PROP_NONE;
import static blender.makesrna.RNATypes.PROP_POINTER;
import static blender.makesrna.RNATypes.PROP_UNSIGNED;
import static blender.makesrna.RnaDefine.RNA_def_function;
import static blender.makesrna.RnaDefine.RNA_def_function_ui_description;
import static blender.makesrna.RnaDefine.RNA_def_property;
import static blender.makesrna.RnaDefine.RNA_def_property_boolean_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_boolean_negative_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_clear_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_collection_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_items;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_int_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_pointer_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_pointer_sdna;
import static blender.makesrna.RnaDefine.RNA_def_property_struct_type;
import static blender.makesrna.RnaDefine.RNA_def_property_ui_text;
import static blender.makesrna.RnaDefine.RNA_def_property_update;
import static blender.makesrna.RnaDefine.RNA_def_string;
import static blender.makesrna.RnaDefine.RNA_def_struct;
import static blender.makesrna.RnaDefine.RNA_def_struct_sdna;
import static blender.makesrna.RnaDefine.RNA_def_struct_ui_icon;
import static blender.makesrna.RnaDefine.RNA_def_struct_ui_text;
import blender.blenkernel.bContext;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.makesdna.ScreenTypes;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bScreen;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PropPointerSetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.rna_internal_types.UpdateFunc;

public class RnaScreenUtil {
//	/**
//	 * $Id: rna_screen.c 33838 2010-12-21 18:55:49Z ton $
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
//	 * Contributor(s): Blender Foundation (2008), Nathan Letwory
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdlib.h>
//	#include <stddef.h>
//
//	#include "RNA_define.h"
//	#include "RNA_enum_types.h"
//
//	#include "rna_internal.h"
//
//	#include "DNA_screen_types.h"
//	#include "DNA_scene_types.h"

	public static EnumPropertyItem[] region_type_items = {
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_WINDOW, "WINDOW", 0, "Window", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_HEADER, "HEADER", 0, "Header", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_CHANNELS, "CHANNELS", 0, "Channels", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_TEMPORARY, "TEMPORARY", 0, "Temporary", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_UI, "UI", 0, "UI", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_TOOLS, "TOOLS", 0, "Tools", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_TOOL_PROPS, "TOOL_PROPS", 0, "Tool Properties", ""),
		new EnumPropertyItem(ScreenTypes.RGN_TYPE_PREVIEW, "PREVIEW", 0, "Preview", ""),
		new EnumPropertyItem(0, null, 0, null, null)};

//	#include "ED_screen.h"
//
//	#ifdef RNA_RUNTIME
//
//
//	#include "WM_api.h"
//	#include "WM_types.h"

	public static PropPointerSetFunc rna_Screen_scene_set = new PropPointerSetFunc() {
	public void setPtr(PointerRNA ptr, PointerRNA value)
	{
		bScreen sc= (bScreen)ptr.data;

		if(value.data == null)
			return;

		/* exception: can't set screens inside of area/region handers */
		sc.newscene= (Scene)value.data;
	}};

	public static UpdateFunc rna_Screen_scene_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
	{
//		bScreen *sc= (bScreen*)ptr->data;
//
//		/* exception: can't set screens inside of area/region handers */
//		if(sc->newscene) {
//			WM_event_add_notifier(C, NC_SCENE|ND_SCENEBROWSE, sc->newscene);
//			sc->newscene= NULL;
//		}
	}
    public String getName() { return "rna_Screen_scene_update"; }
	};

	public static boolean rna_Screen_is_animation_playing_get(PointerRNA ptr)
	{
		bScreen sc= (bScreen)ptr.data;
		return (sc.animtimer != null);
	}

	public static boolean rna_Screen_fullscreen_get(PointerRNA ptr)
	{
		bScreen sc= (bScreen)ptr.data;
		return (sc.full != 0);
	}

//	static void rna_Area_type_set(PointerRNA *ptr, int value)
//	{
//		ScrArea *sa= (ScrArea*)ptr->data;
//		sa->butspacetype= value;
//	}
//
//	static void rna_Area_type_update(bContext *C, PointerRNA *ptr)
//	{
//		ScrArea *sa= (ScrArea*)ptr->data;
//
//		ED_area_newspace(C, sa, sa->butspacetype); /* XXX - this uses the window */
//		ED_area_tag_redraw(sa);
//	}
//
//	#else

	static void rna_def_area(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		FunctionRNA func;
		PropertyRNA parm;
		
		srna= RNA_def_struct(brna, "Area", null);
		RNA_def_struct_ui_text(srna, "Area", "Area in a subdivided screen, containing an editor");
		RNA_def_struct_sdna(srna, "ScrArea");

		prop= RNA_def_property(srna, "spaces", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "spacedata", null);
		RNA_def_property_struct_type(prop, "Space");
		RNA_def_property_ui_text(prop, "Spaces", "Spaces contained in this area, the first being the active space. NOTE: Useful for example to restore a previously used 3d view space in a certain area to get the old view orientation.");

		prop= RNA_def_property(srna, "active_space", PROP_POINTER, PROP_NONE);
		RNA_def_property_pointer_sdna(prop, null, "spacedata.first");
		RNA_def_property_struct_type(prop, "Space");
		RNA_def_property_ui_text(prop, "Active Space", "Space currently being displayed in this area");

		prop= RNA_def_property(srna, "regions", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "regionbase", null);
		RNA_def_property_struct_type(prop, "Region");
		RNA_def_property_ui_text(prop, "Regions", "Regions this area is subdivided in");

		prop= RNA_def_property(srna, "show_menus", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_boolean_negative_sdna(prop, null, "flag", ScreenTypes.HEADER_NO_PULLDOWN);
		RNA_def_property_ui_text(prop, "Show Menus", "Show menus in the header");
		
		prop= RNA_def_property(srna, "type", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "spacetype");
		RNA_def_property_enum_items(prop, RnaSpaceUtil.space_type_items);
		RNA_def_property_enum_funcs(prop, null, "rna_Area_type_set", null);
		RNA_def_property_ui_text(prop, "Type", "Space type");
		RNA_def_property_flag(prop, PROP_CONTEXT_UPDATE);
		RNA_def_property_update(prop, 0, "rna_Area_type_update");

		RNA_def_function(srna, "tag_redraw", "ED_area_tag_redraw");

		func= RNA_def_function(srna, "header_text_set", "ED_area_headerprint");
		RNA_def_function_ui_description(func, "Set the header text");
		parm= RNA_def_string(func, "text", null, 0, "Text", "New string for the header, no argument clears the text.");
	}

	static void rna_def_region(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		
		srna= RNA_def_struct(brna, "Region", null);
		RNA_def_struct_ui_text(srna, "Region", "Region in a subdivided screen area");
		RNA_def_struct_sdna(srna, "ARegion");
		
		prop= RNA_def_property(srna, "id", PROP_INT, PROP_NONE);
		RNA_def_property_int_sdna(prop, null, "swinid");
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Region ID", "Unique ID for this region");

		prop= RNA_def_property(srna, "type", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_sdna(prop, null, "regiontype");
		RNA_def_property_enum_items(prop, region_type_items);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Region Type", "Type of this region");

		prop= RNA_def_property(srna, "width", PROP_INT, PROP_UNSIGNED);
		RNA_def_property_int_sdna(prop, null, "winx");
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Width", "Region width");

		prop= RNA_def_property(srna, "height", PROP_INT, PROP_UNSIGNED);
		RNA_def_property_int_sdna(prop, null, "winy");
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Height", "Region height");
		
		RNA_def_function(srna, "tag_redraw", "ED_region_tag_redraw");
	}

	static void rna_def_screen(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		
		srna= RNA_def_struct(brna, "Screen", "ID");
		RNA_def_struct_sdna(srna, "Screen"); /* it is actually bScreen but for 2.5 the dna is patched! */
		RNA_def_struct_ui_text(srna, "Screen", "Screen datablock, defining the layout of areas in a window");
		RNA_def_struct_ui_icon(srna, BIFIconID.ICON_SPLITSCREEN.ordinal());
		
		prop= RNA_def_property(srna, "scene", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EDITABLE|PROP_NEVER_NULL);
		RNA_def_property_pointer_funcs(prop, null, "rna_Screen_scene_set", null, null);
		RNA_def_property_ui_text(prop, "Scene", "Active scene to be edited in the screen");
		RNA_def_property_flag(prop, PROP_CONTEXT_UPDATE);
		RNA_def_property_update(prop, 0, "rna_Screen_scene_update");
		
		prop= RNA_def_property(srna, "areas", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_collection_sdna(prop, null, "areabase", null);
		RNA_def_property_struct_type(prop, "Area");
		RNA_def_property_ui_text(prop, "Areas", "Areas the screen is subdivided into");

		prop= RNA_def_property(srna, "is_animation_playing", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_boolean_funcs(prop, "rna_Screen_is_animation_playing_get", null);
		RNA_def_property_ui_text(prop, "Animation Playing", "Animation playback is active");
		
		prop= RNA_def_property(srna, "show_fullscreen", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_boolean_funcs(prop, "rna_Screen_fullscreen_get", null);
		RNA_def_property_ui_text(prop, "Fullscreen", "An area is maximised, filling this screen");
	}

	public static RNAProcess RNA_def_screen = new RNAProcess() {
	public void run(BlenderRNA brna)
	{
		rna_def_screen(brna);
		rna_def_area(brna);
		rna_def_region(brna);
	}};
	
//	public static RNAProcess RNA_def_area = new RNAProcess() {
//	public void run(BlenderRNA brna)
//	{
//		rna_def_area(brna);
//	}};
//		
//	public static RNAProcess RNA_def_region = new RNAProcess() {
//	public void run(BlenderRNA brna)
//	{
//		rna_def_region(brna);
//	}};

//	#endif

}

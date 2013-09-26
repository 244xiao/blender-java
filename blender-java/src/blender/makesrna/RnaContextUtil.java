package blender.makesrna;

import static blender.blenkernel.Blender.U;
import static blender.makesrna.RNATypes.PROP_EDITABLE;
import static blender.makesrna.RNATypes.PROP_ENUM;
import static blender.makesrna.RNATypes.PROP_NONE;
import static blender.makesrna.RNATypes.PROP_POINTER;
import static blender.makesrna.RnaDefine.RNA_def_property;
import static blender.makesrna.RnaDefine.RNA_def_property_clear_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_enum_items;
import static blender.makesrna.RnaDefine.RNA_def_property_pointer_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_struct_type;
import static blender.makesrna.RnaDefine.RNA_def_struct;
import static blender.makesrna.RnaDefine.RNA_def_struct_sdna;
import static blender.makesrna.RnaDefine.RNA_def_struct_ui_text;
import blender.blenkernel.bContext;
import blender.makesdna.sdna.ID;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.PropEnumGetFunc;
import blender.makesrna.rna_internal_types.PropPointerGetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.srna.RnaScene;
import blender.makesrna.srna.RnaBlendData;
import blender.makesrna.srna.RnaScreen;
import blender.makesrna.srna.RnaSpace;
import blender.makesrna.srna.RnaToolSettings;
import blender.makesrna.srna.RnaWindow;

public class RnaContextUtil {
//	/**
//	 * $Id: rna_context.c 31713 2010-09-02 04:53:05Z campbellbarton $
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
//	 * Contributor(s): Blender Foundation (2009).
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdlib.h>
//
//	#include "DNA_ID.h"
//	#include "DNA_userdef_types.h"
//
//	#include "RNA_access.h"
//	#include "RNA_define.h"
//
//	#include "BKE_context.h"
//
//	#ifdef RNA_RUNTIME

	public static PropPointerGetFunc rna_Context_manager_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		return RnaAccess.rna_pointer_inherit_refine(ptr, RnaAccess.RNA_WindowManager, bContext.CTX_wm_manager(C));
//		return new PointerRNA(RnaAccess.RNA_WindowManager, bContext.CTX_wm_manager(C));
	}};

	public static PropPointerGetFunc rna_Context_window_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
//		return rna_pointer_inherit_refine(ptr, &RNA_Window, CTX_wm_window(C));
		return new PointerRNA(RnaWindow.RNA_Window, bContext.CTX_wm_window(C));
	}};

	public static PropPointerGetFunc rna_Context_screen_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
//		return rna_pointer_inherit_refine(ptr, &RNA_Screen, CTX_wm_screen(C));
		return new PointerRNA(RnaScreen.RNA_Screen, bContext.CTX_wm_screen(C));
	}};

	public static PropPointerGetFunc rna_Context_area_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
//		PointerRNA newptr;
//		RNA_pointer_create((ID*)CTX_wm_screen(C), &RNA_Area, CTX_wm_area(C), &newptr);
//		return newptr;
		return new PointerRNA(RnaAccess.RNA_Area, bContext.CTX_wm_area(C));
	}};

	public static PropPointerGetFunc rna_Context_space_data_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		PointerRNA newptr = new PointerRNA();
		RnaAccess.RNA_pointer_create((ID)bContext.CTX_wm_screen(C), RnaSpace.RNA_Space, bContext.CTX_wm_space_data(C), newptr);
		return newptr;
	}};

	public static PropPointerGetFunc rna_Context_region_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		PointerRNA newptr = new PointerRNA();
		RnaAccess.RNA_pointer_create((ID)bContext.CTX_wm_screen(C), RnaAccess.RNA_Region, bContext.CTX_wm_region(C), newptr);
		return newptr;
	}};

	public static PropPointerGetFunc rna_Context_region_data_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;

//		/* only exists for one space still, no generic system yet */
//		if(bContext.CTX_wm_view3d(C)) {
//			PointerRNA newptr = new PointerRNA();
//			RnaAccess.RNA_pointer_create((ID)bContext.CTX_wm_screen(C), RNA_RegionView3D, bContext.CTX_wm_region_data(C), newptr);
//			return newptr;
//		}

		return RnaAccess.PointerRNA_NULL;
	}};

	public static PropPointerGetFunc rna_Context_main_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		return RnaAccess.rna_pointer_inherit_refine(ptr, RnaBlendData.RNA_BlendData, bContext.CTX_data_main(C));
	}};

	public static PropPointerGetFunc rna_Context_scene_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
//		return rna_pointer_inherit_refine(ptr, &RNA_Scene, CTX_data_scene(C));
		return new PointerRNA(RnaScene.RNA_Scene, bContext.CTX_data_scene(C));
	}};

	public static PropPointerGetFunc rna_Context_tool_settings_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		ptr.id.data= bContext.CTX_data_scene(C);
//		return rna_pointer_inherit_refine(ptr, &RNA_ToolSettings, CTX_data_tool_settings(C));
		return new PointerRNA(RnaToolSettings.RNA_ToolSettings, bContext.CTX_data_tool_settings(C));
	}};

	public static PropPointerGetFunc rna_Context_user_preferences_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
//		PointerRNA newptr;
//		RNA_pointer_create(NULL, &RNA_UserPreferences, &U, &newptr);
//		return newptr;
		return new PointerRNA(RnaAccess.RNA_UserPreferences, U);
	}};

	public static PropEnumGetFunc rna_Context_mode_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
	{
		bContext C= (bContext)ptr.data;
		return bContext.CTX_data_mode_enum(C);
	}};

//	#else

	static EnumPropertyItem[] mode_items = {
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_MESH, "EDIT_MESH", 0, "Mesh Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_CURVE, "EDIT_CURVE", 0, "Curve Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_SURFACE, "EDIT_SURFACE", 0, "Surface Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_TEXT, "EDIT_TEXT", 0, "Edit Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_ARMATURE, "EDIT_ARMATURE", 0, "Armature Edit", ""), // PARSKEL reuse will give issues
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_METABALL, "EDIT_METABALL", 0, "Metaball Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_EDIT_LATTICE, "EDIT_LATTICE", 0, "Lattice Edit", ""),
		new EnumPropertyItem(bContext.CTX_MODE_POSE, "POSE", 0, "Pose ", ""),
		new EnumPropertyItem(bContext.CTX_MODE_SCULPT, "SCULPT", 0, "Sculpt", ""),
		new EnumPropertyItem(bContext.CTX_MODE_PAINT_WEIGHT, "PAINT_WEIGHT", 0, "Weight Paint", ""),
		new EnumPropertyItem(bContext.CTX_MODE_PAINT_VERTEX, "PAINT_VERTEX", 0, "Vertex Paint", ""),
		new EnumPropertyItem(bContext.CTX_MODE_PAINT_TEXTURE, "PAINT_TEXTURE", 0, "Texture Paint", ""),
		new EnumPropertyItem(bContext.CTX_MODE_PARTICLE, "PARTICLE", 0, "Particle", ""),
		new EnumPropertyItem(bContext.CTX_MODE_OBJECT, "OBJECT", 0, "Object", ""),
		new EnumPropertyItem(0, null, 0, null, null)};
	
	public static RNAProcess RNA_def_context = new RNAProcess() {
	public void run(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;

//		static EnumPropertyItem mode_items[] = {
//			{CTX_MODE_EDIT_MESH, "EDIT_MESH", 0, "Mesh Edit", ""},
//			{CTX_MODE_EDIT_CURVE, "EDIT_CURVE", 0, "Curve Edit", ""},
//			{CTX_MODE_EDIT_SURFACE, "EDIT_SURFACE", 0, "Surface Edit", ""},
//			{CTX_MODE_EDIT_TEXT, "EDIT_TEXT", 0, "Edit Edit", ""},
//			{CTX_MODE_EDIT_ARMATURE, "EDIT_ARMATURE", 0, "Armature Edit", ""}, // PARSKEL reuse will give issues
//			{CTX_MODE_EDIT_METABALL, "EDIT_METABALL", 0, "Metaball Edit", ""},
//			{CTX_MODE_EDIT_LATTICE, "EDIT_LATTICE", 0, "Lattice Edit", ""},
//			{CTX_MODE_POSE, "POSE", 0, "Pose ", ""},
//			{CTX_MODE_SCULPT, "SCULPT", 0, "Sculpt", ""},
//			{CTX_MODE_PAINT_WEIGHT, "PAINT_WEIGHT", 0, "Weight Paint", ""},
//			{CTX_MODE_PAINT_VERTEX, "PAINT_VERTEX", 0, "Vertex Paint", ""},
//			{CTX_MODE_PAINT_TEXTURE, "PAINT_TEXTURE", 0, "Texture Paint", ""},
//			{CTX_MODE_PARTICLE, "PARTICLE", 0, "Particle", ""},
//			{CTX_MODE_OBJECT, "OBJECT", 0, "Object", ""},
//			{0, NULL, 0, NULL, NULL}};

		srna= RNA_def_struct(brna, "Context", null);
		RNA_def_struct_ui_text(srna, "Context", "Current windowmanager and data context");
		RNA_def_struct_sdna(srna, "bContext");

		/* WM */
		prop= RNA_def_property(srna, "window_manager", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "WindowManager");
		RNA_def_property_pointer_funcs(prop, "rna_Context_manager_get", null, null, null);

		prop= RNA_def_property(srna, "window", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Window");
		RNA_def_property_pointer_funcs(prop, "rna_Context_window_get", null, null, null);

		prop= RNA_def_property(srna, "screen", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Screen");
		RNA_def_property_pointer_funcs(prop, "rna_Context_screen_get", null, null, null);

		prop= RNA_def_property(srna, "area", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Area");
		RNA_def_property_pointer_funcs(prop, "rna_Context_area_get", null, null, null);

		prop= RNA_def_property(srna, "space_data", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Space");
		RNA_def_property_pointer_funcs(prop, "rna_Context_space_data_get", null, null, null);

		prop= RNA_def_property(srna, "region", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Region");
		RNA_def_property_pointer_funcs(prop, "rna_Context_region_get", null, null, null);

		prop= RNA_def_property(srna, "region_data", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "RegionView3D");
		RNA_def_property_pointer_funcs(prop, "rna_Context_region_data_get", null, null, null);

		/* Data */
		prop= RNA_def_property(srna, "blend_data", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "BlendData");
		RNA_def_property_pointer_funcs(prop, "rna_Context_main_get", null, null, null);

		prop= RNA_def_property(srna, "scene", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "Scene");
		RNA_def_property_pointer_funcs(prop, "rna_Context_scene_get", null, null, null);

		prop= RNA_def_property(srna, "tool_settings", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "ToolSettings");
		RNA_def_property_pointer_funcs(prop, "rna_Context_tool_settings_get", null, null, null);

		prop= RNA_def_property(srna, "user_preferences", PROP_POINTER, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "UserPreferences");
		RNA_def_property_pointer_funcs(prop, "rna_Context_user_preferences_get", null, null, null);

		prop= RNA_def_property(srna, "mode", PROP_ENUM, PROP_NONE);
		RNA_def_property_enum_items(prop, mode_items);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_enum_funcs(prop, "rna_Context_mode_get", null, null);
	}};

//	#endif

}

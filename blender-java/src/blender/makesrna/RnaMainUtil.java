package blender.makesrna;

import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.*;
import blender.blenkernel.Main;
import blender.blenlib.StringUtil;
import static blender.blenkernel.Blender.G;
import static blender.editors.uinterface.Resources.BIFIconID.*;
import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaDefine.*;
import static blender.makesrna.RnaMainApi.*;
import static blender.windowmanager.WmTypes.*;

public class RnaMainUtil {
//	/**
//	 * $Id: rna_main.c 32433 2010-10-12 23:47:43Z campbellbarton $
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
//	 * Contributor(s): Blender Foundation (2008).
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdlib.h>
//	#include <string.h>
//
//	#include "RNA_define.h"
//
//	#include "rna_internal.h"
//
//	#ifdef RNA_RUNTIME
//
//	#include "BKE_main.h"
//	#include "BKE_mesh.h"
//	#include "BKE_global.h"

	/* all the list begin functions are added manually here, Main is not in SDNA */

	public static PropBooleanGetFunc rna_Main_is_dirty_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
	{
		return G.relbase_valid==0;
	}};

	public static PropStringGetFunc rna_Main_filepath_get = new PropStringGetFunc() {
	public void getStr(PointerRNA ptr, byte[] value, int offset)
	{
		Main bmain= (Main)ptr.data;
		StringUtil.BLI_strncpy(value,offset, bmain.name,0, bmain.name.length);
	}};

	public static PropStringLengthFunc rna_Main_filepath_length = new PropStringLengthFunc() {
	public int lenStr(PointerRNA ptr)
	{
		Main bmain= (Main)ptr.data;
		return StringUtil.strlen(bmain.name,0);
	}};

//	#if 0
//	static void rna_Main_filepath_set(PointerRNA *ptr, const char *value)
//	{
//		Main *bmain= (Main*)ptr->data;
//		BLI_strncpy(bmain->name, value, sizeof(bmain->name));
//	}
//	#endif

	public static PropCollectionBeginFunc rna_Main_scene_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->scene, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_object_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->object, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_lamp_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->lamp, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_library_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->library, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_mesh_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->mesh, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_curve_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->curve, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_mball_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->mball, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_mat_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->mat, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_tex_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->tex, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_image_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->image, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_latt_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->latt, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_camera_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->camera, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_key_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->key, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_world_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->world, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_screen_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->screen, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_script_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->script, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_font_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->vfont, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_text_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->text, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_sound_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->sound, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_group_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->group, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_armature_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->armature, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_action_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->action, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_nodetree_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->nodetree, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_brush_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->brush, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_particle_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->particle, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_gpencil_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->gpencil, NULL);
	}};

	public static PropCollectionBeginFunc rna_Main_wm_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		Main *bmain= (Main*)ptr->data;
//		rna_iterator_listbase_begin(iter, &bmain->wm, NULL);
	}};

//	#ifdef UNIT_TEST
//
//	static PointerRNA rna_Test_test_get(PointerRNA *ptr)
//	{
//		PointerRNA ret= *ptr;
//		ret.type= &RNA_Test;
//
//		return ret;
//	}
//
//	#endif
//
//	#else

	/* local convenience types */
	static interface CollectionDefFunc {
		public void run(BlenderRNA brna, PropertyRNA cprop);
	}

	static class MainCollectionDef {
		String identifier;
		String type;
		String iter_begin;
		String name;
		String description;
		CollectionDefFunc func;
		public MainCollectionDef(String identifier, String type, String iter_begin, String name, String description, CollectionDefFunc func) {
			this.identifier = identifier;
			this.type = type;
			this.iter_begin = iter_begin;
			this.name = name;
			this.description = description;
		}
	};

	public static RNAProcess RNA_def_main = new RNAProcess() {
	public void run(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;
		CollectionDefFunc func;

		/* plural must match idtypes in readblenentry.c */
		MainCollectionDef lists[]= {
			new MainCollectionDef("cameras", "Camera", "rna_Main_camera_begin", "Cameras", "Camera datablocks.", RNA_def_main_cameras),
			new MainCollectionDef("scenes", "Scene", "rna_Main_scene_begin", "Scenes", "Scene datablocks.", RNA_def_main_scenes),
			new MainCollectionDef("objects", "Object", "rna_Main_object_begin", "Objects", "Object datablocks.", RNA_def_main_objects),
			new MainCollectionDef("materials", "Material", "rna_Main_mat_begin", "Materials", "Material datablocks.", RNA_def_main_materials),
			new MainCollectionDef("node_groups", "NodeTree", "rna_Main_nodetree_begin", "Node Groups", "Node group datablocks.", RNA_def_main_node_groups),
			new MainCollectionDef("meshes", "Mesh", "rna_Main_mesh_begin", "Meshes", "Mesh datablocks.", RNA_def_main_meshes),
			new MainCollectionDef("lamps", "Lamp", "rna_Main_lamp_begin", "Lamps", "Lamp datablocks.", RNA_def_main_lamps),
			new MainCollectionDef("libraries", "Library", "rna_Main_library_begin", "Libraries", "Library datablocks.", RNA_def_main_libraries),
			new MainCollectionDef("screens", "Screen", "rna_Main_screen_begin", "Screens", "Screen datablocks.", RNA_def_main_screens),
			new MainCollectionDef("window_managers", "WindowManager", "rna_Main_wm_begin", "Window Managers", "Window manager datablocks.", RNA_def_main_window_managers),
			new MainCollectionDef("images", "Image", "rna_Main_image_begin", "Images", "Image datablocks.", RNA_def_main_images),
			new MainCollectionDef("lattices", "Lattice", "rna_Main_latt_begin", "Lattices", "Lattice datablocks.", RNA_def_main_lattices),
			new MainCollectionDef("curves", "Curve", "rna_Main_curve_begin", "Curves", "Curve datablocks.", RNA_def_main_curves) ,
			new MainCollectionDef("metaballs", "MetaBall", "rna_Main_mball_begin", "Metaballs", "Metaball datablocks.", RNA_def_main_metaballs),
			new MainCollectionDef("fonts", "VectorFont", "rna_Main_font_begin", "Vector Fonts", "Vector font datablocks.", RNA_def_main_fonts),
			new MainCollectionDef("textures", "Texture", "rna_Main_tex_begin", "Textures", "Texture datablocks.", RNA_def_main_textures),
			new MainCollectionDef("brushes", "Brush", "rna_Main_brush_begin", "Brushes", "Brush datablocks.", RNA_def_main_brushes),
			new MainCollectionDef("worlds", "World", "rna_Main_world_begin", "Worlds", "World datablocks.", RNA_def_main_worlds),
			new MainCollectionDef("groups", "Group", "rna_Main_group_begin", "Groups", "Group datablocks.", RNA_def_main_groups),
			new MainCollectionDef("shape_keys", "Key", "rna_Main_key_begin", "Keys", "Key datablocks.", null),
			new MainCollectionDef("scripts", "ID", "rna_Main_script_begin", "Scripts", "Script datablocks (DEPRECATED).", null),
			new MainCollectionDef("texts", "Text", "rna_Main_text_begin", "Texts", "Text datablocks.", RNA_def_main_texts),
			new MainCollectionDef("sounds", "Sound", "rna_Main_sound_begin", "Sounds", "Sound datablocks.", RNA_def_main_sounds),
			new MainCollectionDef("armatures", "Armature", "rna_Main_armature_begin", "Armatures", "Armature datablocks.", RNA_def_main_armatures),
			new MainCollectionDef("actions", "Action", "rna_Main_action_begin", "Actions", "Action datablocks.", RNA_def_main_actions),
			new MainCollectionDef("particles", "ParticleSettings", "rna_Main_particle_begin", "Particles", "Particle datablocks.", RNA_def_main_particles),
			new MainCollectionDef("grease_pencil", "GreasePencil", "rna_Main_gpencil_begin", "Grease Pencil", "Grease Pencil datablocks.", RNA_def_main_gpencil),
			new MainCollectionDef(null, null, null, null, null, null)};

		int i;
		
		srna= RNA_def_struct(brna, "BlendData", null);
		RNA_def_struct_ui_text(srna, "Blendfile Data", "Main data structure representing a .blend file and all its datablocks");
		RNA_def_struct_ui_icon(srna, ICON_BLENDER);

		prop= RNA_def_property(srna, "filepath", PROP_STRING, PROP_FILEPATH);
		RNA_def_property_string_maxlength(prop, 240);
		RNA_def_property_string_funcs(prop, "rna_Main_filepath_get", "rna_Main_filepath_length", "rna_Main_filepath_set");
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Filename", "Path to the .blend file");
		
		prop= RNA_def_property(srna, "is_dirty", PROP_BOOLEAN, PROP_NONE);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_boolean_funcs(prop, "rna_Main_is_dirty_get", null);
		RNA_def_property_ui_text(prop, "File is Saved", "Has the current session been saved to disk as a .blend file");

		for(i=0; lists[i].name!=null; i++)
		{
			prop= RNA_def_property(srna, lists[i].identifier, PROP_COLLECTION, PROP_NONE);
			RNA_def_property_struct_type(prop, lists[i].type);
			RNA_def_property_collection_funcs(prop, lists[i].iter_begin, "rna_iterator_listbase_next", "rna_iterator_listbase_end", "rna_iterator_listbase_get", null, null, null);
			RNA_def_property_ui_text(prop, lists[i].name, lists[i].description);

			/* collection functions */
			func= lists[i].func;
			if(func!=null)
				func.run(brna, prop);
		}

		RNA_api_main(srna);

//	#ifdef UNIT_TEST
//
//		RNA_define_verify_sdna(0);
//
//		prop= RNA_def_property(srna, "test", PROP_POINTER, PROP_NONE);
//		RNA_def_property_struct_type(prop, "Test");
//		RNA_def_property_pointer_funcs(prop, "rna_Test_test_get", NULL, NULL, NULL);
//
//		RNA_define_verify_sdna(1);
//
//	#endif
	}};

//	#endif

}

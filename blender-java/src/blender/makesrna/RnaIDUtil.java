package blender.makesrna;

import static blender.makesrna.RNATypes.PROP_COLLECTION;
import static blender.makesrna.RNATypes.PROP_EDITABLE;
import static blender.makesrna.RNATypes.PROP_EXPORT;
import static blender.makesrna.RNATypes.PROP_FLOAT;
import static blender.makesrna.RNATypes.PROP_IDPROPERTY;
import static blender.makesrna.RNATypes.PROP_INT;
import static blender.makesrna.RNATypes.PROP_NONE;
import static blender.makesrna.RNATypes.PROP_POINTER;
import static blender.makesrna.RNATypes.PROP_STRING;
import static blender.makesrna.RnaDefine.RNA_def_property;
import static blender.makesrna.RnaDefine.RNA_def_property_array;
import static blender.makesrna.RnaDefine.RNA_def_property_clear_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_collection_funcs;
import static blender.makesrna.RnaDefine.RNA_def_property_flag;
import static blender.makesrna.RnaDefine.RNA_def_property_struct_type;
import static blender.makesrna.RnaDefine.RNA_def_property_ui_text;
import static blender.makesrna.RnaDefine.RNA_def_struct;
import static blender.makesrna.RnaDefine.RNA_def_struct_idprops_func;
import static blender.makesrna.RnaDefine.RNA_def_struct_name_property;
import static blender.makesrna.RnaDefine.RNA_def_struct_refine_func;
import static blender.makesrna.RnaDefine.RNA_def_struct_register_funcs;
import static blender.makesrna.RnaDefine.RNA_def_struct_ui_text;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.IDProperty;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionLengthFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.srna.RnaScene;
import blender.makesrna.srna.RnaScreen;

public class RnaIDUtil {
//	/**
//	 * $Id: rna_ID.c 33738 2010-12-17 14:20:20Z ton $
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
//	#include <stdio.h>
//
//	#include "RNA_access.h"
//	#include "RNA_define.h"
//
//	#include "DNA_ID.h"
//	#include "DNA_vfont_types.h"
//
//	#include "WM_types.h"
//
//	#include "rna_internal.h"
//
//	/* enum of ID-block types 
//	 * NOTE: need to keep this in line with the other defines for these
//	 */
//	EnumPropertyItem id_type_items[] = {
//		{ID_AC, "ACTION", ICON_ACTION, "Action", ""},
//		{ID_AR, "ARMATURE", ICON_ARMATURE_DATA, "Armature", ""},
//		{ID_BR, "BRUSH", ICON_BRUSH_DATA, "Brush", ""},
//		{ID_CA, "CAMERA", ICON_CAMERA_DATA, "Camera", ""},
//		{ID_CU, "CURVE", ICON_CURVE_DATA, "Curve", ""},
//		{ID_VF, "FONT", ICON_FONT_DATA, "Font", ""},
//		{ID_GD, "GREASEPENCIL", ICON_GREASEPENCIL, "Grease Pencil", ""},
//		{ID_GR, "GROUP", ICON_GROUP, "Group", ""},
//		{ID_IM, "IMAGE", ICON_IMAGE_DATA, "Image", ""},
//		{ID_KE, "KEY", ICON_SHAPEKEY_DATA, "Key", ""},
//		{ID_LA, "LAMP", ICON_LAMP_DATA, "Lamp", ""},
//		{ID_LI, "LIBRARY", ICON_LIBRARY_DATA_DIRECT, "Library", ""},
//		{ID_LT, "LATTICE", ICON_LATTICE_DATA, "Lattice", ""},
//		{ID_MA, "MATERIAL", ICON_MATERIAL_DATA, "Material", ""},
//		{ID_MB, "META", ICON_META_DATA, "MetaBall", ""},
//		{ID_ME, "MESH", ICON_MESH_DATA, "Mesh", ""},
//		{ID_NT, "NODETREE", ICON_NODETREE, "NodeTree", ""},
//		{ID_OB, "OBJECT", ICON_OBJECT_DATA, "Object", ""},
//		{ID_PA, "PARTICLE", ICON_PARTICLE_DATA, "Particle", ""},
//		{ID_SCE, "SCENE", ICON_SCENE_DATA, "Scene", ""},
//		{ID_SCR, "SCREEN", ICON_SPLITSCREEN, "Screen", ""},
//		{ID_SO, "SOUND", ICON_PLAY_AUDIO, "Sound", ""},
//		{ID_TXT, "TEXT", ICON_TEXT, "Text", ""},
//		{ID_TE, "TEXTURE", ICON_TEXTURE_DATA, "Texture", ""},
//		{ID_WO, "WORLD", ICON_WORLD_DATA, "World", ""},
//		{ID_WM, "WINDOWMANAGER", ICON_FULLSCREEN, "Window Manager", ""},
//		{0, NULL, 0, NULL, NULL}};
//
//	#ifdef RNA_RUNTIME
//
//	#include "BKE_idprop.h"
//	#include "BKE_library.h"
//	#include "BKE_animsys.h"
//	#include "BKE_material.h"
//
//	/* name functions that ignore the first two ID characters */
//	void rna_ID_name_get(PointerRNA *ptr, char *value)
//	{
//		ID *id= (ID*)ptr->data;
//		BLI_strncpy(value, id->name+2, sizeof(id->name)-2);
//	}
//
//	int rna_ID_name_length(PointerRNA *ptr)
//	{
//		ID *id= (ID*)ptr->data;
//		return strlen(id->name+2);
//	}
//
//	void rna_ID_name_set(PointerRNA *ptr, const char *value)
//	{
//		ID *id= (ID*)ptr->data;
//		BLI_strncpy(id->name+2, value, sizeof(id->name)-2);
//		test_idbutton(id->name+2);
//	}
//
//	static int rna_ID_name_editable(PointerRNA *ptr)
//	{
//		ID *id= (ID*)ptr->data;
//		
//		if (GS(id->name) == ID_VF) {
//			VFont *vf= (VFont *)id;
//			if (strcmp(vf->name, FO_BUILTIN_NAME)==0)
//				return 0;
//		}
//		
//		return 1;
//	}

	public static short RNA_type_to_ID_code(StructRNA type)
	{
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Action)) return DNA_ID.ID_AC;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Armature)) return DNA_ID.ID_AR;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Brush)) return DNA_ID.ID_BR;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Camera)) return DNA_ID.ID_CA;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Curve)) return DNA_ID.ID_CU;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_GreasePencil)) return DNA_ID.ID_GD;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Group)) return DNA_ID.ID_GR;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Image)) return DNA_ID.ID_IM;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Key)) return DNA_ID.ID_KE;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Lamp)) return DNA_ID.ID_LA;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Library)) return DNA_ID.ID_LI;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Lattice)) return DNA_ID.ID_LT;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Material)) return DNA_ID.ID_MA;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_MetaBall)) return DNA_ID.ID_MB;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_NodeTree)) return DNA_ID.ID_NT;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Mesh)) return DNA_ID.ID_ME;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Object)) return DNA_ID.ID_OB;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_ParticleSettings)) return DNA_ID.ID_PA;
		if(RnaAccess.RNA_struct_is_a(type, RnaScene.RNA_Scene)) return DNA_ID.ID_SCE;
		if(RnaAccess.RNA_struct_is_a(type, RnaScreen.RNA_Screen)) return DNA_ID.ID_SCR;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Sound)) return DNA_ID.ID_SO;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Text)) return DNA_ID.ID_TXT;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_Texture)) return DNA_ID.ID_TE;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_VectorFont)) return DNA_ID.ID_VF;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_World)) return DNA_ID.ID_WO;
		if(RnaAccess.RNA_struct_is_a(type, RnaAccess.RNA_WindowManager)) return DNA_ID.ID_WM;

		return 0;
	}

//	StructRNA *ID_code_to_RNA_type(short idcode)
//	{
//		switch(idcode) {
//			case ID_AC: return &RNA_Action;
//			case ID_AR: return &RNA_Armature;
//			case ID_BR: return &RNA_Brush;
//			case ID_CA: return &RNA_Camera;
//			case ID_CU: return &RNA_Curve;
//			case ID_GD: return &RNA_GreasePencil;
//			case ID_GR: return &RNA_Group;
//			case ID_IM: return &RNA_Image;
//			case ID_KE: return &RNA_Key;
//			case ID_LA: return &RNA_Lamp;
//			case ID_LI: return &RNA_Library;
//			case ID_LT: return &RNA_Lattice;
//			case ID_MA: return &RNA_Material;
//			case ID_MB: return &RNA_MetaBall;
//			case ID_NT: return &RNA_NodeTree;
//			case ID_ME: return &RNA_Mesh;
//			case ID_OB: return &RNA_Object;
//			case ID_PA: return &RNA_ParticleSettings;
//			case ID_SCE: return &RNA_Scene;
//			case ID_SCR: return &RNA_Screen;
//			case ID_SO: return &RNA_Sound;
//			case ID_TXT: return &RNA_Text;
//			case ID_TE: return &RNA_Texture;
//			case ID_VF: return &RNA_VectorFont;
//			case ID_WO: return &RNA_World;
//			case ID_WM: return &RNA_WindowManager;
//			default: return &RNA_ID;
//		}
//	}
//
//	StructRNA *rna_ID_refine(PointerRNA *ptr)
//	{
//		ID *id= (ID*)ptr->data;
//
//		return ID_code_to_RNA_type(GS(id->name));
//	}
//
//	IDProperty *rna_ID_idprops(PointerRNA *ptr, int create)
//	{
//		return IDP_GetProperties(ptr->data, create);
//	}
//
//	void rna_ID_fake_user_set(PointerRNA *ptr, int value)
//	{
//		ID *id= (ID*)ptr->data;
//
//		if(value && !(id->flag & LIB_FAKEUSER)) {
//			id->flag |= LIB_FAKEUSER;
//			id_us_plus(id);
//		}
//		else if(!value && (id->flag & LIB_FAKEUSER)) {
//			id->flag &= ~LIB_FAKEUSER;
//			id_us_min(id);
//		}
//	}
//
//	IDProperty *rna_IDPropertyGroup_idprops(PointerRNA *ptr, int create)
//	{
//		return ptr->data;
//	}
//
//	void rna_IDPropertyGroup_unregister(const bContext *C, StructRNA *type)
//	{
//		RNA_struct_free(&BLENDER_RNA, type);
//	}
//
//	StructRNA *rna_IDPropertyGroup_register(bContext *C, ReportList *reports, void *data, const char *identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
//	{
//		PointerRNA dummyptr;
//
//		/* create dummy pointer */
//		RNA_pointer_create(NULL, &RNA_IDPropertyGroup, NULL, &dummyptr);
//
//		/* validate the python class */
//		if(validate(&dummyptr, data, NULL) != 0)
//			return NULL;
//
//		/* note: it looks like there is no length limit on the srna id since its
//		 * just a char pointer, but take care here, also be careful that python
//		 * owns the string pointer which it could potentually free while blender
//		 * is running. */
//		if(BLI_strnlen(identifier, MAX_IDPROP_NAME) == MAX_IDPROP_NAME) {
//			BKE_reportf(reports, RPT_ERROR, "registering id property class: '%s' is too long, maximum length is " STRINGIFY(MAX_IDPROP_NAME) ".", identifier);
//			return NULL;
//		}
//
//		return RNA_def_struct(&BLENDER_RNA, identifier, "IDPropertyGroup");  // XXX
//	}
//
//	StructRNA* rna_IDPropertyGroup_refine(PointerRNA *ptr)
//	{
//		return ptr->type;
//	}
//
//	ID *rna_ID_copy(ID *id)
//	{
//		ID *newid;
//
//		if(id_copy(id, &newid, 0)) {
//			if(newid) id_us_min(newid);
//			return newid;
//		}
//		
//		return NULL;
//	}
//
//	void rna_ID_user_clear(ID *id)
//	{
//		id->us= 0; /* dont save */
//		id->flag &= ~LIB_FAKEUSER;
//	}

	public static PropCollectionBeginFunc rna_IDPArray_begin = new PropCollectionBeginFunc() {
		public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
//	static void rna_IDPArray_begin(CollectionPropertyIterator *iter, PointerRNA *ptr)
	{
		IDProperty prop= (IDProperty)ptr.data;
//		rna_iterator_array_begin(iter, IDP_IDPArray(prop), sizeof(IDProperty), prop.len, 0, null);
		RnaAccess.rna_iterator_array_begin(iter, (Object[])prop.data.pointer, 1, prop.len, 0, null);
	}};

	public static PropCollectionLengthFunc rna_IDPArray_length = new PropCollectionLengthFunc() {
		public int length(PointerRNA ptr)
//	public static int rna_IDPArray_length(PointerRNA ptr)
	{
		IDProperty prop= (IDProperty)ptr.data;
		return prop.len;
	}};

//	#else

	public static void rna_def_ID_properties(BlenderRNA brna)
	{
		StructRNA srna;
		PropertyRNA prop;

		/* this is struct is used for holding the virtual
		 * PropertyRNA's for ID properties */
		srna= RNA_def_struct(brna, "IDProperty", null);
		RNA_def_struct_ui_text(srna, "ID Property", "Property that stores arbitrary, user defined properties");
		
		/* IDP_STRING */
		prop= RNA_def_property(srna, "string", PROP_STRING, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);

		/* IDP_INT */
		prop= RNA_def_property(srna, "int", PROP_INT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);

		prop= RNA_def_property(srna, "int_array", PROP_INT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		RNA_def_property_array(prop, 1);

		/* IDP_FLOAT */
		prop= RNA_def_property(srna, "float", PROP_FLOAT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);

		prop= RNA_def_property(srna, "float_array", PROP_FLOAT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		RNA_def_property_array(prop, 1);

		/* IDP_DOUBLE */
		prop= RNA_def_property(srna, "double", PROP_FLOAT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);

		prop= RNA_def_property(srna, "double_array", PROP_FLOAT, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		RNA_def_property_array(prop, 1);

		/* IDP_GROUP */
		prop= RNA_def_property(srna, "group", PROP_POINTER, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_struct_type(prop, "IDPropertyGroup");

		prop= RNA_def_property(srna, "collection", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		RNA_def_property_struct_type(prop, "IDPropertyGroup");

		prop= RNA_def_property(srna, "idp_array", PROP_COLLECTION, PROP_NONE);
		RNA_def_property_struct_type(prop, "IDPropertyGroup");
		RNA_def_property_collection_funcs(prop, "rna_IDPArray_begin", "rna_iterator_array_next", "rna_iterator_array_end", "rna_iterator_array_get", "rna_IDPArray_length", null, null);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);

		// never tested, maybe its useful to have this?
//	#if 0
//		prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
//		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
//		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//		RNA_def_property_ui_text(prop, "Name", "Unique name used in the code and scripting");
//		RNA_def_struct_name_property(srna, prop);
//	#endif

		/* IDP_ID -- not implemented yet in id properties */

		/* ID property groups > level 0, since level 0 group is merged
		 * with native RNA properties. the builtin_properties will take
		 * care of the properties here */
		srna= RNA_def_struct(brna, "IDPropertyGroup", null);
		RNA_def_struct_ui_text(srna, "ID Property Group", "Group of ID properties");
		RNA_def_struct_idprops_func(srna, "rna_IDPropertyGroup_idprops");
		RNA_def_struct_register_funcs(srna, "rna_IDPropertyGroup_register", "rna_IDPropertyGroup_unregister");
		RNA_def_struct_refine_func(srna, "rna_IDPropertyGroup_refine");

		/* important so python types can have their name used in list views
		 * however this isnt prefect because it overrides how python would set the name
		 * when we only really want this so RNA_def_struct_name_property() is set to something useful */
		prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
		RNA_def_property_flag(prop, PROP_EXPORT|PROP_IDPROPERTY);
		//RNA_def_property_clear_flag(prop, PROP_EDITABLE);
		RNA_def_property_ui_text(prop, "Name", "Unique name used in the code and scripting");
		RNA_def_struct_name_property(srna, prop);
	}


//	static void rna_def_ID_materials(BlenderRNA *brna)
//	{
//		StructRNA *srna;
//		FunctionRNA *func;
//		PropertyRNA *parm;
//		
//		/* for mesh/mball/curve materials */
//		srna= RNA_def_struct(brna, "IDMaterials", NULL);
//		RNA_def_struct_sdna(srna, "ID");
//		RNA_def_struct_ui_text(srna, "ID Materials", "Collection of materials");
//
//		func= RNA_def_function(srna, "append", "material_append_id");
//		RNA_def_function_ui_description(func, "Add a new material to Mesh.");
//		parm= RNA_def_pointer(func, "material", "Material", "", "Material to add.");
//		RNA_def_property_flag(parm, PROP_REQUIRED);
//		
//		func= RNA_def_function(srna, "pop", "material_pop_id");
//		RNA_def_function_ui_description(func, "Add a new material to Mesh.");
//		parm= RNA_def_int(func, "index", 0, 0, INT_MAX, "", "Frame number to set.", 0, INT_MAX);
//		RNA_def_property_flag(parm, PROP_REQUIRED);
//		parm= RNA_def_pointer(func, "material", "Material", "", "Material to add.");
//		RNA_def_function_return(func, parm);
//	}
//
//	static void rna_def_ID(BlenderRNA *brna)
//	{
//		StructRNA *srna;
//		FunctionRNA *func;
//		PropertyRNA *prop, *parm;
//
//		srna= RNA_def_struct(brna, "ID", NULL);
//		RNA_def_struct_ui_text(srna, "ID", "Base type for datablocks, defining a unique name, linking from other libraries and garbage collection");
//		RNA_def_struct_flag(srna, STRUCT_ID|STRUCT_ID_REFCOUNT);
//		RNA_def_struct_refine_func(srna, "rna_ID_refine");
//		RNA_def_struct_idprops_func(srna, "rna_ID_idprops");
//
//		prop= RNA_def_property(srna, "name", PROP_STRING, PROP_NONE);
//		RNA_def_property_ui_text(prop, "Name", "Unique datablock ID name");
//		RNA_def_property_string_funcs(prop, "rna_ID_name_get", "rna_ID_name_length", "rna_ID_name_set");
//		RNA_def_property_string_maxlength(prop, sizeof(((ID*)NULL)->name)-2);
//		RNA_def_property_editable_func(prop, "rna_ID_name_editable");
//		RNA_def_property_update(prop, NC_ID|NA_RENAME, NULL);
//		RNA_def_struct_name_property(srna, prop);
//
//		prop= RNA_def_property(srna, "users", PROP_INT, PROP_UNSIGNED);
//		RNA_def_property_int_sdna(prop, NULL, "us");
//		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//		RNA_def_property_ui_text(prop, "Users", "Number of times this datablock is referenced");
//
//		prop= RNA_def_property(srna, "use_fake_user", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, NULL, "flag", LIB_FAKEUSER);
//		RNA_def_property_ui_text(prop, "Fake User", "Saves this datablock even if it has no users");
//		RNA_def_property_boolean_funcs(prop, NULL, "rna_ID_fake_user_set");
//
//		prop= RNA_def_property(srna, "tag", PROP_BOOLEAN, PROP_NONE);
//		RNA_def_property_boolean_sdna(prop, NULL, "flag", LIB_DOIT);
//		RNA_def_property_flag(prop, PROP_LIB_EXCEPTION);
//		RNA_def_property_ui_text(prop, "Tag", "Tools can use this to tag data, (initial state is undefined)");
//
//		prop= RNA_def_property(srna, "library", PROP_POINTER, PROP_NONE);
//		RNA_def_property_pointer_sdna(prop, NULL, "lib");
//		RNA_def_property_clear_flag(prop, PROP_EDITABLE);
//		RNA_def_property_ui_text(prop, "Library", "Library file the datablock is linked from");
//
//		/* functions */
//		func= RNA_def_function(srna, "copy", "rna_ID_copy");
//		RNA_def_function_ui_description(func, "Create a copy of this datablock (not supported for all datablocks).");
//		parm= RNA_def_pointer(func, "id", "ID", "", "New copy of the ID.");
//		RNA_def_function_return(func, parm);
//
//		func= RNA_def_function(srna, "user_clear", "rna_ID_user_clear");
//		RNA_def_function_ui_description(func, "Clears the user count of a datablock so its not saved, on reload the data will be removed.");
//
//		func= RNA_def_function(srna, "animation_data_create", "BKE_id_add_animdata");
//		RNA_def_function_ui_description(func, "Create animation data to this ID, note that not all ID types support this.");
//		parm= RNA_def_pointer(func, "anim_data", "AnimData", "", "New animation data or NULL.");
//		RNA_def_function_return(func, parm);
//
//		func= RNA_def_function(srna, "animation_data_clear", "BKE_free_animdata");
//		RNA_def_function_ui_description(func, "Clear animation on this this ID.");
//
//	}
//
//	static void rna_def_library(BlenderRNA *brna)
//	{
//		StructRNA *srna;
//		PropertyRNA *prop;
//
//		srna= RNA_def_struct(brna, "Library", "ID");
//		RNA_def_struct_ui_text(srna, "Library", "External .blend file from which data is linked");
//		RNA_def_struct_ui_icon(srna, ICON_LIBRARY_DATA_DIRECT);
//
//		prop= RNA_def_property(srna, "filepath", PROP_STRING, PROP_FILEPATH);
//		RNA_def_property_string_sdna(prop, NULL, "name");
//		RNA_def_property_ui_text(prop, "File Path", "Path to the library .blend file");
//		/* TODO - lib->filename isnt updated, however the outliner also skips this, probably only needed on read. */
//		
//		prop= RNA_def_property(srna, "parent", PROP_POINTER, PROP_NONE);
//		RNA_def_property_struct_type(prop, "Library");
//		RNA_def_property_ui_text(prop, "Parent", "");	
//	}
	
	public static RNAProcess RNA_def_ID = new RNAProcess() {
		public void run(BlenderRNA brna)
//	void RNA_def_ID(BlenderRNA *brna)
	{
		StructRNA srna;

		/* built-in unknown type */
		srna= RNA_def_struct(brna, "UnknownType", null);
		RNA_def_struct_ui_text(srna, "Unknown Type", "Stub RNA type used for pointers to unknown or internal data");

		/* built-in any type */
		srna= RNA_def_struct(brna, "AnyType", null);
		RNA_def_struct_ui_text(srna, "Any Type", "RNA type used for pointers to any possible data");

//		rna_def_ID(brna);
		rna_def_ID_properties(brna);
//		rna_def_ID_materials(brna);
//		rna_def_library(brna);
	}};

//	#endif

}

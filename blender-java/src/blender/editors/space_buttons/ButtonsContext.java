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
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_buttons;

//#include <stdlib.h>

import blender.blenkernel.Pointer;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenkernel.bContext;
import blender.blenkernel.bContext.bContextDataCallback;
import blender.blenkernel.bContext.bContextDataResult;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UI.uiBlock;
import blender.editors.uinterface.UI.uiBut;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.SpaceButs;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RnaAccess;
import blender.makesrna.rna_internal_types.StructRNA;

//#include <string.h>
//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_armature_types.h"
//#include "DNA_brush_types.h"
//#include "DNA_lamp_types.h"
//#include "DNA_material_types.h"
//#include "DNA_modifier_types.h"
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_space_types.h"
//#include "DNA_particle_types.h"
//#include "DNA_texture_types.h"
//#include "DNA_world_types.h"
//
//#include "BLI_listbase.h"
//
//#include "BKE_context.h"
//#include "BKE_global.h"
//#include "BKE_material.h"
//#include "BKE_modifier.h"
//#include "BKE_particle.h"
//#include "BKE_screen.h"
//#include "BKE_utildefines.h"
//#include "BKE_world.h"
//
//#include "RNA_access.h"
//
//#include "ED_armature.h"
//#include "ED_screen.h"
//
//#include "UI_interface.h"
//#include "UI_resources.h"
//
//#include "buttons_intern.h"	// own include
//
public class ButtonsContext {

public static class ButsContextPath {
	public PointerRNA[] ptr = new PointerRNA[8];
	public int len;
	public int flag;
        public ButsContextPath() {
            initptr();
        }
        public void clear() {
            ptr = new PointerRNA[8];
            initptr();
            len = 0;
            flag = 0;
        }
        private void initptr() {
            for (int i=0; i<ptr.length; i++) {
                ptr[i] = new PointerRNA();
            }
        }
};

static boolean set_pointer_type(ButsContextPath path, bContextDataResult result, StructRNA type)
{
	PointerRNA ptr;
	int a;

	for(a=0; a<path.len; a++) {
		ptr= path.ptr[a];

		if(RnaAccess.RNA_struct_is_a(ptr.type, type)) {
			bContext.CTX_data_pointer_set(result, (ID)ptr.id.data, ptr.type, ptr.data);
			return true;
		}
	}

	return false;
}

//static PointerRNA *get_pointer_type(ButsContextPath *path, StructRNA *type)
//{
//	PointerRNA *ptr;
//	int a;
//
//	for(a=0; a<path.len; a++) {
//		ptr= &path.ptr[a];
//
//		if(RNA_struct_is_a(ptr.type, type))
//			return ptr;
//	}
//
//	return NULL;
//}

/************************* Creating the Path ************************/

static boolean buttons_context_path_scene(ButsContextPath path)
{
//	PointerRNA ptr= path.ptr[path.len-1];

	/* this one just verifies */
//	return RNA_struct_is_a(ptr.type, &RNA_Scene);
    // TMP
    return true;
}

static boolean buttons_context_path_world(ButsContextPath path)
{
//	Scene scene;
//	PointerRNA ptr= path.ptr[path.len-1];
//
//	/* if we already have a (pinned) world, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_World)) {
//		return true;
//	}
//	/* if we have a scene, use the scene's world */
//	else if(buttons_context_path_scene(path)) {
//		scene= path.ptr[path.len-1].data;
//
//		RNA_id_pointer_create(&scene.world.id, &path.ptr[path.len]);
//		path.len++;
//
//		return 1;
//	}
//
//	/* no path to a world possible */
//	return 0;

    // TMP
    return true;
}


static boolean buttons_context_path_object(ButsContextPath path)
{
//	Scene *scene;
//	Object *ob;
//	PointerRNA *ptr= &path.ptr[path.len-1];
//
//	/* if we already have a (pinned) object, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_Object)) {
//		return 1;
//	}
//	/* if we have a scene, use the scene's active object */
//	else if(buttons_context_path_scene(path)) {
//		scene= path.ptr[path.len-1].data;
//		ob= (scene.basact)? scene.basact.object: NULL;
//
//		if(ob) {
//			RNA_id_pointer_create(&ob.id, &path.ptr[path.len]);
//			path.len++;
//
//			return 1;
//		}
//	}
//
//	/* no path to a object possible */
//	return 0;

    // TMP
    return true;
}

static boolean buttons_context_path_data(ButsContextPath path, int type)
{
//	Object *ob;
//	PointerRNA *ptr= &path.ptr[path.len-1];
//
//	/* if we already have a data, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_Mesh) && (type == -1 || type == OB_MESH)) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_Curve) && (type == -1 || ELEM3(type, OB_CURVE, OB_SURF, OB_FONT))) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_Armature) && (type == -1 || type == OB_ARMATURE)) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_MetaBall) && (type == -1 || type == OB_MBALL)) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_Lattice) && (type == -1 || type == OB_LATTICE)) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_Camera) && (type == -1 || type == OB_CAMERA)) return 1;
//	else if(RNA_struct_is_a(ptr.type, &RNA_Lamp) && (type == -1 || type == OB_LAMP)) return 1;
//	/* try to get an object in the path, no pinning supported here */
//	else if(buttons_context_path_object(path)) {
//		ob= path.ptr[path.len-1].data;
//
//		if(ob && (type == -1 || type == ob.type)) {
//			RNA_id_pointer_create(ob.data, &path.ptr[path.len]);
//			path.len++;
//
//			return 1;
//		}
//	}
//
//	/* no path to data possible */
//	return 0;

    // TMP
    return true;
}

static boolean buttons_context_path_modifier(ButsContextPath path)
{
//	Object *ob;
//
//	if(buttons_context_path_object(path)) {
//		ob= path.ptr[path.len-1].data;
//
//		if(ob && ELEM4(ob.type, OB_MESH, OB_CURVE, OB_FONT, OB_SURF))
//			return 1;
//	}
//
//	return 0;

    // TMP
    return true;
}

static boolean buttons_context_path_material(ButsContextPath path)
{
//	Object *ob;
//	PointerRNA *ptr= &path.ptr[path.len-1];
//	Material *ma;
//
//	/* if we already have a (pinned) material, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_Material)) {
//		return 1;
//	}
//	/* if we have an object, use the object material slot */
//	else if(buttons_context_path_object(path)) {
//		ob= path.ptr[path.len-1].data;
//
//		if(ob && ob.type && (ob.type<OB_LAMP)) {
//			ma= give_current_material(ob, ob.actcol);
//			RNA_id_pointer_create(&ma.id, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//
//	/* no path to a material possible */
//	return 0;

    // TMP
    return true;
}

//static Bone *find_active_bone(Bone *bone)
//{
//	Bone *active;
//
//	for(; bone; bone=bone.next) {
//		if(bone.flag & BONE_ACTIVE)
//			return bone;
//
//		active= find_active_bone(bone.childbase.first);
//		if(active)
//			return active;
//	}
//
//	return NULL;
//}

static boolean buttons_context_path_bone(ButsContextPath path)
{
//	bArmature *arm;
//	Bone *bone;
//	EditBone *edbo;
//
//	/* if we have an armature, get the active bone */
//	if(buttons_context_path_data(path, OB_ARMATURE)) {
//		arm= path.ptr[path.len-1].data;
//
//		if(arm.edbo) {
//			for(edbo=arm.edbo.first; edbo; edbo=edbo.next) {
//				if(edbo.flag & BONE_ACTIVE) {
//					RNA_pointer_create(&arm.id, &RNA_EditBone, edbo, &path.ptr[path.len]);
//					path.len++;
//					return 1;
//				}
//			}
//		}
//		else {
//			bone= find_active_bone(arm.bonebase.first);
//
//			if(bone) {
//				RNA_pointer_create(&arm.id, &RNA_Bone, bone, &path.ptr[path.len]);
//				path.len++;
//				return 1;
//			}
//		}
//	}

	/* no path to a bone possible */
	return false;
}

static boolean buttons_context_path_pose_bone(ButsContextPath path)
{
//	PointerRNA *ptr= &path->ptr[path->len-1];
//
//	/* if we already have a (pinned) PoseBone, we're done */
//	if(RNA_struct_is_a(ptr->type, &RNA_PoseBone)) {
//		return 1;
//	}
//
//	/* if we have an armature, get the active bone */
//	if(buttons_context_path_object(path)) {
//		Object *ob= path->ptr[path->len-1].data;
//		bArmature *arm= ob->data; /* path->ptr[path->len-1].data - works too */
//
//		if(ob->type != OB_ARMATURE || arm->edbo) {
//			return 0;
//		}
//		else {
//			if(arm->act_bone) {
//				bPoseChannel *pchan= get_pose_channel(ob->pose, arm->act_bone->name);
//				if(pchan) {
//					RNA_pointer_create(&ob->id, &RNA_PoseBone, pchan, &path->ptr[path->len]);
//					path->len++;
//					return 1;
//				}
//			}
//		}
//	}
//
//	/* no path to a bone possible */
//	return 0;
	
	// TMP
    return true;
}

static boolean buttons_context_path_particle(ButsContextPath path)
{
//	Object *ob;
//	ParticleSystem *psys;
//
//	/* if we have an object, get the active particle system */
//	if(buttons_context_path_object(path)) {
//		ob= path.ptr[path.len-1].data;
//
//		if(ob && ob.type == OB_MESH) {
//			psys= psys_get_current(ob);
//
//			RNA_pointer_create(&ob.id, &RNA_ParticleSystem, psys, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//
//	/* no path to a particle system possible */
//	return 0;

    // TMP
    return true;
}

//static int buttons_context_path_brush(ButsContextPath *path)
//{
//	Scene *scene;
//	ToolSettings *ts;
//	Brush *br= NULL;
//	PointerRNA *ptr= &path.ptr[path.len-1];
//
//	/* if we already have a (pinned) brush, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_Brush)) {
//		return 1;
//	}
//	/* if we have a scene, use the toolsettings brushes */
//	else if(buttons_context_path_scene(path)) {
//		scene= path.ptr[path.len-1].data;
//		ts= scene.toolsettings;
//
//		if(G.f & G_SCULPTMODE)
//			br= ts.sculpt.brush;
//		else if(G.f & G_VERTEXPAINT)
//			br= ts.vpaint.brush;
//		else if(G.f & G_WEIGHTPAINT)
//			br= ts.wpaint.brush;
//		else if(G.f & G_TEXTUREPAINT)
//			br= ts.imapaint.brush;
//
//		if(br) {
//			RNA_id_pointer_create(&br.id, &path.ptr[path.len]);
//			path.len++;
//
//			return 1;
//		}
//	}
//
//	/* no path to a world possible */
//	return 0;
//}

static boolean buttons_context_path_texture(ButsContextPath path)
{
//	Material *ma;
//	Lamp *la;
//	Brush *br;
//	World *wo;
//	MTex *mtex;
//	Tex *tex;
//	PointerRNA *ptr= &path.ptr[path.len-1];
//
//	/* if we already have a (pinned) texture, we're done */
//	if(RNA_struct_is_a(ptr.type, &RNA_Texture)) {
//		return 1;
//	}
//	/* try brush */
//	else if((path.flag & SB_BRUSH_TEX) && buttons_context_path_brush(path)) {
//		br= path.ptr[path.len-1].data;
//
//		if(br) {
//			mtex= br.mtex[(int)br.texact];
//			tex= (mtex)? mtex.tex: NULL;
//
//			RNA_id_pointer_create(&tex.id, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//	/* try world */
//	else if((path.flag & SB_WORLD_TEX) && buttons_context_path_world(path)) {
//		wo= path.ptr[path.len-1].data;
//
//		if(wo) {
//			mtex= wo.mtex[(int)wo.texact];
//			tex= (mtex)? mtex.tex: NULL;
//
//			RNA_id_pointer_create(&tex.id, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//	/* try material */
//	else if(buttons_context_path_material(path)) {
//		ma= path.ptr[path.len-1].data;
//
//		if(ma) {
//			mtex= ma.mtex[(int)ma.texact];
//			tex= (mtex)? mtex.tex: NULL;
//
//			RNA_id_pointer_create(&tex.id, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//	/* try lamp */
//	else if(buttons_context_path_data(path, OB_LAMP)) {
//		la= path.ptr[path.len-1].data;
//
//		if(la) {
//			mtex= la.mtex[(int)la.texact];
//			tex= (mtex)? mtex.tex: NULL;
//
//			RNA_id_pointer_create(&tex.id, &path.ptr[path.len]);
//			path.len++;
//			return 1;
//		}
//	}
//	/* TODO: material nodes */
//
//	/* no path to a texture possible */
//	return 0;

    // TMP
    return true;
}


static boolean buttons_context_path(bContext C, ButsContextPath path, int mainb, int flag)
{
	SpaceButs sbuts= bContext.CTX_wm_space_buts(C);
	ID id;
	boolean found;

//	memset(path, 0, sizeof(*path));
        path.clear();
	path.flag= flag;

	/* if some ID datablock is pinned, set the root pointer */
	if(sbuts.pinid!=null) {
		id= (ID)sbuts.pinid;

		RnaAccess.RNA_id_pointer_create(id, path.ptr[0]);
		path.len++;
	}

	/* no pinned root, use scene as root */
	if(path.len == 0) {
		id= (ID)bContext.CTX_data_scene(C);
		RnaAccess.RNA_id_pointer_create(id, path.ptr[0]);
		path.len++;
	}

	/* now for each buttons context type, we try to construct a path,
	 * tracing back recursively */
	switch(mainb) {
		case SpaceTypes.BCONTEXT_SCENE:
		case SpaceTypes.BCONTEXT_RENDER:
			found= buttons_context_path_scene(path);
			break;
		case SpaceTypes.BCONTEXT_WORLD:
			found= buttons_context_path_world(path);
			break;
		case SpaceTypes.BCONTEXT_OBJECT:
		case SpaceTypes.BCONTEXT_PHYSICS:
		case SpaceTypes.BCONTEXT_CONSTRAINT:
			found= buttons_context_path_object(path);
			break;
		case SpaceTypes.BCONTEXT_MODIFIER:
			found= buttons_context_path_modifier(path);
			break;
		case SpaceTypes.BCONTEXT_DATA:
			found= buttons_context_path_data(path, -1);
			break;
		case SpaceTypes.BCONTEXT_PARTICLE:
			found= buttons_context_path_particle(path);
			break;
		case SpaceTypes.BCONTEXT_MATERIAL:
			found= buttons_context_path_material(path);
			break;
		case SpaceTypes.BCONTEXT_TEXTURE:
			found= buttons_context_path_texture(path);
			break;
		case SpaceTypes.BCONTEXT_BONE:
			found= buttons_context_path_bone(path);
			if(!found)
				found= buttons_context_path_data(path, ObjectTypes.OB_ARMATURE);
			break;
		case SpaceTypes.BCONTEXT_BONE_CONSTRAINT:
			found= buttons_context_path_pose_bone(path);
			break;
		default:
			found= false;
			break;
	}

	return found;
}

public static void buttons_context_compute(bContext C, SpaceButs sbuts)
{
	ButsContextPath path;
	PointerRNA ptr;
	int a, pflag, flag= 0;

	if(sbuts.path==null)
		sbuts.path= new ButsContextPath();
	
	path= (ButsContextPath)sbuts.path;
	pflag= (sbuts.flag & (SpaceTypes.SB_WORLD_TEX|SpaceTypes.SB_BRUSH_TEX));

	/* for each context, see if we can compute a valid path to it, if
	 * this is the case, we know we have to display the button */
	for(a=0; a<SpaceTypes.BCONTEXT_TOT; a++) {
		if(buttons_context_path(C, path, a, pflag)) {
			flag |= (1<<a);

			/* setting icon for data context */
			if(a == SpaceTypes.BCONTEXT_DATA) {
				ptr= path.ptr[path.len-1];

				if(ptr.type!=null)
					sbuts.dataicon= RnaAccess.RNA_struct_ui_icon(ptr.type);
				else
					sbuts.dataicon= BIFIconID.ICON_EMPTY_DATA.ordinal();
			}
		}
	}

	/* always try to use the tab that was explicitly
	 * set to the user, so that once that context comes
	 * back, the tab is activated again */
	sbuts.mainb= sbuts.mainbuser;

	/* in case something becomes invalid, change */
	if((flag & (1 << sbuts.mainb)) == 0) {
		if((flag & SpaceTypes.BCONTEXT_OBJECT)!=0) {
			sbuts.mainb= SpaceTypes.BCONTEXT_OBJECT;
		}
		else {
			for(a=0; a<SpaceTypes.BCONTEXT_TOT; a++) {
				if((flag & (1 << a))!=0) {
					sbuts.mainb= (short)a;
					break;
				}
			}
		}
	}

	buttons_context_path(C, path, sbuts.mainb, pflag);

	if((flag & (1 << sbuts.mainb))==0) {
		if((flag & (1 << SpaceTypes.BCONTEXT_OBJECT))!=0)
			sbuts.mainb= SpaceTypes.BCONTEXT_OBJECT;
		else
			sbuts.mainb= SpaceTypes.BCONTEXT_SCENE;
	}

	sbuts.pathflag= flag;
}

/************************* Context Callback ************************/

static String[] dir = {
			"world", "object", "mesh", "armature", "lattice", "curve",
			"meta_ball", "lamp", "camera", "material", "material_slot",
			"texture", "texture_slot", "bone", "edit_bone", "particle_system",
			"cloth", "soft_body", "fluid", "collision", "brush", null
};

public static bContextDataCallback buttons_context = new bContextDataCallback() {
public boolean run(bContext C, byte[] member, bContextDataResult result)
{
	SpaceButs sbuts= bContext.CTX_wm_space_buts(C);
	ButsContextPath path= sbuts!=null?(ButsContextPath)sbuts.path:null;

	if(path==null)
		return false;

	/* here we handle context, getting data from precomputed path */
	if(bContext.CTX_data_dir(member)) {
//		static const char *dir[] = {
//			"world", "object", "mesh", "armature", "lattice", "curve",
//			"meta_ball", "lamp", "camera", "material", "material_slot",
//			"texture", "texture_slot", "bone", "edit_bone", "particle_system",
//			"cloth", "soft_body", "fluid", "collision", "brush", NULL};

		bContext.CTX_data_dir_set(result, dir);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "world")) {
//		set_pointer_type(path, result, &RNA_World);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "object")) {
//		set_pointer_type(path, result, &RNA_Object);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "mesh")) {
//		set_pointer_type(path, result, &RNA_Mesh);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "armature")) {
//		set_pointer_type(path, result, &RNA_Armature);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "lattice")) {
//		set_pointer_type(path, result, &RNA_Lattice);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "curve")) {
//		set_pointer_type(path, result, &RNA_Curve);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "meta_ball")) {
//		set_pointer_type(path, result, &RNA_MetaBall);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "lamp")) {
//		set_pointer_type(path, result, &RNA_Lamp);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "camera")) {
//		set_pointer_type(path, result, &RNA_Camera);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "material")) {
//		set_pointer_type(path, result, &RNA_Material);
		return true;
	}
	else if(bContext.CTX_data_equals(member, "texture")) {
//		set_pointer_type(path, result, &RNA_Texture);
		return true;
	}
//	else if(bContext.CTX_data_equals(member, "material_slot")) {
//		PointerRNA ptr= get_pointer_type(path, &RNA_Object);
//
//		if(ptr!=null) {
//			bObject ob= ptr.data;
//
//			if(ob!=null && ob.type!=null && (ob.type<ObjectTypes.OB_LAMP) && ob.totcol)
//				bContext.CTX_data_pointer_set(result, &ob.id, &RNA_MaterialSlot, ob.mat+ob.actcol-1);
//		}
//
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "texture_slot")) {
//		PointerRNA *ptr;
//
//		if((ptr=get_pointer_type(path, &RNA_Material))) {
//			Material *ma= ptr.data;
//
//			if(ma)
//				bContext.CTX_data_pointer_set(result, &ma.id, &RNA_MaterialTextureSlot, ma.mtex[(int)ma.texact]);
//		}
//		else if((ptr=get_pointer_type(path, &RNA_Lamp))) {
//			Lamp *la= ptr.data;
//
//			if(la)
//				bContext.CTX_data_pointer_set(result, &la.id, &RNA_LampTextureSlot, la.mtex[(int)la.texact]);
//		}
//		else if((ptr=get_pointer_type(path, &RNA_World))) {
//			World *wo= ptr.data;
//
//			if(wo)
//				bContext.CTX_data_pointer_set(result, &wo.id, &RNA_WorldTextureSlot, wo.mtex[(int)wo.texact]);
//		}
//		else if((ptr=get_pointer_type(path, &RNA_Brush))) { /* how to get this into context? */
//			Brush *br= ptr.data;
//
//			if(br)
//				bContext.CTX_data_pointer_set(result, &br.id, &RNA_TextureSlot, br.mtex[(int)br.texact]);
//		}
//
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "bone")) {
//		set_pointer_type(path, result, &RNA_Bone);
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "edit_bone")) {
//		set_pointer_type(path, result, &RNA_EditBone);
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "particle_system")) {
//		set_pointer_type(path, result, &RNA_ParticleSystem);
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "cloth")) {
//		PointerRNA *ptr= get_pointer_type(path, &RNA_Object);
//
//		if(ptr && ptr.data) {
//			Object *ob= ptr.data;
//			ModifierData *md= modifiers_findByType(ob, eModifierType_Cloth);
//			bContext.CTX_data_pointer_set(result, &ob.id, &RNA_ClothModifier, md);
//			return 1;
//		}
//	}
//	else if(bContext.CTX_data_equals(member, "soft_body")) {
//		PointerRNA *ptr= get_pointer_type(path, &RNA_Object);
//
//		if(ptr && ptr.data) {
//			Object *ob= ptr.data;
//			ModifierData *md= modifiers_findByType(ob, eModifierType_Softbody);
//			bContext.CTX_data_pointer_set(result, &ob.id, &RNA_SoftBodyModifier, md);
//			return 1;
//		}
//	}
//	else if(bContext.CTX_data_equals(member, "fluid")) {
//		PointerRNA *ptr= get_pointer_type(path, &RNA_Object);
//
//		if(ptr && ptr.data) {
//			Object *ob= ptr.data;
//			ModifierData *md= modifiers_findByType(ob, eModifierType_Fluidsim);
//			bContext.CTX_data_pointer_set(result, &ob.id, &RNA_FluidSimulationModifier, md);
//			return 1;
//		}
//	}
//	else if(bContext.CTX_data_equals(member, "collision")) {
//		PointerRNA *ptr= get_pointer_type(path, &RNA_Object);
//
//		if(ptr && ptr.data) {
//			Object *ob= ptr.data;
//			ModifierData *md= modifiers_findByType(ob, eModifierType_Collision);
//			bContext.CTX_data_pointer_set(result, &ob.id, &RNA_CollisionModifier, md);
//			return 1;
//		}
//	}
//	else if(bContext.CTX_data_equals(member, "brush")) {
//		set_pointer_type(path, result, &RNA_Brush);
//		return 1;
//	}

	return false;
}};

///************************* Drawing the Path ************************/
//
//static void pin_cb(bContext *C, void *arg1, void *arg2)
//{
//	SpaceButs *sbuts= CTX_wm_space_buts(C);
//	ButsContextPath *path= sbuts.path;
//	PointerRNA *ptr;
//	int a;
//
//	if(sbuts.flag & SB_PIN_CONTEXT) {
//		if(path.len) {
//			for(a=path.len-1; a>=0; a--) {
//				ptr= &path.ptr[a];
//
//				if(ptr.id.data) {
//					sbuts.pinid= ptr.id.data;
//					break;
//				}
//			}
//		}
//	}
//	else
//		sbuts.pinid= NULL;
//
//	ED_area_tag_redraw(CTX_wm_area(C));
//}

public static void buttons_context_draw(bContext C, uiLayout layout)
{
	final SpaceButs sbuts= bContext.CTX_wm_space_buts(C);
	ButsContextPath path= (ButsContextPath)sbuts.path;
	uiLayout row;
	uiBlock block;
	uiBut but;
	PointerRNA ptr;
//	char namebuf[128], *name;
	int a;
    int icon;

	if(path==null)
		return;

	row= UILayout.uiLayoutRow(layout, 1);
	UILayout.uiLayoutSetAlignment(row, UI.UI_LAYOUT_ALIGN_LEFT);

	block= UILayout.uiLayoutGetBlock(row);
	UI.uiBlockSetEmboss(block, UI.UI_EMBOSSN);
        Pointer<Byte> sbuts_flag = new Pointer<Byte>() {

            public Byte get() {
                return sbuts.flag;
            }

            public void set(Byte obj) {
                sbuts.flag = obj;
            }

        };
	but= UI.uiDefIconButBitC(block, UI.ICONTOG, SpaceTypes.SB_PIN_CONTEXT, 0, BIFIconID.ICON_UNPINNED, 0, 0, UI.UI_UNIT_X, UI.UI_UNIT_Y, sbuts_flag, 0, 0, 0, 0, "Follow context or keep fixed datablock displayed.");
//	uiButSetFunc(but, pin_cb, NULL, NULL);

	for(a=0; a<path.len; a++) {
		ptr= path.ptr[a];

		if(a != 0)
//			UI.uiDefIconBut(block, UI.LABEL, 0, BIFIconID.VICON_SMALL_TRI_RIGHT, 0, 0, 10, UI.UI_UNIT_Y, null, 0, 0, 0, 0, "");
			UILayout.uiItemL(row, "", BIFIconID.VICON_SMALL_TRI_RIGHT.ordinal());

		if(ptr.data!=null) {
			icon= RnaAccess.RNA_struct_ui_icon(ptr.type);
//			name= RNA_struct_name_get_alloc(ptr, namebuf, sizeof(namebuf));

//			if(name!=null) {
//				if(sbuts.mainb != SpaceTypes.BCONTEXT_SCENE && ptr.type == &RNA_Scene)
//					UILayout.uiItemL(row, "", icon); /* save some space */
//				else
//					UILayout.uiItemL(row, name, icon);

//				if(name != namebuf)
//					MEM_freeN(name);
//			}
//			else
				UILayout.uiItemL(row, "", icon);
		}
	}
}

public static PanelType.Draw buttons_panel_context = new PanelType.Draw() {
public void run(bContext C, Panel pa)
{
	buttons_context_draw(C, (uiLayout)pa.layout);
}};

public static void buttons_context_register(ARegionType art)
{
	PanelType pt;

	pt= new PanelType();
	StringUtil.strcpy(pt.idname,0, StringUtil.toCString("BUTTONS_PT_context"),0);
	StringUtil.strcpy(pt.label,0, StringUtil.toCString("Context"),0);
	pt.draw= buttons_panel_context;
	pt.flag= ScreenTypes.PNL_NO_HEADER;
	ListBaseUtil.BLI_addtail(art.paneltypes, pt);
}

}
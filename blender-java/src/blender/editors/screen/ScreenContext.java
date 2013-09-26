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
 * The Original Code is Copyright (C) 2008 Blender Foundation.
 * All rights reserved.
 *
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.screen;

//#include <stdlib.h>

//import blender.blenkernel.Blender;
//import blender.blenkernel.ObjectUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.bContext.bContextDataCallback;
import blender.blenkernel.bContext.bContextDataResult;
//import blender.makesdna.ObjectTypes;
//import blender.makesdna.sdna.Base;
//import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bScreen;
//import blender.makesrna.RnaAccess;

//#include <string.h>
//
//#include "DNA_object_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//
//#include "BKE_context.h"
//#include "BKE_utildefines.h"
//#include "BKE_global.h"
//
//#include "RNA_access.h"
//
//#include "ED_object.h"

public class ScreenContext {

static String[] dir = {
			"scene", "selected_objects", "selected_bases",
			"selected_editable_objects", "selected_editable_bases",
			"active_base", "active_object", "edit_object",
			"sculpt_object", "vertex_paint_object", "weight_paint_object",
			"texture_paint_object", "brush", "particle_edit_object", null
};

public static bContextDataCallback ed_screen_context = new bContextDataCallback() {
public boolean run(bContext C, byte[] member, bContextDataResult result)
//int ed_screen_context(const bContext *C, const char *member, bContextDataResult *result)
{
	bScreen sc= bContext.CTX_wm_screen(C);
//	Scene scene= sc.scene;
//	Base base;

//	if(bContext.CTX_data_dir(member)) {
////		static const char *dir[] = {
////			"scene", "selected_objects", "selected_bases",
////			"selected_editable_objects", "selected_editable_bases"
////			"active_base", "active_object", "edit_object",
////			"sculpt_object", "vertex_paint_object", "weight_paint_object",
////			"texture_paint_object", "brush", "particle_edit_object", NULL};
//
//		bContext.CTX_data_dir_set(result, dir);
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "scene")) {
//		bContext.CTX_data_id_pointer_set(result, scene.id);
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "selected_objects") || bContext.CTX_data_equals(member, "selected_bases")) {
//		boolean selected_objects= bContext.CTX_data_equals(member, "selected_objects");
//
//		for(base=(Base)scene.base.first; base!=null; base=base.next) {
//			if((base.flag & Blender.SELECT)!=0 && (base.lay & scene.lay)!=0) {
//				if(selected_objects)
//					bContext.CTX_data_id_list_add(result, base.object.id);
//				else
//					bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
//			}
//		}
//
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "selected_editable_objects") || bContext.CTX_data_equals(member, "selected_editable_bases")) {
//		boolean selected_editable_objects= bContext.CTX_data_equals(member, "selected_editable_objects");
//
//		for(base=(Base)scene.base.first; base!=null; base=base.next) {
//			if((base.flag & Blender.SELECT)!=0 && (base.lay & scene.lay)!=0) {
//				if((base.object.restrictflag & ObjectTypes.OB_RESTRICT_VIEW)==0) {
//					if(false==ObjectUtil.object_is_libdata(base.object)) {
//						if(selected_editable_objects)
//							bContext.CTX_data_id_list_add(result, base.object.id);
//						else
//							bContext.CTX_data_list_add(result, scene.id, RnaAccess.RNA_UnknownType, base);
//					}
//				}
//			}
//		}
//
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "active_base")) {
//		if(scene.basact!=null)
//			bContext.CTX_data_pointer_set(result, scene.id, RnaAccess.RNA_UnknownType, scene.basact);
//
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "active_object")) {
//		if(scene.basact!=null)
//			bContext.CTX_data_id_pointer_set(result, scene.basact.object.id);
//
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "edit_object")) {
//		/* convenience for now, 1 object per scene in editmode */
//		if(scene.obedit!=null)
//			bContext.CTX_data_id_pointer_set(result, scene.obedit.id);
//		
//		return true;
//	}
//	else if(bContext.CTX_data_equals(member, "sculpt_object")) {
//		if(G.f & G_SCULPTMODE && scene.basact)
//			bContext.CTX_data_id_pointer_set(result, &scene.basact.object.id);
//
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "vertex_paint_object")) {
//		if(G.f & G_VERTEXPAINT && scene.basact)
//			bContext.CTX_data_id_pointer_set(result, &scene.basact.object.id);
//
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "weight_paint_object")) {
//		if(G.f & G_WEIGHTPAINT && scene.basact)
//			bContext.CTX_data_id_pointer_set(result, &scene.basact.object.id);
//
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "texture_paint_object")) {
//		if(G.f & G_TEXTUREPAINT && scene.basact)
//			bContext.CTX_data_id_pointer_set(result, &scene.basact.object.id);
//
//		return 1;
//	}
//	else if(bContext.CTX_data_equals(member, "particle_edit_object")) {
//		if(G.f & G_PARTICLEEDIT && scene.basact)
//			bContext.CTX_data_id_pointer_set(result, &scene.basact.object.id);
//
//		return 1;
//	}

	return false;
}};

}

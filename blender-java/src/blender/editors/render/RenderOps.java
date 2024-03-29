package blender.editors.render;

import blender.windowmanager.WmOperators;

public class RenderOps {
//	/**
//	 * $Id: render_ops.c 27821 2010-03-29 05:37:34Z broken $
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
//	 * The Original Code is Copyright (C) 2009 Blender Foundation.
//	 * All rights reserved.
//	 *
//	 * Contributor(s): Blender Foundation
//	 *
//	 * ***** END GPL LICENSE BLOCK *****
//	 */
//
//	#include <stdlib.h>
//
//
//	#include "WM_api.h"
//
//	#include "render_intern.h" // own include
//
//	#if (defined(WITH_QUICKTIME) && !defined(USE_QTKIT))
//	#include "quicktime_export.h"
//	#endif

	/***************************** render ***********************************/

	public static void ED_operatortypes_render()
	{
//		WM_operatortype_append(OBJECT_OT_material_slot_add);
//		WM_operatortype_append(OBJECT_OT_material_slot_remove);
//		WM_operatortype_append(OBJECT_OT_material_slot_assign);
//		WM_operatortype_append(OBJECT_OT_material_slot_select);
//		WM_operatortype_append(OBJECT_OT_material_slot_deselect);
//		WM_operatortype_append(OBJECT_OT_material_slot_copy);
//
//		WM_operatortype_append(MATERIAL_OT_new);
//		WM_operatortype_append(TEXTURE_OT_new);
//		WM_operatortype_append(WORLD_OT_new);
//		
//		WM_operatortype_append(MATERIAL_OT_copy);
//		WM_operatortype_append(MATERIAL_OT_paste);
//
//		WM_operatortype_append(SCENE_OT_render_layer_add);
//		WM_operatortype_append(SCENE_OT_render_layer_remove);
//
//	#if (defined(WITH_QUICKTIME) && !defined(USE_QTKIT))
//		WM_operatortype_append(SCENE_OT_render_data_set_quicktime_codec);
//	#endif
//		
//		WM_operatortype_append(TEXTURE_OT_slot_copy);
//		WM_operatortype_append(TEXTURE_OT_slot_paste);
//		WM_operatortype_append(TEXTURE_OT_slot_move);
//		WM_operatortype_append(TEXTURE_OT_envmap_save);
//		WM_operatortype_append(TEXTURE_OT_envmap_clear);
//		WM_operatortype_append(TEXTURE_OT_envmap_clear_all);
//
//		/* render_internal.c */
//		WM_operatortype_append(RENDER_OT_view_show);
//		WM_operatortype_append(RENDER_OT_render);
//		WM_operatortype_append(RENDER_OT_view_cancel);

		/* render_opengl.c */
		WmOperators.WM_operatortype_append(RenderOpengl.RENDER_OT_opengl);
	}

}

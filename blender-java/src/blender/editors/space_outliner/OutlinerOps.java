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
 * Contributor(s): Blender Foundation
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.space_outliner;

import blender.blenkernel.ScreenUtil.SpaceType;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesrna.RnaAccess;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmTypes;

public class OutlinerOps {

/* ************************** registration **********************************/


public static SpaceType.OperatorTypes outliner_operatortypes = new SpaceType.OperatorTypes() {
public void run()
{
	WmOperators.WM_operatortype_append(Outliner.OUTLINER_OT_item_activate);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_item_openclose);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_item_rename);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_operation);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_object_operation);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_group_operation);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_id_operation);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_data_operation);
//
//	WmOperators.WM_operatortype_append(OUTLINER_OT_show_one_level);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_show_active);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_show_hierarchy);
//	
//	WmOperators.WM_operatortype_append(OUTLINER_OT_selected_toggle);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_expanded_toggle);
//	
//	WmOperators.WM_operatortype_append(OUTLINER_OT_renderability_toggle);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_selectability_toggle);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_visibility_toggle);
//	
//	WmOperators.WM_operatortype_append(OUTLINER_OT_keyingset_add_selected);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_keyingset_remove_selected);
//	
//	WmOperators.WM_operatortype_append(OUTLINER_OT_drivers_add_selected);
//	WmOperators.WM_operatortype_append(OUTLINER_OT_drivers_delete_selected);
}};

public static SpaceType.KeyMap outliner_keymap = new SpaceType.KeyMap() {
public void run(wmKeyConfig keyconf)
{
	wmKeyMap keymap= WmKeymap.WM_keymap_find(keyconf, "Outliner", SpaceTypes.SPACE_OUTLINER, 0);

	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "OUTLINER_OT_item_activate", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, 0, 0).ptr, "extend", false);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "OUTLINER_OT_item_activate", WmEventTypes.LEFTMOUSE, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "extend", true);

//	RNA_boolean_set(WM_keymap_add_item(keymap, "OUTLINER_OT_item_openclose", RETKEY, KM_PRESS, 0, 0)->ptr, "all", 0);
//	RNA_boolean_set(WM_keymap_add_item(keymap, "OUTLINER_OT_item_openclose", RETKEY, KM_PRESS, KM_SHIFT, 0)->ptr, "all", 1);
//
//	WM_keymap_add_item(keymap, "OUTLINER_OT_item_rename", LEFTMOUSE, KM_PRESS, KM_CTRL, 0);
//	WM_keymap_add_item(keymap, "OUTLINER_OT_operation", RIGHTMOUSE, KM_PRESS, 0, 0);
//
//	/* drag & drop */
//	WM_keymap_add_item(keymap, "OUTLINER_OT_drag", MOUSEDRAG, KM_ANY, 0, 0);
//
//	WM_keymap_add_item(keymap, "OUTLINER_OT_show_hierarchy", HOMEKEY, KM_PRESS, 0, 0);
//
//	WM_keymap_add_item(keymap, "OUTLINER_OT_show_active", PERIODKEY, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "OUTLINER_OT_show_active", PADPERIOD, KM_PRESS, 0, 0);
//
//	WM_keymap_add_item(keymap, "OUTLINER_OT_show_one_level", PADPLUSKEY, KM_PRESS, 0, 0); /* open */
//	RNA_boolean_set(WM_keymap_add_item(keymap, "OUTLINER_OT_show_one_level", PADMINUS, KM_PRESS, 0, 0)->ptr, "open", 0); /* close */
//
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_selected_toggle", AKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_expanded_toggle", AKEY, KM_PRESS, KM_SHIFT, 0);
//
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_renderability_toggle", RKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_selectability_toggle", SKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_visibility_toggle", VKEY, KM_PRESS, 0, 0);
//
//
//	/* keying sets - only for databrowse */
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_keyingset_add_selected", KKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_keyingset_remove_selected", KKEY, KM_PRESS, KM_ALT, 0);
//
//	WM_keymap_verify_item(keymap, "ANIM_OT_insert_keyframe", IKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "ANIM_OT_delete_keyframe", IKEY, KM_PRESS, KM_ALT, 0);
//
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_drivers_add", DKEY, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "OUTLINER_OT_drivers_delete", DKEY, KM_PRESS, KM_ALT, 0);
}};

}

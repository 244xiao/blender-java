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
package blender.editors.space_view3d;

import blender.blenkernel.ScreenUtil.SpaceType;
import blender.editors.transform.TransformOps;
import blender.makesdna.ObjectTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmKeyMapItem;
import blender.makesrna.RnaAccess;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmTypes;

public class View3dOps {

/* ************************** registration **********************************/

public static SpaceType.OperatorTypes view3d_operatortypes = new SpaceType.OperatorTypes() {
public void run()
//public static void view3d_operatortypes()
{
//        System.out.println("view3d_operatortypes");
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_viewrotate);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_viewmove);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_zoom);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_view_all);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_viewnumpad);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_view_orbit);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_view_pan);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_view_persportho);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_view_center);
	WmOperators.WM_operatortype_append(View3dSelect.VIEW3D_OT_select);
//	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_select_border);
//	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_drag);
//	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_clip_border);
//	WM_operatortype_append(VIEW3D_OT_select_circle);
//	WM_operatortype_append(VIEW3D_OT_smoothview);
//	WM_operatortype_append(VIEW3D_OT_render_border);
//	WM_operatortype_append(VIEW3D_OT_zoom_border);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_manipulator);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_cursor3d);
//	WM_operatortype_append(VIEW3D_OT_select_lasso);
//	WM_operatortype_append(VIEW3D_OT_setcameratoview);
	WmOperators.WM_operatortype_append(View3dEdit.VIEW3D_OT_drawtype);
//	WM_operatortype_append(VIEW3D_OT_localview);
//	WM_operatortype_append(VIEW3D_OT_game_start);
//	WM_operatortype_append(VIEW3D_OT_layers);

	WmOperators.WM_operatortype_append(View3dButtons.VIEW3D_OT_properties);
	WmOperators.WM_operatortype_append(View3dToolbar.VIEW3D_OT_toolbar);

//	WM_operatortype_append(VIEW3D_OT_snap_selected_to_grid);
//	WM_operatortype_append(VIEW3D_OT_snap_selected_to_cursor);
//	WM_operatortype_append(VIEW3D_OT_snap_selected_to_center);
//	WM_operatortype_append(VIEW3D_OT_snap_cursor_to_grid);
//	WM_operatortype_append(VIEW3D_OT_snap_cursor_to_selected);
//	WM_operatortype_append(VIEW3D_OT_snap_cursor_to_active);
//	WM_operatortype_append(VIEW3D_OT_snap_menu);

	TransformOps.transform_operatortypes();
}};

public static SpaceType.KeyMap view3d_keymap = new SpaceType.KeyMap() {
public void run(wmKeyConfig keyconf)
//public static void view3d_keymap(wmWindowManager wm)
{
//        System.out.println("view3d_keymap");
	wmKeyMap keymap= WmKeymap.WM_keymap_find(keyconf, "View3D Generic", SpaceTypes.SPACE_VIEW3D, 0);
	wmKeyMapItem km;

//	WM_keymap_add_item(keymap, "PAINT_OT_vertex_paint_toggle", VKEY, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "PAINT_OT_weight_paint_toggle", TABKEY, KM_PRESS, KM_CTRL, 0);

	WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_properties", WmEventTypes.NKEY, WmTypes.KM_PRESS, 0, 0);
	WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_toolbar", WmEventTypes.TKEY, WmTypes.KM_PRESS, 0, 0);

	/* only for region 3D window */
	keymap= WmKeymap.WM_keymap_find(keyconf, "View3D", SpaceTypes.SPACE_VIEW3D, 0);

//	/* paint poll checks mode */
//	WM_keymap_verify_item(keymap, "PAINT_OT_vertex_paint", LEFTMOUSE, KM_PRESS, 0, 0);
//	WM_keymap_verify_item(keymap, "PAINT_OT_weight_paint", LEFTMOUSE, KM_PRESS, 0, 0);
//
//	WM_keymap_add_item(keymap, "PAINT_OT_image_paint", LEFTMOUSE, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "PAINT_OT_sample_color", RIGHTMOUSE, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "PAINT_OT_clone_cursor_set", LEFTMOUSE, KM_PRESS, KM_CTRL, 0);
//
//	WM_keymap_add_item(keymap, "SCULPT_OT_brush_stroke", LEFTMOUSE, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "SCULPT_OT_brush_stroke", LEFTMOUSE, KM_PRESS, KM_SHIFT, 0);
//
//	/* sketch poll checks mode */
//	WM_keymap_add_item(keymap, "SKETCH_OT_gesture", ACTIONMOUSE, KM_PRESS, KM_SHIFT, 0);
//	WM_keymap_add_item(keymap, "SKETCH_OT_draw_stroke", ACTIONMOUSE, KM_PRESS, 0, 0);
//	km = WM_keymap_add_item(keymap, "SKETCH_OT_draw_stroke", ACTIONMOUSE, KM_PRESS, KM_CTRL, 0);
//	RNA_boolean_set(km.ptr, "snap", 1);
//	WM_keymap_add_item(keymap, "SKETCH_OT_draw_preview", MOUSEMOVE, KM_ANY, 0, 0);
//	km = WM_keymap_add_item(keymap, "SKETCH_OT_draw_preview", MOUSEMOVE, KM_ANY, KM_CTRL, 0);
//	RNA_boolean_set(km.ptr, "snap", 1);

	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_manipulator", WmEventTypes.ACTIONMOUSE, WmTypes.KM_PRESS, 0, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_cursor3d", WmEventTypes.ACTIONMOUSE, WmTypes.KM_PRESS, 0, 0);

	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_viewrotate", WmEventTypes.MIDDLEMOUSE, WmTypes.KM_PRESS, 0, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_viewmove", WmEventTypes.MIDDLEMOUSE, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_zoom", WmEventTypes.MIDDLEMOUSE, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0);
	WmKeymap.WM_keymap_verify_item(keymap, "VIEW3D_OT_view_center", WmEventTypes.PADPERIOD, WmTypes.KM_PRESS, 0, 0);

//	WM_keymap_verify_item(keymap, "VIEW3D_OT_smoothview", TIMER1, KM_ANY, KM_ANY, 0);

	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_zoom", WmEventTypes.PADPLUSKEY, WmTypes.KM_PRESS, 0, 0).ptr, "delta", 1);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_zoom", WmEventTypes.PADMINUS, WmTypes.KM_PRESS, 0, 0).ptr, "delta", -1);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_zoom", WmEventTypes.WHEELINMOUSE, WmTypes.KM_PRESS, 0, 0).ptr, "delta", 1);
	RnaAccess.RNA_int_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_zoom", WmEventTypes.WHEELOUTMOUSE, WmTypes.KM_PRESS, 0, 0).ptr, "delta", -1);

	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_all", WmEventTypes.HOMEKEY, WmTypes.KM_PRESS, 0, 0).ptr, "center", false);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_all", WmEventTypes.CKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "center", true);

	/* numpad view hotkeys*/
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD0, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_CAMERA);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD1, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_FRONT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_orbit", WmEventTypes.PAD2, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_STEPDOWN);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD3, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_RIGHT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_orbit", WmEventTypes.PAD4, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_STEPLEFT);
	WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_persportho", WmEventTypes.PAD5, WmTypes.KM_PRESS, 0, 0);

	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_orbit", WmEventTypes.PAD6, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_STEPRIGHT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD7, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_TOP);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_orbit", WmEventTypes.PAD8, WmTypes.KM_PRESS, 0, 0).ptr, "type", View3dTypes.V3D_VIEW_STEPUP);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD1, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_BACK);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD3, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_LEFT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_viewnumpad", WmEventTypes.PAD7, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_BOTTOM);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_pan", WmEventTypes.PAD2, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_PANDOWN);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_pan", WmEventTypes.PAD4, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_PANLEFT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_pan", WmEventTypes.PAD6, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_PANRIGHT);
	RnaAccess.RNA_enum_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_view_pan", WmEventTypes.PAD8, WmTypes.KM_PRESS, WmTypes.KM_CTRL, 0).ptr, "type", View3dTypes.V3D_VIEW_PANUP);

//	WM_keymap_add_item(keymap, "VIEW3D_OT_localview", PADSLASHKEY, KM_PRESS, 0, 0);
//
//	WM_keymap_add_item(keymap, "VIEW3D_OT_game_start", PKEY, KM_PRESS, 0, 0);
//
//	/* layers, shift + alt are properties set in invoke() */
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", ONEKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 1);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", TWOKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 2);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", THREEKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 3);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", FOURKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 4);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", FIVEKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 5);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", SIXKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 6);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", SEVENKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 7);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", EIGHTKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 8);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", NINEKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 9);
//	RNA_int_set(WM_keymap_add_item(keymap, "VIEW3D_OT_layers", ZEROKEY, KM_PRESS, KM_ANY, 0).ptr, "nr", 10);

	/* drawtype */
	km = WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_drawtype", WmEventTypes.ZKEY, WmTypes.KM_PRESS, 0, 0);
	RnaAccess.RNA_int_set(km.ptr, "draw_type", ObjectTypes.OB_SOLID);
	RnaAccess.RNA_int_set(km.ptr, "draw_type_alternate", ObjectTypes.OB_WIRE);

	km = WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_drawtype", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_ALT, 0);
	RnaAccess.RNA_int_set(km.ptr, "draw_type", ObjectTypes.OB_TEXTURE);
	RnaAccess.RNA_int_set(km.ptr, "draw_type_alternate", ObjectTypes.OB_SOLID);

	km = WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_drawtype", WmEventTypes.ZKEY, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0);
	RnaAccess.RNA_int_set(km.ptr, "draw_type", ObjectTypes.OB_SHADED);
	RnaAccess.RNA_int_set(km.ptr, "draw_type_alternate", ObjectTypes.OB_WIRE);

	/* selection*/
	WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_select", WmEventTypes.SELECTMOUSE, WmTypes.KM_PRESS, 0, 0);
	RnaAccess.RNA_boolean_set(WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_select", WmEventTypes.SELECTMOUSE, WmTypes.KM_PRESS, WmTypes.KM_SHIFT, 0).ptr, "extend", true);
//	WM_keymap_add_item(keymap, "VIEW3D_OT_select_border", BKEY, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "VIEW3D_OT_select_lasso", EVT_TWEAK_A, KM_ANY, KM_CTRL, 0);
//	RNA_boolean_set(WM_keymap_add_item(keymap, "VIEW3D_OT_select_lasso", EVT_TWEAK_A, KM_ANY, KM_SHIFT|KM_CTRL, 0).ptr, "deselect", 1);
//	WM_keymap_add_item(keymap, "VIEW3D_OT_select_circle", CKEY, KM_PRESS, 0, 0);
//
//	WM_keymap_add_item(keymap, "VIEW3D_OT_clip_border", BKEY, KM_PRESS, KM_ALT, 0);
//	WM_keymap_add_item(keymap, "VIEW3D_OT_zoom_border", BKEY, KM_PRESS, KM_SHIFT, 0);
//	WM_keymap_add_item(keymap, "VIEW3D_OT_render_border", BKEY, KM_PRESS, KM_SHIFT, 0);
//
//	WM_keymap_add_item(keymap, "VIEW3D_OT_camera_to_view", PAD0, KM_PRESS, KM_ALT|KM_CTRL, 0);
//
//	WM_keymap_add_item(keymap, "VIEW3D_OT_snap_menu", SKEY, KM_PRESS, KM_SHIFT, 0);

	/* drag & drop */
//	WmKeymap.WM_keymap_add_item(keymap, "VIEW3D_OT_drag", WmEventTypes.MOUSEDRAG, WmTypes.KM_ANY, 0, 0);

//	/* radial control */
//	RNA_enum_set(WM_keymap_add_item(keymap, "SCULPT_OT_radial_control", FKEY, KM_PRESS, 0, 0).ptr, "mode", WM_RADIALCONTROL_SIZE);
//	RNA_enum_set(WM_keymap_add_item(keymap, "SCULPT_OT_radial_control", FKEY, KM_PRESS, KM_SHIFT, 0).ptr, "mode", WM_RADIALCONTROL_STRENGTH);
//	RNA_enum_set(WM_keymap_add_item(keymap, "SCULPT_OT_radial_control", FKEY, KM_PRESS, KM_CTRL, 0).ptr, "mode", WM_RADIALCONTROL_ANGLE);
//
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_vertex_paint_radial_control", FKEY, KM_PRESS, 0, 0).ptr, "mode", WM_RADIALCONTROL_SIZE);
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_weight_paint_radial_control", FKEY, KM_PRESS, 0, 0).ptr, "mode", WM_RADIALCONTROL_SIZE);
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_texture_paint_radial_control", FKEY, KM_PRESS, 0, 0).ptr, "mode", WM_RADIALCONTROL_SIZE);
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_vertex_paint_radial_control", FKEY, KM_PRESS, KM_SHIFT, 0).ptr, "mode", WM_RADIALCONTROL_STRENGTH);
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_weight_paint_radial_control", FKEY, KM_PRESS, KM_SHIFT, 0).ptr, "mode", WM_RADIALCONTROL_STRENGTH);
//	RNA_enum_set(WM_keymap_add_item(keymap, "PAINT_OT_texture_paint_radial_control", FKEY, KM_PRESS, KM_SHIFT, 0).ptr, "mode", WM_RADIALCONTROL_STRENGTH);

	TransformOps.transform_keymap_for_space(keyconf, keymap, SpaceTypes.SPACE_VIEW3D);

}};

}

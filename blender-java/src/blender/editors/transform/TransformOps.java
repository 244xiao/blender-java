/**
 * $Id: TransformOps.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): none yet.
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.transform;

//#include "MEM_guardedalloc.h"

import static blender.blenkernel.Blender.G;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.editors.screen.ScreenOps;
import blender.editors.transform.Transform.TransInfo;
import blender.makesdna.SpaceTypes;
import blender.makesdna.WindowManagerTypes;
import blender.makesdna.WindowManagerTypes.wmOperatorType;
import blender.makesdna.sdna.wmKeyConfig;
import blender.makesdna.sdna.wmKeyMap;
import blender.makesdna.sdna.wmKeyMapItem;
import blender.makesdna.sdna.wmOperator;
import blender.makesrna.RnaAccess;
import blender.makesrna.RnaDefine;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmEventTypes;
import blender.windowmanager.WmKeymap;
import blender.windowmanager.WmOperators;
import blender.windowmanager.WmTypes;
import blender.windowmanager.WmOperators.OpFunc;
import blender.windowmanager.WmTypes.wmEvent;

//
//#include "DNA_scene_types.h"
//#include "DNA_space_types.h"
//#include "DNA_windowmanager_types.h"
//
//#include "RNA_access.h"
//#include "RNA_define.h"
//#include "RNA_enum_types.h"
//
//#include "BLI_arithb.h"
//
//#include "BKE_utildefines.h"
//#include "BKE_context.h"
//#include "BKE_global.h"
//
//#include "WM_api.h"
//#include "WM_types.h"
//
//#include "UI_interface.h"
//
//#include "ED_screen.h"
//
//#include "transform.h"

public class TransformOps {

public static class TransformModeItem
{
	public String idname;
	public int mode;

        public TransformModeItem(String idname, int mode) {
            this.idname = idname;
            this.mode = mode;
        }
};

static float[] VecOne = {1, 1, 1};

///* need constants for this */
//EnumPropertyItem proportional_mode_types[] = {
//		{0, "OFF", 0, "Off", ""},
//		{1, "ON", 0, "On", ""},
//		{2, "CONNECTED", 0, "Connected", ""},
//		{0, NULL, 0, NULL, NULL}
//};
//
//EnumPropertyItem snap_mode_types[] = {
//		{SCE_SNAP_TARGET_CLOSEST, "CLOSEST", 0, "Closest", ""},
//		{SCE_SNAP_TARGET_CENTER,  "CENTER", 0, "Center", ""},
//		{SCE_SNAP_TARGET_MEDIAN,  "MEDIAN", 0, "Median", ""},
//		{SCE_SNAP_TARGET_ACTIVE,  "ACTIVE", 0, "Active", ""},
//		{0, NULL, 0, NULL, NULL}
//};
//
//EnumPropertyItem proportional_falloff_types[] = {
//		{PROP_SMOOTH, "SMOOTH", 0, "Smooth", ""},
//		{PROP_SPHERE, "SPHERE", 0, "Sphere", ""},
//		{PROP_ROOT, "ROOT", 0, "Root", ""},
//		{PROP_SHARP, "SHARP", 0, "Sharp", ""},
//		{PROP_LIN, "LINEAR", 0, "Linear", ""},
//		{PROP_CONST, "CONSTANT", 0, "Constant", ""},
//		{PROP_RANDOM, "RANDOM", 0, "Random", ""},
//		{0, NULL, 0, NULL, NULL}
//};
//
//EnumPropertyItem orientation_items[]= {
//	{V3D_MANIP_GLOBAL, "GLOBAL", 0, "Global", ""},
//	{V3D_MANIP_NORMAL, "NORMAL", 0, "Normal", ""},
//	{V3D_MANIP_LOCAL, "LOCAL", 0, "Local", ""},
//	{V3D_MANIP_VIEW, "VIEW", 0, "View", ""},
//	{0, NULL, 0, NULL, NULL}};

static String OP_TRANSLATION = "TFM_OT_translate";
static String OP_ROTATION = "TFM_OT_rotate";
static String OP_TOSPHERE = "TFM_OT_tosphere";
static String OP_RESIZE = "TFM_OT_resize";
static String OP_SHEAR = "TFM_OT_shear";
static String OP_WARP = "TFM_OT_warp";
static String OP_SHRINK_FATTEN = "TFM_OT_shrink_fatten";
static String OP_TILT = "TFM_OT_tilt";
static String OP_TRACKBALL = "TFM_OT_trackball";


static TransformModeItem[] transform_modes =
{
	new TransformModeItem(OP_TRANSLATION, Transform.TFM_TRANSLATION),
	new TransformModeItem(OP_ROTATION, Transform.TFM_ROTATION),
	new TransformModeItem(OP_TOSPHERE, Transform.TFM_TOSPHERE),
	new TransformModeItem(OP_RESIZE, Transform.TFM_RESIZE),
	new TransformModeItem(OP_SHEAR, Transform.TFM_SHEAR),
	new TransformModeItem(OP_WARP, Transform.TFM_WARP),
	new TransformModeItem(OP_SHRINK_FATTEN, Transform.TFM_SHRINKFATTEN),
	new TransformModeItem(OP_TILT, Transform.TFM_TILT),
	new TransformModeItem(OP_TRACKBALL, Transform.TFM_TRACKBALL),
	new TransformModeItem(null, 0)
};

//static int select_orientation_exec(bContext *C, wmOperator *op)
//{
//	int orientation = RNA_enum_get(op.ptr, "orientation");
//
//	BIF_selectTransformOrientationValue(C, orientation);
//
//	return OPERATOR_FINISHED;
//}
//
//static int select_orientation_invoke(bContext *C, wmOperator *op, wmEvent *event)
//{
//	uiPopupMenu *pup;
//	uiLayout *layout;
//
//	pup= uiPupMenuBegin(C, "Orientation", 0);
//	layout= uiPupMenuLayout(pup);
//	uiItemsEnumO(layout, "TFM_OT_select_orientation", "orientation");
//	uiPupMenuEnd(C, pup);
//
//	return OPERATOR_CANCELLED;
//}
//
//static EnumPropertyItem *select_orientation_itemf(bContext *C, PointerRNA *ptr, int *free)
//{
//	*free= 1;
//	return BIF_enumTransformOrientation(C);
//}
//
//void TFM_OT_select_orientation(struct wmOperatorType *ot)
//{
//	PropertyRNA *prop;
//
//	/* identifiers */
//	ot.name   = "Select Orientation";
//	ot.idname = "TFM_OT_select_orientation";
//
//	/* api callbacks */
//	ot.invoke = select_orientation_invoke;
//	ot.exec   = select_orientation_exec;
//	ot.poll   = ED_operator_areaactive;
//
//	prop= RNA_def_enum(ot.srna, "orientation", orientation_items, V3D_MANIP_GLOBAL, "Orientation", "DOC_BROKEN");
//	RNA_def_enum_funcs(prop, select_orientation_itemf);
//}

static void transformops_exit(bContext C, wmOperator op)
{
//        System.out.println("transformops_exit");
	Transform.saveTransform(C, (TransInfo)op.customdata, op);
//	MEM_freeN(op.customdata);
	op.customdata = null;
	G.moving = 0;
}

static int transformops_data(bContext C, wmOperator op, wmEvent event)
{
	int retval = 1;
	if (op.customdata == null)
	{
		TransInfo t = new TransInfo();
		TransformModeItem[] tmodes;
                int tmode_p = 0;
		int mode = -1;

		for (tmodes = transform_modes; tmodes[tmode_p].idname!=null; tmode_p++)
		{
			if (((wmOperatorType)op.type).idname.equals(tmodes[tmode_p].idname))
			{
				mode = tmodes[tmode_p].mode;
			}
		}

		if (mode == -1)
		{
			mode = RnaAccess.RNA_int_get(op.ptr, "mode");
		}

		retval = Transform.initTransform(C, t, op, event, mode);
		G.moving = 1;

		/* store data */
		op.customdata = t;
	}

	return retval; /* return 0 on error */
}

public static wmOperatorType.Operator transform_modal = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int transform_modal(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("transform_modal");
	int exit_code;

	TransInfo t = (TransInfo)op.customdata;

	Transform.transformEvent(t, event);

	Transform.transformApply(C, t);

	exit_code = Transform.transformEnd(C, t);

	if (exit_code != WindowManagerTypes.OPERATOR_RUNNING_MODAL)
	{
		transformops_exit(C, op);
	}

	return exit_code;
}};

public static wmOperatorType.Operator transform_cancel = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int transform_cancel(bContext *C, wmOperator *op)
{
        System.out.println("transform_cancel");
	TransInfo t = (TransInfo)op.customdata;

	t.state = Transform.TRANS_CANCEL;
	Transform.transformEnd(C, t);
	transformops_exit(C, op);

	return WindowManagerTypes.OPERATOR_CANCELLED;
}};

public static wmOperatorType.Operator transform_exec = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int transform_exec(bContext *C, wmOperator *op)
{
        System.out.println("transform_exec");
	TransInfo t;

	if (transformops_data(C, op, null)==0)
	{
		return WindowManagerTypes.OPERATOR_CANCELLED;
	}

	t = (TransInfo)op.customdata;

	t.options |= Transform.CTX_AUTOCONFIRM;

	Transform.transformApply(C, t);

	Transform.transformEnd(C, t);

	transformops_exit(C, op);

	return WindowManagerTypes.OPERATOR_FINISHED;
}};

public static wmOperatorType.Operator transform_invoke = new wmOperatorType.Operator() {
public int run(bContext C, wmOperator op, wmEvent event)
//static int transform_invoke(bContext *C, wmOperator *op, wmEvent *event)
{
//        System.out.println("transform_invoke");
	if (transformops_data(C, op, event)==0)
	{
		return WindowManagerTypes.OPERATOR_CANCELLED;
	}

	if(RnaAccess.RNA_property_is_set(op.ptr, "value")) {
		return transform_exec.run(C, op, null);
	}
	else {
		TransInfo t = (TransInfo)op.customdata;

		/* add temp handler */
//		WmEventSystem.WM_event_add_modal_handler(C, bContext.CTX_wm_window(C).handlers, op);
		WmEventSystem.WM_event_add_modal_handler(C, op);

		t.flag |= Transform.T_MODAL; // XXX meh maybe somewhere else

		return WindowManagerTypes.OPERATOR_RUNNING_MODAL;
	}
}};

//void Properties_Proportional(struct wmOperatorType *ot)
//{
//	RNA_def_enum(ot.srna, "proportional", proportional_mode_types, 0, "Proportional Editing", "");
//	RNA_def_enum(ot.srna, "proportional_editing_falloff", prop_mode_items, 0, "Proportional Editing Falloff", "Falloff type for proportional editing mode.");
//	RNA_def_float(ot.srna, "proportional_size", 1, 0, FLT_MAX, "Proportional Size", "", 0, 100);
//}
//
//void Properties_Snapping(struct wmOperatorType *ot, short align)
//{
//	RNA_def_boolean(ot.srna, "snap", 0, "Snap to Point", "");
//	RNA_def_enum(ot.srna, "snap_mode", snap_mode_types, 0, "Mode", "");
//	RNA_def_float_vector(ot.srna, "snap_point", 3, NULL, -FLT_MAX, FLT_MAX, "Point", "", -FLT_MAX, FLT_MAX);
//
//	if (align)
//	{
//		RNA_def_boolean(ot.srna, "snap_align", 0, "Align with Point Normal", "");
//		RNA_def_float_vector(ot.srna, "snap_normal", 3, NULL, -FLT_MAX, FLT_MAX, "Normal", "", -FLT_MAX, FLT_MAX);
//	}
//}
//
//void Properties_Constraints(struct wmOperatorType *ot)
//{
//	PropertyRNA *prop;
//
//	RNA_def_boolean_vector(ot.srna, "constraint_axis", 3, NULL, "Constraint Axis", "");
//	prop= RNA_def_enum(ot.srna, "constraint_orientation", orientation_items, V3D_MANIP_GLOBAL, "Orientation", "DOC_BROKEN");
//	RNA_def_enum_funcs(prop, select_orientation_itemf);
//}

public static OpFunc TFM_OT_translate = new OpFunc() {
public void run(wmOperatorType ot)
//void TFM_OT_translate(struct wmOperatorType *ot)
{
	/* identifiers */
	ot.name   = "Translate";
	ot.idname = OP_TRANSLATION;
	ot.flag = WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_UNDO|WmTypes.OPTYPE_BLOCKING;

	/* api callbacks */
	ot.invoke = transform_invoke;
	ot.exec   = transform_exec;
	ot.modal  = transform_modal;
	ot.cancel  = transform_cancel;
	ot.poll   = ScreenOps.ED_operator_areaactive;

	RnaDefine.RNA_def_float_vector(ot.srna, "value", 3, null, -Float.MAX_VALUE, Float.MAX_VALUE, "Vector", "", -Float.MAX_VALUE, Float.MAX_VALUE);

//	Properties_Proportional(ot);

	RnaDefine.RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");

//	Properties_Constraints(ot);

//	Properties_Snapping(ot, 1);
}};

public static OpFunc TFM_OT_resize = new OpFunc() {
public void run(wmOperatorType ot)
//void TFM_OT_resize(struct wmOperatorType *ot)
{
	/* identifiers */
	ot.name   = "Resize";
	ot.idname = OP_RESIZE;
	ot.flag = WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_UNDO|WmTypes.OPTYPE_BLOCKING;

	/* api callbacks */
	ot.invoke = transform_invoke;
	ot.exec   = transform_exec;
	ot.modal  = transform_modal;
	ot.cancel  = transform_cancel;
	ot.poll   = ScreenOps.ED_operator_areaactive;

	RnaDefine.RNA_def_float_vector(ot.srna, "value", 3, VecOne, -Float.MAX_VALUE, Float.MAX_VALUE, "Vector", "", -Float.MAX_VALUE, Float.MAX_VALUE);

//	Properties_Proportional(ot);

	RnaDefine.RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");

//	Properties_Constraints(ot);

//	Properties_Snapping(ot, 0);
}};


//void TFM_OT_trackball(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "Trackball";
//	ot.idname = OP_TRACKBALL;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_areaactive;
//
//	RNA_def_float_vector(ot.srna, "value", 2, VecOne, -FLT_MAX, FLT_MAX, "angle", "", -FLT_MAX, FLT_MAX);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//}

public static OpFunc TFM_OT_rotate = new OpFunc() {
public void run(wmOperatorType ot)
//void TFM_OT_rotate(struct wmOperatorType *ot)
{
	/* identifiers */
	ot.name   = "Rotate";
	ot.idname = OP_ROTATION;
	ot.flag = WmTypes.OPTYPE_REGISTER|WmTypes.OPTYPE_UNDO|WmTypes.OPTYPE_BLOCKING;

	/* api callbacks */
	ot.invoke = transform_invoke;
	ot.exec   = transform_exec;
	ot.modal  = transform_modal;
	ot.cancel  = transform_cancel;
	ot.poll   = ScreenOps.ED_operator_areaactive;

	RnaDefine.RNA_def_float_rotation(ot.srna, "value", 1, null, -Float.MAX_VALUE, Float.MAX_VALUE, "Angle", "", -Arithb.M_PI*2, Arithb.M_PI*2);

//	Properties_Proportional(ot);

	RnaDefine.RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");

//	Properties_Constraints(ot);

//	Properties_Snapping(ot, 0);
}};

//void TFM_OT_tilt(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "Tilt";
//	ot.idname = OP_TILT;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_editcurve;
//
//	RNA_def_float_rotation(ot.srna, "value", 1, NULL, -FLT_MAX, FLT_MAX, "Angle", "", -M_PI*2, M_PI*2);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//
//	Properties_Constraints(ot);
//}
//
//void TFM_OT_warp(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "Warp";
//	ot.idname = OP_WARP;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_areaactive;
//
//	RNA_def_float_rotation(ot.srna, "value", 1, NULL, -FLT_MAX, FLT_MAX, "Angle", "", 0, 1);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//
//	// XXX Shear axis?
////	Properties_Constraints(ot);
//}
//
//void TFM_OT_shear(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "Shear";
//	ot.idname = OP_SHEAR;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_areaactive;
//
//	RNA_def_float(ot.srna, "value", 0, -FLT_MAX, FLT_MAX, "Offset", "", -FLT_MAX, FLT_MAX);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//
//	// XXX Shear axis?
////	Properties_Constraints(ot);
//}
//
//void TFM_OT_shrink_fatten(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "Shrink/Fatten";
//	ot.idname = OP_SHRINK_FATTEN;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_editmesh;
//
//	RNA_def_float(ot.srna, "value", 0, -FLT_MAX, FLT_MAX, "Offset", "", -FLT_MAX, FLT_MAX);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//}
//
//void TFM_OT_tosphere(struct wmOperatorType *ot)
//{
//	/* identifiers */
//	ot.name   = "To Sphere";
//	ot.idname = OP_TOSPHERE;
//	ot.flag = OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_areaactive;
//
//	RNA_def_float_percentage(ot.srna, "value", 0, 0, 1, "Percentage", "", 0, 1);
//
//	Properties_Proportional(ot);
//
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//}
//
//void TFM_OT_transform(struct wmOperatorType *ot)
//{
//	static EnumPropertyItem transform_mode_types[] = {
//			{TFM_INIT, "INIT", 0, "Init", ""},
//			{TFM_DUMMY, "DUMMY", 0, "Dummy", ""},
//			{TFM_TRANSLATION, "TRANSLATION", 0, "Translation", ""},
//			{TFM_ROTATION, "ROTATION", 0, "Rotation", ""},
//			{TFM_RESIZE, "RESIZE", 0, "Resize", ""},
//			{TFM_TOSPHERE, "TOSPHERE", 0, "Tosphere", ""},
//			{TFM_SHEAR, "SHEAR", 0, "Shear", ""},
//			{TFM_WARP, "WARP", 0, "Warp", ""},
//			{TFM_SHRINKFATTEN, "SHRINKFATTEN", 0, "Shrinkfatten", ""},
//			{TFM_TILT, "TILT", 0, "Tilt", ""},
//			{TFM_TRACKBALL, "TRACKBALL", 0, "Trackball", ""},
//			{TFM_PUSHPULL, "PUSHPULL", 0, "Pushpull", ""},
//			{TFM_CREASE, "CREASE", 0, "Crease", ""},
//			{TFM_MIRROR, "MIRROR", 0, "Mirror", ""},
//			{TFM_BONESIZE, "BONESIZE", 0, "Bonesize", ""},
//			{TFM_BONE_ENVELOPE, "BONE_ENVELOPE", 0, "Bone_Envelope", ""},
//			{TFM_CURVE_SHRINKFATTEN, "CURVE_SHRINKFATTEN", 0, "Curve_Shrinkfatten", ""},
//			{TFM_BONE_ROLL, "BONE_ROLL", 0, "Bone_Roll", ""},
//			{TFM_TIME_TRANSLATE, "TIME_TRANSLATE", 0, "Time_Translate", ""},
//			{TFM_TIME_SLIDE, "TIME_SLIDE", 0, "Time_Slide", ""},
//			{TFM_TIME_SCALE, "TIME_SCALE", 0, "Time_Scale", ""},
//			{TFM_TIME_EXTEND, "TIME_EXTEND", 0, "Time_Extend", ""},
//			{TFM_BAKE_TIME, "BAKE_TIME", 0, "Bake_Time", ""},
//			{TFM_BEVEL, "BEVEL", 0, "Bevel", ""},
//			{TFM_BWEIGHT, "BWEIGHT", 0, "Bweight", ""},
//			{TFM_ALIGN, "ALIGN", 0, "Align", ""},
//			{0, NULL, 0, NULL, NULL}
//	};
//
//	/* identifiers */
//	ot.name   = "Transform";
//	ot.idname = "TFM_OT_transform";
//	ot.flag= OPTYPE_REGISTER|OPTYPE_UNDO|OPTYPE_BLOCKING;
//
//	/* api callbacks */
//	ot.invoke = transform_invoke;
//	ot.exec   = transform_exec;
//	ot.modal  = transform_modal;
//	ot.cancel  = transform_cancel;
//	ot.poll   = ED_operator_areaactive;
//
//	RNA_def_enum(ot.srna, "mode", transform_mode_types, 0, "Mode", "");
//
//	RNA_def_float_vector(ot.srna, "value", 4, NULL, -FLT_MAX, FLT_MAX, "Values", "", -FLT_MAX, FLT_MAX);
//
//	Properties_Proportional(ot);
//	RNA_def_boolean(ot.srna, "mirror", 0, "Mirror Editing", "");
//
//	Properties_Constraints(ot);
//}

public static void transform_operatortypes()
{
//	WM_operatortype_append(TFM_OT_transform);
	WmOperators.WM_operatortype_append(TFM_OT_translate);
	WmOperators.WM_operatortype_append(TFM_OT_rotate);
//	WM_operatortype_append(TFM_OT_tosphere);
	WmOperators.WM_operatortype_append(TFM_OT_resize);
//	WM_operatortype_append(TFM_OT_shear);
//	WM_operatortype_append(TFM_OT_warp);
//	WM_operatortype_append(TFM_OT_shrink_fatten);
//	WM_operatortype_append(TFM_OT_tilt);
//	WM_operatortype_append(TFM_OT_trackball);
//
//	WM_operatortype_append(TFM_OT_select_orientation);
}

//public static void transform_keymap_for_space(wmWindowManager wm, ListBase keymap, int spaceid)
public static void transform_keymap_for_space(wmKeyConfig keyconf, wmKeyMap keymap, int spaceid)
{
	wmKeyMapItem km;

	/* transform.c, only adds modal map once, checks if it's there */
	Transform.transform_modal_keymap(keyconf);

	switch(spaceid)
	{
		case SpaceTypes.SPACE_VIEW3D:
			km = WmKeymap.WM_keymap_add_item(keymap, "TFM_OT_translate", WmEventTypes.GKEY, WmTypes.KM_PRESS, 0, 0);

			km = WmKeymap.WM_keymap_add_item(keymap, "TFM_OT_translate", WmEventTypes.EVT_TWEAK_S, WmTypes.KM_ANY, 0, 0);

			km = WmKeymap.WM_keymap_add_item(keymap, "TFM_OT_rotate", WmEventTypes.RKEY, WmTypes.KM_PRESS, 0, 0);

			km = WmKeymap.WM_keymap_add_item(keymap, "TFM_OT_resize", WmEventTypes.SKEY, WmTypes.KM_PRESS, 0, 0);

//			km = WM_keymap_add_item(keymap, "TFM_OT_warp", WKEY, KM_PRESS, KM_SHIFT, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_tosphere", SKEY, KM_PRESS, KM_CTRL|KM_SHIFT, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_shear", SKEY, KM_PRESS, KM_ALT|KM_CTRL|KM_SHIFT, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_shrink_fatten", SKEY, KM_PRESS, KM_ALT, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_tilt", TKEY, KM_PRESS, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_select_orientation", SPACEKEY, KM_PRESS, KM_ALT, 0);

			break;
		case SpaceTypes.SPACE_ACTION:
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", GKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_TRANSLATE);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EVT_TWEAK_S, KM_ANY, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_TRANSLATE);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_EXTEND);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", SKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_SCALE);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", TKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_SLIDE);
			break;
		case SpaceTypes.SPACE_IPO:
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", GKEY, KM_PRESS, 0, 0);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", EVT_TWEAK_S, KM_ANY, 0, 0);
//
//				// XXX the 'mode' identifier here is not quite right
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_EXTEND);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_rotate", RKEY, KM_PRESS, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_resize", SKEY, KM_PRESS, 0, 0);
			break;
		case SpaceTypes.SPACE_NLA:
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", GKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TRANSLATION);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EVT_TWEAK_S, KM_ANY, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TRANSLATION);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_EXTEND);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", SKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_SCALE);
			break;
		case SpaceTypes.SPACE_NODE:
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", GKEY, KM_PRESS, 0, 0);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", EVT_TWEAK_A, KM_ANY, 0, 0);
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", EVT_TWEAK_S, KM_ANY, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_rotate", RKEY, KM_PRESS, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_resize", SKEY, KM_PRESS, 0, 0);
			break;
		case SpaceTypes.SPACE_SEQ:
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", GKEY, KM_PRESS, 0, 0);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", EVT_TWEAK_S, KM_ANY, 0, 0);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_transform", EKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_TIME_EXTEND);
			break;
		case SpaceTypes.SPACE_IMAGE:
//			km = WM_keymap_add_item(keymap, "TFM_OT_translate", GKEY, KM_PRESS, 0, 0);
//
//			km= WM_keymap_add_item(keymap, "TFM_OT_translate", EVT_TWEAK_S, KM_ANY, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_rotate", RKEY, KM_PRESS, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_resize", SKEY, KM_PRESS, 0, 0);
//
//			km = WM_keymap_add_item(keymap, "TFM_OT_transform", MKEY, KM_PRESS, 0, 0);
//			RNA_int_set(km.ptr, "mode", TFM_MIRROR);
			break;
		default:
			break;
	}
}

}

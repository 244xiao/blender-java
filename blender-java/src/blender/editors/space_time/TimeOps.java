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
package blender.editors.space_time;

import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.makesdna.sdna.wmKeyConfig;

public class TimeOps {

/* ****************** Start/End Frame Operators *******************************/

//static int time_set_sfra_exec (bContext *C, wmOperator *op)
//{
//	Scene *scene= CTX_data_scene(C);
//	int frame= CFRA;
//
//	if (scene == NULL)
//		return OPERATOR_CANCELLED;
//
//	/* if 'end frame' (Preview Range or Actual) is less than 'frame',
//	 * clamp 'frame' to 'end frame'
//	 */
//	if (PEFRA < frame) frame= PEFRA;
//
//	/* if Preview Range is defined, set the 'start' frame for that */
//	if (scene->r.psfra)
//		scene->r.psfra= frame;
//	else
//		scene->r.sfra= frame;
//
//	WM_event_add_notifier(C, NC_SCENE|ND_FRAME, scene);
//
//	return OPERATOR_FINISHED;
//}
//
//void TIME_OT_start_frame_set (wmOperatorType *ot)
//{
//	/* identifiers */
//	ot->name= "Set Start Frame";
//	ot->idname= "TIME_OT_start_frame_set";
//
//	/* api callbacks */
//	ot->exec= time_set_sfra_exec;
//	ot->poll= ED_operator_timeline_active;
//
//	// XXX properties???
//}
//
//
//static int time_set_efra_exec (bContext *C, wmOperator *op)
//{
//	Scene *scene= CTX_data_scene(C);
//	int frame= CFRA;
//
//	if (scene == NULL)
//		return OPERATOR_CANCELLED;
//
//	/* if 'start frame' (Preview Range or Actual) is greater than 'frame',
//	 * clamp 'frame' to 'end frame'
//	 */
//	if (PSFRA > frame) frame= PSFRA;
//
//	/* if Preview Range is defined, set the 'end' frame for that */
//	if (scene->r.pefra)
//		scene->r.pefra= frame;
//	else
//		scene->r.efra= frame;
//
//	WM_event_add_notifier(C, NC_SCENE|ND_FRAME, scene);
//
//	return OPERATOR_FINISHED;
//}
//
//void TIME_OT_end_frame_set (wmOperatorType *ot)
//{
//	/* identifiers */
//	ot->name= "Set End Frame";
//	ot->idname= "TIME_OT_end_frame_set";
//
//	/* api callbacks */
//	ot->exec= time_set_efra_exec;
//	ot->poll= ED_operator_timeline_active;
//
//	// XXX properties???
//}

/* ************************** registration **********************************/

public static SpaceType.OperatorTypes time_operatortypes = new SpaceType.OperatorTypes() {
public void run()
//void time_operatortypes(void)
{
//	WM_operatortype_append(TIME_OT_start_frame_set);
//	WM_operatortype_append(TIME_OT_end_frame_set);
}};

public static ARegionType.KeyMap time_keymap = new ARegionType.KeyMap() {
public void run(wmKeyConfig keyconf)
//void time_keymap(wmWindowManager *wm)
{
//	ListBase *keymap= WM_keymap_listbase(wm, "TimeLine", SPACE_TIME, 0);
//
//	WM_keymap_add_item(keymap, "TIME_OT_start_frame_set", SKEY, KM_PRESS, 0, 0);
//	WM_keymap_add_item(keymap, "TIME_OT_end_frame_set", EKEY, KM_PRESS, 0, 0);
}};

}

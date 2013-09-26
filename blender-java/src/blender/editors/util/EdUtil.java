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
package blender.editors.util;

import blender.blenkernel.bContext;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UIStyle;
import blender.makesdna.ObjectTypes;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.bObject;
import static blender.blenkernel.Blender.G;

public class EdUtil {

/* ********* general editor util funcs, not BKE stuff please! ********* */

/* frees all editmode stuff */
public static void ED_editors_exit(bContext C)
{
	Scene sce;
	
	/* frees all editmode undos */
//	undo_editmode_clear();
//	undo_imagepaint_clear();
	
	for(sce=(Scene)G.main.scene.first; sce!=null; sce= (Scene)sce.id.next) {
		if(sce.obedit!=null) {
			bObject ob= sce.obedit;
		
			/* global in meshtools... */
//			mesh_octree_table(ob, NULL, NULL, 'e');
			
			if(ob!=null) {
				if(ob.type==ObjectTypes.OB_MESH) {
					Mesh me= (Mesh)ob.data;
					if(me.edit_mesh!=null) {
//						free_editMesh(me.edit_mesh);
//						MEM_freeN(me.edit_mesh);
						me.edit_mesh= null;
					}
				}
//				else if(ob.type==OB_ARMATURE) {
//					ED_armature_edit_free(ob);
//				}
//				else if(ob.type==OB_FONT) {
//					//			free_editText();
//				}
//				//		else if(ob.type==OB_MBALL)
//				//			BLI_freelistN(&editelems);
//				//	free_editLatt();
//				//	free_posebuf();
			}
		}
	}
	
}


///* ***** XXX: functions are using old blender names, cleanup later ***** */
//
//
///* now only used in 2d spaces, like time, ipo, nla, sima... */
///* XXX shift/ctrl not configurable */
//void apply_keyb_grid(int shift, int ctrl, float *val, float fac1, float fac2, float fac3, int invert)
//{
//	/* fac1 is for 'nothing', fac2 for CTRL, fac3 for SHIFT */
//	if(invert)
//		ctrl= !ctrl;
//
//	if(ctrl && shift) {
//		if(fac3!= 0.0) *val= fac3*floor(*val/fac3 +.5);
//	}
//	else if(ctrl) {
//		if(fac2!= 0.0) *val= fac2*floor(*val/fac2 +.5);
//	}
//	else {
//		if(fac1!= 0.0) *val= fac1*floor(*val/fac1 +.5);
//	}
//}


public static int GetButStringLength(String str)
{
	int rt;

	rt= UIStyle.UI_GetStringWidth(StringUtil.toCString(str),0);

	return rt + 15;
}

}

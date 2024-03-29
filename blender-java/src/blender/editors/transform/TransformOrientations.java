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
 * Contributor(s): Martin Poirier
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.editors.transform;

//#include <string.h>

import blender.blenkernel.Blender;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenlib.Arithb;
import blender.blenlib.EditVertUtil;
import blender.blenlib.EditVertUtil.EditEdge;
import blender.blenlib.EditVertUtil.EditFace;
import blender.blenlib.EditVertUtil.EditMesh;
import blender.blenlib.EditVertUtil.EditSelection;
import blender.blenlib.EditVertUtil.EditVert;
import blender.blenlib.StringUtil;
import blender.editors.mesh.EditMeshLib;
import blender.editors.transform.Transform.TransInfo;
import blender.makesdna.ObjectTypes;
import blender.makesdna.ScreenTypes;
import blender.makesdna.View3dTypes;
import blender.makesdna.sdna.Base;
import blender.makesdna.sdna.ListBase;
import blender.makesdna.sdna.Mesh;
import blender.makesdna.sdna.RegionView3D;
import blender.makesdna.sdna.Scene;
import blender.makesdna.sdna.TransformOrientation;
import blender.makesdna.sdna.View3D;
import blender.makesdna.sdna.bObject;

//
//#include "MEM_guardedalloc.h"
//
//#include "DNA_armature_types.h"
//#include "DNA_action_types.h"
//#include "DNA_curve_types.h"
//#include "DNA_listBase.h"
//#include "DNA_object_types.h"
//#include "DNA_meshdata_types.h"
//#include "DNA_mesh_types.h"
//#include "DNA_meta_types.h"
//#include "DNA_scene_types.h"
//#include "DNA_screen_types.h"
//#include "DNA_space_types.h"
//#include "DNA_view3d_types.h"
//
//#include "BKE_global.h"
//#include "BKE_utildefines.h"
//#include "BKE_armature.h"
//#include "BKE_context.h"
//
//#include "BLI_arithb.h"
//#include "BLI_blenlib.h"
//#include "BLI_editVert.h"
//
////#include "BIF_editmesh.h"
////#include "BIF_interface.h"
////#include "BIF_space.h"
////#include "BIF_toolbox.h"
//
//#include "ED_armature.h"
//#include "ED_mesh.h"
//#include "ED_util.h"
//
//#include "UI_interface.h"
//
//#include "RNA_define.h"
//
//#include "transform.h"

public class TransformOrientations {
//
///* *********************** TransSpace ************************** */
//
//void BIF_clearTransformOrientation(bContext *C)
//{
//	ListBase *transform_spaces = &CTX_data_scene(C).transform_spaces;
//	BLI_freelistN(transform_spaces);
//
//	// TRANSFORM_FIX_ME
//	// Need to loop over all view3d
////	if (G.vd.twmode >= V3D_MANIP_CUSTOM)
////		G.vd.twmode = V3D_MANIP_GLOBAL;	/* fallback to global	*/
//}
//
//void BIF_manageTransformOrientation(bContext *C, int confirm, int set) {
//	Object *obedit = CTX_data_edit_object(C);
//	Object *ob = CTX_data_active_object(C);
//	int index = -1;
//
//	if (obedit) {
//		if (obedit.type == OB_MESH)
//			index = manageMeshSpace(C, confirm, set);
//		else if (obedit.type == OB_ARMATURE)
//			index = manageBoneSpace(C, confirm, set);
//	}
//	else if (ob && (ob.flag & OB_POSEMODE)) {
//			index = manageBoneSpace(C, confirm, set);
//	}
//	else {
//		index = manageObjectSpace(C, confirm, set);
//	}
//
//	if (set && index != -1)
//	{
//		BIF_selectTransformOrientationValue(C, V3D_MANIP_CUSTOM + index);
//	}
//}
//
//int manageObjectSpace(bContext *C, int confirm, int set) {
//	Base *base = CTX_data_active_base(C);
//
//	if (base == NULL)
//		return -1;
//
////XXX	if (confirm == 0) {
////		if (set && pupmenu("Custom Orientation %t|Add and Use Active Object%x1") != 1) {
////			return -1;
////		}
////		else if (set == 0 && pupmenu("Custom Orientation %t|Add Active Object%x1") != 1) {
////			return -1;
////		}
////	}
//
//	return addObjectSpace(C, base.object);
//}
//
///* return 1 on confirm */
//int confirmSpace(int set, char text[])
//{
//	char menu[64];
//
//	if (set) {
//		sprintf(menu, "Custom Orientation %%t|Add and Use %s%%x1", text);
//	}
//	else {
//		sprintf(menu, "Custom Orientation %%t|Add %s%%x1", text);
//	}
//
////XXX	if (pupmenu(menu) == 1) {
//		return 1;
////	}
////	else {
////		return 0;
////	}
//}
//
//int manageBoneSpace(bContext *C, int confirm, int set) {
//	float mat[3][3];
//	float normal[3], plane[3];
//	char name[36] = "";
//	int index;
//
//	getTransformOrientation(C, normal, plane, 0);
//
//	if (confirm == 0 && confirmSpace(set, "Bone") == 0) {
//		return -1;
//	}
//
//	if (createSpaceNormalTangent(mat, normal, plane) == 0) {
////XXX		error("Cannot use zero-length bone");
//		return -1;
//	}
//
//	strcpy(name, "Bone");
//
//	/* Input name */
////XXX	sbutton(name, 1, 35, "name: ");
//
//	index = addMatrixSpace(C, mat, name);
//	return index;
//}
//
//int manageMeshSpace(bContext *C, int confirm, int set) {
//	float mat[3][3];
//	float normal[3], plane[3];
//	char name[36] = "";
//	int index;
//	int type;
//
//	type = getTransformOrientation(C, normal, plane, 0);
//
//	switch (type)
//	{
//		case ORIENTATION_VERT:
//			if (confirm == 0 && confirmSpace(set, "vertex") == 0) {
//				return -1;
//			}
//
//			if (createSpaceNormal(mat, normal) == 0) {
//// XXX				error("Cannot use vertex with zero-length normal");
//				return -1;
//			}
//
//			strcpy(name, "Vertex");
//			break;
//		case ORIENTATION_EDGE:
//			if (confirm == 0 && confirmSpace(set, "Edge") == 0) {
//				return -1;
//			}
//
//			if (createSpaceNormalTangent(mat, normal, plane) == 0) {
//// XXX				error("Cannot use zero-length edge");
//				return -1;
//			}
//
//			strcpy(name, "Edge");
//			break;
//		case ORIENTATION_FACE:
//			if (confirm == 0 && confirmSpace(set, "Face") == 0) {
//				return -1;
//			}
//
//			if (createSpaceNormalTangent(mat, normal, plane) == 0) {
//// XXX				error("Cannot use zero-area face");
//				return -1;
//			}
//
//			strcpy(name, "Face");
//			break;
//		default:
//			return -1;
//			break;
//	}
//
//	/* Input name */
////XXX	sbutton(name, 1, 35, "name: ");
//
//	index = addMatrixSpace(C, mat, name);
//	return index;
//}

public static boolean createSpaceNormal(float[][] mat, float[] normal)
{
	float[] tangent = {0.0f, 0.0f, 1.0f};

	UtilDefines.VECCOPY(mat[2], normal);
	if (Arithb.Normalize(mat[2]) == 0.0f) {
		return false; /* error return */
	}

	Arithb.Crossf(mat[0], mat[2], tangent);
	if (Arithb.Inpf(mat[0], mat[0]) == 0.0f) {
		tangent[0] = 1.0f;
		tangent[1] = tangent[2] = 0.0f;
		Arithb.Crossf(mat[0], tangent, mat[2]);
	}

	Arithb.Crossf(mat[1], mat[2], mat[0]);

	Arithb.Mat3Ortho(mat);

	return true;
}

public static boolean createSpaceNormalTangent(float[][] mat, float[] normal, float[] tangent)
{
	UtilDefines.VECCOPY(mat[2], normal);
	if (Arithb.Normalize(mat[2]) == 0.0f) {
		return false; /* error return */
	}

	/* preempt zero length tangent from causing trouble */
	if (tangent[0] == 0 && tangent[1] == 0 && tangent[2] == 0)
	{
		tangent[2] = 1;
	}

	Arithb.Crossf(mat[0], mat[2], tangent);
	if (Arithb.Normalize(mat[0]) == 0.0f) {
		return false; /* error return */
	}

	Arithb.Crossf(mat[1], mat[2], mat[0]);

	Arithb.Mat3Ortho(mat);

	return true;
}


//int addObjectSpace(bContext *C, Object *ob) {
//	float mat[3][3];
//	char name[36] = "";
//
//	Mat3CpyMat4(mat, ob.obmat);
//	Mat3Ortho(mat);
//
//	strncpy(name, ob.id.name+2, 35);
//
//	/* Input name */
////XXX	sbutton(name, 1, 35, "name: ");
//
//	return addMatrixSpace(C, mat, name);
//}
//
//int addMatrixSpace(bContext *C, float mat[3][3], char name[]) {
//	ListBase *transform_spaces = &CTX_data_scene(C).transform_spaces;
//	TransformOrientation *ts;
//	int index = 0;
//
//	/* if name is found in list, reuse that transform space */
//	for (index = 0, ts = transform_spaces.first; ts; ts = ts.next, index++) {
//		if (strncmp(ts.name, name, 35) == 0) {
//			break;
//		}
//	}
//
//	/* if not, create a new one */
//	if (ts == NULL)
//	{
//		ts = MEM_callocN(sizeof(TransformOrientation), "UserTransSpace from matrix");
//		BLI_addtail(transform_spaces, ts);
//		strncpy(ts.name, name, 35);
//	}
//
//	/* copy matrix into transform space */
//	Mat3CpyMat3(ts.mat, mat);
//
//	ED_undo_push(C, "Add/Update Transform Orientation");
//
//	return index;
//}
//
//void BIF_removeTransformOrientation(bContext *C, TransformOrientation *target) {
//	ListBase *transform_spaces = &CTX_data_scene(C).transform_spaces;
//	TransformOrientation *ts = transform_spaces.first;
//	//int selected_index = (G.vd.twmode - V3D_MANIP_CUSTOM);
//	int i;
//
//	for (i = 0, ts = transform_spaces.first; ts; ts = ts.next, i++) {
//		if (ts == target) {
//			// Transform_fix_me NEED TO DO THIS FOR ALL VIEW3D
////			if (selected_index == i) {
////				G.vd.twmode = V3D_MANIP_GLOBAL;	/* fallback to global	*/
////			}
////			else if (selected_index > i)
////				G.vd.twmode--;
//
//			BLI_freelinkN(transform_spaces, ts);
//			break;
//		}
//	}
//	ED_undo_push(C, "Remove Transform Orientation");
//}
//
//void BIF_selectTransformOrientation(bContext *C, TransformOrientation *target) {
//	ListBase *transform_spaces = &CTX_data_scene(C).transform_spaces;
//	View3D *v3d = CTX_wm_view3d(C);
//	TransformOrientation *ts = transform_spaces.first;
//	int i;
//
//	for (i = 0, ts = transform_spaces.first; ts; ts = ts.next, i++) {
//		if (ts == target) {
//			v3d.twmode = V3D_MANIP_CUSTOM + i;
//			break;
//		}
//	}
//}
//
//void BIF_selectTransformOrientationValue(bContext *C, int orientation) {
//	View3D *v3d = CTX_wm_view3d(C);
//	v3d.twmode = orientation;
//}
//
//EnumPropertyItem *BIF_enumTransformOrientation(bContext *C)
//{
//	Scene *scene;
//	ListBase *transform_spaces;
//	TransformOrientation *ts= NULL;
//
//	EnumPropertyItem global	= {V3D_MANIP_GLOBAL, "GLOBAL", 0, "Global", ""};
//	EnumPropertyItem normal = {V3D_MANIP_NORMAL, "NORMAL", 0, "Normal", ""};
//	EnumPropertyItem local = {V3D_MANIP_LOCAL, "LOCAL", 0, "Local", ""};
//	EnumPropertyItem view = {V3D_MANIP_VIEW, "VIEW", 0, "View", ""};
//	EnumPropertyItem tmp = {0, "", 0, "", ""};
//	EnumPropertyItem *item= NULL;
//	int i = V3D_MANIP_CUSTOM, totitem= 0;
//
//	RNA_enum_item_add(&item, &totitem, &global);
//	RNA_enum_item_add(&item, &totitem, &normal);
//	RNA_enum_item_add(&item, &totitem, &local);
//	RNA_enum_item_add(&item, &totitem, &view);
//
//	if(C) {
//		scene= CTX_data_scene(C);
//
//		if(scene) {
//			transform_spaces = &scene.transform_spaces;
//			ts = transform_spaces.first;
//		}
//	}
//
//	if(ts)
//		RNA_enum_item_add_separator(&item, &totitem);
//
//	for(; ts; ts = ts.next) {
//		tmp.identifier = "CUSTOM";
//		tmp.name= ts.name;
//		tmp.value = i++;
//		RNA_enum_item_add(&item, &totitem, &tmp);
//	}
//
//	RNA_enum_item_end(&item, &totitem);
//
//	return item;
//}

public static String BIF_menustringTransformOrientation(bContext C, String title) {
	String menu = "%t|Global%x0|Local%x1|Normal%x2|View%x3";
	ListBase transform_spaces = bContext.CTX_data_scene(C).transform_spaces;
	TransformOrientation ts;
	int i = View3dTypes.V3D_MANIP_CUSTOM;
//	char *str_menu, *p;
        StringBuilder str_menu = new StringBuilder();


//	str_menu = MEM_callocN(strlen(menu) + strlen(title) + 1 + 40 * BIF_countTransformOrientation(C), "UserTransSpace from matrix");
//	p = str_menu;

//	p += sprintf(str_menu, "%s", title);
        str_menu.append(title);
//	p += sprintf(p, "%s", menu);
        str_menu.append(menu);

	for (ts = (TransformOrientation)transform_spaces.first; ts!=null; ts = ts.next) {
//		p += sprintf(p, "|%s%%x%d", ts.name, i++);
                str_menu.append(String.format("|%s%%x%d", ts.name, i++));
	}

	return str_menu.toString();
}

public static int BIF_countTransformOrientation(bContext C) {
	ListBase transform_spaces = bContext.CTX_data_scene(C).transform_spaces;
	TransformOrientation ts;
	int count = 0;

	for (ts = (TransformOrientation)transform_spaces.first; ts!=null; ts = ts.next) {
		count++;
	}

	return count;
}

public static void applyTransformOrientation(bContext C, TransInfo t) {
	TransformOrientation ts;
	View3D v3d = bContext.CTX_wm_view3d(C);
	int selected_index = (v3d.twmode - View3dTypes.V3D_MANIP_CUSTOM);
	int i;

	if (selected_index >= 0) {
		for (i = 0, ts = (TransformOrientation)bContext.CTX_data_scene(C).transform_spaces.first; ts!=null; ts = ts.next, i++) {
			if (selected_index == i) {
				StringUtil.strcpy(t.spacename,0, ts.name,0);
				Arithb.Mat3CpyMat3(t.spacemtx, ts.mat);
				break;
			}
		}
  	}
}

//static int count_bone_select(bArmature *arm, ListBase *lb, int do_it)
//{
//	Bone *bone;
//	int do_next;
//	int total = 0;
//
//	for(bone= lb.first; bone; bone= bone.next) {
//		bone.flag &= ~BONE_TRANSFORM;
//		do_next = do_it;
//		if(do_it) {
//			if(bone.layer & arm.layer) {
//				if (bone.flag & BONE_SELECTED) {
//					bone.flag |= BONE_TRANSFORM;
//					total++;
//					do_next= 0;	// no transform on children if one parent bone is selected
//				}
//			}
//		}
//		total += count_bone_select(arm, &bone.childbase, do_next);
//	}
//
//	return total;
//}

public static void initTransformOrientation(bContext C, TransInfo t)
{
	View3D v3d = bContext.CTX_wm_view3d(C);
	bObject ob = bContext.CTX_data_active_object(C);
	bObject obedit = bContext.CTX_data_active_object(C);
	float[] normal={0.0f, 0.0f, 0.0f};
	float[] plane={0.0f, 0.0f, 0.0f};

	switch(t.current_orientation) {
	case View3dTypes.V3D_MANIP_GLOBAL:
		StringUtil.strcpy(t.spacename,0, StringUtil.toCString("global"),0);
		break;

	case View3dTypes.V3D_MANIP_NORMAL:
		if(obedit!=null || (ob.flag & ObjectTypes.OB_POSEMODE)!=0) {
//			float mat[3][3];
//			int type;
//
//			strcpy(t.spacename, "normal");
//
//			type = getTransformOrientation(C, normal, plane, (v3d.around == V3D_ACTIVE));
//
//			switch (type)
//			{
//				case ORIENTATION_NORMAL:
//					if (createSpaceNormalTangent(mat, normal, plane) == 0)
//					{
//						type = ORIENTATION_NONE;
//					}
//					break;
//				case ORIENTATION_VERT:
//					if (createSpaceNormal(mat, normal) == 0)
//					{
//						type = ORIENTATION_NONE;
//					}
//					break;
//				case ORIENTATION_EDGE:
//					if (createSpaceNormalTangent(mat, normal, plane) == 0)
//					{
//						type = ORIENTATION_NONE;
//					}
//					break;
//				case ORIENTATION_FACE:
//					if (createSpaceNormalTangent(mat, normal, plane) == 0)
//					{
//						type = ORIENTATION_NONE;
//					}
//					break;
//			}
//
//			if (type == ORIENTATION_NONE)
//			{
//				Mat3One(t.spacemtx);
//			}
//			else
//			{
//				Mat3CpyMat3(t.spacemtx, mat);
//			}
//			break;
		}
		/* no break we define 'normal' as 'local' in Object mode */
	case View3dTypes.V3D_MANIP_LOCAL:
                StringUtil.strcpy(t.spacename,0, StringUtil.toCString("local"),0);
		Arithb.Mat3CpyMat4(t.spacemtx, ob.obmat);
		Arithb.Mat3Ortho(t.spacemtx);
		break;

	case View3dTypes.V3D_MANIP_VIEW:
		if (t.ar.regiontype == ScreenTypes.RGN_TYPE_WINDOW)
		{
			RegionView3D rv3d = (RegionView3D)t.ar.regiondata;
			float[][] mat = new float[3][3];

                        StringUtil.strcpy(t.spacename,0, StringUtil.toCString("view"),0);
			Arithb.Mat3CpyMat4(mat, rv3d.viewinv);
			Arithb.Mat3Ortho(mat);
			Arithb.Mat3CpyMat3(t.spacemtx, mat);
		}
		else
		{
			Arithb.Mat3One(t.spacemtx);
		}
		break;
	default: /* V3D_MANIP_CUSTOM */
		applyTransformOrientation(C, t);
		break;
	}
}

public static int getTransformOrientation(bContext C, float[] normal, float[] plane, boolean activeOnly)
{
	Scene scene = bContext.CTX_data_scene(C);
	View3D v3d = bContext.CTX_wm_view3d(C);
	bObject obedit= bContext.CTX_data_edit_object(C);
	Base base;
	bObject ob = Blender.OBACT(scene);
	int result = Transform.ORIENTATION_NONE;

	normal[0] = normal[1] = normal[2] = 0;
	plane[0] = plane[1] = plane[2] = 0;

	if(obedit!=null)
	{
		float[][] imat = new float[3][3], mat = new float[3][3];

		/* we need the transpose of the inverse for a normal... */
		Arithb.Mat3CpyMat4(imat, ob.obmat);

		Arithb.Mat3Inv(mat, imat);
		Arithb.Mat3Transp(mat);

		ob= obedit;

		if(ob.type==ObjectTypes.OB_MESH)
		{
			Mesh me= (Mesh)ob.data;
			EditMesh em = (EditMesh)me.edit_mesh;
			EditVert eve;
			EditSelection ese = new EditSelection();
			float[] vec= {0,0,0};

			/* USE LAST SELECTED WITH ACTIVE */
			if (activeOnly && EditMeshLib.EM_get_actSelection(em, ese))
			{
				EditMeshLib.EM_editselection_normal(normal, ese);
				EditMeshLib.EM_editselection_plane(plane, ese);

				switch (ese.type)
				{
					case EditVertUtil.EDITVERT:
						result = Transform.ORIENTATION_VERT;
						break;
					case EditVertUtil.EDITEDGE:
						result = Transform.ORIENTATION_EDGE;
						break;
					case EditVertUtil.EDITFACE:
						result = Transform.ORIENTATION_FACE;
						break;
				}
			}
			else
			{
				if (em.totfacesel >= 1)
				{
					EditFace efa;

					for(efa= em.faces.first; efa!=null; efa= efa.next)
					{
						if((efa.f & Blender.SELECT)!=0)
						{
							UtilDefines.VECADD(normal, normal, efa.n);
							Arithb.VecSubf(vec, efa.v2.co, efa.v1.co);
							UtilDefines.VECADD(plane, plane, vec);
						}
					}

					result = Transform.ORIENTATION_FACE;
				}
				else if (em.totvertsel == 3)
				{
					EditVert v1 = null, v2 = null, v3 = null;
					float[] cotangent = new float[3];

					for (eve = em.verts.first; eve!=null; eve = eve.next)
					{
						if ((eve.f & Blender.SELECT)!=0) {
							if (v1 == null) {
								v1 = eve;
							}
							else if (v2 == null) {
								v2 = eve;
							}
							else {
								v3 = eve;

								Arithb.VecSubf(plane, v2.co, v1.co);
								Arithb.VecSubf(cotangent, v3.co, v2.co);
								Arithb.Crossf(normal, cotangent, plane);
								break;
							}
						}
					}

					/* if there's an edge available, use that for the tangent */
					if (em.totedgesel >= 1)
					{
						EditEdge eed = null;

						for(eed= em.edges.first; eed!=null; eed= eed.next) {
							if((eed.f & Blender.SELECT)!=0) {
								Arithb.VecSubf(plane, eed.v2.co, eed.v1.co);
								break;
							}
						}
					}

					result = Transform.ORIENTATION_FACE;
				}
				else if (em.totedgesel == 1)
				{
					EditEdge eed;

					for(eed= em.edges.first; eed!=null; eed= eed.next) {
						if((eed.f & Blender.SELECT)!=0) {
							/* use average vert normals as plane and edge vector as normal */
							UtilDefines.VECCOPY(plane, eed.v1.no);
							UtilDefines.VECADD(plane, plane, eed.v2.no);
							Arithb.VecSubf(normal, eed.v2.co, eed.v1.co);
							break;
						}
					}
					result = Transform.ORIENTATION_EDGE;
				}
				else if (em.totvertsel == 2)
				{
					EditVert v1 = null, v2 = null;

					for (eve = em.verts.first; eve!=null; eve = eve.next)
					{
						if ((eve.f & Blender.SELECT)!=0) {
							if (v1 == null) {
								v1 = eve;
							}
							else {
								v2 = eve;

								UtilDefines.VECCOPY(plane, v1.no);
								UtilDefines.VECADD(plane, plane, v2.no);
								Arithb.VecSubf(normal, v2.co, v1.co);
								break;
							}
						}
					}
					result = Transform.ORIENTATION_EDGE;
				}
				else if (em.totvertsel == 1)
				{
					for (eve = em.verts.first; eve!=null; eve = eve.next)
					{
						if ((eve.f & Blender.SELECT)!=0) {
							UtilDefines.VECCOPY(normal, eve.no);
							break;
						}
					}
					result = Transform.ORIENTATION_VERT;
				}
				else if (em.totvertsel > 3)
				{
					normal[0] = normal[1] = normal[2] = 0;

					for (eve = em.verts.first; eve!=null; eve = eve.next)
					{
						if ((eve.f & Blender.SELECT)!=0) {
							Arithb.VecAddf(normal, normal, eve.no);
						}
					}
					Arithb.Normalize(normal);
					result = Transform.ORIENTATION_VERT;
				}
			}
		} /* end editmesh */
//		else if ELEM(obedit.type, OB_CURVE, OB_SURF)
//		{
//			Curve *cu= obedit.data;
//			Nurb *nu;
//			BezTriple *bezt;
//			int a;
//
//			for (nu = cu.editnurb.first; nu; nu = nu.next)
//			{
//				/* only bezier has a normal */
//				if((nu.type & 7) == CU_BEZIER)
//				{
//					bezt= nu.bezt;
//					a= nu.pntsu;
//					while(a--)
//					{
//						/* exception */
//						if ( (bezt.f1 & SELECT) + (bezt.f2 & SELECT) + (bezt.f3 & SELECT) > SELECT )
//						{
//							VecSubf(normal, bezt.vec[0], bezt.vec[2]);
//						}
//						else
//						{
//							if(bezt.f1)
//							{
//								VecSubf(normal, bezt.vec[0], bezt.vec[1]);
//							}
//							if(bezt.f2)
//							{
//								VecSubf(normal, bezt.vec[0], bezt.vec[2]);
//							}
//							if(bezt.f3)
//							{
//								VecSubf(normal, bezt.vec[1], bezt.vec[2]);
//							}
//						}
//						bezt++;
//					}
//				}
//			}
//
//			if (normal[0] != 0 || normal[1] != 0 || normal[2] != 0)
//			{
//				result = ORIENTATION_NORMAL;
//			}
//		}
//		else if(obedit.type==OB_MBALL)
//		{
//#if 0 // XXX
//			/* editmball.c */
//			extern ListBase editelems;  /* go away ! */
//			MetaElem *ml, *ml_sel = NULL;
//
//			/* loop and check that only one element is selected */
//			for (ml = editelems.first; ml; ml = ml.next)
//			{
//				if (ml.flag & SELECT) {
//					if (ml_sel == NULL)
//					{
//						ml_sel = ml;
//					}
//					else
//					{
//						ml_sel = NULL;
//						break;
//					}
//				}
//			}
//
//			if (ml_sel)
//			{
//				float mat[4][4];
//
//				/* Rotation of MetaElem is stored in quat */
// 				QuatToMat4(ml_sel.quat, mat);
//
//				VECCOPY(normal, mat[2]);
//				VECCOPY(plane, mat[1]);
//
//				VecMulf(plane, -1.0);
//
//				result = ORIENTATION_NORMAL;
//			}
//#endif
//
//		}
//		else if (obedit.type == OB_ARMATURE)
//		{
//			bArmature *arm = obedit.data;
//			EditBone *ebone;
//
//			for (ebone = arm.edbo.first; ebone; ebone=ebone.next) {
//				if (arm.layer & ebone.layer)
//				{
//					if (ebone.flag & BONE_SELECTED)
//					{
//						float mat[3][3];
//						float vec[3];
//						VecSubf(vec, ebone.tail, ebone.head);
//						Normalize(vec);
//						VecAddf(normal, normal, vec);
//
//						vec_roll_to_mat3(vec, ebone.roll, mat);
//						VecAddf(plane, plane, mat[2]);
//					}
//				}
//			}
//
//			Normalize(normal);
//			Normalize(plane);
//
//			if (plane[0] != 0 || plane[1] != 0 || plane[2] != 0)
//			{
//				result = ORIENTATION_EDGE;
//			}
//
//		}

		/* Vectors from edges don't need the special transpose inverse multiplication */
		if (result == Transform.ORIENTATION_EDGE)
		{
			Arithb.Mat4Mul3Vecfl(ob.obmat, normal);
			Arithb.Mat4Mul3Vecfl(ob.obmat, plane);
		}
		else
		{
			Arithb.Mat3MulVecfl(mat, normal);
			Arithb.Mat3MulVecfl(mat, plane);
		}
	}
//	else if(ob && (ob.flag & OB_POSEMODE))
//	{
//		bArmature *arm= ob.data;
//		bPoseChannel *pchan;
//		int totsel;
//
//		totsel = count_bone_select(arm, &arm.bonebase, 1);
//		if(totsel) {
//			float imat[3][3], mat[3][3];
//
//			/* use channels to get stats */
//			for(pchan= ob.pose.chanbase.first; pchan; pchan= pchan.next) {
//				if (pchan.bone && pchan.bone.flag & BONE_TRANSFORM) {
//					VecAddf(normal, normal, pchan.pose_mat[2]);
//					VecAddf(plane, plane, pchan.pose_mat[1]);
//				}
//			}
//			VecMulf(plane, -1.0);
//
//			/* we need the transpose of the inverse for a normal... */
//			Mat3CpyMat4(imat, ob.obmat);
//
//			Mat3Inv(mat, imat);
//			Mat3Transp(mat);
//			Mat3MulVecfl(mat, normal);
//			Mat3MulVecfl(mat, plane);
//
//			result = ORIENTATION_EDGE;
//		}
//	}
//	else if(G.f & (G_VERTEXPAINT + G_TEXTUREPAINT + G_WEIGHTPAINT + G_SCULPTMODE))
//	{
//	}
//	else if(G.f & G_PARTICLEEDIT)
//	{
//	}
	else {
		/* we need the one selected object, if its not active */
		ob = Blender.OBACT(scene);
		if(ob!=null && (ob.flag & Blender.SELECT)==0) ob = null;

		for(base= (Base)scene.base.first; base!=null; base= base.next) {
			if (Blender.TESTBASELIB(v3d, base)) {
				if(ob == null) {
					ob= base.object;
					break;
				}
			}
		}

		UtilDefines.VECCOPY(normal, ob.obmat[2]);
		UtilDefines.VECCOPY(plane, ob.obmat[1]);
		result = Transform.ORIENTATION_NORMAL;
	}

	return result;
}

}
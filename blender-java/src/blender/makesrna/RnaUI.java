/**
 * $Id: RnaUI.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
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
 * Contributor(s): Blender Foundation (2009)
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.makesrna;

//import javax.media.opengl.GL2;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyType;

import blender.blenkernel.ScreenUtil;
import blender.blenkernel.bContext;
import blender.blenkernel.ScreenUtil.ARegionType;
import blender.blenkernel.ScreenUtil.Header;
import blender.blenkernel.ScreenUtil.HeaderType;
import blender.blenkernel.ScreenUtil.Menu;
import blender.blenkernel.ScreenUtil.MenuType;
import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenkernel.ScreenUtil.SpaceType;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UI;
import blender.editors.uinterface.UILayout;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.ScreenTypes;
import blender.makesdna.SpaceTypes;
import blender.makesdna.sdna.Panel;
import blender.makesdna.sdna.ReportList;
import blender.makesrna.Makesrna.RNAProcess;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.ParameterList;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RNATypes.StructCallbackFunc;
import blender.makesrna.RNATypes.StructFreeFunc;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.StructUnregisterFunc;
import blender.makesrna.RNATypes.StructValidateFunc;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.rna_internal_types.StructRefineFunc;
import blender.makesrna.srna.RnaHeader;
import blender.windowmanager.Wm;
import blender.windowmanager.WmEventSystem;
import blender.windowmanager.WmTypes;

public class RnaUI {
	
	/* see WM_types.h */
	private static EnumPropertyItem[] operator_context_items = {
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_DEFAULT, "INVOKE_DEFAULT", 0, "Invoke Default", ""),
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_REGION_WIN, "INVOKE_REGION_WIN", 0, "Invoke Region Window", ""),
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_REGION_CHANNELS, "INVOKE_REGION_CHANNELS", 0, "Invoke Region Channels", ""),
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_REGION_PREVIEW, "INVOKE_REGION_PREVIEW", 0, "Invoke Region Preview", ""),
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_AREA, "INVOKE_AREA", 0, "Invoke Area", ""),
		new EnumPropertyItem(WmTypes.WM_OP_INVOKE_SCREEN, "INVOKE_SCREEN", 0, "Invoke Screen", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_DEFAULT, "EXEC_DEFAULT", 0, "Exec Default", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_REGION_WIN, "EXEC_REGION_WIN", 0, "Exec Region Window", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_REGION_CHANNELS, "EXEC_REGION_CHANNELS", 0, "Exec Region Channels", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_REGION_PREVIEW, "EXEC_REGION_PREVIEW", 0, "Exec Region Preview", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_AREA, "EXEC_AREA", 0, "Exec Area", ""),
		new EnumPropertyItem(WmTypes.WM_OP_EXEC_SCREEN, "EXEC_SCREEN", 0, "Exec Screen", ""),
		new EnumPropertyItem(0, null, 0, null, null)};

static ARegionType region_type_find(ReportList reports, int space_type, int region_type)
{
	SpaceType st;
	ARegionType art;

	st= ScreenUtil.BKE_spacetype_from_id(space_type);

	for(art= (st!=null)? st.regiontypes.first: null; art!=null; art= art.next) {
		if (art.regionid==region_type)
			break;
	}
	
	/* region type not found? abort */
	if (art==null) {
//		BKE_report(reports, RPT_ERROR, "Region not found in spacetype.");
		return null;
	}
	
//	if (space_type == SpaceTypes.SPACE_BUTS && region_type == ScreenTypes.RGN_TYPE_WINDOW) {
//		System.out.println("st2: "+st.hashCode());
//		System.out.println("art2: "+art.hashCode());
//	}

	return art;
}

///* Panel */
//
public static PanelType.Poll panel_poll = new PanelType.Poll() {
public boolean run(bContext C, PanelType pt)
//static int panel_poll(const bContext *C, PanelType *pt)
{
//	PointerRNA ptr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
	boolean visible;

//	RNA_pointer_create(NULL, pt.ext.srna, NULL, &ptr); /* dummy */
//	func= RNA_struct_find_function(&ptr, "poll");
//
//	RNA_parameter_list_create(&list, &ptr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	pt.ext.call(&ptr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "visible", &ret);
//	visible= *(int*)ret;
	visible = true;
//
//	RNA_parameter_list_free(&list);

	return visible;
}};

public static PanelType.Draw panel_draw = new PanelType.Draw() {
public void run(bContext C, Panel pnl)
//static void panel_draw(const bContext *C, Panel *pnl)
{
//	System.out.println("panel_draw");
	PointerRNA ptr = new PointerRNA();
	ParameterList list = new ParameterList();
	FunctionRNA func;

//	RNA_pointer_create(&CTX_wm_screen(C)->id, pnl->type->ext.srna, pnl, &ptr);
	RnaAccess.RNA_pointer_create(bContext.CTX_wm_screen(C).id, ((PanelType)pnl.type).ext.srna, pnl, ptr);
//	func= RNA_struct_find_function(&ptr, "draw");
	func= RnaAccess.RNA_struct_find_function(ptr, "draw");
//
//	RNA_parameter_list_create(&list, &ptr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	pnl->type->ext.call((bContext *)C, &ptr, func, &list);
	((PanelType)pnl.type).ext.call.run(C, ptr, func, list);
//
//	RNA_parameter_list_free(&list);
}};

public static PanelType.Draw panel_draw_header = new PanelType.Draw() {
public void run(bContext C, Panel pnl)
//static void panel_draw_header(const bContext *C, Panel *pnl)
{
//	System.out.println("panel_draw_header");
	PointerRNA ptr = new PointerRNA();
	ParameterList list = new ParameterList();
	FunctionRNA func;

//	RNA_pointer_create(&CTX_wm_screen(C)->id, pnl->type->ext.srna, pnl, &ptr);
	RnaAccess.RNA_pointer_create(bContext.CTX_wm_screen(C).id, ((PanelType)pnl.type).ext.srna, pnl, ptr);
//	func= RNA_struct_find_function(&ptr, "draw_header");
	func= RnaAccess.RNA_struct_find_function(ptr, "draw_header");
//
//	RNA_parameter_list_create(&list, &ptr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	pnl->type->ext.call((bContext *)C, &ptr, func, &list);
	((PanelType)pnl.type).ext.call.run(C, ptr, func, list);
//
//	RNA_parameter_list_free(&list);
}};

public static StructUnregisterFunc rna_Panel_unregister = new StructUnregisterFunc() {
public void run(bContext C, StructRNA type)
//static void rna_Panel_unregister(const bContext *C, StructRNA *type)
{
//	ARegionType *art;
//	PanelType *pt= RNA_struct_blender_type_get(type);
//
//	if(!pt)
//		return;
//	if(!(art=region_type_find(NULL, pt.space_type, pt.region_type)))
//		return;
//
//	BLI_freelinkN(&art.paneltypes, pt);
//	RNA_struct_free(&BLENDER_RNA, type);
//
//	/* update while blender is running */
//	if(C)
//		WM_event_add_notifier(C, NC_SCREEN|NA_EDITED, NULL);
}
public String toString() { return "rna_Panel_unregister"; }
};

public static StructRegisterFunc rna_Panel_register = new StructRegisterFunc() {
public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
//static StructRNA *rna_Panel_register(const bContext *C, ReportList *reports, void *data, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
{
//	System.out.println("rna_Panel_register");
	ARegionType art;
	PanelType pt, dummypt = new PanelType();
	Panel dummypanel= new Panel();
	PointerRNA dummyptr = new PointerRNA();
//	int have_function[3];
	int[] have_function=new int[3];

	/* setup dummy panel & panel type to store static properties in */
	dummypanel.type= dummypt;
//	RnaAccess.RNA_pointer_create(null, &RNA_Panel, dummypanel, dummyptr);
	RnaAccess.RNA_pointer_create(null, new StructRNA(RnaUI.rna_Panel_register), dummypanel, dummyptr);

	/* validate the python class */
//	if(validate.run(dummyptr, data, have_function) != 0)
//		return null;
	validate.run(dummyptr, data, have_function);
//	System.out.println("spacetype: "+dummypt.space_type+", regiontype: "+dummypt.region_type);
	
	// TMP
	PyType type = (PyType)data;
	dummypt.space_type = SpaceTypes.SPACE_EMPTY;
//	String bl_space_type = type.__findattr__("bl_space_type").toString();
	String bl_space_type = type.__findattr__("bl_space_type")!=null?type.__findattr__("bl_space_type").toString():"";
//	String bl_region_type = type.__findattr__("bl_region_type").toString();
	String bl_region_type = type.__findattr__("bl_region_type")!=null?type.__findattr__("bl_region_type").toString():"";
	if (bl_region_type.equals("WINDOW")) {
		dummypt.region_type = ScreenTypes.RGN_TYPE_WINDOW;
	}
	else {
		dummypt.region_type = ScreenTypes.RGN_TYPE_HEADER;
	}
	if (bl_space_type.equals("PROPERTIES")) {
		dummypt.space_type = SpaceTypes.SPACE_BUTS;
	}
	else if (bl_space_type.equals("VIEW_3D")) {
		dummypt.space_type = SpaceTypes.SPACE_VIEW3D;
	}
	else if (bl_space_type.equals("NODE_EDITOR")) {
		dummypt.space_type = SpaceTypes.SPACE_NODE;
	}
	else {
		System.out.println("unknown Panel space type: "+bl_space_type);
	}

	if((art=region_type_find(reports, dummypt.space_type, dummypt.region_type))==null)
		return null;
//
//	/* check if we have registered this panel type before, and remove it */
//	for(pt=art.paneltypes.first; pt; pt=pt.next) {
//		if(strcmp(pt.idname, dummypt.idname) == 0) {
//			if(pt.ext.srna)
//				rna_Panel_unregister(C, pt.ext.srna);
//			else
//				BLI_freelinkN(&art.paneltypes, pt);
//			break;
//		}
//	}

	/* create a new panel type */
	pt= new PanelType();
//	pt= (PanelType)dummypt.copy();
//	memcpy(pt, &dummypt, sizeof(dummypt));
	pt.idname = StringUtil.toCString(type.__findattr__("bl_label").toString());
	PyObject context = type.__findattr__("bl_context");
	if (context != null) {
		pt.context = StringUtil.toCString(context.toString());
	}

//	pt.ext.srna= RNA_def_struct(&BLENDER_RNA, pt.idname, "Panel");
	pt.ext.srna= RnaDefine.RNA_def_struct(RnaAccess.BLENDER_RNA, StringUtil.toJString(pt.idname,0), "Panel");
	pt.ext.data= data;
	pt.ext.call= call;
//	pt.ext.free= free;
//	RNA_struct_blender_type_set(pt.ext.srna, pt);

//	pt.poll= (have_function[0])? panel_poll: NULL;
	pt.poll= panel_poll;
//	pt.draw= (have_function[1])? panel_draw: NULL;
//	System.out.println("rna_Panel_register assigned draw");
	pt.draw= panel_draw;
//	pt.draw_header= (have_function[2])? panel_draw_header: NULL;
	pt.draw_header= panel_draw_header;

//	System.out.println("Added pt "+StringUtil.toJString(pt.idname,0)+" to art.paneltypes "+art.hashCode());
	ListBaseUtil.BLI_addtail(art.paneltypes, pt);

	/* update while blender is running */
	if(C!=null)
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);

	return pt.ext.srna;
}
public String toString() { return "rna_Panel_register"; }
};

public static StructRefineFunc rna_Panel_refine = new StructRefineFunc() {
public StructRNA refine(PointerRNA ptr)
//static StructRNA* rna_Panel_refine(PointerRNA *ptr)
{
	Panel hdr= (Panel)ptr.data;
	return (hdr.type!=null && ((PanelType)hdr.type).ext.srna!=null)? ((PanelType)hdr.type).ext.srna: new StructRNA()/*&RNA_Panel*/;
}
public String getName() { return "rna_Panel_refine"; }
};

/* Header */

public static HeaderType.Draw header_draw = new HeaderType.Draw() {
//public void run(GL2 gl, bContext C, Header hdr)
public void run(bContext C, Header hdr)
//static void header_draw(const bContext *C, Header *hdr)
{
//        System.out.println("header_draw");
	PointerRNA htr = new PointerRNA();
	ParameterList list = new ParameterList();
//	Object[] list = new Object[1];
	FunctionRNA func;
	
	//System.out.println("hdr.type.ext.srna: "+hdr.type.ext.srna);

	RnaAccess.RNA_pointer_create(bContext.CTX_wm_screen(C).id, hdr.type.ext.srna, hdr, htr);
	func= RnaAccess.RNA_struct_find_function(htr, "draw");
	
	// TMP
	//C.gl = gl;

//	RnaAccess.RNA_parameter_list_create(list, htr, func);
//	RnaAccess.RNA_parameter_set_lookup(list, "context", C);
//	list[0] = C;
	hdr.type.ext.call.run(C, htr, func, list);

//	RNA_parameter_list_free(list);
}};

public static StructUnregisterFunc rna_Header_unregister = new StructUnregisterFunc() {
public void run(bContext C, StructRNA type)
//static void rna_Header_unregister(bContext C, StructRNA type)
{
//	ARegionType art;
//	HeaderType ht= RNA_struct_blender_type_get(type);
//
//	if(ht==null)
//		return;
//	if((art=region_type_find(null, ht.space_type, ScreenTypes.RGN_TYPE_HEADER))==null)
//		return;
//
//	ListBaseUtil.BLI_freelinkN(art.headertypes, ht);
//	RNA_struct_free(&BLENDER_RNA, type);

//	/* update while blender is running */
//	if(C)
//		WM_event_add_notifier(C, NC_SCREEN|NA_EDITED, NULL);
}
public String toString() { return "rna_Header_unregister"; }
};

public static StructRegisterFunc rna_Header_register = new StructRegisterFunc() {
public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
{
//	System.out.println("rna_Header_register");
	ARegionType art;
	HeaderType ht, dummyht = new HeaderType();
	Header dummyheader= new Header();
	PointerRNA dummyhtr = new PointerRNA();
	int[] have_function = new int[1];

	/* setup dummy header & header type to store static properties in */
	dummyheader.type= dummyht;
//	RnaAccess.RNA_pointer_create(null, &RNA_Header, dummyheader, dummyhtr);
	RnaAccess.RNA_pointer_create(null, new StructRNA(), dummyheader, dummyhtr);

	/* validate the python class */
	if(validate.run(dummyhtr, data, have_function) != 0)
		return null;
	
	// TMP
	PyType type = (PyType)data;
	dummyht.space_type = SpaceTypes.SPACE_EMPTY;
	String bl_space_type = type.__findattr__("bl_space_type").toString();
	if (bl_space_type.equals("INFO")) {
		dummyht.space_type = SpaceTypes.SPACE_INFO;
	}
	else if (bl_space_type.equals("TIMELINE")) {
		dummyht.space_type = SpaceTypes.SPACE_TIME;
	}
	else if (bl_space_type.equals("OUTLINER")) {
		dummyht.space_type = SpaceTypes.SPACE_OUTLINER;
	}
	else if (bl_space_type.equals("VIEW_3D")) {
		dummyht.space_type = SpaceTypes.SPACE_VIEW3D;
	}
	else if (bl_space_type.equals("NODE_EDITOR")) {
		dummyht.space_type = SpaceTypes.SPACE_NODE;
	}
	
	if((art=region_type_find(reports, dummyht.space_type, ScreenTypes.RGN_TYPE_HEADER))==null)
		return null;

	/* check if we have registered this header type before, and remove it */
//	for(ht=art.headertypes.first; ht!=null; ht=ht.next) {
//		if(StringUtil.strcmp(ht.idname,0, dummyht.idname,0) == 0) {
//			if(ht.ext.srna!=null)
//				rna_Header_unregister(C, ht.ext.srna);
//			break;
//		}
//	}
	
	/* create a new header type */
	ht= new HeaderType();
//	memcpy(ht, &dummyht, sizeof(dummyht));

	//ht.ext.srna= RNA_def_struct(BLENDER_RNA, ht.idname, "Header");
	ht.ext.srna= new StructRNA(StringUtil.toJString(ht.idname,0));
	ht.ext.data= data;
	ht.ext.call= call;
	ht.ext.free= free;
//	RNA_struct_blender_type_set(ht.ext.srna, ht);

//	ht.draw= (have_function[0]!=0)? header_draw: null;
	ht.draw= header_draw;

//	System.out.println("adding header types");
	ListBaseUtil.BLI_addtail(art.headertypes, ht);

	/* update while blender is running */
	if(C!=null)
		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	
	return ht.ext.srna;
}
public String toString() { return "rna_Header_register"; }
};

public static StructRefineFunc rna_Header_refine = new StructRefineFunc() {
public StructRNA refine(PointerRNA htr)
//static StructRNA* rna_Header_refine(PointerRNA *htr)
{
	Header hdr= (Header)htr.data;
	return (hdr.type!=null && hdr.type.ext.srna!=null)? hdr.type.ext.srna: RnaHeader.RNA_Header;
}
public String getName() { return "rna_Header_refine"; }
};

///* Menu */
//
//static int menu_poll(const bContext *C, MenuType *pt)
//{
//	PointerRNA ptr;
//	ParameterList list;
//	FunctionRNA *func;
//	void *ret;
//	int visible;
//
//	RNA_pointer_create(NULL, pt.ext.srna, NULL, &ptr); /* dummy */
//	func= RNA_struct_find_function(&ptr, "poll");
//
//	RNA_parameter_list_create(&list, &ptr, func);
//	RNA_parameter_set_lookup(&list, "context", &C);
//	pt.ext.call(&ptr, func, &list);
//
//	RNA_parameter_get_lookup(&list, "visible", &ret);
//	visible= *(int*)ret;
//
//	RNA_parameter_list_free(&list);
//
//	return visible;
//}
//
public static MenuType.Draw menu_draw = new MenuType.Draw() {
public void run(bContext C, Menu hdr)
//static void menu_draw(const bContext *C, Menu *hdr)
{
	PointerRNA mtr = new PointerRNA();;
	ParameterList list = new ParameterList();
//	Object[] list = new Object[1];
	FunctionRNA func;

	RnaAccess.RNA_pointer_create(bContext.CTX_wm_screen(C).id, hdr.type.ext.srna, hdr, mtr);
	func= RnaAccess.RNA_struct_find_function(mtr, "draw");

//	RnaAccess.RNA_parameter_list_create(list, mtr, func);
//	RnaAccess.RNA_parameter_set_lookup(list, "context", C);
//	list[0] = C;
	hdr.type.ext.call.run(C, mtr, func, list);
//
//	RNA_parameter_list_free(&list);
}};

public static StructUnregisterFunc rna_Menu_unregister = new StructUnregisterFunc() {
public void run(bContext C, StructRNA type)
//static void rna_Menu_unregister(const bContext *C, StructRNA *type)
{
//	ARegionType *art;
//	MenuType *mt= RNA_struct_blender_type_get(type);
//
//	if(!mt)
//		return;
//	if(!(art=region_type_find(NULL, mt.space_type, RGN_TYPE_HEADER)))
//		return;
//
//	BLI_freelinkN(&art.menutypes, mt);
//	RNA_struct_free(&BLENDER_RNA, type);
//
//	/* update while blender is running */
//	if(C)
//		WM_event_add_notifier(C, NC_SCREEN|NA_EDITED, NULL);
}
//public String toString() { return "rna_Menu_unregister"; }
};

//public static StructRegisterFunc rna_Menu_register = new StructRegisterFunc() {
//public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
////static StructRNA *rna_Menu_register(const bContext *C, ReportList *reports, void *data, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
//{
////	System.out.println("rna_Menu_register");
//	ARegionType art;
//	MenuType mt, dummymt = new MenuType();
//	Menu dummymenu= new Menu();
//	PointerRNA dummymtr = new PointerRNA();
//	int[] have_function = new int[2];
//
//	/* setup dummy menu & menu type to store static properties in */
//	dummymenu.type= dummymt;
////	RnaAccess.RNA_pointer_create(null, &RNA_Menu, dummymenu, dummymtr);
//	RnaAccess.RNA_pointer_create(null, new StructRNA(), dummymenu, dummymtr);
//
//	/* validate the python class */
//	if(validate.run(dummymtr, data, have_function) != 0)
//		return null;
//	
//	// TMP
//	PyType type = (PyType)data;
//	dummymt.space_type = SpaceTypes.SPACE_EMPTY;
//	String bl_space_type = type.toString();
//	if (bl_space_type.contains("INFO_MT")) {
//		dummymt.space_type = SpaceTypes.SPACE_INFO;
//	}
//	else if (bl_space_type.contains("TIME_MT")) {
//		dummymt.space_type = SpaceTypes.SPACE_TIME;
//	}
//	else if (bl_space_type.contains("OUTLINER_MT")) {
//		dummymt.space_type = SpaceTypes.SPACE_OUTLINER;
//	}
//	else if (bl_space_type.contains("VIEW3D_MT")) {
//		dummymt.space_type = SpaceTypes.SPACE_VIEW3D;
//	}
//	else if (bl_space_type.contains("RENDER_MT")) {
//		dummymt.space_type = SpaceTypes.SPACE_BUTS;
//		dummymt.region_type = ScreenTypes.RGN_TYPE_WINDOW;
//	}
//
//	if((art=region_type_find(reports, dummymt.space_type, ScreenTypes.RGN_TYPE_HEADER))==null)
//		return null;
//	
////	System.out.println("rna_Menu_register menuType idname: " + ((PyType)data).getName());
//
////	/* check if we have registered this menu type before, and remove it */
////	for(mt=art.menutypes.first; mt; mt=mt.next) {
////		if(strcmp(mt.idname, dummymt.idname) == 0) {
////			if(mt.ext.srna)
////				rna_Menu_unregister(C, mt.ext.srna);
////			break;
////		}
////	}
//
//	/* create a new menu type */
//	mt= new MenuType();
////	memcpy(mt, &dummymt, sizeof(dummymt));
//	
//	PyObject pyData = (PyObject)data;
//	String mtStr = pyData.toString();
//	String idname = mtStr.substring(mtStr.indexOf(".")+1, mtStr.length()-2);
////	System.out.println("rna_Menu_register menuType idname: " + idname);
//	mt.idname = StringUtil.toCString(idname);
//	String label = Py.tojava(pyData.__findattr__("bl_label"), String.class);
//	mt.label = StringUtil.toCString(label);
//
////	mt.ext.srna= RNA_def_struct(&BLENDER_RNA, mt.idname, "Menu");
//	mt.ext.srna= new StructRNA(StringUtil.toJString(mt.idname,0));
//	mt.ext.data= data;
//	mt.ext.call= call;
//	mt.ext.free= free;
////	RNA_struct_blender_type_set(mt.ext.srna, mt);
////
////	mt.poll= (have_function[0])? menu_poll: NULL;
////	mt.draw= (have_function[1])? menu_draw: NULL;
//	mt.draw= menu_draw;
//
////	System.out.println("adding menu types");
//	ListBaseUtil.BLI_addtail(art.menutypes, mt);
//	Wm.WM_menutype_add(mt);
//
//	/* update while blender is running */
//	if(C!=null)
//		WmEventSystem.WM_event_add_notifier(C, WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
//
//	return mt.ext.srna;
//}
//public String toString() { return "rna_Menu_register"; }
//};
public static StructRegisterFunc rna_Menu_register = new StructRegisterFunc() {
public StructRNA run(bContext C, ReportList reports, Object data, String identifier, StructValidateFunc validate, StructCallbackFunc call, StructFreeFunc free)
{
	MenuType mt, dummymt = new MenuType();
//	Menu dummymenu= {0};
//	PointerRNA dummymtr;
//	int have_function[2];
//
//	/* setup dummy menu & menu type to store static properties in */
//	dummymenu.type= &dummymt;
//	RNA_pointer_create(NULL, &RNA_Menu, &dummymenu, &dummymtr);
//
//	/* validate the python class */
//	if(validate(&dummymtr, data, have_function) != 0)
//		return NULL;
//	
//	if(strlen(identifier) >= sizeof(dummymt.idname)) {
//		BKE_reportf(reports, RPT_ERROR, "registering menu class: '%s' is too long, maximum length is %d.", identifier, (int)sizeof(dummymt.idname));
//		return NULL;
//	}
//
//	/* check if we have registered this menu type before, and remove it */
//	mt= WM_menutype_find(dummymt.idname, TRUE);
//	if(mt && mt->ext.srna)
//		rna_Menu_unregister(C, mt->ext.srna);
	
	/* create a new menu type */
	mt= new MenuType();
//	memcpy(mt, &dummymt, sizeof(dummymt));
	
	// TMP
	mt.idname = StringUtil.toCString(((PyType)data).getName());
	mt.label = StringUtil.toCString(Py.tojava(((PyType)data).__findattr__("bl_label"), String.class));

//	mt.ext.srna= RnaDefine.RNA_def_struct(BLENDER_RNA, mt.idname, "Menu");
	mt.ext.srna= new StructRNA(StringUtil.toJString(mt.idname,0));
	mt.ext.data= data;
	mt.ext.call= call;
	mt.ext.free= free;
//	RNA_struct_blender_type_set(mt->ext.srna, mt);
//	RNA_def_struct_flag(mt->ext.srna, STRUCT_NO_IDPROPERTIES);
//
//	mt->poll= (have_function[0])? menu_poll: NULL;
//	mt->draw= (have_function[1])? menu_draw: NULL;
	mt.draw= menu_draw;

	Wm.WM_menutype_add(mt);

	/* update while blender is running */
	if(C!=null)
		WmEventSystem.WM_main_add_notifier(WmTypes.NC_SCREEN|WmTypes.NA_EDITED, null);
	
	return mt.ext.srna;
}};

public static StructRefineFunc rna_Menu_refine = new StructRefineFunc() {
public StructRNA refine(PointerRNA mtr)
//static StructRNA* rna_Menu_refine(PointerRNA *mtr)
{
	Menu hdr= (Menu)mtr.data;
	return (hdr.type!=null && hdr.type.ext.srna!=null)? hdr.type.ext.srna: new StructRNA()/*&RNA_Menu*/;
}
public String toString() { return "rna_Menu_refine"; }
};

public static boolean rna_UILayout_active_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetActive((uiLayout)ptr.data)!=0;
}

public static void rna_UILayout_active_set(PointerRNA ptr, boolean value)
{
	UILayout.uiLayoutSetActive((uiLayout)ptr.data, value?1:0);
}

public static void rna_UILayout_op_context_set(PointerRNA ptr, int value)
{
	UILayout.uiLayoutSetOperatorContext((uiLayout)ptr.data, value);
}

public static int rna_UILayout_op_context_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetOperatorContext((uiLayout)ptr.data);
}

public static boolean rna_UILayout_enabled_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetEnabled((uiLayout)ptr.data)!=0;
}

public static void rna_UILayout_enabled_set(PointerRNA ptr, boolean value)
{
	UILayout.uiLayoutSetEnabled((uiLayout)ptr.data, value?1:0);
}

//#if 0
//static int rna_UILayout_red_alert_get(PointerRNA *ptr)
//{
//	return uiLayoutGetRedAlert(ptr.data);
//}
//
//static void rna_UILayout_red_alert_set(PointerRNA *ptr, int value)
//{
//	uiLayoutSetRedAlert(ptr.data, value);
//}
//
//static int rna_UILayout_keep_aspect_get(PointerRNA *ptr)
//{
//	return uiLayoutGetKeepAspect(ptr.data);
//}
//
//static void rna_UILayout_keep_aspect_set(PointerRNA *ptr, int value)
//{
//	uiLayoutSetKeepAspect(ptr.data, value);
//}
//#endif

public static int rna_UILayout_alignment_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetAlignment((uiLayout)ptr.data);
}

public static void rna_UILayout_alignment_set(PointerRNA ptr, int value)
{
	UILayout.uiLayoutSetAlignment((uiLayout)ptr.data, value);
}

public static float rna_UILayout_scale_x_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetScaleX((uiLayout)ptr.data);
}

public static void rna_UILayout_scale_x_set(PointerRNA ptr, float value)
{
	UILayout.uiLayoutSetScaleX((uiLayout)ptr.data, value);
}

public static float rna_UILayout_scale_y_get(PointerRNA ptr)
{
	return UILayout.uiLayoutGetScaleY((uiLayout)ptr.data);
}

public static void rna_UILayout_scale_y_set(PointerRNA ptr, float value)
{
	UILayout.uiLayoutSetScaleY((uiLayout)ptr.data, value);
}

//static PointerRNA rna_UIListItem_layout_get(PointerRNA *ptr)
//{
//	uiListItem *item= (uiListItem*)ptr.data;
//	PointerRNA newptr;
//	RNA_pointer_create(NULL, &RNA_UILayout, item.layout, &newptr);
//	return newptr;
//}
//
//static PointerRNA rna_UIListItem_data_get(PointerRNA *ptr)
//{
//	uiListItem *item= (uiListItem*)ptr.data;
//	return item.data;
//}
//
//#else // RNA_RUNTIME

private static EnumPropertyItem[] alignment_items = {
	new EnumPropertyItem(UI.UI_LAYOUT_ALIGN_EXPAND, "EXPAND", 0, "Expand", ""),
	new EnumPropertyItem(UI.UI_LAYOUT_ALIGN_LEFT, "LEFT", 0, "Left", ""),
	new EnumPropertyItem(UI.UI_LAYOUT_ALIGN_CENTER, "CENTER", 0, "Center", ""),
	new EnumPropertyItem(UI.UI_LAYOUT_ALIGN_RIGHT, "RIGHT", 0, "Right", ""),
	new EnumPropertyItem(0, null, 0, null, null)};
static void rna_def_ui_layout(BlenderRNA brna)
{
//	System.out.println("rna_def_ui_layout");
	StructRNA srna;
	PropertyRNA prop;

//	static EnumPropertyItem alignment_items[] = {
//		{UI_LAYOUT_ALIGN_EXPAND, "EXPAND", 0, "Expand", ""},
//		{UI_LAYOUT_ALIGN_LEFT, "LEFT", 0, "Left", ""},
//		{UI_LAYOUT_ALIGN_CENTER, "CENTER", 0, "Center", ""},
//		{UI_LAYOUT_ALIGN_RIGHT, "RIGHT", 0, "Right", ""},
//		{0, NULL, 0, NULL, NULL}};
	
	/* layout */

	srna= RnaDefine.RNA_def_struct(brna, "UILayout", null);
	RnaDefine.RNA_def_struct_sdna(srna, "uiLayout");
	RnaDefine.RNA_def_struct_ui_text(srna, "UI Layout", "User interface layout in a panel or header");

	prop= RnaDefine.RNA_def_property(srna, "active", RNATypes.PROP_BOOLEAN, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_boolean_funcs(prop, "rna_UILayout_active_get", "rna_UILayout_active_set");
	
	prop= RnaDefine.RNA_def_property(srna, "operator_context", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_items(prop, operator_context_items);
	RnaDefine.RNA_def_property_enum_funcs(prop, "rna_UILayout_op_context_get", "rna_UILayout_op_context_set", null);
	
	prop= RnaDefine.RNA_def_property(srna, "enabled", RNATypes.PROP_BOOLEAN, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_boolean_funcs(prop, "rna_UILayout_enabled_get", "rna_UILayout_enabled_set");
	RnaDefine.RNA_def_property_ui_text(prop, "Enabled", "When false, this (sub)layout is greyed out.");
	
//#if 0
//	prop= RNA_def_property(srna, "red_alert", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_funcs(prop, "rna_UILayout_red_alert_get", "rna_UILayout_red_alert_set");
//#endif

	prop= RnaDefine.RNA_def_property(srna, "alignment", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_items(prop, alignment_items);
	RnaDefine.RNA_def_property_enum_funcs(prop, "rna_UILayout_alignment_get", "rna_UILayout_alignment_set", null);

//#if 0
//	prop= RNA_def_property(srna, "keep_aspect", PROP_BOOLEAN, PROP_NONE);
//	RNA_def_property_boolean_funcs(prop, "rna_UILayout_keep_aspect_get", "rna_UILayout_keep_aspect_set");
//#endif

	prop= RnaDefine.RNA_def_property(srna, "scale_x", RNATypes.PROP_FLOAT, RNATypes.PROP_UNSIGNED);
	RnaDefine.RNA_def_property_float_funcs(prop, "rna_UILayout_scale_x_get", "rna_UILayout_scale_x_set", null);
	RnaDefine.RNA_def_property_ui_text(prop, "Scale X", "Scale factor along the X for items in this (sub)layout.");
	
	prop= RnaDefine.RNA_def_property(srna, "scale_y", RNATypes.PROP_FLOAT, RNATypes.PROP_UNSIGNED);
	RnaDefine.RNA_def_property_float_funcs(prop, "rna_UILayout_scale_y_get", "rna_UILayout_scale_y_set", null);
	RnaDefine.RNA_def_property_ui_text(prop, "Scale Y", "Scale factor along the Y for items in this (sub)layout.");
	RnaUIApi.RNA_api_ui_layout(srna);
}

private static EnumPropertyItem[] panel_flag_items = {
	new EnumPropertyItem(ScreenTypes.PNL_DEFAULT_CLOSED, "DEFAULT_CLOSED", 0, "Default Closed", "Defines if the panel has to be open or collapsed at the time of its creation."),
	new EnumPropertyItem(ScreenTypes.PNL_NO_HEADER, "HIDE_HEADER", 0, "Show Header", "If set to True, the panel shows a header, which contains a clickable arrow to collapse the panel and the label (see bl_label)."),
	new EnumPropertyItem(0, null, 0, null, null)};
static void rna_def_panel(BlenderRNA brna)
{
//	System.out.println("rna_def_panel");
	StructRNA srna;
	PropertyRNA prop;
	PropertyRNA parm;
	FunctionRNA func;
	
//	static EnumPropertyItem panel_flag_items[] = {
//			{PNL_DEFAULT_CLOSED, "DEFAULT_CLOSED", 0, "Default Closed", "Defines if the panel has to be open or collapsed at the time of its creation."},
//			{PNL_NO_HEADER, "HIDE_HEADER", 0, "Show Header", "If set to True, the panel shows a header, which contains a clickable arrow to collapse the panel and the label (see bl_label)."},
//			{0, NULL, 0, NULL, NULL}};
	
	srna= RnaDefine.RNA_def_struct(brna, "Panel", null);
	RnaDefine.RNA_def_struct_ui_text(srna, "Panel", "Panel containing UI elements");
	RnaDefine.RNA_def_struct_sdna(srna, "Panel");
	RnaDefine.RNA_def_struct_refine_func(srna, "rna_Panel_refine");
	RnaDefine.RNA_def_struct_register_funcs(srna, "rna_Panel_register", "rna_Panel_unregister");

	/* poll */
	func= RnaDefine.RNA_def_function(srna, "poll", null);
	RnaDefine.RNA_def_function_ui_description(func, "If this method returns a non-null output, then the panel can be drawn.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_NO_SELF|RNATypes.FUNC_REGISTER|RNATypes.FUNC_REGISTER_OPTIONAL);
	RnaDefine.RNA_def_function_return(func, RnaDefine.RNA_def_boolean(func, "visible", 1, "", ""));
	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	/* draw */
	func= RnaDefine.RNA_def_function(srna, "draw", null);
	RnaDefine.RNA_def_function_ui_description(func, "Draw UI elements into the panel UI layout.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_REGISTER);
	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	func= RnaDefine.RNA_def_function(srna, "draw_header", null);
	RnaDefine.RNA_def_function_ui_description(func, "Draw UI elements into the panel's header UI layout.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_REGISTER);
	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	prop= RnaDefine.RNA_def_property(srna, "layout", RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_struct_type(prop, "UILayout");
	RnaDefine.RNA_def_property_ui_text(prop, "Layout", "Defines the structure of the panel in the UI.");
	
	prop= RnaDefine.RNA_def_property(srna, "text", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "drawname");
	RnaDefine.RNA_def_property_ui_text(prop, "Text", "XXX todo");
	
	/* registration */
	prop= RnaDefine.RNA_def_property(srna, "bl_idname", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->idname");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "ID Name", "If this is set, the panel gets a custom ID, otherwise it takes the name of the class used to define the panel. For example, if the class name is \"OBJECT_PT_hello\", and bl_idname is not set by the script, then bl_idname = \"OBJECT_PT_hello\"");
	
	prop= RnaDefine.RNA_def_property(srna, "bl_label", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->label");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "Label", "The panel label, shows up in the panel header at the right of the triangle used to collapse the panel.");
	
	prop= RnaDefine.RNA_def_property(srna, "bl_space_type", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_sdna(prop, null, "type->space_type");
	RnaDefine.RNA_def_property_enum_items(prop, RnaSpaceUtil.space_type_items);
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "Space type", "The space where the panel is going to be used in.");
	
	prop= RnaDefine.RNA_def_property(srna, "bl_region_type", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_sdna(prop, null, "type->region_type");
	RnaDefine.RNA_def_property_enum_items(prop, RnaScreenUtil.region_type_items);
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "Region Type", "The region where the panel is going to be used in.");

	prop= RnaDefine.RNA_def_property(srna, "bl_context", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->context");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER_OPTIONAL); /* should this be optional? - Campbell */
	RnaDefine.RNA_def_property_ui_text(prop, "Context", "The context in which the panel belongs to. (TODO: explain the possible combinations bl_context/bl_region_type/bl_space_type)");
	
	prop= RnaDefine.RNA_def_property(srna, "bl_options", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_sdna(prop, null, "type->flag");
	RnaDefine.RNA_def_property_enum_items(prop, panel_flag_items);
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER_OPTIONAL|RNATypes.PROP_ENUM_FLAG);
	RnaDefine.RNA_def_property_ui_text(prop, "Options",  "Options for this panel type");
}

static void rna_def_header(BlenderRNA brna)
{
//	System.out.println("rna_def_header");
	StructRNA srna;
	PropertyRNA prop;
	PropertyRNA parm;
	FunctionRNA func;
	
	srna= RnaDefine.RNA_def_struct(brna, "Header", null);
	RnaDefine.RNA_def_struct_ui_text(srna, "Header", "Editor header containing UI elements.");
	RnaDefine.RNA_def_struct_sdna(srna, "Header");
	RnaDefine.RNA_def_struct_refine_func(srna, "rna_Header_refine");
	RnaDefine.RNA_def_struct_register_funcs(srna, "rna_Header_register", "rna_Header_unregister");

	/* draw */
	func= RnaDefine.RNA_def_function(srna, "draw", null);
	RnaDefine.RNA_def_function_ui_description(func, "Draw UI elements into the header UI layout.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_REGISTER);
//	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
//	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	RnaDefine.RNA_define_verify_sdna(0); // not in sdna

	prop= RnaDefine.RNA_def_property(srna, "layout", RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_pointer_sdna(prop, null, "layout");
//	RnaDefine.RNA_def_property_struct_type(prop, "UILayout");
	RnaDefine.RNA_def_property_ui_text(prop, "Layout", "Defines the structure of the header in the UI.");

	/* registration */
	prop= RnaDefine.RNA_def_property(srna, "bl_idname", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->idname");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "ID Name", "If this is set, the header gets a custom ID, otherwise it takes the name of the class used to define the panel. For example, if the class name is \"OBJECT_HT_hello\", and bl_idname is not set by the script, then bl_idname = \"OBJECT_HT_hello\"");

	prop= RnaDefine.RNA_def_property(srna, "bl_space_type", RNATypes.PROP_ENUM, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_enum_sdna(prop, null, "type->space_type");
	RnaDefine.RNA_def_property_enum_items(prop, RnaSpaceUtil.space_type_items);
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "Space type", "The space where the header is going to be used in.");

	RnaDefine.RNA_define_verify_sdna(1);
}

static void rna_def_menu(BlenderRNA brna)
{
//	System.out.println("rna_def_menu");
	StructRNA srna;
	PropertyRNA prop;
	PropertyRNA parm;
	FunctionRNA func;
	
	srna= RnaDefine.RNA_def_struct(brna, "Menu", null);
	RnaDefine.RNA_def_struct_ui_text(srna, "Menu", "Editor menu containing buttons");
	RnaDefine.RNA_def_struct_sdna(srna, "Menu");
	RnaDefine.RNA_def_struct_refine_func(srna, "rna_Menu_refine");
	RnaDefine.RNA_def_struct_register_funcs(srna, "rna_Menu_register", "rna_Menu_unregister");

	/* poll */
	func= RnaDefine.RNA_def_function(srna, "poll", null);
	RnaDefine.RNA_def_function_ui_description(func, "If this method returns a non-null output, then the menu can be drawn.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_NO_SELF|RNATypes.FUNC_REGISTER|RNATypes.FUNC_REGISTER_OPTIONAL);
	RnaDefine.RNA_def_function_return(func, RnaDefine.RNA_def_boolean(func, "visible", 1, "", ""));
	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	/* draw */
	func= RnaDefine.RNA_def_function(srna, "draw", null);
	RnaDefine.RNA_def_function_ui_description(func, "Draw UI elements into the menu UI layout.");
	RnaDefine.RNA_def_function_flag(func, RNATypes.FUNC_REGISTER);
	parm= RnaDefine.RNA_def_pointer(func, "context", "Context", "", "");
	RnaDefine.RNA_def_property_flag(parm, RNATypes.PROP_REQUIRED);

	RnaDefine.RNA_define_verify_sdna(0); // not in sdna

	prop= RnaDefine.RNA_def_property(srna, "layout", RNATypes.PROP_POINTER, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_pointer_sdna(prop, null, "layout");
	RnaDefine.RNA_def_property_struct_type(prop, "UILayout");
	RnaDefine.RNA_def_property_ui_text(prop, "Layout", "Defines the structure of the menu in the UI.");

	/* registration */
	prop= RnaDefine.RNA_def_property(srna, "bl_idname", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->idname");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "ID Name", "If this is set, the menu gets a custom ID, otherwise it takes the name of the class used to define the panel. For example, if the class name is \"OBJECT_MT_hello\", and bl_idname is not set by the script, then bl_idname = \"OBJECT_MT_hello\"");

	prop= RnaDefine.RNA_def_property(srna, "bl_label", RNATypes.PROP_STRING, RNATypes.PROP_NONE);
	RnaDefine.RNA_def_property_string_sdna(prop, null, "type->label");
	RnaDefine.RNA_def_property_flag(prop, RNATypes.PROP_REGISTER);
	RnaDefine.RNA_def_property_ui_text(prop, "Label", "The menu label");

	RnaDefine.RNA_define_verify_sdna(1);
}

public static RNAProcess RNA_def_ui = new RNAProcess() {
public void run(BlenderRNA brna)
{
	rna_def_ui_layout(brna);
	rna_def_panel(brna);
	rna_def_header(brna);
	rna_def_menu(brna);
}};

//public static RNAProcess RNA_def_panel = new RNAProcess() {
//public void run(BlenderRNA brna)
//{
//	rna_def_panel(brna);
//}};
//	
//public static RNAProcess RNA_def_header = new RNAProcess() {
//public void run(BlenderRNA brna)
//{
//	rna_def_header(brna);
//}};
//	
//public static RNAProcess RNA_def_menu = new RNAProcess() {
//public void run(BlenderRNA brna)
//{
//	rna_def_menu(brna);
//}};

//#endif // RNA_RUNTIME
}

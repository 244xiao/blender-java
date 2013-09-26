package blender.makesrna.srna;

import static blender.editors.space_view3d.View3dHeader.uiTemplateHeader3D;
import static blender.editors.uinterface.UILayout.uiItemL;
import static blender.editors.uinterface.UILayout.uiItemM;
import static blender.editors.uinterface.UILayout.uiItemMenuEnumO;
import static blender.editors.uinterface.UILayout.uiItemPointerR;
import static blender.editors.uinterface.UILayout.uiItemS;
import static blender.editors.uinterface.UILayout.uiLayoutColumn;
import static blender.editors.uinterface.UILayout.uiLayoutIntrospect;
import static blender.editors.uinterface.UILayout.uiLayoutRow;
import static blender.editors.uinterface.UILayout.uiLayoutSplit;
import static blender.editors.uinterface.UITemplates.uiTemplateHeader;
import static blender.editors.uinterface.UITemplates.uiTemplateID;
import static blender.editors.uinterface.UITemplates.uiTemplateList;
import static blender.editors.uinterface.UITemplates.uiTemplateReportsBanner;
import static blender.editors.uinterface.UITemplates.uiTemplateRunningJobs;
import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaDefine.*;
import static blender.makesrna.RnaRna.*;
import static blender.makesrna.RnaUI.*;
import static blender.makesrna.RnaUIApi.rna_uiItemO;
import static blender.makesrna.RnaUIApi.rna_uiItemR;

import org.python.core.Py;
import org.python.core.PyObject;

import blender.blenkernel.bContext;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.sdna.ReportList;
import blender.makesrna.RNATypes.CallFunc;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.ParameterList;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FloatPropertyRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropBooleanGetFunc;
import blender.makesrna.rna_internal_types.PropBooleanSetFunc;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropEnumGetFunc;
import blender.makesrna.rna_internal_types.PropEnumSetFunc;
import blender.makesrna.rna_internal_types.PropFloatGetFunc;
import blender.makesrna.rna_internal_types.PropFloatSetFunc;
import blender.makesrna.rna_internal_types.PropPointerGetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StringPropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.python.BpyUtil;

public class RnaUILayout extends RnaStruct {
	
	public RnaUILayout(PointerRNA ptr) {
		super(ptr);
	}
	
	public PointerRNA getRna_type() {
		return UILayout_rna_type_get.getPtr(ptr);
	}
	
	public boolean getActive() {
		return UILayout_active_get.getBool(ptr);
	}
	
	public void setActive(boolean value) {
		UILayout_active_set.setBool(ptr, value);
	}
	
	public String getOperator_context() {
		int value = UILayout_operator_context_get.getEnum(ptr);
		for (EnumPropertyItem item : rna_UILayout_operator_context_items) {
			if (value == item.value) {
				return item.identifier;
			}
		}
		return rna_UILayout_operator_context_items[0].identifier;
	}
	
	public void setOperator_context(String value) {
		for (EnumPropertyItem item : rna_UILayout_operator_context_items) {
			if (value.equals(item.identifier)) {
				UILayout_operator_context_set.setEnum(ptr, item.value); break;
			}
		}
	}
	
	public boolean getEnabled() {
		return UILayout_enabled_get.getBool(ptr);
	}
	
	public void setEnabled(boolean value) {
		UILayout_enabled_set.setBool(ptr, value);
	}
	
	public String getAlignment() {
		int value = UILayout_alignment_get.getEnum(ptr);
		for (EnumPropertyItem item : rna_UILayout_alignment_items) {
			if (value == item.value) {
				return item.identifier;
			}
		}
		return rna_UILayout_alignment_items[0].identifier;
	}
	
	public void setAlignment(String value) {
		for (EnumPropertyItem item : rna_UILayout_alignment_items) {
			if (value.equals(item.identifier)) {
				UILayout_alignment_set.setEnum(ptr, item.value); break;
			}
		}
	}
	
	public float getScale_x() {
		return UILayout_scale_x_get.getFloat(ptr);
	}
	
	public void setScale_x(float value) {
		UILayout_scale_x_set.setFloat(ptr, value);
	}
	
	public float getScale_y() {
		return UILayout_scale_y_get.getFloat(ptr);
	}
	
	public void setScale_y(float value) {
		UILayout_scale_y_set.setFloat(ptr, value);
	}
	
	// Sub-layout. Items placed in this sublayout are placed next to each other in a row.
	public RnaUILayout row(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		boolean align = false;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("layout")) _self = Py.tojava(values[i+0], uiLayout.class);
			else if (names[i].equals("align")) align = Py.py2boolean(values[i+0]);
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[2];
		parms[1] = (Integer)(align?1:0);
		_parms.data = parms;
		
		UILayout_row_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
		return new RnaUILayout(new PointerRNA(null, (uiLayout)parms[0]));
	}
	
	// Sub-layout. Items placed in this sublayout are placed under each other in a column.
	public RnaUILayout column(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		boolean align = false;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("layout")) _self = Py.tojava(values[i+0], uiLayout.class);
			else if (names[i].equals("align")) align = Py.py2boolean(values[i+0]);
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[2];
		parms[1] = (Integer)(align?1:0);
		_parms.data = parms;
		
		UILayout_column_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
		return new RnaUILayout(new PointerRNA(null, (uiLayout)parms[0]));
	}
	
	public RnaUILayout split(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		float percentage = 0.0f;
		boolean align = false;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("layout")) _self = Py.tojava(values[i+0], uiLayout.class);
			else if (names[i].equals("percentage")) percentage = Py.py2float(values[i+0]);
			else if (names[i].equals("align")) align = Py.py2boolean(values[i+0]);
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[3];
		parms[1] = (Float)(percentage);
		parms[2] = (Integer)(align?1:0);
		_parms.data = parms;
		
		UILayout_split_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
		return new RnaUILayout(new PointerRNA(null, (uiLayout)parms[0]));
	}
	
	// Item. Exposes an RNA item and places it into the layout.
	public void prop(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		PyObject data = values[0];
		String property = values[1].toString();
		String text = null;
		int icon = 0;
		boolean expand = false;
		boolean slider = false;
		boolean toggle = false;
		boolean icon_only = false;
		boolean event = false;
		boolean full_event = false;
		boolean emboss = true;
		int index = -1;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("text")) text = values[i+2].toString();
			else if (names[i].equals("icon")) icon = getIcon(values[i+2].toString());
			else if (names[i].equals("expand")) expand = Py.py2boolean(values[i+2]);
			else if (names[i].equals("slider")) slider = Py.py2boolean(values[i+2]);
			else if (names[i].equals("toggle")) toggle = Py.py2boolean(values[i+2]);
			else if (names[i].equals("icon_only")) icon_only = Py.py2boolean(values[i+2]);
			else if (names[i].equals("event")) event = Py.py2boolean(values[i+2]);
			else if (names[i].equals("full_event")) full_event = Py.py2boolean(values[i+2]);
			else if (names[i].equals("emboss")) emboss = Py.py2boolean(values[i+2]);
			else if (names[i].equals("index")) index = values[i+2].asInt();
		}
		
		RnaStruct struct = Py.tojava(data, RnaStruct.class);
		if (struct == null) {
			System.out.println("null structure: "+data);
		}
		PointerRNA ptr;
//		if (struct instanceof RnaScene) {
//			ptr = new PointerRNA(RnaScene.RNA_Scene, struct);
//		}
//		else if (struct instanceof RnaToolSettings) {
//			ptr = new PointerRNA(RnaToolSettings.RNA_ToolSettings, struct);
//		}
//		else if (struct instanceof RnaSpaceOutliner) {
//			ptr = new PointerRNA(RnaSpaceOutliner.RNA_SpaceOutliner, struct);
//		}
//		else if (struct instanceof RnaSpaceTimeline) {
//			ptr = new PointerRNA(RnaSpaceTimeline.RNA_SpaceTimeline, struct);
//		}
//		else if (struct instanceof RnaSpaceNodeEditor) {
//			ptr = new PointerRNA(RnaSpaceNodeEditor.RNA_SpaceNodeEditor, struct);
//		}
//		else if (struct instanceof RnaSpaceView3D) {
//			ptr = new PointerRNA(RnaSpaceView3D.RNA_SpaceView3D, struct);
//		}
////		else if (struct instanceof RnaRenderSettings) {
//////			ptr = new PointerRNA(RnaRenderSettings.RNA_RenderSettings, struct);
////			ptr = struct.ptr;
////		}
//		else {
//			ptr = new PointerRNA(new StructRNA(), struct);
//			System.out.println("prop: unknown struct: "+struct.getClass());
//		}
		ptr = struct.ptr;
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[12];
		parms[0] = ptr;
		parms[1] = property;
		parms[2] = text;
		parms[3] = icon;
		parms[4] = expand?1:0;
		parms[5] = slider?1:0;
		parms[6] = toggle?1:0;
		parms[7] = icon_only?1:0;
		parms[8] = event?1:0;
		parms[9] = full_event?1:0;
		parms[10] = emboss?1:0;
		parms[11] = index;
		_parms.data = parms;
		
		UILayout_prop_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// prop_search
	public void prop_search(PyObject[] values, String[] names) {
//		String name = null;
//		int icon = 0;
//		int expand = 0;
//		int slider = 0;
//		int toggle = 0;
//		int icon_only = 0;
//		int event = 0;
//		int full_event = 0;
//		int emboss = 1;
//		int index = -1;
//		PointerRNA ptr = null;//(PointerRNA)values[0];
//		String propname = values[1].toString();
//		for (int i=0; i<names.length; i++) {
//			if (names[i].equals("text")) name = values[i+2].toString();
//			else if (names[i].equals("icon")) icon = getIcon(values[i+2].toString());
//			else if (names[i].equals("expand")) expand = values[i+2].asInt();
//			else if (names[i].equals("slider")) slider = values[i+2].asInt();
//			else if (names[i].equals("toggle")) toggle = values[i+2].asInt();
//			else if (names[i].equals("icon_only")) icon_only = values[i+2].asInt();
//			else if (names[i].equals("event")) event = values[i+2].asInt();
//			else if (names[i].equals("full_event")) full_event = values[i+2].asInt();
//			else if (names[i].equals("emboss")) emboss = values[i+2].asInt();
//			else if (names[i].equals("index")) index = values[i+2].asInt();
//		}
////		System.out.println("RNA_UILayout: prop_search");
//		uiLayout _self = this.layout;
//		
//		ReportList reports = new ReportList();
//		PointerRNA _ptr = new PointerRNA();
//		ParameterList _parms = new ParameterList();
//		
//		_ptr.data = _self;
//		Object[] parms = new Object[12];
//		parms[0] = ptr;
//
//		_parms.data = parms;
//		
//		UILayout_prop_search_call.call(bpy_util.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	public void prop_enum(PyObject[] values, String[] names) {
		
	}
	
	// Item. Places a button into the layout to call an Operator.
	public RnaOperatorProperties operator(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		String operator = values[0].toString();
		String text = "";
		int icon = 0;
		boolean emboss = true;
		Object properties = null;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("text")) text = values[i+1].toString();
			else if (names[i].equals("icon")) icon = getIcon(values[i+1].toString());
			else if (names[i].equals("emboss")) emboss = Py.py2boolean(values[i+1]);
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[5];
		parms[0] = operator;
		parms[1] = text;
		parms[2] = icon;
		parms[3] = emboss?1:0;
		_parms.data = parms;
		
		UILayout_operator_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
		return new RnaOperatorProperties((PointerRNA)parms[4]);
	}
	
	// operator_menu_enum
	public void operator_menu_enum(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		String operator = values[0].toString();
		String property = values[1].toString();
		String text = "";
		int icon = 0;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("text")) text = values[i+2].toString();
			else if (names[i].equals("icon")) icon = getIcon(values[i+2].toString());
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[4];
		parms[0] = operator;
		parms[1] = property;
		parms[2] = text;
		parms[3] = icon;
		_parms.data = parms;
		
		UILayout_operator_menu_enum_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// Item. Display text in the layout.
	public void label(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		String text = "";
		int icon = 0;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("text")) text = values[i+0].toString();
			else if (names[i].equals("icon")) icon = getIcon(values[i+0].toString());
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[2];
		parms[0] = text;
		parms[1] = icon;
		_parms.data = parms;
		
		UILayout_label_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// menu
	public void menu(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		String menu = values[0].toString();
		String text = null;
		int icon = 0;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("text")) text = values[i+1].toString();
			else if (names[i].equals("icon")) icon = getIcon(values[i+1].toString());
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[3];
		parms[0] = menu;
		parms[1] = text;
		parms[2] = icon;
		_parms.data = parms;
		
		UILayout_menu_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// Item. Inserts empty space into the layout between items.
	public void separator(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[0];
		_parms.data = parms;
		
		UILayout_separator_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// template_header
	public void template_header(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		boolean menus = true;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("menus")) menus = Py.py2boolean(values[i+1]);
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[1];
		parms[0] = menus?1:0;
		_parms.data = parms;
		
		UILayout_template_header_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// template_ID
	public void template_ID(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		PointerRNA data = null;
		if (values[0].getType().getName().equals("RnaWindow")) {
			data = new PointerRNA(RnaWindow.RNA_Window, Py.tojava(values[0], RnaWindow.class));
		}
		else if (values[0].getType().getName().equals("RnaScreen")) {
			data = new PointerRNA(RnaScreen.RNA_Screen, Py.tojava(values[0], RnaScreen.class));
		}
		else {
			System.out.println("template_ID unknown data: "+values[0].getType().getName());
		}
		String property = values[1].toString();
		String _new = null;
		String open = null;
		String unlink = null;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("new")) _new = values[i+2].toString();
			else if (names[i].equals("open")) open = values[i+2].toString();
			else if (names[i].equals("unlink")) unlink = values[i+2].toString();
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[5];
		parms[0] = data;
		parms[1] = property;
		parms[2] = _new;
		parms[3] = open;
		parms[4] = unlink;
		_parms.data = parms;
		
		UILayout_template_ID_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	private static EnumPropertyItem[] rna_UILayout_template_list_type_items = {
		new EnumPropertyItem(0, "DEFAULT", 0, "None", ""),
		new EnumPropertyItem(99, "COMPACT", 0, "Compact", ""),
		new EnumPropertyItem(105, "ICONS", 0, "Icons", ""),
		new EnumPropertyItem(0, null, 0, null, null)
	};
	
	public void template_list(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		PointerRNA data = null;
		if (values[0].getType().getName().equals("RNA_Window")) {
			data = new PointerRNA(RnaWindow.RNA_Window, Py.tojava(values[0], RnaWindow.class));
		}
		else if (values[0].getType().getName().equals("RNA_Screen")) {
			data = new PointerRNA(RnaScreen.RNA_Screen, Py.tojava(values[0], RnaScreen.class));
		}
		else if (values[0].getType().getName().equals("RNA_Scene")) {
			data = new PointerRNA(RnaScene.RNA_Scene, Py.tojava(values[0], RnaScene.class));
		}
		String property = values[1].toString();
		Object active_data = values[2];
		String active_property = values[3].toString();
		int rows = 5;
		int maxrows = 5;
//		String type = "DEFAULT";
		String type = rna_UILayout_template_list_type_items[0].identifier;
		
		for (int i=0; i<names.length; i++) {
			if (names[i].equals("rows")) rows = Py.py2int(values[i+4]);
			else if (names[i].equals("maxrows")) maxrows = Py.py2int(values[i+4]);
			else if (names[i].equals("type")) type = values[i+4].toString();
		}
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[7];
		parms[0] = data;
		parms[1] = property;
//		parms[2] = active_data;
		parms[2] = new PointerRNA();
		parms[3] = active_property;
		parms[4] = rows;
		parms[5] = maxrows;
		// TMP
		int typeOrdinal = 0;
		for (EnumPropertyItem item : rna_UILayout_template_list_type_items) {
			if (item.identifier.equals(type)) {
				typeOrdinal = item.value;
				break;
			}
		}
		parms[6] = typeOrdinal;
//		parms[6] = type;
		_parms.data = parms;
		
		UILayout_template_list_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// template_running_jobs
	public void template_running_jobs(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[0];
		_parms.data = parms;
		
		UILayout_template_running_jobs_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	public void template_header_3D(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[0];
		_parms.data = parms;
		
		UILayout_template_header_3D_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	// template_reports_banner
	public void template_reports_banner(PyObject[] values, String[] names) {
		uiLayout _self = (uiLayout)ptr.data;
		
		ReportList reports = new ReportList();
		PointerRNA _ptr = new PointerRNA();
		ParameterList _parms = new ParameterList();
		
		_ptr.data = _self;
		Object[] parms = new Object[0];
		_parms.data = parms;
		
		UILayout_template_reports_banner_call.call(BpyUtil.BPy_GetContext(), reports, _ptr, _parms);
	}
	
	public void operator_enums(PyObject[] values, String[] names) {
		
	}
	
	private int getIcon(String iconName) {
		int icon = 0;
		try {
			icon = BIFIconID.valueOf("ICON_"+iconName).ordinal();
		} catch(Exception ex) {
//			System.out.println("no icon found: "+iconName);
		}
		return icon;
	}
	
	//////////////////////////////////
	
	private static PropCollectionGetFunc UILayout_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc UILayout_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{

//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_UILayout_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= UILayout_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc UILayout_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= UILayout_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc UILayout_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};

	private static PropPointerGetFunc UILayout_rna_type_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		return rna_builtin_type_get.getPtr(ptr);
	}};
	
	private static PropBooleanGetFunc UILayout_active_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
	{
		return rna_UILayout_active_get(ptr);
	}};

	private static PropBooleanSetFunc UILayout_active_set = new PropBooleanSetFunc() {
	public void setBool(PointerRNA ptr, boolean value)
	{
		rna_UILayout_active_set(ptr, value);
	}};

	private static PropEnumGetFunc UILayout_operator_context_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
	{
		return rna_UILayout_op_context_get(ptr);
	}};

	private static PropEnumSetFunc UILayout_operator_context_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
	{
		rna_UILayout_op_context_set(ptr, value);
	}};

	private static PropBooleanGetFunc UILayout_enabled_get = new PropBooleanGetFunc() {
	public boolean getBool(PointerRNA ptr)
	{
		return rna_UILayout_enabled_get(ptr);
	}};

	private static PropBooleanSetFunc UILayout_enabled_set = new PropBooleanSetFunc() {
	public void setBool(PointerRNA ptr, boolean value)
	{
		rna_UILayout_enabled_set(ptr, value);
	}};

	private static PropEnumGetFunc UILayout_alignment_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
	{
		return rna_UILayout_alignment_get(ptr);
	}};

	private static PropEnumSetFunc UILayout_alignment_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
	{
		rna_UILayout_alignment_set(ptr, value);
	}};

	private static PropFloatGetFunc UILayout_scale_x_get = new PropFloatGetFunc() {
	public float getFloat(PointerRNA ptr)
	{
		return rna_UILayout_scale_x_get(ptr);
	}};

	private static PropFloatSetFunc UILayout_scale_x_set = new PropFloatSetFunc() {
	public void setFloat(PointerRNA ptr, float value)
	{
		rna_UILayout_scale_x_set(ptr, value);
	}};
	
	private static PropFloatGetFunc UILayout_scale_y_get = new PropFloatGetFunc() {
	public float getFloat(PointerRNA ptr)
	{
		return rna_UILayout_scale_y_get(ptr);
	}};

	private static PropFloatSetFunc UILayout_scale_y_set = new PropFloatSetFunc() {
	public void setFloat(PointerRNA ptr, float value)
	{
		rna_UILayout_scale_y_set(ptr, value);
	}};
	
	private static CallFunc UILayout_row_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		uiLayout layout;
		int align;
		Object[] _data, _retdata;
		int _data_p=0, _retdata_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		_retdata= _data;
		_retdata_p = _data_p;
		_data_p++;
		align= ((Integer)_data[_data_p]);
		
		layout= uiLayoutRow(_self, align);
		_retdata[_retdata_p]= layout;
	}};
	
	private static CallFunc UILayout_column_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		uiLayout layout;
		int align;
		Object[] _data, _retdata;
		int _data_p=0, _retdata_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		_retdata= _data;
		_retdata_p = _data_p;
		_data_p++;
		align= ((Integer)_data[_data_p]);
		
		layout= uiLayoutColumn(_self, align);
		_retdata[_retdata_p]= layout;
	}};
	
	private static CallFunc UILayout_split_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		uiLayout layout;
		float percentage;
		int align;
		Object[] _data, _retdata;
		int _data_p=0, _retdata_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		_retdata= _data;
		_retdata_p = _data_p;
		_data_p++;
		percentage= ((Float)_data[_data_p]);
		_data_p++;
		align= ((Integer)_data[_data_p]);
		
		layout= uiLayoutSplit(_self, percentage, align);
		_retdata[_retdata_p]= layout;
	}};
	
	private static CallFunc UILayout_prop_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		PointerRNA data;
		String property;
		String text;
		int icon;
		int expand;
		int slider;
		int toggle;
		int icon_only;
		int event;
		int full_event;
		int emboss;
		int index;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		property= ((String)_data[_data_p]);
		_data_p++;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		_data_p++;
		expand= ((Integer)_data[_data_p]);
		_data_p++;
		slider= ((Integer)_data[_data_p]);
		_data_p++;
		toggle= ((Integer)_data[_data_p]);
		_data_p++;
		icon_only= ((Integer)_data[_data_p]);
		_data_p++;
		event= ((Integer)_data[_data_p]);
		_data_p++;
		full_event= ((Integer)_data[_data_p]);
		_data_p++;
		emboss= ((Integer)_data[_data_p]);
		_data_p++;
		index= ((Integer)_data[_data_p]);
		
		rna_uiItemR(_self, data, property, text, icon, expand, slider, toggle, icon_only, event, full_event, emboss, index);
	}};
	
	private static CallFunc UILayout_prop_search_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		PointerRNA data;
		String property;
		PointerRNA search_data;
		String search_property;
		String text;
		int icon;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		property= ((String)_data[_data_p]);
		_data_p++;
		search_data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		search_property= ((String)_data[_data_p]);
		_data_p++;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		
		uiItemPointerR(_self, data, property, search_data, search_property, text, icon);
	}};
	
	private static CallFunc UILayout_operator_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		String operator;
		String text;
		int icon;
		int emboss;
		PointerRNA properties;
		Object[] _data, _retdata;
		int _data_p=0, _retdata_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		operator= ((String)_data[_data_p]);
		_data_p++;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		_data_p++;
		emboss= ((Integer)_data[_data_p]);
		_data_p++;
		_retdata= _data;
		_retdata_p = _data_p;
		
		properties= rna_uiItemO(_self, operator, text, icon, emboss);
		_retdata[_retdata_p]= properties;
	}};
	
	private static CallFunc UILayout_operator_menu_enum_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		String operator;
		String property;
		String text;
		int icon;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		operator= ((String)_data[_data_p]);
		_data_p++;
		property= ((String)_data[_data_p]);
		_data_p++;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		
		uiItemMenuEnumO(_self, operator, property, text, icon);
	}};
	
	private static CallFunc UILayout_label_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		String text;
		int icon;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		
		uiItemL(_self, text, icon);
	}};
	
	private static CallFunc UILayout_menu_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		String menu;
		String text;
		int icon;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		menu= ((String)_data[_data_p]);
		_data_p++;
		text= ((String)_data[_data_p]);
		_data_p++;
		icon= ((Integer)_data[_data_p]);
		
		uiItemM(_self, C, menu, text, icon);
	}};
	
	private static CallFunc UILayout_separator_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		
		uiItemS(_self);
	}};
	
	private static CallFunc UILayout_template_header_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		int menus;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		menus= ((Integer)_data[_data_p]);
		
		uiTemplateHeader(_self, C, menus);
	}};
	
	private static CallFunc UILayout_template_ID_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		PointerRNA data;
		String property;
		String _new;
		String open;
		String unlink;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		property= ((String)_data[_data_p]);
		_data_p++;
		_new= ((String)_data[_data_p]);
		_data_p++;
		open= ((String)_data[_data_p]);
		_data_p++;
		unlink= ((String)_data[_data_p]);
		
		uiTemplateID(_self, C, data, property, _new, open, unlink);
	}};
	
	private static CallFunc UILayout_template_list_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		PointerRNA data;
		String property;
		PointerRNA active_data;
		String active_property;
		int rows;
		int maxrows;
		int type;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		property= ((String)_data[_data_p]);
		_data_p++;
		active_data= ((PointerRNA)_data[_data_p]);
		_data_p++;
		active_property= ((String)_data[_data_p]);
		_data_p++;
		rows= ((Integer)_data[_data_p]);
		_data_p++;
		maxrows= ((Integer)_data[_data_p]);
		_data_p++;
		type= ((Integer)_data[_data_p]);
		
		uiTemplateList(_self, C, data, property, active_data, active_property, rows, maxrows, type);
	}};
	
	private static CallFunc UILayout_template_running_jobs_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		
		uiTemplateRunningJobs(_self, C);
	}};
	
	private static CallFunc UILayout_template_header_3D_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		
		uiTemplateHeader3D(_self, C);
	}};
	
	private static CallFunc UILayout_template_reports_banner_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		Object[] _data;
		int _data_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		
		uiTemplateReportsBanner(_self, C);
	}};
	
	private static CallFunc UILayout_introspect_call = new CallFunc() {
	public void call(bContext C, ReportList reports, PointerRNA _ptr, ParameterList _parms)
	{
		uiLayout _self;
		String string;
		Object[] _data, _retdata;
		int _data_p=0, _retdata_p=0;
		
		_self= (uiLayout)_ptr.data;
		_data= (Object[])_parms.data;
		_retdata= _data;
		_retdata_p = _data_p;
		
		string= uiLayoutIntrospect(_self);
		_retdata[_retdata_p]= string;
	}};
	
	/* UI Layout */
	private static CollectionPropertyRNA rna_UILayout_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_rna_properties_begin, UILayout_rna_properties_next, UILayout_rna_properties_end, UILayout_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);

	private static PointerPropertyRNA rna_UILayout_rna_type = new PointerPropertyRNA(
		null, rna_UILayout_rna_properties,
		-1, "rna_type", 524288, "RNA",
		"RNA type definition",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_rna_type_get, null, null, null, RNA_Struct
	);
	
	private static BooleanPropertyRNA rna_UILayout_active = new BooleanPropertyRNA(
		null, rna_UILayout_rna_type,
		-1, "active", 3, "active",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_active_get, UILayout_active_set, null, null, false, null
	);
	
	private static EnumPropertyItem[] rna_UILayout_operator_context_items = {
		new EnumPropertyItem(0, "INVOKE_DEFAULT", 0, "Invoke Default", ""),
		new EnumPropertyItem(1, "INVOKE_REGION_WIN", 0, "Invoke Region Window", ""),
		new EnumPropertyItem(2, "INVOKE_REGION_CHANNELS", 0, "Invoke Region Channels", ""),
		new EnumPropertyItem(3, "INVOKE_REGION_PREVIEW", 0, "Invoke Region Preview", ""),
		new EnumPropertyItem(4, "INVOKE_AREA", 0, "Invoke Area", ""),
		new EnumPropertyItem(5, "INVOKE_SCREEN", 0, "Invoke Screen", ""),
		new EnumPropertyItem(6, "EXEC_DEFAULT", 0, "Exec Default", ""),
		new EnumPropertyItem(7, "EXEC_REGION_WIN", 0, "Exec Region Window", ""),
		new EnumPropertyItem(8, "EXEC_REGION_CHANNELS", 0, "Exec Region Channels", ""),
		new EnumPropertyItem(9, "EXEC_REGION_PREVIEW", 0, "Exec Region Preview", ""),
		new EnumPropertyItem(10, "EXEC_AREA", 0, "Exec Area", ""),
		new EnumPropertyItem(11, "EXEC_SCREEN", 0, "Exec Screen", ""),
		new EnumPropertyItem(0, null, 0, null, null)
	};

	private static EnumPropertyRNA rna_UILayout_operator_context = new EnumPropertyRNA(
		null, rna_UILayout_active,
		-1, "operator_context", 3, "operator_context",
		"",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_operator_context_get, UILayout_operator_context_set, null, rna_UILayout_operator_context_items, 12, 0
	);
	
	private static BooleanPropertyRNA rna_UILayout_enabled = new BooleanPropertyRNA(
		null, rna_UILayout_operator_context,
		-1, "enabled", 3, "Enabled",
		"When false, this (sub)layout is greyed out.",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_enabled_get, UILayout_enabled_set, null, null, false, null
	);

	private static EnumPropertyItem[] rna_UILayout_alignment_items = {
		new EnumPropertyItem(0, "EXPAND", 0, "Expand", ""),
		new EnumPropertyItem(1, "LEFT", 0, "Left", ""),
		new EnumPropertyItem(2, "CENTER", 0, "Center", ""),
		new EnumPropertyItem(3, "RIGHT", 0, "Right", ""),
		new EnumPropertyItem(0, null, 0, null, null)
	};

	private static EnumPropertyRNA rna_UILayout_alignment = new EnumPropertyRNA(
		null, rna_UILayout_enabled,
		-1, "alignment", 3, "alignment",
		"",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_alignment_get, UILayout_alignment_set, null, rna_UILayout_alignment_items, 4, 0
	);

	private static FloatPropertyRNA rna_UILayout_scale_x = new FloatPropertyRNA(
		null, rna_UILayout_alignment,
		-1, "scale_x", 3, "Scale X",
		"Scale factor along the X for items in this (sub)layout.",
		0,
		PROP_FLOAT, PROP_UNSIGNED|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_scale_x_get, UILayout_scale_x_set, null, null, null, 0.0f, 10000.0f, 0.0f, FLT_MAX, 10.0f, 3, 0.0f, null
	);
	
	private static FloatPropertyRNA rna_UILayout_scale_y = new FloatPropertyRNA(
		null, rna_UILayout_rna_type,
		-1, "scale_y", 3, "Scale Y",
		"Scale factor along the Y for items in this (sub)layout.",
		0,
		PROP_FLOAT, PROP_UNSIGNED|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		UILayout_scale_y_get, UILayout_scale_y_set, null, null, null, 0.0f, 10000.0f, 0.0f, FLT_MAX, 10.0f, 3, 0.0f, null
	);
	
	private static PointerPropertyRNA rna_UILayout_row_layout = new PointerPropertyRNA(
		null, null,
		-1, "layout", 8, "",
		"Sub-layout to put items in.",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, null
	);

	private static BooleanPropertyRNA rna_UILayout_row_align = new BooleanPropertyRNA(
		null, rna_UILayout_row_layout,
		-1, "align", 3, "",
		"Align buttons to each other.",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);

	private static FunctionRNA rna_UILayout_row_func = new FunctionRNA(
		null, null,
			null,
		rna_UILayout_row_layout, rna_UILayout_row_align,
		"row", 0, "Sub-layout. Items placed in this sublayout are placed next to each other in a row.",
		UILayout_row_call,
		rna_UILayout_row_layout
	);
	
	private static StringPropertyRNA rna_UILayout_introspect_string = new StringPropertyRNA(
		null, null,
		-1, "string", 9, "Descr",
		"DESCR",
		0,
		PROP_STRING, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, 1048576, ""
	);

	private static FunctionRNA rna_UILayout_introspect_func = new FunctionRNA(
		null, rna_UILayout_row_func,
			null,
		rna_UILayout_introspect_string, rna_UILayout_introspect_string,
		"introspect", 0, "introspect",
		UILayout_introspect_call,
		rna_UILayout_introspect_string
	);
	
	public static StructRNA RNA_UILayout = new StructRNA(
		RNA_Panel, RnaSpaceInfo.RNA_SpaceInfo,
		null,
		rna_UILayout_rna_properties, rna_UILayout_scale_y,
		null,null,
		"UILayout", 0, "UI Layout", "User interface layout in a panel or header",
		17,
		null, rna_UILayout_rna_properties,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		rna_UILayout_row_func, rna_UILayout_introspect_func
	);
	
	static {
		((PropertyRNA)rna_UILayout_rna_properties).next = rna_UILayout_rna_type;
		((PropertyRNA)rna_UILayout_rna_type).next = rna_UILayout_active;
		((PropertyRNA)rna_UILayout_active).next = rna_UILayout_operator_context;
		((PropertyRNA)rna_UILayout_operator_context).next = rna_UILayout_enabled;
		((PropertyRNA)rna_UILayout_enabled).next = rna_UILayout_alignment;
		((PropertyRNA)rna_UILayout_alignment).next = rna_UILayout_scale_x;
		((PropertyRNA)rna_UILayout_scale_x).next = rna_UILayout_scale_y;
		rna_UILayout_row_layout.type = RNA_UILayout;
	}

}

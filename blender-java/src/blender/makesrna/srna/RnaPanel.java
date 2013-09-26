package blender.makesrna.srna;

import org.python.core.PyObject;

import blender.blenkernel.ScreenUtil.PanelType;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.UILayout.uiLayout;
import blender.makesdna.sdna.Panel;
import blender.makesrna.RnaUI;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.python.BpyUtil;
import blender.python.BpyRna.BPy_StructRNA;

public class RnaPanel extends RnaStruct {
	
	public static BPy_StructRNA __rna__ = new BPy_StructRNA(new StructRNA(), new StructRNA(RnaUI.rna_Panel_register));
	
//	public PointerRNA ptr;
	
	public RnaPanel(PointerRNA ptr) {
		super(ptr);
	}
	
	public Object getRna_type() {
//		return rna_builtin_type_get(ptr);
		return null;
	}
	
	public RnaUILayout getLayout() {
		Panel data= (Panel)(ptr.data);
//		return rna_pointer_inherit_refine(ptr, &RNA_UILayout, &data.layout);
//		return new RNA_UILayout((uiLayout)data.layout, bpy_util.BPy_GetContext());
		return new RnaUILayout(new PointerRNA(null, data.layout));
	}
	
	public String getText() {
		Panel data= (Panel)(ptr.data);
//		StringUtil.BLI_strncpy(value, data.drawname, data.drawname.length);
		return StringUtil.toJString(data.drawname,0);
	}
	
	public void setText(String value) {
		Panel data= (Panel)(ptr.data);
		StringUtil.BLI_strncpy(data.drawname,0, StringUtil.toCString(value),0, data.drawname.length);
	}
	
	public String getBl_idname() {
		Panel data= (Panel)(ptr.data);
//		StringUtil.BLI_strncpy(value, ((PanelType)data.type).idname, ((PanelType)data.type).idname.length);
		return StringUtil.toJString(((PanelType)data.type).idname,0);
	}
	
	public void setBl_idname(String value) {
		Panel data= (Panel)(ptr.data);
		StringUtil.BLI_strncpy(((PanelType)data.type).idname,0, StringUtil.toCString(value),0, ((PanelType)data.type).idname.length);
	}
	
	public String getBl_label() {
		Panel data= (Panel)(ptr.data);
//		StringUtil.BLI_strncpy(value, ((PanelType)data.type).label, ((PanelType)data.type).label.length);
		return StringUtil.toJString(((PanelType)data.type).label,0);
	}
	
	public void setBl_label(String value) {
		Panel data= (Panel)(ptr.data);
		StringUtil.BLI_strncpy(((PanelType)data.type).label,0, StringUtil.toCString(value),0, ((PanelType)data.type).label.length);
	}
	
	public String getBl_space_type() {
		Panel data= (Panel)(ptr.data);
		return rna_Panel_bl_space_type_items.values()[((PanelType)data.type).space_type].name();
	}
	
	public void setBl_space_type(String value) {
		Panel data= (Panel)(ptr.data);
		((PanelType)data.type).space_type= rna_Panel_bl_space_type_items.valueOf(value).ordinal();
	}
	
	public String getBl_region_type() {
		Panel data= (Panel)(ptr.data);
		return rna_Panel_bl_region_type_items.values()[((PanelType)data.type).region_type].name();
	}
	
	public void setBl_region_type(String value) {
		Panel data= (Panel)(ptr.data);
		((PanelType)data.type).region_type= rna_Panel_bl_region_type_items.valueOf(value).ordinal();
	}
	
	public String getBl_context() {
		Panel data= (Panel)(ptr.data);
//		StringUtil.BLI_strncpy(value, ((PanelType)data.type).context, ((PanelType)data.type).context.length);
		return StringUtil.toJString(((PanelType)data.type).context,0);
	}
	
	public void setBl_context(String value) {
		Panel data= (Panel)(ptr.data);
		StringUtil.BLI_strncpy(((PanelType)data.type).context,0, StringUtil.toCString(value),0, ((PanelType)data.type).context.length);
	}
	
	public String getBl_options() {
		Panel data= (Panel)(ptr.data);
		return rna_Panel_bl_options_items.values()[((PanelType)data.type).flag].name();
	}
	
	public void setBl_options(String value) {
		Panel data= (Panel)(ptr.data);
		((PanelType)data.type).flag= rna_Panel_bl_options_items.valueOf(value).ordinal();
	}
	
	private static enum rna_Panel_bl_space_type_items {
		EMPTY,
		VIEW_3D,
		GRAPH_EDITOR,
		OUTLINER,
		PROPERTIES,
		FILE_BROWSER,
		IMAGE_EDITOR,
		INFO,
		SEQUENCE_EDITOR,
		TEXT_EDITOR,
		BLANK_01,
		AUDIO_WINDOW,
		DOPESHEET_EDITOR,
		NLA_EDITOR,
		SCRIPTS_WINDOW,
		TIMELINE,
		NODE_EDITOR,
		LOGIC_EDITOR,
		CONSOLE,
		USER_PREFERENCES,
	};
	
	private static enum rna_Panel_bl_region_type_items {
		WINDOW,
		HEADER,
		CHANNELS,
		TEMPORARY,
		UI,
		TOOLS,
		TOOL_PROPS,
		PREVIEW,
	};
	
	private static enum rna_Panel_bl_options_items {
		BLANK_01,
		DEFAULT_CLOSED,
		HIDE_HEADER,
	};
	
//	// If this method returns a non-null output, then the panel can be drawn.
//	public boolean poll(PyObject[] values, String[] names) {
//		// 
//		 boolean visible = true;
//		// 
//		/*required*/ Object context = null;
//		
//		return false;
//	}
//	
//	// Draw UI elements into the panel UI layout.
//	public void draw(PyObject[] values, String[] names) {
//		// 
//		/*required*/ Object context = null;
//	}
	
//	// Draw UI elements into the panel's header UI layout.
//	public void draw_header(PyObject[] values, String[] names) {
//		// 
//		/*required*/ Object context = null;
//	}
//	public void draw_header(PyObject[] values) {
//		// 
//		/*required*/ Object context = null;
//	}

}

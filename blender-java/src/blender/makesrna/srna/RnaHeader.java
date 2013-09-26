package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaRna.*;
import static blender.makesrna.RnaUI.*;

import org.python.core.PyObject;

import blender.blenkernel.ScreenUtil.Header;
import blender.blenlib.StringUtil;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropEnumGetFunc;
import blender.makesrna.rna_internal_types.PropEnumSetFunc;
import blender.makesrna.rna_internal_types.PropPointerGetFunc;
import blender.makesrna.rna_internal_types.PropStringGetFunc;
import blender.makesrna.rna_internal_types.PropStringLengthFunc;
import blender.makesrna.rna_internal_types.PropStringSetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StringPropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.python.BpyRna.BPy_StructRNA;

public class RnaHeader extends RnaStruct {
	
	public RnaHeader(PointerRNA ptr) {
		super(ptr);
	}
	
	public Object getRna_type() {
		return Header_rna_type_get.getPtr(ptr);
	}
	
	public RnaUILayout getLayout() {
		return new RnaUILayout(Header_layout_get.getPtr(ptr));
	}
	
	public String getBl_idname() {
		byte[] buf = new byte[1024];
		Header_bl_idname_get.getStr(ptr, buf,0);
		return StringUtil.toJString(buf,0);
	}
	
	public void setBl_idname(String value) {
		Header_bl_idname_set.setStr(ptr, StringUtil.toCString(value),0);
	}
	
	public String getBl_space_type() {
		int value = Header_bl_space_type_get.getEnum(ptr);
		for (EnumPropertyItem item : rna_Header_bl_space_type_items) {
			if (value == item.value) {
				return item.identifier;
			}
		}
		return rna_Header_bl_space_type_items[0].identifier;
	}
	
	public void setBl_space_type(String value) {
		for (EnumPropertyItem item : rna_Header_bl_space_type_items) {
			if (value.equals(item.identifier)) {
				Header_bl_space_type_set.setEnum(ptr, item.value); break;
			}
		}
	}
	
	// Draw UI elements into the header UI layout.
	public void draw(PyObject[] values, String[] names) {
		
	}
	
	///////////////////////
	
	private static PropCollectionGetFunc Header_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc Header_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{

//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_Header_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= Header_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc Header_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= Header_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc Header_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	private static PropPointerGetFunc Header_rna_type_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		return rna_builtin_type_get.getPtr(ptr);
	}};

	private static PropPointerGetFunc Header_layout_get = new PropPointerGetFunc() {
	public PointerRNA getPtr(PointerRNA ptr)
	{
		Header data= (Header)(ptr.data);
		return rna_pointer_inherit_refine(ptr, new StructRNA(), data.layout);
	}};
	
	private static PropStringGetFunc Header_bl_idname_get = new PropStringGetFunc() {
	public void getStr(PointerRNA ptr, byte[] value, int offset)
	{
		Header data= (Header)(ptr.data);
		StringUtil.BLI_strncpy(value,offset, data.type.idname,0, data.type.idname.length);
	}};

	private static PropStringLengthFunc Header_bl_idname_length = new PropStringLengthFunc() {
	public int lenStr(PointerRNA ptr)
	{
		Header data= (Header)(ptr.data);
		return StringUtil.strlen(data.type.idname,0);
	}};

	private static PropStringSetFunc Header_bl_idname_set = new PropStringSetFunc() {
	public void setStr(PointerRNA ptr, byte[] value, int offset)
	{
		Header data= (Header)(ptr.data);
		StringUtil.BLI_strncpy(data.type.idname,0, value,offset, data.type.idname.length);
	}};

	private static PropEnumGetFunc Header_bl_space_type_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
	{
		Header data= (Header)(ptr.data);
		return (int)(data.type.space_type);
	}};

	private static PropEnumSetFunc Header_bl_space_type_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
	{
		Header data= (Header)(ptr.data);
		data.type.space_type= value;
	}};
	
	/* Header */
	private static CollectionPropertyRNA rna_Header_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Header_rna_properties_begin, Header_rna_properties_next, Header_rna_properties_end, Header_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);

	private static PointerPropertyRNA rna_Header_rna_type = new PointerPropertyRNA(
		null, rna_Header_rna_properties,
		-1, "rna_type", 524288, "RNA",
		"RNA type definition",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Header_rna_type_get, null, null, null, RNA_Struct
	);

	private static PointerPropertyRNA rna_Header_layout = new PointerPropertyRNA(
		null, rna_Header_rna_type,
		-1, "layout", 0, "Layout",
		"Defines the structure of the header in the UI.",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Header_layout_get, null, null, null,null
	);

	private static StringPropertyRNA rna_Header_bl_idname = new StringPropertyRNA(
		null, rna_Header_layout,
		-1, "bl_idname", 17, "ID Name",
		"If this is set, the header gets a custom ID, otherwise it takes the name of the class used to define the panel. For example, if the class name is \"OBJECT_HT_hello\", and bl_idname is not set by the script, then bl_idname = \"OBJECT_HT_hello\"",
		0,
		PROP_STRING, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Header_bl_idname_get, Header_bl_idname_length, Header_bl_idname_set, 0, ""
	);

	private static EnumPropertyItem[] rna_Header_bl_space_type_items = {
		new EnumPropertyItem(0, "EMPTY", 0, "Empty", ""),
		new EnumPropertyItem(1, "VIEW_3D", 0, "3D View", ""),
		new EnumPropertyItem(2, "GRAPH_EDITOR", 0, "Graph Editor", ""),
		new EnumPropertyItem(3, "OUTLINER", 0, "Outliner", ""),
		new EnumPropertyItem(4, "PROPERTIES", 0, "Properties", ""),
		new EnumPropertyItem(5, "FILE_BROWSER", 0, "File Browser", ""),
		new EnumPropertyItem(6, "IMAGE_EDITOR", 0, "Image Editor", ""),
		new EnumPropertyItem(7, "INFO", 0, "Info", ""),
		new EnumPropertyItem(8, "SEQUENCE_EDITOR", 0, "Sequence Editor", ""),
		new EnumPropertyItem(9, "TEXT_EDITOR", 0, "Text Editor", ""),
		new EnumPropertyItem(11, "AUDIO_WINDOW", 0, "Audio Window", ""),
		new EnumPropertyItem(12, "DOPESHEET_EDITOR", 0, "DopeSheet Editor", ""),
		new EnumPropertyItem(13, "NLA_EDITOR", 0, "NLA Editor", ""),
		new EnumPropertyItem(14, "SCRIPTS_WINDOW", 0, "Scripts Window", ""),
		new EnumPropertyItem(15, "TIMELINE", 0, "Timeline", ""),
		new EnumPropertyItem(16, "NODE_EDITOR", 0, "Node Editor", ""),
		new EnumPropertyItem(17, "LOGIC_EDITOR", 0, "Logic Editor", ""),
		new EnumPropertyItem(18, "CONSOLE", 0, "Python Console", ""),
		new EnumPropertyItem(19, "USER_PREFERENCES", 0, "User Preferences", ""),
		new EnumPropertyItem(0, null, 0, null, null)
	};

	private static EnumPropertyRNA rna_Header_bl_space_type = new EnumPropertyRNA(
		null, rna_Header_bl_idname,
		-1, "bl_space_type", 19, "Space type",
		"The space where the header is going to be used in.",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Header_bl_space_type_get, Header_bl_space_type_set, null, rna_Header_bl_space_type_items, 19, 0
	);
	
	private static FunctionRNA rna_Header_draw_func = new FunctionRNA(
		null, null,
		null,
		null, null,
		"draw", 8, "Draw UI elements into the header UI layout.",
		null,
		null
	);
	
	public static StructRNA RNA_Header = new StructRNA(
		RnaMenu.RNA_Menu, RNA_Panel,
		null,
		rna_Header_rna_properties, rna_Header_bl_space_type,
		null,null,
		"Header", 0, "Header", "Editor header containing UI elements.",
		17,
		null, rna_Header_rna_properties,
		null,
		null,
		rna_Header_refine,
		null,
		rna_Header_register,
		rna_Header_unregister,
		null,
		rna_Header_draw_func, rna_Header_draw_func
	);
	
	static {
		((PropertyRNA)rna_Header_rna_properties).next = rna_Header_rna_type;
		((PropertyRNA)rna_Header_rna_type).next = rna_Header_layout;
		((PropertyRNA)rna_Header_layout).next = rna_Header_bl_idname;
		((PropertyRNA)rna_Header_bl_idname).next = rna_Header_bl_space_type;
	}
	
	public static BPy_StructRNA __rna__ = new BPy_StructRNA(new StructRNA(), RNA_Header);

}

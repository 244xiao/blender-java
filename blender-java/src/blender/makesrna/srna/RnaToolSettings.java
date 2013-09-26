package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaRna.*;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaToolSettings extends RnaStruct {
	
	public RnaToolSettings(PointerRNA ptr) {
		super(ptr);
	}
	
	public boolean getUse_keyframe_insert_auto() {
		return false;
	}
	
	public String getSnap_element() {
		return rna_ToolSettings_snap_element_items[0].identifier;
	}
	
//	@Override
//	public PropertyRNA getProperty(String identifier) {
//		if (identifier.equals("use_keyframe_insert_auto")) {
//			return rna_ToolSettings_use_keyframe_insert_auto;
//		}
//		else if (identifier.equals("snap_element")) {
//			return rna_ToolSettings_snap_element;
//		}
//		else if (identifier.equals("use_snap")) {
//			return rna_ToolSettings_use_snap;
//		}
//		return null;
//	}
	
	private static PropCollectionGetFunc ToolSettings_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc ToolSettings_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_ToolSettings_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= ToolSettings_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc ToolSettings_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= ToolSettings_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc ToolSettings_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	/* Tool Settings */
	private static CollectionPropertyRNA rna_ToolSettings_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		ToolSettings_rna_properties_begin, ToolSettings_rna_properties_next, ToolSettings_rna_properties_end, ToolSettings_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);
	
	private static BooleanPropertyRNA rna_ToolSettings_use_snap = new BooleanPropertyRNA(
		null, null,
		-1, "use_snap", 4099, "Snap",
		"Snap during transform",
		420,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 68091904, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	static EnumPropertyItem[] rna_ToolSettings_snap_element_items = {
		new EnumPropertyItem(0, "INCREMENT", 423, "Increment", "Snap to increments of grid"),
		new EnumPropertyItem(1, "VERTEX", 424, "Vertex", "Snap to vertices"),
		new EnumPropertyItem(2, "EDGE", 425, "Edge", "Snap to edges"),
		new EnumPropertyItem(3, "FACE", 426, "Face", "Snap to faces"),
		new EnumPropertyItem(4, "VOLUME", 427, "Volume", "Snap to volume"),
		new EnumPropertyItem(0, null, 0, null, null)
	};
	
	private static EnumPropertyRNA rna_ToolSettings_snap_element = new EnumPropertyRNA(
			null, null,
		-1, "snap_element", 3, "Snap Element",
		"Type of element to snap to",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 68091904, null, null,
		0, -1, null,
		null, null, null, rna_ToolSettings_snap_element_items, 5, 0
	);
	
	private static BooleanPropertyRNA rna_ToolSettings_use_keyframe_insert_auto = new BooleanPropertyRNA(
		null, null,
		-1, "use_keyframe_insert_auto", 3, "Auto Keying",
		"Automatic keyframe insertion for Objects and Bones",
		338,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	private static BooleanPropertyRNA rna_ToolSettings_sculpt_paint_use_unified_strength = new BooleanPropertyRNA(
		null, rna_ToolSettings_use_keyframe_insert_auto,
		-1, "sculpt_paint_use_unified_strength", 3, "Sculpt/Paint Use Unified Strength",
		"Instead of per brush strength, the strength is shared across brushes",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
//	public static StructRNA RNA_ToolSettings = new StructRNA();
	
	public static StructRNA RNA_ToolSettings = new StructRNA(
//		RNA_Screen, RNA_Scene,
			null, null,
			null,
		(PropertyRNA)rna_ToolSettings_rna_properties, (PropertyRNA)rna_ToolSettings_sculpt_paint_use_unified_strength,
		null,null,
		"ToolSettings", 0, "Tool Settings", "",
		17,
		null, (PropertyRNA)rna_ToolSettings_rna_properties,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null, null
	);
	
	static {
		((PropertyRNA)rna_ToolSettings_rna_properties).next = rna_ToolSettings_use_snap;
		((PropertyRNA)rna_ToolSettings_use_snap).next = rna_ToolSettings_snap_element;
		((PropertyRNA)rna_ToolSettings_snap_element).next = rna_ToolSettings_use_keyframe_insert_auto;
		((PropertyRNA)rna_ToolSettings_use_keyframe_insert_auto).next = rna_ToolSettings_sculpt_paint_use_unified_strength;
	}

}

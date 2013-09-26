package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaRna.*;
import blender.makesrna.RnaSpaceUtil;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StringPropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaSpaceOutliner extends RnaSpace {

	public String display_mode = "";
	public String filter_test = "";
	public boolean use_filter_case_sensitive = false;
	public boolean use_filter_complete = false;
	public boolean show_restrict_columns = false;
	
	public RnaSpaceOutliner(PointerRNA ptr) {
		super(ptr);
	}
	
//	@Override
//	public PropertyRNA getProperty(String identifier) {
//		if (identifier.equals("display_mode")) {
//			return rna_SpaceOutliner_display_mode;
//		}
//		else if (identifier.equals("filter_text")) {
//			return rna_SpaceOutliner_filter_text;
//		}
//		else if (identifier.equals("use_filter_case_sensitive")) {
//			return rna_SpaceOutliner_use_filter_case_sensitive;
//		}
//		else if (identifier.equals("use_filter_complete")) {
//			return rna_SpaceOutliner_use_filter_complete;
//		}
//		else if (identifier.equals("show_restrict_columns")) {
//			return rna_SpaceOutliner_show_restrict_columns;
//		}
//		return super.getProperty(identifier);
//	}
	
	private static PropCollectionGetFunc SpaceOutliner_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc SpaceOutliner_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_SpaceOutliner_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= SpaceOutliner_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc SpaceOutliner_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= SpaceOutliner_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc SpaceOutliner_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	/* Space Outliner */
	private static CollectionPropertyRNA rna_SpaceOutliner_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		SpaceOutliner_rna_properties_begin, SpaceOutliner_rna_properties_next, SpaceOutliner_rna_properties_end, SpaceOutliner_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);
	
	private static EnumPropertyRNA rna_SpaceOutliner_display_mode = new EnumPropertyRNA(
		null, rna_SpaceOutliner_rna_properties,
		-1, "display_mode", 3, "Display Mode",
		"Type of information to display",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252182528, null, null,
		0, -1, null,
		null, null, null, RnaSpaceUtil.display_mode_items, 12, 0
	);
	
	private static StringPropertyRNA rna_SpaceOutliner_filter_text = new StringPropertyRNA(
		null, rna_SpaceOutliner_display_mode,
		-1, "filter_text", 1, "Display Filter",
		"Live search filtering string",
		0,
		PROP_STRING, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252182528, null, null,
		0, -1, null,
		null, null, null, 0, ""
	);
	
	private static BooleanPropertyRNA rna_SpaceOutliner_use_filter_case_sensitive = new BooleanPropertyRNA(
		null, rna_SpaceOutliner_filter_text,
		-1, "use_filter_case_sensitive", 3, "Case Sensitive Matches Only",
		"Only use case sensitive matches of search string",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252182528, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	private static BooleanPropertyRNA rna_SpaceOutliner_use_filter_complete = new BooleanPropertyRNA(
		null, rna_SpaceOutliner_use_filter_case_sensitive,
		-1, "use_filter_complete", 3, "Complete Matches Only",
		"Only use complete matches of search string",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252182528, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	private static BooleanPropertyRNA rna_SpaceOutliner_show_restrict_columns = new BooleanPropertyRNA(
		null, rna_SpaceOutliner_use_filter_complete,
		-1, "show_restrict_columns", 3, "Show Restriction Columns",
		"Show column",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252182528, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	public static StructRNA RNA_SpaceOutliner = new StructRNA(
//		RNA_SpaceView3D, RNA_Space,
			null, null,
			null,
		(PropertyRNA)rna_SpaceOutliner_rna_properties, (PropertyRNA)rna_SpaceOutliner_show_restrict_columns,
		null,null,
		"SpaceOutliner", 0, "Space Outliner", "Outliner space data",
		17,
		null, (PropertyRNA)rna_SpaceOutliner_rna_properties,
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
		((PropertyRNA)rna_SpaceOutliner_rna_properties).next = rna_SpaceOutliner_display_mode;
		((PropertyRNA)rna_SpaceOutliner_display_mode).next = rna_SpaceOutliner_filter_text;
		((PropertyRNA)rna_SpaceOutliner_filter_text).next = rna_SpaceOutliner_use_filter_case_sensitive;
		((PropertyRNA)rna_SpaceOutliner_use_filter_case_sensitive).next = rna_SpaceOutliner_use_filter_complete;
		((PropertyRNA)rna_SpaceOutliner_use_filter_complete).next = rna_SpaceOutliner_show_restrict_columns;
	}
	
}

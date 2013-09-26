package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaRna.*;
import blender.blenkernel.bContext;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.rna_internal_types.UpdateFunc;

public class RnaSpaceTimeline extends RnaSpace {
	
	public boolean show_frame_indicator = false;
	public boolean show_only_selected = false;
	public boolean use_play_top_left_3d_editor = false;
	public boolean use_play_3d_editors = false;
	public boolean use_play_animation_editors = false;
	public boolean use_play_properties_editors = false;
	public boolean use_play_image_editors = false;
	public boolean use_play_sequence_editors = false;
	public boolean use_play_node_editors = false;
	public boolean show_cache = false;
	
	public RnaSpaceTimeline(PointerRNA ptr) {
		super(ptr);
	}
	
//	@Override
//	public PropertyRNA getProperty(String identifier) {
//		if (identifier.equals("show_frame_indicator")) {
//			return rna_SpaceTimeline_show_frame_indicator;
//		}
//		else if (identifier.equals("show_only_selected")) {
//			return rna_SpaceTimeline_show_only_selected;
//		}
//		else if (identifier.equals("use_play_top_left_3d_editor")) {
//			return rna_SpaceTimeline_use_play_top_left_3d_editor;
//		}
//		else if (identifier.equals("use_play_3d_editors")) {
//			return rna_SpaceTimeline_use_play_3d_editors;
//		}
//		else if (identifier.equals("use_play_animation_editors")) {
//			return rna_SpaceTimeline_use_play_animation_editors;
//		}
//		else if (identifier.equals("use_play_properties_editors")) {
//			return rna_SpaceTimeline_use_play_properties_editors;
//		}
//		else if (identifier.equals("use_play_image_editors")) {
//			return rna_SpaceTimeline_use_play_image_editors;
//		}
//		else if (identifier.equals("use_play_sequence_editors")) {
//			return rna_SpaceTimeline_use_play_sequence_editors;
//		}
//		else if (identifier.equals("use_play_node_editors")) {
//			return rna_SpaceTimeline_use_play_node_editors;
//		}
//		return super.getProperty(identifier);
//	}
	
	public static UpdateFunc rna_SpaceTime_redraw_update = new UpdateFunc() {
    public void update(bContext C, PointerRNA ptr)
	{

	}
    public String getName() { return "rna_SpaceTime_redraw_update"; }
	};
	
	private static PropCollectionGetFunc SpaceTimeline_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc SpaceTimeline_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_SpaceTimeline_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= SpaceTimeline_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc SpaceTimeline_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= SpaceTimeline_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc SpaceTimeline_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	/* Space Timeline Editor */
	private static CollectionPropertyRNA rna_SpaceTimeline_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		SpaceTimeline_rna_properties_begin, SpaceTimeline_rna_properties_next, SpaceTimeline_rna_properties_end, SpaceTimeline_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);
		
	private static BooleanPropertyRNA rna_SpaceTimeline_show_frame_indicator = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_rna_properties,
		-1, "show_frame_indicator", 3, "Show Frame Number Indicator",
		"Show frame number beside the current frame indicator line",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_show_only_selected = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_show_frame_indicator,
		-1, "show_only_selected", 3, "Only Selected channels",
		"Show keyframes for active Object and/or its selected channels only",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_top_left_3d_editor = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_show_only_selected,
		-1, "use_play_top_left_3d_editor", 3, "Top-Left 3D Editor",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_3d_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_top_left_3d_editor,
		-1, "use_play_3d_editors", 3, "All 3D View Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_animation_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_3d_editors,
		-1, "use_play_animation_editors", 3, "Animation Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_properties_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_animation_editors,
		-1, "use_play_properties_editors", 3, "Property Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_image_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_properties_editors,
		-1, "use_play_image_editors", 3, "Image Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_sequence_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_image_editors,
		-1, "use_play_sequence_editors", 3, "Sequencer Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	private static BooleanPropertyRNA rna_SpaceTimeline_use_play_node_editors = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_sequence_editors,
		-1, "use_play_node_editors", 3, "Node Editors",
		"",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		rna_SpaceTime_redraw_update, 252444672, null, null,
		0, -1, null,
		null, null, null, null, false, null
	);
	
	private static BooleanPropertyRNA rna_SpaceTimeline_cache_smoke = new BooleanPropertyRNA(
		null, rna_SpaceTimeline_use_play_node_editors,
		-1, "cache_smoke", 3, "Smoke",
		"Show the active object\'s smoke cache",
		0,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252444672, null, null,
		0, -1, null,
//		SpaceTimeline_cache_smoke_get, SpaceTimeline_cache_smoke_set, null, null, false, null
		null, null, null, null, false, null
	);
	
	public static StructRNA RNA_SpaceTimeline = new StructRNA(
//		RNA_SpaceInfo, RNA_SpaceProperties,
			null, null,
			null,
		(PropertyRNA)rna_SpaceTimeline_rna_properties, (PropertyRNA)rna_SpaceTimeline_cache_smoke,
		null,null,
		"SpaceTimeline", 0, "Space Timeline Editor", "Timeline editor space data",
		17,
		null, (PropertyRNA)rna_SpaceTimeline_rna_properties,
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
		((PropertyRNA)rna_SpaceTimeline_rna_properties).next = rna_SpaceTimeline_show_frame_indicator;
		((PropertyRNA)rna_SpaceTimeline_show_frame_indicator).next = rna_SpaceTimeline_show_only_selected;
		((PropertyRNA)rna_SpaceTimeline_show_only_selected).next = rna_SpaceTimeline_use_play_top_left_3d_editor;
		((PropertyRNA)rna_SpaceTimeline_use_play_top_left_3d_editor).next = rna_SpaceTimeline_use_play_3d_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_3d_editors).next = rna_SpaceTimeline_use_play_animation_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_animation_editors).next = rna_SpaceTimeline_use_play_properties_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_properties_editors).next = rna_SpaceTimeline_use_play_image_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_image_editors).next = rna_SpaceTimeline_use_play_sequence_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_sequence_editors).next = rna_SpaceTimeline_use_play_node_editors;
		((PropertyRNA)rna_SpaceTimeline_use_play_node_editors).next = rna_SpaceTimeline_cache_smoke;
		
	}

}

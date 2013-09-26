package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.rna_iterator_listbase_end;
import static blender.makesrna.RnaRna.rna_builtin_properties_begin;
import static blender.makesrna.RnaRna.rna_builtin_properties_get;
import static blender.makesrna.RnaRna.rna_builtin_properties_lookup_string;
import static blender.makesrna.RnaRna.rna_builtin_properties_next;
import blender.makesdna.sdna.View3D;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropEnumGetFunc;
import blender.makesrna.rna_internal_types.PropEnumSetFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaSpaceView3D extends RnaSpace {
	
	public RnaSpaceView3D(PointerRNA ptr) {
		super(ptr);
	}
	
//	@Override
//	public PropertyRNA getProperty(String identifier) {
//		if (identifier.equals("viewport_shade")) {
//			return rna_SpaceView3D_viewport_shade;
//		}
//		return super.getProperty(identifier);
//	}
	
	private static PropCollectionGetFunc SpaceView3D_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc SpaceView3D_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_SpaceView3D_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= SpaceView3D_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc SpaceView3D_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= SpaceView3D_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc SpaceView3D_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	private static PropEnumGetFunc SpaceView3D_pivot_point_get = new PropEnumGetFunc() {
	public int getEnum(PointerRNA ptr)
	{
		View3D data= (View3D)(ptr.data);
		return (int)(data.around);
	}};
	
	private static PropEnumSetFunc SpaceView3D_pivot_point_set = new PropEnumSetFunc() {
	public void setEnum(PointerRNA ptr, int value)
	{
		View3D data= (View3D)(ptr.data);
		data.around= (short)value;
	}};
	
	/* 3D View Space */
	private static CollectionPropertyRNA rna_SpaceView3D_rna_properties = new CollectionPropertyRNA(
			null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		SpaceView3D_rna_properties_begin, SpaceView3D_rna_properties_next, SpaceView3D_rna_properties_end, SpaceView3D_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);
	
	private static EnumPropertyItem[] rna_SpaceView3D_viewport_shade_items = {
		new EnumPropertyItem(1, "BOUNDBOX", 468, "Bounding Box", "Display the object\'s local bounding boxes only"),
		new EnumPropertyItem(2, "WIREFRAME", 469, "Wireframe", "Display the object as wire edges"),
		new EnumPropertyItem(3, "SOLID", 470, "Solid", "Display the object solid, lit with default OpenGL lights"),
		new EnumPropertyItem(5, "TEXTURED", 472, "Textured", "Display the object solid, with face-assigned textures"),
		new EnumPropertyItem(0, null, 0, null, null)
	};
	
	private static EnumPropertyRNA rna_SpaceView3D_viewport_shade = new EnumPropertyRNA(
		null, rna_SpaceView3D_rna_properties,
		-1, "viewport_shade", 3, "Viewport Shading",
		"Method to display/shade objects in the 3D View",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
//			SpaceView3D_viewport_shade_get, SpaceView3D_viewport_shade_set, null, rna_SpaceView3D_viewport_shade_items, 4, 1
		null, null, null, rna_SpaceView3D_viewport_shade_items, 4, 1
	);
	
	private static EnumPropertyItem[] rna_SpaceView3D_pivot_point_items = {
		new EnumPropertyItem(0, "BOUNDING_BOX_CENTER", 395, "Bounding Box Center", ""),
		new EnumPropertyItem(1, "CURSOR", 396, "3D Cursor", ""),
		new EnumPropertyItem(2, "INDIVIDUAL_ORIGINS", 397, "Individual Origins", ""),
		new EnumPropertyItem(3, "MEDIAN_POINT", 398, "Median Point", ""),
		new EnumPropertyItem(4, "ACTIVE_ELEMENT", 399, "Active Element", ""),
		new EnumPropertyItem(0, null, 0, null, null)
	};
	
	private static EnumPropertyRNA rna_SpaceView3D_pivot_point = new EnumPropertyRNA(
		null, rna_SpaceView3D_viewport_shade,
		-1, "pivot_point", 3, "Pivot Point",
		"Pivot center for rotation/scaling",
		0,
		PROP_ENUM, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
		SpaceView3D_pivot_point_get, SpaceView3D_pivot_point_set, null, rna_SpaceView3D_pivot_point_items, 5, 0
	);
	
	private static BooleanPropertyRNA rna_SpaceView3D_use_pivot_point_align = new BooleanPropertyRNA(
		null, rna_SpaceView3D_pivot_point,
		-1, "use_pivot_point_align", 3, "Align",
		"Manipulate object centers only",
		400,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
//		SpaceView3D_use_pivot_point_align_get, SpaceView3D_use_pivot_point_align_set, null, null, false, null
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_SpaceView3D_show_manipulator = new BooleanPropertyRNA(
			null, rna_SpaceView3D_use_pivot_point_align,
		-1, "show_manipulator", 3, "Manipulator",
		"Use a 3D manipulator widget for controlling transforms",
		419,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
//		SpaceView3D_show_manipulator_get, SpaceView3D_show_manipulator_set, null, null, false, null
		null, null, null, null, false, null
	);
	
	private static BooleanPropertyRNA rna_SpaceView3D_lock_camera_and_layers = new BooleanPropertyRNA(
		null, rna_SpaceView3D_show_manipulator,
		-1, "lock_camera_and_layers", 4099, "Lock Camera and Layers",
		"Use the scene\'s active camera and layers in this view, rather than local layers",
		477,
		PROP_BOOLEAN, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
//		SpaceView3D_lock_camera_and_layers_get, SpaceView3D_lock_camera_and_layers_set, null, null, false, null
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_SpaceView3D_layers = new BooleanPropertyRNA(
			null, rna_SpaceView3D_lock_camera_and_layers,
		-1, "layers", 3, "Visible Layers",
		"Layers visible in this 3D View",
		0,
		PROP_BOOLEAN, PROP_LAYER_MEMBER|PROP_UNIT_NONE, null, 0, new int[]{20, 0, 0}, 0,
		null, 252248064, null, null,
		0, -1, null,
//		SpaceView3D_layers_get, SpaceView3D_layers_set, null, rna_SpaceView3D_layer_set, false, null
		null, null, null, null, false, null
	);

	private static BooleanPropertyRNA rna_SpaceView3D_layers_used = new BooleanPropertyRNA(
			null, rna_SpaceView3D_layers,
		-1, "layers_used", 2, "Used Layers",
		"Layers that contain something",
		0,
		PROP_BOOLEAN, PROP_LAYER_MEMBER|PROP_UNIT_NONE, null, 0, new int[]{20, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
//		SpaceView3D_layers_used_get, null, null, null, false, null
		null, null, null, null, false, null
	);
	
	private static PointerPropertyRNA rna_SpaceView3D_region_quadview = new PointerPropertyRNA(
		null, rna_SpaceView3D_layers_used,
		-1, "region_quadview", 0, "Quad View Region",
		"3D region that defines the quad view settings",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
//		SpaceView3D_region_quadview_get, null, null, null,RNA_RegionView3D.RNA_RegionView3D
		null, null, null, null,new StructRNA()
	);
	
	public static StructRNA RNA_SpaceView3D = new StructRNA(
//		RNA_RegionView3D, RNA_SpaceOutliner,
			null, null,
			null,
		rna_SpaceView3D_rna_properties, rna_SpaceView3D_region_quadview,
		null,null,
		"SpaceView3D", 0, "3D View Space", "3D View space data",
		17,
		null, (PropertyRNA)rna_SpaceView3D_rna_properties,
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
		((PropertyRNA)rna_SpaceView3D_rna_properties).next = rna_SpaceView3D_viewport_shade;
		((PropertyRNA)rna_SpaceView3D_viewport_shade).next = rna_SpaceView3D_pivot_point;
		((PropertyRNA)rna_SpaceView3D_pivot_point).next = rna_SpaceView3D_use_pivot_point_align;
		((PropertyRNA)rna_SpaceView3D_use_pivot_point_align).next = rna_SpaceView3D_show_manipulator;
		((PropertyRNA)rna_SpaceView3D_show_manipulator).next = rna_SpaceView3D_lock_camera_and_layers;
		((PropertyRNA)rna_SpaceView3D_lock_camera_and_layers).next = rna_SpaceView3D_layers;
		((PropertyRNA)rna_SpaceView3D_layers).next = rna_SpaceView3D_layers_used;
		((PropertyRNA)rna_SpaceView3D_layers_used).next = rna_SpaceView3D_region_quadview;
	}

}

package blender.makesrna.srna;

import static blender.makesrna.RNATypes.*;
import static blender.makesrna.RnaAccess.*;
import static blender.makesrna.RnaRna.*;
import static blender.makesrna.RnaWm.rna_Window_screen_set;
import blender.makesdna.sdna.wmWindow;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionBeginFunc;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaWindow extends RnaStruct {
	
	public RnaWindow(PointerRNA ptr) {
		super(ptr);
	}
	
//	@Override
//	public PropertyRNA getProperty(String identifier) {
//		if (identifier.equals("screen")) {
//			return rna_Window_screen;
//		}
//		return super.getProperty(identifier);
//	}
	
	public RnaScreen getScreen() {
		wmWindow data= (wmWindow)(ptr.data);
//		return rna_pointer_inherit_refine(ptr, &RNA_Screen, &data.screen);
		return new RnaScreen(new PointerRNA(RnaScreen.RNA_Screen, data.screen));
	}
	
	public void setScreen(RnaScreen value) {
		rna_Window_screen_set(ptr, value.ptr);
	}
	
	private static PropCollectionGetFunc Window_rna_properties_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
	{
		return rna_builtin_properties_get.get(iter);
	}};

	private static PropCollectionBeginFunc Window_rna_properties_begin = new PropCollectionBeginFunc() {
	public void begin(CollectionPropertyIterator iter, PointerRNA ptr)
	{
//		memset(iter, 0, sizeof(*iter));
		iter.clear();
		iter.parent= ptr;
		iter.prop= (PropertyRNA)rna_Window_rna_properties;

		rna_builtin_properties_begin.begin(iter, ptr);

		if(iter.valid)
			iter.ptr= Window_rna_properties_get.get(iter);
	}};

	private static PropCollectionNextFunc Window_rna_properties_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
	{
		rna_builtin_properties_next.next(iter);

		if(iter.valid)
			iter.ptr= Window_rna_properties_get.get(iter);
	}};

	private static PropCollectionEndFunc Window_rna_properties_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
	{
		rna_iterator_listbase_end.end(iter);
	}};
	
	/* Window */
	private static CollectionPropertyRNA rna_Window_rna_properties = new CollectionPropertyRNA(
		null, null,
		-1, "rna_properties", 128, "Properties",
		"RNA property collection",
		0,
		PROP_COLLECTION, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
		null, 0, null, null,
		0, -1, null,
		Window_rna_properties_begin, Window_rna_properties_next, Window_rna_properties_end, Window_rna_properties_get, null, null, rna_builtin_properties_lookup_string, RnaProperty.RNA_Property
	);
	
	private static PointerPropertyRNA rna_Window_screen = new PointerPropertyRNA(
			null, rna_Window_rna_properties,
		-1, "screen", 4456449, "Screen",
		"Active screen showing in the window",
		0,
		PROP_POINTER, PROP_NONE|PROP_UNIT_NONE, null, 0, new int[]{0, 0, 0}, 0,
//		"rna_Window_screen_update", 0, null, null,
		null, 0, null, null,
		0, -1, null,
//		"Window_screen_get", "Window_screen_set", null, null,RNA_Screen
		null, null, null, null,RnaScreen.RNA_Screen
	);
	
	public static StructRNA RNA_Window = new StructRNA(
//		null, (ContainerRNA)RNA_OperatorProperties,
			null, null,
			null,
		(PropertyRNA)rna_Window_rna_properties, (PropertyRNA)rna_Window_screen,
		null,null,
		"Window", 0, "Window", "Open window",
		17,
		null, (PropertyRNA)rna_Window_rna_properties,
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
		((PropertyRNA)rna_Window_rna_properties).next = rna_Window_screen;
	}

}

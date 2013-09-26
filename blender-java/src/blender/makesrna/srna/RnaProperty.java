package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaProperty extends RnaStruct {
	
	public RnaProperty(PointerRNA ptr) {
		super(ptr);
	}
	
	public static StructRNA RNA_Property = new StructRNA();

}

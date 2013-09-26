package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaStruct
{
	public PointerRNA ptr;
	
	public RnaStruct(PointerRNA ptr) {
		this.ptr = ptr;
	}
	
//	public PropertyRNA getProperty(String identifier) {
//		return null;
//	}
	
	public static StructRNA RNA_Struct = new StructRNA();

}

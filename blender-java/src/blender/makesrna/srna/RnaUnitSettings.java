package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;

public class RnaUnitSettings extends RnaStruct {
	
	public String system = "NONE";

	public RnaUnitSettings(PointerRNA ptr) {
		super(ptr);
	}
	
}

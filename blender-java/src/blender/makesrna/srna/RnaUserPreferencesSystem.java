package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;

public class RnaUserPreferencesSystem extends RnaStruct {
	
	public String audio_device;
	
	public RnaUserPreferencesSystem(PointerRNA ptr) {
		super(ptr);
	}

}

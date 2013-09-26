package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;

public class RnaUserPreferences extends RnaStruct {
	
//	public PointerRNA ptr;
	
	public RnaUserPreferencesSystem system;
	
//	public RNA_UserPreferences(PointerRNA ptr) {
//		this.ptr = ptr;
//		system = new RNA_UserPreferencesSystem();
//	}
	public RnaUserPreferences(PointerRNA ptr) {
		super(ptr);
		system = new RnaUserPreferencesSystem(null);
	}

}

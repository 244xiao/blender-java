package blender.makesrna.srna;

import blender.makesrna.RNATypes.PointerRNA;

public class RnaTimelineMarker extends RnaStruct {
	
	public boolean select = false;
	
	public RnaTimelineMarker(PointerRNA ptr) {
		super(ptr);
	}

}

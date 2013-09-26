package blender.makesrna.srna;

import blender.makesdna.sdna.ListBase;
import blender.makesrna.RNATypes.PointerRNA;

public class RnaKeyingSet extends RnaStruct {
	
	public RnaKeyingSet active = null;
	public ListBase paths = new ListBase();
	public boolean is_path_absolute = false;
	
	public RnaKeyingSet(PointerRNA ptr) {
		super(ptr);
		if (ptr != null) {
			active = new RnaKeyingSet(null);
		}
	}
	
}

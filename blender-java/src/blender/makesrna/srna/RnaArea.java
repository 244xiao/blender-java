package blender.makesrna.srna;

import blender.makesdna.sdna.ScrArea;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.rna_internal_types.StructRNA;

public class RnaArea extends RnaStruct {
	
//	public PointerRNA ptr;
	
	public RnaArea(PointerRNA ptr) {
		super(ptr);
	}
	
	public boolean getShow_menus()
	{
		ScrArea data= (ScrArea)(ptr.data);
		return !(((data.flag) & 1) != 0);
	}

	public void setShow_menus(boolean value)
	{
		ScrArea data= (ScrArea)(ptr.data);
		if(!value) data.flag |= 1;
		else data.flag &= ~1;
	}
	
	public static StructRNA RNA_Area = new StructRNA();

}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SoftbodyModifierData extends ModifierData implements DNA, Cloneable { // #92
  public SoftbodyModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;

  public void read(ByteBuffer buffer) {
    super.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (SoftbodyModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SoftbodyModifierData:\n");
    sb.append(super.toString());
    return sb.toString();
  }
  public SoftbodyModifierData copy() { try {return (SoftbodyModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

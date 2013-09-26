package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ShapeKeyModifierData extends ModifierData implements DNA, Cloneable { // #107
  public ShapeKeyModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;

  public void read(ByteBuffer buffer) {
    super.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (ShapeKeyModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ShapeKeyModifierData:\n");
    sb.append(super.toString());
    return sb.toString();
  }
  public ShapeKeyModifierData copy() { try {return (ShapeKeyModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

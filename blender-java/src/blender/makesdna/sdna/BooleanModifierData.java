package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BooleanModifierData extends ModifierData implements DNA, Cloneable { // #96
  public BooleanModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public int operation; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    operation = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeInt(operation);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (BooleanModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BooleanModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  operation: ").append(operation).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public BooleanModifierData copy() { try {return (BooleanModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

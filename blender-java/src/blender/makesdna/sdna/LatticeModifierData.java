package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class LatticeModifierData extends ModifierData implements DNA, Cloneable { // #74
  public LatticeModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (LatticeModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("LatticeModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public LatticeModifierData copy() { try {return (LatticeModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

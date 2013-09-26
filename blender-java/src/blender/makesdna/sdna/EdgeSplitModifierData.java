package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class EdgeSplitModifierData extends ModifierData implements DNA, Cloneable { // #80
  public EdgeSplitModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float split_angle; // 4
  public int flags; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    split_angle = buffer.getFloat();
    flags = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(split_angle);
    buffer.writeInt(flags);
  }
  public Object setmyarray(Object array) {
    myarray = (EdgeSplitModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("EdgeSplitModifierData:\n");
    sb.append(super.toString());
    sb.append("  split_angle: ").append(split_angle).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    return sb.toString();
  }
  public EdgeSplitModifierData copy() { try {return (EdgeSplitModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

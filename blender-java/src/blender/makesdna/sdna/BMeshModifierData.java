package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BMeshModifierData extends ModifierData implements DNA, Cloneable { // #82
  public BMeshModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float pad; // 4
  public int type; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    pad = buffer.getFloat();
    type = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(pad);
    buffer.writeInt(type);
  }
  public Object setmyarray(Object array) {
    myarray = (BMeshModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BMeshModifierData:\n");
    sb.append(super.toString());
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public BMeshModifierData copy() { try {return (BMeshModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

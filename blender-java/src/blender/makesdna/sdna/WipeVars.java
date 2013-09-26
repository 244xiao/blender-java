package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class WipeVars implements DNA, Cloneable { // #207
  public WipeVars[] myarray;
  public float edgeWidth; // 4
  public float angle; // 4
  public short forward; // 2
  public short wipetype; // 2

  public void read(ByteBuffer buffer) {
    edgeWidth = buffer.getFloat();
    angle = buffer.getFloat();
    forward = buffer.getShort();
    wipetype = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(edgeWidth);
    buffer.writeFloat(angle);
    buffer.writeShort(forward);
    buffer.writeShort(wipetype);
  }
  public Object setmyarray(Object array) {
    myarray = (WipeVars[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("WipeVars:\n");
    sb.append("  edgeWidth: ").append(edgeWidth).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  forward: ").append(forward).append("\n");
    sb.append("  wipetype: ").append(wipetype).append("\n");
    return sb.toString();
  }
  public WipeVars copy() { try {return (WipeVars)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

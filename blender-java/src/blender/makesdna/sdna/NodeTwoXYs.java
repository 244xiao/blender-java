package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeTwoXYs implements DNA, Cloneable { // #318
  public NodeTwoXYs[] myarray;
  public short x1; // 2
  public short x2; // 2
  public short y1; // 2
  public short y2; // 2
  public float fac_x1; // 4
  public float fac_x2; // 4
  public float fac_y1; // 4
  public float fac_y2; // 4

  public void read(ByteBuffer buffer) {
    x1 = buffer.getShort();
    x2 = buffer.getShort();
    y1 = buffer.getShort();
    y2 = buffer.getShort();
    fac_x1 = buffer.getFloat();
    fac_x2 = buffer.getFloat();
    fac_y1 = buffer.getFloat();
    fac_y2 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(x1);
    buffer.writeShort(x2);
    buffer.writeShort(y1);
    buffer.writeShort(y2);
    buffer.writeFloat(fac_x1);
    buffer.writeFloat(fac_x2);
    buffer.writeFloat(fac_y1);
    buffer.writeFloat(fac_y2);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeTwoXYs[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeTwoXYs:\n");
    sb.append("  x1: ").append(x1).append("\n");
    sb.append("  x2: ").append(x2).append("\n");
    sb.append("  y1: ").append(y1).append("\n");
    sb.append("  y2: ").append(y2).append("\n");
    sb.append("  fac_x1: ").append(fac_x1).append("\n");
    sb.append("  fac_x2: ").append(fac_x2).append("\n");
    sb.append("  fac_y1: ").append(fac_y1).append("\n");
    sb.append("  fac_y2: ").append(fac_y2).append("\n");
    return sb.toString();
  }
  public NodeTwoXYs copy() { try {return (NodeTwoXYs)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

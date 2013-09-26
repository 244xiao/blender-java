package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeTonemap implements DNA, Cloneable { // #325
  public NodeTonemap[] myarray;
  public float key; // 4
  public float offset; // 4
  public float gamma; // 4
  public float f; // 4
  public float m; // 4
  public float a; // 4
  public float c; // 4
  public int type; // 4

  public void read(ByteBuffer buffer) {
    key = buffer.getFloat();
    offset = buffer.getFloat();
    gamma = buffer.getFloat();
    f = buffer.getFloat();
    m = buffer.getFloat();
    a = buffer.getFloat();
    c = buffer.getFloat();
    type = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(key);
    buffer.writeFloat(offset);
    buffer.writeFloat(gamma);
    buffer.writeFloat(f);
    buffer.writeFloat(m);
    buffer.writeFloat(a);
    buffer.writeFloat(c);
    buffer.writeInt(type);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeTonemap[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeTonemap:\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  gamma: ").append(gamma).append("\n");
    sb.append("  f: ").append(f).append("\n");
    sb.append("  m: ").append(m).append("\n");
    sb.append("  a: ").append(a).append("\n");
    sb.append("  c: ").append(c).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public NodeTonemap copy() { try {return (NodeTonemap)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresCol implements DNA, Cloneable { // #65
  public MultiresCol[] myarray;
  public float a; // 4
  public float r; // 4
  public float g; // 4
  public float b; // 4

  public void read(ByteBuffer buffer) {
    a = buffer.getFloat();
    r = buffer.getFloat();
    g = buffer.getFloat();
    b = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(a);
    buffer.writeFloat(r);
    buffer.writeFloat(g);
    buffer.writeFloat(b);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresCol[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresCol:\n");
    sb.append("  a: ").append(a).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    return sb.toString();
  }
  public MultiresCol copy() { try {return (MultiresCol)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

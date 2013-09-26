package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CBData implements DNA, Cloneable { // #26
  public CBData[] myarray;
  public float r; // 4
  public float g; // 4
  public float b; // 4
  public float a; // 4
  public float pos; // 4
  public int cur; // 4

  public void read(ByteBuffer buffer) {
    r = buffer.getFloat();
    g = buffer.getFloat();
    b = buffer.getFloat();
    a = buffer.getFloat();
    pos = buffer.getFloat();
    cur = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(r);
    buffer.writeFloat(g);
    buffer.writeFloat(b);
    buffer.writeFloat(a);
    buffer.writeFloat(pos);
    buffer.writeInt(cur);
  }
  public Object setmyarray(Object array) {
    myarray = (CBData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CBData:\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    sb.append("  a: ").append(a).append("\n");
    sb.append("  pos: ").append(pos).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    return sb.toString();
  }
  public CBData copy() { try {return (CBData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

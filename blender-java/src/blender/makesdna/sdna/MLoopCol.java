package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MLoopCol implements DNA, Cloneable { // #56
  public MLoopCol[] myarray;
  public byte a; // 1
  public byte r; // 1
  public byte g; // 1
  public byte b; // 1
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    a = buffer.get();
    r = buffer.get();
    g = buffer.get();
    b = buffer.get();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeByte(a);
    buffer.writeByte(r);
    buffer.writeByte(g);
    buffer.writeByte(b);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (MLoopCol[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MLoopCol:\n");
    sb.append("  a: ").append(a).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public MLoopCol copy() { try {return (MLoopCol)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

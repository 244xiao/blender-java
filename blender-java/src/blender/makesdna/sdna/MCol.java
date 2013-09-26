package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MCol implements DNA, Cloneable { // #53
  public MCol[] myarray;
  public byte a; // 1
  public byte r; // 1
  public byte g; // 1
  public byte b; // 1

  public void read(ByteBuffer buffer) {
    a = buffer.get();
    r = buffer.get();
    g = buffer.get();
    b = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeByte(a);
    buffer.writeByte(r);
    buffer.writeByte(g);
    buffer.writeByte(b);
  }
  public Object setmyarray(Object array) {
    myarray = (MCol[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MCol:\n");
    sb.append("  a: ").append(a).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    return sb.toString();
  }
  public MCol copy() { try {return (MCol)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

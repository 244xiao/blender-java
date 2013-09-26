package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class vec2s implements DNA, Cloneable { // #3
  public vec2s[] myarray;
  public short x; // 2
  public short y; // 2

  public void read(ByteBuffer buffer) {
    x = buffer.getShort();
    y = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(x);
    buffer.writeShort(y);
  }
  public Object setmyarray(Object array) {
    myarray = (vec2s[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("vec2s:\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    return sb.toString();
  }
  public vec2s copy() { try {return (vec2s)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

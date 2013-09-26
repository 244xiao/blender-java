package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class vec2f implements DNA, Cloneable { // #4
  public vec2f[] myarray;
  public float x; // 4
  public float y; // 4

  public void read(ByteBuffer buffer) {
    x = buffer.getFloat();
    y = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(x);
    buffer.writeFloat(y);
  }
  public Object setmyarray(Object array) {
    myarray = (vec2f[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("vec2f:\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    return sb.toString();
  }
  public vec2f copy() { try {return (vec2f)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

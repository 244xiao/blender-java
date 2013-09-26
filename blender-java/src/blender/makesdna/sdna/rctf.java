package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class rctf implements DNA, Cloneable { // #6
  public rctf[] myarray;
  public float xmin; // 4
  public float xmax; // 4
  public float ymin; // 4
  public float ymax; // 4

  public void read(ByteBuffer buffer) {
    xmin = buffer.getFloat();
    xmax = buffer.getFloat();
    ymin = buffer.getFloat();
    ymax = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(xmin);
    buffer.writeFloat(xmax);
    buffer.writeFloat(ymin);
    buffer.writeFloat(ymax);
  }
  public Object setmyarray(Object array) {
    myarray = (rctf[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("rctf:\n");
    sb.append("  xmin: ").append(xmin).append("\n");
    sb.append("  xmax: ").append(xmax).append("\n");
    sb.append("  ymin: ").append(ymin).append("\n");
    sb.append("  ymax: ").append(ymax).append("\n");
    return sb.toString();
  }
  public rctf copy() { try {return (rctf)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

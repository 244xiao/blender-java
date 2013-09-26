package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class rcti implements DNA, Cloneable { // #5
  public rcti[] myarray;
  public int xmin; // 4
  public int xmax; // 4
  public int ymin; // 4
  public int ymax; // 4

  public void read(ByteBuffer buffer) {
    xmin = buffer.getInt();
    xmax = buffer.getInt();
    ymin = buffer.getInt();
    ymax = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(xmin);
    buffer.writeInt(xmax);
    buffer.writeInt(ymin);
    buffer.writeInt(ymax);
  }
  public Object setmyarray(Object array) {
    myarray = (rcti[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("rcti:\n");
    sb.append("  xmin: ").append(xmin).append("\n");
    sb.append("  xmax: ").append(xmax).append("\n");
    sb.append("  ymin: ").append(ymin).append("\n");
    sb.append("  ymax: ").append(ymax).append("\n");
    return sb.toString();
  }
  public rcti copy() { try {return (rcti)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

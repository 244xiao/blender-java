package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class StripElem implements DNA, Cloneable { // #197
  public StripElem[] myarray;
  public byte[] name = new byte[80]; // 1
  public int orig_width; // 4
  public int orig_height; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    orig_width = buffer.getInt();
    orig_height = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeInt(orig_width);
    buffer.writeInt(orig_height);
  }
  public Object setmyarray(Object array) {
    myarray = (StripElem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("StripElem:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  orig_width: ").append(orig_width).append("\n");
    sb.append("  orig_height: ").append(orig_height).append("\n");
    return sb.toString();
  }
  public StripElem copy() { try {return (StripElem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

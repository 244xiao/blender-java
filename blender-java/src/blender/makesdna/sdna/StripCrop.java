package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class StripCrop implements DNA, Cloneable { // #198
  public StripCrop[] myarray;
  public int top; // 4
  public int bottom; // 4
  public int left; // 4
  public int right; // 4

  public void read(ByteBuffer buffer) {
    top = buffer.getInt();
    bottom = buffer.getInt();
    left = buffer.getInt();
    right = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(top);
    buffer.writeInt(bottom);
    buffer.writeInt(left);
    buffer.writeInt(right);
  }
  public Object setmyarray(Object array) {
    myarray = (StripCrop[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("StripCrop:\n");
    sb.append("  top: ").append(top).append("\n");
    sb.append("  bottom: ").append(bottom).append("\n");
    sb.append("  left: ").append(left).append("\n");
    sb.append("  right: ").append(right).append("\n");
    return sb.toString();
  }
  public StripCrop copy() { try {return (StripCrop)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

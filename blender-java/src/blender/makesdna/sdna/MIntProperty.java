package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MIntProperty implements DNA, Cloneable { // #61
  public MIntProperty[] myarray;
  public int i; // 4

  public void read(ByteBuffer buffer) {
    i = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(i);
  }
  public Object setmyarray(Object array) {
    myarray = (MIntProperty[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MIntProperty:\n");
    sb.append("  i: ").append(i).append("\n");
    return sb.toString();
  }
  public MIntProperty copy() { try {return (MIntProperty)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

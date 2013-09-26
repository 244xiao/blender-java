package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MSelect implements DNA, Cloneable { // #58
  public MSelect[] myarray;
  public int index; // 4
  public int type; // 4

  public void read(ByteBuffer buffer) {
    index = buffer.getInt();
    type = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(index);
    buffer.writeInt(type);
  }
  public Object setmyarray(Object array) {
    myarray = (MSelect[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MSelect:\n");
    sb.append("  index: ").append(index).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public MSelect copy() { try {return (MSelect)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

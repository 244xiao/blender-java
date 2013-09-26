package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bVisibilityActuator implements DNA, Cloneable { // #252
  public bVisibilityActuator[] myarray;
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bVisibilityActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bVisibilityActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bVisibilityActuator copy() { try {return (bVisibilityActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

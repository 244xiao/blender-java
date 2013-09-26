package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bStateActuator implements DNA, Cloneable { // #255
  public bStateActuator[] myarray;
  public int type; // 4
  public int mask; // 4

  public void read(ByteBuffer buffer) {
    type = buffer.getInt();
    mask = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(type);
    buffer.writeInt(mask);
  }
  public Object setmyarray(Object array) {
    myarray = (bStateActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bStateActuator:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  mask: ").append(mask).append("\n");
    return sb.toString();
  }
  public bStateActuator copy() { try {return (bStateActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

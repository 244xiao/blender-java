package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMouseSensor implements DNA, Cloneable { // #220
  public bMouseSensor[] myarray;
  public short type; // 2
  public short flag; // 2
  public short pad1; // 2
  public short pad2; // 2

  public void read(ByteBuffer buffer) {
    type = buffer.getShort();
    flag = buffer.getShort();
    pad1 = buffer.getShort();
    pad2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(pad1);
    buffer.writeShort(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (bMouseSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMouseSensor:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public bMouseSensor copy() { try {return (bMouseSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

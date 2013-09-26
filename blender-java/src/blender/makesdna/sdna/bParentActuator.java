package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bParentActuator implements DNA, Cloneable { // #254
  public bParentActuator[] myarray;
  public byte[] pad = new byte[2]; // 1
  public short flag; // 2
  public int type; // 4
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    buffer.get(pad);
    flag = buffer.getShort();
    type = buffer.getInt();
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(pad);
    buffer.writeShort(flag);
    buffer.writeInt(type);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bParentActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bParentActuator:\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public bParentActuator copy() { try {return (bParentActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

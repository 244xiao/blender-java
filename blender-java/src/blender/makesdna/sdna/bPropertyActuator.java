package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPropertyActuator implements DNA, Cloneable { // #243
  public bPropertyActuator[] myarray;
  public int pad; // 4
  public int type; // 4
  public byte[] name = new byte[32]; // 1
  public byte[] value = new byte[32]; // 1
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    pad = buffer.getInt();
    type = buffer.getInt();
    buffer.get(name);
    buffer.get(value);
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(pad);
    buffer.writeInt(type);
    buffer.write(name);
    buffer.write(value);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bPropertyActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPropertyActuator:\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  value: ").append(new String(value)).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public bPropertyActuator copy() { try {return (bPropertyActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

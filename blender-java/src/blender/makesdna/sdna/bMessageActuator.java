package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMessageActuator implements DNA, Cloneable { // #250
  public bMessageActuator[] myarray;
  public byte[] toPropName = new byte[32]; // 1
  public bObject toObject; // ptr 1296
  public byte[] subject = new byte[32]; // 1
  public short bodyType; // 2
  public short pad1; // 2
  public int pad2; // 4
  public byte[] body = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(toPropName);
    toObject = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subject);
    bodyType = buffer.getShort();
    pad1 = buffer.getShort();
    pad2 = buffer.getInt();
    buffer.get(body);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(toPropName);
    buffer.writeInt(toObject!=null?toObject.hashCode():0);
    buffer.write(subject);
    buffer.writeShort(bodyType);
    buffer.writeShort(pad1);
    buffer.writeInt(pad2);
    buffer.write(body);
  }
  public Object setmyarray(Object array) {
    myarray = (bMessageActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMessageActuator:\n");
    sb.append("  toPropName: ").append(new String(toPropName)).append("\n");
    sb.append("  toObject: ").append(toObject).append("\n");
    sb.append("  subject: ").append(new String(subject)).append("\n");
    sb.append("  bodyType: ").append(bodyType).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  body: ").append(new String(body)).append("\n");
    return sb.toString();
  }
  public bMessageActuator copy() { try {return (bMessageActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

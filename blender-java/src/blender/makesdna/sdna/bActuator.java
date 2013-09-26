package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActuator extends Link<bActuator> implements DNA, Cloneable { // #257
  public bActuator[] myarray;
  public bActuator mynew; // ptr 80
  public short type; // 2
  public short flag; // 2
  public short otype; // 2
  public short go; // 2
  public byte[] name = new byte[32]; // 1
  public Object data; // ptr 0
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bActuator.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bActuator.class); // get ptr
    mynew = DNATools.link(DNATools.ptr(buffer), bActuator.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    otype = buffer.getShort();
    go = buffer.getShort();
    buffer.get(name);
    data = DNATools.ptr(buffer); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(mynew!=null?mynew.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(otype);
    buffer.writeShort(go);
    buffer.write(name);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActuator:\n");
    sb.append("  mynew: ").append(mynew).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  otype: ").append(otype).append("\n");
    sb.append("  go: ").append(go).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public bActuator copy() { try {return (bActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

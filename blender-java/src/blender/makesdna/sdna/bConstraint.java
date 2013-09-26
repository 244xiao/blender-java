package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bConstraint extends Link<bConstraint> implements DNA, Cloneable { // #276
  public bConstraint[] myarray;
  public Object data; // ptr 0
  public short type; // 2
  public short flag; // 2
  public byte ownspace; // 1
  public byte tarspace; // 1
  public byte[] name = new byte[30]; // 1
  public float enforce; // 4
  public float headtail; // 4
  public int pad; // 4
  public Ipo ipo; // ptr 112
  public float lin_error; // 4
  public float rot_error; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bConstraint.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bConstraint.class); // get ptr
    data = DNATools.ptr(buffer); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    ownspace = buffer.get();
    tarspace = buffer.get();
    buffer.get(name);
    enforce = buffer.getFloat();
    headtail = buffer.getFloat();
    pad = buffer.getInt();
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    lin_error = buffer.getFloat();
    rot_error = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeByte(ownspace);
    buffer.writeByte(tarspace);
    buffer.write(name);
    buffer.writeFloat(enforce);
    buffer.writeFloat(headtail);
    buffer.writeInt(pad);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeFloat(lin_error);
    buffer.writeFloat(rot_error);
  }
  public Object setmyarray(Object array) {
    myarray = (bConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bConstraint:\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  ownspace: ").append(ownspace).append("\n");
    sb.append("  tarspace: ").append(tarspace).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  enforce: ").append(enforce).append("\n");
    sb.append("  headtail: ").append(headtail).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  lin_error: ").append(lin_error).append("\n");
    sb.append("  rot_error: ").append(rot_error).append("\n");
    return sb.toString();
  }
  public bConstraint copy() { try {return (bConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

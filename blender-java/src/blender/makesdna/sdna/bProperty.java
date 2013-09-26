package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bProperty extends Link<bProperty> implements DNA, Cloneable { // #218
  public bProperty[] myarray;
  public byte[] name = new byte[32]; // 1
  public short type; // 2
  public short flag; // 2
  public int data; // 4
  public Object poin; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bProperty.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bProperty.class); // get ptr
    buffer.get(name);
    type = buffer.getShort();
    flag = buffer.getShort();
    data = buffer.getInt();
    poin = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeInt(data);
    buffer.writeInt(poin!=null?poin.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bProperty[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bProperty:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  poin: ").append(poin).append("\n");
    return sb.toString();
  }
  public bProperty copy() { try {return (bProperty)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

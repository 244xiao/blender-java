package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IDProperty extends Link<IDProperty> implements DNA, Cloneable { // #8
  public IDProperty[] myarray;
  public byte type; // 1
  public byte subtype; // 1
  public short flag; // 2
  public byte[] name = new byte[32]; // 1
  public int saved; // 4
  public IDPropertyData data = new IDPropertyData(); // 32
  public int len; // 4
  public int totallen; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    type = buffer.get();
    subtype = buffer.get();
    flag = buffer.getShort();
    buffer.get(name);
    saved = buffer.getInt();
    data.read(buffer);
    len = buffer.getInt();
    totallen = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeByte(type);
    buffer.writeByte(subtype);
    buffer.writeShort(flag);
    buffer.write(name);
    buffer.writeInt(saved);
    data.write(buffer);
    buffer.writeInt(len);
    buffer.writeInt(totallen);
  }
  public Object setmyarray(Object array) {
    myarray = (IDProperty[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("IDProperty:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  subtype: ").append(subtype).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  saved: ").append(saved).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  totallen: ").append(totallen).append("\n");
    return sb.toString();
  }
  public IDProperty copy() { try {return (IDProperty)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ID extends Link implements DNA, Cloneable { // #9
  public Object newid; // ptr 72
  public Library lib; // ptr 584
  public byte[] name = new byte[24]; // 1
  public short us; // 2
  public short flag; // 2
  public int icon_id; // 4
  public IDProperty properties; // ptr 96

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    newid = DNATools.ptr(buffer); // get ptr
    lib = DNATools.link(DNATools.ptr(buffer), Library.class); // get ptr
    buffer.get(name);
    us = buffer.getShort();
    flag = buffer.getShort();
    icon_id = buffer.getInt();
    properties = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(newid!=null?newid.hashCode():0);
    buffer.writeInt(lib!=null?lib.hashCode():0);
    buffer.write(name);
    buffer.writeShort(us);
    buffer.writeShort(flag);
    buffer.writeInt(icon_id);
    buffer.writeInt(properties!=null?properties.hashCode():0);
  }
  public Object setmyarray(Object array) {
    return this;
  }
  public Object getmyarray() {
    return null;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ID:\n");
    sb.append("  newid: ").append(newid).append("\n");
    sb.append("  lib: ").append(lib).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  us: ").append(us).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  icon_id: ").append(icon_id).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    return sb.toString();
  }
  public ID copy() { try {return (ID)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

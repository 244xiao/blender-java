package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmKeyMapItem extends Link<wmKeyMapItem> implements DNA, Cloneable { // #361
  public wmKeyMapItem[] myarray;
  public byte[] idname = new byte[64]; // 1
  public IDProperty properties; // ptr 96
  public short propvalue; // 2
  public short type; // 2
  public short val; // 2
  public short shift; // 2
  public short ctrl; // 2
  public short alt; // 2
  public short oskey; // 2
  public short keymodifier; // 2
  public short flag; // 2
  public short maptype; // 2
  public short id; // 2
  public short pad; // 2
  public Object ptr; // ptr (PointerRNA) 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), wmKeyMapItem.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), wmKeyMapItem.class); // get ptr
    buffer.get(idname);
    properties = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    propvalue = buffer.getShort();
    type = buffer.getShort();
    val = buffer.getShort();
    shift = buffer.getShort();
    ctrl = buffer.getShort();
    alt = buffer.getShort();
    oskey = buffer.getShort();
    keymodifier = buffer.getShort();
    flag = buffer.getShort();
    maptype = buffer.getShort();
    id = buffer.getShort();
    pad = buffer.getShort();
    ptr = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(idname);
    buffer.writeInt(properties!=null?properties.hashCode():0);
    buffer.writeShort(propvalue);
    buffer.writeShort(type);
    buffer.writeShort(val);
    buffer.writeShort(shift);
    buffer.writeShort(ctrl);
    buffer.writeShort(alt);
    buffer.writeShort(oskey);
    buffer.writeShort(keymodifier);
    buffer.writeShort(flag);
    buffer.writeShort(maptype);
    buffer.writeShort(id);
    buffer.writeShort(pad);
    buffer.writeInt(ptr!=null?ptr.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (wmKeyMapItem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmKeyMapItem:\n");
    sb.append("  idname: ").append(new String(idname)).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("  propvalue: ").append(propvalue).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  val: ").append(val).append("\n");
    sb.append("  shift: ").append(shift).append("\n");
    sb.append("  ctrl: ").append(ctrl).append("\n");
    sb.append("  alt: ").append(alt).append("\n");
    sb.append("  oskey: ").append(oskey).append("\n");
    sb.append("  keymodifier: ").append(keymodifier).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  maptype: ").append(maptype).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  ptr: ").append(ptr).append("\n");
    return sb.toString();
  }
  public wmKeyMapItem copy() { try {return (wmKeyMapItem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

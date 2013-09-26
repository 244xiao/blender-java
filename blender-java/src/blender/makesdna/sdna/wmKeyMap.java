package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmKeyMap extends Link<wmKeyMap> implements DNA, Cloneable { // #362
  public wmKeyMap[] myarray;
  public ListBase items = new ListBase(); // 16
  public byte[] idname = new byte[64]; // 1
  public short spaceid; // 2
  public short regionid; // 2
  public short flag; // 2
  public short kmi_id; // 2
  public Object poll; // func ptr 4
  public Object modal_items; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), wmKeyMap.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), wmKeyMap.class); // get ptr
    items.read(buffer);
    buffer.get(idname);
    spaceid = buffer.getShort();
    regionid = buffer.getShort();
    flag = buffer.getShort();
    kmi_id = buffer.getShort();
    poll = DNATools.ptr(buffer); // get ptr
    modal_items = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    items.write(buffer);
    buffer.write(idname);
    buffer.writeShort(spaceid);
    buffer.writeShort(regionid);
    buffer.writeShort(flag);
    buffer.writeShort(kmi_id);
    buffer.writeInt(poll!=null?poll.hashCode():0);
    buffer.writeInt(modal_items!=null?modal_items.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (wmKeyMap[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmKeyMap:\n");
    sb.append("  items: ").append(items).append("\n");
    sb.append("  idname: ").append(new String(idname)).append("\n");
    sb.append("  spaceid: ").append(spaceid).append("\n");
    sb.append("  regionid: ").append(regionid).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  kmi_id: ").append(kmi_id).append("\n");
    sb.append("  poll: ").append(poll).append("\n");
    sb.append("  modal_items: ").append(modal_items).append("\n");
    return sb.toString();
  }
  public wmKeyMap copy() { try {return (wmKeyMap)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

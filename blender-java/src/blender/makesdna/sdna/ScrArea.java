package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ScrArea extends Link<ScrArea> implements DNA, Cloneable { // #194
  public ScrArea[] myarray;
  public ScrVert v1; // ptr 32
  public ScrVert v2; // ptr 32
  public ScrVert v3; // ptr 32
  public ScrVert v4; // ptr 32
  public bScreen full; // ptr 216
  public rcti totrct = new rcti(); // 16
  public byte spacetype; // 1
  public byte butspacetype; // 1
  public short winx; // 2
  public short winy; // 2
  public short headertype; // 2
  public short pad; // 2
  public short do_refresh; // 2
  public short cursor; // 2
  public short flag; // 2
  public Object type; // ptr (SpaceType) 0
  public ListBase spacedata = new ListBase(); // 16
  public ListBase regionbase = new ListBase(); // 16
  public ListBase handlers = new ListBase(); // 16
  public ListBase actionzones = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ScrArea.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ScrArea.class); // get ptr
    v1 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    v2 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    v3 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    v4 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    full = DNATools.link(DNATools.ptr(buffer), bScreen.class); // get ptr
    totrct.read(buffer);
    spacetype = buffer.get();
    butspacetype = buffer.get();
    winx = buffer.getShort();
    winy = buffer.getShort();
    headertype = buffer.getShort();
    pad = buffer.getShort();
    do_refresh = buffer.getShort();
    cursor = buffer.getShort();
    flag = buffer.getShort();
    type = DNATools.ptr(buffer); // get ptr
    spacedata.read(buffer);
    regionbase.read(buffer);
    handlers.read(buffer);
    actionzones.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(v1!=null?v1.hashCode():0);
    buffer.writeInt(v2!=null?v2.hashCode():0);
    buffer.writeInt(v3!=null?v3.hashCode():0);
    buffer.writeInt(v4!=null?v4.hashCode():0);
    buffer.writeInt(full!=null?full.hashCode():0);
    totrct.write(buffer);
    buffer.writeByte(spacetype);
    buffer.writeByte(butspacetype);
    buffer.writeShort(winx);
    buffer.writeShort(winy);
    buffer.writeShort(headertype);
    buffer.writeShort(pad);
    buffer.writeShort(do_refresh);
    buffer.writeShort(cursor);
    buffer.writeShort(flag);
    buffer.writeInt(type!=null?type.hashCode():0);
    spacedata.write(buffer);
    regionbase.write(buffer);
    handlers.write(buffer);
    actionzones.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (ScrArea[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ScrArea:\n");
    sb.append("  v1: ").append(v1).append("\n");
    sb.append("  v2: ").append(v2).append("\n");
    sb.append("  v3: ").append(v3).append("\n");
    sb.append("  v4: ").append(v4).append("\n");
    sb.append("  full: ").append(full).append("\n");
    sb.append("  totrct: ").append(totrct).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  butspacetype: ").append(butspacetype).append("\n");
    sb.append("  winx: ").append(winx).append("\n");
    sb.append("  winy: ").append(winy).append("\n");
    sb.append("  headertype: ").append(headertype).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  do_refresh: ").append(do_refresh).append("\n");
    sb.append("  cursor: ").append(cursor).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  spacedata: ").append(spacedata).append("\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  handlers: ").append(handlers).append("\n");
    sb.append("  actionzones: ").append(actionzones).append("\n");
    return sb.toString();
  }
  public ScrArea copy() { try {return (ScrArea)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

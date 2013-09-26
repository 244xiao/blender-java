package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Panel extends Link<Panel> implements DNA, Cloneable { // #193
  public Panel[] myarray;
  public Object type; // ptr (PanelType) 0
  public Object layout; // ptr (uiLayout) 0
  public byte[] panelname = new byte[64]; // 1
  public byte[] tabname = new byte[64]; // 1
  public byte[] drawname = new byte[64]; // 1
  public short ofsx; // 2
  public short ofsy; // 2
  public short sizex; // 2
  public short sizey; // 2
  public short labelofs; // 2
  public short pad; // 2
  public short flag; // 2
  public short runtime_flag; // 2
  public short control; // 2
  public short snap; // 2
  public int sortorder; // 4
  public Panel paneltab; // ptr 344
  public Object activedata; // ptr 0
  public int list_scroll; // 4
  public int list_size; // 4
  public int list_last_len; // 4
  public int list_grip_size; // 4
  public byte[] list_search = new byte[64]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Panel.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Panel.class); // get ptr
    type = DNATools.ptr(buffer); // get ptr
    layout = DNATools.ptr(buffer); // get ptr
    buffer.get(panelname);
    buffer.get(tabname);
    buffer.get(drawname);
    ofsx = buffer.getShort();
    ofsy = buffer.getShort();
    sizex = buffer.getShort();
    sizey = buffer.getShort();
    labelofs = buffer.getShort();
    pad = buffer.getShort();
    flag = buffer.getShort();
    runtime_flag = buffer.getShort();
    control = buffer.getShort();
    snap = buffer.getShort();
    sortorder = buffer.getInt();
    paneltab = DNATools.link(DNATools.ptr(buffer), Panel.class); // get ptr
    activedata = DNATools.ptr(buffer); // get ptr
    list_scroll = buffer.getInt();
    list_size = buffer.getInt();
    list_last_len = buffer.getInt();
    list_grip_size = buffer.getInt();
    buffer.get(list_search);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(type!=null?type.hashCode():0);
    buffer.writeInt(layout!=null?layout.hashCode():0);
    buffer.write(panelname);
    buffer.write(tabname);
    buffer.write(drawname);
    buffer.writeShort(ofsx);
    buffer.writeShort(ofsy);
    buffer.writeShort(sizex);
    buffer.writeShort(sizey);
    buffer.writeShort(labelofs);
    buffer.writeShort(pad);
    buffer.writeShort(flag);
    buffer.writeShort(runtime_flag);
    buffer.writeShort(control);
    buffer.writeShort(snap);
    buffer.writeInt(sortorder);
    buffer.writeInt(paneltab!=null?paneltab.hashCode():0);
    buffer.writeInt(activedata!=null?activedata.hashCode():0);
    buffer.writeInt(list_scroll);
    buffer.writeInt(list_size);
    buffer.writeInt(list_last_len);
    buffer.writeInt(list_grip_size);
    buffer.write(list_search);
  }
  public Object setmyarray(Object array) {
    myarray = (Panel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Panel:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  layout: ").append(layout).append("\n");
    sb.append("  panelname: ").append(new String(panelname)).append("\n");
    sb.append("  tabname: ").append(new String(tabname)).append("\n");
    sb.append("  drawname: ").append(new String(drawname)).append("\n");
    sb.append("  ofsx: ").append(ofsx).append("\n");
    sb.append("  ofsy: ").append(ofsy).append("\n");
    sb.append("  sizex: ").append(sizex).append("\n");
    sb.append("  sizey: ").append(sizey).append("\n");
    sb.append("  labelofs: ").append(labelofs).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  runtime_flag: ").append(runtime_flag).append("\n");
    sb.append("  control: ").append(control).append("\n");
    sb.append("  snap: ").append(snap).append("\n");
    sb.append("  sortorder: ").append(sortorder).append("\n");
    sb.append("  paneltab: ").append(paneltab).append("\n");
    sb.append("  activedata: ").append(activedata).append("\n");
    sb.append("  list_scroll: ").append(list_scroll).append("\n");
    sb.append("  list_size: ").append(list_size).append("\n");
    sb.append("  list_last_len: ").append(list_last_len).append("\n");
    sb.append("  list_grip_size: ").append(list_grip_size).append("\n");
    sb.append("  list_search: ").append(new String(list_search)).append("\n");
    return sb.toString();
  }
  public Panel copy() { try {return (Panel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

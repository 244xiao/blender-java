package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ARegion extends Link<ARegion> implements DNA, Cloneable { // #195
  public ARegion[] myarray;
  public View2D v2d = new View2D(); // 144
  public rcti winrct = new rcti(); // 16
  public rcti drawrct = new rcti(); // 16
  public short winx; // 2
  public short winy; // 2
  public short swinid; // 2
  public short regiontype; // 2
  public short alignment; // 2
  public short flag; // 2
  public float fsize; // 4
  public short sizex; // 2
  public short sizey; // 2
  public short do_draw; // 2
  public short do_draw_overlay; // 2
  public short swap; // 2
  public short[] pad = new short[3]; // 2
  public Object type; // ptr (ARegionType) 0
  public ListBase uiblocks = new ListBase(); // 16
  public ListBase panels = new ListBase(); // 16
  public ListBase handlers = new ListBase(); // 16
  public Object headerstr; // ptr 1
  public Object regiondata; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ARegion.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ARegion.class); // get ptr
    v2d.read(buffer);
    winrct.read(buffer);
    drawrct.read(buffer);
    winx = buffer.getShort();
    winy = buffer.getShort();
    swinid = buffer.getShort();
    regiontype = buffer.getShort();
    alignment = buffer.getShort();
    flag = buffer.getShort();
    fsize = buffer.getFloat();
    sizex = buffer.getShort();
    sizey = buffer.getShort();
    do_draw = buffer.getShort();
    do_draw_overlay = buffer.getShort();
    swap = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    type = DNATools.ptr(buffer); // get ptr
    uiblocks.read(buffer);
    panels.read(buffer);
    handlers.read(buffer);
    headerstr = DNATools.ptr(buffer); // get ptr
    regiondata = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    v2d.write(buffer);
    winrct.write(buffer);
    drawrct.write(buffer);
    buffer.writeShort(winx);
    buffer.writeShort(winy);
    buffer.writeShort(swinid);
    buffer.writeShort(regiontype);
    buffer.writeShort(alignment);
    buffer.writeShort(flag);
    buffer.writeFloat(fsize);
    buffer.writeShort(sizex);
    buffer.writeShort(sizey);
    buffer.writeShort(do_draw);
    buffer.writeShort(do_draw_overlay);
    buffer.writeShort(swap);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.writeInt(type!=null?type.hashCode():0);
    uiblocks.write(buffer);
    panels.write(buffer);
    handlers.write(buffer);
    buffer.writeInt(headerstr!=null?headerstr.hashCode():0);
    buffer.writeInt(regiondata!=null?regiondata.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ARegion[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ARegion:\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  winrct: ").append(winrct).append("\n");
    sb.append("  drawrct: ").append(drawrct).append("\n");
    sb.append("  winx: ").append(winx).append("\n");
    sb.append("  winy: ").append(winy).append("\n");
    sb.append("  swinid: ").append(swinid).append("\n");
    sb.append("  regiontype: ").append(regiontype).append("\n");
    sb.append("  alignment: ").append(alignment).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  fsize: ").append(fsize).append("\n");
    sb.append("  sizex: ").append(sizex).append("\n");
    sb.append("  sizey: ").append(sizey).append("\n");
    sb.append("  do_draw: ").append(do_draw).append("\n");
    sb.append("  do_draw_overlay: ").append(do_draw_overlay).append("\n");
    sb.append("  swap: ").append(swap).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  uiblocks: ").append(uiblocks).append("\n");
    sb.append("  panels: ").append(panels).append("\n");
    sb.append("  handlers: ").append(handlers).append("\n");
    sb.append("  headerstr: ").append(headerstr).append("\n");
    sb.append("  regiondata: ").append(regiondata).append("\n");
    return sb.toString();
  }
  public ARegion copy() { try {return (ARegion)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

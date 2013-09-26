package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class View2D implements DNA, Cloneable { // #155
  public View2D[] myarray;
  public rctf tot = new rctf(); // 16
  public rctf cur = new rctf(); // 16
  public rcti vert = new rcti(); // 16
  public rcti hor = new rcti(); // 16
  public rcti mask = new rcti(); // 16
  public float[] min = new float[2]; // 4
  public float[] max = new float[2]; // 4
  public float minzoom; // 4
  public float maxzoom; // 4
  public short scroll; // 2
  public short scroll_ui; // 2
  public short keeptot; // 2
  public short keepzoom; // 2
  public short keepofs; // 2
  public short flag; // 2
  public short align; // 2
  public short winx; // 2
  public short winy; // 2
  public short oldwinx; // 2
  public short oldwiny; // 2
  public short around; // 2
  public Object tab_offset; // ptr 4
  public int tab_num; // 4
  public int tab_cur; // 4

  public void read(ByteBuffer buffer) {
    tot.read(buffer);
    cur.read(buffer);
    vert.read(buffer);
    hor.read(buffer);
    mask.read(buffer);
    for(int i=0;i<min.length;i++) min[i]=buffer.getFloat();
    for(int i=0;i<max.length;i++) max[i]=buffer.getFloat();
    minzoom = buffer.getFloat();
    maxzoom = buffer.getFloat();
    scroll = buffer.getShort();
    scroll_ui = buffer.getShort();
    keeptot = buffer.getShort();
    keepzoom = buffer.getShort();
    keepofs = buffer.getShort();
    flag = buffer.getShort();
    align = buffer.getShort();
    winx = buffer.getShort();
    winy = buffer.getShort();
    oldwinx = buffer.getShort();
    oldwiny = buffer.getShort();
    around = buffer.getShort();
    tab_offset = DNATools.ptr(buffer); // get ptr
    tab_num = buffer.getInt();
    tab_cur = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    tot.write(buffer);
    cur.write(buffer);
    vert.write(buffer);
    hor.write(buffer);
    mask.write(buffer);
    for(int i=0;i<min.length;i++) buffer.writeFloat(min[i]);
    for(int i=0;i<max.length;i++) buffer.writeFloat(max[i]);
    buffer.writeFloat(minzoom);
    buffer.writeFloat(maxzoom);
    buffer.writeShort(scroll);
    buffer.writeShort(scroll_ui);
    buffer.writeShort(keeptot);
    buffer.writeShort(keepzoom);
    buffer.writeShort(keepofs);
    buffer.writeShort(flag);
    buffer.writeShort(align);
    buffer.writeShort(winx);
    buffer.writeShort(winy);
    buffer.writeShort(oldwinx);
    buffer.writeShort(oldwiny);
    buffer.writeShort(around);
    buffer.writeInt(tab_offset!=null?tab_offset.hashCode():0);
    buffer.writeInt(tab_num);
    buffer.writeInt(tab_cur);
  }
  public Object setmyarray(Object array) {
    myarray = (View2D[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("View2D:\n");
    sb.append("  tot: ").append(tot).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    sb.append("  vert: ").append(vert).append("\n");
    sb.append("  hor: ").append(hor).append("\n");
    sb.append("  mask: ").append(mask).append("\n");
    sb.append("  min: ").append(Arrays.toString(min)).append("\n");
    sb.append("  max: ").append(Arrays.toString(max)).append("\n");
    sb.append("  minzoom: ").append(minzoom).append("\n");
    sb.append("  maxzoom: ").append(maxzoom).append("\n");
    sb.append("  scroll: ").append(scroll).append("\n");
    sb.append("  scroll_ui: ").append(scroll_ui).append("\n");
    sb.append("  keeptot: ").append(keeptot).append("\n");
    sb.append("  keepzoom: ").append(keepzoom).append("\n");
    sb.append("  keepofs: ").append(keepofs).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  align: ").append(align).append("\n");
    sb.append("  winx: ").append(winx).append("\n");
    sb.append("  winy: ").append(winy).append("\n");
    sb.append("  oldwinx: ").append(oldwinx).append("\n");
    sb.append("  oldwiny: ").append(oldwiny).append("\n");
    sb.append("  around: ").append(around).append("\n");
    sb.append("  tab_offset: ").append(tab_offset).append("\n");
    sb.append("  tab_num: ").append(tab_num).append("\n");
    sb.append("  tab_cur: ").append(tab_cur).append("\n");
    return sb.toString();
  }
  public View2D copy() { try {return (View2D)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bScreen extends ID implements DNA, Cloneable { // #190
  public bScreen[] myarray;
  public ID id = (ID)this;
  public ListBase vertbase = new ListBase(); // 16
  public ListBase edgebase = new ListBase(); // 16
  public ListBase areabase = new ListBase(); // 16
  public ListBase regionbase = new ListBase(); // 16
  public Scene scene; // ptr 1552
  public Scene newscene; // ptr 1552
  public int redraws_flag; // 4
  public int pad1; // 4
  public short full; // 2
  public short temp; // 2
  public short winid; // 2
  public short do_draw; // 2
  public short do_refresh; // 2
  public short do_draw_gesture; // 2
  public short do_draw_paintcursor; // 2
  public short do_draw_drag; // 2
  public short swap; // 2
  public short mainwin; // 2
  public short subwinactive; // 2
  public short pad; // 2
  public Object animtimer; // ptr (wmTimer) 0
  public Object context; // ptr 0
  public short[] handler = new short[8]; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    vertbase.read(buffer);
    edgebase.read(buffer);
    areabase.read(buffer);
    regionbase.read(buffer);
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    newscene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    redraws_flag = buffer.getInt();
    pad1 = buffer.getInt();
    full = buffer.getShort();
    temp = buffer.getShort();
    winid = buffer.getShort();
    do_draw = buffer.getShort();
    do_refresh = buffer.getShort();
    do_draw_gesture = buffer.getShort();
    do_draw_paintcursor = buffer.getShort();
    do_draw_drag = buffer.getShort();
    swap = buffer.getShort();
    mainwin = buffer.getShort();
    subwinactive = buffer.getShort();
    pad = buffer.getShort();
    animtimer = DNATools.ptr(buffer); // get ptr
    context = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<handler.length;i++) handler[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    vertbase.write(buffer);
    edgebase.write(buffer);
    areabase.write(buffer);
    regionbase.write(buffer);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(newscene!=null?newscene.hashCode():0);
    buffer.writeInt(redraws_flag);
    buffer.writeInt(pad1);
    buffer.writeShort(full);
    buffer.writeShort(temp);
    buffer.writeShort(winid);
    buffer.writeShort(do_draw);
    buffer.writeShort(do_refresh);
    buffer.writeShort(do_draw_gesture);
    buffer.writeShort(do_draw_paintcursor);
    buffer.writeShort(do_draw_drag);
    buffer.writeShort(swap);
    buffer.writeShort(mainwin);
    buffer.writeShort(subwinactive);
    buffer.writeShort(pad);
    buffer.writeInt(animtimer!=null?animtimer.hashCode():0);
    buffer.writeInt(context!=null?context.hashCode():0);
    for(int i=0;i<handler.length;i++) buffer.writeShort(handler[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (bScreen[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bScreen:\n");
    sb.append(super.toString());
    sb.append("  vertbase: ").append(vertbase).append("\n");
    sb.append("  edgebase: ").append(edgebase).append("\n");
    sb.append("  areabase: ").append(areabase).append("\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  newscene: ").append(newscene).append("\n");
    sb.append("  redraws_flag: ").append(redraws_flag).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  full: ").append(full).append("\n");
    sb.append("  temp: ").append(temp).append("\n");
    sb.append("  winid: ").append(winid).append("\n");
    sb.append("  do_draw: ").append(do_draw).append("\n");
    sb.append("  do_refresh: ").append(do_refresh).append("\n");
    sb.append("  do_draw_gesture: ").append(do_draw_gesture).append("\n");
    sb.append("  do_draw_paintcursor: ").append(do_draw_paintcursor).append("\n");
    sb.append("  do_draw_drag: ").append(do_draw_drag).append("\n");
    sb.append("  swap: ").append(swap).append("\n");
    sb.append("  mainwin: ").append(mainwin).append("\n");
    sb.append("  subwinactive: ").append(subwinactive).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  animtimer: ").append(animtimer).append("\n");
    sb.append("  context: ").append(context).append("\n");
    sb.append("  handler: ").append(Arrays.toString(handler)).append("\n");
    return sb.toString();
  }
  public bScreen copy() { try {return (bScreen)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

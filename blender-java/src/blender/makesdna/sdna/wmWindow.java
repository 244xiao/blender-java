package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmWindow extends Link<wmWindow> implements DNA, Cloneable { // #360
  public wmWindow[] myarray;
  public Object ghostwin; // ptr 0
  public int winid; // 4
  public short grabcursor; // 2
  public short pad; // 2
  public bScreen screen; // ptr 216
  public bScreen newscreen; // ptr 216
  public byte[] screenname = new byte[32]; // 1
  public short posx; // 2
  public short posy; // 2
  public short sizex; // 2
  public short sizey; // 2
  public short windowstate; // 2
  public short monitor; // 2
  public short active; // 2
  public short cursor; // 2
  public short lastcursor; // 2
  public short modalcursor; // 2
  public short addmousemove; // 2
  public short pad2; // 2
  public Object eventstate; // ptr (wmEvent) 0
  public Object curswin; // ptr (wmSubWindow) 0
  public Object tweak; // ptr (wmGesture) 0
  public int drawmethod; // 4
  public int drawfail; // 4
  public Object drawdata; // ptr 0
  public ListBase queue = new ListBase(); // 16
  public ListBase handlers = new ListBase(); // 16
  public ListBase modalhandlers = new ListBase(); // 16
  public ListBase subwindows = new ListBase(); // 16
  public ListBase gesture = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), wmWindow.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), wmWindow.class); // get ptr
    ghostwin = DNATools.ptr(buffer); // get ptr
    winid = buffer.getInt();
    grabcursor = buffer.getShort();
    pad = buffer.getShort();
    screen = DNATools.link(DNATools.ptr(buffer), bScreen.class); // get ptr
    newscreen = DNATools.link(DNATools.ptr(buffer), bScreen.class); // get ptr
    buffer.get(screenname);
    posx = buffer.getShort();
    posy = buffer.getShort();
    sizex = buffer.getShort();
    sizey = buffer.getShort();
    windowstate = buffer.getShort();
    monitor = buffer.getShort();
    active = buffer.getShort();
    cursor = buffer.getShort();
    lastcursor = buffer.getShort();
    modalcursor = buffer.getShort();
    addmousemove = buffer.getShort();
    pad2 = buffer.getShort();
    eventstate = DNATools.ptr(buffer); // get ptr
    curswin = DNATools.ptr(buffer); // get ptr
    tweak = DNATools.ptr(buffer); // get ptr
    drawmethod = buffer.getInt();
    drawfail = buffer.getInt();
    drawdata = DNATools.ptr(buffer); // get ptr
    queue.read(buffer);
    handlers.read(buffer);
    modalhandlers.read(buffer);
    subwindows.read(buffer);
    gesture.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ghostwin!=null?ghostwin.hashCode():0);
    buffer.writeInt(winid);
    buffer.writeShort(grabcursor);
    buffer.writeShort(pad);
    buffer.writeInt(screen!=null?screen.hashCode():0);
    buffer.writeInt(newscreen!=null?newscreen.hashCode():0);
    buffer.write(screenname);
    buffer.writeShort(posx);
    buffer.writeShort(posy);
    buffer.writeShort(sizex);
    buffer.writeShort(sizey);
    buffer.writeShort(windowstate);
    buffer.writeShort(monitor);
    buffer.writeShort(active);
    buffer.writeShort(cursor);
    buffer.writeShort(lastcursor);
    buffer.writeShort(modalcursor);
    buffer.writeShort(addmousemove);
    buffer.writeShort(pad2);
    buffer.writeInt(eventstate!=null?eventstate.hashCode():0);
    buffer.writeInt(curswin!=null?curswin.hashCode():0);
    buffer.writeInt(tweak!=null?tweak.hashCode():0);
    buffer.writeInt(drawmethod);
    buffer.writeInt(drawfail);
    buffer.writeInt(drawdata!=null?drawdata.hashCode():0);
    queue.write(buffer);
    handlers.write(buffer);
    modalhandlers.write(buffer);
    subwindows.write(buffer);
    gesture.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (wmWindow[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmWindow:\n");
    sb.append("  ghostwin: ").append(ghostwin).append("\n");
    sb.append("  winid: ").append(winid).append("\n");
    sb.append("  grabcursor: ").append(grabcursor).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  screen: ").append(screen).append("\n");
    sb.append("  newscreen: ").append(newscreen).append("\n");
    sb.append("  screenname: ").append(new String(screenname)).append("\n");
    sb.append("  posx: ").append(posx).append("\n");
    sb.append("  posy: ").append(posy).append("\n");
    sb.append("  sizex: ").append(sizex).append("\n");
    sb.append("  sizey: ").append(sizey).append("\n");
    sb.append("  windowstate: ").append(windowstate).append("\n");
    sb.append("  monitor: ").append(monitor).append("\n");
    sb.append("  active: ").append(active).append("\n");
    sb.append("  cursor: ").append(cursor).append("\n");
    sb.append("  lastcursor: ").append(lastcursor).append("\n");
    sb.append("  modalcursor: ").append(modalcursor).append("\n");
    sb.append("  addmousemove: ").append(addmousemove).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  eventstate: ").append(eventstate).append("\n");
    sb.append("  curswin: ").append(curswin).append("\n");
    sb.append("  tweak: ").append(tweak).append("\n");
    sb.append("  drawmethod: ").append(drawmethod).append("\n");
    sb.append("  drawfail: ").append(drawfail).append("\n");
    sb.append("  drawdata: ").append(drawdata).append("\n");
    sb.append("  queue: ").append(queue).append("\n");
    sb.append("  handlers: ").append(handlers).append("\n");
    sb.append("  modalhandlers: ").append(modalhandlers).append("\n");
    sb.append("  subwindows: ").append(subwindows).append("\n");
    sb.append("  gesture: ").append(gesture).append("\n");
    return sb.toString();
  }
  public wmWindow copy() { try {return (wmWindow)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

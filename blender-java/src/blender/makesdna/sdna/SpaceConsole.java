package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceConsole extends SpaceLink implements DNA, Cloneable { // #175
  public SpaceConsole[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public int lheight; // 4
  public int pad; // 4
  public ListBase scrollback = new ListBase(); // 16
  public ListBase history = new ListBase(); // 16
  public byte[] prompt = new byte[256]; // 1
  public byte[] language = new byte[32]; // 1
  public int sel_start; // 4
  public int sel_end; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    lheight = buffer.getInt();
    pad = buffer.getInt();
    scrollback.read(buffer);
    history.read(buffer);
    buffer.get(prompt);
    buffer.get(language);
    sel_start = buffer.getInt();
    sel_end = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    buffer.writeInt(lheight);
    buffer.writeInt(pad);
    scrollback.write(buffer);
    history.write(buffer);
    buffer.write(prompt);
    buffer.write(language);
    buffer.writeInt(sel_start);
    buffer.writeInt(sel_end);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceConsole[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceConsole:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  lheight: ").append(lheight).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  scrollback: ").append(scrollback).append("\n");
    sb.append("  history: ").append(history).append("\n");
    sb.append("  prompt: ").append(new String(prompt)).append("\n");
    sb.append("  language: ").append(new String(language)).append("\n");
    sb.append("  sel_start: ").append(sel_start).append("\n");
    sb.append("  sel_end: ").append(sel_end).append("\n");
    return sb.toString();
  }
  public SpaceConsole copy() { try {return (SpaceConsole)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

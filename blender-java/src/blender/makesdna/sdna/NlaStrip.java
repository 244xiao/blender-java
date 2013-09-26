package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NlaStrip extends Link<NlaStrip> implements DNA, Cloneable { // #382
  public NlaStrip[] myarray;
  public ListBase strips = new ListBase(); // 16
  public bAction act; // ptr 152
  public AnimMapper remap; // ptr 40
  public ListBase fcurves = new ListBase(); // 16
  public ListBase modifiers = new ListBase(); // 16
  public byte[] name = new byte[64]; // 1
  public float influence; // 4
  public float strip_time; // 4
  public float start; // 4
  public float end; // 4
  public float actstart; // 4
  public float actend; // 4
  public float repeat; // 4
  public float scale; // 4
  public float blendin; // 4
  public float blendout; // 4
  public short blendmode; // 2
  public short extendmode; // 2
  public short pad1; // 2
  public short type; // 2
  public int flag; // 4
  public int pad2; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), NlaStrip.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), NlaStrip.class); // get ptr
    strips.read(buffer);
    act = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    remap = DNATools.link(DNATools.ptr(buffer), AnimMapper.class); // get ptr
    fcurves.read(buffer);
    modifiers.read(buffer);
    buffer.get(name);
    influence = buffer.getFloat();
    strip_time = buffer.getFloat();
    start = buffer.getFloat();
    end = buffer.getFloat();
    actstart = buffer.getFloat();
    actend = buffer.getFloat();
    repeat = buffer.getFloat();
    scale = buffer.getFloat();
    blendin = buffer.getFloat();
    blendout = buffer.getFloat();
    blendmode = buffer.getShort();
    extendmode = buffer.getShort();
    pad1 = buffer.getShort();
    type = buffer.getShort();
    flag = buffer.getInt();
    pad2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    strips.write(buffer);
    buffer.writeInt(act!=null?act.hashCode():0);
    buffer.writeInt(remap!=null?remap.hashCode():0);
    fcurves.write(buffer);
    modifiers.write(buffer);
    buffer.write(name);
    buffer.writeFloat(influence);
    buffer.writeFloat(strip_time);
    buffer.writeFloat(start);
    buffer.writeFloat(end);
    buffer.writeFloat(actstart);
    buffer.writeFloat(actend);
    buffer.writeFloat(repeat);
    buffer.writeFloat(scale);
    buffer.writeFloat(blendin);
    buffer.writeFloat(blendout);
    buffer.writeShort(blendmode);
    buffer.writeShort(extendmode);
    buffer.writeShort(pad1);
    buffer.writeShort(type);
    buffer.writeInt(flag);
    buffer.writeInt(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (NlaStrip[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NlaStrip:\n");
    sb.append("  strips: ").append(strips).append("\n");
    sb.append("  act: ").append(act).append("\n");
    sb.append("  remap: ").append(remap).append("\n");
    sb.append("  fcurves: ").append(fcurves).append("\n");
    sb.append("  modifiers: ").append(modifiers).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  influence: ").append(influence).append("\n");
    sb.append("  strip_time: ").append(strip_time).append("\n");
    sb.append("  start: ").append(start).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  actstart: ").append(actstart).append("\n");
    sb.append("  actend: ").append(actend).append("\n");
    sb.append("  repeat: ").append(repeat).append("\n");
    sb.append("  scale: ").append(scale).append("\n");
    sb.append("  blendin: ").append(blendin).append("\n");
    sb.append("  blendout: ").append(blendout).append("\n");
    sb.append("  blendmode: ").append(blendmode).append("\n");
    sb.append("  extendmode: ").append(extendmode).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public NlaStrip copy() { try {return (NlaStrip)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

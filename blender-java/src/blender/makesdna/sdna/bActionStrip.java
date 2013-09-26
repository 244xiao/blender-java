package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionStrip extends Link<bActionStrip> implements DNA, Cloneable { // #304
  public bActionStrip[] myarray;
  public short flag; // 2
  public short mode; // 2
  public short stride_axis; // 2
  public short curmod; // 2
  public Ipo ipo; // ptr 112
  public bAction act; // ptr 152
  public bObject object; // ptr 1296
  public float start; // 4
  public float end; // 4
  public float actstart; // 4
  public float actend; // 4
  public float actoffs; // 4
  public float stridelen; // 4
  public float repeat; // 4
  public float scale; // 4
  public float blendin; // 4
  public float blendout; // 4
  public byte[] stridechannel = new byte[32]; // 1
  public byte[] offs_bone = new byte[32]; // 1
  public ListBase modifiers = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bActionStrip.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bActionStrip.class); // get ptr
    flag = buffer.getShort();
    mode = buffer.getShort();
    stride_axis = buffer.getShort();
    curmod = buffer.getShort();
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    act = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    start = buffer.getFloat();
    end = buffer.getFloat();
    actstart = buffer.getFloat();
    actend = buffer.getFloat();
    actoffs = buffer.getFloat();
    stridelen = buffer.getFloat();
    repeat = buffer.getFloat();
    scale = buffer.getFloat();
    blendin = buffer.getFloat();
    blendout = buffer.getFloat();
    buffer.get(stridechannel);
    buffer.get(offs_bone);
    modifiers.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(flag);
    buffer.writeShort(mode);
    buffer.writeShort(stride_axis);
    buffer.writeShort(curmod);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(act!=null?act.hashCode():0);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeFloat(start);
    buffer.writeFloat(end);
    buffer.writeFloat(actstart);
    buffer.writeFloat(actend);
    buffer.writeFloat(actoffs);
    buffer.writeFloat(stridelen);
    buffer.writeFloat(repeat);
    buffer.writeFloat(scale);
    buffer.writeFloat(blendin);
    buffer.writeFloat(blendout);
    buffer.write(stridechannel);
    buffer.write(offs_bone);
    modifiers.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionStrip[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionStrip:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  stride_axis: ").append(stride_axis).append("\n");
    sb.append("  curmod: ").append(curmod).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  act: ").append(act).append("\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  start: ").append(start).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  actstart: ").append(actstart).append("\n");
    sb.append("  actend: ").append(actend).append("\n");
    sb.append("  actoffs: ").append(actoffs).append("\n");
    sb.append("  stridelen: ").append(stridelen).append("\n");
    sb.append("  repeat: ").append(repeat).append("\n");
    sb.append("  scale: ").append(scale).append("\n");
    sb.append("  blendin: ").append(blendin).append("\n");
    sb.append("  blendout: ").append(blendout).append("\n");
    sb.append("  stridechannel: ").append(new String(stridechannel)).append("\n");
    sb.append("  offs_bone: ").append(new String(offs_bone)).append("\n");
    sb.append("  modifiers: ").append(modifiers).append("\n");
    return sb.toString();
  }
  public bActionStrip copy() { try {return (bActionStrip)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

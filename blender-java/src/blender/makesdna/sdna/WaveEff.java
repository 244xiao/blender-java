package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class WaveEff extends Link<WaveEff> implements DNA, Cloneable { // #215
  public WaveEff[] myarray;
  public short type; // 2
  public short flag; // 2
  public short buttype; // 2
  public short stype; // 2
  public float startx; // 4
  public float starty; // 4
  public float height; // 4
  public float width; // 4
  public float narrow; // 4
  public float speed; // 4
  public float minfac; // 4
  public float damp; // 4
  public float timeoffs; // 4
  public float lifetime; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), WaveEff.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), WaveEff.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    buttype = buffer.getShort();
    stype = buffer.getShort();
    startx = buffer.getFloat();
    starty = buffer.getFloat();
    height = buffer.getFloat();
    width = buffer.getFloat();
    narrow = buffer.getFloat();
    speed = buffer.getFloat();
    minfac = buffer.getFloat();
    damp = buffer.getFloat();
    timeoffs = buffer.getFloat();
    lifetime = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(buttype);
    buffer.writeShort(stype);
    buffer.writeFloat(startx);
    buffer.writeFloat(starty);
    buffer.writeFloat(height);
    buffer.writeFloat(width);
    buffer.writeFloat(narrow);
    buffer.writeFloat(speed);
    buffer.writeFloat(minfac);
    buffer.writeFloat(damp);
    buffer.writeFloat(timeoffs);
    buffer.writeFloat(lifetime);
  }
  public Object setmyarray(Object array) {
    myarray = (WaveEff[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("WaveEff:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  buttype: ").append(buttype).append("\n");
    sb.append("  stype: ").append(stype).append("\n");
    sb.append("  startx: ").append(startx).append("\n");
    sb.append("  starty: ").append(starty).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  narrow: ").append(narrow).append("\n");
    sb.append("  speed: ").append(speed).append("\n");
    sb.append("  minfac: ").append(minfac).append("\n");
    sb.append("  damp: ").append(damp).append("\n");
    sb.append("  timeoffs: ").append(timeoffs).append("\n");
    sb.append("  lifetime: ").append(lifetime).append("\n");
    return sb.toString();
  }
  public WaveEff copy() { try {return (WaveEff)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

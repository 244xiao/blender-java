package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceSound extends SpaceLink implements DNA, Cloneable { // #177
  public SpaceSound[] myarray;
  public float blockscale; // 4
  public ScrArea area; // ptr 160
  public View2D v2d = new View2D(); // 144
  public bSound sound; // ptr 392
  public short mode; // 2
  public short sndnr; // 2
  public short xof; // 2
  public short yof; // 2
  public short flag; // 2
  public short lock; // 2
  public int pad2; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    area = DNATools.link(DNATools.ptr(buffer), ScrArea.class); // get ptr
    v2d.read(buffer);
    sound = DNATools.link(DNATools.ptr(buffer), bSound.class); // get ptr
    mode = buffer.getShort();
    sndnr = buffer.getShort();
    xof = buffer.getShort();
    yof = buffer.getShort();
    flag = buffer.getShort();
    lock = buffer.getShort();
    pad2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    buffer.writeInt(area!=null?area.hashCode():0);
    v2d.write(buffer);
    buffer.writeInt(sound!=null?sound.hashCode():0);
    buffer.writeShort(mode);
    buffer.writeShort(sndnr);
    buffer.writeShort(xof);
    buffer.writeShort(yof);
    buffer.writeShort(flag);
    buffer.writeShort(lock);
    buffer.writeInt(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceSound[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceSound:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  area: ").append(area).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  sound: ").append(sound).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  sndnr: ").append(sndnr).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  lock: ").append(lock).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public SpaceSound copy() { try {return (SpaceSound)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

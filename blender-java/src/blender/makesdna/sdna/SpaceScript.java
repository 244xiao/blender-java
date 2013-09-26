package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceScript extends SpaceLink implements DNA, Cloneable { // #168
  public SpaceScript[] myarray;
  public float blockscale; // 4
  public Script script; // ptr 632
  public short flags; // 2
  public short menunr; // 2
  public int pad1; // 4
  public Object but_refs; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    script = DNATools.link(DNATools.ptr(buffer), Script.class); // get ptr
    flags = buffer.getShort();
    menunr = buffer.getShort();
    pad1 = buffer.getInt();
    but_refs = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    buffer.writeInt(script!=null?script.hashCode():0);
    buffer.writeShort(flags);
    buffer.writeShort(menunr);
    buffer.writeInt(pad1);
    buffer.writeInt(but_refs!=null?but_refs.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceScript[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceScript:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  script: ").append(script).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  menunr: ").append(menunr).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  but_refs: ").append(but_refs).append("\n");
    return sb.toString();
  }
  public SpaceScript copy() { try {return (SpaceScript)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

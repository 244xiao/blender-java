package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionConstraint implements DNA, Cloneable { // #288
  public bActionConstraint[] myarray;
  public bObject tar; // ptr 1296
  public short type; // 2
  public short local; // 2
  public int start; // 4
  public int end; // 4
  public float min; // 4
  public float max; // 4
  public int pad; // 4
  public bAction act; // ptr 152
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    type = buffer.getShort();
    local = buffer.getShort();
    start = buffer.getInt();
    end = buffer.getInt();
    min = buffer.getFloat();
    max = buffer.getFloat();
    pad = buffer.getInt();
    act = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(local);
    buffer.writeInt(start);
    buffer.writeInt(end);
    buffer.writeFloat(min);
    buffer.writeFloat(max);
    buffer.writeInt(pad);
    buffer.writeInt(act!=null?act.hashCode():0);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  local: ").append(local).append("\n");
    sb.append("  start: ").append(start).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  min: ").append(min).append("\n");
    sb.append("  max: ").append(max).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  act: ").append(act).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bActionConstraint copy() { try {return (bActionConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

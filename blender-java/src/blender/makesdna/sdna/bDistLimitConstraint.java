package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bDistLimitConstraint implements DNA, Cloneable { // #301
  public bDistLimitConstraint[] myarray;
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1
  public float dist; // 4
  public float soft; // 4
  public short flag; // 2
  public short mode; // 2
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
    dist = buffer.getFloat();
    soft = buffer.getFloat();
    flag = buffer.getShort();
    mode = buffer.getShort();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
    buffer.writeFloat(dist);
    buffer.writeFloat(soft);
    buffer.writeShort(flag);
    buffer.writeShort(mode);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bDistLimitConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bDistLimitConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  soft: ").append(soft).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bDistLimitConstraint copy() { try {return (bDistLimitConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

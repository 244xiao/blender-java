package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMinMaxConstraint implements DNA, Cloneable { // #287
  public bMinMaxConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int minmaxflag; // 4
  public float offset; // 4
  public int flag; // 4
  public short sticky; // 2
  public short stuck; // 2
  public short pad1; // 2
  public short pad2; // 2
  public float[] cache = new float[3]; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    minmaxflag = buffer.getInt();
    offset = buffer.getFloat();
    flag = buffer.getInt();
    sticky = buffer.getShort();
    stuck = buffer.getShort();
    pad1 = buffer.getShort();
    pad2 = buffer.getShort();
    for(int i=0;i<cache.length;i++) cache[i]=buffer.getFloat();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(minmaxflag);
    buffer.writeFloat(offset);
    buffer.writeInt(flag);
    buffer.writeShort(sticky);
    buffer.writeShort(stuck);
    buffer.writeShort(pad1);
    buffer.writeShort(pad2);
    for(int i=0;i<cache.length;i++) buffer.writeFloat(cache[i]);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bMinMaxConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMinMaxConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  minmaxflag: ").append(minmaxflag).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sticky: ").append(sticky).append("\n");
    sb.append("  stuck: ").append(stuck).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  cache: ").append(Arrays.toString(cache)).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bMinMaxConstraint copy() { try {return (bMinMaxConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bFollowPathConstraint implements DNA, Cloneable { // #291
  public bFollowPathConstraint[] myarray;
  public bObject tar; // ptr 1296
  public float offset; // 4
  public float offset_fac; // 4
  public int followflag; // 4
  public short trackflag; // 2
  public short upflag; // 2

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    offset = buffer.getFloat();
    offset_fac = buffer.getFloat();
    followflag = buffer.getInt();
    trackflag = buffer.getShort();
    upflag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeFloat(offset);
    buffer.writeFloat(offset_fac);
    buffer.writeInt(followflag);
    buffer.writeShort(trackflag);
    buffer.writeShort(upflag);
  }
  public Object setmyarray(Object array) {
    myarray = (bFollowPathConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bFollowPathConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  offset_fac: ").append(offset_fac).append("\n");
    sb.append("  followflag: ").append(followflag).append("\n");
    sb.append("  trackflag: ").append(trackflag).append("\n");
    sb.append("  upflag: ").append(upflag).append("\n");
    return sb.toString();
  }
  public bFollowPathConstraint copy() { try {return (bFollowPathConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

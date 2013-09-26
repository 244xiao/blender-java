package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bDampTrackConstraint implements DNA, Cloneable { // #290
  public bDampTrackConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int trackflag; // 4
  public int pad; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    trackflag = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(trackflag);
    buffer.writeInt(pad);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bDampTrackConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bDampTrackConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  trackflag: ").append(trackflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bDampTrackConstraint copy() { try {return (bDampTrackConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

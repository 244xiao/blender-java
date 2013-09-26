package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bLockTrackConstraint implements DNA, Cloneable { // #289
  public bLockTrackConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int trackflag; // 4
  public int lockflag; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    trackflag = buffer.getInt();
    lockflag = buffer.getInt();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(trackflag);
    buffer.writeInt(lockflag);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bLockTrackConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bLockTrackConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  trackflag: ").append(trackflag).append("\n");
    sb.append("  lockflag: ").append(lockflag).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bLockTrackConstraint copy() { try {return (bLockTrackConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

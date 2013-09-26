package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTrackToConstraint implements DNA, Cloneable { // #281
  public bTrackToConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int reserved1; // 4
  public int reserved2; // 4
  public int flags; // 4
  public int pad; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    reserved1 = buffer.getInt();
    reserved2 = buffer.getInt();
    flags = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(reserved1);
    buffer.writeInt(reserved2);
    buffer.writeInt(flags);
    buffer.writeInt(pad);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bTrackToConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTrackToConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  reserved1: ").append(reserved1).append("\n");
    sb.append("  reserved2: ").append(reserved2).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bTrackToConstraint copy() { try {return (bTrackToConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

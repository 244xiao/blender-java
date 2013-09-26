package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTransLikeConstraint implements DNA, Cloneable { // #286
  public bTransLikeConstraint[] myarray;
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bTransLikeConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTransLikeConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bTransLikeConstraint copy() { try {return (bTransLikeConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

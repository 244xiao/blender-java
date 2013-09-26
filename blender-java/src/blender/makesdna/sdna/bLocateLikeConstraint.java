package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bLocateLikeConstraint implements DNA, Cloneable { // #283
  public bLocateLikeConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int flag; // 4
  public int reserved1; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    flag = buffer.getInt();
    reserved1 = buffer.getInt();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(reserved1);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bLocateLikeConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bLocateLikeConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  reserved1: ").append(reserved1).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bLocateLikeConstraint copy() { try {return (bLocateLikeConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

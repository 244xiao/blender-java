package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bClampToConstraint implements DNA, Cloneable { // #294
  public bClampToConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int flag; // 4
  public int flag2; // 4

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    flag = buffer.getInt();
    flag2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(flag2);
  }
  public Object setmyarray(Object array) {
    myarray = (bClampToConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bClampToConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  flag2: ").append(flag2).append("\n");
    return sb.toString();
  }
  public bClampToConstraint copy() { try {return (bClampToConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

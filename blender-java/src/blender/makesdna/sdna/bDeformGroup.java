package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bDeformGroup extends Link<bDeformGroup> implements DNA, Cloneable { // #112
  public bDeformGroup[] myarray;
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bDeformGroup.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bDeformGroup.class); // get ptr
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (bDeformGroup[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bDeformGroup:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public bDeformGroup copy() { try {return (bDeformGroup)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bAddon extends Link<bAddon> implements DNA, Cloneable { // #187
  public bAddon[] myarray;
  public byte[] module = new byte[64]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bAddon.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bAddon.class); // get ptr
    buffer.get(module);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(module);
  }
  public Object setmyarray(Object array) {
    myarray = (bAddon[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bAddon:\n");
    sb.append("  module: ").append(new String(module)).append("\n");
    return sb.toString();
  }
  public bAddon copy() { try {return (bAddon)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

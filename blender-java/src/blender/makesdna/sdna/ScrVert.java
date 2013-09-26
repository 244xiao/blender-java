package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ScrVert extends Link<ScrVert> implements DNA, Cloneable { // #191
  public ScrVert[] myarray;
  public ScrVert newv; // ptr 32
  public vec2s vec = new vec2s(); // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    newv = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    vec.read(buffer);
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(newv!=null?newv.hashCode():0);
    vec.write(buffer);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (ScrVert[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ScrVert:\n");
    sb.append("  newv: ").append(newv).append("\n");
    sb.append("  vec: ").append(vec).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public ScrVert copy() { try {return (ScrVert)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

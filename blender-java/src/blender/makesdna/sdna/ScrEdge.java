package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ScrEdge extends Link<ScrEdge> implements DNA, Cloneable { // #192
  public ScrEdge[] myarray;
  public ScrVert v1; // ptr 32
  public ScrVert v2; // ptr 32
  public short border; // 2
  public short flag; // 2
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ScrEdge.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ScrEdge.class); // get ptr
    v1 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    v2 = DNATools.link(DNATools.ptr(buffer), ScrVert.class); // get ptr
    border = buffer.getShort();
    flag = buffer.getShort();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(v1!=null?v1.hashCode():0);
    buffer.writeInt(v2!=null?v2.hashCode():0);
    buffer.writeShort(border);
    buffer.writeShort(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (ScrEdge[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ScrEdge:\n");
    sb.append("  v1: ").append(v1).append("\n");
    sb.append("  v2: ").append(v2).append("\n");
    sb.append("  border: ").append(border).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public ScrEdge copy() { try {return (ScrEdge)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

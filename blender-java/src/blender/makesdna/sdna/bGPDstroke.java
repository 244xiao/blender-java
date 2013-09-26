package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGPDstroke extends Link<bGPDstroke> implements DNA, Cloneable { // #354
  public bGPDstroke[] myarray;
  public bGPDspoint points; // ptr 16
  public int totpoints; // 4
  public short thickness; // 2
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bGPDstroke.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bGPDstroke.class); // get ptr
    points = DNATools.link(DNATools.ptr(buffer), bGPDspoint.class); // get ptr
    totpoints = buffer.getInt();
    thickness = buffer.getShort();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(points!=null?points.hashCode():0);
    buffer.writeInt(totpoints);
    buffer.writeShort(thickness);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bGPDstroke[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGPDstroke:\n");
    sb.append("  points: ").append(points).append("\n");
    sb.append("  totpoints: ").append(totpoints).append("\n");
    sb.append("  thickness: ").append(thickness).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bGPDstroke copy() { try {return (bGPDstroke)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

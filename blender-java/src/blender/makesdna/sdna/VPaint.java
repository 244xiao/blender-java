package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class VPaint implements DNA, Cloneable { // #146
  public VPaint[] myarray;
  public Paint paint = new Paint(); // 24
  public short flag; // 2
  public short pad; // 2
  public int tot; // 4
  public Object vpaint_prev; // ptr 4
  public MDeformVert wpaint_prev; // ptr 16
  public Object paintcursor; // ptr 0

  public void read(ByteBuffer buffer) {
    paint.read(buffer);
    flag = buffer.getShort();
    pad = buffer.getShort();
    tot = buffer.getInt();
    vpaint_prev = DNATools.ptr(buffer); // get ptr
    wpaint_prev = DNATools.link(DNATools.ptr(buffer), MDeformVert.class); // get ptr
    paintcursor = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    paint.write(buffer);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
    buffer.writeInt(tot);
    buffer.writeInt(vpaint_prev!=null?vpaint_prev.hashCode():0);
    buffer.writeInt(wpaint_prev!=null?wpaint_prev.hashCode():0);
    buffer.writeInt(paintcursor!=null?paintcursor.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (VPaint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("VPaint:\n");
    sb.append("  paint: ").append(paint).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  tot: ").append(tot).append("\n");
    sb.append("  vpaint_prev: ").append(vpaint_prev).append("\n");
    sb.append("  wpaint_prev: ").append(wpaint_prev).append("\n");
    sb.append("  paintcursor: ").append(paintcursor).append("\n");
    return sb.toString();
  }
  public VPaint copy() { try {return (VPaint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGPDframe extends Link<bGPDframe> implements DNA, Cloneable { // #355
  public bGPDframe[] myarray;
  public ListBase strokes = new ListBase(); // 16
  public int framenum; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bGPDframe.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bGPDframe.class); // get ptr
    strokes.read(buffer);
    framenum = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    strokes.write(buffer);
    buffer.writeInt(framenum);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bGPDframe[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGPDframe:\n");
    sb.append("  strokes: ").append(strokes).append("\n");
    sb.append("  framenum: ").append(framenum).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bGPDframe copy() { try {return (bGPDframe)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

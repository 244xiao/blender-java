package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ConsoleLine extends Link<ConsoleLine> implements DNA, Cloneable { // #174
  public ConsoleLine[] myarray;
  public int len_alloc; // 4
  public int len; // 4
  public Object line; // ptr 1
  public int cursor; // 4
  public int type; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ConsoleLine.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ConsoleLine.class); // get ptr
    len_alloc = buffer.getInt();
    len = buffer.getInt();
    line = DNATools.ptr(buffer); // get ptr
    cursor = buffer.getInt();
    type = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(len_alloc);
    buffer.writeInt(len);
    buffer.writeInt(line!=null?line.hashCode():0);
    buffer.writeInt(cursor);
    buffer.writeInt(type);
  }
  public Object setmyarray(Object array) {
    myarray = (ConsoleLine[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ConsoleLine:\n");
    sb.append("  len_alloc: ").append(len_alloc).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  line: ").append(line).append("\n");
    sb.append("  cursor: ").append(cursor).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public ConsoleLine copy() { try {return (ConsoleLine)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

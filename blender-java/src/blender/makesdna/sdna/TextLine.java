package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TextLine extends Link<TextLine> implements DNA, Cloneable { // #17
  public TextLine[] myarray;
  public Object line; // ptr 1
  public Object format; // ptr 1
  public int len; // 4
  public int blen; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), TextLine.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), TextLine.class); // get ptr
    line = DNATools.ptr(buffer); // get ptr
    format = DNATools.ptr(buffer); // get ptr
    len = buffer.getInt();
    blen = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(line!=null?line.hashCode():0);
    buffer.writeInt(format!=null?format.hashCode():0);
    buffer.writeInt(len);
    buffer.writeInt(blen);
  }
  public Object setmyarray(Object array) {
    myarray = (TextLine[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TextLine:\n");
    sb.append("  line: ").append(line).append("\n");
    sb.append("  format: ").append(format).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  blen: ").append(blen).append("\n");
    return sb.toString();
  }
  public TextLine copy() { try {return (TextLine)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

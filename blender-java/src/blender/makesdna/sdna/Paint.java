package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Paint implements DNA, Cloneable { // #140
  public Paint[] myarray;
  public Brush brush; // ptr 760
  public Object paint_cursor; // ptr 0
  public byte[] paint_cursor_col = new byte[4]; // 1
  public int flags; // 4

  public void read(ByteBuffer buffer) {
    brush = DNATools.link(DNATools.ptr(buffer), Brush.class); // get ptr
    paint_cursor = DNATools.ptr(buffer); // get ptr
    buffer.get(paint_cursor_col);
    flags = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(brush!=null?brush.hashCode():0);
    buffer.writeInt(paint_cursor!=null?paint_cursor.hashCode():0);
    buffer.write(paint_cursor_col);
    buffer.writeInt(flags);
  }
  public Object setmyarray(Object array) {
    myarray = (Paint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Paint:\n");
    sb.append("  brush: ").append(brush).append("\n");
    sb.append("  paint_cursor: ").append(paint_cursor).append("\n");
    sb.append("  paint_cursor_col: ").append(new String(paint_cursor_col)).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    return sb.toString();
  }
  public Paint copy() { try {return (Paint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

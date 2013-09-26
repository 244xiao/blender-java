package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Text extends ID implements DNA, Cloneable { // #19
  public Text[] myarray;
  public ID id = (ID)this;
  public Object name; // ptr 1
  public int flags; // 4
  public int nlines; // 4
  public ListBase lines = new ListBase(); // 16
  public TextLine curl; // ptr 40
  public TextLine sell; // ptr 40
  public int curc; // 4
  public int selc; // 4
  public ListBase markers = new ListBase(); // 16
  public Object undo_buf; // ptr 1
  public int undo_pos; // 4
  public int undo_len; // 4
  public Object compiled; // ptr 0
  public double mtime; // 8

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    name = DNATools.ptr(buffer); // get ptr
    flags = buffer.getInt();
    nlines = buffer.getInt();
    lines.read(buffer);
    curl = DNATools.link(DNATools.ptr(buffer), TextLine.class); // get ptr
    sell = DNATools.link(DNATools.ptr(buffer), TextLine.class); // get ptr
    curc = buffer.getInt();
    selc = buffer.getInt();
    markers.read(buffer);
    undo_buf = DNATools.ptr(buffer); // get ptr
    undo_pos = buffer.getInt();
    undo_len = buffer.getInt();
    compiled = DNATools.ptr(buffer); // get ptr
    mtime = buffer.getDouble();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(name!=null?name.hashCode():0);
    buffer.writeInt(flags);
    buffer.writeInt(nlines);
    lines.write(buffer);
    buffer.writeInt(curl!=null?curl.hashCode():0);
    buffer.writeInt(sell!=null?sell.hashCode():0);
    buffer.writeInt(curc);
    buffer.writeInt(selc);
    markers.write(buffer);
    buffer.writeInt(undo_buf!=null?undo_buf.hashCode():0);
    buffer.writeInt(undo_pos);
    buffer.writeInt(undo_len);
    buffer.writeInt(compiled!=null?compiled.hashCode():0);
    buffer.writeDouble(mtime);
  }
  public Object setmyarray(Object array) {
    myarray = (Text[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Text:\n");
    sb.append(super.toString());
    sb.append("  name: ").append(name).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  nlines: ").append(nlines).append("\n");
    sb.append("  lines: ").append(lines).append("\n");
    sb.append("  curl: ").append(curl).append("\n");
    sb.append("  sell: ").append(sell).append("\n");
    sb.append("  curc: ").append(curc).append("\n");
    sb.append("  selc: ").append(selc).append("\n");
    sb.append("  markers: ").append(markers).append("\n");
    sb.append("  undo_buf: ").append(undo_buf).append("\n");
    sb.append("  undo_pos: ").append(undo_pos).append("\n");
    sb.append("  undo_len: ").append(undo_len).append("\n");
    sb.append("  compiled: ").append(compiled).append("\n");
    sb.append("  mtime: ").append(mtime).append("\n");
    return sb.toString();
  }
  public Text copy() { try {return (Text)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

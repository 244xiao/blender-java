package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TextMarker extends Link<TextMarker> implements DNA, Cloneable { // #18
  public TextMarker[] myarray;
  public int lineno; // 4
  public int start; // 4
  public int end; // 4
  public int pad1; // 4
  public int group; // 4
  public int flags; // 4
  public byte[] color = new byte[4]; // 1
  public byte[] pad = new byte[4]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), TextMarker.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), TextMarker.class); // get ptr
    lineno = buffer.getInt();
    start = buffer.getInt();
    end = buffer.getInt();
    pad1 = buffer.getInt();
    group = buffer.getInt();
    flags = buffer.getInt();
    buffer.get(color);
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(lineno);
    buffer.writeInt(start);
    buffer.writeInt(end);
    buffer.writeInt(pad1);
    buffer.writeInt(group);
    buffer.writeInt(flags);
    buffer.write(color);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (TextMarker[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TextMarker:\n");
    sb.append("  lineno: ").append(lineno).append("\n");
    sb.append("  start: ").append(start).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  group: ").append(group).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  color: ").append(new String(color)).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public TextMarker copy() { try {return (TextMarker)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

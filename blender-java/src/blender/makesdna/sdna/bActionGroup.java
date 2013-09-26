package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionGroup extends Link<bActionGroup> implements DNA, Cloneable { // #270
  public bActionGroup[] myarray;
  public ListBase channels = new ListBase(); // 16
  public int flag; // 4
  public int customCol; // 4
  public byte[] name = new byte[64]; // 1
  public ThemeWireColor cs = new ThemeWireColor(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bActionGroup.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bActionGroup.class); // get ptr
    channels.read(buffer);
    flag = buffer.getInt();
    customCol = buffer.getInt();
    buffer.get(name);
    cs.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    channels.write(buffer);
    buffer.writeInt(flag);
    buffer.writeInt(customCol);
    buffer.write(name);
    cs.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionGroup[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionGroup:\n");
    sb.append("  channels: ").append(channels).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  customCol: ").append(customCol).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  cs: ").append(cs).append("\n");
    return sb.toString();
  }
  public bActionGroup copy() { try {return (bActionGroup)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

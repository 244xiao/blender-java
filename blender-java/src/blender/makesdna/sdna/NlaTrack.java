package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NlaTrack extends Link<NlaTrack> implements DNA, Cloneable { // #383
  public NlaTrack[] myarray;
  public ListBase strips = new ListBase(); // 16
  public int flag; // 4
  public int index; // 4
  public byte[] name = new byte[64]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), NlaTrack.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), NlaTrack.class); // get ptr
    strips.read(buffer);
    flag = buffer.getInt();
    index = buffer.getInt();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    strips.write(buffer);
    buffer.writeInt(flag);
    buffer.writeInt(index);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (NlaTrack[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NlaTrack:\n");
    sb.append("  strips: ").append(strips).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  index: ").append(index).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public NlaTrack copy() { try {return (NlaTrack)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

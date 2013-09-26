package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bDopeSheet implements DNA, Cloneable { // #272
  public bDopeSheet[] myarray;
  public Object source; // ptr 72
  public ListBase chanbase = new ListBase(); // 16
  public Group filter_grp; // ptr 104
  public byte[] searchstr = new byte[64]; // 1
  public int filterflag; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    source = DNATools.ptr(buffer); // get ptr
    chanbase.read(buffer);
    filter_grp = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    buffer.get(searchstr);
    filterflag = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(source!=null?source.hashCode():0);
    chanbase.write(buffer);
    buffer.writeInt(filter_grp!=null?filter_grp.hashCode():0);
    buffer.write(searchstr);
    buffer.writeInt(filterflag);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bDopeSheet[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bDopeSheet:\n");
    sb.append("  source: ").append(source).append("\n");
    sb.append("  chanbase: ").append(chanbase).append("\n");
    sb.append("  filter_grp: ").append(filter_grp).append("\n");
    sb.append("  searchstr: ").append(new String(searchstr)).append("\n");
    sb.append("  filterflag: ").append(filterflag).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bDopeSheet copy() { try {return (bDopeSheet)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

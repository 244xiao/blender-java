package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class KeyingSet extends Link<KeyingSet> implements DNA, Cloneable { // #385
  public KeyingSet[] myarray;
  public ListBase paths = new ListBase(); // 16
  public byte[] name = new byte[64]; // 1
  public byte[] typeinfo = new byte[64]; // 1
  public short flag; // 2
  public short keyingflag; // 2
  public int active_path; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), KeyingSet.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), KeyingSet.class); // get ptr
    paths.read(buffer);
    buffer.get(name);
    buffer.get(typeinfo);
    flag = buffer.getShort();
    keyingflag = buffer.getShort();
    active_path = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    paths.write(buffer);
    buffer.write(name);
    buffer.write(typeinfo);
    buffer.writeShort(flag);
    buffer.writeShort(keyingflag);
    buffer.writeInt(active_path);
  }
  public Object setmyarray(Object array) {
    myarray = (KeyingSet[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("KeyingSet:\n");
    sb.append("  paths: ").append(paths).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  typeinfo: ").append(new String(typeinfo)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  keyingflag: ").append(keyingflag).append("\n");
    sb.append("  active_path: ").append(active_path).append("\n");
    return sb.toString();
  }
  public KeyingSet copy() { try {return (KeyingSet)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

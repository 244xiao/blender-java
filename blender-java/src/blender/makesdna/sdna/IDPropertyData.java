package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IDPropertyData implements DNA, Cloneable { // #7
  public IDPropertyData[] myarray;
  public Object pointer; // ptr 0
  public ListBase group = new ListBase(); // 16
  public int val; // 4
  public int val2; // 4

  public void read(ByteBuffer buffer) {
    pointer = DNATools.ptr(buffer); // get ptr
    group.read(buffer);
    val = buffer.getInt();
    val2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(pointer!=null?pointer.hashCode():0);
    group.write(buffer);
    buffer.writeInt(val);
    buffer.writeInt(val2);
  }
  public Object setmyarray(Object array) {
    myarray = (IDPropertyData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("IDPropertyData:\n");
    sb.append("  pointer: ").append(pointer).append("\n");
    sb.append("  group: ").append(group).append("\n");
    sb.append("  val: ").append(val).append("\n");
    sb.append("  val2: ").append(val2).append("\n");
    return sb.toString();
  }
  public IDPropertyData copy() { try {return (IDPropertyData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRule extends Link<BoidRule> implements DNA, Cloneable { // #389
  public BoidRule[] myarray;
  public int type; // 4
  public int flag; // 4
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), BoidRule.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), BoidRule.class); // get ptr
    type = buffer.getInt();
    flag = buffer.getInt();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(type);
    buffer.writeInt(flag);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRule[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRule:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public BoidRule copy() { try {return (BoidRule)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

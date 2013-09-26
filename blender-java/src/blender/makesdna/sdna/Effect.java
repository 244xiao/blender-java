package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Effect extends Link<Effect> implements DNA, Cloneable { // #212
  public Effect[] myarray;
  public short type; // 2
  public short flag; // 2
  public short buttype; // 2
  public short rt; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Effect.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Effect.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    buttype = buffer.getShort();
    rt = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(buttype);
    buffer.writeShort(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (Effect[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Effect:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  buttype: ").append(buttype).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public Effect copy() { try {return (Effect)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

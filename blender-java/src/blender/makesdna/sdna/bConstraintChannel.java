package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bConstraintChannel extends Link<bConstraintChannel> implements DNA, Cloneable { // #275
  public bConstraintChannel[] myarray;
  public Ipo ipo; // ptr 112
  public short flag; // 2
  public byte[] name = new byte[30]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bConstraintChannel.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bConstraintChannel.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    flag = buffer.getShort();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeShort(flag);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (bConstraintChannel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bConstraintChannel:\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public bConstraintChannel copy() { try {return (bConstraintChannel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionChannel extends Link<bActionChannel> implements DNA, Cloneable { // #274
  public bActionChannel[] myarray;
  public bActionGroup grp; // ptr 120
  public Ipo ipo; // ptr 112
  public ListBase constraintChannels = new ListBase(); // 16
  public int flag; // 4
  public byte[] name = new byte[32]; // 1
  public int temp; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bActionChannel.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bActionChannel.class); // get ptr
    grp = DNATools.link(DNATools.ptr(buffer), bActionGroup.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    constraintChannels.read(buffer);
    flag = buffer.getInt();
    buffer.get(name);
    temp = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(grp!=null?grp.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    constraintChannels.write(buffer);
    buffer.writeInt(flag);
    buffer.write(name);
    buffer.writeInt(temp);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionChannel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionChannel:\n");
    sb.append("  grp: ").append(grp).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  constraintChannels: ").append(constraintChannels).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  temp: ").append(temp).append("\n");
    return sb.toString();
  }
  public bActionChannel copy() { try {return (bActionChannel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

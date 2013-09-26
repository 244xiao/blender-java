package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class DriverVar extends Link<DriverVar> implements DNA, Cloneable { // #376
  public DriverVar[] myarray;
  public byte[] name = new byte[64]; // 1
  public DriverTarget[] targets = new DriverTarget[8]; // 56
  public short num_targets; // 2
  public short type; // 2
  public float curval; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), DriverVar.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), DriverVar.class); // get ptr
    buffer.get(name);
    for(int i=0;i<targets.length;i++) { targets[i]=new DriverTarget(); targets[i].read(buffer); }
    num_targets = buffer.getShort();
    type = buffer.getShort();
    curval = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    for(int i=0;i<targets.length;i++) targets[i].write(buffer);
    buffer.writeShort(num_targets);
    buffer.writeShort(type);
    buffer.writeFloat(curval);
  }
  public Object setmyarray(Object array) {
    myarray = (DriverVar[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("DriverVar:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  targets: ").append(Arrays.toString(targets)).append("\n");
    sb.append("  num_targets: ").append(num_targets).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    return sb.toString();
  }
  public DriverVar copy() { try {return (DriverVar)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

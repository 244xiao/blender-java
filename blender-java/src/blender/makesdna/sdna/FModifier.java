package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FModifier extends Link<FModifier> implements DNA, Cloneable { // #365
  public FModifier[] myarray;
  public Object data; // ptr 0
  public Object edata; // ptr 0
  public byte[] name = new byte[64]; // 1
  public short type; // 2
  public short flag; // 2
  public float influence; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), FModifier.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), FModifier.class); // get ptr
    data = DNATools.ptr(buffer); // get ptr
    edata = DNATools.ptr(buffer); // get ptr
    buffer.get(name);
    type = buffer.getShort();
    flag = buffer.getShort();
    influence = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(edata!=null?edata.hashCode():0);
    buffer.write(name);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeFloat(influence);
  }
  public Object setmyarray(Object array) {
    myarray = (FModifier[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FModifier:\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  edata: ").append(edata).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  influence: ").append(influence).append("\n");
    return sb.toString();
  }
  public FModifier copy() { try {return (FModifier)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

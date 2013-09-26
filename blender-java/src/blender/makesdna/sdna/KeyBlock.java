package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class KeyBlock extends Link<KeyBlock> implements DNA, Cloneable { // #15
  public KeyBlock[] myarray;
  public float pos; // 4
  public float curval; // 4
  public short type; // 2
  public short adrcode; // 2
  public short relative; // 2
  public short flag; // 2
  public int totelem; // 4
  public int pad2; // 4
  public Object data; // ptr 0
  public Object weights; // ptr 4
  public byte[] name = new byte[32]; // 1
  public byte[] vgroup = new byte[32]; // 1
  public float slidermin; // 4
  public float slidermax; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), KeyBlock.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), KeyBlock.class); // get ptr
    pos = buffer.getFloat();
    curval = buffer.getFloat();
    type = buffer.getShort();
    adrcode = buffer.getShort();
    relative = buffer.getShort();
    flag = buffer.getShort();
    totelem = buffer.getInt();
    pad2 = buffer.getInt();
    data = DNATools.ptr(buffer); // get ptr
    weights = DNATools.ptr(buffer); // get ptr
    buffer.get(name);
    buffer.get(vgroup);
    slidermin = buffer.getFloat();
    slidermax = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeFloat(pos);
    buffer.writeFloat(curval);
    buffer.writeShort(type);
    buffer.writeShort(adrcode);
    buffer.writeShort(relative);
    buffer.writeShort(flag);
    buffer.writeInt(totelem);
    buffer.writeInt(pad2);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(weights!=null?weights.hashCode():0);
    buffer.write(name);
    buffer.write(vgroup);
    buffer.writeFloat(slidermin);
    buffer.writeFloat(slidermax);
  }
  public Object setmyarray(Object array) {
    myarray = (KeyBlock[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("KeyBlock:\n");
    sb.append("  pos: ").append(pos).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  adrcode: ").append(adrcode).append("\n");
    sb.append("  relative: ").append(relative).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  totelem: ").append(totelem).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  weights: ").append(weights).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  vgroup: ").append(new String(vgroup)).append("\n");
    sb.append("  slidermin: ").append(slidermin).append("\n");
    sb.append("  slidermax: ").append(slidermax).append("\n");
    return sb.toString();
  }
  public KeyBlock copy() { try {return (KeyBlock)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

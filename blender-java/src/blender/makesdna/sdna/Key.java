package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Key extends ID implements DNA, Cloneable { // #16
  public Key[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public KeyBlock refkey; // ptr 128
  public byte[] elemstr = new byte[32]; // 1
  public int elemsize; // 4
  public float curval; // 4
  public ListBase block = new ListBase(); // 16
  public Ipo ipo; // ptr 112
  public Object from; // ptr 72
  public short type; // 2
  public short totkey; // 2
  public short slurph; // 2
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    refkey = DNATools.link(DNATools.ptr(buffer), KeyBlock.class); // get ptr
    buffer.get(elemstr);
    elemsize = buffer.getInt();
    curval = buffer.getFloat();
    block.read(buffer);
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    from = DNATools.ptr(buffer); // get ptr
    type = buffer.getShort();
    totkey = buffer.getShort();
    slurph = buffer.getShort();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(refkey!=null?refkey.hashCode():0);
    buffer.write(elemstr);
    buffer.writeInt(elemsize);
    buffer.writeFloat(curval);
    block.write(buffer);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(from!=null?from.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(totkey);
    buffer.writeShort(slurph);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (Key[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Key:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  refkey: ").append(refkey).append("\n");
    sb.append("  elemstr: ").append(new String(elemstr)).append("\n");
    sb.append("  elemsize: ").append(elemsize).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    sb.append("  block: ").append(block).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  from: ").append(from).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  totkey: ").append(totkey).append("\n");
    sb.append("  slurph: ").append(slurph).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public Key copy() { try {return (Key)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class KS_Path extends Link<KS_Path> implements DNA, Cloneable { // #384
  public KS_Path[] myarray;
  public Object id; // ptr 72
  public byte[] group = new byte[64]; // 1
  public int idtype; // 4
  public short groupmode; // 2
  public short pad; // 2
  public Object rna_path; // ptr 1
  public int array_index; // 4
  public short flag; // 2
  public short keyingflag; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), KS_Path.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), KS_Path.class); // get ptr
    id = DNATools.ptr(buffer); // get ptr
    buffer.get(group);
    idtype = buffer.getInt();
    groupmode = buffer.getShort();
    pad = buffer.getShort();
    rna_path = DNATools.ptr(buffer); // get ptr
    array_index = buffer.getInt();
    flag = buffer.getShort();
    keyingflag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(id!=null?id.hashCode():0);
    buffer.write(group);
    buffer.writeInt(idtype);
    buffer.writeShort(groupmode);
    buffer.writeShort(pad);
    buffer.writeInt(rna_path!=null?rna_path.hashCode():0);
    buffer.writeInt(array_index);
    buffer.writeShort(flag);
    buffer.writeShort(keyingflag);
  }
  public Object setmyarray(Object array) {
    myarray = (KS_Path[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("KS_Path:\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  group: ").append(new String(group)).append("\n");
    sb.append("  idtype: ").append(idtype).append("\n");
    sb.append("  groupmode: ").append(groupmode).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  rna_path: ").append(rna_path).append("\n");
    sb.append("  array_index: ").append(array_index).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  keyingflag: ").append(keyingflag).append("\n");
    return sb.toString();
  }
  public KS_Path copy() { try {return (KS_Path)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

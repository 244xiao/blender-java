package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class DriverTarget implements DNA, Cloneable { // #375
  public DriverTarget[] myarray;
  public Object id; // ptr 72
  public Object rna_path; // ptr 1
  public byte[] pchan_name = new byte[32]; // 1
  public short transChan; // 2
  public short flag; // 2
  public int idtype; // 4

  public void read(ByteBuffer buffer) {
    id = DNATools.ptr(buffer); // get ptr
    rna_path = DNATools.ptr(buffer); // get ptr
    buffer.get(pchan_name);
    transChan = buffer.getShort();
    flag = buffer.getShort();
    idtype = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(id!=null?id.hashCode():0);
    buffer.writeInt(rna_path!=null?rna_path.hashCode():0);
    buffer.write(pchan_name);
    buffer.writeShort(transChan);
    buffer.writeShort(flag);
    buffer.writeInt(idtype);
  }
  public Object setmyarray(Object array) {
    myarray = (DriverTarget[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("DriverTarget:\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  rna_path: ").append(rna_path).append("\n");
    sb.append("  pchan_name: ").append(new String(pchan_name)).append("\n");
    sb.append("  transChan: ").append(transChan).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  idtype: ").append(idtype).append("\n");
    return sb.toString();
  }
  public DriverTarget copy() { try {return (DriverTarget)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

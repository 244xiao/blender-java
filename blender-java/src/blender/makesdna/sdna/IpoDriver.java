package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IpoDriver implements DNA, Cloneable { // #12
  public IpoDriver[] myarray;
  public bObject ob; // ptr 1296
  public short blocktype; // 2
  public short adrcode; // 2
  public short type; // 2
  public short flag; // 2
  public byte[] name = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    blocktype = buffer.getShort();
    adrcode = buffer.getShort();
    type = buffer.getShort();
    flag = buffer.getShort();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeShort(blocktype);
    buffer.writeShort(adrcode);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (IpoDriver[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("IpoDriver:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  blocktype: ").append(blocktype).append("\n");
    sb.append("  adrcode: ").append(adrcode).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public IpoDriver copy() { try {return (IpoDriver)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

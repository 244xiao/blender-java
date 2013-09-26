package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BuildEff extends Link<BuildEff> implements DNA, Cloneable { // #213
  public BuildEff[] myarray;
  public short type; // 2
  public short flag; // 2
  public short buttype; // 2
  public short rt; // 2
  public float len; // 4
  public float sfra; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), BuildEff.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), BuildEff.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    buttype = buffer.getShort();
    rt = buffer.getShort();
    len = buffer.getFloat();
    sfra = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(buttype);
    buffer.writeShort(rt);
    buffer.writeFloat(len);
    buffer.writeFloat(sfra);
  }
  public Object setmyarray(Object array) {
    myarray = (BuildEff[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BuildEff:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  buttype: ").append(buttype).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    return sb.toString();
  }
  public BuildEff copy() { try {return (BuildEff)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

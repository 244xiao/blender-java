package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Base extends Link<Base> implements DNA, Cloneable { // #127
  public Base[] myarray;
  public int lay; // 4
  public int selcol; // 4
  public int flag; // 4
  public short sx; // 2
  public short sy; // 2
  public bObject object; // ptr 1296

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Base.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Base.class); // get ptr
    lay = buffer.getInt();
    selcol = buffer.getInt();
    flag = buffer.getInt();
    sx = buffer.getShort();
    sy = buffer.getShort();
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(lay);
    buffer.writeInt(selcol);
    buffer.writeInt(flag);
    buffer.writeShort(sx);
    buffer.writeShort(sy);
    buffer.writeInt(object!=null?object.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Base[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Base:\n");
    sb.append("  lay: ").append(lay).append("\n");
    sb.append("  selcol: ").append(selcol).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sx: ").append(sx).append("\n");
    sb.append("  sy: ").append(sy).append("\n");
    sb.append("  object: ").append(object).append("\n");
    return sb.toString();
  }
  public Base copy() { try {return (Base)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

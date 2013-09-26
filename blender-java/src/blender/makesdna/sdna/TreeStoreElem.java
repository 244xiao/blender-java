package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TreeStoreElem implements DNA, Cloneable { // #216
  public TreeStoreElem[] myarray;
  public short type; // 2
  public short nr; // 2
  public short flag; // 2
  public short used; // 2
  public Object id; // ptr 72

  public void read(ByteBuffer buffer) {
    type = buffer.getShort();
    nr = buffer.getShort();
    flag = buffer.getShort();
    used = buffer.getShort();
    id = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(type);
    buffer.writeShort(nr);
    buffer.writeShort(flag);
    buffer.writeShort(used);
    buffer.writeInt(id!=null?id.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (TreeStoreElem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TreeStoreElem:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  nr: ").append(nr).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  used: ").append(used).append("\n");
    sb.append("  id: ").append(id).append("\n");
    return sb.toString();
  }
  public TreeStoreElem copy() { try {return (TreeStoreElem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

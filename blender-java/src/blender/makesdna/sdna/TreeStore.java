package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TreeStore implements DNA, Cloneable { // #217
  public TreeStore[] myarray;
  public int totelem; // 4
  public int usedelem; // 4
  public TreeStoreElem data; // ptr 16

  public void read(ByteBuffer buffer) {
    totelem = buffer.getInt();
    usedelem = buffer.getInt();
    data = DNATools.link(DNATools.ptr(buffer), TreeStoreElem.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(totelem);
    buffer.writeInt(usedelem);
    buffer.writeInt(data!=null?data.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (TreeStore[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TreeStore:\n");
    sb.append("  totelem: ").append(totelem).append("\n");
    sb.append("  usedelem: ").append(usedelem).append("\n");
    sb.append("  data: ").append(data).append("\n");
    return sb.toString();
  }
  public TreeStore copy() { try {return (TreeStore)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

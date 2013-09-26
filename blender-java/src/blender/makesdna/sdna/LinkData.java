package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class LinkData extends Link<LinkData> implements DNA, Cloneable { // #1
  public LinkData[] myarray;
  public Object data; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), LinkData.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), LinkData.class); // get ptr
    data = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(data!=null?data.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (LinkData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("LinkData:\n");
    sb.append("  data: ").append(data).append("\n");
    return sb.toString();
  }
  public LinkData copy() { try {return (LinkData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

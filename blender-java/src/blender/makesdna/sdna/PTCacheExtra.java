package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PTCacheExtra extends Link<PTCacheExtra> implements DNA, Cloneable { // #119
  public PTCacheExtra[] myarray;
  public int type; // 4
  public int totdata; // 4
  public Object data; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), PTCacheExtra.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), PTCacheExtra.class); // get ptr
    type = buffer.getInt();
    totdata = buffer.getInt();
    data = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(type);
    buffer.writeInt(totdata);
    buffer.writeInt(data!=null?data.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PTCacheExtra[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PTCacheExtra:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  totdata: ").append(totdata).append("\n");
    sb.append("  data: ").append(data).append("\n");
    return sb.toString();
  }
  public PTCacheExtra copy() { try {return (PTCacheExtra)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

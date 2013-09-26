package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Link<T> implements DNA, Cloneable { // #0
  public T next;
  public T prev;

  public void read(ByteBuffer buffer) {
    next = (T)(Object)DNATools.ptr(buffer);
    prev = (T)(Object)DNATools.ptr(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
  }
  public Object setmyarray(Object array) {
    return this;
  }
  public Object getmyarray() {
    return null;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Link:\n");
    sb.append("  next: ").append(next).append("\n");
    sb.append("  prev: ").append(prev).append("\n");
    return sb.toString();
  }
  public Link copy() { try {return (Link)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

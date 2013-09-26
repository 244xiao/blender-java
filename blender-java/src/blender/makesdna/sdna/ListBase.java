package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ListBase<T> implements DNA, Cloneable { // #2
  public T first;
  public T last;

  public void read(ByteBuffer buffer) {
    first = (T)(Object)DNATools.ptr(buffer);
    last = (T)(Object)DNATools.ptr(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(first!=null?first.hashCode():0);
    buffer.writeInt(last!=null?last.hashCode():0);
  }
  public Object setmyarray(Object array) {
    return this;
  }
  public Object getmyarray() {
    return null;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ListBase:\n");
    sb.append("  first: ").append(first).append("\n");
    sb.append("  last: ").append(last).append("\n");
    return sb.toString();
  }
  public ListBase copy() { try {return (ListBase)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

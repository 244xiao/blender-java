package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceTimeCache extends Link<SpaceTimeCache> implements DNA, Cloneable { // #169
  public SpaceTimeCache[] myarray;
  public Object array; // ptr 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), SpaceTimeCache.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), SpaceTimeCache.class); // get ptr
    array = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(array!=null?array.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceTimeCache[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceTimeCache:\n");
    sb.append("  array: ").append(array).append("\n");
    return sb.toString();
  }
  public SpaceTimeCache copy() { try {return (SpaceTimeCache)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

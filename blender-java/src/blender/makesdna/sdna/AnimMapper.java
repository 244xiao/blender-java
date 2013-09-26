package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AnimMapper extends Link<AnimMapper> implements DNA, Cloneable { // #381
  public AnimMapper[] myarray;
  public bAction target; // ptr 152
  public ListBase mappings = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), AnimMapper.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), AnimMapper.class); // get ptr
    target = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    mappings.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(target!=null?target.hashCode():0);
    mappings.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (AnimMapper[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AnimMapper:\n");
    sb.append("  target: ").append(target).append("\n");
    sb.append("  mappings: ").append(mappings).append("\n");
    return sb.toString();
  }
  public AnimMapper copy() { try {return (AnimMapper)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

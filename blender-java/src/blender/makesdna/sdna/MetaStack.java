package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MetaStack extends Link<MetaStack> implements DNA, Cloneable { // #205
  public MetaStack[] myarray;
  public ListBase oldbasep; // ptr 16
  public Sequence parseq; // ptr 288

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), MetaStack.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), MetaStack.class); // get ptr
    oldbasep = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    parseq = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(oldbasep!=null?oldbasep.hashCode():0);
    buffer.writeInt(parseq!=null?parseq.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MetaStack[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MetaStack:\n");
    sb.append("  oldbasep: ").append(oldbasep).append("\n");
    sb.append("  parseq: ").append(parseq).append("\n");
    return sb.toString();
  }
  public MetaStack copy() { try {return (MetaStack)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

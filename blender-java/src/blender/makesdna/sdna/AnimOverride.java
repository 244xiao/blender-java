package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AnimOverride extends Link<AnimOverride> implements DNA, Cloneable { // #386
  public AnimOverride[] myarray;
  public Object rna_path; // ptr 1
  public int array_index; // 4
  public float value; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), AnimOverride.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), AnimOverride.class); // get ptr
    rna_path = DNATools.ptr(buffer); // get ptr
    array_index = buffer.getInt();
    value = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(rna_path!=null?rna_path.hashCode():0);
    buffer.writeInt(array_index);
    buffer.writeFloat(value);
  }
  public Object setmyarray(Object array) {
    myarray = (AnimOverride[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AnimOverride:\n");
    sb.append("  rna_path: ").append(rna_path).append("\n");
    sb.append("  array_index: ").append(array_index).append("\n");
    sb.append("  value: ").append(value).append("\n");
    return sb.toString();
  }
  public AnimOverride copy() { try {return (AnimOverride)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

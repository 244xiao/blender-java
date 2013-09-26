package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PackedFile implements DNA, Cloneable { // #20
  public PackedFile[] myarray;
  public int size; // 4
  public int seek; // 4
  public Object data; // ptr 0

  public void read(ByteBuffer buffer) {
    size = buffer.getInt();
    seek = buffer.getInt();
    data = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(size);
    buffer.writeInt(seek);
    buffer.writeInt(data!=null?data.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PackedFile[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PackedFile:\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  seek: ").append(seek).append("\n");
    sb.append("  data: ").append(data).append("\n");
    return sb.toString();
  }
  public PackedFile copy() { try {return (PackedFile)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

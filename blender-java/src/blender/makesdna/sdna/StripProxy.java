package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class StripProxy implements DNA, Cloneable { // #201
  public StripProxy[] myarray;
  public byte[] dir = new byte[160]; // 1
  public byte[] file = new byte[80]; // 1
  public Object anim; // ptr (anim) 0
  public short size; // 2
  public short quality; // 2
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(dir);
    buffer.get(file);
    anim = DNATools.ptr(buffer); // get ptr
    size = buffer.getShort();
    quality = buffer.getShort();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(dir);
    buffer.write(file);
    buffer.writeInt(anim!=null?anim.hashCode():0);
    buffer.writeShort(size);
    buffer.writeShort(quality);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (StripProxy[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("StripProxy:\n");
    sb.append("  dir: ").append(new String(dir)).append("\n");
    sb.append("  file: ").append(new String(file)).append("\n");
    sb.append("  anim: ").append(anim).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  quality: ").append(quality).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public StripProxy copy() { try {return (StripProxy)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

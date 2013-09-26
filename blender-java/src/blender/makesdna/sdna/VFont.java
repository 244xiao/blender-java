package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class VFont extends ID implements DNA, Cloneable { // #36
  public VFont[] myarray;
  public ID id = (ID)this;
  public byte[] name = new byte[256]; // 1
  public Object data; // ptr (VFontData) 0
  public PackedFile packedfile; // ptr 16

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    buffer.get(name);
    data = DNATools.ptr(buffer); // get ptr
    packedfile = DNATools.link(DNATools.ptr(buffer), PackedFile.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.write(name);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(packedfile!=null?packedfile.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (VFont[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("VFont:\n");
    sb.append(super.toString());
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  packedfile: ").append(packedfile).append("\n");
    return sb.toString();
  }
  public VFont copy() { try {return (VFont)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

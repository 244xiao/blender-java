package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGPdata extends ID implements DNA, Cloneable { // #357
  public bGPdata[] myarray;
  public ID id = (ID)this;
  public ListBase layers = new ListBase(); // 16
  public int flag; // 4
  public short sbuffer_size; // 2
  public short sbuffer_sflag; // 2
  public Object sbuffer; // ptr 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    layers.read(buffer);
    flag = buffer.getInt();
    sbuffer_size = buffer.getShort();
    sbuffer_sflag = buffer.getShort();
    sbuffer = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    layers.write(buffer);
    buffer.writeInt(flag);
    buffer.writeShort(sbuffer_size);
    buffer.writeShort(sbuffer_sflag);
    buffer.writeInt(sbuffer!=null?sbuffer.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bGPdata[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGPdata:\n");
    sb.append(super.toString());
    sb.append("  layers: ").append(layers).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sbuffer_size: ").append(sbuffer_size).append("\n");
    sb.append("  sbuffer_sflag: ").append(sbuffer_sflag).append("\n");
    sb.append("  sbuffer: ").append(sbuffer).append("\n");
    return sb.toString();
  }
  public bGPdata copy() { try {return (bGPdata)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

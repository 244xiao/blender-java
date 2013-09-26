package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNodePreview implements DNA, Cloneable { // #307
  public bNodePreview[] myarray;
  public Object rect; // ptr 1
  public short xsize; // 2
  public short ysize; // 2
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    rect = DNATools.ptr(buffer); // get ptr
    xsize = buffer.getShort();
    ysize = buffer.getShort();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(rect!=null?rect.hashCode():0);
    buffer.writeShort(xsize);
    buffer.writeShort(ysize);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bNodePreview[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNodePreview:\n");
    sb.append("  rect: ").append(rect).append("\n");
    sb.append("  xsize: ").append(xsize).append("\n");
    sb.append("  ysize: ").append(ysize).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bNodePreview copy() { try {return (bNodePreview)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

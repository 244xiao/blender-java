package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BrushClone implements DNA, Cloneable { // #335
  public BrushClone[] myarray;
  public Image image; // ptr 496
  public float[] offset = new float[2]; // 4
  public float alpha; // 4
  public float pad; // 4

  public void read(ByteBuffer buffer) {
    image = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    for(int i=0;i<offset.length;i++) offset[i]=buffer.getFloat();
    alpha = buffer.getFloat();
    pad = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(image!=null?image.hashCode():0);
    for(int i=0;i<offset.length;i++) buffer.writeFloat(offset[i]);
    buffer.writeFloat(alpha);
    buffer.writeFloat(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (BrushClone[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BrushClone:\n");
    sb.append("  image: ").append(image).append("\n");
    sb.append("  offset: ").append(Arrays.toString(offset)).append("\n");
    sb.append("  alpha: ").append(alpha).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public BrushClone copy() { try {return (BrushClone)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

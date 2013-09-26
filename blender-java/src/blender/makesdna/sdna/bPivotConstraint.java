package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPivotConstraint implements DNA, Cloneable { // #297
  public bPivotConstraint[] myarray;
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1
  public float[] offset = new float[3]; // 4
  public short rotAxis; // 2
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
    for(int i=0;i<offset.length;i++) offset[i]=buffer.getFloat();
    rotAxis = buffer.getShort();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
    for(int i=0;i<offset.length;i++) buffer.writeFloat(offset[i]);
    buffer.writeShort(rotAxis);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bPivotConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPivotConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  offset: ").append(Arrays.toString(offset)).append("\n");
    sb.append("  rotAxis: ").append(rotAxis).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bPivotConstraint copy() { try {return (bPivotConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

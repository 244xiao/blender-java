package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTransformConstraint implements DNA, Cloneable { // #296
  public bTransformConstraint[] myarray;
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1
  public short from; // 2
  public short to; // 2
  public byte[] map = new byte[3]; // 1
  public byte expo; // 1
  public float[] from_min = new float[3]; // 4
  public float[] from_max = new float[3]; // 4
  public float[] to_min = new float[3]; // 4
  public float[] to_max = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
    from = buffer.getShort();
    to = buffer.getShort();
    buffer.get(map);
    expo = buffer.get();
    for(int i=0;i<from_min.length;i++) from_min[i]=buffer.getFloat();
    for(int i=0;i<from_max.length;i++) from_max[i]=buffer.getFloat();
    for(int i=0;i<to_min.length;i++) to_min[i]=buffer.getFloat();
    for(int i=0;i<to_max.length;i++) to_max[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
    buffer.writeShort(from);
    buffer.writeShort(to);
    buffer.write(map);
    buffer.writeByte(expo);
    for(int i=0;i<from_min.length;i++) buffer.writeFloat(from_min[i]);
    for(int i=0;i<from_max.length;i++) buffer.writeFloat(from_max[i]);
    for(int i=0;i<to_min.length;i++) buffer.writeFloat(to_min[i]);
    for(int i=0;i<to_max.length;i++) buffer.writeFloat(to_max[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (bTransformConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTransformConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  from: ").append(from).append("\n");
    sb.append("  to: ").append(to).append("\n");
    sb.append("  map: ").append(new String(map)).append("\n");
    sb.append("  expo: ").append(expo).append("\n");
    sb.append("  from_min: ").append(Arrays.toString(from_min)).append("\n");
    sb.append("  from_max: ").append(Arrays.toString(from_max)).append("\n");
    sb.append("  to_min: ").append(Arrays.toString(to_min)).append("\n");
    sb.append("  to_max: ").append(Arrays.toString(to_max)).append("\n");
    return sb.toString();
  }
  public bTransformConstraint copy() { try {return (bTransformConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

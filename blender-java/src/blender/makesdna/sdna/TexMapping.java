package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TexMapping implements DNA, Cloneable { // #32
  public TexMapping[] myarray;
  public float[] loc = new float[3]; // 4
  public float[] rot = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public int flag; // 4
  public float[][] mat = new float[4][4]; // 4
  public float[] min = new float[3]; // 4
  public float[] max = new float[3]; // 4
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    flag = buffer.getInt();
    for(int i=0;i<mat.length;i++) for(int j=0;j<mat[i].length;j++) mat[i][j]=buffer.getFloat();
    for(int i=0;i<min.length;i++) min[i]=buffer.getFloat();
    for(int i=0;i<max.length;i++) max[i]=buffer.getFloat();
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    buffer.writeInt(flag);
    for(int i=0; i<mat.length; i++)  for(int j=0;j<mat[i].length;j++) buffer.writeFloat(mat[i][j]);
    for(int i=0;i<min.length;i++) buffer.writeFloat(min[i]);
    for(int i=0;i<max.length;i++) buffer.writeFloat(max[i]);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (TexMapping[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TexMapping:\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  min: ").append(Arrays.toString(min)).append("\n");
    sb.append("  max: ").append(Arrays.toString(max)).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public TexMapping copy() { try {return (TexMapping)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TransformOrientation extends Link<TransformOrientation> implements DNA, Cloneable { // #144
  public TransformOrientation[] myarray;
  public byte[] name = new byte[36]; // 1
  public float[][] mat = new float[3][3]; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), TransformOrientation.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), TransformOrientation.class); // get ptr
    buffer.get(name);
    for(int i=0;i<mat.length;i++) for(int j=0;j<mat[i].length;j++) mat[i][j]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    for(int i=0; i<mat.length; i++)  for(int j=0;j<mat[i].length;j++) buffer.writeFloat(mat[i][j]);
  }
  public Object setmyarray(Object array) {
    myarray = (TransformOrientation[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TransformOrientation:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    return sb.toString();
  }
  public TransformOrientation copy() { try {return (TransformOrientation)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

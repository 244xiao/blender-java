package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoundBox implements DNA, Cloneable { // #113
  public BoundBox[] myarray;
  public float[][] vec = new float[8][3]; // 4
  public int flag; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) for(int j=0;j<vec[i].length;j++) vec[i][j]=buffer.getFloat();
    flag = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0; i<vec.length; i++)  for(int j=0;j<vec[i].length;j++) buffer.writeFloat(vec[i][j]);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (BoundBox[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoundBox:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public BoundBox copy() { try {return (BoundBox)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

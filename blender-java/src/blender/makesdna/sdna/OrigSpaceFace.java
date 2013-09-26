package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class OrigSpaceFace implements DNA, Cloneable { // #63
  public OrigSpaceFace[] myarray;
  public float[][] uv = new float[4][2]; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<uv.length;i++) for(int j=0;j<uv[i].length;j++) uv[i][j]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0; i<uv.length; i++)  for(int j=0;j<uv[i].length;j++) buffer.writeFloat(uv[i][j]);
  }
  public Object setmyarray(Object array) {
    myarray = (OrigSpaceFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("OrigSpaceFace:\n");
    sb.append("  uv: ").append(Arrays.toString(uv)).append("\n");
    return sb.toString();
  }
  public OrigSpaceFace copy() { try {return (OrigSpaceFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

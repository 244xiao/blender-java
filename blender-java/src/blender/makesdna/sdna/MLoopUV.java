package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MLoopUV implements DNA, Cloneable { // #55
  public MLoopUV[] myarray;
  public float[] uv = new float[2]; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<uv.length;i++) uv[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<uv.length;i++) buffer.writeFloat(uv[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (MLoopUV[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MLoopUV:\n");
    sb.append("  uv: ").append(Arrays.toString(uv)).append("\n");
    return sb.toString();
  }
  public MLoopUV copy() { try {return (MLoopUV)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

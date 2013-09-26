package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SBVertex implements DNA, Cloneable { // #122
  public SBVertex[] myarray;
  public float[] vec = new float[4]; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) vec[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<vec.length;i++) buffer.writeFloat(vec[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (SBVertex[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SBVertex:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    return sb.toString();
  }
  public SBVertex copy() { try {return (SBVertex)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

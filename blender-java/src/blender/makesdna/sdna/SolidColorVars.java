package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SolidColorVars implements DNA, Cloneable { // #210
  public SolidColorVars[] myarray;
  public float[] col = new float[3]; // 4
  public float pad; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<col.length;i++) col[i]=buffer.getFloat();
    pad = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<col.length;i++) buffer.writeFloat(col[i]);
    buffer.writeFloat(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (SolidColorVars[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SolidColorVars:\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public SolidColorVars copy() { try {return (SolidColorVars)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

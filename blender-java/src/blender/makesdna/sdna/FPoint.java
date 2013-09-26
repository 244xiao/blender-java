package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FPoint implements DNA, Cloneable { // #378
  public FPoint[] myarray;
  public float[] vec = new float[2]; // 4
  public int flag; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) vec[i]=buffer.getFloat();
    flag = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<vec.length;i++) buffer.writeFloat(vec[i]);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (FPoint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FPoint:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public FPoint copy() { try {return (FPoint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

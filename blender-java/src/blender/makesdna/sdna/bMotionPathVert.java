package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMotionPathVert implements DNA, Cloneable { // #263
  public bMotionPathVert[] myarray;
  public float[] co = new float[3]; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<co.length;i++) co[i]=buffer.getFloat();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<co.length;i++) buffer.writeFloat(co[i]);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bMotionPathVert[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMotionPathVert:\n");
    sb.append("  co: ").append(Arrays.toString(co)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bMotionPathVert copy() { try {return (bMotionPathVert)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

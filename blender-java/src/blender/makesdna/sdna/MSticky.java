package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MSticky implements DNA, Cloneable { // #57
  public MSticky[] myarray;
  public float[] co = new float[2]; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<co.length;i++) co[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<co.length;i++) buffer.writeFloat(co[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (MSticky[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MSticky:\n");
    sb.append("  co: ").append(Arrays.toString(co)).append("\n");
    return sb.toString();
  }
  public MSticky copy() { try {return (MSticky)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SolidLight implements DNA, Cloneable { // #188
  public SolidLight[] myarray;
  public int flag; // 4
  public int pad; // 4
  public float[] col = new float[4]; // 4
  public float[] spec = new float[4]; // 4
  public float[] vec = new float[4]; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    pad = buffer.getInt();
    for(int i=0;i<col.length;i++) col[i]=buffer.getFloat();
    for(int i=0;i<spec.length;i++) spec[i]=buffer.getFloat();
    for(int i=0;i<vec.length;i++) vec[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeInt(pad);
    for(int i=0;i<col.length;i++) buffer.writeFloat(col[i]);
    for(int i=0;i<spec.length;i++) buffer.writeFloat(spec[i]);
    for(int i=0;i<vec.length;i++) buffer.writeFloat(vec[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (SolidLight[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SolidLight:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    sb.append("  spec: ").append(Arrays.toString(spec)).append("\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    return sb.toString();
  }
  public SolidLight copy() { try {return (SolidLight)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

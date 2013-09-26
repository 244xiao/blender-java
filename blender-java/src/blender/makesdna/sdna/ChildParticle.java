package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ChildParticle implements DNA, Cloneable { // #344
  public ChildParticle[] myarray;
  public int num; // 4
  public int parent; // 4
  public int[] pa = new int[4]; // 4
  public float[] w = new float[4]; // 4
  public float[] fuv = new float[4]; // 4
  public float foffset; // 4
  public float rt; // 4

  public void read(ByteBuffer buffer) {
    num = buffer.getInt();
    parent = buffer.getInt();
    for(int i=0;i<pa.length;i++) pa[i]=buffer.getInt();
    for(int i=0;i<w.length;i++) w[i]=buffer.getFloat();
    for(int i=0;i<fuv.length;i++) fuv[i]=buffer.getFloat();
    foffset = buffer.getFloat();
    rt = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(num);
    buffer.writeInt(parent);
    for(int i=0;i<pa.length;i++) buffer.writeInt(pa[i]);
    for(int i=0;i<w.length;i++) buffer.writeFloat(w[i]);
    for(int i=0;i<fuv.length;i++) buffer.writeFloat(fuv[i]);
    buffer.writeFloat(foffset);
    buffer.writeFloat(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (ChildParticle[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ChildParticle:\n");
    sb.append("  num: ").append(num).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  pa: ").append(Arrays.toString(pa)).append("\n");
    sb.append("  w: ").append(Arrays.toString(w)).append("\n");
    sb.append("  fuv: ").append(Arrays.toString(fuv)).append("\n");
    sb.append("  foffset: ").append(foffset).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public ChildParticle copy() { try {return (ChildParticle)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

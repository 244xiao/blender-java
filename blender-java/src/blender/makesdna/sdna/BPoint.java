package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BPoint implements DNA, Cloneable { // #40
  public BPoint[] myarray;
  public float[] vec = new float[4]; // 4
  public float alfa; // 4
  public float weight; // 4
  public short f1; // 2
  public short hide; // 2
  public float radius; // 4
  public float pad; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) vec[i]=buffer.getFloat();
    alfa = buffer.getFloat();
    weight = buffer.getFloat();
    f1 = buffer.getShort();
    hide = buffer.getShort();
    radius = buffer.getFloat();
    pad = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<vec.length;i++) buffer.writeFloat(vec[i]);
    buffer.writeFloat(alfa);
    buffer.writeFloat(weight);
    buffer.writeShort(f1);
    buffer.writeShort(hide);
    buffer.writeFloat(radius);
    buffer.writeFloat(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (BPoint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BPoint:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    sb.append("  alfa: ").append(alfa).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  f1: ").append(f1).append("\n");
    sb.append("  hide: ").append(hide).append("\n");
    sb.append("  radius: ").append(radius).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public BPoint copy() { try {return (BPoint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

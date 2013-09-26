package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BezTriple implements DNA, Cloneable { // #39
  public BezTriple[] myarray;
  public float[][] vec = new float[3][3]; // 4
  public float alfa; // 4
  public float weight; // 4
  public float radius; // 4
  public short ipo; // 2
  public byte h1; // 1
  public byte h2; // 1
  public byte f1; // 1
  public byte f2; // 1
  public byte f3; // 1
  public byte hide; // 1

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) for(int j=0;j<vec[i].length;j++) vec[i][j]=buffer.getFloat();
    alfa = buffer.getFloat();
    weight = buffer.getFloat();
    radius = buffer.getFloat();
    ipo = buffer.getShort();
    h1 = buffer.get();
    h2 = buffer.get();
    f1 = buffer.get();
    f2 = buffer.get();
    f3 = buffer.get();
    hide = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0; i<vec.length; i++)  for(int j=0;j<vec[i].length;j++) buffer.writeFloat(vec[i][j]);
    buffer.writeFloat(alfa);
    buffer.writeFloat(weight);
    buffer.writeFloat(radius);
    buffer.writeShort(ipo);
    buffer.writeByte(h1);
    buffer.writeByte(h2);
    buffer.writeByte(f1);
    buffer.writeByte(f2);
    buffer.writeByte(f3);
    buffer.writeByte(hide);
  }
  public Object setmyarray(Object array) {
    myarray = (BezTriple[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BezTriple:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    sb.append("  alfa: ").append(alfa).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  radius: ").append(radius).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  h1: ").append(h1).append("\n");
    sb.append("  h2: ").append(h2).append("\n");
    sb.append("  f1: ").append(f1).append("\n");
    sb.append("  f2: ").append(f2).append("\n");
    sb.append("  f3: ").append(f3).append("\n");
    sb.append("  hide: ").append(hide).append("\n");
    return sb.toString();
  }
  public BezTriple copy() { try {return (BezTriple)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

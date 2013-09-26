package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSizeLimitConstraint implements DNA, Cloneable { // #300
  public bSizeLimitConstraint[] myarray;
  public float xmin; // 4
  public float xmax; // 4
  public float ymin; // 4
  public float ymax; // 4
  public float zmin; // 4
  public float zmax; // 4
  public short flag; // 2
  public short flag2; // 2

  public void read(ByteBuffer buffer) {
    xmin = buffer.getFloat();
    xmax = buffer.getFloat();
    ymin = buffer.getFloat();
    ymax = buffer.getFloat();
    zmin = buffer.getFloat();
    zmax = buffer.getFloat();
    flag = buffer.getShort();
    flag2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(xmin);
    buffer.writeFloat(xmax);
    buffer.writeFloat(ymin);
    buffer.writeFloat(ymax);
    buffer.writeFloat(zmin);
    buffer.writeFloat(zmax);
    buffer.writeShort(flag);
    buffer.writeShort(flag2);
  }
  public Object setmyarray(Object array) {
    myarray = (bSizeLimitConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSizeLimitConstraint:\n");
    sb.append("  xmin: ").append(xmin).append("\n");
    sb.append("  xmax: ").append(xmax).append("\n");
    sb.append("  ymin: ").append(ymin).append("\n");
    sb.append("  ymax: ").append(ymax).append("\n");
    sb.append("  zmin: ").append(zmin).append("\n");
    sb.append("  zmax: ").append(zmax).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  flag2: ").append(flag2).append("\n");
    return sb.toString();
  }
  public bSizeLimitConstraint copy() { try {return (bSizeLimitConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

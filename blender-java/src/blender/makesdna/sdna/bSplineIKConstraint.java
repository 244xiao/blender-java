package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSplineIKConstraint implements DNA, Cloneable { // #280
  public bSplineIKConstraint[] myarray;
  public bObject tar; // ptr 1296
  public Object points; // ptr 4
  public short numpoints; // 2
  public short chainlen; // 2
  public short flag; // 2
  public short xzScaleMode; // 2

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    points = DNATools.ptr(buffer); // get ptr
    numpoints = buffer.getShort();
    chainlen = buffer.getShort();
    flag = buffer.getShort();
    xzScaleMode = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(points!=null?points.hashCode():0);
    buffer.writeShort(numpoints);
    buffer.writeShort(chainlen);
    buffer.writeShort(flag);
    buffer.writeShort(xzScaleMode);
  }
  public Object setmyarray(Object array) {
    myarray = (bSplineIKConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSplineIKConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  points: ").append(points).append("\n");
    sb.append("  numpoints: ").append(numpoints).append("\n");
    sb.append("  chainlen: ").append(chainlen).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  xzScaleMode: ").append(xzScaleMode).append("\n");
    return sb.toString();
  }
  public bSplineIKConstraint copy() { try {return (bSplineIKConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

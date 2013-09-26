package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bRigidBodyJointConstraint implements DNA, Cloneable { // #293
  public bRigidBodyJointConstraint[] myarray;
  public bObject tar; // ptr 1296
  public bObject child; // ptr 1296
  public int type; // 4
  public float pivX; // 4
  public float pivY; // 4
  public float pivZ; // 4
  public float axX; // 4
  public float axY; // 4
  public float axZ; // 4
  public float[] minLimit = new float[6]; // 4
  public float[] maxLimit = new float[6]; // 4
  public float extraFz; // 4
  public short flag; // 2
  public short pad; // 2
  public short pad1; // 2
  public short pad2; // 2

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    child = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    type = buffer.getInt();
    pivX = buffer.getFloat();
    pivY = buffer.getFloat();
    pivZ = buffer.getFloat();
    axX = buffer.getFloat();
    axY = buffer.getFloat();
    axZ = buffer.getFloat();
    for(int i=0;i<minLimit.length;i++) minLimit[i]=buffer.getFloat();
    for(int i=0;i<maxLimit.length;i++) maxLimit[i]=buffer.getFloat();
    extraFz = buffer.getFloat();
    flag = buffer.getShort();
    pad = buffer.getShort();
    pad1 = buffer.getShort();
    pad2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(child!=null?child.hashCode():0);
    buffer.writeInt(type);
    buffer.writeFloat(pivX);
    buffer.writeFloat(pivY);
    buffer.writeFloat(pivZ);
    buffer.writeFloat(axX);
    buffer.writeFloat(axY);
    buffer.writeFloat(axZ);
    for(int i=0;i<minLimit.length;i++) buffer.writeFloat(minLimit[i]);
    for(int i=0;i<maxLimit.length;i++) buffer.writeFloat(maxLimit[i]);
    buffer.writeFloat(extraFz);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
    buffer.writeShort(pad1);
    buffer.writeShort(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (bRigidBodyJointConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bRigidBodyJointConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  child: ").append(child).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pivX: ").append(pivX).append("\n");
    sb.append("  pivY: ").append(pivY).append("\n");
    sb.append("  pivZ: ").append(pivZ).append("\n");
    sb.append("  axX: ").append(axX).append("\n");
    sb.append("  axY: ").append(axY).append("\n");
    sb.append("  axZ: ").append(axZ).append("\n");
    sb.append("  minLimit: ").append(Arrays.toString(minLimit)).append("\n");
    sb.append("  maxLimit: ").append(Arrays.toString(maxLimit)).append("\n");
    sb.append("  extraFz: ").append(extraFz).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public bRigidBodyJointConstraint copy() { try {return (bRigidBodyJointConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

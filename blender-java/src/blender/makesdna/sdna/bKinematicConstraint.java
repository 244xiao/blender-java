package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bKinematicConstraint implements DNA, Cloneable { // #279
  public bKinematicConstraint[] myarray;
  public bObject tar; // ptr 1296
  public short iterations; // 2
  public short flag; // 2
  public short rootbone; // 2
  public short max_rootbone; // 2
  public byte[] subtarget = new byte[32]; // 1
  public bObject poletar; // ptr 1296
  public byte[] polesubtarget = new byte[32]; // 1
  public float poleangle; // 4
  public float weight; // 4
  public float orientweight; // 4
  public float[] grabtarget = new float[3]; // 4
  public short type; // 2
  public short mode; // 2
  public float dist; // 4

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    iterations = buffer.getShort();
    flag = buffer.getShort();
    rootbone = buffer.getShort();
    max_rootbone = buffer.getShort();
    buffer.get(subtarget);
    poletar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(polesubtarget);
    poleangle = buffer.getFloat();
    weight = buffer.getFloat();
    orientweight = buffer.getFloat();
    for(int i=0;i<grabtarget.length;i++) grabtarget[i]=buffer.getFloat();
    type = buffer.getShort();
    mode = buffer.getShort();
    dist = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeShort(iterations);
    buffer.writeShort(flag);
    buffer.writeShort(rootbone);
    buffer.writeShort(max_rootbone);
    buffer.write(subtarget);
    buffer.writeInt(poletar!=null?poletar.hashCode():0);
    buffer.write(polesubtarget);
    buffer.writeFloat(poleangle);
    buffer.writeFloat(weight);
    buffer.writeFloat(orientweight);
    for(int i=0;i<grabtarget.length;i++) buffer.writeFloat(grabtarget[i]);
    buffer.writeShort(type);
    buffer.writeShort(mode);
    buffer.writeFloat(dist);
  }
  public Object setmyarray(Object array) {
    myarray = (bKinematicConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bKinematicConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  iterations: ").append(iterations).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rootbone: ").append(rootbone).append("\n");
    sb.append("  max_rootbone: ").append(max_rootbone).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  poletar: ").append(poletar).append("\n");
    sb.append("  polesubtarget: ").append(new String(polesubtarget)).append("\n");
    sb.append("  poleangle: ").append(poleangle).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  orientweight: ").append(orientweight).append("\n");
    sb.append("  grabtarget: ").append(Arrays.toString(grabtarget)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    return sb.toString();
  }
  public bKinematicConstraint copy() { try {return (bKinematicConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

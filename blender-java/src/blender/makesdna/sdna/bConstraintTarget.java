package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bConstraintTarget extends Link<bConstraintTarget> implements DNA, Cloneable { // #277
  public bConstraintTarget[] myarray;
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1
  public float[][] matrix = new float[4][4]; // 4
  public short space; // 2
  public short flag; // 2
  public short type; // 2
  public short rotOrder; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bConstraintTarget.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bConstraintTarget.class); // get ptr
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
    for(int i=0;i<matrix.length;i++) for(int j=0;j<matrix[i].length;j++) matrix[i][j]=buffer.getFloat();
    space = buffer.getShort();
    flag = buffer.getShort();
    type = buffer.getShort();
    rotOrder = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
    for(int i=0; i<matrix.length; i++)  for(int j=0;j<matrix[i].length;j++) buffer.writeFloat(matrix[i][j]);
    buffer.writeShort(space);
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeShort(rotOrder);
  }
  public Object setmyarray(Object array) {
    myarray = (bConstraintTarget[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bConstraintTarget:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  matrix: ").append(Arrays.toString(matrix)).append("\n");
    sb.append("  space: ").append(space).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  rotOrder: ").append(rotOrder).append("\n");
    return sb.toString();
  }
  public bConstraintTarget copy() { try {return (bConstraintTarget)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

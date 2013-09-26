package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bChildOfConstraint implements DNA, Cloneable { // #295
  public bChildOfConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int flag; // 4
  public int pad; // 4
  public float[][] invmat = new float[4][4]; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    flag = buffer.getInt();
    pad = buffer.getInt();
    for(int i=0;i<invmat.length;i++) for(int j=0;j<invmat[i].length;j++) invmat[i][j]=buffer.getFloat();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
    for(int i=0; i<invmat.length; i++)  for(int j=0;j<invmat[i].length;j++) buffer.writeFloat(invmat[i][j]);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bChildOfConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bChildOfConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  invmat: ").append(Arrays.toString(invmat)).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bChildOfConstraint copy() { try {return (bChildOfConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

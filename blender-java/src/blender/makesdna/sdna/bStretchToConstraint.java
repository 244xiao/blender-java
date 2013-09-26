package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bStretchToConstraint implements DNA, Cloneable { // #292
  public bStretchToConstraint[] myarray;
  public bObject tar; // ptr 1296
  public int volmode; // 4
  public int plane; // 4
  public float orglength; // 4
  public float bulge; // 4
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    volmode = buffer.getInt();
    plane = buffer.getInt();
    orglength = buffer.getFloat();
    bulge = buffer.getFloat();
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.writeInt(volmode);
    buffer.writeInt(plane);
    buffer.writeFloat(orglength);
    buffer.writeFloat(bulge);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bStretchToConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bStretchToConstraint:\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  volmode: ").append(volmode).append("\n");
    sb.append("  plane: ").append(plane).append("\n");
    sb.append("  orglength: ").append(orglength).append("\n");
    sb.append("  bulge: ").append(bulge).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bStretchToConstraint copy() { try {return (bStretchToConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

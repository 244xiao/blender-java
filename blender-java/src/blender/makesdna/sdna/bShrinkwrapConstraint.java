package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bShrinkwrapConstraint implements DNA, Cloneable { // #302
  public bShrinkwrapConstraint[] myarray;
  public bObject target; // ptr 1296
  public float dist; // 4
  public short shrinkType; // 2
  public byte projAxis; // 1
  public byte[] pad = new byte[9]; // 1

  public void read(ByteBuffer buffer) {
    target = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    dist = buffer.getFloat();
    shrinkType = buffer.getShort();
    projAxis = buffer.get();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(target!=null?target.hashCode():0);
    buffer.writeFloat(dist);
    buffer.writeShort(shrinkType);
    buffer.writeByte(projAxis);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bShrinkwrapConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bShrinkwrapConstraint:\n");
    sb.append("  target: ").append(target).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  shrinkType: ").append(shrinkType).append("\n");
    sb.append("  projAxis: ").append(projAxis).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public bShrinkwrapConstraint copy() { try {return (bShrinkwrapConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

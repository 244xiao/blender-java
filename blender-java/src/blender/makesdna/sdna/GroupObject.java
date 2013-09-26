package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class GroupObject extends Link<GroupObject> implements DNA, Cloneable { // #259
  public GroupObject[] myarray;
  public bObject ob; // ptr 1296
  public Object lampren; // ptr 0
  public short recalc; // 2
  public byte[] pad = new byte[6]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), GroupObject.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), GroupObject.class); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    lampren = DNATools.ptr(buffer); // get ptr
    recalc = buffer.getShort();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeInt(lampren!=null?lampren.hashCode():0);
    buffer.writeShort(recalc);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (GroupObject[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("GroupObject:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  lampren: ").append(lampren).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public GroupObject copy() { try {return (GroupObject)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

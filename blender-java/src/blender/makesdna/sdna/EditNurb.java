package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class EditNurb implements DNA, Cloneable { // #44
  public EditNurb[] myarray;
  public ListBase nurbs = new ListBase(); // 16
  public Object keyindex; // ptr (GHash) 0
  public int shapenr; // 4
  public byte[] pad = new byte[4]; // 1

  public void read(ByteBuffer buffer) {
    nurbs.read(buffer);
    keyindex = DNATools.ptr(buffer); // get ptr
    shapenr = buffer.getInt();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    nurbs.write(buffer);
    buffer.writeInt(keyindex!=null?keyindex.hashCode():0);
    buffer.writeInt(shapenr);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (EditNurb[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("EditNurb:\n");
    sb.append("  nurbs: ").append(nurbs).append("\n");
    sb.append("  keyindex: ").append(keyindex).append("\n");
    sb.append("  shapenr: ").append(shapenr).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public EditNurb copy() { try {return (EditNurb)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

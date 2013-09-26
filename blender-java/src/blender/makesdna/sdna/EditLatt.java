package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class EditLatt implements DNA, Cloneable { // #110
  public EditLatt[] myarray;
  public Lattice latt; // ptr 272
  public int shapenr; // 4
  public byte[] pad = new byte[4]; // 1

  public void read(ByteBuffer buffer) {
    latt = DNATools.link(DNATools.ptr(buffer), Lattice.class); // get ptr
    shapenr = buffer.getInt();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(latt!=null?latt.hashCode():0);
    buffer.writeInt(shapenr);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (EditLatt[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("EditLatt:\n");
    sb.append("  latt: ").append(latt).append("\n");
    sb.append("  shapenr: ").append(shapenr).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public EditLatt copy() { try {return (EditLatt)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

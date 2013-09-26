package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MDeformVert implements DNA, Cloneable { // #51
  public MDeformVert[] myarray;
  public MDeformWeight dw; // ptr 8
  public int totweight; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    dw = DNATools.link(DNATools.ptr(buffer), MDeformWeight.class); // get ptr
    totweight = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(dw!=null?dw.hashCode():0);
    buffer.writeInt(totweight);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (MDeformVert[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MDeformVert:\n");
    sb.append("  dw: ").append(dw).append("\n");
    sb.append("  totweight: ").append(totweight).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public MDeformVert copy() { try {return (MDeformVert)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

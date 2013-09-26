package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IdAdtTemplate extends ID implements DNA, Cloneable { // #388
  public IdAdtTemplate[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (IdAdtTemplate[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("IdAdtTemplate:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    return sb.toString();
  }
  public IdAdtTemplate copy() { try {return (IdAdtTemplate)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Generator implements DNA, Cloneable { // #366
  public FMod_Generator[] myarray;
  public Object coefficients; // ptr 4
  public int arraysize; // 4
  public int poly_order; // 4
  public int mode; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    coefficients = DNATools.ptr(buffer); // get ptr
    arraysize = buffer.getInt();
    poly_order = buffer.getInt();
    mode = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(coefficients!=null?coefficients.hashCode():0);
    buffer.writeInt(arraysize);
    buffer.writeInt(poly_order);
    buffer.writeInt(mode);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Generator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Generator:\n");
    sb.append("  coefficients: ").append(coefficients).append("\n");
    sb.append("  arraysize: ").append(arraysize).append("\n");
    sb.append("  poly_order: ").append(poly_order).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public FMod_Generator copy() { try {return (FMod_Generator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CustomDataExternal implements DNA, Cloneable { // #338
  public CustomDataExternal[] myarray;
  public byte[] filename = new byte[240]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(filename);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(filename);
  }
  public Object setmyarray(Object array) {
    myarray = (CustomDataExternal[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CustomDataExternal:\n");
    sb.append("  filename: ").append(new String(filename)).append("\n");
    return sb.toString();
  }
  public CustomDataExternal copy() { try {return (CustomDataExternal)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

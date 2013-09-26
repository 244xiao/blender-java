package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MStringProperty implements DNA, Cloneable { // #62
  public MStringProperty[] myarray;
  public byte[] s = new byte[256]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(s);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(s);
  }
  public Object setmyarray(Object array) {
    myarray = (MStringProperty[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MStringProperty:\n");
    sb.append("  s: ").append(new String(s)).append("\n");
    return sb.toString();
  }
  public MStringProperty copy() { try {return (MStringProperty)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MFloatProperty implements DNA, Cloneable { // #60
  public MFloatProperty[] myarray;
  public float f; // 4

  public void read(ByteBuffer buffer) {
    f = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(f);
  }
  public Object setmyarray(Object array) {
    myarray = (MFloatProperty[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MFloatProperty:\n");
    sb.append("  f: ").append(f).append("\n");
    return sb.toString();
  }
  public MFloatProperty copy() { try {return (MFloatProperty)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

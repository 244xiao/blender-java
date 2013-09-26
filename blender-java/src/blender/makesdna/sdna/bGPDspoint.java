package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGPDspoint implements DNA, Cloneable { // #353
  public bGPDspoint[] myarray;
  public float x; // 4
  public float y; // 4
  public float z; // 4
  public float pressure; // 4

  public void read(ByteBuffer buffer) {
    x = buffer.getFloat();
    y = buffer.getFloat();
    z = buffer.getFloat();
    pressure = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(x);
    buffer.writeFloat(y);
    buffer.writeFloat(z);
    buffer.writeFloat(pressure);
  }
  public Object setmyarray(Object array) {
    myarray = (bGPDspoint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGPDspoint:\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    sb.append("  z: ").append(z).append("\n");
    sb.append("  pressure: ").append(pressure).append("\n");
    return sb.toString();
  }
  public bGPDspoint copy() { try {return (bGPDspoint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

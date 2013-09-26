package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeHueSat implements DNA, Cloneable { // #315
  public NodeHueSat[] myarray;
  public float hue; // 4
  public float sat; // 4
  public float val; // 4

  public void read(ByteBuffer buffer) {
    hue = buffer.getFloat();
    sat = buffer.getFloat();
    val = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(hue);
    buffer.writeFloat(sat);
    buffer.writeFloat(val);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeHueSat[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeHueSat:\n");
    sb.append("  hue: ").append(hue).append("\n");
    sb.append("  sat: ").append(sat).append("\n");
    sb.append("  val: ").append(val).append("\n");
    return sb.toString();
  }
  public NodeHueSat copy() { try {return (NodeHueSat)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

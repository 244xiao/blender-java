package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeBilateralBlurData implements DNA, Cloneable { // #314
  public NodeBilateralBlurData[] myarray;
  public float sigma_color; // 4
  public float sigma_space; // 4
  public short iter; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    sigma_color = buffer.getFloat();
    sigma_space = buffer.getFloat();
    iter = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(sigma_color);
    buffer.writeFloat(sigma_space);
    buffer.writeShort(iter);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeBilateralBlurData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeBilateralBlurData:\n");
    sb.append("  sigma_color: ").append(sigma_color).append("\n");
    sb.append("  sigma_space: ").append(sigma_space).append("\n");
    sb.append("  iter: ").append(iter).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public NodeBilateralBlurData copy() { try {return (NodeBilateralBlurData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

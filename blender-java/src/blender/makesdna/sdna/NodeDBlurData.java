package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeDBlurData implements DNA, Cloneable { // #313
  public NodeDBlurData[] myarray;
  public float center_x; // 4
  public float center_y; // 4
  public float distance; // 4
  public float angle; // 4
  public float spin; // 4
  public float zoom; // 4
  public short iter; // 2
  public byte wrap; // 1
  public byte pad; // 1

  public void read(ByteBuffer buffer) {
    center_x = buffer.getFloat();
    center_y = buffer.getFloat();
    distance = buffer.getFloat();
    angle = buffer.getFloat();
    spin = buffer.getFloat();
    zoom = buffer.getFloat();
    iter = buffer.getShort();
    wrap = buffer.get();
    pad = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(center_x);
    buffer.writeFloat(center_y);
    buffer.writeFloat(distance);
    buffer.writeFloat(angle);
    buffer.writeFloat(spin);
    buffer.writeFloat(zoom);
    buffer.writeShort(iter);
    buffer.writeByte(wrap);
    buffer.writeByte(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeDBlurData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeDBlurData:\n");
    sb.append("  center_x: ").append(center_x).append("\n");
    sb.append("  center_y: ").append(center_y).append("\n");
    sb.append("  distance: ").append(distance).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  spin: ").append(spin).append("\n");
    sb.append("  zoom: ").append(zoom).append("\n");
    sb.append("  iter: ").append(iter).append("\n");
    sb.append("  wrap: ").append(wrap).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public NodeDBlurData copy() { try {return (NodeDBlurData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

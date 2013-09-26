package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bCameraActuator implements DNA, Cloneable { // #246
  public bCameraActuator[] myarray;
  public bObject ob; // ptr 1296
  public float height; // 4
  public float min; // 4
  public float max; // 4
  public float pad; // 4
  public short pad1; // 2
  public short axis; // 2
  public float pad2; // 4

  public void read(ByteBuffer buffer) {
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    height = buffer.getFloat();
    min = buffer.getFloat();
    max = buffer.getFloat();
    pad = buffer.getFloat();
    pad1 = buffer.getShort();
    axis = buffer.getShort();
    pad2 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeFloat(height);
    buffer.writeFloat(min);
    buffer.writeFloat(max);
    buffer.writeFloat(pad);
    buffer.writeShort(pad1);
    buffer.writeShort(axis);
    buffer.writeFloat(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (bCameraActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bCameraActuator:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  min: ").append(min).append("\n");
    sb.append("  max: ").append(max).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public bCameraActuator copy() { try {return (bCameraActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

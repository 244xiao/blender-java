package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTouchSensor implements DNA, Cloneable { // #221
  public bTouchSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public Material ma; // ptr 800
  public float dist; // 4
  public float pad; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    ma = DNATools.link(DNATools.ptr(buffer), Material.class); // get ptr
    dist = buffer.getFloat();
    pad = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeInt(ma!=null?ma.hashCode():0);
    buffer.writeFloat(dist);
    buffer.writeFloat(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bTouchSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTouchSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  ma: ").append(ma).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bTouchSensor copy() { try {return (bTouchSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

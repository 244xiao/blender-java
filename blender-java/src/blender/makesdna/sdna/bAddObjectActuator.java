package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bAddObjectActuator implements DNA, Cloneable { // #237
  public bAddObjectActuator[] myarray;
  public int time; // 4
  public int pad; // 4
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    time = buffer.getInt();
    pad = buffer.getInt();
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(time);
    buffer.writeInt(pad);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bAddObjectActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bAddObjectActuator:\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public bAddObjectActuator copy() { try {return (bAddObjectActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

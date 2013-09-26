package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActuatorSensor implements DNA, Cloneable { // #224
  public bActuatorSensor[] myarray;
  public int type; // 4
  public int pad; // 4
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    type = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(type);
    buffer.writeInt(pad);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (bActuatorSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActuatorSensor:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public bActuatorSensor copy() { try {return (bActuatorSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

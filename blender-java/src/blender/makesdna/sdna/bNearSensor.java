package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNearSensor implements DNA, Cloneable { // #219
  public bNearSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public float dist; // 4
  public float resetdist; // 4
  public int lastval; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    dist = buffer.getFloat();
    resetdist = buffer.getFloat();
    lastval = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeFloat(dist);
    buffer.writeFloat(resetdist);
    buffer.writeInt(lastval);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bNearSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNearSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  resetdist: ").append(resetdist).append("\n");
    sb.append("  lastval: ").append(lastval).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bNearSensor copy() { try {return (bNearSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bRandomSensor implements DNA, Cloneable { // #228
  public bRandomSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public int seed; // 4
  public int delay; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    seed = buffer.getInt();
    delay = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeInt(seed);
    buffer.writeInt(delay);
  }
  public Object setmyarray(Object array) {
    myarray = (bRandomSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bRandomSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  seed: ").append(seed).append("\n");
    sb.append("  delay: ").append(delay).append("\n");
    return sb.toString();
  }
  public bRandomSensor copy() { try {return (bRandomSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

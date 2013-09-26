package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bDelaySensor implements DNA, Cloneable { // #225
  public bDelaySensor[] myarray;
  public short delay; // 2
  public short duration; // 2
  public short flag; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    delay = buffer.getShort();
    duration = buffer.getShort();
    flag = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(delay);
    buffer.writeShort(duration);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bDelaySensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bDelaySensor:\n");
    sb.append("  delay: ").append(delay).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bDelaySensor copy() { try {return (bDelaySensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

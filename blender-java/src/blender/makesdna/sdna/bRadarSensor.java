package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bRadarSensor implements DNA, Cloneable { // #227
  public bRadarSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public float angle; // 4
  public float range; // 4
  public short flag; // 2
  public short axis; // 2

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    angle = buffer.getFloat();
    range = buffer.getFloat();
    flag = buffer.getShort();
    axis = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeFloat(angle);
    buffer.writeFloat(range);
    buffer.writeShort(flag);
    buffer.writeShort(axis);
  }
  public Object setmyarray(Object array) {
    myarray = (bRadarSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bRadarSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  range: ").append(range).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    return sb.toString();
  }
  public bRadarSensor copy() { try {return (bRadarSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

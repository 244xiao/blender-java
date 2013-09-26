package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bJoystickSensor implements DNA, Cloneable { // #233
  public bJoystickSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public byte type; // 1
  public byte joyindex; // 1
  public short flag; // 2
  public short axis; // 2
  public short axis_single; // 2
  public int axisf; // 4
  public int button; // 4
  public int hat; // 4
  public int hatf; // 4
  public int precision; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    type = buffer.get();
    joyindex = buffer.get();
    flag = buffer.getShort();
    axis = buffer.getShort();
    axis_single = buffer.getShort();
    axisf = buffer.getInt();
    button = buffer.getInt();
    hat = buffer.getInt();
    hatf = buffer.getInt();
    precision = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeByte(type);
    buffer.writeByte(joyindex);
    buffer.writeShort(flag);
    buffer.writeShort(axis);
    buffer.writeShort(axis_single);
    buffer.writeInt(axisf);
    buffer.writeInt(button);
    buffer.writeInt(hat);
    buffer.writeInt(hatf);
    buffer.writeInt(precision);
  }
  public Object setmyarray(Object array) {
    myarray = (bJoystickSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bJoystickSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  joyindex: ").append(joyindex).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  axis_single: ").append(axis_single).append("\n");
    sb.append("  axisf: ").append(axisf).append("\n");
    sb.append("  button: ").append(button).append("\n");
    sb.append("  hat: ").append(hat).append("\n");
    sb.append("  hatf: ").append(hatf).append("\n");
    sb.append("  precision: ").append(precision).append("\n");
    return sb.toString();
  }
  public bJoystickSensor copy() { try {return (bJoystickSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

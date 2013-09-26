package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPropertySensor implements DNA, Cloneable { // #223
  public bPropertySensor[] myarray;
  public int type; // 4
  public int pad; // 4
  public byte[] name = new byte[32]; // 1
  public byte[] value = new byte[32]; // 1
  public byte[] maxvalue = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    type = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(name);
    buffer.get(value);
    buffer.get(maxvalue);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(type);
    buffer.writeInt(pad);
    buffer.write(name);
    buffer.write(value);
    buffer.write(maxvalue);
  }
  public Object setmyarray(Object array) {
    myarray = (bPropertySensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPropertySensor:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  value: ").append(new String(value)).append("\n");
    sb.append("  maxvalue: ").append(new String(maxvalue)).append("\n");
    return sb.toString();
  }
  public bPropertySensor copy() { try {return (bPropertySensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

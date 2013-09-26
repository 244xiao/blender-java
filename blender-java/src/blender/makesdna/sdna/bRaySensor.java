package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bRaySensor implements DNA, Cloneable { // #229
  public bRaySensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public float range; // 4
  public byte[] propname = new byte[32]; // 1
  public byte[] matname = new byte[32]; // 1
  public short mode; // 2
  public short pad1; // 2
  public int axisflag; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    range = buffer.getFloat();
    buffer.get(propname);
    buffer.get(matname);
    mode = buffer.getShort();
    pad1 = buffer.getShort();
    axisflag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeFloat(range);
    buffer.write(propname);
    buffer.write(matname);
    buffer.writeShort(mode);
    buffer.writeShort(pad1);
    buffer.writeInt(axisflag);
  }
  public Object setmyarray(Object array) {
    myarray = (bRaySensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bRaySensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  range: ").append(range).append("\n");
    sb.append("  propname: ").append(new String(propname)).append("\n");
    sb.append("  matname: ").append(new String(matname)).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  axisflag: ").append(axisflag).append("\n");
    return sb.toString();
  }
  public bRaySensor copy() { try {return (bRaySensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

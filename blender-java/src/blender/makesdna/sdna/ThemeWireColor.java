package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ThemeWireColor implements DNA, Cloneable { // #185
  public ThemeWireColor[] myarray;
  public byte[] solid = new byte[4]; // 1
  public byte[] select = new byte[4]; // 1
  public byte[] active = new byte[4]; // 1
  public short flag; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    buffer.get(solid);
    buffer.get(select);
    buffer.get(active);
    flag = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(solid);
    buffer.write(select);
    buffer.write(active);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (ThemeWireColor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ThemeWireColor:\n");
    sb.append("  solid: ").append(new String(solid)).append("\n");
    sb.append("  select: ").append(new String(select)).append("\n");
    sb.append("  active: ").append(new String(active)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public ThemeWireColor copy() { try {return (ThemeWireColor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

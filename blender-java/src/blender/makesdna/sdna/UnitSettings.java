package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class UnitSettings implements DNA, Cloneable { // #149
  public UnitSettings[] myarray;
  public float scale_length; // 4
  public byte system; // 1
  public byte system_rotation; // 1
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    scale_length = buffer.getFloat();
    system = buffer.get();
    system_rotation = buffer.get();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(scale_length);
    buffer.writeByte(system);
    buffer.writeByte(system_rotation);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (UnitSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("UnitSettings:\n");
    sb.append("  scale_length: ").append(scale_length).append("\n");
    sb.append("  system: ").append(system).append("\n");
    sb.append("  system_rotation: ").append(system_rotation).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public UnitSettings copy() { try {return (UnitSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

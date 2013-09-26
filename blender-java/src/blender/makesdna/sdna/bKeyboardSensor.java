package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bKeyboardSensor implements DNA, Cloneable { // #222
  public bKeyboardSensor[] myarray;
  public short key; // 2
  public short qual; // 2
  public short type; // 2
  public short qual2; // 2
  public byte[] targetName = new byte[32]; // 1
  public byte[] toggleName = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    key = buffer.getShort();
    qual = buffer.getShort();
    type = buffer.getShort();
    qual2 = buffer.getShort();
    buffer.get(targetName);
    buffer.get(toggleName);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(key);
    buffer.writeShort(qual);
    buffer.writeShort(type);
    buffer.writeShort(qual2);
    buffer.write(targetName);
    buffer.write(toggleName);
  }
  public Object setmyarray(Object array) {
    myarray = (bKeyboardSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bKeyboardSensor:\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  qual: ").append(qual).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  qual2: ").append(qual2).append("\n");
    sb.append("  targetName: ").append(new String(targetName)).append("\n");
    sb.append("  toggleName: ").append(new String(toggleName)).append("\n");
    return sb.toString();
  }
  public bKeyboardSensor copy() { try {return (bKeyboardSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

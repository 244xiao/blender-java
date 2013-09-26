package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bArmatureSensor implements DNA, Cloneable { // #230
  public bArmatureSensor[] myarray;
  public byte[] posechannel = new byte[32]; // 1
  public byte[] constraint = new byte[32]; // 1
  public int type; // 4
  public float value; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(posechannel);
    buffer.get(constraint);
    type = buffer.getInt();
    value = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(posechannel);
    buffer.write(constraint);
    buffer.writeInt(type);
    buffer.writeFloat(value);
  }
  public Object setmyarray(Object array) {
    myarray = (bArmatureSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bArmatureSensor:\n");
    sb.append("  posechannel: ").append(new String(posechannel)).append("\n");
    sb.append("  constraint: ").append(new String(constraint)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  value: ").append(value).append("\n");
    return sb.toString();
  }
  public bArmatureSensor copy() { try {return (bArmatureSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

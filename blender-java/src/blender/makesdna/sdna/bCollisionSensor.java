package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bCollisionSensor implements DNA, Cloneable { // #226
  public bCollisionSensor[] myarray;
  public byte[] name = new byte[32]; // 1
  public byte[] materialName = new byte[32]; // 1
  public short damptimer; // 2
  public short damp; // 2
  public short mode; // 2
  public short pad2; // 2

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    buffer.get(materialName);
    damptimer = buffer.getShort();
    damp = buffer.getShort();
    mode = buffer.getShort();
    pad2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.write(materialName);
    buffer.writeShort(damptimer);
    buffer.writeShort(damp);
    buffer.writeShort(mode);
    buffer.writeShort(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (bCollisionSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bCollisionSensor:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  materialName: ").append(new String(materialName)).append("\n");
    sb.append("  damptimer: ").append(damptimer).append("\n");
    sb.append("  damp: ").append(damp).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public bCollisionSensor copy() { try {return (bCollisionSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

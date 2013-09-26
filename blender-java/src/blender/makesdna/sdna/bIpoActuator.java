package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bIpoActuator implements DNA, Cloneable { // #245
  public bIpoActuator[] myarray;
  public short flag; // 2
  public short type; // 2
  public float sta; // 4
  public float end; // 4
  public byte[] name = new byte[32]; // 1
  public byte[] frameProp = new byte[32]; // 1
  public short pad1; // 2
  public short pad2; // 2
  public short pad3; // 2
  public short pad4; // 2

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    type = buffer.getShort();
    sta = buffer.getFloat();
    end = buffer.getFloat();
    buffer.get(name);
    buffer.get(frameProp);
    pad1 = buffer.getShort();
    pad2 = buffer.getShort();
    pad3 = buffer.getShort();
    pad4 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeFloat(sta);
    buffer.writeFloat(end);
    buffer.write(name);
    buffer.write(frameProp);
    buffer.writeShort(pad1);
    buffer.writeShort(pad2);
    buffer.writeShort(pad3);
    buffer.writeShort(pad4);
  }
  public Object setmyarray(Object array) {
    myarray = (bIpoActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bIpoActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  frameProp: ").append(new String(frameProp)).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  pad4: ").append(pad4).append("\n");
    return sb.toString();
  }
  public bIpoActuator copy() { try {return (bIpoActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

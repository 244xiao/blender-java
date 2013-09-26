package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGameActuator implements DNA, Cloneable { // #251
  public bGameActuator[] myarray;
  public short flag; // 2
  public short type; // 2
  public int sta; // 4
  public int end; // 4
  public byte[] filename = new byte[64]; // 1
  public byte[] loadaniname = new byte[64]; // 1

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    type = buffer.getShort();
    sta = buffer.getInt();
    end = buffer.getInt();
    buffer.get(filename);
    buffer.get(loadaniname);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeInt(sta);
    buffer.writeInt(end);
    buffer.write(filename);
    buffer.write(loadaniname);
  }
  public Object setmyarray(Object array) {
    myarray = (bGameActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGameActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  filename: ").append(new String(filename)).append("\n");
    sb.append("  loadaniname: ").append(new String(loadaniname)).append("\n");
    return sb.toString();
  }
  public bGameActuator copy() { try {return (bGameActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

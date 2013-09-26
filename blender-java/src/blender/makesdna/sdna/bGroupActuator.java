package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGroupActuator implements DNA, Cloneable { // #248
  public bGroupActuator[] myarray;
  public short flag; // 2
  public short type; // 2
  public int sta; // 4
  public int end; // 4
  public byte[] name = new byte[32]; // 1
  public short[] pad = new short[3]; // 2
  public short cur; // 2
  public short butsta; // 2
  public short butend; // 2

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    type = buffer.getShort();
    sta = buffer.getInt();
    end = buffer.getInt();
    buffer.get(name);
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    cur = buffer.getShort();
    butsta = buffer.getShort();
    butend = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeInt(sta);
    buffer.writeInt(end);
    buffer.write(name);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.writeShort(cur);
    buffer.writeShort(butsta);
    buffer.writeShort(butend);
  }
  public Object setmyarray(Object array) {
    myarray = (bGroupActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGroupActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    sb.append("  butsta: ").append(butsta).append("\n");
    sb.append("  butend: ").append(butend).append("\n");
    return sb.toString();
  }
  public bGroupActuator copy() { try {return (bGroupActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

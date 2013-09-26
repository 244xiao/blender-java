package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionActuator implements DNA, Cloneable { // #238
  public bActionActuator[] myarray;
  public bAction act; // ptr 152
  public short type; // 2
  public short flag; // 2
  public float sta; // 4
  public float end; // 4
  public byte[] name = new byte[32]; // 1
  public byte[] frameProp = new byte[32]; // 1
  public short blendin; // 2
  public short priority; // 2
  public short end_reset; // 2
  public short strideaxis; // 2
  public float stridelength; // 4

  public void read(ByteBuffer buffer) {
    act = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    sta = buffer.getFloat();
    end = buffer.getFloat();
    buffer.get(name);
    buffer.get(frameProp);
    blendin = buffer.getShort();
    priority = buffer.getShort();
    end_reset = buffer.getShort();
    strideaxis = buffer.getShort();
    stridelength = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(act!=null?act.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeFloat(sta);
    buffer.writeFloat(end);
    buffer.write(name);
    buffer.write(frameProp);
    buffer.writeShort(blendin);
    buffer.writeShort(priority);
    buffer.writeShort(end_reset);
    buffer.writeShort(strideaxis);
    buffer.writeFloat(stridelength);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionActuator:\n");
    sb.append("  act: ").append(act).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  frameProp: ").append(new String(frameProp)).append("\n");
    sb.append("  blendin: ").append(blendin).append("\n");
    sb.append("  priority: ").append(priority).append("\n");
    sb.append("  end_reset: ").append(end_reset).append("\n");
    sb.append("  strideaxis: ").append(strideaxis).append("\n");
    sb.append("  stridelength: ").append(stridelength).append("\n");
    return sb.toString();
  }
  public bActionActuator copy() { try {return (bActionActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

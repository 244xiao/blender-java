package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSensor extends Link<bSensor> implements DNA, Cloneable { // #232
  public bSensor[] myarray;
  public short type; // 2
  public short otype; // 2
  public short flag; // 2
  public short pulse; // 2
  public short freq; // 2
  public short totlinks; // 2
  public short pad1; // 2
  public short pad2; // 2
  public byte[] name = new byte[32]; // 1
  public Object data; // ptr 0
  public bController[] links; // ptr 104
  public bObject ob; // ptr 1296
  public short invert; // 2
  public short level; // 2
  public short tap; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bSensor.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bSensor.class); // get ptr
    type = buffer.getShort();
    otype = buffer.getShort();
    flag = buffer.getShort();
    pulse = buffer.getShort();
    freq = buffer.getShort();
    totlinks = buffer.getShort();
    pad1 = buffer.getShort();
    pad2 = buffer.getShort();
    buffer.get(name);
    data = DNATools.ptr(buffer); // get ptr
    links = DNATools.link(DNATools.ptr(buffer), bController[].class); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    invert = buffer.getShort();
    level = buffer.getShort();
    tap = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(otype);
    buffer.writeShort(flag);
    buffer.writeShort(pulse);
    buffer.writeShort(freq);
    buffer.writeShort(totlinks);
    buffer.writeShort(pad1);
    buffer.writeShort(pad2);
    buffer.write(name);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(links!=null?links.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeShort(invert);
    buffer.writeShort(level);
    buffer.writeShort(tap);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSensor:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  otype: ").append(otype).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pulse: ").append(pulse).append("\n");
    sb.append("  freq: ").append(freq).append("\n");
    sb.append("  totlinks: ").append(totlinks).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  links: ").append(Arrays.toString(links)).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  invert: ").append(invert).append("\n");
    sb.append("  level: ").append(level).append("\n");
    sb.append("  tap: ").append(tap).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bSensor copy() { try {return (bSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bController extends Link<bController> implements DNA, Cloneable { // #236
  public bController[] myarray;
  public bController mynew; // ptr 104
  public short type; // 2
  public short flag; // 2
  public short inputs; // 2
  public short totlinks; // 2
  public short otype; // 2
  public short totslinks; // 2
  public short pad2; // 2
  public short pad3; // 2
  public byte[] name = new byte[32]; // 1
  public Object data; // ptr 0
  public bActuator[] links; // ptr 80
  public bSensor[] slinks; // ptr 96
  public short val; // 2
  public short valo; // 2
  public int state_mask; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bController.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bController.class); // get ptr
    mynew = DNATools.link(DNATools.ptr(buffer), bController.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    inputs = buffer.getShort();
    totlinks = buffer.getShort();
    otype = buffer.getShort();
    totslinks = buffer.getShort();
    pad2 = buffer.getShort();
    pad3 = buffer.getShort();
    buffer.get(name);
    data = DNATools.ptr(buffer); // get ptr
    links = DNATools.link(DNATools.ptr(buffer), bActuator[].class); // get ptr
    slinks = DNATools.link(DNATools.ptr(buffer), bSensor[].class); // get ptr
    val = buffer.getShort();
    valo = buffer.getShort();
    state_mask = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(mynew!=null?mynew.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(inputs);
    buffer.writeShort(totlinks);
    buffer.writeShort(otype);
    buffer.writeShort(totslinks);
    buffer.writeShort(pad2);
    buffer.writeShort(pad3);
    buffer.write(name);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(links!=null?links.hashCode():0);
    buffer.writeInt(slinks!=null?slinks.hashCode():0);
    buffer.writeShort(val);
    buffer.writeShort(valo);
    buffer.writeInt(state_mask);
  }
  public Object setmyarray(Object array) {
    myarray = (bController[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bController:\n");
    sb.append("  mynew: ").append(mynew).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  inputs: ").append(inputs).append("\n");
    sb.append("  totlinks: ").append(totlinks).append("\n");
    sb.append("  otype: ").append(otype).append("\n");
    sb.append("  totslinks: ").append(totslinks).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  links: ").append(Arrays.toString(links)).append("\n");
    sb.append("  slinks: ").append(Arrays.toString(slinks)).append("\n");
    sb.append("  val: ").append(val).append("\n");
    sb.append("  valo: ").append(valo).append("\n");
    sb.append("  state_mask: ").append(state_mask).append("\n");
    return sb.toString();
  }
  public bController copy() { try {return (bController)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

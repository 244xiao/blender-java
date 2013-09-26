package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BGpic extends Link<BGpic> implements DNA, Cloneable { // #152
  public BGpic[] myarray;
  public Image ima; // ptr 496
  public ImageUser iuser = new ImageUser(); // 40
  public float xof; // 4
  public float yof; // 4
  public float size; // 4
  public float blend; // 4
  public short view; // 2
  public short flag; // 2
  public float pad2; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), BGpic.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), BGpic.class); // get ptr
    ima = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    iuser.read(buffer);
    xof = buffer.getFloat();
    yof = buffer.getFloat();
    size = buffer.getFloat();
    blend = buffer.getFloat();
    view = buffer.getShort();
    flag = buffer.getShort();
    pad2 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ima!=null?ima.hashCode():0);
    iuser.write(buffer);
    buffer.writeFloat(xof);
    buffer.writeFloat(yof);
    buffer.writeFloat(size);
    buffer.writeFloat(blend);
    buffer.writeShort(view);
    buffer.writeShort(flag);
    buffer.writeFloat(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (BGpic[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BGpic:\n");
    sb.append("  ima: ").append(ima).append("\n");
    sb.append("  iuser: ").append(iuser).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  blend: ").append(blend).append("\n");
    sb.append("  view: ").append(view).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public BGpic copy() { try {return (BGpic)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPythonConstraint implements DNA, Cloneable { // #278
  public bPythonConstraint[] myarray;
  public Text text; // ptr 176
  public IDProperty prop; // ptr 96
  public int flag; // 4
  public int tarnum; // 4
  public ListBase targets = new ListBase(); // 16
  public bObject tar; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    text = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
    prop = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    flag = buffer.getInt();
    tarnum = buffer.getInt();
    targets.read(buffer);
    tar = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(text!=null?text.hashCode():0);
    buffer.writeInt(prop!=null?prop.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(tarnum);
    targets.write(buffer);
    buffer.writeInt(tar!=null?tar.hashCode():0);
    buffer.write(subtarget);
  }
  public Object setmyarray(Object array) {
    myarray = (bPythonConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPythonConstraint:\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  prop: ").append(prop).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  tarnum: ").append(tarnum).append("\n");
    sb.append("  targets: ").append(targets).append("\n");
    sb.append("  tar: ").append(tar).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    return sb.toString();
  }
  public bPythonConstraint copy() { try {return (bPythonConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

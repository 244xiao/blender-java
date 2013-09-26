package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPythonCont implements DNA, Cloneable { // #235
  public bPythonCont[] myarray;
  public Text text; // ptr 176
  public byte[] module = new byte[64]; // 1
  public int mode; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    text = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
    buffer.get(module);
    mode = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(text!=null?text.hashCode():0);
    buffer.write(module);
    buffer.writeInt(mode);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bPythonCont[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPythonCont:\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  module: ").append(new String(module)).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bPythonCont copy() { try {return (bPythonCont)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

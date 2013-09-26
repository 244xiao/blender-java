package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CharInfo implements DNA, Cloneable { // #42
  public CharInfo[] myarray;
  public short kern; // 2
  public short mat_nr; // 2
  public byte flag; // 1
  public byte pad; // 1
  public short pad2; // 2

  public void read(ByteBuffer buffer) {
    kern = buffer.getShort();
    mat_nr = buffer.getShort();
    flag = buffer.get();
    pad = buffer.get();
    pad2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(kern);
    buffer.writeShort(mat_nr);
    buffer.writeByte(flag);
    buffer.writeByte(pad);
    buffer.writeShort(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (CharInfo[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CharInfo:\n");
    sb.append("  kern: ").append(kern).append("\n");
    sb.append("  mat_nr: ").append(mat_nr).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public CharInfo copy() { try {return (CharInfo)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

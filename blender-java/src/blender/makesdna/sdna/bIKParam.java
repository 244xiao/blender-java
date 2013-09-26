package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bIKParam implements DNA, Cloneable { // #268
  public bIKParam[] myarray;
  public int iksolver; // 4

  public void read(ByteBuffer buffer) {
    iksolver = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(iksolver);
  }
  public Object setmyarray(Object array) {
    myarray = (bIKParam[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bIKParam:\n");
    sb.append("  iksolver: ").append(iksolver).append("\n");
    return sb.toString();
  }
  public bIKParam copy() { try {return (bIKParam)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

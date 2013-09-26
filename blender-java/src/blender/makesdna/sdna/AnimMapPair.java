package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AnimMapPair implements DNA, Cloneable { // #380
  public AnimMapPair[] myarray;
  public byte[] from = new byte[128]; // 1
  public byte[] to = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(from);
    buffer.get(to);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(from);
    buffer.write(to);
  }
  public Object setmyarray(Object array) {
    myarray = (AnimMapPair[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AnimMapPair:\n");
    sb.append("  from: ").append(new String(from)).append("\n");
    sb.append("  to: ").append(new String(to)).append("\n");
    return sb.toString();
  }
  public AnimMapPair copy() { try {return (AnimMapPair)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

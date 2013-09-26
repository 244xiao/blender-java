package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class StripTransform implements DNA, Cloneable { // #199
  public StripTransform[] myarray;
  public int xofs; // 4
  public int yofs; // 4

  public void read(ByteBuffer buffer) {
    xofs = buffer.getInt();
    yofs = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(xofs);
    buffer.writeInt(yofs);
  }
  public Object setmyarray(Object array) {
    myarray = (StripTransform[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("StripTransform:\n");
    sb.append("  xofs: ").append(xofs).append("\n");
    sb.append("  yofs: ").append(yofs).append("\n");
    return sb.toString();
  }
  public StripTransform copy() { try {return (StripTransform)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

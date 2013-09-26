package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bExpressionCont implements DNA, Cloneable { // #234
  public bExpressionCont[] myarray;
  public byte[] str = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(str);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(str);
  }
  public Object setmyarray(Object array) {
    myarray = (bExpressionCont[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bExpressionCont:\n");
    sb.append("  str: ").append(new String(str)).append("\n");
    return sb.toString();
  }
  public bExpressionCont copy() { try {return (bExpressionCont)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

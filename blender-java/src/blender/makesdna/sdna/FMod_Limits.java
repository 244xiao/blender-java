package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Limits implements DNA, Cloneable { // #372
  public FMod_Limits[] myarray;
  public rctf rect = new rctf(); // 16
  public int flag; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    rect.read(buffer);
    flag = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    rect.write(buffer);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Limits[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Limits:\n");
    sb.append("  rect: ").append(rect).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public FMod_Limits copy() { try {return (FMod_Limits)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

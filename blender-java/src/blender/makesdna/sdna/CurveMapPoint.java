package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CurveMapPoint implements DNA, Cloneable { // #330
  public CurveMapPoint[] myarray;
  public float x; // 4
  public float y; // 4
  public short flag; // 2
  public short shorty; // 2

  public void read(ByteBuffer buffer) {
    x = buffer.getFloat();
    y = buffer.getFloat();
    flag = buffer.getShort();
    shorty = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(x);
    buffer.writeFloat(y);
    buffer.writeShort(flag);
    buffer.writeShort(shorty);
  }
  public Object setmyarray(Object array) {
    myarray = (CurveMapPoint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CurveMapPoint:\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  shorty: ").append(shorty).append("\n");
    return sb.toString();
  }
  public CurveMapPoint copy() { try {return (CurveMapPoint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

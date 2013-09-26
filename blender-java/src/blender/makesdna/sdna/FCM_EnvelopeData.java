package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FCM_EnvelopeData implements DNA, Cloneable { // #368
  public FCM_EnvelopeData[] myarray;
  public float min; // 4
  public float max; // 4
  public float time; // 4
  public short f1; // 2
  public short f2; // 2

  public void read(ByteBuffer buffer) {
    min = buffer.getFloat();
    max = buffer.getFloat();
    time = buffer.getFloat();
    f1 = buffer.getShort();
    f2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(min);
    buffer.writeFloat(max);
    buffer.writeFloat(time);
    buffer.writeShort(f1);
    buffer.writeShort(f2);
  }
  public Object setmyarray(Object array) {
    myarray = (FCM_EnvelopeData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FCM_EnvelopeData:\n");
    sb.append("  min: ").append(min).append("\n");
    sb.append("  max: ").append(max).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  f1: ").append(f1).append("\n");
    sb.append("  f2: ").append(f2).append("\n");
    return sb.toString();
  }
  public FCM_EnvelopeData copy() { try {return (FCM_EnvelopeData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

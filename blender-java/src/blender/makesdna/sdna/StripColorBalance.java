package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class StripColorBalance implements DNA, Cloneable { // #200
  public StripColorBalance[] myarray;
  public float[] lift = new float[3]; // 4
  public float[] gamma = new float[3]; // 4
  public float[] gain = new float[3]; // 4
  public int flag; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<lift.length;i++) lift[i]=buffer.getFloat();
    for(int i=0;i<gamma.length;i++) gamma[i]=buffer.getFloat();
    for(int i=0;i<gain.length;i++) gain[i]=buffer.getFloat();
    flag = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<lift.length;i++) buffer.writeFloat(lift[i]);
    for(int i=0;i<gamma.length;i++) buffer.writeFloat(gamma[i]);
    for(int i=0;i<gain.length;i++) buffer.writeFloat(gain[i]);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (StripColorBalance[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("StripColorBalance:\n");
    sb.append("  lift: ").append(Arrays.toString(lift)).append("\n");
    sb.append("  gamma: ").append(Arrays.toString(gamma)).append("\n");
    sb.append("  gain: ").append(Arrays.toString(gain)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public StripColorBalance copy() { try {return (StripColorBalance)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

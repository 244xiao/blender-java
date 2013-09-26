package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Envelope implements DNA, Cloneable { // #369
  public FMod_Envelope[] myarray;
  public FCM_EnvelopeData data; // ptr 16
  public int totvert; // 4
  public float midval; // 4
  public float min; // 4
  public float max; // 4

  public void read(ByteBuffer buffer) {
    data = DNATools.link(DNATools.ptr(buffer), FCM_EnvelopeData.class); // get ptr
    totvert = buffer.getInt();
    midval = buffer.getFloat();
    min = buffer.getFloat();
    max = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(totvert);
    buffer.writeFloat(midval);
    buffer.writeFloat(min);
    buffer.writeFloat(max);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Envelope[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Envelope:\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  midval: ").append(midval).append("\n");
    sb.append("  min: ").append(min).append("\n");
    sb.append("  max: ").append(max).append("\n");
    return sb.toString();
  }
  public FMod_Envelope copy() { try {return (FMod_Envelope)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

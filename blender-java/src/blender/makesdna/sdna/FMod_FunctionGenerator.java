package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_FunctionGenerator implements DNA, Cloneable { // #367
  public FMod_FunctionGenerator[] myarray;
  public float amplitude; // 4
  public float phase_multiplier; // 4
  public float phase_offset; // 4
  public float value_offset; // 4
  public int type; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    amplitude = buffer.getFloat();
    phase_multiplier = buffer.getFloat();
    phase_offset = buffer.getFloat();
    value_offset = buffer.getFloat();
    type = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(amplitude);
    buffer.writeFloat(phase_multiplier);
    buffer.writeFloat(phase_offset);
    buffer.writeFloat(value_offset);
    buffer.writeInt(type);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_FunctionGenerator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_FunctionGenerator:\n");
    sb.append("  amplitude: ").append(amplitude).append("\n");
    sb.append("  phase_multiplier: ").append(phase_multiplier).append("\n");
    sb.append("  phase_offset: ").append(phase_offset).append("\n");
    sb.append("  value_offset: ").append(value_offset).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public FMod_FunctionGenerator copy() { try {return (FMod_FunctionGenerator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

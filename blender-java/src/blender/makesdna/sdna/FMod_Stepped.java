package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Stepped implements DNA, Cloneable { // #374
  public FMod_Stepped[] myarray;
  public float step_size; // 4
  public float offset; // 4
  public float start_frame; // 4
  public float end_frame; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    step_size = buffer.getFloat();
    offset = buffer.getFloat();
    start_frame = buffer.getFloat();
    end_frame = buffer.getFloat();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(step_size);
    buffer.writeFloat(offset);
    buffer.writeFloat(start_frame);
    buffer.writeFloat(end_frame);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Stepped[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Stepped:\n");
    sb.append("  step_size: ").append(step_size).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  start_frame: ").append(start_frame).append("\n");
    sb.append("  end_frame: ").append(end_frame).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public FMod_Stepped copy() { try {return (FMod_Stepped)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Sound3D implements DNA, Cloneable { // #239
  public Sound3D[] myarray;
  public float min_gain; // 4
  public float max_gain; // 4
  public float reference_distance; // 4
  public float max_distance; // 4
  public float rolloff_factor; // 4
  public float cone_inner_angle; // 4
  public float cone_outer_angle; // 4
  public float cone_outer_gain; // 4

  public void read(ByteBuffer buffer) {
    min_gain = buffer.getFloat();
    max_gain = buffer.getFloat();
    reference_distance = buffer.getFloat();
    max_distance = buffer.getFloat();
    rolloff_factor = buffer.getFloat();
    cone_inner_angle = buffer.getFloat();
    cone_outer_angle = buffer.getFloat();
    cone_outer_gain = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(min_gain);
    buffer.writeFloat(max_gain);
    buffer.writeFloat(reference_distance);
    buffer.writeFloat(max_distance);
    buffer.writeFloat(rolloff_factor);
    buffer.writeFloat(cone_inner_angle);
    buffer.writeFloat(cone_outer_angle);
    buffer.writeFloat(cone_outer_gain);
  }
  public Object setmyarray(Object array) {
    myarray = (Sound3D[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Sound3D:\n");
    sb.append("  min_gain: ").append(min_gain).append("\n");
    sb.append("  max_gain: ").append(max_gain).append("\n");
    sb.append("  reference_distance: ").append(reference_distance).append("\n");
    sb.append("  max_distance: ").append(max_distance).append("\n");
    sb.append("  rolloff_factor: ").append(rolloff_factor).append("\n");
    sb.append("  cone_inner_angle: ").append(cone_inner_angle).append("\n");
    sb.append("  cone_outer_angle: ").append(cone_outer_angle).append("\n");
    sb.append("  cone_outer_gain: ").append(cone_outer_gain).append("\n");
    return sb.toString();
  }
  public Sound3D copy() { try {return (Sound3D)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

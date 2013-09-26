package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AudioData implements DNA, Cloneable { // #132
  public AudioData[] myarray;
  public int mixrate; // 4
  public float main; // 4
  public float speed_of_sound; // 4
  public float doppler_factor; // 4
  public int distance_model; // 4
  public short flag; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    mixrate = buffer.getInt();
    main = buffer.getFloat();
    speed_of_sound = buffer.getFloat();
    doppler_factor = buffer.getFloat();
    distance_model = buffer.getInt();
    flag = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(mixrate);
    buffer.writeFloat(main);
    buffer.writeFloat(speed_of_sound);
    buffer.writeFloat(doppler_factor);
    buffer.writeInt(distance_model);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (AudioData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AudioData:\n");
    sb.append("  mixrate: ").append(mixrate).append("\n");
    sb.append("  main: ").append(main).append("\n");
    sb.append("  speed_of_sound: ").append(speed_of_sound).append("\n");
    sb.append("  doppler_factor: ").append(doppler_factor).append("\n");
    sb.append("  distance_model: ").append(distance_model).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public AudioData copy() { try {return (AudioData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

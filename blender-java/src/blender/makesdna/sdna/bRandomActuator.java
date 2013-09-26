package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bRandomActuator implements DNA, Cloneable { // #249
  public bRandomActuator[] myarray;
  public int seed; // 4
  public int distribution; // 4
  public int int_arg_1; // 4
  public int int_arg_2; // 4
  public float float_arg_1; // 4
  public float float_arg_2; // 4
  public byte[] propname = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    seed = buffer.getInt();
    distribution = buffer.getInt();
    int_arg_1 = buffer.getInt();
    int_arg_2 = buffer.getInt();
    float_arg_1 = buffer.getFloat();
    float_arg_2 = buffer.getFloat();
    buffer.get(propname);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(seed);
    buffer.writeInt(distribution);
    buffer.writeInt(int_arg_1);
    buffer.writeInt(int_arg_2);
    buffer.writeFloat(float_arg_1);
    buffer.writeFloat(float_arg_2);
    buffer.write(propname);
  }
  public Object setmyarray(Object array) {
    myarray = (bRandomActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bRandomActuator:\n");
    sb.append("  seed: ").append(seed).append("\n");
    sb.append("  distribution: ").append(distribution).append("\n");
    sb.append("  int_arg_1: ").append(int_arg_1).append("\n");
    sb.append("  int_arg_2: ").append(int_arg_2).append("\n");
    sb.append("  float_arg_1: ").append(float_arg_1).append("\n");
    sb.append("  float_arg_2: ").append(float_arg_2).append("\n");
    sb.append("  propname: ").append(new String(propname)).append("\n");
    return sb.toString();
  }
  public bRandomActuator copy() { try {return (bRandomActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTwoDFilterActuator implements DNA, Cloneable { // #253
  public bTwoDFilterActuator[] myarray;
  public byte[] pad = new byte[4]; // 1
  public short type; // 2
  public short flag; // 2
  public int int_arg; // 4
  public float float_arg; // 4
  public Text text; // ptr 176

  public void read(ByteBuffer buffer) {
    buffer.get(pad);
    type = buffer.getShort();
    flag = buffer.getShort();
    int_arg = buffer.getInt();
    float_arg = buffer.getFloat();
    text = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(pad);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeInt(int_arg);
    buffer.writeFloat(float_arg);
    buffer.writeInt(text!=null?text.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bTwoDFilterActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTwoDFilterActuator:\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  int_arg: ").append(int_arg).append("\n");
    sb.append("  float_arg: ").append(float_arg).append("\n");
    sb.append("  text: ").append(text).append("\n");
    return sb.toString();
  }
  public bTwoDFilterActuator copy() { try {return (bTwoDFilterActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

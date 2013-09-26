package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BuildModifierData extends ModifierData implements DNA, Cloneable { // #76
  public BuildModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float start; // 4
  public float length; // 4
  public int randomize; // 4
  public int seed; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    start = buffer.getFloat();
    length = buffer.getFloat();
    randomize = buffer.getInt();
    seed = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(start);
    buffer.writeFloat(length);
    buffer.writeInt(randomize);
    buffer.writeInt(seed);
  }
  public Object setmyarray(Object array) {
    myarray = (BuildModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BuildModifierData:\n");
    sb.append(super.toString());
    sb.append("  start: ").append(start).append("\n");
    sb.append("  length: ").append(length).append("\n");
    sb.append("  randomize: ").append(randomize).append("\n");
    sb.append("  seed: ").append(seed).append("\n");
    return sb.toString();
  }
  public BuildModifierData copy() { try {return (BuildModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

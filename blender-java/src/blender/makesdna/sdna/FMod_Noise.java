package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Noise implements DNA, Cloneable { // #373
  public FMod_Noise[] myarray;
  public float size; // 4
  public float strength; // 4
  public float phase; // 4
  public float pad; // 4
  public short depth; // 2
  public short modification; // 2

  public void read(ByteBuffer buffer) {
    size = buffer.getFloat();
    strength = buffer.getFloat();
    phase = buffer.getFloat();
    pad = buffer.getFloat();
    depth = buffer.getShort();
    modification = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(size);
    buffer.writeFloat(strength);
    buffer.writeFloat(phase);
    buffer.writeFloat(pad);
    buffer.writeShort(depth);
    buffer.writeShort(modification);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Noise[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Noise:\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  strength: ").append(strength).append("\n");
    sb.append("  phase: ").append(phase).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  depth: ").append(depth).append("\n");
    sb.append("  modification: ").append(modification).append("\n");
    return sb.toString();
  }
  public FMod_Noise copy() { try {return (FMod_Noise)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

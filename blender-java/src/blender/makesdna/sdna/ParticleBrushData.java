package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleBrushData implements DNA, Cloneable { // #142
  public ParticleBrushData[] myarray;
  public short size; // 2
  public short step; // 2
  public short invert; // 2
  public short count; // 2
  public int flag; // 4
  public float strength; // 4

  public void read(ByteBuffer buffer) {
    size = buffer.getShort();
    step = buffer.getShort();
    invert = buffer.getShort();
    count = buffer.getShort();
    flag = buffer.getInt();
    strength = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(size);
    buffer.writeShort(step);
    buffer.writeShort(invert);
    buffer.writeShort(count);
    buffer.writeInt(flag);
    buffer.writeFloat(strength);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleBrushData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleBrushData:\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  step: ").append(step).append("\n");
    sb.append("  invert: ").append(invert).append("\n");
    sb.append("  count: ").append(count).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  strength: ").append(strength).append("\n");
    return sb.toString();
  }
  public ParticleBrushData copy() { try {return (ParticleBrushData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

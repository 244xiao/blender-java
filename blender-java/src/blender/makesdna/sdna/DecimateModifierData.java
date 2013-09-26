package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class DecimateModifierData extends ModifierData implements DNA, Cloneable { // #86
  public DecimateModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float percent; // 4
  public int faceCount; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    percent = buffer.getFloat();
    faceCount = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(percent);
    buffer.writeInt(faceCount);
  }
  public Object setmyarray(Object array) {
    myarray = (DecimateModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("DecimateModifierData:\n");
    sb.append(super.toString());
    sb.append("  percent: ").append(percent).append("\n");
    sb.append("  faceCount: ").append(faceCount).append("\n");
    return sb.toString();
  }
  public DecimateModifierData copy() { try {return (DecimateModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

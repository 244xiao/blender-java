package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpeedControlVars implements DNA, Cloneable { // #211
  public SpeedControlVars[] myarray;
  public Object frameMap; // ptr 4
  public float globalSpeed; // 4
  public int flags; // 4
  public int length; // 4
  public int lastValidFrame; // 4

  public void read(ByteBuffer buffer) {
    frameMap = DNATools.ptr(buffer); // get ptr
    globalSpeed = buffer.getFloat();
    flags = buffer.getInt();
    length = buffer.getInt();
    lastValidFrame = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(frameMap!=null?frameMap.hashCode():0);
    buffer.writeFloat(globalSpeed);
    buffer.writeInt(flags);
    buffer.writeInt(length);
    buffer.writeInt(lastValidFrame);
  }
  public Object setmyarray(Object array) {
    myarray = (SpeedControlVars[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpeedControlVars:\n");
    sb.append("  frameMap: ").append(frameMap).append("\n");
    sb.append("  globalSpeed: ").append(globalSpeed).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  length: ").append(length).append("\n");
    sb.append("  lastValidFrame: ").append(lastValidFrame).append("\n");
    return sb.toString();
  }
  public SpeedControlVars copy() { try {return (SpeedControlVars)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

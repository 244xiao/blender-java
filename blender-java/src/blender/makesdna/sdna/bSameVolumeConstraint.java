package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSameVolumeConstraint implements DNA, Cloneable { // #285
  public bSameVolumeConstraint[] myarray;
  public int flag; // 4
  public float volume; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    volume = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeFloat(volume);
  }
  public Object setmyarray(Object array) {
    myarray = (bSameVolumeConstraint[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSameVolumeConstraint:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    return sb.toString();
  }
  public bSameVolumeConstraint copy() { try {return (bSameVolumeConstraint)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

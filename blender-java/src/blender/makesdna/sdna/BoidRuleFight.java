package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRuleFight implements DNA, Cloneable { // #394
  public BoidRuleFight[] myarray;
  public BoidRule rule = new BoidRule(); // 56
  public float distance; // 4
  public float flee_distance; // 4

  public void read(ByteBuffer buffer) {
    rule.read(buffer);
    distance = buffer.getFloat();
    flee_distance = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    rule.write(buffer);
    buffer.writeFloat(distance);
    buffer.writeFloat(flee_distance);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRuleFight[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRuleFight:\n");
    sb.append("  rule: ").append(rule).append("\n");
    sb.append("  distance: ").append(distance).append("\n");
    sb.append("  flee_distance: ").append(flee_distance).append("\n");
    return sb.toString();
  }
  public BoidRuleFight copy() { try {return (BoidRuleFight)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

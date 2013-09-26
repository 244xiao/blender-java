package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRuleAvoidCollision implements DNA, Cloneable { // #391
  public BoidRuleAvoidCollision[] myarray;
  public BoidRule rule = new BoidRule(); // 56
  public int options; // 4
  public float look_ahead; // 4

  public void read(ByteBuffer buffer) {
    rule.read(buffer);
    options = buffer.getInt();
    look_ahead = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    rule.write(buffer);
    buffer.writeInt(options);
    buffer.writeFloat(look_ahead);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRuleAvoidCollision[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRuleAvoidCollision:\n");
    sb.append("  rule: ").append(rule).append("\n");
    sb.append("  options: ").append(options).append("\n");
    sb.append("  look_ahead: ").append(look_ahead).append("\n");
    return sb.toString();
  }
  public BoidRuleAvoidCollision copy() { try {return (BoidRuleAvoidCollision)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

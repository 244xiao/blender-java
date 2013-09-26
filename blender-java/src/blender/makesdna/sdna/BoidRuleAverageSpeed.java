package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRuleAverageSpeed implements DNA, Cloneable { // #393
  public BoidRuleAverageSpeed[] myarray;
  public BoidRule rule = new BoidRule(); // 56
  public float wander; // 4
  public float level; // 4
  public float speed; // 4
  public float rt; // 4

  public void read(ByteBuffer buffer) {
    rule.read(buffer);
    wander = buffer.getFloat();
    level = buffer.getFloat();
    speed = buffer.getFloat();
    rt = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    rule.write(buffer);
    buffer.writeFloat(wander);
    buffer.writeFloat(level);
    buffer.writeFloat(speed);
    buffer.writeFloat(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRuleAverageSpeed[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRuleAverageSpeed:\n");
    sb.append("  rule: ").append(rule).append("\n");
    sb.append("  wander: ").append(wander).append("\n");
    sb.append("  level: ").append(level).append("\n");
    sb.append("  speed: ").append(speed).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public BoidRuleAverageSpeed copy() { try {return (BoidRuleAverageSpeed)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

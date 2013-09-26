package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRuleGoalAvoid implements DNA, Cloneable { // #390
  public BoidRuleGoalAvoid[] myarray;
  public BoidRule rule = new BoidRule(); // 56
  public bObject ob; // ptr 1296
  public int options; // 4
  public float fear_factor; // 4
  public int signal_id; // 4
  public int channels; // 4

  public void read(ByteBuffer buffer) {
    rule.read(buffer);
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    options = buffer.getInt();
    fear_factor = buffer.getFloat();
    signal_id = buffer.getInt();
    channels = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    rule.write(buffer);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeInt(options);
    buffer.writeFloat(fear_factor);
    buffer.writeInt(signal_id);
    buffer.writeInt(channels);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRuleGoalAvoid[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRuleGoalAvoid:\n");
    sb.append("  rule: ").append(rule).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  options: ").append(options).append("\n");
    sb.append("  fear_factor: ").append(fear_factor).append("\n");
    sb.append("  signal_id: ").append(signal_id).append("\n");
    sb.append("  channels: ").append(channels).append("\n");
    return sb.toString();
  }
  public BoidRuleGoalAvoid copy() { try {return (BoidRuleGoalAvoid)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

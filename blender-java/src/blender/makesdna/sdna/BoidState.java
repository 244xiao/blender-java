package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidState extends Link<BoidState> implements DNA, Cloneable { // #396
  public BoidState[] myarray;
  public ListBase rules = new ListBase(); // 16
  public ListBase conditions = new ListBase(); // 16
  public ListBase actions = new ListBase(); // 16
  public byte[] name = new byte[32]; // 1
  public int id; // 4
  public int flag; // 4
  public int ruleset_type; // 4
  public float rule_fuzziness; // 4
  public int signal_id; // 4
  public int channels; // 4
  public float volume; // 4
  public float falloff; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), BoidState.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), BoidState.class); // get ptr
    rules.read(buffer);
    conditions.read(buffer);
    actions.read(buffer);
    buffer.get(name);
    id = buffer.getInt();
    flag = buffer.getInt();
    ruleset_type = buffer.getInt();
    rule_fuzziness = buffer.getFloat();
    signal_id = buffer.getInt();
    channels = buffer.getInt();
    volume = buffer.getFloat();
    falloff = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    rules.write(buffer);
    conditions.write(buffer);
    actions.write(buffer);
    buffer.write(name);
    buffer.writeInt(id);
    buffer.writeInt(flag);
    buffer.writeInt(ruleset_type);
    buffer.writeFloat(rule_fuzziness);
    buffer.writeInt(signal_id);
    buffer.writeInt(channels);
    buffer.writeFloat(volume);
    buffer.writeFloat(falloff);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidState[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidState:\n");
    sb.append("  rules: ").append(rules).append("\n");
    sb.append("  conditions: ").append(conditions).append("\n");
    sb.append("  actions: ").append(actions).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  ruleset_type: ").append(ruleset_type).append("\n");
    sb.append("  rule_fuzziness: ").append(rule_fuzziness).append("\n");
    sb.append("  signal_id: ").append(signal_id).append("\n");
    sb.append("  channels: ").append(channels).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    sb.append("  falloff: ").append(falloff).append("\n");
    return sb.toString();
  }
  public BoidState copy() { try {return (BoidState)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

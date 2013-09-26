package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SmokeModifierData extends ModifierData implements DNA, Cloneable { // #83
  public SmokeModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public SmokeDomainSettings domain; // ptr 272
  public SmokeFlowSettings flow; // ptr 56
  public SmokeCollSettings coll; // ptr 192
  public float time; // 4
  public int type; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    domain = DNATools.link(DNATools.ptr(buffer), SmokeDomainSettings.class); // get ptr
    flow = DNATools.link(DNATools.ptr(buffer), SmokeFlowSettings.class); // get ptr
    coll = DNATools.link(DNATools.ptr(buffer), SmokeCollSettings.class); // get ptr
    time = buffer.getFloat();
    type = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(domain!=null?domain.hashCode():0);
    buffer.writeInt(flow!=null?flow.hashCode():0);
    buffer.writeInt(coll!=null?coll.hashCode():0);
    buffer.writeFloat(time);
    buffer.writeInt(type);
  }
  public Object setmyarray(Object array) {
    myarray = (SmokeModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SmokeModifierData:\n");
    sb.append(super.toString());
    sb.append("  domain: ").append(domain).append("\n");
    sb.append("  flow: ").append(flow).append("\n");
    sb.append("  coll: ").append(coll).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public SmokeModifierData copy() { try {return (SmokeModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

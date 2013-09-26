package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidRuleFollowLeader implements DNA, Cloneable { // #392
  public BoidRuleFollowLeader[] myarray;
  public BoidRule rule = new BoidRule(); // 56
  public bObject ob; // ptr 1296
  public float[] loc = new float[3]; // 4
  public float[] oloc = new float[3]; // 4
  public float cfra; // 4
  public float distance; // 4
  public int options; // 4
  public int queue_size; // 4

  public void read(ByteBuffer buffer) {
    rule.read(buffer);
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<oloc.length;i++) oloc[i]=buffer.getFloat();
    cfra = buffer.getFloat();
    distance = buffer.getFloat();
    options = buffer.getInt();
    queue_size = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    rule.write(buffer);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<oloc.length;i++) buffer.writeFloat(oloc[i]);
    buffer.writeFloat(cfra);
    buffer.writeFloat(distance);
    buffer.writeInt(options);
    buffer.writeInt(queue_size);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidRuleFollowLeader[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidRuleFollowLeader:\n");
    sb.append("  rule: ").append(rule).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  oloc: ").append(Arrays.toString(oloc)).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  distance: ").append(distance).append("\n");
    sb.append("  options: ").append(options).append("\n");
    sb.append("  queue_size: ").append(queue_size).append("\n");
    return sb.toString();
  }
  public BoidRuleFollowLeader copy() { try {return (BoidRuleFollowLeader)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

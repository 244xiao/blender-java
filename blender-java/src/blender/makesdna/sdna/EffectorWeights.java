package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class EffectorWeights implements DNA, Cloneable { // #118
  public EffectorWeights[] myarray;
  public Group group; // ptr 104
  public float[] weight = new float[13]; // 4
  public float global_gravity; // 4
  public short flag; // 2
  public short[] rt = new short[3]; // 2

  public void read(ByteBuffer buffer) {
    group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    for(int i=0;i<weight.length;i++) weight[i]=buffer.getFloat();
    global_gravity = buffer.getFloat();
    flag = buffer.getShort();
    for(int i=0;i<rt.length;i++) rt[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(group!=null?group.hashCode():0);
    for(int i=0;i<weight.length;i++) buffer.writeFloat(weight[i]);
    buffer.writeFloat(global_gravity);
    buffer.writeShort(flag);
    for(int i=0;i<rt.length;i++) buffer.writeShort(rt[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (EffectorWeights[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("EffectorWeights:\n");
    sb.append("  group: ").append(group).append("\n");
    sb.append("  weight: ").append(Arrays.toString(weight)).append("\n");
    sb.append("  global_gravity: ").append(global_gravity).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rt: ").append(Arrays.toString(rt)).append("\n");
    return sb.toString();
  }
  public EffectorWeights copy() { try {return (EffectorWeights)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

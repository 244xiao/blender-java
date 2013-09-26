package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bArmatureActuator implements DNA, Cloneable { // #256
  public bArmatureActuator[] myarray;
  public byte[] posechannel = new byte[32]; // 1
  public byte[] constraint = new byte[32]; // 1
  public int type; // 4
  public float weight; // 4
  public bObject target; // ptr 1296
  public bObject subtarget; // ptr 1296

  public void read(ByteBuffer buffer) {
    buffer.get(posechannel);
    buffer.get(constraint);
    type = buffer.getInt();
    weight = buffer.getFloat();
    target = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    subtarget = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(posechannel);
    buffer.write(constraint);
    buffer.writeInt(type);
    buffer.writeFloat(weight);
    buffer.writeInt(target!=null?target.hashCode():0);
    buffer.writeInt(subtarget!=null?subtarget.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bArmatureActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bArmatureActuator:\n");
    sb.append("  posechannel: ").append(new String(posechannel)).append("\n");
    sb.append("  constraint: ").append(new String(constraint)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  target: ").append(target).append("\n");
    sb.append("  subtarget: ").append(subtarget).append("\n");
    return sb.toString();
  }
  public bArmatureActuator copy() { try {return (bArmatureActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

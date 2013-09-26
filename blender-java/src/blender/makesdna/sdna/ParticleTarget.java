package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleTarget extends Link<ParticleTarget> implements DNA, Cloneable { // #345
  public ParticleTarget[] myarray;
  public bObject ob; // ptr 1296
  public int psys; // 4
  public short flag; // 2
  public short mode; // 2
  public float time; // 4
  public float duration; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ParticleTarget.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ParticleTarget.class); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    psys = buffer.getInt();
    flag = buffer.getShort();
    mode = buffer.getShort();
    time = buffer.getFloat();
    duration = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeInt(psys);
    buffer.writeShort(flag);
    buffer.writeShort(mode);
    buffer.writeFloat(time);
    buffer.writeFloat(duration);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleTarget[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleTarget:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  psys: ").append(psys).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    return sb.toString();
  }
  public ParticleTarget copy() { try {return (ParticleTarget)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

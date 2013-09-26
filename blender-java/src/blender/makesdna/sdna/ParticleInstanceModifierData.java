package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleInstanceModifierData extends ModifierData implements DNA, Cloneable { // #101
  public ParticleInstanceModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject ob; // ptr 1296
  public short psys; // 2
  public short flag; // 2
  public short axis; // 2
  public short rt; // 2
  public float position; // 4
  public float random_position; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    psys = buffer.getShort();
    flag = buffer.getShort();
    axis = buffer.getShort();
    rt = buffer.getShort();
    position = buffer.getFloat();
    random_position = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeShort(psys);
    buffer.writeShort(flag);
    buffer.writeShort(axis);
    buffer.writeShort(rt);
    buffer.writeFloat(position);
    buffer.writeFloat(random_position);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleInstanceModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleInstanceModifierData:\n");
    sb.append(super.toString());
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  psys: ").append(psys).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  random_position: ").append(random_position).append("\n");
    return sb.toString();
  }
  public ParticleInstanceModifierData copy() { try {return (ParticleInstanceModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MirrorModifierData extends ModifierData implements DNA, Cloneable { // #79
  public MirrorModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public short axis; // 2
  public short flag; // 2
  public float tolerance; // 4
  public bObject mirror_ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    axis = buffer.getShort();
    flag = buffer.getShort();
    tolerance = buffer.getFloat();
    mirror_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeShort(axis);
    buffer.writeShort(flag);
    buffer.writeFloat(tolerance);
    buffer.writeInt(mirror_ob!=null?mirror_ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MirrorModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MirrorModifierData:\n");
    sb.append(super.toString());
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  tolerance: ").append(tolerance).append("\n");
    sb.append("  mirror_ob: ").append(mirror_ob).append("\n");
    return sb.toString();
  }
  public MirrorModifierData copy() { try {return (MirrorModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

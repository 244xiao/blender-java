package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ShrinkwrapModifierData extends ModifierData implements DNA, Cloneable { // #105
  public ShrinkwrapModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject target; // ptr 1296
  public bObject auxTarget; // ptr 1296
  public byte[] vgroup_name = new byte[32]; // 1
  public float keepDist; // 4
  public short shrinkType; // 2
  public short shrinkOpts; // 2
  public byte projAxis; // 1
  public byte subsurfLevels; // 1
  public byte[] pad = new byte[6]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    target = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    auxTarget = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(vgroup_name);
    keepDist = buffer.getFloat();
    shrinkType = buffer.getShort();
    shrinkOpts = buffer.getShort();
    projAxis = buffer.get();
    subsurfLevels = buffer.get();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(target!=null?target.hashCode():0);
    buffer.writeInt(auxTarget!=null?auxTarget.hashCode():0);
    buffer.write(vgroup_name);
    buffer.writeFloat(keepDist);
    buffer.writeShort(shrinkType);
    buffer.writeShort(shrinkOpts);
    buffer.writeByte(projAxis);
    buffer.writeByte(subsurfLevels);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (ShrinkwrapModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ShrinkwrapModifierData:\n");
    sb.append(super.toString());
    sb.append("  target: ").append(target).append("\n");
    sb.append("  auxTarget: ").append(auxTarget).append("\n");
    sb.append("  vgroup_name: ").append(new String(vgroup_name)).append("\n");
    sb.append("  keepDist: ").append(keepDist).append("\n");
    sb.append("  shrinkType: ").append(shrinkType).append("\n");
    sb.append("  shrinkOpts: ").append(shrinkOpts).append("\n");
    sb.append("  projAxis: ").append(projAxis).append("\n");
    sb.append("  subsurfLevels: ").append(subsurfLevels).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public ShrinkwrapModifierData copy() { try {return (ShrinkwrapModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SubsurfModifierData extends ModifierData implements DNA, Cloneable { // #73
  public SubsurfModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public short subdivType; // 2
  public short levels; // 2
  public short renderLevels; // 2
  public short flags; // 2
  public Object emCache; // ptr 0
  public Object mCache; // ptr 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    subdivType = buffer.getShort();
    levels = buffer.getShort();
    renderLevels = buffer.getShort();
    flags = buffer.getShort();
    emCache = DNATools.ptr(buffer); // get ptr
    mCache = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeShort(subdivType);
    buffer.writeShort(levels);
    buffer.writeShort(renderLevels);
    buffer.writeShort(flags);
    buffer.writeInt(emCache!=null?emCache.hashCode():0);
    buffer.writeInt(mCache!=null?mCache.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SubsurfModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SubsurfModifierData:\n");
    sb.append(super.toString());
    sb.append("  subdivType: ").append(subdivType).append("\n");
    sb.append("  levels: ").append(levels).append("\n");
    sb.append("  renderLevels: ").append(renderLevels).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  emCache: ").append(emCache).append("\n");
    sb.append("  mCache: ").append(mCache).append("\n");
    return sb.toString();
  }
  public SubsurfModifierData copy() { try {return (SubsurfModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

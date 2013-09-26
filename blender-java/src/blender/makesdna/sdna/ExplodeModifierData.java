package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ExplodeModifierData extends ModifierData implements DNA, Cloneable { // #102
  public ExplodeModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public Object facepa; // ptr 4
  public short flag; // 2
  public short vgroup; // 2
  public float protect; // 4
  public byte[] uvname = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    facepa = DNATools.ptr(buffer); // get ptr
    flag = buffer.getShort();
    vgroup = buffer.getShort();
    protect = buffer.getFloat();
    buffer.get(uvname);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(facepa!=null?facepa.hashCode():0);
    buffer.writeShort(flag);
    buffer.writeShort(vgroup);
    buffer.writeFloat(protect);
    buffer.write(uvname);
  }
  public Object setmyarray(Object array) {
    myarray = (ExplodeModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ExplodeModifierData:\n");
    sb.append(super.toString());
    sb.append("  facepa: ").append(facepa).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  vgroup: ").append(vgroup).append("\n");
    sb.append("  protect: ").append(protect).append("\n");
    sb.append("  uvname: ").append(new String(uvname)).append("\n");
    return sb.toString();
  }
  public ExplodeModifierData copy() { try {return (ExplodeModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MaskModifierData extends ModifierData implements DNA, Cloneable { // #77
  public MaskModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject ob_arm; // ptr 1296
  public byte[] vgroup = new byte[32]; // 1
  public int mode; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    ob_arm = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(vgroup);
    mode = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(ob_arm!=null?ob_arm.hashCode():0);
    buffer.write(vgroup);
    buffer.writeInt(mode);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (MaskModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MaskModifierData:\n");
    sb.append(super.toString());
    sb.append("  ob_arm: ").append(ob_arm).append("\n");
    sb.append("  vgroup: ").append(new String(vgroup)).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public MaskModifierData copy() { try {return (MaskModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

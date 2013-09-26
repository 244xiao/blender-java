package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CurveModifierData extends ModifierData implements DNA, Cloneable { // #75
  public CurveModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public byte[] name = new byte[32]; // 1
  public short defaxis; // 2
  public byte[] pad = new byte[6]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(name);
    defaxis = buffer.getShort();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.write(name);
    buffer.writeShort(defaxis);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (CurveModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CurveModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  defaxis: ").append(defaxis).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public CurveModifierData copy() { try {return (CurveModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

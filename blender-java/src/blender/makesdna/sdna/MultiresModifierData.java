package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresModifierData extends ModifierData implements DNA, Cloneable { // #103
  public MultiresModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public byte lvl; // 1
  public byte sculptlvl; // 1
  public byte renderlvl; // 1
  public byte totlvl; // 1
  public byte simple; // 1
  public byte flags; // 1
  public byte[] pad = new byte[2]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    lvl = buffer.get();
    sculptlvl = buffer.get();
    renderlvl = buffer.get();
    totlvl = buffer.get();
    simple = buffer.get();
    flags = buffer.get();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeByte(lvl);
    buffer.writeByte(sculptlvl);
    buffer.writeByte(renderlvl);
    buffer.writeByte(totlvl);
    buffer.writeByte(simple);
    buffer.writeByte(flags);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresModifierData:\n");
    sb.append(super.toString());
    sb.append("  lvl: ").append(lvl).append("\n");
    sb.append("  sculptlvl: ").append(sculptlvl).append("\n");
    sb.append("  renderlvl: ").append(renderlvl).append("\n");
    sb.append("  totlvl: ").append(totlvl).append("\n");
    sb.append("  simple: ").append(simple).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public MultiresModifierData copy() { try {return (MultiresModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BevelModifierData extends ModifierData implements DNA, Cloneable { // #81
  public BevelModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float value; // 4
  public int res; // 4
  public int pad; // 4
  public short flags; // 2
  public short val_flags; // 2
  public short lim_flags; // 2
  public short e_flags; // 2
  public float bevel_angle; // 4
  public byte[] defgrp_name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    value = buffer.getFloat();
    res = buffer.getInt();
    pad = buffer.getInt();
    flags = buffer.getShort();
    val_flags = buffer.getShort();
    lim_flags = buffer.getShort();
    e_flags = buffer.getShort();
    bevel_angle = buffer.getFloat();
    buffer.get(defgrp_name);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(value);
    buffer.writeInt(res);
    buffer.writeInt(pad);
    buffer.writeShort(flags);
    buffer.writeShort(val_flags);
    buffer.writeShort(lim_flags);
    buffer.writeShort(e_flags);
    buffer.writeFloat(bevel_angle);
    buffer.write(defgrp_name);
  }
  public Object setmyarray(Object array) {
    myarray = (BevelModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BevelModifierData:\n");
    sb.append(super.toString());
    sb.append("  value: ").append(value).append("\n");
    sb.append("  res: ").append(res).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  val_flags: ").append(val_flags).append("\n");
    sb.append("  lim_flags: ").append(lim_flags).append("\n");
    sb.append("  e_flags: ").append(e_flags).append("\n");
    sb.append("  bevel_angle: ").append(bevel_angle).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    return sb.toString();
  }
  public BevelModifierData copy() { try {return (BevelModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

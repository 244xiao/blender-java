package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SolidifyModifierData extends ModifierData implements DNA, Cloneable { // #108
  public SolidifyModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public byte[] defgrp_name = new byte[32]; // 1
  public float offset; // 4
  public float offset_fac; // 4
  public float crease_inner; // 4
  public float crease_outer; // 4
  public float crease_rim; // 4
  public int flag; // 4
  public short mat_ofs; // 2
  public short mat_ofs_rim; // 2
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    buffer.get(defgrp_name);
    offset = buffer.getFloat();
    offset_fac = buffer.getFloat();
    crease_inner = buffer.getFloat();
    crease_outer = buffer.getFloat();
    crease_rim = buffer.getFloat();
    flag = buffer.getInt();
    mat_ofs = buffer.getShort();
    mat_ofs_rim = buffer.getShort();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.write(defgrp_name);
    buffer.writeFloat(offset);
    buffer.writeFloat(offset_fac);
    buffer.writeFloat(crease_inner);
    buffer.writeFloat(crease_outer);
    buffer.writeFloat(crease_rim);
    buffer.writeInt(flag);
    buffer.writeShort(mat_ofs);
    buffer.writeShort(mat_ofs_rim);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (SolidifyModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SolidifyModifierData:\n");
    sb.append(super.toString());
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  offset_fac: ").append(offset_fac).append("\n");
    sb.append("  crease_inner: ").append(crease_inner).append("\n");
    sb.append("  crease_outer: ").append(crease_outer).append("\n");
    sb.append("  crease_rim: ").append(crease_rim).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mat_ofs: ").append(mat_ofs).append("\n");
    sb.append("  mat_ofs_rim: ").append(mat_ofs_rim).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public SolidifyModifierData copy() { try {return (SolidifyModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SmoothModifierData extends ModifierData implements DNA, Cloneable { // #87
  public SmoothModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public float fac; // 4
  public byte[] defgrp_name = new byte[32]; // 1
  public short flag; // 2
  public short repeat; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    fac = buffer.getFloat();
    buffer.get(defgrp_name);
    flag = buffer.getShort();
    repeat = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeFloat(fac);
    buffer.write(defgrp_name);
    buffer.writeShort(flag);
    buffer.writeShort(repeat);
  }
  public Object setmyarray(Object array) {
    myarray = (SmoothModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SmoothModifierData:\n");
    sb.append(super.toString());
    sb.append("  fac: ").append(fac).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  repeat: ").append(repeat).append("\n");
    return sb.toString();
  }
  public SmoothModifierData copy() { try {return (SmoothModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

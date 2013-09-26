package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CastModifierData extends ModifierData implements DNA, Cloneable { // #88
  public CastModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public float fac; // 4
  public float radius; // 4
  public float size; // 4
  public byte[] defgrp_name = new byte[32]; // 1
  public short flag; // 2
  public short type; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    fac = buffer.getFloat();
    radius = buffer.getFloat();
    size = buffer.getFloat();
    buffer.get(defgrp_name);
    flag = buffer.getShort();
    type = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeFloat(fac);
    buffer.writeFloat(radius);
    buffer.writeFloat(size);
    buffer.write(defgrp_name);
    buffer.writeShort(flag);
    buffer.writeShort(type);
  }
  public Object setmyarray(Object array) {
    myarray = (CastModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CastModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  fac: ").append(fac).append("\n");
    sb.append("  radius: ").append(radius).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    return sb.toString();
  }
  public CastModifierData copy() { try {return (CastModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

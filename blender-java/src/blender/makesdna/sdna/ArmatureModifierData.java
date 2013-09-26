package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ArmatureModifierData extends ModifierData implements DNA, Cloneable { // #90
  public ArmatureModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public short deformflag; // 2
  public short multi; // 2
  public int pad2; // 4
  public bObject object; // ptr 1296
  public Object prevCos; // ptr 4
  public byte[] defgrp_name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    deformflag = buffer.getShort();
    multi = buffer.getShort();
    pad2 = buffer.getInt();
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    prevCos = DNATools.ptr(buffer); // get ptr
    buffer.get(defgrp_name);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeShort(deformflag);
    buffer.writeShort(multi);
    buffer.writeInt(pad2);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeInt(prevCos!=null?prevCos.hashCode():0);
    buffer.write(defgrp_name);
  }
  public Object setmyarray(Object array) {
    myarray = (ArmatureModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ArmatureModifierData:\n");
    sb.append(super.toString());
    sb.append("  deformflag: ").append(deformflag).append("\n");
    sb.append("  multi: ").append(multi).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  prevCos: ").append(prevCos).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    return sb.toString();
  }
  public ArmatureModifierData copy() { try {return (ArmatureModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

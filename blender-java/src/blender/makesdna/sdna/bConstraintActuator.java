package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bConstraintActuator implements DNA, Cloneable { // #247
  public bConstraintActuator[] myarray;
  public short type; // 2
  public short mode; // 2
  public short flag; // 2
  public short damp; // 2
  public short time; // 2
  public short rotdamp; // 2
  public int pad; // 4
  public float[] minloc = new float[3]; // 4
  public float[] maxloc = new float[3]; // 4
  public float[] minrot = new float[3]; // 4
  public float[] maxrot = new float[3]; // 4
  public byte[] matprop = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    type = buffer.getShort();
    mode = buffer.getShort();
    flag = buffer.getShort();
    damp = buffer.getShort();
    time = buffer.getShort();
    rotdamp = buffer.getShort();
    pad = buffer.getInt();
    for(int i=0;i<minloc.length;i++) minloc[i]=buffer.getFloat();
    for(int i=0;i<maxloc.length;i++) maxloc[i]=buffer.getFloat();
    for(int i=0;i<minrot.length;i++) minrot[i]=buffer.getFloat();
    for(int i=0;i<maxrot.length;i++) maxrot[i]=buffer.getFloat();
    buffer.get(matprop);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(type);
    buffer.writeShort(mode);
    buffer.writeShort(flag);
    buffer.writeShort(damp);
    buffer.writeShort(time);
    buffer.writeShort(rotdamp);
    buffer.writeInt(pad);
    for(int i=0;i<minloc.length;i++) buffer.writeFloat(minloc[i]);
    for(int i=0;i<maxloc.length;i++) buffer.writeFloat(maxloc[i]);
    for(int i=0;i<minrot.length;i++) buffer.writeFloat(minrot[i]);
    for(int i=0;i<maxrot.length;i++) buffer.writeFloat(maxrot[i]);
    buffer.write(matprop);
  }
  public Object setmyarray(Object array) {
    myarray = (bConstraintActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bConstraintActuator:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  damp: ").append(damp).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  rotdamp: ").append(rotdamp).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  minloc: ").append(Arrays.toString(minloc)).append("\n");
    sb.append("  maxloc: ").append(Arrays.toString(maxloc)).append("\n");
    sb.append("  minrot: ").append(Arrays.toString(minrot)).append("\n");
    sb.append("  maxrot: ").append(Arrays.toString(maxrot)).append("\n");
    sb.append("  matprop: ").append(new String(matprop)).append("\n");
    return sb.toString();
  }
  public bConstraintActuator copy() { try {return (bConstraintActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

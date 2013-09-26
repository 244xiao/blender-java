package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bItasc implements DNA, Cloneable { // #269
  public bItasc[] myarray;
  public int iksolver; // 4
  public float precision; // 4
  public short numiter; // 2
  public short numstep; // 2
  public float minstep; // 4
  public float maxstep; // 4
  public short solver; // 2
  public short flag; // 2
  public float feedback; // 4
  public float maxvel; // 4
  public float dampmax; // 4
  public float dampeps; // 4

  public void read(ByteBuffer buffer) {
    iksolver = buffer.getInt();
    precision = buffer.getFloat();
    numiter = buffer.getShort();
    numstep = buffer.getShort();
    minstep = buffer.getFloat();
    maxstep = buffer.getFloat();
    solver = buffer.getShort();
    flag = buffer.getShort();
    feedback = buffer.getFloat();
    maxvel = buffer.getFloat();
    dampmax = buffer.getFloat();
    dampeps = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(iksolver);
    buffer.writeFloat(precision);
    buffer.writeShort(numiter);
    buffer.writeShort(numstep);
    buffer.writeFloat(minstep);
    buffer.writeFloat(maxstep);
    buffer.writeShort(solver);
    buffer.writeShort(flag);
    buffer.writeFloat(feedback);
    buffer.writeFloat(maxvel);
    buffer.writeFloat(dampmax);
    buffer.writeFloat(dampeps);
  }
  public Object setmyarray(Object array) {
    myarray = (bItasc[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bItasc:\n");
    sb.append("  iksolver: ").append(iksolver).append("\n");
    sb.append("  precision: ").append(precision).append("\n");
    sb.append("  numiter: ").append(numiter).append("\n");
    sb.append("  numstep: ").append(numstep).append("\n");
    sb.append("  minstep: ").append(minstep).append("\n");
    sb.append("  maxstep: ").append(maxstep).append("\n");
    sb.append("  solver: ").append(solver).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  feedback: ").append(feedback).append("\n");
    sb.append("  maxvel: ").append(maxvel).append("\n");
    sb.append("  dampmax: ").append(dampmax).append("\n");
    sb.append("  dampeps: ").append(dampeps).append("\n");
    return sb.toString();
  }
  public bItasc copy() { try {return (bItasc)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

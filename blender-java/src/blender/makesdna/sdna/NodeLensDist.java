package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeLensDist implements DNA, Cloneable { // #326
  public NodeLensDist[] myarray;
  public short jit; // 2
  public short proj; // 2
  public short fit; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    jit = buffer.getShort();
    proj = buffer.getShort();
    fit = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(jit);
    buffer.writeShort(proj);
    buffer.writeShort(fit);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeLensDist[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeLensDist:\n");
    sb.append("  jit: ").append(jit).append("\n");
    sb.append("  proj: ").append(proj).append("\n");
    sb.append("  fit: ").append(fit).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public NodeLensDist copy() { try {return (NodeLensDist)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

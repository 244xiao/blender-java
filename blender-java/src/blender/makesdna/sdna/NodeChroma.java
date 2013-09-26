package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeChroma implements DNA, Cloneable { // #317
  public NodeChroma[] myarray;
  public float t1; // 4
  public float t2; // 4
  public float t3; // 4
  public float fsize; // 4
  public float fstrength; // 4
  public float falpha; // 4
  public float[] key = new float[4]; // 4
  public short algorithm; // 2
  public short channel; // 2

  public void read(ByteBuffer buffer) {
    t1 = buffer.getFloat();
    t2 = buffer.getFloat();
    t3 = buffer.getFloat();
    fsize = buffer.getFloat();
    fstrength = buffer.getFloat();
    falpha = buffer.getFloat();
    for(int i=0;i<key.length;i++) key[i]=buffer.getFloat();
    algorithm = buffer.getShort();
    channel = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(t1);
    buffer.writeFloat(t2);
    buffer.writeFloat(t3);
    buffer.writeFloat(fsize);
    buffer.writeFloat(fstrength);
    buffer.writeFloat(falpha);
    for(int i=0;i<key.length;i++) buffer.writeFloat(key[i]);
    buffer.writeShort(algorithm);
    buffer.writeShort(channel);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeChroma[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeChroma:\n");
    sb.append("  t1: ").append(t1).append("\n");
    sb.append("  t2: ").append(t2).append("\n");
    sb.append("  t3: ").append(t3).append("\n");
    sb.append("  fsize: ").append(fsize).append("\n");
    sb.append("  fstrength: ").append(fstrength).append("\n");
    sb.append("  falpha: ").append(falpha).append("\n");
    sb.append("  key: ").append(Arrays.toString(key)).append("\n");
    sb.append("  algorithm: ").append(algorithm).append("\n");
    sb.append("  channel: ").append(channel).append("\n");
    return sb.toString();
  }
  public NodeChroma copy() { try {return (NodeChroma)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

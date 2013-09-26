package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeGlare implements DNA, Cloneable { // #324
  public NodeGlare[] myarray;
  public byte quality; // 1
  public byte type; // 1
  public byte iter; // 1
  public byte angle; // 1
  public byte angle_ofs; // 1
  public byte size; // 1
  public byte[] pad = new byte[2]; // 1
  public float colmod; // 4
  public float mix; // 4
  public float threshold; // 4
  public float fade; // 4

  public void read(ByteBuffer buffer) {
    quality = buffer.get();
    type = buffer.get();
    iter = buffer.get();
    angle = buffer.get();
    angle_ofs = buffer.get();
    size = buffer.get();
    buffer.get(pad);
    colmod = buffer.getFloat();
    mix = buffer.getFloat();
    threshold = buffer.getFloat();
    fade = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeByte(quality);
    buffer.writeByte(type);
    buffer.writeByte(iter);
    buffer.writeByte(angle);
    buffer.writeByte(angle_ofs);
    buffer.writeByte(size);
    buffer.write(pad);
    buffer.writeFloat(colmod);
    buffer.writeFloat(mix);
    buffer.writeFloat(threshold);
    buffer.writeFloat(fade);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeGlare[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeGlare:\n");
    sb.append("  quality: ").append(quality).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  iter: ").append(iter).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  angle_ofs: ").append(angle_ofs).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  colmod: ").append(colmod).append("\n");
    sb.append("  mix: ").append(mix).append("\n");
    sb.append("  threshold: ").append(threshold).append("\n");
    sb.append("  fade: ").append(fade).append("\n");
    return sb.toString();
  }
  public NodeGlare copy() { try {return (NodeGlare)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

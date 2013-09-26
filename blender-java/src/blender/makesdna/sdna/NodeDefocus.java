package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeDefocus implements DNA, Cloneable { // #322
  public NodeDefocus[] myarray;
  public byte bktype; // 1
  public byte rotation; // 1
  public byte preview; // 1
  public byte gamco; // 1
  public short samples; // 2
  public short no_zbuf; // 2
  public float fstop; // 4
  public float maxblur; // 4
  public float bthresh; // 4
  public float scale; // 4

  public void read(ByteBuffer buffer) {
    bktype = buffer.get();
    rotation = buffer.get();
    preview = buffer.get();
    gamco = buffer.get();
    samples = buffer.getShort();
    no_zbuf = buffer.getShort();
    fstop = buffer.getFloat();
    maxblur = buffer.getFloat();
    bthresh = buffer.getFloat();
    scale = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeByte(bktype);
    buffer.writeByte(rotation);
    buffer.writeByte(preview);
    buffer.writeByte(gamco);
    buffer.writeShort(samples);
    buffer.writeShort(no_zbuf);
    buffer.writeFloat(fstop);
    buffer.writeFloat(maxblur);
    buffer.writeFloat(bthresh);
    buffer.writeFloat(scale);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeDefocus[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeDefocus:\n");
    sb.append("  bktype: ").append(bktype).append("\n");
    sb.append("  rotation: ").append(rotation).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  gamco: ").append(gamco).append("\n");
    sb.append("  samples: ").append(samples).append("\n");
    sb.append("  no_zbuf: ").append(no_zbuf).append("\n");
    sb.append("  fstop: ").append(fstop).append("\n");
    sb.append("  maxblur: ").append(maxblur).append("\n");
    sb.append("  bthresh: ").append(bthresh).append("\n");
    sb.append("  scale: ").append(scale).append("\n");
    return sb.toString();
  }
  public NodeDefocus copy() { try {return (NodeDefocus)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

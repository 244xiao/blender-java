package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ImageUser implements DNA, Cloneable { // #22
  public ImageUser[] myarray;
  public Scene scene; // ptr 1552
  public int framenr; // 4
  public int frames; // 4
  public int offset; // 4
  public int sfra; // 4
  public byte fie_ima; // 1
  public byte cycl; // 1
  public byte ok; // 1
  public byte pad; // 1
  public short multi_index; // 2
  public short layer; // 2
  public short pass; // 2
  public short flag; // 2
  public int pad2; // 4

  public void read(ByteBuffer buffer) {
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    framenr = buffer.getInt();
    frames = buffer.getInt();
    offset = buffer.getInt();
    sfra = buffer.getInt();
    fie_ima = buffer.get();
    cycl = buffer.get();
    ok = buffer.get();
    pad = buffer.get();
    multi_index = buffer.getShort();
    layer = buffer.getShort();
    pass = buffer.getShort();
    flag = buffer.getShort();
    pad2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(framenr);
    buffer.writeInt(frames);
    buffer.writeInt(offset);
    buffer.writeInt(sfra);
    buffer.writeByte(fie_ima);
    buffer.writeByte(cycl);
    buffer.writeByte(ok);
    buffer.writeByte(pad);
    buffer.writeShort(multi_index);
    buffer.writeShort(layer);
    buffer.writeShort(pass);
    buffer.writeShort(flag);
    buffer.writeInt(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (ImageUser[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ImageUser:\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  framenr: ").append(framenr).append("\n");
    sb.append("  frames: ").append(frames).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  fie_ima: ").append(fie_ima).append("\n");
    sb.append("  cycl: ").append(cycl).append("\n");
    sb.append("  ok: ").append(ok).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  multi_index: ").append(multi_index).append("\n");
    sb.append("  layer: ").append(layer).append("\n");
    sb.append("  pass: ").append(pass).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public ImageUser copy() { try {return (ImageUser)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

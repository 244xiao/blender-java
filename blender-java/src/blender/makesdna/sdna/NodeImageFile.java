package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeImageFile implements DNA, Cloneable { // #316
  public NodeImageFile[] myarray;
  public byte[] name = new byte[256]; // 1
  public short imtype; // 2
  public short subimtype; // 2
  public short quality; // 2
  public short codec; // 2
  public int sfra; // 4
  public int efra; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    imtype = buffer.getShort();
    subimtype = buffer.getShort();
    quality = buffer.getShort();
    codec = buffer.getShort();
    sfra = buffer.getInt();
    efra = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeShort(imtype);
    buffer.writeShort(subimtype);
    buffer.writeShort(quality);
    buffer.writeShort(codec);
    buffer.writeInt(sfra);
    buffer.writeInt(efra);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeImageFile[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeImageFile:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  imtype: ").append(imtype).append("\n");
    sb.append("  subimtype: ").append(subimtype).append("\n");
    sb.append("  quality: ").append(quality).append("\n");
    sb.append("  codec: ").append(codec).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  efra: ").append(efra).append("\n");
    return sb.toString();
  }
  public NodeImageFile copy() { try {return (NodeImageFile)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

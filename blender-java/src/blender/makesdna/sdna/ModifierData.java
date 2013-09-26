package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ModifierData extends Link implements DNA, Cloneable { // #72
  public int type; // 4
  public int mode; // 4
  public int stackindex; // 4
  public int pad; // 4
  public byte[] name = new byte[32]; // 1
  public Scene scene; // ptr 1552
  public Object error; // ptr 1

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    type = buffer.getInt();
    mode = buffer.getInt();
    stackindex = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(name);
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    error = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(type);
    buffer.writeInt(mode);
    buffer.writeInt(stackindex);
    buffer.writeInt(pad);
    buffer.write(name);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(error!=null?error.hashCode():0);
  }
  public Object setmyarray(Object array) {
    return this;
  }
  public Object getmyarray() {
    return null;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ModifierData:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  stackindex: ").append(stackindex).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  error: ").append(error).append("\n");
    return sb.toString();
  }
  public ModifierData copy() { try {return (ModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

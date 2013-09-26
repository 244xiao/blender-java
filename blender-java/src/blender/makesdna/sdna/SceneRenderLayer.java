package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SceneRenderLayer extends Link<SceneRenderLayer> implements DNA, Cloneable { // #133
  public SceneRenderLayer[] myarray;
  public byte[] name = new byte[32]; // 1
  public Material mat_override; // ptr 800
  public Group light_override; // ptr 104
  public int lay; // 4
  public int lay_zmask; // 4
  public int layflag; // 4
  public int pad; // 4
  public int passflag; // 4
  public int pass_xor; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), SceneRenderLayer.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), SceneRenderLayer.class); // get ptr
    buffer.get(name);
    mat_override = DNATools.link(DNATools.ptr(buffer), Material.class); // get ptr
    light_override = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    lay = buffer.getInt();
    lay_zmask = buffer.getInt();
    layflag = buffer.getInt();
    pad = buffer.getInt();
    passflag = buffer.getInt();
    pass_xor = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    buffer.writeInt(mat_override!=null?mat_override.hashCode():0);
    buffer.writeInt(light_override!=null?light_override.hashCode():0);
    buffer.writeInt(lay);
    buffer.writeInt(lay_zmask);
    buffer.writeInt(layflag);
    buffer.writeInt(pad);
    buffer.writeInt(passflag);
    buffer.writeInt(pass_xor);
  }
  public Object setmyarray(Object array) {
    myarray = (SceneRenderLayer[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SceneRenderLayer:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  mat_override: ").append(mat_override).append("\n");
    sb.append("  light_override: ").append(light_override).append("\n");
    sb.append("  lay: ").append(lay).append("\n");
    sb.append("  lay_zmask: ").append(lay_zmask).append("\n");
    sb.append("  layflag: ").append(layflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  passflag: ").append(passflag).append("\n");
    sb.append("  pass_xor: ").append(pass_xor).append("\n");
    return sb.toString();
  }
  public SceneRenderLayer copy() { try {return (SceneRenderLayer)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

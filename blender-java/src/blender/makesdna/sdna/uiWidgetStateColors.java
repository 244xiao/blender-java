package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class uiWidgetStateColors implements DNA, Cloneable { // #182
  public uiWidgetStateColors[] myarray;
  public byte[] inner_anim = new byte[4]; // 1
  public byte[] inner_anim_sel = new byte[4]; // 1
  public byte[] inner_key = new byte[4]; // 1
  public byte[] inner_key_sel = new byte[4]; // 1
  public byte[] inner_driven = new byte[4]; // 1
  public byte[] inner_driven_sel = new byte[4]; // 1
  public float blend; // 4
  public float pad; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(inner_anim);
    buffer.get(inner_anim_sel);
    buffer.get(inner_key);
    buffer.get(inner_key_sel);
    buffer.get(inner_driven);
    buffer.get(inner_driven_sel);
    blend = buffer.getFloat();
    pad = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(inner_anim);
    buffer.write(inner_anim_sel);
    buffer.write(inner_key);
    buffer.write(inner_key_sel);
    buffer.write(inner_driven);
    buffer.write(inner_driven_sel);
    buffer.writeFloat(blend);
    buffer.writeFloat(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (uiWidgetStateColors[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("uiWidgetStateColors:\n");
    sb.append("  inner_anim: ").append(new String(inner_anim)).append("\n");
    sb.append("  inner_anim_sel: ").append(new String(inner_anim_sel)).append("\n");
    sb.append("  inner_key: ").append(new String(inner_key)).append("\n");
    sb.append("  inner_key_sel: ").append(new String(inner_key_sel)).append("\n");
    sb.append("  inner_driven: ").append(new String(inner_driven)).append("\n");
    sb.append("  inner_driven_sel: ").append(new String(inner_driven_sel)).append("\n");
    sb.append("  blend: ").append(blend).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public uiWidgetStateColors copy() { try {return (uiWidgetStateColors)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

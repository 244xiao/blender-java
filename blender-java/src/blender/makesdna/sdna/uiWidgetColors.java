package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class uiWidgetColors implements DNA, Cloneable { // #181
  public uiWidgetColors[] myarray;
  public byte[] outline = new byte[4]; // 1
  public byte[] inner = new byte[4]; // 1
  public byte[] inner_sel = new byte[4]; // 1
  public byte[] item = new byte[4]; // 1
  public byte[] text = new byte[4]; // 1
  public byte[] text_sel = new byte[4]; // 1
  public short shaded; // 2
  public short shadetop; // 2
  public short shadedown; // 2
  public short alpha_check; // 2

  public void read(ByteBuffer buffer) {
    buffer.get(outline);
    buffer.get(inner);
    buffer.get(inner_sel);
    buffer.get(item);
    buffer.get(text);
    buffer.get(text_sel);
    shaded = buffer.getShort();
    shadetop = buffer.getShort();
    shadedown = buffer.getShort();
    alpha_check = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(outline);
    buffer.write(inner);
    buffer.write(inner_sel);
    buffer.write(item);
    buffer.write(text);
    buffer.write(text_sel);
    buffer.writeShort(shaded);
    buffer.writeShort(shadetop);
    buffer.writeShort(shadedown);
    buffer.writeShort(alpha_check);
  }
  public Object setmyarray(Object array) {
    myarray = (uiWidgetColors[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("uiWidgetColors:\n");
    sb.append("  outline: ").append(new String(outline)).append("\n");
    sb.append("  inner: ").append(new String(inner)).append("\n");
    sb.append("  inner_sel: ").append(new String(inner_sel)).append("\n");
    sb.append("  item: ").append(new String(item)).append("\n");
    sb.append("  text: ").append(new String(text)).append("\n");
    sb.append("  text_sel: ").append(new String(text_sel)).append("\n");
    sb.append("  shaded: ").append(shaded).append("\n");
    sb.append("  shadetop: ").append(shadetop).append("\n");
    sb.append("  shadedown: ").append(shadedown).append("\n");
    sb.append("  alpha_check: ").append(alpha_check).append("\n");
    return sb.toString();
  }
  public uiWidgetColors copy() { try {return (uiWidgetColors)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

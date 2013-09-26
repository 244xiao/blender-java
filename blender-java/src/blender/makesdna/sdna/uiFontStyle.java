package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class uiFontStyle implements DNA, Cloneable { // #179
  public uiFontStyle[] myarray;
  public short uifont_id; // 2
  public short points; // 2
  public short kerning; // 2
  public byte[] pad = new byte[6]; // 1
  public short italic; // 2
  public short bold; // 2
  public short shadow; // 2
  public short shadx; // 2
  public short shady; // 2
  public short align; // 2
  public float shadowalpha; // 4
  public float shadowcolor; // 4

  public void read(ByteBuffer buffer) {
    uifont_id = buffer.getShort();
    points = buffer.getShort();
    kerning = buffer.getShort();
    buffer.get(pad);
    italic = buffer.getShort();
    bold = buffer.getShort();
    shadow = buffer.getShort();
    shadx = buffer.getShort();
    shady = buffer.getShort();
    align = buffer.getShort();
    shadowalpha = buffer.getFloat();
    shadowcolor = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(uifont_id);
    buffer.writeShort(points);
    buffer.writeShort(kerning);
    buffer.write(pad);
    buffer.writeShort(italic);
    buffer.writeShort(bold);
    buffer.writeShort(shadow);
    buffer.writeShort(shadx);
    buffer.writeShort(shady);
    buffer.writeShort(align);
    buffer.writeFloat(shadowalpha);
    buffer.writeFloat(shadowcolor);
  }
  public Object setmyarray(Object array) {
    myarray = (uiFontStyle[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("uiFontStyle:\n");
    sb.append("  uifont_id: ").append(uifont_id).append("\n");
    sb.append("  points: ").append(points).append("\n");
    sb.append("  kerning: ").append(kerning).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  italic: ").append(italic).append("\n");
    sb.append("  bold: ").append(bold).append("\n");
    sb.append("  shadow: ").append(shadow).append("\n");
    sb.append("  shadx: ").append(shadx).append("\n");
    sb.append("  shady: ").append(shady).append("\n");
    sb.append("  align: ").append(align).append("\n");
    sb.append("  shadowalpha: ").append(shadowalpha).append("\n");
    sb.append("  shadowcolor: ").append(shadowcolor).append("\n");
    return sb.toString();
  }
  public uiFontStyle copy() { try {return (uiFontStyle)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

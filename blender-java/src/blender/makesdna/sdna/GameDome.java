package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class GameDome implements DNA, Cloneable { // #136
  public GameDome[] myarray;
  public short res; // 2
  public short mode; // 2
  public short angle; // 2
  public short tilt; // 2
  public float resbuf; // 4
  public float pad2; // 4
  public Text warptext; // ptr 176

  public void read(ByteBuffer buffer) {
    res = buffer.getShort();
    mode = buffer.getShort();
    angle = buffer.getShort();
    tilt = buffer.getShort();
    resbuf = buffer.getFloat();
    pad2 = buffer.getFloat();
    warptext = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(res);
    buffer.writeShort(mode);
    buffer.writeShort(angle);
    buffer.writeShort(tilt);
    buffer.writeFloat(resbuf);
    buffer.writeFloat(pad2);
    buffer.writeInt(warptext!=null?warptext.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (GameDome[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("GameDome:\n");
    sb.append("  res: ").append(res).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  tilt: ").append(tilt).append("\n");
    sb.append("  resbuf: ").append(resbuf).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  warptext: ").append(warptext).append("\n");
    return sb.toString();
  }
  public GameDome copy() { try {return (GameDome)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

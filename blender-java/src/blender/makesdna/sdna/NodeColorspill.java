package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeColorspill implements DNA, Cloneable { // #328
  public NodeColorspill[] myarray;
  public short limchan; // 2
  public short unspill; // 2
  public float limscale; // 4
  public float uspillr; // 4
  public float uspillg; // 4
  public float uspillb; // 4

  public void read(ByteBuffer buffer) {
    limchan = buffer.getShort();
    unspill = buffer.getShort();
    limscale = buffer.getFloat();
    uspillr = buffer.getFloat();
    uspillg = buffer.getFloat();
    uspillb = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(limchan);
    buffer.writeShort(unspill);
    buffer.writeFloat(limscale);
    buffer.writeFloat(uspillr);
    buffer.writeFloat(uspillg);
    buffer.writeFloat(uspillb);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeColorspill[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeColorspill:\n");
    sb.append("  limchan: ").append(limchan).append("\n");
    sb.append("  unspill: ").append(unspill).append("\n");
    sb.append("  limscale: ").append(limscale).append("\n");
    sb.append("  uspillr: ").append(uspillr).append("\n");
    sb.append("  uspillg: ").append(uspillg).append("\n");
    sb.append("  uspillb: ").append(uspillb).append("\n");
    return sb.toString();
  }
  public NodeColorspill copy() { try {return (NodeColorspill)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

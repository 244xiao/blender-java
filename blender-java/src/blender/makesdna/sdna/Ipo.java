package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Ipo extends ID implements DNA, Cloneable { // #14
  public Ipo[] myarray;
  public ID id = (ID)this;
  public ListBase curve = new ListBase(); // 16
  public rctf cur = new rctf(); // 16
  public short blocktype; // 2
  public short showkey; // 2
  public short muteipo; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    curve.read(buffer);
    cur.read(buffer);
    blocktype = buffer.getShort();
    showkey = buffer.getShort();
    muteipo = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    curve.write(buffer);
    cur.write(buffer);
    buffer.writeShort(blocktype);
    buffer.writeShort(showkey);
    buffer.writeShort(muteipo);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (Ipo[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Ipo:\n");
    sb.append(super.toString());
    sb.append("  curve: ").append(curve).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    sb.append("  blocktype: ").append(blocktype).append("\n");
    sb.append("  showkey: ").append(showkey).append("\n");
    sb.append("  muteipo: ").append(muteipo).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public Ipo copy() { try {return (Ipo)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class uiFont extends Link<uiFont> implements DNA, Cloneable { // #178
  public uiFont[] myarray;
  public byte[] filename = new byte[256]; // 1
  public short blf_id; // 2
  public short uifont_id; // 2
  public short r_to_l; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), uiFont.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), uiFont.class); // get ptr
    buffer.get(filename);
    blf_id = buffer.getShort();
    uifont_id = buffer.getShort();
    r_to_l = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(filename);
    buffer.writeShort(blf_id);
    buffer.writeShort(uifont_id);
    buffer.writeShort(r_to_l);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (uiFont[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("uiFont:\n");
    sb.append("  filename: ").append(new String(filename)).append("\n");
    sb.append("  blf_id: ").append(blf_id).append("\n");
    sb.append("  uifont_id: ").append(uifont_id).append("\n");
    sb.append("  r_to_l: ").append(r_to_l).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public uiFont copy() { try {return (uiFont)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

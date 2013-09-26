package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmKeyConfig extends Link<wmKeyConfig> implements DNA, Cloneable { // #363
  public wmKeyConfig[] myarray;
  public byte[] idname = new byte[64]; // 1
  public byte[] basename = new byte[64]; // 1
  public ListBase keymaps = new ListBase(); // 16
  public int actkeymap; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), wmKeyConfig.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), wmKeyConfig.class); // get ptr
    buffer.get(idname);
    buffer.get(basename);
    keymaps.read(buffer);
    actkeymap = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(idname);
    buffer.write(basename);
    keymaps.write(buffer);
    buffer.writeInt(actkeymap);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (wmKeyConfig[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmKeyConfig:\n");
    sb.append("  idname: ").append(new String(idname)).append("\n");
    sb.append("  basename: ").append(new String(basename)).append("\n");
    sb.append("  keymaps: ").append(keymaps).append("\n");
    sb.append("  actkeymap: ").append(actkeymap).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public wmKeyConfig copy() { try {return (wmKeyConfig)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

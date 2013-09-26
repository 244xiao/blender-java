package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CustomDataLayer implements DNA, Cloneable { // #337
  public CustomDataLayer[] myarray;
  public int type; // 4
  public int offset; // 4
  public int flag; // 4
  public int active; // 4
  public int active_rnd; // 4
  public int active_clone; // 4
  public int active_mask; // 4
  public byte[] pad = new byte[4]; // 1
  public byte[] name = new byte[32]; // 1
  public Object data; // ptr 0

  public void read(ByteBuffer buffer) {
    type = buffer.getInt();
    offset = buffer.getInt();
    flag = buffer.getInt();
    active = buffer.getInt();
    active_rnd = buffer.getInt();
    active_clone = buffer.getInt();
    active_mask = buffer.getInt();
    buffer.get(pad);
    buffer.get(name);
    data = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(type);
    buffer.writeInt(offset);
    buffer.writeInt(flag);
    buffer.writeInt(active);
    buffer.writeInt(active_rnd);
    buffer.writeInt(active_clone);
    buffer.writeInt(active_mask);
    buffer.write(pad);
    buffer.write(name);
    buffer.writeInt(data!=null?data.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (CustomDataLayer[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CustomDataLayer:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  active: ").append(active).append("\n");
    sb.append("  active_rnd: ").append(active_rnd).append("\n");
    sb.append("  active_clone: ").append(active_clone).append("\n");
    sb.append("  active_mask: ").append(active_mask).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  data: ").append(data).append("\n");
    return sb.toString();
  }
  public CustomDataLayer copy() { try {return (CustomDataLayer)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

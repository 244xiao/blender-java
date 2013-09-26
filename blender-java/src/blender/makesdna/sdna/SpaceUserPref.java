package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceUserPref extends SpaceLink implements DNA, Cloneable { // #176
  public SpaceUserPref[] myarray;
  public int pad; // 4
  public byte[] filter = new byte[64]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(filter);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeInt(pad);
    buffer.write(filter);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceUserPref[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceUserPref:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  filter: ").append(new String(filter)).append("\n");
    return sb.toString();
  }
  public SpaceUserPref copy() { try {return (SpaceUserPref)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

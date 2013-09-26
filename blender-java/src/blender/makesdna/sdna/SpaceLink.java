package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceLink extends Link implements DNA, Cloneable { // #156
  public ListBase regionbase = new ListBase(); // 16
  public int spacetype; // 4
  public float blockscale; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
  }
  public Object setmyarray(Object array) {
    return this;
  }
  public Object getmyarray() {
    return null;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceLink:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    return sb.toString();
  }
  public SpaceLink copy() { try {return (SpaceLink)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

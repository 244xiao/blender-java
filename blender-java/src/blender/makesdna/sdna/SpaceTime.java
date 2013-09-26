package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceTime extends SpaceLink implements DNA, Cloneable { // #170
  public SpaceTime[] myarray;
  public float blockscale; // 4
  public View2D v2d = new View2D(); // 144
  public ListBase caches = new ListBase(); // 16
  public int cache_display; // 4
  public int pad; // 4
  public int flag; // 4
  public int redraws; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    v2d.read(buffer);
    caches.read(buffer);
    cache_display = buffer.getInt();
    pad = buffer.getInt();
    flag = buffer.getInt();
    redraws = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    v2d.write(buffer);
    caches.write(buffer);
    buffer.writeInt(cache_display);
    buffer.writeInt(pad);
    buffer.writeInt(flag);
    buffer.writeInt(redraws);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceTime[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceTime:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  caches: ").append(caches).append("\n");
    sb.append("  cache_display: ").append(cache_display).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  redraws: ").append(redraws).append("\n");
    return sb.toString();
  }
  public SpaceTime copy() { try {return (SpaceTime)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

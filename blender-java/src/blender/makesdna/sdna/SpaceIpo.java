package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceIpo extends SpaceLink implements DNA, Cloneable { // #158
  public SpaceIpo[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public bDopeSheet ads; // ptr 104
  public ListBase ghostCurves = new ListBase(); // 16
  public short mode; // 2
  public short autosnap; // 2
  public int flag; // 4
  public float cursorVal; // 4
  public int around; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    ads = DNATools.link(DNATools.ptr(buffer), bDopeSheet.class); // get ptr
    ghostCurves.read(buffer);
    mode = buffer.getShort();
    autosnap = buffer.getShort();
    flag = buffer.getInt();
    cursorVal = buffer.getFloat();
    around = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    v2d.write(buffer);
    buffer.writeInt(ads!=null?ads.hashCode():0);
    ghostCurves.write(buffer);
    buffer.writeShort(mode);
    buffer.writeShort(autosnap);
    buffer.writeInt(flag);
    buffer.writeFloat(cursorVal);
    buffer.writeInt(around);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceIpo[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceIpo:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  ads: ").append(ads).append("\n");
    sb.append("  ghostCurves: ").append(ghostCurves).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  autosnap: ").append(autosnap).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  cursorVal: ").append(cursorVal).append("\n");
    sb.append("  around: ").append(around).append("\n");
    return sb.toString();
  }
  public SpaceIpo copy() { try {return (SpaceIpo)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

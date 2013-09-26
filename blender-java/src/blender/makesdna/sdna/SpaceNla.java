package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceNla extends SpaceLink implements DNA, Cloneable { // #165
  public SpaceNla[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public short autosnap; // 2
  public short flag; // 2
  public int pad; // 4
  public bDopeSheet ads; // ptr 104
  public View2D v2d = new View2D(); // 144

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    autosnap = buffer.getShort();
    flag = buffer.getShort();
    pad = buffer.getInt();
    ads = DNATools.link(DNATools.ptr(buffer), bDopeSheet.class); // get ptr
    v2d.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    buffer.writeShort(autosnap);
    buffer.writeShort(flag);
    buffer.writeInt(pad);
    buffer.writeInt(ads!=null?ads.hashCode():0);
    v2d.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceNla[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceNla:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  autosnap: ").append(autosnap).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  ads: ").append(ads).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    return sb.toString();
  }
  public SpaceNla copy() { try {return (SpaceNla)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

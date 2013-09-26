package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceLogic extends SpaceLink implements DNA, Cloneable { // #172
  public SpaceLogic[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public short flag; // 2
  public short scaflag; // 2
  public int pad; // 4
  public bGPdata gpd; // ptr 104

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    flag = buffer.getShort();
    scaflag = buffer.getShort();
    pad = buffer.getInt();
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    buffer.writeShort(flag);
    buffer.writeShort(scaflag);
    buffer.writeInt(pad);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceLogic[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceLogic:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  scaflag: ").append(scaflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    return sb.toString();
  }
  public SpaceLogic copy() { try {return (SpaceLogic)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

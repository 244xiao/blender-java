package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceAction extends SpaceLink implements DNA, Cloneable { // #273
  public SpaceAction[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public bAction action; // ptr 152
  public bDopeSheet ads = new bDopeSheet(); // 104
  public byte mode; // 1
  public byte autosnap; // 1
  public short flag; // 2
  public float timeslide; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    action = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    ads.read(buffer);
    mode = buffer.get();
    autosnap = buffer.get();
    flag = buffer.getShort();
    timeslide = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    v2d.write(buffer);
    buffer.writeInt(action!=null?action.hashCode():0);
    ads.write(buffer);
    buffer.writeByte(mode);
    buffer.writeByte(autosnap);
    buffer.writeShort(flag);
    buffer.writeFloat(timeslide);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceAction[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceAction:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  action: ").append(action).append("\n");
    sb.append("  ads: ").append(ads).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  autosnap: ").append(autosnap).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  timeslide: ").append(timeslide).append("\n");
    return sb.toString();
  }
  public SpaceAction copy() { try {return (SpaceAction)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

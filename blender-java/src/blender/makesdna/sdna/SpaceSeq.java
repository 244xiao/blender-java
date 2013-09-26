package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceSeq extends SpaceLink implements DNA, Cloneable { // #160
  public SpaceSeq[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public float xof; // 4
  public float yof; // 4
  public short mainb; // 2
  public short render_size; // 2
  public short chanshown; // 2
  public short zebra; // 2
  public int flag; // 4
  public float zoom; // 4
  public int view; // 4
  public int pad; // 4
  public bGPdata gpd; // ptr 104

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    xof = buffer.getFloat();
    yof = buffer.getFloat();
    mainb = buffer.getShort();
    render_size = buffer.getShort();
    chanshown = buffer.getShort();
    zebra = buffer.getShort();
    flag = buffer.getInt();
    zoom = buffer.getFloat();
    view = buffer.getInt();
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
    v2d.write(buffer);
    buffer.writeFloat(xof);
    buffer.writeFloat(yof);
    buffer.writeShort(mainb);
    buffer.writeShort(render_size);
    buffer.writeShort(chanshown);
    buffer.writeShort(zebra);
    buffer.writeInt(flag);
    buffer.writeFloat(zoom);
    buffer.writeInt(view);
    buffer.writeInt(pad);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceSeq[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceSeq:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  mainb: ").append(mainb).append("\n");
    sb.append("  render_size: ").append(render_size).append("\n");
    sb.append("  chanshown: ").append(chanshown).append("\n");
    sb.append("  zebra: ").append(zebra).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  zoom: ").append(zoom).append("\n");
    sb.append("  view: ").append(view).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    return sb.toString();
  }
  public SpaceSeq copy() { try {return (SpaceSeq)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

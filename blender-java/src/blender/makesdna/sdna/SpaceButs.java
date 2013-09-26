package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceButs extends SpaceLink implements DNA, Cloneable { // #159
  public SpaceButs[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public Object ri; // ptr (RenderInfo) 0
  public View2D v2d = new View2D(); // 144
  public short mainb; // 2
  public short mainbo; // 2
  public short mainbuser; // 2
  public short re_align; // 2
  public short align; // 2
  public short preview; // 2
  public short texture_context; // 2
  public byte flag; // 1
  public byte pad; // 1
  public Object path; // ptr 0
  public int pathflag; // 4
  public int dataicon; // 4
  public Object pinid; // ptr 72

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    ri = DNATools.ptr(buffer); // get ptr
    v2d.read(buffer);
    mainb = buffer.getShort();
    mainbo = buffer.getShort();
    mainbuser = buffer.getShort();
    re_align = buffer.getShort();
    align = buffer.getShort();
    preview = buffer.getShort();
    texture_context = buffer.getShort();
    flag = buffer.get();
    pad = buffer.get();
    path = DNATools.ptr(buffer); // get ptr
    pathflag = buffer.getInt();
    dataicon = buffer.getInt();
    pinid = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    buffer.writeInt(ri!=null?ri.hashCode():0);
    v2d.write(buffer);
    buffer.writeShort(mainb);
    buffer.writeShort(mainbo);
    buffer.writeShort(mainbuser);
    buffer.writeShort(re_align);
    buffer.writeShort(align);
    buffer.writeShort(preview);
    buffer.writeShort(texture_context);
    buffer.writeByte(flag);
    buffer.writeByte(pad);
    buffer.writeInt(path!=null?path.hashCode():0);
    buffer.writeInt(pathflag);
    buffer.writeInt(dataicon);
    buffer.writeInt(pinid!=null?pinid.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceButs[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceButs:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  ri: ").append(ri).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  mainb: ").append(mainb).append("\n");
    sb.append("  mainbo: ").append(mainbo).append("\n");
    sb.append("  mainbuser: ").append(mainbuser).append("\n");
    sb.append("  re_align: ").append(re_align).append("\n");
    sb.append("  align: ").append(align).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  texture_context: ").append(texture_context).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  pathflag: ").append(pathflag).append("\n");
    sb.append("  dataicon: ").append(dataicon).append("\n");
    sb.append("  pinid: ").append(pinid).append("\n");
    return sb.toString();
  }
  public SpaceButs copy() { try {return (SpaceButs)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

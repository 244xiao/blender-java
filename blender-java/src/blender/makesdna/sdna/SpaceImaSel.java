package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceImaSel extends SpaceLink implements DNA, Cloneable { // #173
  public SpaceImaSel[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public Object files; // ptr (FileList) 0
  public byte[] title = new byte[24]; // 1
  public byte[] dir = new byte[240]; // 1
  public byte[] file = new byte[80]; // 1
  public short type; // 2
  public short menu; // 2
  public short flag; // 2
  public short sort; // 2
  public Object curfont; // ptr 0
  public int active_file; // 4
  public int numtilesx; // 4
  public int numtilesy; // 4
  public int selstate; // 4
  public rcti viewrect = new rcti(); // 16
  public rcti bookmarkrect = new rcti(); // 16
  public float scrollpos; // 4
  public float scrollheight; // 4
  public float scrollarea; // 4
  public float aspect; // 4
  public short retval; // 2
  public short ipotype; // 2
  public short filter; // 2
  public short active_bookmark; // 2
  public short pad; // 2
  public short pad1; // 2
  public short prv_w; // 2
  public short prv_h; // 2
  public Object returnfunc; // func ptr 0
  public Object returnfunc_event; // func ptr 0
  public Object returnfunc_args; // func ptr 0
  public Object arg1; // ptr 0
  public Object arg2; // ptr 0
  public Object menup; // ptr 2
  public Object pupmenu; // ptr 1
  public Object img; // ptr (ImBuf) 0

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    files = DNATools.ptr(buffer); // get ptr
    buffer.get(title);
    buffer.get(dir);
    buffer.get(file);
    type = buffer.getShort();
    menu = buffer.getShort();
    flag = buffer.getShort();
    sort = buffer.getShort();
    curfont = DNATools.ptr(buffer); // get ptr
    active_file = buffer.getInt();
    numtilesx = buffer.getInt();
    numtilesy = buffer.getInt();
    selstate = buffer.getInt();
    viewrect.read(buffer);
    bookmarkrect.read(buffer);
    scrollpos = buffer.getFloat();
    scrollheight = buffer.getFloat();
    scrollarea = buffer.getFloat();
    aspect = buffer.getFloat();
    retval = buffer.getShort();
    ipotype = buffer.getShort();
    filter = buffer.getShort();
    active_bookmark = buffer.getShort();
    pad = buffer.getShort();
    pad1 = buffer.getShort();
    prv_w = buffer.getShort();
    prv_h = buffer.getShort();
    returnfunc = DNATools.ptr(buffer); // get ptr
    returnfunc_event = DNATools.ptr(buffer); // get ptr
    returnfunc_args = DNATools.ptr(buffer); // get ptr
    arg1 = DNATools.ptr(buffer); // get ptr
    arg2 = DNATools.ptr(buffer); // get ptr
    menup = DNATools.ptr(buffer); // get ptr
    pupmenu = DNATools.ptr(buffer); // get ptr
    img = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    v2d.write(buffer);
    buffer.writeInt(files!=null?files.hashCode():0);
    buffer.write(title);
    buffer.write(dir);
    buffer.write(file);
    buffer.writeShort(type);
    buffer.writeShort(menu);
    buffer.writeShort(flag);
    buffer.writeShort(sort);
    buffer.writeInt(curfont!=null?curfont.hashCode():0);
    buffer.writeInt(active_file);
    buffer.writeInt(numtilesx);
    buffer.writeInt(numtilesy);
    buffer.writeInt(selstate);
    viewrect.write(buffer);
    bookmarkrect.write(buffer);
    buffer.writeFloat(scrollpos);
    buffer.writeFloat(scrollheight);
    buffer.writeFloat(scrollarea);
    buffer.writeFloat(aspect);
    buffer.writeShort(retval);
    buffer.writeShort(ipotype);
    buffer.writeShort(filter);
    buffer.writeShort(active_bookmark);
    buffer.writeShort(pad);
    buffer.writeShort(pad1);
    buffer.writeShort(prv_w);
    buffer.writeShort(prv_h);
    buffer.writeInt(returnfunc!=null?returnfunc.hashCode():0);
    buffer.writeInt(returnfunc_event!=null?returnfunc_event.hashCode():0);
    buffer.writeInt(returnfunc_args!=null?returnfunc_args.hashCode():0);
    buffer.writeInt(arg1!=null?arg1.hashCode():0);
    buffer.writeInt(arg2!=null?arg2.hashCode():0);
    buffer.writeInt(menup!=null?menup.hashCode():0);
    buffer.writeInt(pupmenu!=null?pupmenu.hashCode():0);
    buffer.writeInt(img!=null?img.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceImaSel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceImaSel:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  files: ").append(files).append("\n");
    sb.append("  title: ").append(new String(title)).append("\n");
    sb.append("  dir: ").append(new String(dir)).append("\n");
    sb.append("  file: ").append(new String(file)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  menu: ").append(menu).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sort: ").append(sort).append("\n");
    sb.append("  curfont: ").append(curfont).append("\n");
    sb.append("  active_file: ").append(active_file).append("\n");
    sb.append("  numtilesx: ").append(numtilesx).append("\n");
    sb.append("  numtilesy: ").append(numtilesy).append("\n");
    sb.append("  selstate: ").append(selstate).append("\n");
    sb.append("  viewrect: ").append(viewrect).append("\n");
    sb.append("  bookmarkrect: ").append(bookmarkrect).append("\n");
    sb.append("  scrollpos: ").append(scrollpos).append("\n");
    sb.append("  scrollheight: ").append(scrollheight).append("\n");
    sb.append("  scrollarea: ").append(scrollarea).append("\n");
    sb.append("  aspect: ").append(aspect).append("\n");
    sb.append("  retval: ").append(retval).append("\n");
    sb.append("  ipotype: ").append(ipotype).append("\n");
    sb.append("  filter: ").append(filter).append("\n");
    sb.append("  active_bookmark: ").append(active_bookmark).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  prv_w: ").append(prv_w).append("\n");
    sb.append("  prv_h: ").append(prv_h).append("\n");
    sb.append("  returnfunc: ").append(returnfunc).append("\n");
    sb.append("  returnfunc_event: ").append(returnfunc_event).append("\n");
    sb.append("  returnfunc_args: ").append(returnfunc_args).append("\n");
    sb.append("  arg1: ").append(arg1).append("\n");
    sb.append("  arg2: ").append(arg2).append("\n");
    sb.append("  menup: ").append(menup).append("\n");
    sb.append("  pupmenu: ").append(pupmenu).append("\n");
    sb.append("  img: ").append(img).append("\n");
    return sb.toString();
  }
  public SpaceImaSel copy() { try {return (SpaceImaSel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

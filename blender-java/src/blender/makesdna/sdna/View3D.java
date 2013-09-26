package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class View3D extends SpaceLink implements DNA, Cloneable { // #154
  public View3D[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public float[] viewquat = new float[4]; // 4
  public float dist; // 4
  public float pad1; // 4
  public int lay_used; // 4
  public short persp; // 2
  public short view; // 2
  public bObject camera; // ptr 1296
  public bObject ob_centre; // ptr 1296
  public ListBase bgpicbase = new ListBase(); // 16
  public BGpic bgpic; // ptr 88
  public View3D localvd; // ptr 328
  public byte[] ob_centre_bone = new byte[32]; // 1
  public int lay; // 4
  public int layact; // 4
  public short drawtype; // 2
  public short ob_centre_cursor; // 2
  public short scenelock; // 2
  public short around; // 2
  public short pad3; // 2
  public short flag; // 2
  public short flag2; // 2
  public short pivot_last; // 2
  public float lens; // 4
  public float grid; // 4
  public float gridview; // 4
  public float near; // 4
  public float far; // 4
  public float[] ofs = new float[3]; // 4
  public float[] cursor = new float[3]; // 4
  public short gridlines; // 2
  public short pad4; // 2
  public short gridflag; // 2
  public short gridsubdiv; // 2
  public short modeselect; // 2
  public short keyflags; // 2
  public short twtype; // 2
  public short twmode; // 2
  public short twflag; // 2
  public short twdrawflag; // 2
  public ListBase afterdraw_transp = new ListBase(); // 16
  public ListBase afterdraw_xray = new ListBase(); // 16
  public ListBase afterdraw_xraytransp = new ListBase(); // 16
  public short zbuf; // 2
  public short transp; // 2
  public short xray; // 2
  public byte ndofmode; // 1
  public byte ndoffilter; // 1
  public Object properties_storage; // ptr 0
  public bGPdata gpd; // ptr 104

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    for(int i=0;i<viewquat.length;i++) viewquat[i]=buffer.getFloat();
    dist = buffer.getFloat();
    pad1 = buffer.getFloat();
    lay_used = buffer.getInt();
    persp = buffer.getShort();
    view = buffer.getShort();
    camera = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    ob_centre = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    bgpicbase.read(buffer);
    bgpic = DNATools.link(DNATools.ptr(buffer), BGpic.class); // get ptr
    localvd = DNATools.link(DNATools.ptr(buffer), View3D.class); // get ptr
    buffer.get(ob_centre_bone);
    lay = buffer.getInt();
    layact = buffer.getInt();
    drawtype = buffer.getShort();
    ob_centre_cursor = buffer.getShort();
    scenelock = buffer.getShort();
    around = buffer.getShort();
    pad3 = buffer.getShort();
    flag = buffer.getShort();
    flag2 = buffer.getShort();
    pivot_last = buffer.getShort();
    lens = buffer.getFloat();
    grid = buffer.getFloat();
    gridview = buffer.getFloat();
    near = buffer.getFloat();
    far = buffer.getFloat();
    for(int i=0;i<ofs.length;i++) ofs[i]=buffer.getFloat();
    for(int i=0;i<cursor.length;i++) cursor[i]=buffer.getFloat();
    gridlines = buffer.getShort();
    pad4 = buffer.getShort();
    gridflag = buffer.getShort();
    gridsubdiv = buffer.getShort();
    modeselect = buffer.getShort();
    keyflags = buffer.getShort();
    twtype = buffer.getShort();
    twmode = buffer.getShort();
    twflag = buffer.getShort();
    twdrawflag = buffer.getShort();
    afterdraw_transp.read(buffer);
    afterdraw_xray.read(buffer);
    afterdraw_xraytransp.read(buffer);
    zbuf = buffer.getShort();
    transp = buffer.getShort();
    xray = buffer.getShort();
    ndofmode = buffer.get();
    ndoffilter = buffer.get();
    properties_storage = DNATools.ptr(buffer); // get ptr
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    for(int i=0;i<viewquat.length;i++) buffer.writeFloat(viewquat[i]);
    buffer.writeFloat(dist);
    buffer.writeFloat(pad1);
    buffer.writeInt(lay_used);
    buffer.writeShort(persp);
    buffer.writeShort(view);
    buffer.writeInt(camera!=null?camera.hashCode():0);
    buffer.writeInt(ob_centre!=null?ob_centre.hashCode():0);
    bgpicbase.write(buffer);
    buffer.writeInt(bgpic!=null?bgpic.hashCode():0);
    buffer.writeInt(localvd!=null?localvd.hashCode():0);
    buffer.write(ob_centre_bone);
    buffer.writeInt(lay);
    buffer.writeInt(layact);
    buffer.writeShort(drawtype);
    buffer.writeShort(ob_centre_cursor);
    buffer.writeShort(scenelock);
    buffer.writeShort(around);
    buffer.writeShort(pad3);
    buffer.writeShort(flag);
    buffer.writeShort(flag2);
    buffer.writeShort(pivot_last);
    buffer.writeFloat(lens);
    buffer.writeFloat(grid);
    buffer.writeFloat(gridview);
    buffer.writeFloat(near);
    buffer.writeFloat(far);
    for(int i=0;i<ofs.length;i++) buffer.writeFloat(ofs[i]);
    for(int i=0;i<cursor.length;i++) buffer.writeFloat(cursor[i]);
    buffer.writeShort(gridlines);
    buffer.writeShort(pad4);
    buffer.writeShort(gridflag);
    buffer.writeShort(gridsubdiv);
    buffer.writeShort(modeselect);
    buffer.writeShort(keyflags);
    buffer.writeShort(twtype);
    buffer.writeShort(twmode);
    buffer.writeShort(twflag);
    buffer.writeShort(twdrawflag);
    afterdraw_transp.write(buffer);
    afterdraw_xray.write(buffer);
    afterdraw_xraytransp.write(buffer);
    buffer.writeShort(zbuf);
    buffer.writeShort(transp);
    buffer.writeShort(xray);
    buffer.writeByte(ndofmode);
    buffer.writeByte(ndoffilter);
    buffer.writeInt(properties_storage!=null?properties_storage.hashCode():0);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (View3D[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("View3D:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  viewquat: ").append(Arrays.toString(viewquat)).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  lay_used: ").append(lay_used).append("\n");
    sb.append("  persp: ").append(persp).append("\n");
    sb.append("  view: ").append(view).append("\n");
    sb.append("  camera: ").append(camera).append("\n");
    sb.append("  ob_centre: ").append(ob_centre).append("\n");
    sb.append("  bgpicbase: ").append(bgpicbase).append("\n");
    sb.append("  bgpic: ").append(bgpic).append("\n");
    sb.append("  localvd: ").append(localvd).append("\n");
    sb.append("  ob_centre_bone: ").append(new String(ob_centre_bone)).append("\n");
    sb.append("  lay: ").append(lay).append("\n");
    sb.append("  layact: ").append(layact).append("\n");
    sb.append("  drawtype: ").append(drawtype).append("\n");
    sb.append("  ob_centre_cursor: ").append(ob_centre_cursor).append("\n");
    sb.append("  scenelock: ").append(scenelock).append("\n");
    sb.append("  around: ").append(around).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  flag2: ").append(flag2).append("\n");
    sb.append("  pivot_last: ").append(pivot_last).append("\n");
    sb.append("  lens: ").append(lens).append("\n");
    sb.append("  grid: ").append(grid).append("\n");
    sb.append("  gridview: ").append(gridview).append("\n");
    sb.append("  near: ").append(near).append("\n");
    sb.append("  far: ").append(far).append("\n");
    sb.append("  ofs: ").append(Arrays.toString(ofs)).append("\n");
    sb.append("  cursor: ").append(Arrays.toString(cursor)).append("\n");
    sb.append("  gridlines: ").append(gridlines).append("\n");
    sb.append("  pad4: ").append(pad4).append("\n");
    sb.append("  gridflag: ").append(gridflag).append("\n");
    sb.append("  gridsubdiv: ").append(gridsubdiv).append("\n");
    sb.append("  modeselect: ").append(modeselect).append("\n");
    sb.append("  keyflags: ").append(keyflags).append("\n");
    sb.append("  twtype: ").append(twtype).append("\n");
    sb.append("  twmode: ").append(twmode).append("\n");
    sb.append("  twflag: ").append(twflag).append("\n");
    sb.append("  twdrawflag: ").append(twdrawflag).append("\n");
    sb.append("  afterdraw_transp: ").append(afterdraw_transp).append("\n");
    sb.append("  afterdraw_xray: ").append(afterdraw_xray).append("\n");
    sb.append("  afterdraw_xraytransp: ").append(afterdraw_xraytransp).append("\n");
    sb.append("  zbuf: ").append(zbuf).append("\n");
    sb.append("  transp: ").append(transp).append("\n");
    sb.append("  xray: ").append(xray).append("\n");
    sb.append("  ndofmode: ").append(ndofmode).append("\n");
    sb.append("  ndoffilter: ").append(ndoffilter).append("\n");
    sb.append("  properties_storage: ").append(properties_storage).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    return sb.toString();
  }
  public View3D copy() { try {return (View3D)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

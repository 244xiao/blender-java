package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Curve extends ID implements DNA, Cloneable { // #45
  public Curve[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public BoundBox bb; // ptr 104
  public ListBase nurb = new ListBase(); // 16
  public ListBase disp = new ListBase(); // 16
  public EditNurb editnurb; // ptr 32
  public bObject bevobj; // ptr 1296
  public bObject taperobj; // ptr 1296
  public bObject textoncurve; // ptr 1296
  public Ipo ipo; // ptr 112
  public Object path; // ptr (Path) 0
  public Key key; // ptr 168
  public Material[] mat; // ptr 800
  public ListBase bev = new ListBase(); // 16
  public float[] loc = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float[] rot = new float[3]; // 4
  public short texflag; // 2
  public short pad1; // 2
  public short drawflag; // 2
  public short twist_mode; // 2
  public float twist_smooth; // 4
  public float smallcaps_scale; // 4
  public int pathlen; // 4
  public short pad; // 2
  public short totcol; // 2
  public short flag; // 2
  public short bevresol; // 2
  public float width; // 4
  public float ext1; // 4
  public float ext2; // 4
  public short resolu; // 2
  public short resolv; // 2
  public short resolu_ren; // 2
  public short resolv_ren; // 2
  public int actnu; // 4
  public Object lastsel; // ptr 0
  public short len; // 2
  public short lines; // 2
  public short pos; // 2
  public short spacemode; // 2
  public float spacing; // 4
  public float linedist; // 4
  public float shear; // 4
  public float fsize; // 4
  public float wordspace; // 4
  public float ulpos; // 4
  public float ulheight; // 4
  public float xof; // 4
  public float yof; // 4
  public float linewidth; // 4
  public Object str; // ptr 1
  public Object selboxes; // ptr (SelBox) 0
  public Object editfont; // ptr (EditFont) 0
  public byte[] family = new byte[24]; // 1
  public VFont vfont; // ptr 344
  public VFont vfontb; // ptr 344
  public VFont vfonti; // ptr 344
  public VFont vfontbi; // ptr 344
  public int sepchar; // 4
  public float ctime; // 4
  public int totbox; // 4
  public int actbox; // 4
  public TextBox tb; // ptr 16
  public int selstart; // 4
  public int selend; // 4
  public CharInfo strinfo; // ptr 8
  public CharInfo curinfo = new CharInfo(); // 8

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    bb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    nurb.read(buffer);
    disp.read(buffer);
    editnurb = DNATools.link(DNATools.ptr(buffer), EditNurb.class); // get ptr
    bevobj = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    taperobj = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    textoncurve = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    path = DNATools.ptr(buffer); // get ptr
    key = DNATools.link(DNATools.ptr(buffer), Key.class); // get ptr
    mat = DNATools.link(DNATools.ptr(buffer), Material[].class); // get ptr
    bev.read(buffer);
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    texflag = buffer.getShort();
    pad1 = buffer.getShort();
    drawflag = buffer.getShort();
    twist_mode = buffer.getShort();
    twist_smooth = buffer.getFloat();
    smallcaps_scale = buffer.getFloat();
    pathlen = buffer.getInt();
    pad = buffer.getShort();
    totcol = buffer.getShort();
    flag = buffer.getShort();
    bevresol = buffer.getShort();
    width = buffer.getFloat();
    ext1 = buffer.getFloat();
    ext2 = buffer.getFloat();
    resolu = buffer.getShort();
    resolv = buffer.getShort();
    resolu_ren = buffer.getShort();
    resolv_ren = buffer.getShort();
    actnu = buffer.getInt();
    lastsel = DNATools.ptr(buffer); // get ptr
    len = buffer.getShort();
    lines = buffer.getShort();
    pos = buffer.getShort();
    spacemode = buffer.getShort();
    spacing = buffer.getFloat();
    linedist = buffer.getFloat();
    shear = buffer.getFloat();
    fsize = buffer.getFloat();
    wordspace = buffer.getFloat();
    ulpos = buffer.getFloat();
    ulheight = buffer.getFloat();
    xof = buffer.getFloat();
    yof = buffer.getFloat();
    linewidth = buffer.getFloat();
    str = DNATools.ptr(buffer); // get ptr
    selboxes = DNATools.ptr(buffer); // get ptr
    editfont = DNATools.ptr(buffer); // get ptr
    buffer.get(family);
    vfont = DNATools.link(DNATools.ptr(buffer), VFont.class); // get ptr
    vfontb = DNATools.link(DNATools.ptr(buffer), VFont.class); // get ptr
    vfonti = DNATools.link(DNATools.ptr(buffer), VFont.class); // get ptr
    vfontbi = DNATools.link(DNATools.ptr(buffer), VFont.class); // get ptr
    sepchar = buffer.getInt();
    ctime = buffer.getFloat();
    totbox = buffer.getInt();
    actbox = buffer.getInt();
    tb = DNATools.link(DNATools.ptr(buffer), TextBox.class); // get ptr
    selstart = buffer.getInt();
    selend = buffer.getInt();
    strinfo = DNATools.link(DNATools.ptr(buffer), CharInfo.class); // get ptr
    curinfo.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(bb!=null?bb.hashCode():0);
    nurb.write(buffer);
    disp.write(buffer);
    buffer.writeInt(editnurb!=null?editnurb.hashCode():0);
    buffer.writeInt(bevobj!=null?bevobj.hashCode():0);
    buffer.writeInt(taperobj!=null?taperobj.hashCode():0);
    buffer.writeInt(textoncurve!=null?textoncurve.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(path!=null?path.hashCode():0);
    buffer.writeInt(key!=null?key.hashCode():0);
    buffer.writeInt(mat!=null?mat.hashCode():0);
    bev.write(buffer);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    buffer.writeShort(texflag);
    buffer.writeShort(pad1);
    buffer.writeShort(drawflag);
    buffer.writeShort(twist_mode);
    buffer.writeFloat(twist_smooth);
    buffer.writeFloat(smallcaps_scale);
    buffer.writeInt(pathlen);
    buffer.writeShort(pad);
    buffer.writeShort(totcol);
    buffer.writeShort(flag);
    buffer.writeShort(bevresol);
    buffer.writeFloat(width);
    buffer.writeFloat(ext1);
    buffer.writeFloat(ext2);
    buffer.writeShort(resolu);
    buffer.writeShort(resolv);
    buffer.writeShort(resolu_ren);
    buffer.writeShort(resolv_ren);
    buffer.writeInt(actnu);
    buffer.writeInt(lastsel!=null?lastsel.hashCode():0);
    buffer.writeShort(len);
    buffer.writeShort(lines);
    buffer.writeShort(pos);
    buffer.writeShort(spacemode);
    buffer.writeFloat(spacing);
    buffer.writeFloat(linedist);
    buffer.writeFloat(shear);
    buffer.writeFloat(fsize);
    buffer.writeFloat(wordspace);
    buffer.writeFloat(ulpos);
    buffer.writeFloat(ulheight);
    buffer.writeFloat(xof);
    buffer.writeFloat(yof);
    buffer.writeFloat(linewidth);
    buffer.writeInt(str!=null?str.hashCode():0);
    buffer.writeInt(selboxes!=null?selboxes.hashCode():0);
    buffer.writeInt(editfont!=null?editfont.hashCode():0);
    buffer.write(family);
    buffer.writeInt(vfont!=null?vfont.hashCode():0);
    buffer.writeInt(vfontb!=null?vfontb.hashCode():0);
    buffer.writeInt(vfonti!=null?vfonti.hashCode():0);
    buffer.writeInt(vfontbi!=null?vfontbi.hashCode():0);
    buffer.writeInt(sepchar);
    buffer.writeFloat(ctime);
    buffer.writeInt(totbox);
    buffer.writeInt(actbox);
    buffer.writeInt(tb!=null?tb.hashCode():0);
    buffer.writeInt(selstart);
    buffer.writeInt(selend);
    buffer.writeInt(strinfo!=null?strinfo.hashCode():0);
    curinfo.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (Curve[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Curve:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  bb: ").append(bb).append("\n");
    sb.append("  nurb: ").append(nurb).append("\n");
    sb.append("  disp: ").append(disp).append("\n");
    sb.append("  editnurb: ").append(editnurb).append("\n");
    sb.append("  bevobj: ").append(bevobj).append("\n");
    sb.append("  taperobj: ").append(taperobj).append("\n");
    sb.append("  textoncurve: ").append(textoncurve).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  bev: ").append(bev).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  texflag: ").append(texflag).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  drawflag: ").append(drawflag).append("\n");
    sb.append("  twist_mode: ").append(twist_mode).append("\n");
    sb.append("  twist_smooth: ").append(twist_smooth).append("\n");
    sb.append("  smallcaps_scale: ").append(smallcaps_scale).append("\n");
    sb.append("  pathlen: ").append(pathlen).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  totcol: ").append(totcol).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  bevresol: ").append(bevresol).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  ext1: ").append(ext1).append("\n");
    sb.append("  ext2: ").append(ext2).append("\n");
    sb.append("  resolu: ").append(resolu).append("\n");
    sb.append("  resolv: ").append(resolv).append("\n");
    sb.append("  resolu_ren: ").append(resolu_ren).append("\n");
    sb.append("  resolv_ren: ").append(resolv_ren).append("\n");
    sb.append("  actnu: ").append(actnu).append("\n");
    sb.append("  lastsel: ").append(lastsel).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  lines: ").append(lines).append("\n");
    sb.append("  pos: ").append(pos).append("\n");
    sb.append("  spacemode: ").append(spacemode).append("\n");
    sb.append("  spacing: ").append(spacing).append("\n");
    sb.append("  linedist: ").append(linedist).append("\n");
    sb.append("  shear: ").append(shear).append("\n");
    sb.append("  fsize: ").append(fsize).append("\n");
    sb.append("  wordspace: ").append(wordspace).append("\n");
    sb.append("  ulpos: ").append(ulpos).append("\n");
    sb.append("  ulheight: ").append(ulheight).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  linewidth: ").append(linewidth).append("\n");
    sb.append("  str: ").append(str).append("\n");
    sb.append("  selboxes: ").append(selboxes).append("\n");
    sb.append("  editfont: ").append(editfont).append("\n");
    sb.append("  family: ").append(new String(family)).append("\n");
    sb.append("  vfont: ").append(vfont).append("\n");
    sb.append("  vfontb: ").append(vfontb).append("\n");
    sb.append("  vfonti: ").append(vfonti).append("\n");
    sb.append("  vfontbi: ").append(vfontbi).append("\n");
    sb.append("  sepchar: ").append(sepchar).append("\n");
    sb.append("  ctime: ").append(ctime).append("\n");
    sb.append("  totbox: ").append(totbox).append("\n");
    sb.append("  actbox: ").append(actbox).append("\n");
    sb.append("  tb: ").append(tb).append("\n");
    sb.append("  selstart: ").append(selstart).append("\n");
    sb.append("  selend: ").append(selend).append("\n");
    sb.append("  strinfo: ").append(strinfo).append("\n");
    sb.append("  curinfo: ").append(curinfo).append("\n");
    return sb.toString();
  }
  public Curve copy() { try {return (Curve)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

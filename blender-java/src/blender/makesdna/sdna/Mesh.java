package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Mesh extends ID implements DNA, Cloneable { // #46
  public Mesh[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public BoundBox bb; // ptr 104
  public Ipo ipo; // ptr 112
  public Key key; // ptr 168
  public Material[] mat; // ptr 800
  public MFace mface; // ptr 20
  public MTFace mtface; // ptr 48
  public TFace tface; // ptr 64
  public MVert mvert; // ptr 20
  public MEdge medge; // ptr 12
  public MDeformVert dvert; // ptr 16
  public MCol mcol; // ptr 4
  public MSticky msticky; // ptr 8
  public Mesh texcomesh; // ptr 408
  public MSelect mselect; // ptr 8
  public Object edit_mesh; // ptr (EditMesh) 0
  public CustomData vdata = new CustomData(); // 40
  public CustomData edata = new CustomData(); // 40
  public CustomData fdata = new CustomData(); // 40
  public int totvert; // 4
  public int totedge; // 4
  public int totface; // 4
  public int totselect; // 4
  public int act_face; // 4
  public float[] loc = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float[] rot = new float[3]; // 4
  public short texflag; // 2
  public short drawflag; // 2
  public short smoothresh; // 2
  public short flag; // 2
  public short subdiv; // 2
  public short subdivr; // 2
  public byte subsurftype; // 1
  public byte editflag; // 1
  public short totcol; // 2
  public Multires mr; // ptr 128
  public PartialVisibility pv; // ptr 48

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    bb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    key = DNATools.link(DNATools.ptr(buffer), Key.class); // get ptr
    mat = DNATools.link(DNATools.ptr(buffer), Material[].class); // get ptr
    mface = DNATools.link(DNATools.ptr(buffer), MFace.class); // get ptr
    mtface = DNATools.link(DNATools.ptr(buffer), MTFace.class); // get ptr
    tface = DNATools.link(DNATools.ptr(buffer), TFace.class); // get ptr
    mvert = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    medge = DNATools.link(DNATools.ptr(buffer), MEdge.class); // get ptr
    dvert = DNATools.link(DNATools.ptr(buffer), MDeformVert.class); // get ptr
    mcol = DNATools.link(DNATools.ptr(buffer), MCol.class); // get ptr
    msticky = DNATools.link(DNATools.ptr(buffer), MSticky.class); // get ptr
    texcomesh = DNATools.link(DNATools.ptr(buffer), Mesh.class); // get ptr
    mselect = DNATools.link(DNATools.ptr(buffer), MSelect.class); // get ptr
    edit_mesh = DNATools.ptr(buffer); // get ptr
    vdata.read(buffer);
    edata.read(buffer);
    fdata.read(buffer);
    totvert = buffer.getInt();
    totedge = buffer.getInt();
    totface = buffer.getInt();
    totselect = buffer.getInt();
    act_face = buffer.getInt();
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    texflag = buffer.getShort();
    drawflag = buffer.getShort();
    smoothresh = buffer.getShort();
    flag = buffer.getShort();
    subdiv = buffer.getShort();
    subdivr = buffer.getShort();
    subsurftype = buffer.get();
    editflag = buffer.get();
    totcol = buffer.getShort();
    mr = DNATools.link(DNATools.ptr(buffer), Multires.class); // get ptr
    pv = DNATools.link(DNATools.ptr(buffer), PartialVisibility.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(bb!=null?bb.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(key!=null?key.hashCode():0);
    buffer.writeInt(mat!=null?mat.hashCode():0);
    buffer.writeInt(mface!=null?mface.hashCode():0);
    buffer.writeInt(mtface!=null?mtface.hashCode():0);
    buffer.writeInt(tface!=null?tface.hashCode():0);
    buffer.writeInt(mvert!=null?mvert.hashCode():0);
    buffer.writeInt(medge!=null?medge.hashCode():0);
    buffer.writeInt(dvert!=null?dvert.hashCode():0);
    buffer.writeInt(mcol!=null?mcol.hashCode():0);
    buffer.writeInt(msticky!=null?msticky.hashCode():0);
    buffer.writeInt(texcomesh!=null?texcomesh.hashCode():0);
    buffer.writeInt(mselect!=null?mselect.hashCode():0);
    buffer.writeInt(edit_mesh!=null?edit_mesh.hashCode():0);
    vdata.write(buffer);
    edata.write(buffer);
    fdata.write(buffer);
    buffer.writeInt(totvert);
    buffer.writeInt(totedge);
    buffer.writeInt(totface);
    buffer.writeInt(totselect);
    buffer.writeInt(act_face);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    buffer.writeShort(texflag);
    buffer.writeShort(drawflag);
    buffer.writeShort(smoothresh);
    buffer.writeShort(flag);
    buffer.writeShort(subdiv);
    buffer.writeShort(subdivr);
    buffer.writeByte(subsurftype);
    buffer.writeByte(editflag);
    buffer.writeShort(totcol);
    buffer.writeInt(mr!=null?mr.hashCode():0);
    buffer.writeInt(pv!=null?pv.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Mesh[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Mesh:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  bb: ").append(bb).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  mface: ").append(mface).append("\n");
    sb.append("  mtface: ").append(mtface).append("\n");
    sb.append("  tface: ").append(tface).append("\n");
    sb.append("  mvert: ").append(mvert).append("\n");
    sb.append("  medge: ").append(medge).append("\n");
    sb.append("  dvert: ").append(dvert).append("\n");
    sb.append("  mcol: ").append(mcol).append("\n");
    sb.append("  msticky: ").append(msticky).append("\n");
    sb.append("  texcomesh: ").append(texcomesh).append("\n");
    sb.append("  mselect: ").append(mselect).append("\n");
    sb.append("  edit_mesh: ").append(edit_mesh).append("\n");
    sb.append("  vdata: ").append(vdata).append("\n");
    sb.append("  edata: ").append(edata).append("\n");
    sb.append("  fdata: ").append(fdata).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  totedge: ").append(totedge).append("\n");
    sb.append("  totface: ").append(totface).append("\n");
    sb.append("  totselect: ").append(totselect).append("\n");
    sb.append("  act_face: ").append(act_face).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  texflag: ").append(texflag).append("\n");
    sb.append("  drawflag: ").append(drawflag).append("\n");
    sb.append("  smoothresh: ").append(smoothresh).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  subdiv: ").append(subdiv).append("\n");
    sb.append("  subdivr: ").append(subdivr).append("\n");
    sb.append("  subsurftype: ").append(subsurftype).append("\n");
    sb.append("  editflag: ").append(editflag).append("\n");
    sb.append("  totcol: ").append(totcol).append("\n");
    sb.append("  mr: ").append(mr).append("\n");
    sb.append("  pv: ").append(pv).append("\n");
    return sb.toString();
  }
  public Mesh copy() { try {return (Mesh)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

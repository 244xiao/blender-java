package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MetaBall extends ID implements DNA, Cloneable { // #38
  public MetaBall[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public BoundBox bb; // ptr 104
  public ListBase elems = new ListBase(); // 16
  public ListBase disp = new ListBase(); // 16
  public ListBase editelems; // ptr 16
  public Ipo ipo; // ptr 112
  public Material[] mat; // ptr 800
  public byte flag; // 1
  public byte flag2; // 1
  public short totcol; // 2
  public short texflag; // 2
  public short pad; // 2
  public float[] loc = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float[] rot = new float[3]; // 4
  public float wiresize; // 4
  public float rendersize; // 4
  public float thresh; // 4
  public MetaElem lastelem; // ptr 128

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    bb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    elems.read(buffer);
    disp.read(buffer);
    editelems = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    mat = DNATools.link(DNATools.ptr(buffer), Material[].class); // get ptr
    flag = buffer.get();
    flag2 = buffer.get();
    totcol = buffer.getShort();
    texflag = buffer.getShort();
    pad = buffer.getShort();
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    wiresize = buffer.getFloat();
    rendersize = buffer.getFloat();
    thresh = buffer.getFloat();
    lastelem = DNATools.link(DNATools.ptr(buffer), MetaElem.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(bb!=null?bb.hashCode():0);
    elems.write(buffer);
    disp.write(buffer);
    buffer.writeInt(editelems!=null?editelems.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(mat!=null?mat.hashCode():0);
    buffer.writeByte(flag);
    buffer.writeByte(flag2);
    buffer.writeShort(totcol);
    buffer.writeShort(texflag);
    buffer.writeShort(pad);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    buffer.writeFloat(wiresize);
    buffer.writeFloat(rendersize);
    buffer.writeFloat(thresh);
    buffer.writeInt(lastelem!=null?lastelem.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MetaBall[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MetaBall:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  bb: ").append(bb).append("\n");
    sb.append("  elems: ").append(elems).append("\n");
    sb.append("  disp: ").append(disp).append("\n");
    sb.append("  editelems: ").append(editelems).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  flag2: ").append(flag2).append("\n");
    sb.append("  totcol: ").append(totcol).append("\n");
    sb.append("  texflag: ").append(texflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  wiresize: ").append(wiresize).append("\n");
    sb.append("  rendersize: ").append(rendersize).append("\n");
    sb.append("  thresh: ").append(thresh).append("\n");
    sb.append("  lastelem: ").append(lastelem).append("\n");
    return sb.toString();
  }
  public MetaBall copy() { try {return (MetaBall)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

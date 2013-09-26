package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Lattice extends ID implements DNA, Cloneable { // #111
  public Lattice[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public short pntsu; // 2
  public short pntsv; // 2
  public short pntsw; // 2
  public short flag; // 2
  public short opntsu; // 2
  public short opntsv; // 2
  public short opntsw; // 2
  public short pad2; // 2
  public byte typeu; // 1
  public byte typev; // 1
  public byte typew; // 1
  public byte pad3; // 1
  public int pad; // 4
  public float fu; // 4
  public float fv; // 4
  public float fw; // 4
  public float du; // 4
  public float dv; // 4
  public float dw; // 4
  public BPoint def; // ptr 36
  public Ipo ipo; // ptr 112
  public Key key; // ptr 168
  public MDeformVert dvert; // ptr 16
  public byte[] vgroup = new byte[32]; // 1
  public Object latticedata; // ptr 4
  public float[][] latmat = new float[4][4]; // 4
  public EditLatt editlatt; // ptr 16

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    pntsu = buffer.getShort();
    pntsv = buffer.getShort();
    pntsw = buffer.getShort();
    flag = buffer.getShort();
    opntsu = buffer.getShort();
    opntsv = buffer.getShort();
    opntsw = buffer.getShort();
    pad2 = buffer.getShort();
    typeu = buffer.get();
    typev = buffer.get();
    typew = buffer.get();
    pad3 = buffer.get();
    pad = buffer.getInt();
    fu = buffer.getFloat();
    fv = buffer.getFloat();
    fw = buffer.getFloat();
    du = buffer.getFloat();
    dv = buffer.getFloat();
    dw = buffer.getFloat();
    def = DNATools.link(DNATools.ptr(buffer), BPoint.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    key = DNATools.link(DNATools.ptr(buffer), Key.class); // get ptr
    dvert = DNATools.link(DNATools.ptr(buffer), MDeformVert.class); // get ptr
    buffer.get(vgroup);
    latticedata = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<latmat.length;i++) for(int j=0;j<latmat[i].length;j++) latmat[i][j]=buffer.getFloat();
    editlatt = DNATools.link(DNATools.ptr(buffer), EditLatt.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeShort(pntsu);
    buffer.writeShort(pntsv);
    buffer.writeShort(pntsw);
    buffer.writeShort(flag);
    buffer.writeShort(opntsu);
    buffer.writeShort(opntsv);
    buffer.writeShort(opntsw);
    buffer.writeShort(pad2);
    buffer.writeByte(typeu);
    buffer.writeByte(typev);
    buffer.writeByte(typew);
    buffer.writeByte(pad3);
    buffer.writeInt(pad);
    buffer.writeFloat(fu);
    buffer.writeFloat(fv);
    buffer.writeFloat(fw);
    buffer.writeFloat(du);
    buffer.writeFloat(dv);
    buffer.writeFloat(dw);
    buffer.writeInt(def!=null?def.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(key!=null?key.hashCode():0);
    buffer.writeInt(dvert!=null?dvert.hashCode():0);
    buffer.write(vgroup);
    buffer.writeInt(latticedata!=null?latticedata.hashCode():0);
    for(int i=0; i<latmat.length; i++)  for(int j=0;j<latmat[i].length;j++) buffer.writeFloat(latmat[i][j]);
    buffer.writeInt(editlatt!=null?editlatt.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Lattice[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Lattice:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  pntsu: ").append(pntsu).append("\n");
    sb.append("  pntsv: ").append(pntsv).append("\n");
    sb.append("  pntsw: ").append(pntsw).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  opntsu: ").append(opntsu).append("\n");
    sb.append("  opntsv: ").append(opntsv).append("\n");
    sb.append("  opntsw: ").append(opntsw).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  typeu: ").append(typeu).append("\n");
    sb.append("  typev: ").append(typev).append("\n");
    sb.append("  typew: ").append(typew).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  fu: ").append(fu).append("\n");
    sb.append("  fv: ").append(fv).append("\n");
    sb.append("  fw: ").append(fw).append("\n");
    sb.append("  du: ").append(du).append("\n");
    sb.append("  dv: ").append(dv).append("\n");
    sb.append("  dw: ").append(dw).append("\n");
    sb.append("  def: ").append(def).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  key: ").append(key).append("\n");
    sb.append("  dvert: ").append(dvert).append("\n");
    sb.append("  vgroup: ").append(new String(vgroup)).append("\n");
    sb.append("  latticedata: ").append(latticedata).append("\n");
    sb.append("  latmat: ").append(Arrays.toString(latmat)).append("\n");
    sb.append("  editlatt: ").append(editlatt).append("\n");
    return sb.toString();
  }
  public Lattice copy() { try {return (Lattice)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

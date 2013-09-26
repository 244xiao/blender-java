package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PartEff extends Link<PartEff> implements DNA, Cloneable { // #214
  public PartEff[] myarray;
  public short type; // 2
  public short flag; // 2
  public short buttype; // 2
  public short stype; // 2
  public short vertgroup; // 2
  public short userjit; // 2
  public float sta; // 4
  public float end; // 4
  public float lifetime; // 4
  public int totpart; // 4
  public int totkey; // 4
  public int seed; // 4
  public float normfac; // 4
  public float obfac; // 4
  public float randfac; // 4
  public float texfac; // 4
  public float randlife; // 4
  public float[] force = new float[3]; // 4
  public float damp; // 4
  public float nabla; // 4
  public float vectsize; // 4
  public float maxlen; // 4
  public float pad; // 4
  public float[] defvec = new float[3]; // 4
  public float[] mult = new float[4]; // 4
  public float[] life = new float[4]; // 4
  public short[] child = new short[4]; // 2
  public short[] mat = new short[4]; // 2
  public short texmap; // 2
  public short curmult; // 2
  public short staticstep; // 2
  public short omat; // 2
  public short timetex; // 2
  public short speedtex; // 2
  public short flag2; // 2
  public short flag2neg; // 2
  public short disp; // 2
  public short vertgroup_v; // 2
  public byte[] vgroupname = new byte[32]; // 1
  public byte[] vgroupname_v = new byte[32]; // 1
  public float[][] imat = new float[4][4]; // 4
  public Object keys; // ptr (Particle) 0
  public Group group; // ptr 104

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), PartEff.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), PartEff.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    buttype = buffer.getShort();
    stype = buffer.getShort();
    vertgroup = buffer.getShort();
    userjit = buffer.getShort();
    sta = buffer.getFloat();
    end = buffer.getFloat();
    lifetime = buffer.getFloat();
    totpart = buffer.getInt();
    totkey = buffer.getInt();
    seed = buffer.getInt();
    normfac = buffer.getFloat();
    obfac = buffer.getFloat();
    randfac = buffer.getFloat();
    texfac = buffer.getFloat();
    randlife = buffer.getFloat();
    for(int i=0;i<force.length;i++) force[i]=buffer.getFloat();
    damp = buffer.getFloat();
    nabla = buffer.getFloat();
    vectsize = buffer.getFloat();
    maxlen = buffer.getFloat();
    pad = buffer.getFloat();
    for(int i=0;i<defvec.length;i++) defvec[i]=buffer.getFloat();
    for(int i=0;i<mult.length;i++) mult[i]=buffer.getFloat();
    for(int i=0;i<life.length;i++) life[i]=buffer.getFloat();
    for(int i=0;i<child.length;i++) child[i]=buffer.getShort();
    for(int i=0;i<mat.length;i++) mat[i]=buffer.getShort();
    texmap = buffer.getShort();
    curmult = buffer.getShort();
    staticstep = buffer.getShort();
    omat = buffer.getShort();
    timetex = buffer.getShort();
    speedtex = buffer.getShort();
    flag2 = buffer.getShort();
    flag2neg = buffer.getShort();
    disp = buffer.getShort();
    vertgroup_v = buffer.getShort();
    buffer.get(vgroupname);
    buffer.get(vgroupname_v);
    for(int i=0;i<imat.length;i++) for(int j=0;j<imat[i].length;j++) imat[i][j]=buffer.getFloat();
    keys = DNATools.ptr(buffer); // get ptr
    group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(buttype);
    buffer.writeShort(stype);
    buffer.writeShort(vertgroup);
    buffer.writeShort(userjit);
    buffer.writeFloat(sta);
    buffer.writeFloat(end);
    buffer.writeFloat(lifetime);
    buffer.writeInt(totpart);
    buffer.writeInt(totkey);
    buffer.writeInt(seed);
    buffer.writeFloat(normfac);
    buffer.writeFloat(obfac);
    buffer.writeFloat(randfac);
    buffer.writeFloat(texfac);
    buffer.writeFloat(randlife);
    for(int i=0;i<force.length;i++) buffer.writeFloat(force[i]);
    buffer.writeFloat(damp);
    buffer.writeFloat(nabla);
    buffer.writeFloat(vectsize);
    buffer.writeFloat(maxlen);
    buffer.writeFloat(pad);
    for(int i=0;i<defvec.length;i++) buffer.writeFloat(defvec[i]);
    for(int i=0;i<mult.length;i++) buffer.writeFloat(mult[i]);
    for(int i=0;i<life.length;i++) buffer.writeFloat(life[i]);
    for(int i=0;i<child.length;i++) buffer.writeShort(child[i]);
    for(int i=0;i<mat.length;i++) buffer.writeShort(mat[i]);
    buffer.writeShort(texmap);
    buffer.writeShort(curmult);
    buffer.writeShort(staticstep);
    buffer.writeShort(omat);
    buffer.writeShort(timetex);
    buffer.writeShort(speedtex);
    buffer.writeShort(flag2);
    buffer.writeShort(flag2neg);
    buffer.writeShort(disp);
    buffer.writeShort(vertgroup_v);
    buffer.write(vgroupname);
    buffer.write(vgroupname_v);
    for(int i=0; i<imat.length; i++)  for(int j=0;j<imat[i].length;j++) buffer.writeFloat(imat[i][j]);
    buffer.writeInt(keys!=null?keys.hashCode():0);
    buffer.writeInt(group!=null?group.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PartEff[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PartEff:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  buttype: ").append(buttype).append("\n");
    sb.append("  stype: ").append(stype).append("\n");
    sb.append("  vertgroup: ").append(vertgroup).append("\n");
    sb.append("  userjit: ").append(userjit).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  lifetime: ").append(lifetime).append("\n");
    sb.append("  totpart: ").append(totpart).append("\n");
    sb.append("  totkey: ").append(totkey).append("\n");
    sb.append("  seed: ").append(seed).append("\n");
    sb.append("  normfac: ").append(normfac).append("\n");
    sb.append("  obfac: ").append(obfac).append("\n");
    sb.append("  randfac: ").append(randfac).append("\n");
    sb.append("  texfac: ").append(texfac).append("\n");
    sb.append("  randlife: ").append(randlife).append("\n");
    sb.append("  force: ").append(Arrays.toString(force)).append("\n");
    sb.append("  damp: ").append(damp).append("\n");
    sb.append("  nabla: ").append(nabla).append("\n");
    sb.append("  vectsize: ").append(vectsize).append("\n");
    sb.append("  maxlen: ").append(maxlen).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  defvec: ").append(Arrays.toString(defvec)).append("\n");
    sb.append("  mult: ").append(Arrays.toString(mult)).append("\n");
    sb.append("  life: ").append(Arrays.toString(life)).append("\n");
    sb.append("  child: ").append(Arrays.toString(child)).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  texmap: ").append(texmap).append("\n");
    sb.append("  curmult: ").append(curmult).append("\n");
    sb.append("  staticstep: ").append(staticstep).append("\n");
    sb.append("  omat: ").append(omat).append("\n");
    sb.append("  timetex: ").append(timetex).append("\n");
    sb.append("  speedtex: ").append(speedtex).append("\n");
    sb.append("  flag2: ").append(flag2).append("\n");
    sb.append("  flag2neg: ").append(flag2neg).append("\n");
    sb.append("  disp: ").append(disp).append("\n");
    sb.append("  vertgroup_v: ").append(vertgroup_v).append("\n");
    sb.append("  vgroupname: ").append(new String(vgroupname)).append("\n");
    sb.append("  vgroupname_v: ").append(new String(vgroupname_v)).append("\n");
    sb.append("  imat: ").append(Arrays.toString(imat)).append("\n");
    sb.append("  keys: ").append(keys).append("\n");
    sb.append("  group: ").append(group).append("\n");
    return sb.toString();
  }
  public PartEff copy() { try {return (PartEff)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

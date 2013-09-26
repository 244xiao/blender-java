package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MeshDeformModifierData extends ModifierData implements DNA, Cloneable { // #99
  public MeshDeformModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public byte[] defgrp_name = new byte[32]; // 1
  public short gridsize; // 2
  public short flag; // 2
  public short mode; // 2
  public short pad; // 2
  public MDefInfluence bindinfluences; // ptr 8
  public Object bindoffsets; // ptr 4
  public Object bindcagecos; // ptr 4
  public int totvert; // 4
  public int totcagevert; // 4
  public MDefCell dyngrid; // ptr 8
  public MDefInfluence dyninfluences; // ptr 8
  public Object dynverts; // ptr 4
  public Object pad2; // ptr 4
  public int dyngridsize; // 4
  public int totinfluence; // 4
  public float[] dyncellmin = new float[3]; // 4
  public float dyncellwidth; // 4
  public float[][] bindmat = new float[4][4]; // 4
  public Object bindweights; // ptr 4
  public Object bindcos; // ptr 4
  public Object bindfunc; // func ptr 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(defgrp_name);
    gridsize = buffer.getShort();
    flag = buffer.getShort();
    mode = buffer.getShort();
    pad = buffer.getShort();
    bindinfluences = DNATools.link(DNATools.ptr(buffer), MDefInfluence.class); // get ptr
    bindoffsets = DNATools.ptr(buffer); // get ptr
    bindcagecos = DNATools.ptr(buffer); // get ptr
    totvert = buffer.getInt();
    totcagevert = buffer.getInt();
    dyngrid = DNATools.link(DNATools.ptr(buffer), MDefCell.class); // get ptr
    dyninfluences = DNATools.link(DNATools.ptr(buffer), MDefInfluence.class); // get ptr
    dynverts = DNATools.ptr(buffer); // get ptr
    pad2 = DNATools.ptr(buffer); // get ptr
    dyngridsize = buffer.getInt();
    totinfluence = buffer.getInt();
    for(int i=0;i<dyncellmin.length;i++) dyncellmin[i]=buffer.getFloat();
    dyncellwidth = buffer.getFloat();
    for(int i=0;i<bindmat.length;i++) for(int j=0;j<bindmat[i].length;j++) bindmat[i][j]=buffer.getFloat();
    bindweights = DNATools.ptr(buffer); // get ptr
    bindcos = DNATools.ptr(buffer); // get ptr
    bindfunc = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.write(defgrp_name);
    buffer.writeShort(gridsize);
    buffer.writeShort(flag);
    buffer.writeShort(mode);
    buffer.writeShort(pad);
    buffer.writeInt(bindinfluences!=null?bindinfluences.hashCode():0);
    buffer.writeInt(bindoffsets!=null?bindoffsets.hashCode():0);
    buffer.writeInt(bindcagecos!=null?bindcagecos.hashCode():0);
    buffer.writeInt(totvert);
    buffer.writeInt(totcagevert);
    buffer.writeInt(dyngrid!=null?dyngrid.hashCode():0);
    buffer.writeInt(dyninfluences!=null?dyninfluences.hashCode():0);
    buffer.writeInt(dynverts!=null?dynverts.hashCode():0);
    buffer.writeInt(pad2!=null?pad2.hashCode():0);
    buffer.writeInt(dyngridsize);
    buffer.writeInt(totinfluence);
    for(int i=0;i<dyncellmin.length;i++) buffer.writeFloat(dyncellmin[i]);
    buffer.writeFloat(dyncellwidth);
    for(int i=0; i<bindmat.length; i++)  for(int j=0;j<bindmat[i].length;j++) buffer.writeFloat(bindmat[i][j]);
    buffer.writeInt(bindweights!=null?bindweights.hashCode():0);
    buffer.writeInt(bindcos!=null?bindcos.hashCode():0);
    buffer.writeInt(bindfunc!=null?bindfunc.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MeshDeformModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MeshDeformModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  gridsize: ").append(gridsize).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  bindinfluences: ").append(bindinfluences).append("\n");
    sb.append("  bindoffsets: ").append(bindoffsets).append("\n");
    sb.append("  bindcagecos: ").append(bindcagecos).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  totcagevert: ").append(totcagevert).append("\n");
    sb.append("  dyngrid: ").append(dyngrid).append("\n");
    sb.append("  dyninfluences: ").append(dyninfluences).append("\n");
    sb.append("  dynverts: ").append(dynverts).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  dyngridsize: ").append(dyngridsize).append("\n");
    sb.append("  totinfluence: ").append(totinfluence).append("\n");
    sb.append("  dyncellmin: ").append(Arrays.toString(dyncellmin)).append("\n");
    sb.append("  dyncellwidth: ").append(dyncellwidth).append("\n");
    sb.append("  bindmat: ").append(Arrays.toString(bindmat)).append("\n");
    sb.append("  bindweights: ").append(bindweights).append("\n");
    sb.append("  bindcos: ").append(bindcos).append("\n");
    sb.append("  bindfunc: ").append(bindfunc).append("\n");
    return sb.toString();
  }
  public MeshDeformModifierData copy() { try {return (MeshDeformModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

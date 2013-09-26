package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bArmature extends ID implements DNA, Cloneable { // #262
  public bArmature[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public ListBase bonebase = new ListBase(); // 16
  public ListBase chainbase = new ListBase(); // 16
  public ListBase edbo; // ptr 16
  public Bone act_bone; // ptr 296
  public Object act_edbone; // ptr 0
  public Object sketch; // ptr 0
  public int flag; // 4
  public int drawtype; // 4
  public short deformflag; // 2
  public short pathflag; // 2
  public int layer_used; // 4
  public int layer; // 4
  public int layer_protected; // 4
  public short ghostep; // 2
  public short ghostsize; // 2
  public short ghosttype; // 2
  public short pathsize; // 2
  public int ghostsf; // 4
  public int ghostef; // 4
  public int pathsf; // 4
  public int pathef; // 4
  public int pathbc; // 4
  public int pathac; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    bonebase.read(buffer);
    chainbase.read(buffer);
    edbo = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    act_bone = DNATools.link(DNATools.ptr(buffer), Bone.class); // get ptr
    act_edbone = DNATools.ptr(buffer); // get ptr
    sketch = DNATools.ptr(buffer); // get ptr
    flag = buffer.getInt();
    drawtype = buffer.getInt();
    deformflag = buffer.getShort();
    pathflag = buffer.getShort();
    layer_used = buffer.getInt();
    layer = buffer.getInt();
    layer_protected = buffer.getInt();
    ghostep = buffer.getShort();
    ghostsize = buffer.getShort();
    ghosttype = buffer.getShort();
    pathsize = buffer.getShort();
    ghostsf = buffer.getInt();
    ghostef = buffer.getInt();
    pathsf = buffer.getInt();
    pathef = buffer.getInt();
    pathbc = buffer.getInt();
    pathac = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    bonebase.write(buffer);
    chainbase.write(buffer);
    buffer.writeInt(edbo!=null?edbo.hashCode():0);
    buffer.writeInt(act_bone!=null?act_bone.hashCode():0);
    buffer.writeInt(act_edbone!=null?act_edbone.hashCode():0);
    buffer.writeInt(sketch!=null?sketch.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(drawtype);
    buffer.writeShort(deformflag);
    buffer.writeShort(pathflag);
    buffer.writeInt(layer_used);
    buffer.writeInt(layer);
    buffer.writeInt(layer_protected);
    buffer.writeShort(ghostep);
    buffer.writeShort(ghostsize);
    buffer.writeShort(ghosttype);
    buffer.writeShort(pathsize);
    buffer.writeInt(ghostsf);
    buffer.writeInt(ghostef);
    buffer.writeInt(pathsf);
    buffer.writeInt(pathef);
    buffer.writeInt(pathbc);
    buffer.writeInt(pathac);
  }
  public Object setmyarray(Object array) {
    myarray = (bArmature[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bArmature:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  bonebase: ").append(bonebase).append("\n");
    sb.append("  chainbase: ").append(chainbase).append("\n");
    sb.append("  edbo: ").append(edbo).append("\n");
    sb.append("  act_bone: ").append(act_bone).append("\n");
    sb.append("  act_edbone: ").append(act_edbone).append("\n");
    sb.append("  sketch: ").append(sketch).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  drawtype: ").append(drawtype).append("\n");
    sb.append("  deformflag: ").append(deformflag).append("\n");
    sb.append("  pathflag: ").append(pathflag).append("\n");
    sb.append("  layer_used: ").append(layer_used).append("\n");
    sb.append("  layer: ").append(layer).append("\n");
    sb.append("  layer_protected: ").append(layer_protected).append("\n");
    sb.append("  ghostep: ").append(ghostep).append("\n");
    sb.append("  ghostsize: ").append(ghostsize).append("\n");
    sb.append("  ghosttype: ").append(ghosttype).append("\n");
    sb.append("  pathsize: ").append(pathsize).append("\n");
    sb.append("  ghostsf: ").append(ghostsf).append("\n");
    sb.append("  ghostef: ").append(ghostef).append("\n");
    sb.append("  pathsf: ").append(pathsf).append("\n");
    sb.append("  pathef: ").append(pathef).append("\n");
    sb.append("  pathbc: ").append(pathbc).append("\n");
    sb.append("  pathac: ").append(pathac).append("\n");
    return sb.toString();
  }
  public bArmature copy() { try {return (bArmature)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

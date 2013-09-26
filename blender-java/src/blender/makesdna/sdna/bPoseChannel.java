package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPoseChannel extends Link<bPoseChannel> implements DNA, Cloneable { // #266
  public bPoseChannel[] myarray;
  public IDProperty prop; // ptr 96
  public ListBase constraints = new ListBase(); // 16
  public byte[] name = new byte[32]; // 1
  public short flag; // 2
  public short constflag; // 2
  public short ikflag; // 2
  public short selectflag; // 2
  public short protectflag; // 2
  public short agrp_index; // 2
  public int pathlen; // 4
  public int pathsf; // 4
  public int pathef; // 4
  public Bone bone; // ptr 296
  public bPoseChannel parent; // ptr 504
  public bPoseChannel child; // ptr 504
  public ListBase iktree = new ListBase(); // 16
  public bMotionPath mpath; // ptr 24
  public bObject custom; // ptr 1296
  public bPoseChannel custom_tx; // ptr 504
  public float[] loc = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float[] eul = new float[3]; // 4
  public float[] quat = new float[4]; // 4
  public float[] rotAxis = new float[3]; // 4
  public float rotAngle; // 4
  public short rotmode; // 2
  public short pad; // 2
  public float[][] chan_mat = new float[4][4]; // 4
  public float[][] pose_mat = new float[4][4]; // 4
  public float[][] constinv = new float[4][4]; // 4
  public float[] pose_head = new float[3]; // 4
  public float[] pose_tail = new float[3]; // 4
  public float[] limitmin = new float[3]; // 4
  public float[] limitmax = new float[3]; // 4
  public float[] stiffness = new float[3]; // 4
  public float ikstretch; // 4
  public float ikrotweight; // 4
  public float iklinweight; // 4
  public Object path; // ptr 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bPoseChannel.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bPoseChannel.class); // get ptr
    prop = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    constraints.read(buffer);
    buffer.get(name);
    flag = buffer.getShort();
    constflag = buffer.getShort();
    ikflag = buffer.getShort();
    selectflag = buffer.getShort();
    protectflag = buffer.getShort();
    agrp_index = buffer.getShort();
    pathlen = buffer.getInt();
    pathsf = buffer.getInt();
    pathef = buffer.getInt();
    bone = DNATools.link(DNATools.ptr(buffer), Bone.class); // get ptr
    parent = DNATools.link(DNATools.ptr(buffer), bPoseChannel.class); // get ptr
    child = DNATools.link(DNATools.ptr(buffer), bPoseChannel.class); // get ptr
    iktree.read(buffer);
    mpath = DNATools.link(DNATools.ptr(buffer), bMotionPath.class); // get ptr
    custom = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    custom_tx = DNATools.link(DNATools.ptr(buffer), bPoseChannel.class); // get ptr
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    for(int i=0;i<eul.length;i++) eul[i]=buffer.getFloat();
    for(int i=0;i<quat.length;i++) quat[i]=buffer.getFloat();
    for(int i=0;i<rotAxis.length;i++) rotAxis[i]=buffer.getFloat();
    rotAngle = buffer.getFloat();
    rotmode = buffer.getShort();
    pad = buffer.getShort();
    for(int i=0;i<chan_mat.length;i++) for(int j=0;j<chan_mat[i].length;j++) chan_mat[i][j]=buffer.getFloat();
    for(int i=0;i<pose_mat.length;i++) for(int j=0;j<pose_mat[i].length;j++) pose_mat[i][j]=buffer.getFloat();
    for(int i=0;i<constinv.length;i++) for(int j=0;j<constinv[i].length;j++) constinv[i][j]=buffer.getFloat();
    for(int i=0;i<pose_head.length;i++) pose_head[i]=buffer.getFloat();
    for(int i=0;i<pose_tail.length;i++) pose_tail[i]=buffer.getFloat();
    for(int i=0;i<limitmin.length;i++) limitmin[i]=buffer.getFloat();
    for(int i=0;i<limitmax.length;i++) limitmax[i]=buffer.getFloat();
    for(int i=0;i<stiffness.length;i++) stiffness[i]=buffer.getFloat();
    ikstretch = buffer.getFloat();
    ikrotweight = buffer.getFloat();
    iklinweight = buffer.getFloat();
    path = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(prop!=null?prop.hashCode():0);
    constraints.write(buffer);
    buffer.write(name);
    buffer.writeShort(flag);
    buffer.writeShort(constflag);
    buffer.writeShort(ikflag);
    buffer.writeShort(selectflag);
    buffer.writeShort(protectflag);
    buffer.writeShort(agrp_index);
    buffer.writeInt(pathlen);
    buffer.writeInt(pathsf);
    buffer.writeInt(pathef);
    buffer.writeInt(bone!=null?bone.hashCode():0);
    buffer.writeInt(parent!=null?parent.hashCode():0);
    buffer.writeInt(child!=null?child.hashCode():0);
    iktree.write(buffer);
    buffer.writeInt(mpath!=null?mpath.hashCode():0);
    buffer.writeInt(custom!=null?custom.hashCode():0);
    buffer.writeInt(custom_tx!=null?custom_tx.hashCode():0);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    for(int i=0;i<eul.length;i++) buffer.writeFloat(eul[i]);
    for(int i=0;i<quat.length;i++) buffer.writeFloat(quat[i]);
    for(int i=0;i<rotAxis.length;i++) buffer.writeFloat(rotAxis[i]);
    buffer.writeFloat(rotAngle);
    buffer.writeShort(rotmode);
    buffer.writeShort(pad);
    for(int i=0; i<chan_mat.length; i++)  for(int j=0;j<chan_mat[i].length;j++) buffer.writeFloat(chan_mat[i][j]);
    for(int i=0; i<pose_mat.length; i++)  for(int j=0;j<pose_mat[i].length;j++) buffer.writeFloat(pose_mat[i][j]);
    for(int i=0; i<constinv.length; i++)  for(int j=0;j<constinv[i].length;j++) buffer.writeFloat(constinv[i][j]);
    for(int i=0;i<pose_head.length;i++) buffer.writeFloat(pose_head[i]);
    for(int i=0;i<pose_tail.length;i++) buffer.writeFloat(pose_tail[i]);
    for(int i=0;i<limitmin.length;i++) buffer.writeFloat(limitmin[i]);
    for(int i=0;i<limitmax.length;i++) buffer.writeFloat(limitmax[i]);
    for(int i=0;i<stiffness.length;i++) buffer.writeFloat(stiffness[i]);
    buffer.writeFloat(ikstretch);
    buffer.writeFloat(ikrotweight);
    buffer.writeFloat(iklinweight);
    buffer.writeInt(path!=null?path.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bPoseChannel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPoseChannel:\n");
    sb.append("  prop: ").append(prop).append("\n");
    sb.append("  constraints: ").append(constraints).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  constflag: ").append(constflag).append("\n");
    sb.append("  ikflag: ").append(ikflag).append("\n");
    sb.append("  selectflag: ").append(selectflag).append("\n");
    sb.append("  protectflag: ").append(protectflag).append("\n");
    sb.append("  agrp_index: ").append(agrp_index).append("\n");
    sb.append("  pathlen: ").append(pathlen).append("\n");
    sb.append("  pathsf: ").append(pathsf).append("\n");
    sb.append("  pathef: ").append(pathef).append("\n");
    sb.append("  bone: ").append(bone).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  child: ").append(child).append("\n");
    sb.append("  iktree: ").append(iktree).append("\n");
    sb.append("  mpath: ").append(mpath).append("\n");
    sb.append("  custom: ").append(custom).append("\n");
    sb.append("  custom_tx: ").append(custom_tx).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  eul: ").append(Arrays.toString(eul)).append("\n");
    sb.append("  quat: ").append(Arrays.toString(quat)).append("\n");
    sb.append("  rotAxis: ").append(Arrays.toString(rotAxis)).append("\n");
    sb.append("  rotAngle: ").append(rotAngle).append("\n");
    sb.append("  rotmode: ").append(rotmode).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  chan_mat: ").append(Arrays.toString(chan_mat)).append("\n");
    sb.append("  pose_mat: ").append(Arrays.toString(pose_mat)).append("\n");
    sb.append("  constinv: ").append(Arrays.toString(constinv)).append("\n");
    sb.append("  pose_head: ").append(Arrays.toString(pose_head)).append("\n");
    sb.append("  pose_tail: ").append(Arrays.toString(pose_tail)).append("\n");
    sb.append("  limitmin: ").append(Arrays.toString(limitmin)).append("\n");
    sb.append("  limitmax: ").append(Arrays.toString(limitmax)).append("\n");
    sb.append("  stiffness: ").append(Arrays.toString(stiffness)).append("\n");
    sb.append("  ikstretch: ").append(ikstretch).append("\n");
    sb.append("  ikrotweight: ").append(ikrotweight).append("\n");
    sb.append("  iklinweight: ").append(iklinweight).append("\n");
    sb.append("  path: ").append(path).append("\n");
    return sb.toString();
  }
  public bPoseChannel copy() { try {return (bPoseChannel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

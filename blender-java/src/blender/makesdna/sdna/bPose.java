package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bPose implements DNA, Cloneable { // #267
  public bPose[] myarray;
  public ListBase chanbase = new ListBase(); // 16
  public Object chanhash; // ptr (GHash) 0
  public short flag; // 2
  public short pad; // 2
  public int proxy_layer; // 4
  public int pad1; // 4
  public float ctime; // 4
  public float[] stride_offset = new float[3]; // 4
  public float[] cyclic_offset = new float[3]; // 4
  public ListBase agroups = new ListBase(); // 16
  public int active_group; // 4
  public int iksolver; // 4
  public Object ikdata; // ptr 0
  public Object ikparam; // ptr 0
  public bAnimVizSettings avs = new bAnimVizSettings(); // 48
  public byte[] proxy_act_bone = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    chanbase.read(buffer);
    chanhash = DNATools.ptr(buffer); // get ptr
    flag = buffer.getShort();
    pad = buffer.getShort();
    proxy_layer = buffer.getInt();
    pad1 = buffer.getInt();
    ctime = buffer.getFloat();
    for(int i=0;i<stride_offset.length;i++) stride_offset[i]=buffer.getFloat();
    for(int i=0;i<cyclic_offset.length;i++) cyclic_offset[i]=buffer.getFloat();
    agroups.read(buffer);
    active_group = buffer.getInt();
    iksolver = buffer.getInt();
    ikdata = DNATools.ptr(buffer); // get ptr
    ikparam = DNATools.ptr(buffer); // get ptr
    avs.read(buffer);
    buffer.get(proxy_act_bone);
  }
  public void write(DataOutput buffer) throws IOException {
    chanbase.write(buffer);
    buffer.writeInt(chanhash!=null?chanhash.hashCode():0);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
    buffer.writeInt(proxy_layer);
    buffer.writeInt(pad1);
    buffer.writeFloat(ctime);
    for(int i=0;i<stride_offset.length;i++) buffer.writeFloat(stride_offset[i]);
    for(int i=0;i<cyclic_offset.length;i++) buffer.writeFloat(cyclic_offset[i]);
    agroups.write(buffer);
    buffer.writeInt(active_group);
    buffer.writeInt(iksolver);
    buffer.writeInt(ikdata!=null?ikdata.hashCode():0);
    buffer.writeInt(ikparam!=null?ikparam.hashCode():0);
    avs.write(buffer);
    buffer.write(proxy_act_bone);
  }
  public Object setmyarray(Object array) {
    myarray = (bPose[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bPose:\n");
    sb.append("  chanbase: ").append(chanbase).append("\n");
    sb.append("  chanhash: ").append(chanhash).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  proxy_layer: ").append(proxy_layer).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  ctime: ").append(ctime).append("\n");
    sb.append("  stride_offset: ").append(Arrays.toString(stride_offset)).append("\n");
    sb.append("  cyclic_offset: ").append(Arrays.toString(cyclic_offset)).append("\n");
    sb.append("  agroups: ").append(agroups).append("\n");
    sb.append("  active_group: ").append(active_group).append("\n");
    sb.append("  iksolver: ").append(iksolver).append("\n");
    sb.append("  ikdata: ").append(ikdata).append("\n");
    sb.append("  ikparam: ").append(ikparam).append("\n");
    sb.append("  avs: ").append(avs).append("\n");
    sb.append("  proxy_act_bone: ").append(new String(proxy_act_bone)).append("\n");
    return sb.toString();
  }
  public bPose copy() { try {return (bPose)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

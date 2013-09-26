package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Bone extends Link<Bone> implements DNA, Cloneable { // #261
  public Bone[] myarray;
  public IDProperty prop; // ptr 96
  public Bone parent; // ptr 296
  public ListBase childbase = new ListBase(); // 16
  public byte[] name = new byte[32]; // 1
  public float roll; // 4
  public float[] head = new float[3]; // 4
  public float[] tail = new float[3]; // 4
  public float[][] bone_mat = new float[3][3]; // 4
  public int flag; // 4
  public float[] arm_head = new float[3]; // 4
  public float[] arm_tail = new float[3]; // 4
  public float[][] arm_mat = new float[4][4]; // 4
  public float arm_roll; // 4
  public float dist; // 4
  public float weight; // 4
  public float xwidth; // 4
  public float length; // 4
  public float zwidth; // 4
  public float ease1; // 4
  public float ease2; // 4
  public float rad_head; // 4
  public float rad_tail; // 4
  public float[] size = new float[3]; // 4
  public int layer; // 4
  public short segments; // 2
  public short[] pad = new short[1]; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Bone.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Bone.class); // get ptr
    prop = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    parent = DNATools.link(DNATools.ptr(buffer), Bone.class); // get ptr
    childbase.read(buffer);
    buffer.get(name);
    roll = buffer.getFloat();
    for(int i=0;i<head.length;i++) head[i]=buffer.getFloat();
    for(int i=0;i<tail.length;i++) tail[i]=buffer.getFloat();
    for(int i=0;i<bone_mat.length;i++) for(int j=0;j<bone_mat[i].length;j++) bone_mat[i][j]=buffer.getFloat();
    flag = buffer.getInt();
    for(int i=0;i<arm_head.length;i++) arm_head[i]=buffer.getFloat();
    for(int i=0;i<arm_tail.length;i++) arm_tail[i]=buffer.getFloat();
    for(int i=0;i<arm_mat.length;i++) for(int j=0;j<arm_mat[i].length;j++) arm_mat[i][j]=buffer.getFloat();
    arm_roll = buffer.getFloat();
    dist = buffer.getFloat();
    weight = buffer.getFloat();
    xwidth = buffer.getFloat();
    length = buffer.getFloat();
    zwidth = buffer.getFloat();
    ease1 = buffer.getFloat();
    ease2 = buffer.getFloat();
    rad_head = buffer.getFloat();
    rad_tail = buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    layer = buffer.getInt();
    segments = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(prop!=null?prop.hashCode():0);
    buffer.writeInt(parent!=null?parent.hashCode():0);
    childbase.write(buffer);
    buffer.write(name);
    buffer.writeFloat(roll);
    for(int i=0;i<head.length;i++) buffer.writeFloat(head[i]);
    for(int i=0;i<tail.length;i++) buffer.writeFloat(tail[i]);
    for(int i=0; i<bone_mat.length; i++)  for(int j=0;j<bone_mat[i].length;j++) buffer.writeFloat(bone_mat[i][j]);
    buffer.writeInt(flag);
    for(int i=0;i<arm_head.length;i++) buffer.writeFloat(arm_head[i]);
    for(int i=0;i<arm_tail.length;i++) buffer.writeFloat(arm_tail[i]);
    for(int i=0; i<arm_mat.length; i++)  for(int j=0;j<arm_mat[i].length;j++) buffer.writeFloat(arm_mat[i][j]);
    buffer.writeFloat(arm_roll);
    buffer.writeFloat(dist);
    buffer.writeFloat(weight);
    buffer.writeFloat(xwidth);
    buffer.writeFloat(length);
    buffer.writeFloat(zwidth);
    buffer.writeFloat(ease1);
    buffer.writeFloat(ease2);
    buffer.writeFloat(rad_head);
    buffer.writeFloat(rad_tail);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    buffer.writeInt(layer);
    buffer.writeShort(segments);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (Bone[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Bone:\n");
    sb.append("  prop: ").append(prop).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  childbase: ").append(childbase).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  roll: ").append(roll).append("\n");
    sb.append("  head: ").append(Arrays.toString(head)).append("\n");
    sb.append("  tail: ").append(Arrays.toString(tail)).append("\n");
    sb.append("  bone_mat: ").append(Arrays.toString(bone_mat)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  arm_head: ").append(Arrays.toString(arm_head)).append("\n");
    sb.append("  arm_tail: ").append(Arrays.toString(arm_tail)).append("\n");
    sb.append("  arm_mat: ").append(Arrays.toString(arm_mat)).append("\n");
    sb.append("  arm_roll: ").append(arm_roll).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  xwidth: ").append(xwidth).append("\n");
    sb.append("  length: ").append(length).append("\n");
    sb.append("  zwidth: ").append(zwidth).append("\n");
    sb.append("  ease1: ").append(ease1).append("\n");
    sb.append("  ease2: ").append(ease2).append("\n");
    sb.append("  rad_head: ").append(rad_head).append("\n");
    sb.append("  rad_tail: ").append(rad_tail).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  layer: ").append(layer).append("\n");
    sb.append("  segments: ").append(segments).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    return sb.toString();
  }
  public Bone copy() { try {return (Bone)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

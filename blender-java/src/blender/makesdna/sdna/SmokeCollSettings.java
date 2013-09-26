package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SmokeCollSettings implements DNA, Cloneable { // #400
  public SmokeCollSettings[] myarray;
  public SmokeModifierData smd; // ptr 112
  public Object bvhtree; // ptr (BVHTree) 0
  public Object dm; // ptr (DerivedMesh) 0
  public Object points; // ptr 4
  public Object points_old; // ptr 4
  public Object vel; // ptr 4
  public float[][] mat = new float[4][4]; // 4
  public float[][] mat_old = new float[4][4]; // 4
  public int numpoints; // 4
  public int numverts; // 4
  public short type; // 2
  public short pad; // 2
  public int pad2; // 4

  public void read(ByteBuffer buffer) {
    smd = DNATools.link(DNATools.ptr(buffer), SmokeModifierData.class); // get ptr
    bvhtree = DNATools.ptr(buffer); // get ptr
    dm = DNATools.ptr(buffer); // get ptr
    points = DNATools.ptr(buffer); // get ptr
    points_old = DNATools.ptr(buffer); // get ptr
    vel = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<mat.length;i++) for(int j=0;j<mat[i].length;j++) mat[i][j]=buffer.getFloat();
    for(int i=0;i<mat_old.length;i++) for(int j=0;j<mat_old[i].length;j++) mat_old[i][j]=buffer.getFloat();
    numpoints = buffer.getInt();
    numverts = buffer.getInt();
    type = buffer.getShort();
    pad = buffer.getShort();
    pad2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(smd!=null?smd.hashCode():0);
    buffer.writeInt(bvhtree!=null?bvhtree.hashCode():0);
    buffer.writeInt(dm!=null?dm.hashCode():0);
    buffer.writeInt(points!=null?points.hashCode():0);
    buffer.writeInt(points_old!=null?points_old.hashCode():0);
    buffer.writeInt(vel!=null?vel.hashCode():0);
    for(int i=0; i<mat.length; i++)  for(int j=0;j<mat[i].length;j++) buffer.writeFloat(mat[i][j]);
    for(int i=0; i<mat_old.length; i++)  for(int j=0;j<mat_old[i].length;j++) buffer.writeFloat(mat_old[i][j]);
    buffer.writeInt(numpoints);
    buffer.writeInt(numverts);
    buffer.writeShort(type);
    buffer.writeShort(pad);
    buffer.writeInt(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (SmokeCollSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SmokeCollSettings:\n");
    sb.append("  smd: ").append(smd).append("\n");
    sb.append("  bvhtree: ").append(bvhtree).append("\n");
    sb.append("  dm: ").append(dm).append("\n");
    sb.append("  points: ").append(points).append("\n");
    sb.append("  points_old: ").append(points_old).append("\n");
    sb.append("  vel: ").append(vel).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  mat_old: ").append(Arrays.toString(mat_old)).append("\n");
    sb.append("  numpoints: ").append(numpoints).append("\n");
    sb.append("  numverts: ").append(numverts).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public SmokeCollSettings copy() { try {return (SmokeCollSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

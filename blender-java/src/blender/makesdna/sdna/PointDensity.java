package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PointDensity implements DNA, Cloneable { // #29
  public PointDensity[] myarray;
  public short flag; // 2
  public short falloff_type; // 2
  public float falloff_softness; // 4
  public float radius; // 4
  public short source; // 2
  public short color_source; // 2
  public int totpoints; // 4
  public int pdpad; // 4
  public bObject object; // ptr 1296
  public int psys; // 4
  public short psys_cache_space; // 2
  public short ob_cache_space; // 2
  public Object point_tree; // ptr 0
  public Object point_data; // ptr 4
  public float noise_size; // 4
  public short noise_depth; // 2
  public short noise_influence; // 2
  public short noise_basis; // 2
  public short[] pdpad3 = new short[3]; // 2
  public float noise_fac; // 4
  public float speed_scale; // 4
  public ColorBand coba; // ptr 776

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    falloff_type = buffer.getShort();
    falloff_softness = buffer.getFloat();
    radius = buffer.getFloat();
    source = buffer.getShort();
    color_source = buffer.getShort();
    totpoints = buffer.getInt();
    pdpad = buffer.getInt();
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    psys = buffer.getInt();
    psys_cache_space = buffer.getShort();
    ob_cache_space = buffer.getShort();
    point_tree = DNATools.ptr(buffer); // get ptr
    point_data = DNATools.ptr(buffer); // get ptr
    noise_size = buffer.getFloat();
    noise_depth = buffer.getShort();
    noise_influence = buffer.getShort();
    noise_basis = buffer.getShort();
    for(int i=0;i<pdpad3.length;i++) pdpad3[i]=buffer.getShort();
    noise_fac = buffer.getFloat();
    speed_scale = buffer.getFloat();
    coba = DNATools.link(DNATools.ptr(buffer), ColorBand.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(falloff_type);
    buffer.writeFloat(falloff_softness);
    buffer.writeFloat(radius);
    buffer.writeShort(source);
    buffer.writeShort(color_source);
    buffer.writeInt(totpoints);
    buffer.writeInt(pdpad);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeInt(psys);
    buffer.writeShort(psys_cache_space);
    buffer.writeShort(ob_cache_space);
    buffer.writeInt(point_tree!=null?point_tree.hashCode():0);
    buffer.writeInt(point_data!=null?point_data.hashCode():0);
    buffer.writeFloat(noise_size);
    buffer.writeShort(noise_depth);
    buffer.writeShort(noise_influence);
    buffer.writeShort(noise_basis);
    for(int i=0;i<pdpad3.length;i++) buffer.writeShort(pdpad3[i]);
    buffer.writeFloat(noise_fac);
    buffer.writeFloat(speed_scale);
    buffer.writeInt(coba!=null?coba.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PointDensity[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PointDensity:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  falloff_type: ").append(falloff_type).append("\n");
    sb.append("  falloff_softness: ").append(falloff_softness).append("\n");
    sb.append("  radius: ").append(radius).append("\n");
    sb.append("  source: ").append(source).append("\n");
    sb.append("  color_source: ").append(color_source).append("\n");
    sb.append("  totpoints: ").append(totpoints).append("\n");
    sb.append("  pdpad: ").append(pdpad).append("\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  psys: ").append(psys).append("\n");
    sb.append("  psys_cache_space: ").append(psys_cache_space).append("\n");
    sb.append("  ob_cache_space: ").append(ob_cache_space).append("\n");
    sb.append("  point_tree: ").append(point_tree).append("\n");
    sb.append("  point_data: ").append(point_data).append("\n");
    sb.append("  noise_size: ").append(noise_size).append("\n");
    sb.append("  noise_depth: ").append(noise_depth).append("\n");
    sb.append("  noise_influence: ").append(noise_influence).append("\n");
    sb.append("  noise_basis: ").append(noise_basis).append("\n");
    sb.append("  pdpad3: ").append(Arrays.toString(pdpad3)).append("\n");
    sb.append("  noise_fac: ").append(noise_fac).append("\n");
    sb.append("  speed_scale: ").append(speed_scale).append("\n");
    sb.append("  coba: ").append(coba).append("\n");
    return sb.toString();
  }
  public PointDensity copy() { try {return (PointDensity)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

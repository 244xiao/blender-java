package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PartDeflect implements DNA, Cloneable { // #117
  public PartDeflect[] myarray;
  public int flag; // 4
  public short deflect; // 2
  public short forcefield; // 2
  public short falloff; // 2
  public short shape; // 2
  public short tex_mode; // 2
  public short kink; // 2
  public short kink_axis; // 2
  public short zdir; // 2
  public float f_strength; // 4
  public float f_damp; // 4
  public float f_flow; // 4
  public float f_size; // 4
  public float f_power; // 4
  public float maxdist; // 4
  public float mindist; // 4
  public float f_power_r; // 4
  public float maxrad; // 4
  public float minrad; // 4
  public float pdef_damp; // 4
  public float pdef_rdamp; // 4
  public float pdef_perm; // 4
  public float pdef_frict; // 4
  public float pdef_rfrict; // 4
  public float pdef_stickness; // 4
  public float absorption; // 4
  public float pdef_sbdamp; // 4
  public float pdef_sbift; // 4
  public float pdef_sboft; // 4
  public float clump_fac; // 4
  public float clump_pow; // 4
  public float kink_freq; // 4
  public float kink_shape; // 4
  public float kink_amp; // 4
  public float free_end; // 4
  public float tex_nabla; // 4
  public Tex tex; // ptr 368
  public Object rng; // ptr (RNG) 0
  public float f_noise; // 4
  public int seed; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    deflect = buffer.getShort();
    forcefield = buffer.getShort();
    falloff = buffer.getShort();
    shape = buffer.getShort();
    tex_mode = buffer.getShort();
    kink = buffer.getShort();
    kink_axis = buffer.getShort();
    zdir = buffer.getShort();
    f_strength = buffer.getFloat();
    f_damp = buffer.getFloat();
    f_flow = buffer.getFloat();
    f_size = buffer.getFloat();
    f_power = buffer.getFloat();
    maxdist = buffer.getFloat();
    mindist = buffer.getFloat();
    f_power_r = buffer.getFloat();
    maxrad = buffer.getFloat();
    minrad = buffer.getFloat();
    pdef_damp = buffer.getFloat();
    pdef_rdamp = buffer.getFloat();
    pdef_perm = buffer.getFloat();
    pdef_frict = buffer.getFloat();
    pdef_rfrict = buffer.getFloat();
    pdef_stickness = buffer.getFloat();
    absorption = buffer.getFloat();
    pdef_sbdamp = buffer.getFloat();
    pdef_sbift = buffer.getFloat();
    pdef_sboft = buffer.getFloat();
    clump_fac = buffer.getFloat();
    clump_pow = buffer.getFloat();
    kink_freq = buffer.getFloat();
    kink_shape = buffer.getFloat();
    kink_amp = buffer.getFloat();
    free_end = buffer.getFloat();
    tex_nabla = buffer.getFloat();
    tex = DNATools.link(DNATools.ptr(buffer), Tex.class); // get ptr
    rng = DNATools.ptr(buffer); // get ptr
    f_noise = buffer.getFloat();
    seed = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeShort(deflect);
    buffer.writeShort(forcefield);
    buffer.writeShort(falloff);
    buffer.writeShort(shape);
    buffer.writeShort(tex_mode);
    buffer.writeShort(kink);
    buffer.writeShort(kink_axis);
    buffer.writeShort(zdir);
    buffer.writeFloat(f_strength);
    buffer.writeFloat(f_damp);
    buffer.writeFloat(f_flow);
    buffer.writeFloat(f_size);
    buffer.writeFloat(f_power);
    buffer.writeFloat(maxdist);
    buffer.writeFloat(mindist);
    buffer.writeFloat(f_power_r);
    buffer.writeFloat(maxrad);
    buffer.writeFloat(minrad);
    buffer.writeFloat(pdef_damp);
    buffer.writeFloat(pdef_rdamp);
    buffer.writeFloat(pdef_perm);
    buffer.writeFloat(pdef_frict);
    buffer.writeFloat(pdef_rfrict);
    buffer.writeFloat(pdef_stickness);
    buffer.writeFloat(absorption);
    buffer.writeFloat(pdef_sbdamp);
    buffer.writeFloat(pdef_sbift);
    buffer.writeFloat(pdef_sboft);
    buffer.writeFloat(clump_fac);
    buffer.writeFloat(clump_pow);
    buffer.writeFloat(kink_freq);
    buffer.writeFloat(kink_shape);
    buffer.writeFloat(kink_amp);
    buffer.writeFloat(free_end);
    buffer.writeFloat(tex_nabla);
    buffer.writeInt(tex!=null?tex.hashCode():0);
    buffer.writeInt(rng!=null?rng.hashCode():0);
    buffer.writeFloat(f_noise);
    buffer.writeInt(seed);
  }
  public Object setmyarray(Object array) {
    myarray = (PartDeflect[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PartDeflect:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  deflect: ").append(deflect).append("\n");
    sb.append("  forcefield: ").append(forcefield).append("\n");
    sb.append("  falloff: ").append(falloff).append("\n");
    sb.append("  shape: ").append(shape).append("\n");
    sb.append("  tex_mode: ").append(tex_mode).append("\n");
    sb.append("  kink: ").append(kink).append("\n");
    sb.append("  kink_axis: ").append(kink_axis).append("\n");
    sb.append("  zdir: ").append(zdir).append("\n");
    sb.append("  f_strength: ").append(f_strength).append("\n");
    sb.append("  f_damp: ").append(f_damp).append("\n");
    sb.append("  f_flow: ").append(f_flow).append("\n");
    sb.append("  f_size: ").append(f_size).append("\n");
    sb.append("  f_power: ").append(f_power).append("\n");
    sb.append("  maxdist: ").append(maxdist).append("\n");
    sb.append("  mindist: ").append(mindist).append("\n");
    sb.append("  f_power_r: ").append(f_power_r).append("\n");
    sb.append("  maxrad: ").append(maxrad).append("\n");
    sb.append("  minrad: ").append(minrad).append("\n");
    sb.append("  pdef_damp: ").append(pdef_damp).append("\n");
    sb.append("  pdef_rdamp: ").append(pdef_rdamp).append("\n");
    sb.append("  pdef_perm: ").append(pdef_perm).append("\n");
    sb.append("  pdef_frict: ").append(pdef_frict).append("\n");
    sb.append("  pdef_rfrict: ").append(pdef_rfrict).append("\n");
    sb.append("  pdef_stickness: ").append(pdef_stickness).append("\n");
    sb.append("  absorption: ").append(absorption).append("\n");
    sb.append("  pdef_sbdamp: ").append(pdef_sbdamp).append("\n");
    sb.append("  pdef_sbift: ").append(pdef_sbift).append("\n");
    sb.append("  pdef_sboft: ").append(pdef_sboft).append("\n");
    sb.append("  clump_fac: ").append(clump_fac).append("\n");
    sb.append("  clump_pow: ").append(clump_pow).append("\n");
    sb.append("  kink_freq: ").append(kink_freq).append("\n");
    sb.append("  kink_shape: ").append(kink_shape).append("\n");
    sb.append("  kink_amp: ").append(kink_amp).append("\n");
    sb.append("  free_end: ").append(free_end).append("\n");
    sb.append("  tex_nabla: ").append(tex_nabla).append("\n");
    sb.append("  tex: ").append(tex).append("\n");
    sb.append("  rng: ").append(rng).append("\n");
    sb.append("  f_noise: ").append(f_noise).append("\n");
    sb.append("  seed: ").append(seed).append("\n");
    return sb.toString();
  }
  public PartDeflect copy() { try {return (PartDeflect)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

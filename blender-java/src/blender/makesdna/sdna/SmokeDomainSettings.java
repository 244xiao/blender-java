package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SmokeDomainSettings implements DNA, Cloneable { // #398
  public SmokeDomainSettings[] myarray;
  public SmokeModifierData smd; // ptr 112
  public Object fluid; // ptr (FLUID_3D) 0
  public Group fluid_group; // ptr 104
  public Group eff_group; // ptr 104
  public Group coll_group; // ptr 104
  public Object wt; // ptr (WTURBULENCE) 0
  public Object tex; // ptr (GPUTexture) 0
  public Object tex_wt; // ptr (GPUTexture) 0
  public Object tex_shadow; // ptr (GPUTexture) 0
  public Object shadow; // ptr 4
  public float[] p0 = new float[3]; // 4
  public float[] p1 = new float[3]; // 4
  public float dx; // 4
  public float omega; // 4
  public float temp; // 4
  public float tempAmb; // 4
  public float alpha; // 4
  public float beta; // 4
  public int[] res = new int[3]; // 4
  public int amplify; // 4
  public int maxres; // 4
  public int flags; // 4
  public int pad; // 4
  public int viewsettings; // 4
  public short noise; // 2
  public short diss_percent; // 2
  public int diss_speed; // 4
  public float strength; // 4
  public int[] res_wt = new int[3]; // 4
  public float dx_wt; // 4
  public int v3dnum; // 4
  public int cache_comp; // 4
  public int cache_high_comp; // 4
  public PointCache[] point_cache = new PointCache[2]; // ptr 528
  public ListBase[] ptcaches = new ListBase[2]; // 16
  public EffectorWeights effector_weights; // ptr 72
  public int border_collisions; // 4
  public float time_scale; // 4
  public float vorticity; // 4
  public int pad2; // 4

  public void read(ByteBuffer buffer) {
    smd = DNATools.link(DNATools.ptr(buffer), SmokeModifierData.class); // get ptr
    fluid = DNATools.ptr(buffer); // get ptr
    fluid_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    eff_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    coll_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    wt = DNATools.ptr(buffer); // get ptr
    tex = DNATools.ptr(buffer); // get ptr
    tex_wt = DNATools.ptr(buffer); // get ptr
    tex_shadow = DNATools.ptr(buffer); // get ptr
    shadow = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<p0.length;i++) p0[i]=buffer.getFloat();
    for(int i=0;i<p1.length;i++) p1[i]=buffer.getFloat();
    dx = buffer.getFloat();
    omega = buffer.getFloat();
    temp = buffer.getFloat();
    tempAmb = buffer.getFloat();
    alpha = buffer.getFloat();
    beta = buffer.getFloat();
    for(int i=0;i<res.length;i++) res[i]=buffer.getInt();
    amplify = buffer.getInt();
    maxres = buffer.getInt();
    flags = buffer.getInt();
    pad = buffer.getInt();
    viewsettings = buffer.getInt();
    noise = buffer.getShort();
    diss_percent = buffer.getShort();
    diss_speed = buffer.getInt();
    strength = buffer.getFloat();
    for(int i=0;i<res_wt.length;i++) res_wt[i]=buffer.getInt();
    dx_wt = buffer.getFloat();
    v3dnum = buffer.getInt();
    cache_comp = buffer.getInt();
    cache_high_comp = buffer.getInt();
    for(int i=0;i<point_cache.length;i++) point_cache[i]=DNATools.link(DNATools.ptr(buffer), PointCache.class);
    for(int i=0;i<ptcaches.length;i++) { ptcaches[i]=new ListBase(); ptcaches[i].read(buffer); }
    effector_weights = DNATools.link(DNATools.ptr(buffer), EffectorWeights.class); // get ptr
    border_collisions = buffer.getInt();
    time_scale = buffer.getFloat();
    vorticity = buffer.getFloat();
    pad2 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(smd!=null?smd.hashCode():0);
    buffer.writeInt(fluid!=null?fluid.hashCode():0);
    buffer.writeInt(fluid_group!=null?fluid_group.hashCode():0);
    buffer.writeInt(eff_group!=null?eff_group.hashCode():0);
    buffer.writeInt(coll_group!=null?coll_group.hashCode():0);
    buffer.writeInt(wt!=null?wt.hashCode():0);
    buffer.writeInt(tex!=null?tex.hashCode():0);
    buffer.writeInt(tex_wt!=null?tex_wt.hashCode():0);
    buffer.writeInt(tex_shadow!=null?tex_shadow.hashCode():0);
    buffer.writeInt(shadow!=null?shadow.hashCode():0);
    for(int i=0;i<p0.length;i++) buffer.writeFloat(p0[i]);
    for(int i=0;i<p1.length;i++) buffer.writeFloat(p1[i]);
    buffer.writeFloat(dx);
    buffer.writeFloat(omega);
    buffer.writeFloat(temp);
    buffer.writeFloat(tempAmb);
    buffer.writeFloat(alpha);
    buffer.writeFloat(beta);
    for(int i=0;i<res.length;i++) buffer.writeInt(res[i]);
    buffer.writeInt(amplify);
    buffer.writeInt(maxres);
    buffer.writeInt(flags);
    buffer.writeInt(pad);
    buffer.writeInt(viewsettings);
    buffer.writeShort(noise);
    buffer.writeShort(diss_percent);
    buffer.writeInt(diss_speed);
    buffer.writeFloat(strength);
    for(int i=0;i<res_wt.length;i++) buffer.writeInt(res_wt[i]);
    buffer.writeFloat(dx_wt);
    buffer.writeInt(v3dnum);
    buffer.writeInt(cache_comp);
    buffer.writeInt(cache_high_comp);
    for(int i=0;i<point_cache.length;i++) buffer.writeInt(point_cache[i]!=null?point_cache[i].hashCode():0);
    for(int i=0;i<ptcaches.length;i++) ptcaches[i].write(buffer);
    buffer.writeInt(effector_weights!=null?effector_weights.hashCode():0);
    buffer.writeInt(border_collisions);
    buffer.writeFloat(time_scale);
    buffer.writeFloat(vorticity);
    buffer.writeInt(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (SmokeDomainSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SmokeDomainSettings:\n");
    sb.append("  smd: ").append(smd).append("\n");
    sb.append("  fluid: ").append(fluid).append("\n");
    sb.append("  fluid_group: ").append(fluid_group).append("\n");
    sb.append("  eff_group: ").append(eff_group).append("\n");
    sb.append("  coll_group: ").append(coll_group).append("\n");
    sb.append("  wt: ").append(wt).append("\n");
    sb.append("  tex: ").append(tex).append("\n");
    sb.append("  tex_wt: ").append(tex_wt).append("\n");
    sb.append("  tex_shadow: ").append(tex_shadow).append("\n");
    sb.append("  shadow: ").append(shadow).append("\n");
    sb.append("  p0: ").append(Arrays.toString(p0)).append("\n");
    sb.append("  p1: ").append(Arrays.toString(p1)).append("\n");
    sb.append("  dx: ").append(dx).append("\n");
    sb.append("  omega: ").append(omega).append("\n");
    sb.append("  temp: ").append(temp).append("\n");
    sb.append("  tempAmb: ").append(tempAmb).append("\n");
    sb.append("  alpha: ").append(alpha).append("\n");
    sb.append("  beta: ").append(beta).append("\n");
    sb.append("  res: ").append(Arrays.toString(res)).append("\n");
    sb.append("  amplify: ").append(amplify).append("\n");
    sb.append("  maxres: ").append(maxres).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  viewsettings: ").append(viewsettings).append("\n");
    sb.append("  noise: ").append(noise).append("\n");
    sb.append("  diss_percent: ").append(diss_percent).append("\n");
    sb.append("  diss_speed: ").append(diss_speed).append("\n");
    sb.append("  strength: ").append(strength).append("\n");
    sb.append("  res_wt: ").append(Arrays.toString(res_wt)).append("\n");
    sb.append("  dx_wt: ").append(dx_wt).append("\n");
    sb.append("  v3dnum: ").append(v3dnum).append("\n");
    sb.append("  cache_comp: ").append(cache_comp).append("\n");
    sb.append("  cache_high_comp: ").append(cache_high_comp).append("\n");
    sb.append("  point_cache: ").append(Arrays.toString(point_cache)).append("\n");
    sb.append("  ptcaches: ").append(Arrays.toString(ptcaches)).append("\n");
    sb.append("  effector_weights: ").append(effector_weights).append("\n");
    sb.append("  border_collisions: ").append(border_collisions).append("\n");
    sb.append("  time_scale: ").append(time_scale).append("\n");
    sb.append("  vorticity: ").append(vorticity).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public SmokeDomainSettings copy() { try {return (SmokeDomainSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

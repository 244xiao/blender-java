package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleSettings extends ID implements DNA, Cloneable { // #349
  public ParticleSettings[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public BoidSettings boids; // ptr 104
  public SPHFluidSettings fluid; // ptr 60
  public EffectorWeights effector_weights; // ptr 72
  public int flag; // 4
  public int rt; // 4
  public short type; // 2
  public short from; // 2
  public short distr; // 2
  public short texact; // 2
  public short phystype; // 2
  public short rotmode; // 2
  public short avemode; // 2
  public short reactevent; // 2
  public short draw; // 2
  public short draw_as; // 2
  public short draw_size; // 2
  public short childtype; // 2
  public short ren_as; // 2
  public short subframes; // 2
  public short draw_col; // 2
  public short draw_step; // 2
  public short ren_step; // 2
  public short hair_step; // 2
  public short keys_step; // 2
  public short adapt_angle; // 2
  public short adapt_pix; // 2
  public short disp; // 2
  public short omat; // 2
  public short interpolation; // 2
  public short rotfrom; // 2
  public short integrator; // 2
  public short kink; // 2
  public short kink_axis; // 2
  public short bb_align; // 2
  public short bb_uv_split; // 2
  public short bb_anim; // 2
  public short bb_split_offset; // 2
  public float bb_tilt; // 4
  public float bb_rand_tilt; // 4
  public float[] bb_offset = new float[2]; // 4
  public float color_vec_max; // 4
  public short simplify_flag; // 2
  public short simplify_refsize; // 2
  public float simplify_rate; // 4
  public float simplify_transition; // 4
  public float simplify_viewport; // 4
  public float sta; // 4
  public float end; // 4
  public float lifetime; // 4
  public float randlife; // 4
  public float timetweak; // 4
  public float jitfac; // 4
  public float eff_hair; // 4
  public float grid_rand; // 4
  public int totpart; // 4
  public int userjit; // 4
  public int grid_res; // 4
  public int effector_amount; // 4
  public float normfac; // 4
  public float obfac; // 4
  public float randfac; // 4
  public float partfac; // 4
  public float tanfac; // 4
  public float tanphase; // 4
  public float reactfac; // 4
  public float[] ob_vel = new float[3]; // 4
  public float avefac; // 4
  public float phasefac; // 4
  public float randrotfac; // 4
  public float randphasefac; // 4
  public float mass; // 4
  public float size; // 4
  public float randsize; // 4
  public float[] acc = new float[3]; // 4
  public float dragfac; // 4
  public float brownfac; // 4
  public float dampfac; // 4
  public float randlength; // 4
  public int child_nbr; // 4
  public int ren_child_nbr; // 4
  public float parents; // 4
  public float childsize; // 4
  public float childrandsize; // 4
  public float childrad; // 4
  public float childflat; // 4
  public float clumpfac; // 4
  public float clumppow; // 4
  public float kink_amp; // 4
  public float kink_freq; // 4
  public float kink_shape; // 4
  public float kink_flat; // 4
  public float kink_amp_clump; // 4
  public float rough1; // 4
  public float rough1_size; // 4
  public float rough2; // 4
  public float rough2_size; // 4
  public float rough2_thres; // 4
  public float rough_end; // 4
  public float rough_end_shape; // 4
  public float clength; // 4
  public float clength_thres; // 4
  public float parting_fac; // 4
  public float parting_min; // 4
  public float parting_max; // 4
  public float branch_thres; // 4
  public float[] draw_line = new float[2]; // 4
  public float path_start; // 4
  public float path_end; // 4
  public int trail_count; // 4
  public int keyed_loops; // 4
  public MTex[] mtex = new MTex[18]; // ptr 280
  public Group dup_group; // ptr 104
  public ListBase dupliweights = new ListBase(); // 16
  public Group eff_group; // ptr 104
  public bObject dup_ob; // ptr 1296
  public bObject bb_ob; // ptr 1296
  public Ipo ipo; // ptr 112
  public PartDeflect pd; // ptr 152
  public PartDeflect pd2; // ptr 152

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    boids = DNATools.link(DNATools.ptr(buffer), BoidSettings.class); // get ptr
    fluid = DNATools.link(DNATools.ptr(buffer), SPHFluidSettings.class); // get ptr
    effector_weights = DNATools.link(DNATools.ptr(buffer), EffectorWeights.class); // get ptr
    flag = buffer.getInt();
    rt = buffer.getInt();
    type = buffer.getShort();
    from = buffer.getShort();
    distr = buffer.getShort();
    texact = buffer.getShort();
    phystype = buffer.getShort();
    rotmode = buffer.getShort();
    avemode = buffer.getShort();
    reactevent = buffer.getShort();
    draw = buffer.getShort();
    draw_as = buffer.getShort();
    draw_size = buffer.getShort();
    childtype = buffer.getShort();
    ren_as = buffer.getShort();
    subframes = buffer.getShort();
    draw_col = buffer.getShort();
    draw_step = buffer.getShort();
    ren_step = buffer.getShort();
    hair_step = buffer.getShort();
    keys_step = buffer.getShort();
    adapt_angle = buffer.getShort();
    adapt_pix = buffer.getShort();
    disp = buffer.getShort();
    omat = buffer.getShort();
    interpolation = buffer.getShort();
    rotfrom = buffer.getShort();
    integrator = buffer.getShort();
    kink = buffer.getShort();
    kink_axis = buffer.getShort();
    bb_align = buffer.getShort();
    bb_uv_split = buffer.getShort();
    bb_anim = buffer.getShort();
    bb_split_offset = buffer.getShort();
    bb_tilt = buffer.getFloat();
    bb_rand_tilt = buffer.getFloat();
    for(int i=0;i<bb_offset.length;i++) bb_offset[i]=buffer.getFloat();
    color_vec_max = buffer.getFloat();
    simplify_flag = buffer.getShort();
    simplify_refsize = buffer.getShort();
    simplify_rate = buffer.getFloat();
    simplify_transition = buffer.getFloat();
    simplify_viewport = buffer.getFloat();
    sta = buffer.getFloat();
    end = buffer.getFloat();
    lifetime = buffer.getFloat();
    randlife = buffer.getFloat();
    timetweak = buffer.getFloat();
    jitfac = buffer.getFloat();
    eff_hair = buffer.getFloat();
    grid_rand = buffer.getFloat();
    totpart = buffer.getInt();
    userjit = buffer.getInt();
    grid_res = buffer.getInt();
    effector_amount = buffer.getInt();
    normfac = buffer.getFloat();
    obfac = buffer.getFloat();
    randfac = buffer.getFloat();
    partfac = buffer.getFloat();
    tanfac = buffer.getFloat();
    tanphase = buffer.getFloat();
    reactfac = buffer.getFloat();
    for(int i=0;i<ob_vel.length;i++) ob_vel[i]=buffer.getFloat();
    avefac = buffer.getFloat();
    phasefac = buffer.getFloat();
    randrotfac = buffer.getFloat();
    randphasefac = buffer.getFloat();
    mass = buffer.getFloat();
    size = buffer.getFloat();
    randsize = buffer.getFloat();
    for(int i=0;i<acc.length;i++) acc[i]=buffer.getFloat();
    dragfac = buffer.getFloat();
    brownfac = buffer.getFloat();
    dampfac = buffer.getFloat();
    randlength = buffer.getFloat();
    child_nbr = buffer.getInt();
    ren_child_nbr = buffer.getInt();
    parents = buffer.getFloat();
    childsize = buffer.getFloat();
    childrandsize = buffer.getFloat();
    childrad = buffer.getFloat();
    childflat = buffer.getFloat();
    clumpfac = buffer.getFloat();
    clumppow = buffer.getFloat();
    kink_amp = buffer.getFloat();
    kink_freq = buffer.getFloat();
    kink_shape = buffer.getFloat();
    kink_flat = buffer.getFloat();
    kink_amp_clump = buffer.getFloat();
    rough1 = buffer.getFloat();
    rough1_size = buffer.getFloat();
    rough2 = buffer.getFloat();
    rough2_size = buffer.getFloat();
    rough2_thres = buffer.getFloat();
    rough_end = buffer.getFloat();
    rough_end_shape = buffer.getFloat();
    clength = buffer.getFloat();
    clength_thres = buffer.getFloat();
    parting_fac = buffer.getFloat();
    parting_min = buffer.getFloat();
    parting_max = buffer.getFloat();
    branch_thres = buffer.getFloat();
    for(int i=0;i<draw_line.length;i++) draw_line[i]=buffer.getFloat();
    path_start = buffer.getFloat();
    path_end = buffer.getFloat();
    trail_count = buffer.getInt();
    keyed_loops = buffer.getInt();
    for(int i=0;i<mtex.length;i++) mtex[i]=DNATools.link(DNATools.ptr(buffer), MTex.class);
    dup_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    dupliweights.read(buffer);
    eff_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    dup_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    bb_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    pd = DNATools.link(DNATools.ptr(buffer), PartDeflect.class); // get ptr
    pd2 = DNATools.link(DNATools.ptr(buffer), PartDeflect.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(boids!=null?boids.hashCode():0);
    buffer.writeInt(fluid!=null?fluid.hashCode():0);
    buffer.writeInt(effector_weights!=null?effector_weights.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(rt);
    buffer.writeShort(type);
    buffer.writeShort(from);
    buffer.writeShort(distr);
    buffer.writeShort(texact);
    buffer.writeShort(phystype);
    buffer.writeShort(rotmode);
    buffer.writeShort(avemode);
    buffer.writeShort(reactevent);
    buffer.writeShort(draw);
    buffer.writeShort(draw_as);
    buffer.writeShort(draw_size);
    buffer.writeShort(childtype);
    buffer.writeShort(ren_as);
    buffer.writeShort(subframes);
    buffer.writeShort(draw_col);
    buffer.writeShort(draw_step);
    buffer.writeShort(ren_step);
    buffer.writeShort(hair_step);
    buffer.writeShort(keys_step);
    buffer.writeShort(adapt_angle);
    buffer.writeShort(adapt_pix);
    buffer.writeShort(disp);
    buffer.writeShort(omat);
    buffer.writeShort(interpolation);
    buffer.writeShort(rotfrom);
    buffer.writeShort(integrator);
    buffer.writeShort(kink);
    buffer.writeShort(kink_axis);
    buffer.writeShort(bb_align);
    buffer.writeShort(bb_uv_split);
    buffer.writeShort(bb_anim);
    buffer.writeShort(bb_split_offset);
    buffer.writeFloat(bb_tilt);
    buffer.writeFloat(bb_rand_tilt);
    for(int i=0;i<bb_offset.length;i++) buffer.writeFloat(bb_offset[i]);
    buffer.writeFloat(color_vec_max);
    buffer.writeShort(simplify_flag);
    buffer.writeShort(simplify_refsize);
    buffer.writeFloat(simplify_rate);
    buffer.writeFloat(simplify_transition);
    buffer.writeFloat(simplify_viewport);
    buffer.writeFloat(sta);
    buffer.writeFloat(end);
    buffer.writeFloat(lifetime);
    buffer.writeFloat(randlife);
    buffer.writeFloat(timetweak);
    buffer.writeFloat(jitfac);
    buffer.writeFloat(eff_hair);
    buffer.writeFloat(grid_rand);
    buffer.writeInt(totpart);
    buffer.writeInt(userjit);
    buffer.writeInt(grid_res);
    buffer.writeInt(effector_amount);
    buffer.writeFloat(normfac);
    buffer.writeFloat(obfac);
    buffer.writeFloat(randfac);
    buffer.writeFloat(partfac);
    buffer.writeFloat(tanfac);
    buffer.writeFloat(tanphase);
    buffer.writeFloat(reactfac);
    for(int i=0;i<ob_vel.length;i++) buffer.writeFloat(ob_vel[i]);
    buffer.writeFloat(avefac);
    buffer.writeFloat(phasefac);
    buffer.writeFloat(randrotfac);
    buffer.writeFloat(randphasefac);
    buffer.writeFloat(mass);
    buffer.writeFloat(size);
    buffer.writeFloat(randsize);
    for(int i=0;i<acc.length;i++) buffer.writeFloat(acc[i]);
    buffer.writeFloat(dragfac);
    buffer.writeFloat(brownfac);
    buffer.writeFloat(dampfac);
    buffer.writeFloat(randlength);
    buffer.writeInt(child_nbr);
    buffer.writeInt(ren_child_nbr);
    buffer.writeFloat(parents);
    buffer.writeFloat(childsize);
    buffer.writeFloat(childrandsize);
    buffer.writeFloat(childrad);
    buffer.writeFloat(childflat);
    buffer.writeFloat(clumpfac);
    buffer.writeFloat(clumppow);
    buffer.writeFloat(kink_amp);
    buffer.writeFloat(kink_freq);
    buffer.writeFloat(kink_shape);
    buffer.writeFloat(kink_flat);
    buffer.writeFloat(kink_amp_clump);
    buffer.writeFloat(rough1);
    buffer.writeFloat(rough1_size);
    buffer.writeFloat(rough2);
    buffer.writeFloat(rough2_size);
    buffer.writeFloat(rough2_thres);
    buffer.writeFloat(rough_end);
    buffer.writeFloat(rough_end_shape);
    buffer.writeFloat(clength);
    buffer.writeFloat(clength_thres);
    buffer.writeFloat(parting_fac);
    buffer.writeFloat(parting_min);
    buffer.writeFloat(parting_max);
    buffer.writeFloat(branch_thres);
    for(int i=0;i<draw_line.length;i++) buffer.writeFloat(draw_line[i]);
    buffer.writeFloat(path_start);
    buffer.writeFloat(path_end);
    buffer.writeInt(trail_count);
    buffer.writeInt(keyed_loops);
    for(int i=0;i<mtex.length;i++) buffer.writeInt(mtex[i]!=null?mtex[i].hashCode():0);
    buffer.writeInt(dup_group!=null?dup_group.hashCode():0);
    dupliweights.write(buffer);
    buffer.writeInt(eff_group!=null?eff_group.hashCode():0);
    buffer.writeInt(dup_ob!=null?dup_ob.hashCode():0);
    buffer.writeInt(bb_ob!=null?bb_ob.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(pd!=null?pd.hashCode():0);
    buffer.writeInt(pd2!=null?pd2.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleSettings:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  boids: ").append(boids).append("\n");
    sb.append("  fluid: ").append(fluid).append("\n");
    sb.append("  effector_weights: ").append(effector_weights).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  from: ").append(from).append("\n");
    sb.append("  distr: ").append(distr).append("\n");
    sb.append("  texact: ").append(texact).append("\n");
    sb.append("  phystype: ").append(phystype).append("\n");
    sb.append("  rotmode: ").append(rotmode).append("\n");
    sb.append("  avemode: ").append(avemode).append("\n");
    sb.append("  reactevent: ").append(reactevent).append("\n");
    sb.append("  draw: ").append(draw).append("\n");
    sb.append("  draw_as: ").append(draw_as).append("\n");
    sb.append("  draw_size: ").append(draw_size).append("\n");
    sb.append("  childtype: ").append(childtype).append("\n");
    sb.append("  ren_as: ").append(ren_as).append("\n");
    sb.append("  subframes: ").append(subframes).append("\n");
    sb.append("  draw_col: ").append(draw_col).append("\n");
    sb.append("  draw_step: ").append(draw_step).append("\n");
    sb.append("  ren_step: ").append(ren_step).append("\n");
    sb.append("  hair_step: ").append(hair_step).append("\n");
    sb.append("  keys_step: ").append(keys_step).append("\n");
    sb.append("  adapt_angle: ").append(adapt_angle).append("\n");
    sb.append("  adapt_pix: ").append(adapt_pix).append("\n");
    sb.append("  disp: ").append(disp).append("\n");
    sb.append("  omat: ").append(omat).append("\n");
    sb.append("  interpolation: ").append(interpolation).append("\n");
    sb.append("  rotfrom: ").append(rotfrom).append("\n");
    sb.append("  integrator: ").append(integrator).append("\n");
    sb.append("  kink: ").append(kink).append("\n");
    sb.append("  kink_axis: ").append(kink_axis).append("\n");
    sb.append("  bb_align: ").append(bb_align).append("\n");
    sb.append("  bb_uv_split: ").append(bb_uv_split).append("\n");
    sb.append("  bb_anim: ").append(bb_anim).append("\n");
    sb.append("  bb_split_offset: ").append(bb_split_offset).append("\n");
    sb.append("  bb_tilt: ").append(bb_tilt).append("\n");
    sb.append("  bb_rand_tilt: ").append(bb_rand_tilt).append("\n");
    sb.append("  bb_offset: ").append(Arrays.toString(bb_offset)).append("\n");
    sb.append("  color_vec_max: ").append(color_vec_max).append("\n");
    sb.append("  simplify_flag: ").append(simplify_flag).append("\n");
    sb.append("  simplify_refsize: ").append(simplify_refsize).append("\n");
    sb.append("  simplify_rate: ").append(simplify_rate).append("\n");
    sb.append("  simplify_transition: ").append(simplify_transition).append("\n");
    sb.append("  simplify_viewport: ").append(simplify_viewport).append("\n");
    sb.append("  sta: ").append(sta).append("\n");
    sb.append("  end: ").append(end).append("\n");
    sb.append("  lifetime: ").append(lifetime).append("\n");
    sb.append("  randlife: ").append(randlife).append("\n");
    sb.append("  timetweak: ").append(timetweak).append("\n");
    sb.append("  jitfac: ").append(jitfac).append("\n");
    sb.append("  eff_hair: ").append(eff_hair).append("\n");
    sb.append("  grid_rand: ").append(grid_rand).append("\n");
    sb.append("  totpart: ").append(totpart).append("\n");
    sb.append("  userjit: ").append(userjit).append("\n");
    sb.append("  grid_res: ").append(grid_res).append("\n");
    sb.append("  effector_amount: ").append(effector_amount).append("\n");
    sb.append("  normfac: ").append(normfac).append("\n");
    sb.append("  obfac: ").append(obfac).append("\n");
    sb.append("  randfac: ").append(randfac).append("\n");
    sb.append("  partfac: ").append(partfac).append("\n");
    sb.append("  tanfac: ").append(tanfac).append("\n");
    sb.append("  tanphase: ").append(tanphase).append("\n");
    sb.append("  reactfac: ").append(reactfac).append("\n");
    sb.append("  ob_vel: ").append(Arrays.toString(ob_vel)).append("\n");
    sb.append("  avefac: ").append(avefac).append("\n");
    sb.append("  phasefac: ").append(phasefac).append("\n");
    sb.append("  randrotfac: ").append(randrotfac).append("\n");
    sb.append("  randphasefac: ").append(randphasefac).append("\n");
    sb.append("  mass: ").append(mass).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  randsize: ").append(randsize).append("\n");
    sb.append("  acc: ").append(Arrays.toString(acc)).append("\n");
    sb.append("  dragfac: ").append(dragfac).append("\n");
    sb.append("  brownfac: ").append(brownfac).append("\n");
    sb.append("  dampfac: ").append(dampfac).append("\n");
    sb.append("  randlength: ").append(randlength).append("\n");
    sb.append("  child_nbr: ").append(child_nbr).append("\n");
    sb.append("  ren_child_nbr: ").append(ren_child_nbr).append("\n");
    sb.append("  parents: ").append(parents).append("\n");
    sb.append("  childsize: ").append(childsize).append("\n");
    sb.append("  childrandsize: ").append(childrandsize).append("\n");
    sb.append("  childrad: ").append(childrad).append("\n");
    sb.append("  childflat: ").append(childflat).append("\n");
    sb.append("  clumpfac: ").append(clumpfac).append("\n");
    sb.append("  clumppow: ").append(clumppow).append("\n");
    sb.append("  kink_amp: ").append(kink_amp).append("\n");
    sb.append("  kink_freq: ").append(kink_freq).append("\n");
    sb.append("  kink_shape: ").append(kink_shape).append("\n");
    sb.append("  kink_flat: ").append(kink_flat).append("\n");
    sb.append("  kink_amp_clump: ").append(kink_amp_clump).append("\n");
    sb.append("  rough1: ").append(rough1).append("\n");
    sb.append("  rough1_size: ").append(rough1_size).append("\n");
    sb.append("  rough2: ").append(rough2).append("\n");
    sb.append("  rough2_size: ").append(rough2_size).append("\n");
    sb.append("  rough2_thres: ").append(rough2_thres).append("\n");
    sb.append("  rough_end: ").append(rough_end).append("\n");
    sb.append("  rough_end_shape: ").append(rough_end_shape).append("\n");
    sb.append("  clength: ").append(clength).append("\n");
    sb.append("  clength_thres: ").append(clength_thres).append("\n");
    sb.append("  parting_fac: ").append(parting_fac).append("\n");
    sb.append("  parting_min: ").append(parting_min).append("\n");
    sb.append("  parting_max: ").append(parting_max).append("\n");
    sb.append("  branch_thres: ").append(branch_thres).append("\n");
    sb.append("  draw_line: ").append(Arrays.toString(draw_line)).append("\n");
    sb.append("  path_start: ").append(path_start).append("\n");
    sb.append("  path_end: ").append(path_end).append("\n");
    sb.append("  trail_count: ").append(trail_count).append("\n");
    sb.append("  keyed_loops: ").append(keyed_loops).append("\n");
    sb.append("  mtex: ").append(Arrays.toString(mtex)).append("\n");
    sb.append("  dup_group: ").append(dup_group).append("\n");
    sb.append("  dupliweights: ").append(dupliweights).append("\n");
    sb.append("  eff_group: ").append(eff_group).append("\n");
    sb.append("  dup_ob: ").append(dup_ob).append("\n");
    sb.append("  bb_ob: ").append(bb_ob).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  pd: ").append(pd).append("\n");
    sb.append("  pd2: ").append(pd2).append("\n");
    return sb.toString();
  }
  public ParticleSettings copy() { try {return (ParticleSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

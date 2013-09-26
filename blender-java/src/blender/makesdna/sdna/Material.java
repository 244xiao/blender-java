package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Material extends ID implements DNA, Cloneable { // #35
  public Material[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public short material_type; // 2
  public short flag; // 2
  public float r; // 4
  public float g; // 4
  public float b; // 4
  public float specr; // 4
  public float specg; // 4
  public float specb; // 4
  public float mirr; // 4
  public float mirg; // 4
  public float mirb; // 4
  public float ambr; // 4
  public float ambb; // 4
  public float ambg; // 4
  public float amb; // 4
  public float emit; // 4
  public float ang; // 4
  public float spectra; // 4
  public float ray_mirror; // 4
  public float alpha; // 4
  public float ref; // 4
  public float spec; // 4
  public float zoffs; // 4
  public float add; // 4
  public float translucency; // 4
  public VolumeSettings vol = new VolumeSettings(); // 88
  public float fresnel_mir; // 4
  public float fresnel_mir_i; // 4
  public float fresnel_tra; // 4
  public float fresnel_tra_i; // 4
  public float filter; // 4
  public float tx_limit; // 4
  public float tx_falloff; // 4
  public short ray_depth; // 2
  public short ray_depth_tra; // 2
  public short har; // 2
  public byte seed1; // 1
  public byte seed2; // 1
  public float gloss_mir; // 4
  public float gloss_tra; // 4
  public short samp_gloss_mir; // 2
  public short samp_gloss_tra; // 2
  public float adapt_thresh_mir; // 4
  public float adapt_thresh_tra; // 4
  public float aniso_gloss_mir; // 4
  public float dist_mir; // 4
  public short fadeto_mir; // 2
  public short shade_flag; // 2
  public int mode; // 4
  public int mode_l; // 4
  public short flarec; // 2
  public short starc; // 2
  public short linec; // 2
  public short ringc; // 2
  public float hasize; // 4
  public float flaresize; // 4
  public float subsize; // 4
  public float flareboost; // 4
  public float strand_sta; // 4
  public float strand_end; // 4
  public float strand_ease; // 4
  public float strand_surfnor; // 4
  public float strand_min; // 4
  public float strand_widthfade; // 4
  public byte[] strand_uvname = new byte[32]; // 1
  public float sbias; // 4
  public float lbias; // 4
  public float shad_alpha; // 4
  public int septex; // 4
  public byte rgbsel; // 1
  public byte texact; // 1
  public byte pr_type; // 1
  public byte use_nodes; // 1
  public short pr_back; // 2
  public short pr_lamp; // 2
  public short pr_texture; // 2
  public short ml_flag; // 2
  public short diff_shader; // 2
  public short spec_shader; // 2
  public float roughness; // 4
  public float refrac; // 4
  public float[] param = new float[4]; // 4
  public float rms; // 4
  public float darkness; // 4
  public short texco; // 2
  public short mapto; // 2
  public ColorBand ramp_col; // ptr 776
  public ColorBand ramp_spec; // ptr 776
  public byte rampin_col; // 1
  public byte rampin_spec; // 1
  public byte rampblend_col; // 1
  public byte rampblend_spec; // 1
  public short ramp_show; // 2
  public short pad3; // 2
  public float rampfac_col; // 4
  public float rampfac_spec; // 4
  public MTex[] mtex = new MTex[18]; // ptr 280
  public bNodeTree nodetree; // ptr 264
  public Ipo ipo; // ptr 112
  public Group group; // ptr 104
  public PreviewImage preview; // ptr 40
  public float friction; // 4
  public float fh; // 4
  public float reflect; // 4
  public float fhdist; // 4
  public float xyfrict; // 4
  public short dynamode; // 2
  public short pad2; // 2
  public float[] sss_radius = new float[3]; // 4
  public float[] sss_col = new float[3]; // 4
  public float sss_error; // 4
  public float sss_scale; // 4
  public float sss_ior; // 4
  public float sss_colfac; // 4
  public float sss_texfac; // 4
  public float sss_front; // 4
  public float sss_back; // 4
  public short sss_flag; // 2
  public short sss_preset; // 2
  public int mapto_textured; // 4
  public short shadowonly_flag; // 2
  public short pad; // 2
  public ListBase gpumaterial = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    material_type = buffer.getShort();
    flag = buffer.getShort();
    r = buffer.getFloat();
    g = buffer.getFloat();
    b = buffer.getFloat();
    specr = buffer.getFloat();
    specg = buffer.getFloat();
    specb = buffer.getFloat();
    mirr = buffer.getFloat();
    mirg = buffer.getFloat();
    mirb = buffer.getFloat();
    ambr = buffer.getFloat();
    ambb = buffer.getFloat();
    ambg = buffer.getFloat();
    amb = buffer.getFloat();
    emit = buffer.getFloat();
    ang = buffer.getFloat();
    spectra = buffer.getFloat();
    ray_mirror = buffer.getFloat();
    alpha = buffer.getFloat();
    ref = buffer.getFloat();
    spec = buffer.getFloat();
    zoffs = buffer.getFloat();
    add = buffer.getFloat();
    translucency = buffer.getFloat();
    vol.read(buffer);
    fresnel_mir = buffer.getFloat();
    fresnel_mir_i = buffer.getFloat();
    fresnel_tra = buffer.getFloat();
    fresnel_tra_i = buffer.getFloat();
    filter = buffer.getFloat();
    tx_limit = buffer.getFloat();
    tx_falloff = buffer.getFloat();
    ray_depth = buffer.getShort();
    ray_depth_tra = buffer.getShort();
    har = buffer.getShort();
    seed1 = buffer.get();
    seed2 = buffer.get();
    gloss_mir = buffer.getFloat();
    gloss_tra = buffer.getFloat();
    samp_gloss_mir = buffer.getShort();
    samp_gloss_tra = buffer.getShort();
    adapt_thresh_mir = buffer.getFloat();
    adapt_thresh_tra = buffer.getFloat();
    aniso_gloss_mir = buffer.getFloat();
    dist_mir = buffer.getFloat();
    fadeto_mir = buffer.getShort();
    shade_flag = buffer.getShort();
    mode = buffer.getInt();
    mode_l = buffer.getInt();
    flarec = buffer.getShort();
    starc = buffer.getShort();
    linec = buffer.getShort();
    ringc = buffer.getShort();
    hasize = buffer.getFloat();
    flaresize = buffer.getFloat();
    subsize = buffer.getFloat();
    flareboost = buffer.getFloat();
    strand_sta = buffer.getFloat();
    strand_end = buffer.getFloat();
    strand_ease = buffer.getFloat();
    strand_surfnor = buffer.getFloat();
    strand_min = buffer.getFloat();
    strand_widthfade = buffer.getFloat();
    buffer.get(strand_uvname);
    sbias = buffer.getFloat();
    lbias = buffer.getFloat();
    shad_alpha = buffer.getFloat();
    septex = buffer.getInt();
    rgbsel = buffer.get();
    texact = buffer.get();
    pr_type = buffer.get();
    use_nodes = buffer.get();
    pr_back = buffer.getShort();
    pr_lamp = buffer.getShort();
    pr_texture = buffer.getShort();
    ml_flag = buffer.getShort();
    diff_shader = buffer.getShort();
    spec_shader = buffer.getShort();
    roughness = buffer.getFloat();
    refrac = buffer.getFloat();
    for(int i=0;i<param.length;i++) param[i]=buffer.getFloat();
    rms = buffer.getFloat();
    darkness = buffer.getFloat();
    texco = buffer.getShort();
    mapto = buffer.getShort();
    ramp_col = DNATools.link(DNATools.ptr(buffer), ColorBand.class); // get ptr
    ramp_spec = DNATools.link(DNATools.ptr(buffer), ColorBand.class); // get ptr
    rampin_col = buffer.get();
    rampin_spec = buffer.get();
    rampblend_col = buffer.get();
    rampblend_spec = buffer.get();
    ramp_show = buffer.getShort();
    pad3 = buffer.getShort();
    rampfac_col = buffer.getFloat();
    rampfac_spec = buffer.getFloat();
    for(int i=0;i<mtex.length;i++) mtex[i]=DNATools.link(DNATools.ptr(buffer), MTex.class);
    nodetree = DNATools.link(DNATools.ptr(buffer), bNodeTree.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
    friction = buffer.getFloat();
    fh = buffer.getFloat();
    reflect = buffer.getFloat();
    fhdist = buffer.getFloat();
    xyfrict = buffer.getFloat();
    dynamode = buffer.getShort();
    pad2 = buffer.getShort();
    for(int i=0;i<sss_radius.length;i++) sss_radius[i]=buffer.getFloat();
    for(int i=0;i<sss_col.length;i++) sss_col[i]=buffer.getFloat();
    sss_error = buffer.getFloat();
    sss_scale = buffer.getFloat();
    sss_ior = buffer.getFloat();
    sss_colfac = buffer.getFloat();
    sss_texfac = buffer.getFloat();
    sss_front = buffer.getFloat();
    sss_back = buffer.getFloat();
    sss_flag = buffer.getShort();
    sss_preset = buffer.getShort();
    mapto_textured = buffer.getInt();
    shadowonly_flag = buffer.getShort();
    pad = buffer.getShort();
    gpumaterial.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeShort(material_type);
    buffer.writeShort(flag);
    buffer.writeFloat(r);
    buffer.writeFloat(g);
    buffer.writeFloat(b);
    buffer.writeFloat(specr);
    buffer.writeFloat(specg);
    buffer.writeFloat(specb);
    buffer.writeFloat(mirr);
    buffer.writeFloat(mirg);
    buffer.writeFloat(mirb);
    buffer.writeFloat(ambr);
    buffer.writeFloat(ambb);
    buffer.writeFloat(ambg);
    buffer.writeFloat(amb);
    buffer.writeFloat(emit);
    buffer.writeFloat(ang);
    buffer.writeFloat(spectra);
    buffer.writeFloat(ray_mirror);
    buffer.writeFloat(alpha);
    buffer.writeFloat(ref);
    buffer.writeFloat(spec);
    buffer.writeFloat(zoffs);
    buffer.writeFloat(add);
    buffer.writeFloat(translucency);
    vol.write(buffer);
    buffer.writeFloat(fresnel_mir);
    buffer.writeFloat(fresnel_mir_i);
    buffer.writeFloat(fresnel_tra);
    buffer.writeFloat(fresnel_tra_i);
    buffer.writeFloat(filter);
    buffer.writeFloat(tx_limit);
    buffer.writeFloat(tx_falloff);
    buffer.writeShort(ray_depth);
    buffer.writeShort(ray_depth_tra);
    buffer.writeShort(har);
    buffer.writeByte(seed1);
    buffer.writeByte(seed2);
    buffer.writeFloat(gloss_mir);
    buffer.writeFloat(gloss_tra);
    buffer.writeShort(samp_gloss_mir);
    buffer.writeShort(samp_gloss_tra);
    buffer.writeFloat(adapt_thresh_mir);
    buffer.writeFloat(adapt_thresh_tra);
    buffer.writeFloat(aniso_gloss_mir);
    buffer.writeFloat(dist_mir);
    buffer.writeShort(fadeto_mir);
    buffer.writeShort(shade_flag);
    buffer.writeInt(mode);
    buffer.writeInt(mode_l);
    buffer.writeShort(flarec);
    buffer.writeShort(starc);
    buffer.writeShort(linec);
    buffer.writeShort(ringc);
    buffer.writeFloat(hasize);
    buffer.writeFloat(flaresize);
    buffer.writeFloat(subsize);
    buffer.writeFloat(flareboost);
    buffer.writeFloat(strand_sta);
    buffer.writeFloat(strand_end);
    buffer.writeFloat(strand_ease);
    buffer.writeFloat(strand_surfnor);
    buffer.writeFloat(strand_min);
    buffer.writeFloat(strand_widthfade);
    buffer.write(strand_uvname);
    buffer.writeFloat(sbias);
    buffer.writeFloat(lbias);
    buffer.writeFloat(shad_alpha);
    buffer.writeInt(septex);
    buffer.writeByte(rgbsel);
    buffer.writeByte(texact);
    buffer.writeByte(pr_type);
    buffer.writeByte(use_nodes);
    buffer.writeShort(pr_back);
    buffer.writeShort(pr_lamp);
    buffer.writeShort(pr_texture);
    buffer.writeShort(ml_flag);
    buffer.writeShort(diff_shader);
    buffer.writeShort(spec_shader);
    buffer.writeFloat(roughness);
    buffer.writeFloat(refrac);
    for(int i=0;i<param.length;i++) buffer.writeFloat(param[i]);
    buffer.writeFloat(rms);
    buffer.writeFloat(darkness);
    buffer.writeShort(texco);
    buffer.writeShort(mapto);
    buffer.writeInt(ramp_col!=null?ramp_col.hashCode():0);
    buffer.writeInt(ramp_spec!=null?ramp_spec.hashCode():0);
    buffer.writeByte(rampin_col);
    buffer.writeByte(rampin_spec);
    buffer.writeByte(rampblend_col);
    buffer.writeByte(rampblend_spec);
    buffer.writeShort(ramp_show);
    buffer.writeShort(pad3);
    buffer.writeFloat(rampfac_col);
    buffer.writeFloat(rampfac_spec);
    for(int i=0;i<mtex.length;i++) buffer.writeInt(mtex[i]!=null?mtex[i].hashCode():0);
    buffer.writeInt(nodetree!=null?nodetree.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(group!=null?group.hashCode():0);
    buffer.writeInt(preview!=null?preview.hashCode():0);
    buffer.writeFloat(friction);
    buffer.writeFloat(fh);
    buffer.writeFloat(reflect);
    buffer.writeFloat(fhdist);
    buffer.writeFloat(xyfrict);
    buffer.writeShort(dynamode);
    buffer.writeShort(pad2);
    for(int i=0;i<sss_radius.length;i++) buffer.writeFloat(sss_radius[i]);
    for(int i=0;i<sss_col.length;i++) buffer.writeFloat(sss_col[i]);
    buffer.writeFloat(sss_error);
    buffer.writeFloat(sss_scale);
    buffer.writeFloat(sss_ior);
    buffer.writeFloat(sss_colfac);
    buffer.writeFloat(sss_texfac);
    buffer.writeFloat(sss_front);
    buffer.writeFloat(sss_back);
    buffer.writeShort(sss_flag);
    buffer.writeShort(sss_preset);
    buffer.writeInt(mapto_textured);
    buffer.writeShort(shadowonly_flag);
    buffer.writeShort(pad);
    gpumaterial.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (Material[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Material:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  material_type: ").append(material_type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    sb.append("  specr: ").append(specr).append("\n");
    sb.append("  specg: ").append(specg).append("\n");
    sb.append("  specb: ").append(specb).append("\n");
    sb.append("  mirr: ").append(mirr).append("\n");
    sb.append("  mirg: ").append(mirg).append("\n");
    sb.append("  mirb: ").append(mirb).append("\n");
    sb.append("  ambr: ").append(ambr).append("\n");
    sb.append("  ambb: ").append(ambb).append("\n");
    sb.append("  ambg: ").append(ambg).append("\n");
    sb.append("  amb: ").append(amb).append("\n");
    sb.append("  emit: ").append(emit).append("\n");
    sb.append("  ang: ").append(ang).append("\n");
    sb.append("  spectra: ").append(spectra).append("\n");
    sb.append("  ray_mirror: ").append(ray_mirror).append("\n");
    sb.append("  alpha: ").append(alpha).append("\n");
    sb.append("  ref: ").append(ref).append("\n");
    sb.append("  spec: ").append(spec).append("\n");
    sb.append("  zoffs: ").append(zoffs).append("\n");
    sb.append("  add: ").append(add).append("\n");
    sb.append("  translucency: ").append(translucency).append("\n");
    sb.append("  vol: ").append(vol).append("\n");
    sb.append("  fresnel_mir: ").append(fresnel_mir).append("\n");
    sb.append("  fresnel_mir_i: ").append(fresnel_mir_i).append("\n");
    sb.append("  fresnel_tra: ").append(fresnel_tra).append("\n");
    sb.append("  fresnel_tra_i: ").append(fresnel_tra_i).append("\n");
    sb.append("  filter: ").append(filter).append("\n");
    sb.append("  tx_limit: ").append(tx_limit).append("\n");
    sb.append("  tx_falloff: ").append(tx_falloff).append("\n");
    sb.append("  ray_depth: ").append(ray_depth).append("\n");
    sb.append("  ray_depth_tra: ").append(ray_depth_tra).append("\n");
    sb.append("  har: ").append(har).append("\n");
    sb.append("  seed1: ").append(seed1).append("\n");
    sb.append("  seed2: ").append(seed2).append("\n");
    sb.append("  gloss_mir: ").append(gloss_mir).append("\n");
    sb.append("  gloss_tra: ").append(gloss_tra).append("\n");
    sb.append("  samp_gloss_mir: ").append(samp_gloss_mir).append("\n");
    sb.append("  samp_gloss_tra: ").append(samp_gloss_tra).append("\n");
    sb.append("  adapt_thresh_mir: ").append(adapt_thresh_mir).append("\n");
    sb.append("  adapt_thresh_tra: ").append(adapt_thresh_tra).append("\n");
    sb.append("  aniso_gloss_mir: ").append(aniso_gloss_mir).append("\n");
    sb.append("  dist_mir: ").append(dist_mir).append("\n");
    sb.append("  fadeto_mir: ").append(fadeto_mir).append("\n");
    sb.append("  shade_flag: ").append(shade_flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  mode_l: ").append(mode_l).append("\n");
    sb.append("  flarec: ").append(flarec).append("\n");
    sb.append("  starc: ").append(starc).append("\n");
    sb.append("  linec: ").append(linec).append("\n");
    sb.append("  ringc: ").append(ringc).append("\n");
    sb.append("  hasize: ").append(hasize).append("\n");
    sb.append("  flaresize: ").append(flaresize).append("\n");
    sb.append("  subsize: ").append(subsize).append("\n");
    sb.append("  flareboost: ").append(flareboost).append("\n");
    sb.append("  strand_sta: ").append(strand_sta).append("\n");
    sb.append("  strand_end: ").append(strand_end).append("\n");
    sb.append("  strand_ease: ").append(strand_ease).append("\n");
    sb.append("  strand_surfnor: ").append(strand_surfnor).append("\n");
    sb.append("  strand_min: ").append(strand_min).append("\n");
    sb.append("  strand_widthfade: ").append(strand_widthfade).append("\n");
    sb.append("  strand_uvname: ").append(new String(strand_uvname)).append("\n");
    sb.append("  sbias: ").append(sbias).append("\n");
    sb.append("  lbias: ").append(lbias).append("\n");
    sb.append("  shad_alpha: ").append(shad_alpha).append("\n");
    sb.append("  septex: ").append(septex).append("\n");
    sb.append("  rgbsel: ").append(rgbsel).append("\n");
    sb.append("  texact: ").append(texact).append("\n");
    sb.append("  pr_type: ").append(pr_type).append("\n");
    sb.append("  use_nodes: ").append(use_nodes).append("\n");
    sb.append("  pr_back: ").append(pr_back).append("\n");
    sb.append("  pr_lamp: ").append(pr_lamp).append("\n");
    sb.append("  pr_texture: ").append(pr_texture).append("\n");
    sb.append("  ml_flag: ").append(ml_flag).append("\n");
    sb.append("  diff_shader: ").append(diff_shader).append("\n");
    sb.append("  spec_shader: ").append(spec_shader).append("\n");
    sb.append("  roughness: ").append(roughness).append("\n");
    sb.append("  refrac: ").append(refrac).append("\n");
    sb.append("  param: ").append(Arrays.toString(param)).append("\n");
    sb.append("  rms: ").append(rms).append("\n");
    sb.append("  darkness: ").append(darkness).append("\n");
    sb.append("  texco: ").append(texco).append("\n");
    sb.append("  mapto: ").append(mapto).append("\n");
    sb.append("  ramp_col: ").append(ramp_col).append("\n");
    sb.append("  ramp_spec: ").append(ramp_spec).append("\n");
    sb.append("  rampin_col: ").append(rampin_col).append("\n");
    sb.append("  rampin_spec: ").append(rampin_spec).append("\n");
    sb.append("  rampblend_col: ").append(rampblend_col).append("\n");
    sb.append("  rampblend_spec: ").append(rampblend_spec).append("\n");
    sb.append("  ramp_show: ").append(ramp_show).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  rampfac_col: ").append(rampfac_col).append("\n");
    sb.append("  rampfac_spec: ").append(rampfac_spec).append("\n");
    sb.append("  mtex: ").append(Arrays.toString(mtex)).append("\n");
    sb.append("  nodetree: ").append(nodetree).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  group: ").append(group).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  friction: ").append(friction).append("\n");
    sb.append("  fh: ").append(fh).append("\n");
    sb.append("  reflect: ").append(reflect).append("\n");
    sb.append("  fhdist: ").append(fhdist).append("\n");
    sb.append("  xyfrict: ").append(xyfrict).append("\n");
    sb.append("  dynamode: ").append(dynamode).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  sss_radius: ").append(Arrays.toString(sss_radius)).append("\n");
    sb.append("  sss_col: ").append(Arrays.toString(sss_col)).append("\n");
    sb.append("  sss_error: ").append(sss_error).append("\n");
    sb.append("  sss_scale: ").append(sss_scale).append("\n");
    sb.append("  sss_ior: ").append(sss_ior).append("\n");
    sb.append("  sss_colfac: ").append(sss_colfac).append("\n");
    sb.append("  sss_texfac: ").append(sss_texfac).append("\n");
    sb.append("  sss_front: ").append(sss_front).append("\n");
    sb.append("  sss_back: ").append(sss_back).append("\n");
    sb.append("  sss_flag: ").append(sss_flag).append("\n");
    sb.append("  sss_preset: ").append(sss_preset).append("\n");
    sb.append("  mapto_textured: ").append(mapto_textured).append("\n");
    sb.append("  shadowonly_flag: ").append(shadowonly_flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  gpumaterial: ").append(gpumaterial).append("\n");
    return sb.toString();
  }
  public Material copy() { try {return (Material)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

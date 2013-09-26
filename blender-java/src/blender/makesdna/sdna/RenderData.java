package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class RenderData implements DNA, Cloneable { // #134
  public RenderData[] myarray;
  public AviCodecData avicodecdata; // ptr 184
  public QuicktimeCodecData qtcodecdata; // ptr 152
  public QuicktimeCodecSettings qtcodecsettings = new QuicktimeCodecSettings(); // 64
  public FFMpegCodecData ffcodecdata = new FFMpegCodecData(); // 64
  public int cfra; // 4
  public int sfra; // 4
  public int efra; // 4
  public float subframe; // 4
  public int psfra; // 4
  public int pefra; // 4
  public int images; // 4
  public int framapto; // 4
  public short flag; // 2
  public short threads; // 2
  public float framelen; // 4
  public float blurfac; // 4
  public float edgeR; // 4
  public float edgeG; // 4
  public float edgeB; // 4
  public short fullscreen; // 2
  public short xplay; // 2
  public short yplay; // 2
  public short freqplay; // 2
  public short depth; // 2
  public short attrib; // 2
  public int frame_step; // 4
  public short stereomode; // 2
  public short dimensionspreset; // 2
  public short filtertype; // 2
  public short size; // 2
  public short maximsize; // 2
  public short xsch; // 2
  public short ysch; // 2
  public short xparts; // 2
  public short yparts; // 2
  public short winpos; // 2
  public short planes; // 2
  public short imtype; // 2
  public short subimtype; // 2
  public short bufflag; // 2
  public short quality; // 2
  public short displaymode; // 2
  public short rpad1; // 2
  public short rpad2; // 2
  public int scemode; // 4
  public int mode; // 4
  public int raytrace_options; // 4
  public short raytrace_structure; // 2
  public short renderer; // 2
  public short ocres; // 2
  public short pad4; // 2
  public short alphamode; // 2
  public short osa; // 2
  public short frs_sec; // 2
  public short edgeint; // 2
  public rctf safety = new rctf(); // 16
  public rctf border = new rctf(); // 16
  public rcti disprect = new rcti(); // 16
  public ListBase layers = new ListBase(); // 16
  public short actlay; // 2
  public short mblur_samples; // 2
  public float xasp; // 4
  public float yasp; // 4
  public float frs_sec_base; // 4
  public float gauss; // 4
  public int color_mgt_flag; // 4
  public float postgamma; // 4
  public float posthue; // 4
  public float postsat; // 4
  public float dither_intensity; // 4
  public short bake_osa; // 2
  public short bake_filter; // 2
  public short bake_mode; // 2
  public short bake_flag; // 2
  public short bake_normal_space; // 2
  public short bake_quad_split; // 2
  public float bake_maxdist; // 4
  public float bake_biasdist; // 4
  public float bake_pad; // 4
  public byte[] backbuf = new byte[160]; // 1
  public byte[] pic = new byte[160]; // 1
  public int stamp; // 4
  public short stamp_font_id; // 2
  public short pad3; // 2
  public byte[] stamp_udata = new byte[160]; // 1
  public float[] fg_stamp = new float[4]; // 4
  public float[] bg_stamp = new float[4]; // 4
  public byte seq_prev_type; // 1
  public byte seq_rend_type; // 1
  public byte seq_flag; // 1
  public byte[] pad5 = new byte[5]; // 1
  public int simplify_flag; // 4
  public short simplify_subsurf; // 2
  public short simplify_shadowsamples; // 2
  public float simplify_particles; // 4
  public float simplify_aosss; // 4
  public short cineonwhite; // 2
  public short cineonblack; // 2
  public float cineongamma; // 4
  public short jp2_preset; // 2
  public short jp2_depth; // 2
  public int rpad3; // 4
  public short domeres; // 2
  public short domemode; // 2
  public short domeangle; // 2
  public short dometilt; // 2
  public float domeresbuf; // 4
  public float pad2; // 4
  public Text dometext; // ptr 176
  public byte[] engine = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    avicodecdata = DNATools.link(DNATools.ptr(buffer), AviCodecData.class); // get ptr
    qtcodecdata = DNATools.link(DNATools.ptr(buffer), QuicktimeCodecData.class); // get ptr
    qtcodecsettings.read(buffer);
    ffcodecdata.read(buffer);
    cfra = buffer.getInt();
    sfra = buffer.getInt();
    efra = buffer.getInt();
    subframe = buffer.getFloat();
    psfra = buffer.getInt();
    pefra = buffer.getInt();
    images = buffer.getInt();
    framapto = buffer.getInt();
    flag = buffer.getShort();
    threads = buffer.getShort();
    framelen = buffer.getFloat();
    blurfac = buffer.getFloat();
    edgeR = buffer.getFloat();
    edgeG = buffer.getFloat();
    edgeB = buffer.getFloat();
    fullscreen = buffer.getShort();
    xplay = buffer.getShort();
    yplay = buffer.getShort();
    freqplay = buffer.getShort();
    depth = buffer.getShort();
    attrib = buffer.getShort();
    frame_step = buffer.getInt();
    stereomode = buffer.getShort();
    dimensionspreset = buffer.getShort();
    filtertype = buffer.getShort();
    size = buffer.getShort();
    maximsize = buffer.getShort();
    xsch = buffer.getShort();
    ysch = buffer.getShort();
    xparts = buffer.getShort();
    yparts = buffer.getShort();
    winpos = buffer.getShort();
    planes = buffer.getShort();
    imtype = buffer.getShort();
    subimtype = buffer.getShort();
    bufflag = buffer.getShort();
    quality = buffer.getShort();
    displaymode = buffer.getShort();
    rpad1 = buffer.getShort();
    rpad2 = buffer.getShort();
    scemode = buffer.getInt();
    mode = buffer.getInt();
    raytrace_options = buffer.getInt();
    raytrace_structure = buffer.getShort();
    renderer = buffer.getShort();
    ocres = buffer.getShort();
    pad4 = buffer.getShort();
    alphamode = buffer.getShort();
    osa = buffer.getShort();
    frs_sec = buffer.getShort();
    edgeint = buffer.getShort();
    safety.read(buffer);
    border.read(buffer);
    disprect.read(buffer);
    layers.read(buffer);
    actlay = buffer.getShort();
    mblur_samples = buffer.getShort();
    xasp = buffer.getFloat();
    yasp = buffer.getFloat();
    frs_sec_base = buffer.getFloat();
    gauss = buffer.getFloat();
    color_mgt_flag = buffer.getInt();
    postgamma = buffer.getFloat();
    posthue = buffer.getFloat();
    postsat = buffer.getFloat();
    dither_intensity = buffer.getFloat();
    bake_osa = buffer.getShort();
    bake_filter = buffer.getShort();
    bake_mode = buffer.getShort();
    bake_flag = buffer.getShort();
    bake_normal_space = buffer.getShort();
    bake_quad_split = buffer.getShort();
    bake_maxdist = buffer.getFloat();
    bake_biasdist = buffer.getFloat();
    bake_pad = buffer.getFloat();
    buffer.get(backbuf);
    buffer.get(pic);
    stamp = buffer.getInt();
    stamp_font_id = buffer.getShort();
    pad3 = buffer.getShort();
    buffer.get(stamp_udata);
    for(int i=0;i<fg_stamp.length;i++) fg_stamp[i]=buffer.getFloat();
    for(int i=0;i<bg_stamp.length;i++) bg_stamp[i]=buffer.getFloat();
    seq_prev_type = buffer.get();
    seq_rend_type = buffer.get();
    seq_flag = buffer.get();
    buffer.get(pad5);
    simplify_flag = buffer.getInt();
    simplify_subsurf = buffer.getShort();
    simplify_shadowsamples = buffer.getShort();
    simplify_particles = buffer.getFloat();
    simplify_aosss = buffer.getFloat();
    cineonwhite = buffer.getShort();
    cineonblack = buffer.getShort();
    cineongamma = buffer.getFloat();
    jp2_preset = buffer.getShort();
    jp2_depth = buffer.getShort();
    rpad3 = buffer.getInt();
    domeres = buffer.getShort();
    domemode = buffer.getShort();
    domeangle = buffer.getShort();
    dometilt = buffer.getShort();
    domeresbuf = buffer.getFloat();
    pad2 = buffer.getFloat();
    dometext = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
    buffer.get(engine);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(avicodecdata!=null?avicodecdata.hashCode():0);
    buffer.writeInt(qtcodecdata!=null?qtcodecdata.hashCode():0);
    qtcodecsettings.write(buffer);
    ffcodecdata.write(buffer);
    buffer.writeInt(cfra);
    buffer.writeInt(sfra);
    buffer.writeInt(efra);
    buffer.writeFloat(subframe);
    buffer.writeInt(psfra);
    buffer.writeInt(pefra);
    buffer.writeInt(images);
    buffer.writeInt(framapto);
    buffer.writeShort(flag);
    buffer.writeShort(threads);
    buffer.writeFloat(framelen);
    buffer.writeFloat(blurfac);
    buffer.writeFloat(edgeR);
    buffer.writeFloat(edgeG);
    buffer.writeFloat(edgeB);
    buffer.writeShort(fullscreen);
    buffer.writeShort(xplay);
    buffer.writeShort(yplay);
    buffer.writeShort(freqplay);
    buffer.writeShort(depth);
    buffer.writeShort(attrib);
    buffer.writeInt(frame_step);
    buffer.writeShort(stereomode);
    buffer.writeShort(dimensionspreset);
    buffer.writeShort(filtertype);
    buffer.writeShort(size);
    buffer.writeShort(maximsize);
    buffer.writeShort(xsch);
    buffer.writeShort(ysch);
    buffer.writeShort(xparts);
    buffer.writeShort(yparts);
    buffer.writeShort(winpos);
    buffer.writeShort(planes);
    buffer.writeShort(imtype);
    buffer.writeShort(subimtype);
    buffer.writeShort(bufflag);
    buffer.writeShort(quality);
    buffer.writeShort(displaymode);
    buffer.writeShort(rpad1);
    buffer.writeShort(rpad2);
    buffer.writeInt(scemode);
    buffer.writeInt(mode);
    buffer.writeInt(raytrace_options);
    buffer.writeShort(raytrace_structure);
    buffer.writeShort(renderer);
    buffer.writeShort(ocres);
    buffer.writeShort(pad4);
    buffer.writeShort(alphamode);
    buffer.writeShort(osa);
    buffer.writeShort(frs_sec);
    buffer.writeShort(edgeint);
    safety.write(buffer);
    border.write(buffer);
    disprect.write(buffer);
    layers.write(buffer);
    buffer.writeShort(actlay);
    buffer.writeShort(mblur_samples);
    buffer.writeFloat(xasp);
    buffer.writeFloat(yasp);
    buffer.writeFloat(frs_sec_base);
    buffer.writeFloat(gauss);
    buffer.writeInt(color_mgt_flag);
    buffer.writeFloat(postgamma);
    buffer.writeFloat(posthue);
    buffer.writeFloat(postsat);
    buffer.writeFloat(dither_intensity);
    buffer.writeShort(bake_osa);
    buffer.writeShort(bake_filter);
    buffer.writeShort(bake_mode);
    buffer.writeShort(bake_flag);
    buffer.writeShort(bake_normal_space);
    buffer.writeShort(bake_quad_split);
    buffer.writeFloat(bake_maxdist);
    buffer.writeFloat(bake_biasdist);
    buffer.writeFloat(bake_pad);
    buffer.write(backbuf);
    buffer.write(pic);
    buffer.writeInt(stamp);
    buffer.writeShort(stamp_font_id);
    buffer.writeShort(pad3);
    buffer.write(stamp_udata);
    for(int i=0;i<fg_stamp.length;i++) buffer.writeFloat(fg_stamp[i]);
    for(int i=0;i<bg_stamp.length;i++) buffer.writeFloat(bg_stamp[i]);
    buffer.writeByte(seq_prev_type);
    buffer.writeByte(seq_rend_type);
    buffer.writeByte(seq_flag);
    buffer.write(pad5);
    buffer.writeInt(simplify_flag);
    buffer.writeShort(simplify_subsurf);
    buffer.writeShort(simplify_shadowsamples);
    buffer.writeFloat(simplify_particles);
    buffer.writeFloat(simplify_aosss);
    buffer.writeShort(cineonwhite);
    buffer.writeShort(cineonblack);
    buffer.writeFloat(cineongamma);
    buffer.writeShort(jp2_preset);
    buffer.writeShort(jp2_depth);
    buffer.writeInt(rpad3);
    buffer.writeShort(domeres);
    buffer.writeShort(domemode);
    buffer.writeShort(domeangle);
    buffer.writeShort(dometilt);
    buffer.writeFloat(domeresbuf);
    buffer.writeFloat(pad2);
    buffer.writeInt(dometext!=null?dometext.hashCode():0);
    buffer.write(engine);
  }
  public Object setmyarray(Object array) {
    myarray = (RenderData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("RenderData:\n");
    sb.append("  avicodecdata: ").append(avicodecdata).append("\n");
    sb.append("  qtcodecdata: ").append(qtcodecdata).append("\n");
    sb.append("  qtcodecsettings: ").append(qtcodecsettings).append("\n");
    sb.append("  ffcodecdata: ").append(ffcodecdata).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  efra: ").append(efra).append("\n");
    sb.append("  subframe: ").append(subframe).append("\n");
    sb.append("  psfra: ").append(psfra).append("\n");
    sb.append("  pefra: ").append(pefra).append("\n");
    sb.append("  images: ").append(images).append("\n");
    sb.append("  framapto: ").append(framapto).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  threads: ").append(threads).append("\n");
    sb.append("  framelen: ").append(framelen).append("\n");
    sb.append("  blurfac: ").append(blurfac).append("\n");
    sb.append("  edgeR: ").append(edgeR).append("\n");
    sb.append("  edgeG: ").append(edgeG).append("\n");
    sb.append("  edgeB: ").append(edgeB).append("\n");
    sb.append("  fullscreen: ").append(fullscreen).append("\n");
    sb.append("  xplay: ").append(xplay).append("\n");
    sb.append("  yplay: ").append(yplay).append("\n");
    sb.append("  freqplay: ").append(freqplay).append("\n");
    sb.append("  depth: ").append(depth).append("\n");
    sb.append("  attrib: ").append(attrib).append("\n");
    sb.append("  frame_step: ").append(frame_step).append("\n");
    sb.append("  stereomode: ").append(stereomode).append("\n");
    sb.append("  dimensionspreset: ").append(dimensionspreset).append("\n");
    sb.append("  filtertype: ").append(filtertype).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  maximsize: ").append(maximsize).append("\n");
    sb.append("  xsch: ").append(xsch).append("\n");
    sb.append("  ysch: ").append(ysch).append("\n");
    sb.append("  xparts: ").append(xparts).append("\n");
    sb.append("  yparts: ").append(yparts).append("\n");
    sb.append("  winpos: ").append(winpos).append("\n");
    sb.append("  planes: ").append(planes).append("\n");
    sb.append("  imtype: ").append(imtype).append("\n");
    sb.append("  subimtype: ").append(subimtype).append("\n");
    sb.append("  bufflag: ").append(bufflag).append("\n");
    sb.append("  quality: ").append(quality).append("\n");
    sb.append("  displaymode: ").append(displaymode).append("\n");
    sb.append("  rpad1: ").append(rpad1).append("\n");
    sb.append("  rpad2: ").append(rpad2).append("\n");
    sb.append("  scemode: ").append(scemode).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  raytrace_options: ").append(raytrace_options).append("\n");
    sb.append("  raytrace_structure: ").append(raytrace_structure).append("\n");
    sb.append("  renderer: ").append(renderer).append("\n");
    sb.append("  ocres: ").append(ocres).append("\n");
    sb.append("  pad4: ").append(pad4).append("\n");
    sb.append("  alphamode: ").append(alphamode).append("\n");
    sb.append("  osa: ").append(osa).append("\n");
    sb.append("  frs_sec: ").append(frs_sec).append("\n");
    sb.append("  edgeint: ").append(edgeint).append("\n");
    sb.append("  safety: ").append(safety).append("\n");
    sb.append("  border: ").append(border).append("\n");
    sb.append("  disprect: ").append(disprect).append("\n");
    sb.append("  layers: ").append(layers).append("\n");
    sb.append("  actlay: ").append(actlay).append("\n");
    sb.append("  mblur_samples: ").append(mblur_samples).append("\n");
    sb.append("  xasp: ").append(xasp).append("\n");
    sb.append("  yasp: ").append(yasp).append("\n");
    sb.append("  frs_sec_base: ").append(frs_sec_base).append("\n");
    sb.append("  gauss: ").append(gauss).append("\n");
    sb.append("  color_mgt_flag: ").append(color_mgt_flag).append("\n");
    sb.append("  postgamma: ").append(postgamma).append("\n");
    sb.append("  posthue: ").append(posthue).append("\n");
    sb.append("  postsat: ").append(postsat).append("\n");
    sb.append("  dither_intensity: ").append(dither_intensity).append("\n");
    sb.append("  bake_osa: ").append(bake_osa).append("\n");
    sb.append("  bake_filter: ").append(bake_filter).append("\n");
    sb.append("  bake_mode: ").append(bake_mode).append("\n");
    sb.append("  bake_flag: ").append(bake_flag).append("\n");
    sb.append("  bake_normal_space: ").append(bake_normal_space).append("\n");
    sb.append("  bake_quad_split: ").append(bake_quad_split).append("\n");
    sb.append("  bake_maxdist: ").append(bake_maxdist).append("\n");
    sb.append("  bake_biasdist: ").append(bake_biasdist).append("\n");
    sb.append("  bake_pad: ").append(bake_pad).append("\n");
    sb.append("  backbuf: ").append(new String(backbuf)).append("\n");
    sb.append("  pic: ").append(new String(pic)).append("\n");
    sb.append("  stamp: ").append(stamp).append("\n");
    sb.append("  stamp_font_id: ").append(stamp_font_id).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  stamp_udata: ").append(new String(stamp_udata)).append("\n");
    sb.append("  fg_stamp: ").append(Arrays.toString(fg_stamp)).append("\n");
    sb.append("  bg_stamp: ").append(Arrays.toString(bg_stamp)).append("\n");
    sb.append("  seq_prev_type: ").append(seq_prev_type).append("\n");
    sb.append("  seq_rend_type: ").append(seq_rend_type).append("\n");
    sb.append("  seq_flag: ").append(seq_flag).append("\n");
    sb.append("  pad5: ").append(new String(pad5)).append("\n");
    sb.append("  simplify_flag: ").append(simplify_flag).append("\n");
    sb.append("  simplify_subsurf: ").append(simplify_subsurf).append("\n");
    sb.append("  simplify_shadowsamples: ").append(simplify_shadowsamples).append("\n");
    sb.append("  simplify_particles: ").append(simplify_particles).append("\n");
    sb.append("  simplify_aosss: ").append(simplify_aosss).append("\n");
    sb.append("  cineonwhite: ").append(cineonwhite).append("\n");
    sb.append("  cineonblack: ").append(cineonblack).append("\n");
    sb.append("  cineongamma: ").append(cineongamma).append("\n");
    sb.append("  jp2_preset: ").append(jp2_preset).append("\n");
    sb.append("  jp2_depth: ").append(jp2_depth).append("\n");
    sb.append("  rpad3: ").append(rpad3).append("\n");
    sb.append("  domeres: ").append(domeres).append("\n");
    sb.append("  domemode: ").append(domemode).append("\n");
    sb.append("  domeangle: ").append(domeangle).append("\n");
    sb.append("  dometilt: ").append(dometilt).append("\n");
    sb.append("  domeresbuf: ").append(domeresbuf).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  dometext: ").append(dometext).append("\n");
    sb.append("  engine: ").append(new String(engine)).append("\n");
    return sb.toString();
  }
  public RenderData copy() { try {return (RenderData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

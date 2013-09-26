package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Tex extends ID implements DNA, Cloneable { // #31
  public Tex[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public float noisesize; // 4
  public float turbul; // 4
  public float bright; // 4
  public float contrast; // 4
  public float saturation; // 4
  public float rfac; // 4
  public float gfac; // 4
  public float bfac; // 4
  public float filtersize; // 4
  public float pad2; // 4
  public float mg_H; // 4
  public float mg_lacunarity; // 4
  public float mg_octaves; // 4
  public float mg_offset; // 4
  public float mg_gain; // 4
  public float dist_amount; // 4
  public float ns_outscale; // 4
  public float vn_w1; // 4
  public float vn_w2; // 4
  public float vn_w3; // 4
  public float vn_w4; // 4
  public float vn_mexp; // 4
  public short vn_distm; // 2
  public short vn_coltype; // 2
  public short noisedepth; // 2
  public short noisetype; // 2
  public short noisebasis; // 2
  public short noisebasis2; // 2
  public short imaflag; // 2
  public short flag; // 2
  public short type; // 2
  public short stype; // 2
  public float cropxmin; // 4
  public float cropymin; // 4
  public float cropxmax; // 4
  public float cropymax; // 4
  public int texfilter; // 4
  public int afmax; // 4
  public short xrepeat; // 2
  public short yrepeat; // 2
  public short extend; // 2
  public short fie_ima; // 2
  public int len; // 4
  public int frames; // 4
  public int offset; // 4
  public int sfra; // 4
  public float checkerdist; // 4
  public float nabla; // 4
  public float pad1; // 4
  public ImageUser iuser = new ImageUser(); // 40
  public bNodeTree nodetree; // ptr 264
  public Ipo ipo; // ptr 112
  public Image ima; // ptr 496
  public PluginTex plugin; // ptr 376
  public ColorBand coba; // ptr 776
  public EnvMap env; // ptr 200
  public PreviewImage preview; // ptr 40
  public PointDensity pd; // ptr 88
  public VoxelData vd; // ptr 296
  public byte use_nodes; // 1
  public byte[] pad = new byte[7]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    noisesize = buffer.getFloat();
    turbul = buffer.getFloat();
    bright = buffer.getFloat();
    contrast = buffer.getFloat();
    saturation = buffer.getFloat();
    rfac = buffer.getFloat();
    gfac = buffer.getFloat();
    bfac = buffer.getFloat();
    filtersize = buffer.getFloat();
    pad2 = buffer.getFloat();
    mg_H = buffer.getFloat();
    mg_lacunarity = buffer.getFloat();
    mg_octaves = buffer.getFloat();
    mg_offset = buffer.getFloat();
    mg_gain = buffer.getFloat();
    dist_amount = buffer.getFloat();
    ns_outscale = buffer.getFloat();
    vn_w1 = buffer.getFloat();
    vn_w2 = buffer.getFloat();
    vn_w3 = buffer.getFloat();
    vn_w4 = buffer.getFloat();
    vn_mexp = buffer.getFloat();
    vn_distm = buffer.getShort();
    vn_coltype = buffer.getShort();
    noisedepth = buffer.getShort();
    noisetype = buffer.getShort();
    noisebasis = buffer.getShort();
    noisebasis2 = buffer.getShort();
    imaflag = buffer.getShort();
    flag = buffer.getShort();
    type = buffer.getShort();
    stype = buffer.getShort();
    cropxmin = buffer.getFloat();
    cropymin = buffer.getFloat();
    cropxmax = buffer.getFloat();
    cropymax = buffer.getFloat();
    texfilter = buffer.getInt();
    afmax = buffer.getInt();
    xrepeat = buffer.getShort();
    yrepeat = buffer.getShort();
    extend = buffer.getShort();
    fie_ima = buffer.getShort();
    len = buffer.getInt();
    frames = buffer.getInt();
    offset = buffer.getInt();
    sfra = buffer.getInt();
    checkerdist = buffer.getFloat();
    nabla = buffer.getFloat();
    pad1 = buffer.getFloat();
    iuser.read(buffer);
    nodetree = DNATools.link(DNATools.ptr(buffer), bNodeTree.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    ima = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    plugin = DNATools.link(DNATools.ptr(buffer), PluginTex.class); // get ptr
    coba = DNATools.link(DNATools.ptr(buffer), ColorBand.class); // get ptr
    env = DNATools.link(DNATools.ptr(buffer), EnvMap.class); // get ptr
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
    pd = DNATools.link(DNATools.ptr(buffer), PointDensity.class); // get ptr
    vd = DNATools.link(DNATools.ptr(buffer), VoxelData.class); // get ptr
    use_nodes = buffer.get();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeFloat(noisesize);
    buffer.writeFloat(turbul);
    buffer.writeFloat(bright);
    buffer.writeFloat(contrast);
    buffer.writeFloat(saturation);
    buffer.writeFloat(rfac);
    buffer.writeFloat(gfac);
    buffer.writeFloat(bfac);
    buffer.writeFloat(filtersize);
    buffer.writeFloat(pad2);
    buffer.writeFloat(mg_H);
    buffer.writeFloat(mg_lacunarity);
    buffer.writeFloat(mg_octaves);
    buffer.writeFloat(mg_offset);
    buffer.writeFloat(mg_gain);
    buffer.writeFloat(dist_amount);
    buffer.writeFloat(ns_outscale);
    buffer.writeFloat(vn_w1);
    buffer.writeFloat(vn_w2);
    buffer.writeFloat(vn_w3);
    buffer.writeFloat(vn_w4);
    buffer.writeFloat(vn_mexp);
    buffer.writeShort(vn_distm);
    buffer.writeShort(vn_coltype);
    buffer.writeShort(noisedepth);
    buffer.writeShort(noisetype);
    buffer.writeShort(noisebasis);
    buffer.writeShort(noisebasis2);
    buffer.writeShort(imaflag);
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeShort(stype);
    buffer.writeFloat(cropxmin);
    buffer.writeFloat(cropymin);
    buffer.writeFloat(cropxmax);
    buffer.writeFloat(cropymax);
    buffer.writeInt(texfilter);
    buffer.writeInt(afmax);
    buffer.writeShort(xrepeat);
    buffer.writeShort(yrepeat);
    buffer.writeShort(extend);
    buffer.writeShort(fie_ima);
    buffer.writeInt(len);
    buffer.writeInt(frames);
    buffer.writeInt(offset);
    buffer.writeInt(sfra);
    buffer.writeFloat(checkerdist);
    buffer.writeFloat(nabla);
    buffer.writeFloat(pad1);
    iuser.write(buffer);
    buffer.writeInt(nodetree!=null?nodetree.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(ima!=null?ima.hashCode():0);
    buffer.writeInt(plugin!=null?plugin.hashCode():0);
    buffer.writeInt(coba!=null?coba.hashCode():0);
    buffer.writeInt(env!=null?env.hashCode():0);
    buffer.writeInt(preview!=null?preview.hashCode():0);
    buffer.writeInt(pd!=null?pd.hashCode():0);
    buffer.writeInt(vd!=null?vd.hashCode():0);
    buffer.writeByte(use_nodes);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (Tex[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Tex:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  noisesize: ").append(noisesize).append("\n");
    sb.append("  turbul: ").append(turbul).append("\n");
    sb.append("  bright: ").append(bright).append("\n");
    sb.append("  contrast: ").append(contrast).append("\n");
    sb.append("  saturation: ").append(saturation).append("\n");
    sb.append("  rfac: ").append(rfac).append("\n");
    sb.append("  gfac: ").append(gfac).append("\n");
    sb.append("  bfac: ").append(bfac).append("\n");
    sb.append("  filtersize: ").append(filtersize).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  mg_H: ").append(mg_H).append("\n");
    sb.append("  mg_lacunarity: ").append(mg_lacunarity).append("\n");
    sb.append("  mg_octaves: ").append(mg_octaves).append("\n");
    sb.append("  mg_offset: ").append(mg_offset).append("\n");
    sb.append("  mg_gain: ").append(mg_gain).append("\n");
    sb.append("  dist_amount: ").append(dist_amount).append("\n");
    sb.append("  ns_outscale: ").append(ns_outscale).append("\n");
    sb.append("  vn_w1: ").append(vn_w1).append("\n");
    sb.append("  vn_w2: ").append(vn_w2).append("\n");
    sb.append("  vn_w3: ").append(vn_w3).append("\n");
    sb.append("  vn_w4: ").append(vn_w4).append("\n");
    sb.append("  vn_mexp: ").append(vn_mexp).append("\n");
    sb.append("  vn_distm: ").append(vn_distm).append("\n");
    sb.append("  vn_coltype: ").append(vn_coltype).append("\n");
    sb.append("  noisedepth: ").append(noisedepth).append("\n");
    sb.append("  noisetype: ").append(noisetype).append("\n");
    sb.append("  noisebasis: ").append(noisebasis).append("\n");
    sb.append("  noisebasis2: ").append(noisebasis2).append("\n");
    sb.append("  imaflag: ").append(imaflag).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  stype: ").append(stype).append("\n");
    sb.append("  cropxmin: ").append(cropxmin).append("\n");
    sb.append("  cropymin: ").append(cropymin).append("\n");
    sb.append("  cropxmax: ").append(cropxmax).append("\n");
    sb.append("  cropymax: ").append(cropymax).append("\n");
    sb.append("  texfilter: ").append(texfilter).append("\n");
    sb.append("  afmax: ").append(afmax).append("\n");
    sb.append("  xrepeat: ").append(xrepeat).append("\n");
    sb.append("  yrepeat: ").append(yrepeat).append("\n");
    sb.append("  extend: ").append(extend).append("\n");
    sb.append("  fie_ima: ").append(fie_ima).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  frames: ").append(frames).append("\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  checkerdist: ").append(checkerdist).append("\n");
    sb.append("  nabla: ").append(nabla).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  iuser: ").append(iuser).append("\n");
    sb.append("  nodetree: ").append(nodetree).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  ima: ").append(ima).append("\n");
    sb.append("  plugin: ").append(plugin).append("\n");
    sb.append("  coba: ").append(coba).append("\n");
    sb.append("  env: ").append(env).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  pd: ").append(pd).append("\n");
    sb.append("  vd: ").append(vd).append("\n");
    sb.append("  use_nodes: ").append(use_nodes).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public Tex copy() { try {return (Tex)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

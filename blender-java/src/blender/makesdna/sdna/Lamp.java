package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Lamp extends ID implements DNA, Cloneable { // #33
  public Lamp[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public short type; // 2
  public short flag; // 2
  public int mode; // 4
  public short colormodel; // 2
  public short totex; // 2
  public float r; // 4
  public float g; // 4
  public float b; // 4
  public float k; // 4
  public float shdwr; // 4
  public float shdwg; // 4
  public float shdwb; // 4
  public float shdwpad; // 4
  public float energy; // 4
  public float dist; // 4
  public float spotsize; // 4
  public float spotblend; // 4
  public float haint; // 4
  public float att1; // 4
  public float att2; // 4
  public CurveMapping curfalloff; // ptr 320
  public short falloff_type; // 2
  public short pad2; // 2
  public float clipsta; // 4
  public float clipend; // 4
  public float shadspotsize; // 4
  public float bias; // 4
  public float soft; // 4
  public float compressthresh; // 4
  public float[] pad5 = new float[3]; // 4
  public short bufsize; // 2
  public short samp; // 2
  public short buffers; // 2
  public short filtertype; // 2
  public byte bufflag; // 1
  public byte buftype; // 1
  public short ray_samp; // 2
  public short ray_sampy; // 2
  public short ray_sampz; // 2
  public short ray_samp_type; // 2
  public short area_shape; // 2
  public float area_size; // 4
  public float area_sizey; // 4
  public float area_sizez; // 4
  public float adapt_thresh; // 4
  public short ray_samp_method; // 2
  public short pad1; // 2
  public short texact; // 2
  public short shadhalostep; // 2
  public short sun_effect_type; // 2
  public short skyblendtype; // 2
  public float horizon_brightness; // 4
  public float spread; // 4
  public float sun_brightness; // 4
  public float sun_size; // 4
  public float backscattered_light; // 4
  public float sun_intensity; // 4
  public float atm_turbidity; // 4
  public float atm_inscattering_factor; // 4
  public float atm_extinction_factor; // 4
  public float atm_distance_factor; // 4
  public float skyblendfac; // 4
  public float sky_exposure; // 4
  public short sky_colorspace; // 2
  public byte[] pad4 = new byte[6]; // 1
  public Ipo ipo; // ptr 112
  public MTex[] mtex = new MTex[18]; // ptr 280
  public short pr_texture; // 2
  public byte[] pad6 = new byte[6]; // 1
  public PreviewImage preview; // ptr 40

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    mode = buffer.getInt();
    colormodel = buffer.getShort();
    totex = buffer.getShort();
    r = buffer.getFloat();
    g = buffer.getFloat();
    b = buffer.getFloat();
    k = buffer.getFloat();
    shdwr = buffer.getFloat();
    shdwg = buffer.getFloat();
    shdwb = buffer.getFloat();
    shdwpad = buffer.getFloat();
    energy = buffer.getFloat();
    dist = buffer.getFloat();
    spotsize = buffer.getFloat();
    spotblend = buffer.getFloat();
    haint = buffer.getFloat();
    att1 = buffer.getFloat();
    att2 = buffer.getFloat();
    curfalloff = DNATools.link(DNATools.ptr(buffer), CurveMapping.class); // get ptr
    falloff_type = buffer.getShort();
    pad2 = buffer.getShort();
    clipsta = buffer.getFloat();
    clipend = buffer.getFloat();
    shadspotsize = buffer.getFloat();
    bias = buffer.getFloat();
    soft = buffer.getFloat();
    compressthresh = buffer.getFloat();
    for(int i=0;i<pad5.length;i++) pad5[i]=buffer.getFloat();
    bufsize = buffer.getShort();
    samp = buffer.getShort();
    buffers = buffer.getShort();
    filtertype = buffer.getShort();
    bufflag = buffer.get();
    buftype = buffer.get();
    ray_samp = buffer.getShort();
    ray_sampy = buffer.getShort();
    ray_sampz = buffer.getShort();
    ray_samp_type = buffer.getShort();
    area_shape = buffer.getShort();
    area_size = buffer.getFloat();
    area_sizey = buffer.getFloat();
    area_sizez = buffer.getFloat();
    adapt_thresh = buffer.getFloat();
    ray_samp_method = buffer.getShort();
    pad1 = buffer.getShort();
    texact = buffer.getShort();
    shadhalostep = buffer.getShort();
    sun_effect_type = buffer.getShort();
    skyblendtype = buffer.getShort();
    horizon_brightness = buffer.getFloat();
    spread = buffer.getFloat();
    sun_brightness = buffer.getFloat();
    sun_size = buffer.getFloat();
    backscattered_light = buffer.getFloat();
    sun_intensity = buffer.getFloat();
    atm_turbidity = buffer.getFloat();
    atm_inscattering_factor = buffer.getFloat();
    atm_extinction_factor = buffer.getFloat();
    atm_distance_factor = buffer.getFloat();
    skyblendfac = buffer.getFloat();
    sky_exposure = buffer.getFloat();
    sky_colorspace = buffer.getShort();
    buffer.get(pad4);
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    for(int i=0;i<mtex.length;i++) mtex[i]=DNATools.link(DNATools.ptr(buffer), MTex.class);
    pr_texture = buffer.getShort();
    buffer.get(pad6);
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeInt(mode);
    buffer.writeShort(colormodel);
    buffer.writeShort(totex);
    buffer.writeFloat(r);
    buffer.writeFloat(g);
    buffer.writeFloat(b);
    buffer.writeFloat(k);
    buffer.writeFloat(shdwr);
    buffer.writeFloat(shdwg);
    buffer.writeFloat(shdwb);
    buffer.writeFloat(shdwpad);
    buffer.writeFloat(energy);
    buffer.writeFloat(dist);
    buffer.writeFloat(spotsize);
    buffer.writeFloat(spotblend);
    buffer.writeFloat(haint);
    buffer.writeFloat(att1);
    buffer.writeFloat(att2);
    buffer.writeInt(curfalloff!=null?curfalloff.hashCode():0);
    buffer.writeShort(falloff_type);
    buffer.writeShort(pad2);
    buffer.writeFloat(clipsta);
    buffer.writeFloat(clipend);
    buffer.writeFloat(shadspotsize);
    buffer.writeFloat(bias);
    buffer.writeFloat(soft);
    buffer.writeFloat(compressthresh);
    for(int i=0;i<pad5.length;i++) buffer.writeFloat(pad5[i]);
    buffer.writeShort(bufsize);
    buffer.writeShort(samp);
    buffer.writeShort(buffers);
    buffer.writeShort(filtertype);
    buffer.writeByte(bufflag);
    buffer.writeByte(buftype);
    buffer.writeShort(ray_samp);
    buffer.writeShort(ray_sampy);
    buffer.writeShort(ray_sampz);
    buffer.writeShort(ray_samp_type);
    buffer.writeShort(area_shape);
    buffer.writeFloat(area_size);
    buffer.writeFloat(area_sizey);
    buffer.writeFloat(area_sizez);
    buffer.writeFloat(adapt_thresh);
    buffer.writeShort(ray_samp_method);
    buffer.writeShort(pad1);
    buffer.writeShort(texact);
    buffer.writeShort(shadhalostep);
    buffer.writeShort(sun_effect_type);
    buffer.writeShort(skyblendtype);
    buffer.writeFloat(horizon_brightness);
    buffer.writeFloat(spread);
    buffer.writeFloat(sun_brightness);
    buffer.writeFloat(sun_size);
    buffer.writeFloat(backscattered_light);
    buffer.writeFloat(sun_intensity);
    buffer.writeFloat(atm_turbidity);
    buffer.writeFloat(atm_inscattering_factor);
    buffer.writeFloat(atm_extinction_factor);
    buffer.writeFloat(atm_distance_factor);
    buffer.writeFloat(skyblendfac);
    buffer.writeFloat(sky_exposure);
    buffer.writeShort(sky_colorspace);
    buffer.write(pad4);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    for(int i=0;i<mtex.length;i++) buffer.writeInt(mtex[i]!=null?mtex[i].hashCode():0);
    buffer.writeShort(pr_texture);
    buffer.write(pad6);
    buffer.writeInt(preview!=null?preview.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Lamp[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Lamp:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  colormodel: ").append(colormodel).append("\n");
    sb.append("  totex: ").append(totex).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    sb.append("  k: ").append(k).append("\n");
    sb.append("  shdwr: ").append(shdwr).append("\n");
    sb.append("  shdwg: ").append(shdwg).append("\n");
    sb.append("  shdwb: ").append(shdwb).append("\n");
    sb.append("  shdwpad: ").append(shdwpad).append("\n");
    sb.append("  energy: ").append(energy).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  spotsize: ").append(spotsize).append("\n");
    sb.append("  spotblend: ").append(spotblend).append("\n");
    sb.append("  haint: ").append(haint).append("\n");
    sb.append("  att1: ").append(att1).append("\n");
    sb.append("  att2: ").append(att2).append("\n");
    sb.append("  curfalloff: ").append(curfalloff).append("\n");
    sb.append("  falloff_type: ").append(falloff_type).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  clipsta: ").append(clipsta).append("\n");
    sb.append("  clipend: ").append(clipend).append("\n");
    sb.append("  shadspotsize: ").append(shadspotsize).append("\n");
    sb.append("  bias: ").append(bias).append("\n");
    sb.append("  soft: ").append(soft).append("\n");
    sb.append("  compressthresh: ").append(compressthresh).append("\n");
    sb.append("  pad5: ").append(Arrays.toString(pad5)).append("\n");
    sb.append("  bufsize: ").append(bufsize).append("\n");
    sb.append("  samp: ").append(samp).append("\n");
    sb.append("  buffers: ").append(buffers).append("\n");
    sb.append("  filtertype: ").append(filtertype).append("\n");
    sb.append("  bufflag: ").append(bufflag).append("\n");
    sb.append("  buftype: ").append(buftype).append("\n");
    sb.append("  ray_samp: ").append(ray_samp).append("\n");
    sb.append("  ray_sampy: ").append(ray_sampy).append("\n");
    sb.append("  ray_sampz: ").append(ray_sampz).append("\n");
    sb.append("  ray_samp_type: ").append(ray_samp_type).append("\n");
    sb.append("  area_shape: ").append(area_shape).append("\n");
    sb.append("  area_size: ").append(area_size).append("\n");
    sb.append("  area_sizey: ").append(area_sizey).append("\n");
    sb.append("  area_sizez: ").append(area_sizez).append("\n");
    sb.append("  adapt_thresh: ").append(adapt_thresh).append("\n");
    sb.append("  ray_samp_method: ").append(ray_samp_method).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  texact: ").append(texact).append("\n");
    sb.append("  shadhalostep: ").append(shadhalostep).append("\n");
    sb.append("  sun_effect_type: ").append(sun_effect_type).append("\n");
    sb.append("  skyblendtype: ").append(skyblendtype).append("\n");
    sb.append("  horizon_brightness: ").append(horizon_brightness).append("\n");
    sb.append("  spread: ").append(spread).append("\n");
    sb.append("  sun_brightness: ").append(sun_brightness).append("\n");
    sb.append("  sun_size: ").append(sun_size).append("\n");
    sb.append("  backscattered_light: ").append(backscattered_light).append("\n");
    sb.append("  sun_intensity: ").append(sun_intensity).append("\n");
    sb.append("  atm_turbidity: ").append(atm_turbidity).append("\n");
    sb.append("  atm_inscattering_factor: ").append(atm_inscattering_factor).append("\n");
    sb.append("  atm_extinction_factor: ").append(atm_extinction_factor).append("\n");
    sb.append("  atm_distance_factor: ").append(atm_distance_factor).append("\n");
    sb.append("  skyblendfac: ").append(skyblendfac).append("\n");
    sb.append("  sky_exposure: ").append(sky_exposure).append("\n");
    sb.append("  sky_colorspace: ").append(sky_colorspace).append("\n");
    sb.append("  pad4: ").append(new String(pad4)).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  mtex: ").append(Arrays.toString(mtex)).append("\n");
    sb.append("  pr_texture: ").append(pr_texture).append("\n");
    sb.append("  pad6: ").append(new String(pad6)).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    return sb.toString();
  }
  public Lamp copy() { try {return (Lamp)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

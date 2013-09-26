package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class World extends ID implements DNA, Cloneable { // #126
  public World[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public short colormodel; // 2
  public short totex; // 2
  public short texact; // 2
  public short mistype; // 2
  public float horr; // 4
  public float horg; // 4
  public float horb; // 4
  public float zenr; // 4
  public float zeng; // 4
  public float zenb; // 4
  public float ambr; // 4
  public float ambg; // 4
  public float ambb; // 4
  public float pad2; // 4
  public int fastcol; // 4
  public float exposure; // 4
  public float exp; // 4
  public float range; // 4
  public float linfac; // 4
  public float logfac; // 4
  public float gravity; // 4
  public float activityBoxRadius; // 4
  public short skytype; // 2
  public short mode; // 2
  public short occlusionRes; // 2
  public short physicsEngine; // 2
  public short ticrate; // 2
  public short maxlogicstep; // 2
  public short physubstep; // 2
  public short maxphystep; // 2
  public float misi; // 4
  public float miststa; // 4
  public float mistdist; // 4
  public float misthi; // 4
  public float starr; // 4
  public float starg; // 4
  public float starb; // 4
  public float stark; // 4
  public float starsize; // 4
  public float starmindist; // 4
  public float stardist; // 4
  public float starcolnoise; // 4
  public short dofsta; // 2
  public short dofend; // 2
  public short dofmin; // 2
  public short dofmax; // 2
  public float aodist; // 4
  public float aodistfac; // 4
  public float aoenergy; // 4
  public float aobias; // 4
  public short aomode; // 2
  public short aosamp; // 2
  public short aomix; // 2
  public short aocolor; // 2
  public float ao_adapt_thresh; // 4
  public float ao_adapt_speed_fac; // 4
  public float ao_approx_error; // 4
  public float ao_approx_correction; // 4
  public float ao_indirect_energy; // 4
  public float ao_env_energy; // 4
  public float ao_pad2; // 4
  public short ao_indirect_bounces; // 2
  public short ao_pad; // 2
  public short ao_samp_method; // 2
  public short ao_gather_method; // 2
  public short ao_approx_passes; // 2
  public short flag; // 2
  public Object aosphere; // ptr 4
  public Object aotables; // ptr 4
  public Ipo ipo; // ptr 112
  public MTex[] mtex = new MTex[18]; // ptr 280
  public short pr_texture; // 2
  public short[] pad = new short[3]; // 2
  public PreviewImage preview; // ptr 40

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    colormodel = buffer.getShort();
    totex = buffer.getShort();
    texact = buffer.getShort();
    mistype = buffer.getShort();
    horr = buffer.getFloat();
    horg = buffer.getFloat();
    horb = buffer.getFloat();
    zenr = buffer.getFloat();
    zeng = buffer.getFloat();
    zenb = buffer.getFloat();
    ambr = buffer.getFloat();
    ambg = buffer.getFloat();
    ambb = buffer.getFloat();
    pad2 = buffer.getFloat();
    fastcol = buffer.getInt();
    exposure = buffer.getFloat();
    exp = buffer.getFloat();
    range = buffer.getFloat();
    linfac = buffer.getFloat();
    logfac = buffer.getFloat();
    gravity = buffer.getFloat();
    activityBoxRadius = buffer.getFloat();
    skytype = buffer.getShort();
    mode = buffer.getShort();
    occlusionRes = buffer.getShort();
    physicsEngine = buffer.getShort();
    ticrate = buffer.getShort();
    maxlogicstep = buffer.getShort();
    physubstep = buffer.getShort();
    maxphystep = buffer.getShort();
    misi = buffer.getFloat();
    miststa = buffer.getFloat();
    mistdist = buffer.getFloat();
    misthi = buffer.getFloat();
    starr = buffer.getFloat();
    starg = buffer.getFloat();
    starb = buffer.getFloat();
    stark = buffer.getFloat();
    starsize = buffer.getFloat();
    starmindist = buffer.getFloat();
    stardist = buffer.getFloat();
    starcolnoise = buffer.getFloat();
    dofsta = buffer.getShort();
    dofend = buffer.getShort();
    dofmin = buffer.getShort();
    dofmax = buffer.getShort();
    aodist = buffer.getFloat();
    aodistfac = buffer.getFloat();
    aoenergy = buffer.getFloat();
    aobias = buffer.getFloat();
    aomode = buffer.getShort();
    aosamp = buffer.getShort();
    aomix = buffer.getShort();
    aocolor = buffer.getShort();
    ao_adapt_thresh = buffer.getFloat();
    ao_adapt_speed_fac = buffer.getFloat();
    ao_approx_error = buffer.getFloat();
    ao_approx_correction = buffer.getFloat();
    ao_indirect_energy = buffer.getFloat();
    ao_env_energy = buffer.getFloat();
    ao_pad2 = buffer.getFloat();
    ao_indirect_bounces = buffer.getShort();
    ao_pad = buffer.getShort();
    ao_samp_method = buffer.getShort();
    ao_gather_method = buffer.getShort();
    ao_approx_passes = buffer.getShort();
    flag = buffer.getShort();
    aosphere = DNATools.ptr(buffer); // get ptr
    aotables = DNATools.ptr(buffer); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    for(int i=0;i<mtex.length;i++) mtex[i]=DNATools.link(DNATools.ptr(buffer), MTex.class);
    pr_texture = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeShort(colormodel);
    buffer.writeShort(totex);
    buffer.writeShort(texact);
    buffer.writeShort(mistype);
    buffer.writeFloat(horr);
    buffer.writeFloat(horg);
    buffer.writeFloat(horb);
    buffer.writeFloat(zenr);
    buffer.writeFloat(zeng);
    buffer.writeFloat(zenb);
    buffer.writeFloat(ambr);
    buffer.writeFloat(ambg);
    buffer.writeFloat(ambb);
    buffer.writeFloat(pad2);
    buffer.writeInt(fastcol);
    buffer.writeFloat(exposure);
    buffer.writeFloat(exp);
    buffer.writeFloat(range);
    buffer.writeFloat(linfac);
    buffer.writeFloat(logfac);
    buffer.writeFloat(gravity);
    buffer.writeFloat(activityBoxRadius);
    buffer.writeShort(skytype);
    buffer.writeShort(mode);
    buffer.writeShort(occlusionRes);
    buffer.writeShort(physicsEngine);
    buffer.writeShort(ticrate);
    buffer.writeShort(maxlogicstep);
    buffer.writeShort(physubstep);
    buffer.writeShort(maxphystep);
    buffer.writeFloat(misi);
    buffer.writeFloat(miststa);
    buffer.writeFloat(mistdist);
    buffer.writeFloat(misthi);
    buffer.writeFloat(starr);
    buffer.writeFloat(starg);
    buffer.writeFloat(starb);
    buffer.writeFloat(stark);
    buffer.writeFloat(starsize);
    buffer.writeFloat(starmindist);
    buffer.writeFloat(stardist);
    buffer.writeFloat(starcolnoise);
    buffer.writeShort(dofsta);
    buffer.writeShort(dofend);
    buffer.writeShort(dofmin);
    buffer.writeShort(dofmax);
    buffer.writeFloat(aodist);
    buffer.writeFloat(aodistfac);
    buffer.writeFloat(aoenergy);
    buffer.writeFloat(aobias);
    buffer.writeShort(aomode);
    buffer.writeShort(aosamp);
    buffer.writeShort(aomix);
    buffer.writeShort(aocolor);
    buffer.writeFloat(ao_adapt_thresh);
    buffer.writeFloat(ao_adapt_speed_fac);
    buffer.writeFloat(ao_approx_error);
    buffer.writeFloat(ao_approx_correction);
    buffer.writeFloat(ao_indirect_energy);
    buffer.writeFloat(ao_env_energy);
    buffer.writeFloat(ao_pad2);
    buffer.writeShort(ao_indirect_bounces);
    buffer.writeShort(ao_pad);
    buffer.writeShort(ao_samp_method);
    buffer.writeShort(ao_gather_method);
    buffer.writeShort(ao_approx_passes);
    buffer.writeShort(flag);
    buffer.writeInt(aosphere!=null?aosphere.hashCode():0);
    buffer.writeInt(aotables!=null?aotables.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    for(int i=0;i<mtex.length;i++) buffer.writeInt(mtex[i]!=null?mtex[i].hashCode():0);
    buffer.writeShort(pr_texture);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.writeInt(preview!=null?preview.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (World[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("World:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  colormodel: ").append(colormodel).append("\n");
    sb.append("  totex: ").append(totex).append("\n");
    sb.append("  texact: ").append(texact).append("\n");
    sb.append("  mistype: ").append(mistype).append("\n");
    sb.append("  horr: ").append(horr).append("\n");
    sb.append("  horg: ").append(horg).append("\n");
    sb.append("  horb: ").append(horb).append("\n");
    sb.append("  zenr: ").append(zenr).append("\n");
    sb.append("  zeng: ").append(zeng).append("\n");
    sb.append("  zenb: ").append(zenb).append("\n");
    sb.append("  ambr: ").append(ambr).append("\n");
    sb.append("  ambg: ").append(ambg).append("\n");
    sb.append("  ambb: ").append(ambb).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  fastcol: ").append(fastcol).append("\n");
    sb.append("  exposure: ").append(exposure).append("\n");
    sb.append("  exp: ").append(exp).append("\n");
    sb.append("  range: ").append(range).append("\n");
    sb.append("  linfac: ").append(linfac).append("\n");
    sb.append("  logfac: ").append(logfac).append("\n");
    sb.append("  gravity: ").append(gravity).append("\n");
    sb.append("  activityBoxRadius: ").append(activityBoxRadius).append("\n");
    sb.append("  skytype: ").append(skytype).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  occlusionRes: ").append(occlusionRes).append("\n");
    sb.append("  physicsEngine: ").append(physicsEngine).append("\n");
    sb.append("  ticrate: ").append(ticrate).append("\n");
    sb.append("  maxlogicstep: ").append(maxlogicstep).append("\n");
    sb.append("  physubstep: ").append(physubstep).append("\n");
    sb.append("  maxphystep: ").append(maxphystep).append("\n");
    sb.append("  misi: ").append(misi).append("\n");
    sb.append("  miststa: ").append(miststa).append("\n");
    sb.append("  mistdist: ").append(mistdist).append("\n");
    sb.append("  misthi: ").append(misthi).append("\n");
    sb.append("  starr: ").append(starr).append("\n");
    sb.append("  starg: ").append(starg).append("\n");
    sb.append("  starb: ").append(starb).append("\n");
    sb.append("  stark: ").append(stark).append("\n");
    sb.append("  starsize: ").append(starsize).append("\n");
    sb.append("  starmindist: ").append(starmindist).append("\n");
    sb.append("  stardist: ").append(stardist).append("\n");
    sb.append("  starcolnoise: ").append(starcolnoise).append("\n");
    sb.append("  dofsta: ").append(dofsta).append("\n");
    sb.append("  dofend: ").append(dofend).append("\n");
    sb.append("  dofmin: ").append(dofmin).append("\n");
    sb.append("  dofmax: ").append(dofmax).append("\n");
    sb.append("  aodist: ").append(aodist).append("\n");
    sb.append("  aodistfac: ").append(aodistfac).append("\n");
    sb.append("  aoenergy: ").append(aoenergy).append("\n");
    sb.append("  aobias: ").append(aobias).append("\n");
    sb.append("  aomode: ").append(aomode).append("\n");
    sb.append("  aosamp: ").append(aosamp).append("\n");
    sb.append("  aomix: ").append(aomix).append("\n");
    sb.append("  aocolor: ").append(aocolor).append("\n");
    sb.append("  ao_adapt_thresh: ").append(ao_adapt_thresh).append("\n");
    sb.append("  ao_adapt_speed_fac: ").append(ao_adapt_speed_fac).append("\n");
    sb.append("  ao_approx_error: ").append(ao_approx_error).append("\n");
    sb.append("  ao_approx_correction: ").append(ao_approx_correction).append("\n");
    sb.append("  ao_indirect_energy: ").append(ao_indirect_energy).append("\n");
    sb.append("  ao_env_energy: ").append(ao_env_energy).append("\n");
    sb.append("  ao_pad2: ").append(ao_pad2).append("\n");
    sb.append("  ao_indirect_bounces: ").append(ao_indirect_bounces).append("\n");
    sb.append("  ao_pad: ").append(ao_pad).append("\n");
    sb.append("  ao_samp_method: ").append(ao_samp_method).append("\n");
    sb.append("  ao_gather_method: ").append(ao_gather_method).append("\n");
    sb.append("  ao_approx_passes: ").append(ao_approx_passes).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  aosphere: ").append(aosphere).append("\n");
    sb.append("  aotables: ").append(aotables).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  mtex: ").append(Arrays.toString(mtex)).append("\n");
    sb.append("  pr_texture: ").append(pr_texture).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    return sb.toString();
  }
  public World copy() { try {return (World)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

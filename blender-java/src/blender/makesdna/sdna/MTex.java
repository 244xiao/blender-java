package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MTex implements DNA, Cloneable { // #24
  public MTex[] myarray;
  public short texco; // 2
  public short mapto; // 2
  public short maptoneg; // 2
  public short blendtype; // 2
  public bObject object; // ptr 1296
  public Tex tex; // ptr 368
  public byte[] uvname = new byte[32]; // 1
  public byte projx; // 1
  public byte projy; // 1
  public byte projz; // 1
  public byte mapping; // 1
  public float[] ofs = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float rot; // 4
  public short texflag; // 2
  public short colormodel; // 2
  public short pmapto; // 2
  public short pmaptoneg; // 2
  public short normapspace; // 2
  public short which_output; // 2
  public byte brush_map_mode; // 1
  public byte[] pad = new byte[7]; // 1
  public float r; // 4
  public float g; // 4
  public float b; // 4
  public float k; // 4
  public float def_var; // 4
  public float rt; // 4
  public float colfac; // 4
  public float varfac; // 4
  public float norfac; // 4
  public float dispfac; // 4
  public float warpfac; // 4
  public float colspecfac; // 4
  public float mirrfac; // 4
  public float alphafac; // 4
  public float difffac; // 4
  public float specfac; // 4
  public float emitfac; // 4
  public float hardfac; // 4
  public float raymirrfac; // 4
  public float translfac; // 4
  public float ambfac; // 4
  public float colemitfac; // 4
  public float colreflfac; // 4
  public float coltransfac; // 4
  public float densfac; // 4
  public float scatterfac; // 4
  public float reflfac; // 4
  public float timefac; // 4
  public float lengthfac; // 4
  public float clumpfac; // 4
  public float dampfac; // 4
  public float kinkfac; // 4
  public float roughfac; // 4
  public float padensfac; // 4
  public float gravityfac; // 4
  public float lifefac; // 4
  public float sizefac; // 4
  public float ivelfac; // 4
  public float fieldfac; // 4
  public float shadowfac; // 4
  public float zenupfac; // 4
  public float zendownfac; // 4
  public float blendfac; // 4

  public void read(ByteBuffer buffer) {
    texco = buffer.getShort();
    mapto = buffer.getShort();
    maptoneg = buffer.getShort();
    blendtype = buffer.getShort();
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    tex = DNATools.link(DNATools.ptr(buffer), Tex.class); // get ptr
    buffer.get(uvname);
    projx = buffer.get();
    projy = buffer.get();
    projz = buffer.get();
    mapping = buffer.get();
    for(int i=0;i<ofs.length;i++) ofs[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    rot = buffer.getFloat();
    texflag = buffer.getShort();
    colormodel = buffer.getShort();
    pmapto = buffer.getShort();
    pmaptoneg = buffer.getShort();
    normapspace = buffer.getShort();
    which_output = buffer.getShort();
    brush_map_mode = buffer.get();
    buffer.get(pad);
    r = buffer.getFloat();
    g = buffer.getFloat();
    b = buffer.getFloat();
    k = buffer.getFloat();
    def_var = buffer.getFloat();
    rt = buffer.getFloat();
    colfac = buffer.getFloat();
    varfac = buffer.getFloat();
    norfac = buffer.getFloat();
    dispfac = buffer.getFloat();
    warpfac = buffer.getFloat();
    colspecfac = buffer.getFloat();
    mirrfac = buffer.getFloat();
    alphafac = buffer.getFloat();
    difffac = buffer.getFloat();
    specfac = buffer.getFloat();
    emitfac = buffer.getFloat();
    hardfac = buffer.getFloat();
    raymirrfac = buffer.getFloat();
    translfac = buffer.getFloat();
    ambfac = buffer.getFloat();
    colemitfac = buffer.getFloat();
    colreflfac = buffer.getFloat();
    coltransfac = buffer.getFloat();
    densfac = buffer.getFloat();
    scatterfac = buffer.getFloat();
    reflfac = buffer.getFloat();
    timefac = buffer.getFloat();
    lengthfac = buffer.getFloat();
    clumpfac = buffer.getFloat();
    dampfac = buffer.getFloat();
    kinkfac = buffer.getFloat();
    roughfac = buffer.getFloat();
    padensfac = buffer.getFloat();
    gravityfac = buffer.getFloat();
    lifefac = buffer.getFloat();
    sizefac = buffer.getFloat();
    ivelfac = buffer.getFloat();
    fieldfac = buffer.getFloat();
    shadowfac = buffer.getFloat();
    zenupfac = buffer.getFloat();
    zendownfac = buffer.getFloat();
    blendfac = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(texco);
    buffer.writeShort(mapto);
    buffer.writeShort(maptoneg);
    buffer.writeShort(blendtype);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeInt(tex!=null?tex.hashCode():0);
    buffer.write(uvname);
    buffer.writeByte(projx);
    buffer.writeByte(projy);
    buffer.writeByte(projz);
    buffer.writeByte(mapping);
    for(int i=0;i<ofs.length;i++) buffer.writeFloat(ofs[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    buffer.writeFloat(rot);
    buffer.writeShort(texflag);
    buffer.writeShort(colormodel);
    buffer.writeShort(pmapto);
    buffer.writeShort(pmaptoneg);
    buffer.writeShort(normapspace);
    buffer.writeShort(which_output);
    buffer.writeByte(brush_map_mode);
    buffer.write(pad);
    buffer.writeFloat(r);
    buffer.writeFloat(g);
    buffer.writeFloat(b);
    buffer.writeFloat(k);
    buffer.writeFloat(def_var);
    buffer.writeFloat(rt);
    buffer.writeFloat(colfac);
    buffer.writeFloat(varfac);
    buffer.writeFloat(norfac);
    buffer.writeFloat(dispfac);
    buffer.writeFloat(warpfac);
    buffer.writeFloat(colspecfac);
    buffer.writeFloat(mirrfac);
    buffer.writeFloat(alphafac);
    buffer.writeFloat(difffac);
    buffer.writeFloat(specfac);
    buffer.writeFloat(emitfac);
    buffer.writeFloat(hardfac);
    buffer.writeFloat(raymirrfac);
    buffer.writeFloat(translfac);
    buffer.writeFloat(ambfac);
    buffer.writeFloat(colemitfac);
    buffer.writeFloat(colreflfac);
    buffer.writeFloat(coltransfac);
    buffer.writeFloat(densfac);
    buffer.writeFloat(scatterfac);
    buffer.writeFloat(reflfac);
    buffer.writeFloat(timefac);
    buffer.writeFloat(lengthfac);
    buffer.writeFloat(clumpfac);
    buffer.writeFloat(dampfac);
    buffer.writeFloat(kinkfac);
    buffer.writeFloat(roughfac);
    buffer.writeFloat(padensfac);
    buffer.writeFloat(gravityfac);
    buffer.writeFloat(lifefac);
    buffer.writeFloat(sizefac);
    buffer.writeFloat(ivelfac);
    buffer.writeFloat(fieldfac);
    buffer.writeFloat(shadowfac);
    buffer.writeFloat(zenupfac);
    buffer.writeFloat(zendownfac);
    buffer.writeFloat(blendfac);
  }
  public Object setmyarray(Object array) {
    myarray = (MTex[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MTex:\n");
    sb.append("  texco: ").append(texco).append("\n");
    sb.append("  mapto: ").append(mapto).append("\n");
    sb.append("  maptoneg: ").append(maptoneg).append("\n");
    sb.append("  blendtype: ").append(blendtype).append("\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  tex: ").append(tex).append("\n");
    sb.append("  uvname: ").append(new String(uvname)).append("\n");
    sb.append("  projx: ").append(projx).append("\n");
    sb.append("  projy: ").append(projy).append("\n");
    sb.append("  projz: ").append(projz).append("\n");
    sb.append("  mapping: ").append(mapping).append("\n");
    sb.append("  ofs: ").append(Arrays.toString(ofs)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  rot: ").append(rot).append("\n");
    sb.append("  texflag: ").append(texflag).append("\n");
    sb.append("  colormodel: ").append(colormodel).append("\n");
    sb.append("  pmapto: ").append(pmapto).append("\n");
    sb.append("  pmaptoneg: ").append(pmaptoneg).append("\n");
    sb.append("  normapspace: ").append(normapspace).append("\n");
    sb.append("  which_output: ").append(which_output).append("\n");
    sb.append("  brush_map_mode: ").append(brush_map_mode).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  g: ").append(g).append("\n");
    sb.append("  b: ").append(b).append("\n");
    sb.append("  k: ").append(k).append("\n");
    sb.append("  def_var: ").append(def_var).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  colfac: ").append(colfac).append("\n");
    sb.append("  varfac: ").append(varfac).append("\n");
    sb.append("  norfac: ").append(norfac).append("\n");
    sb.append("  dispfac: ").append(dispfac).append("\n");
    sb.append("  warpfac: ").append(warpfac).append("\n");
    sb.append("  colspecfac: ").append(colspecfac).append("\n");
    sb.append("  mirrfac: ").append(mirrfac).append("\n");
    sb.append("  alphafac: ").append(alphafac).append("\n");
    sb.append("  difffac: ").append(difffac).append("\n");
    sb.append("  specfac: ").append(specfac).append("\n");
    sb.append("  emitfac: ").append(emitfac).append("\n");
    sb.append("  hardfac: ").append(hardfac).append("\n");
    sb.append("  raymirrfac: ").append(raymirrfac).append("\n");
    sb.append("  translfac: ").append(translfac).append("\n");
    sb.append("  ambfac: ").append(ambfac).append("\n");
    sb.append("  colemitfac: ").append(colemitfac).append("\n");
    sb.append("  colreflfac: ").append(colreflfac).append("\n");
    sb.append("  coltransfac: ").append(coltransfac).append("\n");
    sb.append("  densfac: ").append(densfac).append("\n");
    sb.append("  scatterfac: ").append(scatterfac).append("\n");
    sb.append("  reflfac: ").append(reflfac).append("\n");
    sb.append("  timefac: ").append(timefac).append("\n");
    sb.append("  lengthfac: ").append(lengthfac).append("\n");
    sb.append("  clumpfac: ").append(clumpfac).append("\n");
    sb.append("  dampfac: ").append(dampfac).append("\n");
    sb.append("  kinkfac: ").append(kinkfac).append("\n");
    sb.append("  roughfac: ").append(roughfac).append("\n");
    sb.append("  padensfac: ").append(padensfac).append("\n");
    sb.append("  gravityfac: ").append(gravityfac).append("\n");
    sb.append("  lifefac: ").append(lifefac).append("\n");
    sb.append("  sizefac: ").append(sizefac).append("\n");
    sb.append("  ivelfac: ").append(ivelfac).append("\n");
    sb.append("  fieldfac: ").append(fieldfac).append("\n");
    sb.append("  shadowfac: ").append(shadowfac).append("\n");
    sb.append("  zenupfac: ").append(zenupfac).append("\n");
    sb.append("  zendownfac: ").append(zendownfac).append("\n");
    sb.append("  blendfac: ").append(blendfac).append("\n");
    return sb.toString();
  }
  public MTex copy() { try {return (MTex)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FluidsimSettings implements DNA, Cloneable { // #125
  public FluidsimSettings[] myarray;
  public FluidsimModifierData fmd; // ptr 96
  public short type; // 2
  public short show_advancedoptions; // 2
  public short resolutionxyz; // 2
  public short previewresxyz; // 2
  public float realsize; // 4
  public short guiDisplayMode; // 2
  public short renderDisplayMode; // 2
  public float viscosityValue; // 4
  public short viscosityMode; // 2
  public short viscosityExponent; // 2
  public float gravx; // 4
  public float gravy; // 4
  public float gravz; // 4
  public float animStart; // 4
  public float animEnd; // 4
  public int bakeStart; // 4
  public int bakeEnd; // 4
  public float gstar; // 4
  public int maxRefine; // 4
  public float iniVelx; // 4
  public float iniVely; // 4
  public float iniVelz; // 4
  public Mesh orgMesh; // ptr 408
  public Mesh meshSurface; // ptr 408
  public Mesh meshBB; // ptr 408
  public byte[] surfdataPath = new byte[240]; // 1
  public float[] bbStart = new float[3]; // 4
  public float[] bbSize = new float[3]; // 4
  public Ipo ipo; // ptr 112
  public short typeFlags; // 2
  public byte domainNovecgen; // 1
  public byte volumeInitType; // 1
  public float partSlipValue; // 4
  public int generateTracers; // 4
  public float generateParticles; // 4
  public float surfaceSmoothing; // 4
  public int surfaceSubdivs; // 4
  public int flag; // 4
  public float particleInfSize; // 4
  public float particleInfAlpha; // 4
  public float farFieldSize; // 4
  public MVert meshSurfNormals; // ptr 20
  public float cpsTimeStart; // 4
  public float cpsTimeEnd; // 4
  public float cpsQuality; // 4
  public float attractforceStrength; // 4
  public float attractforceRadius; // 4
  public float velocityforceStrength; // 4
  public float velocityforceRadius; // 4
  public int lastgoodframe; // 4

  public void read(ByteBuffer buffer) {
    fmd = DNATools.link(DNATools.ptr(buffer), FluidsimModifierData.class); // get ptr
    type = buffer.getShort();
    show_advancedoptions = buffer.getShort();
    resolutionxyz = buffer.getShort();
    previewresxyz = buffer.getShort();
    realsize = buffer.getFloat();
    guiDisplayMode = buffer.getShort();
    renderDisplayMode = buffer.getShort();
    viscosityValue = buffer.getFloat();
    viscosityMode = buffer.getShort();
    viscosityExponent = buffer.getShort();
    gravx = buffer.getFloat();
    gravy = buffer.getFloat();
    gravz = buffer.getFloat();
    animStart = buffer.getFloat();
    animEnd = buffer.getFloat();
    bakeStart = buffer.getInt();
    bakeEnd = buffer.getInt();
    gstar = buffer.getFloat();
    maxRefine = buffer.getInt();
    iniVelx = buffer.getFloat();
    iniVely = buffer.getFloat();
    iniVelz = buffer.getFloat();
    orgMesh = DNATools.link(DNATools.ptr(buffer), Mesh.class); // get ptr
    meshSurface = DNATools.link(DNATools.ptr(buffer), Mesh.class); // get ptr
    meshBB = DNATools.link(DNATools.ptr(buffer), Mesh.class); // get ptr
    buffer.get(surfdataPath);
    for(int i=0;i<bbStart.length;i++) bbStart[i]=buffer.getFloat();
    for(int i=0;i<bbSize.length;i++) bbSize[i]=buffer.getFloat();
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    typeFlags = buffer.getShort();
    domainNovecgen = buffer.get();
    volumeInitType = buffer.get();
    partSlipValue = buffer.getFloat();
    generateTracers = buffer.getInt();
    generateParticles = buffer.getFloat();
    surfaceSmoothing = buffer.getFloat();
    surfaceSubdivs = buffer.getInt();
    flag = buffer.getInt();
    particleInfSize = buffer.getFloat();
    particleInfAlpha = buffer.getFloat();
    farFieldSize = buffer.getFloat();
    meshSurfNormals = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    cpsTimeStart = buffer.getFloat();
    cpsTimeEnd = buffer.getFloat();
    cpsQuality = buffer.getFloat();
    attractforceStrength = buffer.getFloat();
    attractforceRadius = buffer.getFloat();
    velocityforceStrength = buffer.getFloat();
    velocityforceRadius = buffer.getFloat();
    lastgoodframe = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(fmd!=null?fmd.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(show_advancedoptions);
    buffer.writeShort(resolutionxyz);
    buffer.writeShort(previewresxyz);
    buffer.writeFloat(realsize);
    buffer.writeShort(guiDisplayMode);
    buffer.writeShort(renderDisplayMode);
    buffer.writeFloat(viscosityValue);
    buffer.writeShort(viscosityMode);
    buffer.writeShort(viscosityExponent);
    buffer.writeFloat(gravx);
    buffer.writeFloat(gravy);
    buffer.writeFloat(gravz);
    buffer.writeFloat(animStart);
    buffer.writeFloat(animEnd);
    buffer.writeInt(bakeStart);
    buffer.writeInt(bakeEnd);
    buffer.writeFloat(gstar);
    buffer.writeInt(maxRefine);
    buffer.writeFloat(iniVelx);
    buffer.writeFloat(iniVely);
    buffer.writeFloat(iniVelz);
    buffer.writeInt(orgMesh!=null?orgMesh.hashCode():0);
    buffer.writeInt(meshSurface!=null?meshSurface.hashCode():0);
    buffer.writeInt(meshBB!=null?meshBB.hashCode():0);
    buffer.write(surfdataPath);
    for(int i=0;i<bbStart.length;i++) buffer.writeFloat(bbStart[i]);
    for(int i=0;i<bbSize.length;i++) buffer.writeFloat(bbSize[i]);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeShort(typeFlags);
    buffer.writeByte(domainNovecgen);
    buffer.writeByte(volumeInitType);
    buffer.writeFloat(partSlipValue);
    buffer.writeInt(generateTracers);
    buffer.writeFloat(generateParticles);
    buffer.writeFloat(surfaceSmoothing);
    buffer.writeInt(surfaceSubdivs);
    buffer.writeInt(flag);
    buffer.writeFloat(particleInfSize);
    buffer.writeFloat(particleInfAlpha);
    buffer.writeFloat(farFieldSize);
    buffer.writeInt(meshSurfNormals!=null?meshSurfNormals.hashCode():0);
    buffer.writeFloat(cpsTimeStart);
    buffer.writeFloat(cpsTimeEnd);
    buffer.writeFloat(cpsQuality);
    buffer.writeFloat(attractforceStrength);
    buffer.writeFloat(attractforceRadius);
    buffer.writeFloat(velocityforceStrength);
    buffer.writeFloat(velocityforceRadius);
    buffer.writeInt(lastgoodframe);
  }
  public Object setmyarray(Object array) {
    myarray = (FluidsimSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FluidsimSettings:\n");
    sb.append("  fmd: ").append(fmd).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  show_advancedoptions: ").append(show_advancedoptions).append("\n");
    sb.append("  resolutionxyz: ").append(resolutionxyz).append("\n");
    sb.append("  previewresxyz: ").append(previewresxyz).append("\n");
    sb.append("  realsize: ").append(realsize).append("\n");
    sb.append("  guiDisplayMode: ").append(guiDisplayMode).append("\n");
    sb.append("  renderDisplayMode: ").append(renderDisplayMode).append("\n");
    sb.append("  viscosityValue: ").append(viscosityValue).append("\n");
    sb.append("  viscosityMode: ").append(viscosityMode).append("\n");
    sb.append("  viscosityExponent: ").append(viscosityExponent).append("\n");
    sb.append("  gravx: ").append(gravx).append("\n");
    sb.append("  gravy: ").append(gravy).append("\n");
    sb.append("  gravz: ").append(gravz).append("\n");
    sb.append("  animStart: ").append(animStart).append("\n");
    sb.append("  animEnd: ").append(animEnd).append("\n");
    sb.append("  bakeStart: ").append(bakeStart).append("\n");
    sb.append("  bakeEnd: ").append(bakeEnd).append("\n");
    sb.append("  gstar: ").append(gstar).append("\n");
    sb.append("  maxRefine: ").append(maxRefine).append("\n");
    sb.append("  iniVelx: ").append(iniVelx).append("\n");
    sb.append("  iniVely: ").append(iniVely).append("\n");
    sb.append("  iniVelz: ").append(iniVelz).append("\n");
    sb.append("  orgMesh: ").append(orgMesh).append("\n");
    sb.append("  meshSurface: ").append(meshSurface).append("\n");
    sb.append("  meshBB: ").append(meshBB).append("\n");
    sb.append("  surfdataPath: ").append(new String(surfdataPath)).append("\n");
    sb.append("  bbStart: ").append(Arrays.toString(bbStart)).append("\n");
    sb.append("  bbSize: ").append(Arrays.toString(bbSize)).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  typeFlags: ").append(typeFlags).append("\n");
    sb.append("  domainNovecgen: ").append(domainNovecgen).append("\n");
    sb.append("  volumeInitType: ").append(volumeInitType).append("\n");
    sb.append("  partSlipValue: ").append(partSlipValue).append("\n");
    sb.append("  generateTracers: ").append(generateTracers).append("\n");
    sb.append("  generateParticles: ").append(generateParticles).append("\n");
    sb.append("  surfaceSmoothing: ").append(surfaceSmoothing).append("\n");
    sb.append("  surfaceSubdivs: ").append(surfaceSubdivs).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  particleInfSize: ").append(particleInfSize).append("\n");
    sb.append("  particleInfAlpha: ").append(particleInfAlpha).append("\n");
    sb.append("  farFieldSize: ").append(farFieldSize).append("\n");
    sb.append("  meshSurfNormals: ").append(meshSurfNormals).append("\n");
    sb.append("  cpsTimeStart: ").append(cpsTimeStart).append("\n");
    sb.append("  cpsTimeEnd: ").append(cpsTimeEnd).append("\n");
    sb.append("  cpsQuality: ").append(cpsQuality).append("\n");
    sb.append("  attractforceStrength: ").append(attractforceStrength).append("\n");
    sb.append("  attractforceRadius: ").append(attractforceRadius).append("\n");
    sb.append("  velocityforceStrength: ").append(velocityforceStrength).append("\n");
    sb.append("  velocityforceRadius: ").append(velocityforceRadius).append("\n");
    sb.append("  lastgoodframe: ").append(lastgoodframe).append("\n");
    return sb.toString();
  }
  public FluidsimSettings copy() { try {return (FluidsimSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

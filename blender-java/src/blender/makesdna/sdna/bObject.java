package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bObject extends ID implements DNA, Cloneable { // #114
  public bObject[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public Object sculpt; // ptr (SculptSession) 0
  public short type; // 2
  public short partype; // 2
  public int par1; // 4
  public int par2; // 4
  public int par3; // 4
  public byte[] parsubstr = new byte[32]; // 1
  public bObject parent; // ptr 1296
  public bObject track; // ptr 1296
  public bObject proxy; // ptr 1296
  public bObject proxy_group; // ptr 1296
  public bObject proxy_from; // ptr 1296
  public Ipo ipo; // ptr 112
  public Object path; // ptr (Path) 0
  public BoundBox bb; // ptr 104
  public bAction action; // ptr 152
  public bAction poselib; // ptr 152
  public bPose pose; // ptr 184
  public Object data; // ptr 0
  public bGPdata gpd; // ptr 104
  public bAnimVizSettings avs = new bAnimVizSettings(); // 48
  public bMotionPath mpath; // ptr 24
  public ListBase constraintChannels = new ListBase(); // 16
  public ListBase effect = new ListBase(); // 16
  public ListBase disp = new ListBase(); // 16
  public ListBase defbase = new ListBase(); // 16
  public ListBase modifiers = new ListBase(); // 16
  public int mode; // 4
  public int restore_mode; // 4
  public Material[] mat; // ptr 800
  public Object matbits; // ptr 1
  public int totcol; // 4
  public int actcol; // 4
  public float[] loc = new float[3]; // 4
  public float[] dloc = new float[3]; // 4
  public float[] orig = new float[3]; // 4
  public float[] size = new float[3]; // 4
  public float[] dsize = new float[3]; // 4
  public float[] rot = new float[3]; // 4
  public float[] drot = new float[3]; // 4
  public float[] quat = new float[4]; // 4
  public float[] dquat = new float[4]; // 4
  public float[] rotAxis = new float[3]; // 4
  public float[] drotAxis = new float[3]; // 4
  public float rotAngle; // 4
  public float drotAngle; // 4
  public float[][] obmat = new float[4][4]; // 4
  public float[][] parentinv = new float[4][4]; // 4
  public float[][] constinv = new float[4][4]; // 4
  public float[][] imat = new float[4][4]; // 4
  public float[][] imat_ren = new float[4][4]; // 4
  public int lay; // 4
  public short flag; // 2
  public short colbits; // 2
  public short transflag; // 2
  public short protectflag; // 2
  public short trackflag; // 2
  public short upflag; // 2
  public short nlaflag; // 2
  public short ipoflag; // 2
  public short ipowin; // 2
  public short scaflag; // 2
  public short scavisflag; // 2
  public short boundtype; // 2
  public int dupon; // 4
  public int dupoff; // 4
  public int dupsta; // 4
  public int dupend; // 4
  public float sf; // 4
  public float ctime; // 4
  public float mass; // 4
  public float damping; // 4
  public float inertia; // 4
  public float formfactor; // 4
  public float rdamping; // 4
  public float sizefac; // 4
  public float margin; // 4
  public float max_vel; // 4
  public float min_vel; // 4
  public float m_contactProcessingThreshold; // 4
  public short rotmode; // 2
  public byte dt; // 1
  public byte dtx; // 1
  public byte empty_drawtype; // 1
  public byte[] pad1 = new byte[3]; // 1
  public float empty_drawsize; // 4
  public float dupfacesca; // 4
  public ListBase prop = new ListBase(); // 16
  public ListBase sensors = new ListBase(); // 16
  public ListBase controllers = new ListBase(); // 16
  public ListBase actuators = new ListBase(); // 16
  public float[] bbsize = new float[3]; // 4
  public short index; // 2
  public short actdef; // 2
  public float[] col = new float[4]; // 4
  public int gameflag; // 4
  public int gameflag2; // 4
  public BulletSoftBody bsoft; // ptr 120
  public short softflag; // 2
  public short recalc; // 2
  public float[] anisotropicFriction = new float[3]; // 4
  public ListBase constraints = new ListBase(); // 16
  public ListBase nlastrips = new ListBase(); // 16
  public ListBase hooks = new ListBase(); // 16
  public ListBase particlesystem = new ListBase(); // 16
  public PartDeflect pd; // ptr 152
  public SoftBody soft; // ptr 376
  public Group dup_group; // ptr 104
  public short fluidsimFlag; // 2
  public short restrictflag; // 2
  public short shapenr; // 2
  public short shapeflag; // 2
  public float smoothresh; // 4
  public short recalco; // 2
  public short body_type; // 2
  public FluidsimSettings fluidsimSettings; // ptr 456
  public Object derivedDeform; // ptr (DerivedMesh) 0
  public Object derivedFinal; // ptr (DerivedMesh) 0
  public int lastDataMask; // 4
  public int state; // 4
  public int init_state; // 4
  public int pad2; // 4
  public ListBase gpulamp = new ListBase(); // 16
  public ListBase pc_ids = new ListBase(); // 16
  public ListBase duplilist; // ptr 16

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    sculpt = DNATools.ptr(buffer); // get ptr
    type = buffer.getShort();
    partype = buffer.getShort();
    par1 = buffer.getInt();
    par2 = buffer.getInt();
    par3 = buffer.getInt();
    buffer.get(parsubstr);
    parent = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    track = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    proxy = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    proxy_group = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    proxy_from = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    path = DNATools.ptr(buffer); // get ptr
    bb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    action = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    poselib = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    pose = DNATools.link(DNATools.ptr(buffer), bPose.class); // get ptr
    data = DNATools.ptr(buffer); // get ptr
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
    avs.read(buffer);
    mpath = DNATools.link(DNATools.ptr(buffer), bMotionPath.class); // get ptr
    constraintChannels.read(buffer);
    effect.read(buffer);
    disp.read(buffer);
    defbase.read(buffer);
    modifiers.read(buffer);
    mode = buffer.getInt();
    restore_mode = buffer.getInt();
    mat = DNATools.link(DNATools.ptr(buffer), Material[].class); // get ptr
    matbits = DNATools.ptr(buffer); // get ptr
    totcol = buffer.getInt();
    actcol = buffer.getInt();
    for(int i=0;i<loc.length;i++) loc[i]=buffer.getFloat();
    for(int i=0;i<dloc.length;i++) dloc[i]=buffer.getFloat();
    for(int i=0;i<orig.length;i++) orig[i]=buffer.getFloat();
    for(int i=0;i<size.length;i++) size[i]=buffer.getFloat();
    for(int i=0;i<dsize.length;i++) dsize[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    for(int i=0;i<drot.length;i++) drot[i]=buffer.getFloat();
    for(int i=0;i<quat.length;i++) quat[i]=buffer.getFloat();
    for(int i=0;i<dquat.length;i++) dquat[i]=buffer.getFloat();
    for(int i=0;i<rotAxis.length;i++) rotAxis[i]=buffer.getFloat();
    for(int i=0;i<drotAxis.length;i++) drotAxis[i]=buffer.getFloat();
    rotAngle = buffer.getFloat();
    drotAngle = buffer.getFloat();
    for(int i=0;i<obmat.length;i++) for(int j=0;j<obmat[i].length;j++) obmat[i][j]=buffer.getFloat();
    for(int i=0;i<parentinv.length;i++) for(int j=0;j<parentinv[i].length;j++) parentinv[i][j]=buffer.getFloat();
    for(int i=0;i<constinv.length;i++) for(int j=0;j<constinv[i].length;j++) constinv[i][j]=buffer.getFloat();
    for(int i=0;i<imat.length;i++) for(int j=0;j<imat[i].length;j++) imat[i][j]=buffer.getFloat();
    for(int i=0;i<imat_ren.length;i++) for(int j=0;j<imat_ren[i].length;j++) imat_ren[i][j]=buffer.getFloat();
    lay = buffer.getInt();
    flag = buffer.getShort();
    colbits = buffer.getShort();
    transflag = buffer.getShort();
    protectflag = buffer.getShort();
    trackflag = buffer.getShort();
    upflag = buffer.getShort();
    nlaflag = buffer.getShort();
    ipoflag = buffer.getShort();
    ipowin = buffer.getShort();
    scaflag = buffer.getShort();
    scavisflag = buffer.getShort();
    boundtype = buffer.getShort();
    dupon = buffer.getInt();
    dupoff = buffer.getInt();
    dupsta = buffer.getInt();
    dupend = buffer.getInt();
    sf = buffer.getFloat();
    ctime = buffer.getFloat();
    mass = buffer.getFloat();
    damping = buffer.getFloat();
    inertia = buffer.getFloat();
    formfactor = buffer.getFloat();
    rdamping = buffer.getFloat();
    sizefac = buffer.getFloat();
    margin = buffer.getFloat();
    max_vel = buffer.getFloat();
    min_vel = buffer.getFloat();
    m_contactProcessingThreshold = buffer.getFloat();
    rotmode = buffer.getShort();
    dt = buffer.get();
    dtx = buffer.get();
    empty_drawtype = buffer.get();
    buffer.get(pad1);
    empty_drawsize = buffer.getFloat();
    dupfacesca = buffer.getFloat();
    prop.read(buffer);
    sensors.read(buffer);
    controllers.read(buffer);
    actuators.read(buffer);
    for(int i=0;i<bbsize.length;i++) bbsize[i]=buffer.getFloat();
    index = buffer.getShort();
    actdef = buffer.getShort();
    for(int i=0;i<col.length;i++) col[i]=buffer.getFloat();
    gameflag = buffer.getInt();
    gameflag2 = buffer.getInt();
    bsoft = DNATools.link(DNATools.ptr(buffer), BulletSoftBody.class); // get ptr
    softflag = buffer.getShort();
    recalc = buffer.getShort();
    for(int i=0;i<anisotropicFriction.length;i++) anisotropicFriction[i]=buffer.getFloat();
    constraints.read(buffer);
    nlastrips.read(buffer);
    hooks.read(buffer);
    particlesystem.read(buffer);
    pd = DNATools.link(DNATools.ptr(buffer), PartDeflect.class); // get ptr
    soft = DNATools.link(DNATools.ptr(buffer), SoftBody.class); // get ptr
    dup_group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
    fluidsimFlag = buffer.getShort();
    restrictflag = buffer.getShort();
    shapenr = buffer.getShort();
    shapeflag = buffer.getShort();
    smoothresh = buffer.getFloat();
    recalco = buffer.getShort();
    body_type = buffer.getShort();
    fluidsimSettings = DNATools.link(DNATools.ptr(buffer), FluidsimSettings.class); // get ptr
    derivedDeform = DNATools.ptr(buffer); // get ptr
    derivedFinal = DNATools.ptr(buffer); // get ptr
    lastDataMask = buffer.getInt();
    state = buffer.getInt();
    init_state = buffer.getInt();
    pad2 = buffer.getInt();
    gpulamp.read(buffer);
    pc_ids.read(buffer);
    duplilist = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(sculpt!=null?sculpt.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(partype);
    buffer.writeInt(par1);
    buffer.writeInt(par2);
    buffer.writeInt(par3);
    buffer.write(parsubstr);
    buffer.writeInt(parent!=null?parent.hashCode():0);
    buffer.writeInt(track!=null?track.hashCode():0);
    buffer.writeInt(proxy!=null?proxy.hashCode():0);
    buffer.writeInt(proxy_group!=null?proxy_group.hashCode():0);
    buffer.writeInt(proxy_from!=null?proxy_from.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(path!=null?path.hashCode():0);
    buffer.writeInt(bb!=null?bb.hashCode():0);
    buffer.writeInt(action!=null?action.hashCode():0);
    buffer.writeInt(poselib!=null?poselib.hashCode():0);
    buffer.writeInt(pose!=null?pose.hashCode():0);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
    avs.write(buffer);
    buffer.writeInt(mpath!=null?mpath.hashCode():0);
    constraintChannels.write(buffer);
    effect.write(buffer);
    disp.write(buffer);
    defbase.write(buffer);
    modifiers.write(buffer);
    buffer.writeInt(mode);
    buffer.writeInt(restore_mode);
    buffer.writeInt(mat!=null?mat.hashCode():0);
    buffer.writeInt(matbits!=null?matbits.hashCode():0);
    buffer.writeInt(totcol);
    buffer.writeInt(actcol);
    for(int i=0;i<loc.length;i++) buffer.writeFloat(loc[i]);
    for(int i=0;i<dloc.length;i++) buffer.writeFloat(dloc[i]);
    for(int i=0;i<orig.length;i++) buffer.writeFloat(orig[i]);
    for(int i=0;i<size.length;i++) buffer.writeFloat(size[i]);
    for(int i=0;i<dsize.length;i++) buffer.writeFloat(dsize[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    for(int i=0;i<drot.length;i++) buffer.writeFloat(drot[i]);
    for(int i=0;i<quat.length;i++) buffer.writeFloat(quat[i]);
    for(int i=0;i<dquat.length;i++) buffer.writeFloat(dquat[i]);
    for(int i=0;i<rotAxis.length;i++) buffer.writeFloat(rotAxis[i]);
    for(int i=0;i<drotAxis.length;i++) buffer.writeFloat(drotAxis[i]);
    buffer.writeFloat(rotAngle);
    buffer.writeFloat(drotAngle);
    for(int i=0; i<obmat.length; i++)  for(int j=0;j<obmat[i].length;j++) buffer.writeFloat(obmat[i][j]);
    for(int i=0; i<parentinv.length; i++)  for(int j=0;j<parentinv[i].length;j++) buffer.writeFloat(parentinv[i][j]);
    for(int i=0; i<constinv.length; i++)  for(int j=0;j<constinv[i].length;j++) buffer.writeFloat(constinv[i][j]);
    for(int i=0; i<imat.length; i++)  for(int j=0;j<imat[i].length;j++) buffer.writeFloat(imat[i][j]);
    for(int i=0; i<imat_ren.length; i++)  for(int j=0;j<imat_ren[i].length;j++) buffer.writeFloat(imat_ren[i][j]);
    buffer.writeInt(lay);
    buffer.writeShort(flag);
    buffer.writeShort(colbits);
    buffer.writeShort(transflag);
    buffer.writeShort(protectflag);
    buffer.writeShort(trackflag);
    buffer.writeShort(upflag);
    buffer.writeShort(nlaflag);
    buffer.writeShort(ipoflag);
    buffer.writeShort(ipowin);
    buffer.writeShort(scaflag);
    buffer.writeShort(scavisflag);
    buffer.writeShort(boundtype);
    buffer.writeInt(dupon);
    buffer.writeInt(dupoff);
    buffer.writeInt(dupsta);
    buffer.writeInt(dupend);
    buffer.writeFloat(sf);
    buffer.writeFloat(ctime);
    buffer.writeFloat(mass);
    buffer.writeFloat(damping);
    buffer.writeFloat(inertia);
    buffer.writeFloat(formfactor);
    buffer.writeFloat(rdamping);
    buffer.writeFloat(sizefac);
    buffer.writeFloat(margin);
    buffer.writeFloat(max_vel);
    buffer.writeFloat(min_vel);
    buffer.writeFloat(m_contactProcessingThreshold);
    buffer.writeShort(rotmode);
    buffer.writeByte(dt);
    buffer.writeByte(dtx);
    buffer.writeByte(empty_drawtype);
    buffer.write(pad1);
    buffer.writeFloat(empty_drawsize);
    buffer.writeFloat(dupfacesca);
    prop.write(buffer);
    sensors.write(buffer);
    controllers.write(buffer);
    actuators.write(buffer);
    for(int i=0;i<bbsize.length;i++) buffer.writeFloat(bbsize[i]);
    buffer.writeShort(index);
    buffer.writeShort(actdef);
    for(int i=0;i<col.length;i++) buffer.writeFloat(col[i]);
    buffer.writeInt(gameflag);
    buffer.writeInt(gameflag2);
    buffer.writeInt(bsoft!=null?bsoft.hashCode():0);
    buffer.writeShort(softflag);
    buffer.writeShort(recalc);
    for(int i=0;i<anisotropicFriction.length;i++) buffer.writeFloat(anisotropicFriction[i]);
    constraints.write(buffer);
    nlastrips.write(buffer);
    hooks.write(buffer);
    particlesystem.write(buffer);
    buffer.writeInt(pd!=null?pd.hashCode():0);
    buffer.writeInt(soft!=null?soft.hashCode():0);
    buffer.writeInt(dup_group!=null?dup_group.hashCode():0);
    buffer.writeShort(fluidsimFlag);
    buffer.writeShort(restrictflag);
    buffer.writeShort(shapenr);
    buffer.writeShort(shapeflag);
    buffer.writeFloat(smoothresh);
    buffer.writeShort(recalco);
    buffer.writeShort(body_type);
    buffer.writeInt(fluidsimSettings!=null?fluidsimSettings.hashCode():0);
    buffer.writeInt(derivedDeform!=null?derivedDeform.hashCode():0);
    buffer.writeInt(derivedFinal!=null?derivedFinal.hashCode():0);
    buffer.writeInt(lastDataMask);
    buffer.writeInt(state);
    buffer.writeInt(init_state);
    buffer.writeInt(pad2);
    gpulamp.write(buffer);
    pc_ids.write(buffer);
    buffer.writeInt(duplilist!=null?duplilist.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bObject[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bObject:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  sculpt: ").append(sculpt).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  partype: ").append(partype).append("\n");
    sb.append("  par1: ").append(par1).append("\n");
    sb.append("  par2: ").append(par2).append("\n");
    sb.append("  par3: ").append(par3).append("\n");
    sb.append("  parsubstr: ").append(new String(parsubstr)).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  track: ").append(track).append("\n");
    sb.append("  proxy: ").append(proxy).append("\n");
    sb.append("  proxy_group: ").append(proxy_group).append("\n");
    sb.append("  proxy_from: ").append(proxy_from).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  bb: ").append(bb).append("\n");
    sb.append("  action: ").append(action).append("\n");
    sb.append("  poselib: ").append(poselib).append("\n");
    sb.append("  pose: ").append(pose).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    sb.append("  avs: ").append(avs).append("\n");
    sb.append("  mpath: ").append(mpath).append("\n");
    sb.append("  constraintChannels: ").append(constraintChannels).append("\n");
    sb.append("  effect: ").append(effect).append("\n");
    sb.append("  disp: ").append(disp).append("\n");
    sb.append("  defbase: ").append(defbase).append("\n");
    sb.append("  modifiers: ").append(modifiers).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  restore_mode: ").append(restore_mode).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  matbits: ").append(matbits).append("\n");
    sb.append("  totcol: ").append(totcol).append("\n");
    sb.append("  actcol: ").append(actcol).append("\n");
    sb.append("  loc: ").append(Arrays.toString(loc)).append("\n");
    sb.append("  dloc: ").append(Arrays.toString(dloc)).append("\n");
    sb.append("  orig: ").append(Arrays.toString(orig)).append("\n");
    sb.append("  size: ").append(Arrays.toString(size)).append("\n");
    sb.append("  dsize: ").append(Arrays.toString(dsize)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  drot: ").append(Arrays.toString(drot)).append("\n");
    sb.append("  quat: ").append(Arrays.toString(quat)).append("\n");
    sb.append("  dquat: ").append(Arrays.toString(dquat)).append("\n");
    sb.append("  rotAxis: ").append(Arrays.toString(rotAxis)).append("\n");
    sb.append("  drotAxis: ").append(Arrays.toString(drotAxis)).append("\n");
    sb.append("  rotAngle: ").append(rotAngle).append("\n");
    sb.append("  drotAngle: ").append(drotAngle).append("\n");
    sb.append("  obmat: ").append(Arrays.toString(obmat)).append("\n");
    sb.append("  parentinv: ").append(Arrays.toString(parentinv)).append("\n");
    sb.append("  constinv: ").append(Arrays.toString(constinv)).append("\n");
    sb.append("  imat: ").append(Arrays.toString(imat)).append("\n");
    sb.append("  imat_ren: ").append(Arrays.toString(imat_ren)).append("\n");
    sb.append("  lay: ").append(lay).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  colbits: ").append(colbits).append("\n");
    sb.append("  transflag: ").append(transflag).append("\n");
    sb.append("  protectflag: ").append(protectflag).append("\n");
    sb.append("  trackflag: ").append(trackflag).append("\n");
    sb.append("  upflag: ").append(upflag).append("\n");
    sb.append("  nlaflag: ").append(nlaflag).append("\n");
    sb.append("  ipoflag: ").append(ipoflag).append("\n");
    sb.append("  ipowin: ").append(ipowin).append("\n");
    sb.append("  scaflag: ").append(scaflag).append("\n");
    sb.append("  scavisflag: ").append(scavisflag).append("\n");
    sb.append("  boundtype: ").append(boundtype).append("\n");
    sb.append("  dupon: ").append(dupon).append("\n");
    sb.append("  dupoff: ").append(dupoff).append("\n");
    sb.append("  dupsta: ").append(dupsta).append("\n");
    sb.append("  dupend: ").append(dupend).append("\n");
    sb.append("  sf: ").append(sf).append("\n");
    sb.append("  ctime: ").append(ctime).append("\n");
    sb.append("  mass: ").append(mass).append("\n");
    sb.append("  damping: ").append(damping).append("\n");
    sb.append("  inertia: ").append(inertia).append("\n");
    sb.append("  formfactor: ").append(formfactor).append("\n");
    sb.append("  rdamping: ").append(rdamping).append("\n");
    sb.append("  sizefac: ").append(sizefac).append("\n");
    sb.append("  margin: ").append(margin).append("\n");
    sb.append("  max_vel: ").append(max_vel).append("\n");
    sb.append("  min_vel: ").append(min_vel).append("\n");
    sb.append("  m_contactProcessingThreshold: ").append(m_contactProcessingThreshold).append("\n");
    sb.append("  rotmode: ").append(rotmode).append("\n");
    sb.append("  dt: ").append(dt).append("\n");
    sb.append("  dtx: ").append(dtx).append("\n");
    sb.append("  empty_drawtype: ").append(empty_drawtype).append("\n");
    sb.append("  pad1: ").append(new String(pad1)).append("\n");
    sb.append("  empty_drawsize: ").append(empty_drawsize).append("\n");
    sb.append("  dupfacesca: ").append(dupfacesca).append("\n");
    sb.append("  prop: ").append(prop).append("\n");
    sb.append("  sensors: ").append(sensors).append("\n");
    sb.append("  controllers: ").append(controllers).append("\n");
    sb.append("  actuators: ").append(actuators).append("\n");
    sb.append("  bbsize: ").append(Arrays.toString(bbsize)).append("\n");
    sb.append("  index: ").append(index).append("\n");
    sb.append("  actdef: ").append(actdef).append("\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    sb.append("  gameflag: ").append(gameflag).append("\n");
    sb.append("  gameflag2: ").append(gameflag2).append("\n");
    sb.append("  bsoft: ").append(bsoft).append("\n");
    sb.append("  softflag: ").append(softflag).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  anisotropicFriction: ").append(Arrays.toString(anisotropicFriction)).append("\n");
    sb.append("  constraints: ").append(constraints).append("\n");
    sb.append("  nlastrips: ").append(nlastrips).append("\n");
    sb.append("  hooks: ").append(hooks).append("\n");
    sb.append("  particlesystem: ").append(particlesystem).append("\n");
    sb.append("  pd: ").append(pd).append("\n");
    sb.append("  soft: ").append(soft).append("\n");
    sb.append("  dup_group: ").append(dup_group).append("\n");
    sb.append("  fluidsimFlag: ").append(fluidsimFlag).append("\n");
    sb.append("  restrictflag: ").append(restrictflag).append("\n");
    sb.append("  shapenr: ").append(shapenr).append("\n");
    sb.append("  shapeflag: ").append(shapeflag).append("\n");
    sb.append("  smoothresh: ").append(smoothresh).append("\n");
    sb.append("  recalco: ").append(recalco).append("\n");
    sb.append("  body_type: ").append(body_type).append("\n");
    sb.append("  fluidsimSettings: ").append(fluidsimSettings).append("\n");
    sb.append("  derivedDeform: ").append(derivedDeform).append("\n");
    sb.append("  derivedFinal: ").append(derivedFinal).append("\n");
    sb.append("  lastDataMask: ").append(lastDataMask).append("\n");
    sb.append("  state: ").append(state).append("\n");
    sb.append("  init_state: ").append(init_state).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  gpulamp: ").append(gpulamp).append("\n");
    sb.append("  pc_ids: ").append(pc_ids).append("\n");
    sb.append("  duplilist: ").append(duplilist).append("\n");
    return sb.toString();
  }
  public bObject copy() { try {return (bObject)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

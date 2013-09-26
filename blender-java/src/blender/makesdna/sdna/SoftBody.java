package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SoftBody implements DNA, Cloneable { // #124
  public SoftBody[] myarray;
  public int totpoint; // 4
  public int totspring; // 4
  public Object bpoint; // ptr (BodyPoint) 0
  public Object bspring; // ptr (BodySpring) 0
  public byte pad; // 1
  public byte msg_lock; // 1
  public short msg_value; // 2
  public float nodemass; // 4
  public byte[] namedVG_Mass = new byte[32]; // 1
  public float grav; // 4
  public float mediafrict; // 4
  public float rklimit; // 4
  public float physics_speed; // 4
  public float goalspring; // 4
  public float goalfrict; // 4
  public float mingoal; // 4
  public float maxgoal; // 4
  public float defgoal; // 4
  public short vertgroup; // 2
  public byte[] namedVG_Softgoal = new byte[32]; // 1
  public short fuzzyness; // 2
  public float inspring; // 4
  public float infrict; // 4
  public byte[] namedVG_Spring_K = new byte[32]; // 1
  public int sfra; // 4
  public int efra; // 4
  public int interval; // 4
  public short local; // 2
  public short solverflags; // 2
  public SBVertex[] keys; // ptr 16
  public int totpointkey; // 4
  public int totkey; // 4
  public float secondspring; // 4
  public float colball; // 4
  public float balldamp; // 4
  public float ballstiff; // 4
  public short sbc_mode; // 2
  public short aeroedge; // 2
  public short minloops; // 2
  public short maxloops; // 2
  public short choke; // 2
  public short solver_ID; // 2
  public short plastic; // 2
  public short springpreload; // 2
  public Object scratch; // ptr (SBScratch) 0
  public float shearstiff; // 4
  public float inpush; // 4
  public PointCache pointcache; // ptr 528
  public ListBase ptcaches = new ListBase(); // 16
  public EffectorWeights effector_weights; // ptr 72
  public float[] lcom = new float[3]; // 4
  public float[][] lrot = new float[3][3]; // 4
  public float[][] lscale = new float[3][3]; // 4
  public byte[] pad4 = new byte[4]; // 1

  public void read(ByteBuffer buffer) {
    totpoint = buffer.getInt();
    totspring = buffer.getInt();
    bpoint = DNATools.ptr(buffer); // get ptr
    bspring = DNATools.ptr(buffer); // get ptr
    pad = buffer.get();
    msg_lock = buffer.get();
    msg_value = buffer.getShort();
    nodemass = buffer.getFloat();
    buffer.get(namedVG_Mass);
    grav = buffer.getFloat();
    mediafrict = buffer.getFloat();
    rklimit = buffer.getFloat();
    physics_speed = buffer.getFloat();
    goalspring = buffer.getFloat();
    goalfrict = buffer.getFloat();
    mingoal = buffer.getFloat();
    maxgoal = buffer.getFloat();
    defgoal = buffer.getFloat();
    vertgroup = buffer.getShort();
    buffer.get(namedVG_Softgoal);
    fuzzyness = buffer.getShort();
    inspring = buffer.getFloat();
    infrict = buffer.getFloat();
    buffer.get(namedVG_Spring_K);
    sfra = buffer.getInt();
    efra = buffer.getInt();
    interval = buffer.getInt();
    local = buffer.getShort();
    solverflags = buffer.getShort();
    keys = DNATools.link(DNATools.ptr(buffer), SBVertex[].class); // get ptr
    totpointkey = buffer.getInt();
    totkey = buffer.getInt();
    secondspring = buffer.getFloat();
    colball = buffer.getFloat();
    balldamp = buffer.getFloat();
    ballstiff = buffer.getFloat();
    sbc_mode = buffer.getShort();
    aeroedge = buffer.getShort();
    minloops = buffer.getShort();
    maxloops = buffer.getShort();
    choke = buffer.getShort();
    solver_ID = buffer.getShort();
    plastic = buffer.getShort();
    springpreload = buffer.getShort();
    scratch = DNATools.ptr(buffer); // get ptr
    shearstiff = buffer.getFloat();
    inpush = buffer.getFloat();
    pointcache = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
    ptcaches.read(buffer);
    effector_weights = DNATools.link(DNATools.ptr(buffer), EffectorWeights.class); // get ptr
    for(int i=0;i<lcom.length;i++) lcom[i]=buffer.getFloat();
    for(int i=0;i<lrot.length;i++) for(int j=0;j<lrot[i].length;j++) lrot[i][j]=buffer.getFloat();
    for(int i=0;i<lscale.length;i++) for(int j=0;j<lscale[i].length;j++) lscale[i][j]=buffer.getFloat();
    buffer.get(pad4);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(totpoint);
    buffer.writeInt(totspring);
    buffer.writeInt(bpoint!=null?bpoint.hashCode():0);
    buffer.writeInt(bspring!=null?bspring.hashCode():0);
    buffer.writeByte(pad);
    buffer.writeByte(msg_lock);
    buffer.writeShort(msg_value);
    buffer.writeFloat(nodemass);
    buffer.write(namedVG_Mass);
    buffer.writeFloat(grav);
    buffer.writeFloat(mediafrict);
    buffer.writeFloat(rklimit);
    buffer.writeFloat(physics_speed);
    buffer.writeFloat(goalspring);
    buffer.writeFloat(goalfrict);
    buffer.writeFloat(mingoal);
    buffer.writeFloat(maxgoal);
    buffer.writeFloat(defgoal);
    buffer.writeShort(vertgroup);
    buffer.write(namedVG_Softgoal);
    buffer.writeShort(fuzzyness);
    buffer.writeFloat(inspring);
    buffer.writeFloat(infrict);
    buffer.write(namedVG_Spring_K);
    buffer.writeInt(sfra);
    buffer.writeInt(efra);
    buffer.writeInt(interval);
    buffer.writeShort(local);
    buffer.writeShort(solverflags);
    buffer.writeInt(keys!=null?keys.hashCode():0);
    buffer.writeInt(totpointkey);
    buffer.writeInt(totkey);
    buffer.writeFloat(secondspring);
    buffer.writeFloat(colball);
    buffer.writeFloat(balldamp);
    buffer.writeFloat(ballstiff);
    buffer.writeShort(sbc_mode);
    buffer.writeShort(aeroedge);
    buffer.writeShort(minloops);
    buffer.writeShort(maxloops);
    buffer.writeShort(choke);
    buffer.writeShort(solver_ID);
    buffer.writeShort(plastic);
    buffer.writeShort(springpreload);
    buffer.writeInt(scratch!=null?scratch.hashCode():0);
    buffer.writeFloat(shearstiff);
    buffer.writeFloat(inpush);
    buffer.writeInt(pointcache!=null?pointcache.hashCode():0);
    ptcaches.write(buffer);
    buffer.writeInt(effector_weights!=null?effector_weights.hashCode():0);
    for(int i=0;i<lcom.length;i++) buffer.writeFloat(lcom[i]);
    for(int i=0; i<lrot.length; i++)  for(int j=0;j<lrot[i].length;j++) buffer.writeFloat(lrot[i][j]);
    for(int i=0; i<lscale.length; i++)  for(int j=0;j<lscale[i].length;j++) buffer.writeFloat(lscale[i][j]);
    buffer.write(pad4);
  }
  public Object setmyarray(Object array) {
    myarray = (SoftBody[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SoftBody:\n");
    sb.append("  totpoint: ").append(totpoint).append("\n");
    sb.append("  totspring: ").append(totspring).append("\n");
    sb.append("  bpoint: ").append(bpoint).append("\n");
    sb.append("  bspring: ").append(bspring).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  msg_lock: ").append(msg_lock).append("\n");
    sb.append("  msg_value: ").append(msg_value).append("\n");
    sb.append("  nodemass: ").append(nodemass).append("\n");
    sb.append("  namedVG_Mass: ").append(new String(namedVG_Mass)).append("\n");
    sb.append("  grav: ").append(grav).append("\n");
    sb.append("  mediafrict: ").append(mediafrict).append("\n");
    sb.append("  rklimit: ").append(rklimit).append("\n");
    sb.append("  physics_speed: ").append(physics_speed).append("\n");
    sb.append("  goalspring: ").append(goalspring).append("\n");
    sb.append("  goalfrict: ").append(goalfrict).append("\n");
    sb.append("  mingoal: ").append(mingoal).append("\n");
    sb.append("  maxgoal: ").append(maxgoal).append("\n");
    sb.append("  defgoal: ").append(defgoal).append("\n");
    sb.append("  vertgroup: ").append(vertgroup).append("\n");
    sb.append("  namedVG_Softgoal: ").append(new String(namedVG_Softgoal)).append("\n");
    sb.append("  fuzzyness: ").append(fuzzyness).append("\n");
    sb.append("  inspring: ").append(inspring).append("\n");
    sb.append("  infrict: ").append(infrict).append("\n");
    sb.append("  namedVG_Spring_K: ").append(new String(namedVG_Spring_K)).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  efra: ").append(efra).append("\n");
    sb.append("  interval: ").append(interval).append("\n");
    sb.append("  local: ").append(local).append("\n");
    sb.append("  solverflags: ").append(solverflags).append("\n");
    sb.append("  keys: ").append(Arrays.toString(keys)).append("\n");
    sb.append("  totpointkey: ").append(totpointkey).append("\n");
    sb.append("  totkey: ").append(totkey).append("\n");
    sb.append("  secondspring: ").append(secondspring).append("\n");
    sb.append("  colball: ").append(colball).append("\n");
    sb.append("  balldamp: ").append(balldamp).append("\n");
    sb.append("  ballstiff: ").append(ballstiff).append("\n");
    sb.append("  sbc_mode: ").append(sbc_mode).append("\n");
    sb.append("  aeroedge: ").append(aeroedge).append("\n");
    sb.append("  minloops: ").append(minloops).append("\n");
    sb.append("  maxloops: ").append(maxloops).append("\n");
    sb.append("  choke: ").append(choke).append("\n");
    sb.append("  solver_ID: ").append(solver_ID).append("\n");
    sb.append("  plastic: ").append(plastic).append("\n");
    sb.append("  springpreload: ").append(springpreload).append("\n");
    sb.append("  scratch: ").append(scratch).append("\n");
    sb.append("  shearstiff: ").append(shearstiff).append("\n");
    sb.append("  inpush: ").append(inpush).append("\n");
    sb.append("  pointcache: ").append(pointcache).append("\n");
    sb.append("  ptcaches: ").append(ptcaches).append("\n");
    sb.append("  effector_weights: ").append(effector_weights).append("\n");
    sb.append("  lcom: ").append(Arrays.toString(lcom)).append("\n");
    sb.append("  lrot: ").append(Arrays.toString(lrot)).append("\n");
    sb.append("  lscale: ").append(Arrays.toString(lscale)).append("\n");
    sb.append("  pad4: ").append(new String(pad4)).append("\n");
    return sb.toString();
  }
  public SoftBody copy() { try {return (SoftBody)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleSystem extends Link<ParticleSystem> implements DNA, Cloneable { // #350
  public ParticleSystem[] myarray;
  public ParticleSettings part; // ptr 704
  public ParticleData particles; // ptr 192
  public ChildParticle child; // ptr 64
  public Object edit; // ptr (PTCacheEdit) 0
  public Object free_edit; // func ptr 0
  public Object pathcache; // ptr (ParticleCacheKey) 0
  public Object childcache; // ptr (ParticleCacheKey) 0
  public ListBase pathcachebufs = new ListBase(); // 16
  public ListBase childcachebufs = new ListBase(); // 16
  public ClothModifierData clmd; // ptr 136
  public Object hair_in_dm; // ptr (DerivedMesh) 0
  public Object hair_out_dm; // ptr (DerivedMesh) 0
  public bObject target_ob; // ptr 1296
  public bObject lattice; // ptr 1296
  public bObject parent; // ptr 1296
  public ListBase targets = new ListBase(); // 16
  public byte[] name = new byte[32]; // 1
  public float[][] imat = new float[4][4]; // 4
  public float cfra; // 4
  public float tree_frame; // 4
  public float bvhtree_frame; // 4
  public int seed; // 4
  public int child_seed; // 4
  public int flag; // 4
  public int totpart; // 4
  public int totunexist; // 4
  public int totchild; // 4
  public int totcached; // 4
  public int totchildcache; // 4
  public short recalc; // 2
  public short target_psys; // 2
  public short totkeyed; // 2
  public short bakespace; // 2
  public byte[][] bb_uvname = new byte[3][32]; // 1
  public short[] vgroup = new short[12]; // 2
  public short vg_neg; // 2
  public short rt3; // 2
  public Object renderdata; // ptr 0
  public PointCache pointcache; // ptr 528
  public ListBase ptcaches = new ListBase(); // 16
  public ListBase effectors; // ptr 16
  public ParticleSpring fluid_springs; // ptr 16
  public int tot_fluidsprings; // 4
  public int alloc_fluidsprings; // 4
  public Object tree; // ptr (KDTree) 0
  public Object bvhtree; // ptr (BVHTree) 0
  public Object pdd; // ptr (ParticleDrawData) 0
  public Object frand; // ptr 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ParticleSystem.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ParticleSystem.class); // get ptr
    part = DNATools.link(DNATools.ptr(buffer), ParticleSettings.class); // get ptr
    particles = DNATools.link(DNATools.ptr(buffer), ParticleData.class); // get ptr
    child = DNATools.link(DNATools.ptr(buffer), ChildParticle.class); // get ptr
    edit = DNATools.ptr(buffer); // get ptr
    free_edit = DNATools.ptr(buffer); // get ptr
    pathcache = DNATools.ptr(buffer); // get ptr
    childcache = DNATools.ptr(buffer); // get ptr
    pathcachebufs.read(buffer);
    childcachebufs.read(buffer);
    clmd = DNATools.link(DNATools.ptr(buffer), ClothModifierData.class); // get ptr
    hair_in_dm = DNATools.ptr(buffer); // get ptr
    hair_out_dm = DNATools.ptr(buffer); // get ptr
    target_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    lattice = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    parent = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    targets.read(buffer);
    buffer.get(name);
    for(int i=0;i<imat.length;i++) for(int j=0;j<imat[i].length;j++) imat[i][j]=buffer.getFloat();
    cfra = buffer.getFloat();
    tree_frame = buffer.getFloat();
    bvhtree_frame = buffer.getFloat();
    seed = buffer.getInt();
    child_seed = buffer.getInt();
    flag = buffer.getInt();
    totpart = buffer.getInt();
    totunexist = buffer.getInt();
    totchild = buffer.getInt();
    totcached = buffer.getInt();
    totchildcache = buffer.getInt();
    recalc = buffer.getShort();
    target_psys = buffer.getShort();
    totkeyed = buffer.getShort();
    bakespace = buffer.getShort();
    for(int i=0; i<bb_uvname.length; i++) buffer.get(bb_uvname[i]);
    for(int i=0;i<vgroup.length;i++) vgroup[i]=buffer.getShort();
    vg_neg = buffer.getShort();
    rt3 = buffer.getShort();
    renderdata = DNATools.ptr(buffer); // get ptr
    pointcache = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
    ptcaches.read(buffer);
    effectors = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    fluid_springs = DNATools.link(DNATools.ptr(buffer), ParticleSpring.class); // get ptr
    tot_fluidsprings = buffer.getInt();
    alloc_fluidsprings = buffer.getInt();
    tree = DNATools.ptr(buffer); // get ptr
    bvhtree = DNATools.ptr(buffer); // get ptr
    pdd = DNATools.ptr(buffer); // get ptr
    frand = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(part!=null?part.hashCode():0);
    buffer.writeInt(particles!=null?particles.hashCode():0);
    buffer.writeInt(child!=null?child.hashCode():0);
    buffer.writeInt(edit!=null?edit.hashCode():0);
    buffer.writeInt(free_edit!=null?free_edit.hashCode():0);
    buffer.writeInt(pathcache!=null?pathcache.hashCode():0);
    buffer.writeInt(childcache!=null?childcache.hashCode():0);
    pathcachebufs.write(buffer);
    childcachebufs.write(buffer);
    buffer.writeInt(clmd!=null?clmd.hashCode():0);
    buffer.writeInt(hair_in_dm!=null?hair_in_dm.hashCode():0);
    buffer.writeInt(hair_out_dm!=null?hair_out_dm.hashCode():0);
    buffer.writeInt(target_ob!=null?target_ob.hashCode():0);
    buffer.writeInt(lattice!=null?lattice.hashCode():0);
    buffer.writeInt(parent!=null?parent.hashCode():0);
    targets.write(buffer);
    buffer.write(name);
    for(int i=0; i<imat.length; i++)  for(int j=0;j<imat[i].length;j++) buffer.writeFloat(imat[i][j]);
    buffer.writeFloat(cfra);
    buffer.writeFloat(tree_frame);
    buffer.writeFloat(bvhtree_frame);
    buffer.writeInt(seed);
    buffer.writeInt(child_seed);
    buffer.writeInt(flag);
    buffer.writeInt(totpart);
    buffer.writeInt(totunexist);
    buffer.writeInt(totchild);
    buffer.writeInt(totcached);
    buffer.writeInt(totchildcache);
    buffer.writeShort(recalc);
    buffer.writeShort(target_psys);
    buffer.writeShort(totkeyed);
    buffer.writeShort(bakespace);
    for(int i=0; i<bb_uvname.length; i++) buffer.write(bb_uvname[i]);
    for(int i=0;i<vgroup.length;i++) buffer.writeShort(vgroup[i]);
    buffer.writeShort(vg_neg);
    buffer.writeShort(rt3);
    buffer.writeInt(renderdata!=null?renderdata.hashCode():0);
    buffer.writeInt(pointcache!=null?pointcache.hashCode():0);
    ptcaches.write(buffer);
    buffer.writeInt(effectors!=null?effectors.hashCode():0);
    buffer.writeInt(fluid_springs!=null?fluid_springs.hashCode():0);
    buffer.writeInt(tot_fluidsprings);
    buffer.writeInt(alloc_fluidsprings);
    buffer.writeInt(tree!=null?tree.hashCode():0);
    buffer.writeInt(bvhtree!=null?bvhtree.hashCode():0);
    buffer.writeInt(pdd!=null?pdd.hashCode():0);
    buffer.writeInt(frand!=null?frand.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleSystem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleSystem:\n");
    sb.append("  part: ").append(part).append("\n");
    sb.append("  particles: ").append(particles).append("\n");
    sb.append("  child: ").append(child).append("\n");
    sb.append("  edit: ").append(edit).append("\n");
    sb.append("  free_edit: ").append(free_edit).append("\n");
    sb.append("  pathcache: ").append(pathcache).append("\n");
    sb.append("  childcache: ").append(childcache).append("\n");
    sb.append("  pathcachebufs: ").append(pathcachebufs).append("\n");
    sb.append("  childcachebufs: ").append(childcachebufs).append("\n");
    sb.append("  clmd: ").append(clmd).append("\n");
    sb.append("  hair_in_dm: ").append(hair_in_dm).append("\n");
    sb.append("  hair_out_dm: ").append(hair_out_dm).append("\n");
    sb.append("  target_ob: ").append(target_ob).append("\n");
    sb.append("  lattice: ").append(lattice).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  targets: ").append(targets).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  imat: ").append(Arrays.toString(imat)).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  tree_frame: ").append(tree_frame).append("\n");
    sb.append("  bvhtree_frame: ").append(bvhtree_frame).append("\n");
    sb.append("  seed: ").append(seed).append("\n");
    sb.append("  child_seed: ").append(child_seed).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  totpart: ").append(totpart).append("\n");
    sb.append("  totunexist: ").append(totunexist).append("\n");
    sb.append("  totchild: ").append(totchild).append("\n");
    sb.append("  totcached: ").append(totcached).append("\n");
    sb.append("  totchildcache: ").append(totchildcache).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  target_psys: ").append(target_psys).append("\n");
    sb.append("  totkeyed: ").append(totkeyed).append("\n");
    sb.append("  bakespace: ").append(bakespace).append("\n");
    sb.append("  bb_uvname: ").append(Arrays.toString(bb_uvname)).append("\n");
    sb.append("  vgroup: ").append(Arrays.toString(vgroup)).append("\n");
    sb.append("  vg_neg: ").append(vg_neg).append("\n");
    sb.append("  rt3: ").append(rt3).append("\n");
    sb.append("  renderdata: ").append(renderdata).append("\n");
    sb.append("  pointcache: ").append(pointcache).append("\n");
    sb.append("  ptcaches: ").append(ptcaches).append("\n");
    sb.append("  effectors: ").append(effectors).append("\n");
    sb.append("  fluid_springs: ").append(fluid_springs).append("\n");
    sb.append("  tot_fluidsprings: ").append(tot_fluidsprings).append("\n");
    sb.append("  alloc_fluidsprings: ").append(alloc_fluidsprings).append("\n");
    sb.append("  tree: ").append(tree).append("\n");
    sb.append("  bvhtree: ").append(bvhtree).append("\n");
    sb.append("  pdd: ").append(pdd).append("\n");
    sb.append("  frand: ").append(frand).append("\n");
    return sb.toString();
  }
  public ParticleSystem copy() { try {return (ParticleSystem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

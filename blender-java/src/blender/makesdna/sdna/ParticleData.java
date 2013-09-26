package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleData implements DNA, Cloneable { // #347
  public ParticleData[] myarray;
  public ParticleKey state = new ParticleKey(); // 56
  public ParticleKey prev_state = new ParticleKey(); // 56
  public HairKey hair; // ptr 24
  public ParticleKey keys; // ptr 56
  public BoidParticle boid; // ptr 56
  public int totkey; // 4
  public float time; // 4
  public float lifetime; // 4
  public float dietime; // 4
  public int num; // 4
  public int num_dmcache; // 4
  public float[] fuv = new float[4]; // 4
  public float foffset; // 4
  public float size; // 4
  public int hair_index; // 4
  public short flag; // 2
  public short alive; // 2

  public void read(ByteBuffer buffer) {
    state.read(buffer);
    prev_state.read(buffer);
    hair = DNATools.link(DNATools.ptr(buffer), HairKey.class); // get ptr
    keys = DNATools.link(DNATools.ptr(buffer), ParticleKey.class); // get ptr
    boid = DNATools.link(DNATools.ptr(buffer), BoidParticle.class); // get ptr
    totkey = buffer.getInt();
    time = buffer.getFloat();
    lifetime = buffer.getFloat();
    dietime = buffer.getFloat();
    num = buffer.getInt();
    num_dmcache = buffer.getInt();
    for(int i=0;i<fuv.length;i++) fuv[i]=buffer.getFloat();
    foffset = buffer.getFloat();
    size = buffer.getFloat();
    hair_index = buffer.getInt();
    flag = buffer.getShort();
    alive = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    state.write(buffer);
    prev_state.write(buffer);
    buffer.writeInt(hair!=null?hair.hashCode():0);
    buffer.writeInt(keys!=null?keys.hashCode():0);
    buffer.writeInt(boid!=null?boid.hashCode():0);
    buffer.writeInt(totkey);
    buffer.writeFloat(time);
    buffer.writeFloat(lifetime);
    buffer.writeFloat(dietime);
    buffer.writeInt(num);
    buffer.writeInt(num_dmcache);
    for(int i=0;i<fuv.length;i++) buffer.writeFloat(fuv[i]);
    buffer.writeFloat(foffset);
    buffer.writeFloat(size);
    buffer.writeInt(hair_index);
    buffer.writeShort(flag);
    buffer.writeShort(alive);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleData:\n");
    sb.append("  state: ").append(state).append("\n");
    sb.append("  prev_state: ").append(prev_state).append("\n");
    sb.append("  hair: ").append(hair).append("\n");
    sb.append("  keys: ").append(keys).append("\n");
    sb.append("  boid: ").append(boid).append("\n");
    sb.append("  totkey: ").append(totkey).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  lifetime: ").append(lifetime).append("\n");
    sb.append("  dietime: ").append(dietime).append("\n");
    sb.append("  num: ").append(num).append("\n");
    sb.append("  num_dmcache: ").append(num_dmcache).append("\n");
    sb.append("  fuv: ").append(Arrays.toString(fuv)).append("\n");
    sb.append("  foffset: ").append(foffset).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  hair_index: ").append(hair_index).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  alive: ").append(alive).append("\n");
    return sb.toString();
  }
  public ParticleData copy() { try {return (ParticleData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

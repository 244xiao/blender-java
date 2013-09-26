package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ClothSimSettings implements DNA, Cloneable { // #351
  public ClothSimSettings[] myarray;
  public Object cache; // ptr (LinkNode) 0
  public float mingoal; // 4
  public float Cdis; // 4
  public float Cvi; // 4
  public float[] gravity = new float[3]; // 4
  public float dt; // 4
  public float mass; // 4
  public float structural; // 4
  public float shear; // 4
  public float bending; // 4
  public float max_bend; // 4
  public float max_struct; // 4
  public float max_shear; // 4
  public float avg_spring_len; // 4
  public float timescale; // 4
  public float maxgoal; // 4
  public float eff_force_scale; // 4
  public float eff_wind_scale; // 4
  public float sim_time_old; // 4
  public float defgoal; // 4
  public float goalspring; // 4
  public float goalfrict; // 4
  public float velocity_smooth; // 4
  public float collider_friction; // 4
  public int stepsPerFrame; // 4
  public int flags; // 4
  public int preroll; // 4
  public int maxspringlen; // 4
  public short solver_type; // 2
  public short vgroup_bend; // 2
  public short vgroup_mass; // 2
  public short vgroup_struct; // 2
  public short shapekey_rest; // 2
  public short presets; // 2
  public short reset; // 2
  public short[] pad = new short[3]; // 2
  public EffectorWeights effector_weights; // ptr 72

  public void read(ByteBuffer buffer) {
    cache = DNATools.ptr(buffer); // get ptr
    mingoal = buffer.getFloat();
    Cdis = buffer.getFloat();
    Cvi = buffer.getFloat();
    for(int i=0;i<gravity.length;i++) gravity[i]=buffer.getFloat();
    dt = buffer.getFloat();
    mass = buffer.getFloat();
    structural = buffer.getFloat();
    shear = buffer.getFloat();
    bending = buffer.getFloat();
    max_bend = buffer.getFloat();
    max_struct = buffer.getFloat();
    max_shear = buffer.getFloat();
    avg_spring_len = buffer.getFloat();
    timescale = buffer.getFloat();
    maxgoal = buffer.getFloat();
    eff_force_scale = buffer.getFloat();
    eff_wind_scale = buffer.getFloat();
    sim_time_old = buffer.getFloat();
    defgoal = buffer.getFloat();
    goalspring = buffer.getFloat();
    goalfrict = buffer.getFloat();
    velocity_smooth = buffer.getFloat();
    collider_friction = buffer.getFloat();
    stepsPerFrame = buffer.getInt();
    flags = buffer.getInt();
    preroll = buffer.getInt();
    maxspringlen = buffer.getInt();
    solver_type = buffer.getShort();
    vgroup_bend = buffer.getShort();
    vgroup_mass = buffer.getShort();
    vgroup_struct = buffer.getShort();
    shapekey_rest = buffer.getShort();
    presets = buffer.getShort();
    reset = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    effector_weights = DNATools.link(DNATools.ptr(buffer), EffectorWeights.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(cache!=null?cache.hashCode():0);
    buffer.writeFloat(mingoal);
    buffer.writeFloat(Cdis);
    buffer.writeFloat(Cvi);
    for(int i=0;i<gravity.length;i++) buffer.writeFloat(gravity[i]);
    buffer.writeFloat(dt);
    buffer.writeFloat(mass);
    buffer.writeFloat(structural);
    buffer.writeFloat(shear);
    buffer.writeFloat(bending);
    buffer.writeFloat(max_bend);
    buffer.writeFloat(max_struct);
    buffer.writeFloat(max_shear);
    buffer.writeFloat(avg_spring_len);
    buffer.writeFloat(timescale);
    buffer.writeFloat(maxgoal);
    buffer.writeFloat(eff_force_scale);
    buffer.writeFloat(eff_wind_scale);
    buffer.writeFloat(sim_time_old);
    buffer.writeFloat(defgoal);
    buffer.writeFloat(goalspring);
    buffer.writeFloat(goalfrict);
    buffer.writeFloat(velocity_smooth);
    buffer.writeFloat(collider_friction);
    buffer.writeInt(stepsPerFrame);
    buffer.writeInt(flags);
    buffer.writeInt(preroll);
    buffer.writeInt(maxspringlen);
    buffer.writeShort(solver_type);
    buffer.writeShort(vgroup_bend);
    buffer.writeShort(vgroup_mass);
    buffer.writeShort(vgroup_struct);
    buffer.writeShort(shapekey_rest);
    buffer.writeShort(presets);
    buffer.writeShort(reset);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.writeInt(effector_weights!=null?effector_weights.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ClothSimSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ClothSimSettings:\n");
    sb.append("  cache: ").append(cache).append("\n");
    sb.append("  mingoal: ").append(mingoal).append("\n");
    sb.append("  Cdis: ").append(Cdis).append("\n");
    sb.append("  Cvi: ").append(Cvi).append("\n");
    sb.append("  gravity: ").append(Arrays.toString(gravity)).append("\n");
    sb.append("  dt: ").append(dt).append("\n");
    sb.append("  mass: ").append(mass).append("\n");
    sb.append("  structural: ").append(structural).append("\n");
    sb.append("  shear: ").append(shear).append("\n");
    sb.append("  bending: ").append(bending).append("\n");
    sb.append("  max_bend: ").append(max_bend).append("\n");
    sb.append("  max_struct: ").append(max_struct).append("\n");
    sb.append("  max_shear: ").append(max_shear).append("\n");
    sb.append("  avg_spring_len: ").append(avg_spring_len).append("\n");
    sb.append("  timescale: ").append(timescale).append("\n");
    sb.append("  maxgoal: ").append(maxgoal).append("\n");
    sb.append("  eff_force_scale: ").append(eff_force_scale).append("\n");
    sb.append("  eff_wind_scale: ").append(eff_wind_scale).append("\n");
    sb.append("  sim_time_old: ").append(sim_time_old).append("\n");
    sb.append("  defgoal: ").append(defgoal).append("\n");
    sb.append("  goalspring: ").append(goalspring).append("\n");
    sb.append("  goalfrict: ").append(goalfrict).append("\n");
    sb.append("  velocity_smooth: ").append(velocity_smooth).append("\n");
    sb.append("  collider_friction: ").append(collider_friction).append("\n");
    sb.append("  stepsPerFrame: ").append(stepsPerFrame).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  preroll: ").append(preroll).append("\n");
    sb.append("  maxspringlen: ").append(maxspringlen).append("\n");
    sb.append("  solver_type: ").append(solver_type).append("\n");
    sb.append("  vgroup_bend: ").append(vgroup_bend).append("\n");
    sb.append("  vgroup_mass: ").append(vgroup_mass).append("\n");
    sb.append("  vgroup_struct: ").append(vgroup_struct).append("\n");
    sb.append("  shapekey_rest: ").append(shapekey_rest).append("\n");
    sb.append("  presets: ").append(presets).append("\n");
    sb.append("  reset: ").append(reset).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  effector_weights: ").append(effector_weights).append("\n");
    return sb.toString();
  }
  public ClothSimSettings copy() { try {return (ClothSimSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SmokeFlowSettings implements DNA, Cloneable { // #399
  public SmokeFlowSettings[] myarray;
  public SmokeModifierData smd; // ptr 112
  public ParticleSystem psys; // ptr 528
  public float density; // 4
  public float temp; // 4
  public float[] velocity = new float[2]; // 4
  public float vel_multi; // 4
  public float[] vgrp_heat_scale = new float[2]; // 4
  public short vgroup_flow; // 2
  public short vgroup_density; // 2
  public short vgroup_heat; // 2
  public short type; // 2
  public int flags; // 4

  public void read(ByteBuffer buffer) {
    smd = DNATools.link(DNATools.ptr(buffer), SmokeModifierData.class); // get ptr
    psys = DNATools.link(DNATools.ptr(buffer), ParticleSystem.class); // get ptr
    density = buffer.getFloat();
    temp = buffer.getFloat();
    for(int i=0;i<velocity.length;i++) velocity[i]=buffer.getFloat();
    vel_multi = buffer.getFloat();
    for(int i=0;i<vgrp_heat_scale.length;i++) vgrp_heat_scale[i]=buffer.getFloat();
    vgroup_flow = buffer.getShort();
    vgroup_density = buffer.getShort();
    vgroup_heat = buffer.getShort();
    type = buffer.getShort();
    flags = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(smd!=null?smd.hashCode():0);
    buffer.writeInt(psys!=null?psys.hashCode():0);
    buffer.writeFloat(density);
    buffer.writeFloat(temp);
    for(int i=0;i<velocity.length;i++) buffer.writeFloat(velocity[i]);
    buffer.writeFloat(vel_multi);
    for(int i=0;i<vgrp_heat_scale.length;i++) buffer.writeFloat(vgrp_heat_scale[i]);
    buffer.writeShort(vgroup_flow);
    buffer.writeShort(vgroup_density);
    buffer.writeShort(vgroup_heat);
    buffer.writeShort(type);
    buffer.writeInt(flags);
  }
  public Object setmyarray(Object array) {
    myarray = (SmokeFlowSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SmokeFlowSettings:\n");
    sb.append("  smd: ").append(smd).append("\n");
    sb.append("  psys: ").append(psys).append("\n");
    sb.append("  density: ").append(density).append("\n");
    sb.append("  temp: ").append(temp).append("\n");
    sb.append("  velocity: ").append(Arrays.toString(velocity)).append("\n");
    sb.append("  vel_multi: ").append(vel_multi).append("\n");
    sb.append("  vgrp_heat_scale: ").append(Arrays.toString(vgrp_heat_scale)).append("\n");
    sb.append("  vgroup_flow: ").append(vgroup_flow).append("\n");
    sb.append("  vgroup_density: ").append(vgroup_density).append("\n");
    sb.append("  vgroup_heat: ").append(vgroup_heat).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    return sb.toString();
  }
  public SmokeFlowSettings copy() { try {return (SmokeFlowSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

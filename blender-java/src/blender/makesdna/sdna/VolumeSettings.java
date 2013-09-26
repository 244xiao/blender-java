package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class VolumeSettings implements DNA, Cloneable { // #34
  public VolumeSettings[] myarray;
  public float density; // 4
  public float emission; // 4
  public float scattering; // 4
  public float reflection; // 4
  public float[] emission_col = new float[3]; // 4
  public float[] transmission_col = new float[3]; // 4
  public float[] reflection_col = new float[3]; // 4
  public float density_scale; // 4
  public float depth_cutoff; // 4
  public float asymmetry; // 4
  public short stepsize_type; // 2
  public short shadeflag; // 2
  public short shade_type; // 2
  public short precache_resolution; // 2
  public float stepsize; // 4
  public float ms_diff; // 4
  public float ms_intensity; // 4
  public float ms_spread; // 4

  public void read(ByteBuffer buffer) {
    density = buffer.getFloat();
    emission = buffer.getFloat();
    scattering = buffer.getFloat();
    reflection = buffer.getFloat();
    for(int i=0;i<emission_col.length;i++) emission_col[i]=buffer.getFloat();
    for(int i=0;i<transmission_col.length;i++) transmission_col[i]=buffer.getFloat();
    for(int i=0;i<reflection_col.length;i++) reflection_col[i]=buffer.getFloat();
    density_scale = buffer.getFloat();
    depth_cutoff = buffer.getFloat();
    asymmetry = buffer.getFloat();
    stepsize_type = buffer.getShort();
    shadeflag = buffer.getShort();
    shade_type = buffer.getShort();
    precache_resolution = buffer.getShort();
    stepsize = buffer.getFloat();
    ms_diff = buffer.getFloat();
    ms_intensity = buffer.getFloat();
    ms_spread = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(density);
    buffer.writeFloat(emission);
    buffer.writeFloat(scattering);
    buffer.writeFloat(reflection);
    for(int i=0;i<emission_col.length;i++) buffer.writeFloat(emission_col[i]);
    for(int i=0;i<transmission_col.length;i++) buffer.writeFloat(transmission_col[i]);
    for(int i=0;i<reflection_col.length;i++) buffer.writeFloat(reflection_col[i]);
    buffer.writeFloat(density_scale);
    buffer.writeFloat(depth_cutoff);
    buffer.writeFloat(asymmetry);
    buffer.writeShort(stepsize_type);
    buffer.writeShort(shadeflag);
    buffer.writeShort(shade_type);
    buffer.writeShort(precache_resolution);
    buffer.writeFloat(stepsize);
    buffer.writeFloat(ms_diff);
    buffer.writeFloat(ms_intensity);
    buffer.writeFloat(ms_spread);
  }
  public Object setmyarray(Object array) {
    myarray = (VolumeSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("VolumeSettings:\n");
    sb.append("  density: ").append(density).append("\n");
    sb.append("  emission: ").append(emission).append("\n");
    sb.append("  scattering: ").append(scattering).append("\n");
    sb.append("  reflection: ").append(reflection).append("\n");
    sb.append("  emission_col: ").append(Arrays.toString(emission_col)).append("\n");
    sb.append("  transmission_col: ").append(Arrays.toString(transmission_col)).append("\n");
    sb.append("  reflection_col: ").append(Arrays.toString(reflection_col)).append("\n");
    sb.append("  density_scale: ").append(density_scale).append("\n");
    sb.append("  depth_cutoff: ").append(depth_cutoff).append("\n");
    sb.append("  asymmetry: ").append(asymmetry).append("\n");
    sb.append("  stepsize_type: ").append(stepsize_type).append("\n");
    sb.append("  shadeflag: ").append(shadeflag).append("\n");
    sb.append("  shade_type: ").append(shade_type).append("\n");
    sb.append("  precache_resolution: ").append(precache_resolution).append("\n");
    sb.append("  stepsize: ").append(stepsize).append("\n");
    sb.append("  ms_diff: ").append(ms_diff).append("\n");
    sb.append("  ms_intensity: ").append(ms_intensity).append("\n");
    sb.append("  ms_spread: ").append(ms_spread).append("\n");
    return sb.toString();
  }
  public VolumeSettings copy() { try {return (VolumeSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

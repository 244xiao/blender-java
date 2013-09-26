package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SPHFluidSettings implements DNA, Cloneable { // #348
  public SPHFluidSettings[] myarray;
  public float radius; // 4
  public float spring_k; // 4
  public float rest_length; // 4
  public float plasticity_constant; // 4
  public float yield_ratio; // 4
  public float plasticity_balance; // 4
  public float yield_balance; // 4
  public float viscosity_omega; // 4
  public float viscosity_beta; // 4
  public float stiffness_k; // 4
  public float stiffness_knear; // 4
  public float rest_density; // 4
  public float buoyancy; // 4
  public int flag; // 4
  public int spring_frames; // 4

  public void read(ByteBuffer buffer) {
    radius = buffer.getFloat();
    spring_k = buffer.getFloat();
    rest_length = buffer.getFloat();
    plasticity_constant = buffer.getFloat();
    yield_ratio = buffer.getFloat();
    plasticity_balance = buffer.getFloat();
    yield_balance = buffer.getFloat();
    viscosity_omega = buffer.getFloat();
    viscosity_beta = buffer.getFloat();
    stiffness_k = buffer.getFloat();
    stiffness_knear = buffer.getFloat();
    rest_density = buffer.getFloat();
    buoyancy = buffer.getFloat();
    flag = buffer.getInt();
    spring_frames = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(radius);
    buffer.writeFloat(spring_k);
    buffer.writeFloat(rest_length);
    buffer.writeFloat(plasticity_constant);
    buffer.writeFloat(yield_ratio);
    buffer.writeFloat(plasticity_balance);
    buffer.writeFloat(yield_balance);
    buffer.writeFloat(viscosity_omega);
    buffer.writeFloat(viscosity_beta);
    buffer.writeFloat(stiffness_k);
    buffer.writeFloat(stiffness_knear);
    buffer.writeFloat(rest_density);
    buffer.writeFloat(buoyancy);
    buffer.writeInt(flag);
    buffer.writeInt(spring_frames);
  }
  public Object setmyarray(Object array) {
    myarray = (SPHFluidSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SPHFluidSettings:\n");
    sb.append("  radius: ").append(radius).append("\n");
    sb.append("  spring_k: ").append(spring_k).append("\n");
    sb.append("  rest_length: ").append(rest_length).append("\n");
    sb.append("  plasticity_constant: ").append(plasticity_constant).append("\n");
    sb.append("  yield_ratio: ").append(yield_ratio).append("\n");
    sb.append("  plasticity_balance: ").append(plasticity_balance).append("\n");
    sb.append("  yield_balance: ").append(yield_balance).append("\n");
    sb.append("  viscosity_omega: ").append(viscosity_omega).append("\n");
    sb.append("  viscosity_beta: ").append(viscosity_beta).append("\n");
    sb.append("  stiffness_k: ").append(stiffness_k).append("\n");
    sb.append("  stiffness_knear: ").append(stiffness_knear).append("\n");
    sb.append("  rest_density: ").append(rest_density).append("\n");
    sb.append("  buoyancy: ").append(buoyancy).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  spring_frames: ").append(spring_frames).append("\n");
    return sb.toString();
  }
  public SPHFluidSettings copy() { try {return (SPHFluidSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

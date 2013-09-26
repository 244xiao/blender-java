package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeColorBalance implements DNA, Cloneable { // #327
  public NodeColorBalance[] myarray;
  public float[] slope = new float[3]; // 4
  public float[] offset = new float[3]; // 4
  public float[] power = new float[3]; // 4
  public float[] lift = new float[3]; // 4
  public float[] gamma = new float[3]; // 4
  public float[] gain = new float[3]; // 4
  public float[] lift_lgg = new float[3]; // 4
  public float[] gamma_inv = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<slope.length;i++) slope[i]=buffer.getFloat();
    for(int i=0;i<offset.length;i++) offset[i]=buffer.getFloat();
    for(int i=0;i<power.length;i++) power[i]=buffer.getFloat();
    for(int i=0;i<lift.length;i++) lift[i]=buffer.getFloat();
    for(int i=0;i<gamma.length;i++) gamma[i]=buffer.getFloat();
    for(int i=0;i<gain.length;i++) gain[i]=buffer.getFloat();
    for(int i=0;i<lift_lgg.length;i++) lift_lgg[i]=buffer.getFloat();
    for(int i=0;i<gamma_inv.length;i++) gamma_inv[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<slope.length;i++) buffer.writeFloat(slope[i]);
    for(int i=0;i<offset.length;i++) buffer.writeFloat(offset[i]);
    for(int i=0;i<power.length;i++) buffer.writeFloat(power[i]);
    for(int i=0;i<lift.length;i++) buffer.writeFloat(lift[i]);
    for(int i=0;i<gamma.length;i++) buffer.writeFloat(gamma[i]);
    for(int i=0;i<gain.length;i++) buffer.writeFloat(gain[i]);
    for(int i=0;i<lift_lgg.length;i++) buffer.writeFloat(lift_lgg[i]);
    for(int i=0;i<gamma_inv.length;i++) buffer.writeFloat(gamma_inv[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeColorBalance[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeColorBalance:\n");
    sb.append("  slope: ").append(Arrays.toString(slope)).append("\n");
    sb.append("  offset: ").append(Arrays.toString(offset)).append("\n");
    sb.append("  power: ").append(Arrays.toString(power)).append("\n");
    sb.append("  lift: ").append(Arrays.toString(lift)).append("\n");
    sb.append("  gamma: ").append(Arrays.toString(gamma)).append("\n");
    sb.append("  gain: ").append(Arrays.toString(gain)).append("\n");
    sb.append("  lift_lgg: ").append(Arrays.toString(lift_lgg)).append("\n");
    sb.append("  gamma_inv: ").append(Arrays.toString(gamma_inv)).append("\n");
    return sb.toString();
  }
  public NodeColorBalance copy() { try {return (NodeColorBalance)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleKey implements DNA, Cloneable { // #341
  public ParticleKey[] myarray;
  public float[] co = new float[3]; // 4
  public float[] vel = new float[3]; // 4
  public float[] rot = new float[4]; // 4
  public float[] ave = new float[3]; // 4
  public float time; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<co.length;i++) co[i]=buffer.getFloat();
    for(int i=0;i<vel.length;i++) vel[i]=buffer.getFloat();
    for(int i=0;i<rot.length;i++) rot[i]=buffer.getFloat();
    for(int i=0;i<ave.length;i++) ave[i]=buffer.getFloat();
    time = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<co.length;i++) buffer.writeFloat(co[i]);
    for(int i=0;i<vel.length;i++) buffer.writeFloat(vel[i]);
    for(int i=0;i<rot.length;i++) buffer.writeFloat(rot[i]);
    for(int i=0;i<ave.length;i++) buffer.writeFloat(ave[i]);
    buffer.writeFloat(time);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleKey[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleKey:\n");
    sb.append("  co: ").append(Arrays.toString(co)).append("\n");
    sb.append("  vel: ").append(Arrays.toString(vel)).append("\n");
    sb.append("  rot: ").append(Arrays.toString(rot)).append("\n");
    sb.append("  ave: ").append(Arrays.toString(ave)).append("\n");
    sb.append("  time: ").append(time).append("\n");
    return sb.toString();
  }
  public ParticleKey copy() { try {return (ParticleKey)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

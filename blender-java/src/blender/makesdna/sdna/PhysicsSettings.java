package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PhysicsSettings implements DNA, Cloneable { // #150
  public PhysicsSettings[] myarray;
  public float[] gravity = new float[3]; // 4
  public int flag; // 4
  public int quick_cache_step; // 4
  public int rt; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<gravity.length;i++) gravity[i]=buffer.getFloat();
    flag = buffer.getInt();
    quick_cache_step = buffer.getInt();
    rt = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<gravity.length;i++) buffer.writeFloat(gravity[i]);
    buffer.writeInt(flag);
    buffer.writeInt(quick_cache_step);
    buffer.writeInt(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (PhysicsSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PhysicsSettings:\n");
    sb.append("  gravity: ").append(Arrays.toString(gravity)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  quick_cache_step: ").append(quick_cache_step).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public PhysicsSettings copy() { try {return (PhysicsSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

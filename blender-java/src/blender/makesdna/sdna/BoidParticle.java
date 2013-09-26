package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidParticle implements DNA, Cloneable { // #342
  public BoidParticle[] myarray;
  public bObject ground; // ptr 1296
  public BoidData data = new BoidData(); // 20
  public float[] gravity = new float[3]; // 4
  public float[] wander = new float[3]; // 4
  public float rt; // 4

  public void read(ByteBuffer buffer) {
    ground = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    data.read(buffer);
    for(int i=0;i<gravity.length;i++) gravity[i]=buffer.getFloat();
    for(int i=0;i<wander.length;i++) wander[i]=buffer.getFloat();
    rt = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(ground!=null?ground.hashCode():0);
    data.write(buffer);
    for(int i=0;i<gravity.length;i++) buffer.writeFloat(gravity[i]);
    for(int i=0;i<wander.length;i++) buffer.writeFloat(wander[i]);
    buffer.writeFloat(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidParticle[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidParticle:\n");
    sb.append("  ground: ").append(ground).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  gravity: ").append(Arrays.toString(gravity)).append("\n");
    sb.append("  wander: ").append(Arrays.toString(wander)).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public BoidParticle copy() { try {return (BoidParticle)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

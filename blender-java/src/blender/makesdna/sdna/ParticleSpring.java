package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleSpring implements DNA, Cloneable { // #343
  public ParticleSpring[] myarray;
  public float rest_length; // 4
  public int[] particle_index = new int[2]; // 4
  public int delete_flag; // 4

  public void read(ByteBuffer buffer) {
    rest_length = buffer.getFloat();
    for(int i=0;i<particle_index.length;i++) particle_index[i]=buffer.getInt();
    delete_flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(rest_length);
    for(int i=0;i<particle_index.length;i++) buffer.writeInt(particle_index[i]);
    buffer.writeInt(delete_flag);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleSpring[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleSpring:\n");
    sb.append("  rest_length: ").append(rest_length).append("\n");
    sb.append("  particle_index: ").append(Arrays.toString(particle_index)).append("\n");
    sb.append("  delete_flag: ").append(delete_flag).append("\n");
    return sb.toString();
  }
  public ParticleSpring copy() { try {return (ParticleSpring)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

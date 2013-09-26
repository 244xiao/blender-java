package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MDefInfluence implements DNA, Cloneable { // #97
  public MDefInfluence[] myarray;
  public int vertex; // 4
  public float weight; // 4

  public void read(ByteBuffer buffer) {
    vertex = buffer.getInt();
    weight = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(vertex);
    buffer.writeFloat(weight);
  }
  public Object setmyarray(Object array) {
    myarray = (MDefInfluence[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MDefInfluence:\n");
    sb.append("  vertex: ").append(vertex).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    return sb.toString();
  }
  public MDefInfluence copy() { try {return (MDefInfluence)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

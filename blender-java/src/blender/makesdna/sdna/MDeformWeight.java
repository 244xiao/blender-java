package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MDeformWeight implements DNA, Cloneable { // #50
  public MDeformWeight[] myarray;
  public int def_nr; // 4
  public float weight; // 4

  public void read(ByteBuffer buffer) {
    def_nr = buffer.getInt();
    weight = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(def_nr);
    buffer.writeFloat(weight);
  }
  public Object setmyarray(Object array) {
    myarray = (MDeformWeight[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MDeformWeight:\n");
    sb.append("  def_nr: ").append(def_nr).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    return sb.toString();
  }
  public MDeformWeight copy() { try {return (MDeformWeight)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

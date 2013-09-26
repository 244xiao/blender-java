package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeGeometry implements DNA, Cloneable { // #320
  public NodeGeometry[] myarray;
  public byte[] uvname = new byte[32]; // 1
  public byte[] colname = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(uvname);
    buffer.get(colname);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(uvname);
    buffer.write(colname);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeGeometry[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeGeometry:\n");
    sb.append("  uvname: ").append(new String(uvname)).append("\n");
    sb.append("  colname: ").append(new String(colname)).append("\n");
    return sb.toString();
  }
  public NodeGeometry copy() { try {return (NodeGeometry)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

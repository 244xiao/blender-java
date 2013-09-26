package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeVertexCol implements DNA, Cloneable { // #321
  public NodeVertexCol[] myarray;
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeVertexCol[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeVertexCol:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public NodeVertexCol copy() { try {return (NodeVertexCol)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

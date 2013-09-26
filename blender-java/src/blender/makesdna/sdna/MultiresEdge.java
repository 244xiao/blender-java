package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresEdge implements DNA, Cloneable { // #68
  public MultiresEdge[] myarray;
  public int[] v = new int[2]; // 4
  public int mid; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<v.length;i++) v[i]=buffer.getInt();
    mid = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<v.length;i++) buffer.writeInt(v[i]);
    buffer.writeInt(mid);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresEdge[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresEdge:\n");
    sb.append("  v: ").append(Arrays.toString(v)).append("\n");
    sb.append("  mid: ").append(mid).append("\n");
    return sb.toString();
  }
  public MultiresEdge copy() { try {return (MultiresEdge)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

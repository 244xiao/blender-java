package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresColFace implements DNA, Cloneable { // #66
  public MultiresColFace[] myarray;
  public MultiresCol[] col = new MultiresCol[4]; // 16

  public void read(ByteBuffer buffer) {
    for(int i=0;i<col.length;i++) { col[i]=new MultiresCol(); col[i].read(buffer); }
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<col.length;i++) col[i].write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresColFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresColFace:\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    return sb.toString();
  }
  public MultiresColFace copy() { try {return (MultiresColFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

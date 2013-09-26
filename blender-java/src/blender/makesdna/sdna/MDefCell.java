package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MDefCell implements DNA, Cloneable { // #98
  public MDefCell[] myarray;
  public int offset; // 4
  public int totinfluence; // 4

  public void read(ByteBuffer buffer) {
    offset = buffer.getInt();
    totinfluence = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(offset);
    buffer.writeInt(totinfluence);
  }
  public Object setmyarray(Object array) {
    myarray = (MDefCell[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MDefCell:\n");
    sb.append("  offset: ").append(offset).append("\n");
    sb.append("  totinfluence: ").append(totinfluence).append("\n");
    return sb.toString();
  }
  public MDefCell copy() { try {return (MDefCell)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

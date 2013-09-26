package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MEdge implements DNA, Cloneable { // #49
  public MEdge[] myarray;
  public int v1; // 4
  public int v2; // 4
  public byte crease; // 1
  public byte bweight; // 1
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    v1 = buffer.getInt();
    v2 = buffer.getInt();
    crease = buffer.get();
    bweight = buffer.get();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(v1);
    buffer.writeInt(v2);
    buffer.writeByte(crease);
    buffer.writeByte(bweight);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (MEdge[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MEdge:\n");
    sb.append("  v1: ").append(v1).append("\n");
    sb.append("  v2: ").append(v2).append("\n");
    sb.append("  crease: ").append(crease).append("\n");
    sb.append("  bweight: ").append(bweight).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public MEdge copy() { try {return (MEdge)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

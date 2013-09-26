package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MFace implements DNA, Cloneable { // #48
  public MFace[] myarray;
  public int v1; // 4
  public int v2; // 4
  public int v3; // 4
  public int v4; // 4
  public short mat_nr; // 2
  public byte edcode; // 1
  public byte flag; // 1

  public void read(ByteBuffer buffer) {
    v1 = buffer.getInt();
    v2 = buffer.getInt();
    v3 = buffer.getInt();
    v4 = buffer.getInt();
    mat_nr = buffer.getShort();
    edcode = buffer.get();
    flag = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(v1);
    buffer.writeInt(v2);
    buffer.writeInt(v3);
    buffer.writeInt(v4);
    buffer.writeShort(mat_nr);
    buffer.writeByte(edcode);
    buffer.writeByte(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (MFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MFace:\n");
    sb.append("  v1: ").append(v1).append("\n");
    sb.append("  v2: ").append(v2).append("\n");
    sb.append("  v3: ").append(v3).append("\n");
    sb.append("  v4: ").append(v4).append("\n");
    sb.append("  mat_nr: ").append(mat_nr).append("\n");
    sb.append("  edcode: ").append(edcode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public MFace copy() { try {return (MFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

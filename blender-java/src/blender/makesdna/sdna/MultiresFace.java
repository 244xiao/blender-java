package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresFace implements DNA, Cloneable { // #67
  public MultiresFace[] myarray;
  public int[] v = new int[4]; // 4
  public int mid; // 4
  public byte flag; // 1
  public byte mat_nr; // 1
  public byte[] pad = new byte[2]; // 1

  public void read(ByteBuffer buffer) {
    for(int i=0;i<v.length;i++) v[i]=buffer.getInt();
    mid = buffer.getInt();
    flag = buffer.get();
    mat_nr = buffer.get();
    buffer.get(pad);
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<v.length;i++) buffer.writeInt(v[i]);
    buffer.writeInt(mid);
    buffer.writeByte(flag);
    buffer.writeByte(mat_nr);
    buffer.write(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresFace:\n");
    sb.append("  v: ").append(Arrays.toString(v)).append("\n");
    sb.append("  mid: ").append(mid).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  mat_nr: ").append(mat_nr).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    return sb.toString();
  }
  public MultiresFace copy() { try {return (MultiresFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TFace implements DNA, Cloneable { // #47
  public TFace[] myarray;
  public Object tpage; // ptr 0
  public float[][] uv = new float[4][2]; // 4
  public int[] col = new int[4]; // 4
  public byte flag; // 1
  public byte transp; // 1
  public short mode; // 2
  public short tile; // 2
  public short unwrap; // 2

  public void read(ByteBuffer buffer) {
    tpage = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<uv.length;i++) for(int j=0;j<uv[i].length;j++) uv[i][j]=buffer.getFloat();
    for(int i=0;i<col.length;i++) col[i]=buffer.getInt();
    flag = buffer.get();
    transp = buffer.get();
    mode = buffer.getShort();
    tile = buffer.getShort();
    unwrap = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(tpage!=null?tpage.hashCode():0);
    for(int i=0; i<uv.length; i++)  for(int j=0;j<uv[i].length;j++) buffer.writeFloat(uv[i][j]);
    for(int i=0;i<col.length;i++) buffer.writeInt(col[i]);
    buffer.writeByte(flag);
    buffer.writeByte(transp);
    buffer.writeShort(mode);
    buffer.writeShort(tile);
    buffer.writeShort(unwrap);
  }
  public Object setmyarray(Object array) {
    myarray = (TFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TFace:\n");
    sb.append("  tpage: ").append(tpage).append("\n");
    sb.append("  uv: ").append(Arrays.toString(uv)).append("\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  transp: ").append(transp).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  tile: ").append(tile).append("\n");
    sb.append("  unwrap: ").append(unwrap).append("\n");
    return sb.toString();
  }
  public TFace copy() { try {return (TFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MTFace implements DNA, Cloneable { // #59
  public MTFace[] myarray;
  public float[][] uv = new float[4][2]; // 4
  public Image tpage; // ptr 496
  public byte flag; // 1
  public byte transp; // 1
  public short mode; // 2
  public short tile; // 2
  public short unwrap; // 2

  public void read(ByteBuffer buffer) {
    for(int i=0;i<uv.length;i++) for(int j=0;j<uv[i].length;j++) uv[i][j]=buffer.getFloat();
    tpage = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    flag = buffer.get();
    transp = buffer.get();
    mode = buffer.getShort();
    tile = buffer.getShort();
    unwrap = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0; i<uv.length; i++)  for(int j=0;j<uv[i].length;j++) buffer.writeFloat(uv[i][j]);
    buffer.writeInt(tpage!=null?tpage.hashCode():0);
    buffer.writeByte(flag);
    buffer.writeByte(transp);
    buffer.writeShort(mode);
    buffer.writeShort(tile);
    buffer.writeShort(unwrap);
  }
  public Object setmyarray(Object array) {
    myarray = (MTFace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MTFace:\n");
    sb.append("  uv: ").append(Arrays.toString(uv)).append("\n");
    sb.append("  tpage: ").append(tpage).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  transp: ").append(transp).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  tile: ").append(tile).append("\n");
    sb.append("  unwrap: ").append(unwrap).append("\n");
    return sb.toString();
  }
  public MTFace copy() { try {return (MTFace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

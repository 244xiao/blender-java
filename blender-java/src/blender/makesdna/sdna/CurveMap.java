package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CurveMap implements DNA, Cloneable { // #331
  public CurveMap[] myarray;
  public short totpoint; // 2
  public short flag; // 2
  public float range; // 4
  public float mintable; // 4
  public float maxtable; // 4
  public float[] ext_in = new float[2]; // 4
  public float[] ext_out = new float[2]; // 4
  public CurveMapPoint curve; // ptr 12
  public CurveMapPoint table; // ptr 12
  public CurveMapPoint premultable; // ptr 12

  public void read(ByteBuffer buffer) {
    totpoint = buffer.getShort();
    flag = buffer.getShort();
    range = buffer.getFloat();
    mintable = buffer.getFloat();
    maxtable = buffer.getFloat();
    for(int i=0;i<ext_in.length;i++) ext_in[i]=buffer.getFloat();
    for(int i=0;i<ext_out.length;i++) ext_out[i]=buffer.getFloat();
    curve = DNATools.link(DNATools.ptr(buffer), CurveMapPoint.class); // get ptr
    table = DNATools.link(DNATools.ptr(buffer), CurveMapPoint.class); // get ptr
    premultable = DNATools.link(DNATools.ptr(buffer), CurveMapPoint.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(totpoint);
    buffer.writeShort(flag);
    buffer.writeFloat(range);
    buffer.writeFloat(mintable);
    buffer.writeFloat(maxtable);
    for(int i=0;i<ext_in.length;i++) buffer.writeFloat(ext_in[i]);
    for(int i=0;i<ext_out.length;i++) buffer.writeFloat(ext_out[i]);
    buffer.writeInt(curve!=null?curve.hashCode():0);
    buffer.writeInt(table!=null?table.hashCode():0);
    buffer.writeInt(premultable!=null?premultable.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (CurveMap[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CurveMap:\n");
    sb.append("  totpoint: ").append(totpoint).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  range: ").append(range).append("\n");
    sb.append("  mintable: ").append(mintable).append("\n");
    sb.append("  maxtable: ").append(maxtable).append("\n");
    sb.append("  ext_in: ").append(Arrays.toString(ext_in)).append("\n");
    sb.append("  ext_out: ").append(Arrays.toString(ext_out)).append("\n");
    sb.append("  curve: ").append(curve).append("\n");
    sb.append("  table: ").append(table).append("\n");
    sb.append("  premultable: ").append(premultable).append("\n");
    return sb.toString();
  }
  public CurveMap copy() { try {return (CurveMap)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

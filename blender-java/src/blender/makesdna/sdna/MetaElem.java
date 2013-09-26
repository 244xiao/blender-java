package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MetaElem extends Link<MetaElem> implements DNA, Cloneable { // #37
  public MetaElem[] myarray;
  public BoundBox bb; // ptr 104
  public int i1; // 4
  public int j1; // 4
  public int k1; // 4
  public int i2; // 4
  public int j2; // 4
  public int k2; // 4
  public short type; // 2
  public short flag; // 2
  public short selcol1; // 2
  public short selcol2; // 2
  public float x; // 4
  public float y; // 4
  public float z; // 4
  public float[] quat = new float[4]; // 4
  public float expx; // 4
  public float expy; // 4
  public float expz; // 4
  public float rad; // 4
  public float rad2; // 4
  public float s; // 4
  public float len; // 4
  public Object mat; // ptr 4
  public Object imat; // ptr 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), MetaElem.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), MetaElem.class); // get ptr
    bb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    i1 = buffer.getInt();
    j1 = buffer.getInt();
    k1 = buffer.getInt();
    i2 = buffer.getInt();
    j2 = buffer.getInt();
    k2 = buffer.getInt();
    type = buffer.getShort();
    flag = buffer.getShort();
    selcol1 = buffer.getShort();
    selcol2 = buffer.getShort();
    x = buffer.getFloat();
    y = buffer.getFloat();
    z = buffer.getFloat();
    for(int i=0;i<quat.length;i++) quat[i]=buffer.getFloat();
    expx = buffer.getFloat();
    expy = buffer.getFloat();
    expz = buffer.getFloat();
    rad = buffer.getFloat();
    rad2 = buffer.getFloat();
    s = buffer.getFloat();
    len = buffer.getFloat();
    mat = DNATools.ptr(buffer); // get ptr
    imat = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(bb!=null?bb.hashCode():0);
    buffer.writeInt(i1);
    buffer.writeInt(j1);
    buffer.writeInt(k1);
    buffer.writeInt(i2);
    buffer.writeInt(j2);
    buffer.writeInt(k2);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(selcol1);
    buffer.writeShort(selcol2);
    buffer.writeFloat(x);
    buffer.writeFloat(y);
    buffer.writeFloat(z);
    for(int i=0;i<quat.length;i++) buffer.writeFloat(quat[i]);
    buffer.writeFloat(expx);
    buffer.writeFloat(expy);
    buffer.writeFloat(expz);
    buffer.writeFloat(rad);
    buffer.writeFloat(rad2);
    buffer.writeFloat(s);
    buffer.writeFloat(len);
    buffer.writeInt(mat!=null?mat.hashCode():0);
    buffer.writeInt(imat!=null?imat.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MetaElem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MetaElem:\n");
    sb.append("  bb: ").append(bb).append("\n");
    sb.append("  i1: ").append(i1).append("\n");
    sb.append("  j1: ").append(j1).append("\n");
    sb.append("  k1: ").append(k1).append("\n");
    sb.append("  i2: ").append(i2).append("\n");
    sb.append("  j2: ").append(j2).append("\n");
    sb.append("  k2: ").append(k2).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  selcol1: ").append(selcol1).append("\n");
    sb.append("  selcol2: ").append(selcol2).append("\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    sb.append("  z: ").append(z).append("\n");
    sb.append("  quat: ").append(Arrays.toString(quat)).append("\n");
    sb.append("  expx: ").append(expx).append("\n");
    sb.append("  expy: ").append(expy).append("\n");
    sb.append("  expz: ").append(expz).append("\n");
    sb.append("  rad: ").append(rad).append("\n");
    sb.append("  rad2: ").append(rad2).append("\n");
    sb.append("  s: ").append(s).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  mat: ").append(mat).append("\n");
    sb.append("  imat: ").append(imat).append("\n");
    return sb.toString();
  }
  public MetaElem copy() { try {return (MetaElem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

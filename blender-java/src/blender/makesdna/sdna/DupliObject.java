package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class DupliObject extends Link<DupliObject> implements DNA, Cloneable { // #116
  public DupliObject[] myarray;
  public bObject ob; // ptr 1296
  public int origlay; // 4
  public int index; // 4
  public int no_draw; // 4
  public int type; // 4
  public int animated; // 4
  public float[][] mat = new float[4][4]; // 4
  public float[][] omat = new float[4][4]; // 4
  public float[] orco = new float[3]; // 4
  public float[] uv = new float[2]; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), DupliObject.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), DupliObject.class); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    origlay = buffer.getInt();
    index = buffer.getInt();
    no_draw = buffer.getInt();
    type = buffer.getInt();
    animated = buffer.getInt();
    for(int i=0;i<mat.length;i++) for(int j=0;j<mat[i].length;j++) mat[i][j]=buffer.getFloat();
    for(int i=0;i<omat.length;i++) for(int j=0;j<omat[i].length;j++) omat[i][j]=buffer.getFloat();
    for(int i=0;i<orco.length;i++) orco[i]=buffer.getFloat();
    for(int i=0;i<uv.length;i++) uv[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeInt(origlay);
    buffer.writeInt(index);
    buffer.writeInt(no_draw);
    buffer.writeInt(type);
    buffer.writeInt(animated);
    for(int i=0; i<mat.length; i++)  for(int j=0;j<mat[i].length;j++) buffer.writeFloat(mat[i][j]);
    for(int i=0; i<omat.length; i++)  for(int j=0;j<omat[i].length;j++) buffer.writeFloat(omat[i][j]);
    for(int i=0;i<orco.length;i++) buffer.writeFloat(orco[i]);
    for(int i=0;i<uv.length;i++) buffer.writeFloat(uv[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (DupliObject[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("DupliObject:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  origlay: ").append(origlay).append("\n");
    sb.append("  index: ").append(index).append("\n");
    sb.append("  no_draw: ").append(no_draw).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  animated: ").append(animated).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  omat: ").append(Arrays.toString(omat)).append("\n");
    sb.append("  orco: ").append(Arrays.toString(orco)).append("\n");
    sb.append("  uv: ").append(Arrays.toString(uv)).append("\n");
    return sb.toString();
  }
  public DupliObject copy() { try {return (DupliObject)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

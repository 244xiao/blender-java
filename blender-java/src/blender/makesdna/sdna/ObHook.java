package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ObHook extends Link<ObHook> implements DNA, Cloneable { // #115
  public ObHook[] myarray;
  public bObject parent; // ptr 1296
  public float[][] parentinv = new float[4][4]; // 4
  public float[][] mat = new float[4][4]; // 4
  public float[] cent = new float[3]; // 4
  public float falloff; // 4
  public byte[] name = new byte[32]; // 1
  public Object indexar; // ptr 4
  public int totindex; // 4
  public int curindex; // 4
  public short type; // 2
  public short active; // 2
  public float force; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ObHook.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ObHook.class); // get ptr
    parent = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    for(int i=0;i<parentinv.length;i++) for(int j=0;j<parentinv[i].length;j++) parentinv[i][j]=buffer.getFloat();
    for(int i=0;i<mat.length;i++) for(int j=0;j<mat[i].length;j++) mat[i][j]=buffer.getFloat();
    for(int i=0;i<cent.length;i++) cent[i]=buffer.getFloat();
    falloff = buffer.getFloat();
    buffer.get(name);
    indexar = DNATools.ptr(buffer); // get ptr
    totindex = buffer.getInt();
    curindex = buffer.getInt();
    type = buffer.getShort();
    active = buffer.getShort();
    force = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(parent!=null?parent.hashCode():0);
    for(int i=0; i<parentinv.length; i++)  for(int j=0;j<parentinv[i].length;j++) buffer.writeFloat(parentinv[i][j]);
    for(int i=0; i<mat.length; i++)  for(int j=0;j<mat[i].length;j++) buffer.writeFloat(mat[i][j]);
    for(int i=0;i<cent.length;i++) buffer.writeFloat(cent[i]);
    buffer.writeFloat(falloff);
    buffer.write(name);
    buffer.writeInt(indexar!=null?indexar.hashCode():0);
    buffer.writeInt(totindex);
    buffer.writeInt(curindex);
    buffer.writeShort(type);
    buffer.writeShort(active);
    buffer.writeFloat(force);
  }
  public Object setmyarray(Object array) {
    myarray = (ObHook[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ObHook:\n");
    sb.append("  parent: ").append(parent).append("\n");
    sb.append("  parentinv: ").append(Arrays.toString(parentinv)).append("\n");
    sb.append("  mat: ").append(Arrays.toString(mat)).append("\n");
    sb.append("  cent: ").append(Arrays.toString(cent)).append("\n");
    sb.append("  falloff: ").append(falloff).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  indexar: ").append(indexar).append("\n");
    sb.append("  totindex: ").append(totindex).append("\n");
    sb.append("  curindex: ").append(curindex).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  active: ").append(active).append("\n");
    sb.append("  force: ").append(force).append("\n");
    return sb.toString();
  }
  public ObHook copy() { try {return (ObHook)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

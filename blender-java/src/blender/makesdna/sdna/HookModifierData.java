package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class HookModifierData extends ModifierData implements DNA, Cloneable { // #91
  public HookModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject object; // ptr 1296
  public byte[] subtarget = new byte[32]; // 1
  public float[][] parentinv = new float[4][4]; // 4
  public float[] cent = new float[3]; // 4
  public float falloff; // 4
  public Object indexar; // ptr 4
  public int totindex; // 4
  public float force; // 4
  public byte[] name = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subtarget);
    for(int i=0;i<parentinv.length;i++) for(int j=0;j<parentinv[i].length;j++) parentinv[i][j]=buffer.getFloat();
    for(int i=0;i<cent.length;i++) cent[i]=buffer.getFloat();
    falloff = buffer.getFloat();
    indexar = DNATools.ptr(buffer); // get ptr
    totindex = buffer.getInt();
    force = buffer.getFloat();
    buffer.get(name);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.write(subtarget);
    for(int i=0; i<parentinv.length; i++)  for(int j=0;j<parentinv[i].length;j++) buffer.writeFloat(parentinv[i][j]);
    for(int i=0;i<cent.length;i++) buffer.writeFloat(cent[i]);
    buffer.writeFloat(falloff);
    buffer.writeInt(indexar!=null?indexar.hashCode():0);
    buffer.writeInt(totindex);
    buffer.writeFloat(force);
    buffer.write(name);
  }
  public Object setmyarray(Object array) {
    myarray = (HookModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("HookModifierData:\n");
    sb.append(super.toString());
    sb.append("  object: ").append(object).append("\n");
    sb.append("  subtarget: ").append(new String(subtarget)).append("\n");
    sb.append("  parentinv: ").append(Arrays.toString(parentinv)).append("\n");
    sb.append("  cent: ").append(Arrays.toString(cent)).append("\n");
    sb.append("  falloff: ").append(falloff).append("\n");
    sb.append("  indexar: ").append(indexar).append("\n");
    sb.append("  totindex: ").append(totindex).append("\n");
    sb.append("  force: ").append(force).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    return sb.toString();
  }
  public HookModifierData copy() { try {return (HookModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

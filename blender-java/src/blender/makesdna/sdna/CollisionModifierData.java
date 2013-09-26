package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CollisionModifierData extends ModifierData implements DNA, Cloneable { // #94
  public CollisionModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public MVert x; // ptr 20
  public MVert xnew; // ptr 20
  public MVert xold; // ptr 20
  public MVert current_xnew; // ptr 20
  public MVert current_x; // ptr 20
  public MVert current_v; // ptr 20
  public MFace mfaces; // ptr 20
  public int numverts; // 4
  public int numfaces; // 4
  public float time_x; // 4
  public float time_xnew; // 4
  public Object bvhtree; // ptr (BVHTree) 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    x = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    xnew = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    xold = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    current_xnew = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    current_x = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    current_v = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    mfaces = DNATools.link(DNATools.ptr(buffer), MFace.class); // get ptr
    numverts = buffer.getInt();
    numfaces = buffer.getInt();
    time_x = buffer.getFloat();
    time_xnew = buffer.getFloat();
    bvhtree = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(x!=null?x.hashCode():0);
    buffer.writeInt(xnew!=null?xnew.hashCode():0);
    buffer.writeInt(xold!=null?xold.hashCode():0);
    buffer.writeInt(current_xnew!=null?current_xnew.hashCode():0);
    buffer.writeInt(current_x!=null?current_x.hashCode():0);
    buffer.writeInt(current_v!=null?current_v.hashCode():0);
    buffer.writeInt(mfaces!=null?mfaces.hashCode():0);
    buffer.writeInt(numverts);
    buffer.writeInt(numfaces);
    buffer.writeFloat(time_x);
    buffer.writeFloat(time_xnew);
    buffer.writeInt(bvhtree!=null?bvhtree.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (CollisionModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CollisionModifierData:\n");
    sb.append(super.toString());
    sb.append("  x: ").append(x).append("\n");
    sb.append("  xnew: ").append(xnew).append("\n");
    sb.append("  xold: ").append(xold).append("\n");
    sb.append("  current_xnew: ").append(current_xnew).append("\n");
    sb.append("  current_x: ").append(current_x).append("\n");
    sb.append("  current_v: ").append(current_v).append("\n");
    sb.append("  mfaces: ").append(mfaces).append("\n");
    sb.append("  numverts: ").append(numverts).append("\n");
    sb.append("  numfaces: ").append(numfaces).append("\n");
    sb.append("  time_x: ").append(time_x).append("\n");
    sb.append("  time_xnew: ").append(time_xnew).append("\n");
    sb.append("  bvhtree: ").append(bvhtree).append("\n");
    return sb.toString();
  }
  public CollisionModifierData copy() { try {return (CollisionModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

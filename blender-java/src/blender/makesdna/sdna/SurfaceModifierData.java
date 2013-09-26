package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SurfaceModifierData extends ModifierData implements DNA, Cloneable { // #95
  public SurfaceModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public MVert x; // ptr 20
  public MVert v; // ptr 20
  public Object dm; // ptr (DerivedMesh) 0
  public Object bvhtree; // ptr (BVHTreeFromMesh) 0
  public int cfra; // 4
  public int numverts; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    x = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    v = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    dm = DNATools.ptr(buffer); // get ptr
    bvhtree = DNATools.ptr(buffer); // get ptr
    cfra = buffer.getInt();
    numverts = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(x!=null?x.hashCode():0);
    buffer.writeInt(v!=null?v.hashCode():0);
    buffer.writeInt(dm!=null?dm.hashCode():0);
    buffer.writeInt(bvhtree!=null?bvhtree.hashCode():0);
    buffer.writeInt(cfra);
    buffer.writeInt(numverts);
  }
  public Object setmyarray(Object array) {
    myarray = (SurfaceModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SurfaceModifierData:\n");
    sb.append(super.toString());
    sb.append("  x: ").append(x).append("\n");
    sb.append("  v: ").append(v).append("\n");
    sb.append("  dm: ").append(dm).append("\n");
    sb.append("  bvhtree: ").append(bvhtree).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  numverts: ").append(numverts).append("\n");
    return sb.toString();
  }
  public SurfaceModifierData copy() { try {return (SurfaceModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

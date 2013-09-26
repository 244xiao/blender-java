package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ClothModifierData extends ModifierData implements DNA, Cloneable { // #93
  public ClothModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public Scene scene; // ptr 1552
  public Object clothObject; // ptr (Cloth) 0
  public ClothSimSettings sim_parms; // ptr 152
  public ClothCollSettings coll_parms; // ptr 40
  public PointCache point_cache; // ptr 528
  public ListBase ptcaches = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    clothObject = DNATools.ptr(buffer); // get ptr
    sim_parms = DNATools.link(DNATools.ptr(buffer), ClothSimSettings.class); // get ptr
    coll_parms = DNATools.link(DNATools.ptr(buffer), ClothCollSettings.class); // get ptr
    point_cache = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
    ptcaches.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(clothObject!=null?clothObject.hashCode():0);
    buffer.writeInt(sim_parms!=null?sim_parms.hashCode():0);
    buffer.writeInt(coll_parms!=null?coll_parms.hashCode():0);
    buffer.writeInt(point_cache!=null?point_cache.hashCode():0);
    ptcaches.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (ClothModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ClothModifierData:\n");
    sb.append(super.toString());
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  clothObject: ").append(clothObject).append("\n");
    sb.append("  sim_parms: ").append(sim_parms).append("\n");
    sb.append("  coll_parms: ").append(coll_parms).append("\n");
    sb.append("  point_cache: ").append(point_cache).append("\n");
    sb.append("  ptcaches: ").append(ptcaches).append("\n");
    return sb.toString();
  }
  public ClothModifierData copy() { try {return (ClothModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

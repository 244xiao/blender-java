package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FluidsimModifierData extends ModifierData implements DNA, Cloneable { // #104
  public FluidsimModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public FluidsimSettings fss; // ptr 456
  public PointCache point_cache; // ptr 528

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    fss = DNATools.link(DNATools.ptr(buffer), FluidsimSettings.class); // get ptr
    point_cache = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(fss!=null?fss.hashCode():0);
    buffer.writeInt(point_cache!=null?point_cache.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (FluidsimModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FluidsimModifierData:\n");
    sb.append(super.toString());
    sb.append("  fss: ").append(fss).append("\n");
    sb.append("  point_cache: ").append(point_cache).append("\n");
    return sb.toString();
  }
  public FluidsimModifierData copy() { try {return (FluidsimModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

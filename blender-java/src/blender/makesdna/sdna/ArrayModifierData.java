package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ArrayModifierData extends ModifierData implements DNA, Cloneable { // #78
  public ArrayModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject start_cap; // ptr 1296
  public bObject end_cap; // ptr 1296
  public bObject curve_ob; // ptr 1296
  public bObject offset_ob; // ptr 1296
  public float[] offset = new float[3]; // 4
  public float[] scale = new float[3]; // 4
  public float length; // 4
  public float merge_dist; // 4
  public int fit_type; // 4
  public int offset_type; // 4
  public int flags; // 4
  public int count; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    start_cap = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    end_cap = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    curve_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    offset_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    for(int i=0;i<offset.length;i++) offset[i]=buffer.getFloat();
    for(int i=0;i<scale.length;i++) scale[i]=buffer.getFloat();
    length = buffer.getFloat();
    merge_dist = buffer.getFloat();
    fit_type = buffer.getInt();
    offset_type = buffer.getInt();
    flags = buffer.getInt();
    count = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(start_cap!=null?start_cap.hashCode():0);
    buffer.writeInt(end_cap!=null?end_cap.hashCode():0);
    buffer.writeInt(curve_ob!=null?curve_ob.hashCode():0);
    buffer.writeInt(offset_ob!=null?offset_ob.hashCode():0);
    for(int i=0;i<offset.length;i++) buffer.writeFloat(offset[i]);
    for(int i=0;i<scale.length;i++) buffer.writeFloat(scale[i]);
    buffer.writeFloat(length);
    buffer.writeFloat(merge_dist);
    buffer.writeInt(fit_type);
    buffer.writeInt(offset_type);
    buffer.writeInt(flags);
    buffer.writeInt(count);
  }
  public Object setmyarray(Object array) {
    myarray = (ArrayModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ArrayModifierData:\n");
    sb.append(super.toString());
    sb.append("  start_cap: ").append(start_cap).append("\n");
    sb.append("  end_cap: ").append(end_cap).append("\n");
    sb.append("  curve_ob: ").append(curve_ob).append("\n");
    sb.append("  offset_ob: ").append(offset_ob).append("\n");
    sb.append("  offset: ").append(Arrays.toString(offset)).append("\n");
    sb.append("  scale: ").append(Arrays.toString(scale)).append("\n");
    sb.append("  length: ").append(length).append("\n");
    sb.append("  merge_dist: ").append(merge_dist).append("\n");
    sb.append("  fit_type: ").append(fit_type).append("\n");
    sb.append("  offset_type: ").append(offset_type).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  count: ").append(count).append("\n");
    return sb.toString();
  }
  public ArrayModifierData copy() { try {return (ArrayModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

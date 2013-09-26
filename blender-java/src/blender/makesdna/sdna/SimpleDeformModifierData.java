package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SimpleDeformModifierData extends ModifierData implements DNA, Cloneable { // #106
  public SimpleDeformModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject origin; // ptr 1296
  public byte[] vgroup_name = new byte[32]; // 1
  public float factor; // 4
  public float[] limit = new float[2]; // 4
  public byte mode; // 1
  public byte axis; // 1
  public byte originOpts; // 1
  public byte pad; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    origin = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(vgroup_name);
    factor = buffer.getFloat();
    for(int i=0;i<limit.length;i++) limit[i]=buffer.getFloat();
    mode = buffer.get();
    axis = buffer.get();
    originOpts = buffer.get();
    pad = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(origin!=null?origin.hashCode():0);
    buffer.write(vgroup_name);
    buffer.writeFloat(factor);
    for(int i=0;i<limit.length;i++) buffer.writeFloat(limit[i]);
    buffer.writeByte(mode);
    buffer.writeByte(axis);
    buffer.writeByte(originOpts);
    buffer.writeByte(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (SimpleDeformModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SimpleDeformModifierData:\n");
    sb.append(super.toString());
    sb.append("  origin: ").append(origin).append("\n");
    sb.append("  vgroup_name: ").append(new String(vgroup_name)).append("\n");
    sb.append("  factor: ").append(factor).append("\n");
    sb.append("  limit: ").append(Arrays.toString(limit)).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  originOpts: ").append(originOpts).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public SimpleDeformModifierData copy() { try {return (SimpleDeformModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

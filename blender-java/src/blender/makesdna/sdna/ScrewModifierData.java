package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ScrewModifierData extends ModifierData implements DNA, Cloneable { // #109
  public ScrewModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject ob_axis; // ptr 1296
  public int steps; // 4
  public int render_steps; // 4
  public int iter; // 4
  public float screw_ofs; // 4
  public float angle; // 4
  public short axis; // 2
  public short flag; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    ob_axis = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    steps = buffer.getInt();
    render_steps = buffer.getInt();
    iter = buffer.getInt();
    screw_ofs = buffer.getFloat();
    angle = buffer.getFloat();
    axis = buffer.getShort();
    flag = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(ob_axis!=null?ob_axis.hashCode():0);
    buffer.writeInt(steps);
    buffer.writeInt(render_steps);
    buffer.writeInt(iter);
    buffer.writeFloat(screw_ofs);
    buffer.writeFloat(angle);
    buffer.writeShort(axis);
    buffer.writeShort(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (ScrewModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ScrewModifierData:\n");
    sb.append(super.toString());
    sb.append("  ob_axis: ").append(ob_axis).append("\n");
    sb.append("  steps: ").append(steps).append("\n");
    sb.append("  render_steps: ").append(render_steps).append("\n");
    sb.append("  iter: ").append(iter).append("\n");
    sb.append("  screw_ofs: ").append(screw_ofs).append("\n");
    sb.append("  angle: ").append(angle).append("\n");
    sb.append("  axis: ").append(axis).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public ScrewModifierData copy() { try {return (ScrewModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

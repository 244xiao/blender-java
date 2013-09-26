package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FCurve extends Link<FCurve> implements DNA, Cloneable { // #379
  public FCurve[] myarray;
  public bActionGroup grp; // ptr 120
  public ChannelDriver driver; // ptr 296
  public ListBase modifiers = new ListBase(); // 16
  public BezTriple bezt; // ptr 56
  public FPoint fpt; // ptr 16
  public int totvert; // 4
  public float curval; // 4
  public short flag; // 2
  public short extend; // 2
  public int array_index; // 4
  public Object rna_path; // ptr 1
  public int color_mode; // 4
  public float[] color = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), FCurve.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), FCurve.class); // get ptr
    grp = DNATools.link(DNATools.ptr(buffer), bActionGroup.class); // get ptr
    driver = DNATools.link(DNATools.ptr(buffer), ChannelDriver.class); // get ptr
    modifiers.read(buffer);
    bezt = DNATools.link(DNATools.ptr(buffer), BezTriple.class); // get ptr
    fpt = DNATools.link(DNATools.ptr(buffer), FPoint.class); // get ptr
    totvert = buffer.getInt();
    curval = buffer.getFloat();
    flag = buffer.getShort();
    extend = buffer.getShort();
    array_index = buffer.getInt();
    rna_path = DNATools.ptr(buffer); // get ptr
    color_mode = buffer.getInt();
    for(int i=0;i<color.length;i++) color[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(grp!=null?grp.hashCode():0);
    buffer.writeInt(driver!=null?driver.hashCode():0);
    modifiers.write(buffer);
    buffer.writeInt(bezt!=null?bezt.hashCode():0);
    buffer.writeInt(fpt!=null?fpt.hashCode():0);
    buffer.writeInt(totvert);
    buffer.writeFloat(curval);
    buffer.writeShort(flag);
    buffer.writeShort(extend);
    buffer.writeInt(array_index);
    buffer.writeInt(rna_path!=null?rna_path.hashCode():0);
    buffer.writeInt(color_mode);
    for(int i=0;i<color.length;i++) buffer.writeFloat(color[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (FCurve[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FCurve:\n");
    sb.append("  grp: ").append(grp).append("\n");
    sb.append("  driver: ").append(driver).append("\n");
    sb.append("  modifiers: ").append(modifiers).append("\n");
    sb.append("  bezt: ").append(bezt).append("\n");
    sb.append("  fpt: ").append(fpt).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  extend: ").append(extend).append("\n");
    sb.append("  array_index: ").append(array_index).append("\n");
    sb.append("  rna_path: ").append(rna_path).append("\n");
    sb.append("  color_mode: ").append(color_mode).append("\n");
    sb.append("  color: ").append(Arrays.toString(color)).append("\n");
    return sb.toString();
  }
  public FCurve copy() { try {return (FCurve)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

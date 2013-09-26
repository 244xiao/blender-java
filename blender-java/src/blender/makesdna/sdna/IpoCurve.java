package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IpoCurve extends Link<IpoCurve> implements DNA, Cloneable { // #13
  public IpoCurve[] myarray;
  public BPoint bp; // ptr 36
  public BezTriple bezt; // ptr 56
  public rctf maxrct = new rctf(); // 16
  public rctf totrct = new rctf(); // 16
  public short blocktype; // 2
  public short adrcode; // 2
  public short vartype; // 2
  public short totvert; // 2
  public short ipo; // 2
  public short extrap; // 2
  public short flag; // 2
  public short rt; // 2
  public float ymin; // 4
  public float ymax; // 4
  public int bitmask; // 4
  public float slide_min; // 4
  public float slide_max; // 4
  public float curval; // 4
  public IpoDriver driver; // ptr 144

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), IpoCurve.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), IpoCurve.class); // get ptr
    bp = DNATools.link(DNATools.ptr(buffer), BPoint.class); // get ptr
    bezt = DNATools.link(DNATools.ptr(buffer), BezTriple.class); // get ptr
    maxrct.read(buffer);
    totrct.read(buffer);
    blocktype = buffer.getShort();
    adrcode = buffer.getShort();
    vartype = buffer.getShort();
    totvert = buffer.getShort();
    ipo = buffer.getShort();
    extrap = buffer.getShort();
    flag = buffer.getShort();
    rt = buffer.getShort();
    ymin = buffer.getFloat();
    ymax = buffer.getFloat();
    bitmask = buffer.getInt();
    slide_min = buffer.getFloat();
    slide_max = buffer.getFloat();
    curval = buffer.getFloat();
    driver = DNATools.link(DNATools.ptr(buffer), IpoDriver.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(bp!=null?bp.hashCode():0);
    buffer.writeInt(bezt!=null?bezt.hashCode():0);
    maxrct.write(buffer);
    totrct.write(buffer);
    buffer.writeShort(blocktype);
    buffer.writeShort(adrcode);
    buffer.writeShort(vartype);
    buffer.writeShort(totvert);
    buffer.writeShort(ipo);
    buffer.writeShort(extrap);
    buffer.writeShort(flag);
    buffer.writeShort(rt);
    buffer.writeFloat(ymin);
    buffer.writeFloat(ymax);
    buffer.writeInt(bitmask);
    buffer.writeFloat(slide_min);
    buffer.writeFloat(slide_max);
    buffer.writeFloat(curval);
    buffer.writeInt(driver!=null?driver.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (IpoCurve[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("IpoCurve:\n");
    sb.append("  bp: ").append(bp).append("\n");
    sb.append("  bezt: ").append(bezt).append("\n");
    sb.append("  maxrct: ").append(maxrct).append("\n");
    sb.append("  totrct: ").append(totrct).append("\n");
    sb.append("  blocktype: ").append(blocktype).append("\n");
    sb.append("  adrcode: ").append(adrcode).append("\n");
    sb.append("  vartype: ").append(vartype).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  extrap: ").append(extrap).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  ymin: ").append(ymin).append("\n");
    sb.append("  ymax: ").append(ymax).append("\n");
    sb.append("  bitmask: ").append(bitmask).append("\n");
    sb.append("  slide_min: ").append(slide_min).append("\n");
    sb.append("  slide_max: ").append(slide_max).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    sb.append("  driver: ").append(driver).append("\n");
    return sb.toString();
  }
  public IpoCurve copy() { try {return (IpoCurve)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

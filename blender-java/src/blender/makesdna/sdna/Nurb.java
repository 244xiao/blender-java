package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Nurb extends Link<Nurb> implements DNA, Cloneable { // #41
  public Nurb[] myarray;
  public short type; // 2
  public short mat_nr; // 2
  public short hide; // 2
  public short flag; // 2
  public short pntsu; // 2
  public short pntsv; // 2
  public short resolu; // 2
  public short resolv; // 2
  public short orderu; // 2
  public short orderv; // 2
  public short flagu; // 2
  public short flagv; // 2
  public Object knotsu; // ptr 4
  public Object knotsv; // ptr 4
  public BPoint bp; // ptr 36
  public BezTriple bezt; // ptr 56
  public short tilt_interp; // 2
  public short radius_interp; // 2
  public int charidx; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Nurb.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Nurb.class); // get ptr
    type = buffer.getShort();
    mat_nr = buffer.getShort();
    hide = buffer.getShort();
    flag = buffer.getShort();
    pntsu = buffer.getShort();
    pntsv = buffer.getShort();
    resolu = buffer.getShort();
    resolv = buffer.getShort();
    orderu = buffer.getShort();
    orderv = buffer.getShort();
    flagu = buffer.getShort();
    flagv = buffer.getShort();
    knotsu = DNATools.ptr(buffer); // get ptr
    knotsv = DNATools.ptr(buffer); // get ptr
    bp = DNATools.link(DNATools.ptr(buffer), BPoint.class); // get ptr
    bezt = DNATools.link(DNATools.ptr(buffer), BezTriple.class); // get ptr
    tilt_interp = buffer.getShort();
    radius_interp = buffer.getShort();
    charidx = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(mat_nr);
    buffer.writeShort(hide);
    buffer.writeShort(flag);
    buffer.writeShort(pntsu);
    buffer.writeShort(pntsv);
    buffer.writeShort(resolu);
    buffer.writeShort(resolv);
    buffer.writeShort(orderu);
    buffer.writeShort(orderv);
    buffer.writeShort(flagu);
    buffer.writeShort(flagv);
    buffer.writeInt(knotsu!=null?knotsu.hashCode():0);
    buffer.writeInt(knotsv!=null?knotsv.hashCode():0);
    buffer.writeInt(bp!=null?bp.hashCode():0);
    buffer.writeInt(bezt!=null?bezt.hashCode():0);
    buffer.writeShort(tilt_interp);
    buffer.writeShort(radius_interp);
    buffer.writeInt(charidx);
  }
  public Object setmyarray(Object array) {
    myarray = (Nurb[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Nurb:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  mat_nr: ").append(mat_nr).append("\n");
    sb.append("  hide: ").append(hide).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pntsu: ").append(pntsu).append("\n");
    sb.append("  pntsv: ").append(pntsv).append("\n");
    sb.append("  resolu: ").append(resolu).append("\n");
    sb.append("  resolv: ").append(resolv).append("\n");
    sb.append("  orderu: ").append(orderu).append("\n");
    sb.append("  orderv: ").append(orderv).append("\n");
    sb.append("  flagu: ").append(flagu).append("\n");
    sb.append("  flagv: ").append(flagv).append("\n");
    sb.append("  knotsu: ").append(knotsu).append("\n");
    sb.append("  knotsv: ").append(knotsv).append("\n");
    sb.append("  bp: ").append(bp).append("\n");
    sb.append("  bezt: ").append(bezt).append("\n");
    sb.append("  tilt_interp: ").append(tilt_interp).append("\n");
    sb.append("  radius_interp: ").append(radius_interp).append("\n");
    sb.append("  charidx: ").append(charidx).append("\n");
    return sb.toString();
  }
  public Nurb copy() { try {return (Nurb)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

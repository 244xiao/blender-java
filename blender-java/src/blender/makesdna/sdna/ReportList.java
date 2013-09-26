package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ReportList implements DNA, Cloneable { // #358
  public ReportList[] myarray;
  public ListBase list = new ListBase(); // 16
  public int printlevel; // 4
  public int storelevel; // 4
  public int flag; // 4
  public int pad; // 4
  public Object reporttimer; // ptr (wmTimer) 0

  public void read(ByteBuffer buffer) {
    list.read(buffer);
    printlevel = buffer.getInt();
    storelevel = buffer.getInt();
    flag = buffer.getInt();
    pad = buffer.getInt();
    reporttimer = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    list.write(buffer);
    buffer.writeInt(printlevel);
    buffer.writeInt(storelevel);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
    buffer.writeInt(reporttimer!=null?reporttimer.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ReportList[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ReportList:\n");
    sb.append("  list: ").append(list).append("\n");
    sb.append("  printlevel: ").append(printlevel).append("\n");
    sb.append("  storelevel: ").append(storelevel).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  reporttimer: ").append(reporttimer).append("\n");
    return sb.toString();
  }
  public ReportList copy() { try {return (ReportList)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

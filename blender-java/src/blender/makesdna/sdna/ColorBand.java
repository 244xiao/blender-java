package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ColorBand implements DNA, Cloneable { // #27
  public ColorBand[] myarray;
  public short flag; // 2
  public short tot; // 2
  public short cur; // 2
  public short ipotype; // 2
  public CBData[] data = new CBData[32]; // 24

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    tot = buffer.getShort();
    cur = buffer.getShort();
    ipotype = buffer.getShort();
    for(int i=0;i<data.length;i++) { data[i]=new CBData(); data[i].read(buffer); }
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(tot);
    buffer.writeShort(cur);
    buffer.writeShort(ipotype);
    for(int i=0;i<data.length;i++) data[i].write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (ColorBand[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ColorBand:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  tot: ").append(tot).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    sb.append("  ipotype: ").append(ipotype).append("\n");
    sb.append("  data: ").append(Arrays.toString(data)).append("\n");
    return sb.toString();
  }
  public ColorBand copy() { try {return (ColorBand)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

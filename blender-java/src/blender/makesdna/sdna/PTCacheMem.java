package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PTCacheMem extends Link<PTCacheMem> implements DNA, Cloneable { // #120
  public PTCacheMem[] myarray;
  public int frame; // 4
  public int totpoint; // 4
  public int data_types; // 4
  public int flag; // 4
  public Object[] data = new Object[8]; // ptr 0
  public Object[] cur = new Object[8]; // ptr 0
  public ListBase extradata = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), PTCacheMem.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), PTCacheMem.class); // get ptr
    frame = buffer.getInt();
    totpoint = buffer.getInt();
    data_types = buffer.getInt();
    flag = buffer.getInt();
    for(int i=0;i<data.length;i++) data[i]=DNATools.link(DNATools.ptr(buffer), Object.class);
    for(int i=0;i<cur.length;i++) cur[i]=DNATools.link(DNATools.ptr(buffer), Object.class);
    extradata.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(frame);
    buffer.writeInt(totpoint);
    buffer.writeInt(data_types);
    buffer.writeInt(flag);
    for(int i=0;i<data.length;i++) buffer.writeInt(data[i]!=null?data[i].hashCode():0);
    for(int i=0;i<cur.length;i++) buffer.writeInt(cur[i]!=null?cur[i].hashCode():0);
    extradata.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (PTCacheMem[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PTCacheMem:\n");
    sb.append("  frame: ").append(frame).append("\n");
    sb.append("  totpoint: ").append(totpoint).append("\n");
    sb.append("  data_types: ").append(data_types).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  data: ").append(Arrays.toString(data)).append("\n");
    sb.append("  cur: ").append(Arrays.toString(cur)).append("\n");
    sb.append("  extradata: ").append(extradata).append("\n");
    return sb.toString();
  }
  public PTCacheMem copy() { try {return (PTCacheMem)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

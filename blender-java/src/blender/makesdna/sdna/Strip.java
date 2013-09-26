package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Strip extends Link<Strip> implements DNA, Cloneable { // #202
  public Strip[] myarray;
  public int rt; // 4
  public int len; // 4
  public int us; // 4
  public int done; // 4
  public int startstill; // 4
  public int endstill; // 4
  public StripElem stripdata; // ptr 88
  public byte[] dir = new byte[160]; // 1
  public StripProxy proxy; // ptr 256
  public StripCrop crop; // ptr 16
  public StripTransform transform; // ptr 8
  public StripColorBalance color_balance; // ptr 44

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Strip.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Strip.class); // get ptr
    rt = buffer.getInt();
    len = buffer.getInt();
    us = buffer.getInt();
    done = buffer.getInt();
    startstill = buffer.getInt();
    endstill = buffer.getInt();
    stripdata = DNATools.link(DNATools.ptr(buffer), StripElem.class); // get ptr
    buffer.get(dir);
    proxy = DNATools.link(DNATools.ptr(buffer), StripProxy.class); // get ptr
    crop = DNATools.link(DNATools.ptr(buffer), StripCrop.class); // get ptr
    transform = DNATools.link(DNATools.ptr(buffer), StripTransform.class); // get ptr
    color_balance = DNATools.link(DNATools.ptr(buffer), StripColorBalance.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(rt);
    buffer.writeInt(len);
    buffer.writeInt(us);
    buffer.writeInt(done);
    buffer.writeInt(startstill);
    buffer.writeInt(endstill);
    buffer.writeInt(stripdata!=null?stripdata.hashCode():0);
    buffer.write(dir);
    buffer.writeInt(proxy!=null?proxy.hashCode():0);
    buffer.writeInt(crop!=null?crop.hashCode():0);
    buffer.writeInt(transform!=null?transform.hashCode():0);
    buffer.writeInt(color_balance!=null?color_balance.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Strip[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Strip:\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  us: ").append(us).append("\n");
    sb.append("  done: ").append(done).append("\n");
    sb.append("  startstill: ").append(startstill).append("\n");
    sb.append("  endstill: ").append(endstill).append("\n");
    sb.append("  stripdata: ").append(stripdata).append("\n");
    sb.append("  dir: ").append(new String(dir)).append("\n");
    sb.append("  proxy: ").append(proxy).append("\n");
    sb.append("  crop: ").append(crop).append("\n");
    sb.append("  transform: ").append(transform).append("\n");
    sb.append("  color_balance: ").append(color_balance).append("\n");
    return sb.toString();
  }
  public Strip copy() { try {return (Strip)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

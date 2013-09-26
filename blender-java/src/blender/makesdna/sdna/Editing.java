package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Editing implements DNA, Cloneable { // #206
  public Editing[] myarray;
  public ListBase seqbasep; // ptr 16
  public ListBase seqbase = new ListBase(); // 16
  public ListBase metastack = new ListBase(); // 16
  public Sequence act_seq; // ptr 288
  public byte[] act_imagedir = new byte[256]; // 1
  public byte[] act_sounddir = new byte[256]; // 1
  public int over_ofs; // 4
  public int over_cfra; // 4
  public int over_flag; // 4
  public int pad; // 4
  public rctf over_border = new rctf(); // 16

  public void read(ByteBuffer buffer) {
    seqbasep = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    seqbase.read(buffer);
    metastack.read(buffer);
    act_seq = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    buffer.get(act_imagedir);
    buffer.get(act_sounddir);
    over_ofs = buffer.getInt();
    over_cfra = buffer.getInt();
    over_flag = buffer.getInt();
    pad = buffer.getInt();
    over_border.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(seqbasep!=null?seqbasep.hashCode():0);
    seqbase.write(buffer);
    metastack.write(buffer);
    buffer.writeInt(act_seq!=null?act_seq.hashCode():0);
    buffer.write(act_imagedir);
    buffer.write(act_sounddir);
    buffer.writeInt(over_ofs);
    buffer.writeInt(over_cfra);
    buffer.writeInt(over_flag);
    buffer.writeInt(pad);
    over_border.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (Editing[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Editing:\n");
    sb.append("  seqbasep: ").append(seqbasep).append("\n");
    sb.append("  seqbase: ").append(seqbase).append("\n");
    sb.append("  metastack: ").append(metastack).append("\n");
    sb.append("  act_seq: ").append(act_seq).append("\n");
    sb.append("  act_imagedir: ").append(new String(act_imagedir)).append("\n");
    sb.append("  act_sounddir: ").append(new String(act_sounddir)).append("\n");
    sb.append("  over_ofs: ").append(over_ofs).append("\n");
    sb.append("  over_cfra: ").append(over_cfra).append("\n");
    sb.append("  over_flag: ").append(over_flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  over_border: ").append(over_border).append("\n");
    return sb.toString();
  }
  public Editing copy() { try {return (Editing)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PointCache extends Link<PointCache> implements DNA, Cloneable { // #121
  public PointCache[] myarray;
  public int flag; // 4
  public int step; // 4
  public int simframe; // 4
  public int startframe; // 4
  public int endframe; // 4
  public int editframe; // 4
  public int last_exact; // 4
  public int totpoint; // 4
  public int index; // 4
  public short compression; // 2
  public short rt; // 2
  public byte[] name = new byte[64]; // 1
  public byte[] prev_name = new byte[64]; // 1
  public byte[] info = new byte[64]; // 1
  public byte[] path = new byte[240]; // 1
  public Object cached_frames; // ptr 1
  public ListBase mem_cache = new ListBase(); // 16
  public Object edit; // ptr (PTCacheEdit) 0
  public Object free_edit; // func ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), PointCache.class); // get ptr
    flag = buffer.getInt();
    step = buffer.getInt();
    simframe = buffer.getInt();
    startframe = buffer.getInt();
    endframe = buffer.getInt();
    editframe = buffer.getInt();
    last_exact = buffer.getInt();
    totpoint = buffer.getInt();
    index = buffer.getInt();
    compression = buffer.getShort();
    rt = buffer.getShort();
    buffer.get(name);
    buffer.get(prev_name);
    buffer.get(info);
    buffer.get(path);
    cached_frames = DNATools.ptr(buffer); // get ptr
    mem_cache.read(buffer);
    edit = DNATools.ptr(buffer); // get ptr
    free_edit = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(step);
    buffer.writeInt(simframe);
    buffer.writeInt(startframe);
    buffer.writeInt(endframe);
    buffer.writeInt(editframe);
    buffer.writeInt(last_exact);
    buffer.writeInt(totpoint);
    buffer.writeInt(index);
    buffer.writeShort(compression);
    buffer.writeShort(rt);
    buffer.write(name);
    buffer.write(prev_name);
    buffer.write(info);
    buffer.write(path);
    buffer.writeInt(cached_frames!=null?cached_frames.hashCode():0);
    mem_cache.write(buffer);
    buffer.writeInt(edit!=null?edit.hashCode():0);
    buffer.writeInt(free_edit!=null?free_edit.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PointCache[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PointCache:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  step: ").append(step).append("\n");
    sb.append("  simframe: ").append(simframe).append("\n");
    sb.append("  startframe: ").append(startframe).append("\n");
    sb.append("  endframe: ").append(endframe).append("\n");
    sb.append("  editframe: ").append(editframe).append("\n");
    sb.append("  last_exact: ").append(last_exact).append("\n");
    sb.append("  totpoint: ").append(totpoint).append("\n");
    sb.append("  index: ").append(index).append("\n");
    sb.append("  compression: ").append(compression).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  prev_name: ").append(new String(prev_name)).append("\n");
    sb.append("  info: ").append(new String(info)).append("\n");
    sb.append("  path: ").append(new String(path)).append("\n");
    sb.append("  cached_frames: ").append(cached_frames).append("\n");
    sb.append("  mem_cache: ").append(mem_cache).append("\n");
    sb.append("  edit: ").append(edit).append("\n");
    sb.append("  free_edit: ").append(free_edit).append("\n");
    return sb.toString();
  }
  public PointCache copy() { try {return (PointCache)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

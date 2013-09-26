package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceFile extends SpaceLink implements DNA, Cloneable { // #162
  public SpaceFile[] myarray;
  public int scroll_offset; // 4
  public FileSelectParams params; // ptr 608
  public Object files; // ptr (FileList) 0
  public ListBase folders_prev; // ptr 16
  public ListBase folders_next; // ptr 16
  public wmOperator op; // ptr 168
  public Object smoothscroll_timer; // ptr (wmTimer) 0
  public Object layout; // ptr (FileLayout) 0
  public short recentnr; // 2
  public short bookmarknr; // 2
  public short systemnr; // 2
  public short pad2; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    scroll_offset = buffer.getInt();
    params = DNATools.link(DNATools.ptr(buffer), FileSelectParams.class); // get ptr
    files = DNATools.ptr(buffer); // get ptr
    folders_prev = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    folders_next = DNATools.link(DNATools.ptr(buffer), ListBase.class); // get ptr
    op = DNATools.link(DNATools.ptr(buffer), wmOperator.class); // get ptr
    smoothscroll_timer = DNATools.ptr(buffer); // get ptr
    layout = DNATools.ptr(buffer); // get ptr
    recentnr = buffer.getShort();
    bookmarknr = buffer.getShort();
    systemnr = buffer.getShort();
    pad2 = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeInt(scroll_offset);
    buffer.writeInt(params!=null?params.hashCode():0);
    buffer.writeInt(files!=null?files.hashCode():0);
    buffer.writeInt(folders_prev!=null?folders_prev.hashCode():0);
    buffer.writeInt(folders_next!=null?folders_next.hashCode():0);
    buffer.writeInt(op!=null?op.hashCode():0);
    buffer.writeInt(smoothscroll_timer!=null?smoothscroll_timer.hashCode():0);
    buffer.writeInt(layout!=null?layout.hashCode():0);
    buffer.writeShort(recentnr);
    buffer.writeShort(bookmarknr);
    buffer.writeShort(systemnr);
    buffer.writeShort(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceFile[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceFile:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  scroll_offset: ").append(scroll_offset).append("\n");
    sb.append("  params: ").append(params).append("\n");
    sb.append("  files: ").append(files).append("\n");
    sb.append("  folders_prev: ").append(folders_prev).append("\n");
    sb.append("  folders_next: ").append(folders_next).append("\n");
    sb.append("  op: ").append(op).append("\n");
    sb.append("  smoothscroll_timer: ").append(smoothscroll_timer).append("\n");
    sb.append("  layout: ").append(layout).append("\n");
    sb.append("  recentnr: ").append(recentnr).append("\n");
    sb.append("  bookmarknr: ").append(bookmarknr).append("\n");
    sb.append("  systemnr: ").append(systemnr).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public SpaceFile copy() { try {return (SpaceFile)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

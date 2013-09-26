package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmWindowManager extends ID implements DNA, Cloneable { // #359
  public wmWindowManager[] myarray;
  public ID id = (ID)this;
  public wmWindow windrawable; // ptr 224
  public wmWindow winactive; // ptr 224
  public ListBase windows = new ListBase(); // 16
  public int initialized; // 4
  public short file_saved; // 2
  public short op_undo_depth; // 2
  public ListBase operators = new ListBase(); // 16
  public ListBase queue = new ListBase(); // 16
  public ReportList reports = new ReportList(); // 40
  public ListBase jobs = new ListBase(); // 16
  public ListBase paintcursors = new ListBase(); // 16
  public ListBase drags = new ListBase(); // 16
  public ListBase keyconfigs = new ListBase(); // 16
  public wmKeyConfig defaultconf; // ptr 168
  public ListBase timers = new ListBase(); // 16
  public Object autosavetimer; // ptr (wmTimer) 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    windrawable = DNATools.link(DNATools.ptr(buffer), wmWindow.class); // get ptr
    winactive = DNATools.link(DNATools.ptr(buffer), wmWindow.class); // get ptr
    windows.read(buffer);
    initialized = buffer.getInt();
    file_saved = buffer.getShort();
    op_undo_depth = buffer.getShort();
    operators.read(buffer);
    queue.read(buffer);
    reports.read(buffer);
    jobs.read(buffer);
    paintcursors.read(buffer);
    drags.read(buffer);
    keyconfigs.read(buffer);
    defaultconf = DNATools.link(DNATools.ptr(buffer), wmKeyConfig.class); // get ptr
    timers.read(buffer);
    autosavetimer = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(windrawable!=null?windrawable.hashCode():0);
    buffer.writeInt(winactive!=null?winactive.hashCode():0);
    windows.write(buffer);
    buffer.writeInt(initialized);
    buffer.writeShort(file_saved);
    buffer.writeShort(op_undo_depth);
    operators.write(buffer);
    queue.write(buffer);
    reports.write(buffer);
    jobs.write(buffer);
    paintcursors.write(buffer);
    drags.write(buffer);
    keyconfigs.write(buffer);
    buffer.writeInt(defaultconf!=null?defaultconf.hashCode():0);
    timers.write(buffer);
    buffer.writeInt(autosavetimer!=null?autosavetimer.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (wmWindowManager[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmWindowManager:\n");
    sb.append(super.toString());
    sb.append("  windrawable: ").append(windrawable).append("\n");
    sb.append("  winactive: ").append(winactive).append("\n");
    sb.append("  windows: ").append(windows).append("\n");
    sb.append("  initialized: ").append(initialized).append("\n");
    sb.append("  file_saved: ").append(file_saved).append("\n");
    sb.append("  op_undo_depth: ").append(op_undo_depth).append("\n");
    sb.append("  operators: ").append(operators).append("\n");
    sb.append("  queue: ").append(queue).append("\n");
    sb.append("  reports: ").append(reports).append("\n");
    sb.append("  jobs: ").append(jobs).append("\n");
    sb.append("  paintcursors: ").append(paintcursors).append("\n");
    sb.append("  drags: ").append(drags).append("\n");
    sb.append("  keyconfigs: ").append(keyconfigs).append("\n");
    sb.append("  defaultconf: ").append(defaultconf).append("\n");
    sb.append("  timers: ").append(timers).append("\n");
    sb.append("  autosavetimer: ").append(autosavetimer).append("\n");
    return sb.toString();
  }
  public wmWindowManager copy() { try {return (wmWindowManager)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

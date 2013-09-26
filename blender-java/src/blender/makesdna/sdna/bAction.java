package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bAction extends ID implements DNA, Cloneable { // #271
  public bAction[] myarray;
  public ID id = (ID)this;
  public ListBase curves = new ListBase(); // 16
  public ListBase chanbase = new ListBase(); // 16
  public ListBase groups = new ListBase(); // 16
  public ListBase markers = new ListBase(); // 16
  public int flag; // 4
  public int active_marker; // 4
  public int idroot; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    curves.read(buffer);
    chanbase.read(buffer);
    groups.read(buffer);
    markers.read(buffer);
    flag = buffer.getInt();
    active_marker = buffer.getInt();
    idroot = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    curves.write(buffer);
    chanbase.write(buffer);
    groups.write(buffer);
    markers.write(buffer);
    buffer.writeInt(flag);
    buffer.writeInt(active_marker);
    buffer.writeInt(idroot);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bAction[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bAction:\n");
    sb.append(super.toString());
    sb.append("  curves: ").append(curves).append("\n");
    sb.append("  chanbase: ").append(chanbase).append("\n");
    sb.append("  groups: ").append(groups).append("\n");
    sb.append("  markers: ").append(markers).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  active_marker: ").append(active_marker).append("\n");
    sb.append("  idroot: ").append(idroot).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bAction copy() { try {return (bAction)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

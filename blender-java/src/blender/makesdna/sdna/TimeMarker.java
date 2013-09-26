package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TimeMarker extends Link<TimeMarker> implements DNA, Cloneable { // #139
  public TimeMarker[] myarray;
  public int frame; // 4
  public byte[] name = new byte[64]; // 1
  public int flag; // 4
  public bObject camera; // ptr 1296

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), TimeMarker.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), TimeMarker.class); // get ptr
    frame = buffer.getInt();
    buffer.get(name);
    flag = buffer.getInt();
    camera = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(frame);
    buffer.write(name);
    buffer.writeInt(flag);
    buffer.writeInt(camera!=null?camera.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (TimeMarker[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TimeMarker:\n");
    sb.append("  frame: ").append(frame).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  camera: ").append(camera).append("\n");
    return sb.toString();
  }
  public TimeMarker copy() { try {return (TimeMarker)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

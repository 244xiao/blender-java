package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PreviewImage implements DNA, Cloneable { // #11
  public PreviewImage[] myarray;
  public int[] w = new int[2]; // 4
  public int[] h = new int[2]; // 4
  public short[] changed = new short[2]; // 2
  public short[] changed_timestamp = new short[2]; // 2
  public Object[] rect = new Object[2]; // ptr 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<w.length;i++) w[i]=buffer.getInt();
    for(int i=0;i<h.length;i++) h[i]=buffer.getInt();
    for(int i=0;i<changed.length;i++) changed[i]=buffer.getShort();
    for(int i=0;i<changed_timestamp.length;i++) changed_timestamp[i]=buffer.getShort();
    for(int i=0;i<rect.length;i++) rect[i]=DNATools.link(DNATools.ptr(buffer), Object.class);
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<w.length;i++) buffer.writeInt(w[i]);
    for(int i=0;i<h.length;i++) buffer.writeInt(h[i]);
    for(int i=0;i<changed.length;i++) buffer.writeShort(changed[i]);
    for(int i=0;i<changed_timestamp.length;i++) buffer.writeShort(changed_timestamp[i]);
    for(int i=0;i<rect.length;i++) buffer.writeInt(rect[i]!=null?rect[i].hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PreviewImage[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PreviewImage:\n");
    sb.append("  w: ").append(Arrays.toString(w)).append("\n");
    sb.append("  h: ").append(Arrays.toString(h)).append("\n");
    sb.append("  changed: ").append(Arrays.toString(changed)).append("\n");
    sb.append("  changed_timestamp: ").append(Arrays.toString(changed_timestamp)).append("\n");
    sb.append("  rect: ").append(Arrays.toString(rect)).append("\n");
    return sb.toString();
  }
  public PreviewImage copy() { try {return (PreviewImage)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

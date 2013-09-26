package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bGPDlayer extends Link<bGPDlayer> implements DNA, Cloneable { // #356
  public bGPDlayer[] myarray;
  public ListBase frames = new ListBase(); // 16
  public bGPDframe actframe; // ptr 40
  public int flag; // 4
  public short thickness; // 2
  public short gstep; // 2
  public float[] color = new float[4]; // 4
  public byte[] info = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bGPDlayer.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bGPDlayer.class); // get ptr
    frames.read(buffer);
    actframe = DNATools.link(DNATools.ptr(buffer), bGPDframe.class); // get ptr
    flag = buffer.getInt();
    thickness = buffer.getShort();
    gstep = buffer.getShort();
    for(int i=0;i<color.length;i++) color[i]=buffer.getFloat();
    buffer.get(info);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    frames.write(buffer);
    buffer.writeInt(actframe!=null?actframe.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeShort(thickness);
    buffer.writeShort(gstep);
    for(int i=0;i<color.length;i++) buffer.writeFloat(color[i]);
    buffer.write(info);
  }
  public Object setmyarray(Object array) {
    myarray = (bGPDlayer[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bGPDlayer:\n");
    sb.append("  frames: ").append(frames).append("\n");
    sb.append("  actframe: ").append(actframe).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  thickness: ").append(thickness).append("\n");
    sb.append("  gstep: ").append(gstep).append("\n");
    sb.append("  color: ").append(Arrays.toString(color)).append("\n");
    sb.append("  info: ").append(new String(info)).append("\n");
    return sb.toString();
  }
  public bGPDlayer copy() { try {return (bGPDlayer)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

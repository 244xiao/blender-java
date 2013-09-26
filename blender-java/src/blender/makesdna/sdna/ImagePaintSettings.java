package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ImagePaintSettings implements DNA, Cloneable { // #141
  public ImagePaintSettings[] myarray;
  public Paint paint = new Paint(); // 24
  public short flag; // 2
  public short pad; // 2
  public short seam_bleed; // 2
  public short normal_angle; // 2
  public short[] screen_grab_size = new short[2]; // 2
  public int pad1; // 4
  public Object paintcursor; // ptr 0

  public void read(ByteBuffer buffer) {
    paint.read(buffer);
    flag = buffer.getShort();
    pad = buffer.getShort();
    seam_bleed = buffer.getShort();
    normal_angle = buffer.getShort();
    for(int i=0;i<screen_grab_size.length;i++) screen_grab_size[i]=buffer.getShort();
    pad1 = buffer.getInt();
    paintcursor = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    paint.write(buffer);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
    buffer.writeShort(seam_bleed);
    buffer.writeShort(normal_angle);
    for(int i=0;i<screen_grab_size.length;i++) buffer.writeShort(screen_grab_size[i]);
    buffer.writeInt(pad1);
    buffer.writeInt(paintcursor!=null?paintcursor.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ImagePaintSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ImagePaintSettings:\n");
    sb.append("  paint: ").append(paint).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  seam_bleed: ").append(seam_bleed).append("\n");
    sb.append("  normal_angle: ").append(normal_angle).append("\n");
    sb.append("  screen_grab_size: ").append(Arrays.toString(screen_grab_size)).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  paintcursor: ").append(paintcursor).append("\n");
    return sb.toString();
  }
  public ImagePaintSettings copy() { try {return (ImagePaintSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

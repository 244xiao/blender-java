package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Sculpt implements DNA, Cloneable { // #145
  public Sculpt[] myarray;
  public Paint paint = new Paint(); // 24
  public int flags; // 4
  public int[] radial_symm = new int[3]; // 4
  public float last_x; // 4
  public float last_y; // 4
  public float last_angle; // 4
  public int draw_anchored; // 4
  public int anchored_size; // 4
  public float[] anchored_location = new float[3]; // 4
  public float[] anchored_initial_mouse = new float[2]; // 4
  public int draw_pressure; // 4
  public float pressure_value; // 4
  public float special_rotation; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    paint.read(buffer);
    flags = buffer.getInt();
    for(int i=0;i<radial_symm.length;i++) radial_symm[i]=buffer.getInt();
    last_x = buffer.getFloat();
    last_y = buffer.getFloat();
    last_angle = buffer.getFloat();
    draw_anchored = buffer.getInt();
    anchored_size = buffer.getInt();
    for(int i=0;i<anchored_location.length;i++) anchored_location[i]=buffer.getFloat();
    for(int i=0;i<anchored_initial_mouse.length;i++) anchored_initial_mouse[i]=buffer.getFloat();
    draw_pressure = buffer.getInt();
    pressure_value = buffer.getFloat();
    special_rotation = buffer.getFloat();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    paint.write(buffer);
    buffer.writeInt(flags);
    for(int i=0;i<radial_symm.length;i++) buffer.writeInt(radial_symm[i]);
    buffer.writeFloat(last_x);
    buffer.writeFloat(last_y);
    buffer.writeFloat(last_angle);
    buffer.writeInt(draw_anchored);
    buffer.writeInt(anchored_size);
    for(int i=0;i<anchored_location.length;i++) buffer.writeFloat(anchored_location[i]);
    for(int i=0;i<anchored_initial_mouse.length;i++) buffer.writeFloat(anchored_initial_mouse[i]);
    buffer.writeInt(draw_pressure);
    buffer.writeFloat(pressure_value);
    buffer.writeFloat(special_rotation);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (Sculpt[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Sculpt:\n");
    sb.append("  paint: ").append(paint).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  radial_symm: ").append(Arrays.toString(radial_symm)).append("\n");
    sb.append("  last_x: ").append(last_x).append("\n");
    sb.append("  last_y: ").append(last_y).append("\n");
    sb.append("  last_angle: ").append(last_angle).append("\n");
    sb.append("  draw_anchored: ").append(draw_anchored).append("\n");
    sb.append("  anchored_size: ").append(anchored_size).append("\n");
    sb.append("  anchored_location: ").append(Arrays.toString(anchored_location)).append("\n");
    sb.append("  anchored_initial_mouse: ").append(Arrays.toString(anchored_initial_mouse)).append("\n");
    sb.append("  draw_pressure: ").append(draw_pressure).append("\n");
    sb.append("  pressure_value: ").append(pressure_value).append("\n");
    sb.append("  special_rotation: ").append(special_rotation).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public Sculpt copy() { try {return (Sculpt)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

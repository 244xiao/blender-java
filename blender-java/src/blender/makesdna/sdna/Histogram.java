package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Histogram implements DNA, Cloneable { // #333
  public Histogram[] myarray;
  public int channels; // 4
  public int x_resolution; // 4
  public float[] data_r = new float[256]; // 4
  public float[] data_g = new float[256]; // 4
  public float[] data_b = new float[256]; // 4
  public float[] data_luma = new float[256]; // 4
  public float xmax; // 4
  public float ymax; // 4
  public int mode; // 4
  public int height; // 4

  public void read(ByteBuffer buffer) {
    channels = buffer.getInt();
    x_resolution = buffer.getInt();
    for(int i=0;i<data_r.length;i++) data_r[i]=buffer.getFloat();
    for(int i=0;i<data_g.length;i++) data_g[i]=buffer.getFloat();
    for(int i=0;i<data_b.length;i++) data_b[i]=buffer.getFloat();
    for(int i=0;i<data_luma.length;i++) data_luma[i]=buffer.getFloat();
    xmax = buffer.getFloat();
    ymax = buffer.getFloat();
    mode = buffer.getInt();
    height = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(channels);
    buffer.writeInt(x_resolution);
    for(int i=0;i<data_r.length;i++) buffer.writeFloat(data_r[i]);
    for(int i=0;i<data_g.length;i++) buffer.writeFloat(data_g[i]);
    for(int i=0;i<data_b.length;i++) buffer.writeFloat(data_b[i]);
    for(int i=0;i<data_luma.length;i++) buffer.writeFloat(data_luma[i]);
    buffer.writeFloat(xmax);
    buffer.writeFloat(ymax);
    buffer.writeInt(mode);
    buffer.writeInt(height);
  }
  public Object setmyarray(Object array) {
    myarray = (Histogram[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Histogram:\n");
    sb.append("  channels: ").append(channels).append("\n");
    sb.append("  x_resolution: ").append(x_resolution).append("\n");
    sb.append("  data_r: ").append(Arrays.toString(data_r)).append("\n");
    sb.append("  data_g: ").append(Arrays.toString(data_g)).append("\n");
    sb.append("  data_b: ").append(Arrays.toString(data_b)).append("\n");
    sb.append("  data_luma: ").append(Arrays.toString(data_luma)).append("\n");
    sb.append("  xmax: ").append(xmax).append("\n");
    sb.append("  ymax: ").append(ymax).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  height: ").append(height).append("\n");
    return sb.toString();
  }
  public Histogram copy() { try {return (Histogram)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

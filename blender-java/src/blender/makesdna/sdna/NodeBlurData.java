package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeBlurData implements DNA, Cloneable { // #312
  public NodeBlurData[] myarray;
  public short sizex; // 2
  public short sizey; // 2
  public short samples; // 2
  public short maxspeed; // 2
  public short minspeed; // 2
  public short relative; // 2
  public short aspect; // 2
  public short curved; // 2
  public float fac; // 4
  public float percentx; // 4
  public float percenty; // 4
  public short filtertype; // 2
  public byte bokeh; // 1
  public byte gamma; // 1
  public int image_in_width; // 4
  public int image_in_height; // 4

  public void read(ByteBuffer buffer) {
    sizex = buffer.getShort();
    sizey = buffer.getShort();
    samples = buffer.getShort();
    maxspeed = buffer.getShort();
    minspeed = buffer.getShort();
    relative = buffer.getShort();
    aspect = buffer.getShort();
    curved = buffer.getShort();
    fac = buffer.getFloat();
    percentx = buffer.getFloat();
    percenty = buffer.getFloat();
    filtertype = buffer.getShort();
    bokeh = buffer.get();
    gamma = buffer.get();
    image_in_width = buffer.getInt();
    image_in_height = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(sizex);
    buffer.writeShort(sizey);
    buffer.writeShort(samples);
    buffer.writeShort(maxspeed);
    buffer.writeShort(minspeed);
    buffer.writeShort(relative);
    buffer.writeShort(aspect);
    buffer.writeShort(curved);
    buffer.writeFloat(fac);
    buffer.writeFloat(percentx);
    buffer.writeFloat(percenty);
    buffer.writeShort(filtertype);
    buffer.writeByte(bokeh);
    buffer.writeByte(gamma);
    buffer.writeInt(image_in_width);
    buffer.writeInt(image_in_height);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeBlurData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeBlurData:\n");
    sb.append("  sizex: ").append(sizex).append("\n");
    sb.append("  sizey: ").append(sizey).append("\n");
    sb.append("  samples: ").append(samples).append("\n");
    sb.append("  maxspeed: ").append(maxspeed).append("\n");
    sb.append("  minspeed: ").append(minspeed).append("\n");
    sb.append("  relative: ").append(relative).append("\n");
    sb.append("  aspect: ").append(aspect).append("\n");
    sb.append("  curved: ").append(curved).append("\n");
    sb.append("  fac: ").append(fac).append("\n");
    sb.append("  percentx: ").append(percentx).append("\n");
    sb.append("  percenty: ").append(percenty).append("\n");
    sb.append("  filtertype: ").append(filtertype).append("\n");
    sb.append("  bokeh: ").append(bokeh).append("\n");
    sb.append("  gamma: ").append(gamma).append("\n");
    sb.append("  image_in_width: ").append(image_in_width).append("\n");
    sb.append("  image_in_height: ").append(image_in_height).append("\n");
    return sb.toString();
  }
  public NodeBlurData copy() { try {return (NodeBlurData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

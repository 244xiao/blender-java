package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Brush extends ID implements DNA, Cloneable { // #336
  public Brush[] myarray;
  public ID id = (ID)this;
  public BrushClone clone = new BrushClone(); // 24
  public CurveMapping curve; // ptr 320
  public MTex mtex = new MTex(); // 280
  public Object icon_imbuf; // ptr (ImBuf) 0
  public PreviewImage preview; // ptr 40
  public byte[] icon_filepath = new byte[240]; // 1
  public float normal_weight; // 4
  public short blend; // 2
  public short ob_mode; // 2
  public int size; // 4
  public int flag; // 4
  public float jitter; // 4
  public int spacing; // 4
  public int smooth_stroke_radius; // 4
  public float smooth_stroke_factor; // 4
  public float rate; // 4
  public float[] rgb = new float[3]; // 4
  public float alpha; // 4
  public int sculpt_plane; // 4
  public float plane_offset; // 4
  public byte sculpt_tool; // 1
  public byte vertexpaint_tool; // 1
  public byte imagepaint_tool; // 1
  public byte[] pad3 = new byte[5]; // 1
  public float autosmooth_factor; // 4
  public float crease_pinch_factor; // 4
  public float plane_trim; // 4
  public float height; // 4
  public float texture_sample_bias; // 4
  public int texture_overlay_alpha; // 4
  public float unprojected_radius; // 4
  public float[] add_col = new float[3]; // 4
  public float[] sub_col = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    clone.read(buffer);
    curve = DNATools.link(DNATools.ptr(buffer), CurveMapping.class); // get ptr
    mtex.read(buffer);
    icon_imbuf = DNATools.ptr(buffer); // get ptr
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
    buffer.get(icon_filepath);
    normal_weight = buffer.getFloat();
    blend = buffer.getShort();
    ob_mode = buffer.getShort();
    size = buffer.getInt();
    flag = buffer.getInt();
    jitter = buffer.getFloat();
    spacing = buffer.getInt();
    smooth_stroke_radius = buffer.getInt();
    smooth_stroke_factor = buffer.getFloat();
    rate = buffer.getFloat();
    for(int i=0;i<rgb.length;i++) rgb[i]=buffer.getFloat();
    alpha = buffer.getFloat();
    sculpt_plane = buffer.getInt();
    plane_offset = buffer.getFloat();
    sculpt_tool = buffer.get();
    vertexpaint_tool = buffer.get();
    imagepaint_tool = buffer.get();
    buffer.get(pad3);
    autosmooth_factor = buffer.getFloat();
    crease_pinch_factor = buffer.getFloat();
    plane_trim = buffer.getFloat();
    height = buffer.getFloat();
    texture_sample_bias = buffer.getFloat();
    texture_overlay_alpha = buffer.getInt();
    unprojected_radius = buffer.getFloat();
    for(int i=0;i<add_col.length;i++) add_col[i]=buffer.getFloat();
    for(int i=0;i<sub_col.length;i++) sub_col[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    clone.write(buffer);
    buffer.writeInt(curve!=null?curve.hashCode():0);
    mtex.write(buffer);
    buffer.writeInt(icon_imbuf!=null?icon_imbuf.hashCode():0);
    buffer.writeInt(preview!=null?preview.hashCode():0);
    buffer.write(icon_filepath);
    buffer.writeFloat(normal_weight);
    buffer.writeShort(blend);
    buffer.writeShort(ob_mode);
    buffer.writeInt(size);
    buffer.writeInt(flag);
    buffer.writeFloat(jitter);
    buffer.writeInt(spacing);
    buffer.writeInt(smooth_stroke_radius);
    buffer.writeFloat(smooth_stroke_factor);
    buffer.writeFloat(rate);
    for(int i=0;i<rgb.length;i++) buffer.writeFloat(rgb[i]);
    buffer.writeFloat(alpha);
    buffer.writeInt(sculpt_plane);
    buffer.writeFloat(plane_offset);
    buffer.writeByte(sculpt_tool);
    buffer.writeByte(vertexpaint_tool);
    buffer.writeByte(imagepaint_tool);
    buffer.write(pad3);
    buffer.writeFloat(autosmooth_factor);
    buffer.writeFloat(crease_pinch_factor);
    buffer.writeFloat(plane_trim);
    buffer.writeFloat(height);
    buffer.writeFloat(texture_sample_bias);
    buffer.writeInt(texture_overlay_alpha);
    buffer.writeFloat(unprojected_radius);
    for(int i=0;i<add_col.length;i++) buffer.writeFloat(add_col[i]);
    for(int i=0;i<sub_col.length;i++) buffer.writeFloat(sub_col[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (Brush[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Brush:\n");
    sb.append(super.toString());
    sb.append("  clone: ").append(clone).append("\n");
    sb.append("  curve: ").append(curve).append("\n");
    sb.append("  mtex: ").append(mtex).append("\n");
    sb.append("  icon_imbuf: ").append(icon_imbuf).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  icon_filepath: ").append(new String(icon_filepath)).append("\n");
    sb.append("  normal_weight: ").append(normal_weight).append("\n");
    sb.append("  blend: ").append(blend).append("\n");
    sb.append("  ob_mode: ").append(ob_mode).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  jitter: ").append(jitter).append("\n");
    sb.append("  spacing: ").append(spacing).append("\n");
    sb.append("  smooth_stroke_radius: ").append(smooth_stroke_radius).append("\n");
    sb.append("  smooth_stroke_factor: ").append(smooth_stroke_factor).append("\n");
    sb.append("  rate: ").append(rate).append("\n");
    sb.append("  rgb: ").append(Arrays.toString(rgb)).append("\n");
    sb.append("  alpha: ").append(alpha).append("\n");
    sb.append("  sculpt_plane: ").append(sculpt_plane).append("\n");
    sb.append("  plane_offset: ").append(plane_offset).append("\n");
    sb.append("  sculpt_tool: ").append(sculpt_tool).append("\n");
    sb.append("  vertexpaint_tool: ").append(vertexpaint_tool).append("\n");
    sb.append("  imagepaint_tool: ").append(imagepaint_tool).append("\n");
    sb.append("  pad3: ").append(new String(pad3)).append("\n");
    sb.append("  autosmooth_factor: ").append(autosmooth_factor).append("\n");
    sb.append("  crease_pinch_factor: ").append(crease_pinch_factor).append("\n");
    sb.append("  plane_trim: ").append(plane_trim).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  texture_sample_bias: ").append(texture_sample_bias).append("\n");
    sb.append("  texture_overlay_alpha: ").append(texture_overlay_alpha).append("\n");
    sb.append("  unprojected_radius: ").append(unprojected_radius).append("\n");
    sb.append("  add_col: ").append(Arrays.toString(add_col)).append("\n");
    sb.append("  sub_col: ").append(Arrays.toString(sub_col)).append("\n");
    return sb.toString();
  }
  public Brush copy() { try {return (Brush)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

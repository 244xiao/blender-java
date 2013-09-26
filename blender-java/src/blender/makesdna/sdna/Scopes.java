package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Scopes implements DNA, Cloneable { // #334
  public Scopes[] myarray;
  public int ok; // 4
  public int sample_full; // 4
  public int sample_lines; // 4
  public float accuracy; // 4
  public int wavefrm_mode; // 4
  public float wavefrm_alpha; // 4
  public float wavefrm_yfac; // 4
  public int wavefrm_height; // 4
  public float vecscope_alpha; // 4
  public int vecscope_height; // 4
  public float[][] minmax = new float[3][2]; // 4
  public Histogram hist = new Histogram(); // 4120
  public Object waveform_1; // ptr 4
  public Object waveform_2; // ptr 4
  public Object waveform_3; // ptr 4
  public Object vecscope; // ptr 4
  public int waveform_tot; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    ok = buffer.getInt();
    sample_full = buffer.getInt();
    sample_lines = buffer.getInt();
    accuracy = buffer.getFloat();
    wavefrm_mode = buffer.getInt();
    wavefrm_alpha = buffer.getFloat();
    wavefrm_yfac = buffer.getFloat();
    wavefrm_height = buffer.getInt();
    vecscope_alpha = buffer.getFloat();
    vecscope_height = buffer.getInt();
    for(int i=0;i<minmax.length;i++) for(int j=0;j<minmax[i].length;j++) minmax[i][j]=buffer.getFloat();
    hist.read(buffer);
    waveform_1 = DNATools.ptr(buffer); // get ptr
    waveform_2 = DNATools.ptr(buffer); // get ptr
    waveform_3 = DNATools.ptr(buffer); // get ptr
    vecscope = DNATools.ptr(buffer); // get ptr
    waveform_tot = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(ok);
    buffer.writeInt(sample_full);
    buffer.writeInt(sample_lines);
    buffer.writeFloat(accuracy);
    buffer.writeInt(wavefrm_mode);
    buffer.writeFloat(wavefrm_alpha);
    buffer.writeFloat(wavefrm_yfac);
    buffer.writeInt(wavefrm_height);
    buffer.writeFloat(vecscope_alpha);
    buffer.writeInt(vecscope_height);
    for(int i=0; i<minmax.length; i++)  for(int j=0;j<minmax[i].length;j++) buffer.writeFloat(minmax[i][j]);
    hist.write(buffer);
    buffer.writeInt(waveform_1!=null?waveform_1.hashCode():0);
    buffer.writeInt(waveform_2!=null?waveform_2.hashCode():0);
    buffer.writeInt(waveform_3!=null?waveform_3.hashCode():0);
    buffer.writeInt(vecscope!=null?vecscope.hashCode():0);
    buffer.writeInt(waveform_tot);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (Scopes[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Scopes:\n");
    sb.append("  ok: ").append(ok).append("\n");
    sb.append("  sample_full: ").append(sample_full).append("\n");
    sb.append("  sample_lines: ").append(sample_lines).append("\n");
    sb.append("  accuracy: ").append(accuracy).append("\n");
    sb.append("  wavefrm_mode: ").append(wavefrm_mode).append("\n");
    sb.append("  wavefrm_alpha: ").append(wavefrm_alpha).append("\n");
    sb.append("  wavefrm_yfac: ").append(wavefrm_yfac).append("\n");
    sb.append("  wavefrm_height: ").append(wavefrm_height).append("\n");
    sb.append("  vecscope_alpha: ").append(vecscope_alpha).append("\n");
    sb.append("  vecscope_height: ").append(vecscope_height).append("\n");
    sb.append("  minmax: ").append(Arrays.toString(minmax)).append("\n");
    sb.append("  hist: ").append(hist).append("\n");
    sb.append("  waveform_1: ").append(waveform_1).append("\n");
    sb.append("  waveform_2: ").append(waveform_2).append("\n");
    sb.append("  waveform_3: ").append(waveform_3).append("\n");
    sb.append("  vecscope: ").append(vecscope).append("\n");
    sb.append("  waveform_tot: ").append(waveform_tot).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public Scopes copy() { try {return (Scopes)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

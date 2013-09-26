package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CurveMapping implements DNA, Cloneable { // #332
  public CurveMapping[] myarray;
  public int flag; // 4
  public int cur; // 4
  public int preset; // 4
  public int changed_timestamp; // 4
  public rctf curr = new rctf(); // 16
  public rctf clipr = new rctf(); // 16
  public CurveMap[] cm = new CurveMap[4]; // 56
  public float[] black = new float[3]; // 4
  public float[] white = new float[3]; // 4
  public float[] bwmul = new float[3]; // 4
  public float[] sample = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    cur = buffer.getInt();
    preset = buffer.getInt();
    changed_timestamp = buffer.getInt();
    curr.read(buffer);
    clipr.read(buffer);
    for(int i=0;i<cm.length;i++) { cm[i]=new CurveMap(); cm[i].read(buffer); }
    for(int i=0;i<black.length;i++) black[i]=buffer.getFloat();
    for(int i=0;i<white.length;i++) white[i]=buffer.getFloat();
    for(int i=0;i<bwmul.length;i++) bwmul[i]=buffer.getFloat();
    for(int i=0;i<sample.length;i++) sample[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeInt(cur);
    buffer.writeInt(preset);
    buffer.writeInt(changed_timestamp);
    curr.write(buffer);
    clipr.write(buffer);
    for(int i=0;i<cm.length;i++) cm[i].write(buffer);
    for(int i=0;i<black.length;i++) buffer.writeFloat(black[i]);
    for(int i=0;i<white.length;i++) buffer.writeFloat(white[i]);
    for(int i=0;i<bwmul.length;i++) buffer.writeFloat(bwmul[i]);
    for(int i=0;i<sample.length;i++) buffer.writeFloat(sample[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (CurveMapping[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CurveMapping:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  cur: ").append(cur).append("\n");
    sb.append("  preset: ").append(preset).append("\n");
    sb.append("  changed_timestamp: ").append(changed_timestamp).append("\n");
    sb.append("  curr: ").append(curr).append("\n");
    sb.append("  clipr: ").append(clipr).append("\n");
    sb.append("  cm: ").append(Arrays.toString(cm)).append("\n");
    sb.append("  black: ").append(Arrays.toString(black)).append("\n");
    sb.append("  white: ").append(Arrays.toString(white)).append("\n");
    sb.append("  bwmul: ").append(Arrays.toString(bwmul)).append("\n");
    sb.append("  sample: ").append(Arrays.toString(sample)).append("\n");
    return sb.toString();
  }
  public CurveMapping copy() { try {return (CurveMapping)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

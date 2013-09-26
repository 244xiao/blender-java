package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidData implements DNA, Cloneable { // #395
  public BoidData[] myarray;
  public float health; // 4
  public float[] acc = new float[3]; // 4
  public short state_id; // 2
  public short mode; // 2

  public void read(ByteBuffer buffer) {
    health = buffer.getFloat();
    for(int i=0;i<acc.length;i++) acc[i]=buffer.getFloat();
    state_id = buffer.getShort();
    mode = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(health);
    for(int i=0;i<acc.length;i++) buffer.writeFloat(acc[i]);
    buffer.writeShort(state_id);
    buffer.writeShort(mode);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidData:\n");
    sb.append("  health: ").append(health).append("\n");
    sb.append("  acc: ").append(Arrays.toString(acc)).append("\n");
    sb.append("  state_id: ").append(state_id).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    return sb.toString();
  }
  public BoidData copy() { try {return (BoidData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

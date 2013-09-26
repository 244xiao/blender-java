package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class HairKey implements DNA, Cloneable { // #340
  public HairKey[] myarray;
  public float[] co = new float[3]; // 4
  public float time; // 4
  public float weight; // 4
  public short editflag; // 2
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    for(int i=0;i<co.length;i++) co[i]=buffer.getFloat();
    time = buffer.getFloat();
    weight = buffer.getFloat();
    editflag = buffer.getShort();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<co.length;i++) buffer.writeFloat(co[i]);
    buffer.writeFloat(time);
    buffer.writeFloat(weight);
    buffer.writeShort(editflag);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (HairKey[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("HairKey:\n");
    sb.append("  co: ").append(Arrays.toString(co)).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  weight: ").append(weight).append("\n");
    sb.append("  editflag: ").append(editflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public HairKey copy() { try {return (HairKey)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

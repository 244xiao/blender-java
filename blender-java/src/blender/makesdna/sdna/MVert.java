package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MVert implements DNA, Cloneable { // #52
  public MVert[] myarray;
  public float[] co = new float[3]; // 4
  public short[] no = new short[3]; // 2
  public byte flag; // 1
  public byte bweight; // 1

  public void read(ByteBuffer buffer) {
    for(int i=0;i<co.length;i++) co[i]=buffer.getFloat();
    for(int i=0;i<no.length;i++) no[i]=buffer.getShort();
    flag = buffer.get();
    bweight = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<co.length;i++) buffer.writeFloat(co[i]);
    for(int i=0;i<no.length;i++) buffer.writeShort(no[i]);
    buffer.writeByte(flag);
    buffer.writeByte(bweight);
  }
  public Object setmyarray(Object array) {
    myarray = (MVert[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MVert:\n");
    sb.append("  co: ").append(Arrays.toString(co)).append("\n");
    sb.append("  no: ").append(Arrays.toString(no)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  bweight: ").append(bweight).append("\n");
    return sb.toString();
  }
  public MVert copy() { try {return (MVert)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class GameFraming implements DNA, Cloneable { // #137
  public GameFraming[] myarray;
  public float[] col = new float[3]; // 4
  public byte type; // 1
  public byte pad1; // 1
  public byte pad2; // 1
  public byte pad3; // 1

  public void read(ByteBuffer buffer) {
    for(int i=0;i<col.length;i++) col[i]=buffer.getFloat();
    type = buffer.get();
    pad1 = buffer.get();
    pad2 = buffer.get();
    pad3 = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<col.length;i++) buffer.writeFloat(col[i]);
    buffer.writeByte(type);
    buffer.writeByte(pad1);
    buffer.writeByte(pad2);
    buffer.writeByte(pad3);
  }
  public Object setmyarray(Object array) {
    myarray = (GameFraming[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("GameFraming:\n");
    sb.append("  col: ").append(Arrays.toString(col)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    return sb.toString();
  }
  public GameFraming copy() { try {return (GameFraming)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

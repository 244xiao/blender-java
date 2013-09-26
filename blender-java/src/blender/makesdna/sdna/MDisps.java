package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MDisps implements DNA, Cloneable { // #64
  public MDisps[] myarray;
  public int totdisp; // 4
  public byte[] pad = new byte[4]; // 1
  public float disps; // func ptr 4

  public void read(ByteBuffer buffer) {
    totdisp = buffer.getInt();
    buffer.get(pad);
    disps = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(totdisp);
    buffer.write(pad);
    buffer.writeFloat(disps);
  }
  public Object setmyarray(Object array) {
    myarray = (MDisps[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MDisps:\n");
    sb.append("  totdisp: ").append(totdisp).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  disps: ").append(disps).append("\n");
    return sb.toString();
  }
  public MDisps copy() { try {return (MDisps)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

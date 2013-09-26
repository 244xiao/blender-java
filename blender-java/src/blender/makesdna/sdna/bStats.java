package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bStats implements DNA, Cloneable { // #148
  public bStats[] myarray;
  public int totobj; // 4
  public int totlamp; // 4
  public int totobjsel; // 4
  public int totcurve; // 4
  public int totmesh; // 4
  public int totarmature; // 4
  public int totvert; // 4
  public int totface; // 4

  public void read(ByteBuffer buffer) {
    totobj = buffer.getInt();
    totlamp = buffer.getInt();
    totobjsel = buffer.getInt();
    totcurve = buffer.getInt();
    totmesh = buffer.getInt();
    totarmature = buffer.getInt();
    totvert = buffer.getInt();
    totface = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(totobj);
    buffer.writeInt(totlamp);
    buffer.writeInt(totobjsel);
    buffer.writeInt(totcurve);
    buffer.writeInt(totmesh);
    buffer.writeInt(totarmature);
    buffer.writeInt(totvert);
    buffer.writeInt(totface);
  }
  public Object setmyarray(Object array) {
    myarray = (bStats[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bStats:\n");
    sb.append("  totobj: ").append(totobj).append("\n");
    sb.append("  totlamp: ").append(totlamp).append("\n");
    sb.append("  totobjsel: ").append(totobjsel).append("\n");
    sb.append("  totcurve: ").append(totcurve).append("\n");
    sb.append("  totmesh: ").append(totmesh).append("\n");
    sb.append("  totarmature: ").append(totarmature).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  totface: ").append(totface).append("\n");
    return sb.toString();
  }
  public bStats copy() { try {return (bStats)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class QuicktimeCodecData implements DNA, Cloneable { // #129
  public QuicktimeCodecData[] myarray;
  public Object cdParms; // ptr 0
  public Object pad; // ptr 0
  public int cdSize; // 4
  public int pad2; // 4
  public byte[] qtcodecname = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    cdParms = DNATools.ptr(buffer); // get ptr
    pad = DNATools.ptr(buffer); // get ptr
    cdSize = buffer.getInt();
    pad2 = buffer.getInt();
    buffer.get(qtcodecname);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(cdParms!=null?cdParms.hashCode():0);
    buffer.writeInt(pad!=null?pad.hashCode():0);
    buffer.writeInt(cdSize);
    buffer.writeInt(pad2);
    buffer.write(qtcodecname);
  }
  public Object setmyarray(Object array) {
    myarray = (QuicktimeCodecData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("QuicktimeCodecData:\n");
    sb.append("  cdParms: ").append(cdParms).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  cdSize: ").append(cdSize).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  qtcodecname: ").append(new String(qtcodecname)).append("\n");
    return sb.toString();
  }
  public QuicktimeCodecData copy() { try {return (QuicktimeCodecData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

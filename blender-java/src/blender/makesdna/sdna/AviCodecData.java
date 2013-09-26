package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AviCodecData implements DNA, Cloneable { // #128
  public AviCodecData[] myarray;
  public Object lpFormat; // ptr 0
  public Object lpParms; // ptr 0
  public int cbFormat; // 4
  public int cbParms; // 4
  public int fccType; // 4
  public int fccHandler; // 4
  public int dwKeyFrameEvery; // 4
  public int dwQuality; // 4
  public int dwBytesPerSecond; // 4
  public int dwFlags; // 4
  public int dwInterleaveEvery; // 4
  public int pad; // 4
  public byte[] avicodecname = new byte[128]; // 1

  public void read(ByteBuffer buffer) {
    lpFormat = DNATools.ptr(buffer); // get ptr
    lpParms = DNATools.ptr(buffer); // get ptr
    cbFormat = buffer.getInt();
    cbParms = buffer.getInt();
    fccType = buffer.getInt();
    fccHandler = buffer.getInt();
    dwKeyFrameEvery = buffer.getInt();
    dwQuality = buffer.getInt();
    dwBytesPerSecond = buffer.getInt();
    dwFlags = buffer.getInt();
    dwInterleaveEvery = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(avicodecname);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(lpFormat!=null?lpFormat.hashCode():0);
    buffer.writeInt(lpParms!=null?lpParms.hashCode():0);
    buffer.writeInt(cbFormat);
    buffer.writeInt(cbParms);
    buffer.writeInt(fccType);
    buffer.writeInt(fccHandler);
    buffer.writeInt(dwKeyFrameEvery);
    buffer.writeInt(dwQuality);
    buffer.writeInt(dwBytesPerSecond);
    buffer.writeInt(dwFlags);
    buffer.writeInt(dwInterleaveEvery);
    buffer.writeInt(pad);
    buffer.write(avicodecname);
  }
  public Object setmyarray(Object array) {
    myarray = (AviCodecData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AviCodecData:\n");
    sb.append("  lpFormat: ").append(lpFormat).append("\n");
    sb.append("  lpParms: ").append(lpParms).append("\n");
    sb.append("  cbFormat: ").append(cbFormat).append("\n");
    sb.append("  cbParms: ").append(cbParms).append("\n");
    sb.append("  fccType: ").append(fccType).append("\n");
    sb.append("  fccHandler: ").append(fccHandler).append("\n");
    sb.append("  dwKeyFrameEvery: ").append(dwKeyFrameEvery).append("\n");
    sb.append("  dwQuality: ").append(dwQuality).append("\n");
    sb.append("  dwBytesPerSecond: ").append(dwBytesPerSecond).append("\n");
    sb.append("  dwFlags: ").append(dwFlags).append("\n");
    sb.append("  dwInterleaveEvery: ").append(dwInterleaveEvery).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  avicodecname: ").append(new String(avicodecname)).append("\n");
    return sb.toString();
  }
  public AviCodecData copy() { try {return (AviCodecData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

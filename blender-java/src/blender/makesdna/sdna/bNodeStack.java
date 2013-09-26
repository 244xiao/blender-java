package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNodeStack implements DNA, Cloneable { // #305
  public bNodeStack[] myarray;
  public float[] vec = new float[4]; // 4
  public float min; // 4
  public float max; // 4
  public Object data; // ptr 0
  public short hasinput; // 2
  public short hasoutput; // 2
  public short datatype; // 2
  public short sockettype; // 2

  public void read(ByteBuffer buffer) {
    for(int i=0;i<vec.length;i++) vec[i]=buffer.getFloat();
    min = buffer.getFloat();
    max = buffer.getFloat();
    data = DNATools.ptr(buffer); // get ptr
    hasinput = buffer.getShort();
    hasoutput = buffer.getShort();
    datatype = buffer.getShort();
    sockettype = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<vec.length;i++) buffer.writeFloat(vec[i]);
    buffer.writeFloat(min);
    buffer.writeFloat(max);
    buffer.writeInt(data!=null?data.hashCode():0);
    buffer.writeShort(hasinput);
    buffer.writeShort(hasoutput);
    buffer.writeShort(datatype);
    buffer.writeShort(sockettype);
  }
  public Object setmyarray(Object array) {
    myarray = (bNodeStack[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNodeStack:\n");
    sb.append("  vec: ").append(Arrays.toString(vec)).append("\n");
    sb.append("  min: ").append(min).append("\n");
    sb.append("  max: ").append(max).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  hasinput: ").append(hasinput).append("\n");
    sb.append("  hasoutput: ").append(hasoutput).append("\n");
    sb.append("  datatype: ").append(datatype).append("\n");
    sb.append("  sockettype: ").append(sockettype).append("\n");
    return sb.toString();
  }
  public bNodeStack copy() { try {return (bNodeStack)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

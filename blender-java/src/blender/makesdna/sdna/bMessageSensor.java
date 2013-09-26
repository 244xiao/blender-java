package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMessageSensor implements DNA, Cloneable { // #231
  public bMessageSensor[] myarray;
  public bObject fromObject; // ptr 1296
  public byte[] subject = new byte[32]; // 1
  public byte[] body = new byte[32]; // 1

  public void read(ByteBuffer buffer) {
    fromObject = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(subject);
    buffer.get(body);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(fromObject!=null?fromObject.hashCode():0);
    buffer.write(subject);
    buffer.write(body);
  }
  public Object setmyarray(Object array) {
    myarray = (bMessageSensor[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMessageSensor:\n");
    sb.append("  fromObject: ").append(fromObject).append("\n");
    sb.append("  subject: ").append(new String(subject)).append("\n");
    sb.append("  body: ").append(new String(body)).append("\n");
    return sb.toString();
  }
  public bMessageSensor copy() { try {return (bMessageSensor)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

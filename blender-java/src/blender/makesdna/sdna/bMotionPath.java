package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bMotionPath implements DNA, Cloneable { // #264
  public bMotionPath[] myarray;
  public bMotionPathVert points; // ptr 16
  public int length; // 4
  public int start_frame; // 4
  public int end_frame; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    points = DNATools.link(DNATools.ptr(buffer), bMotionPathVert.class); // get ptr
    length = buffer.getInt();
    start_frame = buffer.getInt();
    end_frame = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(points!=null?points.hashCode():0);
    buffer.writeInt(length);
    buffer.writeInt(start_frame);
    buffer.writeInt(end_frame);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (bMotionPath[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bMotionPath:\n");
    sb.append("  points: ").append(points).append("\n");
    sb.append("  length: ").append(length).append("\n");
    sb.append("  start_frame: ").append(start_frame).append("\n");
    sb.append("  end_frame: ").append(end_frame).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public bMotionPath copy() { try {return (bMotionPath)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeImageAnim implements DNA, Cloneable { // #311
  public NodeImageAnim[] myarray;
  public int frames; // 4
  public int sfra; // 4
  public int nr; // 4
  public byte cyclic; // 1
  public byte movie; // 1
  public short pad; // 2

  public void read(ByteBuffer buffer) {
    frames = buffer.getInt();
    sfra = buffer.getInt();
    nr = buffer.getInt();
    cyclic = buffer.get();
    movie = buffer.get();
    pad = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(frames);
    buffer.writeInt(sfra);
    buffer.writeInt(nr);
    buffer.writeByte(cyclic);
    buffer.writeByte(movie);
    buffer.writeShort(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeImageAnim[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeImageAnim:\n");
    sb.append("  frames: ").append(frames).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  nr: ").append(nr).append("\n");
    sb.append("  cyclic: ").append(cyclic).append("\n");
    sb.append("  movie: ").append(movie).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public NodeImageAnim copy() { try {return (NodeImageAnim)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

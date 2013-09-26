package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TextBox implements DNA, Cloneable { // #43
  public TextBox[] myarray;
  public float x; // 4
  public float y; // 4
  public float w; // 4
  public float h; // 4

  public void read(ByteBuffer buffer) {
    x = buffer.getFloat();
    y = buffer.getFloat();
    w = buffer.getFloat();
    h = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(x);
    buffer.writeFloat(y);
    buffer.writeFloat(w);
    buffer.writeFloat(h);
  }
  public Object setmyarray(Object array) {
    myarray = (TextBox[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TextBox:\n");
    sb.append("  x: ").append(x).append("\n");
    sb.append("  y: ").append(y).append("\n");
    sb.append("  w: ").append(w).append("\n");
    sb.append("  h: ").append(h).append("\n");
    return sb.toString();
  }
  public TextBox copy() { try {return (TextBox)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

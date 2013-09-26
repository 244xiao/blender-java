package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Group extends ID implements DNA, Cloneable { // #260
  public Group[] myarray;
  public ID id = (ID)this;
  public ListBase gobject = new ListBase(); // 16
  public int layer; // 4
  public float[] dupli_ofs = new float[3]; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    gobject.read(buffer);
    layer = buffer.getInt();
    for(int i=0;i<dupli_ofs.length;i++) dupli_ofs[i]=buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    gobject.write(buffer);
    buffer.writeInt(layer);
    for(int i=0;i<dupli_ofs.length;i++) buffer.writeFloat(dupli_ofs[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (Group[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Group:\n");
    sb.append(super.toString());
    sb.append("  gobject: ").append(gobject).append("\n");
    sb.append("  layer: ").append(layer).append("\n");
    sb.append("  dupli_ofs: ").append(Arrays.toString(dupli_ofs)).append("\n");
    return sb.toString();
  }
  public Group copy() { try {return (Group)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class CustomData implements DNA, Cloneable { // #339
  public CustomData[] myarray;
  public CustomDataLayer layers; // ptr 72
  public int totlayer; // 4
  public int maxlayer; // 4
  public int totsize; // 4
  public int pad; // 4
  public Object pool; // ptr 0
  public CustomDataExternal external; // ptr 240

  public void read(ByteBuffer buffer) {
    layers = DNATools.link(DNATools.ptr(buffer), CustomDataLayer.class); // get ptr
    totlayer = buffer.getInt();
    maxlayer = buffer.getInt();
    totsize = buffer.getInt();
    pad = buffer.getInt();
    pool = DNATools.ptr(buffer); // get ptr
    external = DNATools.link(DNATools.ptr(buffer), CustomDataExternal.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(layers!=null?layers.hashCode():0);
    buffer.writeInt(totlayer);
    buffer.writeInt(maxlayer);
    buffer.writeInt(totsize);
    buffer.writeInt(pad);
    buffer.writeInt(pool!=null?pool.hashCode():0);
    buffer.writeInt(external!=null?external.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (CustomData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("CustomData:\n");
    sb.append("  layers: ").append(layers).append("\n");
    sb.append("  totlayer: ").append(totlayer).append("\n");
    sb.append("  maxlayer: ").append(maxlayer).append("\n");
    sb.append("  totsize: ").append(totsize).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  pool: ").append(pool).append("\n");
    sb.append("  external: ").append(external).append("\n");
    return sb.toString();
  }
  public CustomData copy() { try {return (CustomData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

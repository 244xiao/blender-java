package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Multires implements DNA, Cloneable { // #70
  public Multires[] myarray;
  public ListBase levels = new ListBase(); // 16
  public MVert verts; // ptr 20
  public byte level_count; // 1
  public byte current; // 1
  public byte newlvl; // 1
  public byte edgelvl; // 1
  public byte pinlvl; // 1
  public byte renderlvl; // 1
  public byte use_col; // 1
  public byte flag; // 1
  public CustomData vdata = new CustomData(); // 40
  public CustomData fdata = new CustomData(); // 40
  public Object edge_flags; // ptr 2
  public Object edge_creases; // ptr 1

  public void read(ByteBuffer buffer) {
    levels.read(buffer);
    verts = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
    level_count = buffer.get();
    current = buffer.get();
    newlvl = buffer.get();
    edgelvl = buffer.get();
    pinlvl = buffer.get();
    renderlvl = buffer.get();
    use_col = buffer.get();
    flag = buffer.get();
    vdata.read(buffer);
    fdata.read(buffer);
    edge_flags = DNATools.ptr(buffer); // get ptr
    edge_creases = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    levels.write(buffer);
    buffer.writeInt(verts!=null?verts.hashCode():0);
    buffer.writeByte(level_count);
    buffer.writeByte(current);
    buffer.writeByte(newlvl);
    buffer.writeByte(edgelvl);
    buffer.writeByte(pinlvl);
    buffer.writeByte(renderlvl);
    buffer.writeByte(use_col);
    buffer.writeByte(flag);
    vdata.write(buffer);
    fdata.write(buffer);
    buffer.writeInt(edge_flags!=null?edge_flags.hashCode():0);
    buffer.writeInt(edge_creases!=null?edge_creases.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Multires[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Multires:\n");
    sb.append("  levels: ").append(levels).append("\n");
    sb.append("  verts: ").append(verts).append("\n");
    sb.append("  level_count: ").append(level_count).append("\n");
    sb.append("  current: ").append(current).append("\n");
    sb.append("  newlvl: ").append(newlvl).append("\n");
    sb.append("  edgelvl: ").append(edgelvl).append("\n");
    sb.append("  pinlvl: ").append(pinlvl).append("\n");
    sb.append("  renderlvl: ").append(renderlvl).append("\n");
    sb.append("  use_col: ").append(use_col).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  vdata: ").append(vdata).append("\n");
    sb.append("  fdata: ").append(fdata).append("\n");
    sb.append("  edge_flags: ").append(edge_flags).append("\n");
    sb.append("  edge_creases: ").append(edge_creases).append("\n");
    return sb.toString();
  }
  public Multires copy() { try {return (Multires)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

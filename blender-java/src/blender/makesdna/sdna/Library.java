package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Library extends ID implements DNA, Cloneable { // #10
  public Library[] myarray;
  public ID id = (ID)this;
  public Object idblock; // ptr 72
  public Object filedata; // ptr (FileData) 0
  public byte[] name = new byte[240]; // 1
  public byte[] filepath = new byte[240]; // 1
  public int tot; // 4
  public int pad; // 4
  public Library parent; // ptr 584

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    idblock = DNATools.ptr(buffer); // get ptr
    filedata = DNATools.ptr(buffer); // get ptr
    buffer.get(name);
    buffer.get(filepath);
    tot = buffer.getInt();
    pad = buffer.getInt();
    parent = DNATools.link(DNATools.ptr(buffer), Library.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(idblock!=null?idblock.hashCode():0);
    buffer.writeInt(filedata!=null?filedata.hashCode():0);
    buffer.write(name);
    buffer.write(filepath);
    buffer.writeInt(tot);
    buffer.writeInt(pad);
    buffer.writeInt(parent!=null?parent.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Library[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Library:\n");
    sb.append(super.toString());
    sb.append("  idblock: ").append(idblock).append("\n");
    sb.append("  filedata: ").append(filedata).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  filepath: ").append(new String(filepath)).append("\n");
    sb.append("  tot: ").append(tot).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  parent: ").append(parent).append("\n");
    return sb.toString();
  }
  public Library copy() { try {return (Library)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FileSelectParams implements DNA, Cloneable { // #161
  public FileSelectParams[] myarray;
  public byte[] title = new byte[32]; // 1
  public byte[] dir = new byte[240]; // 1
  public byte[] file = new byte[80]; // 1
  public byte[] renamefile = new byte[80]; // 1
  public byte[] renameedit = new byte[80]; // 1
  public byte[] filter_glob = new byte[64]; // 1
  public int active_file; // 4
  public int sel_first; // 4
  public int sel_last; // 4
  public short type; // 2
  public short flag; // 2
  public short sort; // 2
  public short display; // 2
  public short filter; // 2
  public short f_fp; // 2
  public byte[] fp_str = new byte[8]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(title);
    buffer.get(dir);
    buffer.get(file);
    buffer.get(renamefile);
    buffer.get(renameedit);
    buffer.get(filter_glob);
    active_file = buffer.getInt();
    sel_first = buffer.getInt();
    sel_last = buffer.getInt();
    type = buffer.getShort();
    flag = buffer.getShort();
    sort = buffer.getShort();
    display = buffer.getShort();
    filter = buffer.getShort();
    f_fp = buffer.getShort();
    buffer.get(fp_str);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(title);
    buffer.write(dir);
    buffer.write(file);
    buffer.write(renamefile);
    buffer.write(renameedit);
    buffer.write(filter_glob);
    buffer.writeInt(active_file);
    buffer.writeInt(sel_first);
    buffer.writeInt(sel_last);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(sort);
    buffer.writeShort(display);
    buffer.writeShort(filter);
    buffer.writeShort(f_fp);
    buffer.write(fp_str);
  }
  public Object setmyarray(Object array) {
    myarray = (FileSelectParams[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FileSelectParams:\n");
    sb.append("  title: ").append(new String(title)).append("\n");
    sb.append("  dir: ").append(new String(dir)).append("\n");
    sb.append("  file: ").append(new String(file)).append("\n");
    sb.append("  renamefile: ").append(new String(renamefile)).append("\n");
    sb.append("  renameedit: ").append(new String(renameedit)).append("\n");
    sb.append("  filter_glob: ").append(new String(filter_glob)).append("\n");
    sb.append("  active_file: ").append(active_file).append("\n");
    sb.append("  sel_first: ").append(sel_first).append("\n");
    sb.append("  sel_last: ").append(sel_last).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sort: ").append(sort).append("\n");
    sb.append("  display: ").append(display).append("\n");
    sb.append("  filter: ").append(filter).append("\n");
    sb.append("  f_fp: ").append(f_fp).append("\n");
    sb.append("  fp_str: ").append(new String(fp_str)).append("\n");
    return sb.toString();
  }
  public FileSelectParams copy() { try {return (FileSelectParams)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

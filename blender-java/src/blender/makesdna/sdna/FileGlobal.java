package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FileGlobal implements DNA, Cloneable { // #196
  public FileGlobal[] myarray;
  public byte[] subvstr = new byte[4]; // 1
  public short subversion; // 2
  public short pads; // 2
  public short minversion; // 2
  public short minsubversion; // 2
  public short displaymode; // 2
  public short winpos; // 2
  public bScreen curscreen; // ptr 216
  public Scene curscene; // ptr 1552
  public int fileflags; // 4
  public int globalf; // 4
  public int revision; // 4
  public int pad; // 4
  public byte[] filename = new byte[240]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(subvstr);
    subversion = buffer.getShort();
    pads = buffer.getShort();
    minversion = buffer.getShort();
    minsubversion = buffer.getShort();
    displaymode = buffer.getShort();
    winpos = buffer.getShort();
    curscreen = DNATools.link(DNATools.ptr(buffer), bScreen.class); // get ptr
    curscene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    fileflags = buffer.getInt();
    globalf = buffer.getInt();
    revision = buffer.getInt();
    pad = buffer.getInt();
    buffer.get(filename);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(subvstr);
    buffer.writeShort(subversion);
    buffer.writeShort(pads);
    buffer.writeShort(minversion);
    buffer.writeShort(minsubversion);
    buffer.writeShort(displaymode);
    buffer.writeShort(winpos);
    buffer.writeInt(curscreen!=null?curscreen.hashCode():0);
    buffer.writeInt(curscene!=null?curscene.hashCode():0);
    buffer.writeInt(fileflags);
    buffer.writeInt(globalf);
    buffer.writeInt(revision);
    buffer.writeInt(pad);
    buffer.write(filename);
  }
  public Object setmyarray(Object array) {
    myarray = (FileGlobal[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FileGlobal:\n");
    sb.append("  subvstr: ").append(new String(subvstr)).append("\n");
    sb.append("  subversion: ").append(subversion).append("\n");
    sb.append("  pads: ").append(pads).append("\n");
    sb.append("  minversion: ").append(minversion).append("\n");
    sb.append("  minsubversion: ").append(minsubversion).append("\n");
    sb.append("  displaymode: ").append(displaymode).append("\n");
    sb.append("  winpos: ").append(winpos).append("\n");
    sb.append("  curscreen: ").append(curscreen).append("\n");
    sb.append("  curscene: ").append(curscene).append("\n");
    sb.append("  fileflags: ").append(fileflags).append("\n");
    sb.append("  globalf: ").append(globalf).append("\n");
    sb.append("  revision: ").append(revision).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  filename: ").append(new String(filename)).append("\n");
    return sb.toString();
  }
  public FileGlobal copy() { try {return (FileGlobal)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

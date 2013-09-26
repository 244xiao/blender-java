package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNodeLink extends Link<bNodeLink> implements DNA, Cloneable { // #309
  public bNodeLink[] myarray;
  public bNode fromnode; // ptr 264
  public bNode tonode; // ptr 264
  public bNodeSocket fromsock; // ptr 152
  public bNodeSocket tosock; // ptr 152
  public int flag; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bNodeLink.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bNodeLink.class); // get ptr
    fromnode = DNATools.link(DNATools.ptr(buffer), bNode.class); // get ptr
    tonode = DNATools.link(DNATools.ptr(buffer), bNode.class); // get ptr
    fromsock = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    tosock = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    flag = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(fromnode!=null?fromnode.hashCode():0);
    buffer.writeInt(tonode!=null?tonode.hashCode():0);
    buffer.writeInt(fromsock!=null?fromsock.hashCode():0);
    buffer.writeInt(tosock!=null?tosock.hashCode():0);
    buffer.writeInt(flag);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bNodeLink[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNodeLink:\n");
    sb.append("  fromnode: ").append(fromnode).append("\n");
    sb.append("  tonode: ").append(tonode).append("\n");
    sb.append("  fromsock: ").append(fromsock).append("\n");
    sb.append("  tosock: ").append(tosock).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bNodeLink copy() { try {return (bNodeLink)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

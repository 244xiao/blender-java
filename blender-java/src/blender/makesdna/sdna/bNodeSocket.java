package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNodeSocket extends Link<bNodeSocket> implements DNA, Cloneable { // #306
  public bNodeSocket[] myarray;
  public bNodeSocket new_sock; // ptr 152
  public byte[] name = new byte[32]; // 1
  public bNodeStack ns = new bNodeStack(); // 40
  public short type; // 2
  public short flag; // 2
  public short limit; // 2
  public short stack_type; // 2
  public bNodeStack stack_ptr; // ptr 40
  public short stack_index; // 2
  public short pad1; // 2
  public float locx; // 4
  public float locy; // 4
  public int own_index; // 4
  public bNodeSocket groupsock; // ptr 152
  public int to_index; // 4
  public int pad2; // 4
  public bNodeLink link; // ptr 56

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    new_sock = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    buffer.get(name);
    ns.read(buffer);
    type = buffer.getShort();
    flag = buffer.getShort();
    limit = buffer.getShort();
    stack_type = buffer.getShort();
    stack_ptr = DNATools.link(DNATools.ptr(buffer), bNodeStack.class); // get ptr
    stack_index = buffer.getShort();
    pad1 = buffer.getShort();
    locx = buffer.getFloat();
    locy = buffer.getFloat();
    own_index = buffer.getInt();
    groupsock = DNATools.link(DNATools.ptr(buffer), bNodeSocket.class); // get ptr
    to_index = buffer.getInt();
    pad2 = buffer.getInt();
    link = DNATools.link(DNATools.ptr(buffer), bNodeLink.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(new_sock!=null?new_sock.hashCode():0);
    buffer.write(name);
    ns.write(buffer);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(limit);
    buffer.writeShort(stack_type);
    buffer.writeInt(stack_ptr!=null?stack_ptr.hashCode():0);
    buffer.writeShort(stack_index);
    buffer.writeShort(pad1);
    buffer.writeFloat(locx);
    buffer.writeFloat(locy);
    buffer.writeInt(own_index);
    buffer.writeInt(groupsock!=null?groupsock.hashCode():0);
    buffer.writeInt(to_index);
    buffer.writeInt(pad2);
    buffer.writeInt(link!=null?link.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bNodeSocket[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNodeSocket:\n");
    sb.append("  new_sock: ").append(new_sock).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  ns: ").append(ns).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  limit: ").append(limit).append("\n");
    sb.append("  stack_type: ").append(stack_type).append("\n");
    sb.append("  stack_ptr: ").append(stack_ptr).append("\n");
    sb.append("  stack_index: ").append(stack_index).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  locx: ").append(locx).append("\n");
    sb.append("  locy: ").append(locy).append("\n");
    sb.append("  own_index: ").append(own_index).append("\n");
    sb.append("  groupsock: ").append(groupsock).append("\n");
    sb.append("  to_index: ").append(to_index).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  link: ").append(link).append("\n");
    return sb.toString();
  }
  public bNodeSocket copy() { try {return (bNodeSocket)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bNode extends Link<bNode> implements DNA, Cloneable { // #308
  public bNode[] myarray;
  public bNode new_node; // ptr 264
  public byte[] name = new byte[32]; // 1
  public short type; // 2
  public short flag; // 2
  public short done; // 2
  public short level; // 2
  public short lasty; // 2
  public short menunr; // 2
  public short stack_index; // 2
  public short nr; // 2
  public ListBase inputs = new ListBase(); // 16
  public ListBase outputs = new ListBase(); // 16
  public Object id; // ptr 72
  public Object storage; // ptr 0
  public float locx; // 4
  public float locy; // 4
  public float width; // 4
  public float miniwidth; // 4
  public byte[] label = new byte[32]; // 1
  public short custom1; // 2
  public short custom2; // 2
  public float custom3; // 4
  public float custom4; // 4
  public short need_exec; // 2
  public short exec; // 2
  public Object threaddata; // ptr 0
  public rctf totr = new rctf(); // 16
  public rctf butr = new rctf(); // 16
  public rctf prvr = new rctf(); // 16
  public bNodePreview preview; // ptr 16
  public Object block; // ptr (uiBlock) 0
  public Object typeinfo; // ptr (bNodeType) 0

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bNode.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bNode.class); // get ptr
    new_node = DNATools.link(DNATools.ptr(buffer), bNode.class); // get ptr
    buffer.get(name);
    type = buffer.getShort();
    flag = buffer.getShort();
    done = buffer.getShort();
    level = buffer.getShort();
    lasty = buffer.getShort();
    menunr = buffer.getShort();
    stack_index = buffer.getShort();
    nr = buffer.getShort();
    inputs.read(buffer);
    outputs.read(buffer);
    id = DNATools.ptr(buffer); // get ptr
    storage = DNATools.ptr(buffer); // get ptr
    locx = buffer.getFloat();
    locy = buffer.getFloat();
    width = buffer.getFloat();
    miniwidth = buffer.getFloat();
    buffer.get(label);
    custom1 = buffer.getShort();
    custom2 = buffer.getShort();
    custom3 = buffer.getFloat();
    custom4 = buffer.getFloat();
    need_exec = buffer.getShort();
    exec = buffer.getShort();
    threaddata = DNATools.ptr(buffer); // get ptr
    totr.read(buffer);
    butr.read(buffer);
    prvr.read(buffer);
    preview = DNATools.link(DNATools.ptr(buffer), bNodePreview.class); // get ptr
    block = DNATools.ptr(buffer); // get ptr
    typeinfo = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(new_node!=null?new_node.hashCode():0);
    buffer.write(name);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeShort(done);
    buffer.writeShort(level);
    buffer.writeShort(lasty);
    buffer.writeShort(menunr);
    buffer.writeShort(stack_index);
    buffer.writeShort(nr);
    inputs.write(buffer);
    outputs.write(buffer);
    buffer.writeInt(id!=null?id.hashCode():0);
    buffer.writeInt(storage!=null?storage.hashCode():0);
    buffer.writeFloat(locx);
    buffer.writeFloat(locy);
    buffer.writeFloat(width);
    buffer.writeFloat(miniwidth);
    buffer.write(label);
    buffer.writeShort(custom1);
    buffer.writeShort(custom2);
    buffer.writeFloat(custom3);
    buffer.writeFloat(custom4);
    buffer.writeShort(need_exec);
    buffer.writeShort(exec);
    buffer.writeInt(threaddata!=null?threaddata.hashCode():0);
    totr.write(buffer);
    butr.write(buffer);
    prvr.write(buffer);
    buffer.writeInt(preview!=null?preview.hashCode():0);
    buffer.writeInt(block!=null?block.hashCode():0);
    buffer.writeInt(typeinfo!=null?typeinfo.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bNode[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bNode:\n");
    sb.append("  new_node: ").append(new_node).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  done: ").append(done).append("\n");
    sb.append("  level: ").append(level).append("\n");
    sb.append("  lasty: ").append(lasty).append("\n");
    sb.append("  menunr: ").append(menunr).append("\n");
    sb.append("  stack_index: ").append(stack_index).append("\n");
    sb.append("  nr: ").append(nr).append("\n");
    sb.append("  inputs: ").append(inputs).append("\n");
    sb.append("  outputs: ").append(outputs).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  storage: ").append(storage).append("\n");
    sb.append("  locx: ").append(locx).append("\n");
    sb.append("  locy: ").append(locy).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  miniwidth: ").append(miniwidth).append("\n");
    sb.append("  label: ").append(new String(label)).append("\n");
    sb.append("  custom1: ").append(custom1).append("\n");
    sb.append("  custom2: ").append(custom2).append("\n");
    sb.append("  custom3: ").append(custom3).append("\n");
    sb.append("  custom4: ").append(custom4).append("\n");
    sb.append("  need_exec: ").append(need_exec).append("\n");
    sb.append("  exec: ").append(exec).append("\n");
    sb.append("  threaddata: ").append(threaddata).append("\n");
    sb.append("  totr: ").append(totr).append("\n");
    sb.append("  butr: ").append(butr).append("\n");
    sb.append("  prvr: ").append(prvr).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  block: ").append(block).append("\n");
    sb.append("  typeinfo: ").append(typeinfo).append("\n");
    return sb.toString();
  }
  public bNode copy() { try {return (bNode)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

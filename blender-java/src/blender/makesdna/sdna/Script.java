package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Script extends ID implements DNA, Cloneable { // #167
  public Script[] myarray;
  public ID id = (ID)this;
  public Object py_draw; // ptr 0
  public Object py_event; // ptr 0
  public Object py_button; // ptr 0
  public Object py_browsercallback; // ptr 0
  public Object py_globaldict; // ptr 0
  public int flags; // 4
  public int lastspace; // 4
  public byte[] scriptname = new byte[256]; // 1
  public byte[] scriptarg = new byte[256]; // 1

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    py_draw = DNATools.ptr(buffer); // get ptr
    py_event = DNATools.ptr(buffer); // get ptr
    py_button = DNATools.ptr(buffer); // get ptr
    py_browsercallback = DNATools.ptr(buffer); // get ptr
    py_globaldict = DNATools.ptr(buffer); // get ptr
    flags = buffer.getInt();
    lastspace = buffer.getInt();
    buffer.get(scriptname);
    buffer.get(scriptarg);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(py_draw!=null?py_draw.hashCode():0);
    buffer.writeInt(py_event!=null?py_event.hashCode():0);
    buffer.writeInt(py_button!=null?py_button.hashCode():0);
    buffer.writeInt(py_browsercallback!=null?py_browsercallback.hashCode():0);
    buffer.writeInt(py_globaldict!=null?py_globaldict.hashCode():0);
    buffer.writeInt(flags);
    buffer.writeInt(lastspace);
    buffer.write(scriptname);
    buffer.write(scriptarg);
  }
  public Object setmyarray(Object array) {
    myarray = (Script[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Script:\n");
    sb.append(super.toString());
    sb.append("  py_draw: ").append(py_draw).append("\n");
    sb.append("  py_event: ").append(py_event).append("\n");
    sb.append("  py_button: ").append(py_button).append("\n");
    sb.append("  py_browsercallback: ").append(py_browsercallback).append("\n");
    sb.append("  py_globaldict: ").append(py_globaldict).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  lastspace: ").append(lastspace).append("\n");
    sb.append("  scriptname: ").append(new String(scriptname)).append("\n");
    sb.append("  scriptarg: ").append(new String(scriptarg)).append("\n");
    return sb.toString();
  }
  public Script copy() { try {return (Script)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

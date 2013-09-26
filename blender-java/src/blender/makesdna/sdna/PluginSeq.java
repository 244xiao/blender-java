package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PluginSeq implements DNA, Cloneable { // #203
  public PluginSeq[] myarray;
  public byte[] name = new byte[256]; // 1
  public Object handle; // ptr 0
  public Object pname; // ptr 1
  public int vars; // 4
  public int version; // 4
  public Object varstr; // ptr 0
  public Object cfra; // ptr 4
  public float[] data = new float[32]; // 4
  public Object instance_private_data; // ptr 0
  public Object current_private_data; // ptr 0
  public Object doit; // func ptr 0
  public Object callback; // func ptr 0

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    handle = DNATools.ptr(buffer); // get ptr
    pname = DNATools.ptr(buffer); // get ptr
    vars = buffer.getInt();
    version = buffer.getInt();
    varstr = DNATools.ptr(buffer); // get ptr
    cfra = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<data.length;i++) data[i]=buffer.getFloat();
    instance_private_data = DNATools.ptr(buffer); // get ptr
    current_private_data = DNATools.ptr(buffer); // get ptr
    doit = DNATools.ptr(buffer); // get ptr
    callback = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeInt(handle!=null?handle.hashCode():0);
    buffer.writeInt(pname!=null?pname.hashCode():0);
    buffer.writeInt(vars);
    buffer.writeInt(version);
    buffer.writeInt(varstr!=null?varstr.hashCode():0);
    buffer.writeInt(cfra!=null?cfra.hashCode():0);
    for(int i=0;i<data.length;i++) buffer.writeFloat(data[i]);
    buffer.writeInt(instance_private_data!=null?instance_private_data.hashCode():0);
    buffer.writeInt(current_private_data!=null?current_private_data.hashCode():0);
    buffer.writeInt(doit!=null?doit.hashCode():0);
    buffer.writeInt(callback!=null?callback.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (PluginSeq[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PluginSeq:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  handle: ").append(handle).append("\n");
    sb.append("  pname: ").append(pname).append("\n");
    sb.append("  vars: ").append(vars).append("\n");
    sb.append("  version: ").append(version).append("\n");
    sb.append("  varstr: ").append(varstr).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  data: ").append(Arrays.toString(data)).append("\n");
    sb.append("  instance_private_data: ").append(instance_private_data).append("\n");
    sb.append("  current_private_data: ").append(current_private_data).append("\n");
    sb.append("  doit: ").append(doit).append("\n");
    sb.append("  callback: ").append(callback).append("\n");
    return sb.toString();
  }
  public PluginSeq copy() { try {return (PluginSeq)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

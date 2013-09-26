package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PluginTex implements DNA, Cloneable { // #25
  public PluginTex[] myarray;
  public byte[] name = new byte[160]; // 1
  public Object handle; // ptr 0
  public Object pname; // ptr 1
  public Object stnames; // ptr 1
  public int stypes; // 4
  public int vars; // 4
  public Object varstr; // ptr 0
  public Object result; // ptr 4
  public Object cfra; // ptr 4
  public float[] data = new float[32]; // 4
  public Object doit; // func ptr 4
  public Object instance_init; // func ptr 0
  public Object callback; // func ptr 0
  public int version; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    buffer.get(name);
    handle = DNATools.ptr(buffer); // get ptr
    pname = DNATools.ptr(buffer); // get ptr
    stnames = DNATools.ptr(buffer); // get ptr
    stypes = buffer.getInt();
    vars = buffer.getInt();
    varstr = DNATools.ptr(buffer); // get ptr
    result = DNATools.ptr(buffer); // get ptr
    cfra = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<data.length;i++) data[i]=buffer.getFloat();
    doit = DNATools.ptr(buffer); // get ptr
    instance_init = DNATools.ptr(buffer); // get ptr
    callback = DNATools.ptr(buffer); // get ptr
    version = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(name);
    buffer.writeInt(handle!=null?handle.hashCode():0);
    buffer.writeInt(pname!=null?pname.hashCode():0);
    buffer.writeInt(stnames!=null?stnames.hashCode():0);
    buffer.writeInt(stypes);
    buffer.writeInt(vars);
    buffer.writeInt(varstr!=null?varstr.hashCode():0);
    buffer.writeInt(result!=null?result.hashCode():0);
    buffer.writeInt(cfra!=null?cfra.hashCode():0);
    for(int i=0;i<data.length;i++) buffer.writeFloat(data[i]);
    buffer.writeInt(doit!=null?doit.hashCode():0);
    buffer.writeInt(instance_init!=null?instance_init.hashCode():0);
    buffer.writeInt(callback!=null?callback.hashCode():0);
    buffer.writeInt(version);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (PluginTex[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PluginTex:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  handle: ").append(handle).append("\n");
    sb.append("  pname: ").append(pname).append("\n");
    sb.append("  stnames: ").append(stnames).append("\n");
    sb.append("  stypes: ").append(stypes).append("\n");
    sb.append("  vars: ").append(vars).append("\n");
    sb.append("  varstr: ").append(varstr).append("\n");
    sb.append("  result: ").append(result).append("\n");
    sb.append("  cfra: ").append(cfra).append("\n");
    sb.append("  data: ").append(Arrays.toString(data)).append("\n");
    sb.append("  doit: ").append(doit).append("\n");
    sb.append("  instance_init: ").append(instance_init).append("\n");
    sb.append("  callback: ").append(callback).append("\n");
    sb.append("  version: ").append(version).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public PluginTex copy() { try {return (PluginTex)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

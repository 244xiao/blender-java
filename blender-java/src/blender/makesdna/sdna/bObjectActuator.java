package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bObjectActuator implements DNA, Cloneable { // #244
  public bObjectActuator[] myarray;
  public short flag; // 2
  public short type; // 2
  public short otype; // 2
  public short damping; // 2
  public float[] forceloc = new float[3]; // 4
  public float[] forcerot = new float[3]; // 4
  public float[] pad = new float[3]; // 4
  public float[] pad1 = new float[3]; // 4
  public float[] dloc = new float[3]; // 4
  public float[] drot = new float[3]; // 4
  public float[] linearvelocity = new float[3]; // 4
  public float[] angularvelocity = new float[3]; // 4
  public bObject reference; // ptr 1296

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    type = buffer.getShort();
    otype = buffer.getShort();
    damping = buffer.getShort();
    for(int i=0;i<forceloc.length;i++) forceloc[i]=buffer.getFloat();
    for(int i=0;i<forcerot.length;i++) forcerot[i]=buffer.getFloat();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getFloat();
    for(int i=0;i<pad1.length;i++) pad1[i]=buffer.getFloat();
    for(int i=0;i<dloc.length;i++) dloc[i]=buffer.getFloat();
    for(int i=0;i<drot.length;i++) drot[i]=buffer.getFloat();
    for(int i=0;i<linearvelocity.length;i++) linearvelocity[i]=buffer.getFloat();
    for(int i=0;i<angularvelocity.length;i++) angularvelocity[i]=buffer.getFloat();
    reference = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(type);
    buffer.writeShort(otype);
    buffer.writeShort(damping);
    for(int i=0;i<forceloc.length;i++) buffer.writeFloat(forceloc[i]);
    for(int i=0;i<forcerot.length;i++) buffer.writeFloat(forcerot[i]);
    for(int i=0;i<pad.length;i++) buffer.writeFloat(pad[i]);
    for(int i=0;i<pad1.length;i++) buffer.writeFloat(pad1[i]);
    for(int i=0;i<dloc.length;i++) buffer.writeFloat(dloc[i]);
    for(int i=0;i<drot.length;i++) buffer.writeFloat(drot[i]);
    for(int i=0;i<linearvelocity.length;i++) buffer.writeFloat(linearvelocity[i]);
    for(int i=0;i<angularvelocity.length;i++) buffer.writeFloat(angularvelocity[i]);
    buffer.writeInt(reference!=null?reference.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bObjectActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bObjectActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  otype: ").append(otype).append("\n");
    sb.append("  damping: ").append(damping).append("\n");
    sb.append("  forceloc: ").append(Arrays.toString(forceloc)).append("\n");
    sb.append("  forcerot: ").append(Arrays.toString(forcerot)).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  pad1: ").append(Arrays.toString(pad1)).append("\n");
    sb.append("  dloc: ").append(Arrays.toString(dloc)).append("\n");
    sb.append("  drot: ").append(Arrays.toString(drot)).append("\n");
    sb.append("  linearvelocity: ").append(Arrays.toString(linearvelocity)).append("\n");
    sb.append("  angularvelocity: ").append(Arrays.toString(angularvelocity)).append("\n");
    sb.append("  reference: ").append(reference).append("\n");
    return sb.toString();
  }
  public bObjectActuator copy() { try {return (bObjectActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

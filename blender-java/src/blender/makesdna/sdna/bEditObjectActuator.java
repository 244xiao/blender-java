package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bEditObjectActuator implements DNA, Cloneable { // #241
  public bEditObjectActuator[] myarray;
  public int time; // 4
  public short type; // 2
  public short flag; // 2
  public bObject ob; // ptr 1296
  public Mesh me; // ptr 408
  public byte[] name = new byte[32]; // 1
  public float[] linVelocity = new float[3]; // 4
  public float[] angVelocity = new float[3]; // 4
  public float mass; // 4
  public short localflag; // 2
  public short dyn_operation; // 2

  public void read(ByteBuffer buffer) {
    time = buffer.getInt();
    type = buffer.getShort();
    flag = buffer.getShort();
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    me = DNATools.link(DNATools.ptr(buffer), Mesh.class); // get ptr
    buffer.get(name);
    for(int i=0;i<linVelocity.length;i++) linVelocity[i]=buffer.getFloat();
    for(int i=0;i<angVelocity.length;i++) angVelocity[i]=buffer.getFloat();
    mass = buffer.getFloat();
    localflag = buffer.getShort();
    dyn_operation = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(time);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeInt(me!=null?me.hashCode():0);
    buffer.write(name);
    for(int i=0;i<linVelocity.length;i++) buffer.writeFloat(linVelocity[i]);
    for(int i=0;i<angVelocity.length;i++) buffer.writeFloat(angVelocity[i]);
    buffer.writeFloat(mass);
    buffer.writeShort(localflag);
    buffer.writeShort(dyn_operation);
  }
  public Object setmyarray(Object array) {
    myarray = (bEditObjectActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bEditObjectActuator:\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  me: ").append(me).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  linVelocity: ").append(Arrays.toString(linVelocity)).append("\n");
    sb.append("  angVelocity: ").append(Arrays.toString(angVelocity)).append("\n");
    sb.append("  mass: ").append(mass).append("\n");
    sb.append("  localflag: ").append(localflag).append("\n");
    sb.append("  dyn_operation: ").append(dyn_operation).append("\n");
    return sb.toString();
  }
  public bEditObjectActuator copy() { try {return (bEditObjectActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

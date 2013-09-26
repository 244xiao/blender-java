package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bActionModifier extends Link<bActionModifier> implements DNA, Cloneable { // #303
  public bActionModifier[] myarray;
  public short type; // 2
  public short flag; // 2
  public byte[] channel = new byte[32]; // 1
  public float noisesize; // 4
  public float turbul; // 4
  public short channels; // 2
  public short no_rot_axis; // 2
  public bObject ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bActionModifier.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bActionModifier.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    buffer.get(channel);
    noisesize = buffer.getFloat();
    turbul = buffer.getFloat();
    channels = buffer.getShort();
    no_rot_axis = buffer.getShort();
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.write(channel);
    buffer.writeFloat(noisesize);
    buffer.writeFloat(turbul);
    buffer.writeShort(channels);
    buffer.writeShort(no_rot_axis);
    buffer.writeInt(ob!=null?ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bActionModifier[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bActionModifier:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  channel: ").append(new String(channel)).append("\n");
    sb.append("  noisesize: ").append(noisesize).append("\n");
    sb.append("  turbul: ").append(turbul).append("\n");
    sb.append("  channels: ").append(channels).append("\n");
    sb.append("  no_rot_axis: ").append(no_rot_axis).append("\n");
    sb.append("  ob: ").append(ob).append("\n");
    return sb.toString();
  }
  public bActionModifier copy() { try {return (bActionModifier)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

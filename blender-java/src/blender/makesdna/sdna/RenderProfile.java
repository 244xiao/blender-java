package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class RenderProfile extends Link<RenderProfile> implements DNA, Cloneable { // #135
  public RenderProfile[] myarray;
  public byte[] name = new byte[32]; // 1
  public short particle_perc; // 2
  public short subsurf_max; // 2
  public short shadbufsample_max; // 2
  public short pad1; // 2
  public float ao_error; // 4
  public float pad2; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), RenderProfile.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), RenderProfile.class); // get ptr
    buffer.get(name);
    particle_perc = buffer.getShort();
    subsurf_max = buffer.getShort();
    shadbufsample_max = buffer.getShort();
    pad1 = buffer.getShort();
    ao_error = buffer.getFloat();
    pad2 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    buffer.writeShort(particle_perc);
    buffer.writeShort(subsurf_max);
    buffer.writeShort(shadbufsample_max);
    buffer.writeShort(pad1);
    buffer.writeFloat(ao_error);
    buffer.writeFloat(pad2);
  }
  public Object setmyarray(Object array) {
    myarray = (RenderProfile[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("RenderProfile:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  particle_perc: ").append(particle_perc).append("\n");
    sb.append("  subsurf_max: ").append(subsurf_max).append("\n");
    sb.append("  shadbufsample_max: ").append(shadbufsample_max).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  ao_error: ").append(ao_error).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    return sb.toString();
  }
  public RenderProfile copy() { try {return (RenderProfile)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

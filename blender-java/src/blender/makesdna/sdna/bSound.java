package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSound extends ID implements DNA, Cloneable { // #258
  public bSound[] myarray;
  public ID id = (ID)this;
  public byte[] name = new byte[240]; // 1
  public PackedFile packedfile; // ptr 16
  public Object handle; // ptr 0
  public PackedFile newpackedfile; // ptr 16
  public Ipo ipo; // ptr 112
  public float volume; // 4
  public float attenuation; // 4
  public float pitch; // 4
  public float min_gain; // 4
  public float max_gain; // 4
  public float distance; // 4
  public int flags; // 4
  public int pad; // 4
  public Object cache; // ptr 0
  public Object playback_handle; // ptr 0

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    buffer.get(name);
    packedfile = DNATools.link(DNATools.ptr(buffer), PackedFile.class); // get ptr
    handle = DNATools.ptr(buffer); // get ptr
    newpackedfile = DNATools.link(DNATools.ptr(buffer), PackedFile.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    volume = buffer.getFloat();
    attenuation = buffer.getFloat();
    pitch = buffer.getFloat();
    min_gain = buffer.getFloat();
    max_gain = buffer.getFloat();
    distance = buffer.getFloat();
    flags = buffer.getInt();
    pad = buffer.getInt();
    cache = DNATools.ptr(buffer); // get ptr
    playback_handle = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.write(name);
    buffer.writeInt(packedfile!=null?packedfile.hashCode():0);
    buffer.writeInt(handle!=null?handle.hashCode():0);
    buffer.writeInt(newpackedfile!=null?newpackedfile.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeFloat(volume);
    buffer.writeFloat(attenuation);
    buffer.writeFloat(pitch);
    buffer.writeFloat(min_gain);
    buffer.writeFloat(max_gain);
    buffer.writeFloat(distance);
    buffer.writeInt(flags);
    buffer.writeInt(pad);
    buffer.writeInt(cache!=null?cache.hashCode():0);
    buffer.writeInt(playback_handle!=null?playback_handle.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bSound[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSound:\n");
    sb.append(super.toString());
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  packedfile: ").append(packedfile).append("\n");
    sb.append("  handle: ").append(handle).append("\n");
    sb.append("  newpackedfile: ").append(newpackedfile).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    sb.append("  attenuation: ").append(attenuation).append("\n");
    sb.append("  pitch: ").append(pitch).append("\n");
    sb.append("  min_gain: ").append(min_gain).append("\n");
    sb.append("  max_gain: ").append(max_gain).append("\n");
    sb.append("  distance: ").append(distance).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  cache: ").append(cache).append("\n");
    sb.append("  playback_handle: ").append(playback_handle).append("\n");
    return sb.toString();
  }
  public bSound copy() { try {return (bSound)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

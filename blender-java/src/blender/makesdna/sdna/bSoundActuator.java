package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSoundActuator implements DNA, Cloneable { // #240
  public bSoundActuator[] myarray;
  public short flag; // 2
  public short sndnr; // 2
  public int pad1; // 4
  public int pad2; // 4
  public short[] pad3 = new short[2]; // 2
  public float volume; // 4
  public float pitch; // 4
  public bSound sound; // ptr 392
  public Sound3D sound3D = new Sound3D(); // 32
  public short type; // 2
  public short pad4; // 2
  public short pad5; // 2
  public short[] pad6 = new short[1]; // 2

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    sndnr = buffer.getShort();
    pad1 = buffer.getInt();
    pad2 = buffer.getInt();
    for(int i=0;i<pad3.length;i++) pad3[i]=buffer.getShort();
    volume = buffer.getFloat();
    pitch = buffer.getFloat();
    sound = DNATools.link(DNATools.ptr(buffer), bSound.class); // get ptr
    sound3D.read(buffer);
    type = buffer.getShort();
    pad4 = buffer.getShort();
    pad5 = buffer.getShort();
    for(int i=0;i<pad6.length;i++) pad6[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(sndnr);
    buffer.writeInt(pad1);
    buffer.writeInt(pad2);
    for(int i=0;i<pad3.length;i++) buffer.writeShort(pad3[i]);
    buffer.writeFloat(volume);
    buffer.writeFloat(pitch);
    buffer.writeInt(sound!=null?sound.hashCode():0);
    sound3D.write(buffer);
    buffer.writeShort(type);
    buffer.writeShort(pad4);
    buffer.writeShort(pad5);
    for(int i=0;i<pad6.length;i++) buffer.writeShort(pad6[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (bSoundActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSoundActuator:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  sndnr: ").append(sndnr).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad2: ").append(pad2).append("\n");
    sb.append("  pad3: ").append(Arrays.toString(pad3)).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    sb.append("  pitch: ").append(pitch).append("\n");
    sb.append("  sound: ").append(sound).append("\n");
    sb.append("  sound3D: ").append(sound3D).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad4: ").append(pad4).append("\n");
    sb.append("  pad5: ").append(pad5).append("\n");
    sb.append("  pad6: ").append(Arrays.toString(pad6)).append("\n");
    return sb.toString();
  }
  public bSoundActuator copy() { try {return (bSoundActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

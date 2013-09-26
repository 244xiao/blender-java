package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class WaveModifierData extends ModifierData implements DNA, Cloneable { // #89
  public WaveModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject objectcenter; // ptr 1296
  public byte[] defgrp_name = new byte[32]; // 1
  public Tex texture; // ptr 368
  public bObject map_object; // ptr 1296
  public short flag; // 2
  public short pad; // 2
  public float startx; // 4
  public float starty; // 4
  public float height; // 4
  public float width; // 4
  public float narrow; // 4
  public float speed; // 4
  public float damp; // 4
  public float falloff; // 4
  public int texmapping; // 4
  public int uvlayer_tmp; // 4
  public byte[] uvlayer_name = new byte[32]; // 1
  public float timeoffs; // 4
  public float lifetime; // 4
  public float pad1; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    objectcenter = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(defgrp_name);
    texture = DNATools.link(DNATools.ptr(buffer), Tex.class); // get ptr
    map_object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    flag = buffer.getShort();
    pad = buffer.getShort();
    startx = buffer.getFloat();
    starty = buffer.getFloat();
    height = buffer.getFloat();
    width = buffer.getFloat();
    narrow = buffer.getFloat();
    speed = buffer.getFloat();
    damp = buffer.getFloat();
    falloff = buffer.getFloat();
    texmapping = buffer.getInt();
    uvlayer_tmp = buffer.getInt();
    buffer.get(uvlayer_name);
    timeoffs = buffer.getFloat();
    lifetime = buffer.getFloat();
    pad1 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(objectcenter!=null?objectcenter.hashCode():0);
    buffer.write(defgrp_name);
    buffer.writeInt(texture!=null?texture.hashCode():0);
    buffer.writeInt(map_object!=null?map_object.hashCode():0);
    buffer.writeShort(flag);
    buffer.writeShort(pad);
    buffer.writeFloat(startx);
    buffer.writeFloat(starty);
    buffer.writeFloat(height);
    buffer.writeFloat(width);
    buffer.writeFloat(narrow);
    buffer.writeFloat(speed);
    buffer.writeFloat(damp);
    buffer.writeFloat(falloff);
    buffer.writeInt(texmapping);
    buffer.writeInt(uvlayer_tmp);
    buffer.write(uvlayer_name);
    buffer.writeFloat(timeoffs);
    buffer.writeFloat(lifetime);
    buffer.writeFloat(pad1);
  }
  public Object setmyarray(Object array) {
    myarray = (WaveModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("WaveModifierData:\n");
    sb.append(super.toString());
    sb.append("  objectcenter: ").append(objectcenter).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  texture: ").append(texture).append("\n");
    sb.append("  map_object: ").append(map_object).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  startx: ").append(startx).append("\n");
    sb.append("  starty: ").append(starty).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  narrow: ").append(narrow).append("\n");
    sb.append("  speed: ").append(speed).append("\n");
    sb.append("  damp: ").append(damp).append("\n");
    sb.append("  falloff: ").append(falloff).append("\n");
    sb.append("  texmapping: ").append(texmapping).append("\n");
    sb.append("  uvlayer_tmp: ").append(uvlayer_tmp).append("\n");
    sb.append("  uvlayer_name: ").append(new String(uvlayer_name)).append("\n");
    sb.append("  timeoffs: ").append(timeoffs).append("\n");
    sb.append("  lifetime: ").append(lifetime).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    return sb.toString();
  }
  public WaveModifierData copy() { try {return (WaveModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class DisplaceModifierData extends ModifierData implements DNA, Cloneable { // #84
  public DisplaceModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public Tex texture; // ptr 368
  public float strength; // 4
  public int direction; // 4
  public byte[] defgrp_name = new byte[32]; // 1
  public float midlevel; // 4
  public int texmapping; // 4
  public bObject map_object; // ptr 1296
  public byte[] uvlayer_name = new byte[32]; // 1
  public int uvlayer_tmp; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    texture = DNATools.link(DNATools.ptr(buffer), Tex.class); // get ptr
    strength = buffer.getFloat();
    direction = buffer.getInt();
    buffer.get(defgrp_name);
    midlevel = buffer.getFloat();
    texmapping = buffer.getInt();
    map_object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    buffer.get(uvlayer_name);
    uvlayer_tmp = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(texture!=null?texture.hashCode():0);
    buffer.writeFloat(strength);
    buffer.writeInt(direction);
    buffer.write(defgrp_name);
    buffer.writeFloat(midlevel);
    buffer.writeInt(texmapping);
    buffer.writeInt(map_object!=null?map_object.hashCode():0);
    buffer.write(uvlayer_name);
    buffer.writeInt(uvlayer_tmp);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (DisplaceModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("DisplaceModifierData:\n");
    sb.append(super.toString());
    sb.append("  texture: ").append(texture).append("\n");
    sb.append("  strength: ").append(strength).append("\n");
    sb.append("  direction: ").append(direction).append("\n");
    sb.append("  defgrp_name: ").append(new String(defgrp_name)).append("\n");
    sb.append("  midlevel: ").append(midlevel).append("\n");
    sb.append("  texmapping: ").append(texmapping).append("\n");
    sb.append("  map_object: ").append(map_object).append("\n");
    sb.append("  uvlayer_name: ").append(new String(uvlayer_name)).append("\n");
    sb.append("  uvlayer_tmp: ").append(uvlayer_tmp).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public DisplaceModifierData copy() { try {return (DisplaceModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

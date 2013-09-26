package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class UVProjectModifierData extends ModifierData implements DNA, Cloneable { // #85
  public UVProjectModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public bObject[] projectors = new bObject[10]; // ptr 1296
  public Image image; // ptr 496
  public int flags; // 4
  public int num_projectors; // 4
  public float aspectx; // 4
  public float aspecty; // 4
  public float scalex; // 4
  public float scaley; // 4
  public byte[] uvlayer_name = new byte[32]; // 1
  public int uvlayer_tmp; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    for(int i=0;i<projectors.length;i++) projectors[i]=DNATools.link(DNATools.ptr(buffer), bObject.class);
    image = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    flags = buffer.getInt();
    num_projectors = buffer.getInt();
    aspectx = buffer.getFloat();
    aspecty = buffer.getFloat();
    scalex = buffer.getFloat();
    scaley = buffer.getFloat();
    buffer.get(uvlayer_name);
    uvlayer_tmp = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    for(int i=0;i<projectors.length;i++) buffer.writeInt(projectors[i]!=null?projectors[i].hashCode():0);
    buffer.writeInt(image!=null?image.hashCode():0);
    buffer.writeInt(flags);
    buffer.writeInt(num_projectors);
    buffer.writeFloat(aspectx);
    buffer.writeFloat(aspecty);
    buffer.writeFloat(scalex);
    buffer.writeFloat(scaley);
    buffer.write(uvlayer_name);
    buffer.writeInt(uvlayer_tmp);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (UVProjectModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("UVProjectModifierData:\n");
    sb.append(super.toString());
    sb.append("  projectors: ").append(Arrays.toString(projectors)).append("\n");
    sb.append("  image: ").append(image).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  num_projectors: ").append(num_projectors).append("\n");
    sb.append("  aspectx: ").append(aspectx).append("\n");
    sb.append("  aspecty: ").append(aspecty).append("\n");
    sb.append("  scalex: ").append(scalex).append("\n");
    sb.append("  scaley: ").append(scaley).append("\n");
    sb.append("  uvlayer_name: ").append(new String(uvlayer_name)).append("\n");
    sb.append("  uvlayer_tmp: ").append(uvlayer_tmp).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public UVProjectModifierData copy() { try {return (UVProjectModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

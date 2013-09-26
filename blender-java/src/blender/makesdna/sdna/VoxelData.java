package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class VoxelData implements DNA, Cloneable { // #30
  public VoxelData[] myarray;
  public int[] resol = new int[3]; // 4
  public int interp_type; // 4
  public short file_format; // 2
  public short flag; // 2
  public short extend; // 2
  public short smoked_type; // 2
  public bObject object; // ptr 1296
  public float int_multiplier; // 4
  public int still_frame; // 4
  public byte[] source_path = new byte[240]; // 1
  public Object dataset; // ptr 4
  public int cachedframe; // 4
  public int ok; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<resol.length;i++) resol[i]=buffer.getInt();
    interp_type = buffer.getInt();
    file_format = buffer.getShort();
    flag = buffer.getShort();
    extend = buffer.getShort();
    smoked_type = buffer.getShort();
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    int_multiplier = buffer.getFloat();
    still_frame = buffer.getInt();
    buffer.get(source_path);
    dataset = DNATools.ptr(buffer); // get ptr
    cachedframe = buffer.getInt();
    ok = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0;i<resol.length;i++) buffer.writeInt(resol[i]);
    buffer.writeInt(interp_type);
    buffer.writeShort(file_format);
    buffer.writeShort(flag);
    buffer.writeShort(extend);
    buffer.writeShort(smoked_type);
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeFloat(int_multiplier);
    buffer.writeInt(still_frame);
    buffer.write(source_path);
    buffer.writeInt(dataset!=null?dataset.hashCode():0);
    buffer.writeInt(cachedframe);
    buffer.writeInt(ok);
  }
  public Object setmyarray(Object array) {
    myarray = (VoxelData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("VoxelData:\n");
    sb.append("  resol: ").append(Arrays.toString(resol)).append("\n");
    sb.append("  interp_type: ").append(interp_type).append("\n");
    sb.append("  file_format: ").append(file_format).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  extend: ").append(extend).append("\n");
    sb.append("  smoked_type: ").append(smoked_type).append("\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  int_multiplier: ").append(int_multiplier).append("\n");
    sb.append("  still_frame: ").append(still_frame).append("\n");
    sb.append("  source_path: ").append(new String(source_path)).append("\n");
    sb.append("  dataset: ").append(dataset).append("\n");
    sb.append("  cachedframe: ").append(cachedframe).append("\n");
    sb.append("  ok: ").append(ok).append("\n");
    return sb.toString();
  }
  public VoxelData copy() { try {return (VoxelData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

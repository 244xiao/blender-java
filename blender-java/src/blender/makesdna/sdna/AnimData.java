package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class AnimData implements DNA, Cloneable { // #387
  public AnimData[] myarray;
  public bAction action; // ptr 152
  public bAction tmpact; // ptr 152
  public AnimMapper remap; // ptr 40
  public ListBase nla_tracks = new ListBase(); // 16
  public NlaStrip actstrip; // ptr 200
  public ListBase drivers = new ListBase(); // 16
  public ListBase overrides = new ListBase(); // 16
  public int flag; // 4
  public int recalc; // 4
  public short act_blendmode; // 2
  public short act_extendmode; // 2
  public float act_influence; // 4

  public void read(ByteBuffer buffer) {
    action = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    tmpact = DNATools.link(DNATools.ptr(buffer), bAction.class); // get ptr
    remap = DNATools.link(DNATools.ptr(buffer), AnimMapper.class); // get ptr
    nla_tracks.read(buffer);
    actstrip = DNATools.link(DNATools.ptr(buffer), NlaStrip.class); // get ptr
    drivers.read(buffer);
    overrides.read(buffer);
    flag = buffer.getInt();
    recalc = buffer.getInt();
    act_blendmode = buffer.getShort();
    act_extendmode = buffer.getShort();
    act_influence = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(action!=null?action.hashCode():0);
    buffer.writeInt(tmpact!=null?tmpact.hashCode():0);
    buffer.writeInt(remap!=null?remap.hashCode():0);
    nla_tracks.write(buffer);
    buffer.writeInt(actstrip!=null?actstrip.hashCode():0);
    drivers.write(buffer);
    overrides.write(buffer);
    buffer.writeInt(flag);
    buffer.writeInt(recalc);
    buffer.writeShort(act_blendmode);
    buffer.writeShort(act_extendmode);
    buffer.writeFloat(act_influence);
  }
  public Object setmyarray(Object array) {
    myarray = (AnimData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("AnimData:\n");
    sb.append("  action: ").append(action).append("\n");
    sb.append("  tmpact: ").append(tmpact).append("\n");
    sb.append("  remap: ").append(remap).append("\n");
    sb.append("  nla_tracks: ").append(nla_tracks).append("\n");
    sb.append("  actstrip: ").append(actstrip).append("\n");
    sb.append("  drivers: ").append(drivers).append("\n");
    sb.append("  overrides: ").append(overrides).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  act_blendmode: ").append(act_blendmode).append("\n");
    sb.append("  act_extendmode: ").append(act_extendmode).append("\n");
    sb.append("  act_influence: ").append(act_influence).append("\n");
    return sb.toString();
  }
  public AnimData copy() { try {return (AnimData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

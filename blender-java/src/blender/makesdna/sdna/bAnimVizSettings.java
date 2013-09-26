package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bAnimVizSettings implements DNA, Cloneable { // #265
  public bAnimVizSettings[] myarray;
  public int ghost_sf; // 4
  public int ghost_ef; // 4
  public int ghost_bc; // 4
  public int ghost_ac; // 4
  public short ghost_type; // 2
  public short ghost_step; // 2
  public short ghost_flag; // 2
  public short recalc; // 2
  public short path_type; // 2
  public short path_step; // 2
  public short path_viewflag; // 2
  public short path_bakeflag; // 2
  public int path_sf; // 4
  public int path_ef; // 4
  public int path_bc; // 4
  public int path_ac; // 4

  public void read(ByteBuffer buffer) {
    ghost_sf = buffer.getInt();
    ghost_ef = buffer.getInt();
    ghost_bc = buffer.getInt();
    ghost_ac = buffer.getInt();
    ghost_type = buffer.getShort();
    ghost_step = buffer.getShort();
    ghost_flag = buffer.getShort();
    recalc = buffer.getShort();
    path_type = buffer.getShort();
    path_step = buffer.getShort();
    path_viewflag = buffer.getShort();
    path_bakeflag = buffer.getShort();
    path_sf = buffer.getInt();
    path_ef = buffer.getInt();
    path_bc = buffer.getInt();
    path_ac = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(ghost_sf);
    buffer.writeInt(ghost_ef);
    buffer.writeInt(ghost_bc);
    buffer.writeInt(ghost_ac);
    buffer.writeShort(ghost_type);
    buffer.writeShort(ghost_step);
    buffer.writeShort(ghost_flag);
    buffer.writeShort(recalc);
    buffer.writeShort(path_type);
    buffer.writeShort(path_step);
    buffer.writeShort(path_viewflag);
    buffer.writeShort(path_bakeflag);
    buffer.writeInt(path_sf);
    buffer.writeInt(path_ef);
    buffer.writeInt(path_bc);
    buffer.writeInt(path_ac);
  }
  public Object setmyarray(Object array) {
    myarray = (bAnimVizSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bAnimVizSettings:\n");
    sb.append("  ghost_sf: ").append(ghost_sf).append("\n");
    sb.append("  ghost_ef: ").append(ghost_ef).append("\n");
    sb.append("  ghost_bc: ").append(ghost_bc).append("\n");
    sb.append("  ghost_ac: ").append(ghost_ac).append("\n");
    sb.append("  ghost_type: ").append(ghost_type).append("\n");
    sb.append("  ghost_step: ").append(ghost_step).append("\n");
    sb.append("  ghost_flag: ").append(ghost_flag).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  path_type: ").append(path_type).append("\n");
    sb.append("  path_step: ").append(path_step).append("\n");
    sb.append("  path_viewflag: ").append(path_viewflag).append("\n");
    sb.append("  path_bakeflag: ").append(path_bakeflag).append("\n");
    sb.append("  path_sf: ").append(path_sf).append("\n");
    sb.append("  path_ef: ").append(path_ef).append("\n");
    sb.append("  path_bc: ").append(path_bc).append("\n");
    sb.append("  path_ac: ").append(path_ac).append("\n");
    return sb.toString();
  }
  public bAnimVizSettings copy() { try {return (bAnimVizSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

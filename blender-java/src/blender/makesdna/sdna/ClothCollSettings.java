package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ClothCollSettings implements DNA, Cloneable { // #352
  public ClothCollSettings[] myarray;
  public Object collision_list; // ptr (LinkNode) 0
  public float epsilon; // 4
  public float self_friction; // 4
  public float friction; // 4
  public float selfepsilon; // 4
  public int flags; // 4
  public short self_loop_count; // 2
  public short loop_count; // 2
  public Group group; // ptr 104

  public void read(ByteBuffer buffer) {
    collision_list = DNATools.ptr(buffer); // get ptr
    epsilon = buffer.getFloat();
    self_friction = buffer.getFloat();
    friction = buffer.getFloat();
    selfepsilon = buffer.getFloat();
    flags = buffer.getInt();
    self_loop_count = buffer.getShort();
    loop_count = buffer.getShort();
    group = DNATools.link(DNATools.ptr(buffer), Group.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(collision_list!=null?collision_list.hashCode():0);
    buffer.writeFloat(epsilon);
    buffer.writeFloat(self_friction);
    buffer.writeFloat(friction);
    buffer.writeFloat(selfepsilon);
    buffer.writeInt(flags);
    buffer.writeShort(self_loop_count);
    buffer.writeShort(loop_count);
    buffer.writeInt(group!=null?group.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ClothCollSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ClothCollSettings:\n");
    sb.append("  collision_list: ").append(collision_list).append("\n");
    sb.append("  epsilon: ").append(epsilon).append("\n");
    sb.append("  self_friction: ").append(self_friction).append("\n");
    sb.append("  friction: ").append(friction).append("\n");
    sb.append("  selfepsilon: ").append(selfepsilon).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  self_loop_count: ").append(self_loop_count).append("\n");
    sb.append("  loop_count: ").append(loop_count).append("\n");
    sb.append("  group: ").append(group).append("\n");
    return sb.toString();
  }
  public ClothCollSettings copy() { try {return (ClothCollSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

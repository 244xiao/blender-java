package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BoidSettings implements DNA, Cloneable { // #397
  public BoidSettings[] myarray;
  public int options; // 4
  public int last_state_id; // 4
  public float landing_smoothness; // 4
  public float height; // 4
  public float banking; // 4
  public float pitch; // 4
  public float health; // 4
  public float aggression; // 4
  public float strength; // 4
  public float accuracy; // 4
  public float range; // 4
  public float air_min_speed; // 4
  public float air_max_speed; // 4
  public float air_max_acc; // 4
  public float air_max_ave; // 4
  public float air_personal_space; // 4
  public float land_jump_speed; // 4
  public float land_max_speed; // 4
  public float land_max_acc; // 4
  public float land_max_ave; // 4
  public float land_personal_space; // 4
  public float land_stick_force; // 4
  public ListBase states = new ListBase(); // 16

  public void read(ByteBuffer buffer) {
    options = buffer.getInt();
    last_state_id = buffer.getInt();
    landing_smoothness = buffer.getFloat();
    height = buffer.getFloat();
    banking = buffer.getFloat();
    pitch = buffer.getFloat();
    health = buffer.getFloat();
    aggression = buffer.getFloat();
    strength = buffer.getFloat();
    accuracy = buffer.getFloat();
    range = buffer.getFloat();
    air_min_speed = buffer.getFloat();
    air_max_speed = buffer.getFloat();
    air_max_acc = buffer.getFloat();
    air_max_ave = buffer.getFloat();
    air_personal_space = buffer.getFloat();
    land_jump_speed = buffer.getFloat();
    land_max_speed = buffer.getFloat();
    land_max_acc = buffer.getFloat();
    land_max_ave = buffer.getFloat();
    land_personal_space = buffer.getFloat();
    land_stick_force = buffer.getFloat();
    states.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(options);
    buffer.writeInt(last_state_id);
    buffer.writeFloat(landing_smoothness);
    buffer.writeFloat(height);
    buffer.writeFloat(banking);
    buffer.writeFloat(pitch);
    buffer.writeFloat(health);
    buffer.writeFloat(aggression);
    buffer.writeFloat(strength);
    buffer.writeFloat(accuracy);
    buffer.writeFloat(range);
    buffer.writeFloat(air_min_speed);
    buffer.writeFloat(air_max_speed);
    buffer.writeFloat(air_max_acc);
    buffer.writeFloat(air_max_ave);
    buffer.writeFloat(air_personal_space);
    buffer.writeFloat(land_jump_speed);
    buffer.writeFloat(land_max_speed);
    buffer.writeFloat(land_max_acc);
    buffer.writeFloat(land_max_ave);
    buffer.writeFloat(land_personal_space);
    buffer.writeFloat(land_stick_force);
    states.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (BoidSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BoidSettings:\n");
    sb.append("  options: ").append(options).append("\n");
    sb.append("  last_state_id: ").append(last_state_id).append("\n");
    sb.append("  landing_smoothness: ").append(landing_smoothness).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  banking: ").append(banking).append("\n");
    sb.append("  pitch: ").append(pitch).append("\n");
    sb.append("  health: ").append(health).append("\n");
    sb.append("  aggression: ").append(aggression).append("\n");
    sb.append("  strength: ").append(strength).append("\n");
    sb.append("  accuracy: ").append(accuracy).append("\n");
    sb.append("  range: ").append(range).append("\n");
    sb.append("  air_min_speed: ").append(air_min_speed).append("\n");
    sb.append("  air_max_speed: ").append(air_max_speed).append("\n");
    sb.append("  air_max_acc: ").append(air_max_acc).append("\n");
    sb.append("  air_max_ave: ").append(air_max_ave).append("\n");
    sb.append("  air_personal_space: ").append(air_personal_space).append("\n");
    sb.append("  land_jump_speed: ").append(land_jump_speed).append("\n");
    sb.append("  land_max_speed: ").append(land_max_speed).append("\n");
    sb.append("  land_max_acc: ").append(land_max_acc).append("\n");
    sb.append("  land_max_ave: ").append(land_max_ave).append("\n");
    sb.append("  land_personal_space: ").append(land_personal_space).append("\n");
    sb.append("  land_stick_force: ").append(land_stick_force).append("\n");
    sb.append("  states: ").append(states).append("\n");
    return sb.toString();
  }
  public BoidSettings copy() { try {return (BoidSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

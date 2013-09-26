package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Cycles implements DNA, Cloneable { // #370
  public FMod_Cycles[] myarray;
  public short before_mode; // 2
  public short after_mode; // 2
  public short before_cycles; // 2
  public short after_cycles; // 2

  public void read(ByteBuffer buffer) {
    before_mode = buffer.getShort();
    after_mode = buffer.getShort();
    before_cycles = buffer.getShort();
    after_cycles = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(before_mode);
    buffer.writeShort(after_mode);
    buffer.writeShort(before_cycles);
    buffer.writeShort(after_cycles);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Cycles[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Cycles:\n");
    sb.append("  before_mode: ").append(before_mode).append("\n");
    sb.append("  after_mode: ").append(after_mode).append("\n");
    sb.append("  before_cycles: ").append(before_cycles).append("\n");
    sb.append("  after_cycles: ").append(after_cycles).append("\n");
    return sb.toString();
  }
  public FMod_Cycles copy() { try {return (FMod_Cycles)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

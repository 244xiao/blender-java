package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FMod_Python implements DNA, Cloneable { // #371
  public FMod_Python[] myarray;
  public Text script; // ptr 176
  public IDProperty prop; // ptr 96

  public void read(ByteBuffer buffer) {
    script = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
    prop = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(script!=null?script.hashCode():0);
    buffer.writeInt(prop!=null?prop.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (FMod_Python[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FMod_Python:\n");
    sb.append("  script: ").append(script).append("\n");
    sb.append("  prop: ").append(prop).append("\n");
    return sb.toString();
  }
  public FMod_Python copy() { try {return (FMod_Python)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

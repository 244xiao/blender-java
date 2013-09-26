package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ChannelDriver implements DNA, Cloneable { // #377
  public ChannelDriver[] myarray;
  public ListBase variables = new ListBase(); // 16
  public byte[] expression = new byte[256]; // 1
  public Object expr_comp; // ptr 0
  public float curval; // 4
  public float influence; // 4
  public int type; // 4
  public int flag; // 4

  public void read(ByteBuffer buffer) {
    variables.read(buffer);
    buffer.get(expression);
    expr_comp = DNATools.ptr(buffer); // get ptr
    curval = buffer.getFloat();
    influence = buffer.getFloat();
    type = buffer.getInt();
    flag = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    variables.write(buffer);
    buffer.write(expression);
    buffer.writeInt(expr_comp!=null?expr_comp.hashCode():0);
    buffer.writeFloat(curval);
    buffer.writeFloat(influence);
    buffer.writeInt(type);
    buffer.writeInt(flag);
  }
  public Object setmyarray(Object array) {
    myarray = (ChannelDriver[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ChannelDriver:\n");
    sb.append("  variables: ").append(variables).append("\n");
    sb.append("  expression: ").append(new String(expression)).append("\n");
    sb.append("  expr_comp: ").append(expr_comp).append("\n");
    sb.append("  curval: ").append(curval).append("\n");
    sb.append("  influence: ").append(influence).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    return sb.toString();
  }
  public ChannelDriver copy() { try {return (ChannelDriver)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NodeScriptDict implements DNA, Cloneable { // #323
  public NodeScriptDict[] myarray;
  public Object dict; // ptr 0
  public Object node; // ptr 0

  public void read(ByteBuffer buffer) {
    dict = DNATools.ptr(buffer); // get ptr
    node = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(dict!=null?dict.hashCode():0);
    buffer.writeInt(node!=null?node.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (NodeScriptDict[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("NodeScriptDict:\n");
    sb.append("  dict: ").append(dict).append("\n");
    sb.append("  node: ").append(node).append("\n");
    return sb.toString();
  }
  public NodeScriptDict copy() { try {return (NodeScriptDict)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

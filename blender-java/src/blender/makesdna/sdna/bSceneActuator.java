package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bSceneActuator implements DNA, Cloneable { // #242
  public bSceneActuator[] myarray;
  public short type; // 2
  public short pad1; // 2
  public int pad; // 4
  public Scene scene; // ptr 1552
  public bObject camera; // ptr 1296

  public void read(ByteBuffer buffer) {
    type = buffer.getShort();
    pad1 = buffer.getShort();
    pad = buffer.getInt();
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    camera = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(type);
    buffer.writeShort(pad1);
    buffer.writeInt(pad);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(camera!=null?camera.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (bSceneActuator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bSceneActuator:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  camera: ").append(camera).append("\n");
    return sb.toString();
  }
  public bSceneActuator copy() { try {return (bSceneActuator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

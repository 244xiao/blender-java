package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class PartialVisibility implements DNA, Cloneable { // #71
  public PartialVisibility[] myarray;
  public Object vert_map; // ptr 4
  public Object edge_map; // ptr 4
  public MFace old_faces; // ptr 20
  public MEdge old_edges; // ptr 12
  public int totface; // 4
  public int totedge; // 4
  public int totvert; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    vert_map = DNATools.ptr(buffer); // get ptr
    edge_map = DNATools.ptr(buffer); // get ptr
    old_faces = DNATools.link(DNATools.ptr(buffer), MFace.class); // get ptr
    old_edges = DNATools.link(DNATools.ptr(buffer), MEdge.class); // get ptr
    totface = buffer.getInt();
    totedge = buffer.getInt();
    totvert = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(vert_map!=null?vert_map.hashCode():0);
    buffer.writeInt(edge_map!=null?edge_map.hashCode():0);
    buffer.writeInt(old_faces!=null?old_faces.hashCode():0);
    buffer.writeInt(old_edges!=null?old_edges.hashCode():0);
    buffer.writeInt(totface);
    buffer.writeInt(totedge);
    buffer.writeInt(totvert);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (PartialVisibility[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("PartialVisibility:\n");
    sb.append("  vert_map: ").append(vert_map).append("\n");
    sb.append("  edge_map: ").append(edge_map).append("\n");
    sb.append("  old_faces: ").append(old_faces).append("\n");
    sb.append("  old_edges: ").append(old_edges).append("\n");
    sb.append("  totface: ").append(totface).append("\n");
    sb.append("  totedge: ").append(totedge).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public PartialVisibility copy() { try {return (PartialVisibility)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

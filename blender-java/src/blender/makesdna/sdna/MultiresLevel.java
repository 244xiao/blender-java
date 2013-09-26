package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class MultiresLevel extends Link<MultiresLevel> implements DNA, Cloneable { // #69
  public MultiresLevel[] myarray;
  public MultiresFace faces; // ptr 24
  public MultiresColFace colfaces; // ptr 64
  public MultiresEdge edges; // ptr 12
  public int totvert; // 4
  public int totface; // 4
  public int totedge; // 4
  public int pad; // 4
  public MVert verts; // ptr 20

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), MultiresLevel.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), MultiresLevel.class); // get ptr
    faces = DNATools.link(DNATools.ptr(buffer), MultiresFace.class); // get ptr
    colfaces = DNATools.link(DNATools.ptr(buffer), MultiresColFace.class); // get ptr
    edges = DNATools.link(DNATools.ptr(buffer), MultiresEdge.class); // get ptr
    totvert = buffer.getInt();
    totface = buffer.getInt();
    totedge = buffer.getInt();
    pad = buffer.getInt();
    verts = DNATools.link(DNATools.ptr(buffer), MVert.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(faces!=null?faces.hashCode():0);
    buffer.writeInt(colfaces!=null?colfaces.hashCode():0);
    buffer.writeInt(edges!=null?edges.hashCode():0);
    buffer.writeInt(totvert);
    buffer.writeInt(totface);
    buffer.writeInt(totedge);
    buffer.writeInt(pad);
    buffer.writeInt(verts!=null?verts.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (MultiresLevel[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("MultiresLevel:\n");
    sb.append("  faces: ").append(faces).append("\n");
    sb.append("  colfaces: ").append(colfaces).append("\n");
    sb.append("  edges: ").append(edges).append("\n");
    sb.append("  totvert: ").append(totvert).append("\n");
    sb.append("  totface: ").append(totface).append("\n");
    sb.append("  totedge: ").append(totedge).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  verts: ").append(verts).append("\n");
    return sb.toString();
  }
  public MultiresLevel copy() { try {return (MultiresLevel)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

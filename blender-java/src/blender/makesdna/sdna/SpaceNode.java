package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceNode extends SpaceLink implements DNA, Cloneable { // #171
  public SpaceNode[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public Object id; // ptr 72
  public Object from; // ptr 72
  public short flag; // 2
  public short menunr; // 2
  public float aspect; // 4
  public Object curfont; // ptr 0
  public float xof; // 4
  public float yof; // 4
  public float zoom; // 4
  public float padf; // 4
  public float mx; // 4
  public float my; // 4
  public bNodeTree nodetree; // ptr 264
  public bNodeTree edittree; // ptr 264
  public int treetype; // 4
  public short texfrom; // 2
  public short recalc; // 2
  public ListBase linkdrag = new ListBase(); // 16
  public bGPdata gpd; // ptr 104

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    id = DNATools.ptr(buffer); // get ptr
    from = DNATools.ptr(buffer); // get ptr
    flag = buffer.getShort();
    menunr = buffer.getShort();
    aspect = buffer.getFloat();
    curfont = DNATools.ptr(buffer); // get ptr
    xof = buffer.getFloat();
    yof = buffer.getFloat();
    zoom = buffer.getFloat();
    padf = buffer.getFloat();
    mx = buffer.getFloat();
    my = buffer.getFloat();
    nodetree = DNATools.link(DNATools.ptr(buffer), bNodeTree.class); // get ptr
    edittree = DNATools.link(DNATools.ptr(buffer), bNodeTree.class); // get ptr
    treetype = buffer.getInt();
    texfrom = buffer.getShort();
    recalc = buffer.getShort();
    linkdrag.read(buffer);
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    v2d.write(buffer);
    buffer.writeInt(id!=null?id.hashCode():0);
    buffer.writeInt(from!=null?from.hashCode():0);
    buffer.writeShort(flag);
    buffer.writeShort(menunr);
    buffer.writeFloat(aspect);
    buffer.writeInt(curfont!=null?curfont.hashCode():0);
    buffer.writeFloat(xof);
    buffer.writeFloat(yof);
    buffer.writeFloat(zoom);
    buffer.writeFloat(padf);
    buffer.writeFloat(mx);
    buffer.writeFloat(my);
    buffer.writeInt(nodetree!=null?nodetree.hashCode():0);
    buffer.writeInt(edittree!=null?edittree.hashCode():0);
    buffer.writeInt(treetype);
    buffer.writeShort(texfrom);
    buffer.writeShort(recalc);
    linkdrag.write(buffer);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceNode[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceNode:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  from: ").append(from).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  menunr: ").append(menunr).append("\n");
    sb.append("  aspect: ").append(aspect).append("\n");
    sb.append("  curfont: ").append(curfont).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  zoom: ").append(zoom).append("\n");
    sb.append("  padf: ").append(padf).append("\n");
    sb.append("  mx: ").append(mx).append("\n");
    sb.append("  my: ").append(my).append("\n");
    sb.append("  nodetree: ").append(nodetree).append("\n");
    sb.append("  edittree: ").append(edittree).append("\n");
    sb.append("  treetype: ").append(treetype).append("\n");
    sb.append("  texfrom: ").append(texfrom).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  linkdrag: ").append(linkdrag).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    return sb.toString();
  }
  public SpaceNode copy() { try {return (SpaceNode)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

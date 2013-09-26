package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceOops extends SpaceLink implements DNA, Cloneable { // #163
  public SpaceOops[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public View2D v2d = new View2D(); // 144
  public ListBase tree = new ListBase(); // 16
  public TreeStore treestore; // ptr 16
  public byte[] search_string = new byte[32]; // 1
  public TreeStoreElem search_tse = new TreeStoreElem(); // 16
  public short flag; // 2
  public short outlinevis; // 2
  public short storeflag; // 2
  public short search_flags; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    v2d.read(buffer);
    tree.read(buffer);
    treestore = DNATools.link(DNATools.ptr(buffer), TreeStore.class); // get ptr
    buffer.get(search_string);
    search_tse.read(buffer);
    flag = buffer.getShort();
    outlinevis = buffer.getShort();
    storeflag = buffer.getShort();
    search_flags = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    v2d.write(buffer);
    tree.write(buffer);
    buffer.writeInt(treestore!=null?treestore.hashCode():0);
    buffer.write(search_string);
    search_tse.write(buffer);
    buffer.writeShort(flag);
    buffer.writeShort(outlinevis);
    buffer.writeShort(storeflag);
    buffer.writeShort(search_flags);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceOops[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceOops:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  v2d: ").append(v2d).append("\n");
    sb.append("  tree: ").append(tree).append("\n");
    sb.append("  treestore: ").append(treestore).append("\n");
    sb.append("  search_string: ").append(new String(search_string)).append("\n");
    sb.append("  search_tse: ").append(search_tse).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  outlinevis: ").append(outlinevis).append("\n");
    sb.append("  storeflag: ").append(storeflag).append("\n");
    sb.append("  search_flags: ").append(search_flags).append("\n");
    return sb.toString();
  }
  public SpaceOops copy() { try {return (SpaceOops)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

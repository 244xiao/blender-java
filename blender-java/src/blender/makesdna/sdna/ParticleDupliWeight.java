package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleDupliWeight extends Link<ParticleDupliWeight> implements DNA, Cloneable { // #346
  public ParticleDupliWeight[] myarray;
  public bObject ob; // ptr 1296
  public short count; // 2
  public short flag; // 2
  public short[] rt = new short[2]; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), ParticleDupliWeight.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), ParticleDupliWeight.class); // get ptr
    ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    count = buffer.getShort();
    flag = buffer.getShort();
    for(int i=0;i<rt.length;i++) rt[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(ob!=null?ob.hashCode():0);
    buffer.writeShort(count);
    buffer.writeShort(flag);
    for(int i=0;i<rt.length;i++) buffer.writeShort(rt[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleDupliWeight[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleDupliWeight:\n");
    sb.append("  ob: ").append(ob).append("\n");
    sb.append("  count: ").append(count).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rt: ").append(Arrays.toString(rt)).append("\n");
    return sb.toString();
  }
  public ParticleDupliWeight copy() { try {return (ParticleDupliWeight)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

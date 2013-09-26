package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleSystemModifierData extends ModifierData implements DNA, Cloneable { // #100
  public ParticleSystemModifierData[] myarray;
  public ModifierData modifier = (ModifierData)this;
  public ParticleSystem psys; // ptr 528
  public Object dm; // ptr (DerivedMesh) 0
  public int totdmvert; // 4
  public int totdmedge; // 4
  public int totdmface; // 4
  public short flag; // 2
  public short rt; // 2

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    psys = DNATools.link(DNATools.ptr(buffer), ParticleSystem.class); // get ptr
    dm = DNATools.ptr(buffer); // get ptr
    totdmvert = buffer.getInt();
    totdmedge = buffer.getInt();
    totdmface = buffer.getInt();
    flag = buffer.getShort();
    rt = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(psys!=null?psys.hashCode():0);
    buffer.writeInt(dm!=null?dm.hashCode():0);
    buffer.writeInt(totdmvert);
    buffer.writeInt(totdmedge);
    buffer.writeInt(totdmface);
    buffer.writeShort(flag);
    buffer.writeShort(rt);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleSystemModifierData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleSystemModifierData:\n");
    sb.append(super.toString());
    sb.append("  psys: ").append(psys).append("\n");
    sb.append("  dm: ").append(dm).append("\n");
    sb.append("  totdmvert: ").append(totdmvert).append("\n");
    sb.append("  totdmedge: ").append(totdmedge).append("\n");
    sb.append("  totdmface: ").append(totdmface).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    return sb.toString();
  }
  public ParticleSystemModifierData copy() { try {return (ParticleSystemModifierData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Camera extends ID implements DNA, Cloneable { // #21
  public Camera[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public short type; // 2
  public short flag; // 2
  public float passepartalpha; // 4
  public float clipsta; // 4
  public float clipend; // 4
  public float lens; // 4
  public float ortho_scale; // 4
  public float drawsize; // 4
  public float shiftx; // 4
  public float shifty; // 4
  public float YF_dofdist; // 4
  public Ipo ipo; // ptr 112
  public bObject dof_ob; // ptr 1296

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    type = buffer.getShort();
    flag = buffer.getShort();
    passepartalpha = buffer.getFloat();
    clipsta = buffer.getFloat();
    clipend = buffer.getFloat();
    lens = buffer.getFloat();
    ortho_scale = buffer.getFloat();
    drawsize = buffer.getFloat();
    shiftx = buffer.getFloat();
    shifty = buffer.getFloat();
    YF_dofdist = buffer.getFloat();
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    dof_ob = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeShort(type);
    buffer.writeShort(flag);
    buffer.writeFloat(passepartalpha);
    buffer.writeFloat(clipsta);
    buffer.writeFloat(clipend);
    buffer.writeFloat(lens);
    buffer.writeFloat(ortho_scale);
    buffer.writeFloat(drawsize);
    buffer.writeFloat(shiftx);
    buffer.writeFloat(shifty);
    buffer.writeFloat(YF_dofdist);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(dof_ob!=null?dof_ob.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (Camera[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Camera:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  passepartalpha: ").append(passepartalpha).append("\n");
    sb.append("  clipsta: ").append(clipsta).append("\n");
    sb.append("  clipend: ").append(clipend).append("\n");
    sb.append("  lens: ").append(lens).append("\n");
    sb.append("  ortho_scale: ").append(ortho_scale).append("\n");
    sb.append("  drawsize: ").append(drawsize).append("\n");
    sb.append("  shiftx: ").append(shiftx).append("\n");
    sb.append("  shifty: ").append(shifty).append("\n");
    sb.append("  YF_dofdist: ").append(YF_dofdist).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  dof_ob: ").append(dof_ob).append("\n");
    return sb.toString();
  }
  public Camera copy() { try {return (Camera)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

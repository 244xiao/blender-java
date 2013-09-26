package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class EnvMap implements DNA, Cloneable { // #28
  public EnvMap[] myarray;
  public bObject object; // ptr 1296
  public Image ima; // ptr 496
  public Object[] cube = new Object[6]; // ptr (ImBuf) 0
  public float[][] imat = new float[4][4]; // 4
  public float[][] obimat = new float[3][3]; // 4
  public short type; // 2
  public short stype; // 2
  public float clipsta; // 4
  public float clipend; // 4
  public float viewscale; // 4
  public int notlay; // 4
  public short cuberes; // 2
  public short depth; // 2
  public int ok; // 4
  public int lastframe; // 4
  public short recalc; // 2
  public short lastsize; // 2

  public void read(ByteBuffer buffer) {
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    ima = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    for(int i=0;i<cube.length;i++) cube[i]=DNATools.link(DNATools.ptr(buffer), Object.class);
    for(int i=0;i<imat.length;i++) for(int j=0;j<imat[i].length;j++) imat[i][j]=buffer.getFloat();
    for(int i=0;i<obimat.length;i++) for(int j=0;j<obimat[i].length;j++) obimat[i][j]=buffer.getFloat();
    type = buffer.getShort();
    stype = buffer.getShort();
    clipsta = buffer.getFloat();
    clipend = buffer.getFloat();
    viewscale = buffer.getFloat();
    notlay = buffer.getInt();
    cuberes = buffer.getShort();
    depth = buffer.getShort();
    ok = buffer.getInt();
    lastframe = buffer.getInt();
    recalc = buffer.getShort();
    lastsize = buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(object!=null?object.hashCode():0);
    buffer.writeInt(ima!=null?ima.hashCode():0);
    for(int i=0;i<cube.length;i++) buffer.writeInt(cube[i]!=null?cube[i].hashCode():0);
    for(int i=0; i<imat.length; i++)  for(int j=0;j<imat[i].length;j++) buffer.writeFloat(imat[i][j]);
    for(int i=0; i<obimat.length; i++)  for(int j=0;j<obimat[i].length;j++) buffer.writeFloat(obimat[i][j]);
    buffer.writeShort(type);
    buffer.writeShort(stype);
    buffer.writeFloat(clipsta);
    buffer.writeFloat(clipend);
    buffer.writeFloat(viewscale);
    buffer.writeInt(notlay);
    buffer.writeShort(cuberes);
    buffer.writeShort(depth);
    buffer.writeInt(ok);
    buffer.writeInt(lastframe);
    buffer.writeShort(recalc);
    buffer.writeShort(lastsize);
  }
  public Object setmyarray(Object array) {
    myarray = (EnvMap[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("EnvMap:\n");
    sb.append("  object: ").append(object).append("\n");
    sb.append("  ima: ").append(ima).append("\n");
    sb.append("  cube: ").append(Arrays.toString(cube)).append("\n");
    sb.append("  imat: ").append(Arrays.toString(imat)).append("\n");
    sb.append("  obimat: ").append(Arrays.toString(obimat)).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  stype: ").append(stype).append("\n");
    sb.append("  clipsta: ").append(clipsta).append("\n");
    sb.append("  clipend: ").append(clipend).append("\n");
    sb.append("  viewscale: ").append(viewscale).append("\n");
    sb.append("  notlay: ").append(notlay).append("\n");
    sb.append("  cuberes: ").append(cuberes).append("\n");
    sb.append("  depth: ").append(depth).append("\n");
    sb.append("  ok: ").append(ok).append("\n");
    sb.append("  lastframe: ").append(lastframe).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  lastsize: ").append(lastsize).append("\n");
    return sb.toString();
  }
  public EnvMap copy() { try {return (EnvMap)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

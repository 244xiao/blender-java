package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class GlowVars implements DNA, Cloneable { // #208
  public GlowVars[] myarray;
  public float fMini; // 4
  public float fClamp; // 4
  public float fBoost; // 4
  public float dDist; // 4
  public int dQuality; // 4
  public int bNoComp; // 4

  public void read(ByteBuffer buffer) {
    fMini = buffer.getFloat();
    fClamp = buffer.getFloat();
    fBoost = buffer.getFloat();
    dDist = buffer.getFloat();
    dQuality = buffer.getInt();
    bNoComp = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(fMini);
    buffer.writeFloat(fClamp);
    buffer.writeFloat(fBoost);
    buffer.writeFloat(dDist);
    buffer.writeInt(dQuality);
    buffer.writeInt(bNoComp);
  }
  public Object setmyarray(Object array) {
    myarray = (GlowVars[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("GlowVars:\n");
    sb.append("  fMini: ").append(fMini).append("\n");
    sb.append("  fClamp: ").append(fClamp).append("\n");
    sb.append("  fBoost: ").append(fBoost).append("\n");
    sb.append("  dDist: ").append(dDist).append("\n");
    sb.append("  dQuality: ").append(dQuality).append("\n");
    sb.append("  bNoComp: ").append(bNoComp).append("\n");
    return sb.toString();
  }
  public GlowVars copy() { try {return (GlowVars)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

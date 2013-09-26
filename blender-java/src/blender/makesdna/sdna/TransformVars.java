package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class TransformVars implements DNA, Cloneable { // #209
  public TransformVars[] myarray;
  public float ScalexIni; // 4
  public float ScaleyIni; // 4
  public float ScalexFin; // 4
  public float ScaleyFin; // 4
  public float xIni; // 4
  public float xFin; // 4
  public float yIni; // 4
  public float yFin; // 4
  public float rotIni; // 4
  public float rotFin; // 4
  public int percent; // 4
  public int interpolation; // 4
  public int uniform_scale; // 4

  public void read(ByteBuffer buffer) {
    ScalexIni = buffer.getFloat();
    ScaleyIni = buffer.getFloat();
    ScalexFin = buffer.getFloat();
    ScaleyFin = buffer.getFloat();
    xIni = buffer.getFloat();
    xFin = buffer.getFloat();
    yIni = buffer.getFloat();
    yFin = buffer.getFloat();
    rotIni = buffer.getFloat();
    rotFin = buffer.getFloat();
    percent = buffer.getInt();
    interpolation = buffer.getInt();
    uniform_scale = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(ScalexIni);
    buffer.writeFloat(ScaleyIni);
    buffer.writeFloat(ScalexFin);
    buffer.writeFloat(ScaleyFin);
    buffer.writeFloat(xIni);
    buffer.writeFloat(xFin);
    buffer.writeFloat(yIni);
    buffer.writeFloat(yFin);
    buffer.writeFloat(rotIni);
    buffer.writeFloat(rotFin);
    buffer.writeInt(percent);
    buffer.writeInt(interpolation);
    buffer.writeInt(uniform_scale);
  }
  public Object setmyarray(Object array) {
    myarray = (TransformVars[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("TransformVars:\n");
    sb.append("  ScalexIni: ").append(ScalexIni).append("\n");
    sb.append("  ScaleyIni: ").append(ScaleyIni).append("\n");
    sb.append("  ScalexFin: ").append(ScalexFin).append("\n");
    sb.append("  ScaleyFin: ").append(ScaleyFin).append("\n");
    sb.append("  xIni: ").append(xIni).append("\n");
    sb.append("  xFin: ").append(xFin).append("\n");
    sb.append("  yIni: ").append(yIni).append("\n");
    sb.append("  yFin: ").append(yFin).append("\n");
    sb.append("  rotIni: ").append(rotIni).append("\n");
    sb.append("  rotFin: ").append(rotFin).append("\n");
    sb.append("  percent: ").append(percent).append("\n");
    sb.append("  interpolation: ").append(interpolation).append("\n");
    sb.append("  uniform_scale: ").append(uniform_scale).append("\n");
    return sb.toString();
  }
  public TransformVars copy() { try {return (TransformVars)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

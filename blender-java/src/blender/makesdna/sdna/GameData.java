package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class GameData implements DNA, Cloneable { // #138
  public GameData[] myarray;
  public float gravity; // 4
  public float activityBoxRadius; // 4
  public short mode; // 2
  public short flag; // 2
  public short matmode; // 2
  public short[] pad = new short[3]; // 2
  public short occlusionRes; // 2
  public short physicsEngine; // 2
  public short ticrate; // 2
  public short maxlogicstep; // 2
  public short physubstep; // 2
  public short maxphystep; // 2
  public GameFraming framing = new GameFraming(); // 16
  public short fullscreen; // 2
  public short xplay; // 2
  public short yplay; // 2
  public short freqplay; // 2
  public short depth; // 2
  public short attrib; // 2
  public short rt1; // 2
  public short rt2; // 2
  public GameDome dome = new GameDome(); // 24
  public short stereoflag; // 2
  public short stereomode; // 2
  public short xsch; // 2
  public short ysch; // 2
  public float eyeseparation; // 4
  public float pad1; // 4

  public void read(ByteBuffer buffer) {
    gravity = buffer.getFloat();
    activityBoxRadius = buffer.getFloat();
    mode = buffer.getShort();
    flag = buffer.getShort();
    matmode = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    occlusionRes = buffer.getShort();
    physicsEngine = buffer.getShort();
    ticrate = buffer.getShort();
    maxlogicstep = buffer.getShort();
    physubstep = buffer.getShort();
    maxphystep = buffer.getShort();
    framing.read(buffer);
    fullscreen = buffer.getShort();
    xplay = buffer.getShort();
    yplay = buffer.getShort();
    freqplay = buffer.getShort();
    depth = buffer.getShort();
    attrib = buffer.getShort();
    rt1 = buffer.getShort();
    rt2 = buffer.getShort();
    dome.read(buffer);
    stereoflag = buffer.getShort();
    stereomode = buffer.getShort();
    xsch = buffer.getShort();
    ysch = buffer.getShort();
    eyeseparation = buffer.getFloat();
    pad1 = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeFloat(gravity);
    buffer.writeFloat(activityBoxRadius);
    buffer.writeShort(mode);
    buffer.writeShort(flag);
    buffer.writeShort(matmode);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.writeShort(occlusionRes);
    buffer.writeShort(physicsEngine);
    buffer.writeShort(ticrate);
    buffer.writeShort(maxlogicstep);
    buffer.writeShort(physubstep);
    buffer.writeShort(maxphystep);
    framing.write(buffer);
    buffer.writeShort(fullscreen);
    buffer.writeShort(xplay);
    buffer.writeShort(yplay);
    buffer.writeShort(freqplay);
    buffer.writeShort(depth);
    buffer.writeShort(attrib);
    buffer.writeShort(rt1);
    buffer.writeShort(rt2);
    dome.write(buffer);
    buffer.writeShort(stereoflag);
    buffer.writeShort(stereomode);
    buffer.writeShort(xsch);
    buffer.writeShort(ysch);
    buffer.writeFloat(eyeseparation);
    buffer.writeFloat(pad1);
  }
  public Object setmyarray(Object array) {
    myarray = (GameData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("GameData:\n");
    sb.append("  gravity: ").append(gravity).append("\n");
    sb.append("  activityBoxRadius: ").append(activityBoxRadius).append("\n");
    sb.append("  mode: ").append(mode).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  matmode: ").append(matmode).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  occlusionRes: ").append(occlusionRes).append("\n");
    sb.append("  physicsEngine: ").append(physicsEngine).append("\n");
    sb.append("  ticrate: ").append(ticrate).append("\n");
    sb.append("  maxlogicstep: ").append(maxlogicstep).append("\n");
    sb.append("  physubstep: ").append(physubstep).append("\n");
    sb.append("  maxphystep: ").append(maxphystep).append("\n");
    sb.append("  framing: ").append(framing).append("\n");
    sb.append("  fullscreen: ").append(fullscreen).append("\n");
    sb.append("  xplay: ").append(xplay).append("\n");
    sb.append("  yplay: ").append(yplay).append("\n");
    sb.append("  freqplay: ").append(freqplay).append("\n");
    sb.append("  depth: ").append(depth).append("\n");
    sb.append("  attrib: ").append(attrib).append("\n");
    sb.append("  rt1: ").append(rt1).append("\n");
    sb.append("  rt2: ").append(rt2).append("\n");
    sb.append("  dome: ").append(dome).append("\n");
    sb.append("  stereoflag: ").append(stereoflag).append("\n");
    sb.append("  stereomode: ").append(stereomode).append("\n");
    sb.append("  xsch: ").append(xsch).append("\n");
    sb.append("  ysch: ").append(ysch).append("\n");
    sb.append("  eyeseparation: ").append(eyeseparation).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    return sb.toString();
  }
  public GameData copy() { try {return (GameData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

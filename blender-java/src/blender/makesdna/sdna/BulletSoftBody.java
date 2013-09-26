package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class BulletSoftBody implements DNA, Cloneable { // #123
  public BulletSoftBody[] myarray;
  public int flag; // 4
  public float linStiff; // 4
  public float angStiff; // 4
  public float volume; // 4
  public int viterations; // 4
  public int piterations; // 4
  public int diterations; // 4
  public int citerations; // 4
  public float kSRHR_CL; // 4
  public float kSKHR_CL; // 4
  public float kSSHR_CL; // 4
  public float kSR_SPLT_CL; // 4
  public float kSK_SPLT_CL; // 4
  public float kSS_SPLT_CL; // 4
  public float kVCF; // 4
  public float kDP; // 4
  public float kDG; // 4
  public float kLF; // 4
  public float kPR; // 4
  public float kVC; // 4
  public float kDF; // 4
  public float kMT; // 4
  public float kCHR; // 4
  public float kKHR; // 4
  public float kSHR; // 4
  public float kAHR; // 4
  public int collisionflags; // 4
  public int numclusteriterations; // 4
  public float welding; // 4
  public float margin; // 4

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    linStiff = buffer.getFloat();
    angStiff = buffer.getFloat();
    volume = buffer.getFloat();
    viterations = buffer.getInt();
    piterations = buffer.getInt();
    diterations = buffer.getInt();
    citerations = buffer.getInt();
    kSRHR_CL = buffer.getFloat();
    kSKHR_CL = buffer.getFloat();
    kSSHR_CL = buffer.getFloat();
    kSR_SPLT_CL = buffer.getFloat();
    kSK_SPLT_CL = buffer.getFloat();
    kSS_SPLT_CL = buffer.getFloat();
    kVCF = buffer.getFloat();
    kDP = buffer.getFloat();
    kDG = buffer.getFloat();
    kLF = buffer.getFloat();
    kPR = buffer.getFloat();
    kVC = buffer.getFloat();
    kDF = buffer.getFloat();
    kMT = buffer.getFloat();
    kCHR = buffer.getFloat();
    kKHR = buffer.getFloat();
    kSHR = buffer.getFloat();
    kAHR = buffer.getFloat();
    collisionflags = buffer.getInt();
    numclusteriterations = buffer.getInt();
    welding = buffer.getFloat();
    margin = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeFloat(linStiff);
    buffer.writeFloat(angStiff);
    buffer.writeFloat(volume);
    buffer.writeInt(viterations);
    buffer.writeInt(piterations);
    buffer.writeInt(diterations);
    buffer.writeInt(citerations);
    buffer.writeFloat(kSRHR_CL);
    buffer.writeFloat(kSKHR_CL);
    buffer.writeFloat(kSSHR_CL);
    buffer.writeFloat(kSR_SPLT_CL);
    buffer.writeFloat(kSK_SPLT_CL);
    buffer.writeFloat(kSS_SPLT_CL);
    buffer.writeFloat(kVCF);
    buffer.writeFloat(kDP);
    buffer.writeFloat(kDG);
    buffer.writeFloat(kLF);
    buffer.writeFloat(kPR);
    buffer.writeFloat(kVC);
    buffer.writeFloat(kDF);
    buffer.writeFloat(kMT);
    buffer.writeFloat(kCHR);
    buffer.writeFloat(kKHR);
    buffer.writeFloat(kSHR);
    buffer.writeFloat(kAHR);
    buffer.writeInt(collisionflags);
    buffer.writeInt(numclusteriterations);
    buffer.writeFloat(welding);
    buffer.writeFloat(margin);
  }
  public Object setmyarray(Object array) {
    myarray = (BulletSoftBody[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("BulletSoftBody:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  linStiff: ").append(linStiff).append("\n");
    sb.append("  angStiff: ").append(angStiff).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    sb.append("  viterations: ").append(viterations).append("\n");
    sb.append("  piterations: ").append(piterations).append("\n");
    sb.append("  diterations: ").append(diterations).append("\n");
    sb.append("  citerations: ").append(citerations).append("\n");
    sb.append("  kSRHR_CL: ").append(kSRHR_CL).append("\n");
    sb.append("  kSKHR_CL: ").append(kSKHR_CL).append("\n");
    sb.append("  kSSHR_CL: ").append(kSSHR_CL).append("\n");
    sb.append("  kSR_SPLT_CL: ").append(kSR_SPLT_CL).append("\n");
    sb.append("  kSK_SPLT_CL: ").append(kSK_SPLT_CL).append("\n");
    sb.append("  kSS_SPLT_CL: ").append(kSS_SPLT_CL).append("\n");
    sb.append("  kVCF: ").append(kVCF).append("\n");
    sb.append("  kDP: ").append(kDP).append("\n");
    sb.append("  kDG: ").append(kDG).append("\n");
    sb.append("  kLF: ").append(kLF).append("\n");
    sb.append("  kPR: ").append(kPR).append("\n");
    sb.append("  kVC: ").append(kVC).append("\n");
    sb.append("  kDF: ").append(kDF).append("\n");
    sb.append("  kMT: ").append(kMT).append("\n");
    sb.append("  kCHR: ").append(kCHR).append("\n");
    sb.append("  kKHR: ").append(kKHR).append("\n");
    sb.append("  kSHR: ").append(kSHR).append("\n");
    sb.append("  kAHR: ").append(kAHR).append("\n");
    sb.append("  collisionflags: ").append(collisionflags).append("\n");
    sb.append("  numclusteriterations: ").append(numclusteriterations).append("\n");
    sb.append("  welding: ").append(welding).append("\n");
    sb.append("  margin: ").append(margin).append("\n");
    return sb.toString();
  }
  public BulletSoftBody copy() { try {return (BulletSoftBody)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

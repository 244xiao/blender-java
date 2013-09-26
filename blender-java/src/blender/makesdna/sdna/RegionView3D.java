package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class RegionView3D implements DNA, Cloneable { // #153
  public RegionView3D[] myarray;
  public float[][] winmat = new float[4][4]; // 4
  public float[][] viewmat = new float[4][4]; // 4
  public float[][] viewinv = new float[4][4]; // 4
  public float[][] persmat = new float[4][4]; // 4
  public float[][] persinv = new float[4][4]; // 4
  public float[][] viewmatob = new float[4][4]; // 4
  public float[][] persmatob = new float[4][4]; // 4
  public float[][] twmat = new float[4][4]; // 4
  public float[] viewquat = new float[4]; // 4
  public float dist; // 4
  public float zfac; // 4
  public float camdx; // 4
  public float camdy; // 4
  public float pixsize; // 4
  public float[] ofs = new float[3]; // 4
  public short camzoom; // 2
  public short twdrawflag; // 2
  public int pad; // 4
  public short rflag; // 2
  public short viewlock; // 2
  public short persp; // 2
  public short view; // 2
  public float[][] clip = new float[6][4]; // 4
  public float[][] clip_local = new float[6][4]; // 4
  public BoundBox clipbb; // ptr 104
  public bGPdata gpd; // ptr 104
  public RegionView3D localvd; // ptr 864
  public Object ri; // ptr (RenderInfo) 0
  public Object depths; // ptr (ViewDepths) 0
  public Object sms; // ptr (SmoothViewStore) 0
  public Object smooth_timer; // ptr (wmTimer) 0
  public float[] lviewquat = new float[4]; // 4
  public short lpersp; // 2
  public short lview; // 2
  public float gridview; // 4
  public float[] twangle = new float[3]; // 4
  public float padf; // 4

  public void read(ByteBuffer buffer) {
    for(int i=0;i<winmat.length;i++) for(int j=0;j<winmat[i].length;j++) winmat[i][j]=buffer.getFloat();
    for(int i=0;i<viewmat.length;i++) for(int j=0;j<viewmat[i].length;j++) viewmat[i][j]=buffer.getFloat();
    for(int i=0;i<viewinv.length;i++) for(int j=0;j<viewinv[i].length;j++) viewinv[i][j]=buffer.getFloat();
    for(int i=0;i<persmat.length;i++) for(int j=0;j<persmat[i].length;j++) persmat[i][j]=buffer.getFloat();
    for(int i=0;i<persinv.length;i++) for(int j=0;j<persinv[i].length;j++) persinv[i][j]=buffer.getFloat();
    for(int i=0;i<viewmatob.length;i++) for(int j=0;j<viewmatob[i].length;j++) viewmatob[i][j]=buffer.getFloat();
    for(int i=0;i<persmatob.length;i++) for(int j=0;j<persmatob[i].length;j++) persmatob[i][j]=buffer.getFloat();
    for(int i=0;i<twmat.length;i++) for(int j=0;j<twmat[i].length;j++) twmat[i][j]=buffer.getFloat();
    for(int i=0;i<viewquat.length;i++) viewquat[i]=buffer.getFloat();
    dist = buffer.getFloat();
    zfac = buffer.getFloat();
    camdx = buffer.getFloat();
    camdy = buffer.getFloat();
    pixsize = buffer.getFloat();
    for(int i=0;i<ofs.length;i++) ofs[i]=buffer.getFloat();
    camzoom = buffer.getShort();
    twdrawflag = buffer.getShort();
    pad = buffer.getInt();
    rflag = buffer.getShort();
    viewlock = buffer.getShort();
    persp = buffer.getShort();
    view = buffer.getShort();
    for(int i=0;i<clip.length;i++) for(int j=0;j<clip[i].length;j++) clip[i][j]=buffer.getFloat();
    for(int i=0;i<clip_local.length;i++) for(int j=0;j<clip_local[i].length;j++) clip_local[i][j]=buffer.getFloat();
    clipbb = DNATools.link(DNATools.ptr(buffer), BoundBox.class); // get ptr
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
    localvd = DNATools.link(DNATools.ptr(buffer), RegionView3D.class); // get ptr
    ri = DNATools.ptr(buffer); // get ptr
    depths = DNATools.ptr(buffer); // get ptr
    sms = DNATools.ptr(buffer); // get ptr
    smooth_timer = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<lviewquat.length;i++) lviewquat[i]=buffer.getFloat();
    lpersp = buffer.getShort();
    lview = buffer.getShort();
    gridview = buffer.getFloat();
    for(int i=0;i<twangle.length;i++) twangle[i]=buffer.getFloat();
    padf = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    for(int i=0; i<winmat.length; i++)  for(int j=0;j<winmat[i].length;j++) buffer.writeFloat(winmat[i][j]);
    for(int i=0; i<viewmat.length; i++)  for(int j=0;j<viewmat[i].length;j++) buffer.writeFloat(viewmat[i][j]);
    for(int i=0; i<viewinv.length; i++)  for(int j=0;j<viewinv[i].length;j++) buffer.writeFloat(viewinv[i][j]);
    for(int i=0; i<persmat.length; i++)  for(int j=0;j<persmat[i].length;j++) buffer.writeFloat(persmat[i][j]);
    for(int i=0; i<persinv.length; i++)  for(int j=0;j<persinv[i].length;j++) buffer.writeFloat(persinv[i][j]);
    for(int i=0; i<viewmatob.length; i++)  for(int j=0;j<viewmatob[i].length;j++) buffer.writeFloat(viewmatob[i][j]);
    for(int i=0; i<persmatob.length; i++)  for(int j=0;j<persmatob[i].length;j++) buffer.writeFloat(persmatob[i][j]);
    for(int i=0; i<twmat.length; i++)  for(int j=0;j<twmat[i].length;j++) buffer.writeFloat(twmat[i][j]);
    for(int i=0;i<viewquat.length;i++) buffer.writeFloat(viewquat[i]);
    buffer.writeFloat(dist);
    buffer.writeFloat(zfac);
    buffer.writeFloat(camdx);
    buffer.writeFloat(camdy);
    buffer.writeFloat(pixsize);
    for(int i=0;i<ofs.length;i++) buffer.writeFloat(ofs[i]);
    buffer.writeShort(camzoom);
    buffer.writeShort(twdrawflag);
    buffer.writeInt(pad);
    buffer.writeShort(rflag);
    buffer.writeShort(viewlock);
    buffer.writeShort(persp);
    buffer.writeShort(view);
    for(int i=0; i<clip.length; i++)  for(int j=0;j<clip[i].length;j++) buffer.writeFloat(clip[i][j]);
    for(int i=0; i<clip_local.length; i++)  for(int j=0;j<clip_local[i].length;j++) buffer.writeFloat(clip_local[i][j]);
    buffer.writeInt(clipbb!=null?clipbb.hashCode():0);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
    buffer.writeInt(localvd!=null?localvd.hashCode():0);
    buffer.writeInt(ri!=null?ri.hashCode():0);
    buffer.writeInt(depths!=null?depths.hashCode():0);
    buffer.writeInt(sms!=null?sms.hashCode():0);
    buffer.writeInt(smooth_timer!=null?smooth_timer.hashCode():0);
    for(int i=0;i<lviewquat.length;i++) buffer.writeFloat(lviewquat[i]);
    buffer.writeShort(lpersp);
    buffer.writeShort(lview);
    buffer.writeFloat(gridview);
    for(int i=0;i<twangle.length;i++) buffer.writeFloat(twangle[i]);
    buffer.writeFloat(padf);
  }
  public Object setmyarray(Object array) {
    myarray = (RegionView3D[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("RegionView3D:\n");
    sb.append("  winmat: ").append(Arrays.toString(winmat)).append("\n");
    sb.append("  viewmat: ").append(Arrays.toString(viewmat)).append("\n");
    sb.append("  viewinv: ").append(Arrays.toString(viewinv)).append("\n");
    sb.append("  persmat: ").append(Arrays.toString(persmat)).append("\n");
    sb.append("  persinv: ").append(Arrays.toString(persinv)).append("\n");
    sb.append("  viewmatob: ").append(Arrays.toString(viewmatob)).append("\n");
    sb.append("  persmatob: ").append(Arrays.toString(persmatob)).append("\n");
    sb.append("  twmat: ").append(Arrays.toString(twmat)).append("\n");
    sb.append("  viewquat: ").append(Arrays.toString(viewquat)).append("\n");
    sb.append("  dist: ").append(dist).append("\n");
    sb.append("  zfac: ").append(zfac).append("\n");
    sb.append("  camdx: ").append(camdx).append("\n");
    sb.append("  camdy: ").append(camdy).append("\n");
    sb.append("  pixsize: ").append(pixsize).append("\n");
    sb.append("  ofs: ").append(Arrays.toString(ofs)).append("\n");
    sb.append("  camzoom: ").append(camzoom).append("\n");
    sb.append("  twdrawflag: ").append(twdrawflag).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  rflag: ").append(rflag).append("\n");
    sb.append("  viewlock: ").append(viewlock).append("\n");
    sb.append("  persp: ").append(persp).append("\n");
    sb.append("  view: ").append(view).append("\n");
    sb.append("  clip: ").append(Arrays.toString(clip)).append("\n");
    sb.append("  clip_local: ").append(Arrays.toString(clip_local)).append("\n");
    sb.append("  clipbb: ").append(clipbb).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    sb.append("  localvd: ").append(localvd).append("\n");
    sb.append("  ri: ").append(ri).append("\n");
    sb.append("  depths: ").append(depths).append("\n");
    sb.append("  sms: ").append(sms).append("\n");
    sb.append("  smooth_timer: ").append(smooth_timer).append("\n");
    sb.append("  lviewquat: ").append(Arrays.toString(lviewquat)).append("\n");
    sb.append("  lpersp: ").append(lpersp).append("\n");
    sb.append("  lview: ").append(lview).append("\n");
    sb.append("  gridview: ").append(gridview).append("\n");
    sb.append("  twangle: ").append(Arrays.toString(twangle)).append("\n");
    sb.append("  padf: ").append(padf).append("\n");
    return sb.toString();
  }
  public RegionView3D copy() { try {return (RegionView3D)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

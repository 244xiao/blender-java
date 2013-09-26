package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceImage extends SpaceLink implements DNA, Cloneable { // #164
  public SpaceImage[] myarray;
  public int flag; // 4
  public Image image; // ptr 496
  public ImageUser iuser = new ImageUser(); // 40
  public CurveMapping cumap; // ptr 320
  public Scopes scopes = new Scopes(); // 4224
  public Histogram sample_line_hist = new Histogram(); // 4120
  public bGPdata gpd; // ptr 104
  public float[] cursor = new float[2]; // 4
  public float xof; // 4
  public float yof; // 4
  public float zoom; // 4
  public float centx; // 4
  public float centy; // 4
  public short curtile; // 2
  public short imtypenr; // 2
  public short lock; // 2
  public short pin; // 2
  public byte dt_uv; // 1
  public byte sticky; // 1
  public byte dt_uvstretch; // 1
  public byte around; // 1

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    flag = buffer.getInt();
    image = DNATools.link(DNATools.ptr(buffer), Image.class); // get ptr
    iuser.read(buffer);
    cumap = DNATools.link(DNATools.ptr(buffer), CurveMapping.class); // get ptr
    scopes.read(buffer);
    sample_line_hist.read(buffer);
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
    for(int i=0;i<cursor.length;i++) cursor[i]=buffer.getFloat();
    xof = buffer.getFloat();
    yof = buffer.getFloat();
    zoom = buffer.getFloat();
    centx = buffer.getFloat();
    centy = buffer.getFloat();
    curtile = buffer.getShort();
    imtypenr = buffer.getShort();
    lock = buffer.getShort();
    pin = buffer.getShort();
    dt_uv = buffer.get();
    sticky = buffer.get();
    dt_uvstretch = buffer.get();
    around = buffer.get();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeInt(flag);
    buffer.writeInt(image!=null?image.hashCode():0);
    iuser.write(buffer);
    buffer.writeInt(cumap!=null?cumap.hashCode():0);
    scopes.write(buffer);
    sample_line_hist.write(buffer);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
    for(int i=0;i<cursor.length;i++) buffer.writeFloat(cursor[i]);
    buffer.writeFloat(xof);
    buffer.writeFloat(yof);
    buffer.writeFloat(zoom);
    buffer.writeFloat(centx);
    buffer.writeFloat(centy);
    buffer.writeShort(curtile);
    buffer.writeShort(imtypenr);
    buffer.writeShort(lock);
    buffer.writeShort(pin);
    buffer.writeByte(dt_uv);
    buffer.writeByte(sticky);
    buffer.writeByte(dt_uvstretch);
    buffer.writeByte(around);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceImage[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceImage:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  image: ").append(image).append("\n");
    sb.append("  iuser: ").append(iuser).append("\n");
    sb.append("  cumap: ").append(cumap).append("\n");
    sb.append("  scopes: ").append(scopes).append("\n");
    sb.append("  sample_line_hist: ").append(sample_line_hist).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    sb.append("  cursor: ").append(Arrays.toString(cursor)).append("\n");
    sb.append("  xof: ").append(xof).append("\n");
    sb.append("  yof: ").append(yof).append("\n");
    sb.append("  zoom: ").append(zoom).append("\n");
    sb.append("  centx: ").append(centx).append("\n");
    sb.append("  centy: ").append(centy).append("\n");
    sb.append("  curtile: ").append(curtile).append("\n");
    sb.append("  imtypenr: ").append(imtypenr).append("\n");
    sb.append("  lock: ").append(lock).append("\n");
    sb.append("  pin: ").append(pin).append("\n");
    sb.append("  dt_uv: ").append(dt_uv).append("\n");
    sb.append("  sticky: ").append(sticky).append("\n");
    sb.append("  dt_uvstretch: ").append(dt_uvstretch).append("\n");
    sb.append("  around: ").append(around).append("\n");
    return sb.toString();
  }
  public SpaceImage copy() { try {return (SpaceImage)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

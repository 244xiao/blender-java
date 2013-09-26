package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ParticleEditSettings implements DNA, Cloneable { // #143
  public ParticleEditSettings[] myarray;
  public short flag; // 2
  public short totrekey; // 2
  public short totaddkey; // 2
  public short brushtype; // 2
  public ParticleBrushData[] brush = new ParticleBrushData[7]; // 16
  public Object paintcursor; // ptr 0
  public float emitterdist; // 4
  public float rt; // 4
  public int selectmode; // 4
  public int edittype; // 4
  public int draw_step; // 4
  public int fade_frames; // 4
  public Scene scene; // ptr 1552
  public bObject object; // ptr 1296

  public void read(ByteBuffer buffer) {
    flag = buffer.getShort();
    totrekey = buffer.getShort();
    totaddkey = buffer.getShort();
    brushtype = buffer.getShort();
    for(int i=0;i<brush.length;i++) { brush[i]=new ParticleBrushData(); brush[i].read(buffer); }
    paintcursor = DNATools.ptr(buffer); // get ptr
    emitterdist = buffer.getFloat();
    rt = buffer.getFloat();
    selectmode = buffer.getInt();
    edittype = buffer.getInt();
    draw_step = buffer.getInt();
    fade_frames = buffer.getInt();
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    object = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeShort(flag);
    buffer.writeShort(totrekey);
    buffer.writeShort(totaddkey);
    buffer.writeShort(brushtype);
    for(int i=0;i<brush.length;i++) brush[i].write(buffer);
    buffer.writeInt(paintcursor!=null?paintcursor.hashCode():0);
    buffer.writeFloat(emitterdist);
    buffer.writeFloat(rt);
    buffer.writeInt(selectmode);
    buffer.writeInt(edittype);
    buffer.writeInt(draw_step);
    buffer.writeInt(fade_frames);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(object!=null?object.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (ParticleEditSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ParticleEditSettings:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  totrekey: ").append(totrekey).append("\n");
    sb.append("  totaddkey: ").append(totaddkey).append("\n");
    sb.append("  brushtype: ").append(brushtype).append("\n");
    sb.append("  brush: ").append(Arrays.toString(brush)).append("\n");
    sb.append("  paintcursor: ").append(paintcursor).append("\n");
    sb.append("  emitterdist: ").append(emitterdist).append("\n");
    sb.append("  rt: ").append(rt).append("\n");
    sb.append("  selectmode: ").append(selectmode).append("\n");
    sb.append("  edittype: ").append(edittype).append("\n");
    sb.append("  draw_step: ").append(draw_step).append("\n");
    sb.append("  fade_frames: ").append(fade_frames).append("\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  object: ").append(object).append("\n");
    return sb.toString();
  }
  public ParticleEditSettings copy() { try {return (ParticleEditSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

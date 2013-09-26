package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Image extends ID implements DNA, Cloneable { // #23
  public Image[] myarray;
  public ID id = (ID)this;
  public byte[] name = new byte[240]; // 1
  public ListBase ibufs = new ListBase(); // 16
  public Object gputexture; // ptr (GPUTexture) 0
  public Object anim; // ptr (anim) 0
  public Object rr; // ptr (RenderResult) 0
  public Object[] renders = new Object[8]; // ptr (RenderResult) 0
  public short render_slot; // 2
  public short last_render_slot; // 2
  public short ok; // 2
  public short flag; // 2
  public short source; // 2
  public short type; // 2
  public int lastframe; // 4
  public short tpageflag; // 2
  public short totbind; // 2
  public short xrep; // 2
  public short yrep; // 2
  public short twsta; // 2
  public short twend; // 2
  public int bindcode; // 4
  public Object repbind; // ptr 4
  public PackedFile packedfile; // ptr 16
  public PreviewImage preview; // ptr 40
  public float lastupdate; // 4
  public int lastused; // 4
  public short animspeed; // 2
  public short gen_x; // 2
  public short gen_y; // 2
  public short gen_type; // 2
  public float aspx; // 4
  public float aspy; // 4

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    buffer.get(name);
    ibufs.read(buffer);
    gputexture = DNATools.ptr(buffer); // get ptr
    anim = DNATools.ptr(buffer); // get ptr
    rr = DNATools.ptr(buffer); // get ptr
    for(int i=0;i<renders.length;i++) renders[i]=DNATools.link(DNATools.ptr(buffer), Object.class);
    render_slot = buffer.getShort();
    last_render_slot = buffer.getShort();
    ok = buffer.getShort();
    flag = buffer.getShort();
    source = buffer.getShort();
    type = buffer.getShort();
    lastframe = buffer.getInt();
    tpageflag = buffer.getShort();
    totbind = buffer.getShort();
    xrep = buffer.getShort();
    yrep = buffer.getShort();
    twsta = buffer.getShort();
    twend = buffer.getShort();
    bindcode = buffer.getInt();
    repbind = DNATools.ptr(buffer); // get ptr
    packedfile = DNATools.link(DNATools.ptr(buffer), PackedFile.class); // get ptr
    preview = DNATools.link(DNATools.ptr(buffer), PreviewImage.class); // get ptr
    lastupdate = buffer.getFloat();
    lastused = buffer.getInt();
    animspeed = buffer.getShort();
    gen_x = buffer.getShort();
    gen_y = buffer.getShort();
    gen_type = buffer.getShort();
    aspx = buffer.getFloat();
    aspy = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.write(name);
    ibufs.write(buffer);
    buffer.writeInt(gputexture!=null?gputexture.hashCode():0);
    buffer.writeInt(anim!=null?anim.hashCode():0);
    buffer.writeInt(rr!=null?rr.hashCode():0);
    for(int i=0;i<renders.length;i++) buffer.writeInt(renders[i]!=null?renders[i].hashCode():0);
    buffer.writeShort(render_slot);
    buffer.writeShort(last_render_slot);
    buffer.writeShort(ok);
    buffer.writeShort(flag);
    buffer.writeShort(source);
    buffer.writeShort(type);
    buffer.writeInt(lastframe);
    buffer.writeShort(tpageflag);
    buffer.writeShort(totbind);
    buffer.writeShort(xrep);
    buffer.writeShort(yrep);
    buffer.writeShort(twsta);
    buffer.writeShort(twend);
    buffer.writeInt(bindcode);
    buffer.writeInt(repbind!=null?repbind.hashCode():0);
    buffer.writeInt(packedfile!=null?packedfile.hashCode():0);
    buffer.writeInt(preview!=null?preview.hashCode():0);
    buffer.writeFloat(lastupdate);
    buffer.writeInt(lastused);
    buffer.writeShort(animspeed);
    buffer.writeShort(gen_x);
    buffer.writeShort(gen_y);
    buffer.writeShort(gen_type);
    buffer.writeFloat(aspx);
    buffer.writeFloat(aspy);
  }
  public Object setmyarray(Object array) {
    myarray = (Image[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Image:\n");
    sb.append(super.toString());
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  ibufs: ").append(ibufs).append("\n");
    sb.append("  gputexture: ").append(gputexture).append("\n");
    sb.append("  anim: ").append(anim).append("\n");
    sb.append("  rr: ").append(rr).append("\n");
    sb.append("  renders: ").append(Arrays.toString(renders)).append("\n");
    sb.append("  render_slot: ").append(render_slot).append("\n");
    sb.append("  last_render_slot: ").append(last_render_slot).append("\n");
    sb.append("  ok: ").append(ok).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  source: ").append(source).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  lastframe: ").append(lastframe).append("\n");
    sb.append("  tpageflag: ").append(tpageflag).append("\n");
    sb.append("  totbind: ").append(totbind).append("\n");
    sb.append("  xrep: ").append(xrep).append("\n");
    sb.append("  yrep: ").append(yrep).append("\n");
    sb.append("  twsta: ").append(twsta).append("\n");
    sb.append("  twend: ").append(twend).append("\n");
    sb.append("  bindcode: ").append(bindcode).append("\n");
    sb.append("  repbind: ").append(repbind).append("\n");
    sb.append("  packedfile: ").append(packedfile).append("\n");
    sb.append("  preview: ").append(preview).append("\n");
    sb.append("  lastupdate: ").append(lastupdate).append("\n");
    sb.append("  lastused: ").append(lastused).append("\n");
    sb.append("  animspeed: ").append(animspeed).append("\n");
    sb.append("  gen_x: ").append(gen_x).append("\n");
    sb.append("  gen_y: ").append(gen_y).append("\n");
    sb.append("  gen_type: ").append(gen_type).append("\n");
    sb.append("  aspx: ").append(aspx).append("\n");
    sb.append("  aspy: ").append(aspy).append("\n");
    return sb.toString();
  }
  public Image copy() { try {return (Image)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

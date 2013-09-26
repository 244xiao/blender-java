package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class SpaceText extends SpaceLink implements DNA, Cloneable { // #166
  public SpaceText[] myarray;
  public float blockscale; // 4
  public short[] blockhandler = new short[8]; // 2
  public Text text; // ptr 176
  public int top; // 4
  public int viewlines; // 4
  public short flags; // 2
  public short menunr; // 2
  public short lheight; // 2
  public byte cwidth; // 1
  public byte linenrs_tot; // 1
  public int left; // 4
  public int showlinenrs; // 4
  public int tabnumber; // 4
  public short showsyntax; // 2
  public short line_hlight; // 2
  public short overwrite; // 2
  public short live_edit; // 2
  public float pix_per_line; // 4
  public rcti txtscroll = new rcti(); // 16
  public rcti txtbar = new rcti(); // 16
  public int wordwrap; // 4
  public int doplugins; // 4
  public byte[] findstr = new byte[256]; // 1
  public byte[] replacestr = new byte[256]; // 1
  public short margin_column; // 2
  public byte[] pad = new byte[6]; // 1
  public Object drawcache; // ptr 0

  public void read(ByteBuffer buffer) {
    next = DNATools.ptr(buffer); // get ptr
    prev = DNATools.ptr(buffer); // get ptr
    regionbase.read(buffer);
    spacetype = buffer.getInt();
    blockscale = buffer.getFloat();
    for(int i=0;i<blockhandler.length;i++) blockhandler[i]=buffer.getShort();
    text = DNATools.link(DNATools.ptr(buffer), Text.class); // get ptr
    top = buffer.getInt();
    viewlines = buffer.getInt();
    flags = buffer.getShort();
    menunr = buffer.getShort();
    lheight = buffer.getShort();
    cwidth = buffer.get();
    linenrs_tot = buffer.get();
    left = buffer.getInt();
    showlinenrs = buffer.getInt();
    tabnumber = buffer.getInt();
    showsyntax = buffer.getShort();
    line_hlight = buffer.getShort();
    overwrite = buffer.getShort();
    live_edit = buffer.getShort();
    pix_per_line = buffer.getFloat();
    txtscroll.read(buffer);
    txtbar.read(buffer);
    wordwrap = buffer.getInt();
    doplugins = buffer.getInt();
    buffer.get(findstr);
    buffer.get(replacestr);
    margin_column = buffer.getShort();
    buffer.get(pad);
    drawcache = DNATools.ptr(buffer); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    regionbase.write(buffer);
    buffer.writeInt(spacetype);
    buffer.writeFloat(blockscale);
    for(int i=0;i<blockhandler.length;i++) buffer.writeShort(blockhandler[i]);
    buffer.writeInt(text!=null?text.hashCode():0);
    buffer.writeInt(top);
    buffer.writeInt(viewlines);
    buffer.writeShort(flags);
    buffer.writeShort(menunr);
    buffer.writeShort(lheight);
    buffer.writeByte(cwidth);
    buffer.writeByte(linenrs_tot);
    buffer.writeInt(left);
    buffer.writeInt(showlinenrs);
    buffer.writeInt(tabnumber);
    buffer.writeShort(showsyntax);
    buffer.writeShort(line_hlight);
    buffer.writeShort(overwrite);
    buffer.writeShort(live_edit);
    buffer.writeFloat(pix_per_line);
    txtscroll.write(buffer);
    txtbar.write(buffer);
    buffer.writeInt(wordwrap);
    buffer.writeInt(doplugins);
    buffer.write(findstr);
    buffer.write(replacestr);
    buffer.writeShort(margin_column);
    buffer.write(pad);
    buffer.writeInt(drawcache!=null?drawcache.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (SpaceText[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("SpaceText:\n");
    sb.append("  regionbase: ").append(regionbase).append("\n");
    sb.append("  spacetype: ").append(spacetype).append("\n");
    sb.append("  blockscale: ").append(blockscale).append("\n");
    sb.append("  blockhandler: ").append(Arrays.toString(blockhandler)).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  top: ").append(top).append("\n");
    sb.append("  viewlines: ").append(viewlines).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  menunr: ").append(menunr).append("\n");
    sb.append("  lheight: ").append(lheight).append("\n");
    sb.append("  cwidth: ").append(cwidth).append("\n");
    sb.append("  linenrs_tot: ").append(linenrs_tot).append("\n");
    sb.append("  left: ").append(left).append("\n");
    sb.append("  showlinenrs: ").append(showlinenrs).append("\n");
    sb.append("  tabnumber: ").append(tabnumber).append("\n");
    sb.append("  showsyntax: ").append(showsyntax).append("\n");
    sb.append("  line_hlight: ").append(line_hlight).append("\n");
    sb.append("  overwrite: ").append(overwrite).append("\n");
    sb.append("  live_edit: ").append(live_edit).append("\n");
    sb.append("  pix_per_line: ").append(pix_per_line).append("\n");
    sb.append("  txtscroll: ").append(txtscroll).append("\n");
    sb.append("  txtbar: ").append(txtbar).append("\n");
    sb.append("  wordwrap: ").append(wordwrap).append("\n");
    sb.append("  doplugins: ").append(doplugins).append("\n");
    sb.append("  findstr: ").append(new String(findstr)).append("\n");
    sb.append("  replacestr: ").append(new String(replacestr)).append("\n");
    sb.append("  margin_column: ").append(margin_column).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  drawcache: ").append(drawcache).append("\n");
    return sb.toString();
  }
  public SpaceText copy() { try {return (SpaceText)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

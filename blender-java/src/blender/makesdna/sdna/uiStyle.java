package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class uiStyle extends Link<uiStyle> implements DNA, Cloneable { // #180
  public uiStyle[] myarray;
  public byte[] name = new byte[64]; // 1
  public uiFontStyle paneltitle = new uiFontStyle(); // 32
  public uiFontStyle grouplabel = new uiFontStyle(); // 32
  public uiFontStyle widgetlabel = new uiFontStyle(); // 32
  public uiFontStyle widget = new uiFontStyle(); // 32
  public float panelzoom; // 4
  public short minlabelchars; // 2
  public short minwidgetchars; // 2
  public short columnspace; // 2
  public short templatespace; // 2
  public short boxspace; // 2
  public short buttonspacex; // 2
  public short buttonspacey; // 2
  public short panelspace; // 2
  public short panelouter; // 2
  public short[] pad = new short[1]; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), uiStyle.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), uiStyle.class); // get ptr
    buffer.get(name);
    paneltitle.read(buffer);
    grouplabel.read(buffer);
    widgetlabel.read(buffer);
    widget.read(buffer);
    panelzoom = buffer.getFloat();
    minlabelchars = buffer.getShort();
    minwidgetchars = buffer.getShort();
    columnspace = buffer.getShort();
    templatespace = buffer.getShort();
    boxspace = buffer.getShort();
    buttonspacex = buffer.getShort();
    buttonspacey = buffer.getShort();
    panelspace = buffer.getShort();
    panelouter = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    paneltitle.write(buffer);
    grouplabel.write(buffer);
    widgetlabel.write(buffer);
    widget.write(buffer);
    buffer.writeFloat(panelzoom);
    buffer.writeShort(minlabelchars);
    buffer.writeShort(minwidgetchars);
    buffer.writeShort(columnspace);
    buffer.writeShort(templatespace);
    buffer.writeShort(boxspace);
    buffer.writeShort(buttonspacex);
    buffer.writeShort(buttonspacey);
    buffer.writeShort(panelspace);
    buffer.writeShort(panelouter);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (uiStyle[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("uiStyle:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  paneltitle: ").append(paneltitle).append("\n");
    sb.append("  grouplabel: ").append(grouplabel).append("\n");
    sb.append("  widgetlabel: ").append(widgetlabel).append("\n");
    sb.append("  widget: ").append(widget).append("\n");
    sb.append("  panelzoom: ").append(panelzoom).append("\n");
    sb.append("  minlabelchars: ").append(minlabelchars).append("\n");
    sb.append("  minwidgetchars: ").append(minwidgetchars).append("\n");
    sb.append("  columnspace: ").append(columnspace).append("\n");
    sb.append("  templatespace: ").append(templatespace).append("\n");
    sb.append("  boxspace: ").append(boxspace).append("\n");
    sb.append("  buttonspacex: ").append(buttonspacex).append("\n");
    sb.append("  buttonspacey: ").append(buttonspacey).append("\n");
    sb.append("  panelspace: ").append(panelspace).append("\n");
    sb.append("  panelouter: ").append(panelouter).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    return sb.toString();
  }
  public uiStyle copy() { try {return (uiStyle)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

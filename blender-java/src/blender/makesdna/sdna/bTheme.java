package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class bTheme extends Link<bTheme> implements DNA, Cloneable { // #186
  public bTheme[] myarray;
  public byte[] name = new byte[32]; // 1
  public ThemeUI tui = new ThemeUI(); // 624
  public ThemeSpace tbuts = new ThemeSpace(); // 376
  public ThemeSpace tv3d = new ThemeSpace(); // 376
  public ThemeSpace tfile = new ThemeSpace(); // 376
  public ThemeSpace tipo = new ThemeSpace(); // 376
  public ThemeSpace tinfo = new ThemeSpace(); // 376
  public ThemeSpace tsnd = new ThemeSpace(); // 376
  public ThemeSpace tact = new ThemeSpace(); // 376
  public ThemeSpace tnla = new ThemeSpace(); // 376
  public ThemeSpace tseq = new ThemeSpace(); // 376
  public ThemeSpace tima = new ThemeSpace(); // 376
  public ThemeSpace timasel = new ThemeSpace(); // 376
  public ThemeSpace text = new ThemeSpace(); // 376
  public ThemeSpace toops = new ThemeSpace(); // 376
  public ThemeSpace ttime = new ThemeSpace(); // 376
  public ThemeSpace tnode = new ThemeSpace(); // 376
  public ThemeSpace tlogic = new ThemeSpace(); // 376
  public ThemeSpace tuserpref = new ThemeSpace(); // 376
  public ThemeSpace tconsole = new ThemeSpace(); // 376
  public ThemeWireColor[] tarm = new ThemeWireColor[20]; // 16
  public int active_theme_area; // 4
  public int pad; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), bTheme.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), bTheme.class); // get ptr
    buffer.get(name);
    tui.read(buffer);
    tbuts.read(buffer);
    tv3d.read(buffer);
    tfile.read(buffer);
    tipo.read(buffer);
    tinfo.read(buffer);
    tsnd.read(buffer);
    tact.read(buffer);
    tnla.read(buffer);
    tseq.read(buffer);
    tima.read(buffer);
    timasel.read(buffer);
    text.read(buffer);
    toops.read(buffer);
    ttime.read(buffer);
    tnode.read(buffer);
    tlogic.read(buffer);
    tuserpref.read(buffer);
    tconsole.read(buffer);
    for(int i=0;i<tarm.length;i++) { tarm[i]=new ThemeWireColor(); tarm[i].read(buffer); }
    active_theme_area = buffer.getInt();
    pad = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(name);
    tui.write(buffer);
    tbuts.write(buffer);
    tv3d.write(buffer);
    tfile.write(buffer);
    tipo.write(buffer);
    tinfo.write(buffer);
    tsnd.write(buffer);
    tact.write(buffer);
    tnla.write(buffer);
    tseq.write(buffer);
    tima.write(buffer);
    timasel.write(buffer);
    text.write(buffer);
    toops.write(buffer);
    ttime.write(buffer);
    tnode.write(buffer);
    tlogic.write(buffer);
    tuserpref.write(buffer);
    tconsole.write(buffer);
    for(int i=0;i<tarm.length;i++) tarm[i].write(buffer);
    buffer.writeInt(active_theme_area);
    buffer.writeInt(pad);
  }
  public Object setmyarray(Object array) {
    myarray = (bTheme[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("bTheme:\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  tui: ").append(tui).append("\n");
    sb.append("  tbuts: ").append(tbuts).append("\n");
    sb.append("  tv3d: ").append(tv3d).append("\n");
    sb.append("  tfile: ").append(tfile).append("\n");
    sb.append("  tipo: ").append(tipo).append("\n");
    sb.append("  tinfo: ").append(tinfo).append("\n");
    sb.append("  tsnd: ").append(tsnd).append("\n");
    sb.append("  tact: ").append(tact).append("\n");
    sb.append("  tnla: ").append(tnla).append("\n");
    sb.append("  tseq: ").append(tseq).append("\n");
    sb.append("  tima: ").append(tima).append("\n");
    sb.append("  timasel: ").append(timasel).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  toops: ").append(toops).append("\n");
    sb.append("  ttime: ").append(ttime).append("\n");
    sb.append("  tnode: ").append(tnode).append("\n");
    sb.append("  tlogic: ").append(tlogic).append("\n");
    sb.append("  tuserpref: ").append(tuserpref).append("\n");
    sb.append("  tconsole: ").append(tconsole).append("\n");
    sb.append("  tarm: ").append(Arrays.toString(tarm)).append("\n");
    sb.append("  active_theme_area: ").append(active_theme_area).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    return sb.toString();
  }
  public bTheme copy() { try {return (bTheme)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

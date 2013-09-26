package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class wmOperator extends Link<wmOperator> implements DNA, Cloneable { // #364
  public wmOperator[] myarray;
  public byte[] idname = new byte[64]; // 1
  public IDProperty properties; // ptr 96
  public Object type; // ptr (wmOperatorType) 0
  public Object customdata; // ptr 0
  public Object py_instance; // ptr 0
  public Object ptr; // ptr (PointerRNA) 0
  public ReportList reports; // ptr 40
  public ListBase macro = new ListBase(); // 16
  public wmOperator opm; // ptr 168
  public Object layout; // ptr (uiLayout) 0
  public short flag; // 2
  public short[] pad = new short[3]; // 2

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), wmOperator.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), wmOperator.class); // get ptr
    buffer.get(idname);
    properties = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
    type = DNATools.ptr(buffer); // get ptr
    customdata = DNATools.ptr(buffer); // get ptr
    py_instance = DNATools.ptr(buffer); // get ptr
    ptr = DNATools.ptr(buffer); // get ptr
    reports = DNATools.link(DNATools.ptr(buffer), ReportList.class); // get ptr
    macro.read(buffer);
    opm = DNATools.link(DNATools.ptr(buffer), wmOperator.class); // get ptr
    layout = DNATools.ptr(buffer); // get ptr
    flag = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.write(idname);
    buffer.writeInt(properties!=null?properties.hashCode():0);
    buffer.writeInt(type!=null?type.hashCode():0);
    buffer.writeInt(customdata!=null?customdata.hashCode():0);
    buffer.writeInt(py_instance!=null?py_instance.hashCode():0);
    buffer.writeInt(ptr!=null?ptr.hashCode():0);
    buffer.writeInt(reports!=null?reports.hashCode():0);
    macro.write(buffer);
    buffer.writeInt(opm!=null?opm.hashCode():0);
    buffer.writeInt(layout!=null?layout.hashCode():0);
    buffer.writeShort(flag);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
  }
  public Object setmyarray(Object array) {
    myarray = (wmOperator[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("wmOperator:\n");
    sb.append("  idname: ").append(new String(idname)).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  customdata: ").append(customdata).append("\n");
    sb.append("  py_instance: ").append(py_instance).append("\n");
    sb.append("  ptr: ").append(ptr).append("\n");
    sb.append("  reports: ").append(reports).append("\n");
    sb.append("  macro: ").append(macro).append("\n");
    sb.append("  opm: ").append(opm).append("\n");
    sb.append("  layout: ").append(layout).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    return sb.toString();
  }
  public wmOperator copy() { try {return (wmOperator)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

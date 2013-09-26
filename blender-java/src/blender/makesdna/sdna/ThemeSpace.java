package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ThemeSpace implements DNA, Cloneable { // #184
  public ThemeSpace[] myarray;
  public byte[] back = new byte[4]; // 1
  public byte[] title = new byte[4]; // 1
  public byte[] text = new byte[4]; // 1
  public byte[] text_hi = new byte[4]; // 1
  public byte[] header = new byte[4]; // 1
  public byte[] header_title = new byte[4]; // 1
  public byte[] header_text = new byte[4]; // 1
  public byte[] header_text_hi = new byte[4]; // 1
  public byte[] button = new byte[4]; // 1
  public byte[] button_title = new byte[4]; // 1
  public byte[] button_text = new byte[4]; // 1
  public byte[] button_text_hi = new byte[4]; // 1
  public byte[] list = new byte[4]; // 1
  public byte[] list_title = new byte[4]; // 1
  public byte[] list_text = new byte[4]; // 1
  public byte[] list_text_hi = new byte[4]; // 1
  public byte[] panel = new byte[4]; // 1
  public byte[] panel_title = new byte[4]; // 1
  public byte[] panel_text = new byte[4]; // 1
  public byte[] panel_text_hi = new byte[4]; // 1
  public byte[] shade1 = new byte[4]; // 1
  public byte[] shade2 = new byte[4]; // 1
  public byte[] hilite = new byte[4]; // 1
  public byte[] grid = new byte[4]; // 1
  public byte[] wire = new byte[4]; // 1
  public byte[] select = new byte[4]; // 1
  public byte[] lamp = new byte[4]; // 1
  public byte[] active = new byte[4]; // 1
  public byte[] group = new byte[4]; // 1
  public byte[] group_active = new byte[4]; // 1
  public byte[] transform = new byte[4]; // 1
  public byte[] vertex = new byte[4]; // 1
  public byte[] vertex_select = new byte[4]; // 1
  public byte[] edge = new byte[4]; // 1
  public byte[] edge_select = new byte[4]; // 1
  public byte[] edge_seam = new byte[4]; // 1
  public byte[] edge_sharp = new byte[4]; // 1
  public byte[] edge_facesel = new byte[4]; // 1
  public byte[] edge_crease = new byte[4]; // 1
  public byte[] face = new byte[4]; // 1
  public byte[] face_select = new byte[4]; // 1
  public byte[] face_dot = new byte[4]; // 1
  public byte[] extra_edge_len = new byte[4]; // 1
  public byte[] extra_face_angle = new byte[4]; // 1
  public byte[] extra_face_area = new byte[4]; // 1
  public byte[] pad3 = new byte[4]; // 1
  public byte[] normal = new byte[4]; // 1
  public byte[] vertex_normal = new byte[4]; // 1
  public byte[] bone_solid = new byte[4]; // 1
  public byte[] bone_pose = new byte[4]; // 1
  public byte[] strip = new byte[4]; // 1
  public byte[] strip_select = new byte[4]; // 1
  public byte[] cframe = new byte[4]; // 1
  public byte[] nurb_uline = new byte[4]; // 1
  public byte[] nurb_vline = new byte[4]; // 1
  public byte[] act_spline = new byte[4]; // 1
  public byte[] nurb_sel_uline = new byte[4]; // 1
  public byte[] nurb_sel_vline = new byte[4]; // 1
  public byte[] lastsel_point = new byte[4]; // 1
  public byte[] handle_free = new byte[4]; // 1
  public byte[] handle_auto = new byte[4]; // 1
  public byte[] handle_vect = new byte[4]; // 1
  public byte[] handle_align = new byte[4]; // 1
  public byte[] handle_sel_free = new byte[4]; // 1
  public byte[] handle_sel_auto = new byte[4]; // 1
  public byte[] handle_sel_vect = new byte[4]; // 1
  public byte[] handle_sel_align = new byte[4]; // 1
  public byte[] ds_channel = new byte[4]; // 1
  public byte[] ds_subchannel = new byte[4]; // 1
  public byte[] console_output = new byte[4]; // 1
  public byte[] console_input = new byte[4]; // 1
  public byte[] console_info = new byte[4]; // 1
  public byte[] console_error = new byte[4]; // 1
  public byte[] console_cursor = new byte[4]; // 1
  public byte vertex_size; // 1
  public byte outline_width; // 1
  public byte facedot_size; // 1
  public byte bpad; // 1
  public byte[] syntaxl = new byte[4]; // 1
  public byte[] syntaxn = new byte[4]; // 1
  public byte[] syntaxb = new byte[4]; // 1
  public byte[] syntaxv = new byte[4]; // 1
  public byte[] syntaxc = new byte[4]; // 1
  public byte[] movie = new byte[4]; // 1
  public byte[] image = new byte[4]; // 1
  public byte[] scene = new byte[4]; // 1
  public byte[] audio = new byte[4]; // 1
  public byte[] effect = new byte[4]; // 1
  public byte[] plugin = new byte[4]; // 1
  public byte[] transition = new byte[4]; // 1
  public byte[] meta = new byte[4]; // 1
  public byte[] editmesh_active = new byte[4]; // 1
  public byte[] handle_vertex = new byte[4]; // 1
  public byte[] handle_vertex_select = new byte[4]; // 1
  public byte handle_vertex_size; // 1
  public byte[] hpad = new byte[7]; // 1
  public byte[] preview_back = new byte[4]; // 1

  public void read(ByteBuffer buffer) {
    buffer.get(back);
    buffer.get(title);
    buffer.get(text);
    buffer.get(text_hi);
    buffer.get(header);
    buffer.get(header_title);
    buffer.get(header_text);
    buffer.get(header_text_hi);
    buffer.get(button);
    buffer.get(button_title);
    buffer.get(button_text);
    buffer.get(button_text_hi);
    buffer.get(list);
    buffer.get(list_title);
    buffer.get(list_text);
    buffer.get(list_text_hi);
    buffer.get(panel);
    buffer.get(panel_title);
    buffer.get(panel_text);
    buffer.get(panel_text_hi);
    buffer.get(shade1);
    buffer.get(shade2);
    buffer.get(hilite);
    buffer.get(grid);
    buffer.get(wire);
    buffer.get(select);
    buffer.get(lamp);
    buffer.get(active);
    buffer.get(group);
    buffer.get(group_active);
    buffer.get(transform);
    buffer.get(vertex);
    buffer.get(vertex_select);
    buffer.get(edge);
    buffer.get(edge_select);
    buffer.get(edge_seam);
    buffer.get(edge_sharp);
    buffer.get(edge_facesel);
    buffer.get(edge_crease);
    buffer.get(face);
    buffer.get(face_select);
    buffer.get(face_dot);
    buffer.get(extra_edge_len);
    buffer.get(extra_face_angle);
    buffer.get(extra_face_area);
    buffer.get(pad3);
    buffer.get(normal);
    buffer.get(vertex_normal);
    buffer.get(bone_solid);
    buffer.get(bone_pose);
    buffer.get(strip);
    buffer.get(strip_select);
    buffer.get(cframe);
    buffer.get(nurb_uline);
    buffer.get(nurb_vline);
    buffer.get(act_spline);
    buffer.get(nurb_sel_uline);
    buffer.get(nurb_sel_vline);
    buffer.get(lastsel_point);
    buffer.get(handle_free);
    buffer.get(handle_auto);
    buffer.get(handle_vect);
    buffer.get(handle_align);
    buffer.get(handle_sel_free);
    buffer.get(handle_sel_auto);
    buffer.get(handle_sel_vect);
    buffer.get(handle_sel_align);
    buffer.get(ds_channel);
    buffer.get(ds_subchannel);
    buffer.get(console_output);
    buffer.get(console_input);
    buffer.get(console_info);
    buffer.get(console_error);
    buffer.get(console_cursor);
    vertex_size = buffer.get();
    outline_width = buffer.get();
    facedot_size = buffer.get();
    bpad = buffer.get();
    buffer.get(syntaxl);
    buffer.get(syntaxn);
    buffer.get(syntaxb);
    buffer.get(syntaxv);
    buffer.get(syntaxc);
    buffer.get(movie);
    buffer.get(image);
    buffer.get(scene);
    buffer.get(audio);
    buffer.get(effect);
    buffer.get(plugin);
    buffer.get(transition);
    buffer.get(meta);
    buffer.get(editmesh_active);
    buffer.get(handle_vertex);
    buffer.get(handle_vertex_select);
    handle_vertex_size = buffer.get();
    buffer.get(hpad);
    buffer.get(preview_back);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.write(back);
    buffer.write(title);
    buffer.write(text);
    buffer.write(text_hi);
    buffer.write(header);
    buffer.write(header_title);
    buffer.write(header_text);
    buffer.write(header_text_hi);
    buffer.write(button);
    buffer.write(button_title);
    buffer.write(button_text);
    buffer.write(button_text_hi);
    buffer.write(list);
    buffer.write(list_title);
    buffer.write(list_text);
    buffer.write(list_text_hi);
    buffer.write(panel);
    buffer.write(panel_title);
    buffer.write(panel_text);
    buffer.write(panel_text_hi);
    buffer.write(shade1);
    buffer.write(shade2);
    buffer.write(hilite);
    buffer.write(grid);
    buffer.write(wire);
    buffer.write(select);
    buffer.write(lamp);
    buffer.write(active);
    buffer.write(group);
    buffer.write(group_active);
    buffer.write(transform);
    buffer.write(vertex);
    buffer.write(vertex_select);
    buffer.write(edge);
    buffer.write(edge_select);
    buffer.write(edge_seam);
    buffer.write(edge_sharp);
    buffer.write(edge_facesel);
    buffer.write(edge_crease);
    buffer.write(face);
    buffer.write(face_select);
    buffer.write(face_dot);
    buffer.write(extra_edge_len);
    buffer.write(extra_face_angle);
    buffer.write(extra_face_area);
    buffer.write(pad3);
    buffer.write(normal);
    buffer.write(vertex_normal);
    buffer.write(bone_solid);
    buffer.write(bone_pose);
    buffer.write(strip);
    buffer.write(strip_select);
    buffer.write(cframe);
    buffer.write(nurb_uline);
    buffer.write(nurb_vline);
    buffer.write(act_spline);
    buffer.write(nurb_sel_uline);
    buffer.write(nurb_sel_vline);
    buffer.write(lastsel_point);
    buffer.write(handle_free);
    buffer.write(handle_auto);
    buffer.write(handle_vect);
    buffer.write(handle_align);
    buffer.write(handle_sel_free);
    buffer.write(handle_sel_auto);
    buffer.write(handle_sel_vect);
    buffer.write(handle_sel_align);
    buffer.write(ds_channel);
    buffer.write(ds_subchannel);
    buffer.write(console_output);
    buffer.write(console_input);
    buffer.write(console_info);
    buffer.write(console_error);
    buffer.write(console_cursor);
    buffer.writeByte(vertex_size);
    buffer.writeByte(outline_width);
    buffer.writeByte(facedot_size);
    buffer.writeByte(bpad);
    buffer.write(syntaxl);
    buffer.write(syntaxn);
    buffer.write(syntaxb);
    buffer.write(syntaxv);
    buffer.write(syntaxc);
    buffer.write(movie);
    buffer.write(image);
    buffer.write(scene);
    buffer.write(audio);
    buffer.write(effect);
    buffer.write(plugin);
    buffer.write(transition);
    buffer.write(meta);
    buffer.write(editmesh_active);
    buffer.write(handle_vertex);
    buffer.write(handle_vertex_select);
    buffer.writeByte(handle_vertex_size);
    buffer.write(hpad);
    buffer.write(preview_back);
  }
  public Object setmyarray(Object array) {
    myarray = (ThemeSpace[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ThemeSpace:\n");
    sb.append("  back: ").append(new String(back)).append("\n");
    sb.append("  title: ").append(new String(title)).append("\n");
    sb.append("  text: ").append(new String(text)).append("\n");
    sb.append("  text_hi: ").append(new String(text_hi)).append("\n");
    sb.append("  header: ").append(new String(header)).append("\n");
    sb.append("  header_title: ").append(new String(header_title)).append("\n");
    sb.append("  header_text: ").append(new String(header_text)).append("\n");
    sb.append("  header_text_hi: ").append(new String(header_text_hi)).append("\n");
    sb.append("  button: ").append(new String(button)).append("\n");
    sb.append("  button_title: ").append(new String(button_title)).append("\n");
    sb.append("  button_text: ").append(new String(button_text)).append("\n");
    sb.append("  button_text_hi: ").append(new String(button_text_hi)).append("\n");
    sb.append("  list: ").append(new String(list)).append("\n");
    sb.append("  list_title: ").append(new String(list_title)).append("\n");
    sb.append("  list_text: ").append(new String(list_text)).append("\n");
    sb.append("  list_text_hi: ").append(new String(list_text_hi)).append("\n");
    sb.append("  panel: ").append(new String(panel)).append("\n");
    sb.append("  panel_title: ").append(new String(panel_title)).append("\n");
    sb.append("  panel_text: ").append(new String(panel_text)).append("\n");
    sb.append("  panel_text_hi: ").append(new String(panel_text_hi)).append("\n");
    sb.append("  shade1: ").append(new String(shade1)).append("\n");
    sb.append("  shade2: ").append(new String(shade2)).append("\n");
    sb.append("  hilite: ").append(new String(hilite)).append("\n");
    sb.append("  grid: ").append(new String(grid)).append("\n");
    sb.append("  wire: ").append(new String(wire)).append("\n");
    sb.append("  select: ").append(new String(select)).append("\n");
    sb.append("  lamp: ").append(new String(lamp)).append("\n");
    sb.append("  active: ").append(new String(active)).append("\n");
    sb.append("  group: ").append(new String(group)).append("\n");
    sb.append("  group_active: ").append(new String(group_active)).append("\n");
    sb.append("  transform: ").append(new String(transform)).append("\n");
    sb.append("  vertex: ").append(new String(vertex)).append("\n");
    sb.append("  vertex_select: ").append(new String(vertex_select)).append("\n");
    sb.append("  edge: ").append(new String(edge)).append("\n");
    sb.append("  edge_select: ").append(new String(edge_select)).append("\n");
    sb.append("  edge_seam: ").append(new String(edge_seam)).append("\n");
    sb.append("  edge_sharp: ").append(new String(edge_sharp)).append("\n");
    sb.append("  edge_facesel: ").append(new String(edge_facesel)).append("\n");
    sb.append("  edge_crease: ").append(new String(edge_crease)).append("\n");
    sb.append("  face: ").append(new String(face)).append("\n");
    sb.append("  face_select: ").append(new String(face_select)).append("\n");
    sb.append("  face_dot: ").append(new String(face_dot)).append("\n");
    sb.append("  extra_edge_len: ").append(new String(extra_edge_len)).append("\n");
    sb.append("  extra_face_angle: ").append(new String(extra_face_angle)).append("\n");
    sb.append("  extra_face_area: ").append(new String(extra_face_area)).append("\n");
    sb.append("  pad3: ").append(new String(pad3)).append("\n");
    sb.append("  normal: ").append(new String(normal)).append("\n");
    sb.append("  vertex_normal: ").append(new String(vertex_normal)).append("\n");
    sb.append("  bone_solid: ").append(new String(bone_solid)).append("\n");
    sb.append("  bone_pose: ").append(new String(bone_pose)).append("\n");
    sb.append("  strip: ").append(new String(strip)).append("\n");
    sb.append("  strip_select: ").append(new String(strip_select)).append("\n");
    sb.append("  cframe: ").append(new String(cframe)).append("\n");
    sb.append("  nurb_uline: ").append(new String(nurb_uline)).append("\n");
    sb.append("  nurb_vline: ").append(new String(nurb_vline)).append("\n");
    sb.append("  act_spline: ").append(new String(act_spline)).append("\n");
    sb.append("  nurb_sel_uline: ").append(new String(nurb_sel_uline)).append("\n");
    sb.append("  nurb_sel_vline: ").append(new String(nurb_sel_vline)).append("\n");
    sb.append("  lastsel_point: ").append(new String(lastsel_point)).append("\n");
    sb.append("  handle_free: ").append(new String(handle_free)).append("\n");
    sb.append("  handle_auto: ").append(new String(handle_auto)).append("\n");
    sb.append("  handle_vect: ").append(new String(handle_vect)).append("\n");
    sb.append("  handle_align: ").append(new String(handle_align)).append("\n");
    sb.append("  handle_sel_free: ").append(new String(handle_sel_free)).append("\n");
    sb.append("  handle_sel_auto: ").append(new String(handle_sel_auto)).append("\n");
    sb.append("  handle_sel_vect: ").append(new String(handle_sel_vect)).append("\n");
    sb.append("  handle_sel_align: ").append(new String(handle_sel_align)).append("\n");
    sb.append("  ds_channel: ").append(new String(ds_channel)).append("\n");
    sb.append("  ds_subchannel: ").append(new String(ds_subchannel)).append("\n");
    sb.append("  console_output: ").append(new String(console_output)).append("\n");
    sb.append("  console_input: ").append(new String(console_input)).append("\n");
    sb.append("  console_info: ").append(new String(console_info)).append("\n");
    sb.append("  console_error: ").append(new String(console_error)).append("\n");
    sb.append("  console_cursor: ").append(new String(console_cursor)).append("\n");
    sb.append("  vertex_size: ").append(vertex_size).append("\n");
    sb.append("  outline_width: ").append(outline_width).append("\n");
    sb.append("  facedot_size: ").append(facedot_size).append("\n");
    sb.append("  bpad: ").append(bpad).append("\n");
    sb.append("  syntaxl: ").append(new String(syntaxl)).append("\n");
    sb.append("  syntaxn: ").append(new String(syntaxn)).append("\n");
    sb.append("  syntaxb: ").append(new String(syntaxb)).append("\n");
    sb.append("  syntaxv: ").append(new String(syntaxv)).append("\n");
    sb.append("  syntaxc: ").append(new String(syntaxc)).append("\n");
    sb.append("  movie: ").append(new String(movie)).append("\n");
    sb.append("  image: ").append(new String(image)).append("\n");
    sb.append("  scene: ").append(new String(scene)).append("\n");
    sb.append("  audio: ").append(new String(audio)).append("\n");
    sb.append("  effect: ").append(new String(effect)).append("\n");
    sb.append("  plugin: ").append(new String(plugin)).append("\n");
    sb.append("  transition: ").append(new String(transition)).append("\n");
    sb.append("  meta: ").append(new String(meta)).append("\n");
    sb.append("  editmesh_active: ").append(new String(editmesh_active)).append("\n");
    sb.append("  handle_vertex: ").append(new String(handle_vertex)).append("\n");
    sb.append("  handle_vertex_select: ").append(new String(handle_vertex_select)).append("\n");
    sb.append("  handle_vertex_size: ").append(handle_vertex_size).append("\n");
    sb.append("  hpad: ").append(new String(hpad)).append("\n");
    sb.append("  preview_back: ").append(new String(preview_back)).append("\n");
    return sb.toString();
  }
  public ThemeSpace copy() { try {return (ThemeSpace)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

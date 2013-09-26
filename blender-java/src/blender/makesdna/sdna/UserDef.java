package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class UserDef implements DNA, Cloneable { // #189
  public UserDef[] myarray;
  public int flag; // 4
  public int dupflag; // 4
  public int savetime; // 4
  public byte[] tempdir = new byte[160]; // 1
  public byte[] fontdir = new byte[160]; // 1
  public byte[] renderdir = new byte[160]; // 1
  public byte[] textudir = new byte[160]; // 1
  public byte[] plugtexdir = new byte[160]; // 1
  public byte[] plugseqdir = new byte[160]; // 1
  public byte[] pythondir = new byte[160]; // 1
  public byte[] sounddir = new byte[160]; // 1
  public byte[] image_editor = new byte[240]; // 1
  public byte[] anim_player = new byte[240]; // 1
  public int anim_player_preset; // 4
  public short v2d_min_gridsize; // 2
  public short timecode_style; // 2
  public short versions; // 2
  public short dbl_click_time; // 2
  public int gameflags; // 4
  public int wheellinescroll; // 4
  public int uiflag; // 4
  public int language; // 4
  public short userpref; // 2
  public short viewzoom; // 2
  public int mixbufsize; // 4
  public int audiodevice; // 4
  public int audiorate; // 4
  public int audioformat; // 4
  public int audiochannels; // 4
  public int scrollback; // 4
  public int dpi; // 4
  public short encoding; // 2
  public short transopts; // 2
  public short menuthreshold1; // 2
  public short menuthreshold2; // 2
  public ListBase themes = new ListBase(); // 16
  public ListBase uifonts = new ListBase(); // 16
  public ListBase uistyles = new ListBase(); // 16
  public ListBase keymaps = new ListBase(); // 16
  public ListBase addons = new ListBase(); // 16
  public byte[] keyconfigstr = new byte[64]; // 1
  public short undosteps; // 2
  public short undomemory; // 2
  public short gp_manhattendist; // 2
  public short gp_euclideandist; // 2
  public short gp_eraser; // 2
  public short gp_settings; // 2
  public short tb_leftmouse; // 2
  public short tb_rightmouse; // 2
  public SolidLight[] light = new SolidLight[3]; // 56
  public short tw_hotspot; // 2
  public short tw_flag; // 2
  public short tw_handlesize; // 2
  public short tw_size; // 2
  public short textimeout; // 2
  public short texcollectrate; // 2
  public short wmdrawmethod; // 2
  public short dragthreshold; // 2
  public int memcachelimit; // 4
  public int prefetchframes; // 4
  public short frameserverport; // 2
  public short pad_rot_angle; // 2
  public short obcenter_dia; // 2
  public short rvisize; // 2
  public short rvibright; // 2
  public short recent_files; // 2
  public short smooth_viewtx; // 2
  public short glreslimit; // 2
  public short ndof_pan; // 2
  public short ndof_rotate; // 2
  public short curssize; // 2
  public short color_picker_type; // 2
  public short ipo_new; // 2
  public short keyhandles_new; // 2
  public short scrcastfps; // 2
  public short scrcastwait; // 2
  public short pad8; // 2
  public short[] pad = new short[3]; // 2
  public byte[] versemaster = new byte[160]; // 1
  public byte[] verseuser = new byte[160]; // 1
  public float glalphaclip; // 4
  public short autokey_mode; // 2
  public short autokey_flag; // 2
  public short text_render; // 2
  public short pad9; // 2
  public float pad10; // 4
  public ColorBand coba_weight = new ColorBand(); // 776
  public float[] sculpt_paint_overlay_col = new float[3]; // 4
  public int pad3; // 4
  public byte[] author = new byte[80]; // 1

  public void read(ByteBuffer buffer) {
    flag = buffer.getInt();
    dupflag = buffer.getInt();
    savetime = buffer.getInt();
    buffer.get(tempdir);
    buffer.get(fontdir);
    buffer.get(renderdir);
    buffer.get(textudir);
    buffer.get(plugtexdir);
    buffer.get(plugseqdir);
    buffer.get(pythondir);
    buffer.get(sounddir);
    buffer.get(image_editor);
    buffer.get(anim_player);
    anim_player_preset = buffer.getInt();
    v2d_min_gridsize = buffer.getShort();
    timecode_style = buffer.getShort();
    versions = buffer.getShort();
    dbl_click_time = buffer.getShort();
    gameflags = buffer.getInt();
    wheellinescroll = buffer.getInt();
    uiflag = buffer.getInt();
    language = buffer.getInt();
    userpref = buffer.getShort();
    viewzoom = buffer.getShort();
    mixbufsize = buffer.getInt();
    audiodevice = buffer.getInt();
    audiorate = buffer.getInt();
    audioformat = buffer.getInt();
    audiochannels = buffer.getInt();
    scrollback = buffer.getInt();
    dpi = buffer.getInt();
    encoding = buffer.getShort();
    transopts = buffer.getShort();
    menuthreshold1 = buffer.getShort();
    menuthreshold2 = buffer.getShort();
    themes.read(buffer);
    uifonts.read(buffer);
    uistyles.read(buffer);
    keymaps.read(buffer);
    addons.read(buffer);
    buffer.get(keyconfigstr);
    undosteps = buffer.getShort();
    undomemory = buffer.getShort();
    gp_manhattendist = buffer.getShort();
    gp_euclideandist = buffer.getShort();
    gp_eraser = buffer.getShort();
    gp_settings = buffer.getShort();
    tb_leftmouse = buffer.getShort();
    tb_rightmouse = buffer.getShort();
    for(int i=0;i<light.length;i++) { light[i]=new SolidLight(); light[i].read(buffer); }
    tw_hotspot = buffer.getShort();
    tw_flag = buffer.getShort();
    tw_handlesize = buffer.getShort();
    tw_size = buffer.getShort();
    textimeout = buffer.getShort();
    texcollectrate = buffer.getShort();
    wmdrawmethod = buffer.getShort();
    dragthreshold = buffer.getShort();
    memcachelimit = buffer.getInt();
    prefetchframes = buffer.getInt();
    frameserverport = buffer.getShort();
    pad_rot_angle = buffer.getShort();
    obcenter_dia = buffer.getShort();
    rvisize = buffer.getShort();
    rvibright = buffer.getShort();
    recent_files = buffer.getShort();
    smooth_viewtx = buffer.getShort();
    glreslimit = buffer.getShort();
    ndof_pan = buffer.getShort();
    ndof_rotate = buffer.getShort();
    curssize = buffer.getShort();
    color_picker_type = buffer.getShort();
    ipo_new = buffer.getShort();
    keyhandles_new = buffer.getShort();
    scrcastfps = buffer.getShort();
    scrcastwait = buffer.getShort();
    pad8 = buffer.getShort();
    for(int i=0;i<pad.length;i++) pad[i]=buffer.getShort();
    buffer.get(versemaster);
    buffer.get(verseuser);
    glalphaclip = buffer.getFloat();
    autokey_mode = buffer.getShort();
    autokey_flag = buffer.getShort();
    text_render = buffer.getShort();
    pad9 = buffer.getShort();
    pad10 = buffer.getFloat();
    coba_weight.read(buffer);
    for(int i=0;i<sculpt_paint_overlay_col.length;i++) sculpt_paint_overlay_col[i]=buffer.getFloat();
    pad3 = buffer.getInt();
    buffer.get(author);
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(flag);
    buffer.writeInt(dupflag);
    buffer.writeInt(savetime);
    buffer.write(tempdir);
    buffer.write(fontdir);
    buffer.write(renderdir);
    buffer.write(textudir);
    buffer.write(plugtexdir);
    buffer.write(plugseqdir);
    buffer.write(pythondir);
    buffer.write(sounddir);
    buffer.write(image_editor);
    buffer.write(anim_player);
    buffer.writeInt(anim_player_preset);
    buffer.writeShort(v2d_min_gridsize);
    buffer.writeShort(timecode_style);
    buffer.writeShort(versions);
    buffer.writeShort(dbl_click_time);
    buffer.writeInt(gameflags);
    buffer.writeInt(wheellinescroll);
    buffer.writeInt(uiflag);
    buffer.writeInt(language);
    buffer.writeShort(userpref);
    buffer.writeShort(viewzoom);
    buffer.writeInt(mixbufsize);
    buffer.writeInt(audiodevice);
    buffer.writeInt(audiorate);
    buffer.writeInt(audioformat);
    buffer.writeInt(audiochannels);
    buffer.writeInt(scrollback);
    buffer.writeInt(dpi);
    buffer.writeShort(encoding);
    buffer.writeShort(transopts);
    buffer.writeShort(menuthreshold1);
    buffer.writeShort(menuthreshold2);
    themes.write(buffer);
    uifonts.write(buffer);
    uistyles.write(buffer);
    keymaps.write(buffer);
    addons.write(buffer);
    buffer.write(keyconfigstr);
    buffer.writeShort(undosteps);
    buffer.writeShort(undomemory);
    buffer.writeShort(gp_manhattendist);
    buffer.writeShort(gp_euclideandist);
    buffer.writeShort(gp_eraser);
    buffer.writeShort(gp_settings);
    buffer.writeShort(tb_leftmouse);
    buffer.writeShort(tb_rightmouse);
    for(int i=0;i<light.length;i++) light[i].write(buffer);
    buffer.writeShort(tw_hotspot);
    buffer.writeShort(tw_flag);
    buffer.writeShort(tw_handlesize);
    buffer.writeShort(tw_size);
    buffer.writeShort(textimeout);
    buffer.writeShort(texcollectrate);
    buffer.writeShort(wmdrawmethod);
    buffer.writeShort(dragthreshold);
    buffer.writeInt(memcachelimit);
    buffer.writeInt(prefetchframes);
    buffer.writeShort(frameserverport);
    buffer.writeShort(pad_rot_angle);
    buffer.writeShort(obcenter_dia);
    buffer.writeShort(rvisize);
    buffer.writeShort(rvibright);
    buffer.writeShort(recent_files);
    buffer.writeShort(smooth_viewtx);
    buffer.writeShort(glreslimit);
    buffer.writeShort(ndof_pan);
    buffer.writeShort(ndof_rotate);
    buffer.writeShort(curssize);
    buffer.writeShort(color_picker_type);
    buffer.writeShort(ipo_new);
    buffer.writeShort(keyhandles_new);
    buffer.writeShort(scrcastfps);
    buffer.writeShort(scrcastwait);
    buffer.writeShort(pad8);
    for(int i=0;i<pad.length;i++) buffer.writeShort(pad[i]);
    buffer.write(versemaster);
    buffer.write(verseuser);
    buffer.writeFloat(glalphaclip);
    buffer.writeShort(autokey_mode);
    buffer.writeShort(autokey_flag);
    buffer.writeShort(text_render);
    buffer.writeShort(pad9);
    buffer.writeFloat(pad10);
    coba_weight.write(buffer);
    for(int i=0;i<sculpt_paint_overlay_col.length;i++) buffer.writeFloat(sculpt_paint_overlay_col[i]);
    buffer.writeInt(pad3);
    buffer.write(author);
  }
  public Object setmyarray(Object array) {
    myarray = (UserDef[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("UserDef:\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  dupflag: ").append(dupflag).append("\n");
    sb.append("  savetime: ").append(savetime).append("\n");
    sb.append("  tempdir: ").append(new String(tempdir)).append("\n");
    sb.append("  fontdir: ").append(new String(fontdir)).append("\n");
    sb.append("  renderdir: ").append(new String(renderdir)).append("\n");
    sb.append("  textudir: ").append(new String(textudir)).append("\n");
    sb.append("  plugtexdir: ").append(new String(plugtexdir)).append("\n");
    sb.append("  plugseqdir: ").append(new String(plugseqdir)).append("\n");
    sb.append("  pythondir: ").append(new String(pythondir)).append("\n");
    sb.append("  sounddir: ").append(new String(sounddir)).append("\n");
    sb.append("  image_editor: ").append(new String(image_editor)).append("\n");
    sb.append("  anim_player: ").append(new String(anim_player)).append("\n");
    sb.append("  anim_player_preset: ").append(anim_player_preset).append("\n");
    sb.append("  v2d_min_gridsize: ").append(v2d_min_gridsize).append("\n");
    sb.append("  timecode_style: ").append(timecode_style).append("\n");
    sb.append("  versions: ").append(versions).append("\n");
    sb.append("  dbl_click_time: ").append(dbl_click_time).append("\n");
    sb.append("  gameflags: ").append(gameflags).append("\n");
    sb.append("  wheellinescroll: ").append(wheellinescroll).append("\n");
    sb.append("  uiflag: ").append(uiflag).append("\n");
    sb.append("  language: ").append(language).append("\n");
    sb.append("  userpref: ").append(userpref).append("\n");
    sb.append("  viewzoom: ").append(viewzoom).append("\n");
    sb.append("  mixbufsize: ").append(mixbufsize).append("\n");
    sb.append("  audiodevice: ").append(audiodevice).append("\n");
    sb.append("  audiorate: ").append(audiorate).append("\n");
    sb.append("  audioformat: ").append(audioformat).append("\n");
    sb.append("  audiochannels: ").append(audiochannels).append("\n");
    sb.append("  scrollback: ").append(scrollback).append("\n");
    sb.append("  dpi: ").append(dpi).append("\n");
    sb.append("  encoding: ").append(encoding).append("\n");
    sb.append("  transopts: ").append(transopts).append("\n");
    sb.append("  menuthreshold1: ").append(menuthreshold1).append("\n");
    sb.append("  menuthreshold2: ").append(menuthreshold2).append("\n");
    sb.append("  themes: ").append(themes).append("\n");
    sb.append("  uifonts: ").append(uifonts).append("\n");
    sb.append("  uistyles: ").append(uistyles).append("\n");
    sb.append("  keymaps: ").append(keymaps).append("\n");
    sb.append("  addons: ").append(addons).append("\n");
    sb.append("  keyconfigstr: ").append(new String(keyconfigstr)).append("\n");
    sb.append("  undosteps: ").append(undosteps).append("\n");
    sb.append("  undomemory: ").append(undomemory).append("\n");
    sb.append("  gp_manhattendist: ").append(gp_manhattendist).append("\n");
    sb.append("  gp_euclideandist: ").append(gp_euclideandist).append("\n");
    sb.append("  gp_eraser: ").append(gp_eraser).append("\n");
    sb.append("  gp_settings: ").append(gp_settings).append("\n");
    sb.append("  tb_leftmouse: ").append(tb_leftmouse).append("\n");
    sb.append("  tb_rightmouse: ").append(tb_rightmouse).append("\n");
    sb.append("  light: ").append(Arrays.toString(light)).append("\n");
    sb.append("  tw_hotspot: ").append(tw_hotspot).append("\n");
    sb.append("  tw_flag: ").append(tw_flag).append("\n");
    sb.append("  tw_handlesize: ").append(tw_handlesize).append("\n");
    sb.append("  tw_size: ").append(tw_size).append("\n");
    sb.append("  textimeout: ").append(textimeout).append("\n");
    sb.append("  texcollectrate: ").append(texcollectrate).append("\n");
    sb.append("  wmdrawmethod: ").append(wmdrawmethod).append("\n");
    sb.append("  dragthreshold: ").append(dragthreshold).append("\n");
    sb.append("  memcachelimit: ").append(memcachelimit).append("\n");
    sb.append("  prefetchframes: ").append(prefetchframes).append("\n");
    sb.append("  frameserverport: ").append(frameserverport).append("\n");
    sb.append("  pad_rot_angle: ").append(pad_rot_angle).append("\n");
    sb.append("  obcenter_dia: ").append(obcenter_dia).append("\n");
    sb.append("  rvisize: ").append(rvisize).append("\n");
    sb.append("  rvibright: ").append(rvibright).append("\n");
    sb.append("  recent_files: ").append(recent_files).append("\n");
    sb.append("  smooth_viewtx: ").append(smooth_viewtx).append("\n");
    sb.append("  glreslimit: ").append(glreslimit).append("\n");
    sb.append("  ndof_pan: ").append(ndof_pan).append("\n");
    sb.append("  ndof_rotate: ").append(ndof_rotate).append("\n");
    sb.append("  curssize: ").append(curssize).append("\n");
    sb.append("  color_picker_type: ").append(color_picker_type).append("\n");
    sb.append("  ipo_new: ").append(ipo_new).append("\n");
    sb.append("  keyhandles_new: ").append(keyhandles_new).append("\n");
    sb.append("  scrcastfps: ").append(scrcastfps).append("\n");
    sb.append("  scrcastwait: ").append(scrcastwait).append("\n");
    sb.append("  pad8: ").append(pad8).append("\n");
    sb.append("  pad: ").append(Arrays.toString(pad)).append("\n");
    sb.append("  versemaster: ").append(new String(versemaster)).append("\n");
    sb.append("  verseuser: ").append(new String(verseuser)).append("\n");
    sb.append("  glalphaclip: ").append(glalphaclip).append("\n");
    sb.append("  autokey_mode: ").append(autokey_mode).append("\n");
    sb.append("  autokey_flag: ").append(autokey_flag).append("\n");
    sb.append("  text_render: ").append(text_render).append("\n");
    sb.append("  pad9: ").append(pad9).append("\n");
    sb.append("  pad10: ").append(pad10).append("\n");
    sb.append("  coba_weight: ").append(coba_weight).append("\n");
    sb.append("  sculpt_paint_overlay_col: ").append(Arrays.toString(sculpt_paint_overlay_col)).append("\n");
    sb.append("  pad3: ").append(pad3).append("\n");
    sb.append("  author: ").append(new String(author)).append("\n");
    return sb.toString();
  }
  public UserDef copy() { try {return (UserDef)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

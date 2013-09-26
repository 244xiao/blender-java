package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Scene extends ID implements DNA, Cloneable { // #151
  public Scene[] myarray;
  public ID id = (ID)this;
  public AnimData adt; // ptr 96
  public bObject camera; // ptr 1296
  public World world; // ptr 480
  public Scene set; // ptr 1552
  public ListBase base = new ListBase(); // 16
  public Base basact; // ptr 40
  public bObject obedit; // ptr 1296
  public float[] cursor = new float[3]; // 4
  public float[] twcent = new float[3]; // 4
  public float[] twmin = new float[3]; // 4
  public float[] twmax = new float[3]; // 4
  public int lay; // 4
  public int layact; // 4
  public int lay_updated; // 4
  public int customdata_mask; // 4
  public int customdata_mask_modal; // 4
  public short flag; // 2
  public short use_nodes; // 2
  public bNodeTree nodetree; // ptr 264
  public Editing ed; // ptr 592
  public ToolSettings toolsettings; // ptr 448
  public Object stats; // ptr (SceneStats) 0
  public RenderData r = new RenderData(); // 1024
  public AudioData audio = new AudioData(); // 24
  public ListBase markers = new ListBase(); // 16
  public ListBase transform_spaces = new ListBase(); // 16
  public Object sound_scene; // ptr 0
  public Object sound_scene_handle; // ptr 0
  public Object sound_scrub_handle; // ptr 0
  public Object fps_info; // ptr 0
  public Object theDag; // ptr (DagForest) 0
  public short dagisvalid; // 2
  public short dagflags; // 2
  public short recalc; // 2
  public short pad6; // 2
  public int pad5; // 4
  public int active_keyingset; // 4
  public ListBase keyingsets = new ListBase(); // 16
  public GameFraming framing = new GameFraming(); // 16
  public GameData gm = new GameData(); // 104
  public UnitSettings unit = new UnitSettings(); // 8
  public bGPdata gpd; // ptr 104
  public PhysicsSettings physics_settings = new PhysicsSettings(); // 24

  public void read(ByteBuffer buffer) {
    super.read(buffer);
    adt = DNATools.link(DNATools.ptr(buffer), AnimData.class); // get ptr
    camera = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    world = DNATools.link(DNATools.ptr(buffer), World.class); // get ptr
    set = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    base.read(buffer);
    basact = DNATools.link(DNATools.ptr(buffer), Base.class); // get ptr
    obedit = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    for(int i=0;i<cursor.length;i++) cursor[i]=buffer.getFloat();
    for(int i=0;i<twcent.length;i++) twcent[i]=buffer.getFloat();
    for(int i=0;i<twmin.length;i++) twmin[i]=buffer.getFloat();
    for(int i=0;i<twmax.length;i++) twmax[i]=buffer.getFloat();
    lay = buffer.getInt();
    layact = buffer.getInt();
    lay_updated = buffer.getInt();
    customdata_mask = buffer.getInt();
    customdata_mask_modal = buffer.getInt();
    flag = buffer.getShort();
    use_nodes = buffer.getShort();
    nodetree = DNATools.link(DNATools.ptr(buffer), bNodeTree.class); // get ptr
    ed = DNATools.link(DNATools.ptr(buffer), Editing.class); // get ptr
    toolsettings = DNATools.link(DNATools.ptr(buffer), ToolSettings.class); // get ptr
    stats = DNATools.ptr(buffer); // get ptr
    r.read(buffer);
    audio.read(buffer);
    markers.read(buffer);
    transform_spaces.read(buffer);
    sound_scene = DNATools.ptr(buffer); // get ptr
    sound_scene_handle = DNATools.ptr(buffer); // get ptr
    sound_scrub_handle = DNATools.ptr(buffer); // get ptr
    fps_info = DNATools.ptr(buffer); // get ptr
    theDag = DNATools.ptr(buffer); // get ptr
    dagisvalid = buffer.getShort();
    dagflags = buffer.getShort();
    recalc = buffer.getShort();
    pad6 = buffer.getShort();
    pad5 = buffer.getInt();
    active_keyingset = buffer.getInt();
    keyingsets.read(buffer);
    framing.read(buffer);
    gm.read(buffer);
    unit.read(buffer);
    gpd = DNATools.link(DNATools.ptr(buffer), bGPdata.class); // get ptr
    physics_settings.read(buffer);
  }
  public void write(DataOutput buffer) throws IOException {
    super.write(buffer);
    buffer.writeInt(adt!=null?adt.hashCode():0);
    buffer.writeInt(camera!=null?camera.hashCode():0);
    buffer.writeInt(world!=null?world.hashCode():0);
    buffer.writeInt(set!=null?set.hashCode():0);
    base.write(buffer);
    buffer.writeInt(basact!=null?basact.hashCode():0);
    buffer.writeInt(obedit!=null?obedit.hashCode():0);
    for(int i=0;i<cursor.length;i++) buffer.writeFloat(cursor[i]);
    for(int i=0;i<twcent.length;i++) buffer.writeFloat(twcent[i]);
    for(int i=0;i<twmin.length;i++) buffer.writeFloat(twmin[i]);
    for(int i=0;i<twmax.length;i++) buffer.writeFloat(twmax[i]);
    buffer.writeInt(lay);
    buffer.writeInt(layact);
    buffer.writeInt(lay_updated);
    buffer.writeInt(customdata_mask);
    buffer.writeInt(customdata_mask_modal);
    buffer.writeShort(flag);
    buffer.writeShort(use_nodes);
    buffer.writeInt(nodetree!=null?nodetree.hashCode():0);
    buffer.writeInt(ed!=null?ed.hashCode():0);
    buffer.writeInt(toolsettings!=null?toolsettings.hashCode():0);
    buffer.writeInt(stats!=null?stats.hashCode():0);
    r.write(buffer);
    audio.write(buffer);
    markers.write(buffer);
    transform_spaces.write(buffer);
    buffer.writeInt(sound_scene!=null?sound_scene.hashCode():0);
    buffer.writeInt(sound_scene_handle!=null?sound_scene_handle.hashCode():0);
    buffer.writeInt(sound_scrub_handle!=null?sound_scrub_handle.hashCode():0);
    buffer.writeInt(fps_info!=null?fps_info.hashCode():0);
    buffer.writeInt(theDag!=null?theDag.hashCode():0);
    buffer.writeShort(dagisvalid);
    buffer.writeShort(dagflags);
    buffer.writeShort(recalc);
    buffer.writeShort(pad6);
    buffer.writeInt(pad5);
    buffer.writeInt(active_keyingset);
    keyingsets.write(buffer);
    framing.write(buffer);
    gm.write(buffer);
    unit.write(buffer);
    buffer.writeInt(gpd!=null?gpd.hashCode():0);
    physics_settings.write(buffer);
  }
  public Object setmyarray(Object array) {
    myarray = (Scene[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Scene:\n");
    sb.append(super.toString());
    sb.append("  adt: ").append(adt).append("\n");
    sb.append("  camera: ").append(camera).append("\n");
    sb.append("  world: ").append(world).append("\n");
    sb.append("  set: ").append(set).append("\n");
    sb.append("  base: ").append(base).append("\n");
    sb.append("  basact: ").append(basact).append("\n");
    sb.append("  obedit: ").append(obedit).append("\n");
    sb.append("  cursor: ").append(Arrays.toString(cursor)).append("\n");
    sb.append("  twcent: ").append(Arrays.toString(twcent)).append("\n");
    sb.append("  twmin: ").append(Arrays.toString(twmin)).append("\n");
    sb.append("  twmax: ").append(Arrays.toString(twmax)).append("\n");
    sb.append("  lay: ").append(lay).append("\n");
    sb.append("  layact: ").append(layact).append("\n");
    sb.append("  lay_updated: ").append(lay_updated).append("\n");
    sb.append("  customdata_mask: ").append(customdata_mask).append("\n");
    sb.append("  customdata_mask_modal: ").append(customdata_mask_modal).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  use_nodes: ").append(use_nodes).append("\n");
    sb.append("  nodetree: ").append(nodetree).append("\n");
    sb.append("  ed: ").append(ed).append("\n");
    sb.append("  toolsettings: ").append(toolsettings).append("\n");
    sb.append("  stats: ").append(stats).append("\n");
    sb.append("  r: ").append(r).append("\n");
    sb.append("  audio: ").append(audio).append("\n");
    sb.append("  markers: ").append(markers).append("\n");
    sb.append("  transform_spaces: ").append(transform_spaces).append("\n");
    sb.append("  sound_scene: ").append(sound_scene).append("\n");
    sb.append("  sound_scene_handle: ").append(sound_scene_handle).append("\n");
    sb.append("  sound_scrub_handle: ").append(sound_scrub_handle).append("\n");
    sb.append("  fps_info: ").append(fps_info).append("\n");
    sb.append("  theDag: ").append(theDag).append("\n");
    sb.append("  dagisvalid: ").append(dagisvalid).append("\n");
    sb.append("  dagflags: ").append(dagflags).append("\n");
    sb.append("  recalc: ").append(recalc).append("\n");
    sb.append("  pad6: ").append(pad6).append("\n");
    sb.append("  pad5: ").append(pad5).append("\n");
    sb.append("  active_keyingset: ").append(active_keyingset).append("\n");
    sb.append("  keyingsets: ").append(keyingsets).append("\n");
    sb.append("  framing: ").append(framing).append("\n");
    sb.append("  gm: ").append(gm).append("\n");
    sb.append("  unit: ").append(unit).append("\n");
    sb.append("  gpd: ").append(gpd).append("\n");
    sb.append("  physics_settings: ").append(physics_settings).append("\n");
    return sb.toString();
  }
  public Scene copy() { try {return (Scene)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

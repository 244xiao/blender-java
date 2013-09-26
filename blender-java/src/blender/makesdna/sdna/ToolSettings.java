package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ToolSettings implements DNA, Cloneable { // #147
  public ToolSettings[] myarray;
  public VPaint vpaint; // ptr 56
  public VPaint wpaint; // ptr 56
  public Sculpt sculpt; // ptr 96
  public float vgroup_weight; // 4
  public short cornertype; // 2
  public short editbutflag; // 2
  public float jointrilimit; // 4
  public float degr; // 4
  public short step; // 2
  public short turn; // 2
  public float extr_offs; // 4
  public float doublimit; // 4
  public float normalsize; // 4
  public short automerge; // 2
  public short selectmode; // 2
  public short segments; // 2
  public short rings; // 2
  public short vertices; // 2
  public short unwrapper; // 2
  public float uvcalc_radius; // 4
  public float uvcalc_cubesize; // 4
  public float uvcalc_margin; // 4
  public short uvcalc_mapdir; // 2
  public short uvcalc_mapalign; // 2
  public short uvcalc_flag; // 2
  public short uv_flag; // 2
  public short uv_selectmode; // 2
  public short uv_pad; // 2
  public short gpencil_flags; // 2
  public short autoik_chainlen; // 2
  public ImagePaintSettings imapaint = new ImagePaintSettings(); // 48
  public ParticleEditSettings particle = new ParticleEditSettings(); // 168
  public float proportional_size; // 4
  public float select_thresh; // 4
  public float clean_thresh; // 4
  public short autokey_mode; // 2
  public short autokey_flag; // 2
  public byte retopo_mode; // 1
  public byte retopo_paint_tool; // 1
  public byte line_div; // 1
  public byte ellipse_div; // 1
  public byte retopo_hotspot; // 1
  public byte multires_subdiv_type; // 1
  public short skgen_resolution; // 2
  public float skgen_threshold_internal; // 4
  public float skgen_threshold_external; // 4
  public float skgen_length_ratio; // 4
  public float skgen_length_limit; // 4
  public float skgen_angle_limit; // 4
  public float skgen_correlation_limit; // 4
  public float skgen_symmetry_limit; // 4
  public float skgen_retarget_angle_weight; // 4
  public float skgen_retarget_length_weight; // 4
  public float skgen_retarget_distance_weight; // 4
  public short skgen_options; // 2
  public byte skgen_postpro; // 1
  public byte skgen_postpro_passes; // 1
  public byte[] skgen_subdivisions = new byte[3]; // 1
  public byte skgen_multi_level; // 1
  public bObject skgen_template; // ptr 1296
  public byte bone_sketching; // 1
  public byte bone_sketching_convert; // 1
  public byte skgen_subdivision_number; // 1
  public byte skgen_retarget_options; // 1
  public byte skgen_retarget_roll; // 1
  public byte[] skgen_side_string = new byte[8]; // 1
  public byte[] skgen_num_string = new byte[8]; // 1
  public byte edge_mode; // 1
  public byte edge_mode_live_unwrap; // 1
  public byte snap_mode; // 1
  public short snap_flag; // 2
  public short snap_target; // 2
  public short proportional; // 2
  public short prop_mode; // 2
  public byte proportional_objects; // 1
  public byte[] pad = new byte[3]; // 1
  public int auto_normalize; // 4
  public short sculpt_paint_settings; // 2
  public short pad1; // 2
  public int sculpt_paint_unified_size; // 4
  public float sculpt_paint_unified_unprojected_radius; // 4
  public float sculpt_paint_unified_alpha; // 4

  public void read(ByteBuffer buffer) {
    vpaint = DNATools.link(DNATools.ptr(buffer), VPaint.class); // get ptr
    wpaint = DNATools.link(DNATools.ptr(buffer), VPaint.class); // get ptr
    sculpt = DNATools.link(DNATools.ptr(buffer), Sculpt.class); // get ptr
    vgroup_weight = buffer.getFloat();
    cornertype = buffer.getShort();
    editbutflag = buffer.getShort();
    jointrilimit = buffer.getFloat();
    degr = buffer.getFloat();
    step = buffer.getShort();
    turn = buffer.getShort();
    extr_offs = buffer.getFloat();
    doublimit = buffer.getFloat();
    normalsize = buffer.getFloat();
    automerge = buffer.getShort();
    selectmode = buffer.getShort();
    segments = buffer.getShort();
    rings = buffer.getShort();
    vertices = buffer.getShort();
    unwrapper = buffer.getShort();
    uvcalc_radius = buffer.getFloat();
    uvcalc_cubesize = buffer.getFloat();
    uvcalc_margin = buffer.getFloat();
    uvcalc_mapdir = buffer.getShort();
    uvcalc_mapalign = buffer.getShort();
    uvcalc_flag = buffer.getShort();
    uv_flag = buffer.getShort();
    uv_selectmode = buffer.getShort();
    uv_pad = buffer.getShort();
    gpencil_flags = buffer.getShort();
    autoik_chainlen = buffer.getShort();
    imapaint.read(buffer);
    particle.read(buffer);
    proportional_size = buffer.getFloat();
    select_thresh = buffer.getFloat();
    clean_thresh = buffer.getFloat();
    autokey_mode = buffer.getShort();
    autokey_flag = buffer.getShort();
    retopo_mode = buffer.get();
    retopo_paint_tool = buffer.get();
    line_div = buffer.get();
    ellipse_div = buffer.get();
    retopo_hotspot = buffer.get();
    multires_subdiv_type = buffer.get();
    skgen_resolution = buffer.getShort();
    skgen_threshold_internal = buffer.getFloat();
    skgen_threshold_external = buffer.getFloat();
    skgen_length_ratio = buffer.getFloat();
    skgen_length_limit = buffer.getFloat();
    skgen_angle_limit = buffer.getFloat();
    skgen_correlation_limit = buffer.getFloat();
    skgen_symmetry_limit = buffer.getFloat();
    skgen_retarget_angle_weight = buffer.getFloat();
    skgen_retarget_length_weight = buffer.getFloat();
    skgen_retarget_distance_weight = buffer.getFloat();
    skgen_options = buffer.getShort();
    skgen_postpro = buffer.get();
    skgen_postpro_passes = buffer.get();
    buffer.get(skgen_subdivisions);
    skgen_multi_level = buffer.get();
    skgen_template = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    bone_sketching = buffer.get();
    bone_sketching_convert = buffer.get();
    skgen_subdivision_number = buffer.get();
    skgen_retarget_options = buffer.get();
    skgen_retarget_roll = buffer.get();
    buffer.get(skgen_side_string);
    buffer.get(skgen_num_string);
    edge_mode = buffer.get();
    edge_mode_live_unwrap = buffer.get();
    snap_mode = buffer.get();
    snap_flag = buffer.getShort();
    snap_target = buffer.getShort();
    proportional = buffer.getShort();
    prop_mode = buffer.getShort();
    proportional_objects = buffer.get();
    buffer.get(pad);
    auto_normalize = buffer.getInt();
    sculpt_paint_settings = buffer.getShort();
    pad1 = buffer.getShort();
    sculpt_paint_unified_size = buffer.getInt();
    sculpt_paint_unified_unprojected_radius = buffer.getFloat();
    sculpt_paint_unified_alpha = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(vpaint!=null?vpaint.hashCode():0);
    buffer.writeInt(wpaint!=null?wpaint.hashCode():0);
    buffer.writeInt(sculpt!=null?sculpt.hashCode():0);
    buffer.writeFloat(vgroup_weight);
    buffer.writeShort(cornertype);
    buffer.writeShort(editbutflag);
    buffer.writeFloat(jointrilimit);
    buffer.writeFloat(degr);
    buffer.writeShort(step);
    buffer.writeShort(turn);
    buffer.writeFloat(extr_offs);
    buffer.writeFloat(doublimit);
    buffer.writeFloat(normalsize);
    buffer.writeShort(automerge);
    buffer.writeShort(selectmode);
    buffer.writeShort(segments);
    buffer.writeShort(rings);
    buffer.writeShort(vertices);
    buffer.writeShort(unwrapper);
    buffer.writeFloat(uvcalc_radius);
    buffer.writeFloat(uvcalc_cubesize);
    buffer.writeFloat(uvcalc_margin);
    buffer.writeShort(uvcalc_mapdir);
    buffer.writeShort(uvcalc_mapalign);
    buffer.writeShort(uvcalc_flag);
    buffer.writeShort(uv_flag);
    buffer.writeShort(uv_selectmode);
    buffer.writeShort(uv_pad);
    buffer.writeShort(gpencil_flags);
    buffer.writeShort(autoik_chainlen);
    imapaint.write(buffer);
    particle.write(buffer);
    buffer.writeFloat(proportional_size);
    buffer.writeFloat(select_thresh);
    buffer.writeFloat(clean_thresh);
    buffer.writeShort(autokey_mode);
    buffer.writeShort(autokey_flag);
    buffer.writeByte(retopo_mode);
    buffer.writeByte(retopo_paint_tool);
    buffer.writeByte(line_div);
    buffer.writeByte(ellipse_div);
    buffer.writeByte(retopo_hotspot);
    buffer.writeByte(multires_subdiv_type);
    buffer.writeShort(skgen_resolution);
    buffer.writeFloat(skgen_threshold_internal);
    buffer.writeFloat(skgen_threshold_external);
    buffer.writeFloat(skgen_length_ratio);
    buffer.writeFloat(skgen_length_limit);
    buffer.writeFloat(skgen_angle_limit);
    buffer.writeFloat(skgen_correlation_limit);
    buffer.writeFloat(skgen_symmetry_limit);
    buffer.writeFloat(skgen_retarget_angle_weight);
    buffer.writeFloat(skgen_retarget_length_weight);
    buffer.writeFloat(skgen_retarget_distance_weight);
    buffer.writeShort(skgen_options);
    buffer.writeByte(skgen_postpro);
    buffer.writeByte(skgen_postpro_passes);
    buffer.write(skgen_subdivisions);
    buffer.writeByte(skgen_multi_level);
    buffer.writeInt(skgen_template!=null?skgen_template.hashCode():0);
    buffer.writeByte(bone_sketching);
    buffer.writeByte(bone_sketching_convert);
    buffer.writeByte(skgen_subdivision_number);
    buffer.writeByte(skgen_retarget_options);
    buffer.writeByte(skgen_retarget_roll);
    buffer.write(skgen_side_string);
    buffer.write(skgen_num_string);
    buffer.writeByte(edge_mode);
    buffer.writeByte(edge_mode_live_unwrap);
    buffer.writeByte(snap_mode);
    buffer.writeShort(snap_flag);
    buffer.writeShort(snap_target);
    buffer.writeShort(proportional);
    buffer.writeShort(prop_mode);
    buffer.writeByte(proportional_objects);
    buffer.write(pad);
    buffer.writeInt(auto_normalize);
    buffer.writeShort(sculpt_paint_settings);
    buffer.writeShort(pad1);
    buffer.writeInt(sculpt_paint_unified_size);
    buffer.writeFloat(sculpt_paint_unified_unprojected_radius);
    buffer.writeFloat(sculpt_paint_unified_alpha);
  }
  public Object setmyarray(Object array) {
    myarray = (ToolSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ToolSettings:\n");
    sb.append("  vpaint: ").append(vpaint).append("\n");
    sb.append("  wpaint: ").append(wpaint).append("\n");
    sb.append("  sculpt: ").append(sculpt).append("\n");
    sb.append("  vgroup_weight: ").append(vgroup_weight).append("\n");
    sb.append("  cornertype: ").append(cornertype).append("\n");
    sb.append("  editbutflag: ").append(editbutflag).append("\n");
    sb.append("  jointrilimit: ").append(jointrilimit).append("\n");
    sb.append("  degr: ").append(degr).append("\n");
    sb.append("  step: ").append(step).append("\n");
    sb.append("  turn: ").append(turn).append("\n");
    sb.append("  extr_offs: ").append(extr_offs).append("\n");
    sb.append("  doublimit: ").append(doublimit).append("\n");
    sb.append("  normalsize: ").append(normalsize).append("\n");
    sb.append("  automerge: ").append(automerge).append("\n");
    sb.append("  selectmode: ").append(selectmode).append("\n");
    sb.append("  segments: ").append(segments).append("\n");
    sb.append("  rings: ").append(rings).append("\n");
    sb.append("  vertices: ").append(vertices).append("\n");
    sb.append("  unwrapper: ").append(unwrapper).append("\n");
    sb.append("  uvcalc_radius: ").append(uvcalc_radius).append("\n");
    sb.append("  uvcalc_cubesize: ").append(uvcalc_cubesize).append("\n");
    sb.append("  uvcalc_margin: ").append(uvcalc_margin).append("\n");
    sb.append("  uvcalc_mapdir: ").append(uvcalc_mapdir).append("\n");
    sb.append("  uvcalc_mapalign: ").append(uvcalc_mapalign).append("\n");
    sb.append("  uvcalc_flag: ").append(uvcalc_flag).append("\n");
    sb.append("  uv_flag: ").append(uv_flag).append("\n");
    sb.append("  uv_selectmode: ").append(uv_selectmode).append("\n");
    sb.append("  uv_pad: ").append(uv_pad).append("\n");
    sb.append("  gpencil_flags: ").append(gpencil_flags).append("\n");
    sb.append("  autoik_chainlen: ").append(autoik_chainlen).append("\n");
    sb.append("  imapaint: ").append(imapaint).append("\n");
    sb.append("  particle: ").append(particle).append("\n");
    sb.append("  proportional_size: ").append(proportional_size).append("\n");
    sb.append("  select_thresh: ").append(select_thresh).append("\n");
    sb.append("  clean_thresh: ").append(clean_thresh).append("\n");
    sb.append("  autokey_mode: ").append(autokey_mode).append("\n");
    sb.append("  autokey_flag: ").append(autokey_flag).append("\n");
    sb.append("  retopo_mode: ").append(retopo_mode).append("\n");
    sb.append("  retopo_paint_tool: ").append(retopo_paint_tool).append("\n");
    sb.append("  line_div: ").append(line_div).append("\n");
    sb.append("  ellipse_div: ").append(ellipse_div).append("\n");
    sb.append("  retopo_hotspot: ").append(retopo_hotspot).append("\n");
    sb.append("  multires_subdiv_type: ").append(multires_subdiv_type).append("\n");
    sb.append("  skgen_resolution: ").append(skgen_resolution).append("\n");
    sb.append("  skgen_threshold_internal: ").append(skgen_threshold_internal).append("\n");
    sb.append("  skgen_threshold_external: ").append(skgen_threshold_external).append("\n");
    sb.append("  skgen_length_ratio: ").append(skgen_length_ratio).append("\n");
    sb.append("  skgen_length_limit: ").append(skgen_length_limit).append("\n");
    sb.append("  skgen_angle_limit: ").append(skgen_angle_limit).append("\n");
    sb.append("  skgen_correlation_limit: ").append(skgen_correlation_limit).append("\n");
    sb.append("  skgen_symmetry_limit: ").append(skgen_symmetry_limit).append("\n");
    sb.append("  skgen_retarget_angle_weight: ").append(skgen_retarget_angle_weight).append("\n");
    sb.append("  skgen_retarget_length_weight: ").append(skgen_retarget_length_weight).append("\n");
    sb.append("  skgen_retarget_distance_weight: ").append(skgen_retarget_distance_weight).append("\n");
    sb.append("  skgen_options: ").append(skgen_options).append("\n");
    sb.append("  skgen_postpro: ").append(skgen_postpro).append("\n");
    sb.append("  skgen_postpro_passes: ").append(skgen_postpro_passes).append("\n");
    sb.append("  skgen_subdivisions: ").append(new String(skgen_subdivisions)).append("\n");
    sb.append("  skgen_multi_level: ").append(skgen_multi_level).append("\n");
    sb.append("  skgen_template: ").append(skgen_template).append("\n");
    sb.append("  bone_sketching: ").append(bone_sketching).append("\n");
    sb.append("  bone_sketching_convert: ").append(bone_sketching_convert).append("\n");
    sb.append("  skgen_subdivision_number: ").append(skgen_subdivision_number).append("\n");
    sb.append("  skgen_retarget_options: ").append(skgen_retarget_options).append("\n");
    sb.append("  skgen_retarget_roll: ").append(skgen_retarget_roll).append("\n");
    sb.append("  skgen_side_string: ").append(new String(skgen_side_string)).append("\n");
    sb.append("  skgen_num_string: ").append(new String(skgen_num_string)).append("\n");
    sb.append("  edge_mode: ").append(edge_mode).append("\n");
    sb.append("  edge_mode_live_unwrap: ").append(edge_mode_live_unwrap).append("\n");
    sb.append("  snap_mode: ").append(snap_mode).append("\n");
    sb.append("  snap_flag: ").append(snap_flag).append("\n");
    sb.append("  snap_target: ").append(snap_target).append("\n");
    sb.append("  proportional: ").append(proportional).append("\n");
    sb.append("  prop_mode: ").append(prop_mode).append("\n");
    sb.append("  proportional_objects: ").append(proportional_objects).append("\n");
    sb.append("  pad: ").append(new String(pad)).append("\n");
    sb.append("  auto_normalize: ").append(auto_normalize).append("\n");
    sb.append("  sculpt_paint_settings: ").append(sculpt_paint_settings).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    sb.append("  sculpt_paint_unified_size: ").append(sculpt_paint_unified_size).append("\n");
    sb.append("  sculpt_paint_unified_unprojected_radius: ").append(sculpt_paint_unified_unprojected_radius).append("\n");
    sb.append("  sculpt_paint_unified_alpha: ").append(sculpt_paint_unified_alpha).append("\n");
    return sb.toString();
  }
  public ToolSettings copy() { try {return (ToolSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

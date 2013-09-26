package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Sequence extends Link<Sequence> implements DNA, Cloneable { // #204
  public Sequence[] myarray;
  public Object tmp; // ptr 0
  public Object lib; // ptr 0
  public byte[] name = new byte[24]; // 1
  public int flag; // 4
  public int type; // 4
  public int len; // 4
  public int start; // 4
  public int startofs; // 4
  public int endofs; // 4
  public int startstill; // 4
  public int endstill; // 4
  public int machine; // 4
  public int depth; // 4
  public int startdisp; // 4
  public int enddisp; // 4
  public float sat; // 4
  public float pad; // 4
  public float mul; // 4
  public float handsize; // 4
  public int sfra; // 4
  public int anim_preseek; // 4
  public Strip strip; // ptr 240
  public Ipo ipo; // ptr 112
  public Scene scene; // ptr 1552
  public bObject scene_camera; // ptr 1296
  public Object anim; // ptr (anim) 0
  public float effect_fader; // 4
  public float speed_fader; // 4
  public PluginSeq plugin; // ptr 456
  public Sequence seq1; // ptr 288
  public Sequence seq2; // ptr 288
  public Sequence seq3; // ptr 288
  public ListBase seqbase = new ListBase(); // 16
  public bSound sound; // ptr 392
  public Object scene_sound; // ptr 0
  public float volume; // 4
  public float level; // 4
  public float pan; // 4
  public int scenenr; // 4
  public int multicam_source; // 4
  public float strobe; // 4
  public Object effectdata; // ptr 0
  public int anim_startofs; // 4
  public int anim_endofs; // 4
  public int blend_mode; // 4
  public float blend_opacity; // 4

  public void read(ByteBuffer buffer) {
    next = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    prev = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    tmp = DNATools.ptr(buffer); // get ptr
    lib = DNATools.ptr(buffer); // get ptr
    buffer.get(name);
    flag = buffer.getInt();
    type = buffer.getInt();
    len = buffer.getInt();
    start = buffer.getInt();
    startofs = buffer.getInt();
    endofs = buffer.getInt();
    startstill = buffer.getInt();
    endstill = buffer.getInt();
    machine = buffer.getInt();
    depth = buffer.getInt();
    startdisp = buffer.getInt();
    enddisp = buffer.getInt();
    sat = buffer.getFloat();
    pad = buffer.getFloat();
    mul = buffer.getFloat();
    handsize = buffer.getFloat();
    sfra = buffer.getInt();
    anim_preseek = buffer.getInt();
    strip = DNATools.link(DNATools.ptr(buffer), Strip.class); // get ptr
    ipo = DNATools.link(DNATools.ptr(buffer), Ipo.class); // get ptr
    scene = DNATools.link(DNATools.ptr(buffer), Scene.class); // get ptr
    scene_camera = DNATools.link(DNATools.ptr(buffer), bObject.class); // get ptr
    anim = DNATools.ptr(buffer); // get ptr
    effect_fader = buffer.getFloat();
    speed_fader = buffer.getFloat();
    plugin = DNATools.link(DNATools.ptr(buffer), PluginSeq.class); // get ptr
    seq1 = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    seq2 = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    seq3 = DNATools.link(DNATools.ptr(buffer), Sequence.class); // get ptr
    seqbase.read(buffer);
    sound = DNATools.link(DNATools.ptr(buffer), bSound.class); // get ptr
    scene_sound = DNATools.ptr(buffer); // get ptr
    volume = buffer.getFloat();
    level = buffer.getFloat();
    pan = buffer.getFloat();
    scenenr = buffer.getInt();
    multicam_source = buffer.getInt();
    strobe = buffer.getFloat();
    effectdata = DNATools.ptr(buffer); // get ptr
    anim_startofs = buffer.getInt();
    anim_endofs = buffer.getInt();
    blend_mode = buffer.getInt();
    blend_opacity = buffer.getFloat();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(next!=null?next.hashCode():0);
    buffer.writeInt(prev!=null?prev.hashCode():0);
    buffer.writeInt(tmp!=null?tmp.hashCode():0);
    buffer.writeInt(lib!=null?lib.hashCode():0);
    buffer.write(name);
    buffer.writeInt(flag);
    buffer.writeInt(type);
    buffer.writeInt(len);
    buffer.writeInt(start);
    buffer.writeInt(startofs);
    buffer.writeInt(endofs);
    buffer.writeInt(startstill);
    buffer.writeInt(endstill);
    buffer.writeInt(machine);
    buffer.writeInt(depth);
    buffer.writeInt(startdisp);
    buffer.writeInt(enddisp);
    buffer.writeFloat(sat);
    buffer.writeFloat(pad);
    buffer.writeFloat(mul);
    buffer.writeFloat(handsize);
    buffer.writeInt(sfra);
    buffer.writeInt(anim_preseek);
    buffer.writeInt(strip!=null?strip.hashCode():0);
    buffer.writeInt(ipo!=null?ipo.hashCode():0);
    buffer.writeInt(scene!=null?scene.hashCode():0);
    buffer.writeInt(scene_camera!=null?scene_camera.hashCode():0);
    buffer.writeInt(anim!=null?anim.hashCode():0);
    buffer.writeFloat(effect_fader);
    buffer.writeFloat(speed_fader);
    buffer.writeInt(plugin!=null?plugin.hashCode():0);
    buffer.writeInt(seq1!=null?seq1.hashCode():0);
    buffer.writeInt(seq2!=null?seq2.hashCode():0);
    buffer.writeInt(seq3!=null?seq3.hashCode():0);
    seqbase.write(buffer);
    buffer.writeInt(sound!=null?sound.hashCode():0);
    buffer.writeInt(scene_sound!=null?scene_sound.hashCode():0);
    buffer.writeFloat(volume);
    buffer.writeFloat(level);
    buffer.writeFloat(pan);
    buffer.writeInt(scenenr);
    buffer.writeInt(multicam_source);
    buffer.writeFloat(strobe);
    buffer.writeInt(effectdata!=null?effectdata.hashCode():0);
    buffer.writeInt(anim_startofs);
    buffer.writeInt(anim_endofs);
    buffer.writeInt(blend_mode);
    buffer.writeFloat(blend_opacity);
  }
  public Object setmyarray(Object array) {
    myarray = (Sequence[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("Sequence:\n");
    sb.append("  tmp: ").append(tmp).append("\n");
    sb.append("  lib: ").append(lib).append("\n");
    sb.append("  name: ").append(new String(name)).append("\n");
    sb.append("  flag: ").append(flag).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  len: ").append(len).append("\n");
    sb.append("  start: ").append(start).append("\n");
    sb.append("  startofs: ").append(startofs).append("\n");
    sb.append("  endofs: ").append(endofs).append("\n");
    sb.append("  startstill: ").append(startstill).append("\n");
    sb.append("  endstill: ").append(endstill).append("\n");
    sb.append("  machine: ").append(machine).append("\n");
    sb.append("  depth: ").append(depth).append("\n");
    sb.append("  startdisp: ").append(startdisp).append("\n");
    sb.append("  enddisp: ").append(enddisp).append("\n");
    sb.append("  sat: ").append(sat).append("\n");
    sb.append("  pad: ").append(pad).append("\n");
    sb.append("  mul: ").append(mul).append("\n");
    sb.append("  handsize: ").append(handsize).append("\n");
    sb.append("  sfra: ").append(sfra).append("\n");
    sb.append("  anim_preseek: ").append(anim_preseek).append("\n");
    sb.append("  strip: ").append(strip).append("\n");
    sb.append("  ipo: ").append(ipo).append("\n");
    sb.append("  scene: ").append(scene).append("\n");
    sb.append("  scene_camera: ").append(scene_camera).append("\n");
    sb.append("  anim: ").append(anim).append("\n");
    sb.append("  effect_fader: ").append(effect_fader).append("\n");
    sb.append("  speed_fader: ").append(speed_fader).append("\n");
    sb.append("  plugin: ").append(plugin).append("\n");
    sb.append("  seq1: ").append(seq1).append("\n");
    sb.append("  seq2: ").append(seq2).append("\n");
    sb.append("  seq3: ").append(seq3).append("\n");
    sb.append("  seqbase: ").append(seqbase).append("\n");
    sb.append("  sound: ").append(sound).append("\n");
    sb.append("  scene_sound: ").append(scene_sound).append("\n");
    sb.append("  volume: ").append(volume).append("\n");
    sb.append("  level: ").append(level).append("\n");
    sb.append("  pan: ").append(pan).append("\n");
    sb.append("  scenenr: ").append(scenenr).append("\n");
    sb.append("  multicam_source: ").append(multicam_source).append("\n");
    sb.append("  strobe: ").append(strobe).append("\n");
    sb.append("  effectdata: ").append(effectdata).append("\n");
    sb.append("  anim_startofs: ").append(anim_startofs).append("\n");
    sb.append("  anim_endofs: ").append(anim_endofs).append("\n");
    sb.append("  blend_mode: ").append(blend_mode).append("\n");
    sb.append("  blend_opacity: ").append(blend_opacity).append("\n");
    return sb.toString();
  }
  public Sequence copy() { try {return (Sequence)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

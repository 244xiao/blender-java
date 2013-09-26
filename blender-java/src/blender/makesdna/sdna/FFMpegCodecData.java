package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class FFMpegCodecData implements DNA, Cloneable { // #131
  public FFMpegCodecData[] myarray;
  public int type; // 4
  public int codec; // 4
  public int audio_codec; // 4
  public int video_bitrate; // 4
  public int audio_bitrate; // 4
  public int audio_mixrate; // 4
  public float audio_volume; // 4
  public int gop_size; // 4
  public int flags; // 4
  public int rc_min_rate; // 4
  public int rc_max_rate; // 4
  public int rc_buffer_size; // 4
  public int mux_packet_size; // 4
  public int mux_rate; // 4
  public IDProperty properties; // ptr 96

  public void read(ByteBuffer buffer) {
    type = buffer.getInt();
    codec = buffer.getInt();
    audio_codec = buffer.getInt();
    video_bitrate = buffer.getInt();
    audio_bitrate = buffer.getInt();
    audio_mixrate = buffer.getInt();
    audio_volume = buffer.getFloat();
    gop_size = buffer.getInt();
    flags = buffer.getInt();
    rc_min_rate = buffer.getInt();
    rc_max_rate = buffer.getInt();
    rc_buffer_size = buffer.getInt();
    mux_packet_size = buffer.getInt();
    mux_rate = buffer.getInt();
    properties = DNATools.link(DNATools.ptr(buffer), IDProperty.class); // get ptr
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(type);
    buffer.writeInt(codec);
    buffer.writeInt(audio_codec);
    buffer.writeInt(video_bitrate);
    buffer.writeInt(audio_bitrate);
    buffer.writeInt(audio_mixrate);
    buffer.writeFloat(audio_volume);
    buffer.writeInt(gop_size);
    buffer.writeInt(flags);
    buffer.writeInt(rc_min_rate);
    buffer.writeInt(rc_max_rate);
    buffer.writeInt(rc_buffer_size);
    buffer.writeInt(mux_packet_size);
    buffer.writeInt(mux_rate);
    buffer.writeInt(properties!=null?properties.hashCode():0);
  }
  public Object setmyarray(Object array) {
    myarray = (FFMpegCodecData[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("FFMpegCodecData:\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  codec: ").append(codec).append("\n");
    sb.append("  audio_codec: ").append(audio_codec).append("\n");
    sb.append("  video_bitrate: ").append(video_bitrate).append("\n");
    sb.append("  audio_bitrate: ").append(audio_bitrate).append("\n");
    sb.append("  audio_mixrate: ").append(audio_mixrate).append("\n");
    sb.append("  audio_volume: ").append(audio_volume).append("\n");
    sb.append("  gop_size: ").append(gop_size).append("\n");
    sb.append("  flags: ").append(flags).append("\n");
    sb.append("  rc_min_rate: ").append(rc_min_rate).append("\n");
    sb.append("  rc_max_rate: ").append(rc_max_rate).append("\n");
    sb.append("  rc_buffer_size: ").append(rc_buffer_size).append("\n");
    sb.append("  mux_packet_size: ").append(mux_packet_size).append("\n");
    sb.append("  mux_rate: ").append(mux_rate).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    return sb.toString();
  }
  public FFMpegCodecData copy() { try {return (FFMpegCodecData)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class QuicktimeCodecSettings implements DNA, Cloneable { // #130
  public QuicktimeCodecSettings[] myarray;
  public int codecType; // 4
  public int codecSpatialQuality; // 4
  public int codec; // 4
  public int codecFlags; // 4
  public int colorDepth; // 4
  public int codecTemporalQuality; // 4
  public int minSpatialQuality; // 4
  public int minTemporalQuality; // 4
  public int keyFrameRate; // 4
  public int bitRate; // 4
  public int audiocodecType; // 4
  public int audioSampleRate; // 4
  public short audioBitDepth; // 2
  public short audioChannels; // 2
  public int audioCodecFlags; // 4
  public int audioBitRate; // 4
  public int pad1; // 4

  public void read(ByteBuffer buffer) {
    codecType = buffer.getInt();
    codecSpatialQuality = buffer.getInt();
    codec = buffer.getInt();
    codecFlags = buffer.getInt();
    colorDepth = buffer.getInt();
    codecTemporalQuality = buffer.getInt();
    minSpatialQuality = buffer.getInt();
    minTemporalQuality = buffer.getInt();
    keyFrameRate = buffer.getInt();
    bitRate = buffer.getInt();
    audiocodecType = buffer.getInt();
    audioSampleRate = buffer.getInt();
    audioBitDepth = buffer.getShort();
    audioChannels = buffer.getShort();
    audioCodecFlags = buffer.getInt();
    audioBitRate = buffer.getInt();
    pad1 = buffer.getInt();
  }
  public void write(DataOutput buffer) throws IOException {
    buffer.writeInt(codecType);
    buffer.writeInt(codecSpatialQuality);
    buffer.writeInt(codec);
    buffer.writeInt(codecFlags);
    buffer.writeInt(colorDepth);
    buffer.writeInt(codecTemporalQuality);
    buffer.writeInt(minSpatialQuality);
    buffer.writeInt(minTemporalQuality);
    buffer.writeInt(keyFrameRate);
    buffer.writeInt(bitRate);
    buffer.writeInt(audiocodecType);
    buffer.writeInt(audioSampleRate);
    buffer.writeShort(audioBitDepth);
    buffer.writeShort(audioChannels);
    buffer.writeInt(audioCodecFlags);
    buffer.writeInt(audioBitRate);
    buffer.writeInt(pad1);
  }
  public Object setmyarray(Object array) {
    myarray = (QuicktimeCodecSettings[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("QuicktimeCodecSettings:\n");
    sb.append("  codecType: ").append(codecType).append("\n");
    sb.append("  codecSpatialQuality: ").append(codecSpatialQuality).append("\n");
    sb.append("  codec: ").append(codec).append("\n");
    sb.append("  codecFlags: ").append(codecFlags).append("\n");
    sb.append("  colorDepth: ").append(colorDepth).append("\n");
    sb.append("  codecTemporalQuality: ").append(codecTemporalQuality).append("\n");
    sb.append("  minSpatialQuality: ").append(minSpatialQuality).append("\n");
    sb.append("  minTemporalQuality: ").append(minTemporalQuality).append("\n");
    sb.append("  keyFrameRate: ").append(keyFrameRate).append("\n");
    sb.append("  bitRate: ").append(bitRate).append("\n");
    sb.append("  audiocodecType: ").append(audiocodecType).append("\n");
    sb.append("  audioSampleRate: ").append(audioSampleRate).append("\n");
    sb.append("  audioBitDepth: ").append(audioBitDepth).append("\n");
    sb.append("  audioChannels: ").append(audioChannels).append("\n");
    sb.append("  audioCodecFlags: ").append(audioCodecFlags).append("\n");
    sb.append("  audioBitRate: ").append(audioBitRate).append("\n");
    sb.append("  pad1: ").append(pad1).append("\n");
    return sb.toString();
  }
  public QuicktimeCodecSettings copy() { try {return (QuicktimeCodecSettings)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

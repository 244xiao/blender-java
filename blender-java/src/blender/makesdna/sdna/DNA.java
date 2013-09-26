package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;

public interface DNA {
  public void read(ByteBuffer buffer);
  public void write(DataOutput buffer) throws IOException;
  public Object setmyarray(Object array);
  public Object getmyarray();
}


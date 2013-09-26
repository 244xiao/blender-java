package blender.makesdna.sdna;

import java.nio.ByteBuffer;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ThemeUI implements DNA, Cloneable { // #183
  public ThemeUI[] myarray;
  public uiWidgetColors wcol_regular = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_tool = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_text = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_radio = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_option = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_toggle = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_num = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_numslider = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_menu = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_pulldown = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_menu_back = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_menu_item = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_box = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_scroll = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_progress = new uiWidgetColors(); // 32
  public uiWidgetColors wcol_list_item = new uiWidgetColors(); // 32
  public uiWidgetStateColors wcol_state = new uiWidgetStateColors(); // 32
  public byte[] iconfile = new byte[80]; // 1

  public void read(ByteBuffer buffer) {
    wcol_regular.read(buffer);
    wcol_tool.read(buffer);
    wcol_text.read(buffer);
    wcol_radio.read(buffer);
    wcol_option.read(buffer);
    wcol_toggle.read(buffer);
    wcol_num.read(buffer);
    wcol_numslider.read(buffer);
    wcol_menu.read(buffer);
    wcol_pulldown.read(buffer);
    wcol_menu_back.read(buffer);
    wcol_menu_item.read(buffer);
    wcol_box.read(buffer);
    wcol_scroll.read(buffer);
    wcol_progress.read(buffer);
    wcol_list_item.read(buffer);
    wcol_state.read(buffer);
    buffer.get(iconfile);
  }
  public void write(DataOutput buffer) throws IOException {
    wcol_regular.write(buffer);
    wcol_tool.write(buffer);
    wcol_text.write(buffer);
    wcol_radio.write(buffer);
    wcol_option.write(buffer);
    wcol_toggle.write(buffer);
    wcol_num.write(buffer);
    wcol_numslider.write(buffer);
    wcol_menu.write(buffer);
    wcol_pulldown.write(buffer);
    wcol_menu_back.write(buffer);
    wcol_menu_item.write(buffer);
    wcol_box.write(buffer);
    wcol_scroll.write(buffer);
    wcol_progress.write(buffer);
    wcol_list_item.write(buffer);
    wcol_state.write(buffer);
    buffer.write(iconfile);
  }
  public Object setmyarray(Object array) {
    myarray = (ThemeUI[])array;
    return this;
  }
  public Object getmyarray() {
    return myarray;
  }
  public String toString() {
    StringBuilder sb = new StringBuilder("ThemeUI:\n");
    sb.append("  wcol_regular: ").append(wcol_regular).append("\n");
    sb.append("  wcol_tool: ").append(wcol_tool).append("\n");
    sb.append("  wcol_text: ").append(wcol_text).append("\n");
    sb.append("  wcol_radio: ").append(wcol_radio).append("\n");
    sb.append("  wcol_option: ").append(wcol_option).append("\n");
    sb.append("  wcol_toggle: ").append(wcol_toggle).append("\n");
    sb.append("  wcol_num: ").append(wcol_num).append("\n");
    sb.append("  wcol_numslider: ").append(wcol_numslider).append("\n");
    sb.append("  wcol_menu: ").append(wcol_menu).append("\n");
    sb.append("  wcol_pulldown: ").append(wcol_pulldown).append("\n");
    sb.append("  wcol_menu_back: ").append(wcol_menu_back).append("\n");
    sb.append("  wcol_menu_item: ").append(wcol_menu_item).append("\n");
    sb.append("  wcol_box: ").append(wcol_box).append("\n");
    sb.append("  wcol_scroll: ").append(wcol_scroll).append("\n");
    sb.append("  wcol_progress: ").append(wcol_progress).append("\n");
    sb.append("  wcol_list_item: ").append(wcol_list_item).append("\n");
    sb.append("  wcol_state: ").append(wcol_state).append("\n");
    sb.append("  iconfile: ").append(new String(iconfile)).append("\n");
    return sb.toString();
  }
  public ThemeUI copy() { try {return (ThemeUI)super.clone();} catch(CloneNotSupportedException ex) {return null;} }
}

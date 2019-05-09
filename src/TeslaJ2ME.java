import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/*
 * TeslaJ2ME
 */
public class TeslaJ2ME extends MIDlet {
  private Display display = null;
  private MainCanvas mainCanvas = null;
  private static Image backgroundImage = null;
  private int width;
  private int height;
  private String keyLabel;
  private SpecialFont specialFont = new SpecialFont();
  private Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
  private final int LGRAY = 0x666666;
  private final int DGRAY = 0x2c2c2d;
  private final int BLACK = 0x000000;
  private final int WHITE = 0xFFFFFF;
  private final int GREEN = 0x008800;

  // vehicle_state
  private String vehicle_name = "Black Beauty";
  // charge_state
  private int battery_level = 65;
  private float battery_range = 151.8f;
  private String battery_range_str = "152 mph";
  // drive_state
  private int speed = 65;
  private String speed_str = "65  mph";

  public TeslaJ2ME() {
    display = Display.getDisplay(this);
    mainCanvas = new MainCanvas(this);
  }

  public void startApp() throws MIDletStateChangeException {
    display.setCurrent(mainCanvas);
  }

  public void pauseApp() {}

  protected void destroyApp(boolean unconditional)
      throws MIDletStateChangeException {}

 /*
  * Main Canvas
  */
  class MainCanvas extends Canvas {
    private TeslaJ2ME parent = null;

    public MainCanvas(TeslaJ2ME parent) {
      this.parent = parent;
      this.setFullScreenMode(true);
      width = getWidth();
      height = getHeight();
      try {
        backgroundImage = Image.createImage ("/background.png");
      } catch (Exception ex) {
      }
    }

    public void bailout() {
      try {
        destroyApp(true);
        notifyDestroyed();
      } catch (MIDletStateChangeException e) {
        e.printStackTrace();
      }
    }

    public void keyPressed(int keyCode){
      int value = keyCode - 48;
      keyLabel = getKeyName(keyCode).toUpperCase();
      if (keyLabel.equals("SELECT")) {
        // nothing yet
      } else if (keyLabel.equals("SOFT1")) {
        // nothing yet
      } else if (keyLabel.equals("SOFT2")) {
        bailout();
      }
      this.repaint();
    }

    public void paint(Graphics g) {
      g.setColor(BLACK);
      g.fillRect(0, 0, width, height);
      g.drawImage(backgroundImage, width / 2, height / 2, Graphics.HCENTER | Graphics.VCENTER);
      g.setColor(WHITE);

      specialFont.letters(g, vehicle_name, (width - specialFont.lettersWidth(vehicle_name)) / 2, 20);
      // progress
      g.setColor(DGRAY);
      g.fillRect(59, 48, 52, 16);
      g.setColor(GREEN);
      g.fillRect(60, 49, 30, 14);
      // battery
      g.setColor(LGRAY);
      g.drawRoundRect(58, 47, 53, 17, 5, 5);
      g.drawRect(111, 53, 2, 5);
      g.setColor(DGRAY);
      g.fillRect(111, 54, 2, 4);
      // range
      g.setColor(WHITE);
      specialFont.numbers(g, battery_range_str, width / 2, 45);
      g.setFont(largeFont);
      g.drawString("mi", 166, 50, Graphics.LEFT | Graphics.TOP);
      // status
      g.drawString("Driving", width / 2, 70, Graphics.HCENTER | Graphics.TOP);
      g.drawString(speed_str, width / 2, 88, Graphics.HCENTER | Graphics.TOP);

      Toolbar.drawMenuIcon(g, 18, height - 20);
      Toolbar.drawBackIcon(g, width - 18, height - 22);
    }

  }
}

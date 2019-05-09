import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/*
 * TeslaJ2ME
 */
public class TeslaJ2ME extends MIDlet {
  private Display display = null;
  private MainCanvas mainCanvas = null;
  private static Image backgroundImage = null;
  private static Image drivingImage = null;
  private static Image chargingImage = null;
  private int width;
  private int height;
  private String keyLabel;
  private SpecialFont specialFont = new SpecialFont();
  private Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
  private boolean toggle;
  private final int LGRAY = 0x666666;
  private final int DGRAY = 0x2c2c2d;
  private final int BLACK = 0x000000;
  private final int WHITE = 0xFFFFFF;
  private final int GREEN = 0x008800;

  // vehicle_state
  private String vehicle_name = "Black Beauty";
  // charge_state
  private int battery_level;
  private double battery_range;
  // drive_state
  private int speed;

  private String display_state;
  private String battery_range_str;
  private String speed_str;
  private int battery_full_px = 50;
  private int battery_percent;

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
        drivingImage = Image.createImage ("/driving.png");
        chargingImage = Image.createImage ("/charging.png");
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
        toggle = toggle ? false : true;
      } else if (keyLabel.equals("SOFT1")) {
        // nothing yet
      } else if (keyLabel.equals("SOFT2")) {
        bailout();
      }
      this.repaint();
    }

    public void paint(Graphics g) {
      if (toggle) {
        display_state = "Driving";
        backgroundImage = drivingImage;
        battery_level = 65;
        battery_range = 151.8;
        speed = 65;
      } else {
        display_state = "Charging Complete";
        backgroundImage = chargingImage;
        battery_level = 90;
        battery_range = 215.0;
        speed = 0; // null
      }
      battery_range_str = String.valueOf((int) Math.floor(battery_range));
      battery_percent = (int) Math.floor(((double) (battery_level) / 100) * battery_full_px);
      speed_str = "" + speed + " mph";

      g.setColor(BLACK);
      g.fillRect(0, 0, width, height);
      g.drawImage(backgroundImage, width / 2, height / 2, Graphics.HCENTER | Graphics.VCENTER);
      g.setColor(WHITE);

      specialFont.letters(g, vehicle_name, (width - specialFont.lettersWidth(vehicle_name)) / 2, 15);
      // progress
      g.setColor(DGRAY);
      g.fillRect(59, 48, battery_full_px + 2, 16);
      g.setColor(GREEN);
      g.fillRect(60, 49, battery_percent, 14);
      // battery
      g.setColor(LGRAY);
      g.drawRoundRect(58, 47, 53, 17, 5, 5);
      g.drawRect(111, 53, 2, 5);
      g.setColor(DGRAY);
      g.fillRect(111, 54, 2, 4);
      // range
      g.setColor(WHITE);
      int range_width = specialFont.lettersWidth(battery_range_str);
      specialFont.numbers(g, battery_range_str, width / 2, 45);
      g.setFont(largeFont);
      g.drawString("mi",  width / 2 + range_width + 9, 50, Graphics.LEFT | Graphics.TOP);
      // status
      g.drawString(display_state, width / 2, 72, Graphics.HCENTER | Graphics.TOP);
      if (toggle) {
        g.drawString(speed_str, width / 2, 92, Graphics.HCENTER | Graphics.TOP);
      }

      Toolbar.drawMenuIcon(g, 18, height - 20);
      Toolbar.drawBackIcon(g, width - 18, height - 22);
    }

  }
}

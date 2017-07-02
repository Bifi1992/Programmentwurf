import javafx.scene.paint.Color;


/**
 * Created by y.brisch on 08.06.17.
 */
public final class RocketConstants {

  /**
   * prohibit instantiation
   */
  private RocketConstants() {
  }

  /**
   * holds the maximum speed for a successful landing of all rockets in [m/s]
   */
  public static final int MAX_LANDING_SPEED = 20;

  /**
   * holds the initial coordinates of all rockets
   */
  public static final Coordinate2D INIT_COORDINATES = new Coordinate2D(0, 0);

  /**
   * holds the initial acceleration of all rockets
   */
  public static final Coordinate2D INIT_ACCELERATION = new Coordinate2D(0, 0);

  /**
   * holds the minimal initial distance of a rocket
   */
  public static final double MIN_INIT_DIST = 1000000;

  /**
   * holds the maximal initial distance of a rocket
   */
  public static final double MAX_INIT_DIST = 2000000;

  /**
   * holds the minimal initial fuel level of a rocket
   */
  public static final double MIN_INIT_FUEL_LEVEL = 1500;

  /**
   * holds the maximal initial fuel level of a rocket
   */
  public static final double MAX_INIT_FUEL_LEVEL = 2500;

  /**
   * holds the fuel usage per acceleration in [l / (m/s^2)]
   */
  public static final double FUEL_PER_ACCELERATION = 0.002;

  /**
   * holds the default number of rockets per generation for {@link Interface}.mPopSizeDropDown
   */
  public static final int ROCKETS_PER_GENERATION = 6;

  /**
   * holds the rocket's initial speed in x dir
   */
  public static final double INIT_SPEED_X = 1000;

  /**
   * holds the rocket's initial speed in y dir
   */
  public static final double INIT_SPEED_Y = 100;

  /**
   * holds a number of colors for the rockets with their corresponding rgb values as string
   */
  public static final Object[][] COLOR_PALETTE =
      {
          new Object[]{Color.rgb(0, 117, 220, 1), "0,117,220"},   // "#0075DC"}, // Blue
          new Object[]{Color.rgb(43, 206, 72, 1), "43,206,72"},   // "#2BCE48"}, // Green
          new Object[]{Color.rgb(255, 164, 5, 1), "255,164,5"},   // "#FFA405"}, // Orpiment
          new Object[]{Color.rgb(153, 63, 0, 1), "153,63,0"},   // "#993F00"}, // Caramel
          new Object[]{Color.rgb(194, 0, 136, 1), "194,0,136"},   // "#C20088"}, // Mallow
          new Object[]{Color.rgb(116, 10, 255, 1), "116,10,255"},   // "#740AFF"}, // Violet
          new Object[]{Color.rgb(143, 124, 0, 1), "143,124,0"},   // "#8F7C00"}, // Khaki
          new Object[]{Color.rgb(157, 204, 0, 1), "157,204,0"},   // "#9DCC00"}, // Lime
          new Object[]{Color.rgb(255, 168, 187, 1), "255,168,187"},   // "#FFA8BB"}, // Pink
          new Object[]{Color.rgb(255, 255, 0, 1), "255,255,0"},   // "#FFFF00"}, // Yellow
          new Object[]{Color.rgb(66, 102, 0, 1), "66,102,0"},   // "#426600"}, // Quagmire
          new Object[]{Color.rgb(255, 0, 16, 1), "255,0,16"},   // "#FF0010"}, // Red
          new Object[]{Color.rgb(94, 241, 242, 1), "94,241,242"},   // "#5EF1F2"}, // Sky
          new Object[]{Color.rgb(0, 153, 143, 1), "0,153,143"},   // "#00998F"}, // Turquoise
          new Object[]{Color.rgb(148, 255, 181, 1), "148,255,181"},   // "#94FFB5"}, // Jade
          new Object[]{Color.rgb(255, 204, 153, 1), "255,204,153"},   // "#FFCC99"}, // Hone
          new Object[]{Color.rgb(240, 163, 255, 1), "240,163,255"},   // "#F0A3FF"}, // Amethystydew
          new Object[]{Color.rgb(224, 255, 102, 1), "224,255,102"},   // "#E0FF66"}, // Uranium
          new Object[]{Color.rgb(153, 0, 0, 1), "153,0,0"},   // "#990000"}, // Wine
          new Object[]{Color.rgb(255, 255, 128, 1), "255,255,128"},   // "#FFFF80"}, // Xanthin
          new Object[]{Color.rgb(255, 255, 0, 1), "255,255,0"},   // "#FFFF00"}, // Yellow
          new Object[]{Color.rgb(25, 25, 25, 1), "25,25,25"},   // "#191919"}, // Ebony
          new Object[]{Color.rgb(76, 0, 92, 1), "76,0,92"},   // "#4C005C"}, // Damson
          new Object[]{Color.rgb(128, 128, 128, 1), "128,128,128"},   // "#808080"}, // Iron
          new Object[]{Color.rgb(0, 51, 128, 1), "0,51,128"},   // "#003380"}, // Navy
          new Object[]{Color.rgb(255, 80, 5, 1), "255,80,5"},   // "#FF5005"}, // Zinnia
          new Object[]{Color.rgb(0, 92, 49, 1), "0,92,49"}
      }; // "#005C31"} // Forest
}

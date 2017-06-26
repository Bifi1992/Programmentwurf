import javafx.scene.paint.Color;


/**
 * Created by y.brisch on 08.06.17.
 */
public final class RocketConstants {

  /**
   * prohibit instantiation
   */
  private RocketConstants() {}

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
   * holds the number of rockets per generation
   */
  public static final int ROCKETS_PER_GENERATION = 4;

  /**
   * holds the rocket's initial speed in x dir
   */
  public static final double INIT_SPEED_X = 100;

  /**
   * holds the rocket's initial speed in y dir
   */
  public static final double INIT_SPEED_Y = 10;

  /**
   * holds a number of colors for the rockets
   */
  public static final Color[] COLOR_PALETTE = {
      Color.rgb(240,163,255, 1),  // #F0A3FF Amethyst
      Color.rgb(0,117,220, 1),    // #0075DC Blue
      Color.rgb(153,63,0, 1),     // #993F00 Caramel
      Color.rgb(76,0,92, 1),      // #4C005C	Damson
      Color.rgb(25,25,25, 1),     // #191919	Ebony
      Color.rgb(0,92,49, 1),      // #005C31	Forest
      Color.rgb(43,206,72, 1),    // #2BCE48	Green
      Color.rgb(255,204,153, 1),  // #FFCC99	Honeydew
      Color.rgb(128,128,128, 1),  // #808080	Iron
      Color.rgb(148,255,181, 1),  // #94FFB5	Jade
      Color.rgb(143,124,0, 1),    // #8F7C00	Khaki
      Color.rgb(157,204,0, 1),    // #9DCC00	Lime
      Color.rgb(194,0,136, 1),    // #C20088	Mallow
      Color.rgb(0,51,128, 1),     // #003380	Navy
      Color.rgb(255,164,5, 1),    // #FFA405	Orpiment
      Color.rgb(255,168,187, 1),  // #FFA8BB	Pink
      Color.rgb(66,102,0, 1),     // #426600	Quagmire
      Color.rgb(255,0,16, 1),     // #FF0010	Red
      Color.rgb(94,241,242, 1),   // #5EF1F2	Sky
      Color.rgb(0,153,143, 1),    // #00998F	Turquoise
      Color.rgb(224,255,102, 1),  // #E0FF66	Uranium
      Color.rgb(116,10,255, 1),   // #740AFF	Violet
      Color.rgb(153,0,0, 1),      // #990000	Wine
      Color.rgb(255,255,128, 1),  // #FFFF80	Xanthin
      Color.rgb(255,255,0, 1),    // #FFFF00	Yellow
      Color.rgb(255,80,5, 1)      // #FF5005	Zinnia
  };
}

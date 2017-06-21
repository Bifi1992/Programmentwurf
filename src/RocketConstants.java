/**
 * Created by y.brisch on 08.06.17.
 */
public final class RocketConstants {
  private RocketConstants() {

  }

  /**
   * holds the maximum speed for a successful landing of all rockets
   */
  public static final Coordinate2D MAX_LANDING_SPEED = new Coordinate2D(20, 20);

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
  public static final double FUEL_PER_ACCELERATION = 0.001;

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

}

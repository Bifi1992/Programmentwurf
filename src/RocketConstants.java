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
  public static final Coordinate2D INIT_COORDINATES = new Coordinate2D(5, 5);

  /**
   * holds the initial acceleration of all rockets
   */
  public static final Coordinate2D INIT_ACCELERATION = new Coordinate2D(0, 0);
}

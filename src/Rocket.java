/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * the initial speed
   */
  private final float mInitSpeed;

  /**
   * the current speed
   */
  public float mCurSpeed;

  /**
   * the initial distance to the planet
   */
  private final long mInitDistance;

  /**
   * the current distance to the planet
   */
  public long mCurDistance;

  /**
   * the initial fuel level
   */
  private final float mInitFuelLevel;

  /**
   * the current fuel level
   */
  public float mCurFuelLevel;

  /**
   * the maximum speed for a successful landing
   */
  private static final long MAX_LANDING_SPEED=20;

  public Rocket() {
    //TODO
    mInitSpeed = 1;
    mInitDistance = 1;
    mInitFuelLevel = 1;
  }


}

/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * the initial speed
   */
  public final float mInitSpeed;

  /**
   * the current speed
   */
  private float mCurSpeed;

  /**
   * the initial distance to the planet
   */
  public final long mInitDistance;

  /**
   * the current distance to the planet
   */
  private long mCurDistance;

  /**
   * the initial fuel level
   */
  public final float mInitFuelLevel;

  /**
   * the current fuel level
   */
  private float mCurFuelLevel;

  /**
   * the maximum speed for a successful landing
   */
  private final long MAX_LANDING_SPEED=20;

  public final Coordinate2D mInitCoordinates;
  private Coordinate2D mCurCoordinates;

  public Rocket(float pInitSpeed, long pInitDistance, float pInitFuelLevel, Coordinate2D pCoordinates) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitDistance = pInitDistance;
    mCurDistance = pInitDistance;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = pCoordinates;
    mCurCoordinates = pCoordinates;
  }

  public Rocket(float pInitSpeed, long pInitDistance, float pInitFuelLevel, int pX, int pY) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitDistance = pInitDistance;
    mCurDistance = pInitDistance;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = new Coordinate2D(pX, pY);
    mCurCoordinates = new Coordinate2D(pX, pY);
  }

  public float getCurSpeed() {
    return mCurSpeed;
  }

  public void setCurSpeed(float mCurSpeed) {
    this.mCurSpeed = mCurSpeed;
  }

  public long getCurDistance() {
    return mCurDistance;
  }

  public void setCurDistance(long mCurDistance) {
    this.mCurDistance = mCurDistance;
  }

  public float getCurFuelLevel() {
    return mCurFuelLevel;
  }

  public void setCurFuelLevel(float mCurFuelLevel) {
    this.mCurFuelLevel = mCurFuelLevel;
  }

  public long getMAX_LANDING_SPEED() {
    return MAX_LANDING_SPEED;
  }

  public Coordinate2D getCurCoordinates() {
    return mCurCoordinates;
  }
}

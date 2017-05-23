/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * the initial speed
   */
  public final Coordinate2D mInitSpeed;

  /**
   * the current speed
   */
  private Coordinate2D mCurSpeed;

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
  private static final Coordinate2D MAX_LANDING_SPEED = new Coordinate2D(20, 20);

  public final Coordinate2D mInitCoordinates;
  private Coordinate2D mCurCoordinates;

  public Rocket(Coordinate2D pInitSpeed, float pInitFuelLevel, Coordinate2D pCoordinates) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = pCoordinates;
    mCurCoordinates = pCoordinates;
  }

  public Rocket(Coordinate2D pInitSpeed, float pInitFuelLevel, double pX, double pY) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = new Coordinate2D(pX, pY);
    mCurCoordinates = new Coordinate2D(pX, pY);
  }

  public Rocket(double pSpeedX, double pSpeedY, float pInitFuelLevel, double pCoordX, double pCoordY) {
    mInitSpeed = new Coordinate2D(pSpeedX, pCoordY);
    mCurSpeed = new Coordinate2D(pSpeedX, pCoordY);
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = new Coordinate2D(pCoordX, pCoordY);
    mCurCoordinates = new Coordinate2D(pCoordX, pCoordY);
  }

  public Coordinate2D getCurSpeed() {
    return mCurSpeed;
  }

  public void setCurSpeed(Coordinate2D mCurSpeed) {
    this.mCurSpeed = mCurSpeed;
  }

  public float getCurFuelLevel() {
    return mCurFuelLevel;
  }

  public void setCurFuelLevel(float mCurFuelLevel) {
    this.mCurFuelLevel = mCurFuelLevel;
  }

  public Coordinate2D getCurCoordinates() {
    return mCurCoordinates;
  }

  public void setCurCoordinates(double pX, double pY) {
    this.mCurCoordinates = new Coordinate2D(pX, pY);
  }

  public void setCurCoordinates(Coordinate2D pCoord) {
    this.mCurCoordinates = pCoord;
  }
}

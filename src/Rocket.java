import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * the initial speed
   */
  public final Coordinate2D mInitSpeed;

  //TODO Anflugwinkel

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
   * golds the initial distance of the rocket to the planet's surface
   */
  private double mInitDistance;

  /**
   * the used time
   */
  private int mTime;

  /**
   * the maximum speed for a successful landing
   */

  private static final Coordinate2D MAX_LANDING_SPEED = new Coordinate2D(20, 20);

  /**
   * A Map of the speed process per second <Second, Speed>
   */
  private Map<Integer, Coordinate2D> mProcessSpeed = new LinkedHashMap<>();

  public final Coordinate2D mInitCoordinates;
  private Coordinate2D mCurCoordinates;

  public Rocket(Coordinate2D pInitSpeed, float pInitFuelLevel, Coordinate2D pCoordinates, double pInitDistance) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = pCoordinates;
    mCurCoordinates = pCoordinates;
    mInitDistance = pInitDistance;
  }

  public Rocket(Coordinate2D pInitSpeed, float pInitFuelLevel, double pX, double pY, double pInitDistance) {
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = new Coordinate2D(pX, pY);
    mCurCoordinates = new Coordinate2D(pX, pY);
    mInitDistance = pInitDistance;
  }

  public Rocket(double pSpeedX, double pSpeedY, float pInitFuelLevel, double pCoordX, double pCoordY, double pInitDistance) {
    mInitSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mCurSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = new Coordinate2D(pCoordX, pCoordY);
    mCurCoordinates = new Coordinate2D(pCoordX, pCoordY);
    mInitDistance = pInitDistance;
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

  public double getInitDistance() {
    return this.mInitDistance;
  }

  public void setProcessSpeed() {
    this.mProcessSpeed.put(this.mTime, this.mCurSpeed);
  }

  public Map<Integer, Coordinate2D> getProcessSpeed() {
    return this.mProcessSpeed;
  }

  public int getTime() {
    return mTime;
  }

  public void setTime(int mTime) {
    this.mTime = mTime;
  }


}

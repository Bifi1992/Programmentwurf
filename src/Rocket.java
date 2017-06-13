import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * holds the corresponding generation id of the rocket
   */
  public final int mGenerationId;


  /**
   * holds the rocket id
   */
  public final int mRocketId;

  /**
   * holds the initial speed of the rocket
   */
  public final Coordinate2D mInitSpeed;

  //TODO Anflugwinkel

  /**
   * holds the current speed of the rocket
   */
  private Coordinate2D mCurSpeed;

  /**
   * holds the initial acceleration of the rocket
   */
  public final Coordinate2D mInitAcceleration;

  /**
   * holds the current acceleration of the rocket
   */
  private Coordinate2D mCurAcceleration;

  /**
   * holds the initial fuel level of the rocket
   */
  public final float mInitFuelLevel;

  /**
   * holds the current fuel level of the rocket
   */
  private float mCurFuelLevel;

  /**
   * holds the initial distance of the rocket to the planet's surface
   */
  private double mInitDistance;

  /**
   * holds the used passed flight time of the rocket
   */
  private int mTime;

  /**
   * holds the initial coordinates of the rocket
   */
  public final Coordinate2D mInitCoordinates;

  /**
   * A Map of the speed process per second <Second, Speed>
   */
  private Map<Integer, Coordinate2D> mProcessSpeed = new LinkedHashMap<>();

  /**
   * holds the current coordinates of the Rocket
   */
  private Coordinate2D mCurCoordinates;

  /**
   * constructor for rocket
   * @param pGenerationId the generation id of the Rocket
   * @param pRocketId the id of the rocket
   * @param pInitSpeed the initial speed of the rocket
   * @param pInitFuelLevel the initial fuel level of the rocket
   * @param pInitDistance the initial distance to the surface of the rocket
   */
  public Rocket(int pGenerationId, int pRocketId, Coordinate2D pInitSpeed, float pInitFuelLevel, double pInitDistance) {
    mGenerationId = pGenerationId;
    mRocketId = pRocketId;
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = RocketConstants.INIT_COORDINATES;
    mCurCoordinates = RocketConstants.INIT_COORDINATES;
    mInitDistance = pInitDistance;
    mInitAcceleration = RocketConstants.INIT_ACCELERATION;
    mCurAcceleration = RocketConstants.INIT_ACCELERATION;
  }

  /**
   * constructor for rocket
   * @param pGenerationId the generation id of the Rocket
   * @param pRocketId the id of the rocket
   * @param pSpeedX the initial x speed of the rocket
   * @param pSpeedY the initial y speed of the rocket
   * @param pInitFuelLevel the initial fuel level of the rocket
   * @param pInitDistance the initial distance to the surface of the rocket
   */
  public Rocket(int pGenerationId, int pRocketId, double pSpeedX, double pSpeedY, float pInitFuelLevel, double pInitDistance) {
    mGenerationId = pGenerationId;
    mRocketId = pRocketId;
    mInitSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mCurSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = RocketConstants.INIT_COORDINATES;
    mCurCoordinates = RocketConstants.INIT_COORDINATES;
    mInitDistance = pInitDistance;
    mInitAcceleration = RocketConstants.INIT_ACCELERATION;
    mCurAcceleration = RocketConstants.INIT_ACCELERATION;
  }

  /**
   * getters and setters for rocket member variables
   */
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

  public Coordinate2D getCurAcceleration() {
    return this.mCurAcceleration;
  }
  public int getRocketID() {
    return this.mRocketId;
  }
  public int getGenerationId(){
    return this.mGenerationId;
  }
}

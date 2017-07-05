/**
 * Created by y.brisch on 01.07.17.
 */
public class EliteRocket extends Rocket {

  private double mFinalFuelLevel;
  private double mFinalSpeed;
  private double mFinalDistance;
  private int mFinalTime;
  private double mFinalDistanceX;

  /**
   * A Rocket that contains some additional information important after landing with easier access
   *
   * @param pRocket the rocket to be converted to an elite rocket
   */
  public EliteRocket(Rocket pRocket) {
    super(pRocket.getGenerationId(), pRocket.getRocketID(), pRocket.mInitSpeed, pRocket.mInitFuelLevel, pRocket.getInitDistance());
    mFinalSpeed = pRocket.getCurSpeed().abs();
    this.setCurSpeed(pRocket.getCurSpeed());
    mFinalDistance = pRocket.getCurCoordinates().getY();
    mFinalDistanceX = pRocket.getInitDistance() - pRocket.getCurCoordinates().getX();
    this.setCurCoordinates(pRocket.getCurCoordinates());
    mFinalTime = pRocket.getTime();
    this.setTime(pRocket.getTime());
    mFinalFuelLevel = pRocket.getCurFuelLevel();
    this.setCurFuelLevel(pRocket.getCurFuelLevel());

    this.setProcessAcc(pRocket.getProcessAcc());
  }

  /**
   * Getters of this class
   */
  public double getFinalFuelLevel() {
    return mFinalFuelLevel;
  }

  public double getFinalSpeed() {
    return mFinalSpeed;
  }

  public double getFinalDistanceX() {
    return mFinalDistanceX;
  }

  public int getFinalTime() {
    return mFinalTime;
  }
}

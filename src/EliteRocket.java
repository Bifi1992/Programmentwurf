import java.util.ArrayList;

/**
 * Created by y.brisch on 01.07.17.
 */
public class EliteRocket extends Rocket {

  private double mFinalFuelLevel;
  private double mFinalSpeed;
  private double mFinalDistance;
  private int mFinalTime;

  public EliteRocket(Rocket pRocket) {
    super(pRocket.getGenerationId(), pRocket.getRocketID(), pRocket.mInitSpeed, pRocket.mInitFuelLevel, pRocket.getInitDistance());
    mFinalSpeed = pRocket.getCurSpeed().abs();
    this.setCurSpeed(pRocket.getCurSpeed());
    mFinalDistance = pRocket.getInitDistance() - pRocket.getCurCoordinates().getY();
    this.setCurCoordinates(pRocket.getCurCoordinates());
    mFinalTime = pRocket.getTime();
    this.setTime(pRocket.getTime());
    mFinalFuelLevel = pRocket.getCurFuelLevel();
    this.setCurFuelLevel(pRocket.getCurFuelLevel());

    this.setProcessAcc(pRocket.getProcessAcc());
  }

  public double getFinalFuelLevel() {
    return mFinalFuelLevel;
  }

  public double getFinalSpeed() {
    return mFinalSpeed;
  }

  public double getFinalDistance() {
    return mFinalDistance;
  }

  public int getFinalTime() {
    return mFinalTime;
  }
}

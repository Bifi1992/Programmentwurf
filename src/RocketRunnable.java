/**
 * Created by y.brisch on 17.05.17.
 */
public class RocketRunnable implements Runnable {

  Rocket mRocket;
  Planet mPlanet;

  public RocketRunnable(Rocket pRocket, Planet pPlanet){
    mRocket = pRocket;
    mPlanet = pPlanet;
  }

  @Override
  public void run() {
    //TODO timelimit
    while (mRocket.getCurCoordinates().getY() > 0) {
      calcNewCoordinates();
      System.out.println();
    }
    System.out.println("Test");
  }

  /**
   * The mass of the Rocket is not accounted for in this calculation
   * //TODO calc acceleration not force
   *  F = (G * m * M)/r^2
   *  F = m * a
   *  => a = (G * M)/r^2 || (m^3 * kg)/(kg * s^2 * m^2) => m/s^2
   * @return
   */
  public double calculateGravitationalAcceleration(){
    double distance = calcDistance();
    double gForce = (Planet.GRAVITATIONAL_CONSTANT * mPlanet.mMass)/(distance * distance);
    return 0;
  }

  /**
   * distance to surface: Y Coordinate
   * plus planet radius for simplification
   * @return
   */
  public double calcDistance() {
    return mRocket.getCurCoordinates().getY() + mPlanet.mRadius;
  }

  /**
   * s = 0,5 · a · t^2 + v0 · t + s0
   */
  public Coordinate2D calcNewCoordinates() {
    double newX = (0.5 * calculateGravitationalAcceleration() *  1 + mRocket.getCurSpeed() * 1 +
        mRocket.getCurCoordinates().getX());
    double newY = (0.5 * calculateGravitationalAcceleration() *  1 + mRocket.getCurSpeed() * 1 +
        mRocket.getCurCoordinates().getY());
    return new Coordinate2D(newX, newY);
  }
}

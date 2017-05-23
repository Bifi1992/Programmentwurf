import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Created by y.brisch on 17.05.17.
 */
public class RocketRunnable implements Runnable {

  Rocket mRocket;
  Planet mPlanet;
  //Interface mInterface = Interface.getInstance();

  /**
   * Holds the passed textarea
   */
  TextArea mTextArea;

  public RocketRunnable(Rocket pRocket, Planet pPlanet, TextArea pTextArea){
    mRocket = pRocket;
    mPlanet = pPlanet;
    mTextArea = pTextArea;
  }

  @Override
  public void run() {
    //TODO timelimit, correct condition
    while (mRocket.getCurCoordinates().getY() < 800) {
      Platform.runLater( () -> {
        mTextArea.appendText(mRocket.getCurCoordinates().getX() + " : " +
            mRocket.getCurCoordinates().getY() + "\n");
        calcNewCoordinates();
      });

      /*
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        mTextArea.appendText(e.getMessage());
      }
      //*/

    }
  }

  /**
   * The mass of the Rocket is not accounted for in this calculation
   *  F = (G * m * M)/r^2
   *  F = m * a
   *  => a = (G * M)/r^2 || (m^3 * kg)/(kg * s^2 * m^2) => m/s^2
   * @return
   */
  public double calculateGravitationalAcceleration(){
    double distance = calcDistance();
    return (Planet.GRAVITATIONAL_CONSTANT * mPlanet.getMass())/(distance * distance);
  }

  /**
   * distance to surface: Y Coordinate
   * plus planet radius for simplification
   * @return
   */
  public double calcDistance() {
    return mRocket.getCurCoordinates().getY() + mPlanet.getRadius();
  }

  /**
   * s = 0,5 · a · t^2 + v0 · t + s0
   */
  public void calcNewCoordinates() {
    double newX = (0.5 * calculateGravitationalAcceleration() *  1 + mRocket.getCurSpeed().getX() * 1 +
        mRocket.getCurCoordinates().getX());
    double newY = (0.5 * calculateGravitationalAcceleration() *  1 + mRocket.getCurSpeed().getY() * 1 +
        mRocket.getCurCoordinates().getY());
    mRocket.setCurCoordinates(newX, newY);

  }
}

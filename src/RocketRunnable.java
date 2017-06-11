import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

/**
 * Created by y.brisch on 17.05.17.
 */
public class RocketRunnable implements Runnable {

  /**
   * holds the time difference between every calculation
   */
  private static final int TIME_INTERVAL = 1;

  /**
   * holds the value for adjustment of y coordinates
   */
  private static double COORD_Y_FACTOR;

  /**
   * holds the value for adjustment of x coordinates
   */
  private static final int COORD_X_FACTOR = 600;

  /**
   * Holds the passed textarea
   */
  TextArea mTextArea;

  /**
   * Holds the passed GraphicsContext for the Canvas
   */
  GraphicsContext mGC;

  /**
   * holds the passed rocket
   */
  Rocket mRocket;

  /**
   * holds the passed planet
   */
  Planet mPlanet;

  /**
   * holds the passed canvas
   */
  Canvas mCanvas;

  /**
   * The constructor for {@link RocketRunnable}
   * @param pRocket the rocket which is to be calculated
   * @param pPlanet the planet to land on
   * @param pTextArea the textarea of the simscene
   * @param pCanvas the canvas of the simscene
   * @param pGC the graphics context of the simscene canvas
   */

  public RocketRunnable(Rocket pRocket, Planet pPlanet, TextArea pTextArea, Canvas pCanvas, GraphicsContext pGC){
    mRocket = pRocket;
    mPlanet = pPlanet;
    mTextArea = pTextArea;
    mGC = pGC;
    mCanvas = pCanvas;
    COORD_Y_FACTOR = pCanvas.getHeight()/pRocket.getInitDistance();
  }

  @Override
  public void run() {
    //TODO timelimit, correct condition
    while (mRocket.getCurCoordinates().getY() < mRocket.getInitDistance()) {
      Platform.runLater( () -> {
        Coordinate2D oldCoord = mRocket.getCurCoordinates();
        Coordinate2D newCoord = calcNewCoordinates();
        mGC.strokeLine(oldCoord.getX()/COORD_X_FACTOR, oldCoord.getY() * COORD_Y_FACTOR,
            newCoord.getX()/COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
        if (mRocket.mTime % 20 == 0) {
          mGC.strokeText("" + String.format("%6.3e",(calcDistance() - mPlanet.getRadius())),
              newCoord.getX() / COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
          mTextArea.appendText("mRocket.getInitDistance() = " + mRocket.getInitDistance()
              + "calcDistance() = " + calcDistance()
              + " -> " + String.format("%6.3e",(mRocket.getInitDistance() - calcDistance())) + "\n");
        }
        mRocket.mTime += TIME_INTERVAL;
      });

      // Sleep for a few ms to not spam the program with runnables
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        mTextArea.appendText(e.getMessage());
      }
    }
    Platform.runLater( () -> {
      mTextArea.appendText("landing time:" + mRocket.mTime + "\n");
      mTextArea.appendText(mPlanet.name() + "\n");
    });
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
   * distance to surface: initial distance minus Y Coordinate
   * plus planet radius for simplification
   * @return
   */
  public double calcDistance() {
    return mRocket.getInitDistance() - mRocket.getCurCoordinates().getY() + mPlanet.getRadius();
  }

  /**
   * TODO implement variable rocket acceleration
   * this method calculates and sets the new coordinates and speed of the rocket
   */
  public Coordinate2D calcNewCoordinates() {
    double ag = calculateGravitationalAcceleration();
    double newX = mRocket.getCurSpeed().abs() * mRocket.mTime
        * Math.cos(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()));
    double newY = mRocket.getCurSpeed().abs() * mRocket.mTime
        * Math.sin(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()))
        + 0.5 * ag *  mRocket.mTime * mRocket.mTime;
    mRocket.setCurCoordinates(newX, newY);
    mRocket.setCurSpeed(new Coordinate2D(mRocket.getCurSpeed().getX(),
        mRocket.getCurSpeed().getY() + (ag * TIME_INTERVAL)));

    System.out.println("rocket" + mRocket.mRocketId + ": " + mRocket.mTime + "s : " + mRocket.getCurSpeed().abs() + "m/s");
    return new Coordinate2D(newX, newY);
  }
}

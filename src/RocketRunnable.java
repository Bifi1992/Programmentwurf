import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

/**
 * Created by y.brisch on 17.05.17.
 */
public class RocketRunnable implements Runnable {

  Rocket mRocket;
  Planet mPlanet;
  Canvas mCanvas;

  /**
   * holds the time difference between every calculation
   */
  private static final int TIME_INTERVAL = 1;

  /**
   *
   */
  private static double COORD_Y_FACTOR;

  /**
   *
   */
  private static final int COORD_X_FACTOR = 300;

  /**
   * elapsed time in [s]
   */
  private static int mTime = 0;


  /**
   * Holds the passed textarea
   */
  TextArea mTextArea;

  /**
   * Holds the passed GraphicsContext for the Canvas
   */
  GraphicsContext mGC;

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
        if (mTime % 10 == 0) {
          mGC.strokeText("" + String.format("%6.3e",(mRocket.getInitDistance() - calcDistance())),
              newCoord.getX() / COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
        }
      });

      // Sleep for a few ms to not spam the program with runnables
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        mTextArea.appendText(e.getMessage());
      }
      //*/
      mTime += TIME_INTERVAL;
    }
    Platform.runLater( () -> {
      mTextArea.appendText("landing time:" + mTime + "\n");
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
   * s = 0,5 · a · t^2 + v0 · t + s0
   */
  public Coordinate2D calcNewCoordinates() {
    double newX = mRocket.getCurSpeed().abs() * mTime * Math.cos(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()));
    double newY = mRocket.getCurSpeed().abs() * mTime * Math.sin(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()))
        + 0.5 * calculateGravitationalAcceleration() *  mTime * mTime;
    System.out.println(mRocket.getCurSpeed().abs());
    return new Coordinate2D(newX, newY);
  }
}

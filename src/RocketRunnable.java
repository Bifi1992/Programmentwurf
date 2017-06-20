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
  private static final int TIME_INTERVAL = 5;
  private static int DISPLAY_INTERVAL;


  /**
   * holds the value for adjustment of y coordinates
   */
  private static double COORD_Y_FACTOR;

  /**
   * holds the value for adjustment of x coordinates
   */
  private static double COORD_X_FACTOR;

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
    COORD_X_FACTOR = mRocket.getCurSpeed().getX() == 0 ? 1 :
        (pCanvas.getWidth() / 3) / (mRocket.getCurSpeed().getX() *
            Math.cos(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis())) * mPlanet.getApproxLandingTimeTime());
    DISPLAY_INTERVAL = pPlanet.getApproxLandingTimeTime()/10;
    System.out.println(COORD_X_FACTOR);
  }

  @Override
  public void run() {
    Platform.runLater(() -> {
      displayGrid(30, 30);
      mTextArea.appendText("initSpeed: " + mRocket.getCurSpeed().toString() + "\n" +
      "initAngle: " + mRocket.getCurSpeed().getAngleXAxis() + "°\n" +
      "initCoords: " + mRocket.getCurCoordinates().toString() + "\n");
    });
    //TODO timelimit, correct condition
    while (mRocket.getCurCoordinates().getY() < mRocket.getInitDistance()) {
      Platform.runLater(() -> {
        Coordinate2D oldCoord = mRocket.getCurCoordinates();
        if (mRocket.mTime <= 100) {
          //System.out.println(mRocket.mTime + "s: " + oldCoord.getX() * COORD_X_FACTOR + ", " + oldCoord.getY() * COORD_Y_FACTOR);
        }
        calcCurAcceleration();
        calcNewCoordinates();
        Coordinate2D newCoord = mRocket.getCurCoordinates();
            mGC.strokeLine(oldCoord.getX() * COORD_X_FACTOR, oldCoord.getY() * COORD_Y_FACTOR,
            newCoord.getX() * COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
        if (mRocket.mTime != 0 && mRocket.mTime % DISPLAY_INTERVAL == 0) {
          mGC.strokeText("" + mRocket.mTime, newCoord.getX() * COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
          /*
          mGC.strokeText("(" + String.format("%6.2e",newCoord.getX()) + ", " + String.format("%6.2e",newCoord.getY()) + ")",
              newCoord.getX() * COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
          */
          /*
          mGC.strokeText("" + String.format("%6.3e",(calcDistance() - mPlanet.getRadius())),
              newCoord.getX() / COORD_X_FACTOR, newCoord.getY() * COORD_Y_FACTOR);
           */
          /*
          mTextArea.appendText("mRocket.getInitDistance() = " + mRocket.getInitDistance()
              + "calcDistance() = " + calcDistance()
              + " -> " + String.format("%6.3e",(mRocket.getInitDistance() - calcDistance())) + "\n");
          */
        }
        mRocket.mTime += TIME_INTERVAL;
        mRocket.setTime(mRocket.mTime);
        mRocket.setProcessSpeed();
      });
      // Sleep for a few ms to not spam the program with runnables
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        mTextArea.appendText(e.getMessage());
      }
      //mRocket.setCurSpeed();
      // Setting process speed on every secound
      mRocket.setProcessSpeed();
    }
    System.out.println("Finished");
    /*for (int key: mRocket.getProcessSpeed().keySet()) {
      System.out.println("id: " + mRocket.getRocketID() + " Key: " + key + " " + mRocket.getProcessSpeed().get(key).abs());
    }*/

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
   * Calculate acceleration by getting processAcceleration value
   */
   public void calcCurAcceleration() {
      if (mRocket.mTime < mRocket.getProcessAcc().size()) {
          mRocket.setCurAcceleration(mRocket.getProcessAcc().get(mRocket.mTime));
      } else {
        mRocket.setCurAcceleration(new Coordinate2D((Math.random() * ((20)) - 10), Math.random() * ((600)) - 300));
      }
  }

  /**
   * TODO implement variable rocket acceleration
   * this method calculates and sets the new coordinates and speed of the rocket
   */
  public void calcNewCoordinates() {
    // Variables for the calculation of speed and coordinates
    // gravitational acceleration
    double g = calculateGravitationalAcceleration();
    // inherent acceleration in x direction
    double aX = mRocket.getCurAcceleration().getX();
    // inherent acceleration in y direction
    double aY = mRocket.getCurAcceleration().getY();
    // current speed in x-direction
    double vX = mRocket.getCurSpeed().getX();
    // current speed in y-direction
    double vY = mRocket.getCurSpeed().getY();
    // cosine of angle to x-axis
    double cosAlpha = Math.cos(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()));
    // sinus of angle to x-axis
    double sinAlpha = Math.sin(Math.toRadians(mRocket.getCurSpeed().getAngleXAxis()));

    // Calculation of the new Coordinates
    // v * cos(alpha) * t + 0.5 * aX * t^2
    double newXCoord = mRocket.getCurCoordinates().getX() + vX * TIME_INTERVAL * cosAlpha + 0.5 * aX * TIME_INTERVAL * TIME_INTERVAL;
    // v * sin(alpha) * t + 0.5 * (g + aY) * t^2
    double newYCoord = mRocket.getCurCoordinates().getY() + vY * TIME_INTERVAL * sinAlpha + 0.5 * (g + aY) * TIME_INTERVAL * TIME_INTERVAL;
    mRocket.setCurCoordinates(newXCoord, newYCoord);

    // Calculation of the new Speed
    // v * cos(alpha) + aX * t
    double newXSpeed = vX * cosAlpha + aX * TIME_INTERVAL;
    // v * sin(alpha) + (g + aY) * t
    double newYSpeed = vY * sinAlpha + (g  + aY) * TIME_INTERVAL;
    mRocket.setCurSpeed(new Coordinate2D(newXSpeed, newYSpeed));
  }

  /**
   * This method generates a grid with mash size of x*y
   */
  private void displayGrid(double pX, double pY) {
    for (double i = pX; i < mCanvas.getWidth(); i += pX) {
      mGC.strokeLine(i, 0, i, mCanvas.getHeight());
    }
    for (double i = pY; i < mCanvas.getHeight(); i += pY) {
      mGC.strokeLine(0, i, mCanvas.getWidth(), i);
    }
  }
}

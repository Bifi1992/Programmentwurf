import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Math.abs;
import static java.lang.Math.floor;


/**
 * Created by ludwig on 17.05.17.
 */

/**
 * THIS IS A PROTOTYPE CLASS FOR UNDERSTANDING GENETIC ALGORITHM's
 */
public class GeneticLearningAbstract {
  CountDownLatch latch = new CountDownLatch(1);

  /**
   * responsible to manage runnables
   */
  ThreadPool mThreadPool;

  /**
   * holds the information, if the genetic learner should generate new rockets and, thus, continue to work
   */
  private boolean isRunning = true;

  /**
   * gets initialised in constructor
   */
  Planet mPlanet;

  /**
   * gets initialised in constructor
   */
  final Canvas mCanvas;

  /**
   * gets initialised in constructor
   */
  GraphicsContext mGC;

  /**
   * gets initialised in constructor
   */
  TextArea mTextArea;

  /**
   * gets initialised in constructor
   */
  Interface mInterface;

  /**
   * counter variable which represents actual generation id
   */
  int mGeneration = 1;

  /**
   * list of population wich gets recreated on every generation
   */
  List<Rocket> population = new ArrayList<Rocket>();

  /**
   * list of parents, wich are choosen from the fitness function
   */
  List<Rocket> parents = new ArrayList<>();


  /**
   * @param pInterface
   */
  public GeneticLearningAbstract(Interface pInterface) {
    mInterface = pInterface;
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    mPlanet = pInterface.mPlanetDropDown.getValue();
    mTextArea = pInterface.mTextArea;
    mCanvas = pInterface.mCanvas;
    mGC = pInterface.mGC;
    //TIME_INTERVAL = (int) Math.ceil((double) mPlanet.getMaxLandingTime() / 10000);
  }

  /**
   * Create random population and values of rockets
   */
  public void initPopulationRandom() {
    /**
     * processAcc keeps the acceleration values over time of a certain rocket
     * processAcc gets filled randomly the first time
     */
    ArrayList<Coordinate2D> processAcc;
    for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      processAcc = new ArrayList<>();
      double deltaX;
      double deltaY;
      double xInter;
      double yInter;
      for (int d = 0; d <= 100; d++) {
        processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
      }
      /**
       * setup rocket with process speed.
       */
      Rocket rocket = new Rocket(
          mGeneration,
          i,
          RocketConstants.INIT_SPEED_X,
          RocketConstants.INIT_SPEED_Y,
          mInterface.mSliderInitFuelLevel.getValue(),
          mInterface.mSliderInitDistance.getValue(),
          processAcc
      );
      mThreadPool.execute(new RocketRunnable(rocket, mInterface));
      this.population.add(rocket);
    }

    /**
     * thread which waits until runnables finished. Not the best way to implement this, but it works.
     */
    Thread t = new Thread(() -> {
      try {
        mThreadPool.stop();
        mThreadPool.awaitTermination();
        System.out.println("Threads terminated!");
      } catch (TimeoutException e) {
        e.printStackTrace(System.err);
      }
      mGeneration++;
      getFitness();
    });
    t.start();
  }

  /**
   * Calculate fitness of population
   */
  public void getFitness() {
    /**
     * Variables needed to calculate fitness level of rocket
     */
    float fuelSum = 0.0f;
    float speedSum = 0.0f;
    float timeSum = 0.0f;
    float distanceSum = 0.0f;
    float fitnessTime;
    float fitnessFuel;
    float fitnessSpeed;
    float fitnessAll = 0;
    float fitnessOld;
    float fitnessDistance;
    Rocket currRocket;
    Rocket best = null;
    Rocket secondBest = null;
    parents = new ArrayList<>();

    /**
     * get sum of all values
     */
    for (Integer j = 0; j < this.population.size(); j++) {
      fuelSum += population.get(j).getCurFuelLevel();
      speedSum += population.get(j).getCurSpeed().abs();
      timeSum += population.get(j).getTime();
      distanceSum += population.get(j).getInitDistance() - population.get(j).getCurCoordinates().getY()
          + mPlanet.getRadius();
    }
    /**
     * Choose best items of population depending on their distance to the goal value.
     */
    for (Integer j = 0; j < this.population.size(); j++) {
      currRocket = population.get(j);
      fitnessFuel = (float) (currRocket.getCurFuelLevel() / fuelSum);
      fitnessTime = 1 - (currRocket.getTime() / timeSum);
      fitnessSpeed = 1 - (float) (currRocket.getCurSpeed().abs() / speedSum);
      fitnessOld = fitnessAll;
      fitnessDistance = 1 - ((float) (currRocket.getInitDistance() -
          population.get(j).getCurCoordinates().getY()
          + mPlanet.getRadius()) / distanceSum);
      fitnessAll = (float) (AlgorithmConstants.RATING_FUEL * fitnessFuel + AlgorithmConstants.RATING_TIME
          * fitnessTime + AlgorithmConstants.RATING_SPEED * fitnessSpeed
          + AlgorithmConstants.RATING_DISTANCE * fitnessDistance) * 0.5f;

      /**
       * check if rocket is landed
       */
      if (currRocket.getCurSpeed().abs() <= RocketConstants.MAX_LANDING_SPEED) {
        fitnessAll += 0.5;
      }
      currRocket.setTotalFitness(fitnessAll);
      System.out.println("Rocket ID: " + currRocket.getRocketID() + "Fitness all: " + ((fitnessAll))
          + "Cur Distance:" + (currRocket.getInitDistance()
          - population.get(j).getCurCoordinates().getY() + mPlanet.getRadius())
          + "Cur Speed: " + currRocket.getCurSpeed().abs() + "Cur Fuel: "
          + currRocket.getCurFuelLevel() + " CurAcc: " + currRocket.getCurAcceleration().abs());
      /**
       * If rockets are chosen depends on fitnessAll, which is the sum of every fitnessparameter an it's weight.
       */
      if (fitnessAll > fitnessOld) {
        if (best == null) {
          best = currRocket;
        } else {
          secondBest = best;
          best = currRocket;
        }
        if (secondBest == null) {
          secondBest = currRocket;
        }
      }
    }
    /**
     * Output of best rockets from this thread
     */
    parents.add(best);
    parents.add(secondBest);
    System.out.println("Best Rocket: " + best.getRocketID());
    System.out.println("Parent Size: " + parents.size());
    System.out.println("Secound Best: " + secondBest.getRocketID());
    System.out.println("Parents index: " + parents.get(0));
    if (isRunning) {
      createNextGeneration();
    }

  }

  public void createNextGeneration() {
    // clear population
    population = new ArrayList<>();
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    ArrayList<Coordinate2D> newProcessAcc = new ArrayList<>();
    ArrayList<Coordinate2D> individualProcessAcc;

    /**
     * #### CROSSOVER ####
     */
    int longerParent = parents.get(0).getProcessAcc().size() >= parents.get(1).getProcessAcc().size() ? 0 : 1;
    int shorterParent = longerParent == 0 ? 1 : 0;
    for (int i = 0; i < parents.get(shorterParent).getProcessAcc().size(); i++) {
      if (Math.random() <= 0.5) {
        try {
          newProcessAcc.add(new Coordinate2D(parents.get(longerParent).getProcessAcc().get(i).getX(), parents.get(longerParent).getProcessAcc().get(i).getY()));
        } catch (IndexOutOfBoundsException e) {
          System.out.println("Index: " + i);
          System.out.println("longerParentId: " + longerParent + ", shorterParentId: " + shorterParent);
          System.out.println("longerParent: " + parents.get(longerParent).getProcessAcc().size());
          System.out.println("shorterParent: " + parents.get(shorterParent).getProcessAcc().size());
          throw e;
        }
      } else {
        newProcessAcc.add(new Coordinate2D(parents.get(shorterParent).getProcessAcc().get(i).getX(), parents.get(shorterParent).getProcessAcc().get(i).getY()));
      }
    }
    /**
     * #### MUTATION ####
     */
    for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      individualProcessAcc = new ArrayList<>();
      for (int j = 0; j < newProcessAcc.size(); j++) {
        if (Math.random() <= AlgorithmConstants.MUTATION) {
          individualProcessAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
        } else {
          individualProcessAcc.add(newProcessAcc.get(j));
        }
      }
      Rocket rocket = new Rocket(
          mGeneration,
          i,
          RocketConstants.INIT_SPEED_X,
          RocketConstants.INIT_SPEED_Y,
          mInterface.mSliderInitFuelLevel.getValue(),
          mInterface.mSliderInitDistance.getValue(),
          individualProcessAcc
      );
      mThreadPool.execute(new RocketRunnable(rocket, mInterface));
      this.population.add(rocket);
    }
    Thread t = new Thread(() -> {
      try {
        mThreadPool.stop();
        mThreadPool.awaitTermination();
      } catch (TimeoutException e) {
        e.printStackTrace(System.err);
      }
      mGeneration++;
      getFitness();
      printPopulation();
    });
    t.start();
  }

  /**
   * Print out current population
   * //
   */
  public void printPopulation() {
    System.out.println("Current Population:");
    for (int j = 0; j < this.population.size(); j++) {
      System.out.println("Koordx" + population.get(j).getCurCoordinates().getX() + " KoordY: "
          + population.get(j).getCurCoordinates().getY()
          + "Rocket" + j + "|| Speed: " + population.get(j).getCurSpeed().abs()
          + "  Fuel:  " + population.get(j).getCurFuelLevel());
    }
  }

  public void terminate() {
    isRunning = false;
  }

  public double cosineInterpolate(
    double y1,double y2,
    double mu)
  {
    double mu2;

    mu2 = (1-Math.cos(mu*Math.PI))/2;
    return(y1*(1-mu2)+y2*mu2);
  }
}

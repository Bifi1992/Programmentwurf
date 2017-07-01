import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.*;

import static java.lang.Math.abs;


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
  private ThreadPool mThreadPool;

  /**
   * holds the information, if the genetic learner should generate new rockets and, thus, continue to work
   */
  private boolean isRunning = true;

  /**
   * gets initialised in constructor
   */
  private Planet mPlanet;

  /**
   * gets initialised in constructor
   */
  final Canvas mCanvas;

  /**
   * gets initialised in constructor
   */
  private GraphicsContext mGC;

  /**
   * gets initialised in constructor
   */
  private TextArea mTextArea;

  /**
   * gets initialised in constructor
   */
  private Interface mInterface;

  /**
   * counter variable which represents actual generation id
   */
  private int mGeneration = 1;

  /**
   * list of population wich gets recreated on every generation
   */
  private List<Rocket> population = new ArrayList<Rocket>();

  private Rocket eliteRocket = null;

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
  }

  /**
   * Create random population and values of rockets
   */
  public void initPopulationRandom() {
    /*
     * processAcc keeps the acceleration values over time of a certain rocket
     * processAcc gets filled randomly the first time
     */
    ArrayList<Coordinate2D> processAcc;
    for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      processAcc = new ArrayList<>();
      for (int d = 0; d <= 100; d++) {
        processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), (Math.random() * ((300) - 150))));
        System.out.println(processAcc.get(d).getY());
      }
      /*
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

    /*
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

  /*
   * Calculate fitness of population
   */
  public void getFitness() {
    /*
     * Variables needed to calculate fitness level of rocket
     */
    float fuelSum = 0.0f;
    float speedSum = 0.0f;
    float timeSum = 0.0f;
    float distanceSum = 0.0f;
    float fitnessTime;
    float fitnessFuel;
    float fitnessSpeed;
    float fitnessCurRocket = 0;
    float fitnessPrevRocket;
    float fitnessDistance;
    Rocket currRocket;
    Rocket best = new Rocket(
        -1,
        -1,
        RocketConstants.INIT_SPEED_X,
        RocketConstants.INIT_SPEED_Y,
        mInterface.mSliderInitFuelLevel.getValue(),
        mInterface.mSliderInitDistance.getValue(),
        null
    );
    best.setTotalFitness(0);
    Rocket secondBest = new Rocket(
        -1,
        -1,
        RocketConstants.INIT_SPEED_X,
        RocketConstants.INIT_SPEED_Y,
        mInterface.mSliderInitFuelLevel.getValue(),
        mInterface.mSliderInitDistance.getValue(),
        null
    );
    secondBest.setTotalFitness(0);
    List<Rocket> parents = new ArrayList<>();

    /*
     * get sum of all values
     */
    for (Integer j = 0; j < this.population.size(); j++) {
      fuelSum += population.get(j).getCurFuelLevel();
      speedSum += population.get(j).getCurSpeed().abs();
      timeSum += population.get(j).getTime();
      distanceSum += population.get(j).getInitDistance() - population.get(j).getCurCoordinates().getY();
    }
    /*
     * Choose best items of population depending on their distance to the goal value.
     */
    for (Integer j = 0; j < this.population.size(); j++) {
      currRocket = population.get(j);
      fitnessFuel = (float) (currRocket.getCurFuelLevel() / fuelSum);
      fitnessTime = 1 - (currRocket.getTime() / timeSum);
      fitnessSpeed = 1 - (float) (currRocket.getCurSpeed().abs() / speedSum);
      fitnessDistance = 1 - ((float) (population.get(j).getInitDistance() - population.get(j).getCurCoordinates().getY())/distanceSum);
      fitnessCurRocket = (float) (AlgorithmConstants.RATING_FUEL * fitnessFuel + AlgorithmConstants.RATING_TIME
          * fitnessTime + AlgorithmConstants.RATING_SPEED * fitnessSpeed
          + AlgorithmConstants.RATING_DISTANCE * fitnessDistance) * 0.9f;
      /*
       * TODO: Distance to surface
       */
      /*
       * check if rocket has landed
       */
      if (population.get(j).getInitDistance() - population.get(j).getCurCoordinates().getY() <= 0 ) {
        fitnessCurRocket += 0.1;
      }
      currRocket.setTotalFitness(fitnessCurRocket);
      System.out.println("Rocket ID: " + currRocket.getRocketID() + "Fitness all: " + ((fitnessCurRocket))
          + "Cur Distance:" + (currRocket.getInitDistance()
          - population.get(j).getCurCoordinates().getY() + mPlanet.getRadius())
          + "Cur Speed: " + currRocket.getCurSpeed().abs() + "Cur Fuel: "
          + currRocket.getCurFuelLevel() + " CurAcc: " + currRocket.getCurAcceleration().abs());
      /*
       * If rockets are chosen depends on fitnessCurRocket, which is the sum of every fitness parameter and it's weight.
       */
      if (fitnessCurRocket > best.getTotalFitness()) {
        secondBest = best;
        best = currRocket;
      } else if (fitnessCurRocket > secondBest.getTotalFitness()) {
        secondBest = currRocket;
      }
    }
    /*
     * Output of best rockets from this thread
     */
    /*if(eliteRocket != null && eliteRocket.getTotalFitness() < best.getTotalFitness()){
      eliteRocket = best;
      System.out.println("verbessert");

    }
    if(eliteRocket != null && eliteRocket.getTotalFitness() > best.getTotalFitness()){
      best = eliteRocket;
      System.out.println("Elite nehmen");
    }
    if(eliteRocket == null){
      eliteRocket = best;
    }*/
    parents.add(best);
    parents.add(secondBest);
    prepareCanvasForNextGen(best, secondBest);

    System.out.println("Best Rocket: " + best.getRocketID());
    System.out.println("Parent Size: " + parents.size());
    System.out.println("Secound Best: " + secondBest.getRocketID());
    System.out.println("Parents index: " + parents.get(0));
    if (isRunning) {
      createNextGeneration(parents);
    }
  }

  public void createNextGeneration(List<Rocket> pParents) {
    // clear population
    population = new ArrayList<>();
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    ArrayList<Coordinate2D> newProcessAcc = new ArrayList<>();
    ArrayList<Coordinate2D> individualProcessAcc;

    /*
     * #### CROSSOVER ####
     */
    int longerParent = pParents.get(0).getProcessAcc().size() > pParents.get(1).getProcessAcc().size() ? 0 : 1;
    int shorterParent = longerParent == 0 ? 1 : 0;
    for (int i = 0; i < pParents.get(shorterParent).getProcessAcc().size(); i++) {
      if (Math.random() <= 0.5) {
        try {
          newProcessAcc.add(new Coordinate2D(pParents.get(longerParent).getProcessAcc().get(i).getX(), pParents.get(longerParent).getProcessAcc().get(i).getY()));
        } catch (IndexOutOfBoundsException e) {
          System.out.println("Index: " + i);
          System.out.println("longerParentId: " + longerParent + ", shorterParentId: " + shorterParent);
          System.out.println("longerParent: " + pParents.get(longerParent).getProcessAcc().size());
          System.out.println("shorterParent: " + pParents.get(shorterParent).getProcessAcc().size());
          throw e;
        }
      } else {
        newProcessAcc.add(new Coordinate2D(pParents.get(shorterParent).getProcessAcc().get(i).getX(), pParents.get(shorterParent).getProcessAcc().get(i).getY()));
      }
    }
    /*
     * #### MUTATION ####
     */
    for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      individualProcessAcc = new ArrayList<>();
      for (int j = 0; j < newProcessAcc.size(); j++) {
        if (Math.random() <= AlgorithmConstants.MUTATION) {
          individualProcessAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((300) - 150)));
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

  /**
   * This method prohibits the genetic learner from generating new runnables
   */
  public void terminate() {
    isRunning = false;
  }

  /**
   * - Draw a transparent Rect to hide the previous generations
   * - Redraw the Grid
   * - Show the chosen parents as dots
   * @param pRocket1 the first parent rocket
   * @param pRocket2 the second parent rocket
   */
  private void prepareCanvasForNextGen(Rocket pRocket1, Rocket pRocket2) {
    Platform.runLater(() -> {
      mInterface.drawTransparentRect();
      mInterface.displayGrid(30, 30);
      for (int i = 0; i < 2; i++) {
        Rocket r = i == 0 ? pRocket1 : pRocket2;
        mGC.setFill((Color) RocketConstants.COLOR_PALETTE[r.getRocketID()][0]);
        mGC.setStroke((Color) RocketConstants.COLOR_PALETTE[r.getRocketID()][0]);
        mGC.fillOval(r.getCurCoordinates().getX() * RocketRunnable.COORD_X_FACTOR - 3,
            r.getCurCoordinates().getY() * RocketRunnable.COORD_Y_FACTOR - 3, 6, 6);
        mGC.strokeText(String.valueOf(r.getRocketID()), r.getCurCoordinates().getX() * RocketRunnable.COORD_X_FACTOR - 3,
            r.getCurCoordinates().getY() * RocketRunnable.COORD_Y_FACTOR - 3);
      }
      mTextArea.appendText("Generation " + pRocket1.getGenerationId() + ":\n" +
          "Parents:\n" +
          "Rocket " + pRocket1.getRocketID() + "\n" +
          "Rocket " + pRocket2.getRocketID() + "\n");
    });
  }
}

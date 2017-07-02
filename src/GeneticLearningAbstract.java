import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by ludwig on 17.05.17.
 * THIS IS A PROTOTYPE CLASS FOR UNDERSTANDING GENETIC ALGORITHMS
 */
public class GeneticLearningAbstract {

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
  private List<Rocket> population = new ArrayList<>();

  /**
   * holds a list of the best rockets so far
   */
  private EliteRocket mEliteRocket;

  /**
   * holds all elite rockets
   */
  private ArrayList<EliteRocket> topRockets = new ArrayList<>();

  /**
   * boolean, if acceleration raw values should be written in text document
   */
  private boolean write;

  /**
   * writer for text document
   */
  private PrintWriter writer;

  /**
   * @param pInterface
   */
  public GeneticLearningAbstract(Interface pInterface) {
    mInterface = pInterface;
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    mTextArea = pInterface.mTextArea;
    mCanvas = pInterface.mCanvas;
    mGC = pInterface.mGC;
    write = mInterface.writeInDokument.selectedProperty().getValue();
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
        processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
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
      this.population.add(rocket);
    }

    // start runnable for each rocket
    for (Rocket r : population) {
      if (mInterface.mRadioButtonFastMode.isSelected()){
        mThreadPool.execute(new FastRocketRunnable(r, mInterface));
      } else {
        mThreadPool.execute(new RocketRunnable(r, mInterface));
      }
    }

    /*
     * thread which waits until all runnables terminated.
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
    /*
     * Variables needed to calculate fitness level of rocket
     */
    double fuelSum = 0.0f;
    double speedSum = 0.0f;
    double timeSum = 0.0f;
    double distanceSum = 0.0f;
    Rocket best = null;
    Rocket secondBest = null;
    List<Rocket> parents = new ArrayList<>();

    //set the EliteRocket as the first rocket in the first run
    mEliteRocket = mEliteRocket == null ? new EliteRocket(population.get(0)) : mEliteRocket;

    // get sum of all values
    for (Rocket r : population) {
      fuelSum += r.getCurFuelLevel();
      speedSum += r.getCurSpeed().abs();
      timeSum += r.getTime();
      distanceSum += r.getInitDistance() - r.getCurCoordinates().getY();
    }

    if (topRockets.size() < 6) {
      // fill topRockets in first run
      for (Rocket r : population) {
        topRockets.add(new EliteRocket(r));
      }
    } else {
      // include the topRockets into the sum
      for (EliteRocket er : topRockets) {
        fuelSum += er.getCurFuelLevel();
        speedSum += er.getCurSpeed().abs();
        timeSum += er.getTime();
        distanceSum += er.getInitDistance() - er.getCurCoordinates().getY();
      }
    }

    // set distance to 1 if smaller than 1
    distanceSum = distanceSum < 1 ? 1 : distanceSum;

    // recalc fitness for the topRockets proportionate to the current pop
    for (EliteRocket er : topRockets) {
      er.calculateAndSetFitness(fuelSum, timeSum, speedSum, distanceSum);
    }
    sortTopRocketsByFitness();


    // Choose best rocket of population depending on their fitness
    for (Rocket r : population) {
      r.calculateAndSetFitness(fuelSum, timeSum, speedSum, distanceSum);
      if (mGeneration > 2) {
        for (int i = 0; i < topRockets.size(); i++) {
          if (topRockets.get(i).getTotalFitness() < r.getTotalFitness()) {
            topRockets.add(i, new EliteRocket(r));
            topRockets.get(i).calculateAndSetFitness(fuelSum, timeSum, speedSum, distanceSum);
            topRockets.remove(topRockets.size() - 1);
            break;
          }
        }
      }
      // If rockets are chosen depending on their fitness, which is the sum of every fitness parameter and its weight.
      if (best == null || r.getTotalFitness() > best.getTotalFitness()) {
        secondBest = best;
        best = r;
      } else if (secondBest == null || r.getTotalFitness() > secondBest.getTotalFitness()) {
        secondBest = r;
      }
    }

    parents.add(best);
    parents.add(secondBest);
    prepareCanvasForNextGen(best, secondBest);

    boolean useElite = false;
    if (best.getTotalFitness() > mEliteRocket.getTotalFitness()) {
      if (mEliteRocket.getTotalFitness() > parents.get(1).getTotalFitness()) {
        parents.set(1, mEliteRocket);
        System.out.println("Use old Elite as secoundbest | fitness Elite: " + mEliteRocket.getTotalFitness());
      }
      Rocket bestForTextArea = best;
      mEliteRocket = new EliteRocket(best);
      Platform.runLater(() -> mInterface.mTextArea.appendText("Set rocket" + bestForTextArea.getRocketID() + " as new elite!\n"));
    } else if (best.getGenerationId() > 1) {
      useElite = true;
      Rocket bestForTextArea = best;
      Platform.runLater(() ->
          mInterface.mTextArea.appendText(
              "Using Elite!\n" +
                  "Best Fitness: " + bestForTextArea.getTotalFitness() + "\n" +
                  "Elite Fitness: " + mEliteRocket.getTotalFitness() + "\n"
          )
      );
    }
    if (isRunning) {
      createNextGeneration(parents, useElite);
    }
  }

  public void createNextGeneration(List<Rocket> pParents, boolean pUseElite) {
    // clear population
    population = new ArrayList<>();
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    ArrayList<Coordinate2D> newProcessAcc = new ArrayList<>();
    ArrayList<Coordinate2D> individualProcessAcc;
    //Rocket curRocket;
    System.out.println("Interface Generations: " + mInterface.mSpinnerInitGenerations.getValue());

    // loop through best rockets if generations
    if (mGeneration > mInterface.mSpinnerInitGenerations.getValue()) {
      if (write) {
        createDocument();
      }
      for (int i = 0; i < topRockets.size(); i++) {
        if (write) {
          writeInFile(topRockets.get(i));
        }
        population.add(new Rocket(
            mGeneration,
            i,
            topRockets.get(i).mInitSpeed.getX(),
            topRockets.get(i).mInitSpeed.getY(),
            topRockets.get(i).mInitFuelLevel,
            topRockets.get(i).getInitDistance(),
            topRockets.get(i).getProcessAcc()

        ));
      }

      // start all runnables
      for (Rocket r : population) {
        if (mInterface.mRadioButtonFastMode.isSelected()){
          mThreadPool.execute(new FastRocketRunnable(r, mInterface));
        } else {
          mThreadPool.execute(new RocketRunnable(r, mInterface));
        }
      }
      if(write) {
        writer.close();
        write = false;
      }
      System.out.println("DONE! BEST RESULTS FOUND");
      Thread t = new Thread(() -> {
        try {
          mThreadPool.stop();
          mThreadPool.awaitTermination();
        } catch (TimeoutException e) {
          e.printStackTrace(System.err);
        }
        mGeneration++;
        mInterface.drawTransparentRect();
        mInterface.displayGrid(30, 30);
        createNextGeneration(population, false);
      });
      t.start();
    } else {
    /*
     * #### CROSSOVER ####
     */
      int longerParent = pParents.get(0).getProcessAcc().size() > pParents.get(1).getProcessAcc().size() ? 0 : 1;
      int shorterParent = longerParent == 0 ? 1 : 0;
      for (int i = 0; i < pParents.get(shorterParent).getProcessAcc().size(); i++) {
        if (Math.random() <= 0.5) {
          newProcessAcc.add(new Coordinate2D(pParents.get(longerParent).getProcessAcc().get(i).getX(), pParents.get(longerParent).getProcessAcc().get(i).getY()));
        } else {
          newProcessAcc.add(new Coordinate2D(pParents.get(shorterParent).getProcessAcc().get(i).getX(), pParents.get(shorterParent).getProcessAcc().get(i).getY()));
        }
      }
    /*
     * #### MUTATION ####
     */
      for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
        if (i == 0 && pUseElite) {
          this.population.add(new Rocket(
              mGeneration,
              0,
              mEliteRocket.mInitSpeed.getX(),
              mEliteRocket.mInitSpeed.getY(),
              mEliteRocket.mInitFuelLevel,
              mEliteRocket.getInitDistance(),
              mEliteRocket.getProcessAcc()
          ));
          continue;
        }

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
        this.population.add(rocket);
      }


      // start all runnables
      for (Rocket r : population) {
        if (mInterface.mRadioButtonFastMode.isSelected()){
          mThreadPool.execute(new FastRocketRunnable(r, mInterface));
        } else {
          mThreadPool.execute(new RocketRunnable(r, mInterface));
        }
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
   *
   * @param pRocket1 the first parent rocket
   * @param pRocket2 the second parent rocket
   */
  private void prepareCanvasForNextGen(Rocket pRocket1, Rocket pRocket2) {
    Platform.runLater(() -> {
      mInterface.drawTransparentRect();
      mInterface.displayGrid(30, 30);
      double xFactor = mInterface.mRadioButtonFastMode.isSelected() ? FastRocketRunnable.COORD_X_FACTOR : RocketRunnable.COORD_X_FACTOR;
      double yFactor = mInterface.mRadioButtonFastMode.isSelected() ? FastRocketRunnable.COORD_Y_FACTOR : RocketRunnable.COORD_Y_FACTOR;
      for (int i = 0; i < 2; i++) {
        Rocket r = i == 0 ? pRocket1 : pRocket2;
        mGC.setFill((Color) RocketConstants.COLOR_PALETTE[r.getRocketID()][0]);
        mGC.setStroke((Color) RocketConstants.COLOR_PALETTE[r.getRocketID()][0]);
        mGC.fillOval(r.getCurCoordinates().getX() * xFactor - 3,
            r.getCurCoordinates().getY() * yFactor - 3, 6, 6);
        mGC.strokeText(String.valueOf(r.getRocketID()), r.getCurCoordinates().getX() * xFactor - 3,
            r.getCurCoordinates().getY() * yFactor - 3);
      }
      mTextArea.appendText("Generation " + pRocket1.getGenerationId() + ":\n" +
          "Parents:\n" +
          "Rocket " + pRocket1.getRocketID() + " fitness: " + pRocket1.getTotalFitness() + "\n" +
          "Rocket " + pRocket2.getRocketID() + " fitness: " + pRocket2.getTotalFitness() + "\n");
    });
  }
  public void createDocument(){
    try {
      writer = new PrintWriter("best-results-accelerations.txt", "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void writeInFile(Rocket curRocket) {
    writer.println(" ");
    writer.println("########## RESULTS ROCKET: " + curRocket.getRocketID() +
        " Generation:  " + curRocket.getGenerationId() + "##########");
    for (int j = 0; j < curRocket.getProcessAcc().size(); j++) {
      writer.println("Coordinates: " + curRocket.getProcessAcc().get(j));
      writer.println("Abs value: " + curRocket.getProcessAcc().get(j).abs());
    }
  }

  public void sortTopRocketsByFitness() {
    Collections.sort(topRockets, new Comparator<EliteRocket>() {
      @Override
      public int compare(EliteRocket o1, EliteRocket o2) {
        return (o2.getTotalFitness() > o1.getTotalFitness()) ? 1 : -1;
      }
    });
  }
}

import gui.CalcCompleteBox;
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
public class GeneticLearner {

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
   * list of population which gets recreated on every generation
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
  private boolean mWriteToDoc;

  /**
   * mWriter for text document
   */
  private PrintWriter mWriter;

  /**
   * holds a boolean value that is set to true after the final popup has been displayed
   */
  private boolean finalPopUpShown = false;

  /**
   * the constructor of the genetic learner
   * @param pInterface the Interface from the Application Thread
   */
  public GeneticLearner(Interface pInterface) {
    mInterface = pInterface;
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    mTextArea = pInterface.mTextArea;
    mCanvas = pInterface.mCanvas;
    mGC = pInterface.mGC;
    mWriteToDoc = mInterface.mWriteToDocumentCheckBox.selectedProperty().getValue();
  }

  /**
   * Create the initial random population of rockets
   */
  public void initPopulationRandom() {
    if (mInterface.mRadioButtonSlowMode.isSelected()) {
      Platform.runLater(() -> mInterface.mTextArea.appendText("Starting Calculation of Generation " + mGeneration + "\n"));
    } else {
      Platform.runLater(() -> mInterface.mFastSimTextArea.appendText("Starting Calculation of Generation " + mGeneration + "\n"));
    }
    long startTime = System.currentTimeMillis();

    // processAcc keeps the acceleration values over time of a certain rocket
    // processAcc gets filled randomly the first time
    ArrayList<Coordinate2D> processAcc;
    for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      processAcc = new ArrayList<>();
      for (int d = 0; d <= 100; d++) {
        processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
      }

      // instantiate rocket with random process speed.
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
      if (mInterface.mRadioButtonFastMode.isSelected()) {
        mThreadPool.execute(new FastRocketRunnable(r, mInterface));
      } else {
        mThreadPool.execute(new RocketRunnable(r, mInterface));
      }
    }


    // thread which waits until all runnables terminated.
    Thread t = new Thread(() -> {
      try {
        mThreadPool.stop();
        mThreadPool.awaitTermination();
        long endTime = System.currentTimeMillis() - startTime;
        if (mInterface.mRadioButtonSlowMode.isSelected()) {
          Platform.runLater(() -> mInterface.mTextArea.appendText("Calculation of Generation " +
              (mGeneration - 1) + " took " + endTime + "ms\n\n"));
        } else {
          Platform.runLater(() -> mInterface.mFastSimTextArea.appendText("Calculation of Generation " +
              (mGeneration - 1) + " took " + endTime + "ms\n\n"));
        }
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

    // Variables to calculate the fitness of a rocket
    double fuelSum = 0.0f;
    double speedSum = 0.0f;
    double timeSum = 0.0f;
    double distanceSum = 0.0f;
    double landingPos = 1.19 * ((mCanvas.getWidth() / 2) / RocketRunnable.COORD_X_FACTOR);
    double distanceXSum = 0;
    Rocket best = null;
    Rocket secondBest = null;
    List<Rocket> parents = new ArrayList<>();

    // get sum of all values
    for (Rocket r : population) {
      fuelSum += r.getCurFuelLevel();
      speedSum += r.getCurSpeed().abs();
      timeSum += r.getTime();
      distanceSum += r.getCurCoordinates().getY();
      distanceXSum += Math.abs(landingPos - r.getCurCoordinates().getX());
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
        distanceSum += er.getCurCoordinates().getY();
        distanceXSum += Math.abs(landingPos - er.getCurCoordinates().getX());
      }
    }

    // include the elite rocket into the sum
    if (mEliteRocket == null) {
      mEliteRocket = new EliteRocket(population.get(0));
    } else {
      fuelSum += mEliteRocket.getFinalFuelLevel();
      speedSum += mEliteRocket.getFinalSpeed();
      timeSum += mEliteRocket.getFinalTime();
      distanceSum += mEliteRocket.getCurCoordinates().getY();
      distanceXSum += Math.abs(mEliteRocket.getFinalDistanceX());
    }

    // set distance to 1 if smaller than 1 - prevent division by zero
    distanceSum = distanceSum < 1 ? 1 : distanceSum;

    // recalculate fitness of mEliteRocket
    if (mInterface.mFlyToGoalModeCheckBox.isSelected()) {
      mEliteRocket.calculateAndSetFitnessReachGoalMode(distanceSum, distanceXSum, landingPos);
    } else {
      mEliteRocket.calculateAndSetFitnessNormalMode(fuelSum, timeSum, speedSum, distanceSum);
    }

    // recalculate fitness of the topRockets proportionate to the current population
    for (EliteRocket er : topRockets) {
      if (mInterface.mFlyToGoalModeCheckBox.isSelected()) {
        er.calculateAndSetFitnessReachGoalMode(distanceSum, distanceXSum, landingPos);
      } else {
        er.calculateAndSetFitnessNormalMode(fuelSum, timeSum, speedSum, distanceSum);
      }
    }

    // sort the top rockets by fitness DESC
    sortTopRocketsByFitness();

    // Choose best rocket of population depending on their fitness
    for (Rocket r : population) {
      if (mInterface.mFlyToGoalModeCheckBox.isSelected()) {
        r.calculateAndSetFitnessReachGoalMode(distanceSum, distanceXSum, landingPos);
      } else {
        r.calculateAndSetFitnessNormalMode(fuelSum, timeSum, speedSum, distanceSum);
      }

      // potentially substitute the top rockets with better rockets
      if (mGeneration > 2) {
        for (int i = 0; i < topRockets.size(); i++) {
          if (topRockets.get(i).getTotalFitness() < r.getTotalFitness()) {
            topRockets.add(i, new EliteRocket(r));
            if (mInterface.mFlyToGoalModeCheckBox.isSelected()) {
              topRockets.get(i).calculateAndSetFitnessReachGoalMode(distanceSum, distanceXSum, landingPos);
            } else {
              topRockets.get(i).calculateAndSetFitnessNormalMode(fuelSum, timeSum, speedSum, distanceSum);
            }
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

    // print rocket fitness after each generation - if in fast mode
    if (mInterface.mRadioButtonFastMode.isSelected()) {
      sortPopulationByFitness();
      List<Rocket> popForTextarea = population;
      Platform.runLater(() -> {
        for (Rocket r : popForTextarea) {
          mInterface.mFastSimTextArea.appendText(
              "rocket" + r.getRocketID() + " fitness: " + r.getTotalFitness() + "\n"
          );
        }
      });
    }

    // check if a new elite rocket has to be set
    if (best.getTotalFitness() > mEliteRocket.getTotalFitness()) {
      Rocket bestForTextArea = best;

      mEliteRocket = new EliteRocket(best);

      if (mInterface.mRadioButtonSlowMode.isSelected()) {
        Platform.runLater(() -> mInterface.mTextArea.appendText("Set rocket" + bestForTextArea.getRocketID() + " as new elite!\n"));
      } else {
        Platform.runLater(() -> mInterface.mFastSimTextArea.appendText("Set rocket" + bestForTextArea.getRocketID() + " as new elite!\n"));
      }
      // if the elite rocket is better than the best rocket use it as best rocket
    } else if (best.getGenerationId() > 1) {
      secondBest = best;
      best = mEliteRocket;

      // output to textareas
      Rocket bestForTextArea = best;
      if (mInterface.mRadioButtonSlowMode.isSelected()) {
        Platform.runLater(() ->
            mInterface.mTextArea.appendText(
                "Using elite rocket as parent!\n" +
                    "Best Fitness: " + bestForTextArea.getTotalFitness() + "\n" +
                    "Elite Fitness: " + mEliteRocket.getTotalFitness() + "\n"
            )
        );
      } else {
        Platform.runLater(() ->
            mInterface.mFastSimTextArea.appendText(
                "Using elite rocket as parent!\n" +
                    "Best Fitness: " + bestForTextArea.getTotalFitness() + "\n" +
                    "Elite Fitness: " + mEliteRocket.getTotalFitness() + "\n"
            )
        );
      }
    }

    parents.add(best);
    parents.add(secondBest);
    prepareCanvasForNextGen(best, secondBest);

    if (isRunning) {
      createNextGeneration(parents);
    }
  }

  /**
   * This method generates all new generations after the first one out of two given parent individuals.
   * It uses crossover to generate a new "genome" which acts as prototype for the new generation.
   * The individuals of the new generation differ because of mutations.
   * @param pParents the two parent individuals
   */
  public void createNextGeneration(List<Rocket> pParents) {
    if (mInterface.mRadioButtonSlowMode.isSelected() && mGeneration <= mInterface.mSpinnerInitGenerations.getValue()) {
      Platform.runLater(() -> mInterface.mTextArea.appendText("\nStarting Calculation of Generation " + mGeneration + "\n"));
    } else {
      Platform.runLater(() -> mInterface.mFastSimTextArea.appendText("\nStarting Calculation of Generation " + mGeneration + "\n"));
    }
    long startTime = System.currentTimeMillis();

    // clear population
    population = new ArrayList<>();
    mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
    ArrayList<Coordinate2D> newProcessAcc = new ArrayList<>();
    ArrayList<Coordinate2D> individualProcessAcc;

    // loop through best rockets if selected number of generations is reached
    if (mGeneration > mInterface.mSpinnerInitGenerations.getValue()) {
      if (mInterface.mRadioButtonFastMode.isSelected()) {
        Platform.runLater(() -> mInterface.mPrimaryStage.setScene(mInterface.mSimScene));
      }
      if (!finalPopUpShown) {
        String message = "Calculations finished after " + mInterface.mSpinnerInitGenerations.getValue() + " generations.\n" +
            "The top " + mInterface.mPopSizeDropDown.getValue() + " rockets will be looped until you hit Return or Exit.";
        Platform.runLater(() -> {
          finalPopUpShown = true;
          CalcCompleteBox.display("Calculation complete!", message);
        });
      }
      if (mWriteToDoc) {
        createDocument();
      }
      for (int i = 0; i < topRockets.size(); i++) {
        if (mWriteToDoc) {
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
        mThreadPool.execute(new RocketRunnable(r, mInterface));
      }
      if (mWriteToDoc) {
        mWriter.close();
        mWriteToDoc = false;
      }

      // new thread to wait for the runnables to finish and not disturb the app thread
      Thread t = new Thread(() -> {
        try {
          mThreadPool.stop();
          mThreadPool.awaitTermination();
        } catch (TimeoutException e) {
          e.printStackTrace(System.err);
        }
        mGeneration++;
        Platform.runLater(() -> {
          mInterface.drawTransparentRect();
          mInterface.displayGrid(30, 30);
        });
        if (isRunning) {
          createNextGeneration(population);
        }
      });
      t.start();

      // generate the new generation with crossover and mutations
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
        individualProcessAcc = new ArrayList<>();
        for (int j = 0; j < newProcessAcc.size(); j++) {
          if (Math.random() <= mInterface.mSpinnerMutationRate.getValue()) {
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
        if (mInterface.mRadioButtonFastMode.isSelected()) {
          mThreadPool.execute(new FastRocketRunnable(r, mInterface));
        } else {
          mThreadPool.execute(new RocketRunnable(r, mInterface));
        }
      }

      // new thread to wait for the runnables to finish and not disturb the app thread
      Thread t = new Thread(() -> {
        try {
          mThreadPool.stop();
          mThreadPool.awaitTermination();
          long endTime = System.currentTimeMillis() - startTime;
          if (mInterface.mRadioButtonSlowMode.isSelected()) {
            Platform.runLater(() -> mInterface.mTextArea.appendText("Calculation of Generation " +
                mGeneration + " took " + endTime + "ms\n\n"));
          } else {
            Platform.runLater(() -> mInterface.mFastSimTextArea.appendText("Calculation of Generation " +
                mGeneration + " took " + endTime + "ms\n\n"));
          }
        } catch (TimeoutException e) {
          e.printStackTrace(System.err);
        }
        mGeneration++;
        getFitness();
      });
      t.start();
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
    if (mInterface.mRadioButtonSlowMode.isSelected()) {
      System.out.println("Interface: " + mInterface.mSliderInitDistance.getValue() + "CoordYFact: " + RocketRunnable.COORD_Y_FACTOR);
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
  }

  /**
   * This method assigns a new PrintWrite to the global variable mWriter
   */
  public void createDocument() {
    try {
      mWriter = new PrintWriter("best-results-accelerations.txt", "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method writes information on the current rocket into a Document
   * @param curRocket
   */
  public void writeInFile(Rocket curRocket) {
    mWriter.println(" ");
    mWriter.println("########## RESULTS ROCKET: " + curRocket.getRocketID() +
        " Generation:  " + curRocket.getGenerationId() + "##########");
    for (int j = 0; j < curRocket.getProcessAcc().size(); j++) {
      mWriter.println("Coordinates: " + curRocket.getProcessAcc().get(j));
      mWriter.println("Abs value: " + curRocket.getProcessAcc().get(j).abs());
    }
  }

  /**
   * sort elite rockets by fitness DESC
   */
  public void sortTopRocketsByFitness() {
    Collections.sort(topRockets, new Comparator<EliteRocket>() {
      @Override
      public int compare(EliteRocket o1, EliteRocket o2) {
        return (o2.getTotalFitness() > o1.getTotalFitness()) ? 1 : -1;
      }
    });
  }

  /**
   * sort rockets by fitness DESC
   */
  public void sortPopulationByFitness() {
    Collections.sort(population, new Comparator<Rocket>() {
      @Override
      public int compare(Rocket o1, Rocket o2) {
        return (o2.getTotalFitness() > o1.getTotalFitness()) ? 1 : -1;
      }
    });
  }
}
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

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
  ThreadPool mThreadPool;
  final Duration timeout = Duration.ofSeconds(30);
  ExecutorService executor = Executors.newSingleThreadExecutor();
  double mutation = 10;
  int distance = 100;

  /**
   * holds the information, if the genetic learner should generate new rockets and, thus, continue to work
   */
  private boolean isRunning = true;

  /**
   * TODO put constants into new file like {@link RocketConstants}
   * Goal values to be reached
   */
  int goalSpeed = 100;
  int goalEndFuel = 10;
  int goalTime = 0;
  /**
   * Importance of goals
   */
  // float weightSpeed = 0.1f;
  float weightEndFuel = 0.8f;
  float weightTime = 0.2f;
  // Fuel consumption per time
  float fuelConsumption = 10f;
  Planet mPlanet;
  final Canvas mCanvas;
  GraphicsContext mGC;
  TextArea mTextArea;
  Interface mInterface;
  int mGeneration = 1;

  List<Rocket> population = new ArrayList<Rocket>();
  List<Rocket> parents = new ArrayList<>();

  public GeneticLearningAbstract(Interface pInterface){
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
  public void createPopulationRandom() {
    ArrayList<Coordinate2D> processAcc;
      for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
        processAcc = new ArrayList<>();
        for (int d = 0; d <= 100000; d++) {
            processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
        }
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
        //printPopulation();

      });
      t.start();
    }


  /**
   * Calculate fitness of population
   */
     public void getFitness(){
       /**
        * Variables needed to calculate fitness level of rocket
        * TODO: Fitness level should depend on how important one parameter is -> implement weights of the fitness results e.g time is not so necessary as fuel
        * TODO: Maybe create a class to handle fitness values
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
        float choosingProbability;
        float total = 0;
        double randomVal;

        Rocket currRocket;
        Rocket best = null;
        Rocket secondBest = null;

       parents.clear();

       for (Integer j = 0; j < this.population.size(); j++) {
        fuelSum+= population.get(j).getCurFuelLevel();
        speedSum+= population.get(j).getCurSpeed().abs();
        timeSum+= population.get(j).getTime();
        distanceSum+= population.get(j).getInitDistance() - population.get(j).getCurCoordinates().getY() + mPlanet.getRadius();
       }
        /**
         * Choose best items of pupulation depending on their distance to the goal value.
         //*/
        for (Integer j = 0; j < this.population.size(); j++) {
            currRocket = population.get(j);
            fitnessFuel = (float)(currRocket.getCurFuelLevel()/fuelSum);
            if(fitnessFuel<= 0){
              fitnessFuel = 0;
            }
            fitnessTime = 1-(currRocket.getTime()/timeSum);
            fitnessSpeed = 1-(float)(currRocket.getCurSpeed().abs()/speedSum);
            fitnessOld = fitnessAll;
            fitnessDistance = 1-((float)(currRocket.getInitDistance() - population.get(j).getCurCoordinates().getY() + mPlanet.getRadius())/distanceSum);
            if(fitnessDistance <= 0){
              fitnessDistance = 0;
            }
            fitnessAll = (float)(0.3*fitnessFuel + 0.1*fitnessTime  + 0.2*fitnessSpeed + 0.4*fitnessDistance);
            currRocket.setTotalFitness(fitnessAll);
            total+=fitnessAll;
            System.out.println("Rocket ID: " + currRocket.getRocketID() + "Fitness all: " + ((fitnessAll)) + "Cur Distance:" + (currRocket.getInitDistance() - population.get(j).getCurCoordinates().getY() + mPlanet.getRadius()) + "Cur Speed: " + currRocket.getCurSpeed().abs() + "Cur Fuel: " + currRocket.getCurFuelLevel());
          /**
           * If rockets are chosen depends on fitnessAll, which is the sum of every fitnessparameter an it's weight.
           */
          if(fitnessAll > fitnessOld){
              if(best == null){
                best = currRocket;
              }else {
                secondBest = best;
                best = currRocket;
              }
              if(secondBest == null){
                secondBest = currRocket;
              }
            }
        }
        /**
         * Output of best rockets from this thread
         //*/

       /*for (int j = 0; j < population.size(); j++){
         currRocket = population.get(j);
         choosingProbability = currRocket.getTotalFitness()/total;
         currRocket.setChoosingProbability(choosingProbability);
         System.out.println("Choosing Prob Rocket: " + currRocket.getRocketID() + "Prob: " + choosingProbability);
       }
       float prev = 0;
       for (int j = 0; j < population.size(); j++){
         currRocket = population.get(j);
         currRocket.setCumulativeProbabilities(currRocket.getChoosingProbability() + prev);
         prev+= currRocket.getChoosingProbability();
       }
       parents.clear();
       for (int j = 0; j < population.size(); j++) {
         randomVal = Math.random();
         if (randomVal > population.get(j).getCumulativeProbabilities() && randomVal < population.get(j+1).getCumulativeProbabilities()){
           parents.add(population.get((j+1)));
         }
         else{
           parents.add(population.get(j));
         }
       }*/

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
    public void createNextGeneration(){
      population.clear();
      mThreadPool = ThreadPool.getInstance(mInterface.mPopSizeDropDown.getValue());
       //mThreadPool.terminate();
      ArrayList<Coordinate2D> processAcc;
      int z = 0;
      //for (int i = 0; i < mInterface.mPopSizeDropDown.getValue(); i++) {
      for (int i = 0; i <  mInterface.mPopSizeDropDown.getValue(); i++) {
          System.out.println("RandNumber: " + (Math.random() * ((2))));
        processAcc = new ArrayList<>();
        if(i<=1) {
            if (parents.get(0).getProcessAcc().size() > parents.get(1).getProcessAcc().size()) {
                for (int d = 0; d < parents.get(1).getProcessAcc().size(); d++) {
                    if (d % mutation == 0) {
                        processAcc.add(new Coordinate2D((parents.get(d%2).getProcessAcc().get(d).getX()*(Math.random() * ((2)) - 1)), parents.get(d%2).getProcessAcc().get(d).getY()*(Math.random() * ((2)) - 1)));
                    }
                    processAcc.add(new Coordinate2D(parents.get(d%2).getProcessAcc().get(d).getX(), parents.get(d%2).getProcessAcc().get(d).getY()));
                }
            } else {
                for (int d = 1; d < parents.get(0).getProcessAcc().size(); d++) {
                    if (d % mutation == 0) {
                        processAcc.add(new Coordinate2D((parents.get(d%2).getProcessAcc().get(d).getX()*(Math.random() * ((2)) - 1)), parents.get(d%2).getProcessAcc().get(d).getY()*(Math.random() * ((2)) - 1)));
                    }
                    processAcc.add(new Coordinate2D(parents.get(d%2).getProcessAcc().get(d).getX(), parents.get(d%2).getProcessAcc().get(d).getY()));
                }
            }
        }else{
            for (int d = 0; d < parents.get(0).getProcessAcc().size(); d++) {
                processAcc.add(new Coordinate2D((Math.random() * ((5)) - 2.5), Math.random() * ((200)) - 100));
            }
            System.out.println("RandRocket");

        }

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


      Thread t = new Thread (() -> {
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



//    /**
//     * Optimize parents for new population
//     */
//    public void manipulateParents(){
//        Double optimumvalue = 0.0;
//        for(Integer i = 0; i < parents.size(); i++){
//            System.out.println("Parent " + i + " = " + parents.get(i));
//            optimumvalue += parents.get(i);
//        }
//        optimumvalue = optimumvalue/2;
//        System.out.println("optimized step 1 " + optimumvalue);
//        calculateNewPopulation(optimumvalue);
//    }
//    public void variatePopulation(){
//
//    }
//
//    /**
//     * Set new population of rockets
//     * @param optimumvalue
//     //*/
//    public void calculateNewPopulation(Double optimumvalue){
//        population.clear();
//        Integer sign = 1;
//        for(Integer i = 0; i < 4; i++){
//            this.population.add((double) (optimumvalue - sign*(Math.random() * ((2) + 1))));
//            sign = sign * (-1);
//        }
//        printPopulation();
//    }

  /**
   * Print out current population
   //*/
  public void printPopulation() {
    System.out.println("Current Population:");
    for (int j = 0; j < this.population.size(); j++) {
      System.out.println("Koordx" + population.get(j).getCurCoordinates().getX() + " KoordY: " + population.get(j).getCurCoordinates().getY() +  "Rocket" + j + "|| Speed: " + population.get(j).getCurSpeed().abs() + "  Fuel:  " + population.get(j).getCurFuelLevel());
    }
  }

  public void terminate() {
    isRunning = false;
  }
  //Test

  /*public void simulateFly() {
    Rocket currentRocket;
    double time;
    for (int i = 0; i < 4; i++) {
      currentRocket = this.population.get(i);
      time = distance / currentRocket.getCurSpeed().getX();
      currentRocket.setTime(time);
      if (currentRocket.getCurFuelLevel() - time * fuelConsumption <= 0) {
        currentRocket.setFuelLeft(0);
      } else {
        currentRocket.setFuelLeft(currentRocket.getCurFuelLevel() - time * fuelConsumption);
      }
      //currentRocket;
      System.out.println("Benötigte Zeit: " + currentRocket.getmTime() + "  Noch vorhandener Treibstoff: " + currentRocket.getFuelLeft());
    }
  }

  public void getFitness() {
    Rocket currentRocket;
    //float timeSum = 0f;
    float fuelSum = 0f;
    float fitnessTime;
    float fitnessFuel;
    float fitnessAll;
    for (int i = 0; i < 4; i++) {
      currentRocket = this.population.get(i);
      timeSum += currentRocket.getmTime();
      fuelSum += currentRocket.getFuelLeft();
    }
    for (int j = 0; j < 4; j++) {
      fitnessTime = 1 - (this.population.get(j).getmTime() / timeSum);
      fitnessFuel = this.population.get(j).getFuelLeft() / fuelSum;
      fitnessAll = weightEndFuel * fitnessFuel + weightTime * fitnessTime;
      System.out.println("Rakete  " + j +
        " Fitness Zeit: " + (((fitnessTime)) * 100) + "%" +
        " Fitness Sprit: " + ((fitnessFuel) * 100) + "%" +
        " Fitness Combined: " + (fitnessAll * 100) + "%");
    }
  }*/

  // time_rating = weightTime * time1/time1+time2+time3+time4;
  // fuel_rating = weightFuel * time1/time1+time2+time3+time4;

}

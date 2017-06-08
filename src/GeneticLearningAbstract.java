import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ludwig on 17.05.17.
 */

/**
 * THIS IS A PROTOTYPE CLASS FOR UNDERSTANDING GENETIC ALGORITHM's
 */
public class GeneticLearningAbstract {
  ExecutorService mThreadPool = Executors.newCachedThreadPool();
  double mutation = 0.01;
  int distance = 100;
  /**
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

  List<Rocket> population = new ArrayList<Rocket>();
  List<Double> parents = new ArrayList<Double>();
  public GeneticLearningAbstract(Planet pPlanet, TextArea pTextArea, Canvas pCanvas, GraphicsContext pGC){
    this.mPlanet = pPlanet;
    this.mTextArea = pTextArea;
    this.mCanvas = pCanvas;
    this.mGC = pGC;
  }
  /**
   * Create random population and values of rockets
   */
  public void createPopulationRandom() {
    for (Integer i = 0; i < 4; i++) {
      Rocket rocket = new Rocket(
        //Math.random() * ((100) + 1)
        (float) (Math.random() * ((100) + 1)),
        (float) (Math.random() * ((100) + 1)),
        (float) (1),
        50,
        50,
        1000000);
      mThreadPool.execute(new RocketRunnable(rocket,mPlanet, mTextArea, mCanvas, mGC));
      this.population.add(rocket);

    }
    //getFitness();
    printPopulation();
  }


  /**
   * Calculate fitness of population
   */
//     public void getFitness(){
//        List<Double> sub = new ArrayList<Double>();
//        Double min = 0.0;
//        Double secndmin = 0.0;
//        Double item = 0.0;
//        Double itemorig = 0.0;
//        /**
//         * Choose best items of pupulation depending on their distance to the goal value.
//         //*/
//        for (Integer j = 0; j < this.population.size(); j++) {
//            item = (double)Math.abs(goal - population.get(j));
//            itemorig =  (double)population.get(j);
//            if(j == 0){
//                min = itemorig;
//                secndmin = itemorig;
//            }
//            else if(Math.abs(goal-min) > item){
//                secndmin = min;
//                min = itemorig;
//
//            }else if(Math.abs(goal-secndmin) > item){
//                secndmin = itemorig;
//            }
//            //System.out.println("Sub:  " + item);
//            //sub.add(item);
//        }
//        System.out.println("Best Values ||  Min 1:  " + min + "  Min: 2  " + secndmin);
//        /**
//         * Set Parents
//         //*/
//        parents.clear();
//        parents.add(min);
//        parents.add(secndmin);
//    }
//
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
//
  /**
   * Print out current population
   //*/
  public void printPopulation() {
    System.out.println("Current Population:");
    for (int j = 0; j < this.population.size(); j++) {
      System.out.println("Rocket" + j + "|| Speed: " + population.get(j).getCurSpeed().abs() + "  Fuel:  " + population.get(j).getCurFuelLevel());
    }
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
      System.out.println("Rakete " + j +
        " Fitness Zeit: " + (((fitnessTime)) * 100) + "%" +
        " Fitness Sprit: " + ((fitnessFuel) * 100) + "%" +
        " Fitness Combined: " + (fitnessAll * 100) + "%");
    }
  }*/

  // time_rating = weightTime * time1/time1+time2+time3+time4;
  // fuel_rating = weightFuel * time1/time1+time2+time3+time4;

}

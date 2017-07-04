import java.util.ArrayList;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Rocket {
  /**
   * holds the corresponding generation id of the rocket
   */
  public final int mGenerationId;

  /**
   * holds the rocket id
   */
  public final int mRocketId;

  /**
   * holds the initial speed of the rocket
   */
  public final Coordinate2D mInitSpeed;

  /**
   * holds the current speed of the rocket
   */
  private Coordinate2D mCurSpeed;

  /**
   * holds the initial acceleration of the rocket
   */
  public final Coordinate2D mInitAcceleration;

  /**
   * holds the current acceleration of the rocket
   */
  private Coordinate2D mCurAcceleration;

  /**
   * holds the initial fuel level of the rocket
   */
  public final double mInitFuelLevel;

  /**
   * holds the current fuel level of the rocket
   */
  private double mCurFuelLevel;

  /**
   * holds the initial distance of the rocket to the planet's surface
   */
  private double mInitDistance;

  /**
   * holds the used passed flight time of the rocket
   */
  public int mTime;

  /**
   * holds the initial coordinates of the rocket
   */
  private Coordinate2D mInitCoordinates;

  /**
   * A ArrayList of the acceleration process <acceleration>
   */
  private ArrayList<Coordinate2D> mProcessAcc = new ArrayList<>();

  /**
   * holds the index for the current acceleration of the rocket
   */
  private int mProcessAccIndex = 0;

  /**
   * holds the current coordinates of the Rocket
   */
  private Coordinate2D mCurCoordinates;

  /**
   * fitness values
   */
  private double mTotalFitness;

  /**
   * choosing probability
   */
  private double mChoosingProbability;

  /**
   * necessary for choosing randomly a parent, but the parent with highest store is more likely to be chosen
   */
  private double mCumulativeProbabilities;

  /**
   * holds all coordinates for the fast mode
   */
  private ArrayList<Coordinate2D> mCoordinateList = new ArrayList<>();

  /**
   * constructor for rocket
   * @param pGenerationId the generation id of the Rocket
   * @param pRocketId the id of the rocket
   * @param pInitSpeed the initial speed of the rocket
   * @param pInitFuelLevel the initial fuel level of the rocket
   * @param pInitDistance the initial distance to the surface of the rocket
   */
  public Rocket(int pGenerationId, int pRocketId, Coordinate2D pInitSpeed, double pInitFuelLevel, double pInitDistance) {
    mGenerationId = pGenerationId;
    mRocketId = pRocketId;
    mInitSpeed = pInitSpeed;
    mCurSpeed = pInitSpeed;
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = RocketConstants.INIT_COORDINATES;
    mCurCoordinates = RocketConstants.INIT_COORDINATES;
    mInitDistance = pInitDistance;
    mInitAcceleration = RocketConstants.INIT_ACCELERATION;
    mCurAcceleration = RocketConstants.INIT_ACCELERATION;
    mTime = 0;
  }

  /**
   * constructor for rocket
   * @param pGenerationId the generation id of the Rocket
   * @param pRocketId the id of the rocket
   * @param pSpeedX the initial x speed of the rocket
   * @param pSpeedY the initial y speed of the rocket
   * @param pInitFuelLevel the initial fuel level of the rocket
   * @param pInitDistance the initial distance to the surface of the rocket
   */
  public Rocket(int pGenerationId, int pRocketId, double pSpeedX, double pSpeedY, double pInitFuelLevel, double pInitDistance, ArrayList<Coordinate2D> pProcessAcc) {
    mGenerationId = pGenerationId;
    mRocketId = pRocketId;
    mInitSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mCurSpeed = new Coordinate2D(pSpeedX, pSpeedY);
    mInitFuelLevel = pInitFuelLevel;
    mCurFuelLevel = pInitFuelLevel;
    mInitCoordinates = RocketConstants.INIT_COORDINATES;
    mCurCoordinates = RocketConstants.INIT_COORDINATES;
    mInitDistance = pInitDistance;
    mInitAcceleration = RocketConstants.INIT_ACCELERATION;
    mCurAcceleration = RocketConstants.INIT_ACCELERATION;
    mTime = 0;
    mProcessAcc = pProcessAcc;
  }

  /**
   * getters and setters for rocket member variables
   */
  public Coordinate2D getCurSpeed() {
    return mCurSpeed;
  }

  public void setCurSpeed(Coordinate2D pCurSpeed) {
    mCurSpeed = pCurSpeed;
  }

  public double getCurFuelLevel() {
    return mCurFuelLevel;
  }

  public void setCurFuelLevel(double pCurFuelLevel) {
    mCurFuelLevel = pCurFuelLevel;
  }

  public Coordinate2D getCurCoordinates() {
    return mCurCoordinates;
  }

  public void setCurCoordinates(double pX, double pY) {
    mCurCoordinates = new Coordinate2D(pX, pY);
  }

  public void setCurCoordinates(Coordinate2D pCoord) {
    mCurCoordinates = pCoord;
  }

  public double getInitDistance() {
    return mInitDistance;
  }

  public ArrayList<Coordinate2D> getProcessAcc(){
    return mProcessAcc;
  }

  public void setProcessAcc(ArrayList<Coordinate2D> pProcessAcc) {
    mProcessAcc = pProcessAcc;
  }

  public int getTime() {
    return mTime;
  }

  public void setTime(int pTime) {
    mTime = pTime;
  }

  public void setCurAcceleration(Coordinate2D pCurAcceleration){
    mCurAcceleration = pCurAcceleration;
  }

  public Coordinate2D getCurAcceleration() {
    return mCurAcceleration;
  }

  public int getRocketID() {
    return mRocketId;
  }

  public int getGenerationId(){
    return mGenerationId;
  }

  public void setInitCoordinates(Coordinate2D pInitCoordinates) {
    mInitCoordinates = pInitCoordinates;
    mCurCoordinates = pInitCoordinates;
  }

  public Coordinate2D getInitCoordinates() {
    return mInitCoordinates;
  }

  //Functions for genetic learner
  public double getTotalFitness() {
    return mTotalFitness;
  }

  private void setTotalFitness(double pTotalFitness) {
    mTotalFitness = pTotalFitness;
  }
  public double getChoosingProbability() {
    return mChoosingProbability;
  }

  public void setChoosingProbability(double pChoosingProbability) {
    mChoosingProbability = pChoosingProbability;
  }

  public double getCumulativeProbabilities() {
    return mCumulativeProbabilities;
  }

  public void setCumulativeProbabilities(double pCumulativeProbabilities) {
    mCumulativeProbabilities = pCumulativeProbabilities;
  }

  public int getProcessAccIndex() {
    return mProcessAccIndex;
  }

  public void setProcessAccIndex(int pProcessAccIndex) {
    mProcessAccIndex = pProcessAccIndex;
  }

  public ArrayList<Coordinate2D> getCoordinateList() {
    return mCoordinateList;
  }

  public void setCoordinateList(ArrayList<Coordinate2D> pCoordinateList) {
    mCoordinateList = pCoordinateList;
  }

  public void addCurAccToProcessAcc() {
    mProcessAcc.add(mCurAcceleration);
  }

  public void calculateAndSetFitnessNormalMode(double pFuelSum, double pTimeSum, double pSpeedSum, double pDistanceSum){
    double fitness =
        // minimize fuel consumption
        (mCurFuelLevel / pFuelSum) * AlgorithmConstants.RATING_FUEL +
        // minimize usage of time
        (1 - (mTime / pTimeSum)) * AlgorithmConstants.RATING_TIME +
        // minimize end speed
        (1 - (mCurSpeed.abs() / pSpeedSum)) * AlgorithmConstants.RATING_SPEED +
        // minimize distance to planet
        (1 - ((mInitDistance - mCurCoordinates.getY()) / pDistanceSum)) * AlgorithmConstants.RATING_DISTANCE;
    // remove landing bonus
    fitness *= (1 - AlgorithmConstants.LANDING_BONUS);
    // add landing bonus if rocket has landed
    if (mInitDistance - mCurCoordinates.getY() <= 0) {
      fitness += AlgorithmConstants.LANDING_BONUS;
    }
    this.setTotalFitness(fitness);
  }
  public void calculateAndSetFitnessReachGoalMode(double pDistanceSum, double pDistanceXSum, double landingPos){
    double fitness = 0.4*(1-Math.abs((landingPos-this.getCurCoordinates().getX())/pDistanceXSum)) + 0.6*((this.mCurCoordinates.getY())/pDistanceSum);
    System.out.println("Fitness all: " + fitness + " fitnessX: " + (1-Math.abs((landingPos-this.getCurCoordinates().getX())/pDistanceXSum)) + " fitnessY " + ((this.mCurCoordinates.getY())/pDistanceSum));
    System.out.println("Distance: " + (landingPos-this.getCurCoordinates().getX()) + "PosY: " + this.getCurCoordinates().getY() + "PosX: " +this.getCurCoordinates().getX());
    this.setTotalFitness(fitness);
  }
}
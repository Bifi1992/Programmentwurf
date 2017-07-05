/**
 * Created by y.brisch on 26.06.17.
 */
public final class AlgorithmConstants {

  /**
   * prohibit accidental instantiation
   */
  private AlgorithmConstants() {
  }

  /**
   * holds the minimal populations size
   */
  public static final int MIN_POP_SIZE = 4;

  /**
   * holds the maximal populations size
   */
  public static final int MAX_POP_SIZE = 12;

  /**
   * holds the bonus for a successful landing
   */
  public static final double LANDING_BONUS = 0.2;

  /**
   * holds the initial mutation factor
   */
  public static final double INIT_MUTATION = 0.1;

  /**
   * holds the minimal mutation factor
   */
  public static final double MIN_MUTATION = 0;

  /**
   * holds the maximal mutation factor
   */
  public static final double MAX_MUTATION = 0.9;

  /**
   * Holds the min initial generations
   */
  public static final int MIN_GENERATIONS = 5;

  /**
   * Holds the min initial generations
   */
  public static final int MAX_GENERATIONS = 800;

  /**
   * The ratings for the fitness function. The higher the rating the more emphasis lies on the specific factor.
   * The ratings have to add up to 1
   */
  public static final double RATING_TIME = 0.2;
  public static final double RATING_FUEL = 0.2;
  public static final double RATING_DISTANCE = 0.55;
  public static final double RATING_SPEED = 0.05;

}

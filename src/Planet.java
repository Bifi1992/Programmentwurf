/**
 * Created by y.brisch on 11.05.17.
 */
public class Planet {
  /**
   * the radius of the planet
   */
  public int mRadius;

  /**
   * holds the mass of the planet
   */
  public int mMass;

  /**
   * holds the gravitational constant to calculate the gravitational force
   */
  public static final double GRAVITATIONAL_CONSTANT = 0.000000000667;

  public Planet(int pRadius, int pMass) {
    mRadius = pRadius;
    mMass = pMass;
  }
}

/**
 * Created by y.brisch on 11.05.17.
 */
public enum Planet {
  MERCURY(3.303e23, 2.440e6, 20000),
  VENUS(4.869e24, 6.052e6, 6000),
  EARTH(5.976e24, 6.371e6, 6000),
  MARS(6.421e23, 3.39e6, 20000),
  JUPITER(1.9e27, 6.9911e7, 3000),
  SATURN(5.688e26, 5.8232e7, 5000),
  URANUS(8.686e25, 2.5362e7, 6000),
  NEPTUNE(1.024e26, 2.4622e7, 5000),
  MOON(7.342e22, 1.738100e6, 50000);

  /**
   * holds the mass of the planet [kg]
   */
  private final double mMass;

  /**
   * holds the radius of the planet [m]
   */
  private final double mRadius;

  /**
   * holds the approximate landing time of a rocket on the planet
   */
  private final int mMaxLandingTime;

  /**
   * holds the gravitational constant to calculate the gravitational force [m^3/(kg * s^2)]
   */
  public static final double GRAVITATIONAL_CONSTANT = 0.000000000667;

  Planet(double pMass, double pRadius, int pTime) {
    mMass = pMass;
    mRadius = pRadius;
    mMaxLandingTime = pTime;
  }

  public double getMass() {
    return mMass;
  }

  public double getRadius() {
    return mRadius;
  }

  public int getMaxLandingTime() {
    return mMaxLandingTime;
  }
}

/**
 * Created by y.brisch on 11.05.17.
 */
public enum Planet {
  MERCURY(3.303e23, 2.440e6, 7500), //7500s
  VENUS(4.869e24, 6.052e6, 2500),  //2500s
  EARTH(5.976e24, 6.371e6, 2300),  //2300s
  MARS(6.421e23, 3.39e6, 6800),  //6800s
  JUPITER(1.9e27, 6.9911e7, 800),  //800s
  SATURN(5.688e26, 5.8232e7, 1750),  //1750s
  URANUS(8.686e25, 2.5362e7, 2200),  //2200s
  NEPTUNE(1.024e26, 2.4622e7, 1800),  //1800s
  MOON(7.342e22, 1.738100e6, 20000);   //20000s

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
  private final int mApproxLandingTime;

  /**
   * holds the gravitational constant to calculate the gravitational force [m^3/(kg * s^2)]
   */
  public static final double GRAVITATIONAL_CONSTANT = 0.000000000667;

  Planet(double pMass, double pRadius, int pTime) {
    mMass = pMass;
    mRadius = pRadius;
    mApproxLandingTime = pTime;
  }

  public double getMass() {
    return mMass;
  }

  public double getRadius() {
    return mRadius;
  }

  public int getApproxLandingTimeTime() {
    return mApproxLandingTime;
  }
}

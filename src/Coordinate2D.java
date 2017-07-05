/**
 * Created by y.brisch on 17.05.17.
 */
public class Coordinate2D {
  private double mX;
  private double mY;

  /**
   * A Custom 2D Vector
   *
   * @param pX the x coordinate
   * @param pY the y coordinate
   */
  Coordinate2D(double pX, double pY) {
    mX = pX;
    mY = pY;
  }

  /**
   * getters and setters of this class
   */
  public double getX() {
    return mX;
  }

  public double getY() {
    return mY;
  }

  public void setY(double pY) {
    this.mY = pY;
  }

  /**
   * This method calculates the angle between the x axis and the vector
   *
   * @return the angle between the x axis and the vector
   */
  public double getAngleXAxis() {
    return Math.atan((mY / mX));
  }

  /**
   * This method calculates the length of a vector
   *
   * @return the length of a vector
   */
  public double abs() {
    return Math.sqrt(mX * mX + mY * mY);
  }

  /**
   * Override the toString method for convenient output of coordinates
   *
   * @return the vector as String representation
   */
  @Override
  public String toString() {
    return "Coordinate2D{" +
        "mX=" + mX +
        ", mY=" + mY +
        '}';
  }
}

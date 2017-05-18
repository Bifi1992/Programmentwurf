/**
 * Created by y.brisch on 17.05.17.
 */
public class Coordinate2D {
  private double mX;
  private double mY;

  Coordinate2D() {
    mX = 0;
    mY = 0;
  }

  Coordinate2D(double pX, double pY) {
    mX = pX;
    mY = pY;
  }

  public double getX() {
    return mX;
  }

  public void setX(double mX) {
    this.mX = mX;
  }

  public double getY() {
    return mY;
  }

  public void setY(double mY) {
    this.mY = mY;
  }
}

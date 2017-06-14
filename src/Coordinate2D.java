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

  public void setX(double pX) {
    this.mX = pX;
  }

  public double getY() {
    return mY;
  }

  public void setY(double pY) {
    this.mY = pY;
  }

  public Coordinate2D getRounded(Coordinate2D pCoord) {
    return new Coordinate2D(Math.round(pCoord.getX()), Math.round(pCoord.getY()));
  }

  public double getAngleXAxis() {
    return Math.atan((mY/mX));
  }

  public double abs() {
    return Math.sqrt(mX*mX+mY*mY);
  }

  @Override
  public String toString() {
    return "Coordinate2D{" +
        "mX=" + mX +
        ", mY=" + mY +
        '}';
  }
}

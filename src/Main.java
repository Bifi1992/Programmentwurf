/**
 * Created by y.brisch on 17.05.17.
 */
public class Main {

  private static final int mMoonRadius = 1738100;
  private static final double mMoonMass = 73420000000000000000000.0;

  public static void main(String[] args) {
    Rocket testRocket = new Rocket(new Coordinate2D(20, 1), 1,0 ,0, 2000000);
    Planet testPlanet = Planet.MOON;
    //new Thread(new RocketRunnable(testRocket, testPlanet)).start();
    javafx.application.Application.launch(Interface.class, args);
  }
}

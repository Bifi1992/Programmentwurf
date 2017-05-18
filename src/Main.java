/**
 * Created by y.brisch on 17.05.17.
 */
public class Main {

  public static void main(String[] args) {
    Rocket testRocket = new Rocket(1, 1, 1);
    Planet testPlanet = new Planet(1, 1);
    new Thread(new RocketRunnable(testRocket, testPlanet)).start();
  }
}

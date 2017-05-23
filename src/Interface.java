import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Interface extends Application {

  public TextArea mTextArea = new TextArea();
  ChoiceBox<Planet> mPlanetDropDown = new ChoiceBox<>();
  Button mStartButton = new Button();
  Button mExitButton = new Button();
  private static Interface mInterface;

  /**
   *  synchronized Singleton
   */
  public Interface() {}
  public static synchronized Interface getInstance() {
    if (mInterface == null) {
      mInterface = new Interface();
    }
    return mInterface;
  }

  @Override
  public void start(Stage primaryStage) throws Exception{
    displayStartScene(primaryStage);
  }

  private void displayStartScene(Stage stage) {
    for (Planet p : Planet.values()) {
      mPlanetDropDown.getItems().add(p);
    }
    mPlanetDropDown.setValue(Planet.MOON);

    //close via click or ESC button
    mExitButton.setText("Exit");
    mExitButton.setCancelButton(true);
    mExitButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Platform.exit();
      }
    });

    mStartButton.setText("Start");
    mStartButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        startCalculations();
      }
    });

    Label planetLabel = new Label("Planet");
    Label rocketLabel = new Label("Rocket");

    HBox buttonBox = new HBox(5, mPlanetDropDown, mStartButton, mExitButton);

    VBox planetBox = new VBox(10, planetLabel);
    VBox rocketBox = new VBox(10, rocketLabel);
    HBox sliderBox = new HBox(5, planetBox, rocketBox);

    VBox root = new VBox(10, buttonBox, sliderBox, mTextArea);

    root.setStyle(
        "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;"
    );




    Scene scene = new Scene(root, 800, 400);
    stage.setTitle("Planet Lander");
    stage.setScene(scene);
    stage.show();
  }

  private void startCalculations() {
    Rocket testRocket = new Rocket(new Coordinate2D(20, 1), 1,0 ,0);
    Planet testPlanet = Planet.MOON;
    Thread rocket1 = new Thread(new RocketRunnable(testRocket, testPlanet, mTextArea));
    rocket1.setDaemon(true);
    rocket1.start();
  }

  public TextArea getTextArea() {
    return mTextArea;
  }
}

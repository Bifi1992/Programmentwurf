import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Interface extends Application {

  Scene mStartScene;
  Scene mSimScene;
  Stage mPrimaryStage;
  TextArea mTextArea = new TextArea();
  ChoiceBox<Planet> mPlanetDropDown = new ChoiceBox<>();
  Button mStartButton = new Button();
  Button mStartExitButton = new Button();
  Button mSimExitButton = new Button();
  Button mReturnButton = new Button();
  final Canvas mCanvas = new Canvas();
  GraphicsContext mGC = mCanvas.getGraphicsContext2D();
  Dimension mScreenRes = Toolkit.getDefaultToolkit().getScreenSize();
  Double mInitDistance = (SLIDER_INIT_DIST_MAX - SLIDER_INIT_DIST_MIN) / 2;
  private static final double SLIDER_INIT_DIST_MIN = 1000000;
  private static final double SLIDER_INIT_DIST_MAX = 2000000;

  /**
   * ThreadPool
   */
  ExecutorService mThreadPool = Executors.newCachedThreadPool();


  @Override
  public void start(Stage pPrimaryStage) throws Exception{
    mPrimaryStage = pPrimaryStage;
    mStartScene = getStartScene();
    mSimScene = getSimScene();
    mPrimaryStage.setTitle("Planet Lander");
    mPrimaryStage.setScene(mStartScene);
    mPrimaryStage.show();
  }

  private Scene getStartScene() {
    for (Planet p : Planet.values()) {
      mPlanetDropDown.getItems().add(p);
    }
    mPlanetDropDown.setValue(Planet.MOON);

    //close via click or ESC button
    mStartExitButton.setText("Exit");
    mStartExitButton.setCancelButton(true);
    mStartExitButton.setOnAction(e -> Platform.exit());
    mStartExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    mStartButton.setText("Start");
    mStartButton.setDefaultButton(true);
    mStartButton.setOnAction(e -> {
      mPrimaryStage.setScene(mSimScene);
      startCalculations();
    });
    mStartButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    Label planetLabel = new Label("Planet");
    Slider sliderInitDistance = new Slider();
    sliderInitDistance.setMin(SLIDER_INIT_DIST_MIN);
    sliderInitDistance.setMax(SLIDER_INIT_DIST_MAX);
    sliderInitDistance.setValue(SLIDER_INIT_DIST_MIN / SLIDER_INIT_DIST_MAX);
    sliderInitDistance.setOnMouseReleased(e -> mInitDistance = sliderInitDistance.getValue());
    Label rocketLabel = new Label("Rocket");

    HBox buttonBox = new HBox(5, mStartExitButton);
    HBox topBox = new HBox(5, mPlanetDropDown, mStartExitButton);

    VBox planetBox = new VBox(10, planetLabel);
    planetBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    VBox rocketBox = new VBox(10, rocketLabel, sliderInitDistance);
    rocketBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    HBox sliderBox = new HBox(5, planetBox, rocketBox);


    VBox root = new VBox(10, topBox, sliderBox);

    /*root.setStyle(
        "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;"
    );*/




    mStartScene = new Scene(root, mScreenRes.getWidth(), mScreenRes.getHeight());
    return mStartScene;
  }

  private Scene getSimScene() {
    mCanvas.setHeight(mScreenRes.getHeight() * 0.7);
    mCanvas.setWidth(mScreenRes.getWidth() * 0.8);
    mGC.setStroke(Color.BLUE);
    mGC.setFill(Color.BLUE);
    StackPane canvasContainer = new StackPane(mCanvas);
    canvasContainer.setStyle(
        "-fx-background-color: blue, white;" +
            "    -fx-background-insets: 0, 2;" +
            "    -fx-padding: 2;");

    //close via click or ESC button
    mSimExitButton.setText("Exit");
    mSimExitButton.setCancelButton(true);
    mSimExitButton.setOnAction(e -> Platform.exit());
    mSimExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    //return to StartScene
    mReturnButton.setText("Return");
    mReturnButton.setDefaultButton(true);
    mReturnButton.setOnAction(e -> {
      mPrimaryStage.setScene(mStartScene);
      mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
    });
    mReturnButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    VBox ButtonBox = new VBox(5, mStartExitButton, mReturnButton);

    mTextArea.setPrefHeight(mScreenRes.getHeight() * 0.2);
    mTextArea.setPrefWidth(mScreenRes.getWidth() * 0.8);

    HBox textAndButtonsBox = new HBox(5, mTextArea, ButtonBox);

    VBox root = new VBox(10, textAndButtonsBox, canvasContainer);

    /*root.setStyle(
        "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;"
    );*/
    mSimScene = new Scene(root, mScreenRes.getWidth(), mScreenRes.getHeight());
    return mSimScene;
  }

  private void startCalculations() {
    Rocket testRocket1 = new Rocket(1, 1, new Coordinate2D(100, 0), 1,mInitDistance);
    Planet testPlanet = mPlanetDropDown.getValue();
    Rocket testRocket2 = new Rocket(1, 2, new Coordinate2D(200, 0), 1,mInitDistance);

    GeneticLearningAbstract learner = new GeneticLearningAbstract(testPlanet, mTextArea, mCanvas, mGC);
    learner.createPopulationRandom();
    mThreadPool.execute(new RocketRunnable(testRocket1, testPlanet, mTextArea, mCanvas, mGC));
    mThreadPool.execute(new RocketRunnable(testRocket2, testPlanet, mTextArea, mCanvas, mGC));
  }
}

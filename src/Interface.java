import gui.ConfirmBox;
import gui.CustomProgressVBox;
import gui.CustomSliderVBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
  Dimension mScreenRes = new Dimension(1000,1000);
  Planet mDefaultPlanet = Planet.MARS;
  GeneticLearningAbstract mLearner;

  /**
   * holds the initial distance chosen via the slider
   */
  Double mInitDistance = RocketConstants.MIN_INIT_DIST + (RocketConstants.MAX_INIT_DIST - RocketConstants.MIN_INIT_DIST) * 0.5;


  /**
   * holds the slider for the initial distance
   */
  Slider mSliderInitDistance = new Slider(RocketConstants.MIN_INIT_DIST, RocketConstants.MAX_INIT_DIST, mInitDistance);

  /**
   * holds the choice box for the population size
   */
  ChoiceBox<Integer> mPopSizeDropDown = new ChoiceBox<>();

  /**
   * holds the initial fuel level chosen via the slider
   */
  Double mInitFuelLevel = RocketConstants.MIN_INIT_FUEL_LEVEL +
      (RocketConstants.MAX_INIT_FUEL_LEVEL - RocketConstants.MIN_INIT_FUEL_LEVEL) * 0.5;

  /**
   * holds the slider for the initial fuel level
   */
  Slider mSliderInitFuelLevel = new Slider(RocketConstants.MIN_INIT_FUEL_LEVEL, RocketConstants.MAX_INIT_FUEL_LEVEL,
      mInitFuelLevel);

  /**
   * Holds all ProgressIndicators for the rockets
   */
  HashMap<Integer, CustomProgressVBox> mProgressIndicatorMap = new HashMap<>();

  /**
   * Holds a scrollable pane that contains the canvas
   */
  ScrollPane mScrollPane;

  @Override
  public void start(Stage pPrimaryStage) throws Exception{
    mPrimaryStage = pPrimaryStage;
    mStartScene = getStartScene();
    mSimScene = getSimScene();
    mPrimaryStage.setTitle("Planet Lander");
    mPrimaryStage.setOnCloseRequest(e -> {
        e.consume();
        closeProgram();
    });
    mPrimaryStage.setScene(mStartScene);
    mPrimaryStage.show();
  }

  private Scene getStartScene() {
    /**
     * top side - buttons and PlanetDropDown
     */
    for (Planet p : Planet.values()) {
      mPlanetDropDown.getItems().add(p);
    }
    mPlanetDropDown.setValue(mDefaultPlanet);

    Label planetLabel = new Label("Planet:");

    //close via click or ESC button
    mStartExitButton.setText("Exit");
    mStartExitButton.setCancelButton(true);
    mStartExitButton.setOnAction(e -> closeProgram());
    mStartExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    mStartButton.setText("Start");
    mStartButton.setDefaultButton(true);
    mStartButton.setOnAction(e -> {
      mPrimaryStage.setScene(mSimScene);
      startCalculations();
    });
    mStartButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    HBox buttonBox = new HBox(5, mStartButton, mStartExitButton);
    HBox topBox = new HBox(5, planetLabel, mPlanetDropDown, buttonBox);
    topBox.setAlignment(Pos.CENTER_LEFT);

    /**
     * TODO initial speed / angle
     * right side - Rocket
      */
    Label rocketLabel = new Label("Rocket");
    CustomSliderVBox initDistanceSliderBox = new CustomSliderVBox(5, "Initial Distance: ", mSliderInitDistance, "km");
    CustomSliderVBox initFuelLevelSliderBox = new CustomSliderVBox(5, "Initial Fuel Level: ", mSliderInitFuelLevel, "l");
    VBox rocketBox = new VBox(10, rocketLabel, initDistanceSliderBox, initFuelLevelSliderBox);
    rocketBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    rocketBox.setAlignment(Pos.TOP_CENTER);

    /**
     * TODO generation size
     * left side - Algorithm
      */
    Label algoLabel = new Label("Algorithm");
    for (Integer popSize : IntStream.rangeClosed(AlgorithmConstants.MIN_POP_SIZE, AlgorithmConstants.MAX_POP_SIZE).boxed().collect(Collectors.toList())) {
      mPopSizeDropDown.getItems().add(popSize);
    }
    mPopSizeDropDown.setValue(RocketConstants.ROCKETS_PER_GENERATION);
    mPopSizeDropDown.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> mSimScene = getSimScene());

    Label popSizeLabel = new Label("Rockets per Population: ");
    HBox popSizeVBox = new HBox(5, popSizeLabel, mPopSizeDropDown);
    VBox algoBox = new VBox(10, algoLabel, popSizeVBox);
    algoBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    algoBox.setAlignment(Pos.TOP_CENTER);

    HBox middleBox = new HBox(5, algoBox, new Separator(Orientation.VERTICAL), rocketBox);

    VBox root = new VBox(10, topBox, new Separator(Orientation.HORIZONTAL), middleBox);

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
    // setup scrollable canvas
    mCanvas.setHeight(mScreenRes.getHeight());
    mCanvas.setWidth(mScreenRes.getWidth() * 6);
    mScrollPane = new ScrollPane(mCanvas);
    mScrollPane.setPrefSize(mScreenRes.getWidth(), mScreenRes.getHeight() * 0.7);
    mScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    mScrollPane.setFitToWidth(true);
    mScrollPane.setFitToHeight(true);
    mGC.setStroke(Color.BLACK);
    mGC.setFill(Color.BLUE);

    //close via click or ESC button
    mSimExitButton.setText("Exit");
    mSimExitButton.setCancelButton(true);
    mSimExitButton.setOnAction(e -> closeProgram());
    mSimExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    //return to StartScene
    mReturnButton.setText("Return");
    mReturnButton.setDefaultButton(true);
    mReturnButton.setOnAction(e -> {
      if (mLearner != null) {
        mLearner.terminate();
      }
      ThreadPool.getInstance().stop();
      ThreadPool.getInstance().terminate();
      mPrimaryStage.setScene(mStartScene);
      mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
    });
    mReturnButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    VBox ButtonBox = new VBox(5, mSimExitButton, mReturnButton);

    mTextArea.setPrefHeight(mScreenRes.getHeight() * 0.2);
    mTextArea.setPrefWidth(mScreenRes.getWidth() * 0.8);

    VBox topVBox = setupProgressBoxes(mPopSizeDropDown.getValue());

    HBox textAndButtonsBox = new HBox(5, /*mTextArea,*/ topVBox, ButtonBox);

    VBox root = new VBox(5, textAndButtonsBox, mScrollPane);

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
    Platform.runLater(() -> {
      displayGrid(30, 30);
    });
    mLearner = new GeneticLearningAbstract(this);
    mLearner.createPopulationRandom();
  }

  private void closeProgram() {
    boolean close = ConfirmBox.display("Close Program", "Are you sure you want to close the application?");
    if (close) {
      if (mLearner != null) {
        mLearner.terminate();
      }
      ThreadPool.getInstance().stop();
      ThreadPool.getInstance().terminate();
      mPrimaryStage.close();
      Platform.exit();
      System.exit(0);
    }
  }

  /**
   * This method generates a grid with mash size of x*y
   */
  private void displayGrid(double pX, double pY) {
    for (double i = pX; i < mCanvas.getWidth(); i += pX) {
      mGC.strokeLine(i, 0, i, mCanvas.getHeight());
    }
    for (double i = pY; i < mCanvas.getHeight(); i += pY) {
      mGC.strokeLine(0, i, mCanvas.getWidth(), i);
    }
  }

  private VBox setupProgressBoxes(Integer pNumOfBoxes) {
    mProgressIndicatorMap.clear();
    HBox topHBox1 = new HBox();
    HBox topHBox2 = new HBox();

    for (int i = 0; i < pNumOfBoxes; i++) {
      mProgressIndicatorMap.put(i, new CustomProgressVBox(
          (mScreenRes.getWidth() / pNumOfBoxes),
          (mScreenRes.getHeight() * 0.1)
      ));
      if (i < pNumOfBoxes / 2) {
        topHBox1.getChildren().add(mProgressIndicatorMap.get(i));
      } else {
        topHBox2.getChildren().add(mProgressIndicatorMap.get(i));
      }
    }
    return new VBox(topHBox1, topHBox2);
  }
}

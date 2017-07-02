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
import javafx.scene.layout.*;
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
  Scene mFastSimScene;
  Stage mPrimaryStage;
  TextArea mTextArea = new TextArea();
  TextArea mFastSimTextArea = new TextArea();
  ChoiceBox<Planet> mPlanetDropDown = new ChoiceBox<>();
  Button mStartButton = new Button();
  Button mStartExitButton = new Button();
  Button mSimExitButton = new Button();
  Button mFastSimExitButton = new Button();
  Button mReturnButton = new Button();
  Button mFastSimReturnButton = new Button();
  Button mClearButton = new Button();
  final Canvas mCanvas = new Canvas();
  GraphicsContext mGC = mCanvas.getGraphicsContext2D();
  Dimension mScreenRes = Toolkit.getDefaultToolkit().getScreenSize();
  Planet mDefaultPlanet = Planet.MARS;
  GeneticLearningAbstract mLearner;
  ToggleGroup mRadioButtonGroup = new ToggleGroup();
  RadioButton mRadioButtonFastMode = new RadioButton();
  RadioButton mRadioButtonSlowMode = new RadioButton();

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
   *initial generations
   */
  int mInitGenerations = AlgorithmConstants.MIN_GENERATIONS;
/**
 * slider for initial generations
 */
  Spinner<Integer> mSpinnerInitGenerations = new Spinner<>(AlgorithmConstants.MIN_GENERATIONS,
    AlgorithmConstants.MAX_GENERATIONS, mInitGenerations, 5);


  /**
   * Holds all ProgressIndicators for the rockets
   */
  HashMap<Integer, CustomProgressVBox> mProgressIndicatorMap = new HashMap<>();

  /**
   * Holds a scrollable pane that contains the canvas
   */
  ScrollPane mScrollPane;

  /**
   * checkbox which lets user decides to write raw acceleration data in file
   */
   CheckBox writeInDokument = new CheckBox("Write best accelerations in document");

  @Override
  public void start(Stage pPrimaryStage) throws Exception{
    mScreenRes = mScreenRes.getWidth() > 1280 ? new Dimension(1280, 800) : mScreenRes;

    mPrimaryStage = pPrimaryStage;
    mStartScene = getStartScene();
    mSimScene = getSimScene();
    mFastSimScene = getFastSimScene();
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
      mPrimaryStage.setScene(mRadioButtonFastMode.isSelected() ? mFastSimScene : mSimScene);
      mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
      displayGrid(30, 30);
      startCalculations();
    });
    mStartButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    HBox buttonBox = new HBox(5, mStartButton, mStartExitButton);
    HBox topBox = new HBox(5, planetLabel, mPlanetDropDown, buttonBox);
    topBox.setAlignment(Pos.TOP_LEFT);

    /*
     * right side - Rocket
      */
    Label rocketLabel = new Label("Rocket");
    CustomSliderVBox initDistanceSliderBox = new CustomSliderVBox(5, "Initial Distance: ", mSliderInitDistance, "km");
    CustomSliderVBox initFuelLevelSliderBox = new CustomSliderVBox(5, "Initial Fuel Level: ", mSliderInitFuelLevel, "l");
    VBox rocketBox = new VBox(10, rocketLabel, initDistanceSliderBox, initFuelLevelSliderBox);
    rocketBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    rocketBox.setAlignment(Pos.TOP_CENTER);

    /*
     * left side - Algorithm
      */
    Label algoLabel = new Label("Algorithm");
    for (Integer popSize : IntStream.rangeClosed(AlgorithmConstants.MIN_POP_SIZE, AlgorithmConstants.MAX_POP_SIZE).boxed().collect(Collectors.toList())) {
      mPopSizeDropDown.getItems().add(popSize);
    }
    mPopSizeDropDown.setValue(RocketConstants.ROCKETS_PER_GENERATION);
    mPopSizeDropDown.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> mSimScene = getSimScene());

    mRadioButtonFastMode.setText("Fast mode");
    mRadioButtonFastMode.setToggleGroup(mRadioButtonGroup);
    mRadioButtonSlowMode.setText("Visual mode");
    mRadioButtonSlowMode.setToggleGroup(mRadioButtonGroup);
    mRadioButtonSlowMode.setSelected(true);

    Label generationsLabel = new Label("Number of Generations:");
    HBox generationHBox = new HBox(5, generationsLabel, mSpinnerInitGenerations);
    generationHBox.setAlignment(Pos.CENTER_LEFT);
    Label popSizeLabel = new Label("Rockets per Population:");
    HBox popSizeVBox = new HBox(5, popSizeLabel, mPopSizeDropDown);
    popSizeVBox.setAlignment(Pos.CENTER_LEFT);
    Label modeLabel = new Label("Select calculation mode:");
    VBox RadioButtonBox = new VBox(5, modeLabel, mRadioButtonFastMode, mRadioButtonSlowMode, writeInDokument);
    RadioButtonBox.setAlignment(Pos.CENTER_LEFT);
    VBox algoBox = new VBox(10, algoLabel, popSizeVBox, generationHBox, RadioButtonBox);
    algoBox.setPrefSize(mScreenRes.getWidth() * 0.5, mScreenRes.getHeight() * 0.4);
    algoBox.setAlignment(Pos.TOP_CENTER);

    HBox middleBox = new HBox(5, algoBox, new Separator(Orientation.VERTICAL), rocketBox);

    VBox root = new VBox(10, topBox, new Separator(Orientation.HORIZONTAL), middleBox);

    root.setStyle(
        "-fx-padding: 10;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 2;" +
            "-fx-border-color: #4C4C4C;"
    );
    mStartButton.setStyle(
            "-fx-padding: 10;"+
                    "-fx-background-color: #87FF8A;"
    );
    mStartExitButton.setStyle(
            "-fx-padding: 10;"+
                    "-fx-background-color: #FF5244;"
    );
    mPlanetDropDown.setStyle(
            "-fx-padding: 5;" +
            "-fx-background-color: #8C8C8C;" +
            "-fx-fill: #ffffff;"
    );
    algoLabel.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-padding: 0px 0px 20px 0px"
    );
    rocketLabel.setStyle(
            "-fx-font-size: 17px;"+
            "-fx-padding: 0px 0px 20px 0px"
    );
    planetLabel.setStyle(
            "-fx-font-size: 17px;"
    );
    mPopSizeDropDown.setStyle(
            "margin-left: auto;" +
                    "margin-right: auto"
    );





    mStartScene = new Scene(root, mScreenRes.getWidth(), mScreenRes.getHeight());
    return mStartScene;
  }

  private Scene getSimScene() {
    // setup scrollable canvas
    mCanvas.setHeight(mScreenRes.getHeight() * 0.8);
    mCanvas.setWidth(mScreenRes.getWidth() * 6);
    mScrollPane = new ScrollPane(mCanvas);
    mScrollPane.setPrefSize(Double.MAX_VALUE, mScreenRes.getHeight() * 0.7);
    mScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    mScrollPane.setFitToWidth(true);
    mScrollPane.setFitToHeight(true);
    mScrollPane.setHvalue(mCanvas.getWidth()/2/mCanvas.getWidth());

    mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());

    //close via click or ESC button
    mSimExitButton.setText("Exit");
    mSimExitButton.setCancelButton(true);
    mSimExitButton.setOnAction(e -> closeProgram());
    mSimExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    //return to StartScene
    mReturnButton.setText("Return");
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

    mClearButton.setText("Clear");
    mClearButton.setDefaultButton(true);
    mClearButton.setOnAction(e -> {
      mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
      displayGrid(30, 30);
    });
    mClearButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    VBox topProgressVBox = setupProgressBoxes(mPopSizeDropDown.getValue());
    topProgressVBox.setStyle(
        "-fx-border-insets: 5;" +
        "-fx-border-radius: 2;"
    );

    VBox ButtonBox = new VBox(5, mSimExitButton, mReturnButton, mClearButton);
    mTextArea.setPrefHeight(topProgressVBox.getHeight());
    mTextArea.setPrefWidth(mScreenRes.getWidth() * 0.3);
    mTextArea.setMaxWidth(mScreenRes.getWidth() * 0.3);
    HBox topTextAndButtonsBox = new HBox(5, topProgressVBox, ButtonBox, mTextArea);

    VBox root = new VBox(5, topTextAndButtonsBox, mScrollPane);
    root.setAlignment(Pos.TOP_CENTER);

    /*root.setStyle(
        "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;"
    );*/


    mSimExitButton.setStyle(
            "-fx-padding: 10px;" +
            "-fx-background-color: #FF5244;"
    );
    mReturnButton.setStyle(
            "-fx-padding: 10px;" +
            "-fx-background-color: #FFC601;"
    );
    mClearButton.setStyle(
            "-fx-padding: 10px;" +
            "-fx-background-color: #8C8C8C;"
    );
    mSimScene = new Scene(root, mScreenRes.getWidth(), mScreenRes.getHeight());
    return mSimScene;
  }

  private Scene getFastSimScene() {
    //return to StartScene
    mFastSimReturnButton.setText("Return");
    mFastSimReturnButton.setOnAction(e -> {
      if (mLearner != null) {
        mLearner.terminate();
      }
      ThreadPool.getInstance().stop();
      ThreadPool.getInstance().terminate();
      mPrimaryStage.setScene(mStartScene);
      mGC.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
    });
    mFastSimReturnButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    //close via click or ESC button
    mFastSimExitButton.setText("Exit");
    mFastSimExitButton.setCancelButton(true);
    mFastSimExitButton.setOnAction(e -> closeProgram());
    mFastSimExitButton.setPrefWidth(mScreenRes.getWidth() * 0.1);

    VBox ButtonBox = new VBox(5, mFastSimExitButton, mFastSimReturnButton);
    mFastSimTextArea.setPrefSize(mScreenRes.getWidth(), mScreenRes.getHeight());
    VBox topTextAndButtonsBox = new VBox(5, ButtonBox, mFastSimTextArea);

    VBox root = new VBox(5, topTextAndButtonsBox);
    root.setAlignment(Pos.TOP_CENTER);

    mFastSimExitButton.setStyle(
        "-fx-padding: 10px;" +
            "-fx-background-color: #FF5244;"
    );
    mFastSimReturnButton.setStyle(
        "-fx-padding: 10px;" +
            "-fx-background-color: #FFC601;"
    );
    return mFastSimScene;
  }

  private void startCalculations() {
    Platform.runLater(() -> {
      displayGrid(30, 30);
    });
    mLearner = new GeneticLearningAbstract(this);
    mLearner.initPopulationRandom();
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
  public void displayGrid(double pX, double pY) {
    mGC.setStroke(Color.LIGHTGRAY);
    for (double i = pX; i < mCanvas.getWidth(); i += pX) {
      mGC.strokeLine(i, 0, i, mCanvas.getHeight());
    }
    for (double i = pY; i < mCanvas.getHeight(); i += pY) {
      mGC.strokeLine(0, i, mCanvas.getWidth(), i);
    }
  }

  /**
   * This method draws a transparent grid over the Interface to slowly hide previous generations
   */
  public void drawTransparentRect() {
    mGC.setFill(new Color(1, 1, 1, 0.7));
    mGC.fillRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
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
      if (i % 2 == 0) {
        topHBox1.getChildren().add(mProgressIndicatorMap.get(i));
      } else {
        topHBox2.getChildren().add(mProgressIndicatorMap.get(i));
      }
    }
    return new VBox(topHBox1, topHBox2);
  }
}

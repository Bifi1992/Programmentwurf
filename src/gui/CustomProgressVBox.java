package gui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by y.brisch on 20.06.17.
 */
public class CustomProgressVBox extends VBox {
  private Label mLabelRocketId;
  private Label mLabelDistance;
  private Label mLabelTime;
  private Label mLabelSpeed;
  private ProgressBar mProgressBarFuelLevel;

  public CustomProgressVBox(double pWidth, double pHeight) {
    super();
    mLabelRocketId = new Label();
    mLabelDistance = new Label();
    mLabelSpeed = new Label();
    mProgressBarFuelLevel = new ProgressBar(1.0);
    mLabelTime = new Label();
    Label distance = new Label("Distance:");
    Label fuel = new Label("Fuel:");
    Label time = new Label("Time:");
    Label speed = new Label("Speed: ");

    HBox distanceBox = new HBox(2, distance, mLabelDistance);
    HBox fuelLevelBox = new HBox(2, fuel, mProgressBarFuelLevel);
    HBox timeBox = new HBox(2, time, mLabelTime);
    HBox speedBox = new HBox(2, speed, mLabelSpeed);
    this.getChildren().addAll(mLabelRocketId, fuelLevelBox, distanceBox, timeBox, speedBox);
    this.setMinSize(pWidth, pHeight);
    this.setPrefSize(pWidth * 1.2, pHeight * 1.1);
  }

  public Label getLabelRocketId() {
    return mLabelRocketId;
  }

  public Label getLabelDistance() {
    return mLabelDistance;
  }

  public ProgressIndicator getProgressBarFuelLevel() {
    return mProgressBarFuelLevel;
  }

  public Label getLabelTime() {
    return mLabelTime;
  }

  public Label getLabelSpeed() {
    return mLabelSpeed;
  }
}

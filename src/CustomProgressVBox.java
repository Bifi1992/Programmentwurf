import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by y.brisch on 20.06.17.
 */
public class CustomProgressVBox extends VBox {
  private Label mLabelRocketId;
  private Label mLabelDistance;
  private Label mLabelTime;
  private ProgressBar mProgressBarFuelLevel;

  CustomProgressVBox(double pWidth, double pHeight) {
    super();
    mLabelRocketId = new Label();
    mLabelDistance = new Label();
    mProgressBarFuelLevel = new ProgressBar(1.0);
    mLabelTime = new Label();
    Label distance = new Label("Distance:");
    Label fuel = new Label("Fuel:");
    Label time = new Label("Time:");

    HBox distanceBox = new HBox(2, distance, mLabelDistance);
    HBox fuelLevelBox = new HBox(2, fuel, mProgressBarFuelLevel);
    HBox timeBox = new HBox(2, time, mLabelTime);
    this.getChildren().addAll(mLabelRocketId, fuelLevelBox, distanceBox, timeBox);
    this.setMinSize(pWidth, pHeight);
    this.setPrefSize(pWidth * 1.1, pHeight * 1.1);
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
}

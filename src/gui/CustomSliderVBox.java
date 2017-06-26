package gui;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Locale;

/**
 * Created by y.brisch on 14.06.17.
 */
public class CustomSliderVBox extends VBox {

  public CustomSliderVBox(double pSpacing, String pName, Slider pSlider, String pUnit) {
    super(pSpacing);
    Label nameLabel = new Label(pName);
    Label valueLabel = new Label(String.format(Locale.ENGLISH, "%.2f",pSlider.getValue() / 1000) + " " + pUnit);
    pSlider.setOnMouseReleased(e -> valueLabel.setText(String.format(Locale.ENGLISH, "%.2f",pSlider.getValue() / 1000) + " km"));
    HBox sliderBox = new HBox(pSpacing, pSlider, valueLabel);
    this.getChildren().addAll(nameLabel, sliderBox);
  }
}

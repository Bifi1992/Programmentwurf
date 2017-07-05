package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by y.brisch on 15.06.17.
 */
public class CalcCompleteBox {

  /**
   * This method displays a new window with a given title, message and an OK-Button to close it
   *
   * @param pTitle   the title of the popup
   * @param pMessage the message of the popup
   */
  public static void display(String pTitle, String pMessage) {
    Stage stage = new Stage();

    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(pTitle);
    stage.setMinWidth(250);

    Label label = new Label(pMessage);

    Button okButton = new Button("Ok");
    okButton.setDefaultButton(true);
    okButton.setOnAction(e -> stage.close());

    VBox buttonBox = new VBox(5);
    buttonBox.getChildren().addAll(label, okButton);
    buttonBox.setAlignment(Pos.CENTER);

    Scene scene = new Scene(buttonBox);
    stage.setScene(scene);
    stage.showAndWait();
  }
}

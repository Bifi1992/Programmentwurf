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
public class ConfirmBox {

  /**
   * holds the boolean return value
   */
  private static boolean mAnswer;

  /**
   * This method displays a confirm box with a given title, message, a YES- and a NO-Button. It returns
   * a boolean value depending on the button click
   *
   * @param pTitle   the title of the window
   * @param pMessage the displayed message
   * @return true if YES is clicked, false if NO is clicked
   */
  public static boolean display(String pTitle, String pMessage) {
    Stage stage = new Stage();

    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle(pTitle);
    stage.setMinWidth(250);

    Label label = new Label(pMessage);

    Button yesButton = new Button("Yes");
    yesButton.setDefaultButton(true);
    yesButton.setOnAction(e -> {
      mAnswer = true;
      stage.close();
    });

    Button noButton = new Button("No");
    noButton.setCancelButton(true);
    noButton.setOnAction(e -> {
      mAnswer = false;
      stage.close();
    });

    VBox buttonBox = new VBox(5);
    buttonBox.getChildren().addAll(label, yesButton, noButton);
    buttonBox.setAlignment(Pos.CENTER);

    Scene scene = new Scene(buttonBox);
    stage.setScene(scene);
    stage.showAndWait();
    return mAnswer;
  }
}

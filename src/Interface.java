import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Created by y.brisch on 11.05.17.
 */
public class Interface extends Application {
  public static void main(String[] args) {
    GeneticLearningAbstract mylearner = new GeneticLearningAbstract();
    mylearner.createPopulationRandom();
    for(Integer i = 0; i <= 100; i++) {
      mylearner.getFitness();
      mylearner.manipulateParents();
    }
  }

  @Override
  public void start(Stage primaryStage) {
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.TOP_LEFT);
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(5, 5, 5, 5));

    Text scenetitle = new Text("Welcome");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    gridPane.add(scenetitle, 10, 0, 2, 1);

    Label userName = new Label("User Name:");
    gridPane.add(userName, 10, 1);

    TextField userTextField = new TextField();
    gridPane.add(userTextField, 11, 1);

    Label pw = new Label("Password:");
    gridPane.add(pw, 10, 2);

    PasswordField pwBox = new PasswordField();
    gridPane.add(pwBox, 11, 2);

    Button sBtn = new Button("Sign in");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(sBtn);
    gridPane.add(hbBtn, 11, 4);

    Slider slider = new Slider(0,20,10);
    slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("slider value: " + slider.getValue());
      }
    });

    //Open menu via click or ESC button
    Button btn = new Button();
    btn.setText("CLOSE");
    btn.setCancelButton(true);
    btn.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        System.out.println("btn");
        primaryStage.close();
      }
    });

    gridPane.add(btn, 0, 20);
    gridPane.add(slider, 1, 0);

    Scene scene = new Scene(gridPane, 800, 400);
    primaryStage.setTitle("Planet Lander");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}

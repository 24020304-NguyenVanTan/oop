package Source;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private Pane uiPane;
    @FXML private Button startButton;
    @FXML private Button exitButton;

    public static GameEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double BASE_WIDTH = 1920;
        double BASE_HEIGHT = 1080;

// Center using translate instead of layoutX/Y (avoids binding errors)
		uiPane.translateXProperty().bind(
			rootPane.widthProperty()
                .subtract(uiPane.widthProperty().multiply(uiPane.scaleXProperty()))
                .divide(2)
		);
		uiPane.translateYProperty().bind(
			rootPane.heightProperty()
                .subtract(uiPane.heightProperty().multiply(uiPane.scaleYProperty()))
                .divide(2)
		);
    }

    @FXML
    public void onStartClick() {
        if (engine != null) engine.startGame();
    }

    @FXML
    public void onExitClick() {
        System.exit(0);
    }

    @FXML
    public void onButtonHover() {
        startButton.setStyle("-fx-background-color: #3cb371; -fx-text-fill: white;");
    }

    @FXML
    public void onButtonExit() {
        startButton.setStyle("-fx-background-color: #2e8b57; -fx-text-fill: white;");
    }
}

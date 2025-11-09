package Source;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import java.net.*;
import java.util.*;
import javafx.application.*;

public class Controller implements Initializable {

    @FXML public AnchorPane rootPane;
    @FXML public Pane scalePane;
    @FXML public Canvas gameCanvas;

    @FXML public VBox mainMenu;
	    @FXML public Button startButton;
		@FXML public Button exitButton;
		
    @FXML public VBox pauseMenu;
    @FXML public Rectangle overlay;
	
	@FXML VBox loseMenu;
		@FXML Button restartButton1;
		@FXML Button backToMenuButton1;

    @FXML public Button continueButton;
    @FXML public Button backToMenuButton;
    public static GameEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double BASE_WIDTH = 1920;
        double BASE_HEIGHT = 1080;

		// Center using translate instead of layoutX/Y (avoids binding errors)
		scalePane.translateXProperty().bind(
			rootPane.widthProperty()
                .subtract(scalePane.widthProperty().multiply(scalePane.scaleXProperty()))
                .divide(2)
		);
		scalePane.translateYProperty().bind(
			rootPane.heightProperty()
                .subtract(scalePane.heightProperty().multiply(scalePane.scaleYProperty()))
                .divide(2)
		);
		gameCanvas.layoutXProperty().bind(
			scalePane.widthProperty().subtract(gameCanvas.widthProperty()).divide(2)
		);
		gameCanvas.layoutYProperty().bind(
			scalePane.heightProperty().subtract(gameCanvas.heightProperty()).divide(2)
		);
    }

    /* ============ BUTTON HANDLERS ============ */

    @FXML
    public void onStartClicked() {
        engine.GAME_STATE = 1;
		mainMenu.setVisible(false);
        gameCanvas.setVisible(true);
    }

    @FXML
    public void onQuitClicked() {
        Platform.runLater(Platform::exit);
    }

    @FXML
    public void onContinueClicked() {
        engine.GAME_STATE = 1;
		overlay.setVisible(false);
        pauseMenu.setVisible(false);
    }

    @FXML
    public void onBackToMenuClicked() {
        engine.GAME_STATE = 0;
		overlay.setVisible(false);
        pauseMenu.setVisible(false);
		gameCanvas.setVisible(false);
		loseMenu.setVisible(false);
		mainMenu.setVisible(true);
	}
	@FXML
    public void onRestartClicked() {
		engine.resetGame();
		overlay.setVisible(false);
        pauseMenu.setVisible(false);
		gameCanvas.setVisible(false);
		loseMenu.setVisible(false);
		mainMenu.setVisible(false);
	}
}


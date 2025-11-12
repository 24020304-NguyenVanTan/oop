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
import javafx.scene.media.*;

public class Controller implements Initializable {//Implement interface
	//Loading sound
	public static final AudioClip sound = new AudioClip(Controller.class.getResource("/Source/Assets/Sounds/Click.wav").toExternalForm());

    @FXML public AnchorPane rootPane;
    @FXML public Pane scalePane;
    @FXML public Canvas gameCanvas;

    @FXML public VBox mainMenu;
	    @FXML public Button startButton;
		@FXML public Button exitButton;
		
    @FXML public VBox pauseMenu;
		@FXML public Rectangle overlay;
		@FXML public Button continueButton;
		@FXML public Button backToMenuButton;
	
	@FXML VBox loseMenu;
		@FXML Button restartButton;
		@FXML Button backToMenuButton1;
	
	@FXML VBox passMenu;
		@FXML Button continueButton1;
		@FXML Button backToMenuButton2;
		
	@FXML VBox winMenu;
		@FXML Button restartButton1;
		@FXML Button backToMenuButton3;
	
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
		sound.play();
		if(engine.ball.y>Object.SIM_H) engine.level=0;
		engine.initGame();
        engine.GAME_STATE = 1;
		mainMenu.setVisible(false);
		winMenu.setVisible(false);
		passMenu.setVisible(false);
		pauseMenu.setVisible(false);
		loseMenu.setVisible(false);
        gameCanvas.setVisible(true);
    }

    @FXML
    public void onQuitClicked() {
		sound.play();
        Platform.runLater(Platform::exit);
    }

    @FXML
    public void onContinueClicked() {
		sound.play();
		if(engine.GAME_STATE==4){
			engine.initGame();
		}
        engine.GAME_STATE = 1;
		overlay.setVisible(false);
        pauseMenu.setVisible(false);
		passMenu.setVisible(false);
    }

    @FXML
    public void onBackToMenuClicked() {
		sound.play();
        engine.GAME_STATE = 0;
		overlay.setVisible(false);
        pauseMenu.setVisible(false);
		passMenu.setVisible(false);
		winMenu.setVisible(false);
		gameCanvas.setVisible(false);
		loseMenu.setVisible(false);
		mainMenu.setVisible(true);
	}
	
	@FXML
    public void onRestartClicked() {
		sound.play();
		engine.level=0;
		engine.initGame();
		overlay.setVisible(false);
		loseMenu.setVisible(false);
		passMenu.setVisible(false);
		winMenu.setVisible(false);
	}
}


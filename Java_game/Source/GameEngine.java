package Source;

import javafx.fxml.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class GameEngine extends Application {
    // Simulation size
    public static final int SIM_W = 1560;
    public static final int SIM_H = 1080;

    // Game states
    static int GAME_STATE = 0; // 0 = menu, 1 = playing, 2 = win, 3 = lose

    // Game objects
    Paddle paddle;
    Ball ball;
    ArrayList<Brick> bricks = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();

    // Input tracking
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private double mouseX;
    private boolean mousePressed = false;

    // Canvas + scene
    private double screenW, screenH;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;
    private Scene gameScene;
    private Scene menuScene;

    @Override
    public void start(Stage stage) throws Exception {
		Object.engine=this;
		Controller.engine=this;
        this.stage = stage;
        // --- Load FXML (Menu) ---
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Source/Layout.fxml"));
        Parent menuRoot = loader.load();

        // Link controller
        Controller controller = loader.getController();

        menuScene = new Scene(menuRoot);
        stage.setTitle("Arkanoid Clone - JavaFX");
        stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        stage.setScene(menuScene);
        stage.show();

        // --- Screen setup for game ---
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenW = screenBounds.getWidth();
        screenH = screenBounds.getHeight();

        double scale = screenH / SIM_H;
        canvas = new Canvas(screenW, screenH);
        gc = canvas.getGraphicsContext2D();

        Pane gameRoot = new Pane(canvas);
        gameScene = new Scene(gameRoot);

        // Input handling
        gameScene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        gameScene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        gameScene.setOnMouseMoved(e -> mouseX = e.getX());
        gameScene.setOnMouseDragged(e -> mouseX = e.getX());
        gameScene.setOnMousePressed(e -> mousePressed = true);
        gameScene.setOnMouseReleased(e -> mousePressed = false);

        // Initialize game data
        initGame();

        // Game loop
        new AnimationTimer() {
            long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double deltaTime = (now - last) / 1e9;
                last = now;
                update(deltaTime);
                render(scale);
            }
        }.start();
    }

    public void startGame() {
        GAME_STATE = 1;
        stage.setScene(gameScene);
		stage.setFullScreen(true);
    }
	
	public void quitGame() {
		Platform.exit();
	}

    private void initGame() {
        paddle = new Paddle();
        paddle.x = (SIM_W - paddle.w) / 2;
        paddle.y = SIM_H - 100;

        ball = new Ball();
        ball.x = SIM_W / 2 - Ball.BALL_SIZE / 2;
        ball.y = SIM_H / 2;
        ball.dx = 5;
        ball.dy = -5;

        loadLevel("/Source/Assets/Levels/0.txt");
    }

    private void update(double delta) {
        if (GAME_STATE != 1) return;

        if (pressedKeys.contains(KeyCode.LEFT)) paddle.moveleft();
        if (pressedKeys.contains(KeyCode.RIGHT)) paddle.moveright();

        if (mousePressed) {
            paddle.move((int) (mouseX / (canvas.getWidth() / SIM_W)));
        }

        ball.update();
        items.parallelStream().forEach(Item::update);
        items.removeIf(i -> i.y >= SIM_H);
    }

    private void render(double scale) {
        if (GAME_STATE != 1) return;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screenW, screenH);

        for (Brick b : bricks) b.render(gc, scale);
        for (Item i : items) i.render(gc, scale);
        paddle.render(gc, scale);
        ball.render(gc, scale);

        if (GAME_STATE == 3) {
            gc.setFill(Color.RED);
            gc.fillText("You Lose!", 500, 500);
        }
    }

	public void loadLevel(String resourcePath) {
		bricks.clear();

		try (InputStream is = getClass().getResourceAsStream(resourcePath);
			 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;

				String[] parts = line.split("\\s+");
				if (parts.length < 3) continue;

				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				int type = Integer.parseInt(parts[2]);

				bricks.add(new Brick(type, x * Brick.w, y * Brick.h));
			}

		} catch (Exception e) {
			System.err.println("Failed to load level: " + e.getMessage());
		}
	}

    public static void main(String[] args) {
        launch(args);
    }
}

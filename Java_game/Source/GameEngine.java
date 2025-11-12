package Source;

import javafx.application.*;
import javafx.fxml.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.geometry.*;

import java.io.*;
import java.util.*;

public class GameEngine extends Application {

    // Simulation size
    public static final int SIM_W = 1560;
    public static final int SIM_H = 1080;
	
	final int MAX_LEVEL=7;
	int level = 0;

    int GAME_STATE = 0; // 0:menu, 1:ingame, 2:pause, 3:lose, 4:win

    // Game objects
    Paddle paddle = new Paddle();
    Ball ball = new Ball();
    ArrayList<Brick> bricks = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();

    // Input tracking
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private double mouseX;
    private boolean mousePressed = false;

    // Canvas + graphics
    private double screenW, screenH;
    private GraphicsContext gc;
    private double scale;

    // References
    private Stage stage;
    private Controller controller;

    @Override
    public void start(Stage stage) throws Exception {
        Object.engine = this;
        Controller.engine = this;
        this.stage = stage;

        // --- Load FXML ---
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Source/Layout.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        // --- Stage setup ---
        stage.setTitle("Arkanoid Clone - JavaFX");
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
		Platform.runLater(() -> {controller.rootPane.requestFocus();});

        // --- Setup canvas & scaling ---
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenW = screenBounds.getWidth();
        screenH = screenBounds.getHeight();
        scale = screenH / SIM_H;

        Canvas canvas = controller.gameCanvas;
        canvas.setWidth(screenW);
        canvas.setHeight(screenH);
        gc = canvas.getGraphicsContext2D();

        // --- Input handling ---
        root.setOnMouseMoved(e -> mouseX = e.getX());
        root.setOnMouseDragged(e -> mouseX = e.getX());
        root.setOnMousePressed(e -> mousePressed = true);
        root.setOnMouseReleased(e -> mousePressed = false);

       scene.setOnKeyPressed(e -> {
		pressedKeys.add(e.getCode());
			// handle immediate keys (toggle pause etc)
			if (e.getCode() == KeyCode.ESCAPE) {
				if (GAME_STATE == 1){
					GAME_STATE=2;
					controller.overlay.setVisible(true);
					controller.pauseMenu.setVisible(true);
				}
				else if (GAME_STATE == 2){
					controller.onContinueClicked();
				}
			}
		});
		scene.setOnKeyReleased(e -> {pressedKeys.remove(e.getCode());
		});

        // --- Game loop ---
		// --- Fixed timestep loop (60 FPS) ---
		final double FRAME_DURATION = 1.0 / 60.0; // seconds per frame
		new AnimationTimer() {
			long lastTime = 0;
			double accumulator = 0;

			@Override
			public void handle(long now) {
				if (lastTime == 0) {
					lastTime = now;
					return;
				}

				// Time since last frame (in seconds)
				double delta = (now - lastTime) / 1e9;
				lastTime = now;
				accumulator += delta;

				// Run as many fixed updates as needed (usually 1)
				while (accumulator >= FRAME_DURATION) {
					update(); // no delta parameter needed anymore
					accumulator -= FRAME_DURATION;
				}

				render(); // render once per frame
			}
		}.start();
	}


    /** Called on ESC -> pause */
    public void pauseGame() {
        GAME_STATE = 2;
        controller.overlay.setVisible(true);
        controller.pauseMenu.setVisible(true);
    }

    void initGame() {
        loadLevel("/Source/Assets/Levels/"+level+".txt");
		resetGame();
    }

    private void update() {
        if (GAME_STATE != 1) return;
		if(bricks.isEmpty()){
			onPass();
			System.out.println("Passed");
		}
        if (pressedKeys.contains(KeyCode.LEFT)) paddle.moveleft();
        if (pressedKeys.contains(KeyCode.RIGHT)) paddle.moveright();

        if (mousePressed) {
            paddle.move((int) (mouseX/scale));
        }

        ball.update();
        items.parallelStream().forEach(Item::update);
        items.removeIf(i -> i.y >= SIM_H);
    }

    private void render() {
        if (GAME_STATE != 1) return;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screenW, screenH);

        for (Brick b : bricks) b.render(gc, scale);
        for (Item i : items) i.render(gc, scale);
        paddle.render(gc, scale);
        ball.render(gc, scale);

        // side bars
		gc.setFill(Color.rgb(80, 80, 80, 1));
        gc.fillRect(175*scale, 0, 5 * scale, 1080 * scale);
        gc.fillRect((1920 - 180) * scale, 0, 5 * scale, 1080 * scale);
		
		gc.setFill(Color.rgb(200, 200, 200, 1));
        gc.fillRect(0, 0, 175 * scale, 1080 * scale);
        gc.fillRect((1920 - 175) * scale, 0, 175 * scale, 1080 * scale);
		
    }
	
	//Working fine as is
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
	public void resetGame(){
		//Paddle
		paddle.w=200;
        paddle.x = (SIM_W - paddle.w) / 2;
        paddle.y = SIM_H - 100;
		//Ball
        ball.x = SIM_W / 2 - Ball.SIZE / 2;
        ball.y = paddle.y-Ball.SIZE-1;
		Random r= new Random(); //Random ball movement
		double angle = Math.toRadians(120 * r.nextDouble() - 60); 
		ball.dx = Ball.speed * Math.sin(angle);
		ball.dy = -Ball.speed * Math.cos(angle);
		GAME_STATE=1;
	}
	
	public void onLose() {
		GAME_STATE=3;
		controller.overlay.setVisible(true);
		controller.loseMenu.setVisible(true);
		System.out.println("Menu: Lose");
	}
	
	public void onPass() {
		GAME_STATE=4;
		level++;
		controller.overlay.setVisible(true);
		if(level<MAX_LEVEL){
			controller.passMenu.setVisible(true);
			System.out.println("Menu: Pass");
		}
		else{
			controller.winMenu.setVisible(true);
			System.out.println("Menu: Win");
			//Ball.speed+=10;
			level=0;
		}
	}

    public static void main(String[] args) {
        launch(args);
    }
}
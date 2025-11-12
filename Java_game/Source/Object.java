package Source;
public class Object{
	//The basis of game play simulation, the engine reference ensure every class get access to each other and to the main engine for input/game state
	//Every of it child class will inherit this, adhering to DRY
	public static final int SIM_W = 1560;
    public static final int SIM_H = 1080;
	public static GameEngine engine;
	public static final int RENDER_OFFSET=180;
	double x, y;
	public void update(){
		return;
	}
	public void render(){
		return;
	}
}
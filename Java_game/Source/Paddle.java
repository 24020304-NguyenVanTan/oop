package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
public class Paddle extends Object{
	int w=200;
	int h=40;
	double speed=10;
	int currentPowerUp;
	public void move(int x){
		this.x=Math.max(Math.min(SIM_W-this.w, x), 0);
		return;
	}
	public void moveright(){
		this.x=Math.min(x+speed, SIM_W-w);
		return;
	}
	public void moveleft(){
		this.x=Math.max(x-speed, 0);
		return;
	}
	public void update(){
		return;
	}
	public void render(GraphicsContext gc, double scale){
		gc.setFill(Color.rgb(80, 80, 80, 1));
		gc.fillRect((x+RENDER_OFFSET) * scale, y * scale, w * scale, h * scale);
		gc.setFill(Color.rgb(200, 200, 200, 1));
		gc.fillRect((x+RENDER_OFFSET+5)*scale, (y+5)*scale, (w-10)*scale, (h-10)*scale);
	}
}
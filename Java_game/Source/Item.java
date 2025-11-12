package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
public class Item extends Object {
	final static int SIZE = 45;
	static double speed=10;
	int type;
	
	//Loading texture
	static final Image[] texture = {
		new Image("Assets/Item/0.png")
	};
	
	public Item(double x, double y, int type) {
		this.x=x;
		this.y=y;
		this.type=type;
	}
	
	public void update() {
		this.y+=speed;
		Paddle p = engine.paddle;
		if (y + SIZE >= p.y && y < p.y && x + SIZE > p.x && x < p.x + p.w) {
			this.y=SIM_H;
			switch(type){
				case 0:
					p.w+=20;
			}
		}
		return;
	}
	
	public void render(GraphicsContext gc, double SCALE) {
        gc.drawImage(texture[this.type], (x+RENDER_OFFSET) * SCALE, y * SCALE, SIZE * SCALE, SIZE * SCALE);
		return;
    }
	
	
}
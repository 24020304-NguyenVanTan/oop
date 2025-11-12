package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
public class Bullet extends Object {
	final static int SIZE = 16;
	static double speed=15;
	static final Image texture=new Image(Bullet.class.getResource("/Source/Assets/Item/Bullet.png").toExternalForm());
	
	public boolean AABBCheck(Brick brick) {
        return (this.x + SIZE > brick.x &&
                this.x < brick.x + brick.w &&
                this.y + SIZE > brick.y &&
                this.y < brick.y + brick.h);
    }
	
	public Bullet(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void update() {
		this.y-=speed;
		if(this.y<0){
			return;
		}
		for(Brick b : engine.bricks) {
			if(AABBCheck(b)) {
				b.update();
				this.y=-1;
				return;
			}
		}
		return;
	}
	public void render(GraphicsContext gc, double SCALE) {
        gc.drawImage(texture, (x+RENDER_OFFSET) * SCALE, y * SCALE, SIZE * SCALE, SIZE * 2 * SCALE);
		return;
    }	
}
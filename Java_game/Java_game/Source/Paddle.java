package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;
public class Paddle extends Object{
	int w=200;
	double speed=10;
	double dx;
	private int countdown = 0;
	private int ammo = 0;
	private int cooldown = 0;
	
	public void addAmmo(int n) {
		ammo+=n;
	}
	public void setCountdown() {
		countdown=60*5;
	}
	private void move(int X){
		dx=this.x;
		this.x=Math.max(Math.min(SIM_W-this.w, X-(this.w/2)-180), 0);
		dx=(this.x-dx);
		return;
	}
	private void moveright(){
		this.x=Math.min(x+speed, SIM_W-w);
		return;
	}
	private void moveleft(){
		this.x=Math.max(x-speed, 0);
		return;
	}
	public void update(){
		//Movement
		if (engine.pressedKeys.contains(KeyCode.LEFT)) moveleft();
        if (engine.pressedKeys.contains(KeyCode.RIGHT)) moveright();
        if (engine.mousePressed) {
            move((int) (engine.mouseX/engine.getScale()));
        }
		//Shooting
		if(ammo>0){
			if(engine.pressedKeys.contains(KeyCode.W) && cooldown==0) {
					ammo--;
					cooldown=60/5;
					engine.bullets.add(new Bullet(this.x+this.w/2-Bullet.SIZE/2, y+16*3));
			}
			cooldown=Math.max(cooldown-1, 0);
		}
		countdown = Math.max(0, countdown-1);
		if(countdown == 0 && w>200){
			w-=20;
			System.out.println("Paddle w: "+w);
			if(w>200) countdown=60*5;
		}
		return;
	}
	public void render(GraphicsContext gc, double scale){
		gc.setFill(Color.rgb(80, 80, 80, 1));
		gc.fillRect((x+RENDER_OFFSET) * scale, y * scale, w * scale, 20 * scale);
		gc.setFill(Color.rgb(200, 200, 200, 1));
		gc.fillRect((x+RENDER_OFFSET+5)*scale, (y+5)*scale, (w-10)*scale, (10)*scale);
		if(ammo>0) {
			gc.setFill(Color.BLACK);
			gc.setFont(new javafx.scene.text.Font(40 * scale)); // scaled font
			gc.fillText("Ammo: ", 30 * scale, (1080-30-80) * scale);
			if(ammo<6) gc.setFill(Color.RED);
			gc.fillText(""+ammo, 30 * scale, (1080-30-40) * scale);
		}
	}
}
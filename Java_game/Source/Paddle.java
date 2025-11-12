package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
public class Paddle extends Object{
	int w=200;
	double speed=10;
	double dx;
	private int countdown = 0;
	private int ammo = 0;
	private int cooldown = 0;//Basically fire rate cap
	
	//Loading sound
	public static final AudioClip sound = new AudioClip(Paddle.class.getResource("/Source/Assets/Sounds/Paddle shot.wav").toExternalForm());
	public void addAmmo(int n) {
		ammo=Math.max(0, ammo+n);
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
					sound.play();
					ammo--;
					cooldown=60/5;//5 shots per sec
					engine.bullets.add(new Bullet(this.x+this.w/2-Bullet.SIZE/2, y-32));
			}
			cooldown=Math.max(cooldown-1, 0);
		}
		countdown = Math.max(0, countdown-1);//Counting down
		if(countdown == 0 && w>200){//Power up run out
			w-=20;
			x+=10;
			if(w>200) countdown=60*5;
		}
		return;
	}
	public void render(GraphicsContext gc, double scale){//This doesn't use png to ensure scaling when w is changed
		if(ammo>0) {
			//Red paddle with protruding gun barrel
			gc.setFill(Color.rgb(150, 00, 0, 1));
			gc.fillRect((x+RENDER_OFFSET) * scale, y * scale, w * scale, 20 * scale);
			gc.fillRect((x+w/2-10+RENDER_OFFSET) * scale, (y-20) * scale, 20 * scale, 20 * scale);
			gc.setFill(Color.rgb(255, 0, 0, 1));
			gc.fillRect((x+RENDER_OFFSET+5)*scale, (y+5)*scale, (w-10)*scale, (10)*scale);
			gc.fillRect((x+w/2-5+RENDER_OFFSET) * scale, (y-15) * scale, 10 * scale, 20 * scale);
			//Ammo count
			gc.setFill(Color.BLACK);
			gc.setFont(new javafx.scene.text.Font(40 * scale));
			gc.fillText("Ammo: ", 20 * scale, 970 * scale);
			if(ammo<6) gc.setFill(Color.RED);//Critically low ammo
			gc.fillText(""+ammo, 20 * scale, 1010 * scale);
		}
		else{//Gray paddle
			gc.setFill(Color.rgb(80, 80, 80, 1));
			gc.fillRect((x+RENDER_OFFSET) * scale, y * scale, w * scale, 20 * scale);
			gc.setFill(Color.rgb(200, 200, 200, 1));
			gc.fillRect((x+RENDER_OFFSET+5)*scale, (y+5)*scale, (w-10)*scale, (10)*scale);
		}
	}
}
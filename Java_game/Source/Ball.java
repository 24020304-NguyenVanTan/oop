package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.media.*;
public class Ball extends Object {
    static int SIZE = 40;
    static double speed = 10;
    double dx, dy;
	boolean bounced=false;//This make sure the ball doesn't bounce twice on the paddle
	private int countdown = 0;
	//Loading texture
	static final Image[] texture={
		new Image(Ball.class.getResource("/Source/Assets/Ball/0.png").toExternalForm()),//Normal ball
		new Image(Ball.class.getResource("/Source/Assets/Ball/1.png").toExternalForm())//Big buzzsaw
	};
	//Loading sound
	public static final AudioClip sound = new AudioClip(Ball.class.getResource("/Source/Assets/Sounds/Ball.wav").toExternalForm());
    // AABB collision
    public boolean AABBCheck(Brick brick) {
        return (this.x + SIZE > brick.x &&
                this.x < brick.x + brick.w &&
                this.y + SIZE > brick.y &&
                this.y < brick.y + brick.h);
    }
	public void setCountdown(){//5 sec of power up
		if(countdown==0){//Normalize new coord to preserve center coord
			x-=20;
			y-=20;
		}
		countdown=60*5;
		SIZE=80;//Twice normal size
	}
	public void resetCountdown() {
		if(countdown>0) {
			x+=20;
			y+=20;
			countdown=0;
			SIZE=40;
		}
	}
    // Update
    public void update() {
		if(countdown>0){
			countdown--;//Count down until buzz saw power up run out
			if(countdown==0){
				SIZE=40;
			}
		}
		//Update coord
        x += dx;
        y += dy;

        // Out of lower bound, lose
        if (this.y > SIM_H) {
            engine.onLose();
            return;
        }

        // Top wall
        if (this.y < 0) {
			sound.play();
            this.dy *= -1;
			x+=(1-this.y)*dx/dy;
			y=1;//Make sure ball clear the bound
            return;
        }

        // Side walls
        if (this.x < 0 || this.x + SIZE > SIM_W) {
			sound.play();
            this.dx *= -1;
			//Make sure ball clear the bound
			if(this.x < 0) {
				y+=(1-this.x)*dy/dx;
				x=1;
			}
			else {
				y+=(this.x+SIZE-SIM_W+1)*dy/dx;
				x=SIM_W-1-SIZE;
			}
            return;
        }

		// Paddle collision
		Paddle p = engine.paddle;
		if (y + SIZE >= p.y && y < p.y && x + SIZE > p.x && x < p.x + p.w && !bounced) {//Again, AABB
			sound.play();
			bounced=true;//Disable bouncing, just to be safe
			double angle = Math.toRadians(70) * Math.max(-1, Math.min(1, ((x + SIZE * 0.5) - (p.x + p.w * 0.5)) / (p.w * 0.5)));
			dx = speed * Math.sin(angle);
			dy = -speed * Math.cos(angle);
		}
		else if(y+SIZE<p.y) bounced=false;//Only when the ball clear the paddle's y can it bounce again
		
        // Brick collision detection
        List<Brick> overlap = engine.bricks.parallelStream()
                .filter(b -> this.AABBCheck(b))
                .toList();

        if (overlap.isEmpty()) return;
		sound.play();
		if(countdown==0){//Normal weak ball, bounce on bricks
			// Find the brick with the deepest overlap (to avoid destroying multiple)
			Brick hitBrick = null;
			double maxOverlap = -1;

			for (Brick b : overlap) {
				// Calculate overlap along x and y
				double overlapX = Math.min(this.x + SIZE, b.x + b.w) - Math.max(this.x, b.x);
				double overlapY = Math.min(this.y + SIZE, b.y + b.h) - Math.max(this.y, b.y);
				double totalOverlap = overlapX * overlapY; // can also use min or sum, depends on how you prefer
				if (totalOverlap > maxOverlap) {
					maxOverlap = totalOverlap;
					hitBrick = b;
				}
			}

			if (hitBrick == null) return;
			// Resolve collision by reflecting along the smaller overlap axis
			double overlapX = Math.min(this.x + SIZE, hitBrick.x + hitBrick.w) - Math.max(this.x, hitBrick.x);
			double overlapY = Math.min(this.y + SIZE, hitBrick.y + hitBrick.h) - Math.max(this.y, hitBrick.y);

			if (overlapX < overlapY) {
				// Hit from side
				dx *= -1;
			} else {
				// Hit from top/bottom
				dy *= -1;
			}
			hitBrick.update();
		}
		else{//Unstopable except by map bound collision, wreck all in its path
			for (Brick b : overlap) b.update();
		} 
    }

    public void render(GraphicsContext gc, double SCALE) {
		int t;
		if(countdown==0) t=0;
		else t=1;
        gc.drawImage(texture[t], (x+RENDER_OFFSET) * SCALE, y * SCALE, SIZE * SCALE, SIZE * SCALE);
		return;
    }
}

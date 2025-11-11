package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
public class Ball extends Object {
    final static int SIZE = 45;
    static double speed = 10;
    double dx, dy;
	boolean bounced=false;
	
	//Loading texture
	static final Image texture=new Image(Ball.class.getResource("/Source/Assets/Ball/0.png").toExternalForm());
    // AABB collision
    public boolean AABBCheck(Brick brick) {
        return (this.x + SIZE > brick.x &&
                this.x < brick.x + brick.w &&
                this.y + SIZE > brick.y &&
                this.y < brick.y + brick.h);
    }

    // Update
    public void update() {
        x += dx;
        y += dy;

        // Out of lower bound, lose
        if (this.y > SIM_H) {
            engine.onLose();
            return;
        }

        // Top wall
        if (this.y < 0) {
            this.dy *= -1;
            return;
        }

        // Side walls
        if (this.x < 0 || this.x + SIZE > SIM_W) {
            this.dx *= -1;
            return;
        }

		// Paddle collision (only when ball moving downward)
		Paddle p = engine.paddle;
		if (y + SIZE >= p.y && y < p.y && x + SIZE > p.x && x < p.x + p.w && !bounced) {
			bounced=true;
			double angle = Math.toRadians(70) * Math.max(-1, Math.min(1, ((x + SIZE * 0.5) - (p.x + p.w * 0.5)) / (p.w * 0.5)));
			dx = speed * Math.sin(angle);
			dy = -speed * Math.cos(angle);
		}
		else if(y+SIZE<p.y) bounced=false;
		
        // Brick collision detection
        List<Brick> overlap = engine.bricks.parallelStream()
                .filter(b -> this.AABBCheck(b))
                .toList();

        if (overlap.isEmpty()) return;

        // Find the brick with the deepest overlap (to avoid tunneling)
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

    public void render(GraphicsContext gc, double SCALE) {
        gc.drawImage(texture, (x+RENDER_OFFSET) * SCALE, y * SCALE, SIZE * SCALE, SIZE * SCALE);
		return;
    }
}

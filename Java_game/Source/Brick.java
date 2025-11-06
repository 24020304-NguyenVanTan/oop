package Source;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
class Brick extends Object{
	int hp, type;
	final static int w=120;
	final static int h=80;
	
	//Loading texture
    static final Image[][] texture = {
            {}, // 0: unused
            { null, load("Source/Assets/Brick/1-1.png") },
            { null, load("Source/Assets/Brick/2-1.png"),
                    load("Source/Assets/Brick/2-2.png") },
            { null, load("Source/Assets/Brick/3-1.png"),
                    load("Source/Assets/Brick/3-2.png"),
                    load("Source/Assets/Brick/3-3.png"),
                    load("Source/Assets/Brick/3-4.png"),
                    load("Source/Assets/Brick/3-5.png") },
            { null, load("Source/Assets/Brick/4-1.png") }
    };

    private static Image load(String path) {
        return new Image(Brick.class.getResource("/" + path).toExternalForm());
    }
		
	public Brick(int type, int x, int y){
		this.type=type;
		this.x=x;
		this.y=y;
		switch(type){
			case 1:
				this.hp=1;
				break;
			case 2:
				this.hp=2;
				break;
			case 3:
				this.hp=5;
				break;
			case 4:
				this.hp=1;
				break;
		}
	}
	public void update(){
		this.hp--;
		if(hp<=0){
			switch(this.type){
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					engine.items.add(new Item(this.x+(this.w-Item.SIZE)/2, this.y+(this.h-Item.SIZE)/2, 0));
					break;
			}
			engine.bricks.remove(this);
			
		}
		return;
	}
	public void render(GraphicsContext gc, double scale){
		if(hp<=0) return;
		gc.drawImage(texture[this.type][this.hp], (x+RENDER_OFFSET) * scale, y * scale, w * scale, h * scale);
		return;
	}
}
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
            { null, load("Source/Assets/Brick/4-1.png") },
			{ null, load("Source/Assets/Brick/5-1.png") },
			{ null, load("Source/Assets/Brick/6-1.png") },
    };

    private static Image load(String path) {//A little helper method, just for this class, other don't load enough texture to justify it
        return new Image(Brick.class.getResource("/" + path).toExternalForm());
    }
		
	public Brick(int type, int x, int y){
		this.type=type;
		this.x=x;
		this.y=y;
		switch(type){
			case 1://The yellow one, break in 1 hit
				this.hp=1;
				break;
			case 2://Blue, break in 2
				this.hp=2;
				break;
			case 3://The metal one, take 5 hits
				this.hp=5;
				break;
			case 4://Drop paddle side pu
				this.hp=1;
				break;
			case 5://Drop bullet pu
				this.hp=1;
				break;	
			case 6://Drop buzzsaw pu
				this.hp=1;
				break;
		}
	}
	public void update(){
		this.hp--;
		engine.score++;
		if(hp<=0){
			switch(this.type){
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					engine.items.add(new Item(this.x+(this.w-Item.SIZE)/2, this.y+(this.h-Item.SIZE)/2, 0));//Drop paddle size power up
					break;
				case 5:
					engine.items.add(new Item(this.x+(this.w-Item.SIZE)/2, this.y+(this.h-Item.SIZE)/2, 1));//Drop bullets power up
					break;
				case 6:
					engine.items.add(new Item(this.x+(this.w-Item.SIZE)/2, this.y+(this.h-Item.SIZE)/2, 2));//Turn ball into buzzsaw power up
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
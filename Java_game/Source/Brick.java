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
		{},
		{null, new Image("file:D:/Java_game/Assets/Brick/1-1.png")},
		{null, new Image("file:D:/Java_game/Assets/Brick/2-1.png"),
		new Image("file:D:/Java_game/Assets/Brick/2-2.png")},
		{null, new Image("file:D:/Java_game/Assets/Brick/3-1.png"),
		new Image("file:D:/Java_game/Assets/Brick/3-2.png"),
		new Image("file:D:/Java_game/Assets/Brick/3-3.png"),
		new Image("file:D:/Java_game/Assets/Brick/3-4.png"),
		new Image("file:D:/Java_game/Assets/Brick/3-5.png")},
		{null, new Image("file:D:/Java_game/Assets/Brick/4-1.png")}
	};
		
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
		gc.drawImage(texture[this.type][this.hp], (x+RENDER_OFFSET) * scale, y * scale, w * scale, h * scale);
		return;
	}
}
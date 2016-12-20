import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {

	Color color; 
	
	Player(int x, int y, int type, Color color) {
		super(x, y, type);
		this.color = color;
		velX = (int)(Math.random()*5)-2;
		velY = 0;
	}

	@Override
	public void tick() {
		this.x += this.velX;
		this.y += this.velY;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(this.x, this.y, 25, 25);
	}
	
}
